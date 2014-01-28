/*
 * DrakkarKeel - An Enterprise Collaborative Search Platform
 *
 * The contents of this file are subject under the terms described in the
 * DRAKKARKEEL_LICENSE file included in this distribution; you may not use this
 * file except in compliance with the License. 
 *
 * 2013-2014 DrakkarKeel Platform.
 */
package drakkar.stern.side;

import drakkar.oar.Communication;
import drakkar.oar.Response;
import drakkar.oar.exception.QueryNotExistException;
import drakkar.oar.exception.SessionException;
import drakkar.oar.facade.event.FacadeDesktopEvent;
import static drakkar.oar.util.KeyMessage.*;
import static drakkar.oar.util.KeySession.*;
import static drakkar.oar.util.KeyTransaction.*;
import static drakkar.oar.util.NotifyAction.*;
import drakkar.oar.util.OutputMonitor;
import drakkar.mast.IndexException;
import drakkar.mast.RetrievalManager;
import drakkar.mast.SearchableNotSupportedException;
import drakkar.mast.retrieval.Searchable;
import drakkar.stern.controller.ContainerController;
import drakkar.stern.controller.DataBaseController;
import drakkar.stern.email.EmailConfig;
import drakkar.stern.evaluation.RefereeDB;
import drakkar.stern.facade.event.FacadeListener;
import drakkar.stern.servant.SternServant;
import drakkar.stern.side.Stern;
import drakkar.stern.tracker.persistent.DBConfiguration;
import drakkar.stern.tracker.persistent.objects.EvaluationData;
import drakkar.stern.tracker.persistent.objects.IndexData;
import drakkar.stern.tracker.persistent.objects.SessionData;
import drakkar.stern.tracker.persistent.tables.DriverException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Esta clase representa todas operaciones soportados por el framework DrakkarKeel para
 * ejecutarse en la aplcación servidora
 *
 * 
 *
 * @deprecated As of DrakkarKeel version 1.1,
 * replaced by <code>DrakkarStern</code>.
 *
 * @see drkkar.stern.DrakkarStern
 *
 */
public class DesktopSternApp {

    public static final int DEFAULT_SERVER_PORT = 11900;
    private static DesktopSternApp instance = null;

    public DesktopSternApp() {
        this.retrievalManager = new RetrievalManager();
        this.state = false;
        this.serverData = new SternData();
        this.args = new String[0];
        this.listener = null;
    }

    protected DesktopSternApp(int port) {
        this.retrievalManager = new RetrievalManager();
        this.state = false;
        this.serverData = new SternData(port);
        this.args = new String[0];
        this.listener = null;
    }

    public static DesktopSternApp getInstance() {
        if (instance == null) {
            instance = new DesktopSternApp();
        }
        return instance;
    }

    public static DesktopSternApp getInstance(String[] args) {
        int port = DesktopSternApp.DEFAULT_SERVER_PORT;
        for (int i = 0; i < args.length - 1; i++) {
            port = args[i].equalsIgnoreCase("-sp") ? Integer.parseInt(args[i + 1]) : DesktopSternApp.DEFAULT_SERVER_PORT;
        }

        if (instance == null) {
            instance = new DesktopSternApp(port);
        }
        return instance;
    }

    public void init(String[] args) {
        //- sp
        int port;
        for (int i = 0; i < args.length - 1; i++) {
            port = args[i].equalsIgnoreCase("-sp") ? Integer.parseInt(args[i + 1]) : DesktopSternApp.DEFAULT_SERVER_PORT;
        }
        
    }

    /**
     * Constructor de la clase
     *
     * @param searchers buscadores con los cuales se inciará la sesión de comunicación
     *                  del servidor
     *
     */
    public DesktopSternApp(Searchable[] searchers) {
        this.retrievalManager = new RetrievalManager(searchers);
        this.state = false;
        this.serverData = new SternData();
        this.args = new String[0];
        this.listener = null;
    }

