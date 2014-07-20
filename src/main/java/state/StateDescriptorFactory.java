package state;

import common.Util;
import exceptions.InvalidStateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by david.hislop@korwe.com on 2014/07/07.
 */

public enum StateDescriptorFactory {
    INSTANCE;

    private Logger log = LoggerFactory.getLogger(this.getClass());

    private List<StateDescriptor> stateDescriptors = new ArrayList<>();

    public int Max() {
        return stateDescriptors.size();
    }

    StateDescriptorFactory() {
        log.trace("StateDescriptorFactory.ctor() ...");
        String fileNameValidTransitionTable;
        try {
            fileNameValidTransitionTable = Util.getStringProperty("FileNameValidTransitionTable");
        } catch (IOException e) {
            log.error("StateDescriptorFactory.ctor() IOException "+ e.getMessage() );
            throw new ExceptionInInitializerError(e);
        }

        String[] columnNames;
        try {
            columnNames = getColumnNames(fileNameValidTransitionTable);
        } catch (IOException e) {
            log.error("StateDescriptorFactory.ctor() IOException " + e.getMessage());
            throw new ExceptionInInitializerError(e);
        } catch (InvalidStateException e) {
            log.error("StateDescriptorFactory.ctor() InvalidStateException " + e.getMessage());
            throw new ExceptionInInitializerError(e);
        }

        for ( int i = 0;  i < columnNames.length-1;  i++) {
            String colName = columnNames[i+1];
            stateDescriptors.add(new StateDescriptor(colName, i));
        }
    }

    public StateDescriptor get(int stateIndex) throws InvalidStateException {
        log.trace("StateDescriptorFactory.get() ...");
        if ((stateIndex >= Max()) || (stateIndex < 0)) {
            throw new InvalidStateException("StateDescriptorFactory.get() Invalid State stateIndex = " + stateIndex + ".");
        }
        ListIterator listIterator = stateDescriptors.listIterator();
        while(listIterator.hasNext()) {
            StateDescriptor stateDescriptor = (StateDescriptor) listIterator.next();
            if ( stateDescriptor.ordinal == stateIndex ) {
                return stateDescriptor;
            }
        }
        throw new InvalidStateException();
    }

    public StateDescriptor get(String stateName) throws InvalidStateException {
        log.trace("StateDescriptorFactory.get() StateDescriptorFactory.get() ...");
        ListIterator listIterator = stateDescriptors.listIterator();
        while(listIterator.hasNext()) {
            StateDescriptor stateDescriptor = (StateDescriptor) listIterator.next();
            if ( stateDescriptor.name.contains(stateName) ) {
                return stateDescriptor;
            }
        }
        throw new InvalidStateException();
    }

    public Boolean isInitial(StateDescriptor state) {
        return state.stateType == StateType.Initial;
    }

    public Boolean isFinal(StateDescriptor state) {
        return state.stateType == StateType.Final;
    }

    enum StateType {Initial, Final, Normal};

    private String[] getColumnNames(String fileName) throws IOException, InvalidStateException {
        log.trace("StateDescriptorFactory.getColumnNames() ...");
        FileReader fileReader = new FileReader(fileName);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String line = bufferedReader.readLine();
        String[] colNames = line.split(",");
        fileReader.close();
        return colNames;
    }
}

