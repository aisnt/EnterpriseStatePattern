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
public class State3 extends State {
    final Logger log = LoggerFactory.getLogger(this.getClass());

    public State3( ) {
        super( StateDescriptor.State3);
    }
    @Override
    public ResultWrapper<DTO> doTransition(Message message)  throws InvalidStateTransitionException, SendingException {
        log.trace("State3.sendMessage() From " + this.getState() + " to " + message.getState() +".");
        switch (message.getState()) {

            case State1: {
                ResultWrapper<DTO> dtos = transition(StateDescriptor.State1, message.getPayload());
                return dtos;
            }

            case Final: {
                ResultWrapper<DTO> dtos = transition(StateDescriptor.Final, message.getPayload());
                return dtos;
            }

            default: {
                throw new InvalidStateTransitionException("State3.sendMessage() Failed From " + this.getState() + " to " + message.getState() +".");
            }
        }
    }

    @Override
    protected ResultWrapper<DTO> sendMessage(String payload) {
        System.out.println("State3.sendMessage() Output -> " + payload);
        TransferApi transferApi = new TransferApiImpl();
        DTO dto =  transferApi.get(payload);
        ResultWrapper<DTO> dtos = new ResultWrapper<DTO>(dto);
        return dtos;
    }
}


