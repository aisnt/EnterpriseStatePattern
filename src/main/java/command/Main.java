package command;

import io.StateIOHandler;
import state.State;
import state.StateInitial;

/**
 * Created by davidhislop on 2014/06/22.
 */
public class Main {

    public static void main(String[] args) {
        // MessageListener messageListener = new MessageListenerImpl( new CommandImpl( new TransferApiImpl() ) );
        StateIOHandler stateIOHandler = new StateIOHandler(null);
        StateInitial state = new StateInitial(stateIOHandler);
        stateIOHandler.setState(state);
        MessageListener messageListener = new MessageListenerImpl( stateIOHandler );
        messageListener.start();

        while (stateIOHandler.getCurrentState() != State.StateDescriptor.Final) {
            try {
                System.out.println("main. Waiting 1 second in state " + state.getState());
                Thread.sleep(1000);
                messageListener.putMessage();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}