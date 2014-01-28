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
import drakkar.oar.exception.AwarenessException;
import drakkar.oar.exception.SeekerException;
import drakkar.oar.exception.SessionException;
import drakkar.oar.slice.client.ClientSidePrx;
import drakkar.oar.util.KeySuggest;
import static drakkar.oar.util.KeyTransaction.*;
import static drakkar.oar.util.NotifyAction.*;
import drakkar.oar.util.OutputMonitor;
import drakkar.stern.callback.NotifyAMICallback;
import drakkar.stern.controller.DataBaseController;
import drakkar.stern.facade.event.FacadeListener;
import drakkar.stern.tracker.cache.SeekerInfo;
import drakkar.stern.tracker.cache.SessionProfile;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ImplicitRecomendation extends Service implements Suggestable {

    /**
     *
     * @param defaultSessionName
     * @param defaultSessionProfile
     * @param collaborativeSessions
     * @param htTempSessions
     * @param listener
     * @param dbController
     */
    public ImplicitRecomendation(String defaultSessionName, SessionProfile defaultSessionProfile, Map<String, SessionProfile> collaborativeSessions, Map<String, SessionProfile> htTempSessions, FacadeListener listener, DataBaseController dbController) {
        super(defaultSessionName, defaultSessionProfile, collaborativeSessions, htTempSessions, listener, dbController);
    }

    /**
     *
     * @param defaultSessionName
     * @param defaultSessionProfile
     * @param collaborativeSessions
     * @param htTempSessions
     * @param dbController
     */
    public ImplicitRecomendation(String defaultSessionName, SessionProfile defaultSessionProfile, Map<String, SessionProfile> collaborativeSessions, Map<String, SessionProfile> htTempSessions, DataBaseController dbController) {
        super(defaultSessionName, defaultSessionProfile, collaborativeSessions, htTempSessions, dbController);
    }

    /**
     *
     * @param defaultSessionName
     * @param defaultSessionProfile
     * @param collaborativeSessions
     * @param htTempSessions
     */
    public ImplicitRecomendation(String defaultSessionName, SessionProfile defaultSessionProfile, Map<String, SessionProfile> collaborativeSessions, Map<String, SessionProfile> htTempSessions) {
        super(defaultSessionName, defaultSessionProfile, collaborativeSessions, htTempSessions);
    }

    public void sendCollabTermsSuggestAction(String sessionName, int event, Seeker emitter, ClientSidePrx prx) throws SessionException, SeekerException, AwarenessException {
        if (sessionName.equals(defaultSessionName)) {
            enabledCollabTermsSuggest(sessionName, event, emitter, prx);
        } else if (emitter.getRole() == Seeker.ROLE_CHAIRMAN) {
            enabledCollabTermsSuggest(sessionName, event, emitter, prx);
        } else {
            throw new SeekerException("Doesn't have privileges to executing this technique.");
        }
    }

    private void enabledCollabTermsSuggest(String sessionName, int event, Seeker emitter, ClientSidePrx prx) throws SessionException, SeekerException, AwarenessException {
        boolean enable = (event == KeySuggest.ENABLE_TERMS_SUGGEST);
        if (enable || event == KeySuggest.DISABLE_TERMS_SUGGEST) {

            SessionProfile sessionProfile = getSessionProfile(sessionName);

            if (existSeeker(sessionProfile, emitter)) {

                sessionProfile.setTermsSuggest(enable);
                Map<Object, Object> hash = new HashMap<>(5);
                hash.put(OPERATION, NOTIFY_COLLAB_TERMS_SUGGEST);
                hash.put(SESSION_NAME, sessionName);
                hash.put(DISTRIBUTED_EVENT, event);

                Response response = new Response(hash);
                try {
                    prx.notify_async(new NotifyAMICallback(emitter, "sendCollabTermsSuggestAction"), response.toArray());
                } catch (IOException ex) {
                   OutputMonitor.printStream("",ex);
                }
            }

        } else {
            throw new AwarenessException("Event of Collaborative Terms Suggest not supported.");
        }
    }

    private boolean existSeeker(SessionProfile session, Seeker emitter) throws SeekerException {

        SeekerInfo seekersRecord = session.getSeekerInfo();
        boolean flag = seekersRecord.record.containsKey(emitter);

        if (flag) {
            return flag;
        } else {
            throw new SeekerException("The seeker '" + emitter.getUser() + "' is not registered in the session '" + session.getProperties().getSessionName() + "'.");
        }

    }
}
