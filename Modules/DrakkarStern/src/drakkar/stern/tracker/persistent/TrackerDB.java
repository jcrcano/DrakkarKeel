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
import drakkar.oar.RecommendTracker;
import drakkar.oar.SearchResultData;
import drakkar.oar.SearchTracker;
import drakkar.oar.Seeker;
import drakkar.oar.util.KeySearchable;
import drakkar.oar.util.KeySession;
import drakkar.oar.util.SeekerAction;
import drakkar.stern.tracker.persistent.objects.SeekerData;
import drakkar.stern.tracker.persistent.tables.DerbyConnection;
import drakkar.stern.tracker.persistent.tables.PersistentOperations;
import drakkar.stern.tracker.persistent.tables.TableTracker;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase que contiene los métodos relacionados con la persistencia de los historiales
 */
public class TrackerDB {

    DerbyConnection connection;
    DBUtil util;
    SearchDB dbSearch;
    SearchSessionDB dbSession;

//    public TrackerDB(DerbyConnection conn, SearchDB dbSearch, SearchSessionDB session){
//        this.connection = conn;
//        this.dbSearch = dbSearch;
//        this.dbSession = session;
//    }
    /**
     * Constructor de la clase
     * 
     * @param connection           objeto que representa la conexión con Derby
     * @param util                 objeto que contiene los métodos utilitarios para interactuar con la BD
     * @param dbSearch             objeto que contiene los métodos referentes a las búsquedas persistentes
     * @param dbSession            objeto que contiene los métodos referentes a las sesiones persistentes
     */
    public TrackerDB(DerbyConnection connection, DBUtil util, SearchDB dbSearch, SearchSessionDB dbSession) {

        this.connection = connection;
        this.util = util;
        this.dbSearch = dbSearch;
        this.dbSession = dbSession;

    }

    /*****************************RECORD TABLES*********************/
    ////////////////////RECOMMEND//////////////
    /**
     * Obtiene una lista las recomendaciones realizadas en una sesión.
     *
     * @param session          nombre de la sesión
     *
     * @return                 devuelve una lista con los datos de las recomendaciones realizadas en una sesión
     *
     * @throws SQLException     si ocurre alguna SQLException durante la ejecución
     *                               de la operación
     */
    public List<RecommendTracker> getAllRecommendations(String session) throws SQLException {
        RecommendTracker data;
        List<RecommendTracker> list = new ArrayList<>();
        List<SeekerData> seekers = new ArrayList<>();
        List<String> usersReceptors = new ArrayList<>();
        String[] fields;

        int idDoc;
        String text, uri = null, date = null;
        //get all users for this session
        seekers = dbSession.loadSeekersData(session);

        for (int i = 0; i < seekers.size(); i++) {
            SeekerData seekerData = seekers.get(i);

            fields = new String[3];
            fields[0] = "ID_SR";
            fields[1] = "TEXT";
            fields[2] = "REC_DATE";

            if (util.alreadyExist(seekerData.getUser(), "DRAKKARKEEL.RECOMMENDATION", "SEEKER_USER", "ID_REC")) {
                //Get recommendations made by each seeker
                TableTracker table = PersistentOperations.load(connection, fields, "DRAKKARKEEL.RECOMMENDATION", "SEEKER_USER", seekerData.getUser());
                Object[][] values = table.getValues();

                if (values.length != 0) {
                    for (int j = 0; j < values.length; j++) {
                        Object[] obj = values[j];
                        idDoc = Integer.parseInt(obj[0].toString());
                        uri = dbSearch.getResult(idDoc, session).getURI();
                        text = obj[1].toString();
                        date = obj[2].toString();

                        usersReceptors = getRecommendationReceptors(idDoc, seekerData.getUser());

                        data = new RecommendTracker(usersReceptors, seekerData.getUser(), date, text, uri, idDoc, KeySession.MULTIPLE_QUERIES, KeySearchable.MULTIPLE_SEARCHERS);
                        list.add(data);
                    }
                }
            }

        }

        return list;
    }

