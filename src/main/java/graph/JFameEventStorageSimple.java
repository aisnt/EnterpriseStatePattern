package graph;

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
public class JFameEventStorageSimple implements EventCallback {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private List messages = Collections.synchronizedList(new ArrayList());
    JGraphDynamicStateChangeViewer component = null;

    public JFameEventStorageSimple() {
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

            log.trace("JFameEventStorage.add() change colour " + event.to.name + ".");
            SwingUtilities.invokeLater(new Runnable() {
                                           public void run() {
                                               component.colourVertex(event.to.name);
                                           }
                                       }
                );
        }
    }
}
