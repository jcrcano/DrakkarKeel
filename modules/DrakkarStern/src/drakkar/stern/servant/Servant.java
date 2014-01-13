
package drakkar.stern.servant;

import drakkar.oar.Response;
import drakkar.oar.Seeker;
import drakkar.oar.TermSuggest;
import drakkar.oar.exception.NotificationException;
import drakkar.oar.exception.QueryNotExistException;
import drakkar.oar.exception.ScoreOutOfBoundsException;
import drakkar.oar.exception.SessionException;
import drakkar.oar.facade.event.FacadeDesktopEvent;
import drakkar.oar.slice.client.ClientSidePrx;
import static drakkar.oar.util.KeyMessage.*;
import static drakkar.oar.util.KeyTransaction.*;
import static drakkar.oar.util.NotifyAction.*;
import drakkar.oar.util.OutputMonitor;
import drakkar.oar.util.SeekerAction;
import static drakkar.oar.util.SeekerAction.*;
import drakkar.mast.RetrievalManager;
import drakkar.stern.ResponseSessionFactory;
import drakkar.stern.ResponseSuggestFactory;
import drakkar.stern.SternAppSetting;
import drakkar.stern.callback.NotifyAMICallback;
import drakkar.stern.controller.DataBaseController;
import drakkar.stern.evaluation.Evaluable;
import drakkar.stern.evaluation.Referee;
import drakkar.stern.facade.event.FacadeListener;
import drakkar.stern.servant.service.ImplicitRecomendation;
import drakkar.stern.servant.service.Messenger;
import drakkar.stern.servant.service.Notifiable;
import drakkar.stern.servant.service.Perceptionable;
import drakkar.stern.servant.service.Recommendable;
import drakkar.stern.servant.service.Recommender;
import drakkar.stern.servant.service.SearchableInServerSide;
import drakkar.stern.servant.service.SearcherServer;
import drakkar.stern.servant.service.Sendable;
import drakkar.stern.servant.service.SessionManager;
import drakkar.stern.servant.service.Suggestable;
import drakkar.stern.servant.service.SynchronousAwareness;
import drakkar.stern.servant.service.Trackable;
import drakkar.stern.servant.service.Tracker;
import drakkar.stern.tracker.cache.CommentDocuments;
import drakkar.stern.tracker.cache.CommentDocuments.CommentDocs;
import drakkar.stern.tracker.cache.CommentDocuments.Comments;
import drakkar.stern.tracker.cache.CommentDocuments.CommentsData;
import drakkar.stern.tracker.cache.SeekerInfo;
import drakkar.stern.tracker.cache.SeekerRecommendResults;
import drakkar.stern.tracker.cache.SeekerSearchResults;
import drakkar.stern.tracker.cache.SelectedDocuments;
import drakkar.stern.tracker.cache.SelectedDocuments.Evaluation;
import drakkar.stern.tracker.cache.SelectedDocuments.RelevanceDocs;
import drakkar.stern.tracker.cache.SelectedDocuments.SelectedData;
import drakkar.stern.tracker.cache.SessionProfile;
import drakkar.stern.tracker.cache.ViewedDocuments;
import drakkar.stern.tracker.cache.ViewedDocuments.ViewedData;
import drakkar.stern.tracker.cache.ViewedDocuments.ViewedResults;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Esta clase controla todas las operaciones soportadas por el servidor
 */
public abstract class Servant extends SessionManager implements Notifiable, Sendable, Recommendable, SearchableInServerSide, Evaluable, Trackable, Perceptionable, Suggestable {

    //@TODO revisar los cambios ahora de Document por MetaDocument. también en SearchEngineManager
    //@TODO Por el momento no crear nuevas sesiones con el mismo nombre que anteriores finalizadas.
    protected UUID uuid;
    protected Connection cxn;
    protected String sessionUUID = null;
    /**
     * Esta variable representa el valor máximo de la puntuación que puede recibir
     * documento por el miembro que lo evalúa.
     */
    protected byte maxScore = 10;
    /**
     * Esta variable representa el valor de la puntuación, que permite determinar
     * si un documento es relevante o no.
     */
    protected byte relevScore = 6;
    protected Messenger messenger;
    protected Recommender recommend;
    protected SearcherServer searcher;
    protected Referee evaluator;
    protected Tracker tracker;
    protected SynchronousAwareness awareness;
    protected ImplicitRecomendation impRecomend;

    public Servant(RetrievalManager retrievalManager) {

        super(retrievalManager);
        this.listener = null;

        String session = getCommunicationSessionName();
        updateServer(SeekerAction.UPDATE_SESSIONS, SeekerAction.INCREMENT, session);
        this.messenger = new Messenger(session, defaultSessionProfile, collaborativeSessions, htTempSessions);
        this.recommend = new Recommender(session, defaultSessionProfile, collaborativeSessions, htTempSessions);
        this.searcher = new SearcherServer(session, defaultSessionProfile, collaborativeSessions, htTempSessions, this.retrievalManager);
        this.evaluator = new Referee(session, defaultSessionProfile, collaborativeSessions, htTempSessions);
        this.tracker = new Tracker(session, defaultSessionProfile, collaborativeSessions, htTempSessions);
        this.awareness = new SynchronousAwareness(session, defaultSessionProfile, collaborativeSessions, htTempSessions);
        this.impRecomend = new ImplicitRecomendation(session, defaultSessionProfile, collaborativeSessions, htTempSessions);


    }

