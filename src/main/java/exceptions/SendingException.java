package exceptions;

/**
 * Created by david.hislop@korwe.com on 2014/06/27.
 *
 */
public class SendingException extends Exception {
    public SendingException() {
        super();
    }
    public SendingException(String s) {
        super(s);
    }
}
