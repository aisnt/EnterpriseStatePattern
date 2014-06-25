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
public class StateInitial extends State {
    final Logger log = LoggerFactory.getLogger(this.getClass());
    
    public StateInitial(StateIOHandler stateIOHandler) {
        super(stateIOHandler, StateDescriptor.Initial);
    }

    @Override
    public ResultWrapper<DTO> doTransition(Message message) throws InvalidStateTransitionException {
        log.trace("StateInitial.doTransition() From " + this.getState() + " to " + message.getState() + ".");
        switch (message.getState()) {
            case State1: {
                if (!transition(StateDescriptor.State1, message.getPayload())){
                    throw new InvalidStateTransitionException("StateInitial.doTransition() Failed from " + this.getState() + " to " + message.getState() +".");
                }
                break;
            }
            default: {
                throw new InvalidStateTransitionException("StateInitial.doTransition() Failed from " + this.getState() + " to " + message.getState() +".");
            }
        }
        return new ResultWrapper<DTO>(new DTO());
    }

    //return mooreTransitionStart() && mooreTransitionMid() && mooreTransitionEnd();
    @Override
    protected Boolean mooreTransition(String payload) {
        System.out.println("StateInitial.mooreTransition() Output -> " + payload);
        return true;
    }

    @Override
    protected Boolean mealyTransition() {
        return null;
    }
}