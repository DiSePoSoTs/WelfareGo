package it.wego.undoredo;

import java.util.Date;

/**
 *
 * @author aleph
 */
public class RollbackOperationImpl implements RollbackOperation {

    private Operation operation;
    private Date begin, end;

    public RollbackOperationImpl(Operation operation) {
        this(operation,new Date(),new Date());
    }
    public RollbackOperationImpl() {
        this(null);
    }

    public RollbackOperationImpl(Operation operation, Date begin, Date end) {
        this.operation = operation;
        this.begin = begin;
        this.end = end;
    }

    public Date getBegin() {
        return begin;
    }

    public void setBegin(Date begin) {
        this.begin = begin;
    }

    public void setBegin() {
        this.begin = new Date();
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public void setEnd() {
        this.end = new Date();
    }

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }
}
