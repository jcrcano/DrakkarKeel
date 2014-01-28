/*
 * DrakkarKeel - An Enterprise Collaborative Search Platform
 *
 * The contents of this file are subject under the terms described in the
 * DRAKKARKEEL_LICENSE file included in this distribution; you may not use this
 * file except in compliance with the License. 
 *
 * 2013-2014 DrakkarKeel Platform.
 */
package drakkar.stern.tracker.persistent;

import drakkar.stern.tracker.persistent.tables.DerbyConnection;
import drakkar.stern.tracker.persistent.objects.SeekerData;
import drakkar.stern.tracker.persistent.objects.SessionData;
import drakkar.oar.DocumentMetaData;
import drakkar.oar.MarkupData;
import drakkar.oar.RecommendTracker;
import drakkar.oar.ResultSetMetaData;
import drakkar.oar.SearchResultData;
import drakkar.oar.SearchTracker;
import drakkar.oar.Seeker;
import drakkar.oar.SeekerQuery;
import drakkar.oar.Session;
import java.awt.image.BufferedImage;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Clase para manejar los datos para la persistencia de DrakkarKeel
 * utilizados en la aplicación cliente
 */
public class FacadeDBProw {

    SearchDB search;
    SearchSessionDB session;
    TrackerDB tracker;
    SeekerDB seeker;

    /**
     * Constructor de la clase
     *
     * @param connection
     * @param dbutil
     */
    public FacadeDBProw(DerbyConnection connection, DBUtil dbutil) {

        seeker = new SeekerDB(connection, dbutil);
        session = new SearchSessionDB(connection, dbutil, seeker);
        search = new SearchDB(connection, dbutil, session);
        tracker = new TrackerDB(connection, dbutil, search, session);

    }

    /*********************************SEEKER*******************
    
    /**
     * Registrar un nuevo usuario
     *
     * @param user              nombre del usuario
     * @param name              nombre completo
     * @param password          contraseña
     * @param description       descripción
     * @param email             correo
     * @param avatar            avatar
     * @param rol               rol
     * @param state             estado
     *
     * @return                  true si encontró el seeker, de lo contrario false
     *
     * @throws SQLException     si ocurre alguna SQLException durante la ejecución
     *                          de la operación
     */
    public boolean registerSeeker(String user, String name, String password, String description, String email, byte[] avatar, String rol, String state) throws SQLException {
        return seeker.registerSeeker(user, name, password, description, email, avatar, rol, state);

    }

    /**
     * Elimina un usuario
     *
     * @param user             nombre del usuario
     *
     * @return                 true si lo eliminó, false si no lo encontró
     *
     * @throws SQLException    si ocurre alguna SQLException durante la ejecución
     *                         de la operación
     */
    public boolean deleteSeeker(String user) throws SQLException {
        return seeker.deleteSeeker(user);
    }

    /**
     * Actualiza el rol del usuario
     *
     * @param user               usuario a actualizar
     * @param rol                nombre del nuevo rol
     *
     * @return                   true si lo actualizó, de lo contrario devuelve false
     *
     * @throws SQLException      si ocurre alguna SQLException durante la ejecución
     *                           de la operación
     */
    public boolean updateSeekerRol(String user, String rol) throws SQLException {
        return seeker.updateSeekerRol(user, rol);
    }

    /**
     * Actualiza el estado del usuario
     *
     * @param user              nombre del usuario
     * @param state             estado
     *
     * @return                  true si lo actualizó, de lo contrario devuelve false
     *
     * @throws SQLException     si ocurre alguna SQLException durante la ejecución
     *                          de la operación
     */
    public boolean updateSeekerState(String user, String state) throws SQLException {
        return seeker.updateSeekerState(user, state);
    }

    /**
     * Actualiza la imagen del usuario
     *
     * @param user             nombre del usuario
     * @param newAvatar        imagen que representa a este usuario
     *
     * @return                  true si lo actualizó, de lo contrario devuelve false
     *
     * @throws SQLException     si ocurre alguna SQLException durante la ejecución
     *                          de la operación
     */
    public boolean updateSeekerAvatar(String user, Object newAvatar) throws SQLException {
        return seeker.updateSeekerAvatar(user, newAvatar);
    }

    /**
     * Obtiene el avatar de un usuario
     *
     * @param user              nombre del usuario
     *
     * @return                  el avatar del usuario
     *
     * @throws SQLException     si ocurre alguna SQLException durante la ejecución
     *                          de la operación
     */
    public BufferedImage getAvatar(String user) throws SQLException {
        return seeker.getAvatar(user);
    }

    /**
     * Compara contraseñas de un usuario
     *
     * @param user               nombre del usuario
     * @param password           contraseña
     *
     * @return                   true si la contraseña coincide, de lo contrario false
     *
     * @throws SQLException      si ocurre alguna SQLException durante la ejecución
     *                           de la operación
     */
    public boolean compareSeekerPassword(String user, String password) throws SQLException {
        return seeker.compareSeekerPassword(user, password);
    }

