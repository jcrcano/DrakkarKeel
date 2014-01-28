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

import drakkar.oar.Response;
import drakkar.oar.Seeker;
import drakkar.oar.Session;
import static drakkar.oar.util.KeySession.*;
import static drakkar.oar.util.KeyTransaction.*;
import static drakkar.oar.util.NotifyAction.*;
import static drakkar.oar.util.SeekerAction.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ResponseSessionFactory {

    /**
     * Devuelve un objeto response para notificar a un seeker las operaciones
     * siguientes:
     *<br><br><table border = 1 summary="Shows property keys and associated values">
     * <tr><th>Key</th><th>Description</th></tr>
     * <tr>
     *    <td><code>DECLINE_COLLAB_SESSION</code></td>
     *    <td>No admisión en una sesión colaborativa de búsqueda</td>
     * </tr>
    
     * <tr>
     *    <td><code>DELETED_COLLAB_SESSION</code></td>
     *    <td>Eliminar una sesión colaborativa de búsqueda.</td>
     * </tr>
     * <tr>
     *    <td><code>SEEKER_LOGOUT_COLLAB_SESSION</code></td>
     *    <td>Salida de un seeker de una sesión colaborativa de búsqueda.</td>
     * </tr>
     *  <tr>
     *    <td><code>DECLINE_SEEKER_COLLAB_SESSION</code></td>
     *    <td>Eliminar un seeker de una sesión colaborativa de búsqueda.</td>
     * </tr>
     * </table>
     * 
     * @param sessionName nombre de la sesión colaborativa
     * @param operation   acción a notificar 
     *
     * @return objeto response
     */
    public static Response getResponse(int operation, String sessionName) {

        Map<Object, Object> hash = new HashMap<>(4);

        switch (operation) {
            case DECLINE_COLLAB_SESSION:
                hash.put(OPERATION, NOTIFY_COLLAB_SESSION_ACCEPTANCE);
                hash.put(DISTRIBUTED_EVENT, DECLINE_COLLAB_SESSION);
                break;


            case SEEKER_LOGOUT_COLLAB_SESSION:
                hash.put(OPERATION, NOTIFY_COLLAB_SESSION_AUTHENTICATION);
                hash.put(DISTRIBUTED_EVENT, SEEKER_LOGOUT_COLLAB_SESSION);
                hash.put(CAUSE, END_SESSION);
                break;

            case DELETED_COLLAB_SESSION:
                hash.put(OPERATION, NOTIFY_COLLAB_SESSION_EVENT);
                hash.put(DISTRIBUTED_EVENT, DELETED_COLLAB_SESSION);

                break;

            case DECLINE_SEEKER_COLLAB_SESSION:
                hash.put(OPERATION, NOTIFY_COLLAB_SESSION_AUTHENTICATION);
                hash.put(DISTRIBUTED_EVENT, DECLINE_SEEKER_COLLAB_SESSION);
                break;
            case SELF_LOGOUT_COLLAB_SESSION:
                hash.put(OPERATION, NOTIFY_COLLAB_SESSION_AUTHENTICATION);
                hash.put(DISTRIBUTED_EVENT, SELF_LOGOUT_COLLAB_SESSION);
                break;
        }


        hash.put(SESSION_NAME, sessionName);

        return new Response(hash);

    }

    /**
     * Devuelve un objeto response para notificar a un seeker la finalización
     * de una sesión de búsqueda colaborativa, a partir  de una de las siguientes
     * causas:
     *
     *<br><br><table border = 1 summary="Shows property keys and associated values">
     * <tr><th>Key</th><th>Description</th></tr>
     * <tr>
     *    <td><code>HARD_INTEGRITY_CRITERIA</code></td>
     *    <td>No admisión en una sesión colaborativa de búsqueda</td>
     * </tr>
     * <tr>
     *    <td>Finalizar una sesión colaborativa de búsqueda.</td>
     * </tr>
     * <tr>
     *    <td><code>END_SESSION</code></td>
     *    <td>Eliminar una sesión colaborativa de búsqueda.</td>
     * </tr>
     * 
     * </table>
     *
     * @param sessionName nombre de la sesión colaborativa
     * @param cause
     * @param operation   acción a notificar
     *
     * @return objeto response
     */
    public static Response getResponse(String sessionName, int cause) {

        Map<Object, Object> hash = new HashMap<>(4);

        hash.put(OPERATION, NOTIFY_COLLAB_SESSION_EVENT);
        hash.put(DISTRIBUTED_EVENT, FINALIZED_COLLAB_SESSION);
        hash.put(SESSION_NAME, sessionName);
        hash.put(CAUSE, cause);


        return new Response(hash);

    }

    /**
     * Devuelve un objeto response para notificar la respuesta de solicitud de
     * conexión efectuada por un seeker.
     *
     * @param sessionName nombre de la sesión colaborativa
     * @param operation        acción a notificar (CONNECTION_SUCCESSFUL,CONNECTION_FAILED)
     * @param seekersList      lista de seekers conectados
     * @param searchers        buscadores disponibles
     * @param principles       principios de búsqueda soportados
     * @param activeSessions   sesiones iniciadas
     * @param availableSession sesiones a las que pertenece el seeker
     *
     * @return objeto response
     */
    public static Response getResponse(int operation, String sessionName, List<Seeker> seekersList, String[] searchers, String[] repositories, List<String> principles, List<Session> activeSessions, List<Session> availableSession) {

        Map<Object, Object> hash = new HashMap<>(10);
        hash.put(OPERATION, NOTIFY_REQUEST_CONNECTION);

        switch (operation) {
            case CONNECTION_FAILED:
                hash.put(REPLY, CONNECTION_FAILED);
                hash.put(CAUSE, SEEKER_ALREADY_REGISTERED);
                break;

            case CONNECTION_SUCCESSFUL:
                hash.put(SESSION_NAME, sessionName);
                hash.put(SEEKERS_SESSION, seekersList);
                hash.put(SEARCHERS, searchers);
                hash.put(SVN_REPOSITORIES_NAMES, repositories);
                hash.put(PERSISTENT_SESSIONS, availableSession);
                hash.put(OPEN_SESSIONS, activeSessions);
                hash.put(SEARCH_PRINCIPLES, principles);
                hash.put(REPLY, CONNECTION_SUCCESSFUL);
                break;
        }


        hash.put(SESSION_NAME, sessionName);

        return new Response(hash);

    }

    /**
     * Devuelve un objeto response para notificar la entrada al seeker a la sesión
     * colaborativa de búsqueda.
     *
     * @param sessionName nombre de la sesión colaborativa
     * @param seekersList miembros de la sesión
     *
     * @return objeto response
     */
    public static Response getResponse(String sessionName, List<Seeker> seekersList, List<Session> availableSession) {

        Map<Object, Object> hash = new HashMap<>(4);
        hash.put(OPERATION, NOTIFY_COLLAB_SESSION_ACCEPTANCE);
        hash.put(DISTRIBUTED_EVENT, CONFIRM_COLLAB_SESSION);
        hash.put(SESSION_NAME, sessionName);
        hash.put(SEEKERS_SESSION, seekersList);
        hash.put(PERSISTENT_SESSIONS, availableSession);

        return new Response(hash);

    }

    /**
     * Devuelve un objeto response para notificar las operaciones de:
     *  - Entrada al seeker a la sesión colaborativa de búsqueda.
     *  - La creación de la sesión colaborativa de búsqueda su creador.
     *
     * @param sessionName nombre de la sesión colaborativa
     * @param operation   acción a notificar (SELF_LOGIN_COLLAB_SESSION,CREATED_COLLAB_SESSION)
     * @param seekersList miembros de la sesión
     * @param principles  principios de búsqueda
     *
     * @return objeto response
     */
    public static Response getResponse(List<Seeker> seekersList, List<String> principles, String sessionName) {
        Map<Object, Object> hash = new HashMap<>(5);
        hash.put(OPERATION, NOTIFY_COLLAB_SESSION_AUTHENTICATION);
        hash.put(DISTRIBUTED_EVENT, SELF_LOGIN_COLLAB_SESSION);
        hash.put(SESSION_NAME, sessionName);
        hash.put(SEEKERS_SESSION, seekersList);
        hash.put(SEARCH_PRINCIPLES, principles);

        return new Response(hash);

    }

    public static Response getResponse(String sessionName, List<Seeker> seekersList, List<String> principles, List<Session> availableSession) {
        Map<Object, Object> hash = new HashMap<>(6);
        hash.put(OPERATION, NOTIFY_COLLAB_SESSION_EVENT);
        hash.put(DISTRIBUTED_EVENT, CREATED_COLLAB_SESSION);
        hash.put(SESSION_NAME, sessionName);
        hash.put(SEEKERS_SESSION, seekersList);
        hash.put(PERSISTENT_SESSIONS, availableSession);
        hash.put(SEARCH_PRINCIPLES, principles);

        return new Response(hash);

    }

//    List<Session> availableSession
    /**
     * Devuelve un objeto response para notificar la el cambio del chairman de la
     * sesión colaborativa
     *
     * @param sessionName  nombre de la sesión colaborativa
     * @param isChairman   true si es el chairman de la sesión, flase en caso contrario
     * @param chairmanName nombre del actual chairman de la sesión
     * @param principles   principios de búsqueda
     *
     * @return objeto response
     */
    public static Response getResponse(String sessionName, boolean isChairman, String chairmanName, List<String> principles) {
        Map<Object, Object> hash = new HashMap<>(6);
        hash.put(OPERATION, NOTIFY_CHAIRMAN_SETTING);
        hash.put(DISTRIBUTED_EVENT, CHANGE_CHAIRMAN_COLLAB_SESSION);
        hash.put(SESSION_NAME, sessionName);
        hash.put(IS_CHAIRMAN, isChairman);
        hash.put(SEARCH_PRINCIPLES, principles);
        hash.put(CHAIRMAN_NAME, chairmanName);

        return new Response(hash);

    }

    /**
     * Devuelve un objeto response para notificar la el cambio del chairman de la
     * sesión colaborativa
     *
     * @param sessionName  nombre de la sesión colaborativa
     * @param isChairman   true si es el chairman de la sesión, flase en caso contrario
     * @param chairmanName nombre del actual chairman de la sesión
     * @param principles   principios de búsqueda
     *
     * @return objeto response
     */
    public static Response getResponse(List<String> principles) {
        Map<Object, Object> hash = new HashMap<>(6);
        hash.put(OPERATION, NOTIFY_AVAILABLE_SEARCH_PRINCIPLES);
        hash.put(SEARCH_PRINCIPLES, principles);


        return new Response(hash);

    }

    /**
     * Devuelve un objeto response para notificar la solicitud de un seeker al
     * chairman de una sesión colaborativa de búsqueda.
     *
     * @param sessionName nombre de la sesión colaborativa
     * @param seeker      seeker que realiza la solicitud
     *
     * @return objeto response
     */
    public static Response getResponse(String sessionName, Seeker seeker) {

        Map<Object, Object> hash = new HashMap<>(4);
        hash.put(OPERATION, NOTIFY_COLLAB_SESSION_ACCEPTANCE);
        hash.put(DISTRIBUTED_EVENT, REQUEST_CONFIRM_COLLAB_SESSION);
        hash.put(SESSION_NAME, sessionName);
        hash.put(SEEKER, seeker);

        return new Response(hash);

    }
}
