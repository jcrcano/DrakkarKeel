/*
 * DrakkarKeel - An Enterprise Collaborative Search Platform
 *
 * The contents of this file are subject under the terms described in the
 * DRAKKARKEEL_LICENSE file included in this distribution; you may not use this
 * file except in compliance with the License. 
 *
 * 2013-2014 DrakkarKeel Platform.
 */
package drakkar.stern.servant.service;

import drakkar.oar.Seeker;
import drakkar.oar.exception.SeekerException;
import drakkar.oar.exception.SessionException;
import java.io.IOException;
import java.util.List;

/**
 * The <code>Sendable</code> class is....
 *Esta interfaz declara todos los métodos de mensajería soportados por el
 * framework DrakkarKeel, además de otros para ofrecer información sobre este servicio
 */
public interface Sendable {

    /**
     * Envía un mensaje a toda la sesión del usuario
     *
     * @param sessionName   nombre de la sesión
     * @param emitter       usuario emisor del mensaje
     * @param message       contenido del mensaje
     *
     * @throws SessionException si la sesión no se encuentra registrada
     *                                  en el servidor
     * @throws SeekerException    si el usuario emisor no se encuentra registrado
     *                                  en la sesión
     * @throws IOException si ocurre alguna excepción durante el proceso de
     *                     serialización del objeto Response.
     */
    public void sendMessage(String sessionName, Seeker emitter, String message) throws SessionException, SeekerException, IOException;

    /**
     * Envía mensaje un usuario de la sesión
     *
     * @param sessionName    nombre de la sesión
     * @param emitter        usuario emisor del mensaje
     * @param receptor       usuario a quien va destinado el mensaje
     * @param message        contenido del mensaje
     *
     * @throws SessionException si la sesión no se encuentra registrada en el servidor
     * @throws SeekerException    si el usuario emisor ó receptor no se encuentra
     *                          registrado en la sesión
     * @throws IOException   si ocurre alguna excepción durante el proceso de
     *                       serialización del objeto Response
     */
    public void sendMessage(String sessionName, Seeker emitter, Seeker receptor, String message) throws SessionException, SeekerException, IOException;

    /**
     * Envía un mensaje a un grupo de usuarios de la sesión
     *
     * @param sessionName    nombre de la sesión
     * @param emitter        usuario que emite el mensaje
     * @param receptors      usuarios a quienes va destinado el mensaje
     * @param message        contenido del mensaje
     * @throws SessionException   si la sesión no se encuentra registrada
     *                                    en el servidor
     * @throws SeekerException    si el usuario emisor ó algún receptor
     *                                    no se encuentra registrado en la sesión
     * @throws IOException   si ocurre alguna excepción durante el proceso de
     *                       serialización del objeto Response
     */
    public void sendMessage(String sessionName, Seeker emitter, List<Seeker> receptors, String message) throws SessionException, SeekerException, IOException;

    /**
     * Devuelve la total de mensajes enviados por usuarios de todas las sesiones
     * activas en el servidor
     *
     * @return  total de mensajes
     */
    public long getMessagesCount();

    /**
     * Devuelve el total de mensajes enviandos por usuarios de la sesión especificada
     *
     * @param sessionName    nombre de la sesión
     *
     * @return total de mensajes
     *
     * @throws SessionException   si la sesión no se encuentra registrada
     *                                    en el servidor
     */
    public long getMessagesCount(String sessionName) throws SessionException;

    /**
     * Devuelve el total de mensajes enviandos por un usuario de la sesión especificada
     *
     * @param sessionName nombre de la sesión
     * @param seeker      usuario
     *
     * @return total de mensajes
     *
     * @throws SessionException si la sesión no se encuentra registrada
     *                                  en el servidor
     * @throws SeekerException    si el usuario del que solicita el total
     *                                  mensajes no está registrado en la sesión
     */
    public long getMessagesCount(String sessionName, Seeker seeker) throws SessionException, SeekerException;
}