    /**
     * Devuelve todos los datos de un seeker dado su user
     *
     * @param user               nombre del usuario
     *
     * @return                   devuelve un objeto que contiene los datos de un usuario
     *
     * @throws SQLException      si ocurre alguna SQLException durante la ejecución
     *                           de la operación
     */
    public SeekerData getSeekerData(String user) throws SQLException {
        return seeker.getSeekerData(user);
    }

    /**
     * Obtiene el rol dado su id
     *
     * @param id                    id del rol
     *
     * @return                      devuelve el nombre del rol
     *
     * @throws SQLException         si ocurre alguna SQLException durante la ejecución
     *                              de la operación
     */
    public String getRol(int id) throws SQLException {
        return seeker.getRol(id);
    }

    /**
     * Obtiene el tipo de estado dado su id
     *
     * @param id                   id del estado
     *
     * @return                     devuelve el tipo de estado
     *
     * @throws SQLException        si ocurre alguna SQLException durante la ejecución
     *                             de la operación
     */
    public String getState(int id) throws SQLException {
        return seeker.getState(id);
    }

    /**
     * Obtiene el id del rol
     *
     * @param name                nombre del rol
     *
     * @return                    devuelve el id del rol
     *
     * @throws SQLException       si ocurre alguna SQLException durante la ejecución
     *                            de la operación
     */
    public int getRol(String name) throws SQLException {
        return seeker.getRol(name);
    }

    /**
     * Obtiene el id del estado
     *
     * @param type             nombre del estado
     *
     * @return                 devuelve el id del estado
     *
     * @throws SQLException    si ocurre alguna SQLException durante la ejecución
     *                            de la operación
     */
    public int getState(String type) throws SQLException {
        return seeker.getState(type);
    }

    /**
     * Verifica si un usuario determinado existe (si ya está registrado)
     *
     * @param user              nombre del usuario
     *
     * @return                  true si existe el usuario, false de lo contrario
     *
     * @throws SQLException     si ocurre alguna SQLException durante la ejecución
     *                          de la operación
     */
    public boolean existSeeker(String user) throws SQLException {
        return seeker.existSeeker(user);
    }

    /**
     * Actualiza la contraseña de un usuario
     *
     * @param user                  nombre del usuario
     * @param newPassword           nueva contraseña
     *
     * @throws SQLException         si ocurre alguna SQLException durante la ejecución
     *                              de la operación
     */
    public void updateSeekerPassword(String user, String newPassword) throws SQLException {
        seeker.updateSeekerPassword(user, newPassword);
    }

    /**
     * Devuelve el email de un usuario
     *
     * @param user                 nombre del usuario
     *
     * @return                     devuelve el email del usuario
     *
     * @throws SQLException        si ocurre alguna SQLException durante la ejecución
     *                             de la operación
     */
    public String getSeekerEmail(String user) throws SQLException {
        return seeker.getEmail(user);
    }

    /**
     * Establece el estado de baja a un usuario en una sesión
     *
     * @param sessionTopic            nombre de la sesión
     * @param seekerUser             nombre del usuario
     *
     * @return                   devuelve true si le dio baja al usuario de dicha sesión, devuelve false
     *                           en caso que ese usuario no pertenezca a la sesión especificada
     *
     * @throws SQLException      si ocurre alguna SQLException durante la ejecución
     *                           de la operación
     */
    public boolean declineSeekerSession(String sessionTopic, String seekerUser) throws SQLException {
        return seeker.declineSeekerSession(sessionTopic, seekerUser);
    }

    /**
     * Verifica si un usuario pertenece a una sesión
     *
     * @param session             nombre de la sesión
     * @param seekerUser          nombre del usuario
     *
     * @return                    devuelve true si el seeker pertenece a la sesión, false si no
     *
     * @throws SQLException       si ocurre alguna SQLException durante la ejecución
     *                            de la operación
     */
    public boolean verifySeeker(String session, String seekerUser) throws SQLException {
        return seeker.verifySeeker(session, seekerUser);
    }

    /*******************SEARCH SESSION*******************
    
    /**
     * Inserta una nueva sesión de búsqueda
     *
     * @param topic          nombre de la sesión
     * @param description    descripción
     * @param chairman       usuario que la creó
     * @param criteria       criterio de integridad
     * @param maxMember      número máximo de miembros
     * @param minMember      número mínimo de miembros
     * @param members        número de miembros actuales
     * @param membership     membresía
     *
     * @return               devuelve true si guardó los datos de la sesión, de lo contrario devuelve false
     *
     * @throws SQLException       si ocurre alguna SQLException durante la ejecución
     *                            de la operación
     */
    public boolean saveSearchSession(String topic, String description, String chairman, boolean criteria, int maxMember, int minMember, List<Seeker> members, String membership) throws SQLException {
        return session.saveSearchSession(topic, description, chairman, criteria, maxMember, minMember, members, membership);
    }

    /**
     * Verifica si una sesión de búsqueda está guardada en la BD
     *
     * @param session            nombre de la sesión
     *
     * @return                   true si la sesión existe, false si no
     *
     * @throws SQLException      si ocurre alguna SQLException durante la ejecución
     *                           de la operación
     */
    public boolean existSession(String session) throws SQLException {
        return this.session.existSession(session);
    }

