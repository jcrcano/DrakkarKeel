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

import java.io.Serializable;

/**
 * Esta clase almacena los datos de configuración para establecer la comnunicación
 * con la aplicación servidora
 */
public class Connection implements Serializable{
    private static final long serialVersionUID = 80000000000006L;

    private static String serverName;
    private static String containerName;
    private static String serverHost;
    private static int serverPort;
    private static String sessionName;
    private static String fileStoreName;

    /**
     * Constructor de la clase
     */
    public Connection() {
        serverName = "DefaultServer";
        containerName = "DefaultContainer";
        serverHost = "127.0.0.1";
        serverPort = 11900;
        sessionName = "DefaultSession";
        fileStoreName = "DefaultFileStore";
    }

    /**
     * Constructor de la clase
     *
     * @param serverName  nombre del servidor
     * @param serverHost  dirección host del servidor
     * @param serverPort  puerto de escucha del servidor
     *
     * <br>
     * <br>
     * <b>Nota:</b><br>
     * <tt>Los resto de los atributos no especificados en el constructor, toman
     *     sus valores por defectos.</tt><br>
     *
     */
    public Connection(String serverName, String serverHost, int serverPort) {
        Connection.serverName = serverName;
        Connection.serverHost = serverHost;
        Connection.serverPort = serverPort;
        Connection.containerName = "DefaultContainer";
        Connection.sessionName = "DefaultSession";
        Connection.fileStoreName = "DefaultFileStore";
    }

    /**
     * Constructor de la clase
     *
     * @param serverName    nombre del servidor
     * @param containerName nombre del contenedor de la sesión
     * @param serverHost    dirección host del servidor
     * @param serverPort    puerto de escucha del servidor
     * @param sessionName   nombre de la sesión de comunicación
     *
     * <br>
     * <br>
     * <b>Nota:</b><br>
     * <tt>Los resto de los atributos no especificados en el constructor, toman
     *     sus valores por defectos.</tt><br>
     *  
     */
    public Connection(String serverName, String containerName, String serverHost, int serverPort, String sessionName) {
        Connection.serverName = serverName;
        Connection.containerName = containerName;
        Connection.serverHost = serverHost;
        Connection.serverPort = serverPort;
        Connection.sessionName = sessionName;
        Connection.fileStoreName = "DefaultFileStore";
    }

    /**
     * Constructor de la clase
     *
     * @param serverName    nombre del servidor
     * @param containerName nombre del contenedor de la sesión
     * @param serverHost    dirección host del servidor
     * @param serverPort    puerto de escucha del servidor
     * @param sessionName   nombre de la sesión de comunicación
     * @param fileStoreName nombre del objeto de tranferencia de ficheros
     */
    public Connection(String serverName, String containerName, String serverHost, int serverPort, String sessionName, String fileStoreName) {

        Connection.serverName = serverName;
        Connection.containerName = containerName;
        Connection.serverHost = serverHost;
        Connection.serverPort = serverPort;
        Connection.sessionName = sessionName;
        Connection.fileStoreName = fileStoreName;
    }

    /**
     * Devuelve el nombre del gestor de ficheros
     *
     * @return nombre
     */
    public String getFileStoreName() {
        return fileStoreName;
    }

    /**
     * Modifica el nombre del gestor de ficheros
     *
     * @param fileStoreName nuevo nombre
     */
    public void setFileStoreName(String fileStoreName) {
        Connection.fileStoreName = fileStoreName;
    }

    /**
     * Devuelve el nombre de la sesión de comunicación
     *
     * @return nombre de la sesión
     */
    public String getSessionName() {
        return sessionName;
    }

    /**
     * Modifica el nombre de la sesión de comunicación
     *
     * @param sessionName nuevo nombre
     */
    public void setSessionName(String sessionName) {
        Connection.sessionName = sessionName;
    }

    /**
    /**
     * Devuelve el número del host del servidor
     *
     * @return host
     */
    public String getServerHost() {
        return serverHost;
    }

    /**
     * Modifica el número del host del servidor
     *
     * @param serverHost nuevo host
     */
    public void setServerHost(String serverHost) {
        Connection.serverHost = serverHost;
    }

    /**
    /**
     * Devuelve el nombre del contenedor de sesión
     *
     * @return nombre del contenedor
     */
    public String getContainerName() {
        return containerName;
    }

    /**
     * Modifica el nombre del contenedor de sesión
     *
     * @param containerName nuevo nombre
     */
    public void setContainerName(String containerName) {
        Connection.containerName = containerName;
    }

    /**
     * Devuelve el nombre del servidor
     *
     * @return nombre del servidor
     */
    public String getServerName() {
        return serverName;
    }

    /**
     * Modifica el nombre del servidor
     *
     * @param serverName
     */
    public void setServerName(String serverName) {
        Connection.serverName = serverName;
    }

    /** Devuelve el número del puerto de escucha del servidor
     *
     * @return port
     */
    public int getServerPort() {
        return serverPort;
    }

    /**
     * Modifica el número del puerto de escucha del servidor
     *
     * @param serverPort nuevo puerto
     */
    public void setServerPort(int serverPort) {
        Connection.serverPort = serverPort;
    }
}
