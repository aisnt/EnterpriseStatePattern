package command;

import common.Message;
import io.Event;

/**
 * Created by david.hislop@korwe.com on 2014/06/21.
 */
public interface Command {
    public void onMessage(Message message);
    public Event getLastEvent();
}
