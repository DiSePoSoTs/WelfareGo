package it.wego.undoredo.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import it.wego.undoredo.BdbOperationStore;
import it.wego.undoredo.Operation;
import it.wego.undoredo.SimpleOperationImpl;
import java.io.File;
import org.apache.commons.io.FileUtils;
import org.slf4j.LoggerFactory;
import static junit.framework.Assert.*;

/**
 *
 * @author aleph
 */
public class BdbTest {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private File dir;
    private BdbOperationStore store;
    Operation operation;

    @Before
    public void setUp() throws Exception {
        logger.info("begin bdb testing");
        dir = new File("/tmp/bdbTestFile_" + System.currentTimeMillis());
        store = BdbOperationStore.getSharedStore(dir.getAbsolutePath());
        operation = new SimpleOperationImpl("sample op", "sampleSeq", 1L);
    }

    @After
    public void tearDown() throws Exception {
        store.close();
        FileUtils.deleteDirectory(dir);
        logger.info("bdb testing complete");
    }

    @Test
    public void testStore() throws Exception {
        store.reset();
        assertTrue(store.retrieveOperations().isEmpty());
        
        store.appendOperation(operation);
        assertTrue(store.retrieveOperations().size() == 1);


        Operation retrieved = store.retrieveOperation(operation.getSequenceKey(), operation.getCounter());
        assertNotNull(retrieved);
        assertEquals(operation, retrieved);

        store.close();
        store = BdbOperationStore.getSharedStore(dir.getAbsolutePath());
        retrieved = store.retrieveOperation(operation.getSequenceKey(), operation.getCounter());
        assertNotNull(retrieved);
        assertEquals(operation, retrieved);

        store.removeOperations(operation);
        assertTrue(store.retrieveOperations().isEmpty());

        store.close();
        store = BdbOperationStore.getSharedStore(dir.getAbsolutePath());
        assertTrue(store.retrieveOperations().isEmpty());       
    }
    
    @Test
    public void testSequencer() throws Exception {
        store.reset();
        assertTrue(store.retrieveOperations().isEmpty());
        
        Long counter = store.nextCounter(operation.getSequenceKey());
        assertNotNull(counter);
        
        store.appendOperation(operation);
        counter = store.nextCounter(operation.getSequenceKey());
        assertNotNull(counter);
        assertTrue(counter>operation.getCounter());
    }
}
