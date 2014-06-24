package state;

import command.DTO;
import command.ResultWrapper;
import common.Message;
import io.StateIOHandler;

/**
 * Created by davidhislop on 2014/06/23.
 */
public class StateFinal extends State {

    public StateFinal(StateIOHandler stateIOHandler) {
        super(stateIOHandler,StateDescriptor.Final);
    }
    @Override
    public ResultWrapper<DTO> doIt(Message message)  throws InvalidStateTransitionException{
        System.out.println("StateFinal.doIt From " + this.getState() + " to " + message.getState() +".");
        return new ResultWrapper<DTO>(new DTO());
    }
}
