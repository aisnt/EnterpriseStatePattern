package command;

import common.Message;

import java.util.Vector;

/**
 * Created by david.hislop@korwe.com on 2014/06/22.
 */
public class MessageListenerImpl extends MessageListener {
    public MessageListenerImpl(Command command) {
        super(command);
    }

    @Override
    protected Message getMessage() throws InterruptedException {
        log.trace("MessageListenerImpl.getMessage() start ...");
        return popMessage();
    }

    private synchronized Message popMessage() throws InterruptedException {
        log.trace("MessageListenerImpl.popMessage() start ...");
        notify();
        while ( !messagesAvail() ) {
            log.trace("MessageListenerImpl.popMessage() messages not available. Wait.");
            wait();//By executing wait() from a synchronized block, a thread gives up its hold on the lock and goes to sleep.
            log.trace("MessageListenerImpl.popMessage() continuing ...");
        }
        Message message = (Message)messages.firstElement();
        log.info("MessageListenerImpl.popMessage() retrieved " + message.getState() + " state.");
        messages.removeElement(message);
        return message;
    }

    @Override
    protected boolean messagesAvail() {
        return (messages.size() > 0);
    }

    protected int putMessage(Message message) throws InterruptedException {
        return addElement( message);
    }

    private Vector<Message> messages = new Vector();
    private static final int MAXQUEUE = 5;
    private synchronized int addElement(Message message) throws InterruptedException {
        int size = messages.size();
        while (size >=  MAXQUEUE) {
            log.trace("MessageListenerImpl.addElement() waiting. Queue size = " + size + ".");
            wait();
        }
        messages.addElement(message);
        notify();
        return messages.size();
    }
}
