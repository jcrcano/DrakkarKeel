/*
 * DrakkarKeel - An Enterprise Collaborative Search Platform
 *
 * The contents of this file are subject under the terms described in the
 * DRAKKARKEEL_LICENSE file included in this distribution; you may not use this
 * file except in compliance with the License. 
 *
 * 2013-2014 DrakkarKeel Platform.
 */
package drakkar.stern.servant.service;

import drakkar.oar.Response;
import drakkar.oar.Seeker;
import static drakkar.oar.Seeker.*;
import drakkar.oar.Session;
import drakkar.oar.SessionProperty;
import drakkar.oar.exception.ChairmanNotExistException;
import drakkar.oar.exception.SeekerException;
import drakkar.oar.exception.SessionAlreadyRegisterException;
import drakkar.oar.exception.SessionException;
import drakkar.oar.exception.SessionPropertyException;
import drakkar.oar.exception.UserAlreadyRegisterException;
import drakkar.oar.exception.UserStatusNotSupportedException;
import drakkar.oar.facade.event.FacadeDesktopEvent;
import drakkar.oar.security.DrakkarSecurity;
import drakkar.oar.slice.client.ClientSidePrx;
import drakkar.oar.slice.error.RequestException;
import static drakkar.oar.util.KeyMessage.*;
import static drakkar.oar.util.KeySession.*;
import static drakkar.oar.util.KeyTransaction.*;
import static drakkar.oar.util.NotifyAction.*;
import drakkar.oar.util.OutputMonitor;
import static drakkar.oar.util.SeekerAction.*;
import drakkar.oar.util.XMLParser;
import drakkar.mast.RetrievalManager;
import drakkar.stern.ResponseSessionFactory;
import drakkar.stern.ResponseUtilFactory;
import drakkar.stern.SternAppSetting;
import drakkar.stern.callback.DisconnectAMICallback;
import drakkar.stern.callback.NotifyAMICallback;
import drakkar.stern.controller.DataBaseController;
import drakkar.stern.facade.event.FacadeListener;
import drakkar.stern.tracker.cache.Chairman;
import drakkar.stern.tracker.cache.SeekerInfo;
import drakkar.stern.tracker.cache.SessionProfile;
import drakkar.stern.tracker.persistent.objects.SeekerData;
import drakkar.stern.tracker.persistent.objects.SessionData;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The <code>SessionManager</code> class is....
 * 
 * Esta clase implementa todas las operaciones soportadas por el framework DrakkarKeel
 * para el trabajo con sesiones
 *
 *
 */
public class SessionManager implements Sessionable {

    /**
     * sesiones colaborativas persistentes
     */
    protected Map<String, SessionProfile> collaborativeSessions;
    /**
     *
     */
    protected FacadeListener listener;
    /**
     * 
     */
    protected SessionProfile defaultSessionProfile;
    /**
     *
     */
    protected RetrievalManager retrievalManager;
    /**
     *
     */
    protected DataBaseController dbController;
    /**
     * Esta variable representa el valor máximo de la puntuación que puede recibir
     * documento por el miembro que lo evalúa.
     */
    private byte maxScore = 10;
    /**
     * Esta variable representa el valor de la puntuación, que permite determinar
     * si un documento es relevante o no.
     */
    private byte relevScore = 6;
    /**
     * Esta tabla hash almacena temporalmente hasta que se establezca la persistencia
     * la información relacionada con cada sesión finalizada
     */
    protected Map<String, SessionProfile> htTempSessions;
    /**
     *
     */
    protected SternAppSetting setting;

    /**
     * Constructor de la clase
     *
     * @param retrievalManager
     */
    public SessionManager(RetrievalManager retrievalManager) {
        this.collaborativeSessions = new HashMap<>();
        this.defaultSessionProfile = new SessionProfile();
        this.defaultSessionProfile.getProperties().setSessionName("DefaultSession");
        this.retrievalManager = retrievalManager;
        this.htTempSessions = new HashMap<>();
    }

    /**
     * Constructor de la clase
     *
     * @param retrievalManager
     * @param setting
     */
    public SessionManager(RetrievalManager retrievalManager, SternAppSetting setting) {
        this.setting = setting;
        this.collaborativeSessions = new HashMap<>();
        this.defaultSessionProfile = new SessionProfile(this.setting.getSessionName(), this.setting.getSessionDescription());
        this.retrievalManager = retrievalManager;
        this.htTempSessions = new HashMap<>();
    }

    /**
     *
     * @param retrievalManager
     * @param dbController
     */
    public SessionManager(RetrievalManager retrievalManager, DataBaseController dbController) {
        this.collaborativeSessions = new HashMap<>();
        this.retrievalManager = retrievalManager;
        this.dbController = dbController;
        this.htTempSessions = new HashMap<>();
        this.defaultSessionProfile = new SessionProfile();
        this.defaultSessionProfile.getProperties().setSessionName("DefaultSession");

    }

    /**
     *
     * @param retrievalManager
     * @param listener
     * @param dbController
     */
    public SessionManager(RetrievalManager retrievalManager, FacadeListener listener, DataBaseController dbController) {
        this.collaborativeSessions = new HashMap<>();
        this.listener = listener;
        this.retrievalManager = retrievalManager;
        this.dbController = dbController;
        this.htTempSessions = new HashMap<>();
        this.defaultSessionProfile = new SessionProfile();
        this.defaultSessionProfile.getProperties().setSessionName("DefaultSession");
    }

    /**
     * @param retrievalManager
     * @param listener
     * @param dbController
     * @param setting
     */
    public SessionManager(RetrievalManager retrievalManager, FacadeListener listener, DataBaseController dbController, SternAppSetting setting) {
        this.collaborativeSessions = new HashMap<>();
        this.listener = listener;
        this.retrievalManager = retrievalManager;
        this.dbController = dbController;
        this.htTempSessions = new HashMap<>();
        this.setting = setting;
        this.defaultSessionProfile = new SessionProfile(this.setting.getSessionName(), this.setting.getSessionDescription());

    }

    /**
     * {@inheritDoc}
     *
     */
    public synchronized boolean createCollabSession(final String newSessionName, final Seeker seeker, final int membersMinNumber, final int membersMaxNumber, final int integrityCriteria, final int membershipPolicy, final List<Seeker> seekerList, final String description) throws SeekerException, SessionAlreadyRegisterException, SessionPropertyException, ChairmanNotExistException {

        boolean flag = this.existCollabSession(newSessionName);
        boolean flag2 = false;
        if (dbController != null && dbController.isOpen()) {
            try {
                flag2 = this.dbController.existSession(newSessionName);
            } catch (SQLException ex) {
                OutputMonitor.printStream("",ex);
            }
        }

        if (flag || flag2) {
            throw new SessionAlreadyRegisterException("The session '" + newSessionName + "' already exists");
        } else {
            SeekerInfo seekersRecord = defaultSessionProfile.getSeekerInfo();
            flag = existsSeeker(seeker, seekersRecord);
            if (flag) {
                if (integrityCriteria == SESSION_SOFT || integrityCriteria == SESSION_HARD) {
                    if (membershipPolicy == SESSION_STATIC || membershipPolicy == SESSION_DYNAMIC_AND_CLOSE || membershipPolicy == SESSION_DYNAMIC_AND_OPEN) {
//                        if (members != null && !members.isEmpty()) {
                        SeekerInfo membs = new SeekerInfo();
                        Chairman ch = null;
                        ClientSidePrx itemPrx;
                        ClientSidePrx chairmanPrx = null;
                        int size = seekerList.size();
                        boolean isChairman = true;
                        for (Seeker item : seekerList) {
                            flag = existsSeeker(item, seekersRecord);
                            if (flag) {
                                if (isChairman) {
                                    if (item.getRole() == Seeker.ROLE_CHAIRMAN) {
                                        chairmanPrx = seekersRecord.record.get(item);
                                        ch = new Chairman(item, chairmanPrx);
                                        isChairman = false;
                                    }
                                }
                                itemPrx = seekersRecord.record.get(item);
                                membs.record.put(item, itemPrx);
                            }
                        }

                        itemPrx = seekersRecord.record.get(seeker);

                        if (isChairman) {
                            ch = new Chairman(seeker, itemPrx);
                            seeker.setRole(Seeker.ROLE_CHAIRMAN);
                        }

                        membs.record.put(seeker, itemPrx);
                        SessionProperty property = new SessionProperty(newSessionName, ch.getName(), membersMinNumber, membersMaxNumber, size, integrityCriteria, membershipPolicy, description);
                        SessionProfile newRecordSession = new SessionProfile(ch, membs, property);
                        this.collaborativeSessions.put(newSessionName, newRecordSession);
                        this.updateServer(UPDATE_SESSIONS, INCREMENT, newSessionName);
                        List<Session> availableSession = new ArrayList<>();
                        seekerList.add(seeker);
                        isChairman = true;

                        ///Save DB

                        try {
                            if (dbController != null && dbController.isOpen()) {
                                dbController.saveSearchSession(newSessionName, description, seeker.getUser(), integrityCriteria, membersMaxNumber, membersMinNumber, seekerList, membershipPolicy);
                                availableSession = this.dbController.getAllAvailableSessions(seeker.getUser());
                            }
                        } catch (SQLException ex) {
                            OutputMonitor.printStream("",ex);
                        }

                        for (Seeker item : seekerList) {

                            if (isChairman) {
                                if (item.getRole() == Seeker.ROLE_CHAIRMAN) {
                                    List<String> principles = retrievalManager.getSearchPrinciples(RetrievalManager.COLLABORATIVE_SEARCH);
                                    this.notifyCreatedCollabSession(newSessionName, item, ch.getClientSidePrx(), seekerList, principles, availableSession);
                                    isChairman = false;

                                } else {
                                    itemPrx = membs.record.get(item);
                                    this.notifyConfirmCollabSession(newSessionName, item, itemPrx, seekerList);
                                }
                            } else {
                                itemPrx = membs.record.get(item);
                                this.notifyConfirmCollabSession(newSessionName, item, itemPrx, seekerList);
                            }
                        }
                        final String chairmanName = ch.getName();
                        updateAvailableSession(newSessionName, description, chairmanName, BEGIN_COLLAB_SESSION, seekerList);

                        return true;
//                        } else {
//                            throw new SessionPropertyException("The session requires at least two members to be created");
//                        }
                    } else {
                        throw new SessionPropertyException("The Membership Policy of the session is not valid");
                    }
                } else {
                    throw new SessionPropertyException("The Integrity Criteria of the session is not valid.");
                }
            } else {
                throw new SeekerException("The seeker '" + seeker.getUser() + "' is not registered in the communication session '" + getCommunicationSessionName() + "'.");
            }
        }

    }

