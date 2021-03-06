package common;

/**
 * Created by davidhislop on 2014/07/07.
 */
public class Random {
    static public int index = 0;
    private Random(Builder b) {
        this.index = b.index;
    }

    public static class Builder {
        public int index = 0;
        public Builder index(int index) {
            this.index = index;
            return this;
        }

        public Random build() {
            return new Random(this);
        }
    }

    public static double random() {
        //random (0,1)
        return Math.random();
    }

    //random [lower, upper]
    public static int random(int lower, int upper) {
        //Return pre-cooked random numbers for testing
        if (index > 0) {
            if ( index < fakeRandomVars.length )  {
                return fakeRandomVars[index++];
            }
            else {
                return 0;
            }
        }

        int rand = (int)Math.round(Random.random((double)lower, (double)upper));
        return rand;
    }

    //Return pre-cooked random numbers for testing
    public static int fakeRandom() {
        if ( (index > 0) && ( index < fakeRandomVars.length ) ) {
            return fakeRandomVars[index++];
        }

        return 0;
    }

    //random (lower, upper)
    public static double random(double lower, double upper) {
        double r = Math.random();
        double rand = r * ( upper - lower ) + lower;
        return rand;
    }

    final static int[] fakeRandomVars = {2, 3, 4, 2, 3, 1, 2, 3, 4};
}
