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

import drakkar.oar.DocumentMetaData;
import drakkar.oar.ResultSetMetaData;
import drakkar.oar.SearchResultData;
import drakkar.oar.Seeker;
import drakkar.oar.SeekerQuery;
import drakkar.oar.util.KeySearchable;
import static drakkar.oar.util.KeySearchable.*;
import drakkar.stern.tracker.persistent.objects.SeekerData;
import drakkar.stern.tracker.persistent.tables.DerbyConnection;
import drakkar.stern.tracker.persistent.tables.PersistentOperations;
import drakkar.stern.tracker.persistent.tables.TableTracker;
import java.util.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Clase que contiene los métodos relacionados con la persistencia de las búsquedas
 */
public class SearchDB {

    DerbyConnection connection;
    DBUtil util;
    SearchSessionDB dbSession;

    /**
     * Constructor de la clase
     *
     * @param connection             objeto que representa la conexión con Derby
     * @param util                   objeto que contiene los métodos utilitarios para interactuar con la BD
     * @param dbSession              objeto que contiene los métodos referentes a los datos persistentes del usuario
     */
    public SearchDB(DerbyConnection connection, DBUtil util, SearchSessionDB dbSession) {
        this.connection = connection;
        this.util = util;
        this.dbSession = dbSession;
    }

    /**
     * Obtiene todos los resultados de búsqueda de una sesión
     *
     * @param session              nombre de la sesión
     * 
     * @return                     devuelve una lista de los datos de todos los resultados de búsqueda de esa sesión
     *
     * @throws SQLException        si ocurre alguna SQLException durante la ejecución
     *                             de la operación
     */
    public List<SearchResultData> getAllDocuments(String session) throws SQLException {

        List<SearchResultData> list = new ArrayList<>();
        SearchResultData data;

        String uri = null, name = null, type = null, summary = null, author= null, lastModified= null;
        double score = 0;
        int size = 0, id = 0, index = 0;
        boolean review = false;

        String[] fields = new String[11];
        fields[0] = "URI";
        fields[1] = "SCORE";
        fields[2] = "INDEX";
        fields[3] = "SR_NAME";
        fields[4] = "SR_SIZE";
        fields[5] = "SR_TYPE";
        fields[6] = "REVIEW";
        fields[7] = "ID_SR";
        fields[8] = "SUMMARY";
        fields[9] = "AUTHOR";
        fields[10] = "LASTMODIFIED";

        TableTracker table = PersistentOperations.load(connection, fields, "DRAKKARKEEL.SEARCH_RESULT", "SESSION_TOPIC", session);

        Object[][] values = table.getValues();

        if (values.length != 0) {
            for (int i = 0; i < values.length; i++) {
                Object[] objects = values[i];

                uri = objects[0].toString();
                score = Double.parseDouble(objects[1].toString());
                index = Integer.parseInt(objects[2].toString());
                name = objects[3].toString();
                size = Integer.parseInt(objects[4].toString());
                type = objects[5].toString();
                review = Boolean.parseBoolean(objects[6].toString());
                id = Integer.valueOf(objects[7].toString());
                summary = objects[8].toString();
                author= objects[9].toString();
                lastModified= objects[10].toString();
                

                data = new SearchResultData(id, uri, index, size, name, review, score, type, session, summary, MULTIPLE_SEARCHERS, author, lastModified);
                list.add(data);
            }


        }

        return list;
    }

    /**
     * Obtiene todos los resultados de búsqueda revisados de una sesión
     *
     * @param session            nombre de la sesión
     * 
     * @return                   devuelve una lista de los datos de todos los resultados de búsqueda revisados de esa sesión
     * 
     * @throws SQLException      si ocurre alguna SQLException durante la ejecución
     *                           de la operación
     */
    public List<SearchResultData> getAllReviewDocuments(String session) throws SQLException {

        List<SearchResultData> list = new ArrayList<>();
        SearchResultData data;
        String uri = null, name = null, type = null, summary = null, author= null, lastModified= null;
        double score = 0;
        int size = 0, id = 0, index = 0;

        String[] fields = new String[10];
        fields[0] = "URI";
        fields[1] = "SCORE";
        fields[2] = "INDEX";
        fields[3] = "SR_NAME";
        fields[4] = "SR_SIZE";
        fields[5] = "SR_TYPE";
        fields[6] = "ID_SR";
        fields[7] = "SUMMARY";
        fields[8] = "AUTHOR";
        fields[9] = "LASTMODIFIED";

        String[] whereFields = new String[2];
        whereFields[0] = "SESSION_TOPIC";
        whereFields[1] = "REVIEW";

        Object[] whereValues = new Object[2];
        whereValues[0] = session;
        whereValues[1] = true;

        TableTracker table = PersistentOperations.load(connection, fields, "DRAKKARKEEL.SEARCH_RESULT", whereFields, whereValues);

        Object[][] values = table.getValues();

        if (values.length != 0) {
            for (int i = 0; i < values.length; i++) {
                Object[] objects = values[i];

                uri = objects[0].toString();
                score = Double.parseDouble(objects[1].toString());
                index = Integer.parseInt(objects[2].toString());
                name = objects[3].toString();
                size = Integer.parseInt(objects[4].toString());
                type = objects[5].toString();
                id = Integer.valueOf(objects[6].toString());
                summary = objects[7].toString();
                author= objects[8].toString();
                lastModified= objects[9].toString();

                data = new SearchResultData(id, uri, index, size, name, true, score, type, session, summary, KeySearchable.MULTIPLE_SEARCHERS, author, lastModified);
                list.add(data);
            }


        }

        return list;
    }

