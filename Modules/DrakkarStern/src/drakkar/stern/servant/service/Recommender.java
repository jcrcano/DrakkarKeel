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

import drakkar.oar.DocumentMetaData;
import drakkar.oar.Documents;
import drakkar.oar.QuerySource;
import drakkar.oar.RecommendTracker;
import drakkar.oar.Response;
import drakkar.oar.ResultSetMetaData;
import drakkar.oar.Seeker;
import drakkar.oar.exception.RecommendationException;
import drakkar.oar.exception.SeekerException;
import drakkar.oar.exception.SessionException;
import drakkar.oar.slice.client.ClientSidePrx;
import static drakkar.oar.util.KeySearchable.*;
import static drakkar.oar.util.KeyTransaction.*;
import static drakkar.oar.util.NotifyAction.*;
import drakkar.oar.util.OutputMonitor;
import static drakkar.oar.util.SeekerAction.*;
import drakkar.oar.util.Utilities;
import drakkar.stern.callback.NotifyAMICallback;
import drakkar.stern.controller.DataBaseController;
import drakkar.stern.facade.event.FacadeListener;
import drakkar.stern.tracker.cache.Count;
import drakkar.stern.tracker.cache.SeekerInfo;
import drakkar.stern.tracker.cache.SeekerRecommendResults;
import drakkar.stern.tracker.cache.SeekerRecommendResults.IndexRecommendation;
import drakkar.stern.tracker.cache.SeekerRecommendResults.RecommendResults;
import drakkar.stern.tracker.cache.SeekerRecommendResults.SeekerRecData;
import drakkar.stern.tracker.cache.SeekerRecommends;
import drakkar.stern.tracker.cache.SeekerRecommends.RecomendsData;
import drakkar.stern.tracker.cache.SeekerRecommends.SearcherResults;
import drakkar.stern.tracker.cache.SeekerSearchResults;
import drakkar.stern.tracker.cache.SeekerSearchResults.IndexMetaDoc;
import drakkar.stern.tracker.cache.SeekerSearchResults.SearchData;
import drakkar.stern.tracker.cache.SeekerSearchResults.SearchResults;
import drakkar.stern.tracker.cache.SessionProfile;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The <code>ResponseUtilFactory</code> class is.....
 * Esta clase implementa los diferentes métodos de recomendación soportados por
 * el framework DrakkarKeel
 */
public class Recommender extends Service implements Recommendable {

    private long recommendationsCount = 0;

