package it.wego.undoredo;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Lists;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import org.apache.commons.lang3.Validate;

/**
 *
 * @author davide
 */
public class FileOperationStore<E extends Operation> extends AbstractOperationStore<E> implements OperationStore<E> {

    private final File storeFile;
    public static final String DEFAULT_STORE_FILE = "logStore.data";

    public FileOperationStore() throws IOException {
        this(DEFAULT_STORE_FILE);
    }

    public FileOperationStore(String fileName) throws IOException {
        super(true);
        this.storeFile = new File(fileName);
        if (!storeFile.exists()) {
            reset();
        }
        Validate.isTrue(storeFile.canWrite(), "cannot write operations log to file " + storeFile);
    }

    @Override
    public void reset() throws IOException {
        invalidateCache();
        new FileOutputStream(getStoreFile()).close();
    }

    public File getStoreFile() {
        getLogger().debug("using file " + storeFile.getAbsolutePath() + " (" + storeFile.length() + "byte)");
        return storeFile;
    }

    public void storeOperationNodes(Iterable<OperationNode> nodes) throws IOException {
        OutputStreamWriter out = new FileWriter(getStoreFile(), true);
        for (OperationNode node : nodes) {
            invalidateCache((E) node.getOperation());
            byte[] data = serialize(node);
            out.write(new String(data));
            out.write("\r\n");
        }
        out.flush();
        out.close();
    }

    public List<OperationNode> retrieveOperationNodes(Predicate<E> filter) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(getStoreFile()));
        SortedSet<OperationNode> res = new TreeSet<OperationNode>(new Comparator<OperationNode>() {

            public int compare(OperationNode o1, OperationNode o2) {
                return o1.getOperation().getCounter().compareTo(o2.getOperation().getCounter());
            }
        });
        String line;
        while ((line = reader.readLine()) != null) {
            OperationNode opNode = (OperationNode) deserialize(line);
            E operationLog = (E) opNode.getOperation();
            if (filter == null || filter.apply(operationLog)) {
                res.add(opNode);
            }
        }
        reader.close();
        getLogger().debug("retrieved " + res.size() + " operations");
        return new ArrayList<OperationNode>(res);
    }

    public void removeOperations(Predicate<E> filter) throws Exception {
        getLogger().debug("removing operation[s] ");
        invalidateCache();
        List<OperationNode> nodes = retrieveOperationNodes(Predicates.not(filter));
        reset();
        storeOperationNodes(nodes);
    }    
}
