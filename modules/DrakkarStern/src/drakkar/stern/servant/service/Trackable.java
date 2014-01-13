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
import drakkar.oar.exception.TrackException;
import drakkar.oar.slice.client.ClientSidePrx;
import java.sql.Date;

/**
 * The <code>Trackable</code> class is....
 * Esta interfaz declara todas las operaciones soportadas por el framework DrakkarKeel
 * para el trabajo con historiales
 */
public interface Trackable {

   
    /**
     * Solicita el historial de recomendaciones a
     * partir de los parámetros entrados
     *
     * @param sessionName  nombre de la sesión
     * @param emitter
     * @param emitterPrx
     * 
     * @throws TrackException
     *    
     */
    public void trackRecommendation(String sessionName, Seeker emitter, ClientSidePrx emitterPrx) throws TrackException;

    /**
     * Solicita el historial de recomendaciones a
     * partir de los parámetros entrados
     *
     * @param sessionName  nombre de la sesión
     * @param seeker       usuario que realizó la búsqueda
     * @param emitter
     * @param emitterPrx
     * 
     *
     * @throws TrackException
     *
     *
     *
     */
    public void trackRecommendation(String sessionName, Seeker seeker, Seeker emitter, ClientSidePrx emitterPrx) throws TrackException;

    /**
     * Solicita el historial de recomendaciones a
     * partir de los parámetros entrados
     *
     * @param sessionName  nombre de la sesión
     * @param date         fecha en que se realizó la búsqueda
     * @param emitter
     * @param emitterPrx
     *
     * @throws TrackException
     *
     *
     */
    public void trackRecommendation(String sessionName, Date date, Seeker emitter, ClientSidePrx emitterPrx) throws TrackException;

    /**
     * Solicita el historial de recomendaciones a
     * partir de los parámetros entrados
     *
     * @param sessionName  nombre de la sesión
     * @param query        consulta de la búsqueda
     * @param emitter
     * @param emitterPrx
     *
     * @throws TrackException
     *
     *
     *
     */
    public void trackRecommendation(String sessionName, String query, Seeker emitter, ClientSidePrx emitterPrx) throws TrackException;

    /**
     * Solicita el historial de recomendaciones a
     * partir de los parámetros entrados
     *
     * @param sessionName  nombre de la sesión
     * @param seeker       usuario que realizó la búsqueda
     * @param query        consulta de la búsqueda
     * @param emitter
     * @param emitterPrx
     *
     * @throws TrackException
     *
     *
     *
     */
    public void trackRecommendation(String sessionName, Seeker seeker, String query, Seeker emitter, ClientSidePrx emitterPrx) throws TrackException;

    /**
     * Solicita el historial de recomendaciones a
     * partir de los parámetros entrados
     *
     * @param sessionName  nombre de la sesión
     * @param date         fecha en que se realizó la búsqueda
     * @param query        consulta de la búsqueda
     * @param emitter
     * @param emitterPrx
     *
     * @throws TrackException
     *
     *
     *
     */
    public void trackRecommendation(String sessionName, String query, Date date, Seeker emitter, ClientSidePrx emitterPrx) throws TrackException;

    /**
     * Solicita el historial de recomendaciones a
     * partir de los parámetros entrados
     *
     * @param sessionName  nombre de la sesión
     * @param seeker       usuario que realizó la búsqueda
     * @param date         fecha en que se realizó la búsqueda
     * @param emitter
     * @param emitterPrx
     *
     * @throws TrackException
     *
     *
     *
     */
    public void trackRecommendation(String sessionName, Seeker seeker, Date date, Seeker emitter, ClientSidePrx emitterPrx) throws TrackException;

    /**
     * Solicita el historial de recomendaciones a
     * partir de los parámetros entrados
     *
     * @param sessionName  nombre de la sesión
     * @param seeker       usuario que realizó la búsqueda
     * @param date         fecha en que se realizó la búsqueda
     * @param query        consulta de la búsqueda
     * @param emitter
     * @param emitterPrx
     *
     * @throws TrackException
     *
     *
     *
     */
    public void trackRecommendation(String sessionName, Seeker seeker, Date date, String query, Seeker emitter, ClientSidePrx emitterPrx) throws TrackException;

