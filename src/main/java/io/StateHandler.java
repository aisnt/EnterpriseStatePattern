package io;

import command.DTO;
import command.ResultWrapper;
import common.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import state.InvalidStateTransitionException;
import state.SendingException;
import state.State;
import state.StateInitial;

import java.util.Date;

/**
 * Created by david.hislop@korwe.com on 2014/06/26.
 */
public enum StateHandler {
    INSTANCE;

    Logger log = LoggerFactory.getLogger(this.getClass());

    /*We need to use the derived object*/
    private State currentStateObject;
    private Event currentEvent;
    final private Boolean stateLock = false;
    StateHandler() {
        currentStateObject = new StateInitial();
        currentEvent = new Event(null, currentStateObject.getState(), new Date());
    }

    public void setState(State state) {
        synchronized(stateLock) {
            State.StateDescriptor oldState = currentStateObject.getState();
            log.debug("StateHandler.setState() before changeCurrentState from " + oldState + " to " + state.getState() + ".");
            currentStateObject = state;
            currentEvent = new Event(oldState, state.getState(), new Date());
        }
    }

    public Event getLastEvent() {
        return currentEvent;
    }

    public void changeCurrentState(State state) {
        synchronized(stateLock) {
            State.StateDescriptor oldState = currentStateObject.getState();
            log.debug("StateHandler.changeCurrentState() before changeCurrentState from " + oldState + " to " + state.getState() + ".");
            setState(state);
        }
    }

    public State.StateDescriptor getCurrentState() {
        synchronized(stateLock) {
            return currentStateObject.getState();
        }
    }

    public ResultWrapper<DTO> doTransition(Message s) throws InvalidStateTransitionException, SendingException {
        synchronized(stateLock) {
            return currentStateObject.doTransition(s);
        }
    }
}