    /**
     * Obtiene una lista las recomendaciones realizadas en una sesión.
     *
     * @param session               nombre de la sesión
     * @param seeker                nombre del usuario que emitió la recomendación
     *
     * @return                 devuelve una lista con los datos de las recomendaciones realizadas en una sesión
     *
     * @throws SQLException     si ocurre alguna SQLException durante la ejecución
     *                               de la operación
     */
    public List<RecommendTracker> getAllRecommendations(String session, Seeker seeker) throws SQLException {

        RecommendTracker data;
        List<RecommendTracker> list = new ArrayList<>();
        List<String> usersReceptors = new ArrayList<>();
        int doc;
        String text = null, uri = null, date = null;

        //verify sessions
        String[] fields = new String[3];
        fields[0] = "ID_SR";
        fields[1] = "TEXT";
        fields[2] = "REC_DATE";

        String[] whereFields = new String[2];
        whereFields[0] = "SESSION_TOPIC";
        whereFields[1] = "SEEKER_USER";

        Object[] whereValues = new Object[2];
        whereValues[0] = session;
        whereValues[1] = seeker.getUser();

        if (util.alreadyExist(seeker.getUser(), "DRAKKARKEEL.RECOMMENDATION", "SEEKER_USER", "ID_REC")) {
            TableTracker table = PersistentOperations.load(connection, fields, "DRAKKARKEEL.RECOMMENDATION", whereFields, whereValues);
            Object[][] values = table.getValues();

            if (values.length != 0) {
                for (int i = 0; i < values.length; i++) {
                    Object[] obj = values[i];
                    doc = Integer.valueOf(obj[0].toString());
                    text = obj[1].toString();
                    date = obj[2].toString();
                    usersReceptors = getRecommendationReceptors(doc, seeker.getUser());
                    uri = dbSearch.getResult(doc, session).getURI();
                    data = new RecommendTracker(usersReceptors, seeker.getUser(), date, text, uri, doc, KeySession.MULTIPLE_QUERIES, KeySearchable.MULTIPLE_SEARCHERS);
                    list.add(data);
                }
            }
        }


        return list;
    }

    /**
     * Obtiene una lista las recomendaciones realizadas en una sesión
     *
     * @param session             nombre de la sesión
     * @param date                fecha de la recomendación
     * 
     * @return                 devuelve una lista con los datos de las recomendaciones realizadas en una sesión
     *
     * @throws SQLException     si ocurre alguna SQLException durante la ejecución
     *                               de la operación
     */
    public List<RecommendTracker> getAllRecommendations(String session, Date date) throws SQLException {

        RecommendTracker data;
        List<RecommendTracker> list = new ArrayList<>();
        List<String> usersReceptors = new ArrayList<>();
        int doc;
        String text, seeker, uri = null;

        String[] fields = new String[3];
        fields[0] = "ID_SR";
        fields[1] = "TEXT";
        fields[2] = "SEEKER_USER";

        String[] whereFields = new String[2];
        whereFields[0] = "SESSION_TOPIC";
        whereFields[1] = "REC_DATE";

        Object[] whereValues = new Object[2];
        whereValues[0] = session;
        whereValues[1] = date;

        if (util.alreadyExist(date, "DRAKKARKEEL.RECOMMENDATION", "REC_DATE", "ID_REC")) {
            TableTracker table = PersistentOperations.load(connection, fields, "DRAKKARKEEL.RECOMMENDATION", whereFields, whereValues);

            Object[][] values = table.getValues();

            if (values.length != 0) {
                for (int i = 0; i < values.length; i++) {
                    Object[] obj = values[i];
                    doc = Integer.valueOf(obj[0].toString());
                    text = obj[1].toString();
                    seeker = obj[2].toString();
                    usersReceptors = getRecommendationReceptors(doc, seeker);
                    uri = dbSearch.getResult(doc, session).getURI();

                    data = new RecommendTracker(usersReceptors, seeker, date.toString(), text, uri, doc, KeySession.MULTIPLE_QUERIES, KeySearchable.MULTIPLE_SEARCHERS);
                    list.add(data);
                }
            }

        }
        return list;
    }

    /**
     * Obtiene una lista las recomendaciones realizadas en una sesión.
     *
     * @param session             nombre de la sesión
     * @param query               consulta
     * 
     * @return                 devuelve una lista con los datos de las recomendaciones realizadas en una sesión
     *
     * @throws SQLException     si ocurre alguna SQLException durante la ejecución
     *                               de la operación
     */
    public List<RecommendTracker> getAllRecommendations(String session, String query) throws SQLException {

        RecommendTracker data;
        List<RecommendTracker> list = new ArrayList<>();
        List<String> usersReceptors = new ArrayList<>();
        List<Integer> documents = new ArrayList<>();
        String text = null, seeker = null, date = null;
        int idDoc;

        //get all results for all the ocurrences of that query
        documents = getQueryOcurrences(query);

        //get docs recommended in that session
        for (int i = 0; i < documents.size(); i++) {
            idDoc = documents.get(i);

            if (util.alreadyExist(idDoc, "DRAKKARKEEL.RECOMMENDATION", "ID_SR", "SEEKER_USER")) {
                String[] fields2 = new String[3];
                fields2[0] = "TEXT";
                fields2[1] = "SEEKER_USER";
                fields2[2] = "REC_DATE";

                String[] whereFields = new String[2];
                whereFields[0] = "SESSION_TOPIC";
                whereFields[1] = "ID_SR";

                Object[] whereValues = new Object[2];
                whereValues[0] = session;
                whereValues[1] = idDoc;

                TableTracker table2 = PersistentOperations.load(connection, fields2, "DRAKKARKEEL.RECOMMENDATION", whereFields, whereValues);

                Object[][] val = table2.getValues();
                if (val.length != 0) {
                    for (int j = 0; j < val.length; j++) {
                        Object[] objects = val[j];
                        text = objects[0].toString();
                        seeker = objects[1].toString();
                        date = objects[2].toString();
                        usersReceptors = getRecommendationReceptors(idDoc, seeker);

                        String uri = dbSearch.getResult(idDoc, session).getURI();
                        data = new RecommendTracker(usersReceptors, seeker, date, text, uri, idDoc, query, KeySearchable.MULTIPLE_SEARCHERS);
                        list.add(data);
                    }
                }
            }
        }

        return list;
    }