    /**
     * Obtiene el id de la membresía
     *
     * @param type                nombre del tipo de membresía
     *
     * @return                    devuelve el id de la membrersía
     *
     * @throws SQLException       si ocurre alguna SQLException durante la ejecución
     *                            de la operación
     */
    public int getMembership(String type) throws SQLException {
        return session.getMembership(type);
    }

    /**
     * Elimina una sesión de búsqueda
     *
     * @param sessionTopic           nombre de la sesión
     *
     * @throws SQLException          si ocurre alguna SQLException durante la ejecución
     *                               de la operación
     */
    public void deleteSearchSession(String sessionTopic) throws SQLException {
        session.deleteSearchSession(sessionTopic);
    }

    /**
     * Carga los datos de los usuarios que pertencen a determinada sesión
     *
     * @param sessionTopic       nombre de la sesión
     *
     * @return                   devuelve una lista de objetos Seekers
     *
     * @throws SQLException      si ocurre alguna SQLException durante la ejecución
     *                           de la operación
     */
    public List<Seeker> loadSeekers(String sessionTopic) throws SQLException {
        return session.loadSeekers(sessionTopic);
    }

    /**
     * Relaciona un usuario a una sesión de búsqueda, actualiza el rol del usuario a Miembro
     *
     * @param seeker               nombre del usuario
     * @param sessionTopic              nombre de la sesión
     *
     * @throws SQLException        si ocurre alguna SQLException durante la ejecución
     *                             de la operación
     */
    public void joinNewSeeker(String seeker, String sessionTopic) throws SQLException {
        session.joinNewSeeker(seeker, sessionTopic);
    }

    /**
     * Relaciona varios seekers a una sesión
     *
     * @param seekers                lista de usuarios
     * @param sessionTopic           nombre de la sesión
     *
     * @throws SQLException          si ocurre alguna SQLException durante la ejecución
     *                               de la operación
     */
    public void joinNewSeeker(List<Seeker> seekers, String sessionTopic) throws SQLException {
        session.joinNewSeeker(seekers, sessionTopic);
    }

    /**
     * Obtiene todas las sesiones guardadas en la BD
     *
     * @return                   devuelve una lista de los datos de todas las sesiones persistentes
     *
     * @throws SQLException      si ocurre alguna SQLException durante la ejecución
     *                           de la operación
     */
    public List<SessionData> getAllSessions() throws SQLException {
        return session.getAllSessions();
    }

    /**
     * Guarda los mensajes enviados por un usuario
     *
     * @param sessionTopic          nombre de la sesión
     * @param user             usuario que envió el mensaje
     * @param text             texto del mensaje
     * @param receptor         lista de receptores a quienes fue enviado
     *
     * @throws SQLException    si ocurre alguna SQLException durante la ejecución
     *                         de la operación
     */
    public void saveMessages(String sessionTopic, String user, String text, List<Seeker> receptor) throws SQLException {
        session.saveMessages(sessionTopic, user, text, receptor);
    }

    /**
     * Guarda el mensaje enviado a un receptor
     *
     * @param sessionTopic          nombre de la sesión
     * @param user             usuario que envió el mensaje
     * @param text             texto del mensaje
     * @param receptor         receptor a quien fue enviado
     *
     * @throws SQLException    si ocurre alguna SQLException durante la ejecución
     *                         de la operación
     */
    public void saveMessages(String sessionTopic, String user, String text, Seeker receptor) throws SQLException {
        session.saveMessages(sessionTopic, user, text, receptor);
    }

    /**
     * Obtiene todos los datos de una sesión de búsqueda
     *
     * @param sessionTopic          nombre de la sesión
     *
     * @return                      objeto que contiene datos de la sesión
     *
     * @throws SQLException         si ocurre alguna SQLException durante la ejecución
     *                              de la operación
     */
    public SessionData getSessionData(String sessionTopic) throws SQLException {
        return session.getSessionData(sessionTopic);
    }

    /**
     * Obtener las sesiones activas por usuario
     *
     * @param seekerUser            nombre del usuario
     *
     * @return                      lista con los datos de las sesiones
     *
     * @throws SQLException         si ocurre alguna SQLException durante la ejecución
     *                              de la operación
     */
    public List<Session> getSeekerAvailableSessions(String seekerUser) throws SQLException {
        return session.getSeekerAvailableSessions(seekerUser);
    }

    /**
     *
     * Obtener todos los usuarios que pertenecen a una sesión
     *
     * @param sessionTopic              nombre de la sesión
     *
     * @return                     devuelve una lista con los nombres de los usuarios de la sesión
     *
     * @throws SQLException        si ocurre alguna SQLException durante la ejecución
     *                             de la operación
     */
    public List<String> getSessionSeekers(String sessionTopic) throws SQLException {
        return session.getSessionSeekers(sessionTopic);
    }

    /**
     * Finaliza una sesión
     *
     * @param sessionTopic           nombre de la sesión
     *
     * @throws SQLException          si ocurre alguna SQLException durante la ejecución
     *                               de la operación
     */
    public void closeCollabSession(String sessionTopic) throws SQLException {
        session.closeCollabSession(sessionTopic);
    }

