package it.wego.undoredo.test;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import it.wego.undoredo.Operation;
import it.wego.undoredo.SimpleOperationImpl;
import static junit.framework.Assert.*;

/**
 *
 * @author aleph
 */
public class OperationTest{

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Test
    public void testOperations() {
        logger.info("begin testing operations");
        String seq1 = "sequence1",
                seq2 = "sequence2",
                desc = "desc";
        Long cnt1 = 1L,
                cnt2 = 2L;
        Operation op1 = new SimpleOperationImpl(desc, seq1, cnt1),
                op2 = new SimpleOperationImpl(desc, seq1, cnt1),
                op3 = new SimpleOperationImpl(desc, seq1, cnt2),
                op4 = new SimpleOperationImpl(desc, seq2, cnt1);
        
        assertTrue(op1.equals(op2));
        assertTrue(op1.equals(op1));

        assertFalse(op1.equals(op3));
        assertFalse(op1.equals(op4));
        
        logger.info("complete testing operations");
    }
}
