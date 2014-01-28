/*
 * DrakkarKeel - An Enterprise Collaborative Search Platform
 *
 * The contents of this file are subject under the terms described in the
 * DRAKKARKEEL_LICENSE file included in this distribution; you may not use this
 * file except in compliance with the License. 
 *
 * 2013-2014 DrakkarKeel Platform.
 */
package drakkar.stern.tracker.persistent.tables;

import java.sql.*;
import java.util.*;

/**
 * Clase que contiene las operaciones para trabajar con la BD
 */
public final class PersistentOperations {

    /** */
    public PersistentOperations() {
    }

    /**
     * Inserta un solo valor en la tabla, de acuerdo a la cantidad de campos
     *
     * @param connection
     * @param tableName
     * @param fields
     * @param values
     * 
     * @return
     *
     * @throws SQLException        si ocurre alguna SQLException durante la ejecución  de la operación        si ocurre alguna SQLException durante la ejecución  de la operación
     */
    public static int insert(DerbyConnection connection, String tableName, String[] fields, Object[] values) throws SQLException {
        int sizeFields = fields.length;

        if (sizeFields == 0) {
            return 0;
        }

        StringBuilder query = new StringBuilder(60);
        query.append("INSERT INTO " + tableName + " (");

        for (int i = 0; i < sizeFields; i++) {
            query.append(fields[i]);
            if (i < sizeFields - 1) {
                query.append(",");
            }
        }
        query.append(") VALUES (");
        for (int i = 0; i < sizeFields; i++) {
            query.append("?");
            if (i < sizeFields - 1) {
                query.append(",");
            }
        }
        query.append(")");
        int result;
        try (PreparedStatement statement = connection.connection.prepareStatement(query.toString())) {
            for (int i = 0; i < sizeFields; i++) {
                statement.setObject(i + 1, values[i]);
            }
            result = statement.executeUpdate();
        }

        return result;
    }

    /**
     * Inserta varios valores en una tabla
     *
     * @param connection 
     * @param tableName 
     * @param fields 
     * @param values 
     * @return 
     * @throws SQLException        si ocurre alguna SQLException durante la ejecución  de la operación        si ocurre alguna SQLException durante la ejecución  de la operación
     */
    public static int insert(DerbyConnection connection, String tableName, String[] fields, Object[][] values) throws SQLException {

        if (fields.length == 0) {
            return 0;
        }

        int result = 0;
        for (int i = 0; i < values.length; i++) {
            result += insert(connection, tableName, fields, values[i]);
        }

        return result;
    }

    /**
     * Actualiza valores en una tabla
     * 
     * @param connection 
     * @param tableName 
     * @param fields 
     * @param values 
     * @param field
     * @param value
     * @return 
     * @throws SQLException        si ocurre alguna SQLException durante la ejecución  de la operación        si ocurre alguna SQLException durante la ejecución  de la operación
     */
    public static int update(DerbyConnection connection, String tableName, String[] fields, Object[] values, String field, Object value) throws SQLException {
        int sizeFields = fields.length;
        if (sizeFields == 0) {
            return 0;
        }

        StringBuilder query = new StringBuilder(50);
        query.append("UPDATE ").append(tableName).append(" SET ");
        for (int i = 0; i < sizeFields; i++) {
            query.append(fields[i] + "=?");
            if (i < sizeFields - 1) {
                query.append(", ");
            }
        }
        query.append(" WHERE " + field + " = ?");
        int result;
        try (PreparedStatement statement = connection.connection.prepareStatement(query.toString())) {
            for (int i = 0; i < sizeFields; i++) {
                statement.setObject(i + 1, values[i]);
            }
            statement.setObject(sizeFields + 1, value);
            result = statement.executeUpdate();
        }

        return result;
    }

