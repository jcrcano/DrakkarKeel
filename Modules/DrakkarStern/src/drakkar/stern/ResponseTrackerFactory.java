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
import drakkar.oar.SeekerQuery;
import static drakkar.oar.util.KeySession.*;
import static drakkar.oar.util.KeyTransaction.*;
import static drakkar.oar.util.NotifyAction.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResponseTrackerFactory {

    /**
     * Devuelve un objeto response para notificar a un seeker los historiales de
     * consultas efectuadas en la sesión.
     *
     * @param sessionName nombre de la sesión colaborativa
     * @param operation acción a notificar
     *
     * @return objeto response
     */
    public static Response getResponse(String sessionName, SeekerQuery seekerQuery) {
        Map<Object, Object> table = new HashMap<>(4);
        table.put(OPERATION, NOTIFY_ACTION_TRACK);
        table.put(SESSION_NAME, sessionName);
        table.put(ACTION, COLLAB_SESSION_TRACK);
        table.put(SESSION_DATA, seekerQuery);

        Response response = new Response(table);

        return response;

    }

    /**
     * Devuelve un objeto response para notificar a un seeker las operaciones
     * siguientes:
     *
     * - Historiales de búsqueda (COLLAB_SEARCH_TRACK) - Historiales de
     * recomendaciones (COLLAB_RECOMMENDATION_TRACK)
     *
     * @param sessionName nombre de la sesión colaborativa
     * @param action acción a notificar
     *
     * @return objeto response
     */
    public static Response getResponse(String sessionName, int action, List<?> values) {

        Map<Object, Object> table = new HashMap<>(4);
        table.put(SESSION_NAME, sessionName);

        switch (action) {
            case COLLAB_SEARCH_TRACK:
                table.put(OPERATION, NOTIFY_ACTION_TRACK);
                table.put(ACTION, COLLAB_SEARCH_TRACK);
                table.put(SEARCH_DATA, values);
                break;

            case COLLAB_RECOMMENDATION_TRACK:
                table.put(OPERATION, NOTIFY_ACTION_TRACK);
                table.put(ACTION, COLLAB_RECOMMENDATION_TRACK);
                table.put(RECOMMEND_DATA, values);
                break;

        }
        Response response = new Response(table);
        return response;

    }
}
