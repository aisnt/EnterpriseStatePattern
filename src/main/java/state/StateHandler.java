package state;

import command.DTO;
import command.ResultWrapper;
import common.Message;
import exceptions.InvalidStateTransitionException;
import exceptions.SendingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import exceptions.InvalidStateException;

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
        log.trace("StateHandler.ctor()");
        try {
            //TODO Assume we have one initial state
            currentStateObject = State.create(StateDescriptorFactory.INSTANCE.get("Initial"));
        } catch(Exception e) {
            log.error("StateHandler.ctor() Exception=",e);
        }
        currentEvent = new Event( currentStateObject.getState(), new Date());
    }

    public Boolean setInitialState(String stateName) {
        log.trace("StateHandler.setInitialState() to " + stateName + ".");
        State state = null;
        try {
            state = State.create(StateDescriptorFactory.INSTANCE.get(stateName));
        } catch (InvalidStateException e) {
            log.error("StateHandler.setInitialState() Exception=",e);
            return false;
        }

        return setState( state );
    }

    protected Boolean setState(State state) {
        log.trace("StateHandler.setState() to " + state.getState().name + ".");
        if (state == null) {
            return false;
        }
        synchronized(stateLock) {
            StateDescriptorFactory.StateDescriptor oldState = currentStateObject.getState();
            log.debug("StateHandler.setState() before changeCurrentState from " + oldState.name + " to " + state.getState().name + ".");
            currentStateObject = state;
            currentEvent = new Event(oldState, state.getState(), new Date());
        }
        return true;
    }

    public Event getLastEvent() {
        return currentEvent;
    }

    protected void changeCurrentState(State state) {
        synchronized(stateLock) {
            StateDescriptorFactory.StateDescriptor oldState = currentStateObject.getState();
            log.debug("StateHandler.changeCurrentState() before changeCurrentState from " + oldState + " to " + state.getState() + ".");
            setState(state);
        }
    }

    public StateDescriptorFactory.StateDescriptor getCurrentState() {
        synchronized(stateLock) {
            return currentStateObject.getState();
        }
    }

    protected ResultWrapper<DTO> doTransition(Message s) throws InvalidStateTransitionException, SendingException {
        synchronized(stateLock) {
            return currentStateObject.doTransition(s);
        }
    }
}