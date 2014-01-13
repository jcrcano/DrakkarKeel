/*
 * DrakkarKeel - An Enterprise Collaborative Search Platform
 *
 * The contents of this file are subject under the terms described in the
 * DRAKKARKEEL_LICENSE file included in this distribution; you may not use this
 * file except in compliance with the License. 
 *
 * 2013-2014 DrakkarKeel Platform.
 */
package drakkar.prow.facade.desktop.event;

import drakkar.oar.facade.event.FacadeDesktopListener;

public interface ProwListener extends FacadeDesktopListener {

     /**
     * Notifica la respuesta de petición de conexión de un usuario a la sesión de comunicación
     *
     * @param evt instancia de SeekerEvent
     *
     * <br><br>
     * El objeto SeekerEvent, contiene la respuesta de la de petición de conexión al servisor.
     * <br><pre>
     *  public void notifyRequestConnection(SeekerEvent evt) {
     *       Response response = evt.getResponse();
     *       int reply = (Integer)response.get(REPLY);
     *         switch (reply) {
     *            case CONNECTION_FAILED:
     *              .......
     *             case CONNECTION_SUCCESSFUL:
     *              .....
     *         }
     *    }
     * </pre>
     *
     * La siguiente tabla muestra la relación de campos(clave-valor) del objeto
     * response de la instancia SeekerEvent. Las constantes clave(key) pertenecen
     * a la clase <b>drakkar.oar.​util.​KeyTransaction </b> y los valores(values) a
     * la clase  <b>​drakkar.oar.​util.​KeySession </b>.
     *
     *
     * <br><br><table border = 1 summary="Shows property keys and associated values">
     * <tr><th>Key</th>
     *     <th>Associated Value</th><th>Description</th></tr>
     * <tr>
     *    <td rowspan= 2><code>REPLY</code></td>
     *     <td><code>CONNECTION_FAILED<code></td>
     *    <td>Falló la entrada al sistema</td>
     * </tr>
     * <tr>
     *     <td><code>CONNECTION_SUCCESSFUL<code></td>
     *    <td>Entrada al sistema satisfactorimente</td>
     * </tr>
     *
     * </table>
     *
     * <br><b> Case:</b> CONNECTION_FAILED
     * <table border = 1 >
     * <tr><th>Key</th>
     *     <th>Associated Value</th><th>Description</th></tr>
     * <tr>
     *    <td rowspan= 2><code>CAUSE</code></td>
     *     <td><code>SEEKER_ALREADY_REGISTERED<code></td>
     *    <td>El seeker ya se encuentra conectado</td>
     * </tr>
     * <tr>
     *     <td><code>USER_OR_PASSWORD_INCORRECT<code></td>
     *    <td>Error en el usuario o la contraseña</td>
     * </tr>
     *
     * </table>
     *
     * <br><b>Case:</b> CONNECTION_SUCCESSFUL
     *
     * <table border = 1 >
     * <tr><th>Key</th>
     *     <th>Associated Value</th><th>Description</th></tr>
     * <tr>
     *    <td ><code>SESSION_NAME</code></td>
     *     <td><code>String<code></td>
     *    <td>Nombre de la sesión de comunicación.</td>
     * </tr>
     * <tr>
     *    <td ><code>SEEKERS_SESSION</code></td>
     *     <td><code>ArrayList&lt;Seeker&gt;<code></td>
     *    <td>Lista de seekers conectados al sistema.</td>
     * </tr>
     * <tr>
     *    <td ><code>SEARCHERS</code></td>
     *     <td><code>String[]<code></td>
     *    <td>Lista de nombres de buscadores disponibles.</td>
     * </tr>
     * <tr>
     *    <td ><code>PERSISTENT_SESSIONS</code></td>
     *     <td><code>ArrayList&lt;Session&gt; <code></td>
     *    <td>Lista de sesiones, de las cuales es miembro el seeker.</td>
     * </tr>
     * <tr>
     *    <td ><code>OPEN_SESSIONS</code></td>
     *     <td><code>ArrayList&lt;Session&gt; <code></td>
     *    <td>Sessiones de búsquedas abiertas en sistema.</td>
     * </tr>
     *
     * <tr>
     *    <td ><code>SEARCH_PRINCIPLES</code></td>
     *     <td><code>ArrayList&lt;String&gt; <code></td>
     *    <td>Principios de búsquedas disponibles para el seeker.</td>
     * </tr>
     *
     * </table>
     *
     */
    public void notifyRequestConnection(SeekerEvent evt);

    /**
     * 
     * @param evt
     */
    public void notifyCloseConnection(SeekerEvent evt);


