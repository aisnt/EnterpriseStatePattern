package io;

import command.Command;
import command.DTO;
import command.ResultWrapper;
import common.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import state.InvalidStateTransitionException;
import state.State;

/**
 * Created by david.hislop@korwe.com on 2014/06/22.
 */
public enum MessageSource implements Command {
    INSTANCE;

    Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public void onMessage(Message s)  {
        StateHandler stateHandler = StateHandler.INSTANCE;
        State.StateDescriptor old = stateHandler.getCurrentState();
        log.debug("MessageSource.onMessage() Proposed transition from " + old + " to " + s.getState() + ".");
        try {
            ResultWrapper<DTO> dto = stateHandler.doTransition(s);
            log.info("MessageSource.onMessage() Successful transition from " + old + " to " + s.getState() + ".");
        } catch (InvalidStateTransitionException ex) {
            log.trace("MessageSource.onMessage() Exception=" + ex.getMessage());
            log.info("MessageSource.onMessage() Failed transition from " + stateHandler.getCurrentState() + " to " + s.getState() + ".");
        }
    }
}

