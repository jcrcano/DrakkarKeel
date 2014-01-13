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

/**
 * Esta interfaz se encargará de notificarán los eventos del entorno colaborativo
 */
public interface CollaborativeEnvironmentListener extends FacadeDesktopListener {

    /**
     * Notifica a la aplicación del cliente la apertura o cierre de una sesión
     * colaborativa de búsqueda.
     *
     * @param evt instancia de SessionEvent
     *
     * <br><br>
     * El objeto CollaborativeEnvironmentEvent contiene la información relacionada
     * con el evento efectuado sobre las sesiones colaborativas de búsqueda.
     *
     * <br><pre>
     *  public void notifyAvailableCollabSession(CollaborativeEnvironmentEvent evt){
     *      Response rsp = evt.getResponse();
     *      String sessionName = rsp.get(SESSION_NAME).toString();
     *      int event = (Integer) rsp.get(DISTRIBUTED_EVENT);
     *      switch (reply) {
     *            case BEGIN_COLLAB_SESSION:
     *              String desc = rsp.get(SESSION_DESCRIPTION).toString();
     *              .......
     *             case END_COLLAB_SESSION:
     *              .....
     *         }
     *      ...
     *      ..
     *  }
     * </pre>
     *
     * La siguiente tabla muestra la relación de campos(clave-valor) de esta notificación.
     * Las constantes clave(key) pertenecen a la clase <b>​drakkar.oar.​util.​KeyTransaction </b> y
     * los valores a la clase <b>drakkar.oar.​util.​KeySession</b>.
     *
     *
     * <br><br><table border = 1 summary="Shows property keys and associated values">
     * <tr><th>Key</th>
     *     <th>Associated Value</th><th>Description</th></tr>
     *
     * <tr>
     *    <td><code>SESSION_NAME</code></td>
     *    <td><code>String <code></td>
     *    <td>Nombre de la sesión colaborativa de búsqueda.</td>
     * </tr>
     * <tr>
     *    <td rowspan = 2><code>DISTRIBUTED_EVENT</code></td>
     *     <td><code>BEGIN_COLLAB_SESSION <code></td>
     *    <td>Inicio de una sesión colaborativa de búsqueda.</td>
     * </tr>
     *
     * <tr>
     *    <td><code>END_COLLAB_SESSION <code></td>
     *    <td>Fin de una sesión colaborativa de búsqueda.</td>
     * </tr>
     *
     * </table>
     *
     * <br><b>Case:</b> BEGIN_COLLAB_SESSION
     * <br><br><table border = 1 summary="Shows property keys and associated values">
     * <tr><th>Key</th>
     *     <th>Associated Value</th><th>Description</th></tr>
     *
     * <tr>
     *    <td><code>SESSION_DESCRIPTION</code></td>
     *    <td><code>String <code></td>
     *    <td>Descripción de la sesión colaborativa de búsqueda.</td>
     * </tr>
     * <tr>
     *    <td><code>CHAIRMAN_NAME</code></td>
     *    <td><code>String <code></td>
     *    <td>Nombre del chairman de la sesión colaborativa de búsqueda.</td>
     * </tr>
     *
     * </table>
     */
    public void notifyAvailableCollabSession(CollaborativeEnvironmentEvent evt);

