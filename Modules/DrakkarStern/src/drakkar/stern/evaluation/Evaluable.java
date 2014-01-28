/*
 * DrakkarKeel - An Enterprise Collaborative Search Platform
 *
 * The contents of this file are subject under the terms described in the
 * DRAKKARKEEL_LICENSE file included in this distribution; you may not use this
 * file except in compliance with the License. 
 *
 * 2013-2014 DrakkarKeel Platform.
 */
package drakkar.stern.evaluation;

import drakkar.oar.DocumentMetaData;
import drakkar.oar.Seeker;
import drakkar.oar.SessionProperty;
import drakkar.oar.exception.QueryNotExistException;
import drakkar.oar.exception.SeekerException;
import drakkar.oar.exception.SessionException;
import drakkar.mast.SearchableException;
import java.util.List;

/**
 * Esta interfaz declara los métodos de evaluaciones soportados por el Framework
 * DrakkarKeel, para determinar la efectividad del sistema a la hora de recuperar los
 * documentos y otros métodos que pudieran ser interés
 */
public interface Evaluable {

    /**
     * Devuelve los documentos evaluados de relevantes por los miembros de una
     * sesión, para una consulta y buscador determinado
     *
     * @param sessionName nombre de la sesión
     * @param query       consulta de la búsqueda
     * @param searcher    buscador
     *
     * @return lista de documentos relevantes
     *
     * @throws SessionException   si la sesión no se encuentra registrada
     *                                    en el servidor
     * @throws QueryNotExistException     si la consulta especificada no se encuentra
     *                                    registrada en la sesión
     * @throws SearchableException        si el buscador seleccionado no es soportado
     */
    public List<DocumentMetaData> getRelevantDocuments(String sessionName, String query, int searcher) throws SessionException, QueryNotExistException, SearchableException;

    /**
     * Devuelve los documentos evaluados de relevantes por los miembros de una
     * sesión, para una consulta y los buscadores seleccionados
     *
     * @param sessionName nombre de la sesión
     * @param query       consulta de la búsqueda
     * @param searchers   buscadores
     *
     * @return lista de documentos relevantes
     *
     * @throws SessionException   si la sesión no se encuentra registrada
     *                                    en el servidor
     * @throws QueryNotExistException     si la consulta especificada no se encuentra
     *                                    registrada en la sesión
     * @throws SearchableException        si el buscador seleccionado no es soportado
     */
    public List<DocumentMetaData> getRelevantDocuments(String sessionName, String query, int[] searchers) throws SessionException, QueryNotExistException, SearchableException;

    /**
     * Devuelve los documentos evaluados de relevantes por un miembro de la sesión,
     * para una consulta y buscador seleccionado
     *
     * @param sessionName nombre de la sesión
     * @param seeker      miembro del cual se solicita la información
     * @param query       consulta de la búsqueda
     * @param searcher    buscador
     *
     * @return lista de documentos relevantes
     *
     * @throws SessionException si la sesión no se encuentra registrada
     *                                  en el servidor
     * @throws QueryNotExistException   si la consulta especificada no se encuentra
     *                                  registrada en la sesión
     * @throws UserNotExistException    si el miembro no está registrado en la sesión
     * @throws SearchableException      si el buscador seleccionado no es soportado
     */
    public List<DocumentMetaData> getRelevantDocuments(String sessionName, Seeker seeker, String query, int searcher) throws SessionException, QueryNotExistException, SeekerException, SearchableException;

    /**
     * Devuelve los documentos revisados por los miembros de una sesión, para una
     * consulta determinada y los buscadores seleccionados
     *
     * @param sessionName  nombre de la sesión
     * @param query        consulta de la búsqueda     
     * @param searchers    buscadores
     *
     * @return lista de documentos revisados
     *
     * @throws SessionException si la sesión no se encuentra registrada
     *                                  en el servidor
     * @throws QueryNotExistException   si la consulta especificada no se encuentra
     *                                  registrada en la sesión
     * @throws SearchableException      si el buscador seleccionado no es soportado
     */
    public List<DocumentMetaData> getViewedDocuments(String sessionName, String query, int[] searchers) throws SessionException, QueryNotExistException, SearchableException;

