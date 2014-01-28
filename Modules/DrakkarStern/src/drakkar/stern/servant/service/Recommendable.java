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

import drakkar.oar.Documents;
import drakkar.oar.QuerySource;
import drakkar.oar.Seeker;
import drakkar.oar.exception.RecommendationException;
import drakkar.oar.exception.SeekerException;
import drakkar.oar.exception.SessionException;
import java.io.IOException;
import java.util.List;

/**
 * The <code>ResponseUtilFactory</code> class is.....
 * Esta interfaz declara los diferentes métodos de recomendación soportados por
 * el framework DrakkarKeel
 */
public interface Recommendable {

    /**
     * Recomendar todos resultados de la búsqueda a todos los usuarios de la sesión
     *
     * @param sessionName   nombre de la sesión del usuario
     * @param emitter       usuario emisor
     * @param comments      comentarios de la recomendación
     *
     * @param data
     * @throws SessionException si la sesión no se encuentra registrada en el servidor
     * @throws SeekerException    si el usuario emisor no se encuentra registrado en la sesión
     * @throws RecommendationException  si el usuario que emite la recomendación
     *                                  no cuenta con resultados de búquedas previas
     *                                  registradas
     * @throws IOException   si ocurre alguna excepción durante el proceso de
     *                       serialización del objeto Response
     */
    public void recommendResults(String sessionName, Seeker emitter, String comments, QuerySource data) throws SessionException, SeekerException, RecommendationException, IOException;

    /**
     * Recomendar todos resultados de la búsqueda a un usuario de la sesión
     *
     * @param sessionName   nombre de la sesión
     * @param emitter       usuario emisor
     * @param receptor      usuario receptor
     * @param comments      comentarios de la recomendación
     * @param data
     *
     * @throws SessionException  si la sesión no se encuentra registrada
     *                                   en el servidor
     * @throws SeekerException     si el usuario emisor ó el receptor no se
     *                                   encuentra registrado en la sesión
     * @throws RecommendationException   si el usuario que emite la recomendación
     *                                   no cuenta con resultados de búquedas previas
     *                                   registradas
     * @throws IOException    si ocurre alguna excepción durante el proceso de
     *                       serialización del objeto Response
     */
    public void recommendResults(String sessionName, Seeker emitter, Seeker receptor, String comments, QuerySource data) throws SessionException, SeekerException, RecommendationException, IOException;

    /**
     * Recomendar todos resultados de la búsqueda a un grupo de usuarios
     * de la sesión
     *
     * @param sessionName   nombre de la sesión
     * @param emitter       usuario que emite la recomendación
     * @param receptors     miembros a quienes va dirigido la recomendación
     * @param comments      comentarios de la recomendación
     * @param data
     *
     * @throws SessionException si la sesión no se encuentra registrada
     *                                  en el servidor
     * @throws SeekerException    si el usuario emisor ó alguno de los receptores
     *                                  no se encuentra registrado en la sesión
     * @throws RecommendationException  si el usuario que emite la recomendación
     *                                  no cuenta con resultados de búquedas previas
     *                                  registradas
     * @throws IOException   si ocurre alguna excepción durante el proceso de
     *                       serialización del objeto Response
     */
    public void recommendResults(String sessionName, Seeker emitter, List<Seeker> receptors, String comments, QuerySource data) throws SessionException, SeekerException, RecommendationException, IOException;

    /**
     * Recomendar una selección de los resultados de la búsqueda, obtenidos por
     * un buscador a toda la sesión
     *
     * @param sessionName   nombre de la sesión
     * @param emitter       usuario emisor
     * @param comments      comentarios de la recomendación
     * @param docs 
     * @param data
     *
     * @throws SessionException si la sesión no se encuentra registrada
     *                                  en el servidor
     * @throws SeekerException    si el usuario emisor no se encuentra
     *                                  registrado en la sesión
     * @throws RecommendationException  si el usuario que emite la recomendación
     *                                  no cuenta con resultados de búquedas previas
     *                                  registradas
     * @throws IOException   si ocurre alguna excepción durante el proceso de
     *                       serialización del objeto Response
     */
    public void recommendResults(String sessionName, Seeker emitter, String comments, Documents docs, QuerySource data) throws SessionException, SeekerException, RecommendationException, IOException;

