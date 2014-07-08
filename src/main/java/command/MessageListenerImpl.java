package command;

import common.Message;
import common.Util;
import io.StateHandler;
import state.StateDescriptorX;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    @Override
    protected boolean messagesAvail() {
        log.trace("MessageListenerImpl.messagesAvail() start ...");
        int size = size();
        log.trace("MessageListenerImpl.messagesAvail(). Queue size = " + size + ".");
        return (size > 0);
    }

    protected int size() {
        int size;
        synchronized(messages) {
            size = messages.size();
        }
        log.trace("MessageListenerImpl.size(). Queue size = " + size + ".");
        return size ;
    }

    /*Helper message to load storage for testing*/
    protected int putMessage(Message message) throws InterruptedException {
        log.trace("MessageListenerImpl.putMessage() start ...");
        return addMessage(message);
    }

    private List messages = Collections.synchronizedList(new ArrayList());

    private static final int MAXQUEUE = Util.getIntPropertyDef("MaxQueue", 5);  //Why 5? Why not. This is just an impl thing.

    private synchronized Message popMessage() throws InterruptedException {
        log.trace("MessageListenerImpl.popMessage() start ...");
        notify();
        while (!messagesAvail()) {
            log.info("MessageListenerImpl.popMessage() messages not available. Wait.");
            wait();
            log.trace("MessageListenerImpl.popMessage() continuing ...");
        }
        Message message;
        synchronized(messages) {
            message = (Message) messages.get(0);
            log.info("MessageListenerImpl.popMessage() retrieved " + message.getState().name + " state.");
            log.trace("MessageListenerImpl.popMessage() Before size=" + messages.size() + ".");
            messages.remove(0);
        }

        log.trace("MessageListenerImpl.popMessage() After size=" + messages.size() + ".");
        return message;
    }

    private synchronized int addMessage(Message message) throws InterruptedException {
        log.trace("MessageListenerImpl.addMessage() start ...");
        while ((size() >= MAXQUEUE) && (!StateDescriptorX.INSTANCE.Final(StateHandler.INSTANCE.getCurrentState() ))) {
            log.trace("MessageListenerImpl.addMessage() Queue size = " + size() + ".");
            log.trace("MessageListenerImpl.addMessage() waiting.");
            wait();
        }
        synchronized(message) {
            messages.add(message);
        }
        log.trace("MessageListenerImpl.addMessage() notify.");
        notify();
        log.trace("MessageListenerImpl.addMessage() Queue size = " + size() + ".");
        return size();
    }
}
