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

import drakkar.oar.Communication;
import drakkar.oar.Delegate;
import drakkar.oar.Request;
import drakkar.oar.Response;
import drakkar.oar.callback.GetAMIDCallback;
import drakkar.oar.callback.GetSAMICallback;
import drakkar.oar.exception.InvocationException;
import drakkar.oar.exception.ProxyNotExistException;
import drakkar.oar.slice.container.SessionContainerPrx;
import drakkar.oar.slice.container.SessionContainerPrxHelper;
import drakkar.oar.slice.error.RequestException;
import drakkar.oar.slice.login.AuthenticationPrx;
import drakkar.oar.slice.login.AuthenticationPrxHelper;
import drakkar.oar.slice.session.SearchSessionPrx;
import drakkar.oar.util.Invocation;
import drakkar.oar.util.KeyTransaction;
import drakkar.oar.util.OutputMonitor;
import drakkar.oar.util.SeekerAction;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * Esta clase tiene el objetivo de obtener un objeto proxy(SessionContainerPrx)
 * para que actúe como embajador del correspondiente objeto registrado en el tiempo
 * de ejecución de ICE, pudiendose así ejecutar las operaciones soportadas por estos.
 */
public class DelegateSessionContainer extends Delegate implements Serializable{
    private static final long serialVersionUID = 80000000000018L;

    private SessionContainerPrx containerPrx;

    /**
     * Constructor de la Clase.
     *
     * @param comm  instancia de Communication.
     * @param name  nombre del servidor
     * @param ip    dirección host
     * @param porNumber  puerto por el cual el servidor recibe las peticiones.
     */
    public DelegateSessionContainer(Communication comm, String name, String ip, int porNumber) {
        super(comm, name, ip, porNumber);
    }

    public DelegateSessionContainer(Communication communication, String name, String ip, int porNumber, SessionContainerPrx containerPrx) {
        super(communication, name, ip, porNumber, containerPrx);
        this.containerPrx = containerPrx;

    }

    /**
     * Este método obtiene un proxy del objeto remoto(SessionContainerPrx) para
     * que actúe este actué como embajador del correspondiente objeto registrado
     * en el tiempo de ejecución de Ice.
     * @throws ProxyNotExistException
     */
    public void create() throws ProxyNotExistException {

        // stringToProxy: obtiene un proxy del objeto remoto
        this.proxy = this.communication.getCommunicator().stringToProxy(this.name
                + ":tcp -h " + this.ip + " -p " + this.portNumber);
        if (this.proxy == null) {
            throw new ProxyNotExistException("Invalid Proxy");
        }
        this.containerPrx = SessionContainerPrxHelper.checkedCast(this.proxy);

    }

    public void createMultiListener() throws ProxyNotExistException {

        // stringToProxy: obtiene un proxy del objeto remoto
        this.proxy = this.communication.getCommunicator().stringToProxy(this.name
                + ":tcp -p " + this.portNumber);
        if (this.proxy == null) {
            throw new ProxyNotExistException("Invalid Proxy");
        }
        this.containerPrx = SessionContainerPrxHelper.checkedCast(this.proxy);

    }

    /**
     * Este método obtiene un proxy del objeto remoto(AuthenticationPrx) para que
     * actué este actué como embajador del correspondiente objeto registrado en
     * el tiempo de ejecución de Ice.
     *
     * @param name  nombre del miembro
     *
     * @return instancia de DlgAuthentication
     * @throws ProxyNotExistException
     */
    public DlgAuthentication getDlgAuthentication(String name) throws ProxyNotExistException {

        AuthenticationPrx loginProxy = AuthenticationPrxHelper.checkedCast(this.communication.getCommunicator().stringToProxy(name
                + ":tcp -h " + this.ip + " -p " + this.portNumber));

        if (proxy == null) {
            throw new ProxyNotExistException("Invalid Proxy");

        } else {
            DlgAuthentication authent = new DlgAuthentication(this.communication, name, this.ip, this.portNumber, loginProxy);
            return authent;
        }

    }

