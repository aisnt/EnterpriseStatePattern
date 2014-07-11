package state;

import command.Command;
import command.DataTransferObject;
import command.ResultWrapper;
import common.Message;
import exceptions.InvalidStateTransitionException;
import exceptions.SendingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by david.hislop@korwe.com on 2014/06/22.
 */
public enum MessageSource implements Command {
    INSTANCE;

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public void onMessage(Message s)  {
        StateHandler stateHandler = StateHandler.INSTANCE;
        StateDescriptorFactory.StateDescriptor currentState = stateHandler.getCurrentState();
        log.debug("MessageSource.onMessage() Proposed transition from " + currentState.name + " to " + s.getDestinationState().name + ".");
        try {
            ResultWrapper<DataTransferObject> dto = stateHandler.doTransition(s);
            results.add(dto);
            log.info("MessageSource.onMessage() Successful transition from " + currentState.name + " to " + s.getDestinationState().name + ".");
        } catch (InvalidStateTransitionException ex) {
            log.trace("MessageSource.onMessage() InvalidStateTransitionException = " + ex.getMessage());
            log.info("MessageSource.onMessage() Failed transition from " + stateHandler.getCurrentState().name + " to " + s.getDestinationState().name + ".");
        } catch (SendingException ex) {
            log.trace("MessageSource.onMessage() SendingException = " + ex.getMessage());
            log.info("MessageSource.onMessage() Failed transition from " + stateHandler.getCurrentState().name + " to " + s.getDestinationState().name + ".");
        }
    }

    //TODO these can be sent back
    public List<ResultWrapper<DataTransferObject> > results = new ArrayList<>();
}

