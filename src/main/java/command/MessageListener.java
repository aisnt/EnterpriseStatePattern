package command;

import common.Message;

import java.util.Vector;

/**
 * Created by davidhislop on 2014/06/21.
 */
public abstract class MessageListener extends Thread {
    Command command;
    public MessageListener(Command command){
        System.out.println("MessageListener ctor");
        this.command = command;
    }

    protected void onMessage(Message message) {
        System.out.println("MessageListener onMessage for " + message.getState() + " state.");
        command.handleMessage( message);
    }

    @Override
    public void run() {
        System.out.println("MessageListener run");
        try {
            while (true) {
                System.out.println("MessageListener getMessage");
                getMessage();
                //sleep(5000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private Vector<Message> messages = new Vector();
    static final int MAXQUEUE = 5;

    protected synchronized void putMessage() throws InterruptedException {
        System.out.println("MessageListener.putMessage start ...");
        while (messages.size() == MAXQUEUE) {
            wait();
        }
        Message message = new Message(getRandomState(), new java.util.Date().toString());
        messages.addElement(message);
        System.out.println("MessageListener.putMessage " + message.getState() + " " + message.getPayload() );
        notify();
        //Later, when the necessary event happens, the thread that is running it calls notify() from a block synchronized on the same object.
    }

    private state.State.StateDescriptor getRandomState() {
        System.out.println("MessageListener.getRandomState ");
        int i = (int) (Math.random()*5);
        state.State.StateDescriptor sd =  state.State.StateDescriptor.values()[i];
        System.out.println("MessageListener.getRandomState " + i + " "+sd);
        return sd;
    }

    private synchronized Message getMessage() throws InterruptedException {
        System.out.println("MessageListener.getMessage start ...");
        notify();
        while (messages.size() == 0) {
            wait();//By executing wait() from a synchronized block, a thread gives up its hold on the lock and goes to sleep.
        }
        System.out.println("MessageListener.getMessage ... continuing ...");
        Message message = (Message)messages.firstElement();
        System.out.println("MessageListener.getMessage got  ..." + message.getState() + " state.");
        onMessage( message );
        messages.removeElement(message);
        return message;
    }
}
