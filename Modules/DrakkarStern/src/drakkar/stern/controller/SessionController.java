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
import drakkar.oar.Seeker;
import drakkar.oar.exception.OperationNotExistException;
import drakkar.oar.exception.SessionException;
import drakkar.oar.slice.action.oneway.AMD_Send_sendAMID;
import drakkar.oar.slice.action.twoway.AMD_Get_getAMID;
import drakkar.oar.slice.error.RequestException;
import drakkar.oar.slice.login.RolePrx;
import drakkar.oar.slice.session.SearchSessionPrx;
import drakkar.oar.slice.session.SearchSessionPrxHelper;
import drakkar.oar.slice.session._SearchSessionDisp;
import static drakkar.oar.util.KeySession.*;
import static drakkar.oar.util.KeyTransaction.*;
import static drakkar.oar.util.SeekerAction.*;
import drakkar.stern.facade.event.FacadeListener;
import drakkar.stern.servant.ContainerServant;
import drakkar.stern.servant.SessionServant;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;


public class SessionController extends _SearchSessionDisp {

    public SessionServant servant;
    private SearchSessionPrx searchSessionPrx;
//    private String id = "";
    private String name = "";
    private int count = 1;

    /**
     *
     */
    public SessionController() {
        servant = new SessionServant();
    }

    /**
     *
     * @param comm
     * @param container
     * @param name
     * @param desc
     * @param uuid
     */
    @SuppressWarnings("static-access")
    public SessionController(Communication comm, ContainerServant container, 
            String name, String desc, UUID uuid) {
       
        this.name = name;
        this.servant = new SessionServant(comm, container, name, desc, uuid);
        Ice.ObjectPrx objPrx = comm.getAdapter().add(this, Ice.Util.stringToIdentity(name));
        searchSessionPrx = SearchSessionPrxHelper.uncheckedCast(objPrx);
    }

    /**
     * 
     * @param comm
     * @param container
     * @param name
     * @param desc
     * @param uuid
     * @param listener
     */
    public SessionController(Communication comm, ContainerServant container,
            String name, String desc, UUID uuid, FacadeListener listener) {
      
        this.name = name;
      
        servant = new SessionServant(comm, container,  name, desc, uuid, listener);
        Ice.ObjectPrx objPrx = comm.getAdapter().add(this, Ice.Util.stringToIdentity(name));
        searchSessionPrx = SearchSessionPrxHelper.uncheckedCast(objPrx);
    }

    /**
     *
     * @return
     */
    public SearchSessionPrx getSearchSessionPrx() {
        return searchSessionPrx;
    }

