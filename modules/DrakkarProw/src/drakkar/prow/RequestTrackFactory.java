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
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Esta clase permite construir objetos Request para efectuar las diferentes
 * operaciones soportados por DrakkarKeel referente a las búsquedas en historiales
 */
public class RequestTrackFactory implements Serializable{
    private static final long serialVersionUID = 80000000000012L;

    public RequestTrackFactory() {
    }

    /*********************TRACK RECOMMENDATION********************************/
    /**
     * Devuelve un objeto Request para solicitar el historial de recomendaciones a
     * partir de los parámetros entrados
     *
     * @param sessionName  nombre de la sesión
     *
     * @return  objeto Request
     */
    public static Request getRFRecommendationTrack(String sessionName) {
        Map<Object, Object> hash = new HashMap<>(3);
        hash.put(OPERATION, GET_RECOMMENDATION_TRACK);
        hash.put(SESSION_NAME, sessionName);
        hash.put(TRACK_FILTER, SESSION_TRACK);

        return new Request(hash);
    }

    /**
     * Devuelve un objeto Request para solicitar el historial de recomendaciones a
     * partir de los parámetros entrados
     *
     * @param sessionName  nombre de la sesión
     * @param seeker       usuario que realizó la búsqueda
     *
     *
     * @return  objeto Request
     */
    public static Request getRFRecommendationTrack(String sessionName, Seeker seeker) {
        Map<Object, Object> hash = new HashMap<>(4);
        hash.put(OPERATION, GET_RECOMMENDATION_TRACK);
        hash.put(SESSION_NAME, sessionName);
        hash.put(SEEKER, seeker);
        hash.put(TRACK_FILTER, SEEKER_TRACK);

        return new Request(hash);
    }

    /**
     * Devuelve un objeto Request para solicitar el historial de recomendaciones a
     * partir de los parámetros entrados
     *
     * @param sessionName  nombre de la sesión
     * @param date         fecha en que se realizó la búsqueda
     *
     * @return  objeto Request
     */
    public static Request getRFRecommendationTrack(String sessionName, Date date) {
        Map<Object, Object> hash = new HashMap<>(4);
        hash.put(OPERATION, GET_RECOMMENDATION_TRACK);
        hash.put(SESSION_NAME, sessionName);
        hash.put(TRACK_FILTER, DATE_TRACK);
        hash.put(DATE, date);

        return new Request(hash);
    }

    /**
     * Devuelve un objeto Request para solicitar el historial de recomendaciones a
     * partir de los parámetros entrados
     *
     * @param sessionName  nombre de la sesión
     * @param query        consulta de la búsqueda     
     *
     *
     * @return  objeto Request
     */
    public static Request getRFRecommendationTrack(String sessionName, String query) {
        Map<Object, Object> hash = new HashMap<>(4);
        hash.put(OPERATION, GET_RECOMMENDATION_TRACK);
        hash.put(SESSION_NAME, sessionName);
        hash.put(TRACK_FILTER, QUERY_TRACK);
        hash.put(QUERY, query);

        return new Request(hash);
    }

    /**
     * Devuelve un objeto Request para solicitar el historial de recomendaciones a
     * partir de los parámetros entrados
     *
     * @param sessionName  nombre de la sesión
     * @param seeker       usuario que realizó la búsqueda     
     * @param query        consulta de la búsqueda     
     *
     *
     * @return  objeto Request
     */
    public static Request getRFRecommendationTrack(String sessionName, Seeker seeker, String query) {
        Map<Object, Object> hash = new HashMap<>(5);
        hash.put(OPERATION, GET_RECOMMENDATION_TRACK);
        hash.put(SESSION_NAME, sessionName);
        hash.put(TRACK_FILTER, QUERY_SEEKER_TRACK);
        hash.put(SEEKER, seeker);
        hash.put(QUERY, query);

        return new Request(hash);
    }

    /**
     * Devuelve un objeto Request para solicitar el historial de recomendaciones a
     * partir de los parámetros entrados
     *
     * @param sessionName  nombre de la sesión    
     * @param date         fecha en que se realizó la búsqueda
     * @param query        consulta de la búsqueda     
     *
     *
     * @return  objeto Request
     */
    public static Request getRFRecommendationTrack(String sessionName, String query, Date date) {
        Map<Object, Object> hash = new HashMap<>(5);
        hash.put(OPERATION, GET_RECOMMENDATION_TRACK);
        hash.put(SESSION_NAME, sessionName);
        hash.put(TRACK_FILTER, QUERY_DATE_TRACK);
        hash.put(DATE, date);
        hash.put(QUERY, query);

        return new Request(hash);
    }

