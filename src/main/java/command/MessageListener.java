package command;

import common.Message;
import common.Util;
import io.StateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import state.StateDescriptor;

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
        log.trace("MessageListener.onMessage() for " + message.getState() + " state.");
        command.onMessage(message);
    }

    @Override
    public void run() {
        log.trace("MessageListener.run()");
        try {
            int getMessageWait = Util.getIntProperty("GetMessageWait");
            do {
                log.trace("MessageListener.run() getMessage");
                Message message = getMessage();
                log.info("MessageListener.run() message retrieved " + message.getState() + " state.");
                onMessage(message);
                sleep(getMessageWait);
                log.info("MessageListener.getMessage() last event=" + StateHandler.INSTANCE.getLastEvent().toString() + ".");
            } while ( StateHandler.INSTANCE.getLastEvent().to != StateDescriptor.Final );
        } catch (InterruptedException e) {
            log.info("MessageListener.run() InterruptedException = " + e.getMessage());
        } catch (IOException e) {
            log.info("MessageListener.run() IOException = " + e.getMessage());
        }
    }

    protected abstract Message getMessage() throws InterruptedException;
    protected abstract boolean messagesAvail();
}
