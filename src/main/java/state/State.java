package state;

import command.DTO;
import command.ResultWrapper;
import common.Message;
import exceptions.InvalidStateTransitionException;
import exceptions.SendingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import exceptions.InvalidStateException;
import state.dynamic.StateFactory;

import java.io.IOException;

/**
 * Created by david.hislop@korwe.com on 2014/06/23.
 *
 */
public abstract class State {
    final static Logger log = LoggerFactory.getLogger(State.class);

    public State(StateDescriptorFactory.StateDescriptor stateDescriptor) {
        log.trace("State.ctor() for " + stateDescriptor.name);
        currentState = stateDescriptor;
    }

    public static State create(StateDescriptorFactory.StateDescriptor stateDescriptor) {
        log.trace("State.create() " + stateDescriptor.name);

        try {
            State s = StateFactory.INSTANCE.create(stateDescriptor);
            return s;
        } catch (IOException ex) {
            log.error("State.create() IOException. State.create() " + ex.getMessage());
        } catch (InvalidStateException ex) {
            log.error("State.create() InvalidStateException. State.create() " + ex.getMessage());
        }
        return null;
    }

    public StateDescriptorFactory.StateDescriptor getState() {
        return currentState;
    }

    private StateDescriptorFactory.StateDescriptor currentState ;

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
    protected ResultWrapper<DTO> transition(StateDescriptorFactory.StateDescriptor stateDescriptor, String message) throws SendingException {
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
                    log.error("State.transition() Current state " + stateHandler.getCurrentState().name + " != " + " base state " + this.getState().name + ".");
                    throw new SendingException("State.transition() Current state " + stateHandler.getCurrentState().name + " != " + " base state " + this.getState().name + ".");
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
