package state;

import common.Util;
import exceptions.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.UUID;

/**
 * Created by david.hislop@korwe.com on 2014/06/25.
 *
 */

public class Event {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    public Event( StateDescriptorFactory.StateDescriptor from, StateDescriptorFactory.StateDescriptor to, UUID uuid ) throws ConfigurationException {
        log.trace("Event.ctor() start From: " + from.name +" To: " + to.name + ".");
        if ((uuid==null) || (from==null) || (uuid==null) ) {
            throw new ConfigurationException();
        }

        //Successful event
        this.from = from;
        this.to = to;
        this.date = new Date();
        this.success = true;
        this.uuid = uuid;
        this.error = "";
    }

    public Event( StateDescriptorFactory.StateDescriptor to ) throws ConfigurationException {
        log.trace("Event.ctor()  To: " + to.name + ".");
        if  (to==null)  {
            throw new ConfigurationException();
        }

        //Creation or Configuration event
        this.to = to;
        this.date = new Date();
        this.success = null;
        this.uuid = null;
    }

    public Event( StateDescriptorFactory.StateDescriptor from, StateDescriptorFactory.StateDescriptor to ) throws ConfigurationException {
        log.trace("Event.ctor() start From: null To: " + to.name + ".");
        if ( (from==null ) || (to==null ) ){
            throw new ConfigurationException();
        }

        //Failed Event
        this.from = from;
        this.to = to;
        this.date = new Date();
        this.success = false;
        this.uuid = null;
    }

    public Event( StateDescriptorFactory.StateDescriptor current, String error ) throws ConfigurationException {
        log.trace("Event.ctor() start From: null To: " + to.name + ".");
        if ( (current==null) || (error==null) ) {
            throw new ConfigurationException();
        }

        this.from = null;
        this.to = current;
        this.date = new Date();
        this.success = false;
        this.error = error;
        this.uuid = null;
    }

    public StateDescriptorFactory.StateDescriptor from;
    public StateDescriptorFactory.StateDescriptor to;
    public Date date ;
    public Boolean success = null;
    public String error = "";
    public UUID uuid;

    @Override
    public String toString() {
        String fromName = from==null?"":from.name;
        String toName = to==null?"":to.name;
        String fromStateType = from==null?"":from.stateType.name();
        String toStateType= to==null?"":to.stateType.name();

        String result = "transition " + success + " from: " + fromName + "(" + fromStateType + ") to: " + toName + "(" + toStateType + ") Date: " + Util.prettyPrintDate(date) + " uuid=#" +Util.prettyPrintUuid(uuid);
        if (!error.isEmpty()) {
            result += " Error=" + error + ".";
        }
        return result;
    }
}
