package command;

import common.Message;
import common.Random;
import common.Util;
import io.Event;
import io.MessageSource;
import io.StateHandler;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import state.State;

import java.util.Iterator;

import static org.junit.Assert.*;

/**
 * Created by david.hislop@korwe.com on 2014/06/21.
 */
public class MessageListenerTest  {
    final Logger log = LoggerFactory.getLogger(this.getClass());
    MessageListenerImpl messageListener = null;

    @Before
    public void setUp() throws Exception {
        log.info("MessageListenerTest.setUp()");
        Util.setProperties("src/test/resources/state.properties");

        //Start from any state
        if (!StateHandler.INSTANCE.setState(State.create(State.StateDescriptor.State2))) { //TODO parameterise
            log.error("setState failed.");
            fail();
        }
        messageListener = new MessageListenerImpl( MessageSource.INSTANCE );
        messageListener.start();
    }

    @Test
    public void testListener() throws Exception {
        log.trace("MessageListenerTest.testMain()");
        while (StateHandler.INSTANCE.getCurrentState() != State.StateDescriptor.Final) {
            try {
                int milliseconds = Random.random(0, Util.getIntProperty("PutMessageWait"));
                log.trace("MessageListenerTest.testMain() Waiting " + milliseconds + " ms in state " + StateHandler.INSTANCE.getCurrentState());
                Thread.sleep(milliseconds);
                putMessage();
            } catch (InterruptedException e) {
                log.warn("MessageListenerTest.testMain() InterruptedException " + e.getMessage());
            }
        }

        Iterator it =  MessageSource.INSTANCE.events.iterator();
        Iterator is =  MessageSource.INSTANCE.results.iterator() ;
        Event eventOld = null;
        while (it.hasNext() && is.hasNext() ) {
            Event event = (Event)it.next();
            ResultWrapper<DTO> rw = (ResultWrapper<DTO>)is.next();
            System.out.println(event + "  " + rw);
            assertNotNull("Wrapper Null", rw);
            assertNotNull("Event Null", event);
            assertNotNull("DTO Null", rw.t);
            assertNotNull("No to transition", event.to);
            assertNotNull("No from transition", event.from);
            assertNotNull("No to transition", event.date);
            eventOld = event;
        };
        assertEquals(eventOld.to, State.StateDescriptor.Final);
    }

    @Test
    public void testMessagesAvail()   {
        boolean b = messageListener.messagesAvail();
        Assert.assertFalse(b);
    }

    protected void putMessage() throws InterruptedException {
        log.trace("MessageListenerTest.putMessage() start ...");
        Message message = new Message(makeRandomState(), new java.util.Date().toString());
        int size = messageListener.putMessage(message);
        log.debug("MessageListenerTest.putMessage() messages in queue=" + size + ".");
        log.trace("MessageListenerTest.putMessage() " + message.getState() + " " + message.getPayload());
    }

    private state.State.StateDescriptor makeRandomState() {
        log.trace("MessageListenerTest.makeRandomState() ");
        int stateIndex = Random.random(0, State.StateDescriptor.Max.ordinal()-1);
        state.State.StateDescriptor stateDescriptor = null;
        try {
            stateDescriptor = State.StateDescriptor.getStateDescriptor(stateIndex);
        }
        catch (Exception ex) {
            log.error("MessageListenerTest.makeRandomState() ", ex);
        }
        log.trace("MessageListenerTest.makeRandomState() " + stateIndex + "->" + stateDescriptor);
        return stateDescriptor;
    }
}
