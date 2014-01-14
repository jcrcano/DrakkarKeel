/*
 * DrakkarKeel - An Enterprise Collaborative Search Platform
 *
 * The contents of this file are subject under the terms described in the
 * DRAKKARKEEL_LICENSE file included in this distribution; you may not use this
 * file except in compliance with the License. 
 *
 * 2013-2014 DrakkarKeel Platform.
 */
package drakkar.prow;

import Ice.ObjectPrx;
import drakkar.prow.communication.NetworkController;
import drakkar.oar.Communication;
import drakkar.oar.slice.client.ClientSidePrx;
import drakkar.oar.slice.client.ClientSidePrxHelper;
import drakkar.oar.util.OutputMonitor;
import java.io.Serializable;


/**
 * Esta clase tiene el objetivo de registrar el objeto proxy del usuario en el tiempo
 * de ejcución de ice para recibir las respectivas notificaciones de las operaciones 
 * invocadas,así como otras notificaciones enviadas por la aplicación servidora 
  */
public class ApplicationManager implements Serializable{
    private static final long serialVersionUID = 80000000000001L;

    private ClientSidePrx clientSidePrx;
    private Communication communication;
    private NetworkController controller;
    private int portNumber;

    /**
     * Constructor de la clase
     *
     * @param comm       instancia de la clase Communication
     * @param controller
     * @param portNumber número del puerto por el que el cliente recibirá las notificaciones
     */
    public ApplicationManager(Communication comm, NetworkController controller, int portNumber) {
        this.communication = comm;
        this.portNumber = portNumber;
         this.controller = controller;

        
    }

    /**
     * Activa el objeto proxy que representa el usuario en el tiempo de
     * ejecución de ice, estando listo para escuchar cualquier notificación de la
     * aplicación servidora
     */
    public void activate() {
        // se crea un ObjectAdapter con endpoint, este quiere decir que los servant registrados para este
        // objeto solo escucharan las peticiones entrantes por ese endpoint(Puerto)
        Ice.ObjectAdapter adapter = this.communication.getCommunicator().createObjectAdapterWithEndpoints("Client", " tcp -p " + portNumber);
        this.communication.setAdapter(adapter);
        
        //se agrega un servant del objeto ClientSide en ASM del adptador
        ObjectPrx prx = this.communication.getAdapter().addWithUUID(controller);

        //Activa a todos los endpoints que forman parte de este adaptador del objeto.
        //Después de la activación,el adaptador del objeto puede despachar peticiones
        //recibidas por el endpoint
        this.communication.getAdapter().activate();
        //convierte el objeto proxy creado al tipo ClientSidePrx
        clientSidePrx = ClientSidePrxHelper.uncheckedCast(prx);

      

    }

    /**
     * Desactiva el objeto proxy que representa al usuario dentro del tiempo de
     * ejecución de Ice
     */
    public void deactivate() {
        try {
            this.communication.getAdapter().remove(clientSidePrx.ice_getIdentity());
            this.clientSidePrx = null;

        } catch (Ice.LocalException err) {
            OutputMonitor.printLine("Error mientras se ejecutaba la desactivacion" +
                    " del objeto proxy del cliente. \n Causa: "+err.getMessage(), OutputMonitor.ERROR_MESSAGE);
        }
    }

    /**
     * Este método devuelve el objeto NotificationController
     * 
     * @return instancia de NotificationController
     */
    public NetworkController getNetworkController() {
        return this.controller;
    }

    /**
     * Este método devuelve el objeto ClientSidePrx
     *
     * @return instancia de ClientSidePrx
     *
     */
    public ClientSidePrx getClientSidePrx() {
        return this.clientSidePrx;
    }

    /**
     * Este método reemplaza el objeto NotificationController de la clase
     * 
     * @param client nuevo NotificationController
     */
    public void setNetworkController(NetworkController client) {
        this.controller = client;
    }

    /**
     * Este método reemplaza el objeto proxy ClientSidePrx de la clase
     *
     * @param prx objeto ClientSidePrx
     */
    public void setClientSidePrx(ClientSidePrx prx) {
        this.clientSidePrx = prx;
    }
}