    /**
     * Actualiza valores en una tabla
     * 
     * @param connection 
     * @param tableName 
     * @param fields 
     * @param values 
     * @param wherefields
     * @param wherevalues 
     * @return 
     * @throws SQLException        si ocurre alguna SQLException durante la ejecución  de la operación        si ocurre alguna SQLException durante la ejecución  de la operación
     */
    public static int update(DerbyConnection connection, String tableName, String[] fields, Object[] values, String[] wherefields, Object[] wherevalues) throws SQLException {
        int sizeFields = fields.length;
        if (sizeFields == 0) {
            return 0;
        }

        StringBuilder query = new StringBuilder(50);
        query.append("UPDATE " + tableName + " SET ");

        for (int i = 0; i < sizeFields; i++) {
            query.append(fields[i] + "=?");
            if (i < sizeFields - 1) {
                query.append(", ");
            }
        }
        String temp = (wherefields.length > 0) ? " WHERE " : "";
        query.append(temp);
        for (int i = 0; i < wherefields.length; i++) {
            query.append(wherefields[i] + "=?");
            if (i < wherefields.length - 1) {
                query.append(" AND ");
            }
        }
        int result;
        try (PreparedStatement statement = connection.connection.prepareStatement(query.toString())) {
            int i;
            for (i = 0; i < sizeFields; i++) {
                statement.setObject(i + 1, values[i]);
            }
            for (int k = 0; k < wherefields.length; k++) {
                statement.setObject(k + i + 1, wherevalues[k]);
            }
            result = statement.executeUpdate();
        }

        return result;
    }

    /**
     * Elimina un valor de una tabla
     * 
     * @param connection  
     * @param tableName 
     * @param field
     * @param value
     *
     * @return
     *
     * @throws SQLException        si ocurre alguna SQLException durante la ejecución  de la operación        si ocurre alguna SQLException durante la ejecución  de la operación
     */
    public static int delete(DerbyConnection connection, String tableName, String field, Object value) throws SQLException {
        int result;
        try (PreparedStatement statement = connection.connection.prepareStatement("DELETE FROM " + tableName + " WHERE " + field + " = ?")) {
            statement.setObject(1, value);
            result = statement.executeUpdate();
        }

        return result;
    }

    /**
     * Elimina un valor de una tabla
     *
     * @param connection 
     * @param tableName 
     * @param field
     * @param values
     * 
     * @return 
     * @throws SQLException        si ocurre alguna SQLException durante la ejecución  de la operación        si ocurre alguna SQLException durante la ejecución  de la operación
     */
    public static int delete(DerbyConnection connection, String tableName, String field, Object[] values) throws SQLException {
        int result;
        try (PreparedStatement statement = connection.connection.prepareStatement("DELETE FROM " + tableName + " WHERE " + field + " = ?")) {
            result = 0;
            for (int i = 0; i < values.length; i++) {
                statement.setObject(1, values[i]);
                result += statement.executeUpdate();
            }
        }

        return result;
    }

    /**
     * Elimina un valor de una tabla
     *
     * @param connection 
     * @throws SQLException        si ocurre alguna SQLException durante la ejecución  de la operación        si ocurre alguna SQLException durante la ejecución  de la operación
     * @param tableName 
     * @param fields 
     * @param values 
     * @return 
     */
    public static int delete(DerbyConnection connection, String tableName, String[] fields, Object[] values) throws SQLException {
        StringBuilder query = new StringBuilder(50);
        query.append("DELETE FROM " + tableName + " WHERE ");
        for (int i = 0; i < fields.length; i++) {
            query.append(fields[i] + "=?");
            if (i < fields.length - 1) {
                query.append(" AND ");
            }
        }
        int result;
        try (PreparedStatement statement = connection.connection.prepareStatement(query.toString())) {
            for (int i = 0; i < values.length; i++) {
                statement.setObject(i + 1, values[i]);
            }
            result = statement.executeUpdate();
        }

        return result;
    }

    /**
     * Elimina todos los datos de una tabla
     * 
     * @param connection 
     * @param tableName
     *
     * @return
     *
     * @throws SQLException        si ocurre alguna SQLException durante la ejecución  de la operación        si ocurre alguna SQLException durante la ejecución  de la operación
     */
    public static int deleteAll(DerbyConnection connection, String tableName) throws SQLException {
        int result;
        try (PreparedStatement statement = connection.connection.prepareStatement("DELETE FROM " + tableName)) {
            result = statement.executeUpdate();
        }

        return result;
    }