    /**
     * Constructor de la clase
     *
     * @param searchers buscadores con los cuales se inciará la sesión de comunicación
     *                  del servidor
     * @param dbConfig
     *
     */
    public DesktopSternApp(List<Searchable> searchers, DBConfiguration dbConfig) {
        this.retrievalManager = new RetrievalManager(searchers);
        this.state = false;
        this.serverData = new SternData();
        this.args = new String[0];
        this.listener = null;
        this.dbController = new DataBaseController(dbConfig);
    }

    /**
     *
     * @param searchers
     * @param dbConfig
     * @param emailConfig
     */
    public DesktopSternApp(List<Searchable> searchers, DBConfiguration dbConfig, EmailConfig emailConfig) {
        this.retrievalManager = new RetrievalManager(searchers);
        this.state = false;
        this.serverData = new SternData();
        this.args = new String[0];
        this.listener = null;
        this.dbController = new DataBaseController(dbConfig, emailConfig);
    }

    /**
     * Constructor de la clase
     *
     * @param searchers  buscadores con los cuales se inciará la sesión de comunicación
     *                   del servidor
     * @param serverData datos de configuración del servidor
     *
     */
    public DesktopSternApp(Searchable[] searchers, SternData serverData) {
        this.retrievalManager = new RetrievalManager(searchers);
        this.serverData = serverData;
        this.state = false;
        this.args = new String[0];

    }

    /**
     * Constructor de la clase
     *
     * @param searchers  buscadores con los cuales se inciará la sesión de comunicación
     *                   del servidor
     * @param serverData datos de configuración del servidor
     * @param dbConfig
     *
     */
    public DesktopSternApp(List<Searchable> searchers, SternData serverData, DBConfiguration dbConfig) {
        this.retrievalManager = new RetrievalManager(searchers);
        this.serverData = serverData;
        this.state = false;
        this.args = new String[0];
        this.dbController = new DataBaseController(dbConfig);

    }

    /**
     *
     * @param server
     * @param dbController
     */
    public DesktopSternApp(SternData server, DataBaseController dbController) {
        this.retrievalManager = null;
        this.state = false;
        this.serverData = server;
        this.args = new String[0];
        this.listener = null;
        this.dbController = dbController;
    }

    /**
     *
     * @param server
     * @param listener
     * @param dbConfig
     */
    public DesktopSternApp(SternData server, FacadeListener listener, DBConfiguration dbConfig) {
        this.retrievalManager = null;
        this.state = false;
        this.serverData = server;
        this.args = new String[0];
        this.listener = listener;
        this.dbController = new DataBaseController(dbConfig);
    }

    /**
     * Constructor de la clase
     *
     * @param searchers  buscadores con los cuales se inciará la sesión de comunicación
     *                   del servidor
     * @param serverData datos del servidor
     * @param listener   oyente para la notificación del estado de los procesos
     */
    public DesktopSternApp(Searchable[] searchers, SternData serverData, FacadeListener listener) {
        this.retrievalManager = new RetrievalManager(searchers);
        this.serverData = serverData;
        this.listener = listener;
        this.state = false;
        this.args = new String[0];

    }

    /**
     * Constructor de la clase
     *
     * @param searchers  buscadores con los cuales se inciará la sesión de comunicación
     *                   del servidor
     * @param serverData datos del servidor
     * @param listener   oyente para la notificación del estado de los procesos en el servidor
     * @param dbConfig
     */
    public DesktopSternApp(List<Searchable> searchers, SternData serverData, FacadeListener listener, DBConfiguration dbConfig) {
        this.retrievalManager = new RetrievalManager(searchers);
        this.serverData = serverData;
        this.listener = listener;
        this.state = false;
        this.args = new String[0];
        this.dbController = new DataBaseController(dbConfig);
    }

