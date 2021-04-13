package it.wego.undoredo;

import java.io.Serializable;

/**
 *
 * @author aleph
 */
public class OperationKey implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String sequencekey;
    private Long counter;
    
    protected OperationKey() {
    }
    
    public OperationKey(String sequencekey, Long counter) {
        this.sequencekey = sequencekey;
        this.counter = counter;
    }
    
    public Long getCounter() {
        return counter;
    }
    
    public String getSequenceKey() {
        return sequencekey;
    }
    
    @Override
    public String toString() {
        return sequencekey + ":" + counter;
    }
    
    public static OperationKey fromString(String str) {
        String cnt = str.replaceFirst(".*:([0-9]+)", "$1"), seq = str.replaceFirst("(.*):[0-9]+", "$1");
        return new OperationKey(seq, Long.valueOf(cnt));
    }
}