    /**
     * Devuelve un objeto Request para solicitar el historial de recomendaciones a
     * partir de los parámetros entrados
     *
     * @param sessionName  nombre de la sesión
     * @param seeker       usuario que realizó la búsqueda
     * @param date         fecha en que se realizó la búsqueda
     *
     *
     * @return  objeto Request
     */
    public static Request getRFRecommendationTrack(String sessionName, Seeker seeker, Date date) {
        Map<Object, Object> hash = new HashMap<>(5);
        hash.put(OPERATION, GET_RECOMMENDATION_TRACK);
        hash.put(SESSION_NAME, sessionName);
        hash.put(TRACK_FILTER, SEEKER_DATE_TRACK);
        hash.put(SEEKER, seeker);
        hash.put(DATE, date);

        return new Request(hash);
    }

    /**
     * Devuelve un objeto Request para solicitar el historial de recomendaciones a
     * partir de los parámetros entrados
     *
     * @param sessionName  nombre de la sesión
     * @param seeker       usuario que realizó la búsqueda
     * @param date         fecha en que se realizó la búsqueda
     * @param query        consulta de la búsqueda    
     *
     *
     * @return  objeto Request
     */
    public static Request getRFRecommendationTrack(String sessionName, Seeker seeker, Date date, String query) {
        Map<Object, Object> hash = new HashMap<>(6);
        hash.put(OPERATION, GET_RECOMMENDATION_TRACK);
        hash.put(SESSION_NAME, sessionName);
        hash.put(TRACK_FILTER, QUERY_SEEKER_DATE_TRACK);
        hash.put(SEEKER, seeker);
        hash.put(DATE, date);
        hash.put(QUERY, query);

        return new Request(hash);
    }

    /*********************TRACK SEARCH********************************/
    /**
     * Devuelve un objeto Request para solicitar el historial de búsquedas a
     * partir de los parámetros entrados
     *
     * @param sessionName  nombre de la sesión
     * @param group        clasificador de documentos
     *
     * <b>Nota:</b>
     * Las posibles constantes para clasificar los documentos están definidas en
     * la clase SeekerAction del paquete drakkar.oar.util, ellas son:
     * <br>
     * <br>
     * <tt>- TRACK_SEARCH_REVIEWED/Revisados</tt><br>
     * <tt>- TRACK_SEARCH_SELECTED_RELEVANT/Seleccionados Relevantes</tt>
     * <tt>- TRACK_SEARCH_ALL/Todos</tt>
     * <br>
     * <br>
     *
     * @return  objeto Request
     */
    public static Request getRFSearchTrack(String sessionName, int group) {
        Map<Object, Object> hash = new HashMap<>(4);
        hash.put(OPERATION, GET_SEARCH_TRACK);
        hash.put(SESSION_NAME, sessionName);
        hash.put(TRACK_FILTER, SESSION_TRACK);
        hash.put(DOCUMENT_GROUP, group);

        return new Request(hash);
    }

    /**
     * Devuelve un objeto Request para solicitar el historial de búsquedas a
     * partir de los parámetros entrados
     *
     * @param sessionName  nombre de la sesión
     * @param seeker       usuario que realizó la búsqueda
     * @param group        clasificador de documentos
     *
     * <b>Nota:</b>
     * Las posibles constantes para clasificar los documentos están definidas en
     * la clase SeekerAction del paquete drakkar.oar.util, ellas son:
     * <br>
     * <br>
     * <tt>- TRACK_SEARCH_REVIEWED/Revisados</tt><br>
     * <tt>- TRACK_SEARCH_SELECTED_RELEVANT/Seleccionados Relevantes</tt>
     * <tt>- TRACK_SEARCH_ALL/Todos</tt>
     * <br>
     * <br>
     *
     * @return  objeto Request
     */
    public static Request getRFSearchTrack(String sessionName, Seeker seeker, int group) {
        Map<Object, Object> hash = new HashMap<>(5);
        hash.put(OPERATION, GET_SEARCH_TRACK);
        hash.put(SESSION_NAME, sessionName);
        hash.put(SEEKER, seeker);
        hash.put(TRACK_FILTER, SEEKER_TRACK);
        hash.put(DOCUMENT_GROUP, group);

        return new Request(hash);
    }