    /**
     * Notifica respuesta de la solicitud de creación de una sesión colaborativa de búsqueda.
     *
     * @param evt instancia de CollaborativeEnvironmentEvent
     *
     * <br><br>
     * El objeto CollaborativeEnvironmentEvent contiene la información relacionada
     * con la  respuesta de la solicitud de creación de una sesión colaborativa de búsqueda.
     *
     * <br><pre>
     *  public void notifyCollabSessionCreation(CollaborativeEnvironmentEvent evt){
     *      Response rsp = evt.getResponse();
     *      String sessionName = rsp.get(SESSION_NAME).toString();
     *      ArrayList<Seekers> seekers = (ArrayList<Seekers>) rsp.get(SEEKERS_SESSION);
     *      ArrayList<String> principles = (ArrayList<String>) rsp.get(SEARCH_PRINCIPLES);
     *      .....
     *  }
     * </pre>
     *
     * La siguiente tabla muestra la relación de campos(clave-valor) de esta notificación.
     * Las constantes clave(key) pertenecen a la clase <b>​drakkar.oar.​util.​KeyTransaction </b> y
     * los valores a la clase <b>drakkar.oar.​util.​KeySession</b>.
     *
     *
     * <br><br><table border = 1 summary="Shows property keys and associated values">
     * <tr><th>Key</th>
     *     <th>Associated Value</th><th>Description</th></tr>
     *
     * <tr>
     *    <td ><code>SESSION_NAME</code></td>
     *    <td><code>String <code></td>
     *    <td>Nombre de sesión de búsqueda colaborativa.</td>
     * </tr>
     *
     * <tr>
     *    <td ><code>SEEKERS_SESSION</code></td>
     *    <td><code>ArrayList&lt;Seeker&gt; <code></td>
     *    <td>Lista de seekers de la sesión colaborativa de búsqueda.</td>
     * </tr>
     *  <tr>
     *    <td ><code>SEARCH_PRINCIPLES</code></td>
     *    <td><code>ArrayList&lt;String&gt; <code></td>
     *    <td>Principios de búsquedas disponibles para el seeker.</td>
     * </tr>
     *
     * </table>
     *
     */
    public void notifyCollabSessionCreation(CollaborativeEnvironmentEvent evt);

    /**
     * Notifica los cambios de los chairmans de la sesiones colaborativas de búsqueda.
     *
     * @param evt instancia de CollaborativeEnvironmentEvent
     *
     * <br><br>
     * El objeto CollaborativeEnvironmentEvent contiene la información relacionada
     * con el cambio del chairman de una sesión colaborativad de búsqueda.
     *
     * <br><pre>
     *  public void notifyChairmanSetting(CollaborativeEnvironmentEvent evt){
     *      Response rsp = evt.getResponse();
     *      String sessionName = rsp.get(SESSION_NAME).toString();
     *      String chairmanName = rsp.get(CHAIRMAN_NAME).toString();
     *
     *      int event = (Integer) rsp.get(DISTRIBUTED_EVENT);
     *      switch (event) {
     *            case NEW_CHAIRMAN_COLLAB_SESSION:
     *              System.out.println("New chairman "+chairmanName+" of session "+sessionName+"!!!")
     *              break;
     *             case CHANGE_CHAIRMAN_COLLAB_SESSION:
     *              ArrayList<String> newPrinciples = (ArrayList<String>) rsp.get(SEARCH_PRINCIPLES);
     *              ..... // actualizar los principios de la GUI del cliente
     *              boolean isChairman = (Boolean) rsp.get(IS_CHAIRMAN);
     *              ..... // Mostrar un mensaje de información en dependencia del valor de isChairman
     *              ..
     *       }
     *     
     *  }
     * </pre>
     *
     * La siguiente tabla muestra la relación de campos(clave-valor) de esta notificación.
     * Las constantes clave(key) pertenecen a la clase <b>​drakkar.oar.​util.​KeyTransaction </b> y
     * los valores a la clase <b>drakkar.oar.​util.​KeySession</b>.
     *
     *
     * <br><br><table border = 1 summary="Shows property keys and associated values">
     * <tr><th>Key</th>
     *     <th>Associated Value</th><th>Description</th></tr>
     *
     * <tr>
     *    <td rowspan=2><code>DISTRIBUTED_EVENT</code></td>
     *    <td><code>NEW_CHAIRMAN_COLLAB_SESSION <code></td>
     *    <td>Nofificar al los miembro de la sesión de búsqueda colaborativa el
     *    nuevo chairman de la misma.</td>
     * </tr>
     *
     * <tr>
     *    <td><code>CHANGE_CHAIRMAN_COLLAB_SESSION <code></td>
     *    <td>Nofificar al nuevo y al anterior chairman de sesión de búsqueda colaborativa el cambio.</td>
     * </tr>
     *
     * <tr>
     *    <td ><code>SESSION_NAME</code></td>
     *    <td><code>String <code></td>
     *    <td>Nombre de sesión de búsqueda colaborativa.</td>
     * </tr>
     *
     * <tr>
     *    <td ><code>CHAIRMAN_NAME</code></td>
     *    <td><code>String <code></td>
     *    <td>Nombre del chairman de la sesión de búsqueda colaborativa.</td>
     * </tr>
     *
     * </table>
     *
     * <br><b>Case:</b> CHANGE_CHAIRMAN_COLLAB_SESSION
     * <br><br><table border = 1 summary="Shows property keys and associated values">
     * <tr><th>Key</th>
     *     <th>Associated Value</th><th>Description</th></tr>
     *
     * <tr>
     *    <td ><code>IS_CHAIRMAN</code></td>
     *    <td><code>boolean <code></td>
     *    <td>True si se notifica al chairman de la sesión de búsqueda colaborativa, false
     *    en caso contrario.</td>
     * </tr>
     *
     * <tr>
     *    <td ><code>SEARCH_PRINCIPLES</code></td>
     *    <td><code>ArrayList&lt;String&gt; <code></td>
     *    <td>Principios de búsquedas disponibles para el seeker.</td>
     * </tr>
     *
     * </table>
     *     
     */
    public void notifyChairmanSetting(CollaborativeEnvironmentEvent evt);

