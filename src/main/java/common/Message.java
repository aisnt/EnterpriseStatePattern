package common;

import state.StateDescriptorFactory;

/**
 * Created by david.hislop@korwe.com on 2014/06/23.
 */
public class Message {
    public Message (StateDescriptorFactory.StateDescriptor destinationState, String payload) {
        this.destinationState = destinationState;
        this.payload = payload;
    }
    StateDescriptorFactory.StateDescriptor destinationState;
    String payload = "";
    public StateDescriptorFactory.StateDescriptor getState() {
        return destinationState;
    }
    public String getPayload() {
        return payload;
    }
}
