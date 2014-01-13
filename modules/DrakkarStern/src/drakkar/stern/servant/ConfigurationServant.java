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
import drakkar.oar.Request;
import drakkar.oar.util.KeyTransaction;
import drakkar.stern.Stern;
import drakkar.stern.controller.ContainerController;
import drakkar.stern.controller.SessionController;
import java.util.*;

/**
 * Esta clase constitye el sirviente  del objeto ice Configuration, por lo cual
 * implementa los métodos definidos en esta interfaz, los cuales tienen objetivo
 * brindar a los desarrolladores operaciones que le permitan la administración
 * del servidor
 */
public class ConfigurationServant  {

    private ArrayList<ContainerController> containersList = null;
    private Communication communication = null;
    private Stern server = null;

    /**
     * Constructor de la clase.
     *
     * @param containers lista de ContainerController
     * @param comm       objeto Communnication
     * @param serv       objeto Stern
     */
    public ConfigurationServant(ArrayList<ContainerController> containers, Communication comm, Stern serv) {
        this.containersList = containers;
        this.communication = comm;
        this.server = serv;
    }

    /**
     * Devuelve el uuid de un contenedor apartir de su nombre
     *
     * @param request objeto que contiene el nombre de la operación y los
     *                parámetros de entrada del método
     *
     * @return uuid del contenedor
     */
    public String getUUIDContainer(Request request) {
        String name = (String) request.get(KeyTransaction.CONTAINER_NAME);
        String uuid = null;
        for (ContainerController item : containersList) {
            if (item.servant.getName().equals(name)) {
                uuid = item.servant.getUUID();
                break;
            }
        }    

        return uuid;
    }

    /**
     * Elimina una sesión apartir del uuid de su contenedor y su nombre
     *
     * @param request objeto que contiene el nombre de la operación y los
     *                parámetros de entrada del método
     *
    * @return true si se elimino la sesión, false en caso contrario.
     */
    public boolean removeNameSession(Request request) {
        boolean flag = false;
        String containerUUID = (String) request.get(KeyTransaction.CONTAINER_UUID);
        String sessionName = (String) request.get(KeyTransaction.SESSION_NAME);

        // implementación


        
        return flag;
    }

    /**
     * Elimina una sesión apartir del uuid de su contenedor y su id
     * 
     * @param request objeto que contiene el nombre de la operación y los
     *                parámetros de entrada del método
     *
     * @return un objeto Response con el resultado de la operación
     */
    public boolean removeIdSession(Request request) {
        boolean flag = false;
        String containerUUID = (String) request.get(KeyTransaction.CONTAINER_UUID);
        String sessionID = (String) request.get(KeyTransaction.SESSION_ID);

        // code


        //
       

        return flag;
    }

    /**
     * Este método crea una sesión persitente
     *
     * @param request - objeto que contiene el nombre de la operación y los
     *                  parámetros de entrada del método
     *
     *                  Estructura del Objeto Request:
     *                      <OPERATION,CREATE_SESSION> nombre de la operación
     *                      <CONTAINER_UUID,....> uuid del contenedor de la sesión
     *                      <SESSION_ID,........> id de la sesión
     *                      <SESSION_NAME,......> nombre de la sesión
     *                      <SESSION_DESCRIPTION,......> descripción de la sesión
     *
     * @return un objeto Response con el resultado de la operación
     */
    public boolean createSession(Request request) {
        boolean flag = false;
        String containerUUID = (String) request.get(KeyTransaction.CONTAINER_UUID);
       
        String sessionName = (String) request.get(KeyTransaction.SESSION_NAME);
        String sessionDesc = (String) request.get(KeyTransaction.SESSION_DESCRIPTION);

        for (ContainerController item : containersList) {
            if (containerUUID.equals(item.servant.getUUID())) {
                SessionController session = new SessionController(communication,
                        item.servant, sessionName, sessionDesc, server.getUUID());

                if (session != null) {
//                    Connection Conexcion = null;
//                    ClsPersistenciaCache CacheObjetosPersistentes;
//          Conexion = item.getConectPersistence();
//          CacheObjetosPersistentes = new ClsPersistenciaCache();
//          publicarCache(CacheObjetosPersistentes, Conexcion,session.getsessionI()());

//              session.getSessionI().setCache(CacheObjetosPersistentes);
//                    session.getJSession().setPersistence(item.getConectPersistence());
//                    session.setUUIDClass(item.getUUIDClass());
//                    session.startServerCommunicationWithPersistence();

                    flag = true;
//                    item.AdicionarSessionConPersistencia(session);
                }
            }
        }

        return flag;
    }

    /**
     * Este método registra un nuevo miembro(Member) en servidor
     *
     * @param request - objeto que contiene el nombre de la operación y los
     *                  parámetros de entrada del método
     *
     *                  Estructura del Objeto Request:
     *                      <"met","registerMember"> nombre de la operación
     *                      <"member", Member>       instancia de la clase Member
     *
     * @return un objeto Response con el resultado de la operación
     */
    public boolean registerMember(Request request) {
        boolean flag = false;


        

        return flag;
    }

    /**
     * Elimina un miembro(Member) del servidor
     *
     * @param request objeto que contiene el nombre de la operación y los
     *                parámetros de entrada del método
     *
     *                  Estructura del Objeto Request:
     *                      <"met","unregisterMember"> nombre de la operación
     *                      <"member", Member>         instancia de la clase Member
     *
     * @return un objeto Response con el resultado de la operación
     */
    public boolean unregisterMember(Request request) {
        boolean flag = false;


      

        return flag;
    }



}
