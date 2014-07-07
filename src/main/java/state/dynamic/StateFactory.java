package state.dynamic;

import command.DTO;
import command.ResultWrapper;
import command.TransferApi;
import command.TransferApiImpl;
import common.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import state.InvalidStateTransitionException;
import state.SendingException;
import state.State;

import java.io.*;
import java.util.Arrays;
import java.util.List;

/**
 * Created by david.hislop@korwe.com on 2014/06/23.
 */
public class StateFactory extends State {
    final static Logger log = LoggerFactory.getLogger(StateFactory.class);
    public StateFactory(StateDescriptor s) throws IOException, InvalidStateException {
        super( s );
        log.trace("StateFactory.ctor() start ..." );
        if (transitions == null) {
            transitions = createValidTransitionTable();
        }
        log.trace("StateFactory.ctor() ... end" );
    }

    @Override
    public ResultWrapper<DTO> doTransition(Message message)  throws InvalidStateTransitionException, SendingException {
        log.trace("StateFactory.sendMessage() From " + this.getState() + " to " + message.getState() +".");

        ResultWrapper<DTO> dtos=null;
        if (validatePolicy(this.getState(), message.getState())) {
            dtos = transition(message.getState(), message.getPayload());
        }
        else {
            throw new InvalidStateTransitionException("State1.sendMessage() No transition from " + this.getState() + " to " + message.getState() +".");
        }

        return dtos;
    }

    /*
    * part 5:  This is an implementation from State
    */
    @Override
    protected ResultWrapper<DTO> sendMessage(String payload) {
        log.trace("ResultWrapper.sendMessage() Output -> " + payload);
        TransferApi transferApi = new TransferApiImpl();
        DTO dto =  transferApi.get(payload);
        ResultWrapper<DTO> dtos = new ResultWrapper<DTO>(dto);
        return dtos;
    }

    private Boolean validatePolicy(StateDescriptor thisState, StateDescriptor nextState) {
        log.trace("ResultWrapper.validatePolicy() from " + thisState.name() + " to " + nextState.name() + ".");
        int row = thisState.ordinal();
        int col = nextState.ordinal();
        log.debug("ResultWrapper.validatePolicy() from " + row + " to " + col + ".");
        log.debug("ResultWrapper.validatePolicy() from " + transitions[col][row].from +" to " + transitions[col][row].to + " val " + transitions[col][row].doIt + ".");
        return transitions[col][row].doIt;
    }

    private static  Node[][] transitions = null;
    private static Node[][] createValidTransitionTable() throws IOException, InvalidStateException {
        String fileName = "src/main/resources/transitions.table";   //TODO properties
        log.trace("ResultWrapper.createValidTransitionTable() -> " + fileName);
        FileReader fr = new FileReader(fileName);
        if (fr == null) {
            throw new FileNotFoundException();
        }
        BufferedReader br = new BufferedReader(fr);
        String s = br.readLine();
        String[] colNames = s.split(",");
        int len = colNames.length-1;
        Node[][] tt = new Node[len][len];
        int row = 0;
        while ((s = br.readLine()) != null) {
            String[] vals = s.split(",");
            List<String> vs = Arrays.asList(vals);
            int col = 0;
            String rowName = null;
            for (String v : vs) {
                if (col == 0) {
                    rowName = v;
                }
                else {
                    if (v.contains("Y")) {
                        Node one = new Node(col-1, row);
                        tt[row][col-1] = one;
                    }
                    else {
                        Node two = new Node(colNames[col], rowName, false);
                        tt[row][col-1] = two;
                    }
                }
                col++;
            }
            row++;
        }
        fr.close();
        return tt;
    }
}

class Node {
    public Node(String from, String to, Boolean val) {
        this.from = State.StateDescriptor.getStateDescriptor(from);
        this.to = State.StateDescriptor.getStateDescriptor(to);
        doIt = val;
    }

    public Node(int col, int row) throws InvalidStateException {
        this.from = State.StateDescriptor.getStateDescriptor(col);
        this.to = State.StateDescriptor.getStateDescriptor(row);
        doIt = true;
    }

    public Boolean doIt = false;
    public State.StateDescriptor from;
    public State.StateDescriptor to;
}