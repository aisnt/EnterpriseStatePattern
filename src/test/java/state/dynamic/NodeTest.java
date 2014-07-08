package state.dynamic;

import org.junit.Assert;
import org.junit.Test;
import state.StateDescriptorX;

public class NodeTest {
    @Test
    public void testNumericCtorSimple() throws Exception {
        int col = 1;
        int row = 1;
        Node node = new Node(col, row);
        Assert.assertNotNull( node );
    }

    @Test(expected=InvalidStateException.class)
    public void testNumericCtorFail() throws InvalidStateException {
        int col = StateDescriptorX.INSTANCE.Max();
        int row = 0;
        new Node(col, row);
        Assert.fail();
    }

    @Test
    public void testNumericCtor() throws InvalidStateException {
        int col = StateDescriptorX.INSTANCE.get("Initial").ordinal;
        int row = StateDescriptorX.INSTANCE.get("State2").ordinal;
        Node node = null;
        try {
            node = new Node(col, row);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
        Assert.assertNotNull( node );
    }

    @Test
    public void testStringCtor() throws InvalidStateException {
        String s1 = StateDescriptorX.INSTANCE.get("Initial").name;
        String s2 = StateDescriptorX.INSTANCE.get("Initial").name;
        Node node = new Node(s1, s2, true);
        Assert.assertNotNull( node );
    }

    @Test(expected=InvalidStateException.class)
    public void testStringCtorFail() throws InvalidStateException  {
        String s1 = "nonsense";
        String s2 = StateDescriptorX.INSTANCE.get("Final").name;
        new Node(s1, s2, true);
        Assert.fail();
    }
}