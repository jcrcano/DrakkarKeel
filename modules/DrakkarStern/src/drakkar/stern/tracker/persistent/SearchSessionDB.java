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

import drakkar.oar.MarkupData;
import drakkar.oar.SearchResultData;
import drakkar.oar.Seeker;
import drakkar.oar.Session;
import drakkar.oar.util.OutputMonitor;
import drakkar.stern.tracker.persistent.objects.SeekerData;
import drakkar.stern.tracker.persistent.objects.SessionData;
import drakkar.stern.tracker.persistent.tables.DerbyConnection;
import drakkar.stern.tracker.persistent.tables.PersistentOperations;
import drakkar.stern.tracker.persistent.tables.TableTracker;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase que contiene los métodos relacionados con la persistencia de las sesiones
 * de búsqueda
 */
public class SearchSessionDB {

    DerbyConnection connection;
    DBUtil util;
    SeekerDB dbSeeker;

    /**
     * Constructor de la clase
     *
     * @param connection             objeto que representa la conexión con Derby
     * @param util                   objeto que contiene los métodos utilitarios para interactuar con la BD
     * @param dbSeeker               objeto que contiene los métodos referentes a los datos persistentes del usuario
     */
    public SearchSessionDB(DerbyConnection connection, DBUtil util, SeekerDB dbSeeker) {
        this.connection = connection;
        this.util = util;
        this.dbSeeker = dbSeeker;
    }

    /*******************SEARCH SESSION********************/
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
    @SuppressWarnings({"static-access", "static-access"})
    public boolean saveSearchSession(String topic, String description, String chairman, boolean criteria, int maxMember, int minMember, List<Seeker> members, String membership) throws SQLException {

        String[] fields = new String[10];
        fields[0] = "SESSION_TOPIC";
        fields[1] = "DESCRIPTION";
        fields[2] = "CHAIRMAN";
        fields[3] = "INTEGRITY_CRITERIA";
        fields[4] = "MAX_MEMBER_NUMBER";
        fields[5] = "MIN_MEMBER_NUMBER";
        fields[6] = "CURRENT_MEMBER_NUMBER";
        fields[7] = "ID_MSHIP";
        fields[8] = "START_DATE";
        fields[9] = "ENABLE";

        Object[] oneValue = new Object[10];
        oneValue[0] = topic;
        oneValue[1] = description;
        oneValue[2] = chairman;
        oneValue[3] = criteria;
        oneValue[4] = maxMember;
        oneValue[5] = minMember;
        oneValue[6] = members.size();
        oneValue[7] = getMembership(membership);
        oneValue[8] = DBUtil.getCurrentDate();
        oneValue[9] = true;

        if (!util.alreadyExist(topic, "DRAKKARKEEL.SEARCH_SESSION", "SESSION_TOPIC", "CHAIRMAN")) {
            PersistentOperations.insert(connection, "DRAKKARKEEL.SEARCH_SESSION", fields, oneValue);

            //relacionar el chairman con su sesión
            this.joinNewSeeker(chairman, topic);
            dbSeeker.updateSeekerRol(chairman, "Chairman");

            //relacionar los usuarios seleccionados a la sesión
            this.joinNewSeeker(members, topic);
            return true;
        }

        return false;
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

        String[] fields = new String[1];
        fields[0] = "ID_MSHIP";
        String result = null;
        int id = 0;
        @SuppressWarnings("static-access")
        TableTracker table = PersistentOperations.load(connection, fields, "DRAKKARKEEL.MEMBERSHIP", "MSHIP_TYPE", type);

        Object[][] values = table.getValues();

        if (values.length != 0) {
            result = values[0][0].toString();
            id = Integer.parseInt(result);
        }

        return id;
    }

    /**
     * Elimina una sesión de búsqueda
     *
     * @param session                nombre de la sesión
     *
     * @throws SQLException          si ocurre alguna SQLException durante la ejecución
     *                               de la operación
     */
    @SuppressWarnings("static-access")
    public void deleteSearchSession(String session) throws SQLException {

        if (!util.alreadyExist(session, "DRAKKARKEEL.SEARCH_SESSION", "SESSION_TOPIC", "CHAIRMAN")) {
            PersistentOperations.delete(connection, "DRAKKARKEEL.SEARCH_SESSION", "SESSION_TOPIC", session);
        }
    }

