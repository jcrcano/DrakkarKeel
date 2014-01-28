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
 * The <code>SynchronousAwarenessListener</code> iterface se encargará de notificar los diferentes eventos de awareness.
 */
public interface SynchronousAwarenessListener extends FacadeDesktopListener {

    /**
     * Notifica a la aplicación del cliente, para habilitar o inabilitar la  creación de
     * consulta de búsqueda de forma colaborativa (PQT).
     *
     * @param evt instancia de SynchronousAwarenessEvent
     *
     * <br><br>
     * El objeto SynchronousAwarenessEvent contiene la información sobre el evento.
     *
     * <br><pre>
     *  public void notifyPuttingQueryTermsTogether(SynchronousAwarenessEvent evt){
     *      Response rsp = evt.getResponse();
     *      String sessionName = rsp.get(SESSION_NAME).toString();
     *      
     *      ...
     *      ..
     *  }
     *
     * La siguiente tabla muestra la relación de campos(clave-valor) de esta notificación.
     * Las constantes clave(key) pertenecen a la clase <b>​drakkar.oar.​util.​KeyTransaction </b>, 
     * y los valores a la clase <b>​drakkar.oar.​util.​KeyAwareness</b>.
     *
     *
     * <br><br><table border = 1 summary="Shows property keys and associated values">
     * <tr>
     *     <th>Key</th>
     *     <th>Associated Value</th>
     *     <th>Description</th></tr>
     * 
     * </tr>
     *
     * <tr>
     *    <td><code>SESSION_NAME</code></td>
     *    <td><code>String <code></td>
     *    <td>Nombre de la sesión colaborativa de búsqueda.</td>
     * </tr>
     *
     * <tr>
     *    <td rowspan= 2><code>DISTRIBUTED_EVENT</code></td>
     *     <td><code>KeyAwareness.ENABLE_PQT<code></td>
     *    <td>Habilitar la confección de la consulta de búsqueda.</td>
     * </tr>
     * <tr>
     *     <td><code>KeyAwareness.DISABLE_PQT<code></td>
     *    <td>Inabilitar la confección de la consulta de búsqueda.</td>
     * </tr>
     *
     * </table>
     *
     *  <br><b> Case:</b> ENABLE_PQT
     * <table border = 1 >
     * <tr><th>Key</th>
     *     <th>Associated Value</th><th>Description</th></tr>
     * <tr>
     *    <td><code>KeyTransaction.IS_CHAIRMAN</code></td>
     *    <td><code>boolean<code></td>
     *    <td>Especifica si el usuario a notificar es el chairman
     *    de la sesión colaborativa de búsqueda.</td>
     * </tr>
     *
     * </table>
     *
     */
    public void notifyPuttingQueryTermsTogether(SynchronousAwarenessEvent evt);

    /**
     * Notifica a la aplicación del cliente el cambio de la consulta PQT de un
     * seeker de la sesion colaborativa de búsqueda.
     *
     * @param evt instancia de SynchronousAwarenessEvent
     *
     * <br><br>
     * El objeto SynchronousAwarenessEvent contiene la información sobre los nuevos términos
     * de consulta.
     *
     * <br><pre>
     *  public void notifyQueryChange(SynchronousAwarenessEvent evt){
     *      Response rsp = evt.getResponse();
     *      String sessionName = rsp.get(SESSION_NAME).toString();
     *      
     *      ...
     *      ..
     *  }
     *
     * La siguiente tabla muestra la relación de campos(clave-valor) de esta notificación.
     * Las constantes clave(key) pertenecen a la clase <b>​drakkar.oar.​util.​KeyTransaction </b> y
     * los valores a la clase <b>drakkar.oar.​util.​KeySession</b>.
     *
     *
     * <br><br><table border = 1 summary="Shows property keys and associated values">
     * <tr>
     *     <th>Key</th>
     *     <th>Associated Value</th>
     *     <th>Description</th></tr>
     * 
     * </tr>
     *
     * <tr>
     *    <td><code>SESSION_NAME</code></td>
     *    <td><code>String <code></td>
     *    <td>Nombre de la sesión colaborativa de búsqueda.</td>
     * </tr>
     *
     * <tr>
     *    <td><code>SEEKER_NICKNAME</code></td>
     *    <td><code>String <code></td>
     *    <td>Nombre del usuario emisor de la acción.</td>
     * </tr>
     *
     * <tr>
     *    <td><code>QUERY</code></td>
     *    <td><code>String <code></td>
     *    <td>Nueva consulta propuesta por el usuario emisor de la acción.</td>
     * </tr>
     *
     * <tr>
     *    <td><code>SCORE_PQT</code></td>
     *    <td><code>Map&lt;String,ScorePQT&gt; <code></td>
     *    <td>Relación de votos(término, votos) efectuados, por término de la consulta propuesta.</td>
     * </tr>
     *
     * </table>
     *
     */
    public void notifyQueryChange(SynchronousAwarenessEvent evt);

