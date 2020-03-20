package io.chunkworld.client.engine.load;

import io.chunkworld.api.core.modes.SingleStepLoadProcess;

public class RegisterConfig extends SingleStepLoadProcess {
    public RegisterConfig() {
        super("Registering config...", 1);
    }

    /**
     * Registers the config and the config manager
     *
     * @return returns true
     */
    public boolean step() {
        return true;
    }
}
