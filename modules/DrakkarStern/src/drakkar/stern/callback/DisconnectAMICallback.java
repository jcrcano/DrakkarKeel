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
import drakkar.oar.slice.client.AMI_ClientSide_disconnect;
import drakkar.oar.slice.client.ClientSidePrx;
import drakkar.oar.util.OutputMonitor;

public class DisconnectAMICallback extends AMI_ClientSide_disconnect {

    private Seeker receptor;
    private ClientSidePrx receptorPrx;
    private String method;

    public DisconnectAMICallback(String method, Seeker receptor, ClientSidePrx receptorPrx) {
        this.receptor = receptor;
        this.receptorPrx = receptorPrx;
        this.method = method;
    }

    @Override
    public void ice_response() {
        try {
            OutputMonitor.printLine("Notificación de salida del sistema recibida por el seeker " + receptor.getUser(), OutputMonitor.INFORMATION_MESSAGE);
        } catch (Exception e) {
            OutputMonitor.printStream("Objeto Seeker nulo", e);
        }

    }

    @Override
    public void ice_exception(LocalException ex) {

        OutputMonitor.printStream("DisconnectAMICallback Operation: <" + method + ">Not found the remote host of seeker: " + receptor.getUser(), ex);
        if (ex instanceof Ice.ConnectionRefusedException) {          
            CallbackController.getInstance().notifyConnectionRefused(receptor);
        } 

    }
}
