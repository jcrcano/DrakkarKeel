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

import Ice.Communicator;
import drakkar.oar.Response;
import drakkar.oar.slice.action.twoway.AMI_Get_getAMID;
import drakkar.oar.slice.error.RequestException;
import drakkar.oar.util.OutputMonitor;
import java.io.IOException;


/**
 * Esta clase es utilizada para la invocación del método asíncrono getAMID_async,
 * y posterior notificación de los resultados al cliente
 *
 */
public class GetAMIDCallback extends AMI_Get_getAMID {

    private Response response = null;
    private Communicator communicator;

    /**
     * Constructor de la Clase
     *
     * @param communicator   objeto Communicator, empleado para la deserialización
     *                       del objeto Response.
     */
    public GetAMIDCallback(Communicator communicator) {
        this.communicator = communicator;
    }

    /**
     * Indica que la operación ha finalizado con éxito
     *
     * @param rsp representa el ó los valores de retorno de la operación
     */
    @Override
    synchronized public void ice_response(byte[] rsp) {
        try {
            this.response = Response.arrayToResponse(rsp, communicator);
            notify();
        } catch (IOException ex) {
            OutputMonitor.printStream("IO", ex);
        } catch (ClassNotFoundException ex) {
            OutputMonitor.printStream("", ex);
        }

    }

    /**
     * Este método es invocado cuando ocurre una excepción local antes o
     * después de haber invocado la petición.
     *
     * @param ex  Ice.LocalException ocurrida
     */
    public void ice_exception(Ice.LocalException ex) {
        System.err.println("GetSAMI call failed:");
        ex.printStackTrace();
    }

    /**
     * Este método es invocado cuando ocurre una excepción de usuario antes o
     * después de haber invocado la petición.
     *
     * @param ex  Ice.UserException ocurrida
     */
    public void ice_exception(Ice.UserException ex) {
        if (ex instanceof RequestException) {
            System.err.println("drakkar.oar.exceptions.RequestException");
            System.err.println("reason: " + ex.toString());
        } else {
            ex.printStackTrace();
        }
    }

    /**
     * Devuelve el objeto Response, una vez que la operación invocada
     * halla sido finalizada.
     *
     * @return un Objeto Response con los parámetros de salida correspondientes.
     */
    synchronized public Response getResponse() {
        while (response == null) {
            try {
                wait();
            } catch (java.lang.InterruptedException ex) {
            }
        }
        return response;
    }

    /**
     * Devuelve un objeto Communicator de la clase
     *
     * @return communicator
     */
    public Communicator getCommunicator() {
        return communicator;
    }

    /**
     * Modifica el objeto Communicator de la clase
     *
     * @param communicator  nuevo objeto Communicator
     */
    public void setCommunicator(Communicator communicator) {
        this.communicator = communicator;
    }
}
