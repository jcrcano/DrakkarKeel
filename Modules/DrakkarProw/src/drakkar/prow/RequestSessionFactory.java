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

import drakkar.oar.Request;
import drakkar.oar.Seeker;
import static drakkar.oar.util.KeyTransaction.*;
import static drakkar.oar.util.SeekerAction.*;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Esta clase es permite construir objetos Request para efectuar las diferentes
 * operaciones soportados por DrakkarKeel referente a sesiones de búsquedas individuales
 * ó colaborativas
 */
public class RequestSessionFactory implements Serializable{
    private static final long serialVersionUID = 80000000000010L;

    /**
     * Devuelve un objeto Request para actualizar la foto del usuario
     *
     * @param sessionName nombre de la sesión
     * @param avatar      nueva foto de usuario
     *
     * @return  objeto request
     */
    public static Request getRFUpdateAvatarSeeker(String sessionName, byte[] avatar) {
        Map<Object, Object> hash = new HashMap<>(3);
        hash.put(OPERATION, UPDATE_AVATAR_SEEKER);
        hash.put(SESSION_NAME, sessionName);
        hash.put(SEEKER_AVATAR, avatar);


        return new Request(hash);
    }

    /**
     * Devuelve un objeto Request para salir de una sesión collaborativa de búsqueda
     *
     * @param sessionName nombre de la sesión collaborativa de búsqueda
     *
     * @return objeto request
     */
    public static Request getRFLogoutCollabSession(String sessionName) {
        Map<Object, Object> hash = new HashMap<>(2);
        hash.put(OPERATION, LOGOUT_COLLAB_SESSION);
        hash.put(SESSION_NAME, sessionName);

        return new Request(hash);
    }

    /**
     *
     * @param sessionName
     * @return
     */
    public static Request getRFCloseCollabSession(String sessionName) {
        Map<Object, Object> hash = new HashMap<>(2);
        hash.put(OPERATION, CLOSE_COLLAB_SESSION);
        hash.put(SESSION_NAME, sessionName);

        return new Request(hash);
    }

    /**
     * 
     * @param sessionName
     * @return
     */
    public static Request getRFDeclineSeekerCollabSession(String sessionName) {
        Map<Object, Object> hash = new HashMap<>(2);
        hash.put(OPERATION, DECLINE_SEEKER_COLLAB_SESSION);
        hash.put(SESSION_NAME, sessionName);

        return new Request(hash);
    }

    /**
     * Devuelve un objeto Request para crear una nueva sesión collaborativa de búsqueda
     *
     * @param newSessionName     nombre de la nueva sesión
     * @param description        descripción de la sesión
     * @param membersMinNumber   cantidad mínima de miembros
     * @param membersMaxNumber   cantidad máxima de miembros
     * @param integrityCriteria  criterio de integridad de la sesión, posibles
     *                           valores <SESSION_SOFT,SESSION_HARD>,ambos definidos
     *                           en la clase SessionAssignable
     * @param membershipPolicy   política de membresía de la sesión, posibles valores
     *                           <SESSION_STATIC,SESSION_DYNAMIC_AND_CLOSE,SESSION_DYNAMIC_AND_OPEN>,
     *                           definidos  en la clase SessionAssignable
     * @param members            lista de miembro a integrar la sesión
     *
     * @see SessionAssignable

     * @return objeto request
     *
     */
    public static Request getRFCreateCollabSession(String newSessionName, String description, int membersMinNumber, int membersMaxNumber, int integrityCriteria, int membershipPolicy, List<Seeker> members) {
        Map<Object, Object> hash = new HashMap<>(7);
        hash.put(OPERATION, CREATE_COLLAB_SESSION);
        hash.put(SESSION_NAME, newSessionName);
        hash.put(MEMBERS_MIN_NUMBER, membersMinNumber);
        hash.put(MEMBERS_MAX_NUMBER, membersMaxNumber);
        hash.put(INTEGRITY_CRITERIA, integrityCriteria);
        hash.put(MEMBERSHIP_POLICY, membershipPolicy);
        hash.put(SEEKERS_SESSION, members);
        hash.put(SESSION_DESCRIPTION, description);

        return new Request(hash);
    }

    /**
     * Devuelve un objeto Request para para la unión de un usuario a una sesión
     * colaborativa de búsqueda ó la solitud de admisión si la normativas de esta,
     * no soportan la entrada libre de cualquier usuario.
     *
     * @param sessionName nombre de la sesión a la cual se desea unir
     *
     * @return objeto request
     */
    public static Request getRFJoinCollabSession(String sessionName) {

        Map<Object, Object> hash = new HashMap<>(2);
        hash.put(OPERATION, JOIN_COLLAB_SESSION);
        hash.put(SESSION_NAME, sessionName);

        return new Request(hash);
    }

      /**
     * Devuelve un objeto Request para para que un usuario entre a una sesión
     * colaborativa de búsqueda, a la cual ya pertenece.
     *
     * @param sessionName nombre de la sesión a la cual pertenece
     *
     * @return objeto request
     */
    public static Request getRFEnterCollabSession(String sessionName) {

        Map<Object, Object> hash = new HashMap<>(2);
        hash.put(OPERATION, ENTER_COLLAB_SESSION);
        hash.put(SESSION_NAME, sessionName);

        return new Request(hash);
    }

    /**
     * Devuelve un objeto Request para actualizar el estado de un usuario a todos
     * los miembros de su sesión.
     *
     * @param sessionName  nombre de la sesión
     * @param status       nuevo estado del miembro.
     *
     * @return objeto request
     *
     */
    public static Request getRFUpdateSeekerState(String sessionName, int status) {
        Map<Object, Object> hash = new HashMap<>(3);
        hash.put(OPERATION, UPDATE_STATE_SEEKER);
        hash.put(SESSION_NAME, sessionName);
        hash.put(SEEKER_STATE, status);

        return new Request(hash);
    }