    /**
     * Carga todos los datos de una tabla
     *
     * @param connection 
     * @param tableName
     *
     * @return
     * 
     * @throws SQLException        si ocurre alguna SQLException durante la ejecución  de la operación        si ocurre alguna SQLException durante la ejecución  de la operación
     */
    public static TableTracker load(DerbyConnection connection, String tableName) throws SQLException {
        TableTracker result;
        try (PreparedStatement statement = connection.connection.prepareStatement("SELECT * FROM " + tableName)) {
            ResultSet rs = statement.executeQuery();
            result = new TableTracker(rs);
        }

        return result;
    }

    /**
     * Carga campos especificados de una tabla
     *
     * @param connection 
     * @param fields 
     * @param tableName
     *
     * @return
     * 
     * @throws SQLException        si ocurre alguna SQLException durante la ejecución  de la operación        si ocurre alguna SQLException durante la ejecución  de la operación
     */
    public static TableTracker load(DerbyConnection connection, String[] fields, String tableName) throws SQLException {
        int sizeFields = fields.length;
        StringBuilder query = new StringBuilder(50);
        query.append("SELECT ");

        if (sizeFields == 0) {
            query.append("* ");
        } else {
            for (int i = 0; i < sizeFields; i++) {
                query.append(fields[i]);
                if (i < sizeFields - 1) {
                    query.append(",");
                }
            }
        }
        query.append(" FROM " + tableName);
        TableTracker result;
        try (java.sql.PreparedStatement statement = connection.connection.prepareStatement(query.toString())) {
            java.sql.ResultSet rs = statement.executeQuery();
            result = new TableTracker(rs);
        }
        return result;

    }

    /**
     * Carga datos de una tabla dado un campo especificado
     *
     * @param connection 
     * @param fields 
     * @param tableName 
     * @param filterField 
     * @param filterValue
     *
     * @return
     * 
     * @throws SQLException        si ocurre alguna SQLException durante la ejecución  de la operación        si ocurre alguna SQLException durante la ejecución  de la operación
     */
    public static TableTracker load(DerbyConnection connection, String[] fields, String tableName, String filterField, Object filterValue) throws SQLException {
        int sizeFields = fields.length;
        StringBuilder query = new StringBuilder(50);
        query.append("SELECT ");

        if (sizeFields == 0) {
            query.append("* ");
        } else {
            for (int i = 0; i < sizeFields; i++) {
                query.append(fields[i]);
                if (i < sizeFields - 1) {
                    query.append(",");
                }
            }
        }
        query.append(" FROM " + tableName + " WHERE " + filterField + " = ?");
        TableTracker result;
        try (PreparedStatement statement = connection.connection.prepareStatement(query.toString())) {
            statement.setObject(1, filterValue);
            ResultSet rs = statement.executeQuery();
            result = new TableTracker(rs);
        }
        return result;
    }

    /**
     * Carga datos de una tabla dado un campo y un operador especificado
     *
     * @param connection
     * @param fields
     * @param tableName
     * @param filterField
     * @param filterValue
     * @param filterOperator
     *
     * @return
     * 
     * @throws SQLException        si ocurre alguna SQLException durante la ejecución  de la operación
     */
    public static TableTracker load(DerbyConnection connection, String[] fields, String tableName, String filterField, Object filterValue, String filterOperator) throws SQLException {
        int sizeFields = fields.length;
        StringBuilder query = new StringBuilder(50);
        query.append("SELECT ");

        if (sizeFields == 0) {
            query.append("* ");
        } else {
            for (int i = 0; i < sizeFields; i++) {
                query.append(fields[i]);
                if (i < sizeFields - 1) {
                    query.append(",");
                }
            }
        }
        query.append(" FROM " + tableName + " WHERE " + filterField + filterOperator + " ?");
        TableTracker result;
        try (PreparedStatement statement = connection.connection.prepareStatement(query.toString())) {
            statement.setObject(1, filterValue);
            ResultSet rs = statement.executeQuery();
            result = new TableTracker(rs);
        }
        return result;
    }

