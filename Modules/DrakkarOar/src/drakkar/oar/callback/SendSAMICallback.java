/*
 * DrakkarKeel - An Enterprise Collaborative Search Platform
 *
 * The contents of this file are subject under the terms described in the
 * DRAKKARKEEL_LICENSE file included in this distribution; you may not use this
 * file except in compliance with the License. 
 *
 * 2013-2014 DrakkarKeel Platform.
 */
package drakkar.oar.callback;

import drakkar.oar.slice.action.oneway.AMI_Send_sendSAMI;
import drakkar.oar.util.OutputMonitor;

/**
 * Esta clase es utilizada para la invocación del método asíncrono sendSAMI_async.
 * 
 * 
 */
public class SendSAMICallback extends AMI_Send_sendSAMI {

    /**
     * Constructor de la clase
     */
    public SendSAMICallback() {
    }

    /**
     * Indica que la operación ha finalizado con éxito
     */
    public void ice_response() {
       OutputMonitor.printLine("Completed operation.", OutputMonitor.INFORMATION_MESSAGE);
    }

    /**
     * Este método es invocado cuando ocurre una excepción local antes de haber
     * invocado la petición
     *
     * @param ex  Ice.LocalException ocurrida
     */
    public void ice_exception(Ice.LocalException ex) {
          OutputMonitor.printStream("Invocation method sendAMI failed.", ex);
    }

    /**
     * Este método es invocado cuando ocurre una excepción de usuario antes de
     * haber invocado la petición.
     *
     * @param ex  Ice.UserException ocurrida.
     */
    public void ice_exception(Ice.UserException ex) {
         OutputMonitor.printStream("Invocation method sendAMID failed.", ex);
    }
}