    /**
     * Inicia el servicio de comunicación del servidor
     *
     * @throws NullPointerException si ocurre algún error durante el proceso de inicio
     *                              de la comunicación del servidor, tales como parámetros
     *                              de configuración del servidor con valor nulo
     */
    public void start() throws NullPointerException {
        state = true;
        server = Stern.getInstance();
        server.setListener(listener);
        communication = new Communication();
        communication.initialize();
        server.setConfiguration(communication, serverData);
        server.start();
        if (retrievalManager == null) {
            retrievalManager = new RetrievalManager();
        }
       serverServant = new SternServant(retrievalManager, listener, dbController);
       
        container = new ContainerController(communication, serverData, server.getUUID(), serverServant, listener);
        server.addContainer(container);

        if (dbController != null && !dbController.isOpen()) {

            if (dbController.existDatabase()) {
                try {
                    dbController.openConnection();
                } catch (        DriverException | SQLException ex) {
                   OutputMonitor.printStream("",ex);
                }
            } else {
                //notificar que la bd no existe
                this.notifyListener(ERROR_MESSAGE, "Database doesn't exist, you must build it.");
            }

        } 
    }

    /**
     * Inicia el servicio de comunicación del servidor
     *
     * @param args argumentos pasados por el método main para inicializar el
     *             entorno de ejecución de ice
     *
     * @throws NullPointerException si ocurre algún error durante el proceso de inicio
     *                              de la comunicación del servidor, tales como parámetros
     *                              de configuración del servidor con valor nulo
     */
    public void start(String[] args) throws NullPointerException {
        state = true;
        server = Stern.getInstance();
        server.setListener(listener);
        communication = new Communication();
        communication.initialize(args);
        server.setConfiguration(communication, serverData);
        server.start();
        serverServant = new SternServant(retrievalManager, listener, dbController);        
        container = new ContainerController(communication, serverData, server.getUUID(), serverServant, listener);
        server.addContainer(container);
        if (dbController != null && !dbController.isOpen()) {
            try {
                dbController.openConnection();
            } catch (    DriverException | SQLException ex) {
               OutputMonitor.printStream("",ex);
            }
        }
    }

    /**
     * Renicia el servicio de comunicación del servidor
     */
    public void restart() {
        if (state) {
            state = false;
            try {
                serverServant.notifyServerState(SERVER_RESET,"old-restar");
            } catch (    IOException | IllegalArgumentException ex) {
               OutputMonitor.printStream("",ex);
                this.notifyListener(ERROR_MESSAGE, ex.getMessage());
            }
            //stopServer();
            server.stop();
            if (dbController != null && dbController.isOpen()) {
                dbController.closeConnection();

            }
        }
        start();

    }

    /**
     * Renicia el servicio de comunicación del servidor
     *
     * @param args argumentos pasados por el método main para inicializar el
     *             entorno de ejecución de ice
     */
    public void restart(String[] args) {
        if (state) {
            state = false;
            try {
                serverServant.notifyServerState(SERVER_RESET, "old-restar");
            } catch (    IOException | IllegalArgumentException ex) {
               OutputMonitor.printStream("",ex);
                this.notifyListener(ERROR_MESSAGE, ex.getMessage());
            }
            server.stop();
            if (dbController != null && dbController.isOpen()) {
                dbController.closeConnection();

            }
        }
        start(args);

    }

    /**
     * Detiene el servicio de comunicación del servidor
     *    
     */
    public void stopServer() {
        if (state) {
            state = false;
            try {
                serverServant.notifyServerState(SERVER_STOPPED, "old-restar");
            } catch (    IOException | IllegalArgumentException ex) {
               OutputMonitor.printStream("",ex);
                this.notifyListener(ERROR_MESSAGE, ex.getMessage());
            }
            server.stop();
            if (dbController != null && dbController.isOpen()) {
                dbController.closeConnection();

            }
        }
    }

    /**
     * Develve el estado del servidor
     *
     * @return true si el servidor se encuentra iniciado, false en caso contrario
     */
    public boolean isRunning() {
        return state;
    }

    /**
     * {@inheritDoc}
     */
    public long makeIndex() throws IndexException {
        return this.retrievalManager.getIndexManager().makeIndex();
    }

    /**
     * {@inheritDoc}
     */
    public long makeIndex(File collectionPath) throws IndexException {
        return this.retrievalManager.getIndexManager().makeIndex(collectionPath);
    }

