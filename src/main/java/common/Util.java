package common;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

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

    public static String prettyPrintDate(Date date) {
        //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");
        String sDate = sdf.format(date);
        return sDate;
    }

    private static Map<UUID, Long> map = new HashMap<>();
    public static Long prettyPrintUuid(UUID uuid) {
        if (uuid == null) {
            return (long)-1;
        }
        if (map.containsKey(uuid)) {
            return map.get(uuid);
        }
        else {
            long i = (long)map.size();
            map.put(uuid, i);
            return i;
        }
    }
}
