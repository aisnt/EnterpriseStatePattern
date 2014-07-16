package graph;

import common.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import state.Event;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by david.hislop@korwe.com on 2014/07/13.
 */
public class JFameEventStorage extends Thread implements EventCallback {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    @Override
    public void run() {
        try {
            int period = Util.getIntPropertyDef("GetUIWait",100);
            while (true) {
                log.trace("JFameEventStorage.run() waiting messages.size()=" + messages.size() +".");

                Event e = null;
                try {
                    e = popEvent();
                } catch (InterruptedException e1) {
                    log.error("JFameEventStorage.run() InterruptedException " + e1.getMessage() + ".");
                }

                log.trace("JFameEventStorage.run() change colour " + e.to.name + ".");
                component.colourVertex(e.to.name);

                log.trace("JFameEventStorage.run() sleep "+period+".");
                sleep(period);
            }
        } catch (InterruptedException e) {
            log.error("JFameEventStorage.run() Exception " + e.getMessage() + ".");
        }
    }

    private List messages = Collections.synchronizedList(new ArrayList());
    JGraphDynamicStateChangeViewer component = null;

    public JFameEventStorage() {
        log.trace("JFameEventStorage.ctor().");
        component = new JGraphDynamicStateChangeViewer();

        component.init();

        JFrame frame = new JFrame();
        frame.getContentPane().add(component);
        frame.setTitle("State transitions");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    @Override
    public void add(Event event) {
        log.trace("JFameEventStorage.add() event " + event.toString() + ".");

        if (event != null && (event.success==null || event.success) ) {
            log.debug("JFameEventStorage.add() event " + event.toString() + ".");
            try {
                addEvent(event);
            } catch (InterruptedException e) {
                log.error("JFameEventStorage.add() failed " +e.getMessage() + ".");
            }
        }
    }

    private synchronized Event popEvent() throws InterruptedException {
        log.trace("JFameEventStorage.popEvent().");
        log.trace("JFameEventStorage.popEvent() isEventDispatchThread="+javax.swing.SwingUtilities.isEventDispatchThread());
        notify();

        while (messages.size() == 0 ) {
            log.debug("JFameEventStorage.popEvent() no messagesAvail. Wait.");
            wait();
        }

        Event event;
        log.trace("JFameEventStorage.popEvent() get event.");
        synchronized(messages) {
            event = (Event) messages.get(0);
            messages.remove(0);
        }

        log.debug("JFameEventStorage.popEvent() event " + event.toString() + ".");
        return event;
    }

    private synchronized int addEvent(Event event) throws InterruptedException {
        log.trace("JFameEventStorage.addEvent() ...");
        log.trace("JFameEventStorage.addEvent() isEventDispatchThread="+javax.swing.SwingUtilities.isEventDispatchThread());
        log.trace("JFameEventStorage.addEvent() event = " + event.toString() + ".");
        while ( (messages.size() >= 5) ) {
            log.trace("JFameEventStorage.addEvent() wait " + messages.size() + ".");
            wait();
        }
        log.trace("JFameEventStorage.addEvent().");
        synchronized(messages) {
            messages.add(event);
        }
        notify();
        return messages.size();
    }
}
