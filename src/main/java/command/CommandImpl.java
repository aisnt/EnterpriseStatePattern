package command;

import common.Message;

/**
 * Created by david.hislop@korwe.com on 2014/06/21.
 */
public class CommandImpl implements Command {

    public CommandImpl(TransferApi transferApi) {
        this.transferApi = transferApi;
    }

    private TransferApi transferApi;

    private String parseMessage(Message message){
        return message.getPayload();
    }

    @Override
    public void onMessage(Message message) {
        String arg = parseMessage(message);
        DTO dto = transferApi.get(arg);
    }
}
