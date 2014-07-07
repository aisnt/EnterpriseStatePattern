package state.dynamic;

/**
 * Created by davidhislop on 2014/07/07.
 */
public class InvalidStateException extends Exception {
    public InvalidStateException() {
        super();
    }
    public InvalidStateException(String s) {
        super(s);
    }
}
