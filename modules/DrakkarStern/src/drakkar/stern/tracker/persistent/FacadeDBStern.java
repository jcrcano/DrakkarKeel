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

import drakkar.oar.svn.SVNData;
import drakkar.stern.tracker.persistent.objects.IndexData;
import drakkar.stern.tracker.persistent.tables.DerbyConnection;
import drakkar.stern.tracker.persistent.tables.PersistentOperations;
import drakkar.stern.tracker.persistent.tables.TableTracker;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase para manejar los datos para la persistencia de DrakkarKeel
 * utilizados en la aplicación servidora
 */
public class FacadeDBStern {

    DerbyConnection connection;
    DBUtil util;

    /**
     * Constructor de la clase
     *
     * @param connection
     * @param util
     */
    public FacadeDBStern(DerbyConnection connection, DBUtil util) {

        this.connection = connection;
        this.util = util;
    }

    /**
     * Inserta los datos de un nuevo índice
     *
     * @param uri                dirección del documento
     * @param idEngine           id del motor de búsqueda
     * @param docs               cantidad de documentos
     *
     * @return                   si retorna false es que ya el path de ese indice está en la BD.
     * 
     * @throws SQLException        si ocurre alguna SQLException durante la ejecución  de la operación
     */
    public boolean insertIndex(String uri, int idEngine, int docs) throws SQLException {

        if (!util.alreadyExist(uri, "DRAKKARKEEL.INDEX", "URI", "ID_INDEX")) { //si no existe

            String[] fields = new String[3];
            fields[0] = "URI";
            fields[1] = "ID_SE";
            fields[2] = "DOC_COUNT";

            Object[] oneValue = new Object[3];
            oneValue[0] = uri;
            oneValue[1] = idEngine;
            oneValue[2] = docs;

            PersistentOperations.insert(connection, "DRAKKARKEEL.INDEX", fields, oneValue);

            return true;

        }

        return false;

    }

    /**
     * Elimina un índice
     *
     * @param uri             path del índice
     *
     * @throws SQLException        si ocurre alguna SQLException durante la ejecución  de la operación
     */
    public void deleteIndex(String uri) throws SQLException {

        if (util.alreadyExist(uri, "DRAKKARKEEL.INDEX", "URI", "ID_INDEX")) {
            int id = this.getIndexId(uri);
            PersistentOperations.delete(connection, "DRAKKARKEEL.INDEX_COLLECTION", "ID_INDEX", id);

            PersistentOperations.delete(connection, "DRAKKARKEEL.INDEX", "URI", uri);
        }
    }

    /**
     * Obtiene todos los índices guardados
     *
     * @return
     * @throws SQLException        si ocurre alguna SQLException durante la ejecución  de la operación
     */
    public List<IndexData> getAllIndex() throws SQLException {

        IndexData index = null;
        String uri, date, engine;
        int count = 0;
        List<IndexData> list = new ArrayList<>();

        String[] fields = new String[4];
        fields[0] = "ID_SE";
        fields[1] = "URI";
        fields[2] = "CREATION_DATE";
        fields[3] = "DOC_COUNT";

        TableTracker table = PersistentOperations.load(connection, fields, "DRAKKARKEEL.INDEX");

        Object[][] values = table.getValues();
        if (values.length != 0) {
            for (int i = 0; i < values.length; i++) {
                Object[] objects = values[i];
                int ideng = Integer.parseInt(objects[0].toString());
                engine = getSearchEngine(ideng);
                uri = objects[1].toString();
                date = objects[2].toString();
                count = Integer.valueOf(objects[3].toString());
                index = new IndexData(uri, date, engine, count);
                list.add(index);
            }

        }

        return list;
    }

    /**
     * Devuelve el nombre de un motor de búsqueda
     *
     * @param idEngine           id del motor
     * @return                   nombre en la tabla
     * @throws SQLException        si ocurre alguna SQLException durante la ejecución  de la operación
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
     * Actualiza los datos de un índice
     *
     * @param date            fecha
     * @param uri             ubicación
     * @param count           cantidad de documentos
     * 
     * @throws SQLException        si ocurre alguna SQLException durante la ejecución  de la operación
     */
    public void updateDataIndex(Date date, String uri, int count) throws SQLException {

        String[] fields = new String[2];
        fields[0] = "CREATION_DATE";
        fields[1] = "DOC_COUNT";

        Object[] oneValue = new Object[2];
        oneValue[0] = date;
        oneValue[1] = count;

        if (util.alreadyExist(uri, "DRAKKARKEEL.INDEX", "URI", "ID_INDEX")) {
            PersistentOperations.update(connection, "DRAKKARKEEL.INDEX", fields, oneValue, "URI", uri);
        }

    }

