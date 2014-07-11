package exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by david.hislop@korwe.com on 2014/06/23.
 */
public class InvalidStateTransitionException extends Exception {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    public InvalidStateTransitionException() {
        super();
        log.error("InvalidStateTransitionException "+ this.getMessage());
    }

    public InvalidStateTransitionException(String s) {
        super(s);
        log.error("InvalidStateTransitionException (" + s + ") ");
    }
}
