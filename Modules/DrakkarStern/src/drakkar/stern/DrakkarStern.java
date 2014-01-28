/*
 * DrakkarKeel - An Enterprise Collaborative Search Platform
 *
 * The contents of this file are subject under the terms described in the
 * DRAKKARKEEL_LICENSE file included in this distribution; you may not use this
 * file except in compliance with the License. 
 *
 * 2013-2014 DrakkarKeel Platform.
 */
package drakkar.stern;



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
import drakkar.oar.util.SettingProperties;
import drakkar.oar.util.Utilities;
import drakkar.mast.IndexException;
import drakkar.mast.RetrievalManager;
import drakkar.mast.SearchableNotSupportedException;
import drakkar.mast.retrieval.Searchable;
import drakkar.stern.callback.CallbackController;
import drakkar.stern.controller.ContainerController;
import drakkar.stern.controller.DataBaseController;
import drakkar.stern.email.EmailConfig;
import drakkar.stern.evaluation.RefereeDB;
import drakkar.stern.facade.event.FacadeListener;
import drakkar.stern.servant.SternServant;
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
 * Esta clase representa todas las operaciones soportadas por el framework DrakkarKeel para
 * ejecutarse en la aplicación servidora
 */
public class DrakkarStern {

    /**
     *
     */
    public static final int DEFAULT_SERVER_PORT = 11900;
    /**
     *
     */
    public static final int TOP_MEMORY_CACHE = 100;//megabytes.....todo
    private static DrakkarStern instance = null;
    // SVNController svnController;

    /**
     *
     */
    public DrakkarStern() {
        this.retrievalManager = new RetrievalManager();
        this.state = false;
        this.serverSetting = new SternAppSetting();
        this.args = new String[0];
        this.listener = null;
        this.loadProperties();
    }

    /**
     *
     * @param port
     */
    protected DrakkarStern(int port) {
        this.retrievalManager = new RetrievalManager();
        this.state = false;
        this.serverSetting = new SternAppSetting(port);
        this.args = new String[0];
        this.listener = null;
        this.loadProperties();
    }

    private void loadProperties() {
        try {
            SettingProperties.loadSettingProperies();
        } catch (Exception ex) {
           OutputMonitor.printStream("",ex);
        }

    }

    /**
     *
     * @return
     */
    public static DrakkarStern getInstance() {
        if (instance == null) {
            instance = new DrakkarStern();
        }
        return instance;
    }

    /**
     *
     * @param args
     * @return
     */
    public static DrakkarStern getInstance(String[] args) {
        int port = DrakkarStern.DEFAULT_SERVER_PORT;
        for (int i = 0; i < args.length - 1; i++) {
            port = args[i].equalsIgnoreCase("-sp") ? Integer.parseInt(args[i + 1]) : DrakkarStern.DEFAULT_SERVER_PORT;
        }

        if (instance == null) {
            instance = new DrakkarStern(port);
        }
        return instance;
    }

    /**
     *
     * @param args
     */
    public void init(String[] args) {
        //- sp
        int port;
        for (int i = 0; i < args.length - 1; i++) {
            port = args[i].equalsIgnoreCase("-sp") ? Integer.parseInt(args[i + 1]) : DrakkarStern.DEFAULT_SERVER_PORT;
        }

    }

    /**
     * Constructor de la clase
     *
     * @param searchers buscadores con los cuales se inciará la sesión de comunicación
     *                  del servidor
     *
     */
    public DrakkarStern(Searchable[] searchers) {
        this.retrievalManager = new RetrievalManager(searchers);
        this.state = false;
        this.serverSetting = new SternAppSetting();
        this.args = new String[0];
        this.listener = null;
        this.loadProperties();
    }