    /**
     * Carga datos de dos tablas
     *
     * @param connection 
     * @param fields 
     * @param tableA 
     * @param tableB 
     * @param joinFieldA 
     * @param joinFieldB
     *
     * @return
     * 
     * @throws SQLException        si ocurre alguna SQLException durante la ejecución  de la operación        si ocurre alguna SQLException durante la ejecución  de la operación
     */
    public static TableTracker load(DerbyConnection connection, String[] fields, String tableA, String tableB, String joinFieldA, String joinFieldB) throws SQLException {

        int sizeFields = fields.length;
        StringBuilder query = new StringBuilder(50);
        query.append("SELECT ");
        if (sizeFields == 0) {
            query.append("* ");
        } else {
            for (int i = 0; i < sizeFields; i++) {
                query.append(fields[i]);
                if (i < sizeFields - 1) {
                    query.append(",");
                }
            }
        }
        query.append(" FROM " + tableA + " INNER JOIN " + tableB + " ON " + tableA + "." + joinFieldA + " = " + tableB + "." + joinFieldB);
        TableTracker result;
        try (PreparedStatement statement = connection.connection.prepareStatement(query.toString())) {
            ResultSet rs = statement.executeQuery();
            result = new TableTracker(rs);
        }
        return result;
    }

    /**
     * Selecciona los campos de una tabla especificando los valores de varios campos
     *
     * @param connection
     * @param fields
     * @param tableName
     * @param whereFields
     * @param whereValues
     *
     * @return
     * @throws SQLException        si ocurre alguna SQLException durante la ejecución  de la operación
     */
    public static TableTracker load(DerbyConnection connection, String[] fields, String tableName, String[] whereFields, Object[] whereValues) throws SQLException {
        int sizeFields = fields.length;
        StringBuilder query = new StringBuilder(50);
        query.append("SELECT ");

        if (sizeFields == 0) {
            query.append("* ");
        } else {
            for (int i = 0; i < sizeFields; i++) {
                query.append(fields[i]);
                if (i < sizeFields - 1) {
                    query.append(",");
                }
            }
        }
        query.append(" FROM " + tableName + " WHERE ");

        for (int i = 0; i < whereFields.length; i++) {
            String field = whereFields[i];
            query.append(field + " = ?");

            if (i != whereFields.length - 1) {
                query.append(" AND ");
            }

        }
        TableTracker result;
        try (PreparedStatement statement = connection.connection.prepareStatement(query.toString())) {
            for (int j = 0; j < whereValues.length; j++) {
                Object object = whereValues[j];
                statement.setObject(j + 1, object);
            }
            ResultSet rs = statement.executeQuery();
            result = new TableTracker(rs);
        }
        return result;
    }

    /**
     * Obtiene valores de una tabla que se relaciona con otra a través de un campo,
     * y se pasa un parámetro como condición
     *
     * @param connection
     * @param fields             -- campos a devolver
     * @param tableA
     * @param tableB
     * @param joinFieldA         -- campo coincidente de una tabla
     * @param joinFieldB         -- campo coincidente de otra tabla     
     * @param filterField
     * @param filterValue
     *
     * @return
     * @throws SQLException        si ocurre alguna SQLException durante la ejecución  de la operación
     */
    public static TableTracker load(DerbyConnection connection, String[] fields, String tableA, String tableB, String joinFieldA, String joinFieldB, String filterField, Object filterValue) throws SQLException {

        int sizeFields = fields.length;
        StringBuilder query = new StringBuilder(50);
        query.append("SELECT ");
        if (sizeFields == 0) {
            query.append("* ");
        } else {
            for (int i = 0; i < sizeFields; i++) {
                query.append(fields[i]);
                if (i < sizeFields - 1) {
                    query.append(",");
                }
            }
        }
        query.append(" FROM " + tableA + " INNER JOIN " + tableB + " ON " + tableA + "." + joinFieldA + " = " + tableB + "." + joinFieldB + " WHERE " + filterField + " =? ");
        TableTracker result;
        try (PreparedStatement statement = connection.connection.prepareStatement(query.toString())) {
            statement.setObject(1, filterValue);
            ResultSet rs = statement.executeQuery();
            result = new TableTracker(rs);
        }
        return result;
    }

