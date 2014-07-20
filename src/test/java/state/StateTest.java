package state;

import command.DataTransferObject;
import command.ResultWrapper;
import common.Message;
import common.Util;
import exceptions.InvalidStateTransitionException;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

public class StateTest {


    @Before
    public void setup() throws Exception {
        Util.setProperties("src/test/resources/state.properties");
    }

    @Test
    public void testCreate() throws Exception {
        State state = State.create(StateDescriptorFactory.INSTANCE.get("Initial"));
        Assert.assertTrue(state.getState().name.contains("Initial"));
    }

    @Test
    public void testDoTransition() throws Exception {
        StateDescriptor currentState = StateHandler.INSTANCE.getCurrentState();
        State state = State.create(StateDescriptorFactory.INSTANCE.get(currentState.name));
        StateDescriptor stateDescriptor =  StateDescriptorFactory.INSTANCE.get(2);
        ResultWrapper<DataTransferObject> i = state.doTransition(new Message(stateDescriptor, ""));
        Assert.assertNotNull(i);
    }

    @Test
    public void testValidatePolicy() throws Exception {
        State state = State.create(StateDescriptorFactory.INSTANCE.get("Initial"));
        StateDescriptor stateDescriptor1 =  StateDescriptorFactory.INSTANCE.get(0);
        StateDescriptor stateDescriptor2 =  StateDescriptorFactory.INSTANCE.get(1);
        //TODO no Sanity check here
        Boolean i = state.validatePolicy(stateDescriptor1, stateDescriptor2);
        Assert.assertTrue(i);
    }

    @Test(expected = InvalidStateTransitionException.class )
    public void testTransitionMealyEx() throws Exception {
        State state = State.create(StateDescriptorFactory.INSTANCE.get("Initial"));
        StateDescriptor stateDescriptor =  StateDescriptorFactory.INSTANCE.get(2);
        ResultWrapper<DataTransferObject> i = state.transitionMealy(stateDescriptor, "");
        Assert.assertTrue(i.get().getUuid()!=null);
    }

    @Test
    public void testTransitionMealy() throws Exception {
        State state = State.create(StateDescriptorFactory.INSTANCE.get("State2"));
        StateDescriptor stateDescriptor =  StateDescriptorFactory.INSTANCE.get(3);
        ResultWrapper<DataTransferObject> i = state.transitionMealy(stateDescriptor, "");
        Assert.assertTrue(i.get().getUuid()!=null);
    }

    //TODO stateful
    @Test
    public void testTransitionMoore() throws Exception {
        State state = State.create(StateDescriptorFactory.INSTANCE.get("State3"));
        StateDescriptor stateDescriptor =  StateDescriptorFactory.INSTANCE.get(4);
        ResultWrapper<DataTransferObject> i = state.transitionMoore(stateDescriptor, "");
        Assert.assertTrue(i.get().getUuid()!=null);
    }

    @Test(expected = InvalidStateTransitionException.class)
    public void testTransitionMooreEx() throws Exception {
        State state = State.create(StateDescriptorFactory.INSTANCE.get("Initial"));
        StateDescriptor stateDescriptor =  StateDescriptorFactory.INSTANCE.get(2);
        ResultWrapper<DataTransferObject> i = state.transitionMoore(stateDescriptor, "");
        Assert.assertTrue(i.get().getUuid()!=null);
    }

    @Test
    public void testSendMessage() throws Exception {
        State state = State.create(StateDescriptorFactory.INSTANCE.get("Initial"));
        StateDescriptor stateDescriptor =  StateDescriptorFactory.INSTANCE.get(1);
        ResultWrapper<DataTransferObject> i = state.sendMessage("");
        Assert.assertTrue(i.get().getUuid()!=null);
    }
}