    public Servant(RetrievalManager retrievalManager, SternAppSetting setting) {

        super(retrievalManager, setting);
        this.listener = null;
        // this.htTempSessions = new Map<String, SessionProfile>();
        String session = setting.getSessionName();
        updateServer(SeekerAction.UPDATE_SESSIONS, SeekerAction.INCREMENT, session);

        this.messenger = new Messenger(session, defaultSessionProfile, collaborativeSessions, htTempSessions);
        this.recommend = new Recommender(session, defaultSessionProfile, collaborativeSessions, htTempSessions);
        this.searcher = new SearcherServer(session, defaultSessionProfile, collaborativeSessions, htTempSessions, this.retrievalManager);
        this.evaluator = new Referee(session, defaultSessionProfile, collaborativeSessions, htTempSessions);
        this.tracker = new Tracker(session, defaultSessionProfile, collaborativeSessions, htTempSessions);
        this.awareness = new SynchronousAwareness(session, defaultSessionProfile, collaborativeSessions, htTempSessions);
        this.impRecomend = new ImplicitRecomendation(session, defaultSessionProfile, collaborativeSessions, htTempSessions);
    }

    public Servant(RetrievalManager retrievalManager, DataBaseController dbController) {

        super(retrievalManager, dbController);
        this.listener = null;
        String session = getCommunicationSessionName();
        // this.htTempSessions = new Map<String, SessionProfile>();
        updateServer(SeekerAction.UPDATE_SESSIONS, SeekerAction.INCREMENT, session);
        this.messenger = new Messenger(session, defaultSessionProfile, collaborativeSessions, htTempSessions);
        this.recommend = new Recommender(session, defaultSessionProfile, collaborativeSessions, htTempSessions);
        this.searcher = new SearcherServer(session, defaultSessionProfile, collaborativeSessions, htTempSessions, this.retrievalManager);
        this.evaluator = new Referee(session, defaultSessionProfile, collaborativeSessions, htTempSessions);
        this.tracker = new Tracker(session, defaultSessionProfile, collaborativeSessions, htTempSessions);
        this.awareness = new SynchronousAwareness(session, defaultSessionProfile, collaborativeSessions, htTempSessions);
        this.impRecomend = new ImplicitRecomendation(session, defaultSessionProfile, collaborativeSessions, htTempSessions);
    }

    public Servant(RetrievalManager retrievalManager, FacadeListener listener, DataBaseController dbController) {
        super(retrievalManager, listener, dbController);

        this.listener = listener;
        //  this.htTempSessions = new Map<String, SessionProfile>();
        String session = getCommunicationSessionName();
        this.updateServer(SeekerAction.UPDATE_SESSIONS, SeekerAction.INCREMENT, session);
        this.messenger = new Messenger(session, defaultSessionProfile, collaborativeSessions, htTempSessions, this.listener, dbController);
        this.recommend = new Recommender(session, defaultSessionProfile, collaborativeSessions, htTempSessions, this.listener, dbController);
        this.searcher = new SearcherServer(session, defaultSessionProfile, collaborativeSessions, htTempSessions, this.retrievalManager, this.listener, dbController);
        this.evaluator = new Referee(session, defaultSessionProfile, collaborativeSessions, htTempSessions, this.listener, dbController);
        this.tracker = new Tracker(session, defaultSessionProfile, collaborativeSessions, htTempSessions, this.listener, dbController);
        this.awareness = new SynchronousAwareness(session, defaultSessionProfile, collaborativeSessions, htTempSessions, this.listener, dbController);
        this.impRecomend = new ImplicitRecomendation(session, defaultSessionProfile, collaborativeSessions, htTempSessions, this.listener, dbController);
    }

    public Servant(RetrievalManager retrievalManager, FacadeListener listener, DataBaseController dbController, SternAppSetting setting) {
        super(retrievalManager, listener, dbController, setting);

        this.listener = listener;
        // this.htTempSessions = new Map<String, SessionProfile>();
        String sessionName = setting.getSessionName();

        this.updateServer(SeekerAction.UPDATE_SESSIONS, SeekerAction.INCREMENT, sessionName);
        this.messenger = new Messenger(sessionName, defaultSessionProfile, collaborativeSessions, htTempSessions, this.listener, dbController);
        this.recommend = new Recommender(sessionName, defaultSessionProfile, collaborativeSessions, htTempSessions, this.listener, dbController);
        this.searcher = new SearcherServer(sessionName, defaultSessionProfile, collaborativeSessions, htTempSessions, this.retrievalManager, this.listener, dbController);
        this.evaluator = new Referee(sessionName, defaultSessionProfile, collaborativeSessions, htTempSessions, this.listener, dbController);
        this.tracker = new Tracker(sessionName, defaultSessionProfile, collaborativeSessions, htTempSessions, this.listener, dbController);
        this.awareness = new SynchronousAwareness(sessionName, defaultSessionProfile, collaborativeSessions, htTempSessions, this.listener, dbController);
        this.impRecomend = new ImplicitRecomendation(sessionName, defaultSessionProfile, collaborativeSessions, htTempSessions, this.listener, dbController);

    }
    /**
     *
     */
    public static String tableName = "derbyDBMensajes";