    /**
     * Obtiene una lista las recomendaciones realizadas en una sesión.
     *
     * @param session             nombre de la sesión
     * @param seeker              usuario que hizo la recomendación
     * @param query               consulta
     * 
     * @return                 devuelve una lista con los datos de las recomendaciones realizadas en una sesión
     *
     * @throws SQLException     si ocurre alguna SQLException durante la ejecución
     *                               de la operación
     */
    public List<RecommendTracker> getAllRecommendations(String session, Seeker seeker, String query) throws SQLException {
        RecommendTracker data;
        List<RecommendTracker> list = new ArrayList<>();
        List<String> usersReceptors = new ArrayList<>();
        List<Integer> documents = new ArrayList<>();
        int idDoc;
        String text = null, date = null;

        //get all results for that query
        documents = getQueryOcurrences(query);

        //get all the recommendation that this seeker made for these documents
        for (int i = 0; i < documents.size(); i++) {
            idDoc = documents.get(i);
            if (util.alreadyExist(idDoc, "DRAKKARKEEL.RECOMMENDATION", "ID_SR", "ID_REC")) {
                String[] fields2 = new String[2];
                fields2[0] = "TEXT";
                fields2[1] = "REC_DATE";

                String[] whereFields = new String[3];
                whereFields[0] = "SESSION_TOPIC";
                whereFields[1] = "SEEKER_USER";
                whereFields[2] = "ID_SR";

                Object[] whereValues = new Object[3];
                whereValues[0] = session;
                whereValues[1] = seeker.getUser();
                whereValues[2] = idDoc;

                TableTracker table2 = PersistentOperations.load(connection, fields2, "DRAKKARKEEL.RECOMMENDATION", whereFields, whereValues);

                Object[][] val = table2.getValues();

                if (val.length != 0) {

                    for (int j = 0; j < val.length; j++) {
                        Object[] objects = val[j];
                        text = objects[0].toString();
                        date = objects[1].toString();
                        usersReceptors = getRecommendationReceptors(idDoc, seeker.getUser());

                        data = new RecommendTracker(usersReceptors, seeker.getUser(), date, text, dbSearch.getResult(idDoc, session).getURI(), idDoc, query, KeySearchable.MULTIPLE_SEARCHERS);
                        list.add(data);
                    }
                }
            }
        }


        return list;

    }

    /**
     * Obtiene una lista las recomendaciones realizadas en una sesión
     *
     * @param session        nombre de la sesión
     * @param query          consulta
     * @param date           fecha de recomendación
     * 
     * @return                 devuelve una lista con los datos de las recomendaciones realizadas en una sesión
     *
     * @throws SQLException     si ocurre alguna SQLException durante la ejecución
     *                               de la operación
     */
    public List<RecommendTracker> getAllRecommendations(String session, String query, Date date) throws SQLException {

        RecommendTracker data;
        List<RecommendTracker> list = new ArrayList<>();
        List<String> usersReceptors = new ArrayList<>();
        List<Integer> documents = new ArrayList<>();
        int idDoc;
        String text = null, seeker = null;

        //get all results for that query
        documents = getQueryOcurrences(query);
        //get all the recommendations in that date
        for (int i = 0; i < documents.size(); i++) {
            idDoc = documents.get(i);
            if (util.alreadyExist(idDoc, "DRAKKARKEEL.RECOMMENDATION", "ID_SR", "ID_REC")) {
                String[] fields2 = new String[2];
                fields2[0] = "TEXT";
                fields2[1] = "SEEKER_USER";

                String[] whereFields = new String[3];
                whereFields[0] = "SESSION_TOPIC";
                whereFields[1] = "REC_DATE";
                whereFields[2] = "ID_SR";

                Object[] whereValues = new Object[3];
                whereValues[0] = session;
                whereValues[1] = date;
                whereValues[2] = idDoc;

                TableTracker table2 = PersistentOperations.load(connection, fields2, "DRAKKARKEEL.RECOMMENDATION", whereFields, whereValues);

                Object[][] val = table2.getValues();
                if (val.length != 0) {
                    for (int j = 0; j < val.length; j++) {
                        Object[] objects = val[j];
                        text = objects[0].toString();
                        seeker = objects[1].toString();
                        usersReceptors = getRecommendationReceptors(idDoc, seeker);

                        data = new RecommendTracker(usersReceptors, seeker, date.toString(), text, dbSearch.getResult(idDoc, session).getURI(), idDoc, query, KeySearchable.MULTIPLE_SEARCHERS);
                        list.add(data);
                    }

                }
            }
        }

        return list;

    }

