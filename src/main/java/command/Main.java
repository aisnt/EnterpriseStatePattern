package command;

import common.Message;
import common.Util;
import io.MessageSource;
import io.StateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import state.State;

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
        while (StateHandler.INSTANCE.getCurrentState() != State.StateDescriptor.Final) {
            try {
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

        //Start from any state
        StateHandler.INSTANCE.setState(State.create(State.StateDescriptor.State2));
        messageListener = new MessageListenerImpl( MessageSource.INSTANCE );
        messageListener.start();
    }

    protected static void putMessage() throws InterruptedException {
        log.trace("Main.putMessage() start ...");
        Message message = new Message(makeRandomState(), new java.util.Date().toString());
        int size = messageListener.putMessage(message);
        log.debug("Main.putMessage() messages in queue=" + size + ".");
        log.trace("Main.putMessage() " + message.getState() + " " + message.getPayload() );
    }

    private static state.State.StateDescriptor makeRandomState() {
        log.trace("Main.makeRandomState() ");
        int stateIndex = (int) (Math.random()* State.StateDescriptor.Max.ordinal());
        state.State.StateDescriptor stateDescriptor =  state.State.StateDescriptor.values()[stateIndex]; //Expensive
        log.trace("Main.makeRandomState() " + stateIndex + "->" + stateDescriptor);
        return stateDescriptor;
    }
}