package state;

import command.DataTransferObject;
import command.ResultWrapper;
import common.Message;
import common.Util;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

public class StateHandlerTest {

    @Before
    public void setup() throws Exception {

        Util.setProperties("src/test/resources/state.properties");


        StateHandler.INSTANCE.addFailedEvent(StateDescriptorFactory.INSTANCE.get("Initial"), StateDescriptorFactory.INSTANCE.get("Initial"));
        StateHandler.INSTANCE.setInitialState("Initial");
        StateHandler.INSTANCE.addSuccessEvent(StateDescriptorFactory.INSTANCE.get("Initial"), StateDescriptorFactory.INSTANCE.get("Initial"), new UUID((long)1, (long)2));
        //MessageSource.INSTANCE.reset();
        //StateHandler.INSTANCE.reset();
        //Util.reset();
    }

    @Test
    public void testSetInitialState() throws Exception {
        boolean test = StateHandler.INSTANCE.setInitialState("Initial");
        Assert.assertTrue(test);
    }

    @Test
    public void testAddFailedEvent() throws Exception {
        boolean test = StateHandler.INSTANCE.addFailedEvent(StateDescriptorFactory.INSTANCE.get("Initial"), StateDescriptorFactory.INSTANCE.get("Initial"));
        Assert.assertTrue(test);
    }

    @Test
    public void testAddSuccessEvent() throws Exception {
        boolean test = StateHandler.INSTANCE.addSuccessEvent(StateDescriptorFactory.INSTANCE.get("Initial"), StateDescriptorFactory.INSTANCE.get("Initial"), new UUID((long)1, (long)2));
        Assert.assertTrue(test);
    }

    @Test
    public void testAddInitialEvent() throws Exception {
        boolean test = StateHandler.INSTANCE.addInitialEvent(StateDescriptorFactory.INSTANCE.get("State1"));
        Assert.assertTrue(test);
    }

    @Test
    public void testGetLastEvent() throws Exception {
        Event test = StateHandler.INSTANCE.getLastEvent();
        Assert.assertNotNull(test);
    }

    @Test
    public void testGetLastSuccessfulEvent() throws Exception {
        Event test = StateHandler.INSTANCE.getLastSuccessfulEvent();
        Assert.assertNotNull(test);
    }

    @Test
    public void testGetLastNonFailedEvent() throws Exception {
        Event test = StateHandler.INSTANCE.getLastNonFailedEvent();
        Assert.assertNotNull(test);
    }

    @Test
    public void testChangeCurrentState() throws Exception {
        boolean test = StateHandler.INSTANCE.changeCurrentState(State.create(StateDescriptorFactory.INSTANCE.get("Initial")), new UUID((long)(1), (long)3));
        Assert.assertTrue(test);
    }

    @Test
    public void testGetCurrentState() throws Exception {
        StateDescriptor test = StateHandler.INSTANCE.getCurrentState();
        Assert.assertNotNull(test);
    }

    @Test
    public void testDoTransition() throws Exception {
        ResultWrapper<DataTransferObject> test = StateHandler.INSTANCE.doTransition(new Message(StateDescriptorFactory.INSTANCE.get("State1"), ""));
        Assert.assertTrue(test.get().getUuid() != null);
    }
}