    /**
     * Notifica la sesión colaborativa de búsqueda eliminada.
     *
      * <br><br>
     * El objeto CollaborativeEnvironmentEvent contiene la información relacionada
     * con la sesión colaborativa de búsqueda eliminada.
     *
     * <br><pre>
     *  public void notifyCollabSessionDeleted(CollaborativeEnvironmentEvent evt){
     *      Response rsp = evt.getResponse();
     *      String sessionName = rsp.get(SESSION_NAME).toString();
     *      ........
     *      .....
     *  }
     * </pre>
     *
     * La siguiente tabla muestra la relación de campos(clave-valor) de esta notificación.
     * Las constantes clave(key) pertenecen a la clase <b>​drakkar.oar.​util.​KeyTransaction </b>.
     *
     *
     * <br><br><table border = 1 summary="Shows property keys and associated values">
     * <tr><th>Key</th>
     *     <th>Associated Value</th><th>Description</th></tr>
     *
     * <tr>
     *    <td ><code>SESSION_NAME</code></td>
     *    <td><code>String <code></td>
     *    <td>Nombre de sesión de búsqueda colaborativa.</td>
     * </tr>
     *
     *
     * </table>
     *
     */
    public void notifyCollabSessionDeleted(CollaborativeEnvironmentEvent evt);

    /**
     * Notifica la sesión colaborativa de búsqueda finalizada.
     *
      * <br><br>
     * El objeto CollaborativeEnvironmentEvent contiene la información relacionada
     * con la sesión colaborativa de búsqueda finalizada.
     *
     * <br><pre>
     *  public void notifyCollabSessionEnding(CollaborativeEnvironmentEvent evt){
     *      Response rsp = evt.getResponse();
     *      String sessionName = rsp.get(SESSION_NAME).toString();
     *      ........
     *      .....
     *  }
     * </pre>
     *
     * La siguiente tabla muestra la relación de campos(clave-valor) de esta notificación.
     * Las constantes clave(key) pertenecen a la clase <b>​drakkar.oar.​util.​KeyTransaction </b>.
     *
     *
     * <br><br><table border = 1 summary="Shows property keys and associated values">
     * <tr><th>Key</th>
     *     <th>Associated Value</th><th>Description</th></tr>
     *
     * <tr>
     *    <td ><code>SESSION_NAME</code></td>
     *    <td><code>String <code></td>
     *    <td>Nombre de sesión de búsqueda colaborativa.</td>
     * </tr>
     *
     *
     * </table>
     *
     */
    public void notifyCollabSessionEnding(CollaborativeEnvironmentEvent evt);

