package state;

import command.DTO;
import command.ResultWrapper;
import common.Message;
import io.StateIOHandler;

/**
 * Created by davidhislop on 2014/06/23.
 */
public class State3 extends State {

    public State3(StateIOHandler stateIOHandler) {
        super(stateIOHandler,StateDescriptor.State3);
    }
    @Override
    public ResultWrapper<DTO> doIt(Message message)  throws InvalidStateTransitionException {
        System.out.println("State3.doIt From " + this.getState() + " to " + message.getState() +".");
        switch (message.getState()) {

            case State1: {
                transition(StateDescriptor.State1);
                break;
            }
            case Final: {
                transition(StateDescriptor.Final);
                break;
            }

            default: {
                throw new InvalidStateTransitionException("Failed From " + this.getState() + " to " + message.getState() +".");
            }
        }
        return new ResultWrapper<DTO>(new DTO());
    }
}