    /**
     * Obtener todas las sesiones activas
     *
     * @return                    devuelve una lista de los nombres de las sesiones activas
     *
     * @throws SQLException       si ocurre alguna SQLException durante la ejecución
     *                               de la operación
     */
    public List<String> getAllEnableSessions() throws SQLException {
        return session.getAllEnableSessions();
    }

    /***********************SEARCH************************

    /**
     * Obtiene todos los resultados de búsqueda de una sesión
     *
     * @param sessionTopic              nombre de la sesión
     *
     * @return                     devuelve una lista de los datos de todos los resultados de búsqueda de esa sesión
     *
     * @throws SQLException        si ocurre alguna SQLException durante la ejecución
     *                             de la operación
     */
    public List<SearchResultData> getAllDocuments(String sessionTopic) throws SQLException {
        return search.getAllDocuments(sessionTopic);
    }

    /**
     * Obtiene todos los resultados de búsqueda revisados de una sesión
     *
     * @param sessionTopic            nombre de la sesión
     *
     * @return                   devuelve una lista de los datos de todos los resultados de búsqueda revisados de esa sesión
     *
     * @throws SQLException      si ocurre alguna SQLException durante la ejecución
     *                           de la operación
     */
    public List<SearchResultData> getAllReviewDocuments(String sessionTopic) throws SQLException {
        return search.getAllReviewDocuments(sessionTopic);
    }

    /**
     * Obtiene todos los resultados de búsqueda evaluados de una sesión
     *
     *
     * @param sessionTopic       nombre de la sesión
     * @param date               fecha de la evaluación del documento
     *
     * @return                   devuelve una lista de los datos de todos los resultados de búsqueda relevantes de esa sesión
     *
     * @throws SQLException      si ocurre alguna SQLException durante la ejecución
     *                           de la operación
     */
    public List<SearchResultData> getAllRelevantDocuments(String sessionTopic, Date date) throws SQLException {
        return search.getAllRelevantDocuments(sessionTopic, date);
    }

    /**
     * Obtiene todos los resultados de búsqueda evaluados dada una consulta de
     * una sesión
     *
     * @param session        nombre de la sesión
     * @param query          consulta
     *
     * @return               devuelve una lista que contiene los nombres de los documentos
     *
     * @throws SQLException  si ocurre alguna SQLException durante la ejecución
     *                       de la operación
     */
    public List<String> getAllRelevantDocuments(String session, String query) throws SQLException {
        return search.getRelevantDocumentsQuery(session, query);
    }

    /**
     * Guarda los datos de una búsqueda
     *
     * @param principle      principio de búsqueda utilizado
     * @param query          consulta
     * @param session        nombre de la sesión
     * @param allResults     resultados de búsqueda por motor
     * @param user           nombre del usuario
     *
     * @throws SQLException  si ocurre alguna SQLException durante la ejecución
     *                       de la operación
     */
    public void saveSearchData(String query, String principle, Map<Integer, List<DocumentMetaData>> allResults, String user, String session) throws SQLException {
        search.saveSearchData(session, query, principle, allResults, user);
    }

    /**
     * Obtiene el id de determinado principio de búsqueda
     *
     * @param principle           principio de búsqueda
     *
     * @return                    devuelve el id del principio
     *
     * @throws SQLException       si ocurre alguna SQLException durante la ejecución
     *                            de la operación
     */
    public int getSearchPrinciple(String principle) throws SQLException {
        return search.getSearchPrinciple(principle);
    }

    /**
     * Obtiene el principio de búsqueda dado su id
     *
     * @param principle        id del principio de búsqueda
     *
     * @return                 devuelve el nombre del principio
     *
     * @throws SQLException    si ocurre alguna SQLException durante la ejecución
     *                            de la operación
     */
    public String getSearchPrinciple(int principle) throws SQLException {
        return search.getSearchPrinciple(principle);
    }

    /**
     * Obtiene una lista de todos los estados de usuarios existentes
     *
     * @return                   devuelve una lista de todos los estados
     *
     * @throws SQLException      si ocurre alguna SQLException durante la ejecución
     *                           de la operación
     */
    public List<String> getAllStates() throws SQLException {
        return search.getAllStates();
    }

    /**
     * Obtiene el id de un motor de búsqueda
     *
     * @param engine           nombre del motor
     *
     * @return                 id en la tabla
     *
     * @throws SQLException    si ocurre alguna SQLException durante la ejecución
     *                           de la operación
     */
    public int getSearchEngine(String engine) throws SQLException {
        return search.getSearchEngine(engine);
    }

    /**
     * Obtiene el nombre de un motor de búsqueda
     *
     * @param idEngine           id del motor
     *
     * @return                   nombre en la tabla
     *
     * @throws SQLException      si ocurre alguna SQLException durante la ejecución
     *                           de la operación
     */
    public String getSearchEngine(int idEngine) throws SQLException {
        return search.getSearchEngine(idEngine);
    }

