package state;

import command.DTO;
import command.ResultWrapper;
import common.Message;
import io.StateIOHandler;

/**
 * Created by davidhislop on 2014/06/23.
 */
public class State2 extends State {

    public State2(StateIOHandler stateIOHandler) {
        super(stateIOHandler, StateDescriptor.State2);
    }
    @Override
    public ResultWrapper<DTO> doIt(Message message)  throws InvalidStateTransitionException {
        System.out.println("State2.doIt From " + this.getState() + " to " + message.getState() +".");
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