    /**
     * Notifica las acciones de entrada y salida de los seekers en las sesiones
     * colaborativas de búsqueda.
     *
     * @param evt instancia de CollaborativeEnvironmentEvent
     *
     * <br><br>
     * El objeto CollaborativeEnvironmentEvent contiene la información relacionada
     * con la  respuesta de la solicitud de entrada o la entrada a una sesión colaborativa de búsqueda.
     *
     * <br><pre>
     *  public void notifyCollabSessionAuthentication(CollaborativeEnvironmentEvent evt){
     *      Response rsp = evt.getResponse();
     *      String sessionName = rsp.get(SESSION_NAME).toString();
     *      
     *      int event = (Integer) rsp.get(DISTRIBUTED_EVENT);
     *      switch (event) {
     *            case SEEKER_LOGIN_COLLAB_SESSION:
     *              Seeker seeker = (Seeker)rsp.get(SEEKER);
     *              ........
     *              break;
     *             case SEEKER_LOGOUT_COLLAB_SESSION:
     *              .....
     *              break;
     *              case SELF_LOGIN_COLLAB_SESSION:
     *              .....
     *              break;
     *              
     *       }
     *
     *  }
     * </pre>
     *
     * La siguiente tabla muestra la relación de campos(clave-valor) de esta notificación.
     * Las constantes clave(key) pertenecen a la clase <b>​drakkar.oar.​util.​KeyTransaction </b> y
     * los valores a la clase <b>drakkar.oar.​util.​KeySession</b>.
     *
     *
     * <br><br><table border = 1 summary="Shows property keys and associated values">
     * <tr><th>Key</th>
     *     <th>Associated Value</th><th>Description</th></tr>
     *
     * <tr>
     *    <td rowspan=4><code>DISTRIBUTED_EVENT</code></td>
     *    <td><code>SEEKER_LOGIN_COLLAB_SESSION <code></td>
     *    <td>Nofifica la entrada de un seeker de una sesión de búsqueda colaborativa.</td>
     * </tr>
     *
     * <tr>
     *    <td><code>SEEKER_LOGOUT_COLLAB_SESSION <code></td>
     *    <td>Nofifica la salida de un seeker de una sesión de búsqueda colaborativa.</td>
     * </tr>
     *
     * <tr>
     *    <td><code>SELF_LOGIN_COLLAB_SESSION <code></td>
     *    <td>Nofifica al seeker su entrada a la sesión de búsqueda colaborativa.</td>
     * </tr>
     * <tr>
     *    <td><code>DECLINE_SEEKER_COLLAB_SESSION <code></td>
     *    <td>Nofifica al seeker su salida permanente de una sesión búsqueda colaborativa.</td>
     * </tr>
     * <tr>
     *    <td ><code>SESSION_NAME</code></td>
     *    <td><code>String <code></td>
     *    <td>Nombre de sesión de búsqueda colaborativa.</td>
     * </tr>
     *
     *
     * </table>
     *
     * <br><b>Case:</b> SEEKER_LOGIN_COLLAB_SESSION y SEEKER_LOGOUT_COLLAB_SESSION
     * <br><br><table border = 1 summary="Shows property keys and associated values">
     * <tr><th>Key</th>
     *     <th>Associated Value</th><th>Description</th></tr>
     * <tr>
     *    <td ><code>SEEKER</code></td>
     *    <td><code>Seeker <code></td>
     *    <td>Instancia del objeto seeker que representa al usuario que originó el evento.</td>
     * </tr>
     * </table>
     *
     * <br><b>Case:</b> SEEKER_LOGOUT_COLLAB_SESSION
     * <br><br><table border = 1 summary="Shows property keys and associated values">
     * <tr><th>Key</th>
     *     <th>Associated Value</th><th>Description</th></tr>
     *
     * <tr>
     *    <td rowspan= 2><code>CAUSE</code></td>
     *    <td><code>SEEKER_LOGOUT<code></td>
     *    <td>Salida del seeker de la sesión colaborativa de búsqueda.</td>
     * </tr>
     *
     *  <tr>
     *    <td><code>END_SESSION<code></td>
     *    <td>Fin de la sesión colaborativa de búsqueda.</td>
     * </tr>
     * </table>
     *
     * <br><b>Case:</b> SELF_LOGIN_COLLAB_SESSION
     * <br><br><table border = 1 summary="Shows property keys and associated values">
     * <tr><th>Key</th>
     *     <th>Associated Value</th><th>Description</th></tr>
     *
     * <tr>
     *    <td ><code>SEEKERS_SESSION</code></td>
     *    <td><code>ArrayList&lt;Seeker&gt; <code></td>
     *    <td>Lista de seekers de la sesión colaborativa de búsqueda.</td>
     * </tr>
     *  <tr>
     *    <td ><code>SEARCH_PRINCIPLES</code></td>
     *    <td><code>ArrayList&lt;String&gt; <code></td>
     *    <td>Principios de búsquedas disponibles para el seeker.</td>
     * </tr>
     *
     * </table>
     *
     */
    public void notifyCollabSessionAuthentication(CollaborativeEnvironmentEvent evt);

