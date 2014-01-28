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

import drakkar.oar.Documents;
import drakkar.oar.QuerySource;
import drakkar.oar.Request;
import drakkar.oar.Seeker;
import static drakkar.oar.util.KeyTransaction.*;
import static drakkar.oar.util.SeekerAction.*;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Esta clase es permite construir objetos Request para efectuar los diferentes
 * métodos de recomendaciones soportadas por DrakkarKeel
 */
public class RequestRecommendFactory implements Serializable{
    private static final long serialVersionUID = 80000000000008L;

    /**
     * Devuelve un objeto Request para recomendar todos resultados de la búsqueda
     * a toda la sesión
     *
     * @param sessionName   nombre de la sesión del usuario
     * @param comments      comentarios de la recomendación
     *
     * @param data
     * @return              objeto request
     */
    public static Request getRFRecommendation(String sessionName, String comments, QuerySource data) {
        Map<Object, Object> hash = new HashMap<>(4);
        hash.put(OPERATION, RECOMMEND_SESSION_RESULTS);
        hash.put(SESSION_NAME, sessionName);
        hash.put(COMMENT, comments);
        hash.put(QUERY_SOURCE, data);
        return new Request(hash);
    }

    /**
     * Devuelve un objeto Request para recomendar todos resultados de la búsqueda
     * a un usuario
     *
     * @param sessionName   nombre de la sesión
     * @param receptor      usuario a quien va dirigido la recomendación
     * @param comments      comentarios de la recomendación
     *
     * @param data
     * @return              objeto request
     */
    public static Request getRFRecommendation(String sessionName, Seeker receptor, String comments, QuerySource data) {
        Map<Object, Object> hash = new HashMap<>(5);
        hash.put(OPERATION, RECOMMEND_SINGLE_RESULTS);
        hash.put(SESSION_NAME, sessionName);
        hash.put(MEMBER_RECEPTOR, receptor);
        hash.put(COMMENT, comments);
        hash.put(QUERY_SOURCE, data);
        return new Request(hash);
    }

    /**
     * Devuelve un objeto Request para recomendar a un grupo de miembros
     * de su sesión
     *
     * @param sessionName   nombre de la sesión
     * @param receptors     miembros a quienes va dirigido la recomendación
     * @param comments      comentarios de la recomendación
     *
     * @param data
     * @return              objeto request
     */
    public static Request getRFRecommendation(String sessionName, List<Seeker> receptors, String comments, QuerySource data) {
        Map<Object, Object> hash = new HashMap<>(5);
        hash.put(OPERATION, RECOMMEND_GROUP_RESULTS);
        hash.put(SESSION_NAME, sessionName);
        hash.put(SEEKERS_RECEPTORS, receptors);
        hash.put(COMMENT, comments);
        hash.put(QUERY_SOURCE, data);

        return new Request(hash);
    }

    /**
     * Devuelve un objeto Request para recomendar una selección de los resultados
     * de la búsqueda a toda su sesión
     *
     * @param sessionName   nombre de la sesión
     * @param docs
     * @param comments      comentarios de la recomendación
     * @param data
     *
     * @return              objeto request
     */
    public static Request getRFRecommendation(String sessionName, Documents docs, String comments, QuerySource data) {
        Map<Object, Object> hash = new HashMap<>(5);
        hash.put(OPERATION, RECOMMEND_SESSION_SELECTION_RESULTS);
        hash.put(SESSION_NAME, sessionName);
        hash.put(DOCUMENTS, docs);
        hash.put(COMMENT, comments);
        hash.put(QUERY_SOURCE, data);

        return new Request(hash);
    }

    /**
     * Devuelve un objeto Request para recomendar una selección de los resultados
     * de la búsqueda a un usuario de su sesión
     *
     * @param sessionName   nombre de la sesión
     * @param receptor      usuario a quien va dirigida la recomendación
     * @param docs
     * @param comments      comentarios de la recomendación
     * @param data
     *
     * @return              objeto request
     */
    public static Request getRFRecommendation(String sessionName, Seeker receptor, Documents docs, String comments, QuerySource data) {
        Map<Object, Object> hash = new HashMap<>(6);
        hash.put(OPERATION, RECOMMEND_SINGLE_SELECTION_RESULTS);
        hash.put(SESSION_NAME, sessionName);
        hash.put(MEMBER_RECEPTOR, receptor);
        hash.put(DOCUMENTS, docs);
        hash.put(COMMENT, comments);
        hash.put(QUERY_SOURCE, data);

        return new Request(hash);
    }

    /**
     * Devuelve un objeto Request para recomendar una selección de los resultados
     * de la búsqueda a un grupo de miembros de su sesión
     *
     * @param sessionName   nombre de la sesión
     * @param receptors     a quienes va dirigida la recomendación
     * @param docs 
     * @param data
     * @param comments      comentarios de la recomendación
     *
     * @return              objeto request
     */
    public static Request getRFRecommendation(String sessionName, List<Seeker> receptors, Documents docs, String comments, QuerySource data) {

        Map<Object, Object> hash = new HashMap<>(6);
        hash.put(OPERATION, RECOMMEND_GROUP_SELECTION_RESULTS);
        hash.put(SESSION_NAME, sessionName);
        hash.put(SEEKERS_RECEPTORS, receptors);
        hash.put(DOCUMENTS, docs);
        hash.put(COMMENT, comments);
        hash.put(QUERY_SOURCE, data);

        return new Request(hash);
    }

