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
public class State1 extends State {
    final Logger log = LoggerFactory.getLogger(this.getClass());
    public State1( ) {
        super( StateDescriptor.State1);
    }

    /*
    * part 2: This is an implementation from State
    *
    * This is where the states are implictly wired.
    * TODO it would be nice to make it explicitly injected.
    */
    @Override
    public ResultWrapper<DTO> doTransition(Message message)  throws InvalidStateTransitionException, SendingException {
        log.trace("State1.sendMessage() From " + this.getState() + " to " + message.getState() +".");
        switch (message.getState()) {
            case State2: {
                ResultWrapper<DTO> dtos = transition(StateDescriptor.State2, message.getPayload());
                return dtos;
            }

            default: {
                throw new InvalidStateTransitionException("State1.sendMessage() No transition from " + this.getState() + " to " + message.getState() +".");
            }
        }
    }

    /*
    * part 5:  This is an implementation from State
    */
    @Override
    protected ResultWrapper<DTO> sendMessage(String payload) {
        System.out.println("State1.sendMessage() Output -> " + payload);
        TransferApi transferApi = new TransferApiImpl();
        DTO dto =  transferApi.get(payload);
        ResultWrapper<DTO> dtos = new ResultWrapper<DTO>(dto);
        return dtos;
    }
}