    /**
     * Notifica el inicio o fin de la confección(Es decir notifica cuando comienza y termina de escribir)
     * de una consulta de búsqueda PQT, por por un miembro de la sesión colaborativa de búsqueda.
     *
     * @param evt instancia de SynchronousAwarenessEvent
     *
     * <br><br>
     * El objeto SynchronousAwarenessEvent contiene la información relacionada con la confección
     * de la consulta de búsqueda PQT.
     *
     * <br><pre>
     *  public void notifyQueryTyped(SynchronousAwarenessEvent evt){
     *      Response rsp = evt.getResponse();
     *      String sessionName = rsp.get(SESSION_NAME).toString();
     *    
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
     * <tr>
     *     <th>Key</th>
     *     <th>Associated Value</th>
     *     <th>Description</th></tr>
     *
     * </tr>
     *
     * <tr>
     *    <td><code>SESSION_NAME</code></td>
     *    <td><code>String <code></td>
     *    <td>Nombre de la sesión colaborativa de búsqueda.</td>
     * </tr>
     *
     * <tr>
     *    <td><code>SEEKER_NICKNAME</code></td>
     *    <td><code>String <code></td>
     *    <td>Nombre del usuario emisor de la acción.</td>
     * </tr>
     *
     * <tr>
     *    <td><code>QUERY_TYPED</code></td>
     *    <td><code>boolean <code></td>
     *    <td>true si es el inicio de confección de la consulta, false en caso contrario.</td>
     * </tr>
     *
     * </table>
     */
    public void notifyQueryTyped(SynchronousAwarenessEvent evt);

    /**
     * Notifica la votación de aceptación o no, de un término de la consulta de un seeker, de la sesión
     * colaborativa de búsqueda, por otro miembro de la misma.
     *
     * @param evt instancia de SynchronousAwarenessEvent
     *
     * <br><br>
     * El objeto SynchronousAwarenessEvent contiene la información relacionada
     * con el cambio del chairman de una sesión colaborativad de búsqueda.
     *
     * <br><pre>
     *  public void notifyChairmanSetting(SynchronousAwarenessEvent evt){
     *      Response rsp = evt.getResponse();
     *      String sessionName = rsp.get(SESSION_NAME).toString();
     *
     *      .....
     *  }
     * 
     * La siguiente tabla muestra la relación de campos(clave-valor) de esta notificación.
     * Las constantes clave(key) pertenecen a las clases <b>​drakkar.oar.​util.​KeyTransaction </b>,
     * <b>​drakkar.oar.​util.​KeyAwareness</b> y los valores a la clase <b>drakkar.oar.​util.​KeySession</b>.
     *
     *
     * <br><br><table border = 1 summary="Shows property keys and associated values">
     * <tr>
     *     <th>Key</th>
     *     <th>Associated Value</th>
     *     <th>Description</th></tr>
     *
     * </tr>
     *
     * <tr>
     *    <td><code>SESSION_NAME</code></td>
     *    <td><code>String <code></td>
     *    <td>Nombre de la sesión colaborativa de búsqueda.</td>
     * </tr>
     *
     * <tr>
     *    <td><code>SEEKER_NICKNAME</code></td>
     *    <td><code>String <code></td>
     *    <td>Nombre del usuario emisor de la acción.</td>
     * </tr>
     *
     * <tr>
     *    <td><code>QUERY_TERM</code></td>
     *    <td><code>String <code></td>
     *    <td>término de la consulta búsqueda propuesta a votar.</td>
     * </tr>
     *
     * <tr>
     *    <td rowspan= 2><code>DISTRIBUTED_EVENT</code></td>
     *     <td><code>KeyAwareness.TERM_AGREE<code></td>
     *    <td>No aceptación del témino de la consulta.</td>
     * </tr>
     * <tr>
     *     <td><code>KeyAwareness.TERM_DISAGREE<code></td>
     *    <td>Aceptación del témino de la consulta.</td>
     * </tr>
     *
     * </table>
     */
    public void notifyQueryTermAcceptance(SynchronousAwarenessEvent evt);
}
