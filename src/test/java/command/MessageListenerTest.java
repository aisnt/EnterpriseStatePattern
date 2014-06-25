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
                log.trace("MessageListenerTest.testMain() Waiting a random period in state " + stateIOHandler.getCurrentState());

                int milliseconds = (int) (Math.random()*2000);//TODO Hardcoded hokum
                log.trace("MessageListenerTest.testMain() Sleep for " + milliseconds +"ms.");
                Thread.sleep(milliseconds);
                putMessage();
            } catch (InterruptedException e) {
                log.info("MessageListenerTest.testMain() InterruptedException " + e.getMessage());
            }
        }
    }

    protected void putMessage() throws InterruptedException {
        log.trace("MessageListenerTest.putMessage() start ...");
        Message message = new Message(makeRandomState(), new java.util.Date().toString());
        int size = messageListener.putMessage(message);
        log.info("MessageListenerTest.putMessage() messages in queue=" + size + ".");
        log.trace("MessageListenerTest.putMessage() " + message.getState() + " " + message.getPayload() );
    }

    private state.State.StateDescriptor makeRandomState() {
        log.trace("MessageListenerTest.makeRandomState() ");
        int stateIndex = (int) (Math.random()*5);//TODO Hardcoded hokum
        state.State.StateDescriptor stateDescriptor =  state.State.StateDescriptor.values()[stateIndex];//TODO expensive
        log.trace("MessageListenerTest.makeRandomState() " + stateIndex + "->"+stateDescriptor);
        return stateDescriptor;
    }
}