    /**
     * Obtiene una lista las recomendaciones realizadas en una sesión.
     *
     * @param session        nombre de la sesión
     * @param seeker         nombre del usuario que emitió la recomendación
     * @param date           fecha de la recomendación
     *
     * @return                 devuelve una lista con los datos de las recomendaciones realizadas en una sesión
     *
     * @throws SQLException     si ocurre alguna SQLException durante la ejecución
     *                               de la operación
     */
    public List<RecommendTracker> getAllRecommendations(String session, Seeker seeker, Date date) throws SQLException {

        RecommendTracker data;
        List<RecommendTracker> list = new ArrayList<>();
        List<String> usersReceptors = new ArrayList<>();
        int doc;
        String text, uri = null;

        String[] fields = new String[2];
        fields[0] = "TEXT";
        fields[1] = "ID_SR";

        String[] whereFields = new String[3];
        whereFields[0] = "SESSION_TOPIC";
        whereFields[1] = "REC_DATE";
        whereFields[2] = "SEEKER_USER";

        Object[] whereValues = new Object[3];
        whereValues[0] = session;
        whereValues[1] = date;
        whereValues[2] = seeker.getUser();

        TableTracker table = PersistentOperations.load(connection, fields, "DRAKKARKEEL.RECOMMENDATION", whereFields, whereValues);

        Object[][] values = table.getValues();

        if (values.length != 0) {
            for (int i = 0; i < values.length; i++) {
                Object[] obj = values[i];
                text = obj[0].toString();
                doc = Integer.valueOf(obj[1].toString());
                uri = dbSearch.getResult(doc, session).getURI();
                usersReceptors = this.getRecommendationReceptors(doc, seeker.getUser());

                data = new RecommendTracker(usersReceptors, seeker.getUser(), date.toString(), text, uri, doc, KeySession.MULTIPLE_QUERIES, KeySearchable.MULTIPLE_SEARCHERS);
                list.add(data);
            }
        }
        return list;
    }

    /**
     * Obtiene una lista las recomendaciones realizadas en una sesión.
     *
     * @param session               nombre de la sesión
     * @param seeker                nombre del usuario que emitió la recomendación
     * @param query                 consulta
     * @param date                  fecha
     * 
     * @return                 devuelve una lista con los datos de las recomendaciones realizadas en una sesión
     *
     * @throws SQLException     si ocurre alguna SQLException durante la ejecución
     *                               de la operación
     */
    public List<RecommendTracker> getAllRecommendations(String session, Seeker seeker, String query, Date date) throws SQLException {
        RecommendTracker data;
        List<RecommendTracker> list = new ArrayList<>();
        List<String> usersReceptors = new ArrayList<>();
        List<Integer> documents = new ArrayList<>();
        int idDoc = 0;
        String text = null;

        //get all results for that query
        documents = getQueryOcurrences(query);

        //get the recommendations made for that seeker in that date, in that session
        for (int i = 0; i < documents.size(); i++) {
            idDoc = documents.get(i);
            if (util.alreadyExist(idDoc, "DRAKKARKEEL.RECOMMENDATION", "ID_SR", "ID_REC")) {
                String[] fields2 = new String[1];
                fields2[0] = "TEXT";

                String[] whereFields = new String[4];
                whereFields[0] = "SESSION_TOPIC";
                whereFields[1] = "REC_DATE";
                whereFields[2] = "SEEKER_USER";
                whereFields[3] = "ID_SR";

                Object[] whereValues = new Object[4];
                whereValues[0] = session;
                whereValues[1] = date;
                whereValues[2] = seeker.getUser();
                whereValues[3] = idDoc;

                TableTracker table2 = PersistentOperations.load(connection, fields2, "DRAKKARKEEL.RECOMMENDATION", whereFields, whereValues);

                Object[][] val = table2.getValues();
                if (val.length != 0) {
                    for (int j = 0; j < val.length; j++) {
                        text = val[j][0].toString();
                        usersReceptors = getRecommendationReceptors(idDoc, seeker.getUser());
                        data = new RecommendTracker(usersReceptors, seeker.getUser(), date.toString(), text, dbSearch.getResult(idDoc, session).getURI(), idDoc, query, KeySearchable.MULTIPLE_SEARCHERS);
                        list.add(data);
                    }
                }
            }
        }

        return list;

    }