    /**
     * Notifica la solicitud o respuesta de entrada a una sesión colaborativa de búsqueda.
     *
     * @param evt instancia de CollaborativeEnvironmentEvent
     *
     * <br><br>
     * El objeto CollaborativeEnvironmentEvent contiene la información relacionada
     * con la solicitud o respuesta de entrada a una sesión colaborativa de búsqueda.
     *
     * <br><pre>
     *  public void notifyCollabSessionAcceptance(CollaborativeEnvironmentEvent evt){
     *      Response rsp = evt.getResponse();
     *      String sessionName = rsp.get(SESSION_NAME).toString();
     *
     *      int event = (Integer) rsp.get(DISTRIBUTED_EVENT);
     *      switch (event) {
     *            case CONFIRM_COLLAB_SESSION:
     *              ArrayList<Seekers> seekers = (ArrayList<Seekers>) rsp.get(SEEKERS_SESSION);
     *              break;
     *             case DECLINE_COLLAB_SESSION:
     *              .....
     *             case REQUEST_CONFIRM_COLLAB_SESSION:
     *              .....
     *              ..
     *       }
     *
     *  }
     * </pre>
     *
     * La siguiente tabla muestra la relación de campos(clave-valor) de esta notificación.
     * Las constantes clave(key) pertenecen a la clase <b>​drakkar.oar.​util.​KeyTransaction </b> y
     * los valores a la clase <b>drakkar.oar.​util.​KeySession</b>.
     *
     *
     * <br><br><table border = 1 summary="Shows property keys and associated values">
     * <tr><th>Key</th>
     *     <th>Associated Value</th><th>Description</th></tr>
     *
     * <tr>
     *    <td rowspan=3><code>DISTRIBUTED_EVENT</code></td>
     *    <td><code>CONFIRM_COLLAB_SESSION <code></td>
     *    <td>Nofifica la aceptación en la sesión de búsqueda colaborativa.</td>
     * </tr>
     *
     * <tr>
     *    <td><code>DECLINE_COLLAB_SESSION <code></td>
     *    <td>Nofifica la no aceptación en la sesión de búsqueda colaborativa.</td>
     * </tr>
     *
     * <tr>
     *    <td><code>REQUEST_CONFIRM_COLLAB_SESSION <code></td>
     *    <td>Nofifica la solicitud de entrada al chairman de la sesión de búsqueda colaborativa.</td>
     * </tr>
     *
     * <tr>
     *    <td ><code>SESSION_NAME</code></td>
     *    <td><code>String <code></td>
     *    <td>Nombre de sesión de búsqueda colaborativa.</td>
     * </tr>
     *
     *
     * </table>
     *
     * <br><b>Case:</b> CONFIRM_COLLAB_SESSION
     * <br><br><table border = 1 summary="Shows property keys and associated values">
     * <tr><th>Key</th>
     *     <th>Associated Value</th><th>Description</th></tr>
     *
     * <tr>
     *    <td ><code>SEEKERS_SESSION</code></td>
     *    <td><code>ArrayList&lt;Seeker&gt; <code></td>
     *    <td>Lista de seekers de la sesión colaborativa de búsqueda.</td>
     * </tr>
     * </table>
     *
     * <br><b>Case:</b> REQUEST_CONFIRM_COLLAB_SESSION
     * <br><br><table border = 1 summary="Shows property keys and associated values">
     * <tr><th>Key</th>
     *     <th>Associated Value</th><th>Description</th></tr>
     *
     * <tr>
     *    <td ><code>SEEKER</code></td>
     *    <td><code>Seeker<code></td>
     *    <td>Instancia del objeto Seeker que representa al remitente de la solicitud.</td>
     * </tr>
     * </table>
     *
     */
    public void notifyCollabSessionAcceptance(CollaborativeEnvironmentEvent evt);

