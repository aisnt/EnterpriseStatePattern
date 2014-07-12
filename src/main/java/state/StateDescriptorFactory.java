package state;

import common.Util;
import exceptions.InvalidStateException;

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

    private List<StateDescriptor> stateDescriptors = new ArrayList<>();

    public int Max() {
        return stateDescriptors.size();
    }

    StateDescriptorFactory() {
        String fileNameValidTransitionTable = null;
        try {
            fileNameValidTransitionTable = Util.getStringProperty("FileNameValidTransitionTable");
        } catch (IOException e) {
            e.printStackTrace();
        }

        String[] columnNames = null;
        try {
            columnNames = getColumnNames(fileNameValidTransitionTable);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidStateException e) {
            e.printStackTrace();
        }

        for ( int i = 0;  i < columnNames.length-1;  i++) {
            String colName = columnNames[i+1];
            stateDescriptors.add(new StateDescriptor(colName, i));
        }
    }

    public StateDescriptor get(int stateIndex) throws InvalidStateException {
        if ((stateIndex >= Max()) || (stateIndex < 0)) {
            throw new InvalidStateException("Invalid State stateIndex = " + stateIndex + ".");
        }
        ListIterator l = stateDescriptors.listIterator();
        while(l.hasNext()) {
            StateDescriptor ll = (StateDescriptor) l.next();
            if ( ll.ordinal == stateIndex ) {
                return ll;
            }
        }
        throw new InvalidStateException();
    }

    public StateDescriptor get(String stateName) throws InvalidStateException {
        ListIterator l = stateDescriptors.listIterator();
        while(l.hasNext()) {
            StateDescriptor ll = (StateDescriptor) l.next();
            if ( ll.name.contains(stateName) ) {
                return ll;
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
        FileReader fileReader = new FileReader(fileName);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String line = bufferedReader.readLine();
        String[] colNames = line.split(",");
        fileReader.close();
        return colNames;
    }

    public class StateDescriptor {
        public StateType stateType;
        public String name;
        public int ordinal;
        public StateDescriptor(String s, int i) {
            name = s;
            ordinal = i;
            if (s.contains("Final")) {
                stateType = StateType.Final;
            } else
            if (s.contains("Initial")) {
                stateType = StateType.Initial;
            }
            else {
                stateType = StateType.Normal;
            }
        }
    }
}