    /**
     * Devuelve un objeto Request para solicitar el historial de búsquedas a
     * partir de los parámetros entrados
     *
     * @param sessionName  nombre de la sesión    
     * @param date         fecha en que se realizó la búsqueda    
     * @param group        clasificador de documentos
     *
     * <b>Nota:</b>
     * Las posibles constantes para clasificar los documentos están definidas en
     * la clase SeekerAction del paquete drakkar.oar.util, ellas son:
     * <br>
     * <br>
     * <tt>- TRACK_SEARCH_REVIEWED/Revisados</tt><br>
     * <tt>- TRACK_SEARCH_SELECTED_RELEVANT/Seleccionados Relevantes</tt>
     * <tt>- TRACK_SEARCH_ALL/Todos</tt>
     * <br>
     * <br>
     *
     * @return  objeto Request
     */
    public static Request getRFSearchTrack(String sessionName, Date date, int group) {
        Map<Object, Object> hash = new HashMap<>(5);
        hash.put(OPERATION, GET_SEARCH_TRACK);
        hash.put(SESSION_NAME, sessionName);
        hash.put(TRACK_FILTER, DATE_TRACK);
        hash.put(DATE, date);
        hash.put(DOCUMENT_GROUP, group);

        return new Request(hash);
    }

    /**
     * Devuelve un objeto Request para solicitar el historial de búsquedas a
     * partir de los parámetros entrados
     *
     * @param sessionName  nombre de la sesión
     * @param query        consulta de la búsqueda
     * @param group        clasificador de documentos
     *
     * <b>Nota:</b>
     * Las posibles constantes para clasificar los documentos están definidas en
     * la clase SeekerAction del paquete drakkar.oar.util, ellas son:
     * <br>
     * <br>
     * <tt>- TRACK_SEARCH_REVIEWED/Revisados</tt><br>
     * <tt>- TRACK_SEARCH_SELECTED_RELEVANT/Seleccionados Relevantes</tt>
     * <tt>- TRACK_SEARCH_ALL/Todos</tt>
     * <br>
     * <br>
     *
     * @return  objeto Request
     */
    public static Request getRFSearchTrack(String sessionName, String query, int group) {
        Map<Object, Object> hash = new HashMap<>(5);
        hash.put(OPERATION, GET_SEARCH_TRACK);
        hash.put(SESSION_NAME, sessionName);
        hash.put(TRACK_FILTER, QUERY_TRACK);
        hash.put(QUERY, query);
        hash.put(DOCUMENT_GROUP, group);

        return new Request(hash);
    }

    /**
     * Devuelve un objeto Request para solicitar el historial de búsquedas a
     * partir de los parámetros entrados
     *
     * @param sessionName  nombre de la sesión
     * @param seeker       usuario que realizó la búsqueda  
     * @param query        consulta de la búsqueda
     * @param group        clasificador de documentos
     *
     * <b>Nota:</b>
     * Las posibles constantes para clasificar los documentos están definidas en
     * la clase SeekerAction del paquete drakkar.oar.util, ellas son:
     * <br>
     * <br>
     * <tt>- TRACK_SEARCH_REVIEWED/Revisados</tt><br>
     * <tt>- TRACK_SEARCH_SELECTED_RELEVANT/Seleccionados Relevantes</tt>
     * <tt>- TRACK_SEARCH_ALL/Todos</tt>
     * <br>
     * <br>
     *
     * @return  objeto Request
     */
    public static Request getRFSearchTrack(String sessionName, Seeker seeker, String query, int group) {
        Map<Object, Object> hash = new HashMap<>(6);
        hash.put(OPERATION, GET_SEARCH_TRACK);
        hash.put(SESSION_NAME, sessionName);
        hash.put(TRACK_FILTER, QUERY_SEEKER_TRACK);
        hash.put(SEEKER, seeker);
        hash.put(QUERY, query);
        hash.put(DOCUMENT_GROUP, group);

        return new Request(hash);
    }

