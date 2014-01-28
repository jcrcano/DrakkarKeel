/*
 * DrakkarKeel - An Enterprise Collaborative Search Platform
 *
 * The contents of this file are subject under the terms described in the
 * DRAKKARKEEL_LICENSE file included in this distribution; you may not use this
 * file except in compliance with the License. 
 *
 * 2013-2014 DrakkarKeel Platform.
 */
package drakkar.stern;

import drakkar.oar.Communication;
import drakkar.oar.Response;
import drakkar.oar.facade.event.FacadeDesktopEvent;
import drakkar.oar.file.transfer.FileStoreServant;
import drakkar.oar.svn.SVNController;
import static drakkar.oar.util.KeyMessage.*;
import static drakkar.oar.util.KeyTransaction.*;
import static drakkar.oar.util.NotifyAction.*;
import drakkar.oar.util.OutputMonitor;
import drakkar.oar.util.Utilities;
import drakkar.stern.controller.ContainerController;
import drakkar.stern.controller.ManagerController;
import drakkar.stern.facade.event.FacadeListener;
import drakkar.stern.servant.SternManagerServant;
import java.io.*;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Stern {

    private Communication communication;
    private ManagerController manager;
    private static boolean flag = false;
    private static Stern instance = null;
    private SternAppSetting serverData;
    private SternManagerServant serverManager;
    private FileStoreServant fileServant;
    private UUID uuid;
    public static PrintWriter error = null;
    public static PrintWriter log = null;
    public static String uuidNull = " ";
    // estos para mostrar los mensajes en el servidor(GUI)
    private FacadeListener listener = null;
    private SVNController svnController;

    private Stern() {

        this.communication = null;
        this.serverManager = null;
        this.manager = null;
        uuid = new UUID(56, 79);

    }

    /**
     *
     * @return
     */
    public static Stern getInstance() {
        if (flag) {
            return instance;
        } else {
            instance = new Stern();
            flag = true;
            return instance;
        }
    }

    @Override
    public void finalize() {
        try {
            flag = false;
            super.finalize();
           
        } catch (Throwable ex) {
            Logger.getLogger(Stern.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Establece los términos de configuración del servidor
     *
     * @param communication instancia de la clase Communication
     * @param server        datos del servidor
     */
    public void setConfiguration(Communication communication, SternAppSetting server) {
        this.communication = communication;
        this.serverData = server;
    }

    /**
     * Este método tiene el objetivo de iniciar el servicio de prestaciones del
     * servidor. Para ello realiza las siguientes operaciones:

      - Crea un ObjectAdapter con puntos finales(endPoint) en el objeto Communicator.
        Este endpoint corresponde al número de puerto por el  cuál se
        escucharán las peticiones entrantes de los clientes.
      - Agrega  al Mapa Activo de Sirviente del adaptador de objeto, los sirvientes
        que encaranan a los objetos Ice SternManager y Manager.
     *   
     * @throws NullPointerException si ocurre algún error durante el proceso de inicio
     *                              de la comunicación del servidor, tales como parámetros
     *                              de configuración del servidor con valor nulo
     */
    public void start() throws NullPointerException {

        String serverName = serverData.getServerName();
        // se crea un ObjectAdapter con endpoint, este quiere decir que los servant registrados para este
        // objeto solo escucharan las peticiones entrantes por ese endpoint(puerto por el server escuchará
        //las peticiones de los clientes), un ObjetAdapter puede tener más de un endpoint.
        Ice.ObjectAdapter adapter = this.communication.getCommunicator().createObjectAdapterWithEndpoints(
                serverData.getAdapterName(), "tcp -p " + serverData.getAdapterPort());
        // se actualiza el adapter de la clase Communication
        this.communication.setAdapter(adapter);

        String message = Utilities.getDateTime() + " Initiated service of communication of DrakkarKeel 0.1";
        this.notify(INFORMATION_MESSAGE, message);


        // iniciando el servicio de prestaciones del servidor
        serverManager = new SternManagerServant(this.communication, serverName, serverData.getServerDescription());
        // Se agrega  al adaptador, un sirviente quien encarna al Objeto Ice SternManagerServant,
        // con un Objeto Identity que identifica al sirviente dentro de Mapa Activo de Sirviente
        // del Object Adapter.
        adapter.add(serverManager, Ice.Util.stringToIdentity(serverName));

        message = Utilities.getDateTime() + " Initiated the server:" + serverName;
        OutputMonitor.printLine(message, OutputMonitor.INFORMATION_MESSAGE);
        this.notify(INFORMATION_MESSAGE, message);

        manager = new ManagerController(serverManager.getContainers());
        adapter.add(manager, Ice.Util.stringToIdentity("Manager" + serverName));

        message = Utilities.getDateTime() + " Initiated Manager's service:Manager" + serverName;
        this.notify(INFORMATION_MESSAGE, message);
        OutputMonitor.printLine(message, OutputMonitor.INFORMATION_MESSAGE);

        fileServant = new FileStoreServant(communication, getSvnController());
        adapter.add(fileServant, Ice.Util.stringToIdentity(serverData.getFileStoreName()));

        message = Utilities.getDateTime() + " Initiated File Store Service: " + serverData.getFileStoreName();
        this.notify(INFORMATION_MESSAGE, message);
        OutputMonitor.printLine(message, OutputMonitor.INFORMATION_MESSAGE);

        //Activa a todos los endpoints que forman parte de este adaptador del objeto.
        //Después de la activación,el adaptador del objeto puede despachar peticiones
        //de los clientes recibidas a través de sus puntos finales ( en este caso el
        //puerto de escucha especificado para el servidor).
        adapter.activate();

        message = Utilities.getDateTime() + " Server started....";
        this.notify(INFORMATION_MESSAGE, message);
        OutputMonitor.printLine(message, OutputMonitor.INFORMATION_MESSAGE);

    }

    /**
     * Este método cierra el servicio se comunicación del servidor, desactivando
     * el adaptador del objeto, pero no espera por que concluyan las operaciones
     * que estaban ya en  progreso, por lo pueden seguir corriendo
     */
    public void stop() {

        // deteniendo la comunicación del servidor
        try {
            this.communication.getCommunicator().shutdown();
            this.communication.getCommunicator().destroy();
            this.communication.setCommunicator(Ice.Util.initialize());

            String message = Utilities.getDateTime() + " Stopped service of communication of DrakkarKeel";
            this.notify(INFORMATION_MESSAGE, message);
            OutputMonitor.printLine(message, OutputMonitor.INFORMATION_MESSAGE);

            message = Utilities.getDateTime() + " Stopped the server  " + serverManager.getName();
            this.notify(INFORMATION_MESSAGE, message);
            OutputMonitor.printLine(message, OutputMonitor.INFORMATION_MESSAGE);
        } catch (Ice.LocalException e) {
         
            String message = Utilities.getDateTime() + " Class: Server\n"
                    + "    Method: stop\n"
                    + "       Error:" + e.getMessage();
            this.notify(ERROR_MESSAGE, message);
            OutputMonitor.printStream(message, e);
        }



    }

    /**
     * Agrega un objeto CContainer a la lista de contenedores del administrador
 del servidor (JSternManeger).
     *
     * @param container  contralador del objeto contenedor
     */
    public void addContainer(ContainerController container) {
        container.servant.setUUIDClass(this.uuid);
        this.serverManager.add(container);
    }

    /**
     * Elimina un objeto CContainer a la lista de contenedores del administrador
 del servidor (JSternManeger).
     *
     * @param container  contralador del objeto contenedor
     */
    public void removeContainer(ContainerController container) {
        this.serverManager.remove(container);
    }

    /**
     * Este método notifica al servidor el progreso de las actividades invocadas
     * para actualizar el tablón de Log y Monitor.
     *
     * @param messageType tipo de mensaje
     * @param message     mensdaje
     */
    public void notify(int messageType, String message) {
        if (listener != null) {
            Response rs = new Response();
            rs.put(OPERATION, NOTIFY_TEXT_MESSAGE);
            rs.put(MESSAGE_TYPE, messageType);
            rs.put(MESSAGE, message);
            FacadeDesktopEvent evt = new FacadeDesktopEvent(this, rs);
            listener.notify(evt);
        }
    }

    /**
     * 
     * @return
     */
    public FacadeListener getListener() {
        return listener;
    }

    /**
     *
     * @param listener
     */
    public void setListener(FacadeListener listener) {
        this.listener = listener;
    }

    /**
     *
     * @return
     */
    public UUID getUUID() {
        return uuid;
    }

    /**
     *
     * @return
     */
    public SternManagerServant getServerManagerServant() {
        return serverManager;
    }

    /**
     *
     * @return
     */
    public String getName() {
        return serverManager.getName();
    }

    /**
     * Devuelve la descripcion del objeto Stern.
     * 
     * @return
     */
    public String getDescription() {
        return serverManager.getDescription();
    }

    /**
     * @return the svnController
     */
    public SVNController getSvnController() {
        return svnController;
    }

    /**
     * @param svnController the svnController to set
     */
    public void setSvnController(SVNController svnController) {
        this.svnController = svnController;
    }

    //verify....
    public void updateFileStoreServant(SVNController svn) {
        this.setSvnController(svn);
        fileServant.setSvnController(svn);
    }
}
