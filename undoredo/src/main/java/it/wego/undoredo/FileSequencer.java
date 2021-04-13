package it.wego.undoredo;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author aleph
 */
public class FileSequencer implements Sequencer {

    private final File file;
    public static final String DEFAULT_SEQUENCE_FILE = "sequence.data";

    public FileSequencer() throws IOException {
        this(DEFAULT_SEQUENCE_FILE);
    }

    public FileSequencer(String fileName) throws IOException {
        this.file = new File(fileName);
        if (!file.exists()) {
            resetCounter();
        }
    }

    public File getFile() {
        return file;
    }

    public Long retrieveLastCounter() throws IOException {
        Reader reader = new FileReader(getFile());
        String string = IOUtils.toString(reader);
        reader.close();
        return Long.parseLong(string);
    }

    public void resetCounter() throws IOException {
        storeCounter(0L);
    }

    protected void storeCounter(Long counter) throws IOException {
        Writer writer = new FileWriter(getFile());
        writer.write(counter.toString());
        writer.close();
    }

    public Long nextCounter(String instanceId) throws IOException {
        Long lastCounter = retrieveLastCounter();
        Long nextCounter = lastCounter + 1;
        storeCounter(nextCounter);
        return nextCounter;
    }
}