    /**
     * {@inheritDoc}
     */
    public long makeIndex(File collectionPath, File indexPath) throws IndexException {
        return this.retrievalManager.getIndexManager().makeIndex(collectionPath, indexPath);
    }

    /**
     * {@inheritDoc}
     */
    public long makeIndex(List<File> collectionPath) throws IndexException {
        return this.retrievalManager.getIndexManager().makeIndex(collectionPath);
    }

    /**
     * {@inheritDoc}
     */
    public long makeIndex(List<File> collectionPath, File indexPath) throws IndexException {
        return this.retrievalManager.getIndexManager().makeIndex(collectionPath, indexPath);
    }

    /**
     * {@inheritDoc}
     */
    public long makeIndex(int searcher, File collectionPath) throws SearchableNotSupportedException, IndexException {
        return this.retrievalManager.getIndexManager().makeIndex(searcher, collectionPath);
    }

    /**
     * {@inheritDoc}
     */
    public long makeIndex(int searcher, File collectionPath, File indexPath) throws SearchableNotSupportedException, IndexException {
        return this.retrievalManager.getIndexManager().makeIndex(searcher, collectionPath, indexPath);
    }

    /**
     * {@inheritDoc}
     */
    public long makeIndex(int searcher, List<File> collectionPath) throws SearchableNotSupportedException, IndexException {
        return this.retrievalManager.getIndexManager().makeIndex(searcher, collectionPath);
    }

    /**
     * {@inheritDoc}
     */
    public long makeIndex(int searcher, List<File> collectionPath, File indexPath) throws SearchableNotSupportedException, IndexException {
        return this.retrievalManager.getIndexManager().makeIndex(searcher, collectionPath, indexPath);
    }

    /**
     * {@inheritDoc}
     */
    public long makeIndex(int[] searchers, File collectionPath) throws SearchableNotSupportedException, IndexException {
        return this.retrievalManager.getIndexManager().makeIndex(searchers, collectionPath);
    }

    /**
     * {@inheritDoc}
     */
    public long makeIndex(int[] searchers, File collectionPath, File indexPath) throws SearchableNotSupportedException, IndexException {
        return this.retrievalManager.getIndexManager().makeIndex(searchers, collectionPath, indexPath);
    }

    /**
     * {@inheritDoc}
     */
    public long makeIndex(int[] searchers, List<File> collectionPath) throws SearchableNotSupportedException, IndexException {
        return this.retrievalManager.getIndexManager().makeIndex(searchers, collectionPath);
    }

    /**
     * {@inheritDoc}
     */
    public long makeIndex(int[] searchers, List<File> collectionPath, File indexPath) throws SearchableNotSupportedException, IndexException {
        return this.retrievalManager.getIndexManager().makeIndex(searchers, collectionPath, indexPath);
    }

    /**
     * {@inheritDoc}
     */
    public long updateIndex(int searcher, File collectionPath) throws SearchableNotSupportedException, IndexException {
        return this.retrievalManager.getIndexManager().updateIndex(searcher, collectionPath);
    }

    /**
     * {@inheritDoc}
     */
    public long updateIndex(int searcher, List<File> collectionPath) throws SearchableNotSupportedException, IndexException {
        return this.retrievalManager.getIndexManager().updateIndex(searcher, collectionPath);
    }

    /**
     * {@inheritDoc}
     */
    public long updateIndex(int searcher, File collectionPath, File indexPath) throws SearchableNotSupportedException, IndexException {
        return this.retrievalManager.getIndexManager().updateIndex(searcher, collectionPath, indexPath);
    }

    /**
     * {@inheritDoc}
     */
    public long updateIndex(int searcher, List<File> collectionPath, File indexPath) throws SearchableNotSupportedException, IndexException {
        return this.retrievalManager.getIndexManager().updateIndex(searcher, collectionPath, indexPath);
    }

    /**
     * {@inheritDoc}
     */
    public long updateIndex(int[] searchers, File collectionPath, File indexPath) throws SearchableNotSupportedException, IndexException {
        return this.retrievalManager.getIndexManager().updateIndex(searchers, collectionPath, indexPath);
    }

