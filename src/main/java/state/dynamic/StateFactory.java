package state.dynamic;

import command.DTO;
import command.ResultWrapper;
import command.TransferApi;
import command.TransferApiImpl;
import common.Message;
import common.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import state.InvalidStateTransitionException;
import state.SendingException;
import state.State;
import state.StateDescriptorFactory;

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
        String s = bufferedReader.readLine();
        String[] colNames = s.split(",");
        int len = colNames.length - 1;
        Node[][] transitionTable = new Node[len][len];
        int row = 0;
        while ((s = bufferedReader.readLine()) != null) {
            String[] vals = s.split(",");
            List<String> vs = Arrays.asList(vals);
            int col = 0;
            String rowName = null;
            for (String v : vs) {
                if (col == 0) {
                    rowName = v;
                } else {
                    if (v.contains("Y")) {
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

    public Base create(StateDescriptorFactory.StateDescriptor s) throws IOException, InvalidStateException {
        return new Base(s);
    }

    public class Base extends State {
        public Base(StateDescriptorFactory.StateDescriptor s) throws IOException, InvalidStateException {
            super(s);
            log.trace("StateFactory.Base.ctor() start.");
        }

        @Override
        public ResultWrapper<DTO> doTransition(Message message) throws InvalidStateTransitionException, SendingException {
            log.trace("StateFactory.Base.doTransition() from " + this.getState().name + " to " + message.getState().name + ".");

            ResultWrapper<DTO> dtos = null;
            if (validatePolicy(this.getState(), message.getState())) {
                dtos = transition(message.getState(), message.getPayload());
            } else {
                throw new InvalidStateTransitionException("StateFactory.Base.doTransition() No transition allowed from " + this.getState().name + " to " + message.getState().name + ".");
            }

            return dtos;
        }

        /*
        * part 5:  This is an implementation from State
        */
        @Override
        protected ResultWrapper<DTO> sendMessage(String payload) {
            log.trace("StateFactory.Base.sendMessage() Output -> " + payload);
            TransferApi transferApi = new TransferApiImpl();
            DTO dto = transferApi.get(payload);
            ResultWrapper<DTO> dtos = new ResultWrapper<>(dto);
            return dtos;
        }

        private Boolean validatePolicy(StateDescriptorFactory.StateDescriptor thisState, StateDescriptorFactory.StateDescriptor nextState) {
            log.trace("StateFactory.Base.validatePolicy() from " + thisState.name + " to " + nextState.name + ".");
            int row = thisState.ordinal;
            int col = nextState.ordinal;
            log.debug("StateFactory.Base.validatePolicy() from " + row + " to " + col + ".");
            Node node = transitions[col][row];
            log.debug("StateFactory.Base.validatePolicy() from " + node.from.name + " to " + node.to.name + " transition allowed = " + node.doTransition + ".");
            return node.doTransition;
        }
    }
}

