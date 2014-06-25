package command;

import common.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by david.hislop@korwe.com on 2014/06/21.
 */
public abstract class MessageListener extends Thread {
    final Logger log = LoggerFactory.getLogger(this.getClass());
    Command command;
    public MessageListener(Command command){
        log.trace("MessageListener.ctor()");
        this.command = command;
    }

    protected void onMessage(Message message) {
        log.trace("MessageListener.onMessage() for " + message.getState() + " state.");
        command.onMessage(message);
    }

    @Override
    public void run() {
        log.trace("MessageListener.run()");
        try {
            while (true) {
                log.trace("MessageListener.run() getMessage");
                Message message = getMessage();
                log.info("MessageListener.getMessage() retrieved " + message.getState() + " state.");
                onMessage( message );
            }
        } catch (InterruptedException e) {
            log.info("MessageListener.run() InterruptedException " + e.getMessage());
        }
    }

    protected abstract Message getMessage() throws InterruptedException;
    protected abstract boolean messagesAvail()  ;
}
