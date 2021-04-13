package it.wego.undoredo;

import com.google.common.collect.Lists;
import java.util.Date;
import java.util.List;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * NON Thread-safe
 * @author aleph
 */
public class UndoRedoManager<O extends Operation, E extends RollbackEnvironment> extends UndoRedoUtils {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public Logger getLogger() {
        return logger;
    }
    private OperationStore<O> operationStore;

    public OperationStore<O> getOperationStore() {
        return operationStore;
    }

    public void setOperationStore(OperationStore<O> operationStore) {
        this.operationStore = operationStore;
    }

    public UndoRedoManager(OperationStore<O> operationStore) {
        this.operationStore = operationStore;
    }
    private List<O> undoneOperations, redoneOperations;
    private boolean removeUndoneOperations = true;

    public List<O> getUndoneOperations() {
        return undoneOperations;
    }

    public boolean getRemoveUndoneOperations() {
        return removeUndoneOperations;
    }

    public void setRemoveUndoneOperations(boolean removeUndoneOperations) {
        this.removeUndoneOperations = removeUndoneOperations;
    }

    public void executeUndo(Graph<O, DefaultEdge> graph, O rollbackPoint, E rollbackEnvironment) throws Exception {
        List<O> undoSequence = getUndoSequence(graph, rollbackPoint);
        getLogger().debug("undoing {} operations", undoSequence.size());
        undoneOperations = Lists.newArrayListWithCapacity(undoSequence.size());

        for (O operation : undoSequence) {
            getLogger().debug("undoing {}", operation);
            RollbackOperationImpl undo = new RollbackOperationImpl(operation);
            operation.undo(rollbackEnvironment);
            undo.setEnd();
            rollbackEnvironment.appendRollbackOperation(undo);
            undoneOperations.add(operation);
            if (getRemoveUndoneOperations()) {
                getOperationStore().removeOperations(operation);
            }
        }
        getLogger().debug("undone {} operations", undoneOperations.size());
    }

    public void executeRedo(Graph<O, DefaultEdge> graph, E rollbackEnvironment) throws Exception {
        executeRedo(graph, null, rollbackEnvironment);
    }

    public void executeRedo(Graph<O, DefaultEdge> graph, O rollbackPoint, E rollbackEnvironment) throws Exception {
        List<O> redoSequence = rollbackPoint == null ? getRedoSequence(graph) : getRedoSequence(graph, rollbackPoint);
        getLogger().debug("redoing {} operations", redoSequence.size());
        redoneOperations = Lists.newArrayListWithCapacity(redoSequence.size());

        for (O operation : redoSequence) {
            rollbackEnvironment.setLastOperationInRedoSequence(graph.outDegreeOf(operation) == 0);
            getLogger().debug("redoing {}", operation);
            RollbackOperationImpl redo = new RollbackOperationImpl(operation);
            operation.redo(rollbackEnvironment);
            redo.setEnd();
            rollbackEnvironment.appendRollbackOperation(redo);
            redoneOperations.add(operation);
        }
        getLogger().debug("redone {} operations", redoneOperations.size());
    }
}
