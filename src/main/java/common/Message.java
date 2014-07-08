package common;

import state.StateDescriptorX;

/**
 * Created by david.hislop@korwe.com on 2014/06/23.
 */
public class Message {
    public Message (StateDescriptorX.StateDescriptor destinationState, String payload) {
        this.destinationState = destinationState;
        this.payload = payload;
    }
    StateDescriptorX.StateDescriptor destinationState;
    String payload = "";
    public StateDescriptorX.StateDescriptor getState() {
        return destinationState;
    }
    public String getPayload() {
        return payload;
    }
}
