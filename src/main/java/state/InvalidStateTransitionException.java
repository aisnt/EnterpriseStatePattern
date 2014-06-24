package state;

/**
 * Created by davidhislop on 2014/06/23.
 */
public class InvalidStateTransitionException extends Exception {
    InvalidStateTransitionException() {
        super();
    }
    InvalidStateTransitionException(String s) {
        super(s);
    }
}
