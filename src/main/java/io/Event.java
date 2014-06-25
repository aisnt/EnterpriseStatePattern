package io;

import state.State;

import java.util.Date;

/**
 * Created by davidhislop on 2014/06/25.
 */

public class Event {
    public Event( State.StateDescriptor from, State.StateDescriptor to, Date date ) {
        this.from = from;
        this.to = to;
        this.date = date;
    }
    public State.StateDescriptor from;
    public State.StateDescriptor to;
    public Date date ;

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("From: " + to );
        result.append(" To: " + from );
        result.append(" Date: " + date.toString());

        return result.toString();
    }

}
