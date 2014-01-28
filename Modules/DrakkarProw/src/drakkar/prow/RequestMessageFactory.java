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
 * Esta clase es permite construir objetos Request para efectuar los diferentes
 * métodos soportados por DRakkarKeel para la mensajería
 */
public class RequestMessageFactory implements Serializable{
    private static final long serialVersionUID = 80000000000007L;

    /**
     * Devuelve un objeto Request para enviar un  mensaje a toda la sesión
     *
     * @param sessionName   nombre de la sesión
     * @param message       contenido del mensaje
     *
     * @return              objeto request
     *
     */
    public static Request create(String sessionName, String message) {
        Map<Object, Object> hash = new HashMap<>(3);
        hash.put(OPERATION, SEND_SESSION_MESSAGE);
        hash.put(SESSION_NAME, sessionName);
        hash.put(MESSAGE, message);

        return new Request(hash);
    }

    /**
     * Devuelve un objeto Request para enviar un mensaje a un usuario determinado
     *
     * @param sessionName   nombre de la sesión
     * @param receptor      a quien va destinado el mensaje
     * @param message       contenido del mensaje
     *
     * @return              objeto request
     */
    public static Request create(String sessionName, Seeker receptor, String message) {
        Map<Object, Object> hash = new HashMap<>(4);
        hash.put(OPERATION, SEND_SINGLE_MESSAGE);
        hash.put(SESSION_NAME, sessionName);
        hash.put(MEMBER_RECEPTOR, receptor);
        hash.put(MESSAGE, message);

        return new Request(hash);
    }

    /**
     * Devuelve un objeto Request para enviar un mensaje a un grupo de usuarios
     *
     * @param sessionName   nombre de la sesión
     * @param receptors     a quienes va destinado el mensaje
     * @param message       contenido del mensaje
     * 
     * @return              objeto request
     */
    public static Request create(String sessionName, List<Seeker> receptors, String message) {
        Map<Object, Object> hash = new HashMap<>(4);
        hash.put(OPERATION, SEND_GROUP_MESSAGE);
        hash.put(SESSION_NAME, sessionName);
        hash.put(SEEKERS_RECEPTORS, receptors);
        hash.put(MESSAGE, message);

        return new Request(hash);
    }
}
