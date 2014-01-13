/*
 * DrakkarKeel - An Enterprise Collaborative Search Platform
 *
 * The contents of this file are subject under the terms described in the
 * DRAKKARKEEL_LICENSE file included in this distribution; you may not use this
 * file except in compliance with the License. 
 *
 * 2013-2014 DrakkarKeel Platform.
 */
package drakkar.stern.servant.service;

import drakkar.oar.Seeker;
import drakkar.oar.exception.AwarenessException;
import drakkar.oar.exception.SeekerException;
import drakkar.oar.exception.SessionException;
import drakkar.oar.slice.client.ClientSidePrx;


public interface Suggestable {

     /**
     * Enviar un mensaje de notificación para habilitar o inabilitar la  sugerencia
      * de términos de consulta a los miemnbros de una sesión colaborativa de búsqueda
     *
     * @param sessionName         nombre de la sesión colaborativa de búsqueda
     * @param event
     * @param emitter             seeker que emite la acción
     * @throws SessionException   si la sesión no se encuentra registrada en el servidor
     * @throws SeekerException    si el usuario emisor no se encuentra registrado en la sesión
     * @throws AwarenessException si ocurre un error en el proceso de notificación de la acción
     */
    public void sendCollabTermsSuggestAction(String sessionName, int event, Seeker emitter, ClientSidePrx prx) throws SessionException, SeekerException, AwarenessException;

}