    /**
     * Incrementa el número de documentos del índice
     *
     * @param uri             ubicación
     * @param count           cantidad de documentos
     * 
     * @throws SQLException        si ocurre alguna SQLException durante la ejecución  de la operación
     */
    public void addDocsIndex(String uri, int count) throws SQLException {

        String[] fields = new String[1];
        fields[0] = "DOC_COUNT";

        if (util.alreadyExist(uri, "DRAKKARKEEL.INDEX", "URI", "ID_INDEX")) {
            TableTracker table = PersistentOperations.load(connection, fields, "DRAKKARKEEL.INDEX", "URI", uri);
            Object[][] val = table.getValues();
            int cant = Integer.valueOf(val[0][0].toString());

            Object[] oneValue = new Object[1];
            oneValue[0] = cant + count;

            PersistentOperations.update(connection, "DRAKKARKEEL.INDEX", fields, oneValue, "URI", uri);

        }

    }

    /**
     * Guarda los datos de una colección a indexar
     *
     * @param collectionPath        dirección del repositorio
     * @param context               contexto de la colección (tema)
     * @param docs                  número de documentos de la colección
     * @param direct                true si el path es directo
     * @param indexUri              uri del índice
     * @param user                  usuario (para repositorios SVN)
     * @param password              contraseña (para repositorios SVN)
     * @param repositoryName        nombre del repositorio (para repositorios SVN)
     * 
     * @return
     * @throws SQLException        si ocurre alguna SQLException durante la ejecución  de la operación
     */
    public boolean insertCollection(String collectionPath, String context, int docs, boolean direct, List<String> indexUri, String user, String password, String repositoryName) throws SQLException {

        if (!util.alreadyExist(collectionPath, "DRAKKARKEEL.COLLECTION", "COLLECTION_PATH", "ID_COLLECTION")) { //si no existe

            String[] fields = new String[4];
            fields[0] = "COLLECTION_PATH";
            fields[1] = "CONTEXT";
            fields[2] = "DOC_COUNT";
            fields[3] = "DIRECT_PATH";

            Object[] oneValue = new Object[4];
            oneValue[0] = collectionPath;
            oneValue[1] = context;
            oneValue[2] = docs;
            oneValue[3] = direct;

            PersistentOperations.insert(connection, "DRAKKARKEEL.COLLECTION", fields, oneValue);

            //solo inserta en esta tabla cuando se indexa un repositorio SVN
            if (repositoryName != null && !repositoryName.equals("")) {
                this.insertIndexCollection(indexUri, collectionPath, user, password, repositoryName);
            }


            return true;

        }

        return false;

    }

    /**
     * Relaciona una sesión con un repositorio de documentos
     *
     * @param session             nombre de la sesión
     * @param collectionPath      dirección de la colección
     * 
     * @return
     * @throws SQLException        si ocurre alguna SQLException durante la ejecución  de la operación
     */
    public boolean insertSessionCollection(String session, String collectionPath) throws SQLException {

        String[] whereFields = new String[2];
        whereFields[0] = "SESSION_TOPIC";
        whereFields[1] = "ID_COLLECTION";

        Object[] whereValues = new Object[2];
        whereValues[0] = session;
        whereValues[1] = this.getCollectionId(collectionPath);

        if (!util.alreadyExist("DRAKKARKEEL.SEARCH_SESSION_COLLECTION", whereFields, whereValues, "SESSION_TOPIC")) {

            String[] fields = new String[2];
            fields[0] = "SESSION_TOPIC";
            fields[1] = "COLLECTION_TYPE";

            Object[] oneValue = new Object[2];
            oneValue[0] = session;
            oneValue[1] = collectionPath;

            PersistentOperations.insert(connection, "DRAKKARKEEL.SEARCH_SESSION_COLLECTION", fields, oneValue);

            return true;

        }

        return false;

    }

    /**
     * Obtiene el id de una colección
     * 
     * @param path              path de la colección
     * 
     * @return
     * @throws SQLException        si ocurre alguna SQLException durante la ejecución  de la operación
     */
    public int getCollectionId(String path) throws SQLException {

        int id = 0;
        String[] fields = new String[1];
        fields[0] = "ID_COLLECTION";

        TableTracker table = PersistentOperations.load(connection, fields, "DRAKKARKEEL.COLLECTION", "COLLECTION_PATH", path);
        Object[][] values = table.getValues();

        if (values.length != 0) {
            id = Integer.valueOf(values[0][0].toString());
        }
        return id;
    }

    /**
     * Obtiene el id de un índice
     *
     * @param path           path del índice
     * 
     * @return
     * @throws SQLException        si ocurre alguna SQLException durante la ejecución  de la operación
     */
    public int getIndexId(String path) throws SQLException {

        int id = 0;
        String[] fields = new String[1];
        fields[0] = "ID_INDEX";

        TableTracker table = PersistentOperations.load(connection, fields, "DRAKKARKEEL.INDEX", "URI", path);
        Object[][] values = table.getValues();

        if (values.length != 0) {
            id = Integer.valueOf(values[0][0].toString());
        }
        return id;
    }

