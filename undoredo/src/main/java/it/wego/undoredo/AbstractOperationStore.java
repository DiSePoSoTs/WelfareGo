package it.wego.undoredo;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.codec.binary.Base64OutputStream;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author aleph
 */
public abstract class AbstractOperationStore<E extends Operation> implements OperationStore<E> {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private final boolean useBase64Encoding;
    private final Cache<String, Graph<E, DefaultEdge>> graphCache;

    public AbstractOperationStore(boolean useBase64Encoding) {
        this.useBase64Encoding = useBase64Encoding;
        graphCache = CacheBuilder.newBuilder().expireAfterAccess(1, TimeUnit.MINUTES).build(new CacheLoader<String, Graph<E, DefaultEdge>>() {

            @Override
            public Graph<E, DefaultEdge> load(String key) throws Exception {
                return directRetrieveOperationGraph(key);
            }
        });
    }

    public Logger getLogger() {
        return logger;
    }

    public void invalidateCache(E... operations) {
        for (E operation : operations) {
            graphCache.invalidate(operation.getSequenceKey());
        }
    }

    public void invalidateCache() {
        graphCache.invalidateAll();
    }

    public List<E> retrieveOperations(String sequenceKey, Long counter) throws Exception {
        return retrieveOperations(retrieveOperation(sequenceKey, counter));
    }

    public List<E> retrieveOperations(E operation) throws Exception {
        return Lists.newArrayList(
                new ConnectivityInspector<E, DefaultEdge>(
                        retrieveOperationGraph(operation.getSequenceKey())
                        ).connectedSetOf(operation));
    }

    public abstract void storeOperationNodes(Iterable<OperationNode> nodes) throws IOException;

    public abstract void removeOperations(Predicate<E> filter) throws Exception;

    public abstract List<OperationNode> retrieveOperationNodes(Predicate<E> filter) throws IOException;

