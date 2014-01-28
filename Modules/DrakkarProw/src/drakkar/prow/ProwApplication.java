/*
 * DrakkarKeel - An Enterprise Collaborative Search Platform
 *
 * The contents of this file are subject under the terms described in the
 * DRAKKARKEEL_LICENSE file included in this distribution; you may not use this
 * file except in compliance with the License. 
 *
 * 2013-2014 DrakkarKeel Platform.
 */
package drakkar.prow;

import drakkar.prow.communication.DelegateRole;
import drakkar.prow.communication.DelegateServerManager;
import drakkar.prow.communication.DelegateSession;
import drakkar.prow.communication.DelegateSessionContainer;
import drakkar.prow.communication.NetworkController;
import drakkar.prow.communication.RequestDispatcher;
import drakkar.prow.facade.ListenerManager;
import drakkar.prow.file.transfer.DelegateFileStore;
import drakkar.oar.Communication;
import drakkar.oar.Request;
import drakkar.oar.Response;
import drakkar.oar.Seeker;
import drakkar.oar.security.DrakkarSecurity;
import drakkar.oar.slice.error.RequestException;
import drakkar.oar.slice.transfer.FileAccessException;
import drakkar.oar.util.Invocation;
import drakkar.oar.util.OutputMonitor;
import drakkar.oar.util.SettingProperties;
import drakkar.oar.util.Utilities;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Esta clase contiene los métodos que pueden emplear los clientes para invocar
 * las distintas operaciones soportadas por la aplicación servidora del
 * Framework DrakkarKeel
 */
public abstract class ProwApplication implements Serializable {

    private static final long serialVersionUID = 80000000000004L;

    // declaration of variables
    protected DelegateServerManager server;
    protected DelegateSession session;
    protected DelegateSessionContainer container;
    protected DelegateRole dRole;
    protected DelegateFileStore fileStore;
    protected ApplicationManager appManager;
    protected Communication communication;
    protected RequestDispatcher rqsDispatcher;
    protected ListenerManager listenerManager;
    protected NetworkController netController;
    protected String[] args;
    protected boolean startedCommunication = false;
    protected ProwSetting clientSetting;
    protected Connection cnxServer;

    /**
     * Constructor de la clase
     */
    public ProwApplication() {

        this.cnxServer = new Connection();
        this.clientSetting = new ProwSetting();
        this.args = new String[0];
        this.communication = new Communication();

    }

    /**
     * Contructor de la clase
     *
     * @param args argumentos para la incialización de la aplicación
     */
    public ProwApplication(String[] args) {

        this.cnxServer = new Connection();
        this.clientSetting = new ProwSetting();
        this.args = args;
        this.communication = new Communication();

        this.initValues(args);
    }

    /**
     * Contructor de la clase
     *
     * @param cnxServer configuración del servidor
     */
    public ProwApplication(Connection cnxServer) {

        this.cnxServer = cnxServer;
        this.clientSetting = new ProwSetting();
        this.args = new String[0];
        this.communication = new Communication();

    }

    /**
     * Contructor de la clase
     *
     * @param cnxServer configuración del servidor
     * @param client configuración del cliente
     */
    public ProwApplication(Connection cnxServer, ProwSetting client) {

        this.cnxServer = cnxServer;
        this.clientSetting = client;
        this.args = new String[0];
        this.communication = new Communication();
    }

    /**
     * Contructor de la clase
     *
     * @param cnxServer configuración del servidor
     * @param clientSetting configuración del cliente
     * @param propertiesPath ubicación del fichero de propiedades
     */
    public ProwApplication(Connection cnxServer, ProwSetting clientSetting, String propertiesPath) {

        this.cnxServer = cnxServer;
        this.clientSetting = clientSetting;
        this.args = new String[0];
        this.communication = new Communication();

    }

    /**
     * Contructor de la clase
     *
     * @param cnxServer configuración del servidor
     * @param clientSetting configuración del cliente
     * @param args argumentos para la incialización de la aplicación
     */
    public ProwApplication(Connection cnxServer, ProwSetting clientSetting, String[] args) {
        this.cnxServer = cnxServer;
        this.clientSetting = clientSetting;
        this.args = args;
        this.communication = new Communication();
        this.initValues(args);
    }