    /**
     *  Carga los nombres de los usuarios registrados que están conectados
     *  pertenezcan o no a sesiones de búsqueda
     *
     * @return
     * @throws SQLException
     */
    public List<SeekerData> loadUsersOnline() throws SQLException {
        List<SeekerData> list = null;
        String user;
        SeekerData data;

        String[] fields = new String[1];
        fields[0] = "SEEKER_USER";
        @SuppressWarnings("static-access")
        TableTracker table = PersistentOperations.load(connection, fields, "DRAKKARKEEL.SEEKER", "ID_STATE", dbSeeker.getState("Online"));

        Object[][] values = table.getValues();

        if (values.length != 0) {
            user = values[0][0].toString();
            data = dbSeeker.getSeekerData(user);
            list.add(data);
        }

        return list;
    }

    /**
     *  Carga los datos de los usuarios que pertencen a determinada sesión
     *
     * @param session            nombre de la sesión
     * 
     * @return                   devuelve una lista de objetos Seekers
     *
     * @throws SQLException      si ocurre alguna SQLException durante la ejecución
     *                           de la operación
     */
    public List<Seeker> loadSeekers(String session) throws SQLException {
        List<Seeker> list = new ArrayList<>();
        String[] fields = new String[1];
        fields[0] = "SEEKER_USER";
        String seekerUser = null;
        SeekerData seekerData;
        Seeker seeker;
//        BufferedImage buffer;
        @SuppressWarnings("static-access")
        TableTracker table = PersistentOperations.load(connection, fields, "DRAKKARKEEL.SEARCH_SESSION_SEEKER", "SESSION_TOPIC", session);
        Object[][] values = table.getValues();

        if (values.length != 0) {
            for (int i = 0; i < values.length; i++) {
                Object[] objects = values[i];

                seekerUser = objects[0].toString();
                seekerData = dbSeeker.getSeekerData(seekerUser);
//                buffer = ImageUtil.toBufferedImage(seekerData.getAvatar());

//                seeker = new Seeker(seekerData.getUser(), seekerData.getRol(), Seeker.STATE_OFFLINE, new ImageIcon(buffer));
                seeker = new Seeker(seekerData.getUser(), seekerData.getRol(), Seeker.STATE_OFFLINE, seekerData.getAvatar());
                list.add(seeker);
            }

        }

        return list;
    }

    /**
     * Carga los datos de los seekers de una sesión
     *
     * @param session              nombre de la sesión
     * 
     * @return                     devuelve una lista de objetos SeekerData
     * 
     * @throws SQLException        si ocurre alguna SQLException durante la ejecución
     *                             de la operación
     */
    public List<SeekerData> loadSeekersData(String session) throws SQLException {
        List<SeekerData> list = new ArrayList<>();
        String[] fields = new String[1];
        fields[0] = "SEEKER_USER";
        String result = null;
        SeekerData seekerData;
        // Seeker seeker;
        @SuppressWarnings("static-access")
        TableTracker table = PersistentOperations.load(connection, fields, "DRAKKARKEEL.SEARCH_SESSION_SEEKER", "SESSION_TOPIC", session);
        Object[][] values = table.getValues();

        if (values.length != 0) {
            result = values[0][0].toString();
            seekerData = dbSeeker.getSeekerData(result);
            list.add(seekerData);
        }

        return list;
    }

    /**
     * Relaciona un usuario a una Sesión de búsqueda, actualiza el rol del usuario a Miembro
     *
     * @param seeker               nombre del usuario
     * @param session              nombre de la sesión
     *
     * @throws SQLException        si ocurre alguna SQLException durante la ejecución
     *                             de la operación
     */
    @SuppressWarnings("static-access")
    public void joinNewSeeker(String seeker, String session) throws SQLException {

        String[] fields = new String[3];
        fields[0] = "SESSION_TOPIC";
        fields[1] = "SEEKER_USER";
        fields[2] = "DECLINE";

        Object[] oneValue = new Object[3];
        oneValue[0] = session;
        oneValue[1] = seeker;
        oneValue[2] = false;

        if (!util.relationExist("DRAKKARKEEL.SEARCH_SESSION_SEEKER", "SESSION_TOPIC", session, "SEEKER_USER", seeker)) {
            PersistentOperations.insert(connection, "DRAKKARKEEL.SEARCH_SESSION_SEEKER", fields, oneValue);

            //actualiza el rol del seeker
            dbSeeker.updateSeekerRol(seeker, "Member");
        }
    }

