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
import drakkar.oar.ScorePQT;
import drakkar.oar.Seeker;
import drakkar.oar.exception.AwarenessException;
import drakkar.oar.exception.SeekerException;
import drakkar.oar.exception.SessionException;
import drakkar.oar.slice.client.ClientSidePrx;
import drakkar.oar.util.KeyAwareness;
import static drakkar.oar.util.KeyTransaction.*;
import static drakkar.oar.util.NotifyAction.*;
import drakkar.stern.callback.NotifyAMICallback;
import drakkar.stern.controller.DataBaseController;
import drakkar.stern.facade.event.FacadeListener;
import drakkar.stern.tracker.cache.SeekerInfo;
import drakkar.stern.tracker.cache.SessionProfile;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SynchronousAwareness extends Service implements Perceptionable {

     /**
     * Constructor de la clase
     *
     * @param defaultSessionName
     * @param collaborativeSessions listado de sesiones
     * @param defaultSessionProfile
     */
    public SynchronousAwareness(String defaultSessionName, SessionProfile defaultSessionProfile, Map<String, SessionProfile> collaborativeSessions, Map<String, SessionProfile> htTempSessions) {
        super(defaultSessionName, defaultSessionProfile, collaborativeSessions, htTempSessions);
    }

    /**
     * Constructor de la clase
     *
     * @param defaultSessionName
     * @param defaultSessionProfile
     * @param collaborativeSessions listado de sesiones
     * @param dbController
     */
    public SynchronousAwareness(String defaultSessionName, SessionProfile defaultSessionProfile, Map<String, SessionProfile> collaborativeSessions, Map<String, SessionProfile> htTempSessions, DataBaseController dbController) {
        super(defaultSessionName, defaultSessionProfile, collaborativeSessions, htTempSessions, dbController);
    }

    /**
     * Constructor de la clase
     *
     * @param defaultSessionName
     * @param collaborativeSessions listado de sesiones
     * @param defaultSessionProfile
     * @param listener             oyente de la aplicación servidora
     * @param dbController
     */
    public SynchronousAwareness(String defaultSessionName, SessionProfile defaultSessionProfile, Map<String, SessionProfile> collaborativeSessions, Map<String, SessionProfile> htTempSessions, FacadeListener listener, DataBaseController dbController) {
        super(defaultSessionName, defaultSessionProfile, collaborativeSessions, htTempSessions, listener, dbController);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public void sendPuttingQueryTermsTogetherAction(String sessionName, int event, Seeker emitter) throws SessionException, SeekerException, AwarenessException {
        if (emitter.getRole() == Seeker.ROLE_CHAIRMAN) {
            if (event == KeyAwareness.ENABLE_PQT || event == KeyAwareness.DISABLE_PQT) {
                try {
                    Map<Seeker, ClientSidePrx> record = validate(sessionName, emitter);
                    ArrayList<Seeker> seekers = new ArrayList<>(record.keySet());

                    Map<Object, Object> hash = new HashMap<>(5);
                    hash.put(OPERATION, NOTIFY_PUTTING_QUERY_TERMS_TOGETHER);
                    hash.put(SESSION_NAME, sessionName);
                    hash.put(DISTRIBUTED_EVENT, event);


                    Response response = new Response(hash);

                    // se notifica al chairman de la sesión

                    if (event == KeyAwareness.ENABLE_PQT) {
                        seekers.remove(emitter);
                        ArrayList<Seeker> seekersSession = seekers;
                        response.put(SEEKERS_SESSION, seekersSession);
                        response.put(IS_CHAIRMAN, true);

                        ClientSidePrx receptorPrx = record.get(emitter);
                        receptorPrx.notify_async(new NotifyAMICallback(emitter, "sendPuttingQueryTermsTogetherAction"), response.toArray());

                        // se notifica al resto de los miembros de la sesión
                        response.put(IS_CHAIRMAN, false);
                        for (Seeker seeker : seekers) {
                            receptorPrx = record.get(seeker);

                            seekersSession = new ArrayList<>(seekers);
                            seekersSession.remove(seeker);
                            seekersSession.add(0, emitter);
                            response.put(SEEKERS_SESSION, seekersSession);

                            receptorPrx.notify_async(new NotifyAMICallback(seeker, "sendPuttingQueryTermsTogetherAction"), response.toArray());
                        }

                    } else {
                        ClientSidePrx receptorPrx;
                        for (Seeker seeker : seekers) {
                            receptorPrx = record.get(seeker);
                            receptorPrx.notify_async(new NotifyAMICallback(seeker, "sendPuttingQueryTermsTogetherAction"), response.toArray());
                        }
                    }

                } catch (IOException ex) {
                    throw new AwarenessException(ex.getMessage());
                }

            } else {
                throw new AwarenessException("Event of Putting Query-Terms Together (PQT) not supported.");
            }

        } else {
            throw new SeekerException("Doesn't have privileges to executing this search.");

        }

    }

    /**
     * {@inheritDoc}
     */
    public void sendQueryChangeAction(String sessionName, String query, Map<String, ScorePQT> statistics, Seeker emitter) throws SessionException, SeekerException, AwarenessException {
        Map<Seeker, ClientSidePrx> record = validate(sessionName, emitter);

        Map<Object, Object> hash = new HashMap<>(5);
        hash.put(OPERATION, NOTIFY_QUERY_CHANGE);
        hash.put(SESSION_NAME, sessionName);
        hash.put(SEEKER_NICKNAME, emitter.getUser());
        hash.put(QUERY, query);
        hash.put(SCORE_PQT, statistics);
        Response response = new Response(hash);

        notify("sendQueryChangeAction", emitter, record, response);

    }

    /**
     * {@inheritDoc}
     */
    public void sendQueryTypedAction(String sessionName, boolean typed, Seeker emitter) throws SessionException, SeekerException, AwarenessException {
        Map<Seeker, ClientSidePrx> record = validate(sessionName, emitter);

        Map<Object, Object> hash = new HashMap<>(5);
        hash.put(OPERATION, NOTIFY_QUERY_TYPED);
        hash.put(SESSION_NAME, sessionName);
        hash.put(SEEKER_NICKNAME, emitter.getUser());
        hash.put(QUERY_TYPED, typed);
        Response response = new Response(hash);

        notify("sendQueryTypedAction", emitter, record, response);


    }

    /**
     * {@inheritDoc}
     */
    public void sendTermAcceptanceAction(String sessionName, String term, int event, String user, Seeker emitter) throws SessionException, SeekerException, AwarenessException {
        Map<Seeker, ClientSidePrx> record = validate(sessionName, emitter);

        Map<Object, Object> hash = new HashMap<>(5);
        hash.put(OPERATION, NOTIFY_QUERY_TERM_ACCEPTANCE);
        hash.put(SESSION_NAME, sessionName);
        hash.put(SEEKER_NICKNAME, user);
        hash.put(QUERY_TERM, term);
        hash.put(DISTRIBUTED_EVENT, event);
        Response response = new Response(hash);

        notify("sendTermAcceptanceAction", emitter, record, response);

    }

    private Map<Seeker, ClientSidePrx> validate(String sessionName, Seeker emitter) throws SessionException, SeekerException {
        if (this.existCollabSession(sessionName)) {
            SessionProfile recordSession = collaborativeSessions.get(sessionName);
            SeekerInfo seekersRecord = recordSession.getSeekerInfo();
            boolean flag = seekersRecord.record.containsKey(emitter);

            if (flag) {
                return new HashMap<> (seekersRecord.record);
            } else {
                throw new SeekerException("The seeker '" + emitter.getUser() + "' is not registered in the session '" + sessionName + "'.");
            }
        } else {
            throw new SessionException("The session '" + sessionName + "' doesn't exist.");
        }
    }

    private void notify(String action, Seeker emitter, Map<Seeker, ClientSidePrx> record, Response response) throws AwarenessException {
        @SuppressWarnings("unchecked")
        ArrayList<Seeker> receptors = new ArrayList<>(record.keySet());
        receptors.remove(emitter);
        byte[] array;
        try {
            array = response.toArray(); // se serializa el objeto response
            ClientSidePrx receptorPrx;
            for (Seeker item : receptors) {
                receptorPrx = record.get(item);
                receptorPrx.notify_async(new NotifyAMICallback(item, action), array);
            }
        } catch (IOException ex) {
            throw new AwarenessException(ex.getMessage());
        }
    }
}
