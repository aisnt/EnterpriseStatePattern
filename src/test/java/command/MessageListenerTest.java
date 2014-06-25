package command;

import common.Message;
import io.StateIOHandler;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import state.State;
import state.StateInitial;

/**
 * Created by david.hislop@korwe.com on 2014/06/21.
 */
public class MessageListenerTest  {
    final Logger log = LoggerFactory.getLogger(this.getClass());

    MessageListenerImpl messageListener;
    StateIOHandler stateIOHandler ;
    @Before
    public void setUp() throws Exception {
        log.info("MessageListenerTest.setUp()");
        // MessageListener messageListener = new MessageListenerImpl( new CommandImpl( new TransferApiImpl() ) );
        stateIOHandler = new StateIOHandler(null);
        StateInitial state = new StateInitial(stateIOHandler); //TODO go from any state
        stateIOHandler.setState(state);
        messageListener = new MessageListenerImpl( stateIOHandler );
        messageListener.start();
    }

    @Test
    public void testMain() throws Exception {
        log.trace("MessageListenerTest.testMain()");
        while (stateIOHandler.getCurrentState() != State.StateDescriptor.Final) {
            try {
                log.trace("MessageListenerTest.testMain() Waiting 100 milli-seconds in state " + stateIOHandler.getCurrentState());
                Thread.sleep(100);
                putMessage();
            } catch (InterruptedException e) {
                log.info("InterruptedException " + e.getMessage());
            }
        }
    }

    protected void putMessage() throws InterruptedException {
        log.trace("MessageListenerTest.putMessage() start ...");
        Message message = new Message(makeRandomState(), new java.util.Date().toString());
        int size = messageListener.putMessage(message);
        log.trace("MessageListenerTest.putMessage() " + message.getState() + " " + message.getPayload() );
    }

    private state.State.StateDescriptor makeRandomState() {
        log.trace("MessageListenerTest.makeRandomState() ");
        int i = (int) (Math.random()*5);//TODO Hardcoded hokum
        state.State.StateDescriptor sd =  state.State.StateDescriptor.values()[i];//TODO expensive
        log.trace("MessageListenerTest.makeRandomState() " + i + "->"+sd);
        return sd;
    }
}
