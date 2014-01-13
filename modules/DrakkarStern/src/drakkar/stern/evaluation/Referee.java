/*
 * DrakkarKeel - An Enterprise Collaborative Search Platform
 *
 * The contents of this file are subject under the terms described in the
 * DRAKKARKEEL_LICENSE file included in this distribution; you may not use this
 * file except in compliance with the License. 
 *
 * 2013-2014 DrakkarKeel Platform.
 */
package drakkar.stern.evaluation;

import drakkar.oar.DocumentMetaData;
import drakkar.oar.Seeker;
import drakkar.oar.SessionProperty;
import drakkar.oar.exception.QueryNotExistException;
import drakkar.oar.exception.SeekerException;
import drakkar.oar.exception.SessionException;
import drakkar.oar.util.OutputMonitor;
import drakkar.mast.SearchableException;
import drakkar.stern.controller.DataBaseController;
import drakkar.stern.facade.event.FacadeListener;
import drakkar.stern.servant.service.Service;
import drakkar.stern.tracker.cache.SessionProfile;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Esta clase implementa los diferentes métodos de evaluación soportados por el
 * Framework DrakkarKeel
 */
public class Referee extends Service implements Evaluable {

    /**
     * Constructor de la clase
     *
     * @param defaultSessionName
     * @param collaborativeSessions listado de sesiones
     * @param defaultSessionProfile
     */
    public Referee(String defaultSessionName, SessionProfile defaultSessionProfile, Map<String, SessionProfile> collaborativeSessions, Map<String, SessionProfile> htTempSessions) {
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
    public Referee(String defaultSessionName, SessionProfile defaultSessionProfile, Map<String, SessionProfile> collaborativeSessions, Map<String, SessionProfile> htTempSessions, DataBaseController dbController) {
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
    public Referee(String defaultSessionName, SessionProfile defaultSessionProfile, Map<String, SessionProfile> collaborativeSessions, Map<String, SessionProfile> htTempSessions, FacadeListener listener, DataBaseController dbController) {
        super(defaultSessionName, defaultSessionProfile, collaborativeSessions, htTempSessions, listener, dbController);
    }

    public List<DocumentMetaData> getRelevantDocuments(String sessionName, String query, int searcher) throws SessionException, QueryNotExistException, SearchableException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<DocumentMetaData> getRelevantDocuments(String sessionName, String query, int[] searchers) throws SessionException, QueryNotExistException, SearchableException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<DocumentMetaData> getRelevantDocuments(String sessionName, Seeker seeker, String query, int searcher) throws SessionException, QueryNotExistException, SeekerException, SearchableException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<DocumentMetaData> getViewedDocuments(String sessionName, String query, int[] searchers) throws SessionException, QueryNotExistException, SearchableException {
        return null;
    }

    public List<DocumentMetaData> getViewedDocuments(String sessionName, String query, int searcher) throws SessionException, QueryNotExistException, SearchableException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<DocumentMetaData> getViewedDocuments(String sessionName, Seeker seeker, String query, int searcher) throws SessionException, QueryNotExistException, SeekerException, SearchableException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<DocumentMetaData> getRetrievedDocuments(String sessionName, String query, int searcher) throws SessionException, QueryNotExistException, SearchableException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<DocumentMetaData> getRetrievedDocuments(String sessionName, Seeker seeker, String query, int searcher) throws SessionException, QueryNotExistException, SeekerException, SearchableException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public long getRetrievedDocumentsCount(String sessionName, String query) throws SessionException, QueryNotExistException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<String> getQuerys(String sessionName) throws SessionException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public long getDurationSessionTime(String sessionName) throws SessionException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public SessionProperty getSessionProperties(String sessionName) throws SessionException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Esta tabla hash almacena temporalmente hasta que se establesca la persistencia
     * la información relacionada con cada sesión finalizada
     */
    private Map<String, SessionProfile> htTempSessions;
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
    private String relvJudg = "";
//
//    /**
//     * {@inheritDoc}
//     */
//    public List<DocumentMetaData> getRelevantDocuments(String sessionName, String query, int[] searchers) throws SessionException, QueryNotExistException, SearchableException {
//
//        // Esta lista almacenará lista de documentos revisados por todos los
//        // miembros de la sesión
//        List<DocumentMetaData> list = new List<DocumentMetaData>();
//        boolean flag = this.htTempSessions.containsKey(sessionName);
//        if (flag) {
//            SessionProfile recordSession = this.htTempSessions.get(sessionName);
//            // se obtiene el registro de evaluaciones de la sesión
//            SelectedDocuments recordEvaluations = recordSession.getSelectedDocuments();
//
//            flag = !recordEvaluations.record.isEmpty();
//            if (flag) { // if # 1
//
//                // variable temporal para almacenar los documentos revisados por cada
//                // miembro de la sesión
//                DocumentsList temp = null;
//                List<Integer> indexList;
//                Enumeration<Seeker> members = recordEvaluations.record.keys();
//                Seeker seeker;
//                Evaluations evaluations;
//
//                Evaluations.Evaluation eval;
//                Enumeration<Byte> enScore;
//                Enumeration<Integer> enIndex;
//
//                while (members.hasMoreElements()) {  // while # 1
//                    seeker = members.nextElement();
//                    // se obtienen las evaluaciones realizadas por el miembro actual
//                    evaluations = recordEvaluations.record.get(seeker);
//                    flag = evaluations.record.containsKey(query);
//                    if (flag) { // if # 2
//                        // se obtienen las evaluaciones realizadas para la query
//                        indexList = new List<Integer>();
//                        eval = evaluations.record.get(query);
//                        Collection<Data> dataRec = eval.evaluation.values();
//                        for (Integer searcher : searchers) {
//
//                            RelevanceDocs data = null;
//
//                            for (Data data1 : dataRec) {
//                                data = data1.values.get(searcher);
//                            }
//
//
//                            if (data == null) {
//                                enIndex = data.values.keys();
//                                enScore = data.values.elements();
//                                Integer index;
//                                Byte score;
//                                // se obtienen índices de los documentos evaluados como relevantes
//
//                                while (enIndex.hasMoreElements()) { // while # 2
//                                    index = enIndex.nextElement();
//                                    score = enScore.nextElement();
//                                    if (score >= this.relevScore) {
//                                        indexList.add(index);
//                                    }
//
//                                }// end while # 2
//                                // se obtienen los documentos evaluados como relevantes.
//
//                                try {
//                                    Map<Integer, List<Integer>> d = new Map<Integer, List<Integer>>(1);
//                                    d.put(searcher, indexList);
//                                    temp = getDocuments(recordSession, seeker, new Documents(d), query);
//                                    // en esta lista se van adicionando los resultados de todos miembros
//                                    list.addAll(temp.getResultsList());
//                                } catch (SeekerException ex) {
//                                }
//                            }
//                        }
//
//                    } // end if # 2
//
//                }// end while # 1
//            } else {
//                return new List<DocumentMetaData>(0);
//            }
//        } else {
//            throw new SessionException("The session '" + sessionName + "' doesn't exist.");
//        }
//
//
//        flag = list.isEmpty();
//        if (flag) {
//            throw new QueryNotExistException("The query doesn't find registered for no seeker of the session '" + sessionName + "'.");
//        } else {
//            return list;
//        }
//
//    }
//
//    /**
//     * {@inheritDoc}
//     */
//    public List<DocumentMetaData> getRelevantDocuments(String sessionName, String query, int searcher) throws SessionException, QueryNotExistException, SearchableException, SearchableException {
//
//        // Esta lista almacenará lista de documentos revisados por todos los
//        // miembros de la sesión
//        List<DocumentMetaData> list = new List<DocumentMetaData>();
//        boolean flag = this.htTempSessions.containsKey(sessionName);
//        if (flag) {
//            SessionProfile recordSession = this.htTempSessions.get(sessionName);
//            // se obtiene el registro de evaluaciones de la sesión
//            SelectedDocuments recordEvaluations = recordSession.getSelectedDocuments();
//
//            flag = !recordEvaluations.record.isEmpty();
//            if (flag) { // if # 1
//
//                // variable temporal para almacenar los documentos revisados por cada
//                // miembro de la sesión
//                DocumentsList temp = null;
//                List<Integer> indexList;
//                Enumeration<Seeker> members = recordEvaluations.record.keys();
//                Seeker seeker;
//                Evaluations evaluations;
//
//                Evaluations.Evaluation eval;
//                Enumeration<Byte> enScore;
//                Enumeration<Integer> enIndex;
//
//                while (members.hasMoreElements()) {  // while # 1
//                    seeker = members.nextElement();
//                    // se obtienen las evaluaciones realizadas por el miembro actual
//                    evaluations = recordEvaluations.record.get(seeker);
//                    flag = evaluations.record.containsKey(query);
//                    if (flag) { // if # 2
//                        // se obtienen las evaluaciones realizadas para la query
//                        eval = evaluations.record.get(query);
//                        indexList = new List<Integer>();
//                        Collection<Data> dataRec = eval.evaluation.values();
//
//                        RelevanceDocs data = null;
//
//                        for (Data data1 : dataRec) {
//                            data = data1.values.get(searcher);
//                        }
//                        if (data == null) {
//                            enIndex = data.values.keys();
//                            enScore = data.values.elements();
//                            Integer index;
//                            Byte score;
//                            // se obtienen índices de los documentos evaluados como relevantes
//
//                            while (enIndex.hasMoreElements()) { // while # 2
//                                index = enIndex.nextElement();
//                                score = enScore.nextElement();
//                                if (score >= this.relevScore) {
//                                    indexList.add(index);
//                                }
//
//                            }// end while # 2
//                            // se obtienen los documentos evaluados como relevantes.
//
//
//                            try {
//                                Map<Integer, List<Integer>> d = new Map<Integer, List<Integer>>(1);
//                                d.put(searcher, indexList);
//                                temp = getDocuments(recordSession, seeker, new Documents(d), query);
//                                // en esta lista se van adicionando los resultados de todos miembros
//                                list.addAll(temp.getResultsList());
//                            } catch (SeekerException ex) {
//                            }
//                        }
//                    } // end if # 2
//                }// end while # 1
//            } else {
//                return new List<DocumentMetaData>(0);
//            }
//        } else {
//            throw new SessionException("The session '" + sessionName + "' doesn't exist.");
//        }
//
//
//        flag = list.isEmpty();
//        if (flag) {
//            throw new QueryNotExistException("The query doesn't find registered for no seeker of the session '" + sessionName + "'.");
//        } else {
//            return list;
//        }
//
//
//    }
//
//    /**
//     * {@inheritDoc}
//     * @throws SeekerException
//     */
//    public List<DocumentMetaData> getRelevantDocuments(String sessionName, Seeker seeker, String query, int searcher) throws SessionException, QueryNotExistException, SeekerException, SearchableException {
//
//        boolean flag = this.htTempSessions.containsKey(sessionName);
//        if (flag) {
//            List<DocumentMetaData> list = null;
//            DocumentsList temp = null;
//            List<Integer> indexList;
//            SessionProfile recordSession = this.htTempSessions.get(sessionName);// se obtiene el registro de evaluaciones de la sesión
//            SelectedDocuments recordEvaluations = recordSession.getSelectedDocuments(); // se obtiene el registro de evaluaciones de la sesión
//
//            flag = !recordEvaluations.record.isEmpty();
//            if (flag) {
//                Evaluations evaluations;
//                Evaluations.Evaluation eval;
//                // se comprueba que el miembro este registrado en la sesión
//                if (recordEvaluations.record.containsKey(seeker)) {
//                    evaluations = recordEvaluations.record.get(seeker); // se obtienen las evaluaciones realizadas por el miembro actual
//
//                    if (evaluations.record.containsKey(query)) {     // se comprueba que el miembro halla efectuado la consulta
//                        eval = evaluations.record.get(query); // se obtienen las evaluaciones realizadas para la query
//                        indexList = new List<Integer>();
//                        Collection<Data> dataRec = eval.evaluation.values();
//
//                        RelevanceDocs data = null;
//
//                        for (Data data1 : dataRec) {
//                            data = data1.values.get(searcher);
//                        }
//                        if (data == null) {
//                            throw new SearchableException("The searcher '" + sessionName + "' doesn't sopported.");
//                        }
//
//                        Enumeration<Integer> enIndex = data.values.keys();
//                        Enumeration<Byte> enScore = data.values.elements();
//
//                        Integer index; // variable temporal
//                        Byte score;    // variable temporal
//                        // se obtienen índices de los documentos evaluados como relevantes
//                        while (enIndex.hasMoreElements()) {
//                            index = enIndex.nextElement();
//                            score = enScore.nextElement();
//                            if (score >= this.relevScore) {
//                                indexList.add(index);
//                            }
//                        }// end while
//
//                        Map<Integer, List<Integer>> d = new Map<Integer, List<Integer>>(1);
//                        d.put(searcher, indexList);
//                        temp = getDocuments(recordSession, seeker, new Documents(d), query);
//                        list = temp.getResultsList();
//                        return list;
//
//                    } else {
//                        throw new QueryNotExistException("The seeker '" + seeker.getUser() + "' doesn't have was  registered the query '" + query + "'.");
//                    }
//                } else {
//                    throw new SeekerException("The seeker '" + seeker.getUser() + "' is not registered in the session '" + sessionName + "'.");
//                }
//            } else {
//                return new List<DocumentMetaData>(0);
//            }
//        } else {
//            throw new SessionException("The session '" + sessionName + "' doesn't exist.");
//        }
//
//
//    }
//
//    /**
//     * {@inheritDoc}
//     */
//    public List<DocumentMetaData> getViewedDocuments(String sessionName, String query, int[] searchers) throws SessionException, QueryNotExistException {
//
//        List<DocumentMetaData> list = new List<DocumentMetaData>();
//        boolean flag = this.htTempSessions.containsKey(sessionName);
//        if (flag) {
//            DocumentsList temp = null;
//            List<Integer> index;
//            SessionProfile recordSession = this.htTempSessions.get(sessionName);
//            ViewedDocuments recordCheckups = recordSession.getViewedDocuments();  // se obtiene el registro de revisiones de la sesión
//            flag = !recordCheckups.record.isEmpty();
//
//            if (flag) {
//                Enumeration<Seeker> members = recordCheckups.record.keys();
//                Seeker seeker;
//                String time;
//                Checkups checkups;
//                Checkups.Data data;
//                while (members.hasMoreElements()) {
//                    seeker = members.nextElement();
//                    checkups = recordCheckups.record.get(seeker);
//                    data = checkups.record.get(query);
//
//                    Collection<EngineResults> dataRec = data.values.values();
//                    Enumeration<String> times = data.values.keys();
//                    for (Integer searcher : searchers) {
//
//                        for (EngineResults data1 : dataRec) {
//                            time = times.nextElement();
//                            index = data1.results.get(searcher);
//                            if (index == null) {
//                                try {
//
//                                    Map<Integer, List<Integer>> d = new Map<Integer, List<Integer>>(1);
//                                    d.put(searcher, index);
//                                    temp = getDocuments(recordSession, seeker, new Documents(d), query);
//
//                                } catch (SeekerException ex) {
//                                    Logger.getLogger(Referee.class.getName()).log(Level.SEVERE, null, ex);
//                                }
//                                // adicionan los resultados de todos miembros
//                                list.addAll(temp.getResultsList());
//                            }
//                        }
//                    }
//                }
//                return list;
//
//            } else {
//                return null;
//            }
//        } else {
//            throw new SessionException("The session '" + sessionName + "' doesn't exist.");
//        }
//    }
//
//    /**
//     * {@inheritDoc}
//     */
//    public List<DocumentMetaData> getViewedDocuments(String sessionName, String query, int searcher) throws SessionException, QueryNotExistException, SearchableException {
//
//        List<DocumentMetaData> list = new List<DocumentMetaData>();
//        boolean flag = this.htTempSessions.containsKey(sessionName);
//        if (flag) {
//            DocumentsList temp = null;
//            List<Integer> index;
//            SessionProfile recordSession = this.htTempSessions.get(sessionName);
//            ViewedDocuments recordCheckups = recordSession.getViewedDocuments();  // se obtiene el registro de revisiones de la sesión
//            flag = !recordCheckups.record.isEmpty();
//
//            if (flag) {
//                Enumeration<Seeker> members = recordCheckups.record.keys();
//                Seeker seeker;
//
//                Checkups checkups;
//                Checkups.Data data;
//                while (members.hasMoreElements()) {
//                    seeker = members.nextElement();
//                    checkups = recordCheckups.record.get(seeker);
//                    data = checkups.record.get(query);
//                    index = data.values.get(searcher);
//                    if (index == null) {
//                        throw new SearchableException("The searcher '" + sessionName + "' doesn't sopported.");
//                    }
//
//                    try {
//
//                        Map<Integer, List<Integer>> d = new Map<Integer, List<Integer>>(1);
//                        d.put(searcher, index);
//                        temp = getDocuments(recordSession, seeker, new Documents(d), query);
//
//                    } catch (SeekerException ex) {
//                        Logger.getLogger(Referee.class.getName()).log(Level.SEVERE, null, ex);
//                    }
//                    // adicionan los resultados de todos miembros
//                    list.addAll(temp.getResultsList());
//                }
//                return list;
//
//            } else {
//                return null;
//            }
//        } else {
//            throw new SessionException("The session '" + sessionName + "' doesn't exist.");
//        }
//    }
//
//    /**
//     * {@inheritDoc}
//     * @throws SeekerException
//     */
//    public List<DocumentMetaData> getViewedDocuments(String sessionName, Seeker seeker, String query, int searcher) throws SessionException, QueryNotExistException, SeekerException, SearchableException {
//
//        boolean flag = this.htTempSessions.containsKey(sessionName);
//        if (flag) {
//            SessionProfile recordSession = this.htTempSessions.get(sessionName);
//            // se obtiene el registro de revisiones de la sesión
//            ViewedDocuments recordCheckups = recordSession.getViewedDocuments();
//            flag = recordCheckups.record.containsKey(seeker);
//
//            if (flag) {
//                List<DocumentMetaData> list = null;
//                DocumentsList temp = null;
//                List<Integer> index;
//                Checkups checkups;
//                Checkups.Data data;
//                checkups = recordCheckups.record.get(seeker);
//                flag = checkups.record.containsKey(query);
//                if (flag) {
//
//                    data = checkups.record.get(query);
//                    index = data.values.get(searcher);
//                    if (index == null) {
//                        throw new SearchableException("The searcher '" + sessionName + "' doesn't sopported.");
//                    }
//
//                    temp = getDocuments(sessionName, seeker, index, query, searcher);
//                    list = temp.getResultsList();
//                    return list;
//                } else {
//                    throw new QueryNotExistException("The seeker '" + seeker.getUser() + "' doesn't have was  registered the query '" + query + "'.");
//                }
//            } else {
//                throw new SeekerException("The seeker '" + seeker.getUser() + "' is not registered in the session '" + sessionName + "'.");
//            }
//        } else {
//            throw new SessionException("The session '" + sessionName + "' doesn't exist.");
//        }
//    }
//
//    /**
//     * {@inheritDoc}
//     */
//    public List<DocumentMetaData> getRetrievedDocuments(final String sessionName, final String query, final int searcher) throws SessionException, QueryNotExistException, SearchableException {
//
//        List<DocumentMetaData> list = null;
//        if (this.htTempSessions.containsKey(sessionName)) {
//            SessionProfile recordSession = this.htTempSessions.get(sessionName);
//            // se obtiene el registro de revisiones de la sesión
//            SeekerSearchResults recordSearches = recordSession.getSeekerSearchResults();
//
//            boolean flag = !recordSearches.record.isEmpty();
//            if (flag) {
//                Enumeration<Seeker> members = recordSearches.record.keys();
//                Seeker seeker;
//                // Esta lista almacenará lista de documentos recuperados por todos los
//                // miembros de la sesión
//                list = new List<DocumentMetaData>();
//                List<DocumentMetaData> listTemp;
//
//                SeekerSearchResults.Results results;
//                while (members.hasMoreElements()) {
//                    seeker = members.nextElement();
//                    results = recordSearches.record.get(seeker);
//                    flag = results.record.containsKey(query);
//                    if (flag) {
//                        Results.Data data = results.record.get(query);
//                        listTemp = data.getMetaDocuments(searcher);
//                        list.addAll(listTemp); // adicionan los resultados de todos miembros
//
//                    } else {
//                        throw new QueryNotExistException("The query '" + query + "' doesn'nt registered.");
//                    }
//                }
//            } else {
//                return null;
//            }
//        } else {
//
//            //BD
//            if (this.dbController.isOpen()) {
//                try {
//                    list = this.dbController.getSearchResults(query, sessionName, searcher);
//                } catch (SQLException ex) {
//                    Logger.getLogger(Referee.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            }
//
//
//            //throw new SessionException("The session '" + sessionName + "' doesn't exist.");
//        }
//
//        return list;
//    }
//
//    /**
//     * Devuelve los documentos reuperados por un miembro para una
//     * consulta determinada.
//     *
//     * @param sessionName  nombre de la sesión
//     * @param seeker      miembro del cual se solicita la información.
//     * @param query        consulta de la búsqueda
//     *
//     * @param searcher
//     * @return lista de documentos recuperados.
//     *
//     * @throws SessionException   si la sesión no se encuentra registrada
//     *                                    en el servidor
//     * @throws QueryNotExistException     si la consulta especificada no se encuentra
//     *                                    registrada en la sesión
//     * @throws SeekerException
//     */
//    public List<DocumentMetaData> getRetrievedDocuments(String sessionName, Seeker seeker, String query, int searcher) throws SessionException, QueryNotExistException, SeekerException, SearchableException {
//
//        boolean flag = this.htTempSessions.containsKey(sessionName);
//        if (flag) {
//            SessionProfile recordSession = this.htTempSessions.get(sessionName);
//            // se obtiene el registro de resultados de búsqueda de la sesión
//            SeekerSearchResults recordSearches = recordSession.getSeekerSearchResults();
//            flag = recordSearches.record.containsKey(seeker);
//
//            if (flag) {
//
//                // Esta lista almacenará lista de documentos recuperados por todos los
//                // miembros de la sesión
//                List<DocumentMetaData> list = new List<DocumentMetaData>();
//                // variable temporal para almacenar los documentos recuperados por cada
//                // miembro de la sesión
//                DocumentsList temp = null;
//                SeekerSearchResults.Results results;
//                results = recordSearches.record.get(seeker);
//                flag = results.record.containsKey(query);
//                if (flag) {
//                    Results.Data data = results.record.get(query);
//                    list = data.getMetaDocuments(searcher);
//                    return list;
//                } else {
//                    throw new QueryNotExistException("The seeker '" + seeker.getUser() + "' doesn't have was  registered the query '" + query + "'.");
//                }
//            } else {
//                throw new SeekerException("The seeker '" + seeker.getUser() + "' doesn'nt registered in the session '" + sessionName + "'.");
//            }
//        } else {
//            throw new SessionException("The session '" + sessionName + "' doesn't exist.");
//        }
//    }
//
//    /**
//     * {@inheritDoc}
//     */
//    public long getRetrievedDocumentsCount(String sessionName, String query) throws SessionException, QueryNotExistException {
//
//        boolean flag = this.htTempSessions.containsKey(sessionName);
//        if (flag) {
//            SessionProfile recordSession = this.htTempSessions.get(sessionName);
//            Map<String, Long> querys = recordSession.getQuerys();
//            flag = querys.containsKey(query);
//
//            if (flag) {
//                long count = querys.get(query);
//                return count;
//            } else {
//                throw new QueryNotExistException("The query '" + query + "' doesn'nt registered.");
//            }
//        } else {
//            throw new SessionException("The session '" + sessionName + "' doesn't exist.");
//        }
//    }
//
//    /**
//     * {@inheritDoc}
//     */
//    public List<String> getQuerys(String sessionName) throws SessionException {
//
//        boolean flag = this.htTempSessions.containsKey(sessionName);
//        if (flag) {
//            SessionProfile recordSession = this.htTempSessions.get(sessionName);
//            Enumeration<String> keys = recordSession.getQuerys().keys();
//            List<String> querys = new List<String>();
//            String string;
//            while (keys.hasMoreElements()) {
//                string = keys.nextElement();
//                querys.add(string);
//            }
//            return querys;
//        } else {
//            throw new SessionException("The session '" + sessionName + "' not exist.");
//        }
//
//    }
//
//    /**
//     * {@inheritDoc}
//     */
//    public long getDurationSessionTime(String sessionName) throws SessionException {
//        boolean flag = this.htTempSessions.containsKey(sessionName);
//        if (flag) {
//            SessionProfile recordSession = this.htTempSessions.get(sessionName);
//            Date end = recordSession.getStopDate();
//            Date begin = recordSession.getStartDate();
//            long time = end.getTime() - begin.getTime();
//            return time;
//        } else {
//            throw new SessionException("The session '" + sessionName + "' not exist.");
//        }
//
//    }
//
//    /**
//     * {@inheritDoc}
//     */
//    public SessionProperty getSessionProperties(String sessionName) throws SessionException {
//
//        if (this.collaborativeSessions.containsKey(sessionName)) {
//            SessionProfile recordSession = this.collaborativeSessions.get(sessionName);
//            SessionProperty properties = recordSession.getProperties();
//
//            return properties;
//
//        } else {
//            throw new SessionException("The session " + sessionName + " not exist.");
//        }
//    }
//
////    /**
////     * Devuelve una selección de los resultados de una búsqueda de un usuario para
////     * una consulta y buscador determinado
////     *
////     * @param sessionName    nombre de la sesión
////     * @param emitter        usuario que realiza la recomendación
////     * @param index          índices de los documentos seleccionados
////     * @param query          consulta de la búsqueda
////     * @param searcher       buscador
////     *
////     * @return results documentos
////     *
////     * @throws SessionException si la sesión no se encuentra registrada
////     *                                  en el servidor
////     * @throws UserNotExistException    si el usuario que invoca la operación no
////     *                                  existe
////     * @throws QueryNotExistException   si la consulta no se encuentra registrada
////     *                                  en los registros del emisor
////     */
////    private DocumentsList getDocuments(String sessionName, Seeker emitter, List<Integer> index, String query, int searcher) throws SessionException, SeekerException, QueryNotExistException, SearchableException {
////
////        List<DocumentMetaData> selectedDocuments = new List<DocumentMetaData>();
////        SessionProfile recordSession = this.collaborativeSessions.get(sessionName);
////        SeekerSearchResults searchValues = recordSession.getSeekerSearchResults();
////        boolean flag = !searchValues.record.isEmpty();
////        //si existen resultados de búsq. para la sesión especificada
////        if (flag) {
////            SeekerSearchResults.Results results;
////            flag = searchValues.record.containsKey(emitter);
////            if (flag) {
////                results = searchValues.record.get(emitter);
////                flag = results.record.containsKey(query);
////                if (flag) {
////
////                    Results.Data data = results.record.get(query);
////                    List<DocumentMetaData> docs = data.getMetaDocuments(searcher);
////                    // se seleccionan los documentos apartir de los indices específicados
////                    for (Integer i : index) {
////                        selectedDocuments.add(docs.get(i));
////                    }
////                    DocumentsList list = new DocumentsList(query, searcher, selectedDocuments);
////                    return list;
////                } else {
////                    throw new QueryNotExistException("The query doesn't find searched for the seeker '" + emitter.getUser() + "'.");
////                }
////            } else {
////                throw new SeekerException("The seeker '" + emitter.getUser() + "' is not registered in the session '" + sessionName + "'.");
////            }
////        } else {
////            throw new SessionException("The session '" + sessionName + "' doesn't exist.");
////        }
////    }
//    /**
//     * Este método devuelve una selección los resultados de búsqueda a partir de una consulta determinada,
//     * para buscadores seleccionados
//     *
//     * @param sessionName  nombre de la sesión
//     * @param emitter      miembro que realiza la recomendación
//
//     * @return results listado con los resultados de la búsqueda
//     *
//     * @throws SessionException
//     * @throws UserNotExistException
//     *
//     */
//    private DocumentsList getDocuments(SessionProfile sessionProfile, Seeker emitter, Documents docs, String query) throws SessionException, SeekerException {
//
//        SeekerSearchResults searchValues = sessionProfile.getSeekerSearchResults();
//        boolean flag = !searchValues.record.isEmpty();
//        //si existen resultados de búsq. para la sesión especificada
//        if (flag) {
//            SeekerSearchResults.Results results;
//            flag = searchValues.record.containsKey(emitter);
//            if (flag) {
//                results = searchValues.record.get(emitter);
//
//                Results.Data dataRec = results.record.get(query);
//                Results.Data.EngineResults engineResults = dataRec.values.get(time);
//                DocumentsList temp = null;
//
//                Map<Integer, List<DocumentMetaData>> resultsHash = new Map<Integer, List<DocumentMetaData>>();
//                Results.Data.EngineResults.IndexMetaDoc indexMDoc;
//                List<DocumentMetaData> metaDocs = null;
//
//
//                Map<Integer, List<Integer>> values = docs.getDocs();
//                Enumeration<Integer> searchers = values.keys();
//                Integer item;
//                List<Integer> docsIndex = null;
//
//                while (searchers.hasMoreElements()) {
//                    item = searchers.nextElement();
//                    docsIndex = new List<Integer>(values.get(item));
//                    indexMDoc = engineResults.results.get(item);
//                    if (indexMDoc != null) {
//                        metaDocs = new List<DocumentMetaData>();
//                        for (Integer i : docsIndex) {
//                            metaDocs.add(indexMDoc.index.get(i));
//                        }
//                        resultsHash.put(item, metaDocs);
//                    }
//                }
//
//
//                temp = new DocumentsList(query, time, resultsHash);
//
//                return temp;
//
//            } else {
//                throw new SeekerException("The seeker '" + emitter.getUser() + "' is not registered in the session '" + sessionProfile.getProperties().getSessionName() + "'.");
//            }
//        } else {
//            throw new SessionException("The session '" + sessionProfile.getProperties().getSessionName() + "' doesn't exist.");
//        }
//    }
//
//    /**
//     * Este método devuelve todos los resultados  de búsqueda a partir de una consulta determinada,
//     * de todos los buscadores empleados ó selección de estos.
//     *
//     * @param sessionName  nombre de la sesión
//     * @param emitter      miembro que realiza la recomendación
//
//     * @return results listado con los resultados de la búsqueda
//     *
//     * @throws SessionException
//     * @throws UserNotExistException
//     *
//     */
//    private DocumentsList getDocuments(SessionProfile sessionProfile, Seeker emitter, String query, String time, int searcher) throws SessionException, SeekerException {
//
//        SeekerSearchResults searchValues = sessionProfile.getSeekerSearchResults();
//        boolean flag = !searchValues.record.isEmpty();
//        //si existen resultados de búsq. para la sesión especificada
//        if (flag) {
//            SeekerSearchResults.Results results;
//            flag = searchValues.record.containsKey(emitter);
//            if (flag) {
//                results = searchValues.record.get(emitter);
//
//                Results.Data dataRec = results.record.get(query);
//                Collection<Results.Data.EngineResults> engineResults = dataRec.values.values();
//
//                DocumentsList temp = null;
//
//                List<DocumentMetaData> metaDocs = null;
//                List<DocumentMetaData> metaDocsAll = new List<DocumentMetaData>();
//
//                for (Results.Data.EngineResults i : engineResults) {
//                    metaDocs = i.results.get(searcher).getDocuments();
//                    if (metaDocs != null) {
//                        metaDocsAll.addAll(metaDocs);
//                    }
//                }
//
//                temp = new DocumentsList(query, searcher, metaDocsAll);
//
//                return temp;
//
//            } else {
//                throw new SeekerException("The seeker '" + emitter.getUser() + "' is not registered in the session '" + sessionProfile.getProperties().getSessionName() + "'.");
//            }
//        } else {
//            throw new SessionException("The session '" + sessionProfile.getProperties().getSessionName() + "' doesn't exist.");
//        }
//    }
//
//    /**
//     * Devuelve la tabla hash de las sesiones temporales
//     *
//     * @return sesiones temporales
//     */
//    public Map<String, SessionProfile> getHtTempSessions() {
//        return htTempSessions;
//
//
//    }
//
//    /**
//     * Modifica el valor de la tabla hash de las sesiones temporales
//     *
//     * @param htTempSessions nuevo valor
//     */
//    public void setHtTempSessions(Map<String, SessionProfile> htTempSessions) {
//        this.htTempSessions = htTempSessions;
//
//
//    }
//

    /**
     * Lee el fichero que contiene el jucio de relevancia para el experimento
     *
     * @return jucio de relevancia para el experimento
     */
    private Map<String, List<String>> readQueryFile() {
        String value = "";
        BufferedReader readFile = null;
        Map<String, List<String>> hash = new HashMap<>();

        try {
            FileInputStream fis = new FileInputStream(relvJudg);
            readFile = new BufferedReader(new InputStreamReader(fis));
            String[] querys = null;
            List<String> temp;





            for (;;) {
                value = readFile.readLine();



                if (value != null) {
                    querys = value.split(",");
                    temp = new ArrayList<>();

                    for (int i = 1; i
                            < querys.length; i++) {
                        temp.add(querys[i]);

                    }

                    hash.put(querys[0], temp);


                } else {

                    readFile.close();


                    return hash;


                }
            }

        } catch (FileNotFoundException err) {
            OutputMonitor.printStream("", err);
        } catch (IOException err) {
            OutputMonitor.printStream("", err);



        } finally {
            try {
                readFile.close();


            } catch (IOException ex) {
            }
        }

        return hash;


    }

    /**
     * Devuelve el total de documentos relevantes proporcionados por el
     * jucio de relevancia para la consulta específicada
     *
     * @param query  consulta de búsqueda
     *
     * @return total de documentos relevantes
     */
    private int getRelevantDocumentsCount(String query) {
        Map<String, List<String>> hash = readQueryFile();


        boolean flag = hash.containsKey(query);



        if (flag) {
            List<String> querysList = hash.get(query);


            int count = querysList.size();


            return count;


        } else {
            return -1;


        }

    }

    /**
     * Devuelve los nombres de los metadocumetos de la lista
     *
     * @param query  consulta de búsqueda
     *
     * @return nombres de los documentos
     */
    private List<String> getDocumentName(List<DocumentMetaData> docs) {
        List<String> docsNames = new ArrayList<>();
        String value = "";



        for (DocumentMetaData metaDocument : docs) {
            value = metaDocument.getName();


            if (!docsNames.contains(value)) { // eliminando repetidos
                docsNames.add(value);


            }
        }

        return docsNames;


    }

    /**
     * Devuelve el valor de concurrencia de una lista de documentos seleccionados
     * con los respectivos documentos proporcionados por el jucio de relevancia
     *
     * @param query  consulta de búsqueda
     *
     * @return valor de concurrencia
     */
    private long getConcurrency(String query, List<String> docs, Map<String, List<String>> hash) throws QueryNotExistException {

        boolean flag = hash.containsKey(query);



        if (flag) {
            List<String> querysList = hash.get(query);


            int count = 0;



            for (String string : docs) {
                flag = querysList.contains(string);


                if (flag) {
                    count++;


                }
            }
            return count;


        } else {
            return -1;


        }
    }

    /**
     *
     * @param sessionName nombre de la sesión
     * @param query       consulta de búsqueda
     * @param docs        documentos relevantes
     *
     * @return
     *
     * @throws SessionException si la sesión no se encuentra registrada en
     *                                  el servidor
     * @throws QueryNotExistException   si la consulta especificada no se encuentra
     *                                  registrada en la sesión
     */
    public double evaluatePrecision(String sessionName, String query, List<String> docs) throws SessionException, QueryNotExistException {
        Map<String, List<String>> hash = readQueryFile();


        long concurrency = this.getConcurrency(query, docs, hash);


        long retrivedDocs = getRetrievedDocumentsCount(sessionName, query);


        float precision = concurrency / retrivedDocs;



        return precision;



    }

    /**
     *
     * @param sessionName nombre de la sesión
     * @param query       consulta de búsqueda
     * @param docs        documentos relevantes de la consulta
     *
     * @return
     *
     * @throws SessionException si la sesión no se encuentra registrada en
     *                                  el servidor
     * @throws QueryNotExistException   si la consulta especificada no se encuentra
     *                                  registrada en la sesión
     */
    public double evaluateRecall(String sessionName, String query, List<String> docs) throws SessionException, QueryNotExistException {
        Map<String, List<String>> hash = readQueryFile();


        long concurrency = this.getConcurrency(query, docs, hash);


        int relevDocs;

        relevDocs = getRelevantDocumentsCount(query);


        float recall = concurrency / relevDocs;


        return recall;




    }

    /**
     *
     * @param membersCount cantidad de mensajes
     * @param recsCount    cantidad de recomendaciones
     * @param msgsCount    cantidad de mensajes
     * @param timeMs       tiempo
     *
     * @return
     */
    public double evaluateEoI(int membersCount, long recsCount, long msgsCount, long timeMs) {
        float timeMin = (timeMs / 60000);


        float eoi = (msgsCount + recsCount) / (membersCount + timeMin);


        return eoi;


    }

    /**
     * Ejecuta todas las evaluciones del experimento y guarda los resultados
     * en un fichero .xls
     *
     * @param sessions      nombres de las sessiones
     * @param searchers     buscadores
     * @param membersCount cantidad de mensajes
     * @param recsCount     cantidad de recomendaciones
     * @param msgsCount     cantidad de mensajes
     * @param timeMs        tiempo
     *
     * @throws SessionException si existe una sesión que no se encuentra
     *                                  registrada en el servidor
     */
    public void evaluateAll(String[] sessions, int[] searchers, int membersCount, long recsCount, long msgsCount, long timeMs) throws SessionException {
        int size = sessions.length;
        List<String> evaluations = null;
        List<String> querys = null;
        List<DocumentMetaData> relv = null;
        List<DocumentMetaData> viewed = null;
        List<String> docsNames = null;
        String evaluation = "";
        StringBuilder builder = new StringBuilder();


        boolean flag = true;



        for (String session : sessions) {

            try {
                evaluations = new ArrayList<>(size);
                querys = getQuerys(session);



                for (String query : querys) {

                    relv = getRelevantDocuments(session, query, searchers);
                    viewed = getViewedDocuments(session, query, searchers);

                    docsNames = this.getDocumentName(relv);


                    double Ps = evaluatePrecision(session, query, docsNames);


                    double Rs = evaluatePrecision(session, query, docsNames);

                    docsNames = this.getDocumentName(viewed);


                    double Pv = evaluatePrecision(session, query, docsNames);


                    double Rv = evaluatePrecision(session, query, docsNames);

                    evaluation = builder.append(query).append("\t").append(Ps).append("\t").append(Rs).append("\t").append(Pv).append("\t").append(Rv).toString();

                    evaluations.add(evaluation);


                }

                if (flag) {
                    String eoi = String.valueOf(this.evaluateEoI(membersCount, recsCount, msgsCount, timeMs));
                    evaluations.add("EoI\t" + eoi);


                    this.exportEvaluation(session, evaluations);
                    flag = false;


                }

            } catch (SessionException | QueryNotExistException | SearchableException ex) {
            }
        }
    }

    private void exportEvaluation(String sessionName, List<String> evaluations) {
        File eval = new File("Evaluations.xls");
        PrintWriter write = null;


        try {
            write = new PrintWriter(new FileOutputStream(eval));
            write.println(sessionName);



            for (String string : evaluations) {
                write.println(string);


            }
            write.close();



        } catch (FileNotFoundException err) {
            write.close();


        }

    }
//
//    /**
//     * Devuelve el oyente de la aplicación servidor
//     *
//     * @return oyente
//     */
//    @Override
//    public FacadeListener getListener() {
//        return listener;
//
//
//    }
//
//    /**
//     * Modifica el oyente de la aplicación servidor
//     *
//     * @param listener nuevo oyente
//     */
//    @Override
//    public void setListener(FacadeListener listener) {
//        this.listener = listener;
//
//
//    }
//
//    /**
//     *
//     * @return
//     */
//    public byte getMaxScore() {
//        return maxScore;
//
//
//    }
//
//    /**
//     *
//     * @param maxScore
//     */
//    public void setMaxScore(byte maxScore) {
//        this.maxScore = maxScore;
//
//
//    }
//
//    /**
//     *
//     * @return
//     */
//    public byte getRelevScore() {
//        return relevScore;
//
//
//    }
//
//    /**
//     *
//     * @param relevScore
//     */
//    public void setRelevScore(byte relevScore) {
//        this.relevScore = relevScore;
//
//
//    }
//
//    /**
//     *
//     * @return
//     */
//    @Override
//    public String getDefaultSessionName() {
//        return defaultSessionName;
//
//
//    }
//
//    /**
//     *
//     * @param sessionNameDefault
//     */
//    @Override
//    public void setDefaultSessionName(String sessionNameDefault) {
//        this.defaultSessionName = sessionNameDefault;
//
//
//    }
//
    /////////////////////////////////////FROM DB

    public void getRelevantDocumentsDB() {
        Thread thread = new Thread(new Runnable() {

            public void run() {
                if (dbController.isOpen()) {
                } else {
                    OutputMonitor.printLine("Database connection is closed.", OutputMonitor.ERROR_MESSAGE);
                }
            }
        });
        thread.start();
    }

    public void getViewedDocumentsDB() {
        Thread thread = new Thread(new Runnable() {

            public void run() {
                if (dbController.isOpen()) {
                } else {
                    OutputMonitor.printLine("Database connection is closed.", OutputMonitor.ERROR_MESSAGE);
                }

            }
        });
        thread.start();
    }

    public void getRetrievedDocumentsDB() {
        Thread thread = new Thread(new Runnable() {

            public void run() {
                if (dbController.isOpen()) {
                } else {
                    OutputMonitor.printLine("Database connection is closed.", OutputMonitor.ERROR_MESSAGE);
                }
            }
        });
        thread.start();
    }
}
