package common;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by davidhislop on 2014/07/07.
 */
public class Util {
    static private Properties properties = new Properties();;
    public static Properties setProperties(String file) throws IOException {
        FileInputStream input = new FileInputStream(file);
        properties.load(input);
        input.close();
        return properties;
    }

    public static String getStringProperty(String name) throws IOException {
        return properties.getProperty(name);
    }

    public static int getIntProperty(String name) throws IOException {
        String var = properties.getProperty(name);
        return Integer.parseInt(var);
    }

    public static int getIntPropertyDef(String name, int def) {
        try {
            return getIntProperty( name);
        } catch (IOException ex) {
            return def;
        }
    }
}
