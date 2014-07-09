package state;

import exceptions.InvalidStateException;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by david.hislop@korwe.com on 2014/07/07.
 */
/*
public enum StateDescriptor {isInitial, State1, State2, State3, isFinal, Max;

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

public enum StateDescriptorFactory {
    INSTANCE;
    private List<StateDescriptor> stateDescriptors = new ArrayList<>();

    public int Max() {
        return stateDescriptors.size();
    }

    StateDescriptorFactory() {
        stateDescriptors.add(new StateDescriptor("Initial", 0));
        stateDescriptors.add(new StateDescriptor("State1", 1));
        stateDescriptors.add(new StateDescriptor("State2", 2));
        stateDescriptors.add(new StateDescriptor("State3", 3));
        stateDescriptors.add(new StateDescriptor("Final", 4));
    }

    public StateDescriptor get(int stateIndex) throws InvalidStateException {
        if ((stateIndex >= Max()) || (stateIndex < 0)) {
            throw new InvalidStateException("Invalid State stateIndex = " + stateIndex + ".");
        }
        ListIterator l = stateDescriptors.listIterator();
        while(l.hasNext()) {
            StateDescriptor ll = (StateDescriptor) l.next();
            if ( ll.ordinal == stateIndex ) {
                return ll;
            }
        }
        throw new InvalidStateException();
    }

    public StateDescriptor get(String stateName) throws InvalidStateException {
        ListIterator l = stateDescriptors.listIterator();
        while(l.hasNext()) {
            StateDescriptor ll = (StateDescriptor) l.next();
            if ( ll.name.contains(stateName) ) {
                return ll;
            }
        }
        throw new InvalidStateException();
    }

    public Boolean isInitial(StateDescriptor state) {
        return state.stateType == StateType.Initial;
    }

    public Boolean isFinal(StateDescriptor state) {
        return state.stateType == StateType.Final;
    }

    enum StateType {Initial,Final,Normal};
    public class StateDescriptor {
        public StateType stateType;
        public String name;
        public int ordinal;
        public StateDescriptor(String s, int i) {
            name = s;
            ordinal = i;
            if (s.contains("Final")) {
                stateType = StateType.Final;
            } else
            if (s.contains("Initial")) {
                stateType = StateType.Initial;
            }
            else {
                stateType = StateType.Normal;
            }
        }
    }
}

