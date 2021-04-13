package it.wego.undoredo;

import java.io.IOException;

/**
 *
 * @author aleph
 */
public interface Sequencer{
    public Long nextCounter(String instanceId) throws IOException;
}
