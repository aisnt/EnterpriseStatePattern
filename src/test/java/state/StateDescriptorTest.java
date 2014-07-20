package state;

import org.junit.Assert;
import org.junit.Test;

public class StateDescriptorTest {

    @Test
    public void testStateDescriptor1() throws Exception {
        StateDescriptor sd = new StateDescriptor( "Initial", 1);
        Assert.assertTrue(sd.name.contains("Initial"));
        Assert.assertTrue(sd.stateType== StateDescriptorFactory.StateType.Initial);
    }

    @Test
    public void testStateDescriptor2() throws Exception {
        StateDescriptor sd = new StateDescriptor( "Final", 2);
        Assert.assertTrue(sd.name.contains("Final"));
        Assert.assertTrue(sd.stateType== StateDescriptorFactory.StateType.Final);
    }

    @Test
    public void testStateDescriptor3() throws Exception {
        StateDescriptor sd = new StateDescriptor( "State", 3);
        Assert.assertTrue(sd.name.contains("State"));
        Assert.assertTrue(sd.stateType== StateDescriptorFactory.StateType.Normal);
    }
}