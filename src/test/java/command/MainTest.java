package command;

import common.Message;
import common.Util;
import exceptions.InvalidStateException;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import state.StateDescriptor;

import java.io.IOException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

//@RunWith(MockitoJUnitRunner.class)
//@RunWith(PowerMockRunner.class)
//@PrepareForTest( MessageListenerImpl.class )
public class MainTest {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Test
    public void test() throws InterruptedException {
        log.trace("MainTest.test() ...");
        String[] args = null;
        try {
            Main.test(args);
        } catch (IOException e) {
            Assert.fail(e.getMessage());
        } catch (InvalidStateException e) {
            Assert.fail(e.getMessage());
        }

        Thread.sleep(5000);
        assertTrue(true);
        log.trace("MainTest.test() ... end");
    }

    @Test
    public void testMain() throws InterruptedException {
        log.trace("MainTest.testMain() ...");
        String[] args = null;
        Thread t = new Thread() {
            public void run() {
                log.trace("MainTest.testMain() Starting main.");
                try {
                    Main.main(args);
                } catch (IOException e) {
                    Assert.fail(e.getMessage());
                }
                log.trace("MainTest.testMain() Exiting main.");
            }
        };

        log.trace("MainTest.testMain() Starting thread");
        t.start();

        try {
            log.trace("MainTest.testMain() Initial sleep");
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            log.error("MainTest.testMain() Interrupted initial sleep");
            Assert.fail(e.getMessage());
        }

        log.trace("MainTest.testMain() Interrupting MessageListener ");
        MessageListener.currentThread().interrupt();

        assertTrue(true);
        log.trace("MainTest.testMain() ... end");
    }

    @Test
    public void testMakeRandomState() throws InvalidStateException, IOException {
        log.trace("MainTest.testMakeRandomState() ...");
        Util.setProperties("src/test/resources/state.properties");
        StateDescriptor s = Main.makeRandomState();
        assertNotNull(s);
        log.trace("MainTest.testMakeRandomState() ... end");
    }

    //TODO
    @Test
    @Ignore
    public void testPutMessage() throws InterruptedException, InvalidStateException, IOException {
        log.trace("MainTest.testPutMessage() ...");
        Util.setProperties("src/test/resources/state.properties");

       // MessageListenerImpl messageListener = Mockito.mock(MessageListenerImpl.class);

        PowerMockito.mockStatic(MessageListenerImpl.class);
        Message message = new Message(Main.makeRandomState(), "");
        //PowerMockito.when(MessageListenerImpl.putMessage(message)).thenReturn(3);

        //when(messageListener.putMessage(message)).thenReturn(3);

        int i = Main.putMessage();
        assertTrue(i>0);
        log.trace("MainTest.testPutMessage() ... end");
     }
}