package state.dynamic;

import org.junit.Assert;
import org.junit.Test;
import state.StateDescriptor;

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
        int col = StateDescriptor.Max.ordinal();
        int row = 0;
        new Node(col, row);
        Assert.fail();
    }

    @Test
    public void testNumericCtor() {
        int col = StateDescriptor.Initial.ordinal();
        int row = StateDescriptor.State2.ordinal();
        Node node = null;
        try {
            node = new Node(col, row);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
        Assert.assertNotNull( node );
    }

    @Test
    public void testStringCtor() {
        String s1 = StateDescriptor.Initial.name();
        String s2 = StateDescriptor.Final.name();
        Node node = new Node(s1, s2, true);
        Assert.assertNotNull( node );
    }

    @Test(expected=IllegalArgumentException.class)
    public void testStringCtorFail() {
        String s1 = "nonsense";
        String s2 = StateDescriptor.Final.name();
        new Node(s1, s2, true);
        Assert.fail();
    }
}