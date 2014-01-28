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
import drakkar.oar.Response;
import drakkar.oar.ResultSetMetaData;
import drakkar.oar.Seeker;
import drakkar.oar.exception.SeekerException;
import drakkar.oar.exception.SessionException;
import drakkar.oar.slice.client.ClientSidePrx;
import drakkar.oar.svn.SVNData;
import static drakkar.oar.util.KeySearchable.*;
import drakkar.oar.util.OutputMonitor;
import drakkar.oar.util.SearchPrinciple;
import static drakkar.oar.util.SeekerAction.*;
import drakkar.mast.RetrievalManager;
import drakkar.mast.SearchException;
import drakkar.mast.SearchableException;
import drakkar.stern.ResponseSearchFactory;
import drakkar.stern.callback.NotifyAMICallback;
import drakkar.stern.controller.DataBaseController;
import drakkar.stern.facade.event.FacadeListener;
import drakkar.stern.tracker.cache.SeekerInfo;
import drakkar.stern.tracker.cache.SeekerSearchResults;
import drakkar.stern.tracker.cache.SeekerSearchResults.IndexMetaDoc;
import drakkar.stern.tracker.cache.SeekerSearchResults.SearchData;
import drakkar.stern.tracker.cache.SeekerSearchResults.SearchResults;
import drakkar.stern.tracker.cache.SessionProfile;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The <code>SearcherServer</code> class is....
 * Esta clase maneja todos los métodos búsqueda soportados por el framework DrakkarKeel
 */
public class SearcherServer extends Service implements SearchableInServerSide {

    private RetrievalManager retrievalManager;
    /**
     * Esta variable representa el número de búsquedas efectuadas durante la actual
     * sesión del servidor.
     */
    private long searchesCount = 0;

    /**
     * Constructor de la clase
     *
     * @param defaultSessionName
     * @param collaborativeSessions listado de sesiones
     * @param defaultSessionProfile
     * @param retrievalManager
     */
    public SearcherServer(String defaultSessionName, SessionProfile defaultSessionProfile, Map<String, SessionProfile> collaborativeSessions, Map<String, SessionProfile> htTempSessions, RetrievalManager retrievalManager) {
        super(defaultSessionName, defaultSessionProfile, collaborativeSessions, htTempSessions);
        this.retrievalManager = retrievalManager;
    }

    /**
     * Constructor de la clase
     *
     * @param defaultSessionName
     * @param defaultSessionProfile
     * @param collaborativeSessions listado de sesiones
     * @param retrievalManager
     * @param dbController
     */
    public SearcherServer(String defaultSessionName, SessionProfile defaultSessionProfile, Map<String, SessionProfile> collaborativeSessions, Map<String, SessionProfile> htTempSessions, RetrievalManager retrievalManager, DataBaseController dbController) {
        super(defaultSessionName, defaultSessionProfile, collaborativeSessions, htTempSessions, dbController);
        this.retrievalManager = retrievalManager;
    }

    /**
     * Constructor de la clase
     *
     * @param defaultSessionName
     * @param defaultSessionProfile
     * @param collaborativeSessions listado de sesiones
     * @param retrievalManager
     * @param listener             oyente de la aplicación servidora
     * @param dbController
     */
    public SearcherServer(String defaultSessionName, SessionProfile defaultSessionProfile, Map<String, SessionProfile> collaborativeSessions, Map<String, SessionProfile> htTempSessions, RetrievalManager retrievalManager, FacadeListener listener, DataBaseController dbController) {
        super(defaultSessionName, defaultSessionProfile, collaborativeSessions, htTempSessions, listener, dbController);
        this.retrievalManager = retrievalManager;
    }

    /**
     * {@inheritDoc}
     */
    public void executeSearch(String sessionName, String query, int searcher, int principle, boolean caseSensitive, Seeker seeker, ClientSidePrx seekerPrx) throws SessionException, SeekerException, SearchableException, SearchException, IllegalArgumentException, IOException {
        SessionProfile recordSession = this.getSessionProfile(sessionName);
        SeekerInfo temp = recordSession.getSeekerInfo();

        if (temp.record.containsKey(seeker)) {
            switch (principle) {

                case SearchPrinciple.SINGLE_SEARCH:
                    ResultSetMetaData resultsList = retrievalManager.getSearcherManager().search(query, searcher, principle, caseSensitive);
                    if (resultsList.isEmpty()) {
                        this.updateRecordSession(query, resultsList, 0, seeker, recordSession);
                        this.notifySearchResults(INDIVIDUAL_SEARCH, true, null, seeker, seekerPrx);
                    } else {
                        int documentsCount = resultsList.getDocumentsCount();
                        this.updateRecordSession(query, resultsList, documentsCount, seeker, recordSession);
                        this.notifySearchResults(INDIVIDUAL_SEARCH, false, resultsList, seeker, seekerPrx);
                        updateResultsStore(resultsList);
                        this.saveSearchDB(sessionName, seeker, resultsList, "Single Search");

                    }


                    break;
                case SearchPrinciple.SINGLE_SEARCH_AND_SPLIT:
                    int role = seeker.getRole();
                    if (role == Seeker.ROLE_CHAIRMAN) {
                        int membersNumber = temp.record.size();
                        List<ResultSetMetaData> searchResults = retrievalManager.getSearcherManager().search(searcher, principle, query, caseSensitive, membersNumber);

                        if (searchResults.isEmpty()) {
                            this.notifySearchResults(INDIVIDUAL_SEARCH, true, null, seeker, seekerPrx);
                            this.updateRecordSession(query, new ResultSetMetaData(query), 0, seeker, recordSession);
                        } else {
                            notifySearchResults(query, searchResults, seeker, recordSession, "Single Search and Split");
                            updateResultsStore(searchResults);
                        }
                        break;
                    } else {
                        throw new SeekerException("Doesn't have privileges to executing this search");
                    }
                default:
                    throw new IllegalArgumentException("Search principle is not supported.");
            }
        } else {
            throw new SeekerException("The seeker '" + seeker.getUser() + "' is not registered in the session '" + sessionName + "'.");
        }

    }

    /**
     * {@inheritDoc}
     */
    public void executeSearch(String sessionName, String query, int field, int searcher, int principle, boolean caseSensitive, Seeker seeker, ClientSidePrx seekerPrx) throws SessionException, SeekerException, SearchableException, SearchException, IllegalArgumentException, IOException {
        SessionProfile recordSession = this.getSessionProfile(sessionName);
        SeekerInfo temp = recordSession.getSeekerInfo();
        if (temp.record.containsKey(seeker)) {
            switch (principle) {
                case SearchPrinciple.SINGLE_SEARCH:
                    ResultSetMetaData resultsList = retrievalManager.getSearcherManager().search(query, field, searcher, principle, caseSensitive);

                    if (resultsList.isEmpty()) {
                        this.updateRecordSession(query, resultsList, 0, seeker, recordSession);
                        this.notifySearchResults(INDIVIDUAL_SEARCH, true, null, seeker, seekerPrx);
                    } else {
                        int documentsCount = resultsList.getDocumentsCount();
                        this.updateRecordSession(query, resultsList, documentsCount, seeker, recordSession);
                        this.notifySearchResults(INDIVIDUAL_SEARCH, false, resultsList, seeker, seekerPrx);
                        updateResultsStore(resultsList);
                        this.saveSearchDB(sessionName, seeker, resultsList, "Single Search");
                    }
                    break;
                case SearchPrinciple.SINGLE_SEARCH_AND_SPLIT:
                    int role = seeker.getRole();
                    if (role == Seeker.ROLE_CHAIRMAN) {
                        int membersNumber = temp.record.size();
                        List<ResultSetMetaData> searchResults = retrievalManager.getSearcherManager().search(searcher, principle, query, field, caseSensitive, membersNumber);

                        if (searchResults.isEmpty()) {
                            this.updateRecordSession(query, new ResultSetMetaData(query), 0, seeker, recordSession);
                            this.notifySearchResults(INDIVIDUAL_SEARCH, true, null, seeker, seekerPrx);
                        } else {
                            notifySearchResults(query, searchResults, seeker, recordSession, "Single Search and Split");
                            updateResultsStore(searchResults);
                        }
                        break;
                    } else {
                        throw new SeekerException("Doesn't have privileges to executing this search");
                    }
                default:
                    throw new IllegalArgumentException("Division of labor principle is not supported.");
            }
        } else {
            throw new SeekerException("The seeker '" + seeker.getUser() + "' is not registered in the session '" + sessionName + "'.");
        }
    }

    /**
     * {@inheritDoc}
     */
    public void executeSearch(String sessionName, String query, int[] fields, int searcher, int principle, boolean caseSensitive, Seeker seeker, ClientSidePrx seekerPrx) throws SessionException, SeekerException, SearchableException, SearchException, IllegalArgumentException, IOException {
        SessionProfile recordSession = this.getSessionProfile(sessionName);
        SeekerInfo temp = recordSession.getSeekerInfo();
        if (temp.record.containsKey(seeker)) {
            switch (principle) {
                case SearchPrinciple.SINGLE_SEARCH:
                    ResultSetMetaData resultsList = retrievalManager.getSearcherManager().search(query, fields, searcher, principle, caseSensitive);
                    if (resultsList.isEmpty()) {
                        this.updateRecordSession(query, resultsList, 0, seeker, recordSession);
                        this.notifySearchResults(INDIVIDUAL_SEARCH, true, null, seeker, seekerPrx);
                    } else {
                        int documentsCount = resultsList.getDocumentsCount();
                        this.updateRecordSession(query, resultsList, documentsCount, seeker, recordSession);
                        this.notifySearchResults(INDIVIDUAL_SEARCH, false, resultsList, seeker, seekerPrx);
                        updateResultsStore(resultsList);
                        this.saveSearchDB(sessionName, seeker, resultsList, "Single Search");
                    }

                    break;

                case SearchPrinciple.SINGLE_SEARCH_AND_SPLIT:
                    int role = seeker.getRole();
                    if (role == Seeker.ROLE_CHAIRMAN) {
                        int membersNumber = temp.record.size();
                        List<ResultSetMetaData> searchResults = retrievalManager.getSearcherManager().search(searcher, principle, query, fields, caseSensitive, membersNumber);

                        if (searchResults.isEmpty()) {
                            this.updateRecordSession(query, new ResultSetMetaData(query), 0, seeker, recordSession);
                            this.notifySearchResults(INDIVIDUAL_SEARCH, true, null, seeker, seekerPrx);
                        } else {
                            notifySearchResults(query, searchResults, seeker, recordSession, "Single Search and Split");
                            updateResultsStore(searchResults);
                        }
                        break;
                    } else {
                        throw new SeekerException("Doesn't have privileges to executing this search");
                    }
                default:
                    throw new IllegalArgumentException("Division of labor principle is not supported.");
            }
        } else {
            throw new SeekerException("The seeker '" + seeker.getUser() + "' is not registered in the session '" + sessionName + "'.");
        }
    }

    /**
     * {@inheritDoc}
     */
    public void executeSearch(String sessionName, String query, String docType, int searcher, int principle, boolean caseSensitive, Seeker seeker, ClientSidePrx seekerPrx) throws SessionException, SeekerException, SearchableException, SearchException, IllegalArgumentException, IOException {

        SessionProfile recordSession = this.getSessionProfile(sessionName);
        SeekerInfo temp = recordSession.getSeekerInfo();
        this.validate(sessionName, seeker, temp);

        switch (principle) {

            case SearchPrinciple.SINGLE_SEARCH:
                ResultSetMetaData resultsList = retrievalManager.getSearcherManager().search(query, docType, searcher, principle, caseSensitive);

                if (resultsList.isEmpty()) {
                    this.updateRecordSession(query, resultsList, 0, seeker, recordSession);
                    this.notifySearchResults(INDIVIDUAL_SEARCH, true, null, seeker, seekerPrx);
                } else {
                    int documentsCount = resultsList.getDocumentsCount();
                    this.updateRecordSession(query, resultsList, documentsCount, seeker, recordSession);
                    this.notifySearchResults(INDIVIDUAL_SEARCH, false, resultsList, seeker, seekerPrx);
                    updateResultsStore(resultsList);
                    this.saveSearchDB(sessionName, seeker, resultsList, "Single Search");
                }

                break;

            case SearchPrinciple.SINGLE_SEARCH_AND_SPLIT:
                int role = seeker.getRole();
                if (role == Seeker.ROLE_CHAIRMAN) {
                    int membersNumber = temp.record.size();
                    List<ResultSetMetaData> searchResults = retrievalManager.getSearcherManager().search(searcher, principle, query, docType, caseSensitive, membersNumber);

                    if (searchResults.isEmpty()) {
                        this.updateRecordSession(query, new ResultSetMetaData(query), 0, seeker, recordSession);
                        this.notifySearchResults(INDIVIDUAL_SEARCH, true, null, seeker, seekerPrx);
                    } else {
                        notifySearchResults(query, searchResults, seeker, recordSession, "Single Search and Split");
                        updateResultsStore(searchResults);
                    }
                    break;
                } else {
                    throw new SeekerException("Doesn't have privileges to executing this search");
                }
            default:
                throw new IllegalArgumentException("Division of labor principle is not supported.");
        }
    }

