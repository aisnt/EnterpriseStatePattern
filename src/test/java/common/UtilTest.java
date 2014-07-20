package common;

import exceptions.InvalidPropertyException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.Properties;
import java.util.UUID;

public class UtilTest {

    @Before
    public void setUp() throws Exception  {
        Util.setProperties("src/test/resources/state.properties");
    }

    @Test
    public void testSetProperties() throws Exception {
        Properties p = Util.setProperties("src/test/resources/state.properties");
        Assert.assertNotNull(p);
    }

    @Test
    public void testGetStringProperty() throws Exception {
        Assert.assertTrue(Util.getStringProperty("FileNameValidTransitionTable").contains("src/main/resources/transitions.table"));
    }

    @Test
    public void testGetIntProperty() throws Exception {
        Assert.assertEquals(100, Util.getIntProperty("PutMessageWait"));
        Assert.assertEquals(50, Util.getIntProperty("GetMessageWait"));
        Assert.assertEquals(5, Util.getIntProperty("MaxQueue"));
    }

    @Test(expected = InvalidPropertyException.class)
    public void testGetIntPropertyEx() throws Exception {
        Assert.assertEquals(100, Util.getIntProperty("xx"));
    }

    @Test
    public void testGetIntPropertyDef() throws Exception {
        int s = Util.getIntPropertyDef("MaxQueue", 2);
        Assert.assertTrue(s>=0);
    }

    @Test
    public void testPrettyPrintDate() throws Exception {
        String s = Util.prettyPrintDate(new Date() );
        Assert.assertNotNull(s);
    }

    @Test
    public void testPrettyPrintUuid() throws Exception {
        Long s = Util.prettyPrintUuid(new UUID((long)2, (long)3));
        Assert.assertTrue(s>=0);
    }
}