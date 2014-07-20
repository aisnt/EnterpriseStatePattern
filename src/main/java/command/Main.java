package command;

import common.Message;
import common.Util;
import exceptions.InvalidStateException;
import graph.JFameEventStorageSimple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import state.MessageSource;
import state.StateDescriptor;
import state.StateDescriptorFactory;
import state.StateHandler;

import java.io.IOException;

/**
 * Created by david.hislop@korwe.com on 2014/06/22.
 */
public class Main {
    private final static Logger log = LoggerFactory.getLogger(Main.class);
    private static MessageListenerImpl messageListener;
    private static JFameEventStorageSimple jFameEventStorage;

    public static void test(String[] args) throws IOException, InvalidStateException {
        log.trace("Main.test() ...");
        setUp();

        int period = Util.getIntPropertyDef("PutMessageWait", 100);
        while ( !messageListener.hasTransitionedToFinal() ) {
            int milliseconds = (int)Math.random()*period;
            try {
                log.trace("Main.test() Waiting " + milliseconds + " ms in state " + StateHandler.INSTANCE.getCurrentState().name);
                Thread.sleep(milliseconds);
                putMessage();
            } catch (InterruptedException e) {
                log.warn("Main.test() InterruptedException " + e.getMessage());
            } catch (InvalidStateException e) {
                log.warn("Main.test() InvalidStateException " + e.getMessage());
                throw new InvalidStateException(e.getMessage());
            }
        }
        log.trace("Main.test() ... exit");
    }

    public static void main(String[] args) throws IOException {
        log.trace("Main.main() ...");
        setUp();

        int period = Util.getIntPropertyDef("PutMessageWait", 100);
        boolean interrupted = false;
        while ( !interrupted && !messageListener.hasTransitionedToFinal() ) {
            try {
                Thread.sleep(period);
            } catch (InterruptedException e) {
                log.warn("Main.main() InterruptedException " + e.getMessage());
                interrupted = true;
            }
        }
        log.trace("Main.main() ... exit");
    }

    public static void setUp() throws IOException {
        log.trace("Main.setUp() ...");

        Util.setProperties("src/test/resources/state.properties");

        log.trace("Main.setUp() setting event storage.");
        jFameEventStorage = new JFameEventStorageSimple();
        StateHandler.INSTANCE.setCallback(jFameEventStorage);

        log.trace("Main.setUp() setting messageListener.");
        messageListener = new MessageListenerImpl( MessageSource.INSTANCE );

        log.trace("Main.setUp() setting StateHandler to Initial.");
        StateHandler.INSTANCE.setInitialState("Initial");

        log.trace("Main.setUp() starting messageListener.");
        messageListener.start();

        log.trace("Main.setUp() ... exit");
    }

    protected static int putMessage() throws InterruptedException, InvalidStateException {
        log.trace("Main.putMessage() ...");
        Message message = new Message(makeRandomState(), Util.prettyPrintDate(new java.util.Date()));
        int size = messageListener.putMessage(message);
        log.debug("Main.putMessage() messages in queue=" + size + ".");
        log.trace("Main.putMessage() " + message.getDestinationState().name + " " + message.getPayload());
        log.trace("Main.putMessage() ... exit");
        return size;
    }

    protected static StateDescriptor makeRandomState() throws InvalidStateException {
        log.trace("Main.makeRandomState() ...");
        int stateIndex = (int)Math.round( Math.random()*( StateDescriptorFactory.INSTANCE.Max() - 1) );
        StateDescriptor stateDescriptor =  StateDescriptorFactory.INSTANCE.get(stateIndex);
        log.trace("Main.makeRandomState() " + stateIndex + "->" + stateDescriptor.name);
        log.trace("Main.makeRandomState() ... exit");
        return stateDescriptor;
    }
}