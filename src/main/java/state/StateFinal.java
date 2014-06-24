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
public class StateFinal extends State {
    final Logger log = LoggerFactory.getLogger(this.getClass());

    public StateFinal(StateIOHandler stateIOHandler) {
        super(stateIOHandler,StateDescriptor.Final);
    }
    @Override
    public ResultWrapper<DTO> doIt(Message message)  throws InvalidStateTransitionException{
        log.trace("StateFinal.doIt From " + this.getState() + " to " + message.getState() +".");
        throw new InvalidStateTransitionException("Failed From " + this.getState() + " to " + message.getState() +".");
    }
}