    /**
     * @param tableA 
     * @param tableB 
     * @param joinFieldA 
     * @param joinFieldB 
     * @return 
     * @throws SQLException        si ocurre alguna SQLException durante la ejecución  de la operación        si ocurre alguna SQLException durante la ejecución  de la operación
     */
    public static TableTracker leftjoin(TableTracker tableA, TableTracker tableB, String joinFieldA, String joinFieldB) throws SQLException {
        if (tableA == null || tableB == null) {
            return null;
        }
        if (!tableA.getColumnNames().contains(joinFieldA) || !tableB.getColumnNames().contains(joinFieldB)) {
            return null;
        }

        TableTracker result = new TableTracker();
        ArrayList<ColumnTable> columns = tableA.getColumns();
        ArrayList<String> colNames = tableA.getColumnNames();
        for (ColumnTable col : tableB.getColumns()) {
            if (!colNames.contains(col.getName())) {
                columns.add(col);
            }
        }

        result.setColumns(columns);

        for (RowTable rowA : tableA.getRows()) {
            RowTable newRow = new RowTable(rowA);
            Object joinValueA = rowA.getValue(joinFieldA);

            for (RowTable rowB : tableB.getRows()) {
                if (rowB.getValue(joinFieldB).equals(joinValueA)) {
                    Object[] cols = rowB.getColumnNames().toArray();
                    for (int i = 0; i < cols.length; i++) {
                        if (!newRow.getColumnNames().contains(cols[i].toString())) {
                            newRow.setValue(cols[i].toString(), rowB.getValue(i));
                        }
                    }
                    break;
                }
            }
            result.insertRow(newRow);
        }

        return result;
    }

    /**
     *
     * Obtiene el valor máximo de los registros de un campo
     *
     * @param connection
     * @param table     nombre de la tabla
     * @param field
     *
     * @return
     * 
     * @throws SQLException        si ocurre alguna SQLException durante la ejecución  de la operación
     */
    public static TableTracker countMaxRegister(DerbyConnection connection, String table, String field) throws SQLException {

        StringBuilder query = new StringBuilder(50);
        query.append("SELECT MAX(" + field + ") FROM " + table);
        TableTracker result;
        try (PreparedStatement statement = connection.connection.prepareStatement(query.toString())) {
            ResultSet rs = statement.executeQuery();
            result = new TableTracker(rs);
        }

        return result;
    }

    /**
     * Carga solo una ocurrencia de los valores almacenados en un campo determinado
     * en caso de que estos valores estén repetidos
     *
     * @param connection
     * @param table1
     * @param table2
     * @param fieldToReturn 
     * @param joinField
     * @param filterField
     * @param filterValue
     *
     * @return
     * 
     * @throws SQLException        si ocurre alguna SQLException durante la ejecución  de la operación
     */
    public static TableTracker loadOneOcurrence(DerbyConnection connection, String table1, String table2, String fieldToReturn, String joinField, String filterField, Object filterValue) throws SQLException {

        StringBuilder query = new StringBuilder(50);
        query.append("SELECT DISTINCT " + fieldToReturn + " FROM " + table1 + " INNER JOIN " + table2 + " ON " + table1 + "." + joinField + " = " + table2 + "." + joinField + " WHERE " + filterField + " =?");
        TableTracker result;
        try (PreparedStatement statement = connection.connection.prepareStatement(query.toString())) {
            statement.setObject(1, filterValue);
            ResultSet rs = statement.executeQuery();
            result = new TableTracker(rs);
        }
        return result;


    }

    /**
     * Carga solo una ocurrencia de los valores almacenados en un campo determinado
     * en caso de que estos valores estén repetidos
     *
     * @param connection
     * @param table1
     * @param table2
     * @param fieldToReturn
     * @param joinField
     * @param whereFields
     * @param whereValues
     * @return
     * @throws SQLException        si ocurre alguna SQLException durante la ejecución  de la operación
     */
    public static TableTracker loadOneOcurrence(DerbyConnection connection, String table1, String table2, String fieldToReturn, String joinField, String[] whereFields, Object[] whereValues) throws SQLException {

        StringBuilder query = new StringBuilder(50);
        query.append("SELECT DISTINCT " + fieldToReturn + " FROM " + table1 + " INNER JOIN " + table2 + " ON " + table1 + "." + joinField + " = " + table2 + "." + joinField + " WHERE ");

        for (int i = 0; i < whereFields.length; i++) {
            String field = whereFields[i];
            query.append(field + " = ?");

            if (i != whereFields.length - 1) {
                query.append(" AND ");
            }

        }
        TableTracker result;
        try (PreparedStatement statement = connection.connection.prepareStatement(query.toString())) {
            for (int i = 0; i < whereValues.length; i++) {
                Object object = whereValues[i];
                statement.setObject(i + 1, object);
            }
            ResultSet rs = statement.executeQuery();
            result = new TableTracker(rs);
        }
        return result;


    }

