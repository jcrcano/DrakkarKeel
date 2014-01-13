/*
 * DrakkarKeel - An Enterprise Collaborative Search Platform
 *
 * The contents of this file are subject under the terms described in the
 * DRAKKARKEEL_LICENSE file included in this distribution; you may not use this
 * file except in compliance with the License. 
 *
 * 2013-2014 DrakkarKeel Platform.
 */
package drakkar.stern.controller;

import drakkar.oar.DocumentMetaData;
import drakkar.oar.RecommendTracker;
import drakkar.oar.ResultSetMetaData;
import drakkar.oar.SearchTracker;
import drakkar.oar.Seeker;
import drakkar.oar.SeekerQuery;
import drakkar.oar.Session;
import drakkar.oar.security.DrakkarSecurity;
import drakkar.oar.svn.SVNData;
import static drakkar.oar.util.KeySearchable.*;
import drakkar.oar.util.KeySession;
import drakkar.oar.util.OutputMonitor;
import drakkar.stern.email.DrakkarSternEmail;
import drakkar.stern.email.EmailConfig;
import drakkar.stern.tracker.persistent.DBConfiguration;
import drakkar.stern.tracker.persistent.Manager;
import drakkar.stern.tracker.persistent.Persistence;
import drakkar.stern.tracker.persistent.objects.IndexData;
import drakkar.stern.tracker.persistent.objects.SeekerData;
import drakkar.stern.tracker.persistent.objects.SessionData;
import drakkar.stern.tracker.persistent.security.Encryption;
import drakkar.stern.tracker.persistent.security.SafeAccess;
import drakkar.stern.tracker.persistent.security.User;
import drakkar.stern.tracker.persistent.security.UserAccessLevel;
import drakkar.stern.tracker.persistent.tables.DerbyConnection;
import drakkar.stern.tracker.persistent.tables.DerbyEmbeddedConnection;
import drakkar.stern.tracker.persistent.tables.DriverException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Clase controladora que maneja la BD
 */
public class DataBaseController {

    private DerbyConnection derbyConnection;
    private Manager manager;
    boolean isOpen;
    private String location, user, password;
    DrakkarSternEmail email;

    /**
     * Constructor de la clase
     * 
     * @param dbConfig  objeto que contiene los parámetros de configuración para la BD
     */
    public DataBaseController(DBConfiguration dbConfig) {
        this.location = dbConfig.getLocation();
        this.user = dbConfig.getUser();
        this.password = dbConfig.getPassword();
        this.email = null;

    }

    /**
     * Constructor de la clase
     *
     * @param dbConfig       objeto que contiene los parámetros de configuración para la BD
     * @param emailConfig    objeto que contiene los parámetros de configuración para el correo
     */
    public DataBaseController(DBConfiguration dbConfig, EmailConfig emailConfig) {
        this.location = dbConfig.getLocation();
        this.user = dbConfig.getUser();
        this.password = dbConfig.getPassword();
        this.email = new DrakkarSternEmail(emailConfig);

    }

    /**
     * Para enviar e-mails
     *
     * @param toAddress      dirección a enviar
     * @param ccAddress      dirección con copia
     * @param bccAddress     dirección con copia
     * @param subject        asunto
     * @param isHTMLFormat   formato HTML
     * @param body           texto del mensaje
     * @param debug
     * @return
     */
    public boolean send(String toAddress,
            String ccAddress, String bccAddress, String subject,
            boolean isHTMLFormat, StringBuffer body, boolean debug) {

        return this.email.send(toAddress, ccAddress, bccAddress, subject, isHTMLFormat, body, debug);
    }

    /**
     * Para enviar e-mails
     * 
     * @param toAddress        dirección a enviar
     * @param subject          asunto
     * @param isHTMLFormat     formato HTML
     * @param body             texto del mensaje
     * @param debug
     * @return
     */
    public boolean send(String toAddress, String subject,
            boolean isHTMLFormat, StringBuffer body, boolean debug) {

        return this.email.send(toAddress, subject, isHTMLFormat, body, debug);
    }

    /**
     * Para abrir la conexión con la BD
     *
     * @return
     * @throws DriverException
     * @throws SQLException
     */
    public boolean openConnection() throws DriverException, SQLException {

        derbyConnection = new DerbyEmbeddedConnection();
        isOpen = derbyConnection.open(location, user, password);

        if (isOpen) {
            setManager(new Manager(derbyConnection));
        }

        return isOpen;

    }

    /**
     * Cierra la conexión
     *
     * @return
     */
    public boolean closeConnection() {

        boolean flag = false;
        if (this.derbyConnection.isOpen()) {
            flag = this.derbyConnection.closeConnection();
        }

        isOpen = !flag;

        return flag;
    }

