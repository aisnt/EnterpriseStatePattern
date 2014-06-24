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
public class State2 extends State {
    final Logger log = LoggerFactory.getLogger(this.getClass());

    public State2(StateIOHandler stateIOHandler) {
        super(stateIOHandler, StateDescriptor.State2);
    }
    @Override
    public ResultWrapper<DTO> doIt(Message message)  throws InvalidStateTransitionException {
        log.trace("State2.doIt From " + this.getState() + " to " + message.getState() +".");
        switch (message.getState()) {
            case State3: {
                transition(StateDescriptor.State3);
                break;
            }

            default: {
                throw new InvalidStateTransitionException("Failed From " + this.getState() + " to " + message.getState() +".");
            }
        }
        return new ResultWrapper<DTO>(new DTO());
    }
}
