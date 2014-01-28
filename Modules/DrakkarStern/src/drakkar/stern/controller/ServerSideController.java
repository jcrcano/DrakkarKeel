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
import drakkar.oar.Documents;
import drakkar.oar.QuerySource;
import drakkar.oar.Request;
import drakkar.oar.Response;
import drakkar.oar.ScorePQT;
import drakkar.oar.Seeker;
import drakkar.oar.exception.AwarenessException;
import drakkar.oar.exception.ChairmanNotExistException;
import drakkar.oar.exception.NotificationException;
import drakkar.oar.exception.OperationNotExistException;
import drakkar.oar.exception.RecommendationException;
import drakkar.oar.exception.ScoreOutOfBoundsException;
import drakkar.oar.exception.SeekerException;
import drakkar.oar.exception.SessionAlreadyRegisterException;
import drakkar.oar.exception.SessionException;
import drakkar.oar.exception.SessionPropertyException;
import drakkar.oar.exception.TrackException;
import drakkar.oar.exception.UserStatusNotSupportedException;
import drakkar.oar.slice.action.oneway.AMD_Send_sendAMID;
import drakkar.oar.slice.action.twoway.AMD_Get_getAMID;
import drakkar.oar.slice.client.ClientSidePrx;
import drakkar.oar.slice.error.RequestException;
import drakkar.oar.slice.server._ServerSideDisp;
import static drakkar.oar.util.KeyMessage.*;
import drakkar.oar.util.KeySession;
import static drakkar.oar.util.KeyTransaction.*;
import drakkar.oar.util.OutputMonitor;
import static drakkar.oar.util.SeekerAction.*;
import drakkar.mast.SearchException;
import drakkar.mast.SearchableException;
import drakkar.stern.ResponseUtilFactory;
import drakkar.stern.callback.NotifyAMICallback;
import drakkar.stern.servant.Servant;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServerSideController extends _ServerSideDisp {

    /**
     *
     */
    protected Servant servant;
    /**
     *
     */
    protected Seeker seeker;
    /**
     *
     */
    protected ClientSidePrx seekerPrx;
    /**
     *
     */
    protected String sessionName = null;
    /**
     *
     */
    protected String otherSessionName = null;
    /**
     *
     */
    protected String message = null;
    /**
     *
     */
    protected Seeker receptor = null;
    /**
     *
     */
    protected ArrayList<Seeker> receptors = null;
    /**
     *
     */
    protected String query = null;
    /**
     *
     */
    protected int searcher = 0;
    /**
     *
     */
    protected String docType = null;
    /**
     *
     */
    protected int field = 0;
    /**
     *
     */
    protected int principle = 0;
    /**
     *
     */
    protected String comments = null;
    /**
     *
     */
    protected ArrayList<Integer> index = null;
    /**
     *
     */
    protected int docIndex = 0;
    /**
     *
     */
    protected byte score = 0;
    /**
     *
     */
    protected int operation;
    /**
     *
     */
    protected int[] fields;
    /**
     *
     */
    protected int[] searchers;
    /**
     *
     */
    protected String[] docTypes;
    /**
     *
     */
    protected boolean caseSensitive;
    /**
     *
     */
    protected String session = "";

    /**
     *
     * @param servant
     * @param seeker
     * @param memberPrx
     * @param sessionName  sesión por defecto del contenedor
     * @throws SessionException     
     * @throws SeekerException
     * @throws IOException
     */
    public ServerSideController(Servant servant, Seeker seeker, ClientSidePrx memberPrx, String sessionName) throws SessionException, SeekerException, IOException {

        this.servant = servant;
        this.seeker = seeker;
        this.seekerPrx = memberPrx;
        this.sessionName = sessionName;
        this.servant.loginSeeker(sessionName, this.seeker, this.seekerPrx);

        if (this.seeker.getRole() == Seeker.ROLE_CHAIRMAN) {
            boolean flag = this.servant.getCollaborativeSessions().containsKey("DefaultSCS");
            if (flag) {

                this.servant.joinCollabSession("DefaultSCS", this.seeker, this.seekerPrx);
                this.seeker.setRole(Seeker.ROLE_CHAIRMAN);
            } else {
                try {

                    this.servant.createCollabSession("DefaultSCS", seeker, 1, 100, KeySession.SESSION_SOFT, KeySession.SESSION_DYNAMIC_AND_OPEN, new ArrayList<Seeker>(), "Search Collaborative Session by default");
                } catch (        SessionAlreadyRegisterException | SessionPropertyException | ChairmanNotExistException ex) {
                    OutputMonitor.printStream("Ivocation", ex);
                }
            }
        }


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
            byte[] array = this.getController(rqs);

            return array;

        } catch (IOException | ClassNotFoundException ex) {
            OutputMonitor.printStream("Ivocation", ex);
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
            OutputMonitor.printStream("Ivocation", ex);
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
    public void sendSAMI(final byte[] request, final Current current) throws RequestException {

//        Thread t = new Thread(new Runnable() {
//
//            public void run() {
        try {
            Request rqs = Request.arrayToRequest(request, current.adapter.getCommunicator());
            sendController(rqs);
        } catch (IOException | ClassNotFoundException ex) {
            OutputMonitor.printStream("Ivocation", ex);
        }
//            }
//        });
//
//        t.start();





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
    public void sendAMID_async(final AMD_Send_sendAMID cb, final byte[] request, final Current current) throws RequestException {
//        Thread t = new Thread(new Runnable() {
//
//            public void run() {
        try {

            Request rqs = Request.arrayToRequest(request, current.adapter.getCommunicator());
            sendController(rqs);
            cb.ice_response();

        } catch (IOException | ClassNotFoundException ex) {
            OutputMonitor.printStream("Ivocation", ex);
            cb.ice_exception(new RequestException(ex.getMessage()));
        }
//            }
//        });
//        t.start();



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
            operation = (Integer) hash.get(OPERATION);
            switch (operation) {
                case GET_ONLINE_SEEKERS:
                    session = hash.get(SESSION_NAME).toString();
                    List<Seeker> members = this.servant.getOnlineMembers(session);
                    Map<Object, Object> hashrsp = new HashMap<>(1);
                    hashrsp.put(SEEKERS_SESSION, members);
                    Response rsp = new Response(hashrsp);
                    byte[] array = rsp.toArray();
                    return array;

                default:
                    throw new OperationNotExistException("Operation Not Exist");
            }
        } catch (IOException | OperationNotExistException | SessionException ex) {
            OutputMonitor.printStream("Ivocation", ex);
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
    @SuppressWarnings({"unchecked", "unchecked", "unchecked", "unchecked", "unchecked", "unchecked", "unchecked"})
    public void sendController(Request request) {
        operation = (Integer) request.get(OPERATION);

        try {
            switch (operation) {
                ////////////////////////////////////////////////////////////////////
                case CREATE_COLLAB_SESSION:
                    OutputMonitor.printLine("Operation:[CREATE_COLLAB_SESSION] requesting.. ", OutputMonitor.INFORMATION_MESSAGE);
                    session = request.get(SESSION_NAME).toString();
                    int membersMinNumber = (Integer) request.get(MEMBERS_MIN_NUMBER);
                    int membersMaxNumber = (Integer) request.get(MEMBERS_MAX_NUMBER);
                    int integrityCriteria = (Integer) request.get(INTEGRITY_CRITERIA);
                    int membershipPolicy = (Integer) request.get(MEMBERSHIP_POLICY);
                    String description = (String) request.get(SESSION_DESCRIPTION);//TODO: Humberto revisar
                    ArrayList<Seeker> members = (ArrayList<Seeker>) request.get(SEEKERS_SESSION);
                    this.servant.createCollabSession(session, seeker, membersMinNumber, membersMaxNumber, integrityCriteria, membershipPolicy, members, description);
                    break;

                case JOIN_COLLAB_SESSION:
                    OutputMonitor.printLine("Operation:[JOIN_COLLAB_SESSION] requesting.. ", OutputMonitor.INFORMATION_MESSAGE);
                    session = request.get(SESSION_NAME).toString();
                    this.servant.joinCollabSession(session, seeker, seekerPrx);
                    break;

                case ENTER_COLLAB_SESSION:
                    OutputMonitor.printLine("Operation:[ENTER_COLLAB_SESSION] requesting.. ", OutputMonitor.INFORMATION_MESSAGE);
                    session = request.get(SESSION_NAME).toString();
                    this.servant.enterCollabSession(session, seeker, seekerPrx);
                    break;

                case CLOSE_COLLAB_SESSION:
                    OutputMonitor.printLine("Operation:[CLOSE_COLLAB_SESSION] requesting.. ", OutputMonitor.INFORMATION_MESSAGE);
                    session = request.get(SESSION_NAME).toString();
                    this.servant.deleteCollabSession(session, seeker, seekerPrx);
                    break;


                case DECLINE_SEEKER_COLLAB_SESSION:
                    OutputMonitor.printLine("Operation:[DECLINE_SEEKER_COLLAB_SESSION] requesting.. ", OutputMonitor.INFORMATION_MESSAGE);
                    session = request.get(SESSION_NAME).toString();
                    this.servant.declineSeekerCollabSession(session, seeker, seekerPrx);
                    break;

                case UPDATE_STATE_SEEKER:
                    OutputMonitor.printLine("Operation:[UPDATE_STATE_SEEKER] requesting.. ", OutputMonitor.INFORMATION_MESSAGE);
                    session = request.get(SESSION_NAME).toString();
                    int state = (Integer) request.get(SEEKER_STATE);
                    this.servant.updateStateSeeker(session, seeker, state);
                    seeker.setState(state);
                    break;

                case UPDATE_AVATAR_SEEKER:
                    OutputMonitor.printLine("Operation:[UPDATE_AVATAR_SEEKER] requesting.. ", OutputMonitor.INFORMATION_MESSAGE);
                    session = request.get(SESSION_NAME).toString();
                    byte[] avatar = ( byte[]) request.get(SEEKER_AVATAR);
                    this.servant.updateAvatarSeeker(session, seeker, avatar);
                    seeker.setAvatar(avatar);
                    break;

                ////////////////////////////////////////////////////////////////////
                case SEND_SESSION_MESSAGE:
                    OutputMonitor.printLine("Operation:[SEND_SESSION_MESSAGE] requesting.. ", OutputMonitor.INFORMATION_MESSAGE);
                    session = request.get(SESSION_NAME).toString();
                    message = request.get(MESSAGE).toString();
                    this.servant.sendMessage(session, seeker, message);

                    break;

                case SEND_SINGLE_MESSAGE:
                    OutputMonitor.printLine("Operation:[SEND_SINGLE_MESSAGE] requesting.. ", OutputMonitor.INFORMATION_MESSAGE);
                    session = request.get(SESSION_NAME).toString();
                    receptor = (Seeker) request.get(MEMBER_RECEPTOR);
                    message = request.get(MESSAGE).toString();
                    this.servant.sendMessage(session, seeker, receptor, message);

                    break;
                case SEND_GROUP_MESSAGE:
                    OutputMonitor.printLine("Operation:[SEND_GROUP_MESSAGE] requesting.. ", OutputMonitor.INFORMATION_MESSAGE);
                    session = request.get(SESSION_NAME).toString();
                    receptors = (ArrayList<Seeker>) request.get(SEEKERS_RECEPTORS);
                    message = request.get(MESSAGE).toString();
                    this.servant.sendMessage(session, seeker, receptors, message);

                    break;
                ////////////////////////////////////////////////////////////////////
                case SEARCH_QRY__SS_SSSPLIT:
                    OutputMonitor.printLine("Operation:[SEARCH_QRY__SS_SSSPLIT] requesting.. ", OutputMonitor.INFORMATION_MESSAGE);
                    session = request.get(SESSION_NAME).toString();
                    query = request.get(QUERY).toString();
                    searcher = (Integer) request.get(SEARCHER);
                    principle = (Integer) request.get(SEARCH_PRINCIPLE);
                    caseSensitive = (Boolean) request.get(CASE_SENTITIVE);

                    this.servant.executeSearch(session, query, searcher, principle, caseSensitive, seeker, seekerPrx);

                    break;

                case SEARCH_QRY_FLD__SS_SSSPLIT:
                    OutputMonitor.printLine("Operation:[SEARCH_QRY_FLD__SS_SSSPLIT] requesting.. ", OutputMonitor.INFORMATION_MESSAGE);
                    session = request.get(SESSION_NAME).toString();
                    query = request.get(QUERY).toString();
                    field = (Integer) request.get(FIELD);
                    searcher = (Integer) request.get(SEARCHER);
                    principle = (Integer) request.get(SEARCH_PRINCIPLE);
                    caseSensitive = (Boolean) request.get(CASE_SENTITIVE);

                    this.servant.executeSearch(session, query, field, searcher, principle, caseSensitive, seeker, seekerPrx);

                    break;

                case SEARCH_QRY_FLDS__SS_SSSPLIT:
                    OutputMonitor.printLine("Operation:[SEARCH_QRY_FLDS__SS_SSSPLIT] requesting.. ", OutputMonitor.INFORMATION_MESSAGE);
                    session = request.get(SESSION_NAME).toString();
                    query = request.get(QUERY).toString();
                    fields = (int[]) request.get(FIELDS);
                    searcher = (Integer) request.get(SEARCHER);
                    principle = (Integer) request.get(SEARCH_PRINCIPLE);
                    caseSensitive = (Boolean) request.get(CASE_SENTITIVE);

                    this.servant.executeSearch(session, query, fields, searcher, principle, caseSensitive, seeker, seekerPrx);

                    break;

                case SEARCH_QRY_DOCTYPE__SS_SSSPLIT:
                    OutputMonitor.printLine("Operation:[SEARCH_QRY_DOCTYPE__SS_SSSPLIT] requesting.. ", OutputMonitor.INFORMATION_MESSAGE);
                    session = request.get(SESSION_NAME).toString();
                    query = request.get(QUERY).toString();
                    docType = request.get(DOC_TYPE).toString();
                    searcher = (Integer) request.get(SEARCHER);
                    principle = (Integer) request.get(SEARCH_PRINCIPLE);
                    caseSensitive = (Boolean) request.get(CASE_SENTITIVE);

                    this.servant.executeSearch(session, query, docType, searcher, principle, caseSensitive, seeker, seekerPrx);

                    break;

                case SEARCH_QRY_DOCTYPES__SS_SSSPLIT:
                    OutputMonitor.printLine("Operation:[SEARCH_QRY_DOCTYPES__SS_SSSPLIT] requesting.. ", OutputMonitor.INFORMATION_MESSAGE);
                    session = request.get(SESSION_NAME).toString();
                    query = request.get(QUERY).toString();
                    docTypes = (String[]) request.get(DOC_TYPES);
                    searcher = (Integer) request.get(SEARCHER);
                    principle = (Integer) request.get(SEARCH_PRINCIPLE);
                    caseSensitive = (Boolean) request.get(CASE_SENTITIVE);

                    this.servant.executeSearch(session, query, docTypes, searcher, principle, caseSensitive, seeker, seekerPrx);

                    break;

                case SEARCH_QRY_DOCTYPE_FLD__SS_SSSPLIT:
                    OutputMonitor.printLine("Operation:[SEARCH_QRY_DOCTYPE_FLD__SS_SSSPLIT] requesting.. ", OutputMonitor.INFORMATION_MESSAGE);
                    session = request.get(SESSION_NAME).toString();
                    query = request.get(QUERY).toString();
                    docType = request.get(DOC_TYPE).toString();
                    field = (Integer) request.get(FIELD);
                    searcher = (Integer) request.get(SEARCHER);
                    principle = (Integer) request.get(SEARCH_PRINCIPLE);
                    caseSensitive = (Boolean) request.get(CASE_SENTITIVE);

                    this.servant.executeSearch(session, query, docType, field, searcher, principle, caseSensitive, seeker, seekerPrx);

                    break;

                case SEARCH_QRY_DOCTYPE_FLDS__SS_SSSPLIT:
                    OutputMonitor.printLine("Operation:[SEARCH_QRY_DOCTYPE_FLDS__SS_SSSPLIT] requesting.. ", OutputMonitor.INFORMATION_MESSAGE);
                    session = request.get(SESSION_NAME).toString();
                    query = request.get(QUERY).toString();
                    docType = request.get(DOC_TYPE).toString();
                    fields = (int[]) request.get(FIELDS);
                    searcher = (Integer) request.get(SEARCHER);
                    principle = (Integer) request.get(SEARCH_PRINCIPLE);
                    caseSensitive = (Boolean) request.get(CASE_SENTITIVE);

                    this.servant.executeSearch(session, query, docType, fields, searcher, principle, caseSensitive, seeker, seekerPrx);
                    break;

                case SEARCH_QRY_DOCTYPES_FLD__SS_SSSPLIT:
                    OutputMonitor.printLine("Operation:[SEARCH_QRY_DOCTYPES_FLD__SS_SSSPLIT] requesting.. ", OutputMonitor.INFORMATION_MESSAGE);
                    session = request.get(SESSION_NAME).toString();
                    query = request.get(QUERY).toString();
                    docTypes = (String[]) request.get(DOC_TYPES);
                    field = (Integer) request.get(FIELD);
                    searcher = (Integer) request.get(SEARCHER);
                    principle = (Integer) request.get(SEARCH_PRINCIPLE);
                    caseSensitive = (Boolean) request.get(CASE_SENTITIVE);

                    this.servant.executeSearch(session, query, docTypes, field, searcher, principle, caseSensitive, seeker, seekerPrx);
                    break;

                case SEARCH_QRY_DOCTYPES_FLDS__SS_SSSPLIT:
                    OutputMonitor.printLine("Operation:[SEARCH_QRY_DOCTYPES_FLDS__SS_SSSPLIT] requesting.. ", OutputMonitor.INFORMATION_MESSAGE);
                    session = request.get(SESSION_NAME).toString();
                    query = request.get(QUERY).toString();
                    docTypes = (String[]) request.get(DOC_TYPES);
                    fields = (int[]) request.get(FIELDS);
                    searcher = (Integer) request.get(SEARCHER);
                    principle = (Integer) request.get(SEARCH_PRINCIPLE);
                    caseSensitive = (Boolean) request.get(CASE_SENTITIVE);

                    this.servant.executeSearch(session, query, docTypes, fields, searcher, principle, caseSensitive, seeker, seekerPrx);
                    break;

                case SEARCH_QRY__MS_MSSPLIT_MSSWITCH:
                    OutputMonitor.printLine("Operation:[SEARCH_QRY__MS_MSSPLIT_MSSWITCH] requesting.. ", OutputMonitor.INFORMATION_MESSAGE);
                    session = request.get(SESSION_NAME).toString();
                    principle = (Integer) request.get(SEARCH_PRINCIPLE);
                    query = request.get(QUERY).toString();
                    caseSensitive = (Boolean) request.get(CASE_SENTITIVE);

                    this.servant.executeSearch(session, principle, query, caseSensitive, seeker, seekerPrx);

                    break;

                case SEARCH_QRY_FLD__MS_MSSPLIT_MSSWITCH:
                    OutputMonitor.printLine("Operation:[SEARCH_QRY_FLD__MS_MSSPLIT_MSSWITCH] requesting.. ", OutputMonitor.INFORMATION_MESSAGE);
                    session = request.get(SESSION_NAME).toString();
                    principle = (Integer) request.get(SEARCH_PRINCIPLE);
                    query = request.get(QUERY).toString();
                    field = (Integer) request.get(FIELD);
                    caseSensitive = (Boolean) request.get(CASE_SENTITIVE);

                    this.servant.executeSearch(session, principle, query, field, caseSensitive, seeker, seekerPrx);

                    break;

                case SEARCH_QRY_FLDS__MS_MSSPLIT_MSSWITCH:
                    OutputMonitor.printLine("Operation:[SEARCH_QRY_FLDS__MS_MSSPLIT_MSSWITCH] requesting.. ", OutputMonitor.INFORMATION_MESSAGE);
                    session = request.get(SESSION_NAME).toString();
                    principle = (Integer) request.get(SEARCH_PRINCIPLE);
                    query = request.get(QUERY).toString();
                    fields = (int[]) request.get(FIELDS);
                    caseSensitive = (Boolean) request.get(CASE_SENTITIVE);

                    this.servant.executeSearch(session, principle, query, fields, caseSensitive, seeker, seekerPrx);

                    break;
                /////////////////////////////////////////////////////////////////
                case SEARCH_QRY_DOCTYPE__MS_MSSPLIT_MSSWITCH:
                    OutputMonitor.printLine("Operation:[SEARCH_QRY_DOCTYPE__MS_MSSPLIT_MSSWITCH] requesting.. ", OutputMonitor.INFORMATION_MESSAGE);
                    session = request.get(SESSION_NAME).toString();
                    principle = (Integer) request.get(SEARCH_PRINCIPLE);
                    query = request.get(QUERY).toString();
                    docType = request.get(DOC_TYPE).toString();
                    caseSensitive = (Boolean) request.get(CASE_SENTITIVE);

                    this.servant.executeSearch(session, principle, query, docType, caseSensitive, seeker, seekerPrx);

                    break;

                case SEARCH_QRY_DOCTYPES__MS_MSSPLIT_MSSWITCH:
                    OutputMonitor.printLine("Operation:[SEARCH_QRY_DOCTYPES__MS_MSSPLIT_MSSWITCH] requesting.. ", OutputMonitor.INFORMATION_MESSAGE);
                    session = request.get(SESSION_NAME).toString();
                    principle = (Integer) request.get(SEARCH_PRINCIPLE);
                    query = request.get(QUERY).toString();
                    docTypes = (String[]) request.get(DOC_TYPES);
                    caseSensitive = (Boolean) request.get(CASE_SENTITIVE);

                    this.servant.executeSearch(session, principle, query, docTypes, caseSensitive, seeker, seekerPrx);

                    break;
                /////////////////////////////////////////////////////////////////////////////////////////
                case SEARCH_QRY_DOCTYPE_FLD__MS_MSSPLIT_MSSWITCH:
                    OutputMonitor.printLine("Operation:[SEARCH_QRY_DOCTYPE_FLD__MS_MSSPLIT_MSSWITCH] requesting.. ", OutputMonitor.INFORMATION_MESSAGE);
                    session = request.get(SESSION_NAME).toString();
                    principle = (Integer) request.get(SEARCH_PRINCIPLE);
                    query = request.get(QUERY).toString();
                    docType = request.get(DOC_TYPE).toString();
                    field = (Integer) request.get(FIELD);
                    caseSensitive = (Boolean) request.get(CASE_SENTITIVE);

                    this.servant.executeSearch(session, principle, query, docType, field, caseSensitive, seeker, seekerPrx);

                    break;
                case SEARCH_QRY_DOCTYPE_FLDS__MS_MSSPLIT_MSSWITCH:
                    OutputMonitor.printLine("Operation:[SEARCH_QRY_DOCTYPE_FLDS__MS_MSSPLIT_MSSWITCH] requesting.. ", OutputMonitor.INFORMATION_MESSAGE);
                    session = request.get(SESSION_NAME).toString();
                    principle = (Integer) request.get(SEARCH_PRINCIPLE);
                    query = request.get(QUERY).toString();
                    docType = request.get(DOC_TYPE).toString();
                    fields = (int[]) request.get(FIELDS);
                    caseSensitive = (Boolean) request.get(CASE_SENTITIVE);

                    this.servant.executeSearch(session, principle, query, docType, fields, caseSensitive, seeker, seekerPrx);

                    break;

                case SEARCH_QRY_DOCTYPES_FLD__MS_MSSPLIT_MSSWITCH:
                    OutputMonitor.printLine("Operation:[SEARCH_QRY_DOCTYPES_FLD__MS_MSSPLIT_MSSWITCH] requesting.. ", OutputMonitor.INFORMATION_MESSAGE);
                    session = request.get(SESSION_NAME).toString();
                    principle = (Integer) request.get(SEARCH_PRINCIPLE);
                    query = request.get(QUERY).toString();
                    docTypes = (String[]) request.get(DOC_TYPES);
                    field = (Integer) request.get(FIELD);
                    caseSensitive = (Boolean) request.get(CASE_SENTITIVE);

                    this.servant.executeSearch(session, principle, query, docTypes, field, caseSensitive, seeker, seekerPrx);

                    break;

                case SEARCH_QRY_DOCTYPES_FLDS__MS_MSSPLIT_MSSWITCH:
                    OutputMonitor.printLine("Operation:[SEARCH_QRY_DOCTYPES_FLDS__MS_MSSPLIT_MSSWITCH] requesting.. ", OutputMonitor.INFORMATION_MESSAGE);
                    session = request.get(SESSION_NAME).toString();
                    principle = (Integer) request.get(SEARCH_PRINCIPLE);
                    query = request.get(QUERY).toString();
                    docTypes = (String[]) request.get(DOC_TYPES);
                    fields = (int[]) request.get(FIELDS);
                    caseSensitive = (Boolean) request.get(CASE_SENTITIVE);

                    this.servant.executeSearch(session, principle, query, docTypes, fields, caseSensitive, seeker, seekerPrx);

                    break;

                case SEARCH_QRY__SEARCHERS__MS_MSSPLIT_MSSWITCH:
                    OutputMonitor.printLine("Operation:[SEARCH_QRY__SEARCHERS__MS_MSSPLIT_MSSWITCH] requesting.. ", OutputMonitor.INFORMATION_MESSAGE);
                    session = request.get(SESSION_NAME).toString();
                    principle = (Integer) request.get(SEARCH_PRINCIPLE);
                    searchers = (int[]) request.get(SEARCHERS);
                    query = request.get(QUERY).toString();
                    caseSensitive = (Boolean) request.get(CASE_SENTITIVE);

                    this.servant.executeSearch(session, principle, searchers, query, caseSensitive, seeker, seekerPrx);

                    break;

                case SEARCH_QRY_FLD__SEARCHERS__MS_MSSPLIT_MSSWITCH:
                    OutputMonitor.printLine("Operation:[SEARCH_QRY_FLD__SEARCHERS__MS_MSSPLIT_MSSWITCH] requesting.. ", OutputMonitor.INFORMATION_MESSAGE);
                    session = request.get(SESSION_NAME).toString();
                    principle = (Integer) request.get(SEARCH_PRINCIPLE);
                    searchers = (int[]) request.get(SEARCHERS);
                    query = request.get(QUERY).toString();
                    field = (Integer) request.get(FIELD);
                    caseSensitive = (Boolean) request.get(CASE_SENTITIVE);

                    this.servant.executeSearch(session, principle, searchers, query, field, caseSensitive, seeker, seekerPrx);

                    break;

                case SEARCH_QRY_FLDS__SEARCHERS__MS_MSSPLIT_MSSWITCH:
                    OutputMonitor.printLine("Operation:[SEARCH_QRY_FLDS__SEARCHERS__MS_MSSPLIT_MSSWITCH] requesting.. ", OutputMonitor.INFORMATION_MESSAGE);
                    session = request.get(SESSION_NAME).toString();
                    principle = (Integer) request.get(SEARCH_PRINCIPLE);
                    searchers = (int[]) request.get(SEARCHERS);
                    query = request.get(QUERY).toString();
                    fields = (int[]) request.get(FIELDS);
                    caseSensitive = (Boolean) request.get(CASE_SENTITIVE);

                    this.servant.executeSearch(session, principle, searchers, query, fields, caseSensitive, seeker, seekerPrx);

                    break;

                case SEARCH_QRY_DOCTYPE__SEARCHERS__MS_MSSPLIT_MSSWITCH:
                    OutputMonitor.printLine("Operation:[SEARCH_QRY_DOCTYPE__SEARCHERS__MS_MSSPLIT_MSSWITCH] requesting.. ", OutputMonitor.INFORMATION_MESSAGE);
                    session = request.get(SESSION_NAME).toString();
                    principle = (Integer) request.get(SEARCH_PRINCIPLE);
                    searchers = (int[]) request.get(SEARCHERS);
                    query = request.get(QUERY).toString();
                    docType = request.get(DOC_TYPE).toString();
                    caseSensitive = (Boolean) request.get(CASE_SENTITIVE);

                    this.servant.executeSearch(session, principle, searchers, query, docType, caseSensitive, seeker, seekerPrx);

                    break;

                case SEARCH_QRY_DOCTYPES__SEARCHERS__MS_MSSPLIT_MSSWITCH:
                    OutputMonitor.printLine("Operation:[SEARCH_QRY_DOCTYPES__SEARCHERS__MS_MSSPLIT_MSSWITCH] requesting.. ", OutputMonitor.INFORMATION_MESSAGE);
                    session = request.get(SESSION_NAME).toString();
                    principle = (Integer) request.get(SEARCH_PRINCIPLE);
                    searchers = (int[]) request.get(SEARCHERS);
                    query = request.get(QUERY).toString();
                    docTypes = (String[]) request.get(DOC_TYPES);
                    caseSensitive = (Boolean) request.get(CASE_SENTITIVE);

                    this.servant.executeSearch(session, principle, searchers, query, docTypes, caseSensitive, seeker, seekerPrx);

                    break;

                case SEARCH_QRY_DOCTYPE_FLD__SEARCHERS__MS_MSSPLIT_MSSWITCH:
                    OutputMonitor.printLine("Operation:[SEARCH_QRY_DOCTYPE_FLD__SEARCHERS__MS_MSSPLIT_MSSWITCH] requesting.. ", OutputMonitor.INFORMATION_MESSAGE);
                    session = request.get(SESSION_NAME).toString();
                    principle = (Integer) request.get(SEARCH_PRINCIPLE);
                    searchers = (int[]) request.get(SEARCHERS);
                    query = request.get(QUERY).toString();
                    docType = request.get(DOC_TYPE).toString();
                    field = (Integer) request.get(FIELD);
                    caseSensitive = (Boolean) request.get(CASE_SENTITIVE);

                    this.servant.executeSearch(session, principle, searchers, query, docType, field, caseSensitive, seeker, seekerPrx);

                    break;

                case SEARCH_QRY_DOCTYPE_FLDS__SEARCHERS__MS_MSSPLIT_MSSWITCH:
                    OutputMonitor.printLine("Operation:[SEARCH_QRY_DOCTYPE_FLDS__SEARCHERS__MS_MSSPLIT_MSSWITCH] requesting.. ", OutputMonitor.INFORMATION_MESSAGE);
                    session = request.get(SESSION_NAME).toString();
                    principle = (Integer) request.get(SEARCH_PRINCIPLE);
                    searchers = (int[]) request.get(SEARCHERS);
                    query = request.get(QUERY).toString();
                    docType = request.get(DOC_TYPE).toString();
                    fields = (int[]) request.get(FIELDS);
                    caseSensitive = (Boolean) request.get(CASE_SENTITIVE);

                    this.servant.executeSearch(session, principle, searchers, query, docType, fields, caseSensitive, seeker, seekerPrx);

                    break;

                case SEARCH_QRY_DOCTYPES_FLD__SEARCHERS__MS_MSSPLIT_MSSWITCH:
                    OutputMonitor.printLine("Operation:[SEARCH_QRY_DOCTYPES_FLD__SEARCHERS__MS_MSSPLIT_MSSWITCH] requesting.. ", OutputMonitor.INFORMATION_MESSAGE);
                    session = request.get(SESSION_NAME).toString();
                    principle = (Integer) request.get(SEARCH_PRINCIPLE);
                    searchers = (int[]) request.get(SEARCHERS);
                    query = request.get(QUERY).toString();
                    docTypes = (String[]) request.get(DOC_TYPES);
                    field = (Integer) request.get(FIELD);
                    caseSensitive = (Boolean) request.get(CASE_SENTITIVE);

                    this.servant.executeSearch(session, principle, searchers, query, docTypes, field, caseSensitive, seeker, seekerPrx);

                    break;

                case SEARCH_QRY_DOCTYPES_FLDS__SEARCHERS__MS_MSSPLIT_MSSWITCH:
                    OutputMonitor.printLine("Operation:[SEARCH_QRY_DOCTYPES_FLDS__SEARCHERS__MS_MSSPLIT_MSSWITCH] requesting.. ", OutputMonitor.INFORMATION_MESSAGE);
                    session = request.get(SESSION_NAME).toString();
                    principle = (Integer) request.get(SEARCH_PRINCIPLE);
                    searchers = (int[]) request.get(SEARCHERS);
                    query = request.get(QUERY).toString();
                    docTypes = (String[]) request.get(DOC_TYPES);
                    fields = (int[]) request.get(FIELDS);
                    caseSensitive = (Boolean) request.get(CASE_SENTITIVE);

                    this.servant.executeSearch(session, principle, searchers, query, docTypes, fields, caseSensitive, seeker, seekerPrx);

                    break;

                case SEARCH_SVN_QRY_FILE_SORT_MODIFIED:
                    OutputMonitor.printLine("Operation:[SEARCH_SVN_QRY_FILE_SORT_MODIFIED] requesting.. ", OutputMonitor.INFORMATION_MESSAGE);
                    session = request.get(SESSION_NAME).toString();
                    query = request.get(QUERY).toString();
                    String svnRepository = request.get(SVN_REPOSITORY).toString();
                    String fileType = request.get(FILE_TYPE).toString();
                    String sort = request.get(SORT_TYPE).toString();
                    String lastmodified = request.get(LAST_MODIFIED).toString();
                    boolean fileBody = (Boolean) request.get(ONLY_FILE_BODY);

                    this.servant.executeSearch(session, query, svnRepository, fileType, sort, lastmodified, fileBody, seeker, seekerPrx);
                    break;

                ///////////////////////////////////////////////////////////////////
                case RECOMMEND_SESSION_RESULTS:
                    OutputMonitor.printLine("Operation:[RECOMMEND_SESSION_RESULTS] requesting.. ", OutputMonitor.INFORMATION_MESSAGE);
                    session = request.get(SESSION_NAME).toString();
                    comments = request.get(COMMENT).toString();
                    QuerySource data = (QuerySource) request.get(QUERY_SOURCE);
                    this.servant.recommendResults(session, seeker, comments, data);

                    break;

                case RECOMMEND_SESSION_SELECTION_RESULTS:
                    OutputMonitor.printLine("Operation:[RECOMMEND_SESSION_SELECTION_RESULTS] requesting.. ", OutputMonitor.INFORMATION_MESSAGE);
                    session = request.get(SESSION_NAME).toString();
                    Documents docs = (Documents) request.get(DOCUMENTS);
                    comments = request.get(COMMENT).toString();
                    QuerySource data1 = (QuerySource) request.get(QUERY_SOURCE);
                    this.servant.recommendResults(session, seeker, comments, docs, data1);

                    break;

                case RECOMMEND_SINGLE_RESULTS:
                    OutputMonitor.printLine("Operation:[RECOMMEND_SINGLE_RESULTS] requesting.. ", OutputMonitor.INFORMATION_MESSAGE);
                    session = request.get(SESSION_NAME).toString();
                    receptor = (Seeker) request.get(MEMBER_RECEPTOR);
                    comments = request.get(COMMENT).toString();
                    QuerySource data2 = (QuerySource) request.get(QUERY_SOURCE);
                    this.servant.recommendResults(session, seeker, receptor, comments, data2);

                    break;

                case RECOMMEND_SINGLE_SELECTION_RESULTS:
                    OutputMonitor.printLine("Operation:[RECOMMEND_SINGLE_SELECTION_RESULTS] requesting.. ", OutputMonitor.INFORMATION_MESSAGE);
                    session = request.get(SESSION_NAME).toString();
                    receptor = (Seeker) request.get(MEMBER_RECEPTOR);
                    Documents docs1 = (Documents) request.get(DOCUMENTS);
                    comments = request.get(COMMENT).toString();
                    QuerySource data3 = (QuerySource) request.get(QUERY_SOURCE);
                    this.servant.recommendResults(session, seeker, receptor, docs1, comments, data3);

                    break;

                case RECOMMEND_GROUP_RESULTS:
                    OutputMonitor.printLine("Operation:[RECOMMEND_GROUP_RESULTS] requesting.. ", OutputMonitor.INFORMATION_MESSAGE);
                    session = request.get(SESSION_NAME).toString();
                    receptors = (ArrayList<Seeker>) request.get(SEEKERS_RECEPTORS);
                    comments = request.get(COMMENT).toString();
                    QuerySource data4 = (QuerySource) request.get(QUERY_SOURCE);
                    this.servant.recommendResults(session, seeker, receptors, comments, data4);

                    break;

                case RECOMMEND_GROUP_SELECTION_RESULTS:
                    OutputMonitor.printLine("Operation:[RECOMMEND_GROUP_SELECTION_RESULTS] requesting.. ", OutputMonitor.INFORMATION_MESSAGE);
                    session = request.get(SESSION_NAME).toString();
                    receptors = (ArrayList<Seeker>) request.get(SEEKERS_RECEPTORS);
                    Documents docs2 = (Documents) request.get(DOCUMENTS);
                    comments = request.get(COMMENT).toString();
                    QuerySource data5 = (QuerySource) request.get(QUERY_SOURCE);
                    this.servant.recommendResults(session, seeker, receptors, docs2, comments, data5);

                    break;

                case RECOMMEND_ANOTHER_SESSION_SINGLE_RESULTS:
                    OutputMonitor.printLine("Operation:[RECOMMEND_ANOTHER_SESSION_SINGLE_RESULTS] requesting.. ", OutputMonitor.INFORMATION_MESSAGE);
                    session = request.get(SESSION_NAME).toString();
                    otherSessionName = request.get(OTHER_SESSION_NAME).toString();
                    receptor = (Seeker) request.get(MEMBER_RECEPTOR);
                    comments = request.get(COMMENT).toString();
                    QuerySource data6 = (QuerySource) request.get(QUERY_SOURCE);
                    this.servant.recommendResults(session, seeker, otherSessionName, receptor, comments, data6);

                    break;

                case RECOMMEND_ANOTHER_SESSION_SINGLE_SELECTION_RESULTS:
                    OutputMonitor.printLine("Operation:[RECOMMEND_ANOTHER_SESSION_SINGLE_SELECTION_RESULTS] requesting.. ", OutputMonitor.INFORMATION_MESSAGE);
                    session = request.get(SESSION_NAME).toString();
                    otherSessionName = request.get(OTHER_SESSION_NAME).toString();
                    receptor = (Seeker) request.get(MEMBER_RECEPTOR);
                    Documents docs3 = (Documents) request.get(DOCUMENTS);
                    comments = request.get(COMMENT).toString();
                    QuerySource data7 = (QuerySource) request.get(QUERY_SOURCE);
                    this.servant.recommendResults(session, seeker, otherSessionName, receptor, docs3, comments, data7);

                    break;

                case RECOMMEND_ANOTHER_SESSION_GROUP_RESULTS:
                    OutputMonitor.printLine("Operation:[RECOMMEND_ANOTHER_SESSION_GROUP_RESULTS] requesting.. ", OutputMonitor.INFORMATION_MESSAGE);
                    session = request.get(SESSION_NAME).toString();
                    otherSessionName = request.get(OTHER_SESSION_NAME).toString();
                    receptors = (ArrayList<Seeker>) request.get(SEEKERS_RECEPTORS);
                    comments = request.get(COMMENT).toString();
                    QuerySource data8 = (QuerySource) request.get(QUERY_SOURCE);
                    this.servant.recommendResults(session, seeker, otherSessionName, receptors, comments, data8);

                    break;

                case RECOMMEND_ANOTHER_SESSION_GROUP_SELECTION_RESULTS:
                    OutputMonitor.printLine("Operation:[RECOMMEND_ANOTHER_SESSION_GROUP_SELECTION_RESULTS] requesting.. ", OutputMonitor.INFORMATION_MESSAGE);
                    session = request.get(SESSION_NAME).toString();
                    otherSessionName = request.get(OTHER_SESSION_NAME).toString();
                    receptors = (ArrayList<Seeker>) request.get(SEEKERS_RECEPTORS);
                    Documents docs4 = (Documents) request.get(DOCUMENTS);
                    comments = request.get(COMMENT).toString();
                    QuerySource data9 = (QuerySource) request.get(QUERY_SOURCE);
                    this.servant.recommendResults(session, seeker, otherSessionName, receptors, docs4, comments, data9);

                    break;
                ////////////////////////////////////////////////////////////////////
                case NOTIFY_ADMISSION_COLLAB_SESSION:
                    OutputMonitor.printLine("Operation:[NOTIFY_ADMISSION_COLLAB_SESSION] requesting.. ", OutputMonitor.INFORMATION_MESSAGE);
                    session = request.get(SESSION_NAME).toString();
                    receptor = (Seeker) request.get(SEEKER_NOTIFY);
                    this.servant.notifyConfirmCollabSession(session, seeker, receptor);

                    break;

                case NOTIFY_NO_ADMISSION_COLLAB_SESSION:
                    OutputMonitor.printLine("Operation:[NOTIFY_NO_ADMISSION_COLLAB_SESSION] requesting.. ", OutputMonitor.INFORMATION_MESSAGE);
                    session = request.get(SESSION_NAME).toString();
                    receptor = (Seeker) request.get(SEEKER_NOTIFY);
                    this.servant.notifyDeclineCollabSession(session, seeker, receptor);

                    break;
                ////////////////////////////////////////////////////////////////////
                case NOTIFY_DOCUMENT_VIEWED:
                    OutputMonitor.printLine("Operation:[NOTIFY_DOCUMENT_VIEWED] requesting.. ", OutputMonitor.INFORMATION_MESSAGE);
                    session = request.get(SESSION_NAME).toString();
                    query = request.get(QUERY).toString();
                    searcher = (Integer) request.get(SEARCHER);
                    docIndex = (Integer) request.get(DOC_INDEX);
                    String uri = request.get(URI).toString();
                    this.servant.notifyDocumentViewed(session, seeker, query, searcher, docIndex, uri);

                    break;

                case NOTIFY_DOCUMENT_EVALUATED:
                    OutputMonitor.printLine("Operation:[NOTIFY_DOCUMENT_EVALUATED] requesting.. ", OutputMonitor.INFORMATION_MESSAGE);
                    session = request.get(SESSION_NAME).toString();
                    query = request.get(QUERY).toString();
                    searcher = (Integer) request.get(SEARCHER);
                    docIndex = (Integer) request.get(DOC_INDEX);
                    score = (Byte) request.get(RELEVANCE);
                    int source = (Integer) request.get(SEARCH_RESULT_SOURCE);
                    String uri1 = request.get(URI).toString();

                    this.servant.notifyDocumentEvaluated(session, seeker, query, searcher, docIndex, uri1, score, source);

                    break;

                case NOTIFY_DOCUMENT_COMMENTED: //evaluación
                    OutputMonitor.printLine("Operation:[NOTIFY_DOCUMENT_COMMENTED] requesting.. ", OutputMonitor.INFORMATION_MESSAGE);
                    session = request.get(SESSION_NAME).toString();
                    query = request.get(QUERY).toString();
                    docIndex = (Integer) request.get(DOC_INDEX);
                    searcher = (Integer) request.get(SEARCHER);
                    comments = request.get(COMMENT).toString();
                    int source1 = (Integer) request.get(SEARCH_RESULT_SOURCE);
                    String uri2 = request.get(URI).toString();

                    this.servant.notifyDocumentCommented(session, seeker, query, searcher, docIndex, uri2, comments, source1);

                    break;

                case FINALIZE_COLLABORATIVE_SESSION:
                    OutputMonitor.printLine("Operation:[FINALIZE_COLLABORATIVE_SESSION] requesting.. ", OutputMonitor.INFORMATION_MESSAGE);
                    session = request.get(SESSION_NAME).toString();
                    this.servant.finalizeCollabSession(session, seeker);
                    break;

                case LOGOUT_COLLAB_SESSION:
                    OutputMonitor.printLine("Operation:[LOGOUT_COLLAB_SESSION] requesting.. ", OutputMonitor.INFORMATION_MESSAGE);
                    session = request.get(SESSION_NAME).toString();
                    this.servant.logoutCollabSession(session, seeker);
                    break;

                case GET_RECOMMENDATION_TRACK:
                    OutputMonitor.printLine("Operation:[GET_RECOMMENDATION_TRACK] requesting.. ", OutputMonitor.INFORMATION_MESSAGE);
                    this.mappingTrackRecommendation(request);
                    break;

                case GET_SEARCH_TRACK:
                    OutputMonitor.printLine("Operation:[GET_SEARCH_TRACK] requesting.. ", OutputMonitor.INFORMATION_MESSAGE);
                    this.mappingTrackSearch(request);
                    break;

                case GET_SESSION_TRACK:
                    OutputMonitor.printLine("Operation:[GET_SESSION_TRACK] requesting.. ", OutputMonitor.INFORMATION_MESSAGE);
                    Date date = (Date) request.get(DATE);
                    session = request.get(SESSION_NAME).toString();
                    this.servant.trackSession(session, date, seeker, seekerPrx);
                    break;

                case PUTTING_QUERY_TERMS_TOGETHER:
                    OutputMonitor.printLine("Operation:[PUTTING_QUERY_TERMS_TOGETHER] requesting.. ", OutputMonitor.INFORMATION_MESSAGE);
                    session = request.get(SESSION_NAME).toString();
                    int event1 = (Integer) request.get(DISTRIBUTED_EVENT);

                    this.servant.sendPuttingQueryTermsTogetherAction(session, event1, seeker);

                    break;

                case SEND_ACTION_QUERY_CHANGE:
                    OutputMonitor.printLine("Operation:[SEND_ACTION_QUERY_CHANGE] requesting.. ", OutputMonitor.INFORMATION_MESSAGE);
                    session = request.get(SESSION_NAME).toString();
                    query = request.get(QUERY).toString();
                    Map<String, ScorePQT> statistics = (Map<String, ScorePQT>) request.get(SCORE_PQT);

                    this.servant.sendQueryChangeAction(session, query, statistics, seeker);

                    break;

                case SEND_ACTION_QUERY_TYPED:
                    OutputMonitor.printLine("Operation:[SEND_ACTION_QUERY_TYPED] requesting.. ", OutputMonitor.INFORMATION_MESSAGE);
                    session = request.get(SESSION_NAME).toString();
                    boolean flag = (Boolean) request.get(QUERY_TYPED);

                    this.servant.sendQueryTypedAction(session, flag, seeker);

                    break;

                case SEND_ACTION_TERM_ACCEPTANCE:
                    OutputMonitor.printLine("Operation:[SEND_ACTION_TERM_ACCEPTANCE] requesting.. ", OutputMonitor.INFORMATION_MESSAGE);
                    session = request.get(SESSION_NAME).toString();
                    String term = request.get(QUERY_TERM).toString();
                    String user = request.get(SEEKER_NICKNAME).toString();
                    int event = (Integer) request.get(DISTRIBUTED_EVENT);

                    this.servant.sendTermAcceptanceAction(session, term, event, user, seeker);

                    break;

                case COLLABORATIVE_TERMS_SUGGEST:
                    OutputMonitor.printLine("Operation:[COLLABORATIVE_TERMS_SUGGEST] requesting.. ", OutputMonitor.INFORMATION_MESSAGE);
                    session = request.get(SESSION_NAME).toString();
                    int event2 = (Integer) request.get(DISTRIBUTED_EVENT);

                    this.servant.sendCollabTermsSuggestAction(session, event2, seeker, seekerPrx);

                    break;

                default:
                    throw new OperationNotExistException("The operation is not supported");
            }

        } catch (SeekerException | SessionAlreadyRegisterException | SessionPropertyException | UserStatusNotSupportedException | RecommendationException | NotificationException | SessionException | ScoreOutOfBoundsException | OperationNotExistException | IOException | ChairmanNotExistException | TrackException | AwarenessException ex) {
            OutputMonitor.printStream("Ivocation", ex);
            notifyCommitTransaction(ERROR_MESSAGE, ex.getMessage());
        } catch (SearchableException ex) {
            notifyCommitTransaction(ERROR_MESSAGE, ex.getMessage());
            OutputMonitor.printStream("Ivocation", ex);
        } catch (SearchException ex) {
            notifyCommitTransaction(ERROR_MESSAGE, ex.getMessage());
            OutputMonitor.printStream("Ivocation", ex);
        }




    }

    /**
     *
     * @param current
     */
    public void disconnect(Current current) {

        try {
            servant.logoutSeeker(sessionName, seeker);

            seekerPrx = null;

        } catch (SessionException | SeekerException | IOException ex) {
            notifyCommitTransaction(ERROR_MESSAGE, ex.getMessage());
            OutputMonitor.printStream("Ivocation", ex);
        }

    }

    /**
     * Este método notifica al miembro que invoca una operación el resultado final
     * de la misma.
     *
     * @param messageType   Posibles tipos de mensaje INFORMATION_MESSAGE, ERROR_MESSAGE,
     *                      ambos valores definidos en la interfaz Assignable.
     * @param message       descripción de la operación realizada ó causa del error.
     */
    public synchronized void notifyCommitTransaction(int messageType, String message) {

        Response rsp = ResponseUtilFactory.getResponse(messageType, message);
        try {
            seekerPrx.notify_async(new NotifyAMICallback(seeker, "notifyCommitTransaction"), rsp.toArray());
        } catch (IOException ex) {
            OutputMonitor.printStream("Error al serializar el objeto response.", ex);
        }
    }

    private void mappingTrackRecommendation(Request rqs) throws TrackException {
        int filter = (Integer) rqs.get(TRACK_FILTER);
        String sName = rqs.get(SESSION_NAME).toString();

        switch (filter) {
            case SESSION_TRACK:
                OutputMonitor.printLine("Operation:[GET_RECOMMENDATION_TRACK--SESSION_TRACK] requesting.. ", OutputMonitor.INFORMATION_MESSAGE);
                this.servant.trackRecommendation(sName, seeker, seekerPrx);
                break;

            case SEEKER_TRACK:
                OutputMonitor.printLine("Operation:[GET_RECOMMENDATION_TRACK--SEEKER_TRACK] requesting.. ", OutputMonitor.INFORMATION_MESSAGE);
                Seeker seek1 = (Seeker) rqs.get(SEEKER);
                this.servant.trackRecommendation(sName, seek1, seeker, seekerPrx);
                break;
            case DATE_TRACK:
                OutputMonitor.printLine("Operation:[GET_RECOMMENDATION_TRACK--DATE_TRACK] requesting.. ", OutputMonitor.INFORMATION_MESSAGE);
                Date date = (Date) rqs.get(DATE);
                this.servant.trackRecommendation(sName, date, seeker, seekerPrx);
                break;
            case QUERY_TRACK:
                OutputMonitor.printLine("Operation:[GET_RECOMMENDATION_TRACK--QUERY_TRACK] requesting.. ", OutputMonitor.INFORMATION_MESSAGE);
                String query1 = rqs.get(QUERY).toString();
                this.servant.trackRecommendation(sName, query1, seeker, seekerPrx);
                break;
            case QUERY_SEEKER_TRACK:
                OutputMonitor.printLine("Operation:[GET_RECOMMENDATION_TRACK--QUERY_SEEKER_TRACK] requesting.. ", OutputMonitor.INFORMATION_MESSAGE);
                Seeker seek2 = (Seeker) rqs.get(SEEKER);
                String query2 = rqs.get(QUERY).toString();
                this.servant.trackRecommendation(sName, seek2, query2, seeker, seekerPrx);

                break;
            case QUERY_DATE_TRACK:
                OutputMonitor.printLine("Operation:[GET_RECOMMENDATION_TRACK--QUERY_DATE_TRACK] requesting.. ", OutputMonitor.INFORMATION_MESSAGE);
                String query3 = rqs.get(QUERY).toString();
                Date date3 = (Date) rqs.get(DATE);
                this.servant.trackRecommendation(sName, query3, date3, seeker, seekerPrx);

                break;
            case SEEKER_DATE_TRACK:
                OutputMonitor.printLine("Operation:[GET_RECOMMENDATION_TRACK--SEEKER_DATE_TRACK] requesting.. ", OutputMonitor.INFORMATION_MESSAGE);
                Seeker seek4 = (Seeker) rqs.get(SEEKER);
                Date date4 = (Date) rqs.get(DATE);

                this.servant.trackRecommendation(sName, seek4, date4, seeker, seekerPrx);
                break;
            case QUERY_SEEKER_DATE_TRACK:
                OutputMonitor.printLine("Operation:[GET_RECOMMENDATION_TRACK--QUERY_SEEKER_DATE_TRACK] requesting.. ", OutputMonitor.INFORMATION_MESSAGE);
                Seeker seek5 = (Seeker) rqs.get(SEEKER);
                Date date5 = (Date) rqs.get(DATE);
                String query5 = rqs.get(QUERY).toString();
                this.servant.trackRecommendation(sName, seek5, date5, query5, seeker, seekerPrx);

                break;

            default:

        }

    }

    private void mappingTrackSearch(Request rqs) throws TrackException {
        int filter = (Integer) rqs.get(TRACK_FILTER);
        String sName = rqs.get(SESSION_NAME).toString();
        int docGroup = (Integer) rqs.get(DOCUMENT_GROUP);
        switch (filter) {
            case SESSION_TRACK:
                OutputMonitor.printLine("Operation:[GET_SEARCH_TRACK--SESSION_TRACK] requesting.. ", OutputMonitor.INFORMATION_MESSAGE);
                this.servant.trackSearch(sName, docGroup, seeker, seekerPrx);
                break;

            case SEEKER_TRACK:
                OutputMonitor.printLine("Operation:[GET_SEARCH_TRACK--SEEKER_TRACK] requesting.. ", OutputMonitor.INFORMATION_MESSAGE);
                Seeker seek1 = (Seeker) rqs.get(SEEKER);
                this.servant.trackSearch(sName, seek1, docGroup, seeker, seekerPrx);
                break;
            case DATE_TRACK:
                OutputMonitor.printLine("Operation:[GET_SEARCH_TRACK--DATE_TRACK] requesting.. ", OutputMonitor.INFORMATION_MESSAGE);
                Date date = (Date) rqs.get(DATE);
                this.servant.trackSearch(sName, date, docGroup, seeker, seekerPrx);
                break;
            case QUERY_TRACK:
                OutputMonitor.printLine("Operation:[GET_SEARCH_TRACK--QUERY_TRACK] requesting.. ", OutputMonitor.INFORMATION_MESSAGE);
                String query1 = rqs.get(QUERY).toString();
                this.servant.trackSearch(sName, query1, docGroup, seeker, seekerPrx);
                break;
            case QUERY_SEEKER_TRACK:
                OutputMonitor.printLine("Operation:[GET_SEARCH_TRACK--QUERY_SEEKER_TRACK] requesting.. ", OutputMonitor.INFORMATION_MESSAGE);
                Seeker seek2 = (Seeker) rqs.get(SEEKER);
                String query2 = rqs.get(QUERY).toString();
                this.servant.trackSearch(sName, seek2, query2, docGroup, seeker, seekerPrx);

                break;
            case QUERY_DATE_TRACK:
                OutputMonitor.printLine("Operation:[GET_SEARCH_TRACK--QUERY_DATE_TRACK] requesting.. ", OutputMonitor.INFORMATION_MESSAGE);
                String query3 = rqs.get(QUERY).toString();
                Date date3 = (Date) rqs.get(DATE);
                this.servant.trackSearch(sName, query3, date3, docGroup, seeker, seekerPrx);

                break;
            case SEEKER_DATE_TRACK:
                OutputMonitor.printLine("Operation:[GET_SEARCH_TRACK--SEEKER_DATE_TRACK] requesting.. ", OutputMonitor.INFORMATION_MESSAGE);
                Seeker seek4 = (Seeker) rqs.get(SEEKER);
                Date date4 = (Date) rqs.get(DATE);

                this.servant.trackSearch(sName, seek4, date4, docGroup, seeker, seekerPrx);
                break;
            case QUERY_SEEKER_DATE_TRACK:
                OutputMonitor.printLine("Operation:[GET_SEARCH_TRACK--QUERY_SEEKER_DATE_TRACK] requesting.. ", OutputMonitor.INFORMATION_MESSAGE);
                Seeker seek5 = (Seeker) rqs.get(SEEKER);
                Date date5 = (Date) rqs.get(DATE);
                String query5 = rqs.get(QUERY).toString();
                this.servant.trackSearch(sName, seek5, date5, query5, docGroup, seeker, seekerPrx);

                break;

            default:

        }
    }
}
