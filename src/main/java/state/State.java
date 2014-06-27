package state;

import command.DTO;
import command.ResultWrapper;
import common.Message;
import io.StateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by david.hislop@korwe.com on 2014/06/23.
 */
public abstract class State {
    final static Logger log = LoggerFactory.getLogger(State.class);
    public State(StateDescriptor stateDescriptor) {
        log.trace("State.ctor() for " + stateDescriptor);
        currentState = stateDescriptor;
    }

    public static State create(StateDescriptor stateDescriptor) {
        log.trace("State.create() " + stateDescriptor);
//        StateHandler stateIOHandler = StateHandler.INSTANCE;
        switch (stateDescriptor) {
            case State1: return new State1();
            case State2: return new State2();
            case State3: return new State3();
            case Final: return new StateFinal();
            case Initial: return new StateInitial();
            default:
        }
        return null;
    }

    public StateDescriptor getState() {
        return currentState;
    }

    public enum StateDescriptor {Initial, State1, State2, State3, Final, Max};
    private StateDescriptor currentState ;

    public abstract ResultWrapper<DTO> doTransition(Message message) throws InvalidStateTransitionException;

    protected abstract Boolean mooreTransition(String payload) ;

    protected abstract Boolean mealyTransition() ;

    Boolean mooreTransitionLock = false;

    /*By now the transition is permitted*/
    protected Boolean transition(StateDescriptor sd, String message) {
        log.trace("State.transition() transition from " + this.getState() + " to " + sd + ".");
        log.trace("State.transition() Before class Name = " + this.getClass().getName() + ".");
        StateHandler stateHandler = StateHandler.INSTANCE;
        synchronized (mooreTransitionLock) {
            //mooreTransitionStart
            if (stateHandler.getCurrentState() != this.getState()) {
                log.error("State.transition() Current state " + stateHandler.getCurrentState() + " != " + " base state " + this.getState() + ".");
                return false;
            }

            //mooreTransitionMid
            if (!mooreTransition(message)) {
                log.error("State.transition() Cannot transition.");
                return false;
            }

            //mooreTransitionEnd
            State st = create(sd);
            stateHandler.changeCurrentState(st);
        }
        log.trace("State.transition() After class Name = " + this.getClass().getName() + ".");
        return true;
    }
}