    /**
     * Devuelve un objeto Request para notificar la admisión de un miembro en una sesión.
     *
     * @param sessionName   nombre de la sesión
     * @param seeker        miembro que solicitó la admisión
     *
     * @return objeto request
     *
     */
    public static Request getRFAdmissionCollabSession(String sessionName, Seeker seeker) {
        Map<Object, Object> hash = new HashMap<>(3);
        hash.put(OPERATION, NOTIFY_ADMISSION_COLLAB_SESSION);
        hash.put(SESSION_NAME, sessionName);
        hash.put(SEEKER_NOTIFY, seeker);

        return new Request(hash);

    }

    /**
     * Devuelve un objeto Request para notificar la no admisión de un miembro en una sesión.
     *
     * @param sessionName   nombre de la sesión
     * @param seeker        miembro que solicitó la admisión
     *
     * @return objeto request
     *
     */
    public static Request getRFNoAdmissionCollabSession(String sessionName, Seeker seeker) {
        Map<Object, Object> hash = new HashMap<>(3);
        hash.put(OPERATION, NOTIFY_NO_ADMISSION_COLLAB_SESSION);
        hash.put(SESSION_NAME, sessionName);
        hash.put(SEEKER_NOTIFY, seeker);

        return new Request(hash);
    }

    /**
     * Devuelve un objeto Request para solicitar la lista de usuarios de una sesión.
     *
     * @param sessionName   nombre de la sesión
     * 
     * @return objeto request
     */
    public static Request getRFSeekerList(String sessionName) {
        Map<Object, Object> hash = new HashMap<>(3);
        hash.put(OPERATION, GET_ONLINE_SEEKERS);
        hash.put(SESSION_NAME, sessionName);

        return new Request(hash);
    }

    /**
     * Devuelve un objeto Request para finalizar una sesión colaborativa de búsqueda.
     *
     * @param sessionName   nombre de la sesión de búsqueda
     * 
     * @return objeto request
     */
    public static Request getRFFinalizeCollabSession(String sessionName) {
        Map<Object, Object> hash = new HashMap<>(2);
        hash.put(OPERATION, FINALIZE_COLLABORATIVE_SESSION);
        hash.put(SESSION_NAME, sessionName);

        return new Request(hash);
    }

    /**
     * Devuelve un objeto Request para notificar al servidor la revisión de un documento.
     *
     * @param sessionName   nombre de la sesión.
     * @param query         consulta de la búsqueda por la cual se obtuvo el docuemento     
     * @param docIndex      índice del documento revisado
     * @param searcher      id del buscador que encontró el documento     
     *
     * @param uri 
     * @return objeto request
     */
    public static Request getRFNotifyDocViewed(String sessionName, String query ,int docIndex, int searcher, String uri) {
        Map<Object, Object> hash = new HashMap<>(5);
        hash.put(OPERATION, NOTIFY_DOCUMENT_VIEWED);
        hash.put(SESSION_NAME, sessionName);
        hash.put(QUERY, query);
        hash.put(DOC_INDEX, docIndex);
        hash.put(SEARCHER, searcher);
        hash.put(URI, uri);
        
        return new Request(hash);
    }

    /**
     * Devuelve un objeto Request para notificar al servidor la evaluación de un documento.
     *
     * @param sessionName   nombre de la sesión.
     * @param query         consulta de la búsqueda por la cual se obtuvo el docuemento     
     * @param docIndex      índice del documento revisado.
     * @param relevance     calificación dada al documento por su relevancia.
     * @param searcher      id del buscador que encontró el documento     
     * @param source        fuente del documento: caché, BD, recomendación
     * 
     * @param uri
     * @return objeto request
     */
    public static Request getRFDocEvaluated(String sessionName, String query, int docIndex, byte relevance, int searcher, int source, String uri) {
      
        Map<Object, Object> hash = new HashMap<>(5);
        hash.put(OPERATION, NOTIFY_DOCUMENT_EVALUATED);
        hash.put(SESSION_NAME, sessionName);
        hash.put(QUERY, query);
        hash.put(DOC_INDEX, docIndex);
        hash.put(SEARCHER, searcher);
        hash.put(RELEVANCE, relevance);
        hash.put(SEARCH_RESULT_SOURCE, source);
        hash.put(URI, uri);
       
        return new Request(hash);

    }

    /**
     * Devuelve un objeto Request para notificae al servidor un comentario efectuado sobre un documento.
     *
     * @param sessionName   nombre de la sesión
     * @param query         consulta de la búsqueda por la cual se obtuvo el docuemento     
     * @param docIndex      índice del documento revisado
     * @param searcher    id del buscador que encontró el documento
     * @param comment       comentario del documento
     *
     * @param source        fuente del documento: caché, BD, recomendación
     *
     * @param uri
     * @return objeto request
     */
    public static Request getRFDocCommented(String sessionName, String query, int docIndex, int searcher, String comment, int source, String uri) {
       
        Map<Object, Object> hash = new HashMap<>(6);
        hash.put(OPERATION, NOTIFY_DOCUMENT_COMMENTED);
        hash.put(SESSION_NAME, sessionName);
        hash.put(QUERY, query);
        hash.put(DOC_INDEX, docIndex);
        hash.put(SEARCHER, searcher);
        hash.put(COMMENT, comment);
        hash.put(SEARCH_RESULT_SOURCE, source);
        hash.put(URI, uri);
        
        return new Request(hash);
    }
}