    /**
     * Devuelve un objeto Request para recomendar todos los resultados de la búsqueda
     * a un usuario de otra sesión de búsqueda
     *
     * @param sessionName     nombre de la sesión del emisor
     * @param sessionNameRtr  nombre de la sesión del receptor
     * @param receptor        usuario a quien va dirigida la recomendación
     * @param comments        comentarios de la recomendación
     *
     * @param data
     * @return                objeto request
     */
    public static Request getRFRecommendation(String sessionName, String sessionNameRtr, Seeker receptor, String comments, QuerySource data) {
        Map<Object, Object> hash = new HashMap<>(6);
        hash.put(OPERATION, RECOMMEND_ANOTHER_SESSION_SINGLE_RESULTS);
        hash.put(SESSION_NAME, sessionName);
        hash.put(OTHER_SESSION_NAME, sessionNameRtr);
        hash.put(MEMBER_RECEPTOR, receptor);
        hash.put(COMMENT, comments);
        hash.put(QUERY_SOURCE, data);

        return new Request(hash);
    }

    /**
     * Devuelve un objeto Request para recomendar una selección de los resultados
     * de la búsqueda a un usuario de otra sesión de búsqueda
     *
     * @param sessionName     nombre de la sesión del emisor
     * @param sessionNameRtr  nombre de la sesión del receptor
     * @param receptor        usuario a quien va dirigida la recomendación
     * @param docs
     * @param comments        comentarios de la recomendación
     *
     * @param data
     * @return                objeto request
     */
    public static Request getRFRecommendation(String sessionName, String sessionNameRtr, Seeker receptor, Documents docs, String comments, QuerySource data) {

        Map<Object, Object> hash = new HashMap<>(7);
        hash.put(OPERATION, RECOMMEND_ANOTHER_SESSION_SINGLE_SELECTION_RESULTS);
        hash.put(SESSION_NAME, sessionName);
        hash.put(OTHER_SESSION_NAME, sessionNameRtr);
        hash.put(MEMBER_RECEPTOR, receptor);
        hash.put(DOCUMENTS, docs);
        hash.put(COMMENT, comments);
        hash.put(QUERY_SOURCE, data);

        return new Request(hash);
    }

    /**
     * Devuelve un objeto Request para recomendar todos los resultados de la búsqueda
     * a un grupo de miembros de otra sesión de búsqueda
     *
     * @param sessionName     nombre de la sesión del emisor
     * @param sessionNameRtrs nombre de la sesión de los receptores
     * @param receptors       miembros a quienes va dirigida la recomendación
     * @param comments        comentarios de la recomendación
     *
     * @param data
     * @return                objeto request
     */
    public static Request getRFRecommendation(String sessionName, String sessionNameRtrs, List<Seeker> receptors, String comments, QuerySource data) {
        Map<Object, Object> hash = new HashMap<>(6);
        hash.put(OPERATION, RECOMMEND_ANOTHER_SESSION_GROUP_RESULTS);
        hash.put(SESSION_NAME, sessionName);
        hash.put(OTHER_SESSION_NAME, sessionNameRtrs);
        hash.put(SEEKERS_RECEPTORS, receptors);
        hash.put(COMMENT, comments);
        hash.put(QUERY_SOURCE, data);

        return new Request(hash);
    }

    /**
     * Devuelve un objeto Request para recomendar una selección de los resultados
     * de la búsqueda a un grupo de miembros de otra sesión de búsqueda
     *
     * @param sessionName     nombre de la sesión del emisor
     * @param sessionNameRtrs nombre de la sesión de los receptores
     * @param receptors       miembros a quienes va dirigida la recomendación
     * @param docs
     * @param comments        comentarios de la recomendación
     * @param data
     *
     * @return                objeto request
     */
    public static Request getRFRecommendation(String sessionName, String sessionNameRtrs, List<Seeker> receptors, Documents docs, String comments, QuerySource data) {
        Map<Object, Object> hash = new HashMap<>(7);
        hash.put(OPERATION, RECOMMEND_ANOTHER_SESSION_GROUP_SELECTION_RESULTS);
        hash.put(SESSION_NAME, sessionName);
        hash.put(OTHER_SESSION_NAME, sessionNameRtrs);
        hash.put(SEEKERS_RECEPTORS, receptors);
        hash.put(DOCUMENTS, docs);
        hash.put(COMMENT, comments);
        hash.put(QUERY_SOURCE, data);

        return new Request(hash);
    }

    /**
     * Devuelve un objeto Request para notificar la acción de activar la sugerencias
     * de términos relevantes.
     *
     * @param sessionName nombre de la sesión
     * @param event      true, para activar PQT, false en caso contrario
     * @return            objeto request
     */
    public static Request getRFCollabTermsSuggest(String sessionName, int event) {
        Map<Object, Object> hash = new HashMap<>(3);
        hash.put(OPERATION, COLLABORATIVE_TERMS_SUGGEST);
        hash.put(SESSION_NAME, sessionName);
        hash.put(DISTRIBUTED_EVENT, event);

        return new Request(hash);
    }
}