    /**
     * Este método reemplaza el objeto Connection.
     *
     * @param cxn    nuevo objeto Connection.
     */
    public abstract void setConnection(Connection cxn);

    /**
     * Este método reemplaza el uuid de la sesión.
     *
     * @param sessionUUID    nuevo uuid de la sesión.
     */
    public abstract void setSessionUUID(String sessionUUID);

    /**
     * Este método reemplaza el objeto UUID de la clase.
     *
     * @param uuid    nuevo UUID
     */
    public abstract void setUUIDClass(UUID uuid);

    /**
     * {@inheritDoc}
     */
    public synchronized void notifyDocumentViewed(final String sessionName, Seeker seeker, String query, int searchable, int docIndex, final String uri) throws SessionException, NotificationException {

        SessionProfile recordSession = getSessionProfile(sessionName);
        SeekerInfo members = recordSession.getSeekerInfo();

        boolean flag = members.record.containsKey(seeker);
        if (flag) {
            // se comprueba que la sesión y el miembro que emite la notifcación
            // cuenten con registros de resultados de búsqueda, en caso contrario
            // se lanza una excepción del tipo NotificationException.
            SeekerSearchResults searches = recordSession.getSeekerSearchResults();
            flag = !searches.record.isEmpty();
            if (flag) {
                // se obtiene la relación de los resultados de búquedas de los
                // miembros de la sesión.
                flag = searches.record.containsKey(seeker);
                if (flag) {
                    ViewedDocuments viewedDocuments = recordSession.getViewedDocuments();
                    ViewedData viewedData;
                    ViewedResults viewedResults;
                    // de existir registros
                    flag = !viewedDocuments.record.isEmpty();
                    if (flag) {
                        // se obtienen el registro que almacena los documentos revisados
                        // por los miembros de esta sesión.

                        List<Integer> docs;
                        // si el miembro cuenta con revisiones previas de documentos
                        flag = viewedDocuments.record.containsKey(seeker);
                        if (flag) {
                            // se obtienen los documentos revisados por el miembro
                            viewedData = viewedDocuments.record.get(seeker);
                            flag = viewedData.values.containsKey(query);
                            if (flag) {
                                viewedResults = viewedData.values.get(query);
                                docs = viewedResults.results.get(searchable);
                                if (docs == null) {
                                    docs = new ArrayList<>();
                                    docs.add(docIndex);
                                    viewedResults.results.put(searchable, docs);
                                } else {
                                    flag = viewedResults.isViewed(searchable, docIndex);
                                    if (!flag) {
                                        docs.add(docIndex);
                                    }
                                }

                            } else {
                                // si no existe el registro de documentos revisados
                                // para esa consulta, se procede su registro.
                                viewedResults = viewedDocuments.new ViewedResults(searchable, docIndex);
                                viewedData.values.put(query, viewedResults);

                            }

                        } else {
                            // en caso de que el miembro no halla revisado aún ningún
                            // documento de los resultados de la búsqueda, se efectúa
                            // el registro del mismo en las estructuras correspondientes.
                            viewedData = viewedDocuments.new ViewedData();
                            viewedResults = viewedDocuments.new ViewedResults(searchable, docIndex);
                            viewedData.values.put(query, viewedResults);
                            // se adiciona la nueva revisión del miembro en la
                            // hash de registro de revisiones.
                            viewedDocuments.record.put(seeker, viewedData);
                        }

                    } else {
                        // en caso de que no exista aún un registro de documentos
                        // revisados, se efectúa la inicialización del mismo en
                        // las estructuras correspondientes.
                        viewedData = viewedDocuments.new ViewedData();
                        viewedResults = viewedDocuments.new ViewedResults(searchable, docIndex);
                        viewedData.values.put(query, viewedResults);
                        // se adiciona la nueva revisión del miembro en la
                        // hash de registro de revisiones.
                        viewedDocuments.record.put(seeker, viewedData);
                    }

                    //update estado de los documentos a true a partir del URI
                    final DataBaseController control = this.dbController;
                    Thread thread = new Thread(new Runnable() {

                        public void run() {

                            if (control.isOpen()) {
                                try {
                                    control.setReviewDocument(sessionName, uri);
                                } catch (SQLException ex) {
                                    OutputMonitor.printStream("SQL", ex);
                                }
                            }

                        }
                    });
                    thread.start();

                } else {
                    throw new NotificationException("The seeker '" + seeker.getUser() + "'doesn't have previous results of search");
                }
            } else {
                throw new NotificationException("The session '" + sessionName + "'doesn't have previous results of search");
            }
        } else {
            throw new NotificationException("The session '" + sessionName + " or the seeker '" + seeker.getUser() + "' is not supported this notification ");
        }

    }

