package command;

/**
 * Created by david.hislop@korwe.com on 2014/06/21.
 */
public class DTO {
    String something;
    String somethingElse;
    @Override
    public String toString() {
        String result = null;
        if (something != null) result = "something: " + something;
        if (somethingElse != null) result += " somethingElse: " + somethingElse ;
        if (result == null) result = "<Empty>";
        return result;
    }
}