    /**
     * Relaciona varios seekers a una sesión
     *
     * @param seekers                lista de usuarios
     * @param session                nombre de la sesión
     * 
     * @throws SQLException          si ocurre alguna SQLException durante la ejecución
     *                               de la operación
     */
    public void joinNewSeeker(List<Seeker> seekers, String session) throws SQLException {

        for (int i = 0; i < seekers.size(); i++) {
            String string = seekers.get(i).getUser();
            joinNewSeeker(string, session);
        }
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

        List<SessionData> list = new ArrayList<>();
        SessionData data;
        String topic, desc, chairman, startDate, stopDate = null;
        int maxMember, minMember, currMember, membership = 0;
        boolean criteria;
        boolean enable;
        @SuppressWarnings("static-access")
        TableTracker table = PersistentOperations.load(connection, "DRAKKARKEEL.SEARCH_SESSION");

        Object[][] values = table.getValues();
        if (values.length != 0) {
            for (int i = 0; i < values.length; i++) {
                Object[] objects = values[i];
                topic = objects[0].toString();
                if (!topic.equals("DefaultSession")) {
                    desc = objects[1].toString();
                    chairman = objects[2].toString();
                    criteria = Boolean.parseBoolean(objects[3].toString());
                    maxMember = Integer.parseInt(objects[4].toString());
                    minMember = Integer.parseInt(objects[5].toString());
                    currMember = Integer.parseInt(objects[6].toString());
                    enable = Boolean.parseBoolean(objects[7].toString());
                    startDate = objects[8].toString();
                    stopDate = objects[9].toString();
                    membership = Integer.parseInt(objects[10].toString());

                    data = new SessionData(topic, desc, criteria, maxMember, minMember, currMember, startDate, stopDate, membership, chairman, enable);
                    list.add(data);
                }


            }

        }

        return list;
    }

    /**
     * Guarda los mensajes enviados por un usuario
     *
     * @param session          nombre de la sesión
     * @param user             usuario que envió el mensaje
     * @param text             texto del mensaje
     * @param receptor         lista de receptores a quienes fue enviado
     *
     * @throws SQLException    si ocurre alguna SQLException durante la ejecución
     *                         de la operación
     */
    @SuppressWarnings({"static-access", "static-access"})
    public void saveMessages(String session, String user, String text, List<Seeker> receptor) throws SQLException {

        String[] fields = new String[4];
        fields[0] = "TEXT";
        fields[1] = "SEEKER_RECEPTOR";
        fields[2] = "SEEKER_USER";
        fields[3] = "SESSION_TOPIC";

        if (receptor.size() == 1) {
            Object[] oneValue = new Object[4];
            oneValue[0] = text;
            oneValue[1] = receptor.get(0).getUser();
            oneValue[2] = user;
            oneValue[3] = session;

            PersistentOperations.insert(connection, "DRAKKARKEEL.MESSAGE", fields, oneValue);
        } else if (receptor.size() > 1) {

            for (int i = 0; i < receptor.size(); i++) {
                Object[] oneValue = new Object[4];
                oneValue[0] = text;
                oneValue[1] = receptor.get(i).getUser();
                oneValue[2] = user;
                oneValue[3] = session;

                PersistentOperations.insert(connection, "DRAKKARKEEL.MESSAGE", fields, oneValue);
            }

        } else if (receptor.isEmpty()) {
            OutputMonitor.printLine("La lista de usuarios receptores está vacía", OutputMonitor.ERROR_MESSAGE);
        }



    }

    /**
     * Guarda el mensaje enviado a un receptor
     *
     * @param session          nombre de la sesión
     * @param user             usuario que envió el mensaje
     * @param text             texto del mensaje
     * @param receptor         receptor a quien fue enviado
     * 
     * @throws SQLException    si ocurre alguna SQLException durante la ejecución
     *                         de la operación
     */
    @SuppressWarnings("static-access")
    public void saveMessages(String session, String user, String text, Seeker receptor) throws SQLException {

        String[] fields = new String[4];
        fields[0] = "TEXT";
        fields[1] = "SEEKER_RECEPTOR";
        fields[2] = "SEEKER_USER";
        fields[3] = "SESSION_TOPIC";

        Object[] oneValue = new Object[4];
        oneValue[0] = text;
        oneValue[1] = receptor.getUser();
        oneValue[2] = user;
        oneValue[3] = session;

        PersistentOperations.insert(connection, "DRAKKARKEEL.MESSAGE", fields, oneValue);

    }

