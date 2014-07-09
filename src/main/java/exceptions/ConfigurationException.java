package exceptions;

/**
 * Created by david.hislop@korwe.com on 2014/06/26.
 */
public class ConfigurationException extends Exception {
    ConfigurationException() {
        super();
    }
    ConfigurationException(String s) {
        super(s);
    }
}

