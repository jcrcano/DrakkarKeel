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

import drakkar.oar.util.SettingProperties;
import drakkar.oar.util.Utilities;

/**
 * Esta clase contiene todo los atributos necesarios para establecer todos los
 * servicios soportados por el framework DrakkarKeel en la aplcación servidora
 */
public class SternAppSetting {

   

    private String adapterName;
    private String serverName;
    private String serverDescription;
    private int adapterPort;
    private String containerName;
    private String containerDescription;
    private String sessionName;
    private String sessionDescription;
    private String fileStoreName;

    /**
     * Cosntructor por defecto de la clase
     */
    public SternAppSetting() {
        this.adapterName = "DefaultAdapter";
        this.serverName = "DefaultServer";
        this.serverDescription = "Default server";
        this.adapterPort = 11900;
        this.containerName = "DefaultContainer";
        this.containerDescription = "Default description";
        this.sessionName = "DefaultSession";
        this.sessionDescription = "Default description";
        this.fileStoreName = "DefaultFileStore";
        SettingProperties.ACTIVE_MONITOR_PROPERTY = true;
        SettingProperties.CONSOLE_OUTPUT_PROPERTY = true;
    }

    public SternAppSetting(int port) {
        this.adapterName = "DefaultAdapter";
        this.serverName = "DefaultServer";
        this.serverDescription = "Default server";
        this.adapterPort = port;
        this.containerName = "DefaultContainer";
        this.containerDescription = "Default description";
        this.sessionName = "DefaultSession";
        this.sessionDescription = "Default description";
        this.fileStoreName = "DefaultFileStore";
        SettingProperties.ACTIVE_MONITOR_PROPERTY = true;
        SettingProperties.CONSOLE_OUTPUT_PROPERTY = true;
    }

    /**
     * Constructor de la clase
     * 
     * @param adapterName          nombre del adaptador
     * @param serverName           nombre del servidor
     * @param serverDescription    descripción del servidor
     * @param containerName        nombre del contenedor
     * @param containerDescription descripción del contenedor
     * @param sessionName          nombre de la sesión de comunicación
     * @param sessionDescription   descripción de la sesión de comunicación
     * @param fileStoreName        nombre del servicio de transferencia de ficheros
     *
     * <br>
     * <br>
     * <b>Nota:</b>
     * <br>
     * <br>
     * <tt>Al construir el objeto el sistema se autogestiona un número de puerto, por
     * el cuál escuchará las peticiones del cliente.</tt>
     * <br>
     *
     */
    public SternAppSetting(String adapterName, String serverName, String serverDescription, String containerName, String containerDescription, String sessionName, String sessionDescription, String fileStoreName) {
        this.adapterName = adapterName;
        this.serverName = serverName;
        this.serverDescription = serverDescription;
        this.containerName = containerName;
        this.containerDescription = containerDescription;
        this.sessionName = sessionName;
        this.sessionDescription = sessionDescription;
        this.fileStoreName = fileStoreName;
        this.adapterPort = Utilities.getAvailablePort();
    }

    /**
     * Constructor de la clase
     *
     * @param adapterName          nombre del adaptador
     * @param adapterPort          puerto de escucha del servidor
     * @param serverName           nombre del servidor
     * @param serverDescription    descripción del servidor
     * @param containerName        nombre del contenedor
     * @param containerDescription descripción del contenedor    
     *
     * <br>
     * <br>
     * <b>Nota:</b>
     * <br>
     * <tt>Al construir el objeto se chequea el número del puerto sea >= 10000 y que
     * este se encuentre disponible en el sistema. En caso caso de no cumplir
     * con las anteriores premisas, el sistema se autogestiona un número de puerto.</tt>
     * <br>
     * <br>
     * <tt>Los resto de los atributos no especificados en el constructor, toman
     *     sus valores por defectos.</tt>
     * <br>
     */
    public SternAppSetting(String adapterName, int adapterPort, String serverName, String serverDescription, String containerName, String containerDescription) {
        this.adapterName = adapterName;
        this.serverName = serverName;
        this.serverDescription = serverDescription;
        this.containerName = containerName;
        this.containerDescription = containerDescription;
        this.adapterPort = (adapterPort >= 10000 && Utilities.isAvailablePort(adapterPort)) ? adapterPort : Utilities.getAvailablePort();
        this.fileStoreName = "DefaultFileStore";
        this.sessionName = "DefaultSession";
        this.sessionDescription = "Default description";
    }