    /**
     * Recomendar una selección de los resultados de la búsqueda, obtenidos por
     * un buscador a un usuario de la sesión
     *
     * @param sessionName   nombre de la sesión
     * @param emitter       usuario emisor
     * @param receptor      usuario receptor
     * @param docs
     * @param data
     * @param comments      comentarios de la recomendación
     *
     * @throws SessionException si la sesión no se encuentra registrada
     *                                  en el servidor
     * @throws SeekerException    si el usuario emisor ó el receptor no se
     *                                  encuentra registrado en la sesión
     * @throws RecommendationException  si el usuario que emite la recomendación
     *                                  no cuenta con resultados de búquedas previas
     *                                  registradas
     * @throws IOException   si ocurre alguna excepción durante el proceso de
     *                       serialización del objeto Response
     */
    public void recommendResults(String sessionName, Seeker emitter, Seeker receptor, Documents docs, String comments , QuerySource data) throws SessionException, SeekerException, RecommendationException, IOException;

    /**
     * Recomendar una selección de los resultados de la búsqueda, obtenidos por
     * un buscador a un grupo de usuarios de su sesión
     *
     * @param sessionName   nombre de la sesión
     * @param emitter       usuario emisor
     * @param receptors     usuario receptor
     * @param docs
     * @param data
     * @param comments      comentarios de la recomendación
     *
     * @throws SessionException  si la sesión no se encuentra registrada
     *                                   en el servidor
     * @throws SeekerException     si el usuario emisor ó alguno de los receptores
     *                                   no se encuentra registrado en la sesión
     * @throws RecommendationException   Si el usuario que emite la recomendación
     *                                   no cuenta con resultados de búquedas previas
     *                                   registradas
     * @throws IOException   si ocurre alguna excepción durante el proceso de
     *                       serialización del objeto Response
     */
    public void recommendResults(String sessionName, Seeker emitter, List<Seeker> receptors, Documents docs, String comments, QuerySource data) throws SessionException, SeekerException, RecommendationException, IOException;

    /**
     * Recomendar todos los resultados de la búsqueda a un usuario de otra sesión
     * de búsqueda
     *
     * @param sessionName     nombre de la sesión del emisor
     * @param emitter         usuario emisor
     * @param sessionNameRtr  nombre de la sesión del receptor
     * @param receptor        usuario a quien va dirigida la recomendación
     * @param comments        comentarios de la recomendación
     * @param data
     *
     * @throws SessionException  si la sesión no se encuentra registrada
     *                                   en el servidor
     * @throws SeekerException     si el usuario emisor ó el receptor no se
     *                                   encuentra registrado en la sesión
     * @throws RecommendationException   si el usuario que emite la recomendación
     *                                   no cuenta con resultados de búquedas previas
     *                                   registradas
     * @throws IOException   si ocurre alguna excepción durante el proceso de
     *                       serialización del objeto Response
     */
    public void recommendResults(String sessionName, Seeker emitter, String sessionNameRtr, Seeker receptor, String comments, QuerySource data) throws SessionException, SeekerException, RecommendationException, IOException;