    /**
     * {@inheritDoc}
     */
    public void executeSearch(String sessionName, String query, String[] docTypes, int searcher, int principle, boolean caseSensitive, Seeker seeker, ClientSidePrx seekerPrx) throws SessionException, SeekerException, SearchableException, SearchException, IllegalArgumentException, IOException {

        SessionProfile recordSession = this.getSessionProfile(sessionName);
        SeekerInfo temp = recordSession.getSeekerInfo();
        this.validate(sessionName, seeker, temp);

        switch (principle) {

            case SearchPrinciple.SINGLE_SEARCH:
                ResultSetMetaData resultsList = retrievalManager.getSearcherManager().search(query, docTypes, searcher, principle, caseSensitive);

                if (resultsList.isEmpty()) {
                    this.updateRecordSession(query, resultsList, 0, seeker, recordSession);
                    this.notifySearchResults(INDIVIDUAL_SEARCH, true, null, seeker, seekerPrx);
                } else {
                    int documentsCount = resultsList.getDocumentsCount();
                    this.updateRecordSession(query, resultsList, documentsCount, seeker, recordSession);
                    this.notifySearchResults(INDIVIDUAL_SEARCH, false, resultsList, seeker, seekerPrx);
                    updateResultsStore(resultsList);
                    this.saveSearchDB(sessionName, seeker, resultsList, "Single Search");
                }

                break;

            case SearchPrinciple.SINGLE_SEARCH_AND_SPLIT:
                int role = seeker.getRole();
                if (role == Seeker.ROLE_CHAIRMAN) {
                    int membersNumber = temp.record.size();
                    List<ResultSetMetaData> searchResults = retrievalManager.getSearcherManager().search(searcher, principle, query, docTypes, caseSensitive, membersNumber);

                    if (searchResults.isEmpty()) {
                        // TODO  ver esto, por que
                        this.updateRecordSession(query, new ResultSetMetaData(query), 0, seeker, recordSession);
                        this.notifySearchResults(INDIVIDUAL_SEARCH, true, null, seeker, seekerPrx);
                    } else {
                        notifySearchResults(query, searchResults, seeker, recordSession, "Single Search and Split");
                        updateResultsStore(searchResults);

                    }
                    break;
                } else {
                    throw new SeekerException("Doesn't have privileges to executing this search");
                }
            default:
                throw new IllegalArgumentException("Division of labor principle is not supported.");
        }
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("fallthrough")
    public void executeSearch(String sessionName, String query, String docType, int field, int searcher, int principle, boolean caseSensitive, Seeker seeker, ClientSidePrx seekerPrx) throws SessionException, SeekerException, SearchableException, SearchException, IllegalArgumentException, IOException {

        SessionProfile recordSession = this.getSessionProfile(sessionName);
        SeekerInfo temp = recordSession.getSeekerInfo();
        this.validate(sessionName, seeker, temp);
        switch (principle) {

            case SearchPrinciple.SINGLE_SEARCH:
                ResultSetMetaData resultsList = retrievalManager.getSearcherManager().search(query, docType, field, searcher, principle, caseSensitive);

                if (resultsList.isEmpty()) {
                    this.updateRecordSession(query, resultsList, 0, seeker, recordSession);
                    this.notifySearchResults(INDIVIDUAL_SEARCH, true, null, seeker, seekerPrx);
                } else {
                    int documentsCount = resultsList.getDocumentsCount();
                    this.updateRecordSession(query, resultsList, documentsCount, seeker, recordSession);
                    this.notifySearchResults(INDIVIDUAL_SEARCH, false, resultsList, seeker, seekerPrx);
                    updateResultsStore(resultsList);
                    this.saveSearchDB(sessionName, seeker, resultsList, "Single Search");
                }

                break;

            case SearchPrinciple.SINGLE_SEARCH_AND_SPLIT:
                int role = seeker.getRole();
                if (role == Seeker.ROLE_CHAIRMAN) {
                    int membersNumber = temp.record.size();
                    List<ResultSetMetaData> searchResults = retrievalManager.getSearcherManager().search(searcher, principle, query, docType, field, caseSensitive, membersNumber);

                    if (searchResults.isEmpty()) {
                        this.updateRecordSession(query, new ResultSetMetaData(query), 0, seeker, recordSession);
                        this.notifySearchResults(INDIVIDUAL_SEARCH, true, null, seeker, seekerPrx);
                    } else {
                        notifySearchResults(query, searchResults, seeker, recordSession, "Single Search and Split");
                        updateResultsStore(searchResults);
                    }

                    break;
                } else {
                    throw new SeekerException("Doesn't have privileges to executing this search");
                }

            default:
                throw new IllegalArgumentException("Division of labor principle is not supported.");
        }
    }

    /**
     * {@inheritDoc}
     */
    public void executeSearch(String sessionName, String query, String docType, int[] fields, int searcher, int principle, boolean caseSensitive, Seeker seeker, ClientSidePrx seekerPrx) throws SessionException, SeekerException, SearchableException, SearchException, IllegalArgumentException, IOException {

        SessionProfile recordSession = this.getSessionProfile(sessionName);
        SeekerInfo temp = recordSession.getSeekerInfo();
        this.validate(sessionName, seeker, temp);
        switch (principle) {

            case SearchPrinciple.SINGLE_SEARCH:
                ResultSetMetaData resultsList = retrievalManager.getSearcherManager().search(query, docType, fields, searcher, principle, caseSensitive);

                if (resultsList.isEmpty()) {
                    this.updateRecordSession(query, resultsList, 0, seeker, recordSession);
                    this.notifySearchResults(INDIVIDUAL_SEARCH, true, null, seeker, seekerPrx);
                } else {
                    int documentsCount = resultsList.getDocumentsCount();
                    this.updateRecordSession(query, resultsList, documentsCount, seeker, recordSession);
                    this.notifySearchResults(INDIVIDUAL_SEARCH, false, resultsList, seeker, seekerPrx);
                    updateResultsStore(resultsList);
                    this.saveSearchDB(sessionName, seeker, resultsList, "Single Search");
                }

                break;

            case SearchPrinciple.SINGLE_SEARCH_AND_SPLIT:
                int role = seeker.getRole();
                if (role == Seeker.ROLE_CHAIRMAN) {
                    int membersNumber = temp.record.size();
                    List<ResultSetMetaData> searchResults = retrievalManager.getSearcherManager().search(searcher, principle, query, docType, fields, caseSensitive, membersNumber);

                    if (searchResults.isEmpty()) {
                        this.updateRecordSession(query, new ResultSetMetaData(query), 0, seeker, recordSession);
                        this.notifySearchResults(INDIVIDUAL_SEARCH, true, null, seeker, seekerPrx);
                    } else {
                        notifySearchResults(query, searchResults, seeker, recordSession, "Single Search and Split");
                        updateResultsStore(searchResults);
                    }

                    break;
                } else {
                    throw new SeekerException("Doesn't have privileges to executing this search");
                }

            default:
                throw new IllegalArgumentException("Division of labor principle is not supported.");
        }
    }

    /**
     * {@inheritDoc}
     */
    public void executeSearch(String sessionName, String query, String[] docTypes, int field, int searcher, int principle, boolean caseSensitive, Seeker seeker, ClientSidePrx seekerPrx) throws SessionException, SeekerException, SearchableException, SearchException, IllegalArgumentException, IOException {

        SessionProfile recordSession = this.getSessionProfile(sessionName);
        SeekerInfo temp = recordSession.getSeekerInfo();
        this.validate(sessionName, seeker, temp);
        switch (principle) {

            case SearchPrinciple.SINGLE_SEARCH:
                ResultSetMetaData resultsList = retrievalManager.getSearcherManager().search(query, docTypes, field, searcher, principle, caseSensitive);

                if (resultsList.isEmpty()) {
                    this.updateRecordSession(query, resultsList, 0, seeker, recordSession);
                    this.notifySearchResults(INDIVIDUAL_SEARCH, true, null, seeker, seekerPrx);
                } else {
                    int documentsCount = resultsList.getDocumentsCount();
                    this.updateRecordSession(query, resultsList, documentsCount, seeker, recordSession);
                    this.notifySearchResults(INDIVIDUAL_SEARCH, false, resultsList, seeker, seekerPrx);
                    updateResultsStore(resultsList);
                    this.saveSearchDB(sessionName, seeker, resultsList, "Single Search");
                }

                break;

            case SearchPrinciple.SINGLE_SEARCH_AND_SPLIT:
                int role = seeker.getRole();
                if (role == Seeker.ROLE_CHAIRMAN) {
                    int membersNumber = temp.record.size();
                    List<ResultSetMetaData> searchResults = retrievalManager.getSearcherManager().search(searcher, principle, query, docTypes, field, caseSensitive, membersNumber);

                    if (searchResults.isEmpty()) {
                        this.updateRecordSession(query, new ResultSetMetaData(query), 0, seeker, recordSession);
                        this.notifySearchResults(INDIVIDUAL_SEARCH, true, null, seeker, seekerPrx);
                    } else {
                        notifySearchResults(query, searchResults, seeker, recordSession, "Single Search and Split");
                        updateResultsStore(searchResults);
                    }
                    break;
                } else {
                    throw new SeekerException("Doesn't have privileges to executing this search");
                }
            default:
                throw new IllegalArgumentException("Division of labor principle is not supported.");
        }
    }

    /**
     * {@inheritDoc}
     */
    public void executeSearch(String sessionName, String query, String[] docTypes, int[] fields, int searcher, int principle, boolean caseSensitive, Seeker seeker, ClientSidePrx seekerPrx) throws SessionException, SeekerException, SearchableException, SearchException, IllegalArgumentException, IOException {

        SessionProfile recordSession = this.getSessionProfile(sessionName);
        SeekerInfo temp = recordSession.getSeekerInfo();
        this.validate(sessionName, seeker, temp);
        switch (principle) {

            case SearchPrinciple.SINGLE_SEARCH:
                ResultSetMetaData resultsList = retrievalManager.getSearcherManager().search(query, docTypes, fields, searcher, principle, caseSensitive);

                if (resultsList.isEmpty()) {
                    this.updateRecordSession(query, resultsList, 0, seeker, recordSession);
                    this.notifySearchResults(INDIVIDUAL_SEARCH, true, null, seeker, seekerPrx);
                } else {
                    int documentsCount = resultsList.getDocumentsCount();
                    this.updateRecordSession(query, resultsList, documentsCount, seeker, recordSession);
                    this.notifySearchResults(INDIVIDUAL_SEARCH, false, resultsList, seeker, seekerPrx);
                    updateResultsStore(resultsList);
                    this.saveSearchDB(sessionName, seeker, resultsList, "Single Search");
                }

                break;

            case SearchPrinciple.SINGLE_SEARCH_AND_SPLIT:
                int role = seeker.getRole();
                if (role == Seeker.ROLE_CHAIRMAN) {
                    int membersNumber = temp.record.size();
                    List<ResultSetMetaData> searchResults = retrievalManager.getSearcherManager().search(searcher, principle, query, docTypes, fields, caseSensitive, membersNumber);

                    if (searchResults.isEmpty()) {
                        this.updateRecordSession(query, new ResultSetMetaData(query), 0, seeker, recordSession);
                        this.notifySearchResults(INDIVIDUAL_SEARCH, true, null, seeker, seekerPrx);
                    } else {
                        notifySearchResults(query, searchResults, seeker, recordSession, "Single Search and Split");
                        updateResultsStore(searchResults);

                    }

                    break;
                } else {
                    throw new SeekerException("Doesn't have privileges to executing this search");
                }

            default:
                throw new IllegalArgumentException("Division of labor principle is not supported.");
        }

    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * {@inheritDoc}
     */
    public void executeSearch(String sessionName, int principle, String query, boolean caseSensitive, Seeker seeker, ClientSidePrx seekerPrx) throws SessionException, SeekerException, SearchException, IllegalArgumentException, IOException {

        SessionProfile recordSession = this.getSessionProfile(sessionName);
        SeekerInfo temp = recordSession.getSeekerInfo();
        this.validate(sessionName, seeker, temp);
        ResultSetMetaData resultsList;
        try {
            switch (principle) {

                case SearchPrinciple.META_SEARCH:
                    resultsList = retrievalManager.getSearcherManager().search(query, MULTIPLE_SEARCHERS, principle, caseSensitive);

                    if (resultsList.isEmpty()) {
                        this.updateRecordSession(query, resultsList, 0, seeker, recordSession);
                        this.notifySearchResults(INDIVIDUAL_SEARCH, true, null, seeker, seekerPrx);
                    } else {
                        int documentsCount = resultsList.getDocumentsCount();
                        this.updateRecordSession(query, resultsList, documentsCount, seeker, recordSession);
                        this.notifySearchResults(INDIVIDUAL_SEARCH, false, resultsList, seeker, seekerPrx);
                        updateResultsStore(resultsList);
                        this.saveSearchDB(sessionName, seeker, resultsList, "Meta Search");
                    }

                    break;

                case SearchPrinciple.MULTI_SEARCH:

                    resultsList = retrievalManager.getSearcherManager().search(query, MULTIPLE_SEARCHERS, principle, caseSensitive);

                    if (resultsList.isEmpty()) {
                        this.updateRecordSession(query, resultsList, 0, seeker, recordSession);
                        this.notifySearchResults(INDIVIDUAL_SEARCH, true, null, seeker, seekerPrx);
                    } else {
                        int documentsCount = resultsList.getDocumentsCount();
                        this.updateRecordSession(query, resultsList, documentsCount, seeker, recordSession);
                        this.notifySearchResults(INDIVIDUAL_SEARCH, false, resultsList, seeker, seekerPrx);
                        updateResultsStore(resultsList);
                        this.saveSearchDB(sessionName, seeker, resultsList, "Multi Search");
                    }

                    break;

                case SearchPrinciple.META_SEARCH_AND_SPLIT:

                    int role = seeker.getRole();
                    if (role == Seeker.ROLE_CHAIRMAN) {
                        int membersNumber = temp.record.size();
                        List<ResultSetMetaData> searchResults = retrievalManager.getSearcherManager().search(MULTIPLE_SEARCHERS, principle, query, caseSensitive, membersNumber);

                        if (searchResults.isEmpty()) {
                            this.updateRecordSession(query, new ResultSetMetaData(query), 0, seeker, recordSession);
                            this.notifySearchResults(INDIVIDUAL_SEARCH, true, null, seeker, seekerPrx);
                        } else {
                            notifySearchResults(query, searchResults, seeker, recordSession, "Meta Search and Split");
                            updateResultsStore(searchResults);
                        }

                        break;
                    } else {
                        throw new SeekerException("Doesn't have privileges to executing this search");
                    }

                case SearchPrinciple.MULTI_SEARCH_AND_SWITCH:
                    int roleM = seeker.getRole();
                    if (roleM == Seeker.ROLE_CHAIRMAN) {
                        int membersNumber = temp.record.size();
                        List<ResultSetMetaData> searchResults = retrievalManager.getSearcherManager().search(MULTIPLE_SEARCHERS, principle, query, caseSensitive, membersNumber);

                        if (searchResults.isEmpty()) {
                            this.updateRecordSession(query, new ResultSetMetaData(query), 0, seeker, recordSession);
                            this.notifySearchResults(INDIVIDUAL_SEARCH, true, null, seeker, seekerPrx);
                        } else {
                            notifySearchResults(query, searchResults, seeker, recordSession, "Multi Search and Switch");
                            updateResultsStore(searchResults);
                        }
                        break;
                    } else {
                        throw new SeekerException("Doesn't have privileges to executing this search");
                    }

                default:
                    throw new IllegalArgumentException("Division of labor principle is not supported.");
            }

        } catch (SearchableException ex) {
            OutputMonitor.printStream("Searchable", ex);
        }

    }

    /**
     * {@inheritDoc}
     *
     */
    public void executeSearch(String sessionName, int principle, String query, int field, boolean caseSensitive, Seeker seeker, ClientSidePrx seekerPrx) throws SessionException, SeekerException, SearchException, IllegalArgumentException, IOException {

        SessionProfile recordSession = this.getSessionProfile(sessionName);
        SeekerInfo temp = recordSession.getSeekerInfo();
        this.validate(sessionName, seeker, temp);

        ResultSetMetaData resultsList;

        try {
            switch (principle) {

                case SearchPrinciple.META_SEARCH:
                    resultsList = retrievalManager.getSearcherManager().search(query, field, MULTIPLE_SEARCHERS, principle, caseSensitive);

                    if (resultsList.isEmpty()) {
                        this.updateRecordSession(query, resultsList, 0, seeker, recordSession);
                        this.notifySearchResults(INDIVIDUAL_SEARCH, true, null, seeker, seekerPrx);
                    } else {
                        int documentsCount = resultsList.getDocumentsCount();
                        this.updateRecordSession(query, resultsList, documentsCount, seeker, recordSession);
                        this.notifySearchResults(INDIVIDUAL_SEARCH, false, resultsList, seeker, seekerPrx);
                        updateResultsStore(resultsList);
                        this.saveSearchDB(sessionName, seeker, resultsList, "Meta Search");

                    }

                    break;

                case SearchPrinciple.MULTI_SEARCH:
                    resultsList = retrievalManager.getSearcherManager().search(query, field, MULTIPLE_SEARCHERS, principle, caseSensitive);

                    if (resultsList.isEmpty()) {
                        this.updateRecordSession(query, resultsList, 0, seeker, recordSession);
                        this.notifySearchResults(INDIVIDUAL_SEARCH, true, null, seeker, seekerPrx);
                    } else {
                        int documentsCount = resultsList.getDocumentsCount();
                        this.updateRecordSession(query, resultsList, documentsCount, seeker, recordSession);
                        this.notifySearchResults(INDIVIDUAL_SEARCH, false, resultsList, seeker, seekerPrx);
                        updateResultsStore(resultsList);
                        this.saveSearchDB(sessionName, seeker, resultsList, "Multi Search");

                    }

                    break;

                case SearchPrinciple.META_SEARCH_AND_SPLIT:
                    int role = seeker.getRole();
                    if (role == Seeker.ROLE_CHAIRMAN) {
                        int membersNumber = temp.record.size();
                        List<ResultSetMetaData> searchResults = retrievalManager.getSearcherManager().search(MULTIPLE_SEARCHERS, principle, query, field, caseSensitive, membersNumber);

                        if (searchResults.isEmpty()) {
                            this.updateRecordSession(query, new ResultSetMetaData(query), 0, seeker, recordSession);
                            this.notifySearchResults(INDIVIDUAL_SEARCH, true, null, seeker, seekerPrx);
                        } else {
                            notifySearchResults(query, searchResults, seeker, recordSession, "Meta Search and Split");
                            updateResultsStore(searchResults);
                        }

                        break;
                    } else {
                        throw new SeekerException("Doesn't have privileges to executing this search");
                    }

                case SearchPrinciple.MULTI_SEARCH_AND_SWITCH:
                    int roleM = seeker.getRole();
                    if (roleM == Seeker.ROLE_CHAIRMAN) {
                        int membersNumber = temp.record.size();
                        List<ResultSetMetaData> searchResults = retrievalManager.getSearcherManager().search(MULTIPLE_SEARCHERS, principle, query, field, caseSensitive, membersNumber);

                        if (searchResults.isEmpty()) {
                            this.updateRecordSession(query, new ResultSetMetaData(query), 0, seeker, recordSession);
                            this.notifySearchResults(INDIVIDUAL_SEARCH, true, null, seeker, seekerPrx);
                        } else {
                            notifySearchResults(query, searchResults, seeker, recordSession, "Multi Search and Switch");
                            updateResultsStore(searchResults);

                        }
                        break;
                    } else {
                        throw new SeekerException("Doesn't have privileges to executing this search");
                    }
                default:
                    throw new IllegalArgumentException("Division of labor principle is not supported.");
            }
        } catch (SearchableException ex) {
            OutputMonitor.printStream("Searchable", ex);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void executeSearch(String sessionName, int principle, String query, int[] fields, boolean caseSensitive, Seeker seeker, ClientSidePrx seekerPrx) throws SessionException, SeekerException, SearchException, IllegalArgumentException, IOException {

        SessionProfile recordSession = this.getSessionProfile(sessionName);
        SeekerInfo temp = recordSession.getSeekerInfo();
        this.validate(sessionName, seeker, temp);
        ResultSetMetaData resultsList;

        try {
            switch (principle) {
                case SearchPrinciple.META_SEARCH:
                    resultsList = retrievalManager.getSearcherManager().search(query, fields, MULTIPLE_SEARCHERS, principle, caseSensitive);
                    if (resultsList.isEmpty()) {
                        this.updateRecordSession(query, resultsList, 0, seeker, recordSession);
                        this.notifySearchResults(INDIVIDUAL_SEARCH, true, null, seeker, seekerPrx);
                    } else {
                        int documentsCount = resultsList.getDocumentsCount();
                        this.updateRecordSession(query, resultsList, documentsCount, seeker, recordSession);
                        this.notifySearchResults(INDIVIDUAL_SEARCH, false, resultsList, seeker, seekerPrx);
                        updateResultsStore(resultsList);
                        this.saveSearchDB(sessionName, seeker, resultsList, "Meta Search");
                    }
                    break;
                case SearchPrinciple.MULTI_SEARCH:
                    resultsList = retrievalManager.getSearcherManager().search(query, fields, MULTIPLE_SEARCHERS, principle, caseSensitive);
                    if (resultsList.isEmpty()) {
                        this.updateRecordSession(query, resultsList, 0, seeker, recordSession);
                        this.notifySearchResults(INDIVIDUAL_SEARCH, true, null, seeker, seekerPrx);
                    } else {
                        int documentsCount = resultsList.getDocumentsCount();
                        this.updateRecordSession(query, resultsList, documentsCount, seeker, recordSession);
                        this.notifySearchResults(INDIVIDUAL_SEARCH, false, resultsList, seeker, seekerPrx);
                        updateResultsStore(resultsList);
                        this.saveSearchDB(sessionName, seeker, resultsList, "Multi Search");

                    }
                    break;
                case SearchPrinciple.META_SEARCH_AND_SPLIT:
                    int role = seeker.getRole();
                    if (role == Seeker.ROLE_CHAIRMAN) {
                        int membersNumber = temp.record.size();
                        List<ResultSetMetaData> searchResults = retrievalManager.getSearcherManager().search(MULTIPLE_SEARCHERS, principle, query, fields, caseSensitive, membersNumber);
                        if (searchResults.isEmpty()) {
                            this.updateRecordSession(query, new ResultSetMetaData(query), 0, seeker, recordSession);
                            this.notifySearchResults(INDIVIDUAL_SEARCH, true, null, seeker, seekerPrx);
                        } else {
                            notifySearchResults(query, searchResults, seeker, recordSession, "Meta Search and Split");
                            updateResultsStore(searchResults);

                        }
                        break;
                    } else {
                        throw new SeekerException("Doesn't have privileges to executing this search");
                    }
                case SearchPrinciple.MULTI_SEARCH_AND_SWITCH:
                    int roleM = seeker.getRole();
                    if (roleM == Seeker.ROLE_CHAIRMAN) {
                        int membersNumber = temp.record.size();
                        List<ResultSetMetaData> searchResults = retrievalManager.getSearcherManager().search(MULTIPLE_SEARCHERS, principle, query, fields, caseSensitive, membersNumber);
                        if (searchResults.isEmpty()) {
                            this.updateRecordSession(query, new ResultSetMetaData(query), 0, seeker, recordSession);
                            this.notifySearchResults(INDIVIDUAL_SEARCH, true, null, seeker, seekerPrx);
                        } else {
                            notifySearchResults(query, searchResults, seeker, recordSession, "Multi Search and Switch");
                            updateResultsStore(searchResults);

                        }
                        break;
                    } else {
                        throw new SeekerException("Doesn't have privileges to executing this search");
                    }
                default:
                    throw new IllegalArgumentException("Division of labor principle is not supported.");
            }
        } catch (SearchableException ex) {
            OutputMonitor.printStream("Searchable", ex);
        }

    }

    /**
     * {@inheritDoc}
     */
    public void executeSearch(String sessionName, int principle, String query, String docType, boolean caseSensitive, Seeker seeker, ClientSidePrx seekerPrx) throws SessionException, SeekerException, SearchException, IllegalArgumentException, IOException {

        SessionProfile recordSession = this.getSessionProfile(sessionName);
        SeekerInfo temp = recordSession.getSeekerInfo();
        this.validate(sessionName, seeker, temp);
        ResultSetMetaData resultsList;

        try {
            switch (principle) {

                case SearchPrinciple.META_SEARCH:
                    resultsList = retrievalManager.getSearcherManager().search(query, docType, MULTIPLE_SEARCHERS, principle, caseSensitive);

                    if (resultsList.isEmpty()) {
                        this.updateRecordSession(query, resultsList, 0, seeker, recordSession);
                        this.notifySearchResults(INDIVIDUAL_SEARCH, true, null, seeker, seekerPrx);
                    } else {
                        int documentsCount = resultsList.getDocumentsCount();
                        this.updateRecordSession(query, resultsList, documentsCount, seeker, recordSession);
                        this.notifySearchResults(INDIVIDUAL_SEARCH, false, resultsList, seeker, seekerPrx);
                        updateResultsStore(resultsList);
                        this.saveSearchDB(sessionName, seeker, resultsList, "Meta Search");
                    }

                    break;

                case SearchPrinciple.MULTI_SEARCH:
                    resultsList = retrievalManager.getSearcherManager().search(query, docType, MULTIPLE_SEARCHERS, principle, caseSensitive);

                    if (resultsList.isEmpty()) {
                        this.updateRecordSession(query, resultsList, 0, seeker, recordSession);
                        this.notifySearchResults(INDIVIDUAL_SEARCH, true, null, seeker, seekerPrx);
                    } else {
                        int documentsCount = resultsList.getDocumentsCount();
                        this.updateRecordSession(query, resultsList, documentsCount, seeker, recordSession);
                        this.notifySearchResults(INDIVIDUAL_SEARCH, false, resultsList, seeker, seekerPrx);
                        updateResultsStore(resultsList);
                        this.saveSearchDB(sessionName, seeker, resultsList, "Multi Search");
                    }

                    break;

                case SearchPrinciple.META_SEARCH_AND_SPLIT:
                    int role = seeker.getRole();
                    if (role == Seeker.ROLE_CHAIRMAN) {
                        int membersNumber = temp.record.size();
                        List<ResultSetMetaData> searchResults = retrievalManager.getSearcherManager().search(MULTIPLE_SEARCHERS, principle, query, docType, caseSensitive, membersNumber);

                        if (searchResults.isEmpty()) {
                            this.updateRecordSession(query, new ResultSetMetaData(query), 0, seeker, recordSession);
                            this.notifySearchResults(INDIVIDUAL_SEARCH, true, null, seeker, seekerPrx);
                        } else {
                            notifySearchResults(query, searchResults, seeker, recordSession, "Meta Search and Split");
                            updateResultsStore(searchResults);

                        }

                        break;
                    } else {
                        throw new SeekerException("Doesn't have privileges to executing this search");
                    }

                case SearchPrinciple.MULTI_SEARCH_AND_SWITCH:
                    int roleM = seeker.getRole();
                    if (roleM == Seeker.ROLE_CHAIRMAN) {
                        int membersNumber = temp.record.size();
                        List<ResultSetMetaData> searchResults = retrievalManager.getSearcherManager().search(MULTIPLE_SEARCHERS, principle, query, docType, caseSensitive, membersNumber);

                        if (searchResults.isEmpty()) {
                            this.updateRecordSession(query, new ResultSetMetaData(query), 0, seeker, recordSession);
                            this.notifySearchResults(INDIVIDUAL_SEARCH, true, null, seeker, seekerPrx);
                        } else {
                            notifySearchResults(query, searchResults, seeker, recordSession, "Multi Search and Switch");
                            updateResultsStore(searchResults);
                        }

                        break;
                    } else {
                        throw new SeekerException("Doesn't have privileges to executing this search");
                    }

                default:
                    throw new IllegalArgumentException("Division of labor principle is not supported.");
            }

        } catch (SearchableException ex) {
            OutputMonitor.printStream("Searchable", ex);
        }


    }

    /**
     * {@inheritDoc}
     */
    public void executeSearch(String sessionName, int principle, String query, String[] docTypes, boolean caseSensitive, Seeker seeker, ClientSidePrx seekerPrx) throws SessionException, SeekerException, SearchException, IllegalArgumentException, IOException {

        SessionProfile recordSession = this.getSessionProfile(sessionName);
        SeekerInfo temp = recordSession.getSeekerInfo();
        this.validate(sessionName, seeker, temp);
        ResultSetMetaData resultsList;

        try {
            switch (principle) {

                case SearchPrinciple.META_SEARCH:
                    resultsList = retrievalManager.getSearcherManager().search(query, docTypes, MULTIPLE_SEARCHERS, principle, caseSensitive);

                    if (resultsList.isEmpty()) {
                        this.updateRecordSession(query, resultsList, 0, seeker, recordSession);
                        this.notifySearchResults(INDIVIDUAL_SEARCH, true, null, seeker, seekerPrx);
                    } else {
                        int documentsCount = resultsList.getDocumentsCount();
                        this.updateRecordSession(query, resultsList, documentsCount, seeker, recordSession);
                        this.notifySearchResults(INDIVIDUAL_SEARCH, false, resultsList, seeker, seekerPrx);
                        updateResultsStore(resultsList);
                        this.saveSearchDB(sessionName, seeker, resultsList, "Meta Search");
                    }

                    break;

                case SearchPrinciple.MULTI_SEARCH:
                    resultsList = retrievalManager.getSearcherManager().search(query, docTypes, MULTIPLE_SEARCHERS, principle, caseSensitive);

                    if (resultsList.isEmpty()) {
                        this.updateRecordSession(query, resultsList, 0, seeker, recordSession);
                        this.notifySearchResults(INDIVIDUAL_SEARCH, true, null, seeker, seekerPrx);
                    } else {
                        int documentsCount = resultsList.getDocumentsCount();
                        this.updateRecordSession(query, resultsList, documentsCount, seeker, recordSession);
                        this.notifySearchResults(INDIVIDUAL_SEARCH, false, resultsList, seeker, seekerPrx);

                        updateResultsStore(resultsList);
                        this.saveSearchDB(sessionName, seeker, resultsList, "Multi Search");
                    }

                    break;

                case SearchPrinciple.META_SEARCH_AND_SPLIT:
                    int role = seeker.getRole();
                    if (role == Seeker.ROLE_CHAIRMAN) {
                        int membersNumber = temp.record.size();
                        List<ResultSetMetaData> searchResults = retrievalManager.getSearcherManager().search(MULTIPLE_SEARCHERS, principle, query, docTypes, caseSensitive, membersNumber);

                        if (searchResults.isEmpty()) {
                            this.updateRecordSession(query, new ResultSetMetaData(query), 0, seeker, recordSession);
                            this.notifySearchResults(INDIVIDUAL_SEARCH, true, null, seeker, seekerPrx);
                        } else {
                            notifySearchResults(query, searchResults, seeker, recordSession, "Meta Search and Split");
                            updateResultsStore(searchResults);
                        }

                        break;
                    } else {
                        throw new SeekerException("Doesn't have privileges to executing this search");
                    }

                case SearchPrinciple.MULTI_SEARCH_AND_SWITCH:
                    int roleM = seeker.getRole();
                    if (roleM == Seeker.ROLE_CHAIRMAN) {
                        int membersNumber = temp.record.size();
                        List<ResultSetMetaData> searchResults = retrievalManager.getSearcherManager().search(MULTIPLE_SEARCHERS, principle, query, docTypes, caseSensitive, membersNumber);

                        if (searchResults.isEmpty()) {
                            this.updateRecordSession(query, new ResultSetMetaData(query), 0, seeker, recordSession);
                            this.notifySearchResults(INDIVIDUAL_SEARCH, true, null, seeker, seekerPrx);
                        } else {
                            notifySearchResults(query, searchResults, seeker, recordSession, "Multi Search and Switch");
                            updateResultsStore(searchResults);

                        }
                        break;
                    } else {
                        throw new SeekerException("Doesn't have privileges to executing this search");
                    }
                default:
                    throw new IllegalArgumentException("Division of labor principle is not supported.");
            }

        } catch (SearchableException ex) {
            OutputMonitor.printStream("Searchable", ex);
        }

    }

    /**
     * {@inheritDoc}
     */
    public void executeSearch(String sessionName, int principle, String query, String docType, int field, boolean caseSensitive, Seeker seeker, ClientSidePrx seekerPrx) throws SessionException, SeekerException, SearchException, IllegalArgumentException, IOException {

        SessionProfile recordSession = this.getSessionProfile(sessionName);
        SeekerInfo temp = recordSession.getSeekerInfo();
        this.validate(sessionName, seeker, temp);
        ResultSetMetaData resultsList;

        try {
            switch (principle) {

                case SearchPrinciple.META_SEARCH:
                    resultsList = retrievalManager.getSearcherManager().search(query, docType, field, MULTIPLE_SEARCHERS, principle, caseSensitive);

                    if (resultsList.isEmpty()) {
                        this.updateRecordSession(query, resultsList, 0, seeker, recordSession);
                        this.notifySearchResults(INDIVIDUAL_SEARCH, true, null, seeker, seekerPrx);
                    } else {
                        int documentsCount = resultsList.getDocumentsCount();
                        this.updateRecordSession(query, resultsList, documentsCount, seeker, recordSession);
                        this.notifySearchResults(INDIVIDUAL_SEARCH, false, resultsList, seeker, seekerPrx);
                        updateResultsStore(resultsList);
                        this.saveSearchDB(sessionName, seeker, resultsList, "Meta Search");
                    }

                    break;

                case SearchPrinciple.MULTI_SEARCH:
                    resultsList = retrievalManager.getSearcherManager().search(query, docType, field, MULTIPLE_SEARCHERS, principle, caseSensitive);

                    if (resultsList.isEmpty()) {
                        this.updateRecordSession(query, resultsList, 0, seeker, recordSession);
                        this.notifySearchResults(INDIVIDUAL_SEARCH, true, null, seeker, seekerPrx);
                    } else {
                        int documentsCount = resultsList.getDocumentsCount();
                        this.updateRecordSession(query, resultsList, documentsCount, seeker, recordSession);
                        this.notifySearchResults(INDIVIDUAL_SEARCH, false, resultsList, seeker, seekerPrx);
                        updateResultsStore(resultsList);
                        this.saveSearchDB(sessionName, seeker, resultsList, "Multi Search");
                    }

                    break;

                case SearchPrinciple.META_SEARCH_AND_SPLIT:
                    int role = seeker.getRole();
                    if (role == Seeker.ROLE_CHAIRMAN) {
                        int membersNumber = temp.record.size();
                        List<ResultSetMetaData> searchResults = retrievalManager.getSearcherManager().search(MULTIPLE_SEARCHERS, principle, query, docType, field, caseSensitive, membersNumber);

                        if (searchResults.isEmpty()) {
                            this.updateRecordSession(query, new ResultSetMetaData(query), 0, seeker, recordSession);
                            this.notifySearchResults(INDIVIDUAL_SEARCH, true, null, seeker, seekerPrx);
                        } else {
                            notifySearchResults(query, searchResults, seeker, recordSession, "Meta Search and Split");
                            updateResultsStore(searchResults);

                        }

                        break;
                    } else {
                        throw new SeekerException("Doesn't have privileges to executing this search");
                    }

                case SearchPrinciple.MULTI_SEARCH_AND_SWITCH:
                    int roleM = seeker.getRole();
                    if (roleM == Seeker.ROLE_CHAIRMAN) {
                        int membersNumber = temp.record.size();
                        List<ResultSetMetaData> searchResults = retrievalManager.getSearcherManager().search(MULTIPLE_SEARCHERS, principle, query, docType, field, caseSensitive, membersNumber);

                        if (searchResults.isEmpty()) {
                            this.updateRecordSession(query, new ResultSetMetaData(query), 0, seeker, recordSession);
                            this.notifySearchResults(INDIVIDUAL_SEARCH, true, null, seeker, seekerPrx);
                        } else {
                            notifySearchResults(query, searchResults, seeker, recordSession, "Multi Search and Switch");
                            updateResultsStore(searchResults);
                        }

                        break;
                    } else {
                        throw new SeekerException("Doesn't have privileges to executing this search");
                    }

                default:
                    throw new IllegalArgumentException("Division of labor principle is not supported.");
            }
        } catch (SearchableException ex) {
            OutputMonitor.printStream("Searchable", ex);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void executeSearch(String sessionName, int principle, String query, String docType, int[] fields, boolean caseSensitive, Seeker seeker, ClientSidePrx seekerPrx) throws SessionException, SeekerException, SearchException, IllegalArgumentException, IOException {

        SessionProfile recordSession = this.getSessionProfile(sessionName);
        SeekerInfo temp = recordSession.getSeekerInfo();
        this.validate(sessionName, seeker, temp);
        ResultSetMetaData resultsList;

        try {
            switch (principle) {

                case SearchPrinciple.META_SEARCH:
                    resultsList = retrievalManager.getSearcherManager().search(query, docType, fields, MULTIPLE_SEARCHERS, principle, caseSensitive);

                    if (resultsList.isEmpty()) {
                        this.updateRecordSession(query, resultsList, 0, seeker, recordSession);
                        this.notifySearchResults(INDIVIDUAL_SEARCH, true, null, seeker, seekerPrx);
                    } else {
                        int documentsCount = resultsList.getDocumentsCount();
                        this.updateRecordSession(query, resultsList, documentsCount, seeker, recordSession);
                        this.notifySearchResults(INDIVIDUAL_SEARCH, false, resultsList, seeker, seekerPrx);
                        updateResultsStore(resultsList);
                        this.saveSearchDB(sessionName, seeker, resultsList, "Meta Search");
                    }

                    break;

                case SearchPrinciple.MULTI_SEARCH:
                    resultsList = retrievalManager.getSearcherManager().search(query, docType, fields, MULTIPLE_SEARCHERS, principle, caseSensitive);

                    if (resultsList.isEmpty()) {
                        this.updateRecordSession(query, resultsList, 0, seeker, recordSession);
                        this.notifySearchResults(INDIVIDUAL_SEARCH, true, null, seeker, seekerPrx);
                    } else {
                        int documentsCount = resultsList.getDocumentsCount();
                        this.updateRecordSession(query, resultsList, documentsCount, seeker, recordSession);
                        this.notifySearchResults(INDIVIDUAL_SEARCH, false, resultsList, seeker, seekerPrx);
                        updateResultsStore(resultsList);
                        this.saveSearchDB(sessionName, seeker, resultsList, "Multi Search");
                    }

                    break;

                case SearchPrinciple.META_SEARCH_AND_SPLIT:
                    int role = seeker.getRole();
                    if (role == Seeker.ROLE_CHAIRMAN) {
                        int membersNumber = temp.record.size();
                        List<ResultSetMetaData> searchResults = retrievalManager.getSearcherManager().search(MULTIPLE_SEARCHERS, principle, query, docType, fields, caseSensitive, membersNumber);

                        if (searchResults.isEmpty()) {
                            this.updateRecordSession(query, new ResultSetMetaData(query), 0, seeker, recordSession);
                            this.notifySearchResults(INDIVIDUAL_SEARCH, true, null, seeker, seekerPrx);
                        } else {
                            notifySearchResults(query, searchResults, seeker, recordSession, "Meta Search and Split");
                            updateResultsStore(searchResults);

                        }

                        break;
                    } else {
                        throw new SeekerException("Doesn't have privileges to executing this search");
                    }

                case SearchPrinciple.MULTI_SEARCH_AND_SWITCH:
                    int roleM = seeker.getRole();
                    if (roleM == Seeker.ROLE_CHAIRMAN) {
                        int membersNumber = temp.record.size();
                        List<ResultSetMetaData> searchResults = retrievalManager.getSearcherManager().search(MULTIPLE_SEARCHERS, principle, query, docType, fields, caseSensitive, membersNumber);

                        if (searchResults.isEmpty()) {
                            this.updateRecordSession(query, new ResultSetMetaData(query), 0, seeker, recordSession);
                            this.notifySearchResults(INDIVIDUAL_SEARCH, true, null, seeker, seekerPrx);
                        } else {
                            notifySearchResults(query, searchResults, seeker, recordSession, "Multi Search and Switch");
                            updateResultsStore(searchResults);
                        }

                        break;
                    } else {
                        throw new SeekerException("Doesn't have privileges to executing this search");
                    }

                default:
                    throw new IllegalArgumentException("Division of labor principle is not supported.");
            }

        } catch (SearchableException ex) {
            OutputMonitor.printStream("Searchable", ex);
        }
    }

    /**
     * {@inheritDoc}
     * @throws SessionException
     * @throws SearchException
     * @throws IllegalArgumentException
     * @throws IOException
     */
    public void executeSearch(String sessionName, int principle, String query, String[] docTypes, int field, boolean caseSensitive, Seeker seeker, ClientSidePrx seekerPrx) throws SessionException, SeekerException, SearchException, IllegalArgumentException, IOException {

        SessionProfile recordSession = this.getSessionProfile(sessionName);
        SeekerInfo temp = recordSession.getSeekerInfo();
        this.validate(sessionName, seeker, temp);
        ResultSetMetaData resultsList;

        try {
            switch (principle) {

                case SearchPrinciple.META_SEARCH:
                    resultsList = retrievalManager.getSearcherManager().search(query, docTypes, field, MULTIPLE_SEARCHERS, principle, caseSensitive);

                    if (resultsList.isEmpty()) {
                        this.updateRecordSession(query, resultsList, 0, seeker, recordSession);
                        this.notifySearchResults(INDIVIDUAL_SEARCH, true, null, seeker, seekerPrx);
                    } else {
                        int documentsCount = resultsList.getDocumentsCount();
                        this.updateRecordSession(query, resultsList, documentsCount, seeker, recordSession);
                        this.notifySearchResults(INDIVIDUAL_SEARCH, false, resultsList, seeker, seekerPrx);
                        updateResultsStore(resultsList);
                        this.saveSearchDB(sessionName, seeker, resultsList, "Meta Search");

                    }

                    break;

                case SearchPrinciple.MULTI_SEARCH:
                    resultsList = retrievalManager.getSearcherManager().search(query, docTypes, field, MULTIPLE_SEARCHERS, principle, caseSensitive);

                    if (resultsList.isEmpty()) {
                        this.updateRecordSession(query, resultsList, 0, seeker, recordSession);
                        this.notifySearchResults(INDIVIDUAL_SEARCH, true, null, seeker, seekerPrx);
                    } else {
                        int documentsCount = resultsList.getDocumentsCount();
                        this.updateRecordSession(query, resultsList, documentsCount, seeker, recordSession);
                        this.notifySearchResults(INDIVIDUAL_SEARCH, false, resultsList, seeker, seekerPrx);
                        updateResultsStore(resultsList);
                        this.saveSearchDB(sessionName, seeker, resultsList, "Multi Search");

                    }

                    break;

                case SearchPrinciple.META_SEARCH_AND_SPLIT:
                    int role = seeker.getRole();
                    if (role == Seeker.ROLE_CHAIRMAN) {
                        int membersNumber = temp.record.size();
                        List<ResultSetMetaData> searchResults = retrievalManager.getSearcherManager().search(MULTIPLE_SEARCHERS, principle, query, docTypes, field, caseSensitive, membersNumber);

                        if (searchResults.isEmpty()) {
                            this.updateRecordSession(query, new ResultSetMetaData(query), 0, seeker, recordSession);
                            this.notifySearchResults(INDIVIDUAL_SEARCH, true, null, seeker, seekerPrx);
                        } else {
                            notifySearchResults(query, searchResults, seeker, recordSession, "Meta Search and Split");
                            updateResultsStore(searchResults);
                        }

                        break;
                    } else {
                        throw new SeekerException("Doesn't have privileges to executing this search");
                    }

                case SearchPrinciple.MULTI_SEARCH_AND_SWITCH:
                    int roleM = seeker.getRole();
                    if (roleM == Seeker.ROLE_CHAIRMAN) {
                        int membersNumber = temp.record.size();
                        List<ResultSetMetaData> searchResults = retrievalManager.getSearcherManager().search(MULTIPLE_SEARCHERS, principle, query, docTypes, field, caseSensitive, membersNumber);

                        if (searchResults.isEmpty()) {
                            this.updateRecordSession(query, new ResultSetMetaData(query), 0, seeker, recordSession);
                            this.notifySearchResults(INDIVIDUAL_SEARCH, true, null, seeker, seekerPrx);
                        } else {
                            notifySearchResults(query, searchResults, seeker, recordSession, "Multi Search and Switch");
                            updateResultsStore(searchResults);
                        }
                        break;
                    } else {
                        throw new SeekerException("Doesn't have privileges to executing this search");
                    }

                default:
                    throw new IllegalArgumentException("Division of labor principle is not supported.");
            }
        } catch (SearchableException ex) {
            OutputMonitor.printStream("Searchable", ex);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void executeSearch(String sessionName, int principle, String query, String[] docTypes, int[] fields, boolean caseSensitive, Seeker seeker, ClientSidePrx seekerPrx) throws SessionException, SeekerException, SearchException, IllegalArgumentException, IOException {

        SessionProfile recordSession = this.getSessionProfile(sessionName);
        SeekerInfo temp = recordSession.getSeekerInfo();
        this.validate(sessionName, seeker, temp);
        ResultSetMetaData resultsList;

        try {
            switch (principle) {

                case SearchPrinciple.META_SEARCH:
                    resultsList = retrievalManager.getSearcherManager().search(query, docTypes, fields, MULTIPLE_SEARCHERS, principle, caseSensitive);

                    if (resultsList.isEmpty()) {
                        this.updateRecordSession(query, resultsList, 0, seeker, recordSession);
                        this.notifySearchResults(INDIVIDUAL_SEARCH, true, null, seeker, seekerPrx);
                    } else {
                        int documentsCount = resultsList.getDocumentsCount();
                        this.updateRecordSession(query, resultsList, documentsCount, seeker, recordSession);
                        this.notifySearchResults(INDIVIDUAL_SEARCH, false, resultsList, seeker, seekerPrx);
                        updateResultsStore(resultsList);
                        this.saveSearchDB(sessionName, seeker, resultsList, "Meta Search");
                    }

                    break;

                case SearchPrinciple.MULTI_SEARCH:
                    resultsList = retrievalManager.getSearcherManager().search(query, docTypes, fields, MULTIPLE_SEARCHERS, principle, caseSensitive);

                    if (resultsList.isEmpty()) {
                        this.updateRecordSession(query, resultsList, 0, seeker, recordSession);
                        this.notifySearchResults(INDIVIDUAL_SEARCH, true, null, seeker, seekerPrx);
                    } else {
                        int documentsCount = resultsList.getDocumentsCount();
                        this.updateRecordSession(query, resultsList, documentsCount, seeker, recordSession);
                        this.notifySearchResults(INDIVIDUAL_SEARCH, false, resultsList, seeker, seekerPrx);
                        updateResultsStore(resultsList);
                        this.saveSearchDB(sessionName, seeker, resultsList, "Multi Search");
                    }

                    break;

                case SearchPrinciple.META_SEARCH_AND_SPLIT:
                    int role = seeker.getRole();
                    if (role == Seeker.ROLE_CHAIRMAN) {
                        int membersNumber = temp.record.size();
                        List<ResultSetMetaData> searchResults = retrievalManager.getSearcherManager().search(MULTIPLE_SEARCHERS, principle, query, docTypes, fields, caseSensitive, membersNumber);

                        if (searchResults.isEmpty()) {
                            this.updateRecordSession(query, new ResultSetMetaData(query), 0, seeker, recordSession);
                            this.notifySearchResults(INDIVIDUAL_SEARCH, true, null, seeker, seekerPrx);
                        } else {
                            notifySearchResults(query, searchResults, seeker, recordSession, "Meta Search and Split");
                            updateResultsStore(searchResults);
                        }

                        break;
                    } else {
                        throw new SeekerException("Doesn't have privileges to executing this search");
                    }

                case SearchPrinciple.MULTI_SEARCH_AND_SWITCH:
                    int roleM = seeker.getRole();
                    if (roleM == Seeker.ROLE_CHAIRMAN) {
                        int membersNumber = temp.record.size();
                        List<ResultSetMetaData> searchResults = retrievalManager.getSearcherManager().search(MULTIPLE_SEARCHERS, principle, query, docTypes, fields, caseSensitive, membersNumber);

                        if (searchResults.isEmpty()) {
                            this.updateRecordSession(query, new ResultSetMetaData(query), 0, seeker, recordSession);
                            this.notifySearchResults(INDIVIDUAL_SEARCH, true, null, seeker, seekerPrx);
                        } else {
                            notifySearchResults(query, searchResults, seeker, recordSession, "Multi Search and Switch");
                            updateResultsStore(searchResults);
                        }
                        break;
                    } else {
                        throw new SeekerException("Doesn't have privileges to executing this search");
                    }

                default:
                    throw new IllegalArgumentException("Division of labor principle is not supported.");
            }
        } catch (SearchableException ex) {
            OutputMonitor.printStream("Searchable", ex);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void executeSearch(String sessionName, int principle, int[] searchers, String query, boolean caseSensitive, Seeker seeker, ClientSidePrx seekerPrx) throws SessionException, SeekerException, SearchableException, SearchException, IllegalArgumentException, IOException {

        SessionProfile recordSession = this.getSessionProfile(sessionName);
        SeekerInfo temp = recordSession.getSeekerInfo();
        this.validate(sessionName, seeker, temp);
        ResultSetMetaData resultsList;

        switch (principle) {

            case SearchPrinciple.META_SEARCH:
                resultsList = retrievalManager.getSearcherManager().search(searchers, query, principle, caseSensitive);

                if (resultsList.isEmpty()) {
                    this.updateRecordSession(query, resultsList, 0, seeker, recordSession);
                    this.notifySearchResults(INDIVIDUAL_SEARCH, true, null, seeker, seekerPrx);
                } else {
                    int documentsCount = resultsList.getDocumentsCount();
                    this.updateRecordSession(query, resultsList, documentsCount, seeker, recordSession);
                    this.notifySearchResults(INDIVIDUAL_SEARCH, false, resultsList, seeker, seekerPrx);
                    updateResultsStore(resultsList);
                    this.saveSearchDB(sessionName, seeker, resultsList, "Meta Search");
                }

                break;

            case SearchPrinciple.MULTI_SEARCH:
                resultsList = retrievalManager.getSearcherManager().search(searchers, query, principle, caseSensitive);

                if (resultsList.isEmpty()) {
                    this.updateRecordSession(query, resultsList, 0, seeker, recordSession);
                    this.notifySearchResults(INDIVIDUAL_SEARCH, true, null, seeker, seekerPrx);
                } else {
                    int documentsCount = resultsList.getDocumentsCount();
                    this.updateRecordSession(query, resultsList, documentsCount, seeker, recordSession);
                    this.notifySearchResults(INDIVIDUAL_SEARCH, false, resultsList, seeker, seekerPrx);
                    updateResultsStore(resultsList);
                    this.saveSearchDB(sessionName, seeker, resultsList, "Multi Search");
                }

                break;

            case SearchPrinciple.META_SEARCH_AND_SPLIT:
                int role = seeker.getRole();
                if (role == Seeker.ROLE_CHAIRMAN) {
                    int membersNumber = temp.record.size();
                    List<ResultSetMetaData> searchResults = retrievalManager.getSearcherManager().search(searchers, principle, query, caseSensitive, membersNumber);

                    if (searchResults.isEmpty()) {
                        this.updateRecordSession(query, new ResultSetMetaData(query), 0, seeker, recordSession);
                        this.notifySearchResults(INDIVIDUAL_SEARCH, true, null, seeker, seekerPrx);
                    } else {
                        notifySearchResults(query, searchResults, seeker, recordSession, "Meta Search and Split");
                        updateResultsStore(searchResults);
                    }

                    break;
                } else {
                    throw new SeekerException("Doesn't have privileges to executing this search");
                }

            case SearchPrinciple.MULTI_SEARCH_AND_SWITCH:
                int roleM = seeker.getRole();

                if (roleM == Seeker.ROLE_CHAIRMAN) {
                    int membersNumber = temp.record.size();
                    List<ResultSetMetaData> searchResults = retrievalManager.getSearcherManager().search(searchers, principle, query, caseSensitive, membersNumber);

                    if (searchResults.isEmpty()) {
                        this.updateRecordSession(query, new ResultSetMetaData(query), 0, seeker, recordSession);
                        this.notifySearchResults(INDIVIDUAL_SEARCH, true, null, seeker, seekerPrx);
                    } else {
                        notifySearchResults(query, searchResults, seeker, recordSession, "Multi Search and Switch");
                        updateResultsStore(searchResults);
                    }

                    break;
                } else {
                    throw new SeekerException("Doesn't have privileges to executing this search");
                }

            default:
                throw new IllegalArgumentException("Division of labor principle is not supported.");
        }

    }

    /**
     * {@inheritDoc}
     */
    public void executeSearch(String sessionName, int principle, int[] searchers, String query, int field, boolean caseSensitive, Seeker seeker, ClientSidePrx seekerPrx) throws SessionException, SeekerException, SearchableException, SearchException, IllegalArgumentException, IOException {

        SessionProfile recordSession = this.getSessionProfile(sessionName);
        SeekerInfo temp = recordSession.getSeekerInfo();
        this.validate(sessionName, seeker, temp);
        ResultSetMetaData resultsList;

        switch (principle) {

            case SearchPrinciple.META_SEARCH:
                resultsList = retrievalManager.getSearcherManager().search(searchers, query, field, principle, caseSensitive);

                if (resultsList.isEmpty()) {
                    this.updateRecordSession(query, resultsList, 0, seeker, recordSession);
                    this.notifySearchResults(INDIVIDUAL_SEARCH, true, null, seeker, seekerPrx);
                } else {
                    int documentsCount = resultsList.getDocumentsCount();
                    this.updateRecordSession(query, resultsList, documentsCount, seeker, recordSession);
                    this.notifySearchResults(INDIVIDUAL_SEARCH, false, resultsList, seeker, seekerPrx);
                    updateResultsStore(resultsList);
                    this.saveSearchDB(sessionName, seeker, resultsList, "Meta Search");
                }

                break;

            case SearchPrinciple.MULTI_SEARCH:
                resultsList = retrievalManager.getSearcherManager().search(searchers, query, field, principle, caseSensitive);

                if (resultsList.isEmpty()) {
                    this.updateRecordSession(query, resultsList, 0, seeker, recordSession);
                    this.notifySearchResults(INDIVIDUAL_SEARCH, true, null, seeker, seekerPrx);
                } else {
                    int documentsCount = resultsList.getDocumentsCount();
                    this.updateRecordSession(query, resultsList, documentsCount, seeker, recordSession);
                    this.notifySearchResults(INDIVIDUAL_SEARCH, false, resultsList, seeker, seekerPrx);
                    this.saveSearchDB(sessionName, seeker, resultsList, "Multi Search");
                    updateResultsStore(resultsList);
                }

                break;

            case SearchPrinciple.META_SEARCH_AND_SPLIT:
                int role = seeker.getRole();
                if (role == Seeker.ROLE_CHAIRMAN) {
                    int membersNumber = temp.record.size();
                    List<ResultSetMetaData> searchResults = retrievalManager.getSearcherManager().search(searchers, principle, query, field, caseSensitive, membersNumber);

                    if (searchResults.isEmpty()) {
                        this.updateRecordSession(query, new ResultSetMetaData(query), 0, seeker, recordSession);
                        this.notifySearchResults(INDIVIDUAL_SEARCH, true, null, seeker, seekerPrx);
                    } else {
                        notifySearchResults(query, searchResults, seeker, recordSession, "Meta Search and Split");
                        updateResultsStore(searchResults);
                    }
                    break;
                } else {
                    throw new SeekerException("Doesn't have privileges to executing this search");
                }

            case SearchPrinciple.MULTI_SEARCH_AND_SWITCH:
                int roleM = seeker.getRole();
                if (roleM == Seeker.ROLE_CHAIRMAN) {
                    int membersNumber = temp.record.size();
                    List<ResultSetMetaData> searchResults = retrievalManager.getSearcherManager().search(searchers, principle, query, field, caseSensitive, membersNumber);

                    if (searchResults.isEmpty()) {
                        this.updateRecordSession(query, new ResultSetMetaData(query), 0, seeker, recordSession);
                        this.notifySearchResults(INDIVIDUAL_SEARCH, true, null, seeker, seekerPrx);
                    } else {
                        notifySearchResults(query, searchResults, seeker, recordSession, "Multi Search and Switch");
                        updateResultsStore(searchResults);
                    }
                    break;
                } else {
                    throw new SeekerException("Doesn't have privileges to executing this search");
                }
            default:
                throw new IllegalArgumentException("Division of labor principle is not supported.");
        }
    }

    /**
     * {@inheritDoc}
     */
    public void executeSearch(String sessionName, int principle, int[] searchers, String query, int[] fields, boolean caseSensitive, Seeker seeker, ClientSidePrx seekerPrx) throws SessionException, SeekerException, SearchableException, SearchException, IllegalArgumentException, IOException {

        SessionProfile recordSession = this.getSessionProfile(sessionName);
        SeekerInfo temp = recordSession.getSeekerInfo();
        this.validate(sessionName, seeker, temp);
        ResultSetMetaData resultsList;

        switch (principle) {

            case SearchPrinciple.META_SEARCH:
                resultsList = retrievalManager.getSearcherManager().search(searchers, query, fields, principle, caseSensitive);

                if (resultsList.isEmpty()) {
                    this.updateRecordSession(query, resultsList, 0, seeker, recordSession);
                    this.notifySearchResults(INDIVIDUAL_SEARCH, true, null, seeker, seekerPrx);
                } else {
                    int documentsCount = resultsList.getDocumentsCount();
                    this.updateRecordSession(query, resultsList, documentsCount, seeker, recordSession);
                    this.notifySearchResults(INDIVIDUAL_SEARCH, false, resultsList, seeker, seekerPrx);
                    this.saveSearchDB(sessionName, seeker, resultsList, "Meta Search");
                    updateResultsStore(resultsList);
                }

                break;

            case SearchPrinciple.MULTI_SEARCH:
                resultsList = retrievalManager.getSearcherManager().search(searchers, query, fields, principle, caseSensitive);

                if (resultsList.isEmpty()) {
                    this.updateRecordSession(query, resultsList, 0, seeker, recordSession);
                    this.notifySearchResults(INDIVIDUAL_SEARCH, true, null, seeker, seekerPrx);
                } else {
                    int documentsCount = resultsList.getDocumentsCount();
                    this.updateRecordSession(query, resultsList, documentsCount, seeker, recordSession);
                    this.notifySearchResults(INDIVIDUAL_SEARCH, false, resultsList, seeker, seekerPrx);
                    this.saveSearchDB(sessionName, seeker, resultsList, "Multi Search");
                    updateResultsStore(resultsList);
                }

                break;

            case SearchPrinciple.META_SEARCH_AND_SPLIT:
                int role = seeker.getRole();
                if (role == Seeker.ROLE_CHAIRMAN) {
                    int membersNumber = temp.record.size();
                    List<ResultSetMetaData> searchResults = retrievalManager.getSearcherManager().search(searchers, principle, query, fields, caseSensitive, membersNumber);

                    if (searchResults.isEmpty()) {
                        this.updateRecordSession(query, new ResultSetMetaData(query), 0, seeker, recordSession);
                        this.notifySearchResults(INDIVIDUAL_SEARCH, true, null, seeker, seekerPrx);
                    } else {
                        notifySearchResults(query, searchResults, seeker, recordSession, "Meta Search and Split");
                        updateResultsStore(searchResults);
                    }
                    break;
                } else {
                    throw new SeekerException("Doesn't have privileges to executing this search");
                }

            case SearchPrinciple.MULTI_SEARCH_AND_SWITCH:
                int roleM = seeker.getRole();
                if (roleM == Seeker.ROLE_CHAIRMAN) {
                    int membersNumber = temp.record.size();
                    List<ResultSetMetaData> searchResults = retrievalManager.getSearcherManager().search(searchers, principle, query, fields, caseSensitive, membersNumber);

                    if (searchResults.isEmpty()) {
                        this.updateRecordSession(query, new ResultSetMetaData(query), 0, seeker, recordSession);
                        this.notifySearchResults(INDIVIDUAL_SEARCH, true, null, seeker, seekerPrx);
                    } else {
                        notifySearchResults(query, searchResults, seeker, recordSession, "Multi Search and Switch");
                        updateResultsStore(searchResults);
                    }
                    break;
                } else {
                    throw new SeekerException("Doesn't have privileges to executing this search");
                }
            default:
                throw new IllegalArgumentException("Division of labor principle is not supported.");
        }
    }

    /**
     * {@inheritDoc}
     */
    public void executeSearch(String sessionName, int principle, int[] searchers, String query, String docType, boolean caseSensitive, Seeker seeker, ClientSidePrx seekerPrx) throws SessionException, SeekerException, SearchableException, SearchException, IllegalArgumentException, IOException {

        SessionProfile recordSession = this.getSessionProfile(sessionName);
        SeekerInfo temp = recordSession.getSeekerInfo();
        this.validate(sessionName, seeker, temp);
        ResultSetMetaData resultsList;
        switch (principle) {

            case SearchPrinciple.META_SEARCH:
                resultsList = retrievalManager.getSearcherManager().search(searchers, query, docType, principle, caseSensitive);

                if (resultsList.isEmpty()) {
                    this.updateRecordSession(query, resultsList, 0, seeker, recordSession);
                    this.notifySearchResults(INDIVIDUAL_SEARCH, true, null, seeker, seekerPrx);
                } else {
                    int documentsCount = resultsList.getDocumentsCount();
                    this.updateRecordSession(query, resultsList, documentsCount, seeker, recordSession);
                    this.notifySearchResults(INDIVIDUAL_SEARCH, false, resultsList, seeker, seekerPrx);
                    this.saveSearchDB(sessionName, seeker, resultsList, "Meta Search");
                    updateResultsStore(resultsList);
                }

                break;

            case SearchPrinciple.MULTI_SEARCH:
                resultsList = retrievalManager.getSearcherManager().search(searchers, query, docType, principle, caseSensitive);

                if (resultsList.isEmpty()) {
                    this.updateRecordSession(query, resultsList, 0, seeker, recordSession);
                    this.notifySearchResults(INDIVIDUAL_SEARCH, true, null, seeker, seekerPrx);
                } else {
                    int documentsCount = resultsList.getDocumentsCount();
                    this.updateRecordSession(query, resultsList, documentsCount, seeker, recordSession);
                    this.notifySearchResults(INDIVIDUAL_SEARCH, false, resultsList, seeker, seekerPrx);
                    this.saveSearchDB(sessionName, seeker, resultsList, "Multi Search");
                    updateResultsStore(resultsList);
                }

                break;

            case SearchPrinciple.META_SEARCH_AND_SPLIT:
                int role = seeker.getRole();
                if (role == Seeker.ROLE_CHAIRMAN) {
                    int membersNumber = temp.record.size();
                    List<ResultSetMetaData> searchResults = retrievalManager.getSearcherManager().search(searchers, principle, query, docType, caseSensitive, membersNumber);

                    if (searchResults.isEmpty()) {
                        this.updateRecordSession(query, new ResultSetMetaData(query), 0, seeker, recordSession);
                        this.notifySearchResults(INDIVIDUAL_SEARCH, true, null, seeker, seekerPrx);
                    } else {
                        notifySearchResults(query, searchResults, seeker, recordSession, "Meta Search and Split");
                        updateResultsStore(searchResults);
                    }

                    break;
                } else {
                    throw new SeekerException("Doesn't have privileges to executing this search");
                }

            case SearchPrinciple.MULTI_SEARCH_AND_SWITCH:
                int roleM = seeker.getRole();
                if (roleM == Seeker.ROLE_CHAIRMAN) {
                    int membersNumber = temp.record.size();
                    List<ResultSetMetaData> searchResults = retrievalManager.getSearcherManager().search(searchers, principle, query, docType, caseSensitive, membersNumber);

                    if (searchResults.isEmpty()) {
                        this.updateRecordSession(query, new ResultSetMetaData(query), 0, seeker, recordSession);
                        this.notifySearchResults(INDIVIDUAL_SEARCH, true, null, seeker, seekerPrx);
                    } else {
                        notifySearchResults(query, searchResults, seeker, recordSession, "Multi Search and Switch");
                        updateResultsStore(searchResults);
                    }

                    break;
                } else {
                    throw new SeekerException("Doesn't have privileges to executing this search");
                }

            default:
                throw new IllegalArgumentException("Division of labor principle is not supported.");
        }

    }

    /**
     * {@inheritDoc}
     */
    public void executeSearch(String sessionName, int principle, int[] searchers, String query, String[] docTypes, boolean caseSensitive, Seeker seeker, ClientSidePrx seekerPrx) throws SessionException, SeekerException, SearchableException, SearchException, IllegalArgumentException, IOException {

        SessionProfile recordSession = this.getSessionProfile(sessionName);
        SeekerInfo temp = recordSession.getSeekerInfo();
        this.validate(sessionName, seeker, temp);
        ResultSetMetaData resultsList;

        switch (principle) {

            case SearchPrinciple.META_SEARCH:
                resultsList = retrievalManager.getSearcherManager().search(searchers, query, docTypes, principle, caseSensitive);

                if (resultsList.isEmpty()) {
                    this.updateRecordSession(query, resultsList, 0, seeker, recordSession);
                    this.notifySearchResults(INDIVIDUAL_SEARCH, true, null, seeker, seekerPrx);
                } else {
                    int documentsCount = resultsList.getDocumentsCount();
                    this.updateRecordSession(query, resultsList, documentsCount, seeker, recordSession);
                    this.notifySearchResults(INDIVIDUAL_SEARCH, false, resultsList, seeker, seekerPrx);
                    this.saveSearchDB(sessionName, seeker, resultsList, "Meta Search");
                    updateResultsStore(resultsList);
                }

                break;

            case SearchPrinciple.MULTI_SEARCH:
                resultsList = retrievalManager.getSearcherManager().search(searchers, query, docTypes, principle, caseSensitive);

                if (resultsList.isEmpty()) {
                    this.updateRecordSession(query, resultsList, 0, seeker, recordSession);
                    this.notifySearchResults(INDIVIDUAL_SEARCH, true, null, seeker, seekerPrx);
                } else {
                    int documentsCount = resultsList.getDocumentsCount();
                    this.updateRecordSession(query, resultsList, documentsCount, seeker, recordSession);
                    this.notifySearchResults(INDIVIDUAL_SEARCH, false, resultsList, seeker, seekerPrx);
                    this.saveSearchDB(sessionName, seeker, resultsList, "Multi Search");
                    updateResultsStore(resultsList);
                }

                break;

            case SearchPrinciple.META_SEARCH_AND_SPLIT:
                int role = seeker.getRole();
                if (role == Seeker.ROLE_CHAIRMAN) {
                    int membersNumber = temp.record.size();
                    List<ResultSetMetaData> searchResults = retrievalManager.getSearcherManager().search(searchers, principle, query, docTypes, caseSensitive, membersNumber);

                    if (searchResults.isEmpty()) {
                        this.updateRecordSession(query, new ResultSetMetaData(query), 0, seeker, recordSession);
                        this.notifySearchResults(INDIVIDUAL_SEARCH, true, null, seeker, seekerPrx);
                    } else {
                        notifySearchResults(query, searchResults, seeker, recordSession, "Meta Search and Split");
                        updateResultsStore(searchResults);
                    }

                    break;
                } else {
                    throw new SeekerException("Doesn't have privileges to executing this search");
                }

            case SearchPrinciple.MULTI_SEARCH_AND_SWITCH:
                int roleM = seeker.getRole();
                if (roleM == Seeker.ROLE_CHAIRMAN) {
                    int membersNumber = temp.record.size();
                    List<ResultSetMetaData> searchResults = retrievalManager.getSearcherManager().search(searchers, principle, query, docTypes, caseSensitive, membersNumber);

                    if (searchResults.isEmpty()) {
                        this.updateRecordSession(query, new ResultSetMetaData(query), 0, seeker, recordSession);
                        this.notifySearchResults(INDIVIDUAL_SEARCH, true, null, seeker, seekerPrx);
                    } else {
                        notifySearchResults(query, searchResults, seeker, recordSession, "Multi Search and Switch");
                        updateResultsStore(searchResults);
                    }
                    break;
                } else {
                    throw new SeekerException("Doesn't have privileges to executing this search");
                }
            default:
                throw new IllegalArgumentException("Division of labor principle is not supported.");
        }
    }

    /**
     * {@inheritDoc}
     */
    public void executeSearch(String sessionName, int principle, int[] searchers, String query, String docType, int field, boolean caseSensitive, Seeker seeker, ClientSidePrx seekerPrx) throws SessionException, SeekerException, SearchableException, SearchException, IllegalArgumentException, IOException {
        SessionProfile recordSession = this.getSessionProfile(sessionName);
        SeekerInfo temp = recordSession.getSeekerInfo();
        this.validate(sessionName, seeker, temp);
        ResultSetMetaData resultsList;
        switch (principle) {

            case SearchPrinciple.META_SEARCH:
                resultsList = retrievalManager.getSearcherManager().search(searchers, query, docType, field, principle, caseSensitive);

                if (resultsList.isEmpty()) {
                    this.updateRecordSession(query, resultsList, 0, seeker, recordSession);
                    this.notifySearchResults(INDIVIDUAL_SEARCH, true, null, seeker, seekerPrx);
                } else {
                    int documentsCount = resultsList.getDocumentsCount();
                    this.updateRecordSession(query, resultsList, documentsCount, seeker, recordSession);
                    this.notifySearchResults(INDIVIDUAL_SEARCH, false, resultsList, seeker, seekerPrx);
                    this.saveSearchDB(sessionName, seeker, resultsList, "Meta Search");
                    updateResultsStore(resultsList);
                }

                break;

            case SearchPrinciple.MULTI_SEARCH:
                resultsList = retrievalManager.getSearcherManager().search(searchers, query, docType, field, principle, caseSensitive);

                if (resultsList.isEmpty()) {
                    this.updateRecordSession(query, resultsList, 0, seeker, recordSession);
                    this.notifySearchResults(INDIVIDUAL_SEARCH, true, null, seeker, seekerPrx);
                } else {
                    int documentsCount = resultsList.getDocumentsCount();
                    this.updateRecordSession(query, resultsList, documentsCount, seeker, recordSession);
                    this.notifySearchResults(INDIVIDUAL_SEARCH, false, resultsList, seeker, seekerPrx);
                    this.saveSearchDB(sessionName, seeker, resultsList, "Multi Search");
                }

                break;

            case SearchPrinciple.META_SEARCH_AND_SPLIT:
                int role = seeker.getRole();
                if (role == Seeker.ROLE_CHAIRMAN) {
                    int membersNumber = temp.record.size();
                    List<ResultSetMetaData> searchResults = retrievalManager.getSearcherManager().search(searchers, principle, query, docType, field, caseSensitive, membersNumber);

                    if (searchResults.isEmpty()) {
                        this.updateRecordSession(query, new ResultSetMetaData(query), 0, seeker, recordSession);
                        this.notifySearchResults(INDIVIDUAL_SEARCH, true, null, seeker, seekerPrx);
                    } else {
                        notifySearchResults(query, searchResults, seeker, recordSession, "Meta Search and Split");
                        updateResultsStore(searchResults);
                    }

                    break;
                } else {
                    throw new SeekerException("Doesn't have privileges to executing this search");
                }

            case SearchPrinciple.MULTI_SEARCH_AND_SWITCH:
                int roleM = seeker.getRole();
                if (roleM == Seeker.ROLE_CHAIRMAN) {
                    int membersNumber = temp.record.size();
                    List<ResultSetMetaData> searchResults = retrievalManager.getSearcherManager().search(searchers, principle, query, docType, field, caseSensitive, membersNumber);

                    if (searchResults.isEmpty()) {
                        this.updateRecordSession(query, new ResultSetMetaData(query), 0, seeker, recordSession);
                        this.notifySearchResults(INDIVIDUAL_SEARCH, true, null, seeker, seekerPrx);
                    } else {
                        notifySearchResults(query, searchResults, seeker, recordSession, "Multi Search and Switch");
                        updateResultsStore(searchResults);
                    }

                    break;
                } else {
                    throw new SeekerException("Doesn't have privileges to executing this search");
                }

            default:
                throw new IllegalArgumentException("Division of labor principle is not supported.");
        }

    }

    /**
     * {@inheritDoc}
     */
    public void executeSearch(String sessionName, int principle, int[] searchers, String query, String docType, int[] fields, boolean caseSensitive, Seeker seeker, ClientSidePrx seekerPrx) throws SessionException, SeekerException, SearchableException, SearchException, IllegalArgumentException, IOException {

        SessionProfile recordSession = this.getSessionProfile(sessionName);
        SeekerInfo temp = recordSession.getSeekerInfo();
        this.validate(sessionName, seeker, temp);
        ResultSetMetaData resultsList;

        switch (principle) {

            case SearchPrinciple.META_SEARCH:
                resultsList = retrievalManager.getSearcherManager().search(searchers, query, docType, fields, principle, caseSensitive);

                if (resultsList.isEmpty()) {
                    this.updateRecordSession(query, resultsList, 0, seeker, recordSession);
                    this.notifySearchResults(INDIVIDUAL_SEARCH, true, null, seeker, seekerPrx);
                } else {
                    int documentsCount = resultsList.getDocumentsCount();
                    this.updateRecordSession(query, resultsList, documentsCount, seeker, recordSession);
                    this.notifySearchResults(INDIVIDUAL_SEARCH, false, resultsList, seeker, seekerPrx);
                    this.saveSearchDB(sessionName, seeker, resultsList, "Meta Search");
                    updateResultsStore(resultsList);
                }

                break;

            case SearchPrinciple.MULTI_SEARCH:
                resultsList = retrievalManager.getSearcherManager().search(searchers, query, docType, fields, principle, caseSensitive);

                if (resultsList.isEmpty()) {
                    this.updateRecordSession(query, resultsList, 0, seeker, recordSession);
                    this.notifySearchResults(INDIVIDUAL_SEARCH, true, null, seeker, seekerPrx);
                } else {
                    int documentsCount = resultsList.getDocumentsCount();
                    this.updateRecordSession(query, resultsList, documentsCount, seeker, recordSession);
                    this.notifySearchResults(INDIVIDUAL_SEARCH, false, resultsList, seeker, seekerPrx);
                    this.saveSearchDB(sessionName, seeker, resultsList, "Multi Search");
                    updateResultsStore(resultsList);
                }

                break;

            case SearchPrinciple.META_SEARCH_AND_SPLIT:
                int role = seeker.getRole();
                if (role == Seeker.ROLE_CHAIRMAN) {
                    int membersNumber = temp.record.size();
                    List<ResultSetMetaData> searchResults = retrievalManager.getSearcherManager().search(searchers, principle, query, docType, fields, caseSensitive, membersNumber);

                    if (searchResults.isEmpty()) {
                        this.updateRecordSession(query, new ResultSetMetaData(query), 0, seeker, recordSession);
                        this.notifySearchResults(INDIVIDUAL_SEARCH, true, null, seeker, seekerPrx);
                    } else {
                        notifySearchResults(query, searchResults, seeker, recordSession, "Meta Search and Split");
                        updateResultsStore(searchResults);
                    }

                    break;
                } else {
                    throw new SeekerException("Doesn't have privileges to executing this search");
                }

            case SearchPrinciple.MULTI_SEARCH_AND_SWITCH:
                int roleM = seeker.getRole();
                if (roleM == Seeker.ROLE_CHAIRMAN) {
                    int membersNumber = temp.record.size();
                    List<ResultSetMetaData> searchResults = retrievalManager.getSearcherManager().search(searchers, principle, query, docType, fields, caseSensitive, membersNumber);

                    if (searchResults.isEmpty()) {
                        this.updateRecordSession(query, new ResultSetMetaData(query), 0, seeker, recordSession);
                        this.notifySearchResults(INDIVIDUAL_SEARCH, true, null, seeker, seekerPrx);
                    } else {
                        notifySearchResults(query, searchResults, seeker, recordSession, "Multi Search and Switch");
                        updateResultsStore(searchResults);
                    }

                    break;
                } else {
                    throw new SeekerException("Doesn't have privileges to executing this search");
                }

            default:
                throw new IllegalArgumentException("Division of labor principle is not supported.");
        }

    }

    /**
     * {@inheritDoc}
     */
    public void executeSearch(String sessionName, int principle, int[] searchers, String query, String[] docTypes, int field, boolean caseSensitive, Seeker seeker, ClientSidePrx seekerPrx) throws SessionException, SeekerException, SearchableException, SearchException, IllegalArgumentException, IOException {

        SessionProfile recordSession = this.getSessionProfile(sessionName);
        SeekerInfo temp = recordSession.getSeekerInfo();
        this.validate(sessionName, seeker, temp);
        ResultSetMetaData resultsList;

        switch (principle) {

            case SearchPrinciple.META_SEARCH:
                resultsList = retrievalManager.getSearcherManager().search(searchers, query, docTypes, field, principle, caseSensitive);

                if (resultsList.isEmpty()) {
                    this.updateRecordSession(query, resultsList, 0, seeker, recordSession);
                    this.notifySearchResults(INDIVIDUAL_SEARCH, true, null, seeker, seekerPrx);
                } else {
                    int documentsCount = resultsList.getDocumentsCount();
                    this.updateRecordSession(query, resultsList, documentsCount, seeker, recordSession);
                    this.notifySearchResults(INDIVIDUAL_SEARCH, false, resultsList, seeker, seekerPrx);
                    this.saveSearchDB(sessionName, seeker, resultsList, "Meta Search");
                    updateResultsStore(resultsList);
                }

                break;

            case SearchPrinciple.MULTI_SEARCH:
                resultsList = retrievalManager.getSearcherManager().search(searchers, query, docTypes, field, principle, caseSensitive);

                if (resultsList.isEmpty()) {
                    this.updateRecordSession(query, resultsList, 0, seeker, recordSession);
                    this.notifySearchResults(INDIVIDUAL_SEARCH, true, null, seeker, seekerPrx);
                } else {
                    int documentsCount = resultsList.getDocumentsCount();
                    this.updateRecordSession(query, resultsList, documentsCount, seeker, recordSession);
                    this.notifySearchResults(INDIVIDUAL_SEARCH, false, resultsList, seeker, seekerPrx);
                    this.saveSearchDB(sessionName, seeker, resultsList, "Multi Search");
                    updateResultsStore(resultsList);
                }

                break;

            case SearchPrinciple.META_SEARCH_AND_SPLIT:
                int role = seeker.getRole();
                if (role == Seeker.ROLE_CHAIRMAN) {
                    int membersNumber = temp.record.size();
                    List<ResultSetMetaData> searchResults = retrievalManager.getSearcherManager().search(searchers, principle, query, docTypes, field, caseSensitive, membersNumber);

                    if (searchResults.isEmpty()) {
                        this.updateRecordSession(query, new ResultSetMetaData(query), 0, seeker, recordSession);
                        this.notifySearchResults(INDIVIDUAL_SEARCH, true, null, seeker, seekerPrx);
                    } else {
                        notifySearchResults(query, searchResults, seeker, recordSession, "Meta Search and Split");
                        updateResultsStore(searchResults);
                    }

                    break;
                } else {
                    throw new SeekerException("Doesn't have privileges to executing this search");
                }

            case SearchPrinciple.MULTI_SEARCH_AND_SWITCH:
                int roleM = seeker.getRole();
                if (roleM == Seeker.ROLE_CHAIRMAN) {
                    int membersNumber = temp.record.size();
                    List<ResultSetMetaData> searchResults = retrievalManager.getSearcherManager().search(searchers, principle, query, docTypes, field, caseSensitive, membersNumber);

                    if (searchResults.isEmpty()) {
                        this.updateRecordSession(query, new ResultSetMetaData(query), 0, seeker, recordSession);
                        this.notifySearchResults(INDIVIDUAL_SEARCH, true, null, seeker, seekerPrx);
                    } else {
                        notifySearchResults(query, searchResults, seeker, recordSession, "Multi Search and Switch");
                        updateResultsStore(searchResults);
                    }
                    break;
                } else {
                    throw new SeekerException("Doesn't have privileges to executing this search");
                }
            default:
                throw new IllegalArgumentException("Division of labor principle is not supported.");
        }
    }

    /**
     * {@inheritDoc}
     */
    public void executeSearch(String sessionName, int principle, int[] searchers, String query, String[] docTypes, int[] fields, boolean caseSensitive, Seeker seeker, ClientSidePrx seekerPrx) throws SessionException, SeekerException, SearchableException, SearchException, IllegalArgumentException, IOException {

        SessionProfile recordSession = this.getSessionProfile(sessionName);
        SeekerInfo temp = recordSession.getSeekerInfo();
        this.validate(sessionName, seeker, temp);
        ResultSetMetaData resultsList;

        switch (principle) {

            case SearchPrinciple.META_SEARCH:
                resultsList = retrievalManager.getSearcherManager().search(searchers, query, docTypes, fields, principle, caseSensitive);

                if (resultsList.isEmpty()) {
                    this.updateRecordSession(query, resultsList, 0, seeker, recordSession);
                    this.notifySearchResults(INDIVIDUAL_SEARCH, true, null, seeker, seekerPrx);
                } else {
                    int documentsCount = resultsList.getDocumentsCount();
                    this.updateRecordSession(query, resultsList, documentsCount, seeker, recordSession);
                    this.notifySearchResults(INDIVIDUAL_SEARCH, false, resultsList, seeker, seekerPrx);
                    this.saveSearchDB(sessionName, seeker, resultsList, "Meta Search");
                    updateResultsStore(resultsList);
                }

                break;

            case SearchPrinciple.MULTI_SEARCH:
                resultsList = retrievalManager.getSearcherManager().search(searchers, query, docTypes, fields, principle, caseSensitive);

                if (resultsList.isEmpty()) {
                    this.updateRecordSession(query, resultsList, 0, seeker, recordSession);
                    this.notifySearchResults(INDIVIDUAL_SEARCH, true, null, seeker, seekerPrx);
                } else {
                    int documentsCount = resultsList.getDocumentsCount();
                    this.updateRecordSession(query, resultsList, documentsCount, seeker, recordSession);
                    this.notifySearchResults(INDIVIDUAL_SEARCH, false, resultsList, seeker, seekerPrx);
                    this.saveSearchDB(sessionName, seeker, resultsList, "Multi Search");
                    updateResultsStore(resultsList);
                }

                break;

            case SearchPrinciple.META_SEARCH_AND_SPLIT:
                int role = seeker.getRole();
                if (role == Seeker.ROLE_CHAIRMAN) {
                    int membersNumber = temp.record.size();
                    List<ResultSetMetaData> searchResults = retrievalManager.getSearcherManager().search(searchers, principle, query, docTypes, fields, caseSensitive, membersNumber);

                    if (searchResults.isEmpty()) {
                        this.updateRecordSession(query, new ResultSetMetaData(query), 0, seeker, recordSession);
                        this.notifySearchResults(INDIVIDUAL_SEARCH, true, null, seeker, seekerPrx);
                    } else {
                        notifySearchResults(query, searchResults, seeker, recordSession, "Meta Search and Split");
                        updateResultsStore(searchResults);
                    }

                    break;
                } else {
                    throw new SeekerException("Doesn't have privileges to executing this search");
                }

            case SearchPrinciple.MULTI_SEARCH_AND_SWITCH:
                int roleM = seeker.getRole();
                if (roleM == Seeker.ROLE_CHAIRMAN) {
                    int membersNumber = temp.record.size();
                    List<ResultSetMetaData> searchResults = retrievalManager.getSearcherManager().search(searchers, principle, query, docTypes, fields, caseSensitive, membersNumber);

                    if (searchResults.isEmpty()) {
                        this.updateRecordSession(query, new ResultSetMetaData(query), 0, seeker, recordSession);
                        this.notifySearchResults(INDIVIDUAL_SEARCH, true, null, seeker, seekerPrx);
                    } else {
                        notifySearchResults(query, searchResults, seeker, recordSession, "Multi Search and Switch");
                        updateResultsStore(searchResults);
                    }
                    break;
                } else {
                    throw new SeekerException("Doesn't have privileges to executing this search");
                }
            default:
                throw new IllegalArgumentException("Division of labor principle is not supported.");
        }
    }

    //svn search
    public void executeSearch(String sessionName, String query, String svnRepository, String fileType, String sort, String lastmodified, boolean fileBody, Seeker seeker, ClientSidePrx seekerPrx) throws SessionException, SeekerException, SearchableException, SearchException, IOException {
        SessionProfile recordSession = this.getSessionProfile(sessionName);
        SeekerInfo temp = recordSession.getSeekerInfo();
        this.validate(sessionName, seeker, temp);
        ResultSetMetaData resultsList;

        SVNData data = this.getSVNData(svnRepository);
        resultsList = retrievalManager.getSearcherManager().search(query, data, fileType, sort, lastmodified, seeker.getUser(), fileBody);
        if (resultsList.isEmpty()) {
            this.updateRecordSession(query, resultsList, 0, seeker, recordSession);
            this.notifySearchResults(INDIVIDUAL_SEARCH, true, null, seeker, seekerPrx);
        } else {
            int documentsCount = resultsList.getDocumentsCount();
            this.updateRecordSession(query, resultsList, documentsCount, seeker, recordSession);
            this.notifySearchResults(INDIVIDUAL_SEARCH, false, resultsList, seeker, seekerPrx);
            updateResultsStore(resultsList);
        }


    }
    ///////////////////*******Métodos Utilitarios de las búsquedas*****//////////////////

    /**
     * Este método es empleado para actualizar el registro de búsquedas efectuadas
     * por un seeker sin aplicar mecanismos de división del  trabajo.
     *
     * @param query           término de la consulta.
     * @param list   resultados de la búsqueda.
     * @param documentsCount  cantidad de documentos encontrados
     * @param emitter          miembro que realiza la búsqueda.
     * @param recordSession   registro de la sesión.
     */
    private void updateRecordSession(String query, ResultSetMetaData list, int documentsCount, Seeker seeker, SessionProfile recordSession) {

        SeekerSearchResults searchTemp = recordSession.getSeekerSearchResults();
        //si existen resultados de búsq. para la sesión especificada
        boolean flag = !searchTemp.record.isEmpty();

        if (flag) {

            updateRecordSession(query, list, seeker, searchTemp);

        } else {

            // si no existen registros de búsquedas para la sesión especificada se
            // procede a registrar la misma para almacenar los resultados del miembro.
            SearchData searchData = searchTemp.new SearchData();
            SearchResults searchResults = searchTemp.new SearchResults();
            Map<Integer, List<DocumentMetaData>> hash = list.getResultsMap();
            Set<Integer> searchableEnum = hash.keySet();
            for (Integer searcher : searchableEnum) {
                searchResults.results.put(searcher, searchTemp.new IndexMetaDoc(hash.get(searcher)));
            }
            searchData.values.put(query, searchResults);
            // se registra el miembro con los resultados obtenidos
            searchTemp.record.put(seeker, searchData);
        }

        this.updateRecordSession(query, documentsCount, recordSession);


    }

    /**
     * Este método es empleado para actualizar el registro de cada miembro de la sesión
     * colaborativa, una vez que se invoca una búsqueda aplicando mecanismos de división
     * de trabajo.
     *
     * @param query       término de la consulta
     * @param list        lista de resultados.
     * @param emitter      miembro al cual actualizar su registro.
     * @param searchTemp  registro de búsquedas de la sesión a la que pertenece el usuario.
     */
    private void updateRecordSession(String query, ResultSetMetaData list, Seeker seeker, SeekerSearchResults searchTemp) {
        SearchData searchData;
        // si el miembro cuenta con resultados anteriores, se le adicionan
        // los nuevos resultados
        boolean flag = searchTemp.record.containsKey(seeker);
        if (flag) {
            //se obtienen los resultados de búsquedas anteriores del miembro
            searchData = searchTemp.record.get(seeker);
            SearchResults searchResults = null;

            if (searchData.values.containsKey(query)) {
                searchResults = searchData.values.get(query);


                Map<Integer, List<DocumentMetaData>> hash = list.getResultsMap();

                IndexMetaDoc indexMDoc;
                Set<Integer> searchableEnum = hash.keySet();
                for (Integer searcher : searchableEnum) {
                    if (searchResults.results.containsKey(searcher)) {
                        indexMDoc = searchResults.results.get(searcher);
                        indexMDoc.insertMetaDoc(hash.get(searcher));
                    } else {
                        indexMDoc = searchTemp.new IndexMetaDoc(hash.get(searcher));
                        searchResults.results.put(searcher, indexMDoc);
                    }
                }

            } else {
                searchResults = searchTemp.new SearchResults(list.getResultsMap());
                searchData.values.put(query, searchResults);
            }

        } else {
            // en caso de que el miembro no cuente resultados previos, se
            // crea una nueva lista de resultados y se le agregan los resultados obtenidos
            searchData = searchTemp.new SearchData();
            SearchResults searchResults = searchTemp.new SearchResults(list.getResultsMap());
            searchData.values.put(query, searchResults);
            // se registra el miembro con los resultados obtenidos en la tabla
            // hash de los resultados de la búsquedas
            searchTemp.record.put(seeker, searchData);
        }

    }

    // act. mecanismos de división del trabajo
    /**
     * Este método es empleado cuando se realiza una búsqueda, aplicando métodos de
     * división del trabajo. Actualiza los registro de cada usuario y de la sesión.
     *
     * @param query          término de consulta.
     * @param list  listado de los resultados de la búsqueda.
     * @param emitter         miembro que realiza la búsqueda.
     * @param recordSession  registro de la sesión que se afecta.
     * @throws IOException
     */
    private void notifySearchResults(String query, List<ResultSetMetaData> searchResults, Seeker emitter, SessionProfile recordSession, String principle) throws IOException {
        SeekerSearchResults searchTemp = recordSession.getSeekerSearchResults();
        ResultSetMetaData list = null;

        //si existen resultados de búsq. para la sesión especificada
        boolean flag = !searchTemp.record.isEmpty();
        int documentsCount = 0;
        if (flag) {
            // se obtiene los miembros
            Set<Seeker> enMembers = recordSession.getSeekerInfo().record.keySet();
            int i = 0;
            for (Seeker receptor : enMembers) {
                  list = searchResults.get(i);
                documentsCount += list.getDocumentsCount();
                i++;

                // actualiza ó crea el registro del miembro.
                ClientSidePrx prx = recordSession.getSeekerInfo().record.get(receptor);
                this.notifySearchResults(emitter, list, receptor, prx);
                this.updateRecordSession(query, list, receptor, searchTemp);

                //BD
                this.saveSearchDB(recordSession.getProperties().getSessionName(), receptor, list, principle);
            }
            

        } else {
            // si no existen registros de búsquedas para la sesión especificada se
            // procede a registrar la misma para almacenar los resultados del miembro.
            int j = 0;
            flag = false;
            SeekerInfo recMembers = recordSession.getSeekerInfo();
            Set<Seeker> enMembers = recMembers.record.keySet();
           
            ClientSidePrx prx;
            for (Seeker receptor : enMembers) {
                list = searchResults.get(j);
                documentsCount += list.getDocumentsCount();
                j++;

                SearchResults data = searchTemp.new SearchResults(list.getResultsMap());
                SearchData searchData = searchTemp.new SearchData(query, data);
                // se registra el miembro con los resultados obtenidos
                searchTemp.record.put(receptor, searchData);
                // noticaficación al miembro
                prx = recordSession.getSeekerInfo().record.get(receptor);
                this.notifySearchResults(emitter, list, receptor, prx);

                this.saveSearchDB(recordSession.getProperties().getSessionName(), receptor, list, principle);
            }

            }
            

        // actualizando registro de la sesión
        this.updateRecordSession(query, documentsCount, recordSession);

    }

    /**
     * Este método actualiza el registro de la sesión.
     * @param query           término de la consulta.
     * @param documentsCount  cantidad de documentos encontrados para la consulta
     * @param recordSession   registro de la sesión.
     */
    public void updateRecordSession(String query, long documentsCount, SessionProfile recordSession) {
        recordSession.getQuerys().put(query, documentsCount);
        int count = recordSession.getSearchesCount();
        count++;

        searchesCount++;

        if (this.listener != null) {
            this.listener.notifyDoneSearch();
        }

    }

    /**
     * Este método se emplea para notificar los resultados de la búsqueda al miembro
     *
     * @param emitter     nombre del miembro que invocó la búsqueda
     * @param isEmpty        true si no se encontraron resultados para consulta, false en caso contrario.
     * @param list  lista con los resultados de la búsqueda.
     * @param receptorPrx      objeto proxy del miembro a notificar
     * @throws IOException
     */
    private void notifySearchResults(Seeker emitter, ResultSetMetaData searchResults, Seeker receptor, ClientSidePrx receptorPrx) throws IOException {

        Response rsp = ResponseSearchFactory.getResponse(emitter, searchResults);
        // se notifica los resultados de la búsqueda al miembro
        byte[] array = rsp.toArray();
        try {
            receptorPrx.notify_async(new NotifyAMICallback(receptor, "notifySearchResults"), array);
        } catch (NullPointerException e) {
        }

    }

    private void notifySearchResults(int searchType, boolean isEmpty, ResultSetMetaData searchResults, Seeker seeker, ClientSidePrx seekerPrx) throws IOException {

        // se crea el objeto response
        Response rsp = ResponseSearchFactory.getResponse(searchType, isEmpty, searchResults);
        // se notifica los resultados de la búsqueda al miembro
        byte[] array = rsp.toArray();

        try {
            seekerPrx.notify_async(new NotifyAMICallback(seeker, "notifySearchResults"), array);
        } catch (NullPointerException e) {
        }

    }

    private void validate(String sessionName, Seeker seeker, SeekerInfo recordMembers) throws SessionException, SeekerException {

        boolean flag = recordMembers.record.containsKey(seeker);

        if (!flag) {
            throw new SeekerException("The seeker '" + seeker.getUser() + "' is not registered in the session '" + sessionName + "'.");
        }

    }

    /**
     * {@inheritDoc}
     */
    public long getSearchesCount() {
        return this.searchesCount;
    }

    /**
     * {@inheritDoc}
     */
    public long getSearchesCount(String sessionName) throws SessionException {

        SessionProfile recordSession = this.getSessionProfile(sessionName);
        int count = recordSession.getSearchesCount();
        return count;

    }

    /**
     * {@inheritDoc}
     */
    public long getSearchesCount(String sessionName, Seeker seeker) throws SessionException, SeekerException {

        SessionProfile sessionProfile = this.getSessionProfile(sessionName);
        SeekerSearchResults searches = sessionProfile.getSeekerSearchResults();
        boolean flag = searches.record.containsKey(seeker);
        if (flag) {
            SearchData results = searches.record.get(seeker);
            int count = results.values.size();
            return count;
        } else {
            throw new SeekerException("The seeker '" + seeker.getUser() + "' is not registered in the session '" + sessionName + "'.");
        }
    }

    /**
     * Devuelve la instacia del objeto RetrievalManager
     *
     * @return objeto
     */
    public RetrievalManager getSearchManager() {
        return retrievalManager;
    }

    /**
     * Modifica la instacia del objeto RetrievalManager
     *
     * @param retrievalManager nueva instancia
     */
    public void setSearchManager(RetrievalManager retrievalManager) {
        this.retrievalManager = retrievalManager;
    }

    /////////////SAVE IN DB///////////////////////
    /**
     *
     * @param sessionName
     * @param seeker 
     * @param emitter
     * @param resultsList
     * @param principle 
     */
    public void saveSearchDB(final String sessionName, final Seeker seeker, final ResultSetMetaData resultsList, final String principle) {

        Thread thread = new Thread(new Runnable() {

            public void run() {
                if (dbController != null && dbController.isOpen()) {
                    try {
                        dbController.saveSearch(sessionName, principle, seeker.getUser(), resultsList);
                    } catch (SQLException ex) {
                        OutputMonitor.printStream("Searchable", ex);
                    }
                }
            }
        });
        thread.start();
    }

    public SVNData getSVNData(final String repositoryName) {

        SVNData data = new SVNData();
        if (dbController != null && dbController.isOpen()) {
            try {
                data = dbController.getSVNRepositoryData(repositoryName, SVN_SEARCHER);
            } catch (SQLException ex) {
                OutputMonitor.printStream("Searchable", ex);
            }
        }

        return data;
    }

    private int getSearchPrinciple(String principle) {
        switch (principle) {
            case "Single Search":
                return SearchPrinciple.SINGLE_SEARCH;
            case "Single Search and Split":
                return SearchPrinciple.SINGLE_SEARCH_AND_SPLIT;
            case "Meta Search":
                return SearchPrinciple.META_SEARCH;
            case "Meta Search and Split":
                return SearchPrinciple.META_SEARCH_AND_SPLIT;
            case "Multi Search":
                return SearchPrinciple.MULTI_SEARCH;
            case "Multi Search and Switch":
                return SearchPrinciple.MULTI_SEARCH_AND_SWITCH;
            default:
                return 0;
        }

    }
}