    /**
     * {@inheritDoc}
     */
    public long updateIndex(int[] searchers, List<File> collectionPath, File indexPath) throws SearchableNotSupportedException, IndexException {
        return this.retrievalManager.getIndexManager().updateIndex(searchers, collectionPath, indexPath);
    }

    /**
     * {@inheritDoc}
     */
    public long updateIndex(int[] searchers, File collectionPath) throws SearchableNotSupportedException, IndexException {
        return this.retrievalManager.getIndexManager().updateIndex(searchers, collectionPath);
    }

    /**
     * {@inheritDoc}
     */
    public long updateIndex(int[] searchers, List<File> collectionPath) throws SearchableNotSupportedException, IndexException {
        return this.retrievalManager.getIndexManager().updateIndex(searchers, collectionPath);
    }

    /**
     * {@inheritDoc}
     */
    public boolean loadIndex() throws IndexException {
        return this.retrievalManager.getIndexManager().loadIndex();
    }

    /**
     * {@inheritDoc}
     */
    public boolean loadIndex(File indexPath) throws IndexException {
        return this.retrievalManager.getIndexManager().loadIndex(indexPath);
    }

    /**
     * {@inheritDoc}
     */
    public boolean loadIndex(int searcher) throws IndexException, SearchableNotSupportedException {
        return this.retrievalManager.getIndexManager().loadIndex(searcher);
    }

    /**
     * {@inheritDoc}
     */
    public boolean loadIndex(int[] searchers) throws IndexException, SearchableNotSupportedException {
        return this.retrievalManager.getIndexManager().loadIndex(searchers);
    }

    /**
     * {@inheritDoc}
     */
    public boolean loadIndex(int searcher, File indexPath) throws SearchableNotSupportedException, IndexException {
        return this.retrievalManager.getIndexManager().loadIndex(searcher, indexPath);
    }

    /**
     * {@inheritDoc}
     */
    public boolean loadIndex(int[] searchers, File indexPath) throws SearchableNotSupportedException, IndexException {
        return this.retrievalManager.getIndexManager().loadIndex(searchers, indexPath);
    }

    /******************************DB***************************************/
    /**
     *
     * @return
     */
    public boolean isOpen() {
        return this.dbController.isOpen();
    }

    /**
     * Inserta un índice en la BD.
     *
     * @param url
     * @param idSE
     * @param docs 
     * @return
     * @throws SQLException
     */
    public boolean insertIndex(String url, int idSE, int docs) throws SQLException {
        return this.dbController.insertIndex(url, idSE, docs);
    }

    /**
     * Obtiene el id del motor en la BD.
     *
     * @param name
     * @return
     * @throws SQLException
     */
    public int getSearchEngine(String name) throws SQLException {
        return this.dbController.getSearchEngine(name);
    }

    /**
     * Elimina un índice de la BD.
     *
     * @param value
     * @throws SQLException
     */
    public void deleteIndex(String value) throws SQLException {
        this.dbController.deleteIndex(value);
    }

    /**
     * Obtiene todos los índices
     *
     * @return
     * @throws SQLException
     */
    public List<IndexData> getAllIndex() throws SQLException {
        return this.dbController.getAllIndex();
    }

    /**
     * Actualiza la fecha de un índice
     *
     * @param time
     * @param url
     * @param count
     * @throws SQLException
     */
    public void updateDBIndex(Date time, String url, int count) throws SQLException {
        this.dbController.updateIndex(time, url, count);
    }

    /**
     * 
     * @param uri
     * @param count
     * @throws SQLException
     */
    public void addDocsIndex(String uri, int count) throws SQLException {
        this.dbController.addDocsIndex(uri, count);
    }

    /**
     *
     * @param path
     * @param context
     * @param docs
     * @return
     * @throws SQLException
     */
//    public boolean insertCollection(String path, String context, int docs) throws SQLException {
//        return this.dbController.saveCollection(path, context, docs);
//    }

    /**
     * 
     * @param path
     * @param session
     * @return
     * @throws SQLException
     */
    public boolean insertCollectionSession(String path, String session) throws SQLException {
        return this.dbController.saveCollectionSession(path, session);
    }