    /**
     * Obtiene los motores seleccionados por consulta
     *
     * @param query          id de la consulta
     *
     * @return               devuelve una lista de todos los motores utilizados para dicha consulta
     *
     * @throws SQLException  si ocurre alguna SQLException durante la ejecución
     *                           de la operación
     */
    public List<String> getSearchEngineQuery(int query) throws SQLException {
        return search.getSearchEngineQuery(query);
    }

    /**
     * Obtiene todas las consultas de una sesión sin repetir
     *
     * @param session           nombre de la sesión
     *
     * @return                  devuelve una lista de todas las consultas para esa sesión
     *
     * @throws SQLException      si ocurre alguna SQLException durante la ejecución
     *                       de la operación
     */
    public List<String> getAllQueries(String session) throws SQLException {
        return search.getAllQueries(session);

    }

    /**
     * Obtiene todas las consultas de una sesión sin repetir
     *
     * @param session                nombre de la sesión
     * @param date                   fecha de la consulta
     *
     * @return                       devuelve todos los seekers de una sesión con sus consultas
     *
     * @throws SQLException           si ocurre alguna SQLException durante la ejecución
     *                                de la operación
     */
    public SeekerQuery getSeekerQueries(String session, Date date) throws SQLException {
        return search.getSeekerQueries(session, date);

    }

    /**
     * Dados los ids de documentos devuelve una lista con sus datos
     * correspondientes
     *
     *
     * @param list           lista de ids de documentos
     *
     * @return               devuelve una lista con los datos de los documentos correspondientes a los ids especificados
     *
     * @throws SQLException   si ocurre alguna SQLException durante la ejecución
     *                                de la operación
     */
    public List<DocumentMetaData> getSearchResults(List<Integer> list) throws SQLException {
        return this.search.getSearchResults(list);
    }

    /**
     * Devuelve todos los resultados de búsqueda para una consulta y una sesión
     *
     * @param session       nombre de la sesión
     * @param query         consulta
     *
     * @return               devuelve una lista con los datos de los documentos correspondientes a la consulta
     *                      especificada en esa sesión
     *
     * @throws SQLException   si ocurre alguna SQLException durante la ejecución
     *                                de la operación
     */
    public List<DocumentMetaData> getSearchResults(String session, String query) throws SQLException {
        return this.search.getSearchResults(session, query);
    }

    /**
     * Devuelve resultados de búsqueda
     *
     * @param session       nombre de la sesión
     * @param query         consulta
     * @param engine        motor de búsqueda empleado
     *
     * @return               devuelve una lista con los datos de los documentos correspondientes a la consulta
     *                      especificada en esa sesión para un motor de búsqueda
     *
     * @throws SQLException   si ocurre alguna SQLException durante la ejecución
     *                                de la operación
     */
    public List<DocumentMetaData> getSearchResults(String query, String session, String engine) throws SQLException {
        return this.search.getSearchResults(session, query, engine);
    }

    /***************SEARCH RESULT*********************


    /**
     * Actualiza el campo de revisión de un documento
     *
     * @param idDoc                id del documento
     * @param review               revisado
     *
     * @throws SQLException        si ocurre alguna SQLException durante la ejecución
     *                                de la operación
     */
    public void updateDocumentReview(int idDoc, boolean review) throws SQLException {
        search.updateDocumentReview(idDoc, review);
    }

    /**
     * Establece que un documento fue revisado
     *
     * @param uri             dirección del documento revisado
     * @param session         nombre de la sesión
     *
     * @throws SQLException        si ocurre alguna SQLException durante la ejecución  de la operación
     */
    public void setReviewDocument(String session, String uri) throws SQLException {
        this.search.setReviewDocument(session, uri);
    }

    /**
     * Guarda los datos de la recomendación hecha por un usuario a muchos  usuarios
     * de su sesión
     *
     * @param sessionTopic              nombre de la sesión
     * @param user                 nombre del usuario que emitió la recomendación
     * @param receptors            lista de recpetores
     * @param idDoc                id del documento
     * @param text                 texto o comentario de la recomendación
     *
     * @throws SQLException        si ocurre alguna SQLException durante la ejecución  de la operación
     */
    public void saveRecommendation(String sessionTopic, String user, List<Seeker> receptors, int idDoc, String text) throws SQLException {
        search.saveRecommendation(sessionTopic, user, receptors, idDoc, text);
    }

    /**
     * Guarda los datos de la recomendación hecha por un usuario a muchos  usuarios
     * de su sesión
     *
     * @param sessionTopic              nombre de la sesión
     * @param user                 nombre del usuario que emitió la recomendación
     * @param receptors            lista de receptores
     * @param list                 documentos
     * @param text                 texto o comentario de la recomendación
     *
     * @throws SQLException        si ocurre alguna SQLException durante la ejecución  de la operación
     */
    public void saveRecommendation(String sessionTopic, String user, List<Seeker> receptors, ResultSetMetaData list, String text) throws SQLException {
        search.saveRecommendation(sessionTopic, user, receptors, list, text);
    }

