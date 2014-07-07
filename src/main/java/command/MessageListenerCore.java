package command;

import common.Message;

/**
 * Created by davidhislop on 2014/07/04.
 */
public class MessageListenerCore extends MessageListener  {
    public MessageListenerCore(Command command) {
        super(command);
    }

    @Override
    protected Message getMessage() throws InterruptedException {
        return null;
    }

    @Override
    protected boolean messagesAvail() {
        return false;
    }
}
