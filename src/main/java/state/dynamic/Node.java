package state.dynamic;

import state.StateDescriptorFactory;

/**
 * Created by david.hislop@korwe.com on 2014/07/07.
 */
class Node {
    public Node(String from, String to, Boolean val) throws InvalidStateException {
        this.from = StateDescriptorFactory.INSTANCE.get(from);
        this.to = StateDescriptorFactory.INSTANCE.get(to);
        doTransition = val;
    }

    public Node(int col, int row) throws InvalidStateException {
        this.from = StateDescriptorFactory.INSTANCE.get(col);
        this.to = StateDescriptorFactory.INSTANCE.get(row);
        doTransition = true;
    }

    public Boolean doTransition = false;
    public StateDescriptorFactory.StateDescriptor from;
    public StateDescriptorFactory.StateDescriptor to;
}