    /**
     * {@inheritDoc}
     *
     */
    public synchronized void notifyDocumentEvaluated(final String sessionName, final Seeker seeker, final String query, int searchable, int docIndex, String uri, byte score, int source) throws SessionException, NotificationException, ScoreOutOfBoundsException, IOException {
        final SessionProfile sessionProfile = getSessionProfile(sessionName);
        switch (source) {

            case LOCAL_SEARCH_RESULT:
                localEvaluation(sessionProfile, seeker, query, searchable, docIndex, score, uri);
                break;

            case RECOMMEND_SEARCH_RESULT:
                recommendEvaluation(sessionProfile, seeker, query, searchable, docIndex, score);
                break;

            case TRACK_SEARCH_RESULT:
                try {
                    collabEvaluation(sessionProfile, seeker, query, searchable, docIndex, score, uri);
                } catch (SQLException ex) {
                    throw new NotificationException(ex.getMessage());
                }

                break;
            default:
                throw new NotificationException("El origen de la evaluación no es válido.");
        }


        if (score >= this.relevScore) {
            Thread t = new Thread(new Runnable() {

                public void run() {
                    try {
                        String defaultSession = defaultSessionProfile.getProperties().getSessionName();
                        if (sessionName.equals(defaultSession)) {
                            if (sessionProfile.isActiveTermsSuggest(seeker)) {
                                List<TermSuggest> termsList = retrievalManager.getTermsSuggest(query);
                                if (!termsList.isEmpty()) {
                                    ClientSidePrx prx = sessionProfile.getSeekerInfo().record.get(seeker);
                                    Response rsp = ResponseSuggestFactory.getResponse(sessionName, query, termsList);
                                    prx.notify_async(new NotifyAMICallback(seeker), rsp.toArray());

                                }
                            }//TODO responder si no está activo

                        } else if (sessionProfile.isTermsSuggest()) {
                            List<TermSuggest> termsList1 = retrievalManager.getTermsSuggest(query);

                            if (!termsList1.isEmpty()) {

                                int members = sessionProfile.getSeekerInfo().record.size();
                                int terms = termsList1.size();


                                ClientSidePrx prx;
                                Response rsp;
                                List<TermSuggest> temp;

                                if (members == 1) {
                                    rsp = ResponseSuggestFactory.getResponse(sessionName, query, termsList1);
                                    prx = sessionProfile.getSeekerInfo().record.get(seeker);
                                    prx.notify_async(new NotifyAMICallback(seeker), rsp.toArray());

                                } else {

                                    Set<Seeker> list = sessionProfile.getSeekerInfo().record.keySet();
                                    List<Seeker> seekerList = new ArrayList<>(list);


                                    if (members > terms) {
                                        for (int i = 0; i < terms; i++) {
                                            temp = new ArrayList<>(1);
                                            temp.add(termsList1.get(i));
                                            rsp = ResponseSuggestFactory.getResponse(sessionName, query, temp);
                                            prx = sessionProfile.getSeekerInfo().record.get(seekerList.get(i));
                                            prx.notify_async(new NotifyAMICallback(seeker), rsp.toArray());

                                        }

                                    } else {
                                        int quantity = terms / members;
                                        int index = 0;


                                        for (int i = 0; i < (members - 1); i++) {
                                            temp = new ArrayList<>(termsList1.subList(index, index + quantity));
                                            rsp = ResponseSuggestFactory.getResponse(sessionName, query, temp);
                                            prx = sessionProfile.getSeekerInfo().record.get(seekerList.get(i));
                                            prx.notify_async(new NotifyAMICallback(seeker), rsp.toArray());
                                            index += quantity;
                                        }

                                        // se notifica al último miembro de la sesión
                                        temp = new ArrayList<>(termsList1.subList(index, terms));
                                        rsp = ResponseSuggestFactory.getResponse(sessionName, query, temp);
                                        prx = sessionProfile.getSeekerInfo().record.get(seekerList.get(members - 1));
                                        prx.notify_async(new NotifyAMICallback(seeker), rsp.toArray());
                                    }

                                }

                            }

                        }
                    } catch (IOException ex) {
                        OutputMonitor.printStream("Notify terms suggest to seeker: " + seeker.getUser(), ex);
                    }
                }
            });

            t.start();


        }
    }

    private void localEvaluation(SessionProfile sessionProfile, Seeker seeker, String query, int searchable, int docIndex, byte relevance, String uri) throws SessionException, NotificationException, ScoreOutOfBoundsException, IOException {

        SeekerInfo members = sessionProfile.getSeekerInfo();
        String sessionName = sessionProfile.getProperties().getSessionName();



        if (relevance > this.maxScore && relevance < 1) {
            throw new ScoreOutOfBoundsException("The assigned score finds out of the range of values possible < 0 - " + this.maxScore + " >.");


        }

        if (members.record.containsKey(seeker)) {
            // se comprueba que la sesión y el miembro que emite la notifcación
            // cuenten con registros de resultados de búsqueda, en caso contrario
            // se lanza una excepción del tipo NotificationException.
            SeekerSearchResults searches = sessionProfile.getSeekerSearchResults();
            boolean flag = !searches.record.isEmpty();
            if (flag) {
                // se obtiene la relación de los resultados de búquedas de los
                // miembros de la sesión.
                flag = searches.record.containsKey(seeker);

                if (flag) {
                    registerEvaluation(sessionProfile, query, searchable, docIndex, relevance, seeker);
                    //Guardar BD
                    if (this.dbController.isOpen()) {
                        try {
                            String sessionSave;
                            if (sessionName.equals("")) {
                                sessionSave = getCommunicationSessionName();
                            } else {
                                sessionSave = sessionName;
                            }
                            this.dbController.saveEvaluation(sessionSave, "", (int) relevance, seeker.getUser(), uri);
                        } catch (SQLException ex) {
                            OutputMonitor.printStream("SQL", ex);
                        }
                    } else {
                        throw new NotificationException("No existe una conexión con la BD del servidor.");
                    }
                } else {
                    throw new NotificationException("The seeker '" + seeker.getUser() + "'doesn't have previous results of search");
                }
            } else {
                throw new NotificationException("The session '" + sessionProfile.getProperties().getSessionName() + " doesn't have previous results of search");
            }
        } else {
            throw new NotificationException("The session '" + sessionProfile.getProperties().getSessionName() + " or the seeker '" + seeker.getUser() + "' is not supported this notification ");
        }
    }

