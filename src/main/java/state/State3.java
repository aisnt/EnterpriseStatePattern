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
public class State3 extends State {
    final Logger log = LoggerFactory.getLogger(this.getClass());

    public State3(StateIOHandler stateIOHandler) {
        super(stateIOHandler,StateDescriptor.State3);
    }
    @Override
    public ResultWrapper<DTO> doIt(Message message)  throws InvalidStateTransitionException {
        log.trace("State3.doIt From " + this.getState() + " to " + message.getState() +".");
        switch (message.getState()) {

            case State1: {
                if (!transition(StateDescriptor.State1, message.getPayload())){
                    throw new InvalidStateTransitionException("Failed from " + this.getState() + " to " + message.getState() +".");
                }
                break;
            }
            case Final: {
                if (!transition(StateDescriptor.Final, message.getPayload())){
                    throw new InvalidStateTransitionException("Failed from " + this.getState() + " to " + message.getState() +".");
                }
                break;
            }

            default: {
                throw new InvalidStateTransitionException("Failed From " + this.getState() + " to " + message.getState() +".");
            }
        }
        return new ResultWrapper<DTO>(new DTO());
    }

    @Override
    protected Boolean mooreTransition(String payload) {
        log.info("mooreTransition -> " + payload);
        return true;
    }

    @Override
    protected Boolean mealyTransition() {
        return null;
    }
}


