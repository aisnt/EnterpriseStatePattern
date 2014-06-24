package command;

import common.Message;

/**
 * Created by davidhislop on 2014/06/21.
 */
public class CommandImpl implements Command {

    public CommandImpl(TransferApi transferApi) {
        this.transferApi = transferApi;
    }

    private TransferApi transferApi;

    private String parseMessage(Message message){
        return "";
    }

    @Override

    public void handleMessage(Message message) {
        String msisdn = parseMessage(message);
        DTO dto = transferApi.get(msisdn);
    }
}