    private void recommendEvaluation(SessionProfile sessionProfile, Seeker seeker, String query, int searchable, int docIndex, byte relevance) throws SessionException, NotificationException, ScoreOutOfBoundsException {

        SeekerInfo members = sessionProfile.getSeekerInfo();
        if (relevance > this.maxScore && relevance < 1) {
            throw new ScoreOutOfBoundsException("The assigned score finds out of the range of values possible < 0 - " + this.maxScore + " >.");
        }

        if (members.record.containsKey(seeker)) {
            // se comprueba que la sesión y el miembro que emite la notifcación
            // cuenten con registros de resultados de búsqueda, en caso contrario
            // se lanza una excepción del tipo NotificationException.
            SeekerRecommendResults recommendations = sessionProfile.getSeekerRecommendResults();


            boolean flag = !recommendations.record.isEmpty();


            if (flag) {
                // se obtiene la relación de los resultados de búquedas de los
                // miembros de la sesión.
                flag = recommendations.record.containsKey(seeker);


                if (flag) {

                    registerEvaluation(sessionProfile, query, searchable, docIndex, relevance, seeker);



                } else {
                    throw new NotificationException("The seeker '" + seeker.getUser() + "'doesn't have previous results of recommendations");


                }
            } else {
                throw new NotificationException("The session '" + sessionProfile.getProperties().getSessionName() + "'doesn't have previous results of recommendation");


            }
        } else {
            throw new NotificationException("The session '" + sessionProfile.getProperties().getSessionName() + " or the seeker '" + seeker.getUser() + "' is not supported this notification ");


        }

    }

    private void collabEvaluation(SessionProfile sessionProfile, Seeker seeker, String query, int searchable, int docIndex, byte relevance, String uri) throws SessionException, NotificationException, ScoreOutOfBoundsException, SQLException {

        String sessionName = sessionProfile.getProperties().getSessionName();
        SeekerInfo members = sessionProfile.getSeekerInfo();


        if (relevance > this.maxScore && relevance < 1) {
            throw new ScoreOutOfBoundsException("The assigned score finds out of the range of values possible < 0 - " + this.maxScore + " >.");


        }

        if (members.record.containsKey(seeker)) {

            registerEvaluation(sessionProfile, query, searchable, docIndex, relevance, seeker);



            if (this.dbController.isOpen()) {

                this.dbController.saveEvaluation(sessionName, "", (int) relevance, seeker.getUser(), uri);


            } else {
                throw new NotificationException("No existe una conexión con la BD del servidor.");


            }
        } else {
            throw new NotificationException("The session '" + sessionProfile.getProperties().getSessionName() + " or the seeker '" + seeker.getUser() + "' is not supported this notification ");


        }
    }

