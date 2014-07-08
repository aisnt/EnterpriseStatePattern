package command;

import common.Message;
import common.Util;
import io.StateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import state.StateDescriptorX;

import java.io.IOException;

/**
 * Created by david.hislop@korwe.com on 2014/06/21.
 */
public abstract class MessageListener extends Thread {
    final Logger log = LoggerFactory.getLogger(this.getClass());
    public MessageListener(Command command){
        log.trace("MessageListener.ctor()");
        this.command = command;
    }

    Command command;

    protected void onMessage(Message message) {
        log.trace("MessageListener.onMessage() for " + message.getState().name + " state.");
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
                log.info("MessageListener.run() message retrieved " + message.getState().name + " state.");
                onMessage(message);
                sleep(getMessageWait);
                log.info("MessageListener.run() last event=" + StateHandler.INSTANCE.getLastEvent().toString() + ".");
            } while ( !StateDescriptorX.INSTANCE.Final(StateHandler.INSTANCE.getLastEvent().to) );
        } catch (InterruptedException e) {
            log.info("MessageListener.run() InterruptedException = " + e.getMessage());
        } catch (IOException e) {
            log.info("MessageListener.run() IOException = " + e.getMessage());
        }
        log.trace("MessageListener.run() ... end");
    }

    protected abstract Message getMessage() throws InterruptedException;
    protected abstract boolean messagesAvail();
}