    /**
     * 
     * @param dbConfig
     */
    public void setDBConfiguration(DBConfiguration dbConfig) {
        this.dbController = new DataBaseController(dbConfig);
    }

    /**
     *
     * @return
     * @throws DriverException
     * @throws SQLException
     */
    public boolean openConnection() throws DriverException, SQLException {
        if (dbController != null && !dbController.isOpen()) {
            return this.dbController.openConnection();
        } else {
            return false;
        }

    }

    /**
     * 
     * @return
     */
    public boolean closeConnection() {
        return this.dbController.closeConnection();
    }

    /**
     * 
     * @param user
     * @param password
     * @throws SQLException
     */
    public void setNewPassword(String user, String password) throws SQLException {
        this.dbController.setNewPassword(user, password);
    }

    /**
     * 
     * @param user
     * @param password
     * @return
     * @throws SQLException
     * @throws NoSuchAlgorithmException
     */
    public boolean verifyManager(String user, String password) throws SQLException, NoSuchAlgorithmException {
        return this.dbController.isManager(user, password);
    }

    /**
     *
     * @throws SQLException
     * @throws NoSuchAlgorithmException
     * @throws DriverException
     */
    public void buildDB() throws SQLException, NoSuchAlgorithmException, DriverException {
        this.dbController.buildDB();
        this.notifyListener(INFORMATION_MESSAGE, "Database was build in " + dbController.getLocation());

    }

    /**
     *
     * @param uri
     * @throws SQLException
     * @throws NoSuchAlgorithmException
     * @throws DriverException
     */
    public void backupDB(String uri) throws SQLException, NoSuchAlgorithmException, DriverException {
        this.dbController.backupDB(uri);
        this.notifyListener(INFORMATION_MESSAGE, "Database was save in " + uri);

    }

    /**
     * 
     * @param uri
     * @throws SQLException
     * @throws NoSuchAlgorithmException
     * @throws DriverException
     */
    public void restoreDB(String uri) throws SQLException, NoSuchAlgorithmException, DriverException {
     //   this.dbController.restoreDB(uri);
        this.notifyListener(INFORMATION_MESSAGE, "Database was restore from " + uri);

    }

    /**
     * 
     * @param idSE
     * @return
     * @throws SQLException
     */
    public List<String> getIndexList(int idSE) throws SQLException {
        return this.dbController.getIndexList(idSE);
    }

    /**
     * 
     * @return
     * @throws SQLException
     */
    public List<SessionData> getAllSessions() throws SQLException {
        return this.dbController.getAllSessions();

    }

    /******************************EVALUATION*****************************/
    /**
     *
     * @param sessionName
     * @param pathRelevJudg
     * @return
     * @throws SessionException
     * @throws QueryNotExistException
     * @throws SQLException
     */
    public List<EvaluationData> evaluate(String sessionName, String pathRelevJudg) throws SessionException, QueryNotExistException, SQLException {
        EvaluationData eval = null;
        List<EvaluationData> evaluations = new ArrayList<>();

        if (dbController != null && dbController.isOpen()) {
            evaluation = new RefereeDB(dbController, pathRelevJudg);

            //List<String> queries = dbController.getAllQueries(sessionName);

            List<String> queries = evaluation.getQueries();

            for (String query : queries) {
                List<String> relevDocs = dbController.getAllRelevantDocuments(sessionName, query);
                float precision = evaluation.evaluatePrecision(sessionName, query, relevDocs);
                float recall = evaluation.evaluateRecall(sessionName, query, relevDocs);
                eval = new EvaluationData(sessionName, query, precision, recall);
                evaluations.add(eval);
            }


        }

        return evaluations;
    }

    /********************************SERVER*****************************************/
    /**
     * Devuelve el nombre del adaptador de comunicación
     *
     * @return nombre
     */
    public String getAdapterName() {
        return serverData.getAdapterName();
    }

