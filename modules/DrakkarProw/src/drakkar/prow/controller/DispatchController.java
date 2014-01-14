/*
 * DrakkarKeel - An Enterprise Collaborative Search Platform
 *
 * The contents of this file are subject under the terms described in the
 * DRAKKARKEEL_LICENSE file included in this distribution; you may not use this
 * file except in compliance with the License. 
 *
 * 2013-2014 DrakkarKeel Platform.
 */
package drakkar.prow.controller;

import Ice.Communicator;
import drakkar.oar.Request;
import drakkar.oar.Response;
import drakkar.oar.callback.GetAMIDCallback;
import drakkar.oar.callback.GetSAMICallback;
import drakkar.oar.callback.SendAMIDCallback;
import drakkar.oar.callback.SendSAMICallback;
import drakkar.oar.exception.InvocationException;
import drakkar.oar.slice.error.RequestException;
import drakkar.oar.slice.server.ServerSidePrxHolder;
import drakkar.oar.util.Invocation;
import drakkar.oar.util.OutputMonitor;
import java.io.IOException;
import java.io.Serializable;

/**
 * Esta clase tiene el objetivo de despachar las diferentes de operaciones invocadas
 * por el cliente hacia la aplicación servidora.
 *
* @deprecated As of DrakkarKeel version 1.1,
 * replaced by <code>RequestDispatcher</code>.
 *
 * @see drakkar.prow.communication.RequestDispatcher
 * 
 */
public class DispatchController implements Serializable{
    private static final long serialVersionUID = 80000000000023L;

    private ServerSidePrxHolder serverPrxHolder;
    private Communicator comunicator;

    /**
     * Costructor por defecto de la clase.
     *
     * @param comunicator  objeto Communicator, este es utilizado para efectuar
     *                     la deserialización de los objetos Response retornados
     *                      or el método get(...)
     */
    public DispatchController(Communicator comunicator) {
        this.serverPrxHolder = new ServerSidePrxHolder();
        this.comunicator = comunicator;
    }

    /**
     * Este método ejcuta una operación determinada en el servidor, apartir del
     * objeto Request pasado por parámetros
     *
     * @param request     operación a realizar, con sus parámetros de entrada
     * @param invocation  especifica el modo de invocación de la operación:
     *                    <br>
     *                    <tt>- SYNCHRONOUS_METHOD_INVOCATION: Synchronous Method Invocation</tt><br>
     *                    <tt>- ASYNCHRONOUS_METHOD_INVOCATION: Asynchronous Method Invocation</tt><br>
     *                    <tt>- ASYNCHRONOUS_METHOD_DISPATCH: Asynchronous Method Dispatch</tt><br>
     *                    <tt>- ASYNCHRONOUS_METHOD_INVOCATION_DISPATCH: Asynchronous Method Invocation and Dispatch</tt><br>
     *                    <br>
     */
    public void send(Request request, byte invocation) {
        if (serverPrxHolder != null) {


            try {

                switch (invocation) {
                    case Invocation.SYNCHRONOUS_METHOD_INVOCATION:       // case: 0
                        ////////////////////////////////////////////////////////////
                        this.serverPrxHolder.value.sendSAMI(request.toArray());
                        break;
                    ////////////////////////////////////////////////////////////
                    case Invocation.ASYNCHRONOUS_METHOD_INVOCATION:      // case: 1
                        ////////////////////////////////////////////////////////////
                        SendSAMICallback callbackSAMI = new SendSAMICallback();
                        this.serverPrxHolder.value.sendSAMI_async(callbackSAMI, request.toArray());
                        break;
                    ////////////////////////////////////////////////////////////
                    case Invocation.ASYNCHRONOUS_METHOD_DISPATCH:     // case: 2
                        this.serverPrxHolder.value.sendAMID(request.toArray());
                        break;
                    ////////////////////////////////////////////////////////////
                    case Invocation.ASYNCHRONOUS_METHOD_INVOCATION_DISPATCH: // case: 3
                        ////////////////////////////////////////////////////////////
                        SendAMIDCallback callbackAMID = new SendAMIDCallback();
                        this.serverPrxHolder.value.sendAMID_async(callbackAMID, request.toArray());
                        break;
                    ////////////////////////////////////////////////////////////
                    default:
                        throw new InvocationException("Invalid invocation");
                }

            } catch (InvocationException ex) {
                OutputMonitor.printStream("Invocation not supported ", ex);
            } catch (RequestException ex) {
                OutputMonitor.printStream("To request ", ex);
            } catch (IOException ex) {
                OutputMonitor.printStream("Failed or interrupted I/O operations.", ex);
            }
        }
    }

