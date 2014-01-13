/*
 * DrakkarKeel - An Enterprise Collaborative Search Platform
 *
 * The contents of this file are subject under the terms described in the
 * DRAKKARKEEL_LICENSE file included in this distribution; you may not use this
 * file except in compliance with the License. 
 *
 * 2013-2014 DrakkarKeel Platform.
 */
package drakkar.stern.servant;

import drakkar.oar.Communication;
import drakkar.oar.Response;
import drakkar.oar.Seeker;
import drakkar.oar.exception.SessionException;
import drakkar.oar.facade.event.FacadeDesktopEvent;
import drakkar.oar.slice.error.RequestException;
import drakkar.oar.slice.login.RolePrx;
import drakkar.oar.slice.login.RolePrxHelper;
import static drakkar.oar.util.KeyMessage.*;
import drakkar.oar.util.KeyTransaction;
import drakkar.oar.util.NotifyAction;
import drakkar.oar.util.OutputMonitor;
import drakkar.oar.util.Utilities;
import drakkar.stern.facade.event.FacadeListener;
import java.util.List;
import java.util.UUID;
import javax.naming.Context;

public class SessionServant {

    private String name;
//    private String id;
    private String description;
    private String uuid;
//  private CCachePersistence Cache ;
    private Communication communication;
    private Servant servant;
//  private ClsConectPersistencia persistence ;
    private ContainerServant container;
    private UUID uuidClass;
    private Context context;
    // estos para mostrar los mensajes en el servidor(GUI)
    private FacadeListener listener = null;
    private boolean hasPersistent = false;
    private RolePrx rolePrx;

    /**
     *
     */
    public SessionServant() {
        this.name = "";
        this.description = "";
        this.uuid = "";
//        this.id = "";
        this.communication = null;
        this.servant = null;
        this.container = null;
        this.rolePrx = null;
    }

    /**
     *
     * @param comm
     * @param container
     * @param id
     * @param name
     * @param desc
     * @param uuid
     */
    @SuppressWarnings("static-access")
    public SessionServant(Communication comm, ContainerServant container, 
            String name, String desc, UUID uuid) {
        this.name = name;
        this.description = desc;
        this.uuid = uuid.randomUUID().toString();
//        this.id = id;
        this.communication = comm;
        this.servant = null;
        this.container = container;
        this.start();
        String message = Utilities.getDateTime() + " Session persistence " + name + " initiate satisfactorily";
        this.notify(INFORMATION_MESSAGE, message);
        OutputMonitor.printLine(message, OutputMonitor.INFORMATION_MESSAGE);


        RoleServant role = new RoleServant(this.communication, this.servant, this.name);
        // Ice genera un UUID para este servant equivalente a la identidad
        Ice.ObjectPrx objectPrx = this.communication.getAdapter().addWithUUID(role);
        rolePrx = RolePrxHelper.uncheckedCast(objectPrx);
    }

    /**
     *
     * @param comm
     * @param container
     * @param id
     * @param name
     * @param desc
     * @param uuid
     * @param listener
     */
    @SuppressWarnings("static-access")
    public SessionServant(Communication comm, ContainerServant container,
            String name, String desc, UUID uuid, FacadeListener listener) {

        this.name = name;
        this.description = desc;
        this.uuid = uuid.randomUUID().toString();
      
        this.communication = comm;
        this.servant = null;
        this.container = container;
        this.listener = listener;        
        String message = Utilities.getDateTime() +" Communication Session: "+ name +" initiate satisfactorily";
        this.notify(INFORMATION_MESSAGE, message);
          OutputMonitor.printLine(message, OutputMonitor.INFORMATION_MESSAGE);
        this.start();
        RoleServant role = new RoleServant(this.communication, this.servant, this.name);
        // Ice genera un UUID para este servant equivalente a la identidad
        Ice.ObjectPrx objectPrx = this.communication.getAdapter().addWithUUID(role);
        rolePrx = RolePrxHelper.uncheckedCast(objectPrx);
    }

    /**
     * Este método inicia la comunicacion de la actual sesion
     */
    public void start() {
        this.servant = this.container.getServant();
    }

    /**
     * Este método detiene la comunicacion de la actual sesion
     */
    public void stop() {
        this.servant = null;
    }