    /**
     *
     * Guarda los datos de la recomendación de varios documentos hecha por un usuario a muchos  usuarios
     * de su sesión
     *
     * @param sessionTopic              nombre de la sesión
     * @param user                 nombre del usuario que emitió la recomendación
     * @param receptors            lista de receptores
     * @param idDoc                documentos
     * @param text                 texto o comentario de la recomendación
     *
     * @throws SQLException        si ocurre alguna SQLException durante la ejecución  de la operación
     */
    public void saveRecommendation(String sessionTopic, String user, List<Seeker> receptors, List<Integer> idDoc, String text) throws SQLException {
        search.saveRecommendation(sessionTopic, user, receptors, idDoc, text);
    }

    /**
     * Guarda los datos de la recomendación hecha por un usuario a un  usuario
     * de su sesión
     *
     * @param sessionTopic              nombre de la sesión
     * @param user                 nombre del usuario que emitió la recomendación
     * @param receptor             receptor
     * @param idDoc                documentos
     * @param text                 texto o comentario de la recomendación
     *
     * @throws SQLException        si ocurre alguna SQLException durante la ejecución  de la operación
     */
    public void saveRecommendation(String sessionTopic, String user, Seeker receptor, int idDoc, String text) throws SQLException {
        search.saveRecommendation(sessionTopic, user, receptor, idDoc, text);
    }

    /**
     * Obtiene los datos de un resultado de búsqueda, dado su id
     *
     * @param id                id del documento
     * @param session           nombre de la sesión
     *
     * @return                  devuelve los datos de un resultado de búsqueda
     *
     * @throws SQLException        si ocurre alguna SQLException durante la ejecución  de la operación
     */
    public SearchResultData getResult(int id, String session) throws SQLException {
        return search.getResult(id, session);
    }

    /**
     * Obtiene el id del resultado dado su uri
     *
     * @param uri              ubicacion del documento
     * @param session          nombre de la sesión
     *
     * @return                 devuelve el id del resultado
     *
     * @throws SQLException        si ocurre alguna SQLException durante la ejecución  de la operación
     */
    public int getResultId(String uri, String session) throws SQLException {
        return search.getResultId(uri, session);
    }

    /**
     * Cargar resultados de búsquedas a partir de la consulta
     *
     * @param idQuery        id de la consulta
     * @param session        nombre de la sesión
     *
     * @return               devuelve una lista de los ids de resultados de búsquedas
     *
     * @throws SQLException        si ocurre alguna SQLException durante la ejecución  de la operación
     */
    public List<Integer> loadQueryResults(int idQuery, String session) throws SQLException {
        return search.loadQueryResults(idQuery, session);
    }

    /**************REVIEW DOCUMENTS********************

    /**
     * Guarda los datos de la revisión hecha por un usuario a un resultado de búsqueda
     *
     * @param sessionTopic             nombre de la sesión
     * @param comment                  comentario sobre el documento
     * @param relevance                valor de relevancia dado al documento
     * @param user                     nombre del usuario
     * @param uri                      uri del documento
     *
     * @throws SQLException        si ocurre alguna SQLException durante la ejecución  de la operación
     */
    public void saveEvaluation(String sessionTopic, String comment, int relevance, String user, String uri) throws SQLException {
        search.saveEvaluation(sessionTopic, comment, relevance, user, uri);
    }

    /**
     * Inserta un comentario realizado por un usuario para un documento
     * en una sesión, de existir lo reemplaza
     *
     * @param sessionTopic              nombre de la sesión
     * @param seeker               objeto seeker del usuario
     * @param uri                  uri del documento evaluado
     * @param comment              comentario
     *
     * @throws SQLException        si ocurre alguna SQLException durante la ejecución  de la operación
     */
    public void updateComment(String sessionTopic, Seeker seeker, String uri, String comment) throws SQLException {
        search.updateComment(sessionTopic, seeker, uri, comment);
    }

    /**
     * Obtiene el id de la revisión
     *
     * @param idDoc            id del documento
     * @param user             usuario que hizo la revisión
     *
     * @return                 devuelve el id de la revisión
     *
     * @throws SQLException        si ocurre alguna SQLException durante la ejecución  de la operación
     */
    public int getMarkup(int idDoc, String user) throws SQLException {
        return search.getMarkup(idDoc, user);
    }

    /**
     * Obtener las evaluaciones hechas a un resultado de búsqueda.
     *
     * @param result                objeto que representa el resultado de búsqueda
     * @param sessionTopic          nombre de la sesión
     *
     * @return                      devuelve una lista con los datos de todas las evaluaciones hechas a un resultado
     *
     * @throws SQLException         si ocurre alguna SQLException durante la ejecución
     *                               de la operación
     */
    public List<MarkupData> getEvaluations(SearchResultData result, String sessionTopic) throws SQLException {
        return session.getEvaluations(result, sessionTopic);
    }

    /*****************************RECORD TABLES********************/
    ////////////////////RECOMMEND//////////////
    /**
     * Obtiene una lista las recomendaciones realizadas en una sesión.
     *
     * @param sessionTopic          nombre de la sesión
     *
     * @return                 devuelve una lista con los datos de las recomendaciones realizadas en una sesión
     *
     * @throws SQLException     si ocurre alguna SQLException durante la ejecución
     *                               de la operación
     */
    public List<RecommendTracker> getAllRecommendations(String sessionTopic) throws SQLException {
        return tracker.getAllRecommendations(sessionTopic);
    }