    /**
     * Captura los valores pasados por parámetros, para la configuración de la
     * aplicacion
     *
     * @param args argumentos para la incialización de la aplicación
     */
    protected void initValues(String[] args) {
        //- sh -sp -cp
        Map m = new HashMap();
        for (int i = 0; i < args.length - 1; i++) {
            m.put(args[i], args[i + 1]);
        }

        this.cnxServer.setServerHost(m.containsKey("-sh") ? m.get("-sh").toString() : this.cnxServer.getServerHost());
        this.cnxServer.setServerPort(m.containsKey("-sp") ? Integer.parseInt(m.get("-sp").toString()) : this.cnxServer.getServerPort());
        clientSetting.setPortNumber(m.containsKey("-cp") ? Integer.parseInt(m.get("-cp").toString()) : clientSetting.getPortNumber());
    }

    /**
     * Inicializa la comunicación con el servidor.
     *
     * @throws RequestException es lanzada cuando no se puede establecer la
     * comunicación el servidor
     */
    protected void activeCommunication() throws RequestException {
        try {
            OutputMonitor.printLine("Initialize communication", OutputMonitor.INFORMATION_MESSAGE);

            // iniciar el entorno de ejecución de ICE
            if (communication.isInitialize()) {
                communication.reset();
            } else {
                communication.initialize(this.args);
            }
            SettingProperties.loadSettingProperies();
            ApplicationContext.initContext();
            String host = cnxServer.getServerHost();
            int serverPort = cnxServer.getServerPort();
            int clientPort = clientSetting.getPortNumber();
            OutputMonitor.printLine("Server host: " + host, OutputMonitor.INFORMATION_MESSAGE);
            OutputMonitor.printLine("Server port: " + serverPort, OutputMonitor.INFORMATION_MESSAGE);
            OutputMonitor.printLine("Server name: " + cnxServer.getServerName(), OutputMonitor.INFORMATION_MESSAGE);
            OutputMonitor.printLine("Container name: " + cnxServer.getContainerName(), OutputMonitor.INFORMATION_MESSAGE);
            OutputMonitor.printLine("Session name: " + cnxServer.getSessionName(), OutputMonitor.INFORMATION_MESSAGE);
            OutputMonitor.printLine("File store name: " + cnxServer.getFileStoreName(), OutputMonitor.INFORMATION_MESSAGE);

            if (clientPort <= 0) {
                clientPort = (clientPort <= 0) ? Utilities.getAvailablePort() : clientPort;
                clientSetting.setPortNumber(clientPort);
            }

            this.appManager = new ApplicationManager(communication, netController, clientPort);
            this.appManager.activate();
            this.server = new DelegateServerManager(communication, cnxServer.getServerName(), host, serverPort);
            this.server.create(); // se establece la comunicación con el proxy del servidor
            this.container = server.getDlgSessionContainer(cnxServer.getContainerName());
            this.session = container.getNoPersistentSessionByName(cnxServer.getSessionName(), Invocation.SYNCHRONOUS_METHOD_INVOCATION);
            this.session.setCommunication(container.getCommunication());
            this.fileStore = new DelegateFileStore(communication, cnxServer.getFileStoreName(), host, serverPort);
            this.fileStore.create();
            this.startedCommunication = true;

            OutputMonitor.printLine("Initialized communication", OutputMonitor.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            throw new RequestException(ex.getMessage());
        }
    }

    /**
     * Registra la entrada del usuario en la sesión de comunicación del servidor
     * sin autenticación. Tomando el nombre de usuario del sistema operativo,
     * más un ID generado automáticamente por sistema, para identificar al
     * seeker en el servidor. Solo para ejemplos demostrativos.
     *
     * @throws RequestException si ocurre alguna error durante el proceso de la
     * solicitud
     */
    public void login() throws RequestException {
        activeCommunication();

        Seeker seeker = clientSetting.getSeeker();
        String user = seeker.getUser() + session.getSeekerID();
        seeker.setUser(user);
        seeker.setState(Seeker.STATE_ONLINE);
        seeker.setRole(Seeker.ROLE_POTENTIAL_MEMBER);
//        ImageIcon fromDB = seeker.getAvatar();
//        this.icon32 = fromDB;
        this.dRole = session.getDlgRole();
        this.rqsDispatcher = new RequestDispatcher(communication.getCommunicator());

        this.dRole.login(seeker, appManager, rqsDispatcher);
    }

    /**
     * Registra la entrada del usuario en la sesión de comunicación del servidor
     * sin autenticación y al mismo tiempo entra en una sesión de búsqueda
     * colaborativa por defecto. Tomando el nombre de usuario del sistema
     * operativo, más un ID generado automáticamente por sistema, para
     * identificar al seeker en el servidor. Solo para ejemplos demostrativos.
     *
     * @throws RequestException si ocurre alguna error durante el proceso de la
     * solicitud
     */
    public void loginSearchCollabSession() throws RequestException {
        activeCommunication();

        Seeker seeker = clientSetting.getSeeker();
        String user = seeker.getUser() + session.getSeekerID();
        seeker.setUser(user);
        seeker.setState(Seeker.STATE_ONLINE);
        seeker.setRole(Seeker.ROLE_CHAIRMAN);
//        ImageIcon fromDB = seeker.getAvatar();
//        this.icon32 = fromDB;
        this.dRole = session.getDlgRole();
        this.rqsDispatcher = new RequestDispatcher(communication.getCommunicator());

        OutputMonitor.printLine("Request connection...", OutputMonitor.INFORMATION_MESSAGE);
        this.dRole.login(seeker, appManager, rqsDispatcher);
        String chairman = session.getChairmanName("DefaultSCS");
        if (!chairman.equals(user)) {
            seeker.setRole(Seeker.ROLE_MEMBER);

        }
    }

    /**
     * Registra la entrada del usuario en la sesión de comunicación del servidor
     * sin autenticación.
     *
     * @param seeker
     * @throws RequestException si ocurre alguna error durante el proceso de la
     * solicitud
     */
    public void login(Seeker seeker) throws RequestException {
        activeCommunication();

        this.rqsDispatcher = new RequestDispatcher(communication.getCommunicator());
        clientSetting.setSeeker(seeker);
        this.dRole = session.getDlgRole();
        seeker.setState(Seeker.STATE_ONLINE);
//        ImageIcon fromDB = seeker.getAvatar();
//        this.icon32 = fromDB;
//        BufferedImage resized = ImageUtil.getFasterScaledInstance(ImageUtil.makeBufferedImage(fromDB.getImage()), 16, 16, java.awt.RenderingHints.VALUE_INTERPOLATION_BILINEAR, false);
        seeker.setRole(Seeker.ROLE_POTENTIAL_MEMBER);
//        seeker.setAvatar(new ImageIcon(resized));

        this.dRole.login(seeker, appManager, rqsDispatcher);
    }

    /**
     * Registra la entrada del usuario en la sesión de comunicación del servidor
     * sin autenticación y al mismo tiempo entra en una sesión de búsqueda
     * colaborativa por defecto. Tomando el nombre de usuario del sistema
     * operativo, más un ID generado automáticamente por sistema, para
     * identificar al seeker en el servidor. Solo para ejemplos demostrativos.
     *
     *
     * @param seeker
     * @throws RequestException si ocurre alguna error durante el proceso de la
     * solicitud
     */
    public void loginSearchCollabSession(Seeker seeker) throws RequestException {
        activeCommunication();

        this.rqsDispatcher = new RequestDispatcher(communication.getCommunicator());
        clientSetting.setSeeker(seeker);
        this.dRole = session.getDlgRole();
//        ImageIcon fromDB = seeker.getAvatar();
//        this.icon32 = fromDB;
        seeker.setState(Seeker.STATE_ONLINE);
        seeker.setRole(Seeker.ROLE_CHAIRMAN);

        OutputMonitor.printLine("Request connection...", OutputMonitor.INFORMATION_MESSAGE);
        this.dRole.login(seeker, appManager, rqsDispatcher);
        String chairman = session.getChairmanName("DefaultSCS");
        if (!chairman.equals(seeker.getUser())) {
            seeker.setRole(Seeker.ROLE_MEMBER);
        }
    }

    /**
     * Registra la entrada del usuario en la sesión de comunicación del servidor
     *
     * @param user usuario
     * @param password contraseña
     *
     * @throws RequestException si ocurre alguna error durante el proceso de la
     * solicitud
     */
    public void login(String user, String password) throws RequestException {
        activeCommunication();

        this.rqsDispatcher = new RequestDispatcher(communication.getCommunicator());
        Seeker seeker = session.login(user, DrakkarSecurity.SHA(password));

        if (seeker == null) {
            listenerManager.notifyFailedConnection();
            return;
        }

        this.clientSetting.setSeeker(seeker);
        this.dRole = session.getDlgRole();
        seeker.setState(Seeker.STATE_ONLINE);
        seeker.setRole(Seeker.ROLE_POTENTIAL_MEMBER);
//        ImageIcon fromDB = seeker.getAvatar();
//        this.icon32 = fromDB;

//        if (fromDB.getIconHeight() != 16 && fromDB.getIconWidth() != 16) {
//            BufferedImage resized = ImageUtil.getFasterScaledInstance(ImageUtil.makeBufferedImage(fromDB.getImage()), 16, 16, java.awt.RenderingHints.VALUE_INTERPOLATION_BILINEAR, false);
//            this.icon16 = new ImageIcon(resized);
//            seeker.setAvatar(icon16);
//        }
        this.dRole.login(seeker, appManager, rqsDispatcher);
    }

    /**
     * Termina la comunicación del usuario con el servidor
     */
    public void logout() {
        if (rqsDispatcher != null && appManager != null) {
            rqsDispatcher.disconnect();
            appManager.deactivate();
            destroy();
        }
    }

    /**
     * Determina la disponibilidad del nombre de usuario seleccionado, en la
     * base de datos de la aplicación servidora
     *
     * @param userName nombre de usuario
     *
     * @return true si el nombre de usuario está disponible, false en caso
     * contrario
     *
     * @throws RequestException si ocurre alguna error durante el proceso de la
     * solicitud
     */
    public boolean isAvailableUser(String userName) throws RequestException {
        if (!startedCommunication) {
            activeCommunication();
        }
        return this.session.isAvailableUser(userName);
    }

    /**
     * Solicita el registro del usuario en la base de datos de la aplicación
     * servidora
     *
     * @param name nombre
     * @param description apellidos
     * @param password contraseña
     * @param userEmail correo
     * @param nickname usuario
     * @param avatar foto de usuario
     *
     * @throws RequestException si ocurre alguna error durante el proceso de la
     * solicitud
     */
    public void registerSeeker(String name, String description, String password, String userEmail, String nickname, byte[] avatar) throws RequestException {
        if (!startedCommunication) {
            activeCommunication();
        }
        this.session.registerSeeker(name, description, DrakkarSecurity.SHA(password), userEmail, nickname, avatar);
    }

    /**
     * Solicita la recuperación de la contraseña de un usuario.
     *
     * @param user usuario
     *
     * <br> <br> <b>Nota:</b> <br> <tt>La nueva contraseña le será enviada al
     * usuario por vía de correo.</tt> <br>
     * @throws RequestException si ocurre algún error durante el proceso de la
     * solicitud
     */
    public void recoverPassword(String user) throws RequestException {
        if (!startedCommunication) {
            activeCommunication();
        }
        this.session.recoverPassword(user);
    }

    /**
     * Solicita el cambio de la contraseña del usuario en el sistema
     *
     * @param user usuario
     * @param currentPassword actual contraseña
     * @param newPassword nueva contraseña
     *
     * @return true si se completó la solicitud, false en caso de que el usuario
     * ó la contraseña sea incorrecto
     *
     * @throws RequestException si ocurre algún error durante el proceso de la
     * solicitud
     */
    public boolean changePassword(String user, String currentPassword, String newPassword) throws RequestException {
        if (!startedCommunication) {
            activeCommunication();
        }
        return this.session.changePassword(user, DrakkarSecurity.SHA(currentPassword), DrakkarSecurity.SHA(newPassword));
    }

    /**
     * Solicita el fichero al servidor, y lo abre con la aplicación por defecto
     * del sistema operativo para ese tipo de fichero
     *
     * @param pathName dirección física del fichero
     *
     * @throws IOException si ocurre algún error en el proceso descargar ó abrir
     * el fichero solicitado
     * @throws FileAccessException
     */
    public void downloadAndOpenFile(String pathName) throws IOException, FileAccessException {
        this.open(downloadFile(pathName).getAbsolutePath());
    }

    /**
     * Solicita el fichero seleccionado al servidor
     *
     * @param pathName dirección física del fichero
     *
     * @return fichero solicitado
     *
     * @throws IOException si ocurre algún error en el proceso descargar el
     * fichero solicitado
     * @throws FileAccessException
     */
    public File downloadFile(String pathName) throws IOException, FileAccessException {
        File local = getLocalFile(pathName);
        if (local.exists()) {
            OutputMonitor.printLine("The resource " + local.getName() + " is downloaded.", OutputMonitor.INFORMATION_MESSAGE);
            return local;
        } else {
            return this.fileStore.getFile(pathName);
        }

    }

    private File getLocalFile(String pathName) {
        File f = new File(pathName);
        return new File(ApplicationContext.cache + File.separator + f.getName());
    }

    /**
     * Abre un fichero con la aplicación por defecto del sistema operativo para
     * el tipo de documento especificado
     *
     * @param pathName dirección física del fichero
     *
     * @throws IOException si ocurre algún error en el proceso de abrir el
     * fichero solicitado
     */
    public void open(String pathName) throws IOException {
        Desktop.getDesktop().open(new File(pathName));
    }

    /**
     * Abre un fichero con la aplicación por defecto del sistema operativo para
     * el tipo de fichero especificado
     *
     * @param file fichero
     *
     * @throws IOException si ocurre algún error en el proceso de abrir el
     * fichero solicitado
     */
    public void open(File file) throws IOException {
        Desktop.getDesktop().open(file);
    }

    /**
     * Este método ejcuta una operación determinada en el servidor, apartir del
     * objeto Request pasado por parámetro
     *
     * @param request operación a realizar, con sus parámetros de entrada
     * @param invocation especifica el modo de invocación y desapcho de la
     * operación: <br> <tt>- SYNCHRONOUS_METHOD_INVOCATION: Synchronous Method
     * Invocation</tt><br> <tt>- ASYNCHRONOUS_METHOD_INVOCATION: Asynchronous
     * Method Invocation</tt><br> <tt>- ASYNCHRONOUS_METHOD_DISPATCH:
     * Asynchronous Method Dispatch</tt><br> <tt>-
     * ASYNCHRONOUS_METHOD_INVOCATION_DISPATCH: Asynchronous Method Invocation
     * and Dispatch</tt><br> <br>
     */
    public void send(Request request, byte invocation) {
        this.rqsDispatcher.send(request, invocation);
    }

    /**
     * Este método ejcuta una operación determinada en el servidor, apartir del
     * objeto Request pasado por parámetros y devuelve un correspondiente objeto
     * Response.
     *
     * @param request operación a realizar, con sus parámetros de entrada
     * @param invocation especifica el modo de invocación y despacho de la
     * operación: <br> <tt>- SYNCHRONOUS_METHOD_INVOCATION: Synchronous Method
     * Invocation</tt><br> <tt>- ASYNCHRONOUS_METHOD_INVOCATION: Asynchronous
     * Method Invocation</tt><br> <tt>- ASYNCHRONOUS_METHOD_DISPATCH:
     * Asynchronous Method Dispatch</tt><br> <tt>-
     * ASYNCHRONOUS_METHOD_INVOCATION_DISPATCH: Asynchronous Method Invocation
     * and Dispatch</tt><br> <br>
     *
     * @return un objeto Response con resultados de la operación ejecutada.
     */
    public Response get(Request request, byte invocation) {
        Response rsp = this.rqsDispatcher.get(request, invocation);
        return rsp;
    }

    /**
     * Este método ejcuta una operación determinada en el servidor, apartir del
     * objeto Request pasado por parámetros y devuelve un correspondiente objeto
     * Response.
     *
     * @param request operación a realizar, con sus parámetros de entrada
     * @param invocation especifica el modo de invocación y despacho de la
     * operación: <br> <tt>- SYNCHRONOUS_METHOD_INVOCATION: Synchronous Method
     * Invocation</tt><br> <tt>- ASYNCHRONOUS_METHOD_INVOCATION: Asynchronous
     * Method Invocation</tt><br> <tt>- ASYNCHRONOUS_METHOD_DISPATCH:
     * Asynchronous Method Dispatch</tt><br> <tt>-
     * ASYNCHRONOUS_METHOD_INVOCATION_DISPATCH: Asynchronous Method Invocation
     * and Dispatch</tt><br> <br>
     *
     * @return un objeto Response con resultados de la operación ejecutada.
     */
    public Ice.AsyncResult begin(Request request) {

        return this.rqsDispatcher.begin(request);
    }

    public void finish(Ice.AsyncResult r) {

        this.rqsDispatcher.finish(r);
    }

    /**
     * Devuelve el la instancia del objeto de comunicación del framework
     *
     * @return objeto comunicación
     */
    public Communication getCommunication() {
        return communication;
    }

    /**
     * Modifica el la instancia del objeto de comunicación del framework
     *
     * @param communication nuevo objeto comunicación
     */
    public void setCommunication(Communication communication) {
        this.communication = communication;
    }

    /**
     * Devuelve la instancia de ListenerManager
     *
     * @return obejto listenersManager
     *
     */
    public abstract ListenerManager getListenerManager();

    /**
     * Devuelve la instancia de ListenerManager
     *
     * @return obejto listenersManager
     *
     */
    public abstract NetworkController getNetworkController();

    /**
     * Modifica la instancia de la clase listenerManager
     *
     * @param listenerManager
     *
     */
    public void setListenerManager(ListenerManager listenerManager) {
        this.listenerManager = listenerManager;
    }

    /**
     * Modifica la instancia de la clase listenerManager
     *
     * @param netController
     *
     */
    public void setNetworkController(NetworkController netController) {
        this.netController = netController;
        this.appManager.setNetworkController(netController);
    }

    /**
     * Devuelve la instancia de la clase RequestDispatcher
     *
     * @return objeto
     *
     */
    public RequestDispatcher getRequestDispatcher() {
        return rqsDispatcher;
    }

    /**
     * Modifica la instancia de la clase DispatchController
     *
     * @param rqsDispatcher nueva instancia
     *
     */
    public void setRequestDispatcher(RequestDispatcher rqsDispatcher) {
        this.rqsDispatcher = rqsDispatcher;
    }

    /**
     * Devuelve una instancia de la clase ProwSetting
     *
     * @return objeto
     *
     */
    public ProwSetting getClientSetting() {
        return clientSetting;
    }

    /**
     * Modifica la instancia de la clase ProwSetting
     *
     * @param clientSetting nuevo objeto
     *
     */
    public void setClientSetting(ProwSetting clientSetting) {
        this.clientSetting = clientSetting;
    }

    /**
     * Finaliza el entorno de ejcución de ICE
     */
    public void destroy() {
        if (this.communication.isInitialize()) {
            this.communication.getCommunicator().destroy();
        }
    }

    /**
     * Elimina todos los archivos temporares
     */
    public void clearCache() {
        File f = new File(ApplicationContext.cache);
        if (f.exists()) {
            Utilities.deleteFiles(f);
        }
    }

    /**
     * Devuelve la onfiguración del servidor
     */
    public Connection getCnxServer() {
        return cnxServer;
    }

}
