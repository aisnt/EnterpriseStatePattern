package io;

import command.Command;
import command.DTO;
import command.ResultWrapper;
import common.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import state.InvalidStateTransitionException;
import state.SendingException;
import state.StateDescriptorFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by david.hislop@korwe.com on 2014/06/22.
 */
public enum MessageSource implements Command {
    INSTANCE;

    Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public void onMessage(Message s)  {
        StateHandler stateHandler = StateHandler.INSTANCE;
        StateDescriptorFactory.StateDescriptor old = stateHandler.getCurrentState();
        log.debug("MessageSource.onMessage() Proposed transition from " + old.name + " to " + s.getState().name + ".");
        try {
            ResultWrapper<DTO> dto = stateHandler.doTransition(s);
            results.add(dto);
            Event event = stateHandler.getLastEvent();
            events.add(event);
            log.info("MessageSource.onMessage() Successful transition from " + old.name + " to " + s.getState().name + ".");
        } catch (InvalidStateTransitionException ex) {
            log.trace("MessageSource.onMessage() InvalidStateTransitionException = " + ex.getMessage());
            log.info("MessageSource.onMessage() Failed transition from " + stateHandler.getCurrentState().name + " to " + s.getState().name + ".");
        } catch (SendingException ex) {
            log.trace("MessageSource.onMessage() SendingException = " + ex.getMessage());
            log.info("MessageSource.onMessage() Failed transition from " + stateHandler.getCurrentState().name + " to " + s.getState().name + ".");
        }
    }
    public List<Event> events = new ArrayList<>();
    public List<ResultWrapper<DTO> > results = new ArrayList<>();
}

