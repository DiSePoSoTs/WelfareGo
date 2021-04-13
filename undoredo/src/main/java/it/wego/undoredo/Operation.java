package it.wego.undoredo;

import java.util.Collection;
import java.util.Date;

/**
 *
 * @author aleph
 */
public interface Operation<E extends RollbackEnvironment> {

    public Long getCounter();

    public Date getDate();

    public String getDescription();

    public String getSequenceKey();

//        public Collection<Operation> getParents();
//        public OperationKey getOperationKey();
    public void undo(E rollbackEnvironment) throws Exception;

    public void redo(E rollbackEnvironment) throws Exception;

    public void clean(E rollbackEnvironment) throws Exception;

    public OperationKey getKey();
}
