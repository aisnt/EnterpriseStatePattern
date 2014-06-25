package io;

import command.Command;
import command.DTO;
import command.ResultWrapper;
import common.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import state.InvalidStateTransitionException;
import state.State;

import java.util.Date;

/**
 * Created by david.hislop@korwe.com on 2014/06/22.
 */
public class StateIOHandler implements Command {
    final Logger log = LoggerFactory.getLogger(this.getClass());
    public StateIOHandler(State state) {
        currentStateObject = state;
    }

    public void setState(State state) {
        currentStateObject = state;
    }

    /*We need to use the derived object*/
    private State currentStateObject;
    @Override
    public void onMessage(Message s)  {
        synchronized(currentStateObject) {
            State.StateDescriptor old = getCurrentState();
            log.trace("StateIOHandler.onMessage() Desired transition from " + old + " to " + s.getState() + ".");
            try {
                ResultWrapper<DTO> dto = currentStateObject.doTransition(s);
                log.info("StateIOHandler.onMessage() Successful transition from " + old + " to " + s.getState() + ".");
            } catch (InvalidStateTransitionException ex) {
                log.trace("StateIOHandler.onMessage() Exception=" + ex.getMessage());
                log.info("StateIOHandler.onMessage() Failed transition from " + currentStateObject.getState() + ".");
            }
        }
    }

    private Event event = new Event(null, null,new Date());
    @Override
    public Event getLastEvent() {
        return event;
    }

    public void changeCurrentState(State st) {
        log.info("StateIOHandler.changeCurrentState() before changeCurrentState from " + currentStateObject.getState() + " to " + st.getState() + ".");
        synchronized(currentStateObject) {
            currentStateObject = st;
            event = new Event(currentStateObject.getState(), st.getState(), new Date());
        }
    }

    public State.StateDescriptor getCurrentState() {
        synchronized(currentStateObject) {
            return currentStateObject.getState();
        }
    }
}