    /**
     * Obtiene todos los resultados de búsqueda evaluados de una sesión
     *
     *
     * @param session            nombre de la sesión     
     * @param date               fecha de la evaluación del documento
     *
     * @return                   devuelve una lista de los datos de todos los resultados de búsqueda relevantes de esa sesión
     *
     * @throws SQLException      si ocurre alguna SQLException durante la ejecución
     *                           de la operación
     */
    public List<SearchResultData> getAllRelevantDocuments(String session, Date date) throws SQLException {
        List<SearchResultData> list = new ArrayList<>();
        SearchResultData data;

        String uri = null, name = null, type = null, summary = null, author= null, lastModified= null;
        double score = 0;
        int size = 0, id = 0, index = 0;
        boolean review = false;

        String[] fields = new String[11];
        fields[0] = "URI";
        fields[1] = "SCORE";
        fields[2] = "INDEX";
        fields[3] = "SR_NAME";
        fields[4] = "SR_SIZE";
        fields[5] = "SR_TYPE";
        fields[6] = "REVIEW";
        fields[7] = "DRAKKARKEEL.SEARCH_RESULT.ID_SR";
        fields[8] = "SUMMARY";
        fields[9] = "AUTHOR";
        fields[10] = "LASTMODIFIED";
        

        String[] whereFields;
        Object[] whereValues;

        if (date != null) {
            whereFields = new String[2];
            whereFields[0] = "DRAKKARKEEL.MARKUP.SESSION_TOPIC";
            whereFields[1] = "DRAKKARKEEL.MARKUP.MARKUP_DATE";

            whereValues = new Object[2];
            whereValues[0] = session;
            whereValues[1] = date;

        } else {

            whereFields = new String[1];
            whereFields[0] = "DRAKKARKEEL.MARKUP.SESSION_TOPIC";

            whereValues = new Object[1];
            whereValues[0] = session;

        }

        TableTracker table = PersistentOperations.load(connection, fields, "DRAKKARKEEL.SEARCH_RESULT", "DRAKKARKEEL.MARKUP", "ID_SR", "ID_SR", "RELEVANCE", "7", " >= ", whereFields, whereValues);

        Object[][] values = table.getValues();

        if (values.length != 0) {
            for (int i = 0; i < values.length; i++) {
                Object[] objects = values[i];

                uri = objects[0].toString();
                score = Double.parseDouble(objects[1].toString());
                index = Integer.parseInt(objects[2].toString());
                name = objects[3].toString();
                size = Integer.parseInt(objects[4].toString());
                type = objects[5].toString();
                review = Boolean.parseBoolean(objects[6].toString());
                id = Integer.valueOf(objects[7].toString());
                summary = objects[8].toString();
                author= objects[9].toString();
                lastModified= objects[10].toString();

                data = new SearchResultData(id, uri, index, size, name, review, score, type, session, summary, KeySearchable.MULTIPLE_SEARCHERS, author, lastModified);
                list.add(data);
            }
        }

        return list;
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
    public List<String> getRelevantDocumentsQuery(String session, String query) throws SQLException {
        List<String> list = new ArrayList<>();
        List<Integer> listIds = new ArrayList<>();

        //relevants documents by session
        List<SearchResultData> relevants = getAllRelevantDocuments(session, null);
        for (SearchResultData searchResultData : relevants) {
            listIds.add(searchResultData.getId());
        }
        //documents by query and session
        List<DocumentMetaData> results = getSearchResults(session, query);

        for (DocumentMetaData metaDocument : results) {

            if (listIds.contains(getResultId(metaDocument.getPath(), session))) {
                list.add(metaDocument.getName());
            }
        }

        return list;
    }

    /**
     * Obtiene una lista las búsquedas realizadas en una sesión
     *
     * @param session            nombre de la sesión
     * 
     * @return                   devuelve todos los ids de los resultados de búsqueda de una sesión
     *
     * @throws SQLException      si ocurre alguna SQLException durante la ejecución
     *                       de la operación
     */
    public List<Integer> getAllQueriesResults(String session) throws SQLException {

        List<SeekerData> listSeeker = new ArrayList<>();
        List<Integer> listQuery = new ArrayList<>(); //lista de ids de las consultas
        List<Integer> listResult = new ArrayList<>(); //lista id de docs

        int idQuery, doc;

        //Get all seekers for this session
        listSeeker = dbSession.loadSeekersData(session);

        //Get queries by each user
        for (int i = 0; i < listSeeker.size(); i++) {
            SeekerData seekerData = listSeeker.get(i);

            listQuery = getQuerySeeker(seekerData.getUser());

            for (int j = 0; j < listQuery.size(); j++) {
                idQuery = listQuery.get(j);
                String[] fields2 = new String[1];
                fields2[0] = "ID_SR";

                TableTracker table = PersistentOperations.load(connection, fields2, "DRAKKARKEEL.QUERY_SEARCH_RESULT", "ID_QUERY", idQuery);
                Object[][] values = table.getValues();

                if (values.length != 0) {
                    for (int w = 0; w < values.length; w++) {
                        Object[] obj = values[w];
                        doc = Integer.valueOf(obj[0].toString());

                        if (!listResult.contains(doc)) {
                            listResult.add(doc);
                        }

                    }
                }


            }

        }

        return listResult;
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
        List<String> list = new ArrayList<>();
        String query;

        String fields[] = new String[1];
        fields[0] = "TEXT";

        TableTracker table = PersistentOperations.load(connection, fields, "DRAKKARKEEL.QUERY", "DRAKKARKEEL.SEARCH_SESSION_QUERY", "ID_QUERY", "ID_QUERY", "SESSION_TOPIC", session);
        Object[][] values = table.getValues();

        if (values.length != 0) {
            for (int j = 0; j < values.length; j++) {
                Object[] objects = values[j];
                query = objects[0].toString();
                list.add(query);
            }
        }

        //  List<String> seekers = dbSession.getSessionSeekers(session);

//        for (int i = 0; i < seekers.size(); i++) {
//            String seek = seekers.get(i);
//
//            TableTracker table = PersistentOperations.loadOneOcurrence(connection, "DRAKKARKEEL.QUERY", "DRAKKARKELL.SEEKER_QUERY", "TEXT", "ID_QUERY", "SEEKER_USER", seek);
//            Object[][] values = table.getValues();
//
//            if (values.length != 0) {
//                for (int j = 0; j < values.length; j++) {
//                    Object[] objects = values[j];
//                    query = objects[0].toString();
//                    list.add(query);
//                }
//
//            }
//
//
//        }


        return list;
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
        SeekerQuery result = new SeekerQuery();
        List<String> list;
        String query, seek;
        List<String> seekers = dbSession.getSessionSeekers(session);
        List<String> sessionQueries = this.getAllQueries(session);

        String[] whereFields = new String[2];
        whereFields[0] = "SEEKER_USER";
        whereFields[1] = "DATE_QUERY";

        Object[] whereValues = new Object[2];
        whereValues[1] = date;

        for (int i = 0; i < seekers.size(); i++) {
            seek = seekers.get(i);
            list = new ArrayList<>();
            whereValues[0] = seek;

            TableTracker table = PersistentOperations.loadOneOcurrence(connection, "DRAKKARKEEL.QUERY", "DRAKKARKELL.SEEKER_QUERY", "TEXT", "ID_QUERY", whereFields, whereValues);
            Object[][] values = table.getValues();

            if (values.length != 0) {
                for (int j = 0; j < values.length; j++) {
                    Object[] objects = values[j];
                    query = objects[0].toString();
                    if (sessionQueries.contains(query)) {
                        list.add(query);
                    }
                }
            }

            result.add(seek, list);
        }


        return result;
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
    public void saveSearchData(String session, String query, String principle, Map<Integer, List<DocumentMetaData>> allResults, String user) throws SQLException {
        List<DocumentMetaData> results = null;
//        int eng;

        //insertar consulta
        int idQuery = saveQuery(query, getSearchPrinciple(principle));
        // insertar seeker
        saveSeekerQuery(user, idQuery);
        //insertar query and session
        saveSessionQuery(session, idQuery);

        Set<Integer> engines = allResults.keySet();
        for (Integer engine : engines) {
            results = allResults.get(engine);
            //insertar search engine
            saveQueryEngine(idQuery, engine);
            //insertar search result
            saveQueryResults(idQuery, results, session);
            //relacionar motor con resultado
            saveResultEngine(engine, results, session);
        }
        
    }

    /**
     * Guarda una consulta, el texto de las consultas se pueden repetir
     *
     * @param query                 consulta
     * @param idPrinciple           id del principio de búsqueda
     * 
     * @return                      devuelve el id de la consulta insertada
     * @throws SQLException
     */
    private int saveQuery(String query, int idPrinciple) throws SQLException {
        int id = 0;

        String[] fields = new String[2];
        fields[0] = "TEXT";
        fields[1] = "ID_SP";

        Object[] oneValue = new Object[2];
        oneValue[0] = query;
        oneValue[1] = idPrinciple;

        PersistentOperations.insert(connection, "DRAKKARKEEL.QUERY", fields, oneValue);

        id = getLastQuery();

        return id;

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

        String[] fields = new String[1];
        fields[0] = "ID_SP";
        String result = null;
        int id = 0;

        TableTracker table = PersistentOperations.load(connection, fields, "DRAKKARKEEL.SEARCH_PRINCIPLE", "PRINCIPLE", principle);

        Object[][] values = table.getValues();

        if (values.length != 0) {
            result = values[0][0].toString();
            id = Integer.parseInt(result);
        }

        return id;


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

        String[] fields = new String[1];
        fields[0] = "PRINCIPLE";
        String result = null;

        TableTracker table = PersistentOperations.load(connection, fields, "DRAKKARKEEL.SEARCH_PRINCIPLE", "ID_SP", principle);

        Object[][] values = table.getValues();

        if (values.length != 0) {
            result = values[0][0].toString();

        }

        return result;

    }

    /**
     * Guarda la relación de usuario y consulta
     *
     * @param user            nombre del usuario
     * @param idQuery         id de la consulta
     * 
     * @throws SQLException
     */
    private void saveSeekerQuery(String user, int idQuery) throws SQLException {
        String[] fields = new String[2];
        fields[0] = "ID_QUERY";
        fields[1] = "SEEKER_USER";

        Object[] oneValue = new Object[2];
        oneValue[0] = idQuery;
        oneValue[1] = user;

        if (!util.relationExist("DRAKKARKELL.SEEKER_QUERY", "ID_QUERY", idQuery, "SEEKER_USER", user)) {
            PersistentOperations.insert(connection, "DRAKKARKELL.SEEKER_QUERY", fields, oneValue);
        }

    }

    /**
     * Guarda la relación de la sesión y la consulta
     *
     * @param user            nombre de la sesión
     * @param idQuery         id de la consulta
     *
     * @throws SQLException
     */
    private void saveSessionQuery(String session, int idQuery) throws SQLException {
        String[] fields = new String[2];
        fields[0] = "ID_QUERY";
        fields[1] = "SESSION_TOPIC";

        Object[] oneValue = new Object[2];
        oneValue[0] = idQuery;
        oneValue[1] = session;

        if (!util.relationExist("DRAKKARKEEL.SEARCH_SESSION_QUERY", "ID_QUERY", idQuery, "SESSION_TOPIC", session)) {
            PersistentOperations.insert(connection, "DRAKKARKEEL.SEARCH_SESSION_QUERY", fields, oneValue);
        }
    }

    /**
     * Guarda la relación de la consulta con un motor de búsqueda.
     *
     * @param idQuery           id de la consulta
     * @param searchEngine      id del motor
     * 
     * @throws SQLException
     */
    private void saveQueryEngine(int idQuery, int searchEngine) throws SQLException {

        String[] fields = new String[2];
        fields[0] = "ID_QUERY";
        fields[1] = "ID_SE";

        Object[] oneValue = new Object[2];
        oneValue[0] = idQuery;
        oneValue[1] = searchEngine;

        if (!util.relationExist("DRAKKARKEEL.QUERY_SEARCH_ENGINE", "ID_QUERY", idQuery, "ID_SE", searchEngine)) {
            PersistentOperations.insert(connection, "DRAKKARKEEL.QUERY_SEARCH_ENGINE", fields, oneValue);
        }


    }

    /**
     * Guarda la relación de los resultados de búsqueda con su consulta
     *
     * @param idQuery        id de la consulta
     * @param results        resultados de búsqueda
     * 
     * @throws SQLException
     */
    private void saveQueryResults(int idQuery, List<DocumentMetaData> results, String session) throws SQLException {

        int idDoc = 0;
        //guarda los resultados
        for (int i = 0; i < results.size(); i++) {
            DocumentMetaData metaDocument = results.get(i);
            saveDocumentData(session, metaDocument.getPath(), metaDocument.getScore(), metaDocument.getIndex(), metaDocument.getName(), metaDocument.getSize(), metaDocument.getType(), false, metaDocument.getSynthesis(), metaDocument.getAuthor(), metaDocument.getLastModified());

        }

        //guarda la relaci'on de los resultados con la consulta
        for (int i = 0; i < results.size(); i++) {
            String path = results.get(i).getPath();
            idDoc = getResultId(path, session);

            String[] fields = new String[2];
            fields[0] = "ID_QUERY";
            fields[1] = "ID_SR";

            Object[] oneValue = new Object[2];
            oneValue[0] = idQuery;
            oneValue[1] = idDoc;

            if (!util.relationExist("DRAKKARKEEL.QUERY_SEARCH_RESULT", "ID_QUERY", idQuery, "ID_SR", idDoc)) {
                PersistentOperations.insert(connection, "DRAKKARKEEL.QUERY_SEARCH_RESULT", fields, oneValue);
            }

        }


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
        String[] fields = new String[1];
        fields[0] = "STATE_TYPE";
        String result = null;
        List<String> list = new ArrayList<>();

        TableTracker table = PersistentOperations.load(connection, fields, "DRAKKARKEEL.SEEKER_STATE");

        Object[][] values = table.getValues();

        if (values.length != 0) {
            for (int i = 0; i < values.length; i++) {
                Object[] objects = values[i];
                result = objects[0].toString();
                list.add(result);
            }

        }

        return list;
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
        String[] fields = new String[1];
        fields[0] = "ID_SE";
        String result = null;
        int id = 0;

        TableTracker table = PersistentOperations.load(connection, fields, "DRAKKARKEEL.SEARCH_ENGINE", "SE_NAME", engine);

        Object[][] values = table.getValues();

        if (values.length != 0) {
            result = values[0][0].toString();
            id = Integer.parseInt(result);
        }

        return id;
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
        String[] fields = new String[1];
        fields[0] = "SE_NAME";
        String result = null;

        TableTracker table = PersistentOperations.load(connection, fields, "DRAKKARKEEL.SEARCH_ENGINE", "ID_SE", idEngine);

        Object[][] values1 = table.getValues();

        if (values1.length != 0) {
            result = values1[0][0].toString();
        }

        return result;
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
        String[] fields = new String[1];
        fields[0] = "ID_SE";
        int id = 0;
        List<String> engines = null;

        TableTracker table = PersistentOperations.load(connection, fields, "DRAKKARKEEL.QUERY_SEARCH_ENGINE", "ID_QUERY", query);

        Object[][] values = table.getValues();

        if (values.length != 0) {

            for (int i = 0; i < values.length; i++) {
                Object[] obj = values[i];
                id = Integer.valueOf(obj[0].toString());
                engines.add(this.getSearchEngine(id));
            }

        }

        return engines;
    }

    /***************SEARCH RESULT**********************/
    /**
     * Guarda los datos de un resultado de búsqueda
     *
     * @param uri                ubicación del documento
     * @param score              puntuación del motor para dicho documento
     * @param index              posición del documento en el índice
     * @param name               nombre del documento
     * @param size               tamaño
     * @param type               tipo de archivo
     * @param review             true si está revisado, false si no
     * 
     * @throws SQLException       si ocurre alguna SQLException durante la ejecución
     *                                de la operación
     */
    private void saveDocumentData(String session, String uri, double score, int index, String name, float size, String type, boolean review, String summary, String author, String lastModified) throws SQLException {

        String[] fields = new String[9];
        fields[0] = "URI";
        fields[1] = "SCORE";
        fields[2] = "INDEX";
        fields[3] = "SR_NAME";
        fields[4] = "SR_SIZE";
        fields[5] = "SR_TYPE";
        fields[6] = "REVIEW";
        fields[7] = "SESSION_TOPIC";
        fields[8] = "SUMMARY";
        fields[9] = "AUTHOR";
        fields[10] = "LASTMODIFIED";

        Object[] oneValue = new Object[11];
        oneValue[0] = uri;
        oneValue[1] = score;
        oneValue[2] = index;
        oneValue[3] = name;
        oneValue[4] = size;
        oneValue[5] = type;
        oneValue[6] = review;
        oneValue[7] = session;
        oneValue[8] = summary;
        oneValue[9] = author;
        oneValue[10] = lastModified;

        if (!util.relationExist("DRAKKARKEEL.SEARCH_RESULT", "URI", uri, "SESSION_TOPIC", session)) {
            PersistentOperations.insert(connection, "DRAKKARKEEL.SEARCH_RESULT", fields, oneValue);
        }

    }

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

        String[] fields = new String[1];
        fields[0] = "REVIEW";

        Object[] oneValue = new Object[1];
        oneValue[0] = review;

        PersistentOperations.update(connection, "DRAKKARKEEL.SEARCH_RESULT", fields, oneValue, "ID_SR", idDoc);
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

        String[] fields = new String[1];
        fields[0] = "REVIEW";

        Object[] oneValue = new Object[1];
        oneValue[0] = true;

        String[] whereFields = new String[2];
        whereFields[0] = "URI";
        whereFields[1] = "SESSION_TOPIC";

        Object[] whereValues = new Object[2];
        whereValues[0] = uri;
        whereValues[1] = session;

        String[] whereFields1 = new String[3];
        whereFields1[0] = "URI";
        whereFields1[1] = "SESSION_TOPIC";
        whereFields1[2] = "REVIEW";

        Object[] whereValues1 = new Object[3];
        whereValues1[0] = uri;
        whereValues1[1] = session;
        whereValues1[2] = true;

        if (!util.alreadyExist("DRAKKARKEEL.SEARCH_RESULT", whereFields1, whereValues1, "ID_SR")) {
            PersistentOperations.update(connection, "DRAKKARKEEL.SEARCH_RESULT", fields, oneValue, whereFields, whereValues);
        }

    }

    /**
     * Guarda los datos de la recomendación hecha por un usuario a muchos  usuarios
     * de su sesión
     *
     * @param session              nombre de la sesión
     * @param user                 nombre del usuario que emitió la recomendación
     * @param receptors            lista de recpetores
     * @param idDoc                id del documento
     * @param text                 texto o comentario de la recomendación
     * 
     * @throws SQLException        si ocurre alguna SQLException durante la ejecución  de la operación
     */
    public void saveRecommendation(String session, String user, List<Seeker> receptors, int idDoc, String text) throws SQLException {
        String[] fields = new String[5];
        fields[0] = "SEEKER_RECEPTOR";
        fields[1] = "TEXT";
        fields[2] = "SEEKER_USER";
        fields[3] = "ID_SR";
        fields[4] = "SESSION_TOPIC";

        Object[] oneValue = new Object[5];
        String rec = null;
        for (int i = 0; i < receptors.size(); i++) {
            rec = receptors.get(i).getUser();

            oneValue[0] = rec;
            oneValue[1] = text;
            oneValue[2] = user;
            oneValue[3] = idDoc;
            oneValue[4] = session;

            if (!util.alreadyExist("DRAKKARKEEL.RECOMMENDATION", fields, oneValue, "ID_REC")) {
                PersistentOperations.insert(connection, "DRAKKARKEEL.RECOMMENDATION", fields, oneValue);
            }

        }



    }

    /**
     * Guarda los datos de la recomendación hecha por un usuario a muchos  usuarios
     * de su sesión
     *
     * @param session              nombre de la sesión
     * @param user                 nombre del usuario que emitió la recomendación
     * @param receptors            lista de receptores
     * @param list                 documentos
     * @param text                 texto o comentario de la recomendación
     *
     * @throws SQLException        si ocurre alguna SQLException durante la ejecución  de la operación
     */
    public void saveRecommendation(String session, String user, List<Seeker> receptors, ResultSetMetaData list, String text) throws SQLException {
        String[] fields = new String[5];
        fields[0] = "SEEKER_RECEPTOR";
        fields[1] = "TEXT";
        fields[2] = "SEEKER_USER";
        fields[3] = "ID_SR";
        fields[4] = "SESSION_TOPIC";
        Object[] oneValue = new Object[5];
        String rec = null;

        List<DocumentMetaData> metaDocs = list.getAllResultList();

        List<Integer> idList = new ArrayList<>();

        for (DocumentMetaData metaDocument : metaDocs) {
            int ide = this.getResultId(metaDocument.getPath(), session);
            idList.add(ide);
        }

        oneValue[1] = text;
        oneValue[2] = user;
        oneValue[4] = session;

        for (int i = 0; i < receptors.size(); i++) {
            rec = receptors.get(i).getUser();
            oneValue[0] = rec;

            for (Integer integer : idList) {
                oneValue[3] = integer;
                if (!util.alreadyExist("DRAKKARKEEL.RECOMMENDATION", fields, oneValue, "ID_REC")) {
                    PersistentOperations.insert(connection, "DRAKKARKEEL.RECOMMENDATION", fields, oneValue);
                }

            }

        }



    }

    /**
     *
     * Guarda los datos de la recomendación de varios documentos hecha por un usuario a muchos  usuarios
     * de su sesión
     *
     * @param session              nombre de la sesión
     * @param user                 nombre del usuario que emitió la recomendación
     * @param receptors            lista de receptores
     * @param idDoc                documentos
     * @param text                 texto o comentario de la recomendación
     *
     * @throws SQLException        si ocurre alguna SQLException durante la ejecución  de la operación
     */
    public void saveRecommendation(String session, String user, List<Seeker> receptors, List<Integer> idDoc, String text) throws SQLException {
        String[] fields = new String[5];
        fields[0] = "SEEKER_RECEPTOR";
        fields[1] = "TEXT";
        fields[2] = "SEEKER_USER";
        fields[3] = "ID_SR";
        fields[4] = "SESSION_TOPIC";

        Object[] oneValue = new Object[5];
        oneValue[1] = text;
        oneValue[2] = user;
        oneValue[4] = session;

        for (int i = 0; i < receptors.size(); i++) {
            oneValue[0] = receptors.get(i).getUser();
            for (Integer id : idDoc) {
                oneValue[3] = id;
                if (!util.alreadyExist("DRAKKARKEEL.RECOMMENDATION", fields, oneValue, "ID_REC")) {
                    PersistentOperations.insert(connection, "DRAKKARKEEL.RECOMMENDATION", fields, oneValue);
                }

            }

        }



    }

    /**
     * Guarda los datos de la recomendación hecha por un usuario a un  usuario
     * de su sesión
     *
     * @param session              nombre de la sesión
     * @param user                 nombre del usuario que emitió la recomendación
     * @param receptor             receptor
     * @param idDoc                documentos
     * @param text                 texto o comentario de la recomendación
     *
     * @throws SQLException        si ocurre alguna SQLException durante la ejecución  de la operación
     */
    public void saveRecommendation(String session, String user, Seeker receptor, int idDoc, String text) throws SQLException {
        String[] fields = new String[5];
        fields[0] = "SEEKER_RECEPTOR";
        fields[1] = "TEXT";
        fields[2] = "SEEKER_USER";
        fields[3] = "ID_SR";
        fields[4] = "SESSION_TOPIC";

        Object[] oneValue = new Object[5];

        oneValue[0] = receptor.getUser();
        oneValue[1] = text;
        oneValue[2] = user;
        oneValue[3] = idDoc;
        oneValue[4] = session;

        if (!util.alreadyExist("DRAKKARKEEL.RECOMMENDATION", fields, oneValue, "ID_REC")) {
            PersistentOperations.insert(connection, "DRAKKARKEEL.RECOMMENDATION", fields, oneValue);
        }


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

        SearchResultData data = null;
        String uri = null, name = null, type = null, summary = null, author= null, lastModified= null;
        double score = 0;
        int size = 0, index = 0;
        boolean review = false;

        String[] fields = new String[10];
        fields[0] = "URI";
        fields[1] = "SCORE";
        fields[2] = "INDEX";
        fields[3] = "SR_NAME";
        fields[4] = "SR_SIZE";
        fields[5] = "SR_TYPE";
        fields[6] = "REVIEW";
        fields[7] = "SUMMARY";
        fields[8] = "AUTHOR";
        fields[9] = "LASTMODIFIED";

        String[] whereFields = new String[2];
        whereFields[0] = "ID_SR";
        whereFields[1] = "SESSION_TOPIC";

        Object[] whereValues = new Object[2];
        whereValues[0] = id;
        whereValues[1] = session;

        TableTracker table = PersistentOperations.load(connection, fields, "DRAKKARKEEL.SEARCH_RESULT", whereFields, whereValues);

        Object[][] values = table.getValues();

        if (values.length != 0) {

            uri = values[0][0].toString();
            score = Double.parseDouble(values[0][1].toString());
            index = Integer.parseInt(values[0][2].toString());
            name = values[0][3].toString();
            size = Integer.parseInt(values[0][4].toString());
            type = values[0][5].toString();
            review = Boolean.parseBoolean(values[0][6].toString());
            summary = values[0][7].toString();
            author= values[0][8].toString();
            lastModified= values[0][9].toString();

        }

        data = new SearchResultData(id, uri, index, size, name, review, score, type, session, summary, KeySearchable.MULTIPLE_SEARCHERS, author, lastModified);

        return data;
    }

    /**
     * Guarda la relación de resultados de búsqueda por cada motor.
     *
     * @param idSE             id del motor
     * @param list             lista de resultados
     * 
     * @throws SQLException        si ocurre alguna SQLException durante la ejecución  de la operación
     */
    private void saveResultEngine(int idSE, List<DocumentMetaData> list, String session) throws SQLException {
        String[] fieldToSave = new String[2];
        fieldToSave[0] = "ID_SE";
        fieldToSave[1] = "ID_SR";
        Object[] oneValue = new Object[2];

        for (int i = 0; i < list.size(); i++) {
            DocumentMetaData metaDocument = list.get(i);
            String path = metaDocument.getPath();
            int idDoc = getResultId(path, session);
            oneValue[0] = idSE;
            oneValue[1] = idDoc;


            if (!util.relationExist("DRAKKARKEEL.SEARCH_ENGINE_SEARCH_RESULT", "ID_SE", idSE, "ID_SR", idDoc)) {
                PersistentOperations.insert(connection, "DRAKKARKEEL.SEARCH_ENGINE_SEARCH_RESULT", fieldToSave, oneValue);
            }

        }
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
        int id = 0;
        String[] fields = new String[1];
        fields[0] = "ID_SR";

        String[] whereFields = new String[2];
        whereFields[0] = "URI";
        whereFields[1] = "SESSION_TOPIC";

        String[] whereValues = new String[2];
        whereValues[0] = uri;
        whereValues[1] = session;

        TableTracker table = PersistentOperations.load(connection, fields, "DRAKKARKEEL.SEARCH_RESULT", whereFields, whereValues);

        Object[][] values = table.getValues();

        if (values.length != 0) {
            id = Integer.parseInt(values[0][0].toString());
        }

        return id;
    }

    /**
     * Guarda los datos de la revisión hecha por un usuario a un resultado de búsqueda
     *
     * @param session             nombre de la sesión
     * @param comment                  comentario sobre el documento
     * @param relevance                valor de relevancia dado al documento
     * @param user                     nombre del usuario
     * @param uri                      uri del documento
     * 
     * @throws SQLException        si ocurre alguna SQLException durante la ejecución  de la operación
     */
    public void saveEvaluation(String session, String comment, int relevance, String user, String uri) throws SQLException {
        int idDoc = getResultId(uri, session);

        String[] whereFields = new String[3];
        whereFields[0] = "SESSION_TOPIC";
        whereFields[1] = "SEEKER_USER";
        whereFields[2] = "ID_SR";

        Object[] whereValues = new Object[3];
        whereValues[0] = session;
        whereValues[1] = user;
        whereValues[2] = idDoc;

        if (util.alreadyExist("DRAKKARKEEL.MARKUP", whereFields, whereValues, "ID_MARKUP")) {
            updateRelevance(session, user, idDoc, relevance);
        } else {
            String[] fields = new String[5];
            fields[0] = "COMMENT";
            fields[1] = "RELEVANCE";
            fields[2] = "SEEKER_USER";
            fields[3] = "ID_SR";
            fields[4] = "SESSION_TOPIC";

            Object[] oneValue = new Object[5];
            oneValue[0] = comment;
            oneValue[1] = relevance;
            oneValue[2] = user;
            oneValue[3] = idDoc;
            oneValue[4] = session;
            if (relevance != 0) {
                PersistentOperations.insert(connection, "DRAKKARKEEL.MARKUP", fields, oneValue);
            }
        }


    }

    /**
     * Inserta un comentario realizado por un usuario para un documento
     * en una sesión, de existir lo reemplaza
     *
     * @param session              nombre de la sesión
     * @param seeker               objeto seeker del usuario
     * @param uri                  uri del documento evaluado
     * @param comment              comentario
     *
     * @throws SQLException        si ocurre alguna SQLException durante la ejecución  de la operación
     */
    public void updateComment(String session, Seeker seeker, String uri, String comment) throws SQLException {

        int idDoc = getResultId(uri, session);
        String[] fields = new String[1];
        fields[0] = "COMMENT";

        Object[] value = new Object[1];
        value[0] = comment;

        String[] whereFields = new String[3];
        whereFields[0] = "SESSION_TOPIC";
        whereFields[1] = "SEEKER_USER";
        whereFields[2] = "ID_SR";

        Object[] whereValues = new Object[3];
        whereValues[0] = session;
        whereValues[1] = seeker.getUser();
        whereValues[2] = idDoc;

        PersistentOperations.update(connection, "DRAKKARKEEL.MARKUP", fields, value, whereFields, whereValues);
    }

    /**
     *
     * Actualiza el valor de relevancia realizado por un usuario para un documento
     * en una sesión
     *
     * @param session              nombre de la sesión
     * @param seeker               objeto seeker del usuario
     * @param idDoc                id del documento
     * @param relevance            relevancia
     *
     * @throws SQLException        si ocurre alguna SQLException durante la ejecución  de la operación
     */
    public void updateRelevance(String session, String seeker, int idDoc, int relevance) throws SQLException {

        String[] fields = new String[1];
        fields[0] = "RELEVANCE";

        Object[] value = new Object[1];
        value[0] = relevance;

        String[] whereFields = new String[3];
        whereFields[0] = "SESSION_TOPIC";
        whereFields[1] = "SEEKER_USER";
        whereFields[2] = "ID_SR";

        Object[] whereValues = new Object[3];
        whereValues[0] = session;
        whereValues[1] = seeker;
        whereValues[2] = idDoc;

        PersistentOperations.update(connection, "DRAKKARKEEL.MARKUP", fields, value, whereFields, whereValues);
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

        int id = 0;

        String[] fields = new String[1];
        fields[0] = "ID_MARKUP";

        String[] whereFields = new String[2];
        whereFields[0] = "SEEKER_USER";
        whereFields[1] = "ID_SR";

        Object[] whereValues = new Object[2];
        whereValues[0] = user;
        whereValues[1] = idDoc;

        TableTracker table = PersistentOperations.load(connection, fields, "DRAKKARKEEL.MARKUP", whereFields, whereValues);
//        TableTracker table = PersistentOperations.load(connection, fields, "DRAKKARKEEL.MARKUP", "SEEKER_USER", "ID_SR", user, idDoc);


        Object[][] values = table.getValues();

        if (values.length != 0) {
            id = Integer.parseInt(values[0][0].toString());
        }

        return id;
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

        List<Integer> results1 = new ArrayList<>();
        // List<Integer> results2 = new List<Integer>();
        List<Integer> resultsFinal = new ArrayList<>();
        String[] fields = new String[1];
        fields[0] = "ID_SR";

        //get all results for a query
        TableTracker table = PersistentOperations.load(connection, fields, "DRAKKARKEEL.QUERY_SEARCH_RESULT", "SEARCH_RESULT", "ID_SR", "ID_SR", "SESSION_TOPIC", session);
        Object[][] values = table.getValues();
        if (values.length != 0) {
            for (int j = 0; j < values.length; j++) {
                Object[] objects = values[j];

                int idResult = Integer.parseInt(objects[0].toString());
                results1.add(idResult);
            }
        }


        return resultsFinal;
    }

    /**
     * Obtiene el id de la última consulta
     *
     * @return
     * @throws SQLException        si ocurre alguna SQLException durante la ejecución  de la operación
     */
    private int getLastQuery() throws SQLException {

        int count = 0;

        TableTracker table = PersistentOperations.countMaxRegister(connection, "DRAKKARKEEL.QUERY", "ID_QUERY");

        Object[][] values = table.getValues();

        if (values.length != 0) {
            count = Integer.valueOf(values[0][0].toString());
        }

        return count;
    }

    /**
     * Obtiene la lista de id de consultas realizadas por un usuario
     *
     * @param user nombre del usuario
     */
    private List<Integer> getQuerySeeker(String user) throws SQLException {

        List<Integer> list = new ArrayList<>();
        int idQ = 0;

        String fields[] = new String[1];
        fields[0] = "ID_QUERY";

        TableTracker table = PersistentOperations.load(connection, fields, "DRAKKARKELL.SEEKER_QUERY", "SEEKER_USER", user);

        Object[][] val = table.getValues();

        if (val.length != 0) {
            for (int i = 0; i < val.length; i++) {
                Object[] objects = val[i];
                idQ = Integer.valueOf(objects[0].toString());

                list.add(idQ);
            }

        }

        return list;

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
     * @throws SQLException        si ocurre alguna SQLException durante la ejecución  de la operación   si ocurre alguna SQLException durante la ejecución
     *                                de la operación
     */
    public List<DocumentMetaData> getSearchResults(List<Integer> list) throws SQLException {

        List<DocumentMetaData> docs = new ArrayList<>();
        String name, uri, type, summary, author, lastModified;
        int index;
        float size;
        double score;
        DocumentMetaData metaDoc;

        String fields[] = new String[9];
        fields[0] = "URI";
        fields[1] = "SCORE";
        fields[2] = "INDEX";
        fields[3] = "SR_NAME";
        fields[4] = "SR_SIZE";
        fields[5] = "SR_TYPE";
        fields[6] = "SUMMARY";
        fields[7] = "AUTHOR";
        fields[8] = "LASTMODIFIED";

        TableTracker table;

        for (Integer i : list) {

            table = PersistentOperations.load(connection, fields, "DRAKKARKEEL.SEARCH_RESULT", "ID_SR", i);

            Object[][] values = table.getValues();
            if (values.length != 0) {

                for (int j = 0; j < values.length; j++) {
                    Object[] objects = values[j];
                    uri = objects[0].toString();
                    score = Double.valueOf(objects[1].toString());
                    index = Integer.valueOf(objects[2].toString());
                    name = objects[3].toString();
                    size = Float.valueOf(objects[4].toString());
                    type = objects[5].toString();
                    summary = objects[6].toString();
                    author= objects[7].toString();
                    lastModified= objects[8].toString();
                    metaDoc = new DocumentMetaData(author, lastModified, name, uri, size, summary, type, index, score, KeySearchable.MULTIPLE_SEARCHERS);
                    docs.add(metaDoc);
                }
            }

        }

        return docs;


    }

    /**
     * Devuelve todos los id pertenecientes a una consulta
     *
     * (las consultas se pueden repetir)
     *
     * @param text   texto de la consulta
     */
    private List<Integer> getQueryId(String text) throws SQLException {
        List<Integer> list = new ArrayList<>();
        int idQuery = 0;
        String fields[] = new String[1];
        fields[0] = "ID_QUERY";

        TableTracker table = PersistentOperations.load(connection, fields, "DRAKKARKEEL.QUERY", "TEXT", text);
        Object[][] values = table.getValues();

        if (values.length != 0) {
            for (int i = 0; i < values.length; i++) {
                Object[] objects = values[i];
                idQuery = Integer.valueOf(objects[0].toString());
                list.add(idQuery);
            }

        }


        return list;
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
     * @throws SQLException        si ocurre alguna SQLException durante la ejecución  de la operación   si ocurre alguna SQLException durante la ejecución
     *                                de la operación
     */
    public List<DocumentMetaData> getSearchResults(String session, String query, String engine) throws SQLException {

        List<DocumentMetaData> docs = new ArrayList<>();
        List<Integer> idResults = new ArrayList<>();
        List<Integer> idQueries = new ArrayList<>();
        DocumentMetaData metaDoc;

        String fields1[] = new String[1];
        fields1[0] = "DRAKKARKEEL.SEARCH_ENGINE_SEARCH_RESULT.ID_SR";

        int idSE = this.getSearchEngine(engine);

        String whereFields[] = new String[2];
        whereFields[0] = "ID_SE";
        whereFields[1] = "DRAKKARKEEL.QUERY_SEARCH_ENGINE.ID_QUERY";

        Object whereValues[] = new Object[2];
        whereValues[0] = idSE;

        idQueries = this.getQueryId(query);
        TableTracker table;
        Object[][] values;

        for (Integer idq : idQueries) {
            whereValues[1] = idq;

            table = PersistentOperations.loadResults(connection, fields1, "DRAKKARKEEL.QUERY_SEARCH_RESULT", "DRAKKARKEEL.QUERY_SEARCH_ENGINE", "DRAKKARKEEL.SEARCH_ENGINE_SEARCH_RESULT", "ID_QUERY", "ID_QUERY", "ID_SR", whereFields, whereValues);

            values = table.getValues();

            //get all results id
            if (values.length != 0) {
                for (int i = 0; i < values.length; i++) {
                    Object[] objects = values[i];
                    int id = Integer.valueOf(objects[0].toString());
                    if (!idResults.contains(id)) {
                        idResults.add(id);
                    }

                }
            }

            //get all metadocuments
            SearchResultData data;
            for (Integer idR : idResults) {
                data = this.getResult(idR, session);
                metaDoc = new DocumentMetaData(data.getAuthor(), data.getLastModified(), data.getName(), data.getURI(), data.getSize(), data.getSummary(), data.getType(), data.getIndex(), data.getScore(), KeySearchable.MULTIPLE_SEARCHERS);
                docs.add(metaDoc);
            }
        }


        return docs;


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
     * @throws SQLException        si ocurre alguna SQLException durante la ejecución  de la operación   si ocurre alguna SQLException durante la ejecución
     *                                de la operación
     */
    public List<DocumentMetaData> getSearchResults(String session, String query) throws SQLException {

        List<DocumentMetaData> docs = new ArrayList<>();
        List<Integer> idResults = new ArrayList<>();
        List<Integer> idQueries = new ArrayList<>();
        DocumentMetaData metaDoc;

        String fields1[] = new String[1];
        fields1[0] = "DRAKKARKEEL.SEARCH_RESULT.ID_SR";

        String whereFields[] = new String[2];
        whereFields[0] = "DRAKKARKEEL.QUERY_SEARCH_RESULT.ID_QUERY";
        whereFields[1] = "DRAKKARKEEL.SEARCH_RESULT.SESSION_TOPIC";

        Object whereValues[] = new Object[2];
        whereValues[1] = session;

        idQueries = this.getQueryId(query);

        TableTracker table;
        Object[][] values;

        for (Integer idq : idQueries) {
            whereValues[0] = idq;

            table = PersistentOperations.load(connection, fields1, "DRAKKARKEEL.QUERY_SEARCH_RESULT", "DRAKKARKEEL.SEARCH_RESULT", "ID_SR", "ID_SR", whereFields, whereValues);

            values = table.getValues();

            //get all results id
            if (values.length != 0) {
                for (int i = 0; i < values.length; i++) {
                    Object[] objects = values[i];
                    int id = Integer.valueOf(objects[0].toString());
                    if (!idResults.contains(id)) {
                        idResults.add(id);
                    }

                }
            }

            //get all metadocuments
            SearchResultData data;
            for (Integer idR : idResults) {
                data = this.getResult(idR, session);
                metaDoc = new DocumentMetaData(data.getAuthor(), data.getLastModified(), data.getName(), data.getURI(), data.getSize(), data.getSummary(), data.getType(), data.getIndex(), data.getScore(), KeySearchable.MULTIPLE_SEARCHERS);
                docs.add(metaDoc);
            }
        }

        return docs;


    }
}
