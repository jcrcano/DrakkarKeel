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

import drakkar.oar.ScorePQT;
import drakkar.oar.Seeker;
import drakkar.oar.exception.AwarenessException;
import drakkar.oar.exception.SeekerException;
import drakkar.oar.exception.SessionException;
import java.util.Map;

public interface Perceptionable {

    /**
     * Enviar un mensaje de notificación para habilitar o inabilitar la  creación de
     * consulta de búsqueda de forma colaborativa (PQT).
     *
     * @param sessionName         nombre de la sesión colaborativa de búsqueda
     * @param event
     * @param emitter             seeker que emite la acción
     * @throws SessionException   si la sesión no se encuentra registrada en el servidor
     * @throws SeekerException    si el usuario emisor no se encuentra registrado en la sesión
     * @throws AwarenessException si ocurre un error en el proceso de notificación de la acción
     */
    public void sendPuttingQueryTermsTogetherAction(String sessionName, int event, Seeker emitter) throws SessionException, SeekerException, AwarenessException;

    /**
     * Enviar un mensaje de notificación de cambio de la consulta de búsqueda propuesta por el seeker
     * 
     * @param sessionName         nombre de la sesión colaborativa de búsqueda
     * @param query               consulta de búsqueda propuesta
     * @param statistics          relación de votos por término de la consulta
     * @param emitter             seeker que emite la acción  
     * @throws SessionException   si la sesión no se encuentra registrada en el servidor
     * @throws SeekerException    si el usuario emisor no se encuentra registrado en la sesión
     * @throws AwarenessException si ocurre un error en el proceso de notificación de la acción
     */
    public void sendQueryChangeAction(String sessionName, String query, Map<String, ScorePQT> statistics, Seeker emitter) throws SessionException, SeekerException, AwarenessException;

    /**
     * Enviar un mensaje de notificación para informar a los miembros de la sesión colaborativa, que el seeker
     * está modificando su consulta de búsqueda propuesta
     *
     * @param sessionName         nombre de la sesión colaborativa de búsqueda
     * @param typed               true si es el inicio de confección de la consulta, false en caso contrario
     * @param emitter             seeker que emite la acción
     * @throws SessionException   si la sesión no se encuentra registrada en el servidor
     * @throws SeekerException    si el usuario emisor no se encuentra registrado en la sesión
     * @throws AwarenessException si ocurre un error en el proceso de notificación de la acción
     */
    public void sendQueryTypedAction(String sessionName, boolean typed, Seeker emitter) throws SessionException, SeekerException, AwarenessException;

    /**
     * Enviar un mensaje de notificación para informar a los miembros de la sesión colaborativa, el voto del seeker
     * sobre un término de la consulta propuesta por otro seeker
     *
     * @param sessionName         nombre de la sesión colaborativa de búsqueda
     * @param term                término de la consulta búsqueda propuesta a votar
     * @param event               tipo de evento, KeyAwareness.TERM_AGREE para aceptar el término, KeyAwareness.TERM_DISAGREE en caso contrario
     * @param user                usario que propone el término
     * @param emitter             seeker que emite la acción
     * @throws SessionException   si la sesión no se encuentra registrada en el servidor
     * @throws SeekerException    si el usuario emisor no se encuentra registrado en la sesión
     * @throws AwarenessException si ocurre un error en el proceso de notificación de la acción
     */
    public void sendTermAcceptanceAction(String sessionName, String term, int event, String user, Seeker emitter) throws SessionException, SeekerException, AwarenessException;
}