    /**
     * Notifica la acción del usuario en una sesión de comunicación (entrada, salida
     * y actualización del su profile).
     *
     * @param evt instancia de SeekerEvent
     *
     * <br><br>
     * El objeto SeekerEvent, almacena la información referente a la acción a notificar.
     * <br><pre>
     *  public void notifySeekerEvent(SeekerEvent evt){
     *      Response rsp = evt.getResponse();
     *      int event = (Integer) rsp.get(DISTRIBUTED_EVENT);
     *      Seeker item = (Seeker) rsp.get(SEEKER);
     *      String sessionName = rsp.get(SESSION_NAME).toString();
     *      ..... // Implementar identificación del tipo de evento
     *      ...
     *  }    
     * </pre>
     *
     * La siguiente tabla muestra la relación de campos(clave-valor) de los posibles
     * tipos de eventos que pueden originar esta notificación. Las constantes clave(key)
     * pertenecen a la clase <b>​drakkar.oar.​util.​KeyTransaction </b> y los valores(values) a
     * la clase  <b> ​drakkar.oar.​util.​KeySession </b>.
     *
     * 
     * <br><br><table border = 1 summary="Shows property keys and associated values">
     * <tr><th>Key</th>
     *     <th>Associated Value</th><th>Description</th></tr>
     * <tr>
     *    <td rowspan= 3><code>DISTRIBUTED_EVENT</code></td>
     *     <td><code>SEEKER_LOGIN<code></td>
     *    <td>Notificar la entrada del seeker del sistema.</td>
     * </tr>
     * <tr>
     *     <td><code>SEEKER_LOGOUT<code></td>
     *    <td>Notificar la salida del seeker del sistema.</td>
     * </tr>
     * <tr>
     *     <td><code>UPDATE_SEEKER_PROFILE<code></td>
     *    <td>Notificar la actualización del profile del seeker (Cambio estado, avatar).</td>
     * </tr>
     * <tr>
     *    <td ><code>SEEKER</code></td>
     *     <td><code>Objeto Seeker<code></td>
     *    <td>Instancia del la case Seeker que representa el usuario que originó la notificación.</td>
     * </tr>
     * <tr>
     *    <td ><code>SESSION_NAME</code></td>
     *     <td><code>String<code></td>
     *    <td>Nombre de la sesión de comunicación del seeker.</td>
     * </tr>
     * </table>
     *
     *
     */
    public void notifySeekerEvent(SeekerEvent evt);

   

    /**
     * Notifica el estado actual del servidor.
     * 
     * @param evt  instancia de ServerEvent
     *
     * <br><br>
     * El objeto SeekerEvent(evt), en dependencia de la acción a notificar tiene la
     * tiene los siguientes parámetros:
     * <br><pre>
     *  public void notifyServerState(ServerEvent evt){
     *      Response rsp = evt.getResponse();
     *      int reply = (Integer) rsp.get(SERVER_STATE);
     *         switch (reply) {
     *            case SERVER_STOPPED:
     *              .......
     *             case SERVER_RESET:
     *              .....
     *         }
     *  }
     * </pre>
     *
     * La siguiente tabla muestra la relación de campos(clave-valor) de los posibles
     * tipos de acciones que pueden originar esta notificación. Las constantes clave(key)
     * pertenecen a la clase <b>​drakkar.oar.​util.​KeyTransaction </b> y los valores(values) a
     * la clase  <b>​drakkar.oar.​util.​KeySession</b>.
     *
     *
     * <br><br><table border = 1 summary="Shows property keys and associated values">
     * <tr><th>Key</th>
     *     <th>Associated Value</th><th>Description</th></tr>
     * <tr>
     *    <td rowspan= 2><code>SERVER_STATE</code></td>
     *     <td><code>SERVER_STOPPED<code></td>
     *    <td>Notifica que el servicio del servidor se ha detenido.</td>
     * </tr>
     * <tr>
     *     <td><code>SERVER_RESET<code></td>
     *    <td>Notifica el reinicio del servicio del servidor.</td>
     * </tr>
     *
     * </table>
     *
     *
     */
    public void notifyServerState(ServerEvent evt);

    /**
     * Notifica el completamiento de una operación, en caso contrario la
     * o las causas que impidieron la misma.
     *
     * @param evt instancia de TransactionEvent
     *
     * <br><br>
     * El objeto SeekerEvent, en dependencia del tipo de mensaje a notificar tiene la
     * tiene los siguientes parámetros:
     * <br><pre>
     *  public void notifyCommitTransaction(TransactionEvent evt) {
     *      Response response = evt.getResponse();
     *      int type = (Integer)response.get(MESSAGE_TYPE);
     *      String msg = response.get(MESSAGE);
     *         switch (type) {
     *            case INFORMATION_MESSAGE:
     *              System.out.println(msg);
     *              .......
     *             case ERROR_MESSAGE:
     *               System.err.println(msg);
     *              .....
     *         }
     *    }
     * </pre>
     * 
     * La siguiente tabla muestra la relación de campos(clave-valor) del objeto
     * response de la instancia TransactionEvent. Las constantes clave(key) pertenecen
     * a la clase <b>​drakkar.oar.​util.​KeyTransaction </b> y los valores(values) a
     * la clases  <b>drakkar.oar.​util.​KeySession,drakkar.oar.​util.​KeyMessage</b>.
     *
     *
     * <br><br><table border = 1 summary="Shows property keys and associated values">
     * <tr><th>Key</th>
     *     <th>Associated Value</th><th>Description</th></tr>
     * <tr>
     *    <td rowspan= 2><code>MESSAGE_TYPE</code></td>
     *     <td><code>INFORMATION_MESSAGE<code></td>
     *    <td>Mensaje de Información</td>
     * </tr>
     * <tr>
     *     <td><code>ERROR_MESSAGE<code></td>
     *    <td>Mensaje de Error.</td>
     * </tr>
     *
     * <tr>
     *     <td><code>MESSAGE<code></td>
     *      <td><code>String<code></td>
     *    <td>Contenido del Mensaje.</td>
     * </tr>
     *
     * </table>
     * 
     *     
     */
    public void notifyCommitTransaction(TransactionEvent evt);
}
