package io.chunkworld.client.pong.events;

/**
 * Called when someone scored
 */
public class ScoreEvent {
    public static final int PLAYER = 0, OPPONENT = 1;
    public final int whoScored;

    public ScoreEvent(int whoScored) {
        this.whoScored = whoScored;
    }
}