    public byte[] serialize(Serializable operation) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream out;
        if (useBase64encoding()) {
            out = new ObjectOutputStream(new Base64OutputStream(byteArrayOutputStream, true, -1, null));
        } else {
            out = new ObjectOutputStream(byteArrayOutputStream);
        }
        out.writeObject(operation);
        out.close();
        return byteArrayOutputStream.toByteArray();
    }

    public Object deserialize(String data) throws IOException {
        return deserialize(data.getBytes());
    }

    public Object deserialize(byte[] data) throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
        ObjectInputStream in;
        if (useBase64encoding()) {
            in = new ObjectInputStream(new Base64InputStream(byteArrayInputStream));
        } else {
            in = new ObjectInputStream(byteArrayInputStream);
        }
        Object res;
        try {
            res = (Object) in.readObject();
        } catch (ClassNotFoundException ex) {
            throw new IOException(ex);
        }
        in.close();
        return res;
    }

    public List<E> retrieveOperationsByPredicate(Predicate<E> filter) throws IOException {
        return Lists.transform(retrieveOperationNodes(filter), new Function<OperationNode, E>() {

            public E apply(OperationNode input) {
                return (E) input.getOperation();
            }
        });
    }

    public E retrieveOperation(final String sequenceKey, final Long counter) throws Exception {
        List<E> list = retrieveOperationsByPredicate(new Predicate<E>() {

            public boolean apply(E input) {
                return input.getSequenceKey().equals(sequenceKey) && input.getCounter().equals(counter);
            }
        });
        return list.isEmpty() ? null : list.get(0);
    }

    public void storeOperation(E operation, E... parents) throws IOException {
        getLogger().debug("storing operation {}", operation);
        storeOperationNodes(Collections.singleton(new OperationNode(operation, parents)));
    }

    public List<E> retrieveOperations() throws IOException {
        return retrieveOperationsByPredicate(null);
    }

    public Collection<E> retrieveParents(E operation) throws Exception {
        return UndoRedoUtils.getParents(retrieveOperationGraph(operation.getSequenceKey()), operation);
    }

    public void removeOperations(E... operations) throws Exception {
        removeOperations(ImmutableSet.copyOf(operations));
    }

    public void removeOperations(final Collection<E> excludedOperations) throws Exception {
        removeOperations(new Predicate<E>() {

            public boolean apply(E input) {
                return excludedOperations.contains(input);
            }
        });
    }

    public void removeOperationsBySequenceKey(final String sequenceKey) throws Exception {
        removeOperations(new Predicate<E>() {

            public boolean apply(E input) {
                return input.getSequenceKey().equals(sequenceKey);
            }
        });
    }

    public void removeOperation(final String sequenceKey, final Long counter) throws Exception {
        removeOperations(new Predicate<E>() {

            public boolean apply(E input) {
                return input.getCounter().equals(counter) && input.getSequenceKey().equals(sequenceKey);
            }
        });
    }

    public boolean useBase64encoding() {
        return useBase64Encoding;
    }

    public final class MatchSequenceKeyPredicate implements Predicate<E> {

        private final Object key;

        public MatchSequenceKeyPredicate(Object key) {
            this.key = key;
        }

        public boolean apply(E input) {
            return input.getSequenceKey().equals(key);
        }
    }

    public List<E> retrieveOperationsBySequenceKey(String key) throws IOException {
        return Lists.transform(retrieveOperationNodesBySequenceKey(key), new Function<OperationNode, E>() {

            public E apply(OperationNode input) {
                return (E) input.getOperation();
            }
        });
    }

    public List<OperationNode> retrieveOperationNodesBySequenceKey(String key) throws IOException {
        return retrieveOperationNodes(new MatchSequenceKeyPredicate(key));
    }

    public Graph<E, DefaultEdge> retrieveOperationGraph(String sequenceKey) throws Exception {
        return graphCache.getIfPresent(sequenceKey);
    }

    public Graph<E, DefaultEdge> directRetrieveOperationGraph(String sequenceKey) throws Exception {
        List<OperationNode> operations = retrieveOperationNodesBySequenceKey(sequenceKey);
        return buildOperationGraph(operations);
    }

    public Graph<E, DefaultEdge> buildOperationGraph(Collection<OperationNode> nodes) throws Exception {
        Map<Long, Operation> operations = new HashMap<Long, Operation>();
        Graph<E, DefaultEdge> graph = new SimpleDirectedGraph<E, DefaultEdge>(DefaultEdge.class);
        for (OperationNode node : nodes) {
            operations.put(node.getOperation().getCounter(), node.getOperation());
            graph.addVertex((E) node.getOperation());
        }
        for (OperationNode node : nodes) {
            for (Long parentKey : node.getParentsKeys()) {
                graph.addEdge((E) operations.get(parentKey), (E) node.getOperation());
            }
        }
        return graph;
    }

    public static class OperationNode implements Serializable {

        private final Operation operation;
        private final Collection<Long> parentsKeys;

        public OperationNode(Operation operation, Operation... parents) {
            this.operation = operation;
            Collection collection = new ArrayList();
            for (Operation parent : parents) {
                collection.add(parent.getCounter());
            }
            parentsKeys = Collections.unmodifiableCollection(collection);
        }

        public Operation getOperation() {
            return operation;
        }

        public Collection<Long> getParentsKeys() {
            return parentsKeys;
        }
    }

    public E retrieveLastOperation(String sequenceKey) throws IOException {
        List<E> list = Lists.newArrayList(retrieveOperationsBySequenceKey(sequenceKey));
        if (list.isEmpty()) {
            return null;
        }
        Collections.sort(list, new Comparator<E>() {

            public int compare(E o1, E o2) {
                return -o1.getDate().compareTo(o2.getDate());
            }
        });
        return list.iterator().next();
    }

    public void appendOperation(E operation) throws Exception {
        E lastOperation = retrieveLastOperation(operation.getSequenceKey());
        if (lastOperation != null) {
            storeOperation(operation, lastOperation);
        } else {
            storeOperation(operation);
        }
    }

    public void copyTo(AbstractOperationStore otherStore) throws IOException {
        otherStore.storeOperationNodes(retrieveOperationNodes(null));
    }

    public void close() throws Exception {
    }

    public void reset() throws Exception {
    }

    public static class NodeFilter implements Predicate<OperationNode> {

        private final Predicate operationFilter;

        public NodeFilter(Predicate<? extends Operation> operationFilter) {
            this.operationFilter = operationFilter;
        }

        public boolean apply(OperationNode input) {
            return operationFilter.apply(input.getOperation());
        }
    }
}
