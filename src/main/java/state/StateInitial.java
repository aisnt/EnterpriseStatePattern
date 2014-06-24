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
public class StateInitial  extends State {
    final Logger log = LoggerFactory.getLogger(this.getClass());
    
    public StateInitial(StateIOHandler stateIOHandler) {
        super(stateIOHandler, StateDescriptor.Initial);
    }

    @Override
    public ResultWrapper<DTO> doIt(Message message) throws InvalidStateTransitionException {
        log.trace("StateInitial.doIt From " + this.getState() + " to " + message.getState() +".");
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
        log.trace("mooreTransition -> " + payload);
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