    /**
     * Este método obtiene un proxy del objeto remoto(AuthenticationPrx) para que
     * actué este actué como embajador del correspondiente objeto registrado en
     * el tiempo de ejecución de Ice.
     *
     * @param name  nombre del miembro
     *
     * @return instancia de DlgAuthentication
     * @throws ProxyNotExistException
     */
    public DlgAuthentication getDlgAuthenticationMultiListener(String name) throws ProxyNotExistException {

        AuthenticationPrx loginProxy = AuthenticationPrxHelper.checkedCast(this.communication.getCommunicator().stringToProxy(name
                + ":tcp -p " + this.portNumber));

        if (proxy == null) {
            throw new ProxyNotExistException("Invalid Proxy");

        } else {
            DlgAuthentication authent = new DlgAuthentication(this.communication, name, this.ip, this.portNumber, loginProxy);
            return authent;
        }

    }

    /**
     * Este método ejcuta una operación determinada en el servidor, apartir del
     * objeto Request pasado por parámetros y devuelve un correspondiente objeto
     * Response.
     *
     * @param request     Operación a realizar, con sus parámetros de entrada.
     * @param invocation  Especifica el modo de invocación de la operación:
     *                       SYNCHRONOUS_MET_INV       = 0 :Synchronous Method Invocation
     *                       ASYNCHRONOUS_MET_INV      = 1 :Asynchronous Method Invocation
     *                       ASYNCHRONOUS_MET_DISP     = 2 : Asynchronous Method Dispatch
     *                       ASYNCHRONOUS_MET_INV_DISP = 3 :Asynchronous Method Invocation and Dispatch
     *
     * @return un objeto Response con resultados de la operación ejecutada.
     */
    private Response get(Request request, byte invocation) {
        byte[] array = null;
        boolean flag = false;
       
        try {
            switch (invocation) {
                case (Invocation.SYNCHRONOUS_METHOD_INVOCATION):         // case: 0
                    ////////////////////////////////////////////////////////////
                    containerPrx.disconnect();
                    array = this.containerPrx.getSAMI(request.toArray());
                    return Response.arrayToResponse(array, communication.getCommunicator());
                ////////////////////////////////////////////////////////////
                case (Invocation.ASYNCHRONOUS_METHOD_INVOCATION):        // case: 1
                    ////////////////////////////////////////////////////////////
                    GetSAMICallback callbackSAMI = new GetSAMICallback(communication.getCommunicator());
                    flag = this.containerPrx.getSAMI_async(callbackSAMI, request.toArray());
                  
                    return callbackSAMI.getResponse();
                ////////////////////////////////////////////////////////////
                case (Invocation.ASYNCHRONOUS_METHOD_DISPATCH):       // case: 2
                    ////////////////////////////////////////////////////////////
                    array = this.containerPrx.getAMID(request.toArray());

                    return Response.arrayToResponse(array, communication.getCommunicator());
                ////////////////////////////////////////////////////////////
                case (Invocation.ASYNCHRONOUS_METHOD_INVOCATION_DISPATCH):   // case: 3
                    ////////////////////////////////////////////////////////////
                    GetAMIDCallback callbackAMID = new GetAMIDCallback(communication.getCommunicator());
                    flag = this.containerPrx.getAMID_async(callbackAMID, request.toArray());
                   
                    return callbackAMID.getResponse();
                ////////////////////////////////////////////////////////////
                default:
                    throw new InvocationException("Invalid invocation");
            }

        } catch (ClassNotFoundException | InvocationException | RequestException | IOException ex) {
            OutputMonitor.printStream("", ex);
        }

        return null;
    }

    /**
     * Este método reemplaza el objeto SessionContainerPrx de la clase.
     *
     * @param containerPrx  nuevo SessionContainerPrx.
     */
    public void setSessionContainerPrx(SessionContainerPrx containerPrx) {
        this.containerPrx = containerPrx;
    }

    /**
     * Este método devuelve el objeto SessionContainerPrx de la clase.
     *
     * @return objeto SessionContainerPrx.
     */
    public SessionContainerPrx getSessionContainerPrx() {
        return this.containerPrx;
    }

    /**
     *
     * @param name
     * @param invocation
     * @return
     */
    public DelegateSession getNoPersistentSessionByName(String name, byte invocation) {

        Map<Object, Object> hash = new HashMap<>(2);
        hash.put(KeyTransaction.OPERATION, SeekerAction.GET_NAME_NO_PERSISTENT_SESSION);
        hash.put(KeyTransaction.SESSION_NAME, name);
        Request rqs = new Request(hash);

        Response rsp = this.get(rqs, invocation);
        hash = rsp.getParameters();
        SearchSessionPrx searchSession = (SearchSessionPrx) hash.get(KeyTransaction.PROXY);
        DelegateSession session = new DelegateSession(searchSession);
        return session;
    }

