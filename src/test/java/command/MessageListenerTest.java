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

import java.util.Date;
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
        //StateDescriptorFactory.StateDescriptor stateDescriptor = makeRandomState();
        //if (!StateHandler.INSTANCE.setInitialState(stateDescriptor.name)) {
        String state = "State2";
        if (!StateHandler.INSTANCE.setInitialState(state)) {
            log.error("MessageListenerTest.setUp() setState to " + state + "failed.");
            fail();
        }
        messageListener = new MessageListenerImpl( MessageSource.INSTANCE );
        log.trace("MessageListenerTest.setUp() start");
        messageListener.start();

        //TODO
        int index = 2;
        new Random.Builder().index( index ).build();
    }

    @Test
    public void testListener() throws Exception {
        log.trace("MessageListenerTest.testListener()");
        int period = Util.getIntProperty("PutMessageWait");
        log.trace("MessageListenerTest.testListener() period = " + period + ".");
        while ( !StateDescriptorFactory.INSTANCE.isFinal(StateHandler.INSTANCE.getCurrentState())) {
            try {
                //TODO
                //int milliseconds = Random.random(0, period);
                int milliseconds = period/2;
                log.trace("MessageListenerTest.testListener() Waiting " + milliseconds + " ms in state " + StateHandler.INSTANCE.getCurrentState().name );
                Thread.sleep(milliseconds);
                putMessage();
            } catch (InterruptedException e) {
                log.warn("MessageListenerTest.testListener() InterruptedException " + e.getMessage());
            }
        }

        Iterator resultIterator =  MessageSource.INSTANCE.results.iterator();
        ResultWrapper<DataTransferObject> resultWrapper = null;
        while ( resultIterator.hasNext() ) {
            resultWrapper = (ResultWrapper<DataTransferObject>)resultIterator.next();
            assertNotNull("Wrapper Null", resultWrapper);
            assertNotNull("DTO Null", resultWrapper.t);
            System.out.println("Data object " + resultWrapper + ".");
        }

        Iterator eventIterator =  StateHandler.INSTANCE.events.iterator();
        Event event = null;
        while ( eventIterator.hasNext() ) {
            event = (Event)eventIterator.next();
            System.out.println("Event " + event + ".");
            assertNotNull("Event Null", event);
            assertNotNull("No to transition", event.to);
            if (event.uuid != null) {
                assertNotNull("No from transition", event.from);
            }
            assertNotNull("No to transition", event.date);
        }

        assertNotNull("EventOld not Null", event);
        assertTrue(StateDescriptorFactory.INSTANCE.isFinal(event.to));
    }

    @Test
    public void testMessagesAvail()   {
        boolean messagesAvail = messageListener.messagesAvail();
        Assert.assertFalse(messagesAvail);
    }

    protected void putMessage() throws InterruptedException {
        log.trace("MessageListenerTest.putMessage() start ...");
        Date date = new java.util.Date();
        Message message = new Message(makeRandomState(), Util.prettyPrintDate(date));
        int size = messageListener.putMessage(message);
        log.debug("MessageListenerTest.putMessage() messages in queue=" + size + ".");
        log.trace("MessageListenerTest.putMessage() name=" + message.getDestinationState().name + " payload=" + message.getPayload());
    }

    private StateDescriptorFactory.StateDescriptor makeRandomState() {
        log.trace("MessageListenerTest.makeRandomState() ");
        //TODO
        //int stateIndex = Random.random(0, StateDescriptorFactory.INSTANCE.Max()-1);
        int stateIndex = Random.fakeRandom();

        StateDescriptorFactory.StateDescriptor stateDescriptor = null;
        try {
            stateDescriptor = StateDescriptorFactory.INSTANCE.get(stateIndex);
        }
        catch (Exception ex) {
            log.error("MessageListenerTest.makeRandomState() ", ex);
            //TODO Hmmmm
        }
        log.trace("MessageListenerTest.makeRandomState() " + stateIndex + "->" + stateDescriptor.name);
        return stateDescriptor;
    }
}
