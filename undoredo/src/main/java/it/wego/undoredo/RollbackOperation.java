package it.wego.undoredo;

import java.util.Date;

/**
 *
 * @author aleph
 */
public interface RollbackOperation<E extends Operation> {

    public E getOperation();

    public Date getBegin();

    public Date getEnd();
}
