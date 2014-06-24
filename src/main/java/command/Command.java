package command;

import common.Message;

/**
 * Created by david.hislop@korwe.com on 2014/06/21.
 */
public interface Command {
    public void onMessage(Message message);
}