    /**
     * Obtiene una lista de las direcciones correspondientes a los índices creados
     * para un motor determinado
     *
     * @param idSE          id del motor de búsqueda
     * 
     * @return
     * @throws SQLException        si ocurre alguna SQLException durante la ejecución  de la operación
     */
    public List<String> getIndexList(int idSE) throws SQLException {
        List<String> indexList = new ArrayList<>();

        String[] fields = new String[1];
        fields[0] = "URI";

        TableTracker table = PersistentOperations.load(connection, fields, "DRAKKARKEEL.INDEX", "ID_SE", idSE);

        Object[][] values = table.getValues();

        if (values.length != 0) {
            for (int i = 0; i < values.length; i++) {
                indexList.add(values[i][0].toString());
            }

        }

        return indexList;
    }

    /**
     * Establece la relación de un índice con sus colecciones
     */
    private void insertIndexCollection(List<String> indexUri, String collection, String user, String password, String repositoryName) throws SQLException {

        int idCollection = this.getCollectionId(collection);
        String[] fields = new String[5];
        fields[0] = "ID_INDEX";
        fields[1] = "ID_COLLECTION";
        fields[2] = "USER_CVS";
        fields[3] = "PASSWORD_CVS";
        fields[4] = "REPOSITORY_NAME";

        Object[] oneValue = new Object[5];

        for (int i = 0; i < indexUri.size(); i++) {
            String string = indexUri.get(i);
            int idIndex = this.getIndexId(string);

            if (!util.relationExist("DRAKKARKEEL.INDEX_COLLECTION", "ID_INDEX", idIndex, "ID_COLLECTION", idCollection)) {
                oneValue[0] = idIndex;
                oneValue[1] = idCollection;
                oneValue[2] = user;
                oneValue[3] = password;
                oneValue[4] = repositoryName;
                PersistentOperations.insert(connection, "DRAKKARKEEL.INDEX_COLLECTION", fields, oneValue);
            }
        }
    }

    /**
     * Devuelve todos los datos relacionados a un repositorio SVN
     *
     * @param repoName       nombre del repoositorio
     * @param idSE           id del motor de búsqueda
     * 
     * @return
     * @throws SQLException        si ocurre alguna SQLException durante la ejecución  de la operación
     */
    public SVNData getSVNRepositoryData(String repoName, int idSE) throws SQLException {
        SVNData data = new SVNData();
        data.setMergeFactor("1000");

        String[] fields = new String[4];
        fields[0] = "ID_INDEX";
        fields[1] = "ID_COLLECTION";
        fields[2] = "USER_CVS";
        fields[3] = "PASSWORD_CVS";

        int idIndex = 0, idCollection = 0;
        String collectionPath = null, user = null, password = null, indexPath = null;

        TableTracker table = PersistentOperations.load(connection, fields, "DRAKKARKEEL.INDEX_COLLECTION", "REPOSITORY_NAME", repoName);

        Object[][] values = table.getValues();

        if (values.length != 0) {
            idIndex = Integer.valueOf(values[0][0].toString());
            idCollection = Integer.valueOf(values[0][1].toString());
            user = values[0][2].toString();
            password = values[0][3].toString();

        }

        String[] fields1 = new String[2];
        fields1[0] = "COLLECTION_PATH";
        fields1[1] = "DIRECT_PATH";
        boolean direct = false;

        TableTracker table1 = PersistentOperations.load(connection, fields1, "DRAKKARKEEL.COLLECTION", "ID_COLLECTION", idCollection);
        Object[][] values1 = table1.getValues();

        if (values1.length != 0) {
            collectionPath = values1[0][0].toString();
            direct = Boolean.valueOf(values1[0][1].toString());
        }

        String[] fields2 = new String[1];
        fields2[0] = "URI";
        String[] wherefields = new String[2];
        wherefields[0] = "ID_INDEX";
        wherefields[1] = "ID_SE";

        Object[] wherevalues = new Object[2];
        wherevalues[0] = idIndex;
        wherevalues[1] = idSE;

        TableTracker table2 = PersistentOperations.load(connection, fields2, "DRAKKARKEEL.INDEX", wherefields, wherevalues);
        Object[][] values2 = table2.getValues();

        if (values2.length != 0) {
            indexPath = values2[0][0].toString();

        }
        data.setUser(user);
        data.setPassword(password);
        data.setUrl(collectionPath);
        if (direct) {
            data.setDirectPath(collectionPath);
            data.setNames("");
        } else {
            data.setDirectPath("");
            data.setNames(repoName);
        }
        data.setIndexPath(indexPath);

        return data;
    }

    /**
     * Devuelve los nombres de todos los repositorios indexados
     *
     * @return
     * @throws SQLException        si ocurre alguna SQLException durante la ejecución  de la operación
     */
    public List<String> getAllSVNRepositories() throws SQLException {
        List<String> list = new ArrayList<>();

        String[] fields = new String[1];
        fields[0] = "REPOSITORY_NAME";

        TableTracker table = PersistentOperations.load(connection, fields, "DRAKKARKEEL.INDEX_COLLECTION");

        Object[][] values = table.getValues();


        for (int i = 0; i < values.length; i++) {
            list.add(values[i][0].toString());
        }
        return list;
    }
}