    /**
     * Método para establecer el administrador de la BD.
     *
     * @param user            usuario del administrador
     * @param password        contraseña
     * @throws SQLException
     */
    public void setDBManager(String user, String password) throws SQLException {

        SafeAccess safe = new SafeAccess(this.derbyConnection.getConnection());
        List<User> list = new ArrayList<>();
        list.add(new User(user, password, UserAccessLevel.FULLACCESS.toString()));
        safe.setUsers(list);

    }

    /**
     * Cambia contraseña de un adminstrador de la BD
     *
     * @param user          usuario del administrador
     * @param password      contraseña
     * @throws SQLException
     */
    public void setNewPassword(String user, String password) throws SQLException {
        SafeAccess safe = new SafeAccess(this.derbyConnection.getConnection());
        safe.setNewPassword(user, password);

    }

    /**
     * Verifica que un usuario sea manager de la BD
     * 
     * @param user            usuario del administrador
     * @param password        contraseña
     * @return 
     * @throws SQLException
     * @throws NoSuchAlgorithmException
     */
    public boolean isManager(String user, String password) throws SQLException, NoSuchAlgorithmException {
        Encryption enc = new Encryption();

        SafeAccess access = new SafeAccess(this.derbyConnection.getConnection());
        List<String> list = access.getManager();

        if (list.contains(user)) {

            String derbyPass = access.getUserPassword(user);
            String encrypted = enc.getPass(password);
            if (derbyPass.equals(encrypted)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }

    }

    /**
     * Establece los valores constantes en las tablas correspondientes.
     * Se utiliza cuando se crea la BD.
     *
     * @throws SQLException
     */
    public void initTables() throws SQLException {
        this.getManager().setConstantValues();
    }

    /**
     * Devuelve true si la conexión con la BD está abierta
     *
     * @return
     */
    public boolean isOpen() {

        return isOpen;
    }

    /**********************************INDEX*********************************/
    /**
     * Inserta un índice en la BD.
     *
     * @param uri      dirección donde está guardado el índice
     * @param idSE     id del motor de búsqueda utilizado
     * @param count    cantidad de documentos indexados
     * @return
     * @throws SQLException
     */
    public boolean insertIndex(String uri, int idSE, int count) throws SQLException {
        return this.getManager().getServer().insertIndex(uri, idSE, count);
    }

    /**
     * Obtiene el id del motor en la BD.
     *
     * @param name       nombre del motor
     * @return
     * @throws SQLException
     */
    public int getSearchEngine(String name) throws SQLException {
        return this.manager.getClient().getSearchEngine(name);
    }

    /**
     * Elimina un índice de la BD.
     *
     * @param uri             dirección del índice
     * @throws SQLException
     */
    public void deleteIndex(String uri) throws SQLException {
        this.manager.getServer().deleteIndex(uri);
    }

    /**
     * Obtiene todos los índices
     *
     * @return
     * @throws SQLException
     */
    public List<IndexData> getAllIndex() throws SQLException {
        return this.manager.getServer().getAllIndex();
    }

    /**
     * Actualiza los datos de un índice
     *
     * @param time            fecha de la nueva indexación
     * @param uri             dirección del índice
     * @param count           cantidad de documentos indexados
     * @throws SQLException
     */
    public void updateIndex(Date time, String uri, int count) throws SQLException {
        this.manager.getServer().updateDataIndex(time, uri, count);
    }

    /**
     * Adiciona documentos a un índice que ya está creado
     * 
     * @param uri            dirección del índice
     * @param count          cantidad de documentos indexados
     * @throws SQLException
     */
    public void addDocsIndex(String uri, int count) throws SQLException {
        this.manager.getServer().addDocsIndex(uri, count);
    }

    /**
     * Guarda los datos de la colección indexada
     *
     * @param path                ubicación de la colección
     * @param context             contexto de la colección
     * @param docs                cantidad de documentos de la colección
     * @param direct              --es para salvar datos de colecciones SVN, true si se indexó a partir del direct path
     * @param indexUri            dirección del índice
     * @param repositoryName      nombre del repositorio (si se indexó un repositorio SVN)
     * @param password            contraseña
     * @param user                usuario
     * @return
     * @throws SQLException
     */
    public boolean saveCollection(String path, String context, int docs, boolean direct, List<String> indexUri, String user, String password, String repositoryName) throws SQLException {
        return this.manager.getServer().insertCollection(path, context, docs, direct, indexUri, user, password, repositoryName);
    }

    /**
     * Guarda la relación de la colección utilizada para la búsqueda en una sesión
     * 
     * @param path       ubicación de la colección
     * @param session    tema de la sesión
     * @return
     * @throws SQLException
     */
    public boolean saveCollectionSession(String path, String session) throws SQLException {
        return this.manager.getServer().insertSessionCollection(session, path);
    }

    /**
     * Devuelve los datos referentes a un repositorio SVN indexado
     *
     * @param repoName      nombre del repositorio
     * @param idSE          id del motor con el cual se indexó
     * @return
     * @throws SQLException
     */
    public SVNData getSVNRepositoryData(String repoName, int idSE) throws SQLException {
        return this.manager.getServer().getSVNRepositoryData(repoName, idSE);
    }

    /**
     * Devuelve los nombre de todos los repositorios SVN indexados
     *
     * @return
     * @throws SQLException
     */
    public List<String> getAllSVNRepositories() throws SQLException {
        return this.manager.getServer().getAllSVNRepositories();
    }

    /**
     * @return the manager
     */
    public Manager getManager() {
        return manager;
    }

    /**
     * @param manager the manager to set
     */
    public void setManager(Manager manager) {
        this.manager = manager;
    }

    /**
     *
     * Si la BD no existe la crea, sino la elimina y la vuelve a crear
     * 
     *     
     * @throws SQLException
     * @throws DriverException
     */
    public void buildDB() throws SQLException, DriverException {

        String[] sqlCreate = Persistence.createTables;
        String[] sqlDelete = Persistence.deleteTables;
        String string;
        File f = new File(this.location);

        if (!f.exists()) {  //DB does not exist
            derbyConnection = new DerbyEmbeddedConnection();
            boolean create = this.derbyConnection.createDataBase(this.location);
            if (create) {
                manager = new Manager(derbyConnection);
                for (int i = 0; i < sqlCreate.length; i++) {
                    string = sqlCreate[i];
                    manager.createDB(string);
                }
                this.manager.setConstantValues();
                //set authentication               
                this.setDBManager(user, DrakkarSecurity.decryptPassword(password));

            }

        } else { //replace
            derbyConnection = new DerbyEmbeddedConnection();
            boolean open = derbyConnection.open(this.location, user, password);

            if (open) {
                manager = new Manager(derbyConnection);

                for (int i = 0; i < sqlDelete.length; i++) {
                    string = sqlDelete[i];
                    manager.createDB(string);
                }
                for (int i = 0; i < sqlCreate.length; i++) {
                    string = sqlCreate[i];
                    manager.createDB(string);
                }

                this.manager.setConstantValues();
            }

        }

    }

    /**
     * Método para hacer una salva de la BD
     *
     * @param uri             dirección donde se va a guardar la salva
     * @throws SQLException
     * @throws DriverException
     */
    public void backupDB(String uri) throws SQLException, DriverException {
        if (this.derbyConnection.isOpen()) {
            derbyConnection.backUpDatabase(uri);
        }
    }

    /**
     * Método para restaurar una BD salvada anteriormente
     *
     * @param uri              dirección desdee donde se va a restaurar la salva
     * @param user             nombre del administrador de esta DB
     * @param pass             contrasena para conectarse a esta DB
     * @throws SQLException
     * @throws DriverException
     */
    public void restoreDB(String uri, String user, String pass) throws SQLException, DriverException {
        if (this.derbyConnection.isOpen()) {

            derbyConnection.restoringDatabase(uri, user, pass);
        } else {
            throw new SQLException("No existe una conexión abierta con la BD");
        }
    }

    /**
     * Devuelve la lista de los uri de los índices creados por un motor de búsqueda
     *
     * @param idSE            id del motor de búsqueda
     * @return
     * @throws SQLException
     */
    public List<String> getIndexList(int idSE) throws SQLException {
        return this.manager.getServer().getIndexList(idSE);
    }

    /****************************SESSION**************************************/
    /**
     * Guarda un mensaje emitido en una sesión colaborativa de búsqueda
     *
     * @param session          nombre de la sesión
     * @param user             usuario que envió el mensaje
     * @param text             texto del mensaje
     * @param receptor         lista de receptores a quienes fue enviado
     * @throws SQLException
     */
    public void saveMessage(String session, String user, String text, List<Seeker> receptor) throws SQLException {
        this.manager.getClient().saveMessages(session, user, text, receptor);
    }

    /**
     *
     * Guarda un mensaje emitido en una sesión colaborativa de búsqueda
     *
     * @param session             nombre de la sesión
     * @param user                usuario que envió el mensaje
     * @param text                texto del mensaje
     * @param receptor            receptor a quien fue enviado
     * @throws SQLException
     */
    public void saveMessage(String session, String user, String text, Seeker receptor) throws SQLException {
        this.manager.getClient().saveMessages(session, user, text, receptor);
    }

    /**
     * Guarda una recomendación realizada en una sesión colaborativa de búsqueda
     *
     * @param session            nombre de la sesión
     * @param user               usuario que envió el mensaje
     * @param text               texto del mensaje
     * @param idDoc              id del documento recomendado
     * @param receptors          lista de receptores a quienes fue enviado
     * @throws SQLException
     */
    public void saveRecomendation(String session, String user, String text, int idDoc, List<Seeker> receptors) throws SQLException {
        this.manager.getClient().saveRecommendation(session, user, receptors, idDoc, text);
    }

    /**
     * Guarda una recomendación realizada en una sesión colaborativa de búsqueda
     * 
     * @param session            nombre de la sesión
     * @param user               usuario que envió el mensaje
     * @param text               texto del mensaje
     * @param list               resultados de búsqueda recomendados
     * @param receptors          lista de receptores a quienes fue enviado
     * @throws SQLException
     */
    public void saveRecomendation(String session, String user, String text, ResultSetMetaData list, List<Seeker> receptors) throws SQLException {
        this.manager.getClient().saveRecommendation(session, user, receptors, list, text);
    }

    /**
     *
     * Guarda una recomendación realizada en una sesión colaborativa de búsqueda
     *
     * @param session            nombre de la sesión
     * @param user               usuario que envió el mensaje
     * @param text               texto del mensaje
     * @param idDoc              id del documento recomendado
     * @param receptors          lista de receptores a quienes fue enviado
     * @throws SQLException
     */
    public void saveRecomendation(String session, String user, String text, List<Integer> idDoc, List<Seeker> receptors) throws SQLException {
        this.manager.getClient().saveRecommendation(session, user, receptors, idDoc, text);
    }

    /**
     * Guarda una recomendación realizada en una sesión colaborativa de búsqueda
     * 
     * @param session            nombre de la sesión
     * @param user               usuario que envió el mensaje
     * @param text               texto del mensaje
     * @param idDoc              id del documento recomendado
     * @param receptor           receptor a quien fue enviado
     * @throws SQLException
     */
    public void saveRecomendation(String session, String user, String text, int idDoc, Seeker receptor) throws SQLException {
        this.manager.getClient().saveRecommendation(session, user, receptor, idDoc, text);
    }

    /**
     * Guarda los datos referentes a una sesión creada
     *
     * @param newSessionName     nombre de la sesión
     * @param description        descripción
     * @param chairman               usuario que la creó
     * @param criteria           criterio de integridad
     * @param max                número máximo de miembros
     * @param min                número mínimo de miembros
     * @param currentMembers     número de miembros actuales
     * @param membership         membresía
     *
     * @throws SQLException
     */
    public void saveSearchSession(String newSessionName, String description, String chairman, int criteria, int max, int min, List<Seeker> currentMembers, int membership) throws SQLException {
        boolean integrity = false;
        String membershipIdentified = null;

        switch (criteria) {
            case KeySession.SESSION_SOFT:
                integrity = true;
                break;

            case KeySession.SESSION_HARD:
                integrity = false;
                break;

        }

        switch (membership) {
            case KeySession.SESSION_DYNAMIC_AND_OPEN:
                membershipIdentified = "Dynamic and open";
                break;
            case KeySession.SESSION_DYNAMIC_AND_CLOSE:
                membershipIdentified = "Dynamic and close";
                break;
            case KeySession.SESSION_STATIC:
                membershipIdentified = "Static";
                break;
        }

        this.manager.getClient().saveSearchSession(newSessionName, description, chairman, integrity, max, min, currentMembers, membershipIdentified);
    }

    /**
     * Verifica que exista una sesión de búsqueda
     *
     * @param session            nombre de la sesión
     * @return
     * @throws SQLException
     */
    public boolean existSession(String session) throws SQLException {
        return this.manager.getClient().existSession(session);
    }

    /**
     * Cierra una sesión de búsqueda
     *
     * @param session             nombre de la sesión
     * @throws SQLException
     */
    public void closeCollabSession(String session) throws SQLException {
        this.manager.getClient().closeCollabSession(session);

    }

    /**
     * Devuelve las consultas hechas en una sesión
     *
     * @param session            nombre de la sesión
     * @return
     * @throws SQLException
     */
    public List<String> getAllQueries(String session) throws SQLException {
        return this.manager.getClient().getAllQueries(session);

    }

    /**
     * Devuelve la relación de consultas por usuario hechas en una sesión
     * 
     * @param session           nombre de la sesión
     * @param date              fecha de las búsquedas
     * @return
     * @throws SQLException
     */
    public SeekerQuery getSeekerQueries(String session, Date date) throws SQLException {
        return this.manager.getClient().getSeekerQueries(session, date);

    }

    /**
     * Devuelve todos los usuarios de una sesión
     * 
     * @param session         nombre de la sesión
     * @return
     * @throws SQLException
     */
    public List<String> getAllUsers(String session) throws SQLException {
        return this.manager.getClient().getSessionSeekers(session);

    }

    /**
     * Devuelve todos los objetos seekers de una sesión
     *
     * @param session           nombre de la sesión
     * @return
     * @throws SQLException
     */
    public List<Seeker> getAllSeekers(String session) throws SQLException {
        return this.manager.getClient().loadSeekers(session);

    }

    /**
     * Devuelve las sesiones disponibles por usuario
     * 
     * @param seeker           nombre del usuario
     * @return
     * @throws SQLException
     */
    public List<Session> getAllAvailableSessions(String seeker) throws SQLException {
        return this.manager.getClient().getSeekerAvailableSessions(seeker);

    }

    /**
     * Devuelve todas las sesiones que están activas
     *
     * @return
     * @throws SQLException
     */
    public List<String> getAllEnableSessions() throws SQLException {
        return this.manager.getClient().getAllEnableSessions();

    }

    /**
     * Devuelve todas las sesiones guardadas en la BD
     *
     * @return
     * @throws SQLException
     */
    public List<SessionData> getAllSessions() throws SQLException {
        return this.manager.getClient().getAllSessions();

    }

    /**
     * Guarda una evaluación realizada a un resultado de búsqueda
     * 
     * @param sessionTopic             nombre de la sesión
     * @param comment                  comentario sobre el documento
     * @param relevance                valor de relevancia dado al documento
     * @param user                     nombre del usuario
     * @param uri                      uri del documento
     *
     * @throws SQLException
     */
    public void saveEvaluation(String sessionTopic, String comment, int relevance, String user, String uri) throws SQLException {
        this.manager.getClient().saveEvaluation(sessionTopic, comment, relevance, user, uri);
    }

    /**
     * Actualiza una evaluación realizada a un resultado de búsqueda
     *
     * @param session              nombre de la sesión
     * @param seeker               objeto seeker del usuario
     * @param uri                  uri del documento evaluado
     * @param comment              comentario
     *
     * @throws SQLException
     */
    public void updateEvaluationComment(String session, Seeker seeker, String uri, String comment) throws SQLException {
        this.manager.getClient().updateComment(session, seeker, uri, comment);
    }

    /**
     * Devuelve todos los datos de una sesión de búsqueda
     *
     * @param sessionTopic         nombre de la sesión
     * @return
     * @throws SQLException
     */
    public SessionData getSessionData(String sessionTopic) throws SQLException {
        return this.manager.getClient().getSessionData(sessionTopic);
    }

    /****************************SEEKER*******************************/
    /**
     * Relaciona un usuario con una sesión de búsqueda
     *
     * @param seeker              nombre del usuario
     * @param session             nombre de la sesión
     * @throws SQLException
     */
    public void joinNewSeeker(String seeker, String session) throws SQLException {
        this.manager.getClient().joinNewSeeker(seeker, session);
    }

    /**
     * Devuelve true si la contraseña es la misma que la almcenada en la BD
     *
     * @param user            nombre del usuario
     * @param password        contraseña a comparar
     * @return
     * @throws SQLException
     */
    public boolean compareSeekerPassword(String user, String password) throws SQLException {
        return this.manager.getClient().compareSeekerPassword(user, password);
    }

    /**
     * Devuelve los datos de un usuario
     *
     * @param user              nombre del usuario
     * @return
     * @throws SQLException
     */
    public SeekerData getSeekerData(String user) throws SQLException {
        return this.manager.getClient().getSeekerData(user);
    }

    /**
     * Devuelve el id de un rol
     *
     * @param rol             nombre del rol
     * @return
     * @throws SQLException
     */
    public int getRol(String rol) throws SQLException {
        return this.manager.getClient().getRol(rol);
    }

    /**
     * Devuelve el id de un estado que puede tener el seeker en la sesión
     * 
     * @param state                       nombre del estado
     * @return
     * @throws SQLException
     */
    public int getState(String state) throws SQLException {
        return this.manager.getClient().getState(state);
    }

    /**
     * Actualiza el estado que puede tener el seeker en la sesión
     *
     * @param user           nombre del usuario
     * @param status         id del estado
     * 
     * @throws SQLException
     */
    public void updateSeekerState(String user, int status) throws SQLException {
        String stateIdentified = null;

        switch (status) {

            case Seeker.STATE_AWAY:
                stateIdentified = "Away";
                break;

            case Seeker.STATE_BUSY:
                stateIdentified = "Busy";
                break;

            case Seeker.STATE_OFFLINE:
                stateIdentified = "Offline";
                break;

            case Seeker.STATE_ONLINE:
                stateIdentified = "Online";
                break;
        }


        this.manager.getClient().updateSeekerState(user, stateIdentified);
    }

    /**
     * Actualiza la imagen del usuario
     * 
     * @param user             nombre del usuario
     * @param image            imagen que representa a este usuario
     *
     * @throws SQLException
     */
    public void updateSeekerAvatar(String user, byte[] image) throws SQLException {
        this.manager.getClient().updateSeekerAvatar(user, image);
    }

    /**
     * Devuelve true si el seeker existe
     *
     * @param userName         nombre del seeker
     * @return
     * @throws SQLException
     */
    public boolean existSeeker(String userName) throws SQLException {
        return this.manager.getClient().existSeeker(user);
    }

    /**
     *
     * Guarda los datos de un usuario
     *
     * @param user              nombre del usuario
     * @param name              nombre completo
     * @param password          contraseña
     * @param description       descripción
     * @param userEmail         correo
     * @param avatar            avatar
     * @param rol               rol
     * @param state             estado
     *
     * @throws SQLException
     */
    public void registerSeeker(String user, String name, String password, String description, String userEmail, byte[] avatar, int rol, int state) throws SQLException {

        String rolIdentified = null, stateIdentified = null;
        switch (rol) {

            case Seeker.ROLE_CHAIRMAN:
                rolIdentified = "Chairman";
                break;

            case Seeker.ROLE_MEMBER:
                rolIdentified = "Member";
                break;

            case Seeker.ROLE_POTENTIAL_MEMBER:
                rolIdentified = "Potential Member";
                break;

            default:
                rolIdentified = "Potential Member";
                break;
        }

        switch (state) {

            case Seeker.STATE_AWAY:
                stateIdentified = "Away";
                break;

            case Seeker.STATE_BUSY:
                stateIdentified = "Busy";
                break;

            case Seeker.STATE_OFFLINE:
                stateIdentified = "Offline";
                break;

            case Seeker.STATE_ONLINE:
                stateIdentified = "Online";
                break;

            default:
                stateIdentified = "Offline";
                break;
        }
        this.manager.getClient().registerSeeker(user, name, password, description, userEmail, avatar, rolIdentified, stateIdentified);
    }

    /**
     * Actualiza la contraseña de un usuario
     *
     * @param user                  nombre del usuario
     * @param newPassword           nueva contraseña
     * 
     * @throws SQLException
     */
    public void updateSeekerPassword(String user, String newPassword) throws SQLException {
        this.manager.getClient().updateSeekerPassword(user, newPassword);
    }

    /**
     * Actualiza el rol de un usuario
     *
     * @param user              nombre del usuario
     * @param rol               id del rol
     * @throws SQLException
     */
    public void updateSeekerRol(String user, int rol) throws SQLException {
        String rolIdentified = null;

        switch (rol) {

            case Seeker.ROLE_CHAIRMAN:
                rolIdentified = "Chairman";
                break;

            case Seeker.ROLE_MEMBER:
                rolIdentified = "Member";
                break;

            case Seeker.ROLE_POTENTIAL_MEMBER:
                rolIdentified = "Potential Member";
                break;

            default:
                rolIdentified = "Potential Member";
                break;
        }
        this.manager.getClient().updateSeekerRol(user, rolIdentified);

    }

    /**
     * Devuelve el email del usuario
     *
     * @param user             nombre del usuario
     * @return
     * @throws SQLException
     */
    public String getSeekerEmail(String user) throws SQLException {
        return this.manager.getClient().getSeekerEmail(user);
    }

    /**
     * Dar baja a un usuario de su sesión colaborativa
     *
     * @param sessionTopic        nombre de la sesión
     * @param seekerUser          nombre del usuario
     * @return
     * @throws SQLException
     */
    public boolean declineSeekerCollabSession(String sessionTopic, String seekerUser) throws SQLException {
        return this.getManager().getClient().declineSeekerSession(sessionTopic, seekerUser);
    }

    /**
     * Devuelve true si el usuario pertence a esa sesión
     *
     * @param session          nombre de la sesión
     * @param seekerUser       nombre del usuario
     * @return
     * @throws SQLException
     */
    public boolean verifySeeker(String session, String seekerUser) throws SQLException {
        return this.manager.getClient().verifySeeker(session, seekerUser);
    }

    /*******************************SEARCH********************************************/
    /**
     * Guarda los datos de búsquedas
     *     
     * @param principle      principio de búsqueda utilizado
     * @param listDocs       resultados de búsqueda
     * @param session        nombre de la sesión
     * @param user           nombre del usuario
     *
     * @throws SQLException
     */
    public void saveSearch(String session, String principle, String user, ResultSetMetaData listDocs) throws SQLException {
        String query = null;
        Map<Integer, List<DocumentMetaData>> results;

        //verificar que la búsqueda haya dado algun resultado, sino no la guarda
        if (!listDocs.getAllResultList().isEmpty()) {
            query = listDocs.getQuery();
            results = listDocs.getResultsMap();
            this.manager.getClient().saveSearchData(query, principle, results, user, session);

        }

    }

    /**
     * Devuelve los resultados de búsqueda 
     * 
     * @param list               lista de ids de los documentos
     * @return
     * @throws SQLException
     */
    public List<DocumentMetaData> getSearchResults(List<Integer> list) throws SQLException {
        return this.manager.getClient().getSearchResults(list);
    }

    /**
     * Devuelve los resultados de búsqueda
     * 
     * @param session           nombre de la sesión
     * @param query             consulta
     * @return
     * @throws SQLException
     */
    public List<DocumentMetaData> getSearchResults(String session, String query) throws SQLException {
        return this.manager.getClient().getSearchResults(session, query);
    }

    /**
     * Devuelve los resultados de búsqueda
     *
     * @param query             consulta
     * @param session           nombre de la sesión
     * @param engine            motor de búsqueda
     * @return
     * @throws SQLException
     */
    public List<DocumentMetaData> getSearchResults(String query, String session, int engine) throws SQLException {
        String engineIdentified = null;

        switch (engine) {

            case LUCENE_SEARCH_ENGINE:
                engineIdentified = "lucene";
                break;

            case MINION_SEARCH_ENGINE:
                engineIdentified = "minion";
                break;

            case INDRI_SEARCH_ENGINE:
                engineIdentified = "indri";
                break;

            case TERRIER_SEARCH_ENGINE:
                engineIdentified = "terrier";
                break;

        }
        return this.manager.getClient().getSearchResults(query, session, engineIdentified);
    }

    /**
     * Establece que un documento fue revisado en una sesión
     *
     * @param session         nombre de la sesión
     * @param uri             uri del documento
     * @throws SQLException
     */
    public void setReviewDocument(String session, String uri) throws SQLException {
        this.manager.getClient().setReviewDocument(session, uri);
    }

    /**
     * Devuelve los documentos relevantes dada una consulta y una sesión
     *
     * @param session       nombre de la sesión
     * @param query         consulta
     * @return
     * @throws SQLException
     */
    public List<String> getAllRelevantDocuments(String session, String query) throws SQLException {
        return this.manager.getClient().getAllRelevantDocuments(session, query);
    }

    /******************************RECORDS****************************************/
    /**
     * Obtener las recomendaciones
     *
     * @param session          nombre de la sesión
     * @return
     * @throws SQLException 
     */
    public List<RecommendTracker> getTrackRecommendation(String session) throws SQLException {
        return this.manager.getClient().getAllRecommendations(session);
    }

    /**
     * Obtener las recomendaciones
     *
     * 
     * @param session             nombre de la sesión
     * @param seeker              nombre del usuario que la emitió
     * @return
     * @throws SQLException
     */
    public List<RecommendTracker> getTrackRecommendation(String session, Seeker seeker) throws SQLException {
        return this.manager.getClient().getAllRecommendations(session, seeker);
    }

    /**
     * Obtener las recomendaciones
     *
     *
     * @param session             nombre de la sesión
     * @param date                fecha
     * @return
     * @throws SQLException
     */
    public List<RecommendTracker> getTrackRecommendation(String session, Date date) throws SQLException {
        return this.manager.getClient().getAllRecommendations(session, date);
    }

    /**
     * Obtener las recomendaciones
     *
     *
     * @param session         nombre de la sesión
     * @param query           consulta
     * @return
     * @throws SQLException
     */
    public List<RecommendTracker> getTrackRecommendation(String session, String query) throws SQLException {
        return this.manager.getClient().getAllRecommendations(session, query);
    }

    /**
     * Obtener las recomendaciones
     *
     *
     * @param session              nombre de la sesión
     * @param seeker               nombre del usuario
     * @param date                 fecha
     * @return
     * @throws SQLException
     */
    public List<RecommendTracker> getTrackRecommendation(String session, Seeker seeker, Date date) throws SQLException {
        return this.manager.getClient().getAllRecommendations(session, seeker, date);
    }

    /**
     * Obtener las recomendaciones
     *
     *
     * @param session              nombre de la sesión
     * @param seeker               nombre del usuario
     * @param query                consulta
     * @return
     * @throws SQLException
     */
    public List<RecommendTracker> getTrackRecommendation(String session, Seeker seeker, String query) throws SQLException {
        return this.manager.getClient().getAllRecommendations(session, seeker, query);
    }

    /**
     * Obtener las recomendaciones
     *
     *
     * @param session             nombre de la sesión
     * @param query               consulta
     * @param date                fecha
     * @return
     * @throws SQLException
     */
    public List<RecommendTracker> getTrackRecommendation(String session, String query, Date date) throws SQLException {
        return this.manager.getClient().getAllRecommendations(session, query, date);
    }

    /**
     * Obtener las recomendaciones
     *
     *
     * @param session              nombre de la sesión
     * @param seeker               nombre del usuario
     * @param query                consulta
     * @param date                 fecha
     * @return
     * @throws SQLException
     */
    public List<RecommendTracker> getTrackRecommendation(String session, Seeker seeker, String query, Date date) throws SQLException {
        return this.manager.getClient().getAllRecommendations(session, seeker, query, date);
    }

    /**
     * Obtener las búsquedas
     *
     * @param sessionName         nombre de la sesión
     * @param group               identificador de los documentos (revisados, relevantes o todos)
     * @return
     * @throws SQLException
     */
    public List<SearchTracker> getTrackSearch(String sessionName, int group) throws SQLException {
        return this.manager.getClient().getAllSearches(sessionName, group);
    }

    /**
     * Obtener las búsquedas
     *
     * @param sessionName        nombre de la sesión
     * @param seeker             nombre del usuario
     * @param group              identificador de los documentos (revisados, relevantes o todos)
     * @return
     * @throws SQLException
     */
    public List<SearchTracker> getTrackSearch(String sessionName, Seeker seeker, int group) throws SQLException {
        return this.manager.getClient().getAllSearches(sessionName, group, seeker);
    }

    /**
     * Obtener las búsquedas
     *
     * @param sessionName        nombre de la sesión
     * @param date               fecha
     * @param group              identificador de los documentos (revisados, relevantes o todos)
     * @return
     * @throws SQLException
     */
    public List<SearchTracker> getTrackSearch(String sessionName, Date date, int group) throws SQLException {
        return this.manager.getClient().getAllSearches(sessionName, group, date);
    }

    /**
     * Obtener las búsquedas
     *
     * @param sessionName       nombre de la sesión
     * @param query             consulta
     * @param group             identificador de los documentos (revisados, relevantes o todos)
     * @return
     * @throws SQLException
     */
    public List<SearchTracker> getTrackSearch(String sessionName, String query, int group) throws SQLException {
        return this.manager.getClient().getAllSearches(sessionName, group, query);
    }

    /**
     * Obtener las búsquedas
     *
     * @param sessionName        nombre de la sesión
     * @param seeker             nombre del usuario
     * @param query              consulta
     * @param group              identificador de los documentos (revisados, relevantes o todos)
     * @return
     * @throws SQLException
     */
    public List<SearchTracker> getTrackSearch(String sessionName, Seeker seeker, String query, int group) throws SQLException {
        return this.manager.getClient().getAllSearches(sessionName, group, seeker, query);
    }

    /**
     * Obtener las búsquedas
     *
     * @param sessionName        nombre de la sesión
     * @param query              consulta
     * @param date               fecha
     * @param group              identificador de los documentos (revisados, relevantes o todos)
     * @return
     * @throws SQLException
     */
    public List<SearchTracker> getTrackSearch(String sessionName, String query, Date date, int group) throws SQLException {
        return this.manager.getClient().getAllSearches(sessionName, group, query, date);
    }

    /**
     * Obtener las búsquedas
     *
     * @param sessionName        nombre de la sesión
     * @param seeker             nombre del usuario
     * @param date               fecha
     * @param group              identificador de los documentos (revisados, relevantes o todos)
     * @return
     * @throws SQLException
     */
    public List<SearchTracker> getTrackSearch(String sessionName, Seeker seeker, Date date, int group) throws SQLException {
        return this.manager.getClient().getAllSearches(sessionName, group, seeker, date);
    }

    /**
     * Obtener las búsquedas
     *
     * @param sessionName        nombre de la sesión
     * @param seeker             usuario
     * @param date               fecha
     * @param query              consulta
     * @param group              identificador de los documentos (revisados, relevantes o todos)
     * @return
     * @throws SQLException 
     */
    public List<SearchTracker> getTrackSearch(String sessionName, Seeker seeker, Date date, String query, int group) throws SQLException {
        return this.manager.getClient().getAllSearches(sessionName, group, seeker, date, query);
    }

    /**
     * Devuelve los datos referidos a la configuración del correo
     *
     * @return
     */
    public DrakkarSternEmail getEmail() {
        return email;
    }

    /**
     * Actualiza los datos referidos a la configuración del correo
     *
     * @param email
     */
    public void setEmail(DrakkarSternEmail email) {
        this.email = email;
    }

    /**
     * Lee el contenido de un file
     *
     * @param file    file a leer
     * @return
     */
    public static String readFile(File file) {
        String result = " ";
        char c;

        FileInputStream in = null;
        try {
            in = new FileInputStream(file);
        } catch (FileNotFoundException ex) {
            OutputMonitor.printStream("",ex);
        }

        int buffer;

        try {
            while ((buffer = in.read()) != -1) {
                c = (char) buffer;
                result = result.concat(String.valueOf(c));
            }
            in.close();
        } catch (IOException e) {
            OutputMonitor.printStream("", e);

        }

        return result;
    }

    /**
     * Devuelve true si la BD existe
     *
     * @return
     */
    public boolean existDatabase() {

        File f = new File(this.location);
        if (f.exists() && f.list().length != 0) {
//            if (convertToList(f.list()).contains("service.properties")) {
            if (Arrays.asList(f.list()).contains("service.properties")) {
                return true;
            }
        }


        return false;
    }


    // esto está demás
//    private List<String> convertToList(String[] list) {
//        List<String> array = new List<String>(list.length);
//
//        for (int i = 0; i < list.length; i++) {
//            String string = list[i];
//            array.add(string);
//        }
//
//        return array;
//
//    }

    /**
     * Devuelve la ubicación de la BD
     *
     * @return
     */
    public String getLocation() {
        return location;
    }
}
