package state;

import common.Util;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class StateDescriptorFactoryTest {
    @Before
    public void setup() throws Exception {

        Util.setProperties("src/test/resources/state.properties");

        //MessageSource.INSTANCE.reset();
        //StateHandler.INSTANCE.reset();
        //Util.reset();
    }

    @Test
    public void testMax() throws Exception {
        int m = StateDescriptorFactory.INSTANCE.Max();
        Assert.assertTrue(m>=0);
    }

    @Test
    public void testGetInt() throws Exception {
        StateDescriptor sd = StateDescriptorFactory.INSTANCE.get(0);
        Assert.assertTrue(sd.name.contains("Initial"));
    }

    @Test
    public void testGetString() throws Exception {
        StateDescriptor sd =StateDescriptorFactory.INSTANCE.get("Initial");
        Assert.assertTrue(sd.name.contains("Initial"));
    }

    @Test
    public void testIsInitial() throws Exception {
        String stateName = "Initial";
        boolean test = StateDescriptorFactory.INSTANCE.isInitial(StateDescriptorFactory.INSTANCE.get(stateName));
        Assert.assertTrue(test);
    }

    @Test
    public void testIsFinal() throws Exception {
        String stateName = "Final";
        boolean test = StateDescriptorFactory.INSTANCE.isFinal(StateDescriptorFactory.INSTANCE.get(stateName));
        Assert.assertTrue(test);
    }
}