    /**
     *
     * @param sessionPrx
     */
    public void setSearchSessionPrx(SearchSessionPrx sessionPrx) {
        this.searchSessionPrx = sessionPrx;
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
    public SessionServant getServant() {
        return servant;
    }

    /**
     *
     * @param servant
     */
    public void setServant(SessionServant servant) {
        this.servant = servant;
    }

    /**
     * Este método permite la ejecución de una operación con despacho síncrono la
     * cual devuelve un objeto Response serializado.
     *
     * @param request - objeto Request serializado que contiene el nombre de la
     *                  operación a realizar, con sus correspondientes parámetros.
     * @param current - Provee el acceso a la información acerca de la petición
     *                  que actualmente se ejecuta. Es empleado para la deserialización
     *                  del objeto Request.
     *
     * @return  un objeto Response serializado con los resultados de la operación.
     * @throws RequestException - si ocurre alguna exception durante la ejecución
     *                            de la operación
     */
    public byte[] getSAMI(byte[] request, Current current) throws RequestException {
        try {
            Request rqs = Request.arrayToRequest(request, current.adapter.getCommunicator());
            byte[] array = this.getController(rqs);

            return array;

        } catch (IOException | ClassNotFoundException ex) {
            throw new RequestException(ex.getMessage());
        }

    }

    /**
     * Este método permite la ejecución de una operación con despacho asíncrono.
     *
     * @param cb      - objeto de retrollamada, empleado para notificar el éxito
     *                  de la operación junto con el objeto Response serializado
     *                  el cual contiene el resultado de la operación, e en caso
     *                  contrario lanzar una excepción del tipo RequestException.
     * @param request - objeto Request serializado que contiene el nombre de la
     *                  operación a realizar, con sus correspondientes parámetros.
     * @param current - Provee el acceso a la información acerca de la petición
     *                  que actualmente se ejecuta. Es empleado para la deserialización
     *                  del objeto Request.
     *
     * @throws RequestException - si ocurre alguna exception durante la ejecución
     *                            de la operación
     */
    public void getAMID_async(AMD_Get_getAMID cb, byte[] request, Current current) throws RequestException {
        try {

            Request rqs = Request.arrayToRequest(request, current.adapter.getCommunicator());
            byte[] array = this.getController(rqs);

            cb.ice_response(array);

        } catch (IOException | ClassNotFoundException ex) {
            cb.ice_exception(new RequestException(ex.getMessage()));
        }

    }

    /**
     * Este método permite la ejecución de una operación con despacho síncrono.
     *
     * @param request - objeto Request serializado que contiene el nombre de la
     *                  operación a realizar, con sus correspondientes parámetros.
     * @param current - Provee el acceso a la información acerca de la petición
     *                  que actualmente se ejecuta. Es empleado para la deserialización
     *                  del objeto Request.
     *
     * @throws RequestException - si ocurre alguna exception durante la ejecución
     *                            de la operación
     */
    public void sendSAMI(byte[] request, Current current) throws RequestException {
        try {

            Request rqs = Request.arrayToRequest(request, current.adapter.getCommunicator());
            this.sendController(rqs);


        } catch (IOException | ClassNotFoundException ex) {
            throw new RequestException(ex.getMessage());
        }

    }

    /**
     * Este método permite la ejecución de una operación con despacho asíncrono.
     *
     * @param cb      - objeto de retrollamada, empleado para notificar el éxito
     *                  de la operación ó lanzar una excepción en caso contrario
     *                  del tipo RequestException.
     * @param request - objeto Request serializado que contiene el nombre de la
     *                  operación a realizar, con sus correspondientes parámetros.
     * @param current - Provee el acceso a la información acerca de la petición
     *                  que actualmente se ejecuta. Es empleado para la deserialización
     *                  del objeto Request.
     *
     * @throws RequestException - si ocurre alguna exception durante la ejecución
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
        try {
            Map<Object, Object> hash = request.getParameters();
            
            int operation = (Integer) hash.get(OPERATION);
            Response rsp;
            byte[] array = null;
            switch (operation) {


                case GET_ROLEPRX:
                    RolePrx rolePrx = servant.getRolePrx();
                    hash.clear();
                    hash.put(PROXY, rolePrx);
                    rsp = new Response(hash);
                    array = rsp.toArray();
                    return array;

                case IS_AVAILABLE_USER:
                    String userName = hash.get(SEEKER_NICKNAME).toString();
                    boolean flag = servant.isAvailableUser(userName);
                    hash.clear();
                    hash.put(ANSWER, flag);

                    rsp = new Response(hash);
                    array = rsp.toArray();
                    return array;

                case LOGIN_SEEKER:
                    System.out.println(">>>>> LOGIN_SEEKER");
                    String user = hash.get(SEEKER_NICKNAME).toString();
                    String pass = hash.get(SEEKER_PASSWORD).toString();
                    Seeker seeker = servant.login(user, pass);

                    hash.clear();

                    if (seeker == null) {
                        rsp = new Response();
                    } else {

                        hash.put(SEEKER, seeker);
                        rsp = new Response(hash);
                    }

                    array = rsp.toArray();

                    return array;

                case GET_ONLINE_SEEKERS:
                    List<Seeker> members = servant.getOnlineMembers();
                    hash.clear();
                    hash.put(SEEKERS_SESSION, members);

                    rsp = new Response(hash);
                    array = rsp.toArray();
                    return array;

                case CHANGE_PASSWORD:
                    String nick = hash.get(SEEKER_NICKNAME).toString();
                    String oldPass = hash.get(OLD_PASSWORD).toString();
                    String newPass = hash.get(NEW_PASSWORD).toString();

                    boolean ans = servant.changePassword(nick, oldPass, newPass);
                    hash.clear();
                    hash.put(ANSWER, ans);

                    rsp = new Response(hash);
                    array = rsp.toArray();
                    return array;

                case GET_SEEKER_ID:

                    hash.clear();
                    hash.put(SEEKER_ID, count++);

                    rsp = new Response(hash);
                    array = rsp.toArray();
                    return array;

                  // esto es temporal
                 case GET_CHAIRMAN_NAME:

                    String sessionName = hash.get(SESSION_NAME).toString();
                    String chairman = servant.getChairmanName(sessionName);

                    hash.clear();
                    hash.put(CHAIRMAN_NAME, chairman);

                    rsp = new Response(hash);
                    array = rsp.toArray();
                     return array;



                default:
                    throw new OperationNotExistException("Operation Not Exist");
            }
        } catch (SessionException | IOException | OperationNotExistException ex) {
            throw new RequestException(ex.getMessage());
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
     * @throws RequestException
     */
    public void sendController(Request request) throws RequestException {

        Map<Object, Object> hash = request.getParameters();
        int operation = (Integer) hash.get(OPERATION);
        switch (operation) {

            case REGISTER_SEEKER:
                               String userName = hash.get(SEEKER_NAME).toString();
                String description = hash.get(SEEKER_DESCRIPTION).toString();
                String password = hash.get(SEEKER_PASSWORD).toString();
                String email = hash.get(SEEKER_EMAIL).toString();
                String nickName = hash.get(SEEKER_NICKNAME).toString();
                byte[] avatar = (byte[]) hash.get(SEEKER_AVATAR);             
                this.servant.registerSeeker(nickName, userName, description, password, email, avatar);
                break;

            case RECOVER_PASSWORD:
                String nick = hash.get(SEEKER_NICKNAME).toString();
                this.servant.recoverPassword(nick);
                break;

            default:
                throw new RequestException("Operation Not Exist");
        }

    }
}
