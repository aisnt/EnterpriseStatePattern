package state;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
* Created by david.hislop@korwe.com on 2014/07/18.
*/
public class StateDescriptor {
    private Logger log = LoggerFactory.getLogger(this.getClass());
    public StateDescriptorFactory.StateType stateType;
    public String name;
    public int ordinal;
    protected StateDescriptor( String s, int i) {
        log.trace("StateDescriptorFactory.StateDescriptor.ctor() ...");
        name = s;
        ordinal = i;
        if (s.contains("Final")) {
            stateType = StateDescriptorFactory.StateType.Final;
        } else
        if (s.contains("Initial")) {
            stateType = StateDescriptorFactory.StateType.Initial;
        }
        else {
            stateType = StateDescriptorFactory.StateType.Normal;
        }
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof StateDescriptor)) {
            return false;
        }
        StateDescriptor that = (StateDescriptor)other;
        return (this.name.equals(that.name) && (this.ordinal == that.ordinal) && (this.stateType  ==  that.stateType)  );
    }

    @Override
    public int hashCode() {
        return ordinal+Objects.hashCode(this.name)+Objects.hashCode(this.stateType)+13;
    }
}
