package exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by davidhislop on 2014/07/18.
 */
public class InvalidPropertyException extends RuntimeException {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    public InvalidPropertyException() {
        super();
        log.error("InvalidPropertyException "+ this.getMessage());
    }

    public InvalidPropertyException(String s) {
        super(s);
        log.error("InvalidPropertyException (" + s + ") "+ this.getMessage());
    }
}
