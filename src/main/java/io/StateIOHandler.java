package io;

import command.Command;
import command.DTO;
import command.ResultWrapper;
import common.Message;
import state.InvalidStateTransitionException;
import state.State;

/**
 * Created by davidhislop on 2014/06/22.
 */
public class StateIOHandler implements Command {
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
        System.out.println("Base.handleMessage Desired transition from " + old + " to " +  s.getState() + ".");
        try {
            ResultWrapper<DTO> dto = currentStateObject.doIt(s);
            System.out.println("Successful transition from " + old + " to " + s.getState()   + ".");
        } catch (InvalidStateTransitionException ex) {
            System.out.println(ex.getMessage());
            System.out.println("Failed transition from " + currentStateObject.getState() + ".");
        }
    }

    public void changeCurrentState(State st) {
        System.out.println("Base before changeCurrentState to " + st.getState() + " from "+ currentStateObject.getState()  + ".");
        currentStateObject = st;
    }

    public State.StateDescriptor getCurrentState() {
        return currentStateObject.getState();
    }
}