    /**
     * Carga datos de dos tablas especificando valores de campos
     *
     * @param connection
     * @param fields
     * @param tableA
     * @param tableB
     * @param joinFieldA
     * @param joinFieldB
     * @param filterField1
     * @param filterValue1
     * @param filterOperator   para establecer los operadores de comparación
     * @param whereFields 
     * @param whereValues
     *
     * @return
     *
     * @throws SQLException        si ocurre alguna SQLException durante la ejecución  de la operación
     */
    public static TableTracker load(DerbyConnection connection, String[] fields, String tableA, String tableB, String joinFieldA, String joinFieldB, String filterField1, String filterValue1, String filterOperator, String[] whereFields, Object[] whereValues) throws SQLException {
        int sizeFields = fields.length;
        StringBuilder query = new StringBuilder(50);
        query.append("SELECT ");
        if (sizeFields == 0) {
            query.append("* ");
        } else {
            for (int i = 0; i < sizeFields; i++) {
                query.append(fields[i]);
                if (i < sizeFields - 1) {
                    query.append(",");
                }
            }
        }
        query.append(" FROM " + tableA + " INNER JOIN " + tableB + " ON " + tableA + "." + joinFieldA + " = " + tableB + "." + joinFieldB + " WHERE " + filterField1 + filterOperator + filterValue1 + " AND ");


        for (int i = 0; i < whereFields.length; i++) {
            String field = whereFields[i];
            query.append(field + " = ?");

            if (i != whereFields.length - 1) {
                query.append(" AND ");
            }

        }
        TableTracker result;
        try (PreparedStatement statement = connection.connection.prepareStatement(query.toString())) {
            for (int i = 0; i < whereValues.length; i++) {
                Object object = whereValues[i];
                statement.setObject(i + 1, object);
            }
            ResultSet rs = statement.executeQuery();
            result = new TableTracker(rs);
        }
        return result;


    }

    /**
     * Carga datos utilizando Inner join en tres tablas
     *
     * @param connection
     * @param fields
     * @param tableA
     * @param tableB
     * @param tableC
     * @param joinFieldA
     * @param joinFieldB
     * @param joinFieldC
     * @param whereFields
     * @param whereValues
     *
     * @return
     * 
     * @throws SQLException        si ocurre alguna SQLException durante la ejecución  de la operación
     */
    public static TableTracker load(DerbyConnection connection, String[] fields, String tableA, String tableB, String tableC, String joinFieldA, String joinFieldB, String joinFieldC, String[] whereFields, Object[] whereValues) throws SQLException {

        int sizeFields = fields.length;
        StringBuilder query = new StringBuilder(50);
        query.append("SELECT ");
        if (sizeFields == 0) {
            query.append("* ");
        } else {
            for (int i = 0; i < sizeFields; i++) {
                query.append(fields[i]);
                if (i < sizeFields - 1) {
                    query.append(",");
                }
            }
        }
        query.append(" FROM " + "( SELECT " + tableA + "." + joinFieldA + " FROM " + tableA + " INNER JOIN " + tableB + " ON " + tableA + "." + joinFieldA + " = " + tableB + "." + joinFieldB + " WHERE ");

        for (int i = 0; i < whereFields.length; i++) {
            String field = whereFields[i];
            query.append(field + " = ?");

            if (i != whereFields.length - 1) {
                query.append(" AND ");
            }

        }

        query.append(" ) AS T1" + " INNER JOIN " + tableC + " ON " + "DRAKKARKEEL.T1." + joinFieldC + " = " + tableC + "." + joinFieldC);
        TableTracker result;
        try (PreparedStatement statement = connection.connection.prepareStatement(query.toString())) {
            for (int i = 0; i < whereValues.length; i++) {
                Object object = whereValues[i];
                statement.setObject(i + 1, object);
            }
            ResultSet rs = statement.executeQuery();
            result = new TableTracker(rs);
        }
        return result;


    }

