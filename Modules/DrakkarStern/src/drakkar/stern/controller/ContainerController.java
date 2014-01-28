/*
 * DrakkarKeel - An Enterprise Collaborative Search Platform
 *
 * The contents of this file are subject under the terms described in the
 * DRAKKARKEEL_LICENSE file included in this distribution; you may not use this
 * file except in compliance with the License. 
 *
 * 2013-2014 DrakkarKeel Platform.
 */
package drakkar.stern.controller;

import Ice.Current;
import drakkar.oar.Communication;
import drakkar.oar.Request;
import drakkar.oar.Response;
import drakkar.oar.slice.action.oneway.AMD_Send_sendAMID;
import drakkar.oar.slice.action.twoway.AMD_Get_getAMID;
import drakkar.oar.slice.container._SessionContainerDisp;
import drakkar.oar.slice.error.RequestException;
import drakkar.oar.slice.session.SearchSessionPrx;
import drakkar.oar.util.KeyTransaction;
import drakkar.oar.util.SeekerAction;
import drakkar.stern.SternAppSetting;
import drakkar.stern.facade.event.FacadeListener;
import drakkar.stern.servant.ContainerServant;
import drakkar.stern.servant.Servant;
import drakkar.stern.side.SternData;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

public class ContainerController extends _SessionContainerDisp {

    public ContainerServant servant;
    private String id, name, description = null;
    private SearchSessionPrx proxy;
    private Communication comunication;

    public ContainerController(Communication comm, SternData setting,
             UUID uuidClass, Servant server, FacadeListener listener) {

        servant = new ContainerServant(comm, setting, uuidClass, server, listener);
        this.comunication = comm;
        this.comunication.getAdapter().add(this,Ice.Util.stringToIdentity(setting.getContainerName()));

    }

    public ContainerController(Communication comm, SternAppSetting setting,
             UUID uuidClass, Servant server, FacadeListener listener) {

        servant = new ContainerServant(comm, setting, uuidClass, server, listener);
        this.comunication = comm;
        this.comunication.getAdapter().add(this,Ice.Util.stringToIdentity(setting.getContainerName()));

    }
   

    /**
     * Este método permite la ejecución de una operación con despacho síncrono la
     * cual devuelve un objeto Response serializado.
     *
     * @param request   objeto Request serializado que contiene el nombre de la
     *                  operación a realizar, con sus correspondientes parámetros.
     * @param current   Provee el acceso a la información acerca de la petición
     *                  que actualmente se ejecuta. Es empleado para la deserialización
     *                  del objeto Request.
     *
     * @return  un objeto Response serializado con los resultados de la operación.
     * @throws RequestException   si ocurre alguna exception durante la ejecución
     *                            de la operación
     */
    public byte[] getSAMI(byte[] request, Current current) throws RequestException {
        try {

            Request rqs = Request.arrayToRequest(request, current.adapter.getCommunicator());
            Map<Object, Object> hashRqs = rqs.getParameters();
            
            byte[] array = this.getController(rqs);

            return array;

        } catch (IOException | ClassNotFoundException ex) {            
            throw new RequestException(ex.getMessage());
        }

    }

    /**
     * Este método permite la ejecución de una operación con despacho asíncrono.
     *
     * @param cb        objeto de retrollamada, empleado para notificar el éxito
     *                  de la operación junto con el objeto Response serializado
     *                  el cual contiene el resultado de la operación, e en caso
     *                  contrario lanzar una excepción del tipo RequestException.
     * @param request   objeto Request serializado que contiene el nombre de la
     *                  operación a realizar, con sus correspondientes parámetros.
     * @param current   Provee el acceso a la información acerca de la petición
     *                  que actualmente se ejecuta. Es empleado para la deserialización
     *                  del objeto Request.
     *
     * @throws RequestException   si ocurre alguna exception durante la ejecución
     *                            de la operación
     */
    public void getAMID_async(AMD_Get_getAMID cb, byte[] request, Current current) throws RequestException {
        try {
            Request rqs = Request.arrayToRequest(request, current.adapter.getCommunicator());
            byte[] array = this.getController(rqs);

            cb.ice_response(array);

        } catch (IOException | ClassNotFoundException | RequestException ex) {            
            cb.ice_exception(new RequestException(ex.getMessage()));
        }

    }

