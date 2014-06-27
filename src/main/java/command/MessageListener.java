package command;

import common.Message;
import io.StateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
            do {
                log.trace("MessageListener.run() getMessage");
                Message message = getMessage();
                log.info("MessageListener.run() message retrieved " + message.getState() + " state.");
                onMessage(message);
                sleep(50);  //Provide put with atom of opposition
                log.info("MessageListener.getMessage() last event=" + StateHandler.INSTANCE.getLastEvent().toString() + ".");
            } while ( StateHandler.INSTANCE.getLastEvent().to != state.State.StateDescriptor.Final );
        } catch (InterruptedException e) {
            log.info("MessageListener.run() InterruptedException = " + e.getMessage());
        }
    }

    protected abstract Message getMessage() throws InterruptedException;
    protected abstract boolean messagesAvail()  ;
}
