package command;

import io.StateIOHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import state.State;
import state.StateInitial;

/**
 * Created by david.hislop@korwe.com on 2014/06/22.
 */
public class Main {
    final static Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        // MessageListener messageListener = new MessageListenerImpl( new CommandImpl( new TransferApiImpl() ) );
        StateIOHandler stateIOHandler = new StateIOHandler(null);
        StateInitial state = new StateInitial(stateIOHandler);
        stateIOHandler.setState(state);
        MessageListener messageListener = new MessageListenerImpl( stateIOHandler );
        messageListener.start();

        while (stateIOHandler.getCurrentState() != State.StateDescriptor.Final) {
            try {
                log.trace("main. Waiting 1 second in state " + state.getState());
                Thread.sleep(100);
                messageListener.putMessage();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}