    /**
     * Obtiene la lista de receptores de una recomendación
     * dado el documento recomendado y el usuario que hizo la recomendación.
     *
     * @param idDoc             id del deocumento recomendado
     * @param seeker            nombre del usuario que hizo la recomendación
     * 
     * @return                 devuelve una lista con los nombres de los receptores de una recomendación
     *
     * @throws SQLException     si ocurre alguna SQLException durante la ejecución
     *                               de la operación
     */
    private List<String> getRecommendationReceptors(int idDoc, String seeker) throws SQLException {
        List<String> list = new ArrayList<>();

        String[] fields = new String[1];
        fields[0] = "SEEKER_RECEPTOR";

        String[] whereFields = new String[2];
        whereFields[0] = "ID_SR";
        whereFields[1] = "SEEKER_USER";

        Object[] whereValues = new Object[2];
        whereValues[0] = idDoc;
        whereValues[1] = seeker;

        TableTracker table = PersistentOperations.load(connection, fields, "DRAKKARKEEL.RECOMMENDATION", whereFields, whereValues);
        Object[][] values = table.getValues();

        if (values.length != 0) {
            for (int i = 0; i < values.length; i++) {
                String receptor = values[i][0].toString();
                list.add(receptor);
            }

        }
        return list;
    }

    ///////////////////SEARCHES////////////////////////////////
    /**
     * Obtiene una lista de documentos correspondientes a búsquedas
     * realizadas en una sesión
     *
     * @param session               nombre de la sesión
     * @param group                 identificador de los documentos (revisados, relevantes o todos)
     *
     * @return                 devuelve una lista con los datos de las búsquedas realizadas en una sesión
     *
     * @throws SQLException     si ocurre alguna SQLException durante la ejecución
     *                               de la operación
     */
    public List<SearchTracker> getAllSearches(String session, int group) throws SQLException {
        List<SearchTracker> list = new ArrayList<>();
        List<MarkupData> temp = new ArrayList<>();
        List<SearchResultData> results = new ArrayList<>();

        //get documents
        results = filterDocuments(group, session, null);

        for (int i = 0; i < results.size(); i++) {
            SearchResultData searchResultData = results.get(i);
            temp = dbSession.getEvaluations(searchResultData, session);
            list.add(new SearchTracker(searchResultData, temp));
        }

        return list;
    }

    /**
     * Obtiene una lista de documentos correspondientes a búsquedas
     * realizadas en una sesión dados los parámetros entrados
     *
     * @param session           nombre de la sesión
     * @param group             identificador de los documentos (revisados, relevantes o todos)
     * @param seeker            nombre del usuario
     * 
     * @return                 devuelve una lista con los datos de las búsquedas realizadas en una sesión
     *
     * @throws SQLException     si ocurre alguna SQLException durante la ejecución
     *                               de la operación
     */
    public List<SearchTracker> getAllSearches(String session, int group, Seeker seeker) throws SQLException {
        List<SearchTracker> list = new ArrayList<>();
        List<MarkupData> temp = new ArrayList<>();
        int idDoc;
        SearchResultData data;

        //get documents
        List<SearchResultData> results = filterDocuments(group, session, null);

        //get documents for this seeker
        String[] fields = new String[1];
        fields[0] = "ID_SR";

        TableTracker table = PersistentOperations.load(connection, fields, "DRAKKARKELL.SEEKER_QUERY", "DRAKKARKEEL.QUERY_SEARCH_RESULT", "ID_QUERY", "ID_QUERY", "SEEKER_USER", seeker.getUser());
        Object[][] values = table.getValues();

        if (values.length != 0) {
            for (int i = 0; i < values.length; i++) {
                Object[] obj = values[i];
                idDoc = Integer.valueOf(obj[0].toString());//documento perteneciente a la búsqueda del seeker
                data = dbSearch.getResult(idDoc, session);

                for (int j = 0; j < results.size(); j++) {
                    SearchResultData searchResultData = results.get(j);
                    if (searchResultData.getId() == data.getId()) {
                        temp = dbSession.getEvaluations(data, session);
                        list.add(new SearchTracker(data, temp));
                    }
                }
            }
        }

        return list;
    }

    /**
     *
     * Obtiene una lista de documentos correspondientes a búsquedas
     * realizadas en una sesión dados los parámetros entrados
     *
     * @param session            nombre de la sesión
     * @param group              identificador de los documentos (revisados, relevantes o todos)
     * @param date               fecha
     * 
     * @return                 devuelve una lista con los datos de las búsquedas realizadas en una sesión
     *
     * @throws SQLException     si ocurre alguna SQLException durante la ejecución
     *                               de la operación
     */
    public List<SearchTracker> getAllSearches(String session, int group, Date date) throws SQLException {
        List<SearchTracker> list = new ArrayList<>();
        List<MarkupData> temp = new ArrayList<>();
        int idDoc;
        SearchResultData data;
        //get documents
        List<SearchResultData> results = filterDocuments(group, session, null);

        //get all seekers queries results
        List<String> seekers = dbSession.getSessionSeekers(session);

        for (int i = 0; i < seekers.size(); i++) {
            String seek = seekers.get(i);

            String[] fields = new String[1];
            fields[0] = "ID_SR";

            String[] whereFields = new String[2];
            whereFields[0] = "SEEKER_USER";
            whereFields[1] = "DATE_QUERY";

            Object[] whereValues = new Object[2];
            whereValues[0] = seek;
            whereValues[1] = date;

            TableTracker table = PersistentOperations.load(connection, fields, "DRAKKARKELL.SEEKER_QUERY", "DRAKKARKEEL.QUERY", "DRAKKARKEEL.QUERY_SEARCH_RESULT", "ID_QUERY", "ID_QUERY", "ID_QUERY", whereFields, whereValues);
            Object[][] values = table.getValues();

            if (values.length != 0) {
                for (int j = 0; j < values.length; j++) {
                    Object[] obj = values[j];
                    idDoc = Integer.valueOf(obj[0].toString());
                    data = dbSearch.getResult(idDoc, session);

                    for (int k = 0; k < results.size(); k++) {
                        SearchResultData searchResultData = results.get(k);
                        if (searchResultData.getId() == data.getId()) {
                            temp = dbSession.getEvaluations(data, session);
                            list.add(new SearchTracker(data, temp));
                        }
                    }

                }

            }
        }

        return list;
    }

