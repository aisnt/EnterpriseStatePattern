package io;

import command.Command;
import command.DTO;
import command.ResultWrapper;
import common.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import state.InvalidStateTransitionException;
import state.State;

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
            log.trace("Base.onMessage Desired transition from " + old + " to " + s.getState() + ".");
            try {
                ResultWrapper<DTO> dto = currentStateObject.doIt(s);
                log.info("Successful transition from " + old + " to " + s.getState() + ".");
            } catch (InvalidStateTransitionException ex) {
                log.trace(ex.getMessage());
                log.info("Failed transition from " + currentStateObject.getState() + ".");
            }
        }
    }

    public void changeCurrentState(State st) {
        log.info("Base before changeCurrentState to " + st.getState() + " from " + currentStateObject.getState() + ".");
        synchronized(currentStateObject) {
            currentStateObject = st;
        }
    }

    public State.StateDescriptor getCurrentState() {
        synchronized(currentStateObject) {
            return currentStateObject.getState();
        }
    }
}
