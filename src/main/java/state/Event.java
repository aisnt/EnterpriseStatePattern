package state;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import state.StateDescriptorFactory;

import java.util.Date;

/**
 * Created by david.hislop@korwe.com on 2014/06/25.
 *
 */

public class Event {
    final Logger log = LoggerFactory.getLogger(this.getClass());
    public Event( StateDescriptorFactory.StateDescriptor from, StateDescriptorFactory.StateDescriptor to, Date date ) {
        log.trace("Event.ctor() start From: " + from.name +" To: " + to.name + ".");
        this.from = from;
        this.to = to;
        this.date = date;
    }

    public Event( StateDescriptorFactory.StateDescriptor to, Date date ) {
        log.trace("Event.ctor() start From: null To: " + to.name + ".");
        this.from = null;
        this.to = to;
        this.date = date;
    }

    public StateDescriptorFactory.StateDescriptor from;
    public StateDescriptorFactory.StateDescriptor to;
    public Date date ;

    @Override
    public String toString() {
        String result = "From: " + from.name + " To: " + to.name + " Date: " + date.toString();
        return result;
    }
}