    /**
     * Constructor de la  clase
     *
     * @param adapterName          nombre del adaptador
     * @param adapterPort          puerto de escucha del servidor
     * @param serverName           nombre del servidor
     * @param serverDescription    descripción del servidor
     * @param containerName        nombre del contenedor
     * @param containerDescription descripción del contenedor     
     * @param sessionName          nombre de la sesión de comunicación
     * @param sessionDescription   descripción de la sesión de comunicación
     *
     * <br>
     * <br>
     * <b>Nota:</b>
     * <br>
     * <tt>Al construir el objeto se chequea el número del puerto sea >= 10000 y que
     * este se encuentre disponible en el sistema. En caso caso de no cumplir
     * con las anteriores premisas, el sistema se autogestiona un número de puerto.</tt>
     * <br>
     * <br>
     * <tt>Los resto de los atributos no especificados en el constructor, toman
     *     sus valores por defectos.</tt>
     * <br>
     */
    public SternAppSetting(String adapterName, int adapterPort, String serverName, String serverDescription, String containerName, String containerDescription, String sessionName, String sessionDescription) {

        this.adapterName = adapterName;
        this.serverName = serverName;
        this.serverDescription = serverDescription;
        this.adapterPort = (adapterPort >= 10000 && Utilities.isAvailablePort(adapterPort)) ? adapterPort : Utilities.getAvailablePort();
        this.containerName = containerName;
        this.containerDescription = containerDescription;
        this.sessionName = sessionName;
        this.sessionDescription = sessionDescription;
        this.fileStoreName = "DefaultFileStore";

    }

    /**
     * Constructor de la clase
     *
     * @param adapterName          nombre del adaptador
     * @param adapterPort          puerto de escucha del servidor
     * @param serverName           nombre del servidor
     * @param serverDescription    descripción del servidor
     * @param containerName        nombre del contenedor
     * @param containerDescription descripción del contenedor
    
     * @param fileStoreName        nombre del servicio de transferencia de ficheros
     *
     * <br>
     * <br>
     * <b>Nota:</b>
     * <br>
     * <tt>Al construir el objeto se chequea el número del puerto sea >= 10000 y que
     * este se encuentre disponible en el sistema. En caso caso de no cumplir
     * con las anteriores premisas, el sistema se autogestiona un número de puerto.</tt>
     * <br>
     * <br>
     * <tt>Los resto de los atributos no especificados en el constructor, toman
     *     sus valores por defectos.</tt>
     * <br>
     */
    public SternAppSetting(String adapterName, int adapterPort, String serverName, String serverDescription, String containerName, String containerDescription, String fileStoreName) {
        this.adapterName = adapterName;
        this.serverName = serverName;
        this.serverDescription = serverDescription;
        this.adapterPort = (adapterPort >= 10000 && Utilities.isAvailablePort(adapterPort)) ? adapterPort : Utilities.getAvailablePort();
        this.containerName = containerName;
        this.containerDescription = containerDescription;
        this.fileStoreName = fileStoreName;
        this.sessionName = "DefaultSession";
        this.sessionDescription = "Default Session";

    }

