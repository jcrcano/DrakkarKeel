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
import drakkar.oar.facade.event.FacadeDesktopEvent;
import drakkar.oar.slice.session.SearchSessionPrx;
import static drakkar.oar.util.KeyMessage.*;
import drakkar.oar.util.KeyTransaction;
import drakkar.oar.util.NotifyAction;
import drakkar.oar.util.OutputMonitor;
import drakkar.oar.util.Utilities;
import drakkar.stern.SternAppSetting;
import drakkar.stern.controller.SessionController;
import drakkar.stern.facade.event.FacadeListener;
import drakkar.stern.side.SternData;
import java.util.*;
import javax.naming.*;

/**
 * Esta clase tiene como objetivo brindar a los desarrolladores operaciones
 * que le permitan la administración del servidor
 */
public class ContainerServant {
    //TODO arreglar constructores (nombre de las sesiones)

    private String name;
    private String desc;
    private String uuid;
//    private IPersistenciaCrear persistDevice;
    private UUID uuidClass;
    private Communication communication;
    private ArrayList<SessionController> noPersistSessionList;
    private ArrayList<SessionController> persistSessionList;
    private Context context;
//  private  IOrganizacionAwareness _organizacionAwareness = null;
    private Servant server = null;
//    ContainerController controller;
//  private IAwareness _awareness = null ;
    // estos para mostrar los mensajes en el servidor(GUI)
    private FacadeListener listener;

    public ContainerServant(Communication comm, SternData setting,
            UUID uuidClass, Servant server, FacadeListener listener) {

        this.name = setting.getContainerName();
        this.desc = setting.getContainerDescription();
        this.uuid = UUID.randomUUID().toString();
        this.noPersistSessionList = new ArrayList<>();
        this.persistSessionList = null;
        this.communication = comm;
        this.uuidClass = uuidClass;
        this.server = server;
//        this.controller = controller;
        this.listener = listener;

        String message = Utilities.getDateTime() + " Initiating the communication container: " + name;
        this.notify(INFORMATION_MESSAGE, message);

       
        SessionController session = new SessionController(comm, this, setting.getSessionName(),
               setting.getSessionDescription(), uuidClass, listener);



        this.addNoPersistentSession(session);

        message = Utilities.getDateTime() + " The session was added to the container("+name+") satisfactorily";
        this.notify(INFORMATION_MESSAGE, message);
    }


     public ContainerServant(Communication comm, SternAppSetting setting,
            UUID uuidClass, Servant server, FacadeListener listener) {

        this.name = setting.getContainerName();
        this.desc = setting.getContainerDescription();
        this.uuid = UUID.randomUUID().toString();
        this.noPersistSessionList = new ArrayList<>();
        this.persistSessionList = null;
        this.communication = comm;
        this.uuidClass = uuidClass;
        this.server = server;
//        this.controller = controller;
        this.listener = listener;

        String message = Utilities.getDateTime() + " Initiating the communication container: " + name;
        this.notify(INFORMATION_MESSAGE, message);

        SessionController session = new SessionController(comm, this, setting.getSessionName(),
               setting.getSessionDescription(), uuidClass, listener);

      

        this.addNoPersistentSession(session);

        message = Utilities.getDateTime() + " The session was added to the container("+name+") satisfactorily";
        this.notify(INFORMATION_MESSAGE, message);
    }

    /**
     *public SearchSessionPrx createNewNoPersistSession(String sessionId, String sessionName
     * String description, Current current)
     * 
     * @param id
     * @param name
     * @param description
     * @return
     */
    public SearchSessionPrx createSession( String name, String description) {
        SessionController sessionController = new SessionController(communication, this, 
                name, description, this.uuidClass);
        noPersistSessionList.add(sessionController);

        return sessionController.getSearchSessionPrx();
    }
//
//    /**
//     *  public ArrayList<String> getPersistSessionIdList(Current current)
//     * @return
//     */
//    public ArrayList<String> getIdsPersistentSessions() {
//        ArrayList<String> temp = new ArrayList<String>();
//        for (SessionController item : persistSessionList) {
//            temp.add(item.getId());
//        }
//        return temp;
//    }

    /**
     *    public ArrayList<String> getPersistSessionNameList(Current current) {
     * @return
     */
    public ArrayList<String> getNamesPersistentSessions() {
        ArrayList<String> temp = new ArrayList<>();
        for (SessionController item : persistSessionList) {
            temp.add(item.getName());
        }
        return temp;
    }

//    /**
//     * public SearchSessionPrx getNoPersistSessionId(String sessionId, Current current)
//     * @param id
//     * @return
//     */
//    public SearchSessionPrx getIdNoPersistentSession(String id) {
//        SearchSessionPrx sessionPrx = null;
//        for (SessionController item : noPersistSessionList) {
//            if (item.getId().equals(id)) {
//                sessionPrx = item.getSearchSessionPrx();
//                return sessionPrx;
//            }
//        }
//        return sessionPrx;
//    }