    /**
     * Constructor de la clase
     *
     * @param defaultSessionName
     * @param collaborativeSessions listado de sesiones
     * @param defaultSessionProfile
     */
    public Recommender(String defaultSessionName, SessionProfile defaultSessionProfile, Map<String, SessionProfile> collaborativeSessions, Map<String, SessionProfile> htTempSessions) {
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
    public Recommender(String defaultSessionName, SessionProfile defaultSessionProfile, Map<String, SessionProfile> collaborativeSessions, Map<String, SessionProfile> htTempSessions, DataBaseController dbController) {
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
    public Recommender(String defaultSessionName, SessionProfile defaultSessionProfile, Map<String, SessionProfile> collaborativeSessions, Map<String, SessionProfile> htTempSessions, FacadeListener listener, DataBaseController dbController) {
        super(defaultSessionName, defaultSessionProfile, collaborativeSessions, htTempSessions, listener, dbController);
    }

    /**
     * {@inheritDoc}
     */
    public void recommendResults(String sessionName, Seeker emitter, String comments, QuerySource data) throws SessionException, SeekerException, RecommendationException, IOException {

        int role = emitter.getRole();
        if (role == Seeker.ROLE_POTENTIAL_MEMBER || sessionName.equalsIgnoreCase(defaultSessionName)) {
            throw new RecommendationException("The session or the seeker, don't support the invoked operation");
        } else {

            SessionProfile sessionProfile = this.getSessionProfile(sessionName);
            Map<Seeker, ClientSidePrx> receptors = null;
            ResultSetMetaData list = null;
            List<Seeker> seekerList = null;

            switch (data.getSource()) {

                case LOCAL_SEARCH_RESULT:
                    list = this.getSearchResults(sessionProfile, emitter, data);
                    receptors = this.getReceptorsRecord(sessionProfile, emitter);
                    this.notifyRecommendation(sessionName, emitter, receptors, comments, list);
                    seekerList = new ArrayList<>(receptors.keySet());
                    this.registerRecommendations(sessionProfile, emitter, seekerList, list, comments);
                    break;

                case RECOMMEND_SEARCH_RESULT:
                    list = this.getRecommendResults(sessionProfile, emitter, data);
                    receptors = this.getReceptorsRecord(sessionProfile, emitter);
                    this.notifyRecommendation(sessionName, emitter, receptors, comments, list);
                    seekerList = new ArrayList<>(receptors.keySet());
                    this.registerRecommendations(sessionProfile, emitter, seekerList, list, comments);
                    break;

                default:
                    throw new RecommendationException("El source es incorrecto");

            }
            this.updateRecommendationsCount(sessionName, emitter);
            this.listener.notifyDoneRecommendation();
        }
    }

    /**
     * {@inheritDoc}
     */
    public void recommendResults(String sessionName, Seeker emitter, Seeker receptor, String comments, QuerySource data) throws SessionException, SeekerException, RecommendationException, IOException {

        int role = emitter.getRole();
        if (role == Seeker.ROLE_POTENTIAL_MEMBER || sessionName.equalsIgnoreCase(defaultSessionName)) {
            throw new RecommendationException("The session or the seeker, don't support the invoked operation");
        } else {

            SessionProfile sessionProfile = this.getSessionProfile(sessionName);
            Map<Seeker, ClientSidePrx> receptors = null;
            ResultSetMetaData list = null;

            List<Seeker> seekers = new ArrayList<>();
            seekers.add(receptor);

            switch (data.getSource()) {

                case LOCAL_SEARCH_RESULT:
                    list = this.getSearchResults(sessionProfile, emitter, data);
                    receptors = this.getReceptorsRecord(sessionProfile, seekers);
                    this.notifyRecommendation(sessionName, emitter, receptors, comments, list);
                    this.registerRecommendations(sessionProfile, emitter, seekers, list, comments);
                    break;

                case RECOMMEND_SEARCH_RESULT:
                    list = this.getRecommendResults(sessionProfile, emitter, data);
                    receptors = this.getReceptorsRecord(sessionProfile, seekers);
                    this.notifyRecommendation(sessionName, emitter, receptors, comments, list);
                    this.registerRecommendations(sessionProfile, emitter, seekers, list, comments);
                    break;

                default:
                    throw new RecommendationException("El source es incorrecto");
            }
            this.updateRecommendationsCount(sessionName, emitter);
            this.listener.notifyDoneRecommendation();
        }

    }

    /**
     * {@inheritDoc}
     */
    public void recommendResults(String sessionName, Seeker emitter, String comments, Documents docs, QuerySource data) throws SessionException, SeekerException, RecommendationException, IOException {

        int role = emitter.getRole();
        if (role == Seeker.ROLE_POTENTIAL_MEMBER || sessionName.equalsIgnoreCase(defaultSessionName)) {
            throw new RecommendationException("The session or the seeker, don't support the invoked operation");
        } else {

            SessionProfile sessionProfile = this.getSessionProfile(sessionName);
            Map<Seeker, ClientSidePrx> receptors = this.getReceptorsRecord(sessionProfile, emitter);
            ResultSetMetaData list = null;
            List<Seeker> seekerList = null;


            switch (data.getSource()) {

                case LOCAL_SEARCH_RESULT:
                    list = this.getSearchResults(sessionProfile, emitter, data, docs);
                    this.notifyRecommendation(sessionName, emitter, receptors, comments, list);
                    seekerList = new ArrayList<>(receptors.keySet());
                    this.registerRecommendations(sessionProfile, emitter, seekerList, list, comments);
                    break;

                case RECOMMEND_SEARCH_RESULT:
                    list = this.getRecommendResults(sessionProfile, emitter, data, docs);
                    this.notifyRecommendation(sessionName, emitter, receptors, comments, list);
                    seekerList = new ArrayList<>(receptors.keySet());
                    this.registerRecommendations(sessionProfile, emitter, seekerList, list, comments);
                    break;

                case TRACK_SEARCH_RESULT:

                    try {
                        seekerList = new ArrayList<>(receptors.keySet());
                        this.collabSearchResults(sessionProfile, data, emitter, comments, docs, receptors);
                    } catch (SQLException ex) {
                        throw new RecommendationException(ex.getMessage());
                    }

                    break;

                default:
                    throw new RecommendationException("El source es incorrecto");
            }

            this.updateRecommendationsCount(sessionName, emitter);
            this.listener.notifyDoneRecommendation();
        }
    }

    /**
     * {@inheritDoc}
     */
    public void recommendResults(String sessionName, Seeker emitter, Seeker receptor, Documents docs, String comments, QuerySource data) throws SessionException, SeekerException, RecommendationException, IOException {
        int role = emitter.getRole();
        if (role == Seeker.ROLE_POTENTIAL_MEMBER || sessionName.equalsIgnoreCase(defaultSessionName)) {
            throw new RecommendationException("The session or the seeker, don't support the invoked operation");
        } else {

            SessionProfile sessionProfile = this.getSessionProfile(sessionName);
            ResultSetMetaData list = null;

            List<Seeker> seekers = new ArrayList<>();
            seekers.add(receptor);
            Map<Seeker, ClientSidePrx> receptors = this.getReceptorsRecord(sessionProfile, seekers);

            switch (data.getSource()) {

                case LOCAL_SEARCH_RESULT:
                    list = this.getSearchResults(sessionProfile, emitter, data, docs);
                    this.notifyRecommendation(sessionName, emitter, receptors, comments, list);
                    this.registerRecommendations(sessionProfile, emitter, seekers, list, comments);
                    break;

                case RECOMMEND_SEARCH_RESULT:
                    list = this.getRecommendResults(sessionProfile, emitter, data, docs);
                    this.notifyRecommendation(sessionName, emitter, receptors, comments, list);
                    this.registerRecommendations(sessionProfile, emitter, seekers, list, comments);
                    break;

                case TRACK_SEARCH_RESULT:
                    try {
                        this.collabSearchResults(sessionProfile, data, emitter, comments, docs, receptors);
                    } catch (SQLException ex) {
                        throw new RecommendationException(ex.getMessage());
                    }

                    break;

                default:
                    throw new RecommendationException("El source es incorrecto");

            }

            this.updateRecommendationsCount(sessionName, emitter);
            this.listener.notifyDoneRecommendation();
        }
    }

    /**
     * {@inheritDoc}
     */
    public void recommendResults(String sessionName, Seeker emitter, List<Seeker> receptors, String comments, QuerySource data) throws SessionException, SeekerException, RecommendationException, IOException {

        int role = emitter.getRole();
        if (role == Seeker.ROLE_POTENTIAL_MEMBER || sessionName.equalsIgnoreCase(defaultSessionName)) {
            throw new RecommendationException("The session or the seeker, don't support the invoked operation");
        } else {

            SessionProfile sessionProfile = this.getSessionProfile(sessionName);
            Map<Seeker, ClientSidePrx> receptorsData = null;
            ResultSetMetaData list = null;

            List<Seeker> seekerList = null;

            switch (data.getSource()) {

                case LOCAL_SEARCH_RESULT:
                    list = this.getSearchResults(sessionProfile, emitter, data);
                    receptorsData = this.getReceptorsRecord(sessionProfile, receptors);
                    this.notifyRecommendation(sessionName, emitter, receptorsData, comments, list);
                    seekerList = new ArrayList<>(receptorsData.keySet());
                    this.registerRecommendations(sessionProfile, emitter, seekerList, list, comments);
                    break;

                case RECOMMEND_SEARCH_RESULT:
                    list = this.getRecommendResults(sessionProfile, emitter, data);
                    receptorsData = this.getReceptorsRecord(sessionProfile, receptors);
                    this.notifyRecommendation(sessionName, emitter, receptorsData, comments, list);
                    seekerList = new ArrayList<>(receptorsData.keySet());
                    this.registerRecommendations(sessionProfile, emitter, seekerList, list, comments);
                    break;

                default:
                    throw new RecommendationException("El source es incorrecto");

            }

            this.updateRecommendationsCount(sessionName, emitter);
            this.listener.notifyDoneRecommendation();
        }
    }

    /**
     * {@inheritDoc}
     */
    public void recommendResults(String sessionName, Seeker emitter, List<Seeker> receptors, Documents docs, String comments, QuerySource data) throws SessionException, SeekerException, RecommendationException, IOException {

        int role = emitter.getRole();
        if (role == Seeker.ROLE_POTENTIAL_MEMBER || sessionName.equalsIgnoreCase(defaultSessionName)) {

            throw new RecommendationException("The session or the seeker, don't support the invoked operation");

        } else {

            SessionProfile sessionProfile = this.getSessionProfile(sessionName);
            Map<Seeker, ClientSidePrx> record = this.getReceptorsRecord(sessionProfile, receptors);
            ResultSetMetaData list = null;

            List<Seeker> seekerList = null;

            switch (data.getSource()) {

                case LOCAL_SEARCH_RESULT:
                    list = this.getSearchResults(sessionProfile, emitter, data, docs);
                    this.notifyRecommendation(sessionName, emitter, record, comments, list);
                    seekerList = new ArrayList<>(record.keySet());
                    this.registerRecommendations(sessionProfile, emitter, seekerList, list, comments);
                    break;

                case RECOMMEND_SEARCH_RESULT:
                    list = this.getRecommendResults(sessionProfile, emitter, data, docs);
                    this.notifyRecommendation(sessionName, emitter, record, comments, list);
                    seekerList = new ArrayList<>(record.keySet());
                    this.registerRecommendations(sessionProfile, emitter, seekerList, list, comments);
                    break;

                case TRACK_SEARCH_RESULT:
                    try {
                        this.collabSearchResults(sessionProfile, data, emitter, comments, docs, record);
                    } catch (SQLException ex) {
                        throw new RecommendationException(ex.getMessage());
                    }

                    break;

                default:
                    throw new RecommendationException("El source es incorrecto");

            }

            this.updateRecommendationsCount(sessionName, emitter);
            this.listener.notifyDoneRecommendation();

        }
    }

    /**
     * {@inheritDoc}
     */
    public void recommendResults(String sessionName, Seeker emitter, String sessionNameRtr, Seeker receptor, String comments, QuerySource data) throws SessionException, SeekerException, RecommendationException, IOException {

        int role = emitter.getRole();
        if (role == Seeker.ROLE_POTENTIAL_MEMBER || sessionName.equalsIgnoreCase(defaultSessionName)) {
            throw new RecommendationException("The session or the seeker, don't support the invoked operation");
        } else {

            SessionProfile sessionProfile = this.getSessionProfile(sessionName);
            SessionProfile sessionProfile2 = this.getSessionProfile(sessionNameRtr);

            Map<Seeker, ClientSidePrx> receptors = null;
            ResultSetMetaData list = null;

            List<Seeker> seekers = new ArrayList<>();
            seekers.add(receptor);

            switch (data.getSource()) {

                case LOCAL_SEARCH_RESULT:
                    list = this.getSearchResults(sessionProfile, emitter, data);
                    receptors = this.getReceptorsRecord(sessionProfile2, seekers);
                    this.notifyRecommendation(sessionNameRtr, emitter, receptors, comments, list);
                    this.registerRecommendations(sessionProfile2, emitter, seekers, list, comments);
                    break;

                case RECOMMEND_SEARCH_RESULT:
                    list = this.getRecommendResults(sessionProfile, emitter, data);
                    receptors = this.getReceptorsRecord(sessionProfile2, seekers);
                    this.notifyRecommendation(sessionNameRtr, emitter, receptors, comments, list);
                    this.registerRecommendations(sessionProfile2, emitter, seekers, list, comments);

                    break;

                default:
                    throw new RecommendationException("El source es incorrecto");

            }

            this.updateRecommendationsCount(sessionName, emitter);
            this.listener.notifyDoneRecommendation();

        }
    }

    /**
     * {@inheritDoc}
     */
    public void recommendResults(String sessionName, Seeker emitter, String sessionNameRtrs, List<Seeker> receptors, String comments, QuerySource data) throws SessionException, SeekerException, RecommendationException, IOException {

        int role = emitter.getRole();
        if (role == Seeker.ROLE_POTENTIAL_MEMBER || sessionName.equalsIgnoreCase(defaultSessionName)) {
            throw new RecommendationException("The session or the seeker, don't support the invoked operation");
        } else {

            SessionProfile sessionProfile = this.getSessionProfile(sessionName);
            SessionProfile sessionProfile2 = this.getSessionProfile(sessionNameRtrs);

            Map<Seeker, ClientSidePrx> receptorsData = null;
            ResultSetMetaData list = null;

            List<Seeker> seekerList = null;

            switch (data.getSource()) {

                case LOCAL_SEARCH_RESULT:
                    list = this.getSearchResults(sessionProfile, emitter, data);
                    receptorsData = this.getReceptorsRecord(sessionProfile2, receptors);
                    this.notifyRecommendation(sessionNameRtrs, emitter, receptorsData, comments, list);
                    seekerList = new ArrayList<>(receptorsData.keySet());
                    this.registerRecommendations(sessionProfile2, emitter, seekerList, list, comments);
                    break;

                case RECOMMEND_SEARCH_RESULT:
                    list = this.getRecommendResults(sessionProfile, emitter, data);
                    receptorsData = this.getReceptorsRecord(sessionProfile2, receptors);
                    this.notifyRecommendation(sessionNameRtrs, emitter, receptorsData, comments, list);
                    seekerList = new ArrayList<>(receptorsData.keySet());
                    this.registerRecommendations(sessionProfile2, emitter, seekerList, list, comments);
                    break;

                default:
                    throw new RecommendationException("El source es incorrecto");

            }

            this.updateRecommendationsCount(sessionName, emitter);
            this.listener.notifyDoneRecommendation();
        }
    }

    /**
     * {@inheritDoc}
     */
    public void recommendResults(String sessionName, Seeker emitter, String sessionNameRtr, Seeker receptor, Documents docs, String comments, QuerySource data) throws SessionException, SeekerException, RecommendationException, IOException {
        int role = emitter.getRole();
        if (role == Seeker.ROLE_POTENTIAL_MEMBER || sessionName.equalsIgnoreCase(defaultSessionName)) {
            throw new RecommendationException("The session or the seeker, don't support the invoked operation");
        } else {

            SessionProfile sessionProfile = this.getSessionProfile(sessionName);
            SessionProfile sessionProfile2 = this.getSessionProfile(sessionNameRtr);
            List<Seeker> seekers = new ArrayList<>();
            seekers.add(receptor);
            Map<Seeker, ClientSidePrx> receptors = this.getReceptorsRecord(sessionProfile2, seekers);
            ResultSetMetaData list = null;

            switch (data.getSource()) {

                case LOCAL_SEARCH_RESULT:
                    list = this.getSearchResults(sessionProfile, emitter, data, docs);
                    this.notifyRecommendation(sessionNameRtr, emitter, receptors, comments, list);
                    this.registerRecommendations(sessionProfile2, emitter, seekers, list, comments);
                    break;

                case RECOMMEND_SEARCH_RESULT:
                    list = this.getRecommendResults(sessionProfile, emitter, data, docs);
                    this.notifyRecommendation(sessionNameRtr, emitter, receptors, comments, list);
                    this.registerRecommendations(sessionProfile2, emitter, seekers, list, comments);
                    break;

                case TRACK_SEARCH_RESULT:
                    try {

                        this.collabSearchResults(sessionProfile, data, emitter, comments, docs, receptors);
                    } catch (SQLException ex) {
                        throw new RecommendationException(ex.getMessage());
                    }

                    break;

                default:
                    throw new RecommendationException("El source es incorrecto");
            }

            this.updateRecommendationsCount(sessionName, emitter);
            this.listener.notifyDoneRecommendation();

        }
    }

    /**
     * {@inheritDoc}
     */
    public void recommendResults(String sessionName, Seeker emitter, String sessionNameRtrs, List<Seeker> receptors, Documents docs, String comments, QuerySource data) throws SessionException, SeekerException, RecommendationException, IOException {

        int role = emitter.getRole();
        if (role == Seeker.ROLE_POTENTIAL_MEMBER || sessionName.equalsIgnoreCase(defaultSessionName)) {
            throw new RecommendationException("The session or the seeker, don't support the invoked operation");
        } else {

            SessionProfile sessionProfile = this.getSessionProfile(sessionName);
            SessionProfile sessionProfile2 = this.getSessionProfile(sessionNameRtrs);

            Map<Seeker, ClientSidePrx> record = this.getReceptorsRecord(sessionProfile2, receptors);
            ResultSetMetaData list = null;
            List<Seeker> seekerList = null;

            switch (data.getSource()) {

                case LOCAL_SEARCH_RESULT:
                    list = this.getSearchResults(sessionProfile, emitter, data, docs);
                    this.notifyRecommendation(sessionNameRtrs, emitter, record, comments, list);
                    seekerList = new ArrayList<>(record.keySet());
                    this.registerRecommendations(sessionProfile2, emitter, seekerList, list, comments);
                    break;

                case RECOMMEND_SEARCH_RESULT:
                    list = this.getRecommendResults(sessionProfile, emitter, data, docs);
                    this.notifyRecommendation(sessionNameRtrs, emitter, record, comments, list);
                    seekerList = new ArrayList<>(record.keySet());
                    this.registerRecommendations(sessionProfile2, emitter, seekerList, list, comments);
                    break;

                case TRACK_SEARCH_RESULT:
                    try {

                        this.collabSearchResults(sessionProfile, data, emitter, comments, docs, record);
                    } catch (SQLException ex) {
                        throw new RecommendationException(ex.getMessage());
                    }

                    break;

                default:
                    throw new RecommendationException("El source es incorrecto");

            }

            this.updateRecommendationsCount(sessionName, emitter);
            this.listener.notifyDoneRecommendation();



        }
    }

    /**********************************Utilities***********************/
    /**
     * Este método devuelve todos los resultados  de búsqueda a partir de una consulta determinada,
     * de todos los buscadores empleados ó selección de estos.
     *
     * @param sessionProfile  nombre de la sesión
     * @param emitter      miembro que realiza la recomendación

     * @return results listado con los resultados de la búsqueda
     *
     * @throws SessionException
     * @throws UserNotExistException
     *
     */
    private ResultSetMetaData getDocuments(SessionProfile sessionProfile, Seeker emitter, QuerySource data) throws SessionException, SeekerException, RecommendationException {

        SeekerSearchResults searchValues = sessionProfile.getSeekerSearchResults();
        boolean flag = !searchValues.record.isEmpty();
        //si existen resultados de búsq. para la sesión especificada
        if (flag) {
            String query = data.getQuery();
            flag = searchValues.record.containsKey(emitter);
            if (flag) {
                SearchData searchData = searchValues.record.get(emitter);
                SearchResults searchResults = searchData.values.get(query);

                if (searchResults == null) {
                    throw new RecommendationException("The query " + query + " doesn´t exist");
                }

                int[] searcher = data.getSearchers();
                ResultSetMetaData temp = null;

                if (searcher == null) {//todos los resultados obtenidos por buscadores
                    temp = new ResultSetMetaData(query, searchResults.getDocumentMap());
                    return temp;
                } else {

                    Map<Integer, List<DocumentMetaData>> resultsHash = new HashMap<>();
                    List<DocumentMetaData> metaDocs = null;

                    for (int item : searcher) {
                        metaDocs = searchResults.results.get(item).getDocuments();
                        if (metaDocs != null) {
                            resultsHash.put(item, metaDocs);
                        }
                    }
                    temp = new ResultSetMetaData(data.getQuery(), resultsHash);

                    return temp;
                }
            } else {
                throw new SeekerException("The seeker '" + emitter.getUser() + "' is not registered in the session '" + sessionProfile.getProperties().getSessionName() + "'.");
            }
        } else {
            throw new SessionException("The session '" + sessionProfile.getProperties().getSessionName() + "' doesn't exist.");
        }
    }

    /**
     * Este método devuelve una selección los resultados de búsqueda a partir de una consulta determinada,
     * para buscadores seleccionados
     *
     * @param sessionProfile  nombre de la sesión
     * @param emitter      miembro que realiza la recomendación

     * @return results listado con los resultados de la búsqueda
     *
     * @throws SessionException
     * @throws UserNotExistException
     *
     */
    private ResultSetMetaData getDocuments(SessionProfile sessionProfile, Seeker emitter, Documents docs, QuerySource data) throws SessionException, SeekerException, RecommendationException {

        SeekerSearchResults searchValues = sessionProfile.getSeekerSearchResults();
        boolean flag = !searchValues.record.isEmpty();
        //si existen resultados de búsq. para la sesión especificada
        if (flag) {

            flag = searchValues.record.containsKey(emitter);
            if (flag) {

                String query = data.getQuery();
                SearchData searchData = searchValues.record.get(emitter);
                SearchResults searchResults = searchData.values.get(query);

                if (searchResults == null) {
                    throw new RecommendationException("The query " + query + " doesn´t exist");
                }

                ResultSetMetaData temp = null;
                Map<Integer, List<DocumentMetaData>> resultsHash = new HashMap<>();
                IndexMetaDoc indexMDoc;
                List<DocumentMetaData> metaDocs = null;

                // documentos a recomendar por bucadores
                Map<Integer, List<Integer>> values = docs.getDocs();

                Set<Integer> searchersID = values.keySet();
                 List<Integer> docsIndex = null;

                for (Integer item : searchersID) {
                    docsIndex = new ArrayList<>(values.get(item));
                    indexMDoc = searchResults.results.get(item);

                    if (indexMDoc != null) {
                        metaDocs = new ArrayList<>();
                        for (Integer i : docsIndex) {
                            metaDocs.add(indexMDoc.index.get(i));
                        }
                        resultsHash.put(item, metaDocs);
                    }
                }
                

                temp = new ResultSetMetaData(data.getQuery(), resultsHash);
                return temp;

            } else {
                throw new SeekerException("The seeker '" + emitter.getUser() + "' is not registered in the session '" + sessionProfile.getProperties().getSessionName() + "'.");
            }
        } else {
            throw new SessionException("The session '" + sessionProfile.getProperties().getSessionName() + "' doesn't exist.");
        }
    }

    /**
     * Este método devuelve todos los resultados  de recomendaciones recibidas a partir de una consulta determinada,
     * de todos los buscadores empleados ó selección de estos.
     *
     * @param sessionProfile  nombre de la sesión
     * @param emitter      miembro que realiza la recomendación

     * @return results listado con los resultados de la búsqueda
     *
     * @throws SessionException
     * @throws UserNotExistException
     *
     */
    private ResultSetMetaData getRecommends(SessionProfile sessionProfile, Seeker emitter, QuerySource data) throws SessionException, SeekerException, RecommendationException {

        SeekerRecommendResults recommendValues = sessionProfile.getSeekerRecommendResults();
        boolean flag = !recommendValues.record.isEmpty();
        String sessionName = sessionProfile.getProperties().getSessionName();
        //si existen resultados de búsq. para la sesión especificada
        if (flag) {

            flag = recommendValues.record.containsKey(emitter);
            if (flag) {
                String query = data.getQuery();

                SeekerRecData results = recommendValues.record.get(emitter);
                RecommendResults recommendResults = results.values.get(data.getQuery());

                if (recommendResults == null) {
                    throw new RecommendationException("The query " + query + " doesn´t exist");
                }

                int[] searcher = data.getSearchers();
                ResultSetMetaData temp = null;

                if (searcher == null) {//todos los resultados obtenidos por buscadores
                    temp = new ResultSetMetaData(query, recommendResults.getDocuments());
                    return temp;
                } else {

                    Map<Integer, List<DocumentMetaData>> resultsHash = new HashMap<>();
                    List<DocumentMetaData> metaDocs = null;

                    for (int i : searcher) {
                        metaDocs = recommendResults.results.get(i).getDocuments();
                        if (metaDocs != null) {
                            resultsHash.put(i, metaDocs);
                        }
                    }

                    temp = new ResultSetMetaData(query, resultsHash);

                    return temp;
                }

            } else {
                throw new SeekerException("The seeker '" + emitter.getUser() + "' is not registered in the session '" + sessionProfile.getProperties().getSessionName() + "'.");
            }
        } else {
            throw new SessionException("The session '" + sessionName + "' doesn't exist.");
        }
    }

    /**
     * Este método devuelve una selección los resultados de recomendaciones recibidas a partir de una consulta determinada,
     * para buscadores seleccionados
     *
     * @param sessionProfile  nombre de la sesión
     * @param emitter      miembro que realiza la recomendación

     * @return results listado con los resultados de la búsqueda
     *
     * @throws SessionException
     * @throws UserNotExistException
     *
     */
    private ResultSetMetaData getRecommends(SessionProfile sessionProfile, Seeker emitter, Documents docs, QuerySource data) throws SessionException, SeekerException, RecommendationException {

        SeekerRecommendResults recommendValues = sessionProfile.getSeekerRecommendResults();
        boolean flag = !recommendValues.record.isEmpty();
        String sessionName = sessionProfile.getProperties().getSessionName();
        //si existen resultados de búsq. para la sesión especificada
        if (flag) {
            flag = recommendValues.record.containsKey(emitter);
            if (flag) {
                String query = data.getQuery();
                SeekerRecData results = recommendValues.record.get(emitter);
                RecommendResults recommendResults = results.values.get(data.getQuery());

                if (recommendResults == null) {
                    throw new RecommendationException("The query " + query + " doesn´t exist");
                }

                ResultSetMetaData temp = null;

                Map<Integer, List<DocumentMetaData>> resultsHash = new HashMap<>();
                SeekerRecommendResults.IndexRecommendation indexMDoc;
                List<DocumentMetaData> metaDocs = null;

                Map<Integer, List<Integer>> values = docs.getDocs();
              
                List<Integer> docsIndex = null;
                Set<Integer> searchersID = values.keySet();
                for (Integer item : searchersID) {
                    docsIndex = new ArrayList<>(values.get(item));
                    indexMDoc = recommendResults.results.get(item);
                    if (indexMDoc != null) {
                        metaDocs = new ArrayList<>();
                        for (Integer i : docsIndex) {
                            metaDocs.add(indexMDoc.index.get(i));
                        }
                        resultsHash.put(item, metaDocs);
                    }
                }
               
                temp = new ResultSetMetaData(query, resultsHash);

                return temp;

            } else {
                throw new SeekerException("The seeker '" + emitter.getUser() + "' is not registered in the session '" + sessionProfile.getProperties().getSessionName() + "'.");
            }
        } else {
            throw new SessionException("The session '" + sessionName + "' doesn't exist.");
        }
    }

    /**
     * Este método se emplea para actualizar la relación de recomendaciones efectuadas
     * por los miembros de un sesión y el total general de la misma.
     *
     * @param sessionProfile nombre de la sesión a la que pertenece el miembro.
     * @param seeker      miembro que ejecuta la búsqueda.
     */
    private void updateRecommendationsCount(String sessionName, Seeker seeker) {
        SessionProfile recordSession = this.collaborativeSessions.get(sessionName);
        int count = 0;
        //se obtiene la relación de recomendaciones efectuadas por los
        // miembros de la sesión.
        Count membRecCount = recordSession.getSeekersRecommendations();
        boolean flag = !membRecCount.record.isEmpty();
        if (flag) {
            try {
                // se obtiene e incrementa el número de recomendaciones del miembro
                count = membRecCount.record.get(seeker);
                count++;
                // si el miembro no se encuentra registrado, se procede
                // a realizar su registro al capturar la expeción.
            } catch (NullPointerException e) {
                membRecCount.record.put(seeker, 1);
            }
        } else {
            membRecCount.record.put(seeker, 1);

        }
        // se incrementa el valor de las recomendaciones efectuadas
        // durante la actual sesión del servidor.
        this.recommendationsCount++;
        // se incrementa el valor de las recomendaciones efectuadas
        // por la la sesión durante actual sesión del servidor.

        count = recordSession.getRecommendationsCount();
        count++;
    }

    private Response buildResponse(Seeker emitter, ResultSetMetaData recommendation, String comments) {

        Map<Object, Object> hash = new HashMap<>(4); // se confecciona el objeto Response
        hash.put(OPERATION, NOTIFY_EXPLICIT_RECOMMENDATION);
        hash.put(SEEKER_EMITTER, emitter);
        hash.put(RECOMMENDATION, recommendation);
        hash.put(COMMENT, comments);
        Response response = new Response(hash);

        return response;
    }

    /**
     * {@inheritDoc}
     */
    public long getRecommendationsCount() {
        return this.recommendationsCount;
    }

    /**
     * {@inheritDoc}
     */
    public long getRecommendationsCount(String sessionName) throws SessionException {
        SessionProfile recordSession = getSessionProfile(sessionName);
        int count = recordSession.getRecommendationsCount();
        return count;

    }

    /**
     * {@inheritDoc}
     */
    public long getRecommendationsCount(String sessionName, Seeker seeker)
            throws SessionException, SeekerException {

        SessionProfile recordSession = getSessionProfile(sessionName);
        Count members = recordSession.getSeekersRecommendations();
        boolean flag = members.record.containsKey(seeker);
        if (flag) {
            int count = members.record.get(seeker);
            return count;
        } else {
            throw new SeekerException("The seeker '" + seeker.getUser() + "' is not registered in the session '" + sessionName + "'.");
        }


    }

    private ResultSetMetaData getSearchResults(SessionProfile sessionProfile, Seeker emitter, QuerySource data) throws SessionException, SeekerException, RecommendationException, IOException {

        SeekerInfo members = sessionProfile.getSeekerInfo();
        boolean flag = members.record.containsKey(emitter);
        // se comprueba que el miembro que emite la recommendación sea miembro
        // de la sesión, en caso contrario se lanza una excepción del tipo
        // UserNotExistException.
        if (flag) {
            // se obtiene la relación de los resultados de búquedas anteriores de
            // los miembros de la sesión
            SeekerSearchResults searches = sessionProfile.getSeekerSearchResults();
            flag = !searches.record.isEmpty();
            // se comprueba que la sesión cuente con registros de resultados de
            // búsquedas anteriores y que el miembro que emite la recomendación,
            // se encuentre en estos, en caso contrario se lanza una excepción
            // del tipo RecommendationException.
            if (flag) {
                flag = searches.record.containsKey(emitter);
                if (flag) {
                    ResultSetMetaData recommendation = getDocuments(sessionProfile, emitter, data);
                    return recommendation;

                } else {
                    throw new RecommendationException("The seeker '" + emitter.getUser() + "'doesn't have previous results of search");
                }
            } else {
                throw new RecommendationException("The session '" + sessionProfile.getProperties().getSessionName() + "'doesn't have previous results of search");
            }
        } else {
            throw new SeekerException("The seeker '" + emitter.getUser() + "' is not registered in the session '" + sessionProfile.getProperties().getSessionName() + "'.");
        }

    }

    private ResultSetMetaData getSearchResults(SessionProfile sessionProfile, Seeker emitter, QuerySource data, Documents docs) throws SessionException, SeekerException, RecommendationException, IOException {

        SeekerInfo members = sessionProfile.getSeekerInfo();
        boolean flag = members.record.containsKey(emitter);
        // se comprueba que el miembro que emite la recommendación sea miembro
        // de la sesión, en caso contrario se lanza una excepción del tipo
        // UserNotExistException.
        if (flag) {
            // se obtiene la relación de los resultados de búquedas anteriores de
            // los miembros de la sesión
            SeekerSearchResults searches = sessionProfile.getSeekerSearchResults();
            flag = !searches.record.isEmpty();
            // se comprueba que la sesión cuente con registros de resultados de
            // búsquedas anteriores y que el miembro que emite la recomendación,
            // se encuentre en estos, en caso contrario se lanza una excepción
            // del tipo RecommendationException.
            if (flag) {

                flag = searches.record.containsKey(emitter);

                if (flag) {
                    ResultSetMetaData recommendation = getDocuments(sessionProfile, emitter, docs, data);
                    return recommendation;
                } else {
                    throw new RecommendationException("The seeker '" + emitter.getUser() + "'doesn't have previous results of search");
                }
            } else {
                throw new RecommendationException("The session '" + sessionProfile.getProperties().getSessionName() + "'doesn't have previous results of search");
            }
        } else {
            throw new SeekerException("The seeker '" + emitter.getUser() + "' is not registered in the session '" + sessionProfile.getProperties().getSessionName() + "'.");
        }


    }

    private ResultSetMetaData getRecommendResults(SessionProfile sessionProfile, Seeker emitter, QuerySource data) throws SessionException, SeekerException, RecommendationException, IOException {

        SeekerInfo members = sessionProfile.getSeekerInfo();

        if (members.record.containsKey(emitter)) {

            SeekerRecommendResults recommend = sessionProfile.getSeekerRecommendResults();

            if (!recommend.record.isEmpty()) {

                if (recommend.record.containsKey(emitter)) {

                    ResultSetMetaData recommendation = getDocuments(sessionProfile, emitter, data);

                    return recommendation;

                } else {
                    throw new RecommendationException("The seeker '" + emitter.getUser() + "'doesn't have previous results of search");
                }
            } else {
                throw new RecommendationException("The session '" + sessionProfile.getProperties().getSessionName() + "'doesn't have previous results of search");
            }
        } else {
            throw new SeekerException("The seeker '" + emitter.getUser() + "' is not registered in the session '" + sessionProfile.getProperties().getSessionName() + "'.");
        }


    }

    private ResultSetMetaData getRecommendResults(SessionProfile sessionProfile, Seeker emitter, QuerySource data, Documents docs) throws SessionException, SeekerException, RecommendationException, IOException {

        SeekerInfo members = sessionProfile.getSeekerInfo();

        if (members.record.containsKey(emitter)) {
            SeekerSearchResults searches = sessionProfile.getSeekerSearchResults();
            if (!searches.record.isEmpty()) {

                if (searches.record.containsKey(emitter)) {
                    ResultSetMetaData recommendation = getDocuments(sessionProfile, emitter, docs, data);
                    return recommendation;

                } else {
                    throw new RecommendationException("The seeker '" + emitter.getUser() + "'doesn't have previous results of search");
                }
            } else {
                throw new RecommendationException("The session '" + sessionProfile.getProperties().getSessionName() + "'doesn't have previous results of search");
            }
        } else {
            throw new SeekerException("The seeker '" + emitter.getUser() + "' is not registered in the session '" + sessionProfile.getProperties().getSessionName() + "'.");
        }

    }

    private Map<Seeker, ClientSidePrx> getReceptorsRecord(SessionProfile sessionProfile, List<Seeker> seekers) {

        SeekerInfo seekersPrx = sessionProfile.getSeekerInfo();
        Map<Seeker, ClientSidePrx> seekersData = new HashMap<>();

        for (Seeker seeker : seekers) {
            seekersData.put(seeker, seekersPrx.record.get(seeker));
        }

        return seekersData;
    }

    // Devuelve todos los usuarios de la sesión(exceptuando el emisor) con sus correspondientes obj proxies
    private Map<Seeker, ClientSidePrx> getReceptorsRecord(SessionProfile sessionProfile, Seeker emitter) {

        SeekerInfo seekersPrx = sessionProfile.getSeekerInfo();
        Map<Seeker, ClientSidePrx> seekersData = new HashMap<>();
        List<Seeker> seekers = new ArrayList<>(seekersPrx.record.keySet());
        seekers.remove(emitter);

        for (Seeker seeker : seekers) {
            seekersData.put(seeker, seekersPrx.record.get(seeker));
        }
        return seekersData;
    }

    private void notifyRecommendation(String sessionName, Seeker emitter, Map<Seeker, ClientSidePrx> receptors, String comments, ResultSetMetaData list) throws RecommendationException {

        Response rsp = this.buildResponse(emitter, list, comments);
        byte[] array = null;
        try {
            array = rsp.toArray(); // se serializa el objeto Response
        } catch (IOException ex) {
            throw new RecommendationException(ex.getMessage());
        }

        Set<Seeker> sekkers = receptors.keySet();
        ClientSidePrx clientSidePrx;

        for (Seeker seeker : sekkers) {
             clientSidePrx = receptors.get(seeker);
            clientSidePrx.notify_async(new NotifyAMICallback(seeker, "notifyRecommendation"), array);
            this.listener.notifyDoneRecommendation();
        }
        
        this.updateRecommendationsCount(sessionName, emitter);


    }

    private void registerRecommendations(final SessionProfile session, final Seeker emitter, final List<Seeker> seekers, final ResultSetMetaData list, final String comments) {
        final DataBaseController control = this.dbController;
        Thread thread = new Thread(new Runnable() {

            @SuppressWarnings("unchecked")
            public void run() {
                try {

                    String date = Utilities.getDate();
                    String query = list.getQuery();
                    updateRecordSession(query, list, seekers, session);
                    List<String> seekersUsers = new ArrayList<>();

                    for (Seeker seeker : seekers) {
                        seekersUsers.add(seeker.getUser());
                    }
                    RecommendTracker data = null;

                    List<DocumentMetaData> metaDocs = null;
                    List<RecommendTracker> recommendData = new ArrayList<>();

                    Map<Integer, List<RecommendTracker>> hash = new HashMap<>();
                    Set<Integer> searchers = list.getResultsMap().keySet();
                    for (Integer searcher : searchers) {
                        metaDocs = list.getResultList(searcher);
                        recommendData.clear();

                        for (DocumentMetaData docs : metaDocs) {
                            data = new RecommendTracker(seekersUsers, emitter.getUser(), date, comments, docs.getPath(), docs.getIndex(), query, searcher);
                            recommendData.add(data);
                        }

                        hash.put(searcher, (ArrayList<RecommendTracker>) ((ArrayList) recommendData).clone());
                    }

                    registerSeekerRecommendations(emitter, session.getSeekerRecommends(), hash, query);

                    //BD
                    control.saveRecomendation(session.getProperties().getSessionName(), emitter.getUser(), comments, list, seekers);
                } catch (SQLException ex) {
                    OutputMonitor.printStream("SQL", ex);
                }
            }
        });
        thread.start();
    }

    /**
     * Este método es empleado para actualizar el registro de usuario una vez efectuada
     * una recomendación
     *
     * @param query              término de la consulta
     * @param list   resultados de la búsqueda
     * @param documentsCount     cantidad de documentos encontrados
     * @param seeker             receptores de la recomendación
     * @param recordSession      registro de la sesión
     */
    private void updateRecordSession(String query, ResultSetMetaData list, List<Seeker> seekers, SessionProfile recordSession) {

        SeekerRecommendResults recommendProfile = recordSession.getSeekerRecommendResults();

        //si existen resultados de búsq. para la sesión especificada
        boolean flag = !recommendProfile.record.isEmpty();

        if (flag) {
            for (Seeker seeker : seekers) {
                updateRecordSession(query, list, seeker, recommendProfile);
            }

        } else {
            // si no existen registros de recomendaciones para la sesión especificada se
            // procede a registrar la misma para almacenar los resultados del usuario
            SeekerRecData results = recommendProfile.new SeekerRecData();
            RecommendResults recommendResults = recommendProfile.new RecommendResults();
            Map<Integer, List<DocumentMetaData>> hash = list.getResultsMap();
            Set<Integer> searchersEnum = hash.keySet();
            for (Integer searcher : searchersEnum) {
                recommendResults.results.put(searcher, recommendProfile.new IndexRecommendation(hash.get(searcher)));
            }

            results.values.put(query, recommendResults);
            // se registra el miembro con los resultados obtenidos
            for (Seeker item : seekers) {
                recommendProfile.record.put(item, results);
            }

        }
    }

    /**
     * Almacenar en caché las recomendaciones hechas por un usuario
     *
     * @param emitter
     * @param seekerRecommends
     * @param recommends
     * @param query     
     */
    public void registerSeekerRecommendations(Seeker emitter, SeekerRecommends seekerRecommends, Map<Integer, List<RecommendTracker>> recommends, String query) {

        RecomendsData recomendsData;
        SearcherResults searcherResults = null;

        if (seekerRecommends.values.containsKey(emitter)) {

            recomendsData = seekerRecommends.values.get(emitter);

            if (recomendsData.data.containsKey(query)) {
                searcherResults = recomendsData.data.get(query);
                searcherResults.insertRecommendation(recommends);
            } else {
                searcherResults = seekerRecommends.new SearcherResults(recommends);
                recomendsData.data.put(query, searcherResults);
            }



        } else {
            //no ha hecho recomendaciones
            recomendsData = seekerRecommends.new RecomendsData();
            searcherResults = seekerRecommends.new SearcherResults(recommends);
            recomendsData.data.put(query, searcherResults);
            seekerRecommends.values.put(emitter, recomendsData);

        }

    }

    /**
     * Este método es empleado para actualizar el registro de cada miembro de la sesión
     * colaborativa, una vez que se efectúa una recomendación
     *
     * @param query       término de la consulta
     * @param list        lista de resultados.
     * @param seeker      miembro al cual actualizar su registro.
     * @param searchTemp  registro de búsquedas de la sesión a la que pertenece el usuario.
     */
    private void updateRecordSession(String query, ResultSetMetaData list, Seeker seeker, SeekerRecommendResults recommendProfile) {
        SeekerRecData results;
        RecommendResults recommendResults = null;
        // si el miembro cuenta con resultados anteriores, se le adicionan
        // los nuevos resultados
        boolean flag = recommendProfile.record.containsKey(seeker);
        if (flag) {
            //se obtienen los resultados de búsquedas anteriores del miembro
            results = recommendProfile.record.get(seeker);


            if (results.values.containsKey(query)) {
                recommendResults = results.values.get(query);
                Map<Integer, List<DocumentMetaData>> hash = list.getResultsMap();
                recommendResults.insertRecommendation(hash);

            } else {

                recommendResults = recommendProfile.new RecommendResults();
                results.values.put(query, recommendResults);
            }





        } else {
            // en caso de que el usuario no cuente recomendaciones previas, se
            // crea una nueva lista de recomendaciones y se le agregan los resultados obtenidos
            results = recommendProfile.new SeekerRecData();
            recommendResults = recommendProfile.new RecommendResults();
            Map<Integer, List<DocumentMetaData>> hash = list.getResultsMap();
            Set<Integer> searchableEnum = hash.keySet();
            for (Integer searcher : searchableEnum) {
                recommendResults.results.put(searcher, recommendProfile.new IndexRecommendation(hash.get(searcher)));
            }
            results.values.put(query, recommendResults);
            // se registra el miembro con los resultados obtenidos en la tabla
            // hash de los resultados de la búsquedas
            recommendProfile.record.put(seeker, results);
        }
    }

    /**
     *
     * Se recomienda un doc de un historial
     * 
     */
    private void collabSearchResults(final SessionProfile sessionProfile, final QuerySource querySource, final Seeker emitter, final String comments, final Documents docs, final Map<Seeker, ClientSidePrx> receptors) throws SessionException, SeekerException, RecommendationException, IOException, SQLException {
        // TODO:  recomendar todos los resultados de ese usuario en esa sesión

        Thread thread = new Thread(new Runnable() {

            public void run() {
                try {

                    String query = querySource.getQuery();
                    boolean storeSource = querySource.isStoreSource();
                    ResultSetMetaData doc = new ResultSetMetaData();
                    String sessionName = sessionProfile.getProperties().getSessionName();

                    if (storeSource) { //cache
                        doc = getDocumentsList(docs);
                        doc.setQuery(query);

                        registerRecommendations(sessionProfile, emitter, new ArrayList<>(receptors.keySet()), doc, comments);
                        notifyRecommendation(sessionName, emitter, receptors, comments, doc);

                    } else { //BD
                        final DataBaseController control = dbController;

                        if (dbController.isOpen()) { //guardar

                            final List<Integer> documents = docs.getIndexDocuments();

                            List<DocumentMetaData> metaDocs = dbController.getSearchResults(documents);

                            doc.add(MULTIPLE_SEARCHERS, metaDocs);
                            doc.setQuery(query);

                            List<Seeker> seekers = new ArrayList<>(receptors.keySet());
                            registerRecommendations(sessionProfile, emitter, seekers, doc, comments);

                            notifyRecommendation(sessionName, emitter, receptors, comments, doc);
                            control.saveRecomendation(sessionProfile.getProperties().getSessionName(), emitter.getUser(), comments, documents, seekers);
                            updateResultsStore(doc);

                        }
                    }
                } catch (SQLException ex) {
                    OutputMonitor.printStream("SQL", ex);
                }
                catch (RecommendationException ex) {
                    OutputMonitor.printStream("", ex);
                }
            }
        });
        thread.start();

    }
}