    /**
     * Obtiene una lista de documentos correspondientes a búsquedas
     * realizadas en una sesión dados los parámetros entrados
     *
     * @param session           nombre de la sesión
     * @param group             identificador de los documentos (revisados, relevantes o todos)
     * @param query             consulta
     * 
     * @return                 devuelve una lista con los datos de las búsquedas realizadas en una sesión
     *
     * @throws SQLException     si ocurre alguna SQLException durante la ejecución
     *                               de la operación
     */
    public List<SearchTracker> getAllSearches(String session, int group, String query) throws SQLException {
        List<SearchTracker> list = new ArrayList<>();
        List<MarkupData> temp = new ArrayList<>();
        int idDoc = 0;
        SearchResultData data;

        //get documents
        List<SearchResultData> results = filterDocuments(group, session, null);

        //verificar que la consulta la halla realizado un usuario de esa sesion
        List<String> seekers = dbSession.getSessionSeekers(session);

        for (int i = 0; i < seekers.size(); i++) {
            String seek = seekers.get(i);

            String fields[] = new String[1];
            fields[0] = "ID_SR";

            String[] whereFields = new String[2];
            whereFields[0] = "SEEKER_USER";
            whereFields[1] = "TEXT";

            Object[] whereValues = new Object[2];
            whereValues[0] = seek;
            whereValues[1] = query;

            TableTracker table = PersistentOperations.load(connection, fields, "DRAKKARKELL.SEEKER_QUERY", "DRAKKARKEEL.QUERY", "DRAKKARKEEL.QUERY_SEARCH_RESULT", "ID_QUERY", "ID_QUERY", "ID_QUERY", whereFields, whereValues);

            Object[][] values = table.getValues();
            if (values.length != 0) {
                for (int j = 0; j < values.length; j++) {
                    Object[] objects = values[j];
                    idDoc = Integer.valueOf(objects[0].toString());

                    data = dbSearch.getResult(idDoc, session);

                    for (int k = 0; k < results.size(); k++) {
                        SearchResultData searchResultData = results.get(k);
                        if (searchResultData.getId() == data.getId()) {
                            temp = dbSession.getEvaluations(data, session);
                            list.add(new SearchTracker(data, temp));
                        }
                    }
                }
            }

        }


        /* String fields[] = new String[1];
        fields[0] = "ID_SR";
        TableTracker table = PersistentOperations.load(connection, fields, "DRAKKARKEEL.QUERY", "DRAKKARKEEL.QUERY_SEARCH_RESULT", "ID_QUERY", "ID_QUERY", "TEXT", query);
         */


        return list;
    }

    /**
     * Obtiene una lista de documentos correspondientes a búsquedas
     * realizadas en una sesión dados los parámetros entrados
     *
     * @param session            nombre de la sesión
     * @param group              identificador de los documentos (revisados, relevantes o todos)
     * @param seeker             nombre del usuario
     * @param query              consulta
     * 
     * @return                 devuelve una lista con los datos de las búsquedas realizadas en una sesión
     *
     * @throws SQLException     si ocurre alguna SQLException durante la ejecución
     *                               de la operación
     */
    public List<SearchTracker> getAllSearches(String session, int group, Seeker seeker, String query) throws SQLException {
        List<SearchTracker> list = new ArrayList<>();
        List<MarkupData> temp = new ArrayList<>();
        int idDoc = 0;
        SearchResultData data;

        //get documents
        List<SearchResultData> results = filterDocuments(group, session, null);

        //get all results for this query made by this seeker
        String fields[] = new String[1];
        fields[0] = "ID_SR";

        String[] whereFields = new String[2];
        whereFields[0] = "SEEKER_USER";
        whereFields[1] = "TEXT";

        Object[] whereValues = new Object[2];
        whereValues[0] = seeker.getUser();
        whereValues[1] = query;

        TableTracker table = PersistentOperations.load(connection, fields, "DRAKKARKELL.SEEKER_QUERY", "DRAKKARKEEL.QUERY", "DRAKKARKEEL.QUERY_SEARCH_RESULT", "ID_QUERY", "ID_QUERY", "ID_QUERY", whereFields, whereValues);

        Object[][] values = table.getValues();
        if (values.length != 0) {
            for (int i = 0; i < values.length; i++) {
                Object[] objects = values[i];
                idDoc = Integer.valueOf(objects[0].toString());
                data = dbSearch.getResult(idDoc, session);

                for (int k = 0; k < results.size(); k++) {
                    SearchResultData searchResultData = results.get(k);
                    if (searchResultData.getId() == data.getId()) {
                        temp = dbSession.getEvaluations(data, session);
                        list.add(new SearchTracker(data, temp));
                    }
                }
            }
        }

        return list;
    }

