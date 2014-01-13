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
import drakkar.oar.ResultSetMetaData;
import drakkar.oar.Seeker;
import static drakkar.oar.util.KeyTransaction.*;
import static drakkar.oar.util.NotifyAction.*;
import static drakkar.oar.util.SeekerAction.*;
import java.util.HashMap;
import java.util.Map;

public class ResponseSearchFactory {

    /**
     * Devuelve un objeto response para notificar los resultados de una búsqueda
     * colaborativa.
     *
     * @param emitter       seeker que realiza la búsqueda
     * @param searchResults resultados de la búsqueda
     *
     *  
     * @return objeto response
     */
    public static Response getResponse(Seeker emitter, ResultSetMetaData searchResults) {
        Map<Object, Object> hash = new HashMap<>();
        hash.put(OPERATION, NOTIFY_SEARCH_RESULTS);
        hash.put(SEEKER_EMITTER, emitter);
        hash.put(SEARCH_TYPE, COLLAB_SEARCH);
        hash.put(SEARCH_RESULTS, searchResults);
       
        return new Response(hash);
    }

    /**
     * Devuelve un objeto response para notificar los resultados de una búsqueda
     * individual ó notificar al ejecutor de una búsqueda colaborativa que no
     * se encontraron resultados para la consulta específicada.
     * 
     * @param searchType    tipo de búsqueda (INDIVIDUAL_SEARCH)
     * @param isEmpty       true si no se encontraron resultados, false en caso contrario
     * @param searchResults resultados de la búsqueda
     *
     * @return objeto response
     */
    public static Response getResponse(int searchType, boolean isEmpty, ResultSetMetaData searchResults) {
        Map<Object, Object> hash = new HashMap<>();
        hash.put(OPERATION, NOTIFY_SEARCH_RESULTS);
        hash.put(IS_EMPTY, isEmpty);
        hash.put(SEARCH_TYPE, searchType);
        hash.put(SEARCH_RESULTS, searchResults);

        return new Response(hash);
    }
}
