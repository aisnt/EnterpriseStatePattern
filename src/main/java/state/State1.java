package state;

import command.DTO;
import command.ResultWrapper;
import common.Message;
import io.StateIOHandler;

/**
 * Created by davidhislop on 2014/06/23.
 */
public class State1 extends State {
    public State1(StateIOHandler stateIOHandler) {
        super(stateIOHandler, StateDescriptor.State1);
    }

    @Override
    public ResultWrapper<DTO> doIt(Message message)  throws InvalidStateTransitionException {
        System.out.println("State1.doIt From " + this.getState() + " to " + message.getState() +".");
        switch (message.getState()) {
            case State2: {
                transition(StateDescriptor.State2);
                break;
            }

            default: {
                throw new InvalidStateTransitionException("Failed From " + this.getState() + " to " + message.getState() +".");
            }
        }
        return new ResultWrapper<DTO>(new DTO());
    }
}
