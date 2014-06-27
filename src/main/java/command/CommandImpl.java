package command;

import common.Message;
import io.Event;

/**
 * Created by david.hislop@korwe.com on 2014/06/21.
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

    public void onMessage(Message message) {
        String msisdn = parseMessage(message);
        DTO dto = transferApi.get(msisdn);
    }

    public Event getLastEvent() {
        return null;
    }
}