    /**
     *
     * @param name
     * @param invocation
     * @return
     */
    public DelegateSession getNameNoPersistentSession(String name, byte invocation) {

        Map<Object, Object> hash = new HashMap<>(2);
        hash.put(KeyTransaction.OPERATION, SeekerAction.GET_NAME_NO_PERSISTENT_SESSION);
        hash.put(KeyTransaction.SESSION_NAME, name);
        Request rqs = new Request(hash);

        Response rsp = this.get(rqs, invocation);
        hash = rsp.getParameters();
        SearchSessionPrx searchSession = (SearchSessionPrx) hash.get(KeyTransaction.PROXY);

        DelegateSession session = new DelegateSession(searchSession);
        return session;

    }

    /**
     *
     * @param name
     * @param name
     * @param description
     * @param invocation
     * @return
     */
    public DelegateSession createNoPersistentSession(String id, String name, String description, byte invocation) {

        Map<Object, Object> hash = new HashMap<>(4);
        hash.put(KeyTransaction.OPERATION, SeekerAction.CREATE_COLLAB_SESSION);
        hash.put(KeyTransaction.SESSION_ID, id);
        hash.put(KeyTransaction.SESSION_NAME, name);
        hash.put(KeyTransaction.SESSION_DESCRIPTION, description);
        Request rqs = new Request(hash);

        Response rsp = this.get(rqs, invocation);
        hash = rsp.getParameters();
        SearchSessionPrx searchSession = (SearchSessionPrx) hash.get(KeyTransaction.PROXY);

        DelegateSession session = new DelegateSession(searchSession);
        return session;


    }

    /**
     *
     * @param name
     * @param invocation
     * @return
     */
    public boolean removeIdNoPersistSession(String id, byte invocation) {
        Map<Object, Object> hash = new HashMap<>(2);
        hash.put(KeyTransaction.OPERATION, SeekerAction.REMOVE_ID_NO_PERSISTENT_SESSION);
        hash.put(KeyTransaction.SESSION_ID, id);
        Request rqs = new Request(hash);

        Response rsp = this.get(rqs, invocation);
        hash = rsp.getParameters();
        boolean flag = (Boolean) hash.get(KeyTransaction.RESULT);

        return flag;
    }

    /**
     *
     * @param name
     * @param invocation
     * @return
     */
    public boolean removeNameNoPersistentSession(String name, byte invocation) {
        Map<Object, Object> hash = new HashMap<>(2);
        hash.put(KeyTransaction.OPERATION, SeekerAction.REMOVE_NAME_NO_PERSISTENT_SESSION);
        hash.put(KeyTransaction.SESSION_NAME, name);
        Request rqs = new Request(hash);

        Response rsp = this.get(rqs, invocation);
        hash = rsp.getParameters();
        boolean flag = (Boolean) hash.get(KeyTransaction.RESULT);

        return flag;
    }

    /**
     *
     * @param invocation
     * @return
     */
    @SuppressWarnings("unchecked")
    public java.util.ArrayList<String> getIdsNoPersistentSessions(byte invocation) {
        Map<Object, Object> hash = new HashMap<>(2);
        hash.put(KeyTransaction.OPERATION, SeekerAction.GET_IDS_NO_PERSISTENT_SESSIONS);
        Request rqs = new Request(hash);

        Response rsp = this.get(rqs, invocation);
        hash = rsp.getParameters();
        ArrayList<String> ids = (ArrayList<String>) hash.get(KeyTransaction.SESSIONS_IDS);
        return ids;
    }

    /**
     *
     * @param invocation
     * @return
     */
    @SuppressWarnings("unchecked")
    public ArrayList<String> getNamesNoPersistentSessions(byte invocation) {
        Map<Object, Object> hash = new HashMap<>(2);
        hash.put(KeyTransaction.OPERATION, SeekerAction.GET_NAMES_NO_PERSISTENT_SESSIONS);
        Request rqs = new Request(hash);

        Response rsp = this.get(rqs, invocation);
        hash = rsp.getParameters();
        ArrayList<String> names = (ArrayList<String>) hash.get(KeyTransaction.SESSIONS_IDS);
        return names;
    }

}

