package state;

import command.DTO;
import command.ResultWrapper;
import common.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by david.hislop@korwe.com on 2014/06/23.
 */
public class State1 extends State {
    final Logger log = LoggerFactory.getLogger(this.getClass());
    public State1( ) {
        super( StateDescriptor.State1);
    }

    @Override
    public ResultWrapper<DTO> doTransition(Message message)  throws InvalidStateTransitionException {
        log.trace("State1.doTransition() From " + this.getState() + " to " + message.getState() +".");
        switch (message.getState()) {
            case State2: {
                if (!transition(StateDescriptor.State2, message.getPayload())) {
                    throw new InvalidStateTransitionException("State1.doTransition() Failed from " + this.getState() + " to " + message.getState() +".");
                }
                break;
            }

            default: {
                throw new InvalidStateTransitionException("State1.doTransition() No transition from " + this.getState() + " to " + message.getState() +".");
            }
        }
        return new ResultWrapper<DTO>(new DTO());
    }

    @Override
    protected Boolean mooreTransition(String payload) {
        System.out.println("State1.mooreTransition() Output -> " + payload);
        return true;
    }

    @Override
    protected Boolean mealyTransition() {
        return null;
    }
}