    /**
     * Obtiene todos los datos de una sesión de búsqueda
     *
     * @param session               nombre de la sesión
     * 
     * @return                      objeto que contiene datos de la sesión
     *
     * @throws SQLException         si ocurre alguna SQLException durante la ejecución
     *                              de la operación
     */
    public SessionData getSessionData(String session) throws SQLException {

        SessionData data = null;
        String topic = null, descrip = null, chairman = null;
        int maxMember = 0, minMember = 0, currMember = 0, membership = 0;
        String start = null, stop = null;
        boolean criteria = false;
        boolean enable = false;
        final int SESSION_SOFT = 1;
        final int SESSION_HARD = 0;

        String[] fields = new String[11];
        fields[0] = "SESSION_TOPIC";
        fields[1] = "DESCRIPTION";
        fields[2] = "CHAIRMAN";
        fields[3] = "INTEGRITY_CRITERIA";
        fields[4] = "MAX_MEMBER_NUMBER";
        fields[5] = "MIN_MEMBER_NUMBER";
        fields[6] = "CURRENT_MEMBER_NUMBER";
        fields[7] = "START_DATE";
        fields[8] = "STOP_DATE";
        fields[9] = "ID_MSHIP";
        fields[10] = "ENABLE";
        @SuppressWarnings("static-access")
        TableTracker table = PersistentOperations.load(connection, fields, "DRAKKARKEEL.SEARCH_SESSION", "SESSION_TOPIC", session);

        Object[][] values = table.getValues();

        if (values.length != 0) {
            int integrity = 0;

            for (int i = 0; i < values.length; i++) {
                Object[] obj = values[i];
                topic = obj[0].toString();
                descrip = obj[1].toString();
                chairman = obj[2].toString();
                integrity = Integer.valueOf(obj[3].toString());
                maxMember = Integer.valueOf(obj[4].toString());
                minMember = Integer.valueOf(obj[5].toString());
                currMember = Integer.valueOf(obj[6].toString());
                start = obj[7].toString();
                stop = obj[8].toString();
                membership = Integer.valueOf(obj[9].toString());
                enable = Boolean.parseBoolean(obj[10].toString());

                if (integrity == SESSION_SOFT) {
                    criteria = true;
                } else if (integrity == SESSION_HARD) {
                    criteria = false;
                }

                data = new SessionData(topic, descrip, criteria, maxMember, minMember, currMember, start, stop, membership, chairman, enable);

            }
        }

        return data;
    }

    /**
     * Obtener las sesiones activas por usuario
     *
     * @param seeker                nombre del usuario
     * 
     * @return                      lista con los datos de las sesiones
     * 
     * @throws SQLException         si ocurre alguna SQLException durante la ejecución
     *                              de la operación
     */
    public List<Session> getSeekerAvailableSessions(String seeker) throws SQLException {

        List<Session> list = new ArrayList<>();

        String topic = null, description = null, chairman = null;

        String[] fields = new String[3];
        fields[0] = "DRAKKARKEEL.SEARCH_SESSION_SEEKER.SESSION_TOPIC";
        fields[1] = "DESCRIPTION";
        fields[2] = "CHAIRMAN";

        String[] whereFields = new String[2];
        whereFields[0] = "SEEKER_USER";
        whereFields[1] = "DECLINE";

        Object[] whereValues = new Object[2];
        whereValues[0] = seeker;
        whereValues[1] = false;
        @SuppressWarnings("static-access")       
        TableTracker table = PersistentOperations.load(connection, fields, "DRAKKARKEEL.SEARCH_SESSION", "DRAKKARKEEL.SEARCH_SESSION_SEEKER", "SESSION_TOPIC", "SESSION_TOPIC", whereFields, whereValues);

        Object[][] values = table.getValues();

        if (values.length != 0) {
            for (int i = 0; i < values.length; i++) {
                Object[] obj = values[i];
                topic = obj[0].toString();
                description = obj[1].toString();
                chairman = obj[2].toString();
                list.add(new Session(topic, description, chairman));
            }
        }


        return list;
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
        if (util.alreadyExist(session, "DRAKKARKEEL.SEARCH_SESSION", "SESSION_TOPIC", "CHAIRMAN")) {
            return true;
        }
        return false;
    }

