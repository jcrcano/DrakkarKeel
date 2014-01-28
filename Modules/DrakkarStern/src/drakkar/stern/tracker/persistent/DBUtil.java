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
import drakkar.stern.tracker.persistent.tables.PersistentOperations;
import drakkar.stern.tracker.persistent.tables.TableTracker;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * Esta clase contiene métodos utilitarios para administrar los datos de la BD
 */
public class DBUtil {

    Object[][] values;
    Object[] oneValue;
    DerbyConnection connection;

    /**
     * constructor
     *
     * @param derby
     */
    public DBUtil(DerbyConnection derby) {
        connection = derby;
    
    }

    /**
     * Verifica que un valor exista ya en una tabla
     *
     * @param object          valor para verificar
     * @param tableName       nombre de la tabla
     * @param fieldName       nombre del campo correspondiente al valor
     * @param fieldToReturn   campo a devolver
     *
     * @return
     *
     * @throws SQLException        si ocurre alguna SQLException durante la ejecución  de la operación
     */
    public boolean alreadyExist(Object object, String tableName, String fieldName, String fieldToReturn) throws SQLException {

        String[] fields = new String[1];
        fields[0] = fieldToReturn;

        TableTracker table = PersistentOperations.load(connection, fields, tableName, fieldName, object);

        values = table.getValues();

        if (values.length != 0) {
            return true;
        }

        return false;
    }

    /**
     *
     * Verifica que exista un valor en una tabla dado varios campos
     *
     * @param tableName       nombre de la tabla
     * @param whereFields     campos
     * @param whereValues     valores
     * @param fieldToReturn   campo a devolver
     * 
     * @return
     * @throws SQLException        si ocurre alguna SQLException durante la ejecución  de la operación
     */
    public boolean alreadyExist(String tableName, String[] whereFields, Object[] whereValues, String fieldToReturn) throws SQLException {

        String[] fields = new String[1];
        fields[0] = fieldToReturn;

        TableTracker table = PersistentOperations.load(connection, fields, tableName,whereFields,whereValues);

        values = table.getValues();

        if (values.length != 0) {
            return true;
        }

        return false;
    }

    /**
     *
     * Verifica si en una tabla existen dos campos relacionados dados sus valores
     *
     * @param tableName           nombre de la tabla
     * @param fieldName1          campo1
     * @param object1             valor1
     * @param fieldName2          campo2
     * @param object2             valor2
     * 
     * @return
     * @throws SQLException        si ocurre alguna SQLException durante la ejecución  de la operación
     */
    public boolean relationExist(String tableName, String fieldName1, Object object1, String fieldName2, Object object2) throws SQLException {

        String[] fields = new String[1];
        fields[0] = fieldName2;

        ArrayList<Object> list = new ArrayList<>();

       TableTracker table = PersistentOperations.load(connection, fields, tableName,fieldName1,object1);

        values = table.getValues();

        if (values.length != 0) {
            for (int i = 0; i < values.length; i++) {
                list.add(values[i][0]);
            }

        }

        if(!list.isEmpty() && list.contains(object2)){
            return true;
        }

        return false;
    }

    /**
     * Verifica si una tabla está vacía
     *
     * @param tableName     nombre de la tabla
     * 
     * @return
     * @throws SQLException        si ocurre alguna SQLException durante la ejecución  de la operación
     */
    public boolean isEmpty(String tableName) throws SQLException {

        TableTracker table = PersistentOperations.load(connection, tableName);

        if (table.getValues().length == 0) {
            return true;
        }


        return false;
    }

    /**
     * Para obtener java.sql.Date a partir de java.util.Date
     * 
     * @param today  fecha actual
     * 
     * @return
     */
    public static java.sql.Date getJavaSqlDate(java.util.Date today) {
        return new java.sql.Date(today.getTime());
    }

     /**
     * Obtiene la fecha actual
     *
     * @return
     */
    public static Date getCurrentDate(){
        java.util.Date today = new java.util.Date();
        return new java.sql.Date(today.getTime());
    }

    /**
     * Obtiene la fecha actual
     *
     * @return
     */
    public static Timestamp getCurrentTimeStamp(){
        java.util.Date today = new java.util.Date();
        return new java.sql.Timestamp(today.getTime());
    }
}
