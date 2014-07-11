package exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by davidhislop on 2014/07/07.
 */
public class InvalidStateException extends Exception {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    public InvalidStateException() {
        super();
        log.error("InvalidStateException "+ this.getMessage());
    }

    public InvalidStateException(String s) {
        super(s);
        log.error("InvalidStateException (" + s + ") ");
    }
}