    /**
     * Obtiene una lista de documentos correspondientes a búsquedas
     * realizadas en una sesión dados los parámetros entrados
     *
     * @param session           nombre de la sesión
     * @param group             identificador de los documentos (revisados, relevantes o todos)
     * @param query             consulta
     * @param date              fecha
     * 
     * @return                 devuelve una lista con los datos de las búsquedas realizadas en una sesión
     *
     * @throws SQLException     si ocurre alguna SQLException durante la ejecución
     *                               de la operación
     */
    public List<SearchTracker> getAllSearches(String session, int group, String query, Date date) throws SQLException {
        List<SearchTracker> list = new ArrayList<>();
        List<MarkupData> temp = new ArrayList<>();
        int idDoc = 0;
        SearchResultData data;

        //get documents
        List<SearchResultData> results = filterDocuments(group, session, date);

        //verificar que la consulta la halla realizado un usuario de esa sesion
        List<String> seekers = dbSession.getSessionSeekers(session);

        for (int i = 0; i < seekers.size(); i++) {
            String seek = seekers.get(i);

            String fields[] = new String[1];
            fields[0] = "ID_SR";

            String[] whereFields = new String[3];
            whereFields[0] = "SEEKER_USER";
            whereFields[1] = "TEXT";
            whereFields[2] = "DATE_QUERY";

            Object[] whereValues = new Object[3];
            whereValues[0] = seek;
            whereValues[1] = query;
            whereValues[2] = date;

            TableTracker table = PersistentOperations.load(connection, fields, "DRAKKARKELL.SEEKER_QUERY", "DRAKKARKEEL.QUERY", "DRAKKARKEEL.QUERY_SEARCH_RESULT", "ID_QUERY", "ID_QUERY", "ID_QUERY", whereFields, whereValues);

            Object[][] values = table.getValues();
            if (values.length != 0) {
                for (int j = 0; j < values.length; j++) {
                    Object[] objects = values[j];
                    idDoc = Integer.valueOf(objects[0].toString());

                    data = dbSearch.getResult(idDoc, session);

                    for (int k = 0; k < results.size(); k++) {
                        SearchResultData searchResultData = results.get(k);
                        if (searchResultData.getId() == data.getId()) {
                            temp = dbSession.getEvaluations(data, session);
                            list.add(new SearchTracker(data, temp));
                        }
                    }
                }
            }


        }

        return list;
    }

    /**
     * Obtiene una lista de documentos correspondientes a búsquedas
     * realizadas en una sesión dados los parámetros entrados
     *
     * @param session              nombre de la sesión
     * @param group                identificador de los documentos (revisados, relevantes o todos)
     * @param seeker               nombre del usuario
     * @param date                 fecha de la búsqueda
     *
     * @return                 devuelve una lista con los datos de las búsquedas realizadas en una sesión
     *
     * @throws SQLException     si ocurre alguna SQLException durante la ejecución
     *                               de la operación
     */
    public List<SearchTracker> getAllSearches(String session, int group, Seeker seeker, Date date) throws SQLException {
        List<SearchTracker> list = new ArrayList<>();
        List<MarkupData> temp = new ArrayList<>();
        int idDoc;
        SearchResultData data;

        //get documents
        List<SearchResultData> results = filterDocuments(group, session, date);
        //get all queries results that this seeker obtain in that date
        String[] fields = new String[1];
        fields[0] = "ID_SR";

        String[] fields1 = new String[1];
        fields1[0] = "DRAKKARKELL.SEEKER_QUERY.ID_QUERY";

        String[] whereFields = new String[2];
        whereFields[0] = "SEEKER_USER";
        whereFields[1] = "DATE_QUERY";

        Object[] whereValues = new Object[2];
        whereValues[0] = seeker.getUser();
        whereValues[1] = date;

        TableTracker table = PersistentOperations.load(connection, fields, "DRAKKARKELL.SEEKER_QUERY", "DRAKKARKEEL.QUERY", "DRAKKARKEEL.QUERY_SEARCH_RESULT", "ID_QUERY", "ID_QUERY", "ID_QUERY", whereFields, whereValues);
        Object[][] values = table.getValues();

        if (values.length != 0) {
            for (int j = 0; j < values.length; j++) {
                Object[] obj = values[j];
                idDoc = Integer.valueOf(obj[0].toString());
                data = dbSearch.getResult(idDoc, session);
                for (int k = 0; k < results.size(); k++) {
                    SearchResultData searchResultData = results.get(k);
                    if (data.getURI().equals(searchResultData.getURI())) {
                        temp = dbSession.getEvaluations(data, session);
                        list.add(new SearchTracker(data, temp));
                    }
                }

            }

        }

        return list;
    }