    private void registerEvaluation(SessionProfile sessionProfile, String query, int searchable, int docIndex, byte relevance, Seeker seeker) throws NotificationException {
        SelectedDocuments selectedDocuments = sessionProfile.getSelectedDocuments();
        Evaluation eval;
        SelectedData selectedData;
        RelevanceDocs relevanceDocs;
        // de existir registros
        boolean flag = !selectedDocuments.record.isEmpty();
        if (flag) {
            // se obtienen el registro que almacena los documentos revisados
            // por los miembros de esta sesión.
            // si el miembro cuenta con revisiones previas de documentos
            flag = selectedDocuments.record.containsKey(seeker);
            if (flag) {
                // se obtiene la relación de los documentos evavluados
                // por el miembro
                eval = selectedDocuments.record.get(seeker);
                flag = eval.evaluation.containsKey(query);

                if (flag) {
                    // se obtiene el la instancia del objeto Evaluation,
                    // que almacena las evaluaciones de documentos
                    // realizadas por el miembro para la query.
                    selectedData = eval.evaluation.get(query);
                    // se adiciona la evaluación del nuevo documento si
                    // ya no ha sido evaluado, en caso contrario se lanza
                    // una excepción del tipo AlreadyRegisteredException
                    // y se notifica al cliente el error.
                    relevanceDocs = selectedData.values.get(searchable);

                    if (relevanceDocs == null) {
                        // si no existe evaluaciones para el searchable
                        relevanceDocs = selectedDocuments.new RelevanceDocs(docIndex, relevance);
                        selectedData.values.put(searchable, relevanceDocs);
                    } else {
                        flag = relevanceDocs.values.containsKey(docIndex);
                        if (flag) {
                            byte scoreTemp = relevanceDocs.values.get(docIndex);
                            scoreTemp = relevance;
                        } else {
                            relevanceDocs.values.put(docIndex, relevance);
                        }
                    }
                } else {
                    // de no existir evaluaciones para la consulta se
                    // se procede a efectuar su registro en las estructuras
                    // correspondientes.
                    selectedData = selectedDocuments.new SelectedData();
                    relevanceDocs = selectedDocuments.new RelevanceDocs(docIndex, relevance);
                    selectedData.values.put(searchable, relevanceDocs);
                    eval.evaluation.put(query, selectedData);
                }
            } else {
                // en caso de que el miembro cuenta con evaluaciones
                // previas de documentos, se procede a efectuar su
                // registro en las estructuras correspondientes.
                eval = selectedDocuments.new Evaluation();
                selectedData = selectedDocuments.new SelectedData();
                relevanceDocs = selectedDocuments.new RelevanceDocs(docIndex, relevance);
                selectedData.values.put(searchable, relevanceDocs);
                eval.evaluation.put(query, selectedData);
                selectedDocuments.record.put(seeker, eval);// se agrega al registro de evaluaciones del usuario
            }
        } else {
            // en caso de que no exista aún un registro de documentos
            // evaluados para la sesión, se procede a efectuar su
            // registro en las estructuras correspondientes
            eval = selectedDocuments.new Evaluation();
            selectedData = selectedDocuments.new SelectedData();
            relevanceDocs = selectedDocuments.new RelevanceDocs(docIndex, relevance);
            selectedData.values.put(searchable, relevanceDocs);
            eval.evaluation.put(query, selectedData);
            selectedDocuments.record.put(seeker, eval);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void notifyDocumentCommented(String sessionName, Seeker seeker, String query, int searchable, int docIndex, String uri, String comment, int source) throws SessionException, NotificationException {

        SessionProfile sessionProfile = getSessionProfile(sessionName);
        switch (source) {

            case LOCAL_SEARCH_RESULT:
                this.localComments(sessionProfile, seeker, query, searchable, docIndex, comment, uri);
                break;
            case RECOMMEND_SEARCH_RESULT:
                this.registerComments(sessionProfile, query, searchable, docIndex, comment, seeker);
                break;
            case TRACK_SEARCH_RESULT:
                try {
                    this.collabComments(sessionProfile, seeker, query, searchable, docIndex, comment, uri);
                } catch (SQLException ex) {
                    throw new NotificationException(ex.getMessage());
                }
                break;
        }
    }

    private void localComments(SessionProfile recordSession, Seeker seeker, String query, int searchable, int docIndex, String comments, String uri) throws SessionException, NotificationException {
        String sessionName = recordSession.getProperties().getSessionName();
        SeekerInfo members = recordSession.getSeekerInfo();
        // se comprueba que el miembro que emite la notificación sea miembro
        // de la sesión, en caso contrario se lanza una excepción del tipo
        // UserNotExistException.
        boolean flag = members.record.containsKey(seeker);
        if (flag) {
            // se obtiene la relación de los resultados de búquedas de los
            // miembros de la sesión.
            SeekerSearchResults searches = recordSession.getSeekerSearchResults();
            // se comprueba que la sesión y el miembro que emite la notifcación
            // cuenten con registros de resultados de búsqueda, en caso contrario
            // se lanza una excepción del tipo NotificationException.
            flag = !searches.record.isEmpty();
            if (flag) {
                flag = searches.record.containsKey(seeker);
                if (flag) {
                    registerComments(recordSession, query, searchable, docIndex, comments, seeker);
                    //Guardar BD
                    if (this.dbController.isOpen()) {
                        try {
                            //nota: verificar la sesion
                            String sessionSave;
                            if (sessionName.equals("")) {
                                sessionSave = getCommunicationSessionName();
                            } else {
                                sessionSave = sessionName;
                            }
                            this.dbController.updateEvaluationComment(sessionSave, seeker, uri, comments);
                        } catch (SQLException ex) {
                            OutputMonitor.printStream("SQL", ex);
                        }
                    } else {
                        throw new NotificationException("No existe una conexión con la BD del servidor.");
                    }
                } else {
                    throw new NotificationException("The seeker '" + seeker.getUser() + "'doesn't have previous results of search");
                }
            } else {
                throw new NotificationException("The session '" + sessionName + "'doesn't have previous results of search");
            }
        }
    }

    private void recommendComments(SessionProfile sessionProfile, Seeker seeker, String query, int searchable, int docIndex, String comments) throws SessionException, NotificationException {

        SeekerInfo members = sessionProfile.getSeekerInfo();
        String sessionName = sessionProfile.getProperties().getSessionName();
        if (members.record.containsKey(seeker)) {
            // se comprueba que la sesión y el miembro que emite la notifcación
            // cuenten con registros de resultados de búsqueda, en caso contrario
            // se lanza una excepción del tipo NotificationException.
            SeekerRecommendResults recommendations = sessionProfile.getSeekerRecommendResults();
            boolean flag = !recommendations.record.isEmpty();
            if (flag) {
                // se obtiene la relación de los resultados de búquedas de los
                // miembros de la sesión.
                flag = recommendations.record.containsKey(seeker);
                if (flag) {
                    registerComments(sessionProfile, query, searchable, docIndex, comments, seeker);
                } else {
                    throw new NotificationException("The seeker '" + seeker.getUser() + "'doesn't have previous results of recommendations");
                }
            } else {
                throw new NotificationException("The session '" + sessionName + "'doesn't have previous results of recommendation");
            }
        } else {
            throw new NotificationException("The session '" + sessionName + " or the seeker '" + seeker.getUser() + "' is not supported this notification ");
        }
    }

    private void collabComments(SessionProfile sessionProfile, Seeker seeker, String query, int searchable, int docIndex, String comments, String uri) throws SessionException, NotificationException, SQLException {

        String sessionName = sessionProfile.getProperties().getSessionName();
        SeekerInfo members = sessionProfile.getSeekerInfo();
        if (members.record.containsKey(seeker)) {
            registerComments(sessionProfile, query, searchable, docIndex, comments, seeker);
            if (this.dbController.isOpen()) {
                dbController.updateEvaluationComment(sessionName, seeker, uri, comments);
            } else {
                throw new NotificationException("No existe una conexión con la BD del servidor.");
            }
        } else {
            throw new NotificationException("The session '" + sessionName + " or the seeker '" + seeker.getUser() + "' is not supported this notification ");
        }
    }

    private void registerComments(SessionProfile sessionProfile, String query, int searchable, int docIndex, String comment, Seeker seeker) throws NotificationException {
        CommentDocuments seekerComments = sessionProfile.getSeekerComments();
        Comments comments;
        CommentsData commentsData;
        CommentDocs commentDocs;
        String commentTemp;
        // de existir registros para la sesión

        boolean flag = !seekerComments.record.isEmpty();
        if (flag) {
            // se obtienen el registro que almacena los documentos revisados
            // por los miembros de esta sesión.
            // si el miembro cuenta con revisiones previas de documentos
            if (seekerComments.record.containsKey(seeker)) {
                // se obtiene la relación de los documentos evavluados
                // por el miembro
                comments = seekerComments.record.get(seeker);
                if (comments.record.containsKey(query)) {
                    // se obtiene el la instancia del objeto Comment,
                    // quien almacena la lista de los comentarios realizados
                    // por el miembro a cada uno de los documentos.
                    commentsData = comments.record.get(query);
                    if (commentsData.values.containsKey(searchable)) {
                        commentDocs = commentsData.values.get(searchable);
                        if (commentDocs.values.containsKey(docIndex)) {
                            commentTemp = commentDocs.values.get(docIndex);
                            // se adiciona el nuevo comentario del documento
                            commentTemp = comment;
                        } else {
                            // se adiciona el nuevo comentario del documento
                            commentDocs.values.put(docIndex, comment);
                        }
                    } else {
                        commentDocs = seekerComments.new CommentDocs(docIndex, comment);
                        commentsData.values.put(searchable, commentDocs);
                    }
                } else {
                    commentsData = seekerComments.new CommentsData();
                    commentDocs = seekerComments.new CommentDocs(docIndex, comment);
                    commentsData.values.put(searchable, commentDocs);
                    comments.record.put(query, commentsData);
                }
            } else {
                // en caso de que el miembro no halla comentado aún ningún
                // documento de los resultados de la búsqueda, se efectúa
                // el registro del mismo en las estructuras correspondientes.
                comments = seekerComments.new Comments();
                commentsData = seekerComments.new CommentsData();
                commentDocs = seekerComments.new CommentDocs(docIndex, comment);
                commentsData.values.put(searchable, commentDocs);
                comments.record.put(query, commentsData);
                // se adiciona la nueva comentario del miembro en la
                // hash de registro de comentarios.
                seekerComments.record.put(seeker, comments);
            }
        } else {
            // en caso de que no exista aún un registro de documentos
            // evaluados por la sesión, se efectúa la inicialización
            // del mismo en las estructuras correspondientes.
            comments = seekerComments.new Comments();
            commentsData = seekerComments.new CommentsData();
            commentDocs = seekerComments.new CommentDocs(docIndex, comment);
            commentsData.values.put(searchable, commentDocs);
            comments.record.put(query, commentsData);
            // se adiciona la nueva comentario del miembro en la
            // hash de registro de comentarios.
            seekerComments.record.put(seeker, comments);
        }
    }

    /**
     *
     * @param sessionName
     * @param query
     * @param docs
     * @return
     * @throws SessionException
     * @throws QueryNotExistException
     */
    public double evaluatePrecision(String sessionName, String query, List<String> docs) throws SessionException, QueryNotExistException {
        return this.evaluator.evaluatePrecision(sessionName, query, docs);
    }

    /**
     *
     * @param sessionName
     * @param query
     * @param docs
     * @return
     * @throws QueryNotExistException
     * @throws SessionException
     */
    public double evaluateRecall(String sessionName, String query, List<String> docs) throws QueryNotExistException, SessionException {
        return this.evaluator.evaluateRecall(sessionName, query, docs);
    }

    /**
     *
     * @param session
     * @return
     * @throws SessionException
     */
    public double evaluateEoI(String session) throws SessionException {
        int membersCount = this.getSeekersCount(session);
        long recsCount = this.recommend.getRecommendationsCount(session);
        long msgsCount = this.messenger.getMessagesCount(session);
        long timeMs = this.evaluator.getDurationSessionTime(session);
        return this.evaluator.evaluateEoI(membersCount, recsCount, msgsCount, timeMs);
    }

    /**
     * Este método ejecuta todas las operaciones del experimento y guarda los resultados
     * en un fichero .xls.
     *
     * @param sessions nombres de las sessiones
     * @param searchableArray
     * @throws SessionException
     */
    public void evaluateAll(String[] sessions, int[] searchableArray) throws SessionException {
        int membersCount;
        long recsCount, msgsCount, timeMs;
        for (String session : sessions) {
            membersCount = this.getSeekersCount(session);
            recsCount = this.recommend.getRecommendationsCount(session);
            msgsCount = this.messenger.getMessagesCount(session);
            timeMs = this.evaluator.getDurationSessionTime(session);
            this.evaluator.evaluateAll(sessions, searchableArray, membersCount, recsCount, msgsCount, timeMs);
        }
    }

    /**
     *  Notifica los buscadores que están disponibles en el servidor
     */
    public void notifyAvailableSearchers() {

        List<String> searchPrinciples;
        Response response, response1;
        byte[] array = null, array1 = null;
        try {
            String[] searchers = retrievalManager.getAvailableSearchers();
            Map<Object, Object> hash = new HashMap<>(2);
            hash.put(OPERATION, NOTIFY_AVAILABLE_SEARCHERS);
            hash.put(SEARCHERS, searchers);
            response = new Response(hash);
            array = response.toArray();

            ClientSidePrx seekerPrx;
            List<Seeker> seekersChairman = new ArrayList<>();
            boolean flag = collaborativeSessions.isEmpty();
            if (!flag) {
                searchPrinciples = retrievalManager.getSearchPrinciples(RetrievalManager.COLLABORATIVE_SEARCH);
                response1 = ResponseSessionFactory.getResponse(searchPrinciples);
                array1 = response1.toArray();
                Collection<SessionProfile> sessions = collaborativeSessions.values();
                for (SessionProfile sessionProfile : sessions) {
                    Seeker chairman = sessionProfile.getTempChairman();
                    if (chairman == null) {
                        chairman = sessionProfile.getChairman().getSeeker();
                        seekerPrx = sessionProfile.getChairman().getClientSidePrx();
                        seekerPrx.notify_async(new NotifyAMICallback(chairman, "notifyAvailableSearchers"), array);
                        seekerPrx.notify_async(new NotifyAMICallback(chairman, "notifyAvailableSearcPrinciples"), array1);
                    } else {
                        seekerPrx = sessionProfile.getSeekerInfo().record.get(chairman);
                        seekerPrx.notify_async(new NotifyAMICallback(chairman, "notifyAvailableSearchers"), array);
                        seekerPrx.notify_async(new NotifyAMICallback(chairman, "notifyAvailableSearcPrinciples"), array1);
                    }
                    seekersChairman.add(chairman);
                }
            }
            searchPrinciples = retrievalManager.getSearchPrinciples(RetrievalManager.INDIVIDUAL_SEARCH);
            response1 = ResponseSessionFactory.getResponse(searchPrinciples);
            array1 = response1.toArray();
            Map<Seeker, ClientSidePrx> record = defaultSessionProfile.getSeekerInfo().record;
            @SuppressWarnings("unchecked")
            List<Seeker> seekerList = new ArrayList<>(record.keySet());
            seekerList.removeAll(seekersChairman);
            for (Seeker seeker : seekerList) {
                seekerPrx = record.get(seeker);
                seekerPrx.notify_async(new NotifyAMICallback(seeker, "notifyAvailableSearchers"), array);
                seekerPrx.notify_async(new NotifyAMICallback(seeker, "notifyAvailableSearcPrinciples"), array1);
            }
        } catch (IOException ex) {
            this.notifyListener(ERROR_MESSAGE, "Error al notificar los buscadores disponibles.");
            OutputMonitor.printStream(" En la notificación los buscadores disponibles.", ex);
        }
    }

    public void notifyAvailableSVNRepositories() {

        String[] repositories = retrievalManager.getAvailableSVNRepositories();
        Map<Object, Object> hash = new HashMap<>(2);
        hash.put(OPERATION, NOTIFY_AVAILABLE_SVN_REPOSITORIES);
        hash.put(SVN_REPOSITORIES_NAMES, repositories);

        Response response = new Response(hash);

        byte[] array = null;
        try {
            array = response.toArray();
            Map<Seeker, ClientSidePrx> record = defaultSessionProfile.getSeekerInfo().record;
            Set<Seeker> seekers = record.keySet();
            ClientSidePrx seekerPrx;

            for (Seeker seeker : seekers) {
                seekerPrx = record.get(seeker);
                seekerPrx.notify_async(new NotifyAMICallback(seeker, "notifySVNRepositories"), array);
            }


        } catch (IOException ex) {
            this.notifyListener(ERROR_MESSAGE, "Error al notificar los repositorios disponibles.");
            OutputMonitor.printStream("IO", ex);
        }
    }

    /**
     * Notifica al servidor de algún error que ocurra
     *
     * @param messageType
     * @param message
     */
    public void notifyListener(int messageType, String message) {
        if (listener != null) {
            Response rs = new Response();
            rs.put(OPERATION, NOTIFY_TEXT_MESSAGE);
            rs.put(MESSAGE_TYPE, messageType);
            rs.put(MESSAGE, message);
            FacadeDesktopEvent evt = new FacadeDesktopEvent(this, rs);
            listener.notify(evt);

        }
    }
}
