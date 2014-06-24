package state;

import command.DTO;
import command.ResultWrapper;
import common.Message;
import io.StateIOHandler;

/**
 * Created by davidhislop on 2014/06/23.
 */
public class StateInitial  extends State {
    public StateInitial(StateIOHandler stateIOHandler) {
        super(stateIOHandler, StateDescriptor.Initial);
    }

    @Override
    public ResultWrapper<DTO> doIt(Message message) throws InvalidStateTransitionException {
        System.out.println("StateInitial.doIt From " + this.getState() + " to " + message.getState() +".");
        switch (message.getState()) {
            case State1: {
                mooreTransition(message.getPayload());
                transition(StateDescriptor.State1);
                break;
            }
            default: {
                throw new InvalidStateTransitionException("Failed From " + this.getState() + " to " + message.getState() +".");
            }
        }
        return new ResultWrapper<DTO>(new DTO());
    }

    private Boolean mooreTransition(String payload) {
        System.out.println("mooreTransition -> " + payload);
        //return mooreTransitionStart() && mooreTransitionMid() && mooreTransitionEnd();
        return true;
    }

    private Boolean mooreTransitionStart() {
        //TODO
        return true;
    }

    private Boolean mooreTransitionMid() {
        //TODO
        return true;
    }

    private Boolean mooreTransitionEnd() {
        //TODO
        return true;
    }

    private Boolean mealyTransition() {
        //TODO
        return false;
    }
}