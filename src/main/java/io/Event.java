package io;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import state.State;

import java.util.Date;

/**
 * Created by david.hislop@korwe.com on 2014/06/25.
 *
 */

public class Event {
    final Logger log = LoggerFactory.getLogger(this.getClass());
    public Event( State.StateDescriptor from, State.StateDescriptor to, Date date ) {
        log.trace("Event.ctor() start From: " + from +" To: " + to + ".");
        this.from = from;
        this.to = to;
        this.date = date;
    }
    public State.StateDescriptor from;
    public State.StateDescriptor to;
    public Date date ;

    @Override
    public String toString() {
        String result = "From: " + from + " To: " + to + " Date: " + date.toString();
        return result;
    }
}
