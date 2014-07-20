package common;

import state.StateDescriptor;

/**
 * Created by david.hislop@korwe.com on 2014/06/23.
 */
public class Message {
    public Message (StateDescriptor destinationState, String payload) {
        this.destinationState = destinationState;
        this.payload = payload;
    }
    StateDescriptor destinationState;
    String payload = "";
    public StateDescriptor getDestinationState() {
        return destinationState;
    }
    public String getPayload() {
        return payload;
    }
}
