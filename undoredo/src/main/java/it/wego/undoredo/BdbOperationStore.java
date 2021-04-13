package it.wego.undoredo;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.sleepycat.bind.EntityBinding;
import com.sleepycat.bind.EntryBinding;
import com.sleepycat.collections.StoredIterator;
import com.sleepycat.collections.StoredMap;
import com.sleepycat.collections.StoredValueSet;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.je.SecondaryConfig;
import com.sleepycat.je.SecondaryDatabase;
import com.sleepycat.je.SecondaryKeyCreator;
import it.wego.undoredo.AbstractOperationStore.OperationNode;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.Validate;
import org.slf4j.LoggerFactory;

/**
 *
 * @author aleph
 */
public final class BdbOperationStore<E extends Operation> extends AbstractOperationStore<E> implements OperationStore<E>, Sequencer {

    public final static String DEFAULT_DB_DIR = "logStore.bdb", DEFAULT_DB_NAME = "logStore", SEQUENCE_DB_SUFFIX = ".sequence";
    private final static Charset CHARSET = Charset.forName("UTF-8");
    private final File directory;
    private final String dbName;
    private final static Map<String, BdbOperationStore> sharedStoresMap = Maps.newHashMap();

    public static <E extends Operation> BdbOperationStore<E> getSharedStore(String directory) throws IOException {
        String key = directory + ":" + DEFAULT_DB_NAME;
        BdbOperationStore store = sharedStoresMap.get(key);
        if (store == null) {
            sharedStoresMap.put(key, store = new BdbOperationStore<E>(new File(directory), DEFAULT_DB_NAME));
        } else {
            LoggerFactory.getLogger(BdbOperationStore.class).debug("using cached operation store je (Bdb) on {}", new File(directory).getAbsolutePath());
        }
        return store;
    }

    public static <E extends Operation> BdbOperationStore<E> getSharedStore() throws IOException {
        return getSharedStore(DEFAULT_DB_DIR);
    }

//    public BdbOperationStore(File directory) throws IOException {
//        this(directory, DEFAULT_DB_NAME);
//    }
    public BdbOperationStore(File directory, String dbName) throws IOException {
        super(false);
        this.directory = directory;
        this.dbName = dbName;
        initDatabase();
    }
//    public BdbOperationStore(String fileName) throws IOException {
//        this(new File(fileName));
//    }
//
//    public BdbOperationStore() throws IOException {
//        this(DEFAULT_DB_DIR);
//    }
    private Database database = null;
    private SecondaryDatabase sequenceDatabase = null;
    private Environment environment = null;

    public Environment getEnvironment() {
        if (environment == null) {
            getLogger().info("initializing operation store je (Bdb) on {}", directory.getAbsolutePath());
            if (!directory.exists()) {
                directory.mkdirs();
            }
            Validate.isTrue(directory.isDirectory());
            EnvironmentConfig environmentConfig = new EnvironmentConfig();
            environmentConfig.setAllowCreate(true);
            environmentConfig.setTransactional(false);
            environment = new Environment(directory, environmentConfig);
        }
        return environment;
    }

    public void initDatabase() {
        DatabaseConfig databaseConfig = new DatabaseConfig();
        databaseConfig.setAllowCreate(true);
        database = getEnvironment().openDatabase(null, dbName, databaseConfig);

        SecondaryConfig config = new SecondaryConfig();
        config.setAllowCreate(true);
        config.setAllowPopulate(true);
        config.setImmutableSecondaryKey(true);
        config.setKeyCreator(sequenceKeyCreator);
        config.setSortedDuplicates(true);
        sequenceDatabase = getEnvironment().openSecondaryDatabase(null, dbName + SEQUENCE_DB_SUFFIX, database, config);

        sequenceQueryMap = new StoredMap<String, OperationNode>(sequenceDatabase, sequenceBinding, opNodeBinding, false);
        updatableStoreSet = new StoredValueSet(getDatabase(), entityBinding, true);
        readonlyStoreSet = new StoredValueSet(getDatabase(), entityBinding, false);
    }

    public Database getDatabase() {
        return database;
    }

    public Database getSequenceDatabase() {
        return sequenceDatabase;
    }

    @Override
    public void close() {
        sharedStoresMap.values().remove(this);
        if (sequenceDatabase != null) {
            sequenceDatabase.close();
            sequenceDatabase = null;
        }
        if (database != null) {
            database.close();
            database = null;
        }
        if (environment != null) {
            environment.close();
            environment = null;
        }
    }

