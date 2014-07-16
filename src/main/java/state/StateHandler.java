package state;

import command.DataTransferObject;
import command.ResultWrapper;
import common.Message;
import exceptions.ConfigurationException;
import exceptions.InvalidStateException;
import exceptions.InvalidStateTransitionException;
import exceptions.SendingException;
import graph.JFameEventStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

/**
 * Created by david.hislop@korwe.com on 2014/06/26.
 */
public enum StateHandler {
    INSTANCE;

    private Logger log = LoggerFactory.getLogger(this.getClass());

    /*We need to use the derived object*/
    private State currentStateObject;

    StateHandler() {
        log.trace("StateHandler.ctor()");
        try {
            //TODO Assume we have one initial state
            currentStateObject = State.create(StateDescriptorFactory.INSTANCE.get("Initial"));
            log.info("StateHandler.ctor() Initialising to Initial");
        } catch(Exception e) {
            log.error("StateHandler.ctor() create Exception=", e);
            //TODO Hmm changing an exception into runtime.
            throw new ExceptionInInitializerError();
        }
    }

    private void addInitialEvent(StateDescriptorFactory.StateDescriptor state) throws ConfigurationException  {
        log.trace("StateHandler.setInitialState() ...");
        Event current = new Event( state );
        events.add(current);
    }

    public Boolean setInitialState(String stateName) {
        log.trace("StateHandler.setInitialState() ...");
        log.debug("StateHandler.setInitialState() Setting initial state to " + stateName + ".");

        try {
            State state = State.create(StateDescriptorFactory.INSTANCE.get(stateName));
            return setState( state, null );
        } catch (InvalidStateException e) {
            log.error("StateHandler.setInitialState() Exception=", e);
            return false;
        }
    }

    private Boolean setState(State state, UUID uuid) {
        log.trace("StateHandler.setState() ...");
        log.debug("StateHandler.setState() to " + state.getState().name + ".");
        if (state == null) {
            return false;
        }

        synchronized(stateLock) {
            StateDescriptorFactory.StateDescriptor currentState = currentStateObject.getState();
            log.debug("StateHandler.setState() before changeCurrentState from " + currentState.name + " to " + state.getState().name + ".");
            currentStateObject = state;

            try {
                if (uuid != null) {
                    addSuccessEvent(currentState, state.getState(), uuid);
                }
                else {
                    if (currentState.stateType == StateDescriptorFactory.StateType.Initial) {
                        addInitialEvent(state.getState());
                    }
                    else {
                        addFailedEvent(currentState, state.getState());
                    }
                }
            } catch (Exception e) {
                log.error("StateHandler.setState() Exception.", e);
                return false;
            }
        }
        return true;
    }

    private void addSuccessEvent(StateDescriptorFactory.StateDescriptor fromState, StateDescriptorFactory.StateDescriptor toState, UUID uuid) throws ConfigurationException {
        log.trace("StateHandler.addSuccessEvent() ...");
        Event current = new Event(fromState, toState, uuid );
        events.add(current);
    }

    protected boolean addFailedEvent(StateDescriptorFactory.StateDescriptor fromState, StateDescriptorFactory.StateDescriptor toState) {
        log.trace("StateHandler.addFailedEvent() ...");
        Event current = null;
        try {
            current = new Event( fromState, toState );
        } catch (ConfigurationException e) {
            log.error("StateHandler.addFailedEvent() ConfigurationException.");
            return false;
        }
        events.add(current);
        return true;
    }

    public Event getLastEvent() {
        log.trace("StateHandler.getLastEvent() ...");
        int index = events.size() - 1;
        if (index == -1) {
            return null;
        }
        return events.get(index);
    }

    public Event getLastSuccessfulEvent() {
        log.trace("StateHandler.getLastSuccessfulEvent() ...");
        int index = events.size() - 1;
        while (index >= 0) {
            try {
                Event event = events.get(index);
                if (event == null) {
                    log.error("StateHandler.getLastSuccessfulEvent() null event");
                }
                else {
                    if ((event.success != null) && (event.success == true)) {
                        return event;
                    }
                }
            } catch (Exception e) {
                log.error("StateHandler.getLastSuccessfulEvent() Exception=" + e.getMessage() );
            }
            index--;
        }
        return null;
    }

    public Event getLastNonFailedEvent() {
        log.trace("StateHandler.getLastNonFailedEvent() ...");
        int index = events.size() - 1;
        while (index >= 0) {
            try {
                Event event = events.get(index);
                if (event == null) {
                    log.error("StateHandler.getLastNonFailedEvent() null event");
                }
                else {
                    if ((event.success != null) || (event.success == true)) {
                        return event;
                    }
                }
            } catch (Exception e) {
                log.error("StateHandler.getLastNonFailedEvent() Exception=" + e.getMessage() );
            }
            index--;
        }
        return null;
    }

    protected boolean changeCurrentState(State proposedState, UUID uuid) {
        log.trace("StateHandler.changeCurrentState() ...");
        synchronized(stateLock) {
            StateDescriptorFactory.StateDescriptor currentState = currentStateObject.getState();
            log.debug("StateHandler.changeCurrentState() before changeCurrentState from " + currentState.name + " to " + proposedState.getState().name + ".");
            return setState(proposedState, uuid);
        }
    }

    public StateDescriptorFactory.StateDescriptor getCurrentState() {
        log.trace("StateHandler.getCurrentState() ...");
        synchronized(stateLock) {
            return currentStateObject.getState();
        }
    }

    protected ResultWrapper<DataTransferObject> doTransition(Message s) throws InvalidStateTransitionException, SendingException {
        log.trace("StateHandler.doTransition() ...");
        synchronized(stateLock) {
            return currentStateObject.doTransition(s);
        }
    }

    public void reset() {
        events = new Events();
    }

    public Events events = new Events();

    public void setCallback(JFameEventStorage j) {
        log.trace("StateHandler.setCallback() ...");
        this.j = j;
    }
    private JFameEventStorage j = null;

    public class Events {
        private Logger log = LoggerFactory.getLogger(this.getClass());
        protected List<Event> events = new ArrayList<>();
        public boolean add(Event event) {
            log.trace("Events.add() ...");
             if (j != null) {
                 log.trace("Events.add() passing to callback");
                 j.add(event);
             }
            log.trace("Events.add() Added to event list");
            return events.add(event);
        }
        public int size() { return events.size();}

        public Event get(int index) { return events.get(index);}

        public Iterator iterator(){ return events.iterator();}
    }

    final private Boolean stateLock = false;
}