    /**
     * Este método ejcuta una operación determinada en el servidor, apartir del
     * objeto Request pasado por parámetros y devuelve un correspondiente objeto
     * Response
     * 
     * @param request     operación a realizar, con sus parámetros de entrada
     * @param invocation  Especifica el modo de invocación de la operación:
     *                    <br>
     *                    <tt>- SYNCHRONOUS_METHOD_INVOCATION: Synchronous Method Invocation</tt><br>
     *                    <tt>- ASYNCHRONOUS_METHOD_INVOCATION: Asynchronous Method Invocation</tt><br>
     *                    <tt>- ASYNCHRONOUS_METHOD_DISPATCH: Asynchronous Method Dispatch</tt><br>
     *                    <tt>- ASYNCHRONOUS_METHOD_INVOCATION_DISPATCH: Asynchronous Method Invocation and Dispatch</tt><br>
     *                    <br>
     *                   
     * @return un objeto Response con resultados de la operación ejecutada
     */
    public Response get(Request request, byte invocation) {
        byte[] array = null;
        boolean flag = false;
        if (serverPrxHolder != null) {
            try {
                switch (invocation) {
                    case (Invocation.SYNCHRONOUS_METHOD_INVOCATION):         // case: 0
                        ////////////////////////////////////////////////////////////
                        array = this.serverPrxHolder.value.getSAMI(request.toArray());

                        return Response.arrayToResponse(array, comunicator);
                    ////////////////////////////////////////////////////////////
                    case (Invocation.ASYNCHRONOUS_METHOD_INVOCATION):        // case: 1
                        ////////////////////////////////////////////////////////////
                        GetSAMICallback callbackSAMI = new GetSAMICallback(this.comunicator);
                        flag = this.serverPrxHolder.value.getSAMI_async(callbackSAMI, request.toArray());

                        return callbackSAMI.getResponse();
                    ////////////////////////////////////////////////////////////
                    case (Invocation.ASYNCHRONOUS_METHOD_DISPATCH):       // case: 2
                        ////////////////////////////////////////////////////////////
                        array = this.serverPrxHolder.value.getAMID(request.toArray());

                        return Response.arrayToResponse(array, comunicator);
                    ////////////////////////////////////////////////////////////
                    case (Invocation.ASYNCHRONOUS_METHOD_INVOCATION_DISPATCH):   // case: 3
                        ////////////////////////////////////////////////////////////
                        GetAMIDCallback callbackAMID = new GetAMIDCallback(comunicator);
                        flag = this.serverPrxHolder.value.getAMID_async(callbackAMID, request.toArray());

                        return callbackAMID.getResponse();
                    ////////////////////////////////////////////////////////////
                    default:
                        throw new InvocationException("Invalid invocation");
                }

            } catch (ClassNotFoundException ex) {
                OutputMonitor.printStream("To load in a class", ex);
            } catch (InvocationException ex) {
                OutputMonitor.printStream("Invocation not supported ", ex);
            } catch (RequestException ex) {
                OutputMonitor.printStream("Request exception", ex);
            } catch (IOException ex) {
                OutputMonitor.printStream("Failed or interrupted I/O operations.", ex);
            }

        }
        return null;
    }

    /**
     * Devuelve la instancia de la clase ServerPrxHolder
     *
     * @return serverPrxHolder
     */
    public ServerSidePrxHolder getServerSidePrxHolder() {
        return this.serverPrxHolder;
    }

    /**
     * Modifica el objeto ServerSidePrxHolder de la clase
     *
     * @param serverPrxHolder nuevo ServerSidePrxHolder
     */
    public void setServerSidePrxHolder(ServerSidePrxHolder serverPrxHolder) {
        this.serverPrxHolder = serverPrxHolder;
    }

    /**
     * Cierra la comuniciación con el servidor
     */
    public void disconnect() {

        if (this.serverPrxHolder.value != null) {
            this.serverPrxHolder.value.disconnect();
            this.serverPrxHolder = null;
        }

    }
}
