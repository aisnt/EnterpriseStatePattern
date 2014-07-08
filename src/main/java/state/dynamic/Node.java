package state.dynamic;

import state.StateDescriptorX;

/**
 * Created by david.hislop@korwe.com on 2014/07/07.
 */
class Node {
    public Node(String from, String to, Boolean val) throws InvalidStateException {
        this.from = StateDescriptorX.INSTANCE.get(from);
        this.to = StateDescriptorX.INSTANCE.get(to);
        doTransition = val;
    }

    public Node(int col, int row) throws InvalidStateException {
        this.from = StateDescriptorX.INSTANCE.get(col);
        this.to = StateDescriptorX.INSTANCE.get(row);
        doTransition = true;
    }

    public Boolean doTransition = false;
    public StateDescriptorX.StateDescriptor from;
    public StateDescriptorX.StateDescriptor to;
}
