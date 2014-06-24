package state;

import command.DTO;
import command.ResultWrapper;
import common.Message;
import io.StateIOHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by davidhislop on 2014/06/23.
 */
public abstract class State  {
    final Logger log = LoggerFactory.getLogger(this.getClass());
    public State(StateIOHandler stateIOHandler, StateDescriptor stateDescriptor) {
        log.trace("State ctor for " + stateDescriptor);
        this.stateIOHandler = stateIOHandler;
        currentState = stateDescriptor;
    }

    StateIOHandler stateIOHandler;

    State makeNew(StateDescriptor sd) {
        log.trace("State.makeNew  " + sd);
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
        log.trace("Base before changeCurrentState to " + st.getState() + " from " + stateIOHandler.getCurrentState() + ".");
        stateIOHandler.changeCurrentState(st);
    }

    public enum StateDescriptor {Initial, State1, State2, State3, Final};
    private StateDescriptor currentState ;

    public abstract ResultWrapper<DTO> doIt(Message message) throws InvalidStateTransitionException;

    protected Boolean transition(StateDescriptor sd) {
        log.trace("State transition from " + this.getState() + " to " + sd +".");
        if (stateIOHandler.getCurrentState() != this.getState()) {
            log.error("Current state " + stateIOHandler.getCurrentState() + " != " + " base state " + this.getState() + ".");
            return false;
        }
        State st = makeNew(sd);
        changeCurrentState(st);
        return true;
    }
}
