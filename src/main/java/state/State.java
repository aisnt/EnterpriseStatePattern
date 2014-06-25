package state;

import command.DTO;
import command.ResultWrapper;
import common.Message;
import io.StateIOHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by david.hislop@korwe.com on 2014/06/23.
 */
public abstract class State  {
    final Logger log = LoggerFactory.getLogger(this.getClass());
    public State(StateIOHandler stateIOHandler, StateDescriptor stateDescriptor) {
        log.trace("State.ctor() for " + stateDescriptor);
        this.stateIOHandler = stateIOHandler;
        currentState = stateDescriptor;
    }

    StateIOHandler stateIOHandler;

    //TODO
    State makeNew(StateDescriptor sd) {
        log.trace("State.makeNew() " + sd);
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

    public enum StateDescriptor {Initial, State1, State2, State3, Final};
    private StateDescriptor currentState ;

    public abstract ResultWrapper<DTO> doTransition(Message message) throws InvalidStateTransitionException;

    protected abstract Boolean mooreTransition(String payload) ;

    protected abstract Boolean mealyTransition() ;

    Boolean mooreTransitionLock = false;

    protected Boolean transition(StateDescriptor sd, String message) {
        log.trace("State.transition() transition from " + this.getState() + " to " + sd + ".");
        synchronized (mooreTransitionLock) {
            //mooreTransitionStart
            if (stateIOHandler.getCurrentState() != this.getState()) {
                log.error("State.transition() Current state " + stateIOHandler.getCurrentState() + " != " + " base state " + this.getState() + ".");
                return false;
            }

            //mooreTransitionMid
            if (!mooreTransition(message)) {
                log.error("State.transition() Cannot transition.");
                return false;
            }

            //mooreTransitionEnd
            State st = makeNew(sd);
            stateIOHandler.changeCurrentState(st);
        }
        return true;
    }
}
