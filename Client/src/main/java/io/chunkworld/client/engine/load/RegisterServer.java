package io.chunkworld.client.engine.load;

import io.chunkworld.api.core.modes.SingleStepLoadProcess;

public class RegisterServer extends SingleStepLoadProcess {

    public RegisterServer() {
        super("Registering client...", 1);
    }

    /**
     * Registers the client
     *
     * @return
     */
    public boolean step() {
        return true;
    }
}
