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
import drakkar.oar.ScorePQT;
import static drakkar.oar.util.KeyTransaction.*;
import static drakkar.oar.util.SeekerAction.*;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Esta clase es permite construir objetos Request para invocar las diferentes
 * técnicas de awareness soportados por DrakkarKeel para la mensajería
 */
public class RequestSyncAwarenessFactory implements Serializable{
    private static final long serialVersionUID = 80000000000011L;

    /**
     * Devuelve un objeto Request para notificar el cambio de la consulta de un seeker,
     * al resto de su sesión de colaboración. Este métdo es utilizado putting query-terms together (PQT).
     *
     * @param sessionName   nombre de la sesión
     * @param query         términos de la consulta
     * @param statistics    relación de las estadísticas de los términos de la consulta
     * @return              objeto request
     *
     */
    public static Request getRFQueryChange(String sessionName, String query, Map<String, ScorePQT> statistics) {
        Map<Object, Object> hash = new HashMap<>(4);
        hash.put(OPERATION, SEND_ACTION_QUERY_CHANGE);
        hash.put(SESSION_NAME, sessionName);
        hash.put(QUERY, query);
        hash.put(SCORE_PQT, statistics);

        return new Request(hash);
    }

    /**
     * Devuelve un objeto Request para notificar la acción de confección de la consulta de un seeker,
     * al resto de su sesión de colaboración. Este métdo es utilizado putting query-terms together (PQT).
     *
     * @param sessionName   nombre de la sesión
     * @param flag          true si encuentra escribiendo la consulta, false en caso contrario
     * @return              objeto request
     */
    public static Request getRFQueryTyped(String sessionName, boolean flag) {
        Map<Object, Object> hash = new HashMap<>(4);
        hash.put(OPERATION, SEND_ACTION_QUERY_TYPED);
        hash.put(SESSION_NAME, sessionName);
        hash.put(QUERY_TYPED, flag);

        return new Request(hash);
    }

    /**
     * Devuelve un objeto Request para notificar la acción de aceptar un término de consulta
     * especificado por un seeker, a toda la sesión de colaboración. Este métdo es utilizado
     * en putting query-terms together (PQT).
     *
     * @param sessionName nombre de la sesión
     * @param term        término de la consulta
     * @param user        usuario que propone el término
     *
     * @param aceptance   SeekerAction.TERM_AGREE para aceptar el término, SeekerAction.TERM_DISAGREE en caso contrario
     * @return            objeto request
     */
    public static Request getRFQueryTermAcceptance(String sessionName, String term, String user, int aceptance) {
        Map<Object, Object> hash = new HashMap<>(5);
        hash.put(OPERATION, SEND_ACTION_TERM_ACCEPTANCE);
        hash.put(DISTRIBUTED_EVENT, aceptance);
        hash.put(SESSION_NAME, sessionName);
        hash.put(QUERY_TERM, term);
        hash.put(SEEKER_NICKNAME, user);

        return new Request(hash);
    }

     /**
     * Devuelve un objeto Request para notificar la acción de creación de consulta de búsqueda
     * de forma colaborativa (PQT).
     *
     * @param sessionName nombre de la sesión
     * @param event      true, para activar PQT, false en caso contrario
     * @return            objeto request
     */
    public static Request getRFPuttingQueryTermsTogether(String sessionName, int event) {
        Map<Object, Object> hash = new HashMap<>(3);
        hash.put(OPERATION, PUTTING_QUERY_TERMS_TOGETHER);       
        hash.put(SESSION_NAME, sessionName);
        hash.put(DISTRIBUTED_EVENT, event);

        return new Request(hash);
    }

   
}
