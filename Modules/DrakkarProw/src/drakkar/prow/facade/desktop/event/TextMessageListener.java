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

public interface TextMessageListener extends FacadeDesktopListener {

    /**
     * Notifica los mensajes enviados por otro usuario.
     *
     * @param evt instancia de TextMessageEvent
     *
    * <br><br>
     * El objeto TextMessageEvent almacena información del mensaje enviado, tales
     * como: remitente, texto del mensaje, etc.
     * <br><pre>
     *  public void notifyTextMessage(TextMessageEvent evt){
     *      Response rsp = evt.getResponse();
     *      String sessionName = rsp.get(SESSION_NAME).toString();
     *      String msg = rsp.get(MESSAGE).toString();
     *      Seeker emitter = (Seeker)rsp.get(SEEKER_EMITTER);
     *      .....
     *      ...
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
     * <tr>
     *    <td><code>SESSION_NAME</code></td>
     *     <td><code>String <code></td>
     *    <td>Nombre de la sesión a la que pertenece el emisor. El valor de este
     *    parámetro debe tenerse en cuenta, en los casos que se tengan paneles de
     *    mensajes diferentes para la sesión de comunicación y las sesiones de búsqueda
     *    colaborativa, el nombre de la sesión permitirá mostrar el mensaje en el panel
     *    corrcto.</td>
     * </tr>
     * <tr>
     *    <td><code>SEEKER_EMITTER</code></td>
     *     <td><code>Seeker <code></td>
     *    <td>Emisor o remitente del mensaje.</td>
     * </tr>
     * <tr>
     *    <td><code>MESSAGE</code></td>
     *     <td><code>String <code></td>
     *    <td>Texto del mensaje.</td>
     * </tr>
     *
     * </table>
     */
    public void notifyTextMessage(TextMessageEvent evt);
}