    /**
     * 
     * @param name
     * @return
     */
    public SearchSessionPrx getNameNoPersistentSession(String name) {
        SearchSessionPrx proxy = null;
        for (SessionController item : noPersistSessionList) {
            if (name.equals(item.getName())) {
                proxy = item.getSearchSessionPrx();
                return proxy;
            }
        }
        return proxy;
    }

//    /**
//     *
//     * @param id
//     * @return
//     */
//    public boolean removeIdNoPersistentSession(String id) {
//        boolean flag = false;
//        Ice.Object object = communication.getAdapter().find(Ice.Util.stringToIdentity(id));
//        if (object != null) {
//            communication.getAdapter().remove(Ice.Util.stringToIdentity(id));
//            for (SessionController item : noPersistSessionList) {
//                if (item.getId().equals(id)) {
//                    noPersistSessionList.remove(item);
//                    flag = true;
//                    break;
//                }
//            }
//        }
//        return flag;
//    }

    /**
     *
     * @param name
     * @return
     */
    public boolean removeNameNoPersistentSession(String name) {
        boolean flag = false;
        for (SessionController item : noPersistSessionList) {
            if (item.getName().equals(name)) {
                noPersistSessionList.remove(item);
                flag = true;
                break;
            }
        }
        return flag;
    }

    /**
//     *
//     * @return
//     */
//    public ArrayList<String> getIdsNoPersistentSessions() {
//        ArrayList<String> temp = new ArrayList<String>();
//        for (SessionController item : noPersistSessionList) {
//            temp.add(item.getId());
//        }
//        return temp;
//    }

    /**
     *
     * @return
     */
    public ArrayList<String> getNamesNoPersistentSessions() {
        ArrayList<String> temp = new ArrayList<>();
        for (SessionController item : noPersistSessionList) {
            temp.add(item.getName());
        }
        return temp;
    }

    public SearchSessionPrx getIdPersistentSession(String sessionId, Seeker member) {
//        for (Iterator iter = persistSessionList.iterator(); iter.hasNext(); ) {
//         SessionServant item = (SessionServant)iter.next();
//          if ( item.getJSession().getIdName().equals(sessionId) ){
//            ArrayList temp = item.getJSession().getCache().obtenerListaActores();
//              for (Iterator iterActor = temp.iterator(); iterActor.hasNext(); ) {
//                Seeker itemActor = (Seeker)iterActor.next();
//                  if ( itemActor.getNombreActor().equals(actor.NombreActor) ){
//                    return item.getsessionPrx();
//                  }
//              }
//          }
//       }
        return null;
    }

    public SearchSessionPrx getNamePersistentSession(String sessionName, Seeker member) {
//        for (Iterator iter = persistSessionList.iterator(); iter.hasNext(); ) {
//        SessionServant item = (SessionServant)iter.next();
//         if ( item.getJSession().getNombreSession().equals(sessionName) ){
//           ArrayList temp = item.getJSession().getCache().obtenerListaActores();
//             for (Iterator iterActor = temp.iterator(); iterActor.hasNext(); ) {
//               Seeker itemActor = (Seeker)iterActor.next();
//                 if ( itemActor.getNombreActor().equals(actor.NombreActor) ){
//                   return item.getsessionPrx();
//                 }
//             }
//         }
//      }
        return null;
    }

    public void addNoPersistentSession(SessionController session) {
        this.noPersistSessionList.add(session);
    }

    public void removerNoPersistentSession(SessionController session) {
        this.noPersistSessionList.remove(session);
    }

    public void addPersistentSession(SessionController session) {
        this.persistSessionList.add(session);
    }

    public void removerPersistentSession(SessionController session) {
        this.persistSessionList.remove(session);
    }

    /**
     *
     * @return
     */
    public ArrayList<SessionController> getNoPersistentSessions() {
        return this.noPersistSessionList;
    }

    /**
     *
     * @return
     */
    public ArrayList<SessionController> getPersistentSessions() {
        return this.persistSessionList;
    }

    /**
     * Crea una nueva sesión no persistente y la adiciona a la lista de sesiones
     * no persistentes del ContainerServant
     *
     * @param comm
     * @param id
     * @param name
     * @param description
     * @param uuid
     */
    public void createSession(Communication comm, String id, String name, String description, UUID uuid) {
        SessionController sessionController = new SessionController(comm, this,  name, description, uuid);
        this.addNoPersistentSession(sessionController);

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

        OutputMonitor.printLine(message,OutputMonitor.INFORMATION_MESSAGE);
//        Server.log.println(message);
//        Server.log.flush();
    }

    private void loadSessions(Communication comm) {
//    Statement consulta = null;
//    ResultSet resultado = null;
//    try {
//      consulta = ConectPersistencia.Conect.createStatement();
//      resultado = consulta.executeQuery("select * from derbyDBSessiones");
//      while (resultado.next()) {
//        CrearSessiones(comm, resultado.getString("NombreIdentificacion"),
//                       resultado.getString("NombreMostrar"),
//                       resultado.getString("Descripcion"),
//                       resultado.getString("uuidsessiones"));
//      }
//    }
//    catch (SQLException err) {
//      Server.errorFile.println(Utilities.getDateTime() +
//                                                " Clase: CContainer " +
//                                                err.getMessage());
//      Server.errorFile.flush();
//    }
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
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @param descrip
     */
    public void setDescription(String descrip) {
        this.desc = descrip;
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
    public String getName() {
        return this.name;
    }

    /**
     *
     * @return
     */
    public String getDescription() {
        return this.desc;
    }

    /**
     *
     * @return
     */
    public String getUUID() {
        return this.uuid;
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
    public UUID getUUIDClass() {
        return this.uuidClass;
    }

    /**
     *
     * @return
     */
    public Servant getServant() {
        return server;
    }

    /**
     *
     * @param server
     */
    public void setServant(Servant server) {
        this.server = server;
    }
}
