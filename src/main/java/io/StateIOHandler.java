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
 * Created by davidhislop on 2014/06/22.
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
    public void handleMessage(Message s)  {
        State.StateDescriptor old = currentStateObject.getState();
        log.trace("Base.handleMessage Desired transition from " + old + " to " +  s.getState() + ".");
        try {
            ResultWrapper<DTO> dto = currentStateObject.doIt(s);
            log.info("Successful transition from " + old + " to " + s.getState()   + ".");
        } catch (InvalidStateTransitionException ex) {
            log.trace(ex.getMessage());
            log.info("Failed transition from " + currentStateObject.getState() + ".");
        }
    }

    public void changeCurrentState(State st) {
        log.info("Base before changeCurrentState to " + st.getState() + " from "+ currentStateObject.getState()  + ".");
        currentStateObject = st;
    }

    public State.StateDescriptor getCurrentState() {
        return currentStateObject.getState();
    }
}