    /**
     * Obtiene una lista las recomendaciones realizadas en una sesión.
     *
     * @param sessionTopic               nombre de la sesión
     * @param seeker                nombre del usuario que emitió la recomendación
     *
     * @return                 devuelve una lista con los datos de las recomendaciones realizadas en una sesión
     *
     * @throws SQLException     si ocurre alguna SQLException durante la ejecución
     *                               de la operación
     */
    public List<RecommendTracker> getAllRecommendations(String sessionTopic, Seeker seeker) throws SQLException {
        return tracker.getAllRecommendations(sessionTopic, seeker);
    }

    /**
     * Obtiene una lista las recomendaciones realizadas en una sesión
     *
     * @param sessionTopic             nombre de la sesión
     * @param date                fecha de la recomendación
     *
     * @return                 devuelve una lista con los datos de las recomendaciones realizadas en una sesión
     *
     * @throws SQLException     si ocurre alguna SQLException durante la ejecución
     *                               de la operación
     */
    public List<RecommendTracker> getAllRecommendations(String sessionTopic, Date date) throws SQLException {
        return tracker.getAllRecommendations(sessionTopic, date);
    }

    /**
     * Obtiene una lista las recomendaciones realizadas en una sesión.
     *
     * @param sessionTopic             nombre de la sesión
     * @param query               consulta
     *
     * @return                 devuelve una lista con los datos de las recomendaciones realizadas en una sesión
     *
     * @throws SQLException     si ocurre alguna SQLException durante la ejecución
     *                               de la operación
     */
    public List<RecommendTracker> getAllRecommendations(String sessionTopic, String query) throws SQLException {
        return tracker.getAllRecommendations(sessionTopic, query);
    }

    /**
     * Obtiene una lista las recomendaciones realizadas en una sesión.
     *
     * @param sessionTopic             nombre de la sesión
     * @param seeker              usuario que hizo la recomendación
     * @param query               consulta
     *
     * @return                 devuelve una lista con los datos de las recomendaciones realizadas en una sesión
     *
     * @throws SQLException     si ocurre alguna SQLException durante la ejecución
     *                               de la operación
     */
    public List<RecommendTracker> getAllRecommendations(String sessionTopic, Seeker seeker, String query) throws SQLException {
        return tracker.getAllRecommendations(sessionTopic, seeker, query);
    }

    /**
     * Obtiene una lista las recomendaciones realizadas en una sesión
     *
     * @param sessionTopic        nombre de la sesión
     * @param query          consulta
     * @param date           fecha de recomendación
     *
     * @return                 devuelve una lista con los datos de las recomendaciones realizadas en una sesión
     *
     * @throws SQLException     si ocurre alguna SQLException durante la ejecución
     *                               de la operación
     */
    public List<RecommendTracker> getAllRecommendations(String sessionTopic, String query, Date date) throws SQLException {
        return tracker.getAllRecommendations(sessionTopic, query, date);
    }

    /**
     * Obtiene una lista las recomendaciones realizadas en una sesión.
     *
     * @param sessionTopic        nombre de la sesión
     * @param seeker         nombre del usuario que emitió la recomendación
     * @param date           fecha de la recomendación
     *
     * @return                 devuelve una lista con los datos de las recomendaciones realizadas en una sesión
     *
     * @throws SQLException     si ocurre alguna SQLException durante la ejecución
     *                               de la operación
     */
    public List<RecommendTracker> getAllRecommendations(String sessionTopic, Seeker seeker, Date date) throws SQLException {
        return tracker.getAllRecommendations(sessionTopic, seeker, date);
    }

    /**
     * Obtiene una lista las recomendaciones realizadas en una sesión.
     *
     * @param sessionTopic               nombre de la sesión
     * @param seeker                nombre del usuario que emitió la recomendación
     * @param query                 consulta
     * @param date                  fecha
     *
     * @return                 devuelve una lista con los datos de las recomendaciones realizadas en una sesión
     *
     * @throws SQLException     si ocurre alguna SQLException durante la ejecución
     *                               de la operación
     */
    public List<RecommendTracker> getAllRecommendations(String sessionTopic, Seeker seeker, String query, Date date) throws SQLException {
        return tracker.getAllRecommendations(sessionTopic, seeker, query, date);
    }

    ///////////////////SEARCHES////////////////////////////////
    /**
     * Obtiene una lista de documentos correspondientes a búsquedas
     * realizadas en una sesión
     *
     * @param sessionTopic               nombre de la sesión
     * @param group                 identificador de los documentos (revisados, relevantes o todos)
     *
     * @return                 devuelve una lista con los datos de las búsquedas realizadas en una sesión
     *
     * @throws SQLException     si ocurre alguna SQLException durante la ejecución
     *                               de la operación
     */
    public List<SearchTracker> getAllSearches(String sessionTopic, int group) throws SQLException {
        return tracker.getAllSearches(sessionTopic, group);
    }

