package common;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class RandomTest {

    @Before
    public void setUp() throws Exception {
        new Random.Builder().index( 0 ).build();
    }

    @Test
    public void testRandomExistence() throws Exception {
        double r = Random.random();
        assertNotNull(r);
    }

    @Test
    public void testRandomDoubleRanged() throws Exception {
        double lower = 3.2;
        double upper = 13.2;
        for (int i = 1; i<100; ++i) {
            double r = Random.random(lower, upper);
            assertTrue(r>lower);
            assertTrue(r<upper);
        }
    }

    @Test
    public void testRandomIntRanged() throws Exception {
        int lower = 3;
        int upper = 13;
        boolean upperBound = false;
        boolean lowerBound = false;
        while (!(upperBound && lowerBound) ) {
            int r = Random.random(lower, upper);
            assertTrue(r >= lower);
            assertTrue(r <= upper);
            if (r == lower) {
                lowerBound = true;
            }

            if (r == upper) {
                upperBound = true;
            }
        }
    }

    final static int[] fakeRandomVars = {2, 3, 4, 2, 3, 1, 2, 3, 4};
    @Test
    public void testFakeRandom() throws Exception {
        int index = 2;
        int r = new Random.Builder().index( index ).build().fakeRandom();
        assertTrue(r == fakeRandomVars[index]);
        for (int i = index+1; i < index+4; ++i) {
            r = Random.fakeRandom();
            assertTrue(r == fakeRandomVars[i]);
        }
    }
}