    /**
     * Este método permite la ejecución de una operación con despacho síncrono.
     *
     * @param request   objeto Request serializado que contiene el nombre de la
     *                  operación a realizar, con sus correspondientes parámetros.
     * @param current   Provee el acceso a la información acerca de la petición
     *                  que actualmente se ejecuta. Es empleado para la deserialización
     *                  del objeto Request.
     *
     * @throws RequestException   si ocurre alguna exception durante la ejecución
     *                            de la operación
     */
    public void sendSAMI(byte[] request, Current current) throws RequestException {

        try {
            Request rqs = Request.arrayToRequest(request, current.adapter.getCommunicator());
            this.sendController(rqs);
        } catch (IOException | ClassNotFoundException ex) {
           
        }




    }

    /**
     * Este método permite la ejecución de una operación con despacho asíncrono.
     *
     * @param cb        objeto de retrollamada, empleado para notificar el éxito
     *                  de la operación ó lanzar una excepción en caso contrario
     *                  del tipo RequestException.
     * @param request   objeto Request serializado que contiene el nombre de la
     *                  operación a realizar, con sus correspondientes parámetros.
     * @param current   Provee el acceso a la información acerca de la petición
     *                  que actualmente se ejecuta. Es empleado para la deserialización
     *                  del objeto Request.
     *
     * @throws RequestException   si ocurre alguna exception durante la ejecución
     *                            de la operación
     */
    public void sendAMID_async(AMD_Send_sendAMID cb, byte[] request, Current current) throws RequestException {
        try {

            Request rqs = Request.arrayToRequest(request, current.adapter.getCommunicator());
            this.sendController(rqs);
            cb.ice_response();

        } catch (IOException | ClassNotFoundException ex) {
            
            cb.ice_exception(new RequestException(ex.getMessage()));
        }

    }
    