    /**
     *
     */
    private void notifyConfirmCollabSession(final String session, final Seeker seeker, final ClientSidePrx seekerPrx, final List<Seeker> seekersList) {

        Thread t = new Thread(new Runnable() {

            public void run() {
                @SuppressWarnings("unchecked")
                List<Seeker> seekers = (ArrayList<Seeker>) ((ArrayList) seekersList).clone();
                seekers.remove(seeker);
                List<Session> availableSession = new ArrayList<>();
                try {
                    if (dbController != null && dbController.isOpen()) {
                        availableSession = dbController.getAllAvailableSessions(seeker.getUser());
                    }
                } catch (SQLException ex) {
                    availableSession = new ArrayList<>();
                    OutputMonitor.printStream("",ex);
                }

                Response response = ResponseSessionFactory.getResponse(session, seekers, availableSession);

                try {
                    byte[] array = response.toArray();
                    seekerPrx.notify_async(new NotifyAMICallback(seeker, "notifyConfirmCollabSession"), array);
                } catch (IOException ex) {
                    OutputMonitor.printStream("",ex);
                }
            }
        });
        t.start();
    }

    /**
     *
     */
    private void notifyCreatedCollabSession(final String session, final Seeker seeker, final ClientSidePrx seekerPrx, final List<Seeker> seekersList, final List<String> principles, final List<Session> availableSession) {

        Thread t = new Thread(new Runnable() {

            public void run() {
                @SuppressWarnings("unchecked")
                List<Seeker> seekers = (ArrayList<Seeker>) ((ArrayList) seekersList).clone();
                seekers.remove(seeker);
                Response response = ResponseSessionFactory.getResponse(session, seekers, principles, availableSession);
                try {
                    byte[] array = response.toArray();
                    seekerPrx.notify_async(new NotifyAMICallback(seeker, "notifyCreatedCollabSession"), array);

                } catch (IOException ex) {
                    OutputMonitor.printStream("",ex);
                }
            }
        });
        t.start();

    }

