package it.wego.undoredo;

import com.google.common.base.Predicate;
import java.util.Collection;
import java.util.List;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

/**
 *
 * @author aleph
 */
public interface OperationStore<O extends Operation> {

    /**
     * salva un record di log in un qualche tipo di storage persistente
     * @param operationLog 
     */
    public void storeOperation(O operation, O... operationParents) throws Exception;

    /**
     * salva un record di log, come conseguenza dell'ultimo record inserito, se presente
     * @param operationLog 
     */
    public void appendOperation(O operation) throws Exception;

    public List<O> retrieveOperations() throws Exception;

    public O retrieveOperation(String sequenceKey, Long counter) throws Exception;

    public List<O> retrieveOperationsBySequenceKey(String sequenceKey) throws Exception;

    public List<O> retrieveOperations(String sequenceKey, Long counter) throws Exception;

    public List<O> retrieveOperations(O operation) throws Exception;

    public Graph<O, DefaultEdge> retrieveOperationGraph(String sequenceKey) throws Exception;

    public Collection<O> retrieveParents(O operation) throws Exception;

    public void removeOperations(O... operations) throws Exception;

    public void removeOperations(Collection<O> operations) throws Exception;

    public void removeOperationsBySequenceKey(String sequenceKey) throws Exception;

    public void removeOperation(String sequenceKey, Long counter) throws Exception;

    /**
     * chiude lo store, rilascia le risorse associate
     */
    public void close() throws Exception;

    /**
     * resetta lo store, eliminando tutti i contenuti
     */
    public void reset() throws Exception;
}
