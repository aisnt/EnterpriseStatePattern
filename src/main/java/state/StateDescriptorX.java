package state;

import state.dynamic.InvalidStateException;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by david.hislop@korwe.com on 2014/07/07.
 */
/*
public enum StateDescriptor {Initial, State1, State2, State3, Final, Max;

    public static state.StateDescriptor getStateDescriptor(int stateIndex) throws InvalidStateException {
        if ( (stateIndex >= StateDescriptor.Max.ordinal()) || (stateIndex < 0)) {
            throw new InvalidStateException("Invalid State row");
        }
        return StateDescriptor.values()[stateIndex]; //Expensive
    }

    public static state.StateDescriptor getStateDescriptor(String state) {
        return StateDescriptor.valueOf(state);
    }
}

*/

public enum StateDescriptorX {INSTANCE;
    private List<StateDescriptor> ccc = new ArrayList<>();

    public int Max() {
        return ccc.size();
    }

    StateDescriptorX() {
        ccc.add(new StateDescriptor("Initial", 0));
        ccc.add(new StateDescriptor("State1", 1));
        ccc.add(new StateDescriptor("State2", 2));
        ccc.add(new StateDescriptor("State3", 3));
        ccc.add(new StateDescriptor("Final", 4));
    }

    public StateDescriptor get(int stateIndex) throws InvalidStateException {
        if ((stateIndex >= Max()) || (stateIndex < 0)) {
            throw new InvalidStateException("Invalid State stateIndex = " + stateIndex + ".");
        }
        ListIterator l = ccc.listIterator();
        while(l.hasNext()) {
            StateDescriptor ll = (StateDescriptor) l.next();
            if ( ll.ordinal == stateIndex ) {
                return ll;
            }
        }
        throw new InvalidStateException();
    }

    public StateDescriptor get(String state) throws InvalidStateException {
        ListIterator l = ccc.listIterator();
        while(l.hasNext()) {
            StateDescriptor ll = (StateDescriptor) l.next();
            if ( ll.name.contains(state) ) {
                return ll;
            }
        }
        throw new InvalidStateException();
    }

    public Boolean Initial(StateDescriptor s) {
        return s.name.contains(ccc.get(0).name);
    }

    public Boolean Final(StateDescriptor s) {
        return s.name.contains(ccc.get(4).name);//TODO
    }

    public class StateDescriptor {
        public String name;
        public int ordinal;
        public StateDescriptor(String s, int i) {
            name = s;
            ordinal = i;
        }
    }
}

