package command;

import common.Message;
import common.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import state.Event;
import state.StateDescriptorFactory;
import state.StateHandler;

import java.io.IOException;

/**
 * Created by david.hislop@korwe.com on 2014/06/21.
 */
public abstract class MessageListener extends Thread {
    private final static Logger log = LoggerFactory.getLogger(MessageListener.class);
    public MessageListener(Command command){
        log.trace("MessageListener.ctor()");
        this.command = command;
    }

    Command command;

    protected void onMessage(Message message) {
        log.trace("MessageListener.onMessage() for " + message.getDestinationState().name + " state.");
        command.onMessage(message);
    }

    @Override
    public void run() {
        log.trace("MessageListener.run() start ...");
        try {
            int getMessageWait = Util.getIntProperty("GetMessageWait");
            log.trace("MessageListener.run() getMessageWait=" + getMessageWait + ".");
            do {
                log.trace("MessageListener.run() getMessage");
                Message message = getMessage();
                log.info("MessageListener.run() message retrieved " + message.getDestinationState().name + " state.");
                onMessage(message);
                sleep(getMessageWait);
                log.info("MessageListener.run() last event=" + StateHandler.INSTANCE.getLastEvent().toString() + ".");
            } while ( !doRun() );
        } catch (InterruptedException e) {
            log.info("MessageListener.run() InterruptedException = " + e.getMessage());
        } catch (IOException e) {
            log.info("MessageListener.run() IOException = " + e.getMessage());
        }
        log.warn("MessageListener.run() ... end");
    }

    public static boolean doRun() {
        log.trace("MessageListener.doRun() start ...");
        Event event = StateHandler.INSTANCE.getLastSuccessfulEvent();
        if (event == null) {
            log.trace("MessageListener.doRun() getLastSuccessfulEvent null.");
            event = StateHandler.INSTANCE.getLastEvent();
            if ( StateDescriptorFactory.INSTANCE.isFinal(event.to )) {
                log.trace("MessageListener.doRun() lastEvent isFinal.");
                return true;
            }
        }
        StateDescriptorFactory.StateDescriptor toState = event.to;
        log.trace("MessageListener.doRun() toState "+ event.to.name +" is final = " + StateDescriptorFactory.INSTANCE.isFinal(toState) +".");
        return StateDescriptorFactory.INSTANCE.isFinal(toState);
    }

    protected abstract Message getMessage() throws InterruptedException;
    protected abstract boolean messagesAvail();
}