    /**
     * Constructor de la clase
     *
     * @param adapterName          nombre del adaptador
     * @param adapterPort          puerto de escucha del servidor
     * @param serverName           nombre del servidor
     * @param serverDescription    descripción del servidor
     * @param containerName        nombre del contenedor
     * @param containerDescription descripción del contenedor     
     * @param sessionName          nombre de la sesión de comunicación
     * @param sessionDescription   descripción de la sesión de comunicación
     * @param fileStoreName        nombre del servicio de transferencia de ficheros
     *
     * <br>
     * <br>
     * <b>Nota:</b>
     * <br>
     * <br>
     * <tt>Al construir el objeto se chequea el número del puerto sea >= 10000 y que
     * este se encuentre disponible en el sistema. En caso caso de no cumplir
     * con las anteriores premisas, el sistema se autogestiona un número de puerto.</tt>
     * <br>
     */
    public SternAppSetting(String adapterName, int adapterPort,  String serverName, String serverDescription,String containerName, String containerDescription, String sessionName, String sessionDescription, String fileStoreName) {
        this.adapterName = adapterName;
        this.serverName = serverName;
        this.serverDescription = serverDescription;
        this.adapterPort = (adapterPort >= 10000 && Utilities.isAvailablePort(adapterPort)) ? adapterPort : Utilities.getAvailablePort();
        this.containerName = containerName;
        this.containerDescription = containerDescription;
        this.sessionName = sessionName;
        this.sessionDescription = sessionDescription;
        this.fileStoreName = fileStoreName;
    }

    /**
     *
     * @return
     */
    public String getAdapterName() {
        return adapterName;
    }

    /**
     *
     * @param adapterName
     */
    public void setAdapterName(String adapterName) {
        this.adapterName = adapterName;
    }

    /**
     *
     * @return
     */
    public String getContainerName() {
        return containerName;
    }

    /**
     *
     * @param containerName
     */
    public void setContainerName(String containerName) {
        this.containerName = containerName;
    }

    /**
     *
     * @return
     */
    public String getServerName() {
        return serverName;
    }

    /**
     *
     * @param serverName
     */
    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    /**
     *
     * @return
     */
    public int getAdapterPort() {
        return adapterPort;
    }

    /**
     * Modificar el puerto de escucha del servidor
     *
     * @param adapterPort nuevo puerto
     *
     * <br>
     * <tt><b>Nota:</b></tt><br>
     * El número del puerto debe ser >= 10000 y encuentrarse disponible en el sistema.
     * En caso caso de no cumplir con las anteriores premisas, el sistema se
     * autogestiona un puerto disponible.
     * <br>
     */
    public void setAdapterPort(int adapterPort) {
        this.adapterPort = (adapterPort >= 10000 && Utilities.isAvailablePort(adapterPort)) ? adapterPort : Utilities.getAvailablePort();
    }

    /**
     *
     * @return
     */
    public String getContainerDescription() {
        return containerDescription;
    }

    /**
     *
     * @param containerDescription
     */
    public void setContainerDescription(String containerDescription) {
        this.containerDescription = containerDescription;
    }

    /**
     *
     * @return
     */
    public String getServerDescription() {
        return serverDescription;
    }

    /**
     *
     * @param serverDescription
     */
    public void setServerDescription(String serverDescription) {
        this.serverDescription = serverDescription;
    }

    /**
     *
     * @return
     */
    public String getFileStoreName() {
        return fileStoreName;
    }

    /**
     *
     * @param fileStoreName
     */
    public void setFileStoreName(String fileStoreName) {
        this.fileStoreName = fileStoreName;
    }

    /**
     *
     * @return
     */
    public String getSessionDescription() {
        return sessionDescription;
    }

    /**
     *
     * @param sessionDescription
     */
    public void setSessionDescription(String sessionDescription) {
        this.sessionDescription = sessionDescription;
    }

    /**
     *
     * @return
     */
    public String getSessionName() {
        return sessionName;
    }

    /**
     *
     * @param sessionName
     */
    public void setSessionName(String sessionName) {
        this.sessionName = sessionName;
    }
}
