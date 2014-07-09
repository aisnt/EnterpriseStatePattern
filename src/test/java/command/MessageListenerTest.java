package command;

import common.Message;
import common.Random;
import common.Util;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import state.Event;
import state.MessageSource;
import state.StateDescriptorFactory;
import state.StateHandler;

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

        //TODO Start from any state
        String state = "State2";
        if (!StateHandler.INSTANCE.setInitialState(state)) {
            log.error("MessageListenerTest.setUp() setState to " + state + "failed.");
            fail();
        }
        messageListener = new MessageListenerImpl( MessageSource.INSTANCE );
        log.trace("MessageListenerTest.setUp() start");
        messageListener.start();
    }

    @Test
    public void testListener() throws Exception {
        log.trace("MessageListenerTest.testListener()");
        int period = Util.getIntProperty("PutMessageWait");
        log.trace("MessageListenerTest.testListener() period = " + period + ".");
        while ( !StateDescriptorFactory.INSTANCE.isFinal(StateHandler.INSTANCE.getCurrentState())) {
            try {
                int milliseconds = Random.random(0, period);
                log.trace("MessageListenerTest.testListener() Waiting " + milliseconds + " ms in state " + StateHandler.INSTANCE.getCurrentState().name );
                Thread.sleep(milliseconds);
                putMessage();
            } catch (InterruptedException e) {
                log.warn("MessageListenerTest.testListener() InterruptedException " + e.getMessage());
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
        }
        assertNotNull("EventOld not Null", eventOld);
        assertTrue(StateDescriptorFactory.INSTANCE.isFinal(eventOld.to));
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
        log.trace("MessageListenerTest.putMessage() name=" + message.getState().name + " payload=" + message.getPayload());
    }

    private StateDescriptorFactory.StateDescriptor makeRandomState() {
        log.trace("MessageListenerTest.makeRandomState() ");
        int stateIndex = Random.random(0, StateDescriptorFactory.INSTANCE.Max()-1);
        StateDescriptorFactory.StateDescriptor stateDescriptor = null;
        try {
            stateDescriptor = StateDescriptorFactory.INSTANCE.get(stateIndex);
        }
        catch (Exception ex) {
            log.error("MessageListenerTest.makeRandomState() ", ex);
        }
        log.trace("MessageListenerTest.makeRandomState() " + stateIndex + "->" + stateDescriptor.name);
        return stateDescriptor;
    }
}
