package command;

import common.Message;
import common.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import state.MessageSource;
import state.StateDescriptorFactory;
import state.StateHandler;
import exceptions.InvalidStateException;

import java.util.Date;

/**
 * Created by david.hislop@korwe.com on 2014/06/22.
 */
public class Main {
    final static Logger log = LoggerFactory.getLogger(Main.class);
    private static MessageListenerImpl messageListener;

    public static void main(String[] args) throws Exception {
        log.trace("Main.main()");
        setUp();
        int putMessageWait = Util.getIntProperty("PutMessageWait");
        while ( StateDescriptorFactory.INSTANCE.isFinal(StateHandler.INSTANCE.getCurrentState())) {
            try {
                //TODO
                int milliseconds = (int) (Math.random() * putMessageWait);
                log.trace("Main.main() Waiting " + milliseconds + " ms in state " + StateHandler.INSTANCE.getCurrentState());
                Thread.sleep(milliseconds);
                putMessage();
            } catch (InterruptedException e) {
                log.warn("Main.main() InterruptedException " + e.getMessage());
            }
        }
    }

    public static void setUp() throws Exception {
        log.info("Main.setUp()");

        Util.setProperties("src/main/resources/state.properties");

        //TODO Start from any state
        StateHandler.INSTANCE.setInitialState("State2");
        messageListener = new MessageListenerImpl( MessageSource.INSTANCE );
        messageListener.start();
    }

    protected static void putMessage() throws InterruptedException, InvalidStateException {
        log.trace("Main.putMessage() start ...");

        Date date = new java.util.Date();
        Message message = new Message(makeRandomState(), Util.prettyPrintDate(date));
        int size = messageListener.putMessage(message);
        log.debug("Main.putMessage() messages in queue=" + size + ".");
        log.trace("Main.putMessage() " + message.getDestinationState() + " " + message.getPayload() );
    }

    private static StateDescriptorFactory.StateDescriptor makeRandomState() throws InvalidStateException {
        log.trace("Main.makeRandomState() ");
        int stateIndex = (int) (Math.random()* StateDescriptorFactory.INSTANCE.Max());
        StateDescriptorFactory.StateDescriptor stateDescriptor =  StateDescriptorFactory.INSTANCE.get(stateIndex);
        log.trace("Main.makeRandomState() " + stateIndex + "->" + stateDescriptor);
        return stateDescriptor;
    }
}