    /**
     * Modifica el nombre del adaptador de comunicación
     *
     * @param adapterName nombre del adaptador
     */
    public void setAdapterName(String adapterName) {
        this.serverData.setAdapterName(adapterName);
    }

    /**
     * Devuelve el nombre del contenedor de comunicación
     *
     * @return nombre
     */
    public String getContainerName() {
        return this.serverData.getContainerName();
    }

    /**
     * Modifica el nombre del contenedor de comunicación
     *
     * @param containerName nombre del contenedor
     */
    public void setContainerName(String containerName) {
        this.serverData.setContainerName(containerName);
    }

    /**
     * Devuelve la descripción del servidor
     *
     * @return descripción
     */
    public String getServerDescription() {
        return this.serverData.getServerDescription();
    }

    /**
     * Modifica la descripción del servidor
     *
     * @param serverDescription descripción del servidor
     */
    public void setServerDescription(String serverDescription) {
        this.serverData.setSessionDescription(serverDescription);
    }

    /**
     * Devuelve el nombre del servidor
     *
     * @return nombre del servidor
     */
    public String getServerName() {
        return this.serverData.getServerName();
    }

    /**
     * Modifica el nombre del servidor
     *
     * @param serverName nombre del servidor
     */
    public void setServerName(String serverName) {
        this.serverData.setServerName(serverName);
    }

    /**
     * Modifica los buscadores disponibles en la actual sesión de comunicación
     *
     * @param searchers buscadores
     */
    public void setSearchables(Searchable[] searchers) {
        this.retrievalManager = new RetrievalManager(searchers);
    }

    public void addSearcher(Searchable s) {
        this.retrievalManager.addSearcher(s);
    }

    /**
     * Modifica los buscadores disponibles en la actual sesión de comunicación
     *
     * @param searchers buscadores
     */
    public void setSearchables(List<Searchable> searchers) {
        this.retrievalManager = new RetrievalManager(searchers);
    }

    /**
     * Dertimina si el buscador especificado se encuentra activado
     *
     * @param idSearchable identificador del buscador
     *
     * @return true si el buscador se encuantra activo, false en caso contrario
     */
    public boolean isSearchableEnabled(int idSearchable) {
        return this.retrievalManager.isSearchableEnabled(idSearchable);
    }

    /**
     * Modifica el estado del buscador especificado
     *
     * @param idSearchable identificador del buscador
     * @param enable       estado del buscador
     *
     * @throws SearchableNotSupportedException
     */
    public void setSearchableEnabled(int idSearchable, boolean enable) throws SearchableNotSupportedException {
        this.retrievalManager.setSearchableEnabled(idSearchable, enable);
    }

    /**
     * Modifica el estado del buscador especificado
     *
     * @param idSearchable identificador del buscador
     * @param uriIndex
     * @throws FileNotFoundException
     *
     */
    public void setSearchableIndex(int idSearchable, String uriIndex) throws FileNotFoundException {
        this.retrievalManager.setSearchableIndex(idSearchable, uriIndex);
    }

    /**
     * Modifica el estado del buscador especificado
     *
     * @param idSearchable identificador del buscador
     * @param uriIndex
     *
     */
    public void setSearchableCollection(int idSearchable, String uriIndex) {
        this.retrievalManager.setSearchableCollection(idSearchable, uriIndex);
    }

    /**
     *
     * @return
     */
    public File getSearchableIndex() {
        return this.retrievalManager.getIndexPath();
    }

    /**
     *
     * @return
     */
    public File getSearchableCollection() {
        return this.retrievalManager.getCollectionPath();
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

    /**
     * Notifica al servidor de algún error que ocurra
     *
     *
     */
    public void notifyAvailableSearchers() {
        this.serverServant.notifyAvailableSearchers();
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
    private Stern server;
    private Communication communication;
    private ContainerController container;
    /**
     * 
     */
    public SternServant serverServant;
    private RetrievalManager retrievalManager;
    private String[] args;
    private FacadeListener listener;
    private boolean state;
    private SternData serverData;
    private DataBaseController dbController;
    int action;
    RefereeDB evaluation;

    public void waitForShutdown() {
        while (true){}
    }

   
}
