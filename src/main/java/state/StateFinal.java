package state;

import command.DTO;
import command.ResultWrapper;
import common.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by david.hislop@korwe.com on 2014/06/23.
 */
public class StateFinal extends State {
    final Logger log = LoggerFactory.getLogger(this.getClass());

    public StateFinal( ) {
        super( StateDescriptor.Final);
    }
    @Override
    public ResultWrapper<DTO> doTransition(Message message)  throws InvalidStateTransitionException{
        log.trace("StateFinal.doTransition() From " + this.getState() + " to " + message.getState() +".");
        throw new InvalidStateTransitionException("StateFinal.doTransition() Failed From " + this.getState() + " to " + message.getState() +".");
    }

    @Override
    protected Boolean mooreTransition(String payload) {
        System.out.println("StateFinal.mooreTransition() Output -> " + payload);
        return true;
    }

    @Override
    protected Boolean mealyTransition() {
        return null;
    }
}
