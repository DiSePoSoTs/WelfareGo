package it.wego.undoredo;

import com.google.common.base.Objects;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author davide
 */
public class SimpleOperationImpl<E extends RollbackEnvironment> implements Operation<E>, Serializable {

    private static final long serialVersionUID = 1L;
    private static transient long sharedCounter = System.currentTimeMillis();

    public static synchronized Long nextCounter() {
        return sharedCounter++;
    }
    private final Long counter;
    private final Date date;
    private final String description, sequencekey;
//    private transient Collection<SimpleOperationImpl<E>> parents;

    protected SimpleOperationImpl() {
        counter = null;
        date = null;
        description = null;
        sequencekey = null;
    }

    public SimpleOperationImpl(String description, String sequencekey) {
        this(description, sequencekey, nextCounter());
    }

    public SimpleOperationImpl(String description, String sequencekey, Long counter) {
        this(description, sequencekey, counter, new Date());
    }

    public SimpleOperationImpl(String description, String sequencekey, Sequencer sequencer) throws IOException {
        this(description, sequencekey, sequencer.nextCounter(sequencekey));
    }

    public SimpleOperationImpl(String description, String sequencekey, Long counter, Date date) {
        this.description = description;
        this.sequencekey = sequencekey;
        this.counter = counter;
        this.date = date;
    }

    public Long getCounter() {
        return counter;
    }

    public Date getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public String getSequenceKey() {
        return sequencekey;
    }

    public void undo(E rollbackEnvironment) throws Exception {
    }

    public void redo(E rollbackEnvironment) throws Exception {
    }

//    public Collection<Operation> getParents() {
//        return Collections2.transform(parents, new Function<SimpleOperationImpl<E>, Operation>() {
//
//            public Operation apply(SimpleOperationImpl<E> input) {
//                return input;
//            }
//        });
//    }
    @Override
    public boolean equals(Object obj) {
        return obj != null 
                && obj instanceof Operation 
                && Objects.equal(counter, ((Operation) obj).getCounter()) 
                && Objects.equal(sequencekey, ((Operation) obj).getSequenceKey());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(counter,sequencekey);
    }

    @Override
    public String toString() {
        return "Operation " + this.getCounter() + "/" + this.getSequenceKey();
    }

    public void clean(E rollbackEnvironment) throws Exception {
    }
    private transient OperationKey key = null;

    public OperationKey getKey() {
        if (key == null) {
            key = new OperationKey(getSequenceKey(), getCounter());
        }
        return key;
    }
}
