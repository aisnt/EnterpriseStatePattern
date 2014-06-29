package state;

import command.DTO;
import command.ResultWrapper;
import command.TransferApi;
import command.TransferApiImpl;
import common.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by david.hislop@korwe.com on 2014/06/23.
 */
public class StateFinal extends State {
    final Logger log = LoggerFactory.getLogger(this.getClass());

    public StateFinal( ) {
        super( StateDescriptor.Final);
    }
    @Override
    public ResultWrapper<DTO> doTransition(Message message)  throws InvalidStateTransitionException, SendingException{
        log.trace("StateFinal.sendMessage() From " + this.getState() + " to " + message.getState() +".");
        throw new InvalidStateTransitionException("StateFinal.sendMessage() Failed From " + this.getState() + " to " + message.getState() +".");
    }

    @Override
    protected ResultWrapper<DTO> sendMessage(String payload) {
        System.out.println("StateFinal.sendMessage() Output -> " + payload);
        TransferApi transferApi = new TransferApiImpl();
        DTO dto =  transferApi.get(payload);
        ResultWrapper<DTO> dtos = new ResultWrapper<DTO>(dto);
        return dtos;
    }
}
