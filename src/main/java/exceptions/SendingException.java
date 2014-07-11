package exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by david.hislop@korwe.com on 2014/06/27.
 *
 */
public class SendingException extends Exception {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    public SendingException() {
        super();
        log.error("SendingException "+ this.getMessage());
    }
    public SendingException(String s) {
        super(s);
        log.error("SendingException (" + s + ")");
    }
}
