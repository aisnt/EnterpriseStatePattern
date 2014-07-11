package state.dynamic;

import common.Util;
import exceptions.InvalidStateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import state.State;
import state.StateDescriptorFactory;
import state.StateHandler;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by david.hislop@korwe.com on 2014/06/23.
 */
public enum StateFactory {
    INSTANCE;

    Logger log = LoggerFactory.getLogger(this.getClass());

    StateFactory()  {
        log.trace("StateFactory.ctor() start ...");
        try {
            String fileNameValidTransitionTable = Util.getStringProperty("FileNameValidTransitionTable");
            transitions = createValidTransitionTable(fileNameValidTransitionTable);
        } catch ( IOException e) {
            log.error("StateFactory.ctor() IOException ", e);
            throw new ExceptionInInitializerError(e);
        } catch (InvalidStateException e ){
            log.error("StateFactory.ctor() InvalidStateException ", e);
            throw new ExceptionInInitializerError(e);
        }
        log.trace("StateFactory.ctor() ... end");
    }

    private Node[][] transitions = null;

    private Node[][] createValidTransitionTable(String fileName) throws IOException, InvalidStateException {
        log.trace("ResultWrapper.createValidTransitionTable() -> " + fileName);
        FileReader fileReader = new FileReader(fileName);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String line = bufferedReader.readLine();
        String[] colNames = line.split(",");
        int len = colNames.length - 1;
        Node[][] transitionTable = new Node[len][len];
        int row = 0;
        while ((line = bufferedReader.readLine()) != null) {
            String[] vals = line.split(",");
            List<String> cells = Arrays.asList(vals);
            int col = 0;
            String rowName = null;
            for (String cell : cells) {
                if (col == 0) {
                    rowName = cell;
                } else {
                    if (cell.contains("Y")) {
                        Node node = new Node(col - 1, row);
                        transitionTable[row][col - 1] = node;
                    } else {
                        Node node = new Node(colNames[col], rowName, false);
                        transitionTable[row][col - 1] = node;
                    }
                }
                col++;
            }
            row++;
        }
        fileReader.close();
        return transitionTable;
    }

    public Base create(StateDescriptorFactory.StateDescriptor descriptor) throws IOException, InvalidStateException {
        return new Base(descriptor);
    }

    public class Base extends State {
        public Base(StateDescriptorFactory.StateDescriptor descriptor) throws IOException, InvalidStateException {
            super(descriptor);
            log.trace("StateFactory.Base.ctor() start.");
        }

        @Override
        protected Boolean validatePolicy(StateDescriptorFactory.StateDescriptor thisState, StateDescriptorFactory.StateDescriptor nextState) {
            log.trace("StateFactory.Base.validatePolicy() from " + thisState.name + " to " + nextState.name + ".");
            StateHandler stateHandler = StateHandler.INSTANCE;

            if (stateHandler.getCurrentState() != this.getState()) {
                log.error("State.Base.validatePolicy() Current state " + stateHandler.getCurrentState() + " != " + " base state " + this.getState() + ".");
                log.error("State.Base.validatePolicy() Cannot transition.");
                return false;
            }

            int row = thisState.ordinal;
            int col = nextState.ordinal;
            log.debug("StateFactory.Base.validatePolicy() from " + row + " to " + col + ".");
            Node node = transitions[col][row];
            log.debug("StateFactory.Base.validatePolicy() from " + node.from.name + " to " + node.to.name + " transition allowed = " + node.doTransition + ".");
            return node.doTransition;
        }
    }
}