    /**
     * Constructor de la clase
     *
     * @param searchers buscadores con los cuales se inciará la sesión de comunicación
     *                  del servidor
     * @param dbConfig
     *
     */
    public DrakkarStern(List<Searchable> searchers, DBConfiguration dbConfig) {
        this.retrievalManager = new RetrievalManager(searchers);
        this.state = false;
        this.serverSetting = new SternAppSetting();
        this.args = new String[0];
        this.listener = null;
        this.dbController = new DataBaseController(dbConfig);
        this.loadProperties();
    }

    /**
     *
     * @param searchers
     * @param dbConfig
     * @param emailConfig
     */
    public DrakkarStern(List<Searchable> searchers, DBConfiguration dbConfig, EmailConfig emailConfig) {
        this.retrievalManager = new RetrievalManager(searchers);
        this.state = false;
        this.serverSetting = new SternAppSetting();
        this.args = new String[0];
        this.listener = null;
        this.dbController = new DataBaseController(dbConfig, emailConfig);
        this.loadProperties();
    }

    /**
     * Constructor de la clase
     *
     * @param searchers  buscadores con los cuales se inciará la sesión de comunicación
     *                   del servidor
     * @param serverData datos de configuración del servidor
     *
     */
    public DrakkarStern(Searchable[] searchers, SternAppSetting serverData) {
        this.retrievalManager = new RetrievalManager(searchers);
        this.serverSetting = serverData;
        this.state = false;
        this.args = new String[0];
        this.loadProperties();
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
    public DrakkarStern(List<Searchable> searchers, SternAppSetting serverData, DBConfiguration dbConfig) {
        this.retrievalManager = new RetrievalManager(searchers);
        this.serverSetting = serverData;
        this.state = false;
        this.args = new String[0];
        this.dbController = new DataBaseController(dbConfig);
        this.loadProperties();
    }

    /**
     *
     * @param server
     * @param dbController
     */
    public DrakkarStern(SternAppSetting server, DataBaseController dbController) {
        this.retrievalManager = null;
        this.state = false;
        this.serverSetting = server;
        this.args = new String[0];
        this.listener = null;
        this.dbController = dbController;
        this.loadProperties();
    }

    /**
     *
     * @param server
     * @param listener
     * @param dbConfig
     */
    public DrakkarStern(SternAppSetting server, FacadeListener listener, DBConfiguration dbConfig) {
        this.retrievalManager = null;
        this.state = false;
        this.serverSetting = server;
        this.args = new String[0];
        this.listener = listener;
        this.dbController = new DataBaseController(dbConfig);
        this.loadProperties();
    }

    /**
     * Constructor de la clase
     *
     * @param searchers  buscadores con los cuales se inciará la sesión de comunicación
     *                   del servidor
     * @param serverData datos del servidor
     * @param listener   oyente para la notificación del estado de los procesos
     */
    public DrakkarStern(Searchable[] searchers, SternAppSetting serverData, FacadeListener listener) {
        this.retrievalManager = new RetrievalManager(searchers);
        this.serverSetting = serverData;
        this.listener = listener;
        this.state = false;
        this.args = new String[0];
        this.loadProperties();

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
    public DrakkarStern(List<Searchable> searchers, SternAppSetting serverData, FacadeListener listener, DBConfiguration dbConfig) {
        this.retrievalManager = new RetrievalManager(searchers);
        this.serverSetting = serverData;
        this.listener = listener;
        this.state = false;
        this.args = new String[0];
        this.dbController = new DataBaseController(dbConfig);
        this.loadProperties();
    }

    /**
     * Inicia el servicio de comunicación del servidor
     *
     * @throws NullPointerException si ocurre algún error durante el proceso de inicio
     *                              de la comunicación del servidor, tales como parámetros
     *                              de configuración del servidor con valor nulo
     * @throws SQLException         si ocurre algún error al iniciar la conexión con la BD
     */
    public void start() throws NullPointerException, SQLException {
        state = true;
        server = Stern.getInstance();
        server.setListener(listener);
        communication = new Communication();
        communication.initialize();
        server.setConfiguration(communication, serverSetting);

        if (dbController != null && !dbController.isOpen()) {

            if (dbController.existDatabase()) {
                try {
                    dbController.openConnection();
                    OutputMonitor.printLine("Database connection open.", OutputMonitor.INFORMATION_MESSAGE);
                } catch (DriverException ex) {
                    OutputMonitor.printStream("Database connection failed.", ex);
                }
            } else {
                //notificar que la bd no existe
                this.notifyListener(WARNING_MESSAGE, "Database doesn't exist, you must build it.");
            }
        }

        server.start();

        if (retrievalManager == null) {
            retrievalManager = new RetrievalManager();
        }
        serverServant = new SternServant(retrievalManager, listener, dbController, serverSetting);
        callback = CallbackController.getInstance();
        callback.setServerServant(serverServant);
        container = new ContainerController(communication, serverSetting, server.getUUID(), serverServant, listener);
        server.addContainer(container);
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
        server.setConfiguration(communication, serverSetting);

        if (dbController != null && !dbController.isOpen()) {
            try {
                dbController.openConnection();
//                fillSVNData();
//                server.setSvnController(svnController);
                OutputMonitor.printLine("Database connection open.", OutputMonitor.INFORMATION_MESSAGE);

            } catch (    DriverException | SQLException ex) {
                OutputMonitor.printStream("Database connection failed.", ex);
            }
        }
        server.start();

        if (retrievalManager == null) {
            retrievalManager = new RetrievalManager();
        }

        serverServant = new SternServant(retrievalManager, listener, dbController, serverSetting);
        callback = CallbackController.getInstance();
        callback.setServerServant(serverServant);
        container = new ContainerController(communication, serverSetting, server.getUUID(), serverServant, listener);
        server.addContainer(container);
    }

    /**
     * Renicia el servicio de comunicación del servidor
     * @throws NullPointerException
     * @throws SQLException
     */
    public void restart() throws NullPointerException, SQLException {
        if (state) {
            state = false;
            try {
                serverServant.notifyServerState(SERVER_RESET, "reeset");
            } catch (    IOException | IllegalArgumentException ex) {
                OutputMonitor.printStream("", ex);
//               OutputMonitor.printStream("",ex);
                this.notifyListener(ERROR_MESSAGE, ex.getMessage());
            }
            //stopServer();
            server.stop();
            if (dbController != null && dbController.isOpen()) {
                dbController.closeConnection();

            }
            start();
        }


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
                serverServant.notifyServerState(SERVER_RESET, "reset");
            } catch (    IOException | IllegalArgumentException ex) {
                OutputMonitor.printStream("", ex);
//               OutputMonitor.printStream("",ex);
                this.notifyListener(ERROR_MESSAGE, ex.getMessage());
            }
            server.stop();
            if (dbController != null && dbController.isOpen()) {
                dbController.closeConnection();

            }
            start(args);
        }


    }