    @Override
    public void reset() {
        updatableStoreSet.clear();
        environment.sync();
    }
    private StoredMap<String, OperationNode> sequenceQueryMap;
    private StoredValueSet updatableStoreSet, readonlyStoreSet;

    @Override
    public List<OperationNode> retrieveOperationNodesBySequenceKey(String key) throws IOException {
        getLogger().debug("executing hash query via sequence, hash space size : {}", sequenceQueryMap.size());
        return Lists.newArrayList(sequenceQueryMap.duplicates(key));
//        return newArrayList;
    }

//    public Transaction getTransaction() {
//        if (transaction == null) {
//            transaction = getEnvironment().beginTransaction(null, null);
//        }
//        return transaction;
//    }
    @Override
    public void storeOperationNodes(Iterable<OperationNode> nodes) throws IOException {
//        getTransaction();
//        try {
        getLogger().debug("storing operation[s] node[s]");
        invalidateCache();
        Iterables.addAll(updatableStoreSet, nodes);
        getEnvironment().sync();
//        close();
//            Collections..addAll(storedValueSet, nodes);
//            getTransaction().commitSync();
//        } catch (Exception ex) {
//            getTransaction().abort();
//            throw new IOException(ex);
//        }
    }

    @Override
    public void removeOperations(final Predicate<E> filter) throws Exception {
        //        getTransaction();
        //        try {
        //        getSequenceDatabase();
        getLogger().debug("removing operation[s] node[s] via slow iteration, db size : {}", updatableStoreSet.size());
        invalidateCache();
        StoredIterator iterator = updatableStoreSet.storedIterator();
        Iterators.removeIf(iterator, new Predicate<OperationNode>() {

            public boolean apply(OperationNode input) {
                return filter.apply((E) input.getOperation());
            }
        });
        iterator.close();
//            getTransaction().commitSync();
//        } catch (Exception ex) {
//            getTransaction().abort();
//            throw new IOException(ex);
//        }
        getEnvironment().sync();
//        close();
    }

    @Override
    public List<OperationNode> retrieveOperationNodes(Predicate<E> filter) throws IOException {
        getLogger().debug("executing long query (iteration), db size : {}", readonlyStoreSet.size());
        StoredIterator iterator = readonlyStoreSet.storedIterator();
        List<OperationNode> res = Lists.newArrayList(filter != null ? Iterators.filter(iterator, new NodeFilter(filter)) : iterator);
        iterator.close();
        return res;
    }
    private static final SecondaryKeyCreator sequenceKeyCreator = new SecondaryKeyCreator() {

        public boolean createSecondaryKey(SecondaryDatabase secondary, DatabaseEntry key, DatabaseEntry data, DatabaseEntry result) {
            OperationKey operationKey = OperationKey.fromString(new String(key.getData(), CHARSET));
            result.setData(operationKey.getSequenceKey().getBytes(CHARSET));
            return true;
        }
    };
    private final EntityBinding<OperationNode> entityBinding = new EntityBinding<OperationNode>() {

        public OperationNode entryToObject(DatabaseEntry key, DatabaseEntry data) {
            return opNodeBinding.entryToObject(data);
        }

        public void objectToKey(OperationNode object, DatabaseEntry key) {
            key.setData(object.getOperation().getKey().toString().getBytes(CHARSET));
        }

        public void objectToData(OperationNode object, DatabaseEntry data) {
            opNodeBinding.objectToEntry(object, data);
        }
    };
    private final EntryBinding<OperationNode> opNodeBinding = new EntryBinding<OperationNode>() {

        public OperationNode entryToObject(DatabaseEntry entry) {
            try {
                return (OperationNode) deserialize(entry.getData());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }

        public void objectToEntry(OperationNode object, DatabaseEntry entry) {
            try {
                entry.setData(serialize(object));
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    };
    private static final EntryBinding<String> sequenceBinding = new EntryBinding<String>() {

        public String entryToObject(DatabaseEntry entry) {
            return new String(entry.getData(), CHARSET);
        }

        public void objectToEntry(String object, DatabaseEntry entry) {
            entry.setData(object.getBytes(CHARSET));

        }
    };

    public Long nextCounter(String instanceId) throws IOException {

        Collection<OperationNode> duplicates = sequenceQueryMap.duplicates(instanceId);
        return duplicates.isEmpty() ? 1L : (Collections.max(duplicates, new Comparator<OperationNode>() {

            public int compare(OperationNode o1, OperationNode o2) {
                return o1.getOperation().getCounter().compareTo(o2.getOperation().getCounter());
            }
        }).getOperation().getCounter() + 1);
    }
}
