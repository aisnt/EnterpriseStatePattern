package command;

import common.Message;
import io.Event;
import io.MessageSource;
import io.StateHandler;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import state.State;

import java.util.Iterator;

/**
 * Created by david.hislop@korwe.com on 2014/06/21.
 */
public class MessageListenerTest  {
    final Logger log = LoggerFactory.getLogger(this.getClass());

    MessageListenerImpl messageListener;
    @Before
    public void setUp() throws Exception {
        log.info("MessageListenerTest.setUp()");

        //Start from any state
        StateHandler.INSTANCE.setState(State.create(State.StateDescriptor.State2));
        messageListener = new MessageListenerImpl( MessageSource.INSTANCE );
        messageListener.start();
    }

    @Test
    public void testMain() throws Exception {
        log.trace("MessageListenerTest.testMain()");
        while (StateHandler.INSTANCE.getCurrentState() != State.StateDescriptor.Final) {
            try {
                int milliseconds = (int) (Math.random()*500);// Hardcoded
                log.trace("MessageListenerTest.testMain() Waiting " + milliseconds + " ms in state " + StateHandler.INSTANCE.getCurrentState());
                Thread.sleep(milliseconds);
                putMessage();
            } catch (InterruptedException e) {
                log.warn("MessageListenerTest.testMain() InterruptedException " + e.getMessage());
            }
        }

        Iterator it =  MessageSource.INSTANCE.events.iterator();
        Iterator is =  MessageSource.INSTANCE.results.iterator() ;
        while (it.hasNext() && is.hasNext() ) {
            Event e = (Event)it.next();
            ResultWrapper<DTO> rw = (ResultWrapper<DTO>)is.next();
            System.out.println(e + "  " + rw);
        };
    }

    protected void putMessage() throws InterruptedException {
        log.trace("MessageListenerTest.putMessage() start ...");
        Message message = new Message(makeRandomState(), new java.util.Date().toString());
        int size = messageListener.putMessage(message);
        log.debug("MessageListenerTest.putMessage() messages in queue=" + size + ".");
        log.trace("MessageListenerTest.putMessage() " + message.getState() + " " + message.getPayload() );
    }

    private state.State.StateDescriptor makeRandomState() {
        log.trace("MessageListenerTest.makeRandomState() ");
        int stateIndex = (int) (Math.random()* State.StateDescriptor.Max.ordinal());
        state.State.StateDescriptor stateDescriptor =  state.State.StateDescriptor.values()[stateIndex]; //Expensive
        log.trace("MessageListenerTest.makeRandomState() " + stateIndex + "->" + stateDescriptor);
        return stateDescriptor;
    }
}