    /**
     * Notifica los historiales del ámbito seleccionado.
     *
     * @param evt instancia de CollaborativeEnvironmentEvent
     *
     * <br><br>
     * El objeto CollaborativeEnvironmentEvent contiene la información relacionada
     * con el historial solicitado.
     *
     * <br><pre>
     *  public void notifyActionTrack(CollaborativeEnvironmentEvent evt){
     *      Response rsp = evt.getResponse();
     *      String sessionName = rsp.get(SESSION_NAME).toString();
     *      int track = (Integer) rsp.get(ACTION);
     *      switch (track) {
     *            case COLLAB_SESSION_TRACK:
     *              SeekerQuery q = (SeekerQuery )rsp.get(SESSION_DATA);
     *              .......
     *             case COLLAB_SEARCH_TRACK:
     *              .....
     *             case COLLAB_RECOMMENDATION_TRACK:
     *              .....
     *         }
     *      ...
     *      ..
     *  }
     * </pre>
     *
     * La siguiente tabla muestra la relación de campos(clave-valor) de esta notificación.
     * Las constantes clave(key) pertenecen a la clase <b>​drakkar.oar.​util.​KeyTransaction </b> y
     * los valores a la clase <b>drakkar.oar.​util.​KeySession</b>.
     *
     *
     * <br><br><table border = 1 summary="Shows property keys and associated values">
     * <tr><th>Key</th>
     *     <th>Associated Value</th><th>Description</th></tr>
     *
     * <tr>
     *    <td rowspan= 3><code>ACTION</code></td>
     *    <td><code>COLLAB_SESSION_TRACK <code></td>
     *    <td>Historiales de consultas de sesiones de búsqueda colaborativa.</td>
     * </tr>
     * <tr>
     *     <td><code>COLLAB_SEARCH_TRACK <code></td>
     *    <td>Historiales de búsquedas colaborativas.</td>
     * </tr>
     *
     * <tr>
     *    <td><code>COLLAB_RECOMMENDATION_TRACK <code></td>
     *    <td>Historiales de recomendaciones.</td>
     * </tr>
     *
     * </table>
     *
     * <br><b>Case:</b> COLLAB_SESSION_TRACK
     * <br><br><table border = 1 summary="Shows property keys and associated values">
     * <tr><th>Key</th>
     *     <th>Associated Value</th><th>Description</th></tr>
     *
     * <tr>
     *    <td><code>SESSION_DATA</code></td>
     *    <td><code>SeekerQuery <code></td>
     *    <td>Listado de consultas de búsqueda por usuarios de la sesión.</td>
     * </tr>
     *
     * </table>
     *
     * <br><b>Case:</b> COLLAB_SEARCH_TRACK
     * <br><br><table border = 1 summary="Shows property keys and associated values">
     * <tr><th>Key</th>
     *     <th>Associated Value</th><th>Description</th></tr>
     *
     * <tr>
     *    <td><code>SEARCH_DATA</code></td>
     *    <td><code>ArrayList&lt;SearchTracker&gt;<code></td>
     *    <td>Listado de resultados de búsqueda.</td>
     * </tr>
     *
     * </table>
     *
     * <br><b>Case:</b> COLLAB_SEARCH_TRACK
     * <br><br><table border = 1 summary="Shows property keys and associated values">
     * <tr><th>Key</th>
     *     <th>Associated Value</th><th>Description</th></tr>
     *
     * <tr>
     *    <td><code>RECOMMEND_DATA</code></td>
     *    <td><code>ArrayList&lt;RecommendTracker&gt;<code></td>
     *    <td>Listado de recomendaciones.</td>
     * </tr>
     *
     * </table>
     */
    public void notifyActionTrack(CollaborativeEnvironmentEvent evt);
}
