package command;

import common.Util;

import java.util.Date;
import java.util.UUID;

/**
 * Created by david.hislop@korwe.com on 2014/06/21.
 */
public class DataTransferObject {

    private UUID uuid = UUID.randomUUID() ;
    String something;
    String somethingElse;
    Date date = new Date();
    @Override
    public String toString() {
        String result = null;
        if (something != null) result = "something: " + something;
        if (somethingElse != null) result += " somethingElse: " + somethingElse ;
        if (result == null) result = "<Empty>";
        result += " date=" + Util.prettyPrintDate(date) + " uuid=$" + Util.prettyPrintUuid(uuid);

        return result;
    }

    public UUID getUuid() {
        return uuid;
    }
}