    /**
     * Detiene el servicio de comunicación del servidor
     *    
     */
    public void stop() {
        if (state) {
            state = false;
            try {
                serverServant.notifyServerState(SERVER_STOPPED, "stop");
            } catch (    IOException | IllegalArgumentException ex) {
                OutputMonitor.printStream("", ex);
                this.notifyListener(ERROR_MESSAGE, ex.getMessage());
            }

            if (dbController != null && dbController.isOpen()) {
                dbController.closeConnection();
                OutputMonitor.printLine("Closed connection of the database.", OutputMonitor.INFORMATION_MESSAGE);
            }
            server.stop();
            OutputMonitor.printLine("Stopped server", OutputMonitor.INFORMATION_MESSAGE);
            
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
     * @return 
     * @throws IndexException
     */
    public long makeIndex() throws IndexException {
        return this.retrievalManager.getIndexManager().makeIndex();
    }

    /**
     * {@inheritDoc}
     * @param collectionPath
     * @return
     * @throws IndexException
     */
    public long makeIndex(File collectionPath) throws IndexException {
        return this.retrievalManager.getIndexManager().makeIndex(collectionPath);
    }

    /**
     * {@inheritDoc}
     * @param collectionPath 
     * @param indexPath 
     * @return
     * @throws IndexException
     */
    public long makeIndex(File collectionPath, File indexPath) throws IndexException {
        return this.retrievalManager.getIndexManager().makeIndex(collectionPath, indexPath);
    }

    /**
     * {@inheritDoc}
     * @param collectionPath 
     * @return
     * @throws IndexException
     */
    public long makeIndex(List<File> collectionPath) throws IndexException {
        return this.retrievalManager.getIndexManager().makeIndex(collectionPath);
    }

    /**
     * {@inheritDoc}
     * @param collectionPath 
     * @param indexPath 
     * @return
     * @throws IndexException
     */
    public long makeIndex(List<File> collectionPath, File indexPath) throws IndexException {
        return this.retrievalManager.getIndexManager().makeIndex(collectionPath, indexPath);
    }

    /**
     * {@inheritDoc}
     * @param searcher 
     * @param collectionPath
     * @return
     * @throws SearchableNotSupportedException
     * @throws IndexException
     */
    public long makeIndex(int searcher, File collectionPath) throws SearchableNotSupportedException, IndexException {
        return this.retrievalManager.getIndexManager().makeIndex(searcher, collectionPath);
    }

    /**
     * {@inheritDoc}
     * @param searcher 
     * @param collectionPath 
     * @param indexPath
     * @return 
     * @throws SearchableNotSupportedException
     * @throws IndexException
     */
    public long makeIndex(int searcher, File collectionPath, File indexPath) throws SearchableNotSupportedException, IndexException {
        return this.retrievalManager.getIndexManager().makeIndex(searcher, collectionPath, indexPath);
    }

    /**
     * {@inheritDoc}
     * @param searcher 
     * @param collectionPath
     * @return 
     * @throws SearchableNotSupportedException
     * @throws IndexException
     */
    public long makeIndex(int searcher, List<File> collectionPath) throws SearchableNotSupportedException, IndexException {
        return this.retrievalManager.getIndexManager().makeIndex(searcher, collectionPath);
    }

    /**
     * {@inheritDoc}
     * @param searcher 
     * @param collectionPath 
     * @param indexPath 
     * @return
     * @throws SearchableNotSupportedException
     * @throws IndexException
     */
    public long makeIndex(int searcher, List<File> collectionPath, File indexPath) throws SearchableNotSupportedException, IndexException {
        return this.retrievalManager.getIndexManager().makeIndex(searcher, collectionPath, indexPath);
    }

    /**
     * {@inheritDoc}
     * @param searchers 
     * @param collectionPath
     * @return
     * @throws SearchableNotSupportedException
     * @throws IndexException
     */
    public long makeIndex(int[] searchers, File collectionPath) throws SearchableNotSupportedException, IndexException {
        return this.retrievalManager.getIndexManager().makeIndex(searchers, collectionPath);
    }

    /**
     * {@inheritDoc}
     * @param searchers 
     * @param collectionPath
     * @param indexPath
     * @return
     * @throws SearchableNotSupportedException
     * @throws IndexException
     */
    public long makeIndex(int[] searchers, File collectionPath, File indexPath) throws SearchableNotSupportedException, IndexException {
        return this.retrievalManager.getIndexManager().makeIndex(searchers, collectionPath, indexPath);
    }

    /**
     * {@inheritDoc}
     * @param searchers 
     * @param collectionPath 
     * @return
     * @throws SearchableNotSupportedException
     * @throws IndexException
     */
    public long makeIndex(int[] searchers, List<File> collectionPath) throws SearchableNotSupportedException, IndexException {
        return this.retrievalManager.getIndexManager().makeIndex(searchers, collectionPath);
    }

    /**
     * {@inheritDoc}
     * @param searchers 
     * @param collectionPath
     * @param indexPath
     * @return
     * @throws SearchableNotSupportedException
     * @throws IndexException
     */
    public long makeIndex(int[] searchers, List<File> collectionPath, File indexPath) throws SearchableNotSupportedException, IndexException {
        return this.retrievalManager.getIndexManager().makeIndex(searchers, collectionPath, indexPath);
    }

//    public long makeSVNIndex(String url, String repListFile, String indexPath, String user, String password, String mergeFactor, String directPath, FacadeListener facade) throws IndexException, SearchableNotSupportedException {
//        return this.retrievalManager.getIndexManager().makeSVNIndex(url, repListFile, indexPath, user, password, mergeFactor, directPath, facade);
//    }
    /**
     * {@inheritDoc}
     * @param searcher 
     * @param collectionPath
     * @return 
     * @throws SearchableNotSupportedException 
     * @throws IndexException
     */
    public long updateIndex(int searcher, File collectionPath) throws SearchableNotSupportedException, IndexException {
        return this.retrievalManager.getIndexManager().updateIndex(searcher, collectionPath);
    }

    /**
     * {@inheritDoc}
     * @param searcher 
     * @param collectionPath 
     * @return
     * @throws SearchableNotSupportedException
     * @throws IndexException
     */
    public long updateIndex(int searcher, List<File> collectionPath) throws SearchableNotSupportedException, IndexException {
        return this.retrievalManager.getIndexManager().updateIndex(searcher, collectionPath);
    }

    /**
     * {@inheritDoc}
     * @param searcher 
     * @param collectionPath 
     * @param indexPath 
     * @return
     * @throws SearchableNotSupportedException
     * @throws IndexException
     */
    public long updateIndex(int searcher, File collectionPath, File indexPath) throws SearchableNotSupportedException, IndexException {
        return this.retrievalManager.getIndexManager().updateIndex(searcher, collectionPath, indexPath);
    }

    /**
     * {@inheritDoc}
     * @param searcher 
     * @param indexPath
     * @param collectionPath 
     * @return
     * @throws SearchableNotSupportedException
     * @throws IndexException
     */
    public long updateIndex(int searcher, List<File> collectionPath, File indexPath) throws SearchableNotSupportedException, IndexException {
        return this.retrievalManager.getIndexManager().updateIndex(searcher, collectionPath, indexPath);
    }

    /**
     * {@inheritDoc}
     * @param searchers 
     * @param indexPath
     * @param collectionPath
     * @return
     * @throws SearchableNotSupportedException
     * @throws IndexException
     */
    public long updateIndex(int[] searchers, File collectionPath, File indexPath) throws SearchableNotSupportedException, IndexException {
        return this.retrievalManager.getIndexManager().updateIndex(searchers, collectionPath, indexPath);
    }

    /**
     * {@inheritDoc}
     * @param searchers 
     * @param collectionPath
     * @param indexPath 
     * @return
     * @throws SearchableNotSupportedException
     * @throws IndexException
     */
    public long updateIndex(int[] searchers, List<File> collectionPath, File indexPath) throws SearchableNotSupportedException, IndexException {
        return this.retrievalManager.getIndexManager().updateIndex(searchers, collectionPath, indexPath);
    }

    /**
     * {@inheritDoc}
     * @param searchers 
     * @param collectionPath
     * @return
     * @throws SearchableNotSupportedException
     * @throws IndexException
     */
    public long updateIndex(int[] searchers, File collectionPath) throws SearchableNotSupportedException, IndexException {
        return this.retrievalManager.getIndexManager().updateIndex(searchers, collectionPath);
    }

    /**
     * {@inheritDoc}
     * @param searchers
     * @param collectionPath
     * @return
     * @throws SearchableNotSupportedException
     * @throws IndexException
     */
    public long updateIndex(int[] searchers, List<File> collectionPath) throws SearchableNotSupportedException, IndexException {
        return this.retrievalManager.getIndexManager().updateIndex(searchers, collectionPath);
    }

    /**
     * {@inheritDoc}
     * @return
     * @throws IndexException
     */
    public boolean loadIndex() throws IndexException {
        return this.retrievalManager.getIndexManager().loadIndex();
    }

    /**
     * {@inheritDoc}
     * @param indexPath
     * @return
     * @throws IndexException
     */
    public boolean loadIndex(File indexPath) throws IndexException {
        return this.retrievalManager.getIndexManager().loadIndex(indexPath);
    }

    /**
     * {@inheritDoc}
     * @param searcher
     * @return
     * @throws IndexException
     * @throws SearchableNotSupportedException
     */
    public boolean loadIndex(int searcher) throws IndexException, SearchableNotSupportedException {
        return this.retrievalManager.getIndexManager().loadIndex(searcher);
    }

    /**
     * {@inheritDoc}
     * @param searchers 
     * @return
     * @throws IndexException 
     * @throws SearchableNotSupportedException
     */
    public boolean loadIndex(int[] searchers) throws IndexException, SearchableNotSupportedException {
        return this.retrievalManager.getIndexManager().loadIndex(searchers);
    }

    /**
     * {@inheritDoc}
     * @param searcher 
     * @param indexPath 
     * @return
     * @throws SearchableNotSupportedException
     * @throws IndexException
     */
    public boolean loadIndex(int searcher, File indexPath) throws SearchableNotSupportedException, IndexException {
        return this.retrievalManager.getIndexManager().loadIndex(searcher, indexPath);
    }

    /**
     * {@inheritDoc}
     * @param searchers 
     * @param indexPath
     * @return
     * @throws SearchableNotSupportedException
     * @throws IndexException
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
     * @param direct 
     * @param indexUri 
     * @param user
     * @param password
     * @param repositoryName
     * @return
     * @throws SQLException
     */
    public boolean insertCollection(String path, String context, int docs, boolean direct, List<String> indexUri, String user, String password, String repositoryName) throws SQLException {
        return this.dbController.saveCollection(path, context, docs, direct, indexUri, user, password, repositoryName);
    }

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
        boolean flag = false;
        if (dbController != null && !dbController.isOpen()) {
            flag = this.dbController.openConnection();
        }

//        if (flag) {
//            fillSVNData();
//            server.setSvnController(svnController);
//        }
        return flag;
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

        if (!dbController.isOpen()) {
            this.dbController.openConnection();
        }
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
     * @param user 
     * @param pass
     * @throws SQLException
     * @throws NoSuchAlgorithmException
     * @throws DriverException
     */
    public void restoreDB(String uri, String user, String pass) throws SQLException, NoSuchAlgorithmException, DriverException {
        this.dbController.restoreDB(uri, user, pass);
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

    /**
     * 
     * @return
     * @throws SQLException
     */
//    public List<String> getAllSVNRepositories() throws SQLException {
//        return this.dbController.getAllSVNRepositories();
//
//    }
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
        
        List<EvaluationData> evaluations = new ArrayList<>();

        if (dbController != null && dbController.isOpen()) {
            evaluation = new RefereeDB(dbController, pathRelevJudg);

            //List<String> queries = dbController.getAllQueries(sessionName);

            List<String> queries = evaluation.getQueries();
            for (String query : queries) {
                List<String> relevDocs = dbController.getAllRelevantDocuments(sessionName, query);
                float precision = evaluation.evaluatePrecision(sessionName, query, relevDocs);
                float recall = evaluation.evaluateRecall(sessionName, query, relevDocs);
                evaluations.add(new EvaluationData(sessionName, query, precision, recall));
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
        return serverSetting.getAdapterName();
    }

    /**
     * Modifica el nombre del adaptador de comunicación
     *
     * @param adapterName nombre del adaptador
     */
    public void setAdapterName(String adapterName) {
        this.serverSetting.setAdapterName(adapterName);
    }

    /**
     * Devuelve el nombre del contenedor de comunicación
     *
     * @return nombre
     */
    public String getContainerName() {
        return this.serverSetting.getContainerName();
    }

    /**
     * Modifica el nombre del contenedor de comunicación
     *
     * @param containerName nombre del contenedor
     */
    public void setContainerName(String containerName) {
        this.serverSetting.setContainerName(containerName);
    }

    /**
     * Devuelve la descripción del servidor
     *
     * @return descripción
     */
    public String getServerDescription() {
        return this.serverSetting.getServerDescription();
    }

    /**
     * Modifica la descripción del servidor
     *
     * @param serverDescription descripción del servidor
     */
    public void setServerDescription(String serverDescription) {
        this.serverSetting.setSessionDescription(serverDescription);
    }

    /**
     * Devuelve el nombre del servidor
     *
     * @return nombre del servidor
     */
    public String getServerName() {
        return this.serverSetting.getServerName();
    }

    /**
     * Modifica el nombre del servidor
     *
     * @param serverName nombre del servidor
     */
    public void setServerName(String serverName) {
        this.serverSetting.setServerName(serverName);
    }

    /**
     * Devuelve el puerto del servidor
     *
     * @return puerto del servidor
     */
    public int getServerPort() {
        return this.serverSetting.getAdapterPort();
    }

    /**
     * Modifica el puerto del servidor
     *
     * @param port número del puerto de escucha del servidor
     */
    public void setServerPort(int port) {
        this.serverSetting.setAdapterPort(port);
    }

    /**
     * Modifica los buscadores disponibles en la actual sesión de comunicación
     *
     * @param searchers buscadores
     */
    public void setSearchables(Searchable[] searchers) {
        this.retrievalManager = new RetrievalManager(searchers);
    }

    /**
     *
     * @param s
     */
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
     * 
     * @param idSearchable
     * @param uriIndex
     * @param enable
     * @throws FileNotFoundException
     */
//    public void setSearchableSVNIndex(int idSearchable, String uriIndex, boolean enable) throws FileNotFoundException {
//        this.retrievalManager.setSearchableSVNIndex(idSearchable, uriIndex, enable);
//    }
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
     * Notifica al servidor de algún error que ocurra
     *
     *
     */
//    public void notifyAvailableSVNRepositories() {
//        this.serverServant.notifyAvailableSVNRepositories();
//    }
    /**
     *
     * @return
     */
    public DataBaseController getDbController() {
        return dbController;
    }

    //Llena un Hashtable con los datos de todos los repositorios indexados
//    private void fillSVNData() throws SQLException {
//
//        Hashtable<String, SVNData> svnRepositoriesDB = new Hashtable<String, SVNData>();
//
//        List<String> list = dbController.getAllSVNRepositories();
//        for (int i = 0; i < list.size(); i++) {
//            String string = list.get(i);
//            SVNData data = dbController.getSVNRepositoryData(string, KeySearchable.SVN_SEARCHER);
//            if (data != null) {
//                svnRepositoriesDB.put(string, data);
//            }
//
//        }
//        svnController = new SVNController(listener);
//        svnController.setSvnRepositoriesDB(svnRepositoriesDB);
//    }
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
    private SternAppSetting serverSetting;
    private DataBaseController dbController;
    int action;
    RefereeDB evaluation;
    CallbackController callback;
      

    /**
     * Elimina todos los files que se guardaron en la carpeta cache
     */
    public void clearCache() {
        File f = new File("./cache");
        if (f.exists()) {
             Utilities.deleteFiles(f);
        }       
    }
    
    public void waitForShutdown() {
        while(true){}
    }    

}
