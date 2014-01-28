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

public interface ExplicitRecommendationListener extends FacadeDesktopListener {

    /**
     * Notifica las recomendaciones realizadas por otro usuario.
     *
     * @param evt instancia de RecommendationEvent
     *
     * <br><br>
     * El objeto ExplicitRecommendationEvent contiene la información relacionada
     * con la recomendación efectuada por otro seeker.
     * <br><pre>
     *  public void notifyRecommendation(ExplicitRecommendationEvent evt){
     *      Response rsp = evt.getResponse();
     *      ResultSetMetaData rec = (ResultSetMetaData) rsp.get(RECOMMENDATION);
     *      String comment = rsp.get(COMMENT).toString();
     *      Seeker rec = (Seeker) rsp.get(SEEKER_EMITTER);
     *      ...
     *      ..
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
     *    <td><code>RECOMMENDATION</code></td>
     *     <td><code>ResultSetMetaData <code></td>
     *    <td>Listado de los documentos recomendados.</td>
     * </tr>
     *
     * <tr>
     *    <td><code>COMMENT</code></td>
     *     <td><code>String <code></td>
     *    <td>Comentario de la recomendación.</td>
     * </tr>
     *
     * <tr>
     *    <td><code>SEEKER_EMITTER</code></td>
     *     <td><code>Seeker <code></td>
     *    <td>Instancia del objeto Seeker que representa al remitente de la recomendación.</td>
     * </tr>
     *
     * </table>
     */
    public void notifyRecommendation(ExplicitRecommendationEvent evt);
}
