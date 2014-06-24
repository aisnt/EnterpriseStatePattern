package common;

import state.State;

/**
 * Created by david.hislop@korwe.com on 2014/06/23.
 */
public class Message {
    public Message (State.StateDescriptor destinationState, String payload) {
        this.destinationState = destinationState;
        this.payload = payload;
    }
    State.StateDescriptor destinationState;
    String payload = "";
    public State.StateDescriptor getState() {
        return destinationState;
    }
    public String getPayload() {
        return payload;
    }
}
