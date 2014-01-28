/*
 * DrakkarKeel - An Enterprise Collaborative Search Platform
 *
 * The contents of this file are subject under the terms described in the
 * DRAKKARKEEL_LICENSE file included in this distribution; you may not use this
 * file except in compliance with the License. 
 *
 * 2013-2014 DrakkarKeel Platform.
 */
package drakkar.prow.communication;

import drakkar.oar.Response;
import Ice.Current;
import drakkar.prow.facade.ListenerManager;

import drakkar.oar.slice.action.twoway.AMD_Get_getAMID;
import drakkar.oar.slice.client._ClientSideDisp;
import drakkar.oar.slice.error.RequestException;
import drakkar.oar.util.OutputMonitor;
import java.io.IOException;
import java.io.Serializable;

/**
 * Esta clase representa el sirviente del Objeto Ice ClientSide. Esta clase es la
 * encargada de la implementación del método declarado en  la interfaz de la
 * definición slice. Es decir la que determina la operación ha realizar, para
 * actualizar la aplicación del cliente, cuando se reporta una notificación por
 * parte de la aplicación servidora
 */
public abstract class NetworkController extends _ClientSideDisp implements Serializable{
    private static final long serialVersionUID = 80000000000020L;

     protected  ListenerManager listenerManager;

    public NetworkController(ListenerManager listenerManager) {
        this.listenerManager = listenerManager;
    }



    /**
     * Este método es el encargado de llevar a cabo la deserialización del objeto
     * Response enviado por la aplicación servidora, para efectuar la notificación
     * correspondiente en la aplicación cliente
     *
     * @param response  objeto Response serializado
     * @param current   instancia del objeto Current de Ice, quien proporciona
     *                  información sobre la invocación
     *
     */
    public void _notify(byte[] response, Current current) {

        try {

            final Response resp = Response.arrayToResponse(response, current.adapter.getCommunicator());
            // se envia el objeto Response recuperado al método dispatchController,
            // para que este realize el mapeo para la notificación correspondiente.

            Thread t = new Thread(new Runnable() {

                public void run() {
                    dispatch(resp);
                }
            });
            t.start();
        } catch (ClassNotFoundException ex) {
            OutputMonitor.printStream("To load in a class", ex);
        } catch (IOException ex) {
            OutputMonitor.printStream("Failed or interrupted I/O operations.", ex);

        }

    }

    public void getAMID_async(AMD_Get_getAMID cb, byte[] request, Current current) throws RequestException {
    }

    public byte[] getSAMI(byte[] request, Current current) throws RequestException {
        return null;
    }

    public void disconnect(Current current) {
            notifyExit();
    }


     /**
     * Este método realiza el mapeo de la notificación que dio origen al objeto
     * response, para efectuar la correspondiente notificación a la aplicación
     * cliente
     *
     * @param response  este objeto contiene la operación de notificación a efectuar,
     *                  así como los parámeros de la misma
     */
    protected abstract void dispatch(Response response) ;

    protected abstract void notifyExit() ;

    /**
     * @return the listenerManager
     */
    public ListenerManager getListenerManager(){
        return listenerManager;
    }

    /**
     * @param listenerManager the listenerManager to set
     */
    public void setListenerManager(ListenerManager listenerManager) {
        this.listenerManager = listenerManager;
    }

   
}
