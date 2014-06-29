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
public class State2 extends State {
    final Logger log = LoggerFactory.getLogger(this.getClass());

    public State2( ) {
        super( StateDescriptor.State2);
    }
    @Override
    public ResultWrapper<DTO> doTransition(Message message)  throws InvalidStateTransitionException, SendingException {
        log.trace("State2.sendMessage() From " + this.getState() + " to " + message.getState() +".");
        switch (message.getState()) {
            case State3: {
                ResultWrapper<DTO> dtos = transition(StateDescriptor.State3, message.getPayload());
                return dtos;
            }

            default: {
                throw new InvalidStateTransitionException("State2.sendMessage() Failed from " + this.getState() + " to " + message.getState() +".");
            }
        }
    }

    @Override
    protected ResultWrapper<DTO> sendMessage(String payload) {
        System.out.println("State2.sendMessage() Output -> " + payload);
        TransferApi transferApi = new TransferApiImpl();
        DTO dto =  transferApi.get(payload);
        ResultWrapper<DTO> dtos = new ResultWrapper<DTO>(dto);
        return dtos;
    }
}
