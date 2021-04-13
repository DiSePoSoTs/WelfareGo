package it.wego.undoredo;

import com.google.common.collect.Lists;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author aleph
 */
public class RollbackEnvironmentImpl<E extends Operation> implements RollbackEnvironment<E> {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private boolean lastOperationInRedoSequence = false;
    private List<RollbackOperation<E>> rollbackOperations = Lists.newArrayList();

    public boolean isLastOperationInRedoSequence() {
        return lastOperationInRedoSequence;
    }

    public void setLastOperationInRedoSequence(boolean lastOperationInRedoSequence) {
        this.lastOperationInRedoSequence = lastOperationInRedoSequence;
    }

    public RollbackOperation<E> getFirstOperation() {
       return rollbackOperations.isEmpty()?null:rollbackOperations.get(0);
    }

    public RollbackOperation<E> getLastOperation() {
         return rollbackOperations.isEmpty()?null:rollbackOperations.get(rollbackOperations.size()-1);
    }

    public Logger getLogger() {
        return logger;
    }

    public List<RollbackOperation<E>> getRollbackOperations() {
        return rollbackOperations;
    }

    public void appendRollbackOperation(RollbackOperation<E> rollbackOperation) {
        rollbackOperations.add(rollbackOperation);
    }
}
