package command;

import common.Message;
import common.Random;
import common.Util;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import state.*;

import java.util.Date;
import java.util.Iterator;

import static org.junit.Assert.*;

/**
 * Created by david.hislop@korwe.com on 2014/06/21.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MessageListenerTest  {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    MessageListenerImpl messageListener = null;

    @Before
    public void setUp() throws Exception {
        log.info("MessageListenerTest.setUp() ...");
        Util.setProperties("src/test/resources/state.properties");

        MessageSource.INSTANCE.reset();
        StateHandler.INSTANCE.reset();
        Util.reset();

        messageListener = new MessageListenerImpl( MessageSource.INSTANCE );

        log.trace("MessageListenerTest.setUp() start");
        messageListener.start();

        //Clear RNG
        new Random.Builder().index(0).build();
        log.info("MessageListenerTest.setUp() ... end");
    }

    //Different initial state
    @Test
    public void testListenerState0Initial() throws Exception {
        log.trace("MessageListenerTest.testListenerState0Initial() ...");

        String state = "Initial";
        if (!StateHandler.INSTANCE.setInitialState(state)) {
            log.error("MessageListenerTest.testListenerState0Initial() setState to " + state + "failed.");
            fail();
        }

        int index = 3;
        new Random.Builder().index(index).build();

        int period = Util.getIntProperty("PutMessageWait");
        int milliseconds = period / 2;
        log.trace("MessageListenerTest.testListenerState0Initial() period = " + period + ".");
        while (!doRun()) {
            try {
                log.trace("MessageListenerTest.testListenerState0Initial() Waiting " + milliseconds + " ms in state " + StateHandler.INSTANCE.getCurrentState().name);
                Thread.sleep(milliseconds);
                putMessage();
            } catch (InterruptedException e) {
                log.warn("MessageListenerTest.testListenerState0Initial() InterruptedException " + e.getMessage());
            }
        }
        check();

        Thread.sleep(5000);
        log.trace("MessageListenerTest.testListenerState0Initial() ...end");
    }

    //Different initial state
    @Test
    public void testListenerState1() throws Exception {
        log.trace("MessageListenerTest.testListenerState1() ...");

        String state = "State1";
        if (!StateHandler.INSTANCE.setInitialState(state)) {
            log.error("MessageListenerTest.testListenerState1() setState to " + state + "failed.");
            fail();
        }

        int index = 3;
        new Random.Builder().index(index).build();

        int period = Util.getIntProperty("PutMessageWait");
        int milliseconds = period / 2;
        log.trace("MessageListenerTest.testListenerState1() period = " + period + ".");
        while (!doRun()) {
            try {
                log.trace("MessageListenerTest.testListenerState1() Waiting " + milliseconds + " ms in state " + StateHandler.INSTANCE.getCurrentState().name);
                Thread.sleep(milliseconds);
                putMessage();
            } catch (InterruptedException e) {
                log.warn("MessageListenerTest.testListenerState1() InterruptedException " + e.getMessage());
            }
        }
        check();

        Thread.sleep(5000);
        log.trace("MessageListenerTest.testListenerState1() ...end");
    }

    //Faked Random states
    @Test
    public void testListenerState2() throws Exception {
        log.trace("MessageListenerTest.testListenerState2() ...");

        String state = "State2";
        if (!StateHandler.INSTANCE.setInitialState(state)) {
            log.error("MessageListenerTest.testListenerState2() setState to " + state + "failed.");
            fail();
        }

        int index = 3;
        new Random.Builder().index(index).build();

        int period = Util.getIntProperty("PutMessageWait");
        int milliseconds = period / 2;
        log.trace("MessageListenerTest.testListenerState2() period = " + period + ".");
        while (!doRun()) {
            try {
                log.trace("MessageListenerTest.testListenerState2() Waiting " + milliseconds + " ms in state " + StateHandler.INSTANCE.getCurrentState().name);
                Thread.sleep(milliseconds);
                putMessage();
            } catch (InterruptedException e) {
                log.warn("MessageListenerTest.testListenerState2() InterruptedException " + e.getMessage());
            }
        }
        check();

        Thread.sleep(5000);
        log.trace("MessageListenerTest.testListenerState2() ...end");
    }

    //Different initial state
    @Test
    public void testListenerState3() throws Exception {
        log.trace("MessageListenerTest.testListenerState3() ...");

        String state = "State3";
        if (!StateHandler.INSTANCE.setInitialState(state)) {
            log.error("MessageListenerTest.testListenerState3() setState to " + state + "failed.");
            fail();
        }

        int index = 3;
        new Random.Builder().index(index).build();

        int period = Util.getIntProperty("PutMessageWait");
        int milliseconds = period / 2;
        log.trace("MessageListenerTest.testListenerState3() period = " + period + ".");
        while (!doRun()) {
            try {
                log.trace("MessageListenerTest.testListenerState3() Waiting " + milliseconds + " ms in state " + StateHandler.INSTANCE.getCurrentState().name);
                Thread.sleep(milliseconds);
                putMessage();
            } catch (InterruptedException e) {
                log.warn("MessageListenerTest.testListenerState3() InterruptedException " + e.getMessage());
            }
        }
        check();

        Thread.sleep(5000);
        log.trace("MessageListenerTest.testListenerState3() ...end");
    }

    //Different initial state
    @Test
    public void testListenerState4Final() throws Exception {
        log.trace("MessageListenerTest.testListenerState4Final() ...");

        String state = "Final";
        if (!StateHandler.INSTANCE.setInitialState(state)) {
            log.error("MessageListenerTest.testListenerState4Final() setState to " + state + "failed.");
            fail();
        }

        int index = 3;
        new Random.Builder().index(index).build();

        int period = Util.getIntProperty("PutMessageWait");
        log.trace("MessageListenerTest.testListenerState4Final() period = " + period + ".");
        while (!doRun()) {
            try {
                int milliseconds = period / 2;
                log.trace("MessageListenerTest.testListenerState4Final() Waiting " + milliseconds + " ms in state " + StateHandler.INSTANCE.getCurrentState().name);
                Thread.sleep(milliseconds);
                putMessage();
            } catch (InterruptedException e) {
                log.warn("MessageListenerTest.testListenerState4Final() InterruptedException " + e.getMessage());
            }
        }
        check();

        Thread.sleep(5000);
        log.trace("MessageListenerTest.testListenerState4Final() ...end");
    }

    //The whole shebang
    @Test
    public void testListenerStateRandom() throws Exception {
        log.trace("MessageListenerTest.testListenerStateRandom() ...");

        StateDescriptor stateDescriptor = makeRandomState();
        if (!StateHandler.INSTANCE.setInitialState(stateDescriptor.name)) {
            log.error("MessageListenerTest.testListenerStateRandom() setState to " + stateDescriptor.name + "failed.");
            fail();
        }

        int period = Util.getIntProperty("PutMessageWait");
        log.trace("MessageListenerTest.testListenerStateRandom() period = " + period + ". Enter while loop.");
        while (!doRun()) {
            try {
                int milliseconds = Random.random(0, period);
                log.trace("MessageListenerTest.testListenerStateRandom() Waiting " + milliseconds + " ms in state " + StateHandler.INSTANCE.getCurrentState().name);
                Thread.sleep(milliseconds);
                putMessage();
            } catch (InterruptedException e) {
                log.warn("MessageListenerTest.testListenerStateRandom() InterruptedException " + e.getMessage());
            }
        }
        log.trace("MessageListenerTest.testListenerStateRandom() exiting while loop. Printing results.");
        check();

        Thread.sleep(5000);
        log.trace("MessageListenerTest.testListenerStateRandom() ... end");
    }

    private void check() {
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
        log.trace("MessageListenerTest.testMessagesAvail() start ...");
        boolean messagesAvail = messageListener.messagesAvail();
        Assert.assertFalse(messagesAvail);
        log.trace("MessageListenerTest.testMessagesAvail() ... end");
    }

    protected void putMessage() throws InterruptedException {
        log.trace("MessageListenerTest.putMessage() start ...");
        Date date = new java.util.Date();
        Message message = new Message(makeRandomState(), Util.prettyPrintDate(date));
        int size = messageListener.putMessage(message);
        log.debug("MessageListenerTest.putMessage() messages in queue=" + size + ".");
        log.trace("MessageListenerTest.putMessage() name=" + message.getDestinationState().name + " payload=" + message.getPayload());
        log.trace("MessageListenerTest.putMessage() ... end");
    }

    private StateDescriptor makeRandomState() {
        log.trace("MessageListenerTest.makeRandomState() ...");

        int stateIndex = Random.random(0, StateDescriptorFactory.INSTANCE.Max()-1);

        StateDescriptor stateDescriptor = null;
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

    private boolean doRun() {
        return messageListener.hasTransitionedToFinal();
    }

    @Test
    public void hasTransitionedToFinalTest() {
        log.trace("MessageListenerTest.hasTransitionedToFinalTest() start ...");
        boolean b = messageListener.hasTransitionedToFinal();
        assertFalse(b);
        log.trace("MessageListenerTest.hasTransitionedToFinalTest() ... end");
    }
}