    /**
     * Devuelve los documentos revisados por los miembros de una sesión, para una
     * consulta determinada y el buscador seleccionado
     *
     * @param sessionName  nombre de la sesión
     * @param query        consulta de la búsqueda    
     * @param searcher     buscador
     * 
     * @return lista de documentos revisados
     *
     * @throws SessionException si la sesión no se encuentra registrada
     *                                  en el servidor
     * @throws QueryNotExistException   si la consulta especificada no se encuentra
     *                                  registrada en la sesión
     * @throws SearchableException      si el buscador seleccionado no es soportado
     */
    public List<DocumentMetaData> getViewedDocuments(String sessionName, String query, int searcher) throws SessionException, QueryNotExistException, SearchableException;

    /**
     * Devuelve los documentos revisados por un miembro de la sesión, para una consulta
     * determinada y el buscador seleccionado
     *
     * @param sessionName nombre de la sesión
     * @param seeker      miembro del cual se solicita la información
     * @param query       consulta de la búsqueda
     * @param searcher    buscador
     *
     * @return lista de documentos revisados
     *
     * @throws SessionException si la sesión no se encuentra registrada
     *                                  en el servidor
     * @throws QueryNotExistException   si la consulta especificada no se encuentra
     *                                  registrada en la sesión
     * @throws SeekerException          si el miembro no está registrado en la sesión
     * @throws SearchableException      si el buscador seleccionado no es soportado
     */
    public List<DocumentMetaData> getViewedDocuments(String sessionName, Seeker seeker, String query, int searcher) throws SessionException, QueryNotExistException, SeekerException, SearchableException;

    /**
     * Devuelve los documentos recuperados por los miembros de una sesión, para una consulta
     * determinada y el buscador selecccionado
     *
     * @param sessionName  nombre de la sesión
     * @param query        consulta de la búsqueda    
     * @param searcher     buscador
     *
     * @return lista de documentos recuperados
     *
     * @throws SessionException si la sesión no se encuentra registrada
     *                                  en el servidor
     * @throws QueryNotExistException   si la consulta especificada no se encuentra
     *                                  registrada en la sesión
     * @throws SearchableException      si el buscador seleccionado no es soportado
     */
    public List<DocumentMetaData> getRetrievedDocuments(String sessionName, String query, int searcher) throws SessionException, QueryNotExistException, SearchableException;

    /**
     * Devuelve los documentos recuperados por un miembro de la sesión, para una
     * consulta determinada y el buscador selecccionado
     *
     * @param sessionName nombre de la sesión
     * @param seeker      miembro del cual se solicita la información
     * @param query       consulta de la búsqueda    
     * @param searcher    buscador
     *
     * @return lista de documentos recuperados
     *
     * @throws SessionException si la sesión no se encuentra registrada
     *                                  en el servidor
     * @throws QueryNotExistException   si la consulta especificada no se encuentra
     *                                  registrada en la sesión
     * @throws UserNotExistException    si el miembro no está registrado en la sesión
     * @throws SearchableException      si el buscador seleccionado no es soportado
     */
    public List<DocumentMetaData> getRetrievedDocuments(String sessionName, Seeker seeker, String query, int searcher) throws SessionException, QueryNotExistException, SeekerException, SearchableException;

    /**
     * Devuelve el total de documentos recuperados recuperados de una
     * consulta y sesión determinada
     *
     * @param sessionName nombre de la sesión
     * @param query       consulta de la búsqueda
     *
     * @return total de documentos recuperados
     *
     * @throws SessionException si la sesión no se encuentra registrada
     *                                  en el servidor
     * @throws QueryNotExistException   si la consulta especificada no se encuentra
     *                                  registrada en la sesión
     */
    public long getRetrievedDocumentsCount(String sessionName, String query) throws SessionException, QueryNotExistException;

    /**
     * Devuelve la lista de consultas efectuadas por los miembros de una sesión
     * determinada
     *
     * @param sessionName nombre de la sesión
     *
     * @return lista de consultas
     *
     * @throws SessionException si la sesión no se encuentra registrada en
     *                                  el servidor
     */
    public List<String> getQuerys(String sessionName) throws SessionException;

    /**
     * Devuelve el tiempo de duración de la sesión especificada
     *
     * @param sessionName nombre de la sesión
     *
     * @return tiempo de duración
     *
     * @throws SessionException si la sesión no se encuentra registrada en
     *                                  el servidor
     */
    public long getDurationSessionTime(String sessionName) throws SessionException;

    /**
     * Devuelve las propiedades de la sesión especificada
     *
     * @param sessionName nombre de la sesión
     *
     * @return  propiedades
     *
     * @throws SessionException si la sesión no se encuentra registrada en
     *                                  el servidor
     */
    public SessionProperty getSessionProperties(String sessionName) throws SessionException;

   
}
