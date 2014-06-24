package command;

import common.Message;

/**
 * Created by davidhislop on 2014/06/21.
 */
public interface Command {
    public void handleMessage(Message message);
}
