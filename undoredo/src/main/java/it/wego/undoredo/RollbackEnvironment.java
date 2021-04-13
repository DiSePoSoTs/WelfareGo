package it.wego.undoredo;

import java.util.Date;
import java.util.List;
import org.slf4j.Logger;

/**
 *
 * @author aleph
 */
public interface RollbackEnvironment<E extends Operation> {

    public boolean isLastOperationInRedoSequence();

    public void setLastOperationInRedoSequence(boolean lastOperationInRedoSequence);

    public RollbackOperation<E> getLastOperation();

    public RollbackOperation<E> getFirstOperation();

    public Logger getLogger();

    public List<RollbackOperation<E>> getRollbackOperations();

    public void appendRollbackOperation(RollbackOperation<E> rollbackOperation);
}