    /**
     * Devuelve un objeto Request para solicitar el historial de búsquedas a
     * partir de los parámetros entrados
     *
     * @param sessionName  nombre de la sesión    
     * @param date         fecha en que se realizó la búsqueda
     * @param query        consulta de la búsqueda
     * @param group        clasificador de documentos
     *
     * <b>Nota:</b>
     * Las posibles constantes para clasificar los documentos están definidas en
     * la clase SeekerAction del paquete drakkar.oar.util, ellas son:
     * <br>
     * <br>
     * <tt>- TRACK_SEARCH_REVIEWED/Revisados</tt><br>
     * <tt>- TRACK_SEARCH_SELECTED_RELEVANT/Seleccionados Relevantes</tt>
     * <tt>- TRACK_SEARCH_ALL/Todos</tt>
     * <br>
     * <br>
     *
     * @return  objeto Request
     */
    public static Request getRFSearchTrack(String sessionName, String query, Date date, int group) {
        Map<Object, Object> hash = new HashMap<>(6);
        hash.put(OPERATION, GET_SEARCH_TRACK);
        hash.put(SESSION_NAME, sessionName);
        hash.put(TRACK_FILTER, QUERY_DATE_TRACK);
        hash.put(DATE, date);
        hash.put(QUERY, query);
        hash.put(DOCUMENT_GROUP, group);

        return new Request(hash);
    }

    /**
     * Devuelve un objeto Request para solicitar el historial de búsquedas a
     * partir de los parámetros entrados
     *
     * @param sessionName  nombre de la sesión
     * @param seeker       usuario que realizó la búsqueda
     * @param date         fecha en que se realizó la búsqueda   
     * @param group        clasificador de documentos
     *
     * <b>Nota:</b>
     * Las posibles constantes para clasificar los documentos están definidas en
     * la clase SeekerAction del paquete drakkar.oar.util, ellas son:
     * <br>
     * <br>
     * <tt>- TRACK_SEARCH_REVIEWED/Revisados</tt><br>
     * <tt>- TRACK_SEARCH_SELECTED_RELEVANT/Seleccionados Relevantes</tt>
     * <tt>- TRACK_SEARCH_ALL/Todos</tt>
     * <br>
     * <br>
     *
     * @return  objeto Request
     */
    public static Request getRFSearchTrack(String sessionName, Seeker seeker, Date date, int group) {
        Map<Object, Object> hash = new HashMap<>(6);
        hash.put(OPERATION, GET_SEARCH_TRACK);
        hash.put(SESSION_NAME, sessionName);
        hash.put(TRACK_FILTER, SEEKER_DATE_TRACK);
        hash.put(SEEKER, seeker);
        hash.put(DATE, date);
        hash.put(DOCUMENT_GROUP, group);

        return new Request(hash);
    }

    /**
     * Devuelve un objeto Request para solicitar el historial de búsquedas a
     * partir de los parámetros entrados
     * 
     * @param sessionName  nombre de la sesión
     * @param seeker       usuario que realizó la búsqueda
     * @param date         fecha en que se realizó la búsqueda
     * @param query        consulta de la búsqueda
     * @param group        clasificador de documentos
     *
     * <b>Nota:</b>
     * Las posibles constantes para clasificar los documentos están definidas en
     * la clase SeekerAction del paquete drakkar.oar.util, ellas son:
     * <br>
     * <br>
     * <tt>- TRACK_SEARCH_REVIEWED/Revisados</tt><br>
     * <tt>- TRACK_SEARCH_SELECTED_RELEVANT/Seleccionados Relevantes</tt>
     * <tt>- TRACK_SEARCH_ALL/Todos</tt>
     * <br>
     * <br>
     *
     * @return  objeto Request
     */
    public static Request getRFSearchTrack(String sessionName, Seeker seeker, Date date, String query, int group) {
        Map<Object, Object> hash = new HashMap<>(7);
        hash.put(OPERATION, GET_SEARCH_TRACK);
        hash.put(SESSION_NAME, sessionName);
        hash.put(TRACK_FILTER, QUERY_SEEKER_DATE_TRACK);
        hash.put(SEEKER, seeker);
        hash.put(DATE, date);
        hash.put(QUERY, query);
        hash.put(DOCUMENT_GROUP, group);

        return new Request(hash);
    }

//////////////////////
    /**
     * Devuelve un objeto Request para solicitar la lista de consultas efectuadas
     * en una sesión para una fecha de la sesión, por los miembros de la misma.
     *
     * @param sessionName  nombre de la sesión
     * @param date
     * 
     * @return  objeto Request
     */
    public static Request getRFSessionTrack(String sessionName, Date date) {
        Map<Object, Object> hash = new HashMap<>(3);
        hash.put(OPERATION, GET_SESSION_TRACK);
        hash.put(SESSION_NAME, sessionName);
        hash.put(DATE, date);

        return new Request(hash);
    }
}
