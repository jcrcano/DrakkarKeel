/*
 * DrakkarKeel - An Enterprise Collaborative Search Platform
 *
 * The contents of this file are subject under the terms described in the
 * DRAKKARKEEL_LICENSE file included in this distribution; you may not use this
 * file except in compliance with the License. 
 *
 * 2013-2014 DrakkarKeel Platform.
 */
package drakkar.stern.callback;

import Ice.LocalException;
import drakkar.oar.Seeker;
import drakkar.oar.slice.client.AMI_ClientSide_notify;
import drakkar.oar.util.OutputMonitor;

public class NotifyAMICallback extends AMI_ClientSide_notify {

    private Seeker receptor;
    private String operation = "";

    public NotifyAMICallback(Seeker receptor) {
        this.receptor = receptor;

    }

    public NotifyAMICallback(Seeker receptor, String operation) {
        this.receptor = receptor;
        this.operation = operation;
    }

    @Override
    public void ice_response() {
        try {
            OutputMonitor.printLine("Operation: <" + operation + "> notified to the user: " + receptor.getUser(), OutputMonitor.INFORMATION_MESSAGE);
        } catch (NullPointerException e) {
            OutputMonitor.printStream("Object Seeker null", e);
        }

    }

    @Override
    public void ice_exception(LocalException ex) {
        OutputMonitor.printStream("Operation: <" + operation + "> Not found the remote host of seeker: " + receptor.getUser(), ex);
        if (ex instanceof Ice.ConnectionRefusedException) {
            CallbackController.getInstance().notifyConnectionRefused(receptor);
        }
    }
}
