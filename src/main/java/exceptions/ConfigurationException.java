package exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by david.hislop@korwe.com on 2014/06/26.
 */
public class ConfigurationException extends Exception {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    public ConfigurationException() {
        super();
        log.error("ConfigurationException "+ this.getMessage());
    }

    public ConfigurationException(String s) {
        super(s);
        log.error("ConfigurationException (" + s + ") "+ this.getMessage());
    }
}

