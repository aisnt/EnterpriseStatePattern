package state;

import command.DataTransferObject;
import command.ResultWrapper;
import command.TransferApi;
import command.TransferApiImpl;
import common.Message;
import exceptions.InvalidStateException;
import exceptions.InvalidStateTransitionException;
import exceptions.SendingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import state.dynamic.StateFactory;

import java.io.IOException;
import java.util.Objects;

/**
 * Created by david.hislop@korwe.com on 2014/06/23.
 *
 */
public abstract class State {
    private final static Logger log = LoggerFactory.getLogger(State.class);

    public State(StateDescriptor stateDescriptor) {
        log.trace("State.ctor() for " + stateDescriptor.name);
        currentState = stateDescriptor;
    }

    public static State create(StateDescriptor stateDescriptor) {
        log.trace("State.create() " + stateDescriptor.name);

        try {
            State state = StateFactory.INSTANCE.create(stateDescriptor);
            return state;
        } catch (IOException ex) {
            log.error("State.create() IOException. State.create() " + ex.getMessage());
        } catch (InvalidStateException ex) {
            log.error("State.create() InvalidStateException. State.create() " + ex.getMessage());
        }
        return null;
    }

    public StateDescriptor getState() {
        return currentState;
    }

    private StateDescriptor currentState;

    //This is called by the StateHandler
    public ResultWrapper<DataTransferObject> doTransition(Message message)
            throws InvalidStateTransitionException, SendingException {
        log.trace("StateFactory.doTransition() from " + this.getState().name + " to " + message.getDestinationState().name + ".");

         /**************************************************************************************************************
         *                                Select either Moore or Mealy transitions.                                    *
         ***************************************************************************************************************/

        ResultWrapper<DataTransferObject> dtos = transitionMoore(message.getDestinationState(), message.getPayload());

        return dtos;
    }

    protected abstract Boolean validatePolicy(StateDescriptor thisState, StateDescriptor nextState) throws InvalidStateTransitionException;

    final Boolean transitionLock = false;

    protected ResultWrapper<DataTransferObject> transitionMealy(StateDescriptor destinationState, String message)
            throws SendingException, InvalidStateTransitionException {
        log.trace("State.transitionMealy() transition from " + this.getState().name + " to " + destinationState.name + ".");
        log.trace("State.transitionMealy() Before class Name = " + this.getClass().getName() + ".");
        checkThisStateIsCurrent();
        StateHandler stateHandler = StateHandler.INSTANCE;

        synchronized (transitionLock) {
            if (!validatePolicy(this.getState(), destinationState)) {
                String errorMessage = "StateFactory.transitionMealy() No transition allowed from ";
                errorMessage += this.getState().name + " to " + destinationState.name + ".";

                if (!stateHandler.addFailedEvent(this.getState(), destinationState)){
                    log.warn("State.transitionMoore() error");
                }
                throw new InvalidStateTransitionException(errorMessage);
            }

            ResultWrapper<DataTransferObject> wrappedDto = sendMessage(message);
            if (wrappedDto == null)  {
                log.error("State.transitionMealy() Cannot transition.");
                log.error("State.transitionMealy() Cannot transition.");
                throw new SendingException("State.transitionMealy() Cannot transition.");
            }

            State destinationStateInstance = create(destinationState);
            if (!stateHandler.changeCurrentState(destinationStateInstance, wrappedDto.get().getUuid())) {
                log.error("State.transitionMealy() Cannot changeCurrentState.");
                return null;
            }
            return wrappedDto;
        }
    }

    protected ResultWrapper<DataTransferObject> transitionMoore(StateDescriptor destinationState, String message)
            throws SendingException, InvalidStateTransitionException {
        log.trace("State.transitionMoore() transition from " + this.getState().name + " to " + destinationState.name + ".");
        log.trace("State.transitionMoore() Before class Name = " + this.getClass().getName() + ".");
        StateHandler stateHandler = StateHandler.INSTANCE;
        checkThisStateIsCurrent();
        synchronized (transitionLock) {
            //Check that transition is possible
            if (!validatePolicy(this.getState(), destinationState)) {
                String errorMessage = "State.transitionMoore() No transition allowed from ";
                errorMessage += this.getState().name + " to " + destinationState.name + ".";
                log.warn(errorMessage);

                if (!stateHandler.addFailedEvent(this.getState(), destinationState)){
                    log.warn("State.transitionMoore() error");
                }
                throw new InvalidStateTransitionException(errorMessage);
            }
        }

        ResultWrapper<DataTransferObject> wrappedDto = null;
        //TransitionMid
        synchronized (transitionLock) {
            wrappedDto = sendMessage(message);
            if (wrappedDto == null) {
                log.error("State.transitionMoore() Cannot transition.");
                throw new SendingException("State.transition() Cannot transition.");
            }
        }

        //TransitionEnd
        synchronized (transitionLock) {
            //Roll back if need be
            State destinationStateInstance = create(destinationState);
            if (!stateHandler.changeCurrentState(destinationStateInstance, wrappedDto.get().getUuid())) {
                log.error("State.transitionMealy() Cannot changeCurrentState.");
                throw new SendingException("State.transition() Cannot change state for transition.");
            }
        }
        return wrappedDto;
    }

    protected void checkThisStateIsCurrent() throws InvalidStateTransitionException {
        StateHandler stateHandler = StateHandler.INSTANCE;
        log.debug("State.checkThisStateIsCurrent()  current=" + stateHandler.getCurrentState().name + " this=" + this.getState().name + ".");
        if (!stateHandler.getCurrentState().equals(this.getState())) {
            String err = "State.checkThisStateIsCurrent() current state (" + stateHandler.getCurrentState().name +
                    ") and this state (" + this.getState().name + ") do not correspond.";
            throw new InvalidStateTransitionException (err);
        }
    }

    protected ResultWrapper<DataTransferObject> sendMessage(String payload) {
        log.trace("State.sendMessage() Output -> " + payload);
        TransferApi transferApi = new TransferApiImpl();
        DataTransferObject dto = transferApi.get(payload);
        ResultWrapper<DataTransferObject> wrappedDto = new ResultWrapper<>(dto);
        return wrappedDto;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof State)) {
            return false;
        }
        State that = (State)other;
        return (this.currentState.equals(that.currentState));
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.currentState);
    }
}