    /**
     * Carga datos a partir de tres tablas
     * 
     * @param connection
     * @param fields
     * @param tableA
     * @param tableB
     * @param tableC
     * @param joinFieldA
     * @param joinFieldB
     * @param joinFieldC
     * @param whereFields
     * @param whereValues
     *
     * @return
     *
     * @throws SQLException        si ocurre alguna SQLException durante la ejecución  de la operación
     */
    public static TableTracker loadResults(DerbyConnection connection, String[] fields, String tableA, String tableB, String tableC, String joinFieldA, String joinFieldB, String joinFieldC, String[] whereFields, Object[] whereValues) throws SQLException {

        int sizeFields = fields.length;
        StringBuilder query = new StringBuilder(50);
        query.append("SELECT ");
        if (sizeFields == 0) {
            query.append("* ");
        } else {
            for (int i = 0; i < sizeFields; i++) {
                query.append(fields[i]);
                if (i < sizeFields - 1) {
                    query.append(",");
                }
            }
        }
        query.append(" FROM " + "( SELECT " + tableA + "." + joinFieldC + " FROM " + tableA + " INNER JOIN " + tableB + " ON " + tableA + "." + joinFieldA + " = " + tableB + "." + joinFieldB + " WHERE ");

        for (int i = 0; i < whereFields.length; i++) {
            String field = whereFields[i];
            query.append(field + " = ?");

            if (i != whereFields.length - 1) {
                query.append(" AND ");
            }

        }

        query.append(" ) AS T1" + " INNER JOIN " + tableC + " ON " + "DRAKKARKEEL.T1." + joinFieldC + " = " + tableC + "." + joinFieldC);
        TableTracker result;
        try (PreparedStatement statement = connection.connection.prepareStatement(query.toString())) {
            for (int i = 0; i < whereValues.length; i++) {
                Object object = whereValues[i];
                statement.setObject(i + 1, object);
            }
            ResultSet rs = statement.executeQuery();
            result = new TableTracker(rs);
        }
        return result;


    }

    /**
     * Carga datos de dos tablas especificando valores de campos
     * 
     * @param connection
     * @param fields
     * @param tableA
     * @param tableB
     * @param joinFieldA
     * @param joinFieldB
     * @param whereFields
     * @param whereValues
     * 
     * @return
     *
     * @throws SQLException        si ocurre alguna SQLException durante la ejecución  de la operación
     */
    public static TableTracker load(DerbyConnection connection, String[] fields, String tableA, String tableB, String joinFieldA, String joinFieldB, String[] whereFields, Object[] whereValues) throws SQLException {

        int sizeFields = fields.length;
        StringBuilder query = new StringBuilder(50);
        query.append("SELECT ");
        if (sizeFields == 0) {
            query.append("* ");
        } else {
            for (int i = 0; i < sizeFields; i++) {
                query.append(fields[i]);
                if (i < sizeFields - 1) {
                    query.append(",");
                }
            }
        }
        query.append(" FROM " + tableA + " INNER JOIN " + tableB + " ON " + tableA + "." + joinFieldA + " = " + tableB + "." + joinFieldB + " WHERE ");

        for (int i = 0; i < whereFields.length; i++) {
            String field = whereFields[i];
            query.append(field + " = ?");

            if (i != whereFields.length - 1) {
                query.append(" AND ");
            }

        }
        TableTracker result;
        try (PreparedStatement statement = connection.connection.prepareStatement(query.toString())) {
            for (int i = 0; i < whereValues.length; i++) {
                Object object = whereValues[i];
                statement.setObject(i + 1, object);
            }
            ResultSet rs = statement.executeQuery();
            result = new TableTracker(rs);
        }
        return result;

    }
}
