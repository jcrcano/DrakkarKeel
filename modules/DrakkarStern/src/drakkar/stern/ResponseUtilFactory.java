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
import static drakkar.oar.util.KeyTransaction.*;
import static drakkar.oar.util.NotifyAction.*;
import java.util.HashMap;
import java.util.Map;

public class ResponseUtilFactory {

    /**
     * Devuelve un objeto response para notificar al seeker un mesaje de texto con
     * el estado final de una operación invocada.
     *
     * @param messageType   Posibles tipos de mensaje INFORMATION_MESSAGE, ERROR_MESSAGE,
     *                      ambos valores definidos en la interfaz Assignable.
     * @param message       descripción de la operación realizada ó causa del error.
     *
     * @return
     */
    public static Response getResponse(int messageType, String message) {
        Map<Object, Object> hash = new HashMap<>(3);
        hash.put(OPERATION, NOTIFY_COMMIT_TRANSACTION);
        hash.put(MESSAGE_TYPE, messageType);
        hash.put(MESSAGE, message);
        Response rsp = new Response(hash);

        return rsp;
    }
}