    /**
     * Obtiene una lista de documentos correspondientes a búsquedas
     * realizadas en una sesión dados los parámetros entrados
     *
     * @param session               nombre de la sesión
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
    public List<SearchTracker> getAllSearches(String session, int group, Seeker seeker, Date date, String query) throws SQLException {
        List<SearchTracker> list = new ArrayList<>();
        List<MarkupData> temp = new ArrayList<>();
        int idDoc = 0;
        SearchResultData data;
        //get documents
        List<SearchResultData> results = filterDocuments(group, session, date);

        //get documents for that query in that date of this seeker
        String[] fields = new String[1];
        fields[0] = "ID_SR";

        String[] whereFields = new String[3];
        whereFields[0] = "SEEKER_USER";
        whereFields[1] = "DATE_QUERY";
        whereFields[2] = "TEXT";

        Object[] whereValues = new Object[3];
        whereValues[0] = seeker.getUser();
        whereValues[1] = date;
        whereValues[2] = query;

        TableTracker table = PersistentOperations.load(connection, fields, "DRAKKARKELL.SEEKER_QUERY", "DRAKKARKEEL.QUERY", "DRAKKARKEEL.QUERY_SEARCH_RESULT", "ID_QUERY", "ID_QUERY", "ID_QUERY", whereFields, whereValues);
        Object[][] values = table.getValues();

        if (values.length != 0) {
            for (int j = 0; j < values.length; j++) {
                Object[] obj = values[j];
                idDoc = Integer.valueOf(obj[0].toString());
                data = dbSearch.getResult(idDoc, session);

                for (int k = 0; k < results.size(); k++) {
                    SearchResultData searchResultData = results.get(k);
                    if (searchResultData.getId() == data.getId()) {
                        temp = dbSession.getEvaluations(data, session);
                        list.add(new SearchTracker(data, temp));
                    }
                }

            }

        }
        return list;
    }

    /**
     * Devuelve una lista de resultados de búsqueda en una sesión
     * de acuerdo a tres constantes: revisados, relevantes o todos los resultados de la búsqueda
     *
     * @param group                 identificador de los documentos 
     * @param session               nombre de la sesión
     *
     * @return                 devuelve una lista con los datos de los resultados de búsqueda
     *
     * @throws SQLException     si ocurre alguna SQLException durante la ejecución
     *                               de la operación
     */
    private List<SearchResultData> filterDocuments(int group, String session, Date date) throws SQLException {

        List<SearchResultData> list = new ArrayList<>();

        switch (group) {

            case SeekerAction.SEARCH_REVIEWED_TRACK:
                list = dbSearch.getAllReviewDocuments(session);
                break;
            case SeekerAction.SEARCH_SELECTED_RELEVANT_TRACK:
                list = dbSearch.getAllRelevantDocuments(session, date);
                break;
            case SeekerAction.SEARCH_ALL_TRACK:
                list = dbSearch.getAllDocuments(session);
                break;

        }

        return list;
    }

    /**
     * 
     * Obtiene todos los documentos para una consulta, valora si esta se repite
     * de ser el caso, entonces toma todos los documentos que constituyen los
     * resultados de búsqueda para cada una
     *
     * @param query       consulta
     *
     * @return
     * @throws SQLException
     */
    private List<Integer> getQueryOcurrences(String query) throws SQLException {
        List<Integer> list = new ArrayList<>();
        List<Integer> queries = new ArrayList<>();
        int doc = 0;
        int idQuery = 0;

        String[] fields = new String[1];
        fields[0] = "ID_QUERY";

        TableTracker table1 = PersistentOperations.load(connection, fields, "DRAKKARKEEL.QUERY", "TEXT", query);
        Object[][] values1 = table1.getValues();

        if (values1.length != 0) {

            for (int i = 0; i < values1.length; i++) {
                Object[] objects = values1[i];
                idQuery = Integer.valueOf(objects[0].toString());
                queries.add(idQuery);
            }
        }

        for (int i = 0; i < queries.size(); i++) {
            Integer integer = queries.get(i);
            String[] fields2 = new String[1];
            fields2[0] = "ID_SR";

            TableTracker table = PersistentOperations.load(connection, fields2, "DRAKKARKEEL.QUERY_SEARCH_RESULT", "ID_QUERY", integer);

            Object[][] values = table.getValues();

            if (values.length != 0) {
                for (int j = 0; j < values.length; j++) {
                    Object[] obj = values[j];
                    doc = Integer.valueOf(obj[0].toString());

                    if (!list.contains(doc)) {
                        list.add(doc);
                    }

                }
            }
        }

        return list;
    }
}
