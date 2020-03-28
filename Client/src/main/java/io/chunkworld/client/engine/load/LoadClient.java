package io.chunkworld.client.engine.load;

import io.chunkworld.api.core.modes.SingleStepLoadProcess;

public class LoadClient extends SingleStepLoadProcess {

    public LoadClient() {
        super("Registering client...", 1);
    }

    /**
     * Registers the client
     *
     * @return returns true
     */
    public boolean step() {
        return true;
    }
}
