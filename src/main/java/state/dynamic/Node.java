package state.dynamic;

import state.StateDescriptor;

/**
 * Created by david.hislop@korwe.com on 2014/07/07.
 */
class Node {
    public Node(String from, String to, Boolean val) {
        this.from = StateDescriptor.getStateDescriptor(from);
        this.to = StateDescriptor.getStateDescriptor(to);
        doIt = val;
    }

    public Node(int col, int row) throws InvalidStateException {
        this.from = StateDescriptor.getStateDescriptor(col);
        this.to = StateDescriptor.getStateDescriptor(row);
        doIt = true;
    }

    public Boolean doIt = false;
    public StateDescriptor from;
    public StateDescriptor to;
}