    /**
     * Obtener las evaluaciones hechas a un resultado de búsqueda.
     *
     * @param result                objeto que representa el resultado de búsqueda
     * @param session               nombre de la sesión
     * 
     * @return                      devuelve una lista con los datos de todas las evaluaciones hechas a un resultado
     * 
     * @throws SQLException         si ocurre alguna SQLException durante la ejecución
     *                               de la operación
     */
    public List<MarkupData> getEvaluations(SearchResultData result, String session) throws SQLException {

        List<MarkupData> list = new ArrayList<>();
        MarkupData data = null;
        byte relevance;
        String comment, user, date = null;

        String[] fields = new String[4];
        fields[0] = "COMMENT";
        fields[1] = "RELEVANCE";
        fields[2] = "SEEKER_USER";
        fields[3] = "MARKUP_DATE";

        String[] whereFields = new String[2];
        whereFields[0] = "ID_SR";
        whereFields[1] = "SESSION_TOPIC";

        Object[] whereValues = new Object[2];
        whereValues[0] = result.getId();
        whereValues[1] = session;

        if (util.alreadyExist(result.getId(), "DRAKKARKEEL.MARKUP", "ID_SR", "ID_MARKUP")) {
            @SuppressWarnings("static-access")
            TableTracker table = PersistentOperations.load(connection, fields, "DRAKKARKEEL.MARKUP", whereFields, whereValues);

            Object[][] values = table.getValues();

            if (values.length != 0) {
                for (int i = 0; i < values.length; i++) {
                    Object[] obj = values[i];
                    comment = obj[0].toString();
                    relevance = Byte.valueOf(obj[1].toString());
                    user = obj[2].toString();
                    date = obj[3].toString();

                    data = new MarkupData(comment, relevance, user, result.getURI(), date);
                    list.add(data);
                }

            }
        }


        return list;
    }

    
//      Nota: Para obtener todos los votos de relevancia dados a un documento
//      y obtener todos los comentarios dados a un documento usar el método
//      anterior.
     
     
    /**
     *
     * Obtener todos los usuarios que pertenecen a una sesión
     *
     * @param session              nombre de la sesión
     * 
     * @return                     devuelve una lista con los nombres de los usuarios de la sesión
     *
     * @throws SQLException        si ocurre alguna SQLException durante la ejecución
     *                             de la operación
     */
    public List<String> getSessionSeekers(String session) throws SQLException {
        List<String> list = new ArrayList<>();
        String seeker;

        String[] fields = new String[1];
        fields[0] = "SEEKER_USER";
        @SuppressWarnings("static-access")
        TableTracker table = PersistentOperations.load(connection, fields, "DRAKKARKEEL.SEARCH_SESSION_SEEKER", "SESSION_TOPIC", session);

        Object[][] values = table.getValues();

        if (values.length != 0) {
            for (int i = 0; i < values.length; i++) {
                seeker = values[i][0].toString();
                list.add(seeker);
            }
        }

        return list;
    }

    /**
     * Finaliza una sesión
     *
     * @param sessionTopic           nombre de la sesión
     * 
     * @throws SQLException          si ocurre alguna SQLException durante la ejecución
     *                               de la operación
     */
    @SuppressWarnings("static-access")
    public void closeCollabSession(String sessionTopic) throws SQLException {
        String[] fields = new String[2];
        fields[0] = "STOP_DATE";
        fields[1] = "ENABLE";

        Object[] values = new Object[2];
        values[0] = DBUtil.getCurrentDate();
        values[1] = false;

        PersistentOperations.update(connection, "DRAKKARKEEL.SEARCH_SESSION", fields, values, "SESSION_TOPIC", sessionTopic);

        //all members of this session decline state to true
        List<String> seekers = this.getSessionSeekers(sessionTopic);

        for (int i = 0; i < seekers.size(); i++) {
            String seeker = seekers.get(i);
            this.dbSeeker.declineSeekerSession(sessionTopic, seeker);

        }


    }

    /**
     * Obtener todas las sesiones activas
     *
     * @return                    devuelve una lista de los nombres de las sesiones activas
     *
     * @throws SQLException       si ocurre alguna SQLException durante la ejecución
     *                            de la operación
     */
    public List<String> getAllEnableSessions() throws SQLException {
        List<String> list = new ArrayList<>();
        String session;

        String[] fields = new String[1];
        fields[0] = "SESSION_TOPIC";
        @SuppressWarnings("static-access")
        TableTracker table = PersistentOperations.load(connection, fields, "DRAKKARKEEL.SEARCH_SESSION", "ENABLE", true);
        Object[][] values = table.getValues();

        if (values.length != 0) {
            for (int i = 0; i < values.length; i++) {
                Object[] objects = values[i];
                session = objects[0].toString();
                list.add(session);
            }
        }

        return list;
    }
}