    /**
     * Este método traza un mapa entre la operaciones soportadas por la clase que
     * tienen valores de retorno y la invocada por el cliente, para determinar
     * cual efectuar. En caso de que cliente invoque una operación no implementada
     * este lanza una excepción del tipo OperationNotExistException.
     *
     * @param request   objeto que contiene la operación a realizar, con sus parámetros
     *                  de entrada.
     *
     * @return devuelve un objeto Response serializado.
     *
     * @throws RequestException
     */
    public byte[] getController(Request request) throws RequestException {
        boolean flag = false;
      
         Map<Object, Object> hashRqs = request.getParameters();
       
        try {    
            
            int operation = (Integer) hashRqs.get(KeyTransaction.OPERATION);
            Response rsp = null;
            byte[] array = null;
            switch (operation) {
                case SeekerAction.CREATE_COLLAB_SESSION:
                    id = hashRqs.get(KeyTransaction.SESSION_ID).toString();
                    name = hashRqs.get(KeyTransaction.SESSION_NAME).toString();
                    name = hashRqs.get(KeyTransaction.SESSION_DESCRIPTION).toString();

                    proxy = this.servant.createSession( name, description);
                    hashRqs.clear();
                    hashRqs.put(KeyTransaction.PROXY, proxy);
                    rsp = new Response(hashRqs);
                    array = rsp.toArray();
                    return array;

//                case SeekerAction.GET_IDS_PERSISTENT_SESSIONS:
//                    ArrayList<String> idsp = this.servant.getIdsPersistentSessions();
//                    hashRqs.clear();
//                    hashRqs.put(KeyTransaction.OPERATION, SeekerAction.GET_IDS_PERSISTENT_SESSIONS);
//                    hashRqs.put(KeyTransaction.SESSIONS_IDS, idsp);
//                    rsp = new Response(hashRqs);
//                    array = rsp.toArray();
//
//                    return array;

                case SeekerAction.GET_NAMES_PERSISTENT_SESSIONS:
                    ArrayList<String> namesp = this.servant.getNamesPersistentSessions();
                    hashRqs.clear();
                    hashRqs.put(KeyTransaction.SESSIONS_IDS, namesp);
                    rsp = new Response(hashRqs);
                    array = rsp.toArray();

                    return array;

//                case SeekerAction.GET_IDS_NO_PERSISTENT_SESSIONS:
//                    ArrayList<String> idsnp = this.servant.getIdsNoPersistentSessions();
//                    hashRqs.clear();
//                    hashRqs.put(KeyTransaction.SESSIONS_IDS, idsnp);
//                    rsp = new Response(hashRqs);
//                    array = rsp.toArray();
//
//                    return array;

                case SeekerAction.GET_NAMES_NO_PERSISTENT_SESSIONS:
                    ArrayList<String> namesnp = this.servant.getNamesNoPersistentSessions();
                    hashRqs.clear();
                    hashRqs.put(KeyTransaction.OPEN_SESSIONS, namesnp);
                    rsp = new Response(hashRqs);
                    array = rsp.toArray();

                    return array;

//                case SeekerAction.GET_ID_NO_PERSISTENT_SESSION:
//
//                    id = hashRqs.get(KeyTransaction.SESSION_ID).toString();
//                    proxy = this.servant.getIdNoPersistentSession(id);
//                    hashRqs.clear();
//                    if(proxy==null){
//                       rsp = new Response(hashRqs);
//                       array = rsp.toArray();
//                        return array;
//                    }
//                    hashRqs.put(KeyTransaction.PROXY, proxy);
//                    rsp = new Response(hashRqs);
//                    array = rsp.toArray();
//
//                    return array;

                case SeekerAction.GET_NAME_NO_PERSISTENT_SESSION:

                  
                    name = hashRqs.get(KeyTransaction.SESSION_NAME).toString();
                    proxy = this.servant.getNameNoPersistentSession(name);

                   
                    hashRqs.clear();

                    hashRqs.put(KeyTransaction.PROXY, proxy);
                    rsp = new Response(hashRqs);
                    array = rsp.toArray();

                    return array;

                case SeekerAction.GET_ID_PERSISTENT_SESSION:
                   return null;
                case SeekerAction.GET_NAME_PERSISTENT_SESSION:
                    return null;
//                case SeekerAction.REMOVE_ID_NO_PERSISTENT_SESSION:
//                    id = hashRqs.get(KeyTransaction.SESSION_ID).toString();
//                    flag = this.servant.removeIdNoPersistentSession(id);
//                    hashRqs.clear();
//                    hashRqs.put(KeyTransaction.RESULT, flag);
//                    rsp = new Response(hashRqs);
//                    array = rsp.toArray();
//
//                    return array;

                case SeekerAction.REMOVE_NAME_NO_PERSISTENT_SESSION:
                    id = hashRqs.get(KeyTransaction.SESSION_NAME).toString();
                    flag = this.servant.removeNameNoPersistentSession(name);
                    hashRqs.put(KeyTransaction.RESULT, flag);
                    rsp = new Response(hashRqs);
                    array = rsp.toArray();

                    return array;
                    
                case SeekerAction.GET_ID_MEMBER_PERSISTENT_SESSION:
                   return null;
                case SeekerAction.GET_NAME_MEMBER_PERSISTENT_SESSION:
                    return null;
                default:
                    throw new RequestException("The operation is not supported");
            }

        } catch (IOException ex) {
            
            throw new RequestException(ex.getMessage());
        }


    }

    /**
     * Este método traza un mapa entre la operaciones soportadas por la clase que
     * no retornan nigún parámetro y la invocada por el cliente, para determinar
     * cual efectuar. En caso de que cliente invoque una operación no implementada
     * este lanza una excepción del tipo OperationNotExistException.
     *
     * @param request   objeto que contiene la operación a realizar, con sus parámetros
     *                  de entrada.
     */
    public void sendController(Request request) {
//        int operation = (Integer) request.get(Identifiable.OPERATION);
//        notifyConclusionOperation(INFORMATION_Identifiable.MESSAGE, "The operation was completed satisfactorily.");
    }

//    /**
//     * Este método notifica al miembro que invoca una operación el resultado final
//     * de la misma.
//     *
//     * @param messageType   Posibles tipos de mensaje INFORMATION_Identifiable.MESSAGE, ERROR_Identifiable.MESSAGE,
//     *                      ambos valores definidos en la interfaz Assignable.
//     * @param message       descripción de la operación realizada ó causa del error.
//     */
//    public synchronized void notifyConclusionOperation(int messageType, String message) {
//        

    public void disconnect(Current __current) {
    }
}
