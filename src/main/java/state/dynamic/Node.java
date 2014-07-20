package state.dynamic;

import exceptions.InvalidStateException;
import state.StateDescriptor;
import state.StateDescriptorFactory;

import java.util.Objects;

/**
 * Created by david.hislop@korwe.com on 2014/07/07.
 */
public class Node {
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
    public StateDescriptor from;
    public StateDescriptor to;

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Node)) {
            return false;
        }
        Node that = (Node)other;
        return (this.from.equals(that.from) && this.to.equals(that.to) && (this.doTransition == that.doTransition) );
    }

    @Override
    public int hashCode() {
        return (doTransition ? 0 : 1) + Objects.hashCode(this.from) + Objects.hashCode(this.to) + 13;
    }
}