    /**
     * Obtiene una lista de documentos correspondientes a búsquedas
     * realizadas en una sesión dados los parámetros entrados
     *
     * @param sessionTopic           nombre de la sesión
     * @param group             identificador de los documentos (revisados, relevantes o todos)
     * @param seeker            nombre del usuario
     *
     * @return                 devuelve una lista con los datos de las búsquedas realizadas en una sesión
     *
     * @throws SQLException     si ocurre alguna SQLException durante la ejecución
     *                               de la operación
     */
    public List<SearchTracker> getAllSearches(String sessionTopic, int group, Seeker seeker) throws SQLException {
        return tracker.getAllSearches(sessionTopic, group, seeker);
    }

    /**
     *
     * Obtiene una lista de documentos correspondientes a búsquedas
     * realizadas en una sesión dados los parámetros entrados
     *
     * @param sessionTopic            nombre de la sesión
     * @param group              identificador de los documentos (revisados, relevantes o todos)
     * @param date               fecha
     *
     * @return                 devuelve una lista con los datos de las búsquedas realizadas en una sesión
     *
     * @throws SQLException     si ocurre alguna SQLException durante la ejecución
     *                               de la operación
     */
    public List<SearchTracker> getAllSearches(String sessionTopic, int group, Date date) throws SQLException {
        return tracker.getAllSearches(sessionTopic, group, date);
    }

    /**
     * Obtiene una lista de documentos correspondientes a búsquedas
     * realizadas en una sesión dados los parámetros entrados
     *
     * @param sessionTopic           nombre de la sesión
     * @param group             identificador de los documentos (revisados, relevantes o todos)
     * @param query             consulta
     *
     * @return                 devuelve una lista con los datos de las búsquedas realizadas en una sesión
     *
     * @throws SQLException     si ocurre alguna SQLException durante la ejecución
     *                               de la operación
     */
    public List<SearchTracker> getAllSearches(String sessionTopic, int group, String query) throws SQLException {
        return tracker.getAllSearches(sessionTopic, group, query);
    }

    /**
     * Obtiene una lista de documentos correspondientes a búsquedas
     * realizadas en una sesión dados los parámetros entrados
     *
     * @param sessionTopic            nombre de la sesión
     * @param group              identificador de los documentos (revisados, relevantes o todos)
     * @param seeker             nombre del usuario
     * @param query              consulta
     *
     * @return                 devuelve una lista con los datos de las búsquedas realizadas en una sesión
     *
     * @throws SQLException     si ocurre alguna SQLException durante la ejecución
     *                               de la operación
     */
    public List<SearchTracker> getAllSearches(String sessionTopic, int group, Seeker seeker, String query) throws SQLException {
        return tracker.getAllSearches(sessionTopic, group, seeker, query);
    }

    /**
     * Obtiene una lista de documentos correspondientes a búsquedas
     * realizadas en una sesión dados los parámetros entrados
     *
     * @param sessionTopic            nombre de la sesión
     * @param group                   identificador de los documentos (revisados, relevantes o todos)
     * @param query                   consulta
     * @param date                    fecha de la búsqueda
     *
     * @return                 devuelve una lista con los datos de las búsquedas realizadas en una sesión
     *
     * @throws SQLException     si ocurre alguna SQLException durante la ejecución
     *                               de la operación
     */
    public List<SearchTracker> getAllSearches(String sessionTopic, int group, String query, Date date) throws SQLException {
        return tracker.getAllSearches(sessionTopic, group, query, date);
    }

    /**
     * Obtiene una lista de documentos correspondientes a búsquedas
     * realizadas en una sesión dados los parámetros entrados
     *
     * @param sessionTopic              nombre de la sesión
     * @param group                identificador de los documentos (revisados, relevantes o todos)
     * @param seeker               nombre del usuario
     * @param date                 fecha de la búsqueda
     *
     * @return                 devuelve una lista con los datos de las búsquedas realizadas en una sesión
     *
     * @throws SQLException     si ocurre alguna SQLException durante la ejecución
     *                               de la operación
     */
    public List<SearchTracker> getAllSearches(String sessionTopic, int group, Seeker seeker, Date date) throws SQLException {
        return tracker.getAllSearches(sessionTopic, group, seeker, date);
    }

    /**
     * Obtiene una lista de documentos correspondientes a búsquedas
     * realizadas en una sesión dados los parámetros entrados
     *
     * @param sessionTopic               nombre de la sesión
     * @param group                 identificador de los documentos (revisados, relevantes o todos)
     * @param seeker                nombre del usuario
     * @param date                  fecha
     * @param query                 consulta
     *
     * @return                 devuelve una lista con los datos de las búsquedas realizadas en una sesión
     *
     * @throws SQLException     si ocurre alguna SQLException durante la ejecución
     *                               de la operación
     */
    public List<SearchTracker> getAllSearches(String sessionTopic, int group, Seeker seeker, Date date, String query) throws SQLException {
        return tracker.getAllSearches(sessionTopic, group, seeker, date, query);
    }
}