    /**
     * {@inheritDoc}
     */
    public synchronized void joinCollabSession(final String sessionName, final Seeker seeker, final ClientSidePrx seekerPrx) throws SessionException, IOException {
        boolean flag = this.existCollabSession(sessionName);
        if (flag && !sessionName.equals(getCommunicationSessionName())) {
            // se obtiene apartir del nombre de sesión, la relación de miembros
            // con sus correspondientes objetos proxy
            SessionProfile recordSession = this.collaborativeSessions.get(sessionName);
            SeekerInfo seekersRecord = recordSession.getSeekerInfo();

            if (existsSeeker(seeker, seekersRecord)) {
                this.notifyCommitTransaction(ERROR_MESSAGE, "The seeker '" + seeker.getUser() + "' is already registered in session " + sessionName, seeker, seekerPrx);
                return;

            } else {
                SessionProperty properties = recordSession.getProperties();
                int membersMaxNumber = properties.getMembersMaxNumber();
                int membersCurrentNumber = properties.getMembersCurrentNumber();
                int membershipPolicy = properties.getMembershipPolicy();
                Response response;

                // se determina la política de membrasía de la sesión
                if (membershipPolicy == SESSION_STATIC) {
                    this.notifyCommitTransaction(ERROR_MESSAGE, "The policy of membership of the session doesn't permit your admission.", seeker, seekerPrx);

                    return;

                } else if (membershipPolicy == SESSION_DYNAMIC_AND_CLOSE) {
                    if (membersCurrentNumber == membersMaxNumber) {
                        this.notifyCommitTransaction(ERROR_MESSAGE, "The session right now has the maximum numbers.", seeker, seekerPrx);

                        return;
                    } else {

                        requestAdmissionSession(sessionName, seeker, seekerPrx);
                        return;
                    }
                } else if (membershipPolicy == SESSION_DYNAMIC_AND_OPEN) {
                    if (membersCurrentNumber == membersMaxNumber) {
                        this.notifyCommitTransaction(ERROR_MESSAGE, "The session right now has the maximum numbers.", seeker, seekerPrx);
                        return;
                    } else {
                        List<ClientSidePrx> seekersOnlinePrx = getClientSidePrxList(seekersRecord);
                        List<Seeker> seekersOnline = getSeekersList(seekersRecord);

                        seeker.setRole(Seeker.ROLE_MEMBER);
                        seekersRecord.record.put(seeker, seekerPrx);
                        List<Session> availableSession = new ArrayList<>();

                        if (dbController != null && dbController.isOpen()) {
                            List<Seeker> list;
                            try {
                                list = this.dbController.getAllSeekers(sessionName);
                            } catch (SQLException ex) {
                                list = seekersOnline;
                                OutputMonitor.printStream("",ex);
                            }

                            try {
                                this.joinSeekerDB(sessionName, seeker);
                                availableSession = this.dbController.getAllAvailableSessions(seeker.getUser());
                            } catch (SQLException ex) {
                                availableSession = new ArrayList<>();
                                OutputMonitor.printStream("",ex);
                            }
                            response = ResponseSessionFactory.getResponse(sessionName, list, availableSession);
                            seekerPrx.notify_async(new NotifyAMICallback(seeker, "joinCollabSession"), response.toArray());

                        } else {
                            response = ResponseSessionFactory.getResponse(sessionName, seekersOnline, availableSession);
                            seekerPrx.notify_async(new NotifyAMICallback(seeker, "joinCollabSession"), response.toArray());
                        }
                        // se informa al resto de los miembros
                        this.notifyUpdateSession(sessionName, seekersOnline, seekersOnlinePrx, seeker, SEEKER_LOGIN_COLLAB_SESSION);
                        membersCurrentNumber++; // se aumenta el # de miembros


                        return;
                    }
                }
            }
        } else {
            throw new SessionException("The session '" + sessionName + "' doesn't exist.");
        }
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("element-type-mismatch")
    public synchronized void enterCollabSession(final String sessionName, final Seeker seeker, final ClientSidePrx seekerPrx) throws SessionException, IOException {
        try {
            if (dbController != null && dbController.isOpen()) {
                boolean flag = this.existCollabSession(sessionName);

                if (flag) {
                    // se obtiene a partir del nombre de sesión, la relación de miembros
                    // con sus correspondientes objetos proxy
                    SessionProfile recordSession = this.collaborativeSessions.get(sessionName);
                    SeekerInfo seekersRecord = recordSession.getSeekerInfo();
                    if (existsSeeker(seeker, seekersRecord)) {
                        this.notifyCommitTransaction(ERROR_MESSAGE, "The seeker '" + seeker.getUser() + "' is already registered", seeker, seekerPrx);
                        return;
                    } else {
                        if (this.dbController.verifySeeker(sessionName, seeker.getUser())) {
                            List<ClientSidePrx> seekersOnlinePrx = getClientSidePrxList(seekersRecord);
                            List<Seeker> seekersOnline = getSeekersList(seekersRecord);


                            if (seeker.equals(recordSession.getChairman().getSeeker())) {
                                seeker.setRole(ROLE_CHAIRMAN);
                                Seeker tempChairman = recordSession.getTempChairman();

                                if (tempChairman != null) {
                                    ClientSidePrx removed = recordSession.getSeekerInfo().record.remove(tempChairman);
                                    tempChairman.setRole(ROLE_MEMBER);
                                    recordSession.getSeekerInfo().record.put(tempChairman, removed);
                                    notifyNewChairman(sessionName, tempChairman, removed, false, seeker.getUser());

                                }

                                recordSession.setChairman(new Chairman(seeker, seekerPrx));
                                recordSession.setTempChairman(null);
                                notifyNewChairman(sessionName, seeker, seekerPrx, true, seeker.getUser());
                            }
                            if (seeker.getRole() == ROLE_POTENTIAL_MEMBER) {
                                seeker.setRole(ROLE_MEMBER);
                            }
                            seekersRecord.record.put(seeker, seekerPrx);
                            notifyEnterCollabSession(sessionName, seekersOnline, seeker, seekerPrx);

                            // se informa al resto de los miembros
                            this.notifyUpdateSession(sessionName, seekersOnline, seekersOnlinePrx, seeker, SEEKER_LOGIN_COLLAB_SESSION);

                            return;
                        }

                    }

                } else {
                    boolean exist = this.dbController.existSession(sessionName);

                    if (exist) {
                        List<Seeker> list = this.dbController.getAllSeekers(sessionName);
                        final SessionData data = this.dbController.getSessionData(sessionName);
                        SessionProperty property = new SessionProperty();
                        property.setChairman(data.getChairman());

                        int criteria = data.isCriteria() ? SESSION_SOFT : SESSION_HARD;
                        property.setIntegrityCriteria(criteria);
                        property.setMembersCurrentNumber(data.getCurrent());
                        property.setMembersMaxNumber(data.getMaxMember());
                        property.setMembersMinNumber(data.getMinMember());
                        property.setMembershipPolicy(data.getMembership());
                        property.setSessionName(data.getTopic());
                        property.setDescription(data.getDescrip());

                        Chairman chairman = null;
                        flag = false;
                        if (seeker.getUser().equals(data.getChairman())) {
                            chairman = new Chairman(seeker, seekerPrx);

                        } else {
                            String ch = data.getChairman();
                            for (Seeker item : list) {
                                if (item.getUser().equals(ch)) {
                                    chairman = new Chairman(item, null);
                                    break;
                                }
                            }


                            flag = true;
                        }
                        seeker.setRole(ROLE_CHAIRMAN);

                        SeekerInfo seekerProxy = new SeekerInfo();
                        seekerProxy.record.put(seeker, seekerPrx);
                        SessionProfile profile = new SessionProfile(chairman, seekerProxy, property);
                        if (flag) {
                            profile.setTempChairman(seeker);
                        }

                        if (htTempSessions.containsKey(sessionName)) {
                            SessionProfile profile1 = this.htTempSessions.get(sessionName);
                            profile.setMessagesCount(profile1.getMessagesCount());
                            profile.setRecommendationsCount(profile1.getRecommendationsCount());
                            profile.setSearchesCount(profile1.getSearchesCount());
                            profile.setQuerys(profile1.getQuerys());
                            profile.setSeekersMessages(profile1.getSeekersMessages());
                            profile.setRecordComments(profile1.getSeekerComments());
                            profile.setSeekerRecommends(profile1.getSeekerRecommends());
                            profile.setSeekerRecommendResults(profile1.getSeekerRecommendResults());
                            profile.setSeekerSearchResults(profile1.getSeekerSearchResults());
                            profile.setSelectedDocuments(profile1.getSelectedDocuments());
                            profile.setViewedDocuments(profile1.getViewedDocuments());
                        }

                        this.collaborativeSessions.put(sessionName, profile);
                        notifyEnterCollabSession(sessionName, list, seeker, seekerPrx);
                        //notifica al seeker que es el chairman de la session, y actualiza sus principios de busq.
                        notifyNewChairman(sessionName, seeker, seekerPrx, true, seeker.getUser());
                        updateAvailableSession(sessionName, data.getDescrip(), seeker.getUser(), BEGIN_COLLAB_SESSION, new ArrayList<Seeker>());
                    } else {
                        throw new SessionException("The session " + sessionName + " doesn´t exist.");
                    }
                }
            } else {
                throw new SQLException();
            }
        } catch (SQLException ex) {
            OutputMonitor.printStream("",ex);
            this.notifyCommitTransaction(ERROR_MESSAGE, "Problemas de conexión con la Base de Datos del servidor.", seeker, seekerPrx);
        }

    }

    /**
     * 
     * @param sessionName
     * @param seekersList
     * @param seeker
     * @param seekerPrx
     * @throws IOException
     */
    public void notifyEnterCollabSession(String sessionName, List<Seeker> seekersList, Seeker seeker, ClientSidePrx seekerPrx) throws IOException {
        seekersList.remove(seeker);
        List<String> principles;

        if (seeker.getRole() == Seeker.ROLE_CHAIRMAN) {
            principles = retrievalManager.getSearchPrinciples(RetrievalManager.COLLABORATIVE_SEARCH);
        } else {
            principles = retrievalManager.getSearchPrinciples(RetrievalManager.INDIVIDUAL_SEARCH);
        }

        Response response = ResponseSessionFactory.getResponse(seekersList, principles, sessionName);
        seekerPrx.notify_async(new NotifyAMICallback(seeker, "notifyLoginCollabSession"), response.toArray());
    }

    /**
     *
     * @param sessionName     
     * @param seeker
     * @param seekerPrx
     * @param isChairman
     * @param chairmanName 
     * @throws IOException
     */
    public void notifyNewChairman(String sessionName, Seeker seeker, ClientSidePrx seekerPrx, boolean isChairman, String chairmanName) throws IOException {
        List<String> principles = new ArrayList<>();

        if (isChairman) {
            principles = retrievalManager.getSearchPrinciples(RetrievalManager.COLLABORATIVE_SEARCH);
        } else {
            principles = retrievalManager.getSearchPrinciples(RetrievalManager.INDIVIDUAL_SEARCH);
        }

        Response response = ResponseSessionFactory.getResponse(sessionName, isChairman, chairmanName, principles);
        seekerPrx.notify_async(new NotifyAMICallback(seeker, "notifyNewChairman"), response.toArray());
    }

    /**
     *
     * @param sessionName
     * @param seeker
     * @param seekerPrx
     * @param isChairman
     * @throws IOException
     */
    public void notifyUpdateSearchPrinciples(String sessionName, Seeker seeker, ClientSidePrx seekerPrx, boolean isChairman) throws IOException {
        List<String> principles = new ArrayList<>();

        if (isChairman) {
            principles = retrievalManager.getSearchPrinciples(RetrievalManager.COLLABORATIVE_SEARCH);
        } else {
            principles = retrievalManager.getSearchPrinciples(RetrievalManager.INDIVIDUAL_SEARCH);
        }

        Response response = ResponseSessionFactory.getResponse(principles);
        seekerPrx.notify_async(new NotifyAMICallback(seeker, "notifyUpdateSearchPrinciples"), response.toArray());
    }

    /**
     * Este método para solicitar la admisión de una sesión.
     *
     * @param sessionName    nombre de la sesión.
     * @param seeker         miembro que solicita la admisión.
     * @param seekerPrx      objeto proxy del miembro.
     *
     * @throws SessionException   si la sesión no se encuentra registrada
     *                                    en el servidor.
     * @throws UserAlreadyRegisterException si se emite una nueva solicitud por
     *                                        el miembro y este todavía se encuentra
     *                                        en espera de una admisión anterior.
     * @throws IOException   si ocurre alguna excepción durante el proceso de
     *                       serialización del objeto Response
     */
    private synchronized void requestAdmissionSession(final String sessionName, final Seeker seeker, final ClientSidePrx seekerPrx) {

        Thread t = new Thread(new Runnable() {

            public void run() {
                try {
                    if (!sessionName.equals(getCommunicationSessionName())) {
                        boolean flag = existCollabSession(sessionName);
                        if (flag) {
                            SessionProfile recordSession = collaborativeSessions.get(sessionName);
                            SeekerInfo temp = recordSession.getSeekerInfo();
                            flag = existsSeeker(seeker, temp);
                            if (flag) {
                                throw new UserAlreadyRegisterException("The seeker '" + seeker.getUser() + "'is alrealdy registered in the sessión.");
                            } else {
                                SeekerInfo recMembers = recordSession.getSeekerWaitAdmission();
                                flag = existsSeeker(seeker, recMembers);
                                if (flag) {
                                    throw new UserAlreadyRegisterException("The seeker '" + seeker.getUser() + "'right now finds himself in wait of admission");
                                } else {

                                    recMembers.record.put(seeker, seekerPrx);
                                    Chairman chairman = recordSession.getChairman();

                                    Response rsp = ResponseSessionFactory.getResponse(sessionName, seeker);
                                    ClientSidePrx chairmanPrx = chairman.getClientSidePrx();
                                    chairmanPrx.notify_async(new NotifyAMICallback(chairman.getSeeker(), "requestAdmissionSession"), rsp.toArray());
                                    notifyCommitTransaction(INFORMATION_MESSAGE, "His request of admission to the session was sent to the chairman of the same.", seeker, seekerPrx);
                                }
                            }
                        } else {
                            throw new SessionException("The session '" + sessionName + "' doesn't exist.");
                        }
                    }

                } catch (        UserAlreadyRegisterException | SessionException | IOException ex) {
                    OutputMonitor.printStream("",ex);
                    notifyCommitTransaction(ERROR_MESSAGE, ex.getMessage(), seeker, seekerPrx);

                    return;
                }
            }
        });
        t.start();
    }

    /**
     * {@inheritDoc}
     */
    public synchronized void notifyConfirmCollabSession(String sessionName, Seeker chairman, Seeker seeker) throws SessionException, SeekerException, IOException {
        if (!sessionName.equals(getCommunicationSessionName())) {
            if (this.existCollabSession(sessionName)) {
                SessionProfile recordSession = this.collaborativeSessions.get(sessionName);
                Chairman sessionChairman = recordSession.getChairman();
                Seeker item = sessionChairman.getSeeker();
                if (item.equals(chairman)) {

                    SeekerInfo temp = recordSession.getSeekerWaitAdmission();
                    if (!temp.record.isEmpty()) {

                        if (existsSeeker(seeker, temp)) {
                            SeekerInfo seekersRecord = recordSession.getSeekerInfo();
                            List<Seeker> seekersOnline = this.getSeekersList(seekersRecord);
                            List<ClientSidePrx> seekersOnlinePrx = this.getClientSidePrxList(seekersRecord);
                            Response response;

                            List<Session> availableSession = new ArrayList<>();
                            if (dbController != null && dbController.isOpen()) {
                                List<Seeker> list = null;
                                try {
                                    list = this.dbController.getAllSeekers(sessionName);
                                } catch (SQLException ex) {
                                    list = seekersOnline;
                                    OutputMonitor.printStream("",ex);
                                }
                                this.joinSeekerDB(sessionName, seeker);

                                try {
                                    availableSession = this.dbController.getAllAvailableSessions(seeker.getUser());
                                } catch (SQLException ex) {
                                    availableSession = new ArrayList<>();
                                    OutputMonitor.printStream("",ex);
                                }
                                response = ResponseSessionFactory.getResponse(sessionName, list, availableSession);

                            } else {
                                response = ResponseSessionFactory.getResponse(sessionName, seekersOnline, availableSession);
                            }

                            ClientSidePrx itemPrx = temp.record.get(seeker);
                            seeker.setRole(Seeker.ROLE_MEMBER);
                            seekersRecord.record.put(seeker, itemPrx);
//                            this.loginSeeker(sessionName, seeker, itemPrx);// se adiciona a la sesión y se elimina de la lista de espera
                            itemPrx.notify_async(new NotifyAMICallback(seeker, "notifyConfirmCollabSession"), response.toArray());
                            temp.record.remove(seeker);

//                             // se informa al resto de los miembros
                            this.notifyUpdateSession(sessionName, seekersOnline, seekersOnlinePrx, seeker, SEEKER_LOGIN_COLLAB_SESSION);

                        } else {
                            throw new SeekerException("The seeker '" + ((seeker != null) ? seeker.getUser() : "<null>") + "' is not registered in the session '" + sessionName + "'.");
                        }
                    }
                } else {
                    throw new SeekerException("The seeker '" + chairman.getUser() + "' is not the chairman of the session '" + sessionName + "'");
                }
            } else {
                throw new SessionException("The session '" + sessionName + "' doesn't exist.");
            }
        } else {
            throw new SessionException("The session '" + sessionName + "' not supported this operation.");
        }
    }

    /**
     * {@inheritDoc}
     */
    public synchronized void notifyDeclineCollabSession(String sessionName, Seeker chairman, Seeker seeker) throws SessionException, SeekerException, IOException {

        if (!sessionName.equals(getCommunicationSessionName())) {
            boolean flag = this.existCollabSession(sessionName);
            if (flag) {
                SessionProfile recordSession = this.collaborativeSessions.get(sessionName);
                Chairman sessionChairman = recordSession.getChairman();  // se obtiene el objeto Chairman correspondiente a la sesión
                Seeker item = sessionChairman.getSeeker();               // se obtiene el miembro que representa al Chairman de la sesión,
                if (item.equals(chairman)) {
                    // se obtiene el registro que almacena la relación de miembros que
                    // almacena que se encuentran en espera de admisión
                    SeekerInfo temp = recordSession.getSeekerWaitAdmission();
                    if (!temp.record.isEmpty()) {
                        if (existsSeeker(seeker, temp)) {
                            Response response = ResponseSessionFactory.getResponse(DECLINE_COLLAB_SESSION, sessionName);
                            ClientSidePrx receptorPrx = temp.record.get(seeker);
                            receptorPrx.notify_async(new NotifyAMICallback(seeker, "notifyDeclineCollabSession"), response.toArray());
                            temp.record.remove(seeker);
                        } else {
                            throw new SeekerException("The seeker '" + ((seeker != null) ? seeker.getUser() : "null") + "' is not registered in the session '" + sessionName + "'.");
                        }
                    }
                } else {
                    throw new SeekerException("The seeker '" + chairman.getUser() + "' is not the chairman of the session '" + sessionName + "'");
                }
            } else {
                throw new SessionException("The session '" + sessionName + "' doesn't exist.");
            }
        } else {
            throw new SessionException("The session '" + sessionName + "' not supported this operation.");
        }
    }

    /**
     * {@inheritDoc}
     */
    public synchronized void finalizeCollabSession(final String sessionName, Seeker chairman) throws SessionException, IOException {

        boolean flag = this.existCollabSession(sessionName);
        if (flag) {
            SessionProfile recordSession = this.collaborativeSessions.get(sessionName);
            if (chairman.getRole() == Seeker.ROLE_CHAIRMAN) {
                Chairman temp = recordSession.getChairman();
                Seeker memb = temp.getSeeker();
                if (chairman.equals(memb)) {
                    SeekerInfo rec = recordSession.getSeekerInfo();

                    Response rsp = ResponseSessionFactory.getResponse(sessionName, END_SESSION);
                    byte[] array = rsp.toArray();
                    List<Seeker> seekers = getSeekersList(rec);
                    List<ClientSidePrx> seekersPrx = getClientSidePrxList(rec);



                    ClientSidePrx itemPrx;
                    Seeker item;
                    for (int i = 0; i < seekers.size(); i++) {
                        item = seekers.get(i);
                        itemPrx = seekersPrx.get(i);
                        itemPrx.notify_async(new NotifyAMICallback(item, "finalizeCollabSession"), array);

                        //notify search principles
                        notifyNewChairman(sessionName, item, itemPrx, false, chairman.getUser());
                    }

                    this.updateServer(UPDATE_SESSIONS, DECREMENT, sessionName);
                    recordSession.setStopDate(new Date());
                    // almaceno temporalmente los valores de la sesión
                    // para su análisis

                    SessionProfile prof = this.collaborativeSessions.remove(sessionName);
                    this.htTempSessions.put(sessionName, prof);
                    this.updateAvailableSession(sessionName, null, null, END_COLLAB_SESSION, seekers);

                } else {
                    throw new SessionException("The seeker '" + chairman.getUser() + "' is not the chairman of the session");
                }
            } else {
                throw new SessionException("The seeker '" + chairman.getUser() + "' doesn't have privileges to eliminate the session");
            }
        } else {
            throw new SessionException("The session '" + sessionName + "' doesn't exist.");
        }

    }

    private synchronized void finalizeCollabSession(final String sessionName, int cause, SessionProfile recordSession) throws SessionException, IOException {

        SeekerInfo rec = recordSession.getSeekerInfo();
        Response rsp = ResponseSessionFactory.getResponse(sessionName, cause);
        byte[] array = rsp.toArray();
        List<Seeker> seekers = getSeekersList(rec);
        List<ClientSidePrx> seekersPrx = getClientSidePrxList(rec);

        ClientSidePrx itemPrx;
        Seeker item;
        for (int i = 0; i < seekers.size(); i++) {
            item = seekers.get(i);
            itemPrx = seekersPrx.get(i);
            itemPrx.notify_async(new NotifyAMICallback(item, "finalizeCollabSession"), array);
        }

        this.updateServer(UPDATE_SESSIONS, DECREMENT, sessionName);
        recordSession.setStopDate(new Date());
        // almaceno temporalmente los valores de la sesión
        // para su análisis
        this.htTempSessions.put(sessionName, this.collaborativeSessions.remove(sessionName));
        this.updateAvailableSession(sessionName, null, null, END_COLLAB_SESSION, seekers);
    }

    /**
     * {@inheritDoc}
     */
    public synchronized String[] getSessionsNames() {
        int size = this.collaborativeSessions.size();
        String[] sessions = this.collaborativeSessions.keySet().toArray(new String[size]);
        return sessions;
    }

    /**
     * {@inheritDoc}
     */
    public synchronized java.util.List<Seeker> getOnlineMembers(String sessionName) throws SessionException {

        SessionProfile sessionProfile;
        if (sessionName.equals(getCommunicationSessionName())) {
            sessionProfile = this.defaultSessionProfile;
        } else if (existCollabSession(sessionName)) {
            sessionProfile = this.collaborativeSessions.get(sessionName);
        } else {
            throw new SessionException("The session '" + sessionName + "' doesn't exist.");
        }
        SeekerInfo seekersRecord = sessionProfile.getSeekerInfo();
        List<Seeker> seekersList = new ArrayList<>(seekersRecord.record.keySet());

        return seekersList;
    }

    /**
     * {@inheritDoc}
     * @param prx
     */
    public synchronized Seeker login(String user, String password, ClientSidePrx prx) throws RequestException {
        Seeker seeker = null;
        if (dbController != null && dbController.isOpen()) {
            try {
                boolean correct = this.dbController.compareSeekerPassword(user, password);
                if (correct) {
                    SeekerData seekerData;
                    seekerData = this.dbController.getSeekerData(user);
                    int rol = seekerData.getRol();
                    int state = seekerData.getState();
//                    BufferedImage buffer = ImageUtil.toBufferedImage(seekerData.getAvatar());
                    seeker = new Seeker(seekerData.getUser(), rol, state, seekerData.getAvatar());
                }
            } catch (SQLException ex) {
                OutputMonitor.printStream("",ex);
            }
        }

        return seeker;
    }

    /**
     *
     * @param sessionName
     * @return
     * @throws SessionException
     */
    protected SessionProfile getSessionProfile(String sessionName) throws SessionException {
        SessionProfile recordSession = null;

        if (sessionName.equals(getCommunicationSessionName())) {
            // se actualiza el role del miembro en el registro de la sesión
            recordSession = this.defaultSessionProfile;

        } else if (existCollabSession(sessionName)) {
            recordSession = this.collaborativeSessions.get(sessionName);
        } else {
            throw new SessionException("The session '" + sessionName + "' doesn't exist.");
        }
        return recordSession;
    }

    /******************************SEEKER**************************************/
    /**
     * {@inheritDoc}
     */
    public synchronized void updateStateSeeker(String sessionName, final Seeker seeker, final int status) throws SessionException, UserStatusNotSupportedException, SeekerException, IOException {

        if (status == Seeker.STATE_ONLINE || status == Seeker.STATE_BUSY || status == Seeker.STATE_AWAY || status == Seeker.STATE_OFFLINE) {

            SessionProfile recordSession = getSessionProfile(sessionName);

            // se actualiza el role del miembro en el registro de la sesión

            SeekerInfo seekersRecord = recordSession.getSeekerInfo();
            Set<Seeker> enMembers;
            
            boolean flag = existsSeeker(seeker, seekersRecord);
            if (flag) {
                enMembers = seekersRecord.record.keySet();
                for (Seeker item : enMembers) {
                    if (item.equals(seeker)) {
                        item.setState(status);
                        break;
                    }
                }
               
                // notifico la actualización del estado a los miembros de la
                // sesión, exceptuando el usuario
                List<ClientSidePrx> seekersOnlinePrx = getClientSidePrxList(seekersRecord);
                List<Seeker> seekersOnline = getSeekersList(seekersRecord);

                if (seekersOnline.size() > 0) {
                    this.notifyUpdateSession(sessionName, seekersOnline, seekersOnlinePrx, seeker, UPDATE_SEEKER_PROFILE);
                }
            } else {
                throw new SeekerException("The seeker '" + ((seeker != null) ? seeker.getUser() : "null") + "' is not registered in the session '" + sessionName + "'.");
            }
        } else {
            throw new UserStatusNotSupportedException("The seeker status is not supported");
        }


        Thread thread = new Thread(new Runnable() {

            public void run() {
                try {

                    if (dbController != null && dbController.isOpen()) {
                        dbController.updateSeekerState(seeker.getUser(), status);

                    }
                } catch (SQLException ex) {
                    OutputMonitor.printStream("",ex);
                }
            }
        });

        thread.start();
    }

    /**
     * {@inheritDoc}
     */
    public synchronized void updateAvatarSeeker(final String sessionName, final Seeker seeker, final byte[] avatar) throws SessionException, SeekerException, IOException {

        SessionProfile recordSession = getSessionProfile(sessionName);
        SeekerInfo seekersRecord = recordSession.getSeekerInfo();

        if (existsSeeker(seeker, seekersRecord)) {
            Set<Seeker> enMembers = seekersRecord.record.keySet();
           
            for (Seeker item : enMembers) {
                if (item.equals(seeker)) {
                    item.setAvatar(avatar);

                    break;
                }
            }
            

            List<Seeker> seekersOnline = getSeekersList(seekersRecord);

            if (seekersOnline.size() > 0) {
                this.notifyUpdateSession(sessionName, seekersOnline, getClientSidePrxList(seekersRecord), seeker, UPDATE_SEEKER_PROFILE);

            }

        } else {
            throw new SeekerException("The seeker '" + ((seeker != null) ? seeker.getUser() : "null") + "' is not registered in the session '" + sessionName + "'.");


        }

        Thread thread = new Thread(new Runnable() {

            public void run() {
                try {
                    if (dbController != null && dbController.isOpen()) {
//                        byte[] image = ImageUtil.toByte(ImageUtil.makeBufferedImage(avatar.getImage()));
//                        dbController.updateSeekerAvatar(seeker.getUser(), image);
                        dbController.updateSeekerAvatar(seeker.getUser(), avatar);

                    }

                } catch (SQLException ex) {
                    OutputMonitor.printStream("",ex);
                }


            }
        });
        thread.start();



    }

    /**
     * {@inheritDoc}
     */
    public synchronized void loginSeeker(final String sessionName, final Seeker seeker, final ClientSidePrx seekerPrx) throws SessionException, IOException {

        if (sessionName.equals(getCommunicationSessionName())) {
            SeekerInfo seekersRecord = defaultSessionProfile.getSeekerInfo();


            if (existsSeeker(seeker, seekersRecord)) {
                ClientSidePrx itemPrx = seekersRecord.record.get(seeker);
                try {
                    itemPrx.ice_ping();

                } catch (Exception e) {
                    updateCommunicationSession(seeker);
                    loginSeeker(sessionName, seeker, seekerPrx);
                    return;
                }

                Response rsp = ResponseSessionFactory.getResponse(CONNECTION_FAILED, sessionName, null, null, null, null, null, null);
                // se notificar al cliente que no se pudo agregar a la sesión
                seekerPrx.notify_async(new NotifyAMICallback(seeker, "loginSeeker"), rsp.toArray());
                seekerPrx.disconnect_async(new DisconnectAMICallback("loginSeeker", seeker, seekerPrx));


                return;



            } else {
                List<Session> availableSession = new ArrayList<>();
                //  en caso de no estar registrado, este se adiciona a la
                // sesión y se notifica su entrada al mismo y a toda la sesión.
                List<Seeker> seekersList = new ArrayList<>(seekersRecord.record.keySet());
                int size = seekersList.size();

                try {

                    if (dbController != null && dbController.isOpen()) {
                        dbController.updateSeekerState(seeker.getUser(), Seeker.STATE_ONLINE);
                        availableSession = this.dbController.getAllAvailableSessions(seeker.getUser());
                    }
                } catch (SQLException ex) {
                    OutputMonitor.printStream("",ex);
                }

                List<String> principles = retrievalManager.getSearchPrinciples(RetrievalManager.INDIVIDUAL_SEARCH);
                @SuppressWarnings("unchecked")
                List<Session> activeSessions = new ArrayList<>();
                Collection<SessionProfile> profiles = this.collaborativeSessions.values();
                String topic, descrip, chairmanName;


                for (SessionProfile sessionProfile : profiles) {

                    topic = sessionProfile.getProperties().getSessionName();
                    descrip = sessionProfile.getProperties().getDescription();
                    chairmanName = sessionProfile.getProperties().getChairman();

                    activeSessions.add(new Session(topic, descrip, chairmanName));
                }

                String[] searchers = retrievalManager.getAvailableSearchers();
                String[] repositories = retrievalManager.getAvailableSVNRepositories();

                Response response = ResponseSessionFactory.getResponse(CONNECTION_SUCCESSFUL, sessionName, seekersList, searchers, repositories, principles, activeSessions, availableSession);
                seekersRecord.record.put(seeker, seekerPrx);
                seekerPrx.notify_async(new NotifyAMICallback(seeker, "loginSeeker-gsdgdggg"), response.toArray());



                if (size > 0) {
                    this.notifyUpdateSession(sessionName, getSeekersList(seekersRecord), getClientSidePrxList(seekersRecord), seeker, SEEKER_LOGIN);


                }
                this.updateServer(UPDATE_COUNT_MEMBERS, INCREMENT);
                SessionProperty properties = defaultSessionProfile.getProperties();



                int membersCurrentNumber = properties.getMembersCurrentNumber();
                membersCurrentNumber++;

            }


        } else {
            throw new SessionException("The session '" + sessionName + "' doesn't exist.");


        }
    }
    static ClientSidePrx seekerPrx;

    /**
     * {@inheritDoc}
     */
    public synchronized void logoutSeeker(final String sessionName, Seeker seeker) throws SessionException, SeekerException, IOException {
        if (sessionName.equals(getCommunicationSessionName())) {
            SeekerInfo seekersRecord = defaultSessionProfile.getSeekerInfo();

            if (existsSeeker(seeker, seekersRecord)) {
                ClientSidePrx prx = seekersRecord.record.remove(seeker);
                prx.disconnect_async(new DisconnectAMICallback("logoutSeeker", seeker, prx));
                List<Seeker> seekersOnline = getSeekersList(seekersRecord);
                if (seekersOnline.size() > 0) {
                    this.notifyUpdateSession(sessionName, seekersOnline, getClientSidePrxList(seekersRecord), seeker, SEEKER_LOGOUT);
                }

                this.updateCollaborativeSessions(seeker);
                this.updateServer(UPDATE_COUNT_MEMBERS, DECREMENT);
                final String user = seeker.getUser();
                Thread thread = new Thread(new Runnable() {

                    public void run() {
                        try {

                            if (dbController != null && dbController.isOpen()) {
                                dbController.updateSeekerState(user, Seeker.STATE_OFFLINE);
                            }
                        } catch (SQLException ex) {
                            OutputMonitor.printStream("",ex);
                        }
                    }
                });

                thread.start();

            } else {
                throw new SeekerException("The seeker '" + ((seeker != null) ? seeker.getUser() : "null") + "' is not registered in the session '" + sessionName + "'.");
            }
        } else {
            throw new SessionException("The session '" + sessionName + "' doesn't exist.");
        }
    }

    /**
     *
     * @param seeker
     * @throws SessionException
     * @throws SeekerException
     * @throws IOException
     */
    public synchronized void updateSeekersOnline(Seeker seeker) throws SessionException, SeekerException, IOException {
        SeekerInfo seekersRecord = defaultSessionProfile.getSeekerInfo();

        seekersRecord.record.remove(seeker);
        List<Seeker> seekersOnline = getSeekersList(seekersRecord);

        if (seekersOnline.size() > 0) {
            this.notifyUpdateSession(defaultSessionProfile.getProperties().getSessionName(), seekersOnline, getClientSidePrxList(seekersRecord), seeker, SEEKER_LOGOUT);
        }

        this.updateCollaborativeSessions(seeker);
        this.updateServer(UPDATE_COUNT_MEMBERS, DECREMENT);

    }

    private List<Seeker> getCollabSessionsSeekers() {
        List<Seeker> list = new ArrayList<>();
        Collection<SessionProfile> sessions = this.collaborativeSessions.values();
        for (SessionProfile sessionProfile : sessions) {
            list.addAll(sessionProfile.getSeekerInfo().record.keySet());

        }
        return list;
    }

    public void updateCommunicationSession(final Seeker seeker) {
        updateCollaborativeSessions(seeker);

        SeekerInfo seekersRecord = defaultSessionProfile.getSeekerInfo();

        if (existsSeeker(seeker, seekersRecord)) {
            seekersRecord.record.remove(seeker);

            List<Seeker> seekersOnline = getSeekersList(seekersRecord);
            seekersOnline.removeAll(getCollabSessionsSeekers());

            if (seekersOnline.size() > 0) {
                try {
                    this.notifyUpdateSession(defaultSessionProfile.getProperties().getSessionName(), seekersOnline, getClientSidePrxList(seekersRecord, seekersOnline), seeker, SEEKER_LOGOUT);
                } catch (SessionException ex) {
                    OutputMonitor.printStream("",ex);
                }
            }


            this.updateServer(UPDATE_COUNT_MEMBERS, DECREMENT);
            final String user = seeker.getUser();
            Thread thread = new Thread(new Runnable() {

                public void run() {
                    try {

                        if (dbController != null && dbController.isOpen()) {
                            dbController.updateSeekerState(user, Seeker.STATE_OFFLINE);

                        }
                    } catch (SQLException ex) {
                        OutputMonitor.printStream("",ex);
                    }
                }
            });

            thread.start();

        }

    }

    /**
     *
     * @param seeker
     */
    private void updateCollaborativeSessions(final Seeker seeker) {
        Collection<SessionProfile> profiles = collaborativeSessions.values();
        SeekerInfo seekersRecord;

        for (SessionProfile object : profiles) {
            seekersRecord = object.getSeekerInfo();

            if (existsSeeker(seeker, seekersRecord)) {
                try {
                    updateSeekersCollabSession(object, seeker);
                    return;
                } catch (        SessionException | SeekerException | IOException ex) {
                    OutputMonitor.printStream("",ex);
                }


            }


        }


    }

    /**
     * {@inheritDoc}
     */
    public synchronized void logoutCollabSession(final String sessionName, final Seeker seeker) throws SessionException, SeekerException, IOException {
        if (this.existCollabSession(sessionName)) {
            final SessionProfile recordSession = this.collaborativeSessions.get(sessionName);
            final SeekerInfo seekersRecord = recordSession.getSeekerInfo();
            if (existsSeeker(seeker, seekersRecord)) {
                SessionProperty properties = recordSession.getProperties();
                int integrityCriteria = properties.getIntegrityCriteria();
                switch (integrityCriteria) {

                    case SESSION_SOFT:

                        final ClientSidePrx itemPrx1 = seekersRecord.record.remove(seeker);
                        final Chairman ch = recordSession.getChairman();

                        final List<ClientSidePrx> seekerOnlinePrx = getClientSidePrxList(seekersRecord);
                        final List<Seeker> seekersOnline = getSeekersList(seekersRecord);

                        Response r = ResponseSessionFactory.getResponse(SELF_LOGOUT_COLLAB_SESSION, sessionName);
                        itemPrx1.notify_async(new NotifyAMICallback(seeker, "logoutCollabSession"), r.toArray());

                        Thread t = new Thread(new Runnable() {

                            public void run() {

                                try {
                                    int size = seekersRecord.record.size();
                                    notifyUpdateSession(sessionName, seekersOnline, seekerOnlinePrx, seeker, SEEKER_LOGOUT_COLLAB_SESSION);
                                    if (size > 1) {
                                        if (seeker.equals(ch.getSeeker()) || seeker.equals(recordSession.getTempChairman())) {
                                            //notify search principles
                                            notifyUpdateSearchPrinciples(sessionName, seeker, itemPrx1, false);
                                            Seeker item = seekersOnline.get(0);
                                            item.setRole(Seeker.ROLE_CHAIRMAN);
                                            try {
                                                notifyNewChairman(sessionName, item, seekerOnlinePrx.get(0), true, item.getUser());
                                            } catch (IOException ex) {
                                                OutputMonitor.printStream("",ex);
                                            }
                                            notifyUpdateSession(sessionName, seekersOnline, seekerOnlinePrx, item, NEW_CHAIRMAN_COLLAB_SESSION);
                                        }
                                    } else {
                                        if (seeker.equals(ch.getSeeker()) || seeker.equals(recordSession.getTempChairman())) {
                                            //notify search principles
                                            notifyUpdateSearchPrinciples(sessionName, seeker, itemPrx1, false);
                                        }
                                        finalizeCollabSession(sessionName, END_COLLAB_SESSION, recordSession);
                                    }
                                } catch (                        SessionException | IOException ex) {
                                    OutputMonitor.printStream("",ex);
                                }
                            }
                        });
                        t.start();

                        this.updateServer(UPDATE_COUNT_MEMBERS, DECREMENT);


                        break;



                    case SESSION_HARD:
                        ClientSidePrx itemPrx = seekersRecord.record.remove(seeker);


                        this.notifyCommitTransaction(INFORMATION_MESSAGE, "Session's exit satisfactorily", seeker, itemPrx);

                        List<ClientSidePrx> seekersPrx = getClientSidePrxList(seekersRecord);
                        List<Seeker> seekersList = getSeekersList(seekersRecord);

                        Response rsp = ResponseSessionFactory.getResponse(SEEKER_LOGOUT_COLLAB_SESSION, sessionName);
                        final byte[] array = rsp.toArray();

                        Response rsp1 = ResponseSessionFactory.getResponse(sessionName, HARD_INTEGRITY_CRITERIA);
                        final byte[] array1 = rsp1.toArray();
                        ClientSidePrx item;

                        for (int i = 0; i < seekersPrx.size(); i++) {
                            item = seekersPrx.get(i);
                            item.notify_async(new NotifyAMICallback(seekersList.get(i), "logoutCollabSession"), array);
                          
                            item.notify_async(new NotifyAMICallback(seekersList.get(i), "finalizedSession"), array1);
                        }
                        finalizeCollabSession(sessionName, END_SESSION, recordSession);

                        // almaceno temporalmente los valores de la sesión
                        // para su análisis
                        this.htTempSessions.put(sessionName, recordSession);
                        this.collaborativeSessions.remove(sessionName);
                        this.updateAvailableSession(sessionName, null, null, END_COLLAB_SESSION, seekersList);
                        this.updateServer(UPDATE_SESSIONS, DECREMENT, sessionName);

                        break;



                    default:
                        throw new SessionException("The Integrity Criteria of the session is not valid.");


                }
            } else {
                throw new SeekerException("The seeker '" + ((seeker != null) ? seeker.getUser() : "null") + "' is not registered in the session '" + sessionName + "'.");


            }
        } else {
            throw new SessionException("The session '" + sessionName + "' doesn't exist.");


        }
    }

    /**
     *
     * @param recordSession
     * @param seeker
     * @throws SessionException
     * @throws SeekerException
     * @throws IOException
     */
    public synchronized void updateSeekersCollabSession(final SessionProfile recordSession, final Seeker seeker) throws SessionException, SeekerException, IOException {
        final SeekerInfo seekersRecord = recordSession.getSeekerInfo();
        final String sessionName = recordSession.getProperties().getSessionName();
        SessionProperty properties = recordSession.getProperties();
        int integrityCriteria = properties.getIntegrityCriteria();

        switch (integrityCriteria) {

            case SESSION_SOFT:

                seekersRecord.record.remove(seeker); // esto es por si acaso, tengo que verificar en todos
                // los casos que invoken este metodo, el proxy ya no exista...
                final Chairman ch = recordSession.getChairman();

                final List<ClientSidePrx> seekerOnlinePrx = getClientSidePrxList(seekersRecord);
                final List<Seeker> seekersOnline = getSeekersList(seekersRecord);


                Thread t = new Thread(new Runnable() {

                    public void run() {
                        try {
                            notifyUpdateSession(sessionName, seekersOnline, seekerOnlinePrx, seeker, SEEKER_LOGOUT_COLLAB_SESSION);
                            int size = seekersRecord.record.size();

                            if (size > 1) {
                                if (seeker.equals(ch.getSeeker()) || seeker.equals(recordSession.getTempChairman())) {

                                    List<Seeker> membs = getSeekersList(seekersRecord);
                                    List<ClientSidePrx> seekerOnlinePrx1 = getClientSidePrxList(seekersRecord);
                                    Seeker item = membs.get(0);
                                    recordSession.setTempChairman(item);
                                    item.setRole(Seeker.ROLE_CHAIRMAN);
                                    try {
                                        notifyNewChairman(sessionName, item, seekerOnlinePrx1.get(0), true, item.getUser());
                                    } catch (IOException ex) {
                                        OutputMonitor.printStream("",ex);
                                    }
                                    notifyUpdateSession(sessionName, membs, seekerOnlinePrx1, item, NEW_CHAIRMAN_COLLAB_SESSION);
                                }
                            } else {
                                finalizeCollabSession(sessionName, END_COLLAB_SESSION, recordSession);
                            }
                        } catch (                SessionException | IOException ex) {
                            OutputMonitor.printStream("",ex);
                        }
                    }
                });
                t.start();

                this.updateServer(UPDATE_COUNT_MEMBERS, DECREMENT);

                break;

            case SESSION_HARD:
                seekersRecord.record.remove(seeker);

                List<ClientSidePrx> seekersPrx = getClientSidePrxList(seekersRecord);
                List<Seeker> seekersList = getSeekersList(seekersRecord);

                Response rsp = ResponseSessionFactory.getResponse(SEEKER_LOGOUT_COLLAB_SESSION, sessionName);
                final byte[] array = rsp.toArray();

                ClientSidePrx item;

                for (int i = 0; i < seekersPrx.size(); i++) {
                    item = seekersPrx.get(i);
                    item.notify_async(new NotifyAMICallback(seekersList.get(i), "logoutCollabSession"), array);


                }
                finalizeCollabSession(sessionName, HARD_INTEGRITY_CRITERIA, recordSession);
                // almaceno temporalmente los valores de la sesión para su análisis
                this.htTempSessions.put(sessionName, recordSession);
                this.collaborativeSessions.remove(sessionName);
                this.updateAvailableSession(sessionName, null, null, END_COLLAB_SESSION, seekersList);
                this.updateServer(UPDATE_SESSIONS, DECREMENT, sessionName);

                break;

            default:
                throw new SessionException("The Integrity Criteria of the session is not valid.");
        }
    }

    /**
     * {@inheritDoc}
     */
    public synchronized boolean isAvailableUser(String userName) throws RequestException {

        boolean available = false;


        try {
            if (dbController.isOpen()) {
                available = dbController.existSeeker(userName);


                if (available) {
                    return false;


                } else {
                    return true;


                }
            } else {
                OutputMonitor.printLine("DataBase connection is close.", OutputMonitor.ERROR_MESSAGE);
            }

        } catch (SQLException ex) {
            OutputMonitor.printStream("",ex);
        }
        return available;
    }

    /**
     * {@inheritDoc}
     */
    public void registerSeeker(String nickname, String name, String password, String description, String email, byte[] avatar) {
        try {
            if (dbController.isOpen()) {
                dbController.registerSeeker(nickname, name, password, description, email, avatar, Seeker.ROLE_POTENTIAL_MEMBER, Seeker.STATE_OFFLINE);
                if (dbController.getEmail() != null) {
                    String separator = System.getProperty("file.separator");
                    XMLParser config = null;
                    StringBuffer body = new StringBuffer();

                    config = new XMLParser(new File("config" + separator + "email_service.xml"));
                    String text1 = config.getValue("message.welcome.body");
                    String text2 = config.getValue("message.welcome.body.username");
                    String text3 = config.getValue("message.welcome.body.name");
                    String text4 = config.getValue("message.welcome.body.password");
                    String text5 = config.getValue("message.welcome.body.assistence");
                    String text6 = config.getValue("message.welcome.body.site");
                    String text7 = config.getValue("message.welcome.body.footer");

                    String subject = config.getValue("message.welcome.subject");
                    String[] message = text1.split("#");

                    for (String elem : message) {
                        body.append(elem + "\n");
                    }
                    body.append(text2 + " " + nickname + "\n");
                    body.append(text3 + " " + name + "\n");
                    body.append(text4 + " " + password + "\n");
                    body.append(text5 + "\n");
                    body.append(text6 + "\n");

                    String[] message2 = text7.split("#");

                    for (String elem2 : message2) {
                        body.append(elem2 + "\n");
                    } //Enviar email

                    boolean send = this.dbController.send(email, subject, true, body, true);
                }
            }
        } catch (Exception ex) {
            OutputMonitor.printStream("",ex);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void recoverPassword(String user) throws RequestException {

        try {
            if (dbController.isOpen()) {
                StringBuffer body = new StringBuffer();
                float number = (float) Math.random() * 100000;
                int newPass = Math.round(number);
                String generated = String.valueOf(newPass);

                dbController.updateSeekerPassword(user, DrakkarSecurity.SHA(generated));

                if (dbController.getEmail() != null) {
                    String separator = System.getProperty("file.separator");
                    XMLParser config = null;
                    try {
                        config = new XMLParser(new File("config" + separator + "email_service.xml"));
                        String text1 = config.getValue("message.recover.body");
                        String text2 = config.getValue("message.recover.body.username");
                        String text3 = config.getValue("message.recover.body.password");
                        String text4 = config.getValue("message.recover.body.assistence");
                        String text5 = config.getValue("message.recover.body.site");
                        String text6 = config.getValue("message.recover.body.footer");
                        String subject = config.getValue("message.recover.subject");
                        String[] message = text1.split("#");

                        for (String elem : message) {
                            body.append(elem + "\n");
                        }
                        body.append(text2 + " " + user + "\n");
                        body.append(text3 + " " + generated + "\n");
                        body.append(text4 + "\n");
                        body.append(text5 + "\n");

                        String[] message2 = text6.split("#");
                        for (String elem2 : message2) {
                            body.append(elem2 + "\n");
                        } //Enviar email

                        boolean send = this.dbController.send(this.dbController.getSeekerEmail(user), subject, true, body, true);
                    } catch (Exception ex) {
                        OutputMonitor.printStream("",ex);
                        this.notify(ERROR_MESSAGE, ex.getMessage());
                    }
                }
            }
        } catch (SQLException ex) {
            OutputMonitor.printStream("",ex);
        }
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
     * {@inheritDoc}
     */
    public boolean changePassword(String user, String oldPassword, String newPassword) throws RequestException {
        try {
            if (dbController != null && dbController.isOpen()) {
                boolean match = dbController.compareSeekerPassword(user, oldPassword);
                if (match) {
                    dbController.updateSeekerPassword(user, newPassword);
                    if (dbController.getEmail() != null) {
                        XMLParser config;
                        String separator = System.getProperty("file.separator");
                        StringBuffer body = new StringBuffer();
                        try {
                            config = new XMLParser(new File("config" + separator + "email_service.xml"));
                            String text1 = config.getValue("message.change.body");
                            String text2 = config.getValue("message.change.body.username");
                            String text3 = config.getValue("message.change.body.password");
                            String text4 = config.getValue("message.change.body.assistence");
                            String text5 = config.getValue("message.change.body.site");
                            String text6 = config.getValue("message.change.body.footer");

                            String subject = config.getValue("message.change.subject");
                            String[] message = text1.split("#");

                            for (String elem : message) {
                                body.append(elem + "\n");
                            }
                            body.append(text2 + " " + user + "\n");
                            body.append(text3 + " " + newPassword + "\n");
                            body.append(text4 + "\n");
                            body.append(text5 + "\n");

                            String[] message2 = text6.split("#");
                            for (String elem2 : message2) {
                                body.append(elem2 + "\n");
                            } //Enviar email

                            boolean send = this.dbController.send(this.dbController.getSeekerEmail(user), subject, true, body, true);

                        } catch (Exception ex) {
                            OutputMonitor.printStream("",ex);
                            this.notify(ERROR_MESSAGE, ex.getMessage());
                        }
                    }
                    return true;
                }
            }
        } catch (SQLException ex) {
            OutputMonitor.printStream("",ex);
        }

        return false;
    }

    /********************************SERVER*****************************/
    /**
     * Este método notifica a los miembros de las sesiones del servidor, el estado
     * del mismo.
     *
     * @param status estado del servidor
     *
     * @param operation 
     * @throws IOException   si ocurre alguna excepción durante el proceso de
     *                       serialización del objeto Response
     * @throws IllegalArgumentException
     */
    public synchronized void notifyServerState(int status, String operation) throws IOException, IllegalArgumentException {

        if (defaultSessionProfile != null) {
            if (status == SERVER_STOPPED || status == SERVER_RESET) {

                Map<Object, Object> hash = new HashMap<>(2);
                hash.put(OPERATION, NOTIFY_SERVER_STATE);
                hash.put(SERVER_STATE, status);
                Response rsp = new Response(hash);

                byte[] array = rsp.toArray();

                SeekerInfo recordMembers;
                Collection<ClientSidePrx> membersPrx;

                recordMembers = defaultSessionProfile.getSeekerInfo();
                membersPrx = recordMembers.record.values();

                for (ClientSidePrx clientSidePrx : membersPrx) {
                    clientSidePrx._notify(array);
                }
               
                this.defaultSessionProfile.clear();
                this.collaborativeSessions.clear();

            } else {
                throw new IllegalArgumentException("The status of the server is not supported");
            }
        }
    }

    /**
     *
     * @param operation
     * @param action
     */
    protected void updateServer(int operation, int action) {
        if (listener != null) {
            Response rs = new Response();
            rs.put(OPERATION, operation);
            rs.put(DISTRIBUTED_EVENT, action);

            FacadeDesktopEvent evt = new FacadeDesktopEvent(this, rs);
            listener.notify(evt);
        }
    }

    /**
     *
     * @param operation
     * @param action
     * @param sessionName
     */
    protected void updateServer(int operation, int action, String sessionName) {
        if (listener != null) {
            Response rs = new Response();
            rs.put(OPERATION, operation);
            rs.put(DISTRIBUTED_EVENT, action);
            rs.put(SESSION_NAME, sessionName);
            FacadeDesktopEvent evt = new FacadeDesktopEvent(this, rs);
            listener.notify(evt);
        }
    }

    /**
     * Este método se emplea para actualizar la relación de sesiones registradas
     * en el servidor en la aplicación del cliente.
     *
     * @param sessionName nombre de la sesión a la que pertenece el miembro.
     * @param seeker      miembro que ejecuta la búsqueda.
     */
    private synchronized void updateAvailableSession(final String sessionName, final String description, final String chairman, final int event, final List<Seeker> excludeSeekers) {
        Thread t = new Thread(new Runnable() {

            public void run() {
                SessionProfile recordSession = defaultSessionProfile;
                SeekerInfo seekersPrx = recordSession.getSeekerInfo();
                List<Seeker> notify = new ArrayList<>(seekersPrx.record.keySet());
                notify.removeAll(excludeSeekers);

                Map<Object, Object> hash = new HashMap<>(4);
                hash.put(OPERATION, NOTIFY_AVAILABLE_COLLAB_SESSION);
                hash.put(DISTRIBUTED_EVENT, event);
                hash.put(SESSION_NAME, sessionName);

                if (event == BEGIN_COLLAB_SESSION) {
                    hash.put(SESSION_DESCRIPTION, description);
                    hash.put(CHAIRMAN_NAME, chairman);
                }

                Response rsp = new Response(hash);

                byte[] array = null;

                try {
                    array = rsp.toArray();
                    ClientSidePrx clientSidePrx;
                    for (Seeker item : notify) {
                        clientSidePrx = seekersPrx.record.get(item);
                        clientSidePrx.notify_async(new NotifyAMICallback(item, "updateAvailableSession"), array);
                    }

                } catch (IOException ex) {
                    OutputMonitor.printStream("",ex);
                }
            }
        });
        t.start();

    }

    /**
     * Este método notifica al miembro que invoca una operación el resultado final
     * de la misma.
     *
     * @param messageType   Posibles tipos de mensaje Assignable.INFORMATION_MESSAGE, Assignable.ERROR_MESSAGE,
     *                      ambos valores definidos en la interfaz Assignable.
     * @param message       descripción de la operación realizada ó causa del error.
     * @param seeker
     * @param seekerPrx
     */
    public synchronized void notifyCommitTransaction(int messageType, String message, Seeker seeker, ClientSidePrx seekerPrx) {

        if (seekerPrx != null) {
            Response rsp = ResponseUtilFactory.getResponse(messageType, message);

            try {
                seekerPrx.notify_async(new NotifyAMICallback(seeker, "notifyCommitTransaction"), rsp.toArray());
            } catch (IOException ex) {
                OutputMonitor.printStream("Error al serializar el objeto response.", ex);
            }
        } else {
            OutputMonitor.printLine("Error al notificar al seeker el estado final de"
                    + " la transacción invocada. Causa: Objeto Seeker nulo.", OutputMonitor.ERROR_MESSAGE);
        }
    }

    /**
     * Este método actualiza los miembros registrados en una sesión así como su
     * actual estado.
     *
     * @param sessionName   nombre de la sesión
     * @param seeker        miembro a registrar
     * @param event    tipo de actualización de la sesión.Posibles valores
     *                      Assignable.LOGIN, Assignable.USER_LOGOUT, Identifiable.SEEKER_STATE,Identifiable.SEEKER_CHAIRMAN,
     *                      constantes definidas en la interfaz <code>Identifiable </code>
     * @throws SessionException  si la sesión no se encuentra registrada
     *                                   en el servidor
     * @throws IllegalArgumentException si el tipo de actualzación a relizar no es
     *                                  soportada.
     * @throws IOException   si ocurre alguna excepción durante el proceso de
     *                       serialización del objeto Response
     */
    private synchronized void notifyUpdateSession(final String sessionName, final List<Seeker> seekersOnline, final List<ClientSidePrx> seekersOnlinePrx, final Seeker seeker, final int event) throws SessionException {
        int index = seekersOnline.indexOf(seeker);

        if (index >= 0) {
            seekersOnline.remove(index);
            seekersOnlinePrx.remove(index);
        }

        String defaultSession = defaultSessionProfile.getProperties().getSessionName();

        if (sessionName.equals(defaultSession) || existCollabSession(sessionName)) {
            Map<Object, Object> hash = new HashMap<>(4);

            switch (event) {
                case SEEKER_LOGIN:
                    hash.put(OPERATION, NOTIFY_SEEKER_EVENT);
                    hash.put(DISTRIBUTED_EVENT, event);
                    hash.put(SEEKER, seeker);

                    break;

                case SEEKER_LOGOUT:
                    hash.put(OPERATION, NOTIFY_SEEKER_EVENT);
                    hash.put(DISTRIBUTED_EVENT, event);
                    hash.put(SEEKER, seeker);

                    break;

                case UPDATE_SEEKER_PROFILE:
                    hash.put(OPERATION, NOTIFY_SEEKER_EVENT);
                    hash.put(DISTRIBUTED_EVENT, event);
                    hash.put(SEEKER, seeker);

                    break;

                case NEW_CHAIRMAN_COLLAB_SESSION:
                    hash.put(OPERATION, NOTIFY_CHAIRMAN_SETTING);
                    hash.put(DISTRIBUTED_EVENT, event);
                    hash.put(CHAIRMAN_NAME, seeker.getUser());

                    break;

                case SEEKER_LOGIN_COLLAB_SESSION:
                    hash.put(OPERATION, NOTIFY_COLLAB_SESSION_AUTHENTICATION);
                    hash.put(DISTRIBUTED_EVENT, event);
                    hash.put(SEEKER, seeker);

                    break;

                case SEEKER_LOGOUT_COLLAB_SESSION:
                    hash.put(OPERATION, NOTIFY_COLLAB_SESSION_AUTHENTICATION);
                    hash.put(DISTRIBUTED_EVENT, event);
                    hash.put(SEEKER, seeker);
                    hash.put(CAUSE, SEEKER_LOGOUT);

                    break;

                default:
                    throw new IllegalArgumentException("The type of update is not supported");
            }

            hash.put(SESSION_NAME, sessionName);

            Response response = new Response(hash);
            final byte[] array;

            try {
                array = response.toArray(); // se serializa el objeto response
                ClientSidePrx client;

                for (int i = 0; i < seekersOnline.size(); i++) {
                    client = seekersOnlinePrx.get(i);
                    client.notify_async(new NotifyAMICallback(seekersOnline.get(i), "notifyUpdateSession"), array);
                }
            } catch (IOException ex) {
            }

        } else {
            throw new SessionException("The session '" + sessionName + "' doesn't exist.");
        }
    }

    /**
     * {@inheritDoc}
     */
    public synchronized int getSessionsCount() {
        return this.collaborativeSessions.size();
    }

    /**
     * {@inheritDoc}
     */
    public synchronized int getSeekersCount() {
        Collection<SessionProfile> profiles = this.collaborativeSessions.values();
        SeekerInfo recordMembers;
        int count = 0;
        
        for (SessionProfile sessionProfile : profiles) {
            recordMembers = sessionProfile.getSeekerInfo();
            count += recordMembers.record.size();
        }
      
        return count;
    }

    /**
     * {@inheritDoc}
     */
    public synchronized int getSeekersCount(String sessionName) throws SessionException {
        if (this.htTempSessions.containsKey(sessionName)) {
            SessionProfile recordSession = this.htTempSessions.get(sessionName);
            SeekerInfo recordMembers = recordSession.getSeekerInfo();
            return recordMembers.record.size();
        } else {
            throw new SessionException("The session '" + sessionName + "' doesn't exist.");
        }
    }

    private void joinSeekerDB(final String sessionName, final Seeker seeker) {

        Thread thread = new Thread(new Runnable() {

            public void run() {
                try {
                    if (dbController != null && dbController.isOpen()) {
                        dbController.joinNewSeeker(seeker.getUser(), sessionName);
                    }
                } catch (SQLException ex) {
                    OutputMonitor.printStream("",ex);
                }
            }
        });
        thread.start();
    }

    /**
     * {@inheritDoc}
     */
    public void deleteCollabSession(String sessionName, Seeker chairman, ClientSidePrx chairmanProxy) throws SessionException, SeekerException {
        try {
            if (dbController != null && dbController.isOpen()) {
                if (this.existCollabSession(sessionName)) {
                    SessionProfile profile = collaborativeSessions.get(sessionName);
                    Chairman cacheChairman = profile.getChairman();

                    if (cacheChairman.getName().equals(chairman.getUser())) {
                        finalizeCollabSession(sessionName, chairman);
                        this.dbController.closeCollabSession(sessionName);
                        notifyDeletedSession(sessionName, profile.getSeekerInfo());
                    }
                } else {
                    boolean flag = this.dbController.existSession(sessionName);
                    if (flag) {
                        this.dbController.closeCollabSession(sessionName);
                        notifyDeletedSession(sessionName);
                    }
                }
            }
        } catch (SQLException | IOException ex) {
            OutputMonitor.printStream("",ex);
            this.notifyCommitTransaction(ERROR_MESSAGE,
                    "No pudo completar la acción de dar baja de la sesión.", chairman, chairmanProxy);
        }
    }

    private void notifyDeletedSession(String sessionName, SeekerInfo seekersRecord) throws SQLException, IOException {
        List<ClientSidePrx> seekersPrx = getClientSidePrxList(seekersRecord);
        List<Seeker> seekersList = getSeekersList(seekersRecord);
        Response response = ResponseSessionFactory.getResponse(DELETED_COLLAB_SESSION, sessionName);

        ClientSidePrx item;

        for (int i = 0; i  < seekersPrx.size(); i++) {
            item = seekersPrx.get(i);
            item.notify_async(new NotifyAMICallback(seekersList.get(i), "notifyDeletedSession"), response.toArray());
        }
    }

    private void notifyDeletedSession(final String sessionName) throws SQLException, IOException {
        final List<SessionProfile> col = new ArrayList<>(this.collaborativeSessions.values());

        Thread thread = new Thread(new Runnable() {

            public void run() {
                for (SessionProfile item : col) {
                    try {
                        notifyDeletedSession(sessionName, item.getSeekerInfo());
                    } catch (            SQLException | IOException ex) {
                        OutputMonitor.printStream("",ex);
                    }
                }
            }
        });
        thread.start();
    }

    /**
     * {@inheritDoc}
     */
    public void declineSeekerCollabSession(String sessionName, Seeker seeker, ClientSidePrx seekerProxy) throws SessionException, SeekerException {
        try {
            Response res = ResponseSessionFactory.getResponse(DECLINE_SEEKER_COLLAB_SESSION, sessionName);
            if (this.existCollabSession(sessionName)) {
                SessionProfile profile = collaborativeSessions.get(sessionName);

                if (existsSeeker(seeker, profile.getSeekerInfo())) {
                    //DB
                    if (dbController != null && dbController.isOpen()) {
                        boolean decline = this.dbController.declineSeekerCollabSession(sessionName, seeker.getUser());
                        if (decline) {
                            logoutCollabSession(sessionName, seeker);
                            seekerProxy.notify_async(new NotifyAMICallback(seeker, "declineSeekerCollabSession"), res.toArray());
                        } else {
                            this.notifyCommitTransaction(ERROR_MESSAGE, "No se pudo dar de baja de la sesión " + sessionName + "\n" + "El usuario no pertenece a esta sesión.", seeker, seekerProxy);
                        }
                    } else {
                        this.notifyCommitTransaction(ERROR_MESSAGE, "No existe una conexión con la Base de Datos de la aplicación en el servidor.", seeker, seekerProxy);
                    }
                }
            } else {
                if (dbController != null && dbController.isOpen()) {
                    boolean decline = this.dbController.declineSeekerCollabSession(sessionName, seeker.getUser());
                    if (decline) {
                        seekerProxy.notify_async(new NotifyAMICallback(seeker, "declineSeekerCollabSession"), res.toArray());
                    }
                }
            }
        } catch (IOException ex) {
            OutputMonitor.printStream("",ex);
            this.notifyCommitTransaction(ERROR_MESSAGE,
                    "No pudo completar la acción de dar baja de la sesión.", seeker, seekerProxy);
        } catch (SQLException ex) {
            OutputMonitor.printStream("",ex);
            this.notifyCommitTransaction(ERROR_MESSAGE,
                    "No pudo completar la acción de dar baja de la sesión, " + "\n" + "problemas con la conexión de la BD de la aplicación.", seeker, seekerProxy);
        }
    }

    /**
     *
     * @return
     */
    public Map<String, SessionProfile> getCollaborativeSessions() {
        return collaborativeSessions;
    }

    /**
     *
     * @return
     */
    public SessionProfile getDefaultSessionProfile() {
        return defaultSessionProfile;
    }

    private List<Seeker> getSeekersList(SeekerInfo record) {
        @SuppressWarnings("unchecked")
        List<Seeker> list = new ArrayList<>(record.record.keySet());
        return list;
    }

    @SuppressWarnings("unchecked")
    private List<ClientSidePrx> getClientSidePrxList(SeekerInfo record) {
        List<ClientSidePrx> list = new ArrayList<>();
        list.addAll(record.record.values());
        return (List<ClientSidePrx>) ((ArrayList) list).clone();
    }

    @SuppressWarnings("unchecked")
    private List<ClientSidePrx> getClientSidePrxList(SeekerInfo record, List<Seeker> seekers) {
        List<ClientSidePrx> list = new ArrayList<>();
        for (Seeker seeker : seekers) {
            list.add(record.record.get(seeker));
        }
        return (List<ClientSidePrx>) ((ArrayList) list).clone();
    }

    /**
     *
     * @return
     */
    public String getCommunicationSessionName() {
        return defaultSessionProfile.getProperties().getSessionName();
    }

    private boolean existsSeeker(Seeker seeker, SeekerInfo seekersRecord) {
        boolean flag = false;
        try {
            if (seekersRecord.record.containsKey(seeker)) {
                flag = true;
            }
        } catch (NullPointerException e) {
        }
        return flag;
    }

    private boolean existCollabSession(String sessionName) {
        boolean flag = false;
        try {
            if (collaborativeSessions.containsKey(sessionName)) {
                flag = true;
            }
        } catch (NullPointerException e) {
           OutputMonitor.printStream("", e);
        }

        return flag;

    }

    /**
     *
     * @param sessionName
     * @return
     * @throws RequestException
     */
    public String getChairmanName(String sessionName) throws RequestException {

        if (existCollabSession(sessionName)) {
            return collaborativeSessions.get(sessionName).getChairman().getName();
        } else {
            throw new RequestException("The session '" + sessionName + "' doesn't exist.");
        }
    }

    /**
     *
     * @return
     */
    public DataBaseController getDbController() {
        return dbController;
    }

    /**
     *
     * @param dbController
     */
    public void setDbController(DataBaseController dbController) {
        this.dbController = dbController;
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
    public RetrievalManager getRetrievalManager() {
        return retrievalManager;
    }

    /**
     * 
     * @param retrievalManager
     */
    public void setRetrievalManager(RetrievalManager retrievalManager) {
        this.retrievalManager = retrievalManager;
    }

    /**
     *
     * @return
     */
    public SternAppSetting getServerAppSetting() {
        return setting;
    }

    /**
     *
     * @param setting
     */
    public void setServerAppSetting(SternAppSetting setting) {
        this.setting = setting;
    }
}