    /*********************RECORD SEARCH********************************/
    /**
     * Solicita el historial de búsquedas a
     * partir de los parámetros entrados
     *
     * @param sessionName  nombre de la sesión
     * @param group        clasificador de documentos
     * @param emitter
     * @param emitterPrx
     *
     * @throws TrackException
     *
     *
     */
    public void trackSearch(String sessionName, int group, Seeker emitter, ClientSidePrx emitterPrx) throws TrackException;

    /**
     * Solicita el historial de búsquedas a
     * partir de los parámetros entrados
     *
     * @param sessionName  nombre de la sesión
     * @param seeker       usuario que realizó la búsqueda
     * @param group        clasificador de documentos
     * @param emitter
     * @param emitterPrx
     * @throws TrackException
     *     
     *
     *
     */
    public void trackSearch(String sessionName, Seeker seeker, int group, Seeker emitter, ClientSidePrx emitterPrx) throws TrackException;

    /**
     * Solicita el historial de búsquedas a
     * partir de los parámetros entrados
     *
     * @param sessionName  nombre de la sesión
     * @param date         fecha en que se realizó la búsqueda
     * @param group        clasificador de documentos
     * @param emitter
     * @param emitterPrx
     *
     * @throws TrackException
     *   
     *
     */
    public void trackSearch(String sessionName, Date date, int group, Seeker emitter, ClientSidePrx emitterPrx) throws TrackException;

    /**
     * Solicita el historial de búsquedas a
     * partir de los parámetros entrados
     *
     * @param sessionName  nombre de la sesión
     * @param query        consulta de la búsqueda
     * @param group        clasificador de documentos
     * @param emitter
     * @param emitterPrx
     *
     * @throws TrackException
     * 
     *
     */
    public void trackSearch(String sessionName, String query, int group, Seeker emitter, ClientSidePrx emitterPrx) throws TrackException;

    /**
     * Solicita el historial de búsquedas a
     * partir de los parámetros entrados
     *
     * @param sessionName  nombre de la sesión
     * @param seeker       usuario que realizó la búsqueda
     * @param query        consulta de la búsqueda
     * @param group        clasificador de documentos
     * @param emitter
     * @param emitterPrx
     *
     * @throws TrackException
     *    
     *
     *
     */
    public void trackSearch(String sessionName, Seeker seeker, String query, int group, Seeker emitter, ClientSidePrx emitterPrx) throws TrackException;

    /**
     * Solicita el historial de búsquedas a
     * partir de los parámetros entrados
     *
     * @param sessionName  nombre de la sesión
     * @param date         fecha en que se realizó la búsqueda
     * @param query        consulta de la búsqueda
     * @param group        clasificador de documentos
     * @param emitter
     * @param emitterPrx
     *
     * @throws TrackException
     *    
     *
     *
     */
    public void trackSearch(String sessionName, String query, Date date, int group, Seeker emitter, ClientSidePrx emitterPrx) throws TrackException;

    /**
     * Solicita el historial de búsquedas a
     * partir de los parámetros entrados
     *
     * @param sessionName  nombre de la sesión
     * @param seeker       usuario que realizó la búsqueda
     * @param date         fecha en que se realizó la búsqueda
     * @param group        clasificador de documentos
     * @param emitter
     * @param emitterPrx
     *
     * @throws TrackException
     *    
     *
     *
     */
    public void trackSearch(String sessionName, Seeker seeker, Date date, int group, Seeker emitter, ClientSidePrx emitterPrx) throws TrackException;

    /**
     * Solicita el historial de búsquedas a
     * partir de los parámetros entrados
     *
     * @param sessionName  nombre de la sesión
     * @param seeker       usuario que realizó la búsqueda
     * @param date         fecha en que se realizó la búsqueda
     * @param query        consulta de la búsqueda
     * @param group        clasificador de documentos(SeekerAction.SEARCH_ALL_TRACK, SeekerAction.SEARCH_SELECTED_RELEVANT_TRACK,SeekerAction.SEARCH_REVIEWED_TRACK)
     * @param emitter
     * @param emitterPrx
     *
     * @throws TrackException
     *    
     *
     */
    public void trackSearch(String sessionName, Seeker seeker, Date date, String query, int group, Seeker emitter, ClientSidePrx emitterPrx) throws TrackException;


    /**
     * 
     * @param sessionName     
     * @param date     *
     * @param emitter
     * @param prx
     * 
     * @throws TrackException
     */
    public void trackSession(String sessionName, Date date, Seeker emitter, ClientSidePrx prx) throws TrackException;


}
