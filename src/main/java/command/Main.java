package command;

import common.Message;
import common.Util;
import exceptions.InvalidStateException;
import graph.JFameEventStorageSimple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import state.MessageSource;
import state.StateDescriptorFactory;
import state.StateHandler;

import java.util.Date;

/**
 * Created by david.hislop@korwe.com on 2014/06/22.
 */
public class Main {
    private final static Logger log = LoggerFactory.getLogger(Main.class);
    private static MessageListenerImpl messageListener;

    public static void main(String[] args) throws Exception {
        log.trace("Main.main()");
        setUp();

        int period = Util.getIntProperty("PutMessageWait");
        while ( !messageListener.hasTransitionedToFinal() ) {

            int milliseconds = (int)Math.random()*period;
            try {
                log.trace("Main.main() Waiting " + milliseconds + " ms in state " + StateHandler.INSTANCE.getCurrentState().name);
                Thread.sleep(milliseconds);
                putMessage();
            } catch (InterruptedException e) {
                log.warn("Main.main() InterruptedException " + e.getMessage());
            }
        }
    }

    public static void setUp() throws Exception {
        log.trace("Main.setUp() ...");

        Util.setProperties("src/main/resources/state.properties");

        log.trace("Main.setUp() setting event storage.");
        jFameEventStorage = new JFameEventStorageSimple();
        StateHandler.INSTANCE.setCallback(jFameEventStorage);

        log.trace("Main.setUp() setting messageListener.");
        messageListener = new MessageListenerImpl( MessageSource.INSTANCE );
        StateHandler.INSTANCE.setInitialState("Initial");

        log.trace("Main.setUp() starting messageListener.");
        messageListener.start();
    }

    private static JFameEventStorageSimple jFameEventStorage;

    protected static void putMessage() throws InterruptedException, InvalidStateException {
        log.trace("Main.putMessage() start ...");
        Date date = new java.util.Date();
        Message message = new Message(makeRandomState(), Util.prettyPrintDate(date));
        int size = messageListener.putMessage(message);
        log.debug("Main.putMessage() messages in queue=" + size + ".");
        log.trace("Main.putMessage() " + message.getDestinationState().name + " " + message.getPayload() );
    }

    private static StateDescriptorFactory.StateDescriptor makeRandomState() throws InvalidStateException {
        log.trace("Main.makeRandomState() ");
        int stateIndex = (int)Math.round( Math.random()*( StateDescriptorFactory.INSTANCE.Max() - 1) );
        StateDescriptorFactory.StateDescriptor stateDescriptor =  StateDescriptorFactory.INSTANCE.get(stateIndex);
        log.trace("Main.makeRandomState() " + stateIndex + "->" + stateDescriptor.name);
        return stateDescriptor;
    }
}