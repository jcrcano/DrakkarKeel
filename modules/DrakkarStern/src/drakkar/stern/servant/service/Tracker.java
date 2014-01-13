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

import drakkar.oar.RecommendTracker;
import drakkar.oar.SearchTracker;
import drakkar.oar.Seeker;
import drakkar.oar.SeekerQuery;
import drakkar.oar.exception.QueryNotExistException;
import drakkar.oar.exception.SeekerException;
import drakkar.oar.exception.TrackException;
import drakkar.oar.slice.client.ClientSidePrx;
import static drakkar.oar.util.KeyMessage.*;
import static drakkar.oar.util.KeySession.*;
import drakkar.oar.util.OutputMonitor;
import drakkar.stern.ResponseTrackerFactory;
import drakkar.stern.ResponseUtilFactory;
import drakkar.stern.callback.NotifyAMICallback;
import drakkar.stern.controller.DataBaseController;
import drakkar.stern.facade.event.FacadeListener;
import drakkar.stern.tracker.cache.SeekerRecommends;
import drakkar.stern.tracker.cache.SeekerSearchResults;
import drakkar.stern.tracker.cache.SessionProfile;
import drakkar.stern.tracker.persistent.DBUtil;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class Tracker extends Service implements Trackable {

    private long trackerCount = 0;

    /**
     * Constructor de la clase
     *
     * @param defaultSessionName
     * @param collaborativeSessions listado de sesiones
     * @param defaultSessionProfile
     * @param htTempSessions  
     */
    public Tracker(String defaultSessionName, SessionProfile defaultSessionProfile, Map<String, SessionProfile> collaborativeSessions, Map<String, SessionProfile> htTempSessions) {
        super(defaultSessionName, defaultSessionProfile, collaborativeSessions, htTempSessions);
    }

    /**
     * Constructor de la clase
     *
     * @param defaultSessionName
     * @param defaultSessionProfile
     * @param collaborativeSessions listado de sesiones
     * @param htTempSessions 
     * @param dbController
     */
    public Tracker(String defaultSessionName, SessionProfile defaultSessionProfile, Map<String, SessionProfile> collaborativeSessions, Map<String, SessionProfile> htTempSessions, DataBaseController dbController) {
        super(defaultSessionName, defaultSessionProfile, collaborativeSessions, htTempSessions, dbController);
    }

    /**
     * Constructor de la clase
     *
     * @param defaultSessionName
     * @param collaborativeSessions listado de sesiones
     * @param defaultSessionProfile
     * @param htTempSessions 
     * @param listener oyente de la aplicación servidora
     * @param dbController
     */
    public Tracker(String defaultSessionName, SessionProfile defaultSessionProfile, Map<String, SessionProfile> collaborativeSessions, Map<String, SessionProfile> htTempSessions, FacadeListener listener, DataBaseController dbController) {
        super(defaultSessionName, defaultSessionProfile, collaborativeSessions, htTempSessions, listener, dbController);
    }

    /**
     * {@inheritDoc}
     */
    public void trackRecommendation(String sessionName, Seeker emitter, ClientSidePrx emitterPrx) throws TrackException {
        OutputMonitor.printLine("Tracker[ Method: trackRecommendation SessionName: " + sessionName + "]");
        List<RecommendTracker> recommend = null;

        // obtener de la BD
        if (dbController != null && dbController.isOpen()) {
            try {
                if (dbController.existSession(sessionName)) {
                    recommend = dbController.getTrackRecommendation(sessionName);
                } else {
                    throw new TrackException("La sesión " + sessionName + "no existe.");
                }

            } catch (SQLException ex) {
                throw new TrackException(ex.getMessage());
            }
        } else {
            throw new TrackException("Conexión DB cerrada.");
        }

        this.notifyRecommendation(sessionName, recommend, emitter, emitterPrx);
    }

    /**
     * {@inheritDoc}
     *
     */
    @Override
    public void trackRecommendation(String sessionName, Seeker seeker, Seeker emitter, ClientSidePrx emitterPrx) throws TrackException {
        OutputMonitor.printLine("Tracker[ method: trackRecommendation SessionName: " + sessionName + " Seeker: " + seeker.getUser() + "]");
        List<RecommendTracker> recommend = null;

        // obtener de la BD
        if (dbController != null && dbController.isOpen()) {
            try {
                if (dbController.existSession(sessionName)) {
                    recommend = dbController.getTrackRecommendation(sessionName, seeker);
                } else {
                    throw new TrackException("La sesión " + sessionName + "no existe.");
                }

            } catch (SQLException ex) {
                throw new TrackException(ex.getMessage());
            }
        } else {
            throw new TrackException("Conexión DB cerrada.");
        }

        this.notifyRecommendation(sessionName, recommend, emitter, emitterPrx);

    }

    /**
     * {@inheritDoc}
     *
     */
    @Override
    public void trackRecommendation(String sessionName, Date date, Seeker emitter, ClientSidePrx emitterPrx) throws TrackException {
        OutputMonitor.printLine("Tracker[ method: trackRecommendation SessionName: " + sessionName + " Date: " + date.toString() + "]");
        List<RecommendTracker> recommend = null;
        try {
            if (date.toString().equals(DBUtil.getCurrentDate().toString())) { //cache

                //cache
                SessionProfile profile = getTrackSessionProfile(sessionName);
                if (profile != null && !profile.getSeekerRecommends().values.isEmpty()) {
                    SeekerRecommends recommends = profile.getSeekerRecommends();
                    recommend = this.getRecommendData(recommends);
                } else if (dbController != null && dbController.isOpen()) {
                    if (dbController.existSession(sessionName)) {
                        recommend = dbController.getTrackRecommendation(sessionName, date);
                    } else {
                        throw new TrackException("La sesión " + sessionName + "no existe.");
                    }

                } else {
                    throw new TrackException("Conexión DB cerrada.");
                }


            } else {
                // obtener de la BD
                if (dbController != null && dbController.isOpen()) {
                    if (dbController.existSession(sessionName)) {
                        recommend = dbController.getTrackRecommendation(sessionName, date);
                    } else {
                        throw new TrackException("La sesión " + sessionName + "no existe.");
                    }
                } else {
                    throw new TrackException("Conexión DB cerrada.");
                }
            }

        } catch (SQLException ex) {
            throw new TrackException(ex.getMessage());
        }

        this.notifyRecommendation(sessionName, recommend, emitter, emitterPrx);
    }

    /**
     * Obtener todas las recomendaciones de toda la sesión
     */
    private List<RecommendTracker> getRecommendData(SeekerRecommends srec) {
        List<RecommendTracker> list = srec.getRecommendations();
        return list;
    }

    /**
     * Obtener todas las recomendaciones de toda la sesión
     */
    private List<RecommendTracker> getRecommendData(SeekerRecommends srec, String query) {
        List<RecommendTracker> list = srec.getRecommendations(query);
        return list;
    }

    /**
     * Obtener todas las recomendaciones de toda la sesión
     */
    private List<RecommendTracker> getRecommendData(SeekerRecommends srec, Seeker seeker) {
        List<RecommendTracker> list = srec.getRecommendations(seeker);
        return list;
    }

    /**
     * Obtener todas las recomendaciones de toda la sesión
     */
    private List<RecommendTracker> getRecommendData(SeekerRecommends srec, String query, Seeker seeker) {
        List<RecommendTracker> list = srec.getRecommendations(query, seeker);
        return list;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void trackRecommendation(String sessionName, String query, Seeker emitter, ClientSidePrx emitterPrx) throws TrackException {
        OutputMonitor.printLine("Tracker[ Method: trackRecommendation SessionName: " + sessionName + " Query: " + query + "]");
        List<RecommendTracker> recommend = null;

        // obtener de la BD
        if (dbController != null && dbController.isOpen()) {
            try {
                if (dbController.existSession(sessionName)) {
                    recommend = dbController.getTrackRecommendation(sessionName, query);
                } else {
                    throw new TrackException("La sesión " + sessionName + "no existe.");
                }
            } catch (SQLException ex) {
                throw new TrackException(ex.getMessage());
            }
        } else {
            throw new TrackException("Conexión DB cerrada.");
        }

        this.notifyRecommendation(sessionName, recommend, emitter, emitterPrx);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void trackRecommendation(String sessionName, Seeker seeker, String query, Seeker emitter, ClientSidePrx emitterPrx) throws TrackException {
        OutputMonitor.printLine("Tracker[ Method: trackRecommendation SessionName: " + sessionName + " Seeker: " + seeker.getUser() + " Query: " + query + "]");
        List<RecommendTracker> recommend = null;

        // obtener de la BD
        if (dbController != null && dbController.isOpen()) {
            try {
                if (dbController.existSession(sessionName)) {
                    recommend = dbController.getTrackRecommendation(sessionName, seeker, query);
                } else {
                    throw new TrackException("La sesión " + sessionName + "no existe.");
                }
            } catch (SQLException ex) {
                throw new TrackException(ex.getMessage());
            }
        } else {
            throw new TrackException("Conexión DB cerrada.");
        }
        this.notifyRecommendation(sessionName, recommend, emitter, emitterPrx);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void trackRecommendation(String sessionName, String query, Date date, Seeker emitter, ClientSidePrx emitterPrx) throws TrackException {
        OutputMonitor.printLine("Tracker[ Method: trackRecommendation SessionName: " + sessionName + " Date: " + date.toString() + " Query: " + query + "]");
        List<RecommendTracker> recommend = null;

        try {
            if (date.toString().equals(DBUtil.getCurrentDate().toString())) { //cache

                //cache
                SessionProfile profile = getTrackSessionProfile(sessionName);
                if (profile != null && !profile.getSeekerRecommends().values.isEmpty()) {
                    SeekerRecommends recommends = profile.getSeekerRecommends();
                    recommend = this.getRecommendData(recommends, query);
                } else if (dbController != null && dbController.isOpen()) {
                    if (dbController.existSession(sessionName)) {
                        recommend = dbController.getTrackRecommendation(sessionName, query, date);
                    } else {
                        throw new TrackException("La sesión " + sessionName + "no existe.");
                    }
                } else {
                    throw new TrackException("Conexión DB cerrada.");
                }

            } else {
                if (dbController != null && dbController.isOpen()) {
                    if (dbController.existSession(sessionName)) {
                        recommend = dbController.getTrackRecommendation(sessionName, query, date);
                    } else {
                        throw new TrackException("La sesión " + sessionName + "no existe.");
                    }
                } else {
                    throw new TrackException("Conexión DB cerrada.");
                }
            }

        } catch (SQLException ex) {
            throw new TrackException(ex.getMessage());
        }
        this.notifyRecommendation(sessionName, recommend, emitter, emitterPrx);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void trackRecommendation(String sessionName, Seeker seeker, Date date, Seeker emitter, ClientSidePrx emitterPrx) throws TrackException {
        OutputMonitor.printLine("Tracker[ Method: trackRecommendation SessionName: " + sessionName + " Date: " + date.toString() + " Seeker: " + seeker.getUser() + "]");
        List<RecommendTracker> recommend = null;

        try {
            if (date.toString().equals(DBUtil.getCurrentDate().toString())) { //cache
                //cache
                SessionProfile profile = getTrackSessionProfile(sessionName);
                if (profile != null && !profile.getSeekerRecommends().values.isEmpty()) {
                    SeekerRecommends recommends = profile.getSeekerRecommends();
                    recommend = this.getRecommendData(recommends, seeker);
                } else if (dbController != null && dbController.isOpen()) {
                    if (dbController.existSession(sessionName)) {
                        recommend = dbController.getTrackRecommendation(sessionName, seeker, date);
                    } else {
                        throw new TrackException("La sesión " + sessionName + "no existe.");
                    }
                } else {
                    throw new TrackException("Conexión DB cerrada.");
                }

            } else {
                if (dbController != null && dbController.isOpen()) {
                    if (dbController.existSession(sessionName)) {
                        recommend = dbController.getTrackRecommendation(sessionName, seeker, date);
                    } else {
                        throw new TrackException("La sesión " + sessionName + "no existe.");
                    }
                } else {
                    throw new TrackException("Conexión DB cerrada.");
                }
            }

        } catch (SQLException ex) {
            throw new TrackException(ex.getMessage());
        }


        this.notifyRecommendation(sessionName, recommend, emitter, emitterPrx);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void trackRecommendation(String sessionName, Seeker seeker, Date date, String query, Seeker emitter, ClientSidePrx emitterPrx) throws TrackException {
        OutputMonitor.printLine("Tracker[ Method: trackRecommendation SessionName: " + sessionName + " Date: " + date.toString() + " Query: " + query +" Seeker: "+seeker.getUser() +"]");
        List<RecommendTracker> recommend = null;

        try {
            if (date.toString().equals(DBUtil.getCurrentDate().toString())) { //cache
                //cache
                SessionProfile profile = getTrackSessionProfile(sessionName);
                if (profile != null && !profile.getSeekerRecommends().values.isEmpty()) {
                    SeekerRecommends recommends = profile.getSeekerRecommends();
                    recommend = this.getRecommendData(recommends, query, seeker);
                } else if (dbController != null && dbController.isOpen()) {
                    if (dbController.existSession(sessionName)) {
                        recommend = dbController.getTrackRecommendation(sessionName, seeker, query, date);
                    } else {
                        throw new TrackException("La sesión " + sessionName + "no existe.");
                    }
                } else {
                    throw new TrackException("Conexión DB cerrada.");
                }

            } else {
                if (dbController != null && dbController.isOpen()) {
                    if (dbController.existSession(sessionName)) {
                        recommend = dbController.getTrackRecommendation(sessionName, seeker, query, date);
                    } else {
                        throw new TrackException("La sesión " + sessionName + "no existe.");
                    }
                } else {
                    throw new TrackException("Conexión DB cerrada.");
                }
            }

        } catch (SQLException ex) {
            throw new TrackException(ex.getMessage());
        }


        this.notifyRecommendation(sessionName, recommend, emitter, emitterPrx);
    }
//////////////////////////////////////////////////////////

    /**
     * {@inheritDoc}
     */
    @Override
    public void trackSearch(String sessionName, int group, Seeker emitter, ClientSidePrx emitterPrx) throws TrackException {

        OutputMonitor.printLine("Tracker[ Method: trackSearch SessionName: " + sessionName + " Group: " + group + "]");
        List<SearchTracker> search = null;
        if (dbController != null && dbController.isOpen()) {
            try {
                if (dbController.existSession(sessionName)) {
                    search = dbController.getTrackSearch(sessionName, group);
                } else {
                    throw new TrackException("La sesión " + sessionName + "no existe.");
                }
            } catch (SQLException ex) {
                throw new TrackException(ex.getMessage());
            }
        } else {
            throw new TrackException("Conexión DB cerrada.");
        }
        this.notifySearch(sessionName, search, emitter, emitterPrx);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void trackSearch(String sessionName, Seeker seeker, int group, Seeker emitter, ClientSidePrx emitterPrx) throws TrackException {
        List<SearchTracker> search = null;
        OutputMonitor.printLine("Tracker[ Method: trackSearch Seeker: " + seeker.getUser() + " SessionName: " + sessionName + " Group: " + group + "]");
        // obtener de la BD
        if (dbController != null && dbController.isOpen()) {
            try {
                if (dbController.existSession(sessionName)) {
                    search = dbController.getTrackSearch(sessionName, seeker, group);
                } else {
                    throw new TrackException("La sesión " + sessionName + "no existe.");
                }
            } catch (SQLException ex) {
                throw new TrackException(ex.getMessage());
            }
        } else {
            throw new TrackException("Conexión DB cerrada.");
        }
        this.notifySearch(sessionName, search, emitter, emitterPrx);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void trackSearch(String sessionName, Date date, int group, Seeker emitter, ClientSidePrx emitterPrx) throws TrackException {
        List<SearchTracker> search = null;
        OutputMonitor.printLine("Tracker[ Method: trackSearch Date:" + date.toString() + " SessionName: " + sessionName + " Group: " + group +" Date: "+date.toString() +"]");
        try {
            if (date.toString().equals(DBUtil.getCurrentDate().toString())) { //cache
                //cache
                SessionProfile profile = getTrackSessionProfile(sessionName);
                if (profile != null && !profile.getSeekerSearchResults().record.isEmpty()) {
                    SeekerSearchResults searchResults = profile.getSeekerSearchResults();
                    search = searchResults.getSearchResults();
                } else if (dbController != null && dbController.isOpen()) {
                    if (dbController.existSession(sessionName)) {
                        search = dbController.getTrackSearch(sessionName, date, group);
                    } else {
                        throw new TrackException("La sesión " + sessionName + "no existe.");
                    }
                } else {
                    throw new TrackException("Conexión DB cerrada.");
                }

            } else {
                if (dbController != null && dbController.isOpen()) {
                    if (dbController.existSession(sessionName)) {
                        search = dbController.getTrackSearch(sessionName, date, group);
                    } else {
                        throw new TrackException("La sesión " + sessionName + "no existe.");
                    }
                } else {
                    throw new TrackException("Conexión DB cerrada.");
                }
            }

        } catch (SQLException ex) {
            throw new TrackException(ex.getMessage());
        }

        this.notifySearch(sessionName, search, emitter, emitterPrx);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void trackSearch(String sessionName, String query, int group, Seeker emitter, ClientSidePrx emitterPrx) throws TrackException {
        OutputMonitor.printLine("Tracker[ Method: trackSearch Query: " + query + " SessionName: " + sessionName + " Group: " + group + "]");
        List<SearchTracker> search = null;
        // obtener de la BD
        if (dbController != null && dbController.isOpen()) {
            try {
                if (dbController.existSession(sessionName)) {
                    search = dbController.getTrackSearch(sessionName, query, group);
                } else {
                    throw new TrackException("La sesión " + sessionName + "no existe.");
                }
            } catch (SQLException ex) {
                throw new TrackException(ex.getMessage());
            }
        } else {
            throw new TrackException("Conexión DB cerrada.");
        }
        this.notifySearch(sessionName, search, emitter, emitterPrx);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void trackSearch(String sessionName, Seeker seeker, String query, int group, Seeker emitter, ClientSidePrx emitterPrx) throws TrackException {
        OutputMonitor.printLine("Tracker[ Method: trackSearch Seeker: " + seeker.getUser() + " Query: " + query + " SessionName: " + sessionName + " Group: " + group + "]");
        List<SearchTracker> search = null;
        // obtener de la BD
        if (dbController != null && dbController.isOpen()) {
            try {
                if (dbController.existSession(sessionName)) {
                    search = dbController.getTrackSearch(sessionName, seeker, query, group);
                } else {
                    throw new TrackException("La sesión " + sessionName + "no existe.");
                }
            } catch (SQLException ex) {
                throw new TrackException(ex.getMessage());
            }
        } else {
            throw new TrackException("Conexión DB cerrada.");
        }
        this.notifySearch(sessionName, search, emitter, emitterPrx);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void trackSearch(String sessionName, String query, Date date, int group, Seeker emitter, ClientSidePrx emitterPrx) throws TrackException {
        OutputMonitor.printLine("Tracker[ Method: trackSearch Date: " + date.toString() + "Query: " + query + " SessionName: " + sessionName + " Group: " + group + "]");
        List<SearchTracker> search = null;
        try {
            if (date.toString().equals(DBUtil.getCurrentDate().toString())) { //cache
                SessionProfile profile = getTrackSessionProfile(sessionName);
                if (profile != null && !profile.getSeekerSearchResults().record.isEmpty()) {
                    SeekerSearchResults searchResults = profile.getSeekerSearchResults();
                    try {
                        search = searchResults.getSearchResults(query, group);
                    } catch (QueryNotExistException ex) {
                        throw new TrackException(ex.getMessage());
                    }
                } else if (dbController != null && dbController.isOpen()) {
                    if (dbController.existSession(sessionName)) {
                        search = dbController.getTrackSearch(sessionName, query, date, group);
                    } else {
                        throw new TrackException("La sesión " + sessionName + "no existe.");
                    }
                } else {
                    throw new TrackException("Conexión DB cerrada.");
                }

            } else {
                if (dbController != null && dbController.isOpen()) {
                    if (dbController.existSession(sessionName)) {
                        search = dbController.getTrackSearch(sessionName, query, date, group);
                    } else {
                        throw new TrackException("La sesión " + sessionName + "no existe.");
                    }
                } else {
                    throw new TrackException("Conexión DB cerrada.");
                }
            }

        } catch (SQLException ex) {
            throw new TrackException(ex.getMessage());
        }



        this.notifySearch(sessionName, search, emitter, emitterPrx);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void trackSearch(String sessionName, Seeker seeker, Date date, int group, Seeker emitter, ClientSidePrx emitterPrx) throws TrackException {
        OutputMonitor.printLine("Tracker[ Method: trackSearch Date: " + date.toString() + " Seeker: " + seeker.getUser() + " SessionName: " + sessionName + " Group: " + group + "]");
        List<SearchTracker> search = null;

        try {
            if (date.toString().equals(DBUtil.getCurrentDate().toString())) { //cache
                //cache
                SessionProfile profile = getTrackSessionProfile(sessionName);
                if (profile != null && !profile.getSeekerSearchResults().record.isEmpty()) {
                    SeekerSearchResults searchResults = profile.getSeekerSearchResults();
                    try {
                        search = searchResults.getSearchResults(seeker, group);
                    } catch (SeekerException ex) {
                        throw new TrackException(ex.getMessage());
                    }
                } else if (dbController != null && dbController.isOpen()) {
                    if (dbController.existSession(sessionName)) {
                        search = dbController.getTrackSearch(sessionName, seeker, date, group);
                    } else {
                        throw new TrackException("La sesión " + sessionName + "no existe.");
                    }
                } else {
                    throw new TrackException("Conexión DB cerrada.");
                }

            } else {
                if (dbController != null && dbController.isOpen()) {
                    if (dbController.existSession(sessionName)) {
                        search = dbController.getTrackSearch(sessionName, seeker, date, group);
                    } else {
                        throw new TrackException("La sesión " + sessionName + "no existe.");
                    }
                } else {
                    throw new TrackException("Conexión DB cerrada.");
                }
            }

        } catch (SQLException ex) {
            throw new TrackException(ex.getMessage());
        }


        this.notifySearch(sessionName, search, emitter, emitterPrx);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void trackSearch(String sessionName, Seeker seeker, Date date, String query, int group, Seeker emitter, ClientSidePrx emitterPrx) throws TrackException {

        OutputMonitor.printLine("Tracker[ Method: trackSearch Date: " + date.toString() + " Query: " + query + " Seeker: " + seeker.getUser() + " Group: " + group + "]");
        List<SearchTracker> search = null;

        try {
            if (date.toString().equals(DBUtil.getCurrentDate().toString())) { //cache
                //cache
                SessionProfile profile = getTrackSessionProfile(sessionName);
                if (profile != null && !profile.getSeekerSearchResults().record.isEmpty()) {
                    SeekerSearchResults searchResults = profile.getSeekerSearchResults();
                    try {
                        search = searchResults.getSearchResults(query, seeker, group);

                    } catch (            SeekerException | QueryNotExistException ex) {
                        throw new TrackException(ex.getMessage());
                    }
                } else if (dbController != null && dbController.isOpen()) {
                    if (dbController.existSession(sessionName)) {
                        search = dbController.getTrackSearch(sessionName, seeker, date, query, group);
                    } else {
                        throw new TrackException("La sesión " + sessionName + "no existe.");
                    }
                } else {
                    throw new TrackException("Conexión DB cerrada.");
                }

            } else {
                if (dbController != null && dbController.isOpen()) {
                    if (dbController.existSession(sessionName)) {
                        search = dbController.getTrackSearch(sessionName, seeker, date, query, group);
                    } else {
                        throw new TrackException("La sesión " + sessionName + "no existe.");
                    }
                } else {
                    throw new TrackException("Conexión DB cerrada.");
                }
            }
        } catch (SQLException ex) {
            throw new TrackException(ex.getMessage());
        }

        this.notifySearch(sessionName, search, emitter, emitterPrx);
    }

    /**
     * {@inheritDoc}
     *
     * @param emitterPrx 
     */
    @Override
    public void trackSession(String sessionName, Date date, Seeker emitter, ClientSidePrx emitterPrx) throws TrackException {
        OutputMonitor.printLine("Tracker[ method: trackSession Date: " + date.toString() + "Session: " + sessionName + "]");
        SeekerQuery result = new SeekerQuery();


        try {
            if (date.toString().equals(DBUtil.getCurrentDate().toString())) { //cache
                SessionProfile profile = getTrackSessionProfile(sessionName);
                if (profile != null && !profile.getSeekerSearchResults().record.isEmpty()) {
                    SeekerSearchResults searchResults = profile.getSeekerSearchResults();
                    result = searchResults.getSeekerQuery();
                } else if (dbController != null && dbController.isOpen() && dbController.existSession(sessionName)) {
                    result = this.dbController.getSeekerQueries(sessionName, date);
                } else {
                    throw new TrackException("La sesión " + sessionName + "no existe.");
                }
            } else {
                if (dbController != null && dbController.isOpen()) {
                    if (dbController.existSession(sessionName)) {
                        result = this.dbController.getSeekerQueries(sessionName, date);
                    } else {
                        throw new TrackException("La sesión " + sessionName + "no existe.");
                    }
                } else {
                    throw new TrackException("Conexión DB cerrada.");
                }
            }

        } catch (SQLException ex) {
            throw new TrackException(ex.getMessage());
        }

        this.notifySession(sessionName, result, emitter, emitterPrx);
    }

    /**
     *
     * @return
     */
    public String getDefaultSession() {
        return defaultSessionName;
    }

    /**
     *
     * @param defaultSession
     */
    public void setDefaultSession(String defaultSession) {
        this.defaultSessionName = defaultSession;
    }

    /**
     *
     * @return
     */
    public long getTrackerCount() {
        return trackerCount;
    }

    /**
     *
     * @param trackerCount
     */
    public void setTrackerCount(long trackerCount) {
        this.trackerCount = trackerCount;
    }

    /**
     *
     * @param sessionName 
     * @param search
     * @param emitter
     * @param emitterPrx
     * @throws TrackException
     */
    public void notifySearch(String sessionName, List<SearchTracker> search, Seeker emitter, ClientSidePrx emitterPrx) throws TrackException {
        if (search != null) {
            try {
                byte[] array = ResponseTrackerFactory.getResponse(sessionName, COLLAB_SEARCH_TRACK, search).toArray();
                OutputMonitor.printLine("Tracker: notifySearch   Total of Documents: " + search.size());
                emitterPrx.notify_async(new NotifyAMICallback(emitter, "notifySearch"), array);
            } catch (IOException ex) {
                throw new TrackException(ex.getMessage());
            }
        } else {
            try {
                byte[] array = ResponseUtilFactory.getResponse(INFORMATION_MESSAGE, "No existen búsquedas para la sesión.").toArray();
              
                emitterPrx.notify_async(new NotifyAMICallback(emitter, "notifySearch"), array);
            } catch (IOException ex) {
                throw new TrackException(ex.getMessage());
            }

        }


    }

    /**
     *
     * @param sessionName 
     * @param seekerQuery
     * @param emitter
     * @param emitterPrx
     * @throws TrackException
     */
    public void notifySession(String sessionName, SeekerQuery seekerQuery, Seeker emitter, ClientSidePrx emitterPrx) throws TrackException {
        if (seekerQuery != null) {
            try {
                byte[] array = ResponseTrackerFactory.getResponse(sessionName, seekerQuery).toArray();
                 OutputMonitor.printLine("Tracker: notifySession   Total of Queries: " + seekerQuery.getQueries().size());
                emitterPrx.notify_async(new NotifyAMICallback(emitter, "notifySession"), array);
            } catch (IOException ex) {
                throw new TrackException(ex.getMessage());
            }
        } else {
            try {
                byte[] array = ResponseUtilFactory.getResponse(INFORMATION_MESSAGE, "No existen búsquedas para la sesión.").toArray();
                emitterPrx.notify_async(new NotifyAMICallback(emitter, "notifySession"), array);
            } catch (IOException ex) {
                throw new TrackException(ex.getMessage());
            }
        }
    }

    /**
     *
     * @param sessionName 
     * @param recommend
     * @param emitter
     * @param emitterPrx
     * @throws TrackException
     */
    public void notifyRecommendation(String sessionName, List<RecommendTracker> recommend, Seeker emitter, ClientSidePrx emitterPrx) throws TrackException {
        if (recommend != null) {
            try {
                byte[] array = ResponseTrackerFactory.getResponse(sessionName, COLLAB_RECOMMENDATION_TRACK, recommend).toArray();
                   OutputMonitor.printLine("Tracker: notifyRecommendation   Total of recommendations: " + recommend.size());
                emitterPrx.notify_async(new NotifyAMICallback(emitter, "notifyRecommendation"), array);
            } catch (IOException ex) {
                throw new TrackException(ex.getMessage());
            }
        } else {
            try {
                byte[] array = ResponseUtilFactory.getResponse(INFORMATION_MESSAGE, "No existen recomendaciones para la sesión.").toArray();
                emitterPrx.notify_async(new NotifyAMICallback(emitter, "notifyRecommendation"), array);
            } catch (IOException ex) {
                throw new TrackException(ex.getMessage());
            }
        }
    }

    private SessionProfile getTrackSessionProfile(String sessionName) {
        SessionProfile profile = collaborativeSessions.get(sessionName);
        if (profile.getSeekerSearchResults().record.isEmpty()) {
            profile = htTempSessions.get(sessionName);
        }

        return profile;
    }
}
