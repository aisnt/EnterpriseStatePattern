package exceptions;

/**
 * Created by david.hislop@korwe.com on 2014/06/23.
 */
public class InvalidStateTransitionException extends Exception {
    public InvalidStateTransitionException() {
        super();
    }
    public InvalidStateTransitionException(String s) {
        super(s);
    }
}
