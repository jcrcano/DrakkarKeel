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
import drakkar.oar.TermSuggest;
import static drakkar.oar.util.KeyTransaction.*;
import static drakkar.oar.util.NotifyAction.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResponseSuggestFactory {

    /**
     * Devuelve un objeto response para notificar al seeker un mesaje de texto con
     * el estado final de una operación invocada.
     *
     * @param sessionName
     * @param query 
     * @param terms
     * @return
     */
    public static Response getResponse(String sessionName, String query, List<TermSuggest> terms) {
        Map<Object, Object> hash = new HashMap<>(3);
        hash.put(OPERATION, NOTIFY_QUERY_TERMS_SUGGEST);
        hash.put(SESSION_NAME, sessionName);
        hash.put(QUERY, query);
        hash.put(QUERY_TERMS_SUGGEST, terms);

        Response rsp = new Response(hash);

        return rsp;
    }
}
