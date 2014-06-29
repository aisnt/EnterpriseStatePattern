package state;

import command.DTO;
import command.ResultWrapper;
import common.Message;
import io.StateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by david.hislop@korwe.com on 2014/06/23.
 *
 */
public abstract class State {
    final static Logger log = LoggerFactory.getLogger(State.class);
    public State(StateDescriptor stateDescriptor) {
        log.trace("State.ctor() for " + stateDescriptor);
        currentState = stateDescriptor;
    }

    /*TODO OK so each State is implemented separately. But this is still ugly. We would like a default:
    StateHandler stateIOHandler = StateHandler.INSTANCE;
    */
    public static State create(StateDescriptor stateDescriptor) {
        log.trace("State.create() " + stateDescriptor);
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

    //TODO Hmm? Inject. The *abstract* base class should not need to know.
    public enum StateDescriptor {Initial, State1, State2, State3, Final, Max}

    public StateDescriptor getState() {
        return currentState;
    }

    private StateDescriptor currentState ;

    //TODO use DTO or loose
    /*
    part 1: This is called by the StateHandler and is implemented in all the derived classes
     */
    public abstract ResultWrapper<DTO> doTransition(Message message) throws InvalidStateTransitionException, SendingException;

    final Boolean transitionLock = false;

    /*
    * part 3: This is called by the derived state (step 2)
    * This is the housekeeping part of the transition. Teh real work is done in the derived class.
    *
    * By now the transition is permitted
    * */
    protected ResultWrapper<DTO> transition(StateDescriptor stateDescriptor, String message) throws SendingException {
        log.trace("State.transition() transition from " + this.getState() + " to " + stateDescriptor + ".");
        log.trace("State.transition() Before class Name = " + this.getClass().getName() + ".");
        StateHandler stateHandler = StateHandler.INSTANCE;
        ResultWrapper<DTO> dtos = null;

        /**************************************************************************************************************
        *                                Select either Moore or Mealy transitions.                                    *
        ***************************************************************************************************************/
        //Moore
        if (!transitionLock) {
            //TransitionStart
            synchronized (transitionLock) {
                if (stateHandler.getCurrentState() != this.getState()) {
                    log.error("State.transition() Current state " + stateHandler.getCurrentState() + " != " + " base state " + this.getState() + ".");
                    throw new SendingException("State.transition() Current state " + stateHandler.getCurrentState() + " != " + " base state " + this.getState() + ".");
                }
                //Check that transition is possible
            }

            //TransitionMid
            synchronized (transitionLock) {
                dtos = sendMessage(message);
                if (dtos == null) {
                    log.error("State.transition() Cannot transition.");
                    throw new SendingException("State.transition() Cannot transition.");
                }
            }

            //TransitionEnd
            synchronized (transitionLock) {
                //Roll back if need be
                State st = create(stateDescriptor);
                stateHandler.changeCurrentState(st);
            }
        }

        //Mealy
        /*
        if (transitionLock = false) {
            transitionLock = true;
            //TransitionStart
            synchronized (transitionLock) {
                if (stateHandler.getCurrentState() != this.getState()) {
                    log.error("State.transition() Current state " + stateHandler.getCurrentState() + " != " + " base state " + this.getState() + ".");
                    return false;
                }

                if (!sendMessage(message)) {
                    log.error("State.transition() Cannot transition.");
                    return false;
                }

                State st = create(stateDescriptor);
                stateHandler.changeCurrentState(st);
            }
            transitionLock = false;
        }*/

        log.trace("State.transition() After class Name = " + this.getClass().getName() + ".");
        return dtos;
    }

    /*
    * part 4: This is implemented in the derived state to do it
     */
    protected abstract ResultWrapper<DTO> sendMessage(String payload);
}
