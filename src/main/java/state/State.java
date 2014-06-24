package state;

import command.DTO;
import command.ResultWrapper;
import common.Message;
import io.StateIOHandler;

/**
 * Created by davidhislop on 2014/06/23.
 */
public abstract class State  {
    public State(StateIOHandler stateIOHandler, StateDescriptor stateDescriptor) {
        System.out.println("State ctor for " + stateDescriptor);
        this.stateIOHandler = stateIOHandler;
        currentState = stateDescriptor;
    }

    StateIOHandler stateIOHandler;

    State makeNew(StateDescriptor sd) {
        System.out.println("State.makeNew  " + sd);
        switch (sd) {
            case State1: return new State1(stateIOHandler);
            case State2: return new State2(stateIOHandler);
            case State3: return new State3(stateIOHandler);
            case Final: return new StateFinal(stateIOHandler);
            case Initial: return new StateInitial(stateIOHandler);
            default:
        }
        return null;
    }

    public StateDescriptor getState() {
        return currentState;
    }

    protected void changeCurrentState(State st) {
        System.out.println("Base before changeCurrentState to " + st.getState() + " from " + stateIOHandler.getCurrentState() + ".");
        stateIOHandler.changeCurrentState(st);
    }

    public enum StateDescriptor {Initial, State1, State2, State3, Final};
    private StateDescriptor currentState ;

    public abstract ResultWrapper<DTO> doIt(Message message) throws InvalidStateTransitionException;

    protected Boolean transition(StateDescriptor sd) {
        System.out.println("State transition from " + this.getState() + " to " + sd +".");
        if (stateIOHandler.getCurrentState() != this.getState()) {
            System.out.println("#####################################################################");
            System.out.println("Current state " + stateIOHandler.getCurrentState() + " != " + " base state " + this.getState() + ".");
            return false;
        }
        State st = makeNew(sd);
        changeCurrentState(st);
        return true;
    }
}