    /**
     * Recomendar una selección de los resultados de la búsqueda, obtenidos por
     * un buscador a un usuario de otra sesión de búsqueda
     *
     * @param sessionName     nombre de la sesión del emisor
     * @param emitter         usuario emisor
     * @param sessionNameRtr  nombre de la sesión del receptor
     * @param receptor        usuario a quien va dirigida la recomendación
     * @param docs
     * @param comments        comentarios de la recomendación
     * @param data
     *
     * @throws SessionException  si la sesión no se encuentra registrada
     *                                   en el servidor
     * @throws SeekerException     si el usuario emisor ó el receptor no se
     *                                   encuentra registrado en la sesión
     * @throws RecommendationException   si el usuario que emite la recomendación
     *                                   no cuenta con resultados de búquedas previas
     *                                   registradas
     * @throws IOException   si ocurre alguna excepción durante el proceso de
     *                       serialización del objeto Response
     */
    public void recommendResults(String sessionName, Seeker emitter, String sessionNameRtr, Seeker receptor, Documents docs, String comments, QuerySource data) throws SessionException, SeekerException, RecommendationException, IOException;

    /**
     * Recomendar todos los resultados de la búsqueda a un grupo de usuarios de
     * otra sesión de búsqueda
     *
     * @param sessionName     nombre de la sesión del emisor
     * @param emitter         usuario emisor
     * @param sessionNameRtrs nombre de la sesión de los receptores
     * @param receptors       usuario receptor
     * @param comments        comentarios de la recomendación
     * @param data
     *
     * @throws SessionException si la sesión no se encuentra registrada
     *                                  en el servidor
     * @throws SeekerException    si el usuario emisor ó alguno de los receptores
     *                                  no se encuentra registrado en la sesión
     * @throws RecommendationException  si el usuario que emite la recomendación
     *                                  no cuenta con resultados de búquedas previas
     *                                  registradas
     * @throws IOException   si ocurre alguna excepción durante el proceso de
     *                       serialización del objeto Response
     */
    public void recommendResults(String sessionName, Seeker emitter, String sessionNameRtrs, List<Seeker> receptors, String comments, QuerySource data) throws SessionException, SeekerException, RecommendationException, IOException;

    /**
     * Recomendar una selección de los resultados de la búsqueda, obtenidos por
     * un buscador a un grupo de miembros de otra sesión de búsqueda
     *
     * @param sessionName     nombre de la sesión del emisor
     * @param emitter         usuario emisor
     * @param sessionNameRtrs nombre de la sesión de los receptores
     * @param receptors       usuario receptor
     * @param docs
     * @param comments        comentarios de la recomendación
     * @param data
     *
     * @throws SessionException  si la sesión no se encuentra registrada
     *                                   en el servidor
     * @throws SeekerException     si el usuario emisor ó alguno de los receptores
     *                                   no se encuentra registrado en la sesión
     * @throws RecommendationException   si el usuario que emite la recomendación
     *                                   no cuenta con resultados de búquedas previas
     *                                   registradas
     * @throws IOException   si ocurre alguna excepción durante el proceso de
     *                       serialización del objeto Response
     */
    public void recommendResults(String sessionName, Seeker emitter, String sessionNameRtrs, List<Seeker> receptors, Documents docs, String comments, QuerySource data) throws SessionException, SeekerException, RecommendationException, IOException;

    /**
     * Devuelve la total de recomendaciones efectuadas por los usuarios de todas
     * las sesiones activas en el servidor
     *
     * @return  total de recomendaciones.
     */
    public long getRecommendationsCount();

    /**
     * Devuelve la total de recomendaciones efectuadas por los usuarios de la
     * las sesión especificada
     *
     * @param sessionName    nombre de la sesión
     *
     * @return total de recomendaciones
     *
     * @throws SessionException   si la sesión no se encuentra registrada
     *                                    en el servidor
     */
    public long getRecommendationsCount(String sessionName) throws SessionException;

    /**
     * Devuelve el total de recomendaciones efectuadas por un usuario de la sesión
     * especificada
     *
     * @param sessionName nombre de la sesión
     * @param seeker      usuario
     *
     * @return total de recomendaciones
     *
     * @throws SessionException si la sesión no se encuentra registrada
     *                                  en el servidor
     * @throws SeekerException    si el usuario del que solicita el total
     *                                  mensajes no está registrado en la sesión
     */
    public long getRecommendationsCount(String sessionName, Seeker seeker) throws SessionException, SeekerException;
}