    /**
     *
     * @return
     */
//    public RolePrx getInRoleCommunication(Current __current)
    public RolePrx getRolePrx() {
        return rolePrx;
    }

    /**
     *
     * @return
     * @throws SessionException
     */
    public List<Seeker> getOnlineMembers() throws SessionException {
        List<Seeker> members = this.servant.getOnlineMembers(this.name);
        return members;

    }

    /**
     * Determina la disponibilidad del usuario seleccionado
     *
     * @param userName usuario
     *
     * @return true si el usuario está disponible, false en caso contrario
     * 
     * @throws RequestException si ocurre alguna excepción durante el proceso de la solicitud
     */
    public boolean isAvailableUser(String userName) throws RequestException {
        
        boolean available = this.servant.isAvailableUser(userName);
        return available;

    }

    public String getChairmanName(String sessionName) throws RequestException {
        return  this.servant.getChairmanName(sessionName);
    }

    /**
     * Solicitar registro de usuario en el servidor
     *
     * @param name      nombre
     * @param password  contraseña
     * @param description  descripcion
     * @param userEmail correo
     * @param nickName  usuario
     * @param avatar    foto de usuario
     * @throws RequestException si ocurre alguna excepción durante el proceso de la solicitud
     */
    public void registerSeeker(String nickName, String name, String description, String password, String userEmail, byte[] avatar) throws RequestException {
        this.servant.registerSeeker(nickName, name, password,description, userEmail, avatar);
    }

    /**
     * Registra al usuario en la sesión de comunicación del servidor
     *
     * @param user      usuario
     * @param password  contraseña
     *
     * @return el objeto Seeker que representa al usuario, ó un objeto null de existir
     *         problemas con el usuario ó contraseña introducidos
     *
     * @throws RequestException si ocurre alguna excepción durante el proceso de la solicitud
     */
    public Seeker login(String user, String password) throws RequestException {
        return this.servant.login(user, password, null);
    }

    /**
     * 
     * @param user
     * @throws RequestException
     */
    public void recoverPassword(String user) throws RequestException {
        this.servant.recoverPassword(user);
    }

    /**
     * 
     * @param user
     * @param oldPassword
     * @param newPassword
     * @return
     * @throws RequestException
     */
    public boolean changePassword(String user, String oldPassword, String newPassword) throws RequestException {
        return this.servant.changePassword(user, oldPassword, newPassword);
    }

    /**
     * Este método notifica al servidor el progreso de las actividades invocadas
     * para actualizar el tablón de Log y Monitor.
     *
     * @param messageType
     * @param message
     */
    public void notify(int messageType, String message) {
        if (listener != null) {
            Response rs = new Response();
            rs.put(KeyTransaction.OPERATION, NotifyAction.NOTIFY_TEXT_MESSAGE);
            rs.put(KeyTransaction.MESSAGE_TYPE, messageType);
            rs.put(KeyTransaction.MESSAGE, message);
            FacadeDesktopEvent evt = new FacadeDesktopEvent(this, rs);
            listener.notify(evt);
        }
       
    }

    /**
     *
     * @return
     */
    public Communication getCommunication() {
        return communication;
    }

    /**
     *
     * @param communication
     */
    public void setCommunication(Communication communication) {
        this.communication = communication;
    }

    /**
     *
     * @return
     */
    public ContainerServant getContainerServant() {
        return container;
    }

    /**
     *
     * @param container
     */
    public void setContainerServant(ContainerServant container) {
        this.container = container;
    }

    /**
     *
     * @return
     */
    public Context getContext() {
        return context;
    }

    /**
     *
     * @param context
     */
    public void setContext(Context context) {
        this.context = context;
    }

    /**
     *
     * @return
     */
    public String getDescription() {
        return description;
    }

    /**
     *
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    

    /**
     *
     * @return
     */
    public String getUUID() {
        return uuid;
    }

    /**
     *
     * @param uuid
     */
    public void setUUID(String uuid) {
        this.uuid = uuid;
    }

    /**
     *
     * @return
     */
    public UUID getUUIDClass() {
        return uuidClass;
    }

    /**
     *
     * @param uuidClass
     */
    public void setUUIDClass(UUID uuidClass) {
        this.uuidClass = uuidClass;
    }

    /**
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
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
}
