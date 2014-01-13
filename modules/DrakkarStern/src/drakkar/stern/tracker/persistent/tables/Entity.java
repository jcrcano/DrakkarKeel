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
 * Clase que representa una entidad
 */
public class Entity{
    /** */
    protected String name;
    
    /** */
    protected ArrayList<TableTracker> tables;
    
    /** */
    protected DerbyConnection connection;
    
    /**
     * @param c
     * @param name
     */
    public Entity(DerbyConnection c, String name) {
        this.name = name;
        this.connection = c;
        tables = new ArrayList<>();
    }

    /**
     * 
     * @return 
     */
    public String getName() {
        return name;
    }

    /**
     * 
     * @return 
     */
    public ArrayList<TableTracker> getTables() {
        return tables;
    }

    /**
     * 
     * @return 
     */
    public DerbyConnection getConnection() {
        return connection;
    }
        
    
    /**
     * @return
     */
    public int getTableCount(){
        return (tables != null)?tables.size():-1;
    }
    
    /**
     * @param index
     * @return
     */
    public TableTracker getTable(int index){
        if (tables == null) return null;
        return (index < 0 || index >= tables.size())?null:tables.get(index);
    }

    /**
     * 
     * @param name 
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 
     * @param tables 
     */
    public void setTables(ArrayList<TableTracker> tables) {
        this.tables = tables;
    }

    /**
     * 
     * @param connection 
     */
    public void setConnection(DerbyConnection connection) {
        this.connection = connection;
    }
    
    
    
    /**
     * @throws java.sql.SQLException
     */
    public void commit() throws SQLException {
        connection.commit();
    }
    
    /**
     * @throws java.sql.SQLException
     */
    public void rollback() throws SQLException {
        connection.rollback();
    }
    
    /**
     * @param rowIndex
     * @return
     */
    public boolean isDisposable(int rowIndex){
        return tables.get(0).isDisposable(rowIndex);
    }
    
    /**
     * @param table
     * @return
     */
    public int addTable(TableTracker table){
        tables.add(table);
        return tables.size()-1;
    }
    
    /**
     * @param index
     * @return
     */
    public boolean removeTable(int index){
        if (tables == null) return false;
        if (tables.size() < 2) return false;
        if (index < 1 || index >= tables.size()) return false;
        
        tables.remove(index);
        return true;
    }
    
    /**
     * @return
     */
    public ArrayList<Integer> getDisposableRows(){
        ArrayList<Integer> result = new ArrayList();
        int rowCount = tables.get(0).getRowCount();
        
        for (int i = 0; i < rowCount; i++)
            if (tables.get(0).isDisposable(i)) result.add(new Integer(i));
        
        return result;
    }
    
    /**
     * @param fields
     * @param values
     * @return
     * @throws java.sql.SQLException
     */
    public int insert(String[] fields, Object[][] values) throws SQLException {
        return this.insert(this.name, fields, values);
    }
    
    /**
     * @param fields
     * @param values
     * @return
     * @throws java.sql.SQLException
     */
    public int insert(String[] fields, Object[] values) throws SQLException {
        return this.insert(this.name, fields, values);
    }
    
    /**
     * @param fields
     * @param values
     * @param field
     * @param value
     * @return
     * @throws java.sql.SQLException
     */
    public int update(String[] fields, Object[] values, String field, Object value) throws SQLException {
        return update(this.name, fields, values, field, value);
    }
    
    /**
     * @param fields
     * @param values
     * @param wherefields
     * @param wherevalues
     * @return
     * @throws java.sql.SQLException
     */
    public int update(String[] fields, Object[] values, String[] wherefields, Object[] wherevalues) throws SQLException {
        return update(this.name, fields, values, wherefields, wherevalues);
    }
    
    /**
     * @param field
     * @param value
     * @return
     * @throws java.sql.SQLException
     */
    public int delete(String field, Object value) throws SQLException {
        return delete(this.name, field, value);
    }
    
    /**
     * @param field
     * @param values
     * @return
     * @throws java.sql.SQLException
     */
    public int delete(String field, Object[] values) throws SQLException {
        return delete(this.name, field, values);
    }
    
    /**
     * @param fields
     * @param values
     * @return
     * @throws java.sql.SQLException
     */
    public int delete(String[] fields, Object[] values) throws SQLException {
        return delete(this.name, fields, values);
    }
    
    /**
     * @return
     * @throws java.sql.SQLException
     */
    public int deleteAll() throws SQLException {
        return deleteAll(this.name);
    }
    
    /**
     *
     * @throws java.sql.SQLException 
     */
    public void load() throws SQLException {
        if (!this.tables.isEmpty()) this.tables.remove(0);
        this.tables.add(0,this.load(this.name));
        
    }
    
     
    /**
     * @param tableName
     * @return
     * @throws java.sql.SQLException
     */
    public TableTracker load(String tableName)  throws SQLException {
        return PersistentOperations.load(this.connection, tableName);
    }
    
    /**
     * @param fields
     * @param tableName
     * @return
     * @throws java.sql.SQLException
     */
    public TableTracker load(String[] fields, String tableName) throws SQLException {
        return PersistentOperations.load(this.connection, fields, tableName);
    }
    
    /**
     * @param fields
     * @param tableName
     * @param filterField
     * @param filterValue
     * @return
     * @throws java.sql.SQLException
     */
    public TableTracker load(String[] fields, String tableName, String filterField, Object filterValue) throws SQLException {
        return PersistentOperations.load(this.connection, fields, tableName, filterField, filterValue);
    }
    
    /**
     * @param fields
     * @param tableA
     * @param tableB
     * @param joinFieldA
     * @param joinFieldB
     * @return
     * @throws java.sql.SQLException
     */
    public TableTracker load(String[] fields, String tableA, String tableB, String joinFieldA, String joinFieldB) throws SQLException {
        return PersistentOperations.load(this.connection, fields, tableA, tableB, joinFieldA, joinFieldB);
    }
    
    
    /**
     * @param fields
     * @throws java.sql.SQLException
     */
    public void load(String[] fields) throws SQLException {
        if (!this.tables.isEmpty()) this.tables.remove(0);
        this.tables.add(0,this.load(fields, this.name));
    }
    
    /**
     * @param fields
     * @throws java.sql.SQLException
     * @param filterField
     * @param filterValue
     */
    public void load(String[] fields, String filterField, Object filterValue) throws SQLException {
        if (!this.tables.isEmpty()) this.tables.remove(0);
        this.tables.add(0,this.load(fields, this.name, filterField, filterValue));
    }
    
    /**
     * @param tableB
     * @throws java.sql.SQLException
     * @param joinFieldA
     * @param joinFieldB
     * @param fields
     */
    public void load(String tableB, String joinFieldA, String joinFieldB, String[] fields) throws SQLException {
        if (!this.tables.isEmpty()) this.tables.remove(0);
        this.tables.add(0,this.load(fields, this.name, tableB, joinFieldA, joinFieldB));
    }
    
    /**
     * @param tableName
     * @param fields
     * @param values
     * @return
     * @throws java.sql.SQLException
     */
    public int insert(String tableName, String[] fields, Object[] values) throws SQLException {
        return PersistentOperations.insert(this.connection, tableName, fields, values);
    }
    
    /**
     * @param tableName
     * @param fields
     * @param values
     * @return
     * @throws java.sql.SQLException
     */
    public int insert(String tableName, String[] fields, Object[][] values) throws SQLException {
        return PersistentOperations.insert(this.connection, tableName, fields, values);
    }
    
    /**
     * @param tableName
     * @param fields
     * @param values
     * @param field
     * @param value
     * @return
     * @throws java.sql.SQLException
     */
    public int update(String tableName, String[] fields, Object[] values, String field, Object value) throws SQLException {
        return PersistentOperations.update(this.connection, tableName, fields, values, field, value);
    }
    
    /**
     * @param tableName
     * @param fields
     * @param values
     * @param wherefields
     * @param wherevalues
     * @return
     * @throws java.sql.SQLException
     */
    public int update(String tableName, String[] fields, Object[] values, String[] wherefields, Object[] wherevalues) throws SQLException {
        return PersistentOperations.update(this.connection, tableName, fields, values, wherefields, wherevalues);
    }
    
    /**
     * @param tableName
     * @param field
     * @param value
     * @return
     * @throws java.sql.SQLException
     */
    public int delete(String tableName, String field, Object value) throws SQLException {
        return PersistentOperations.delete(this.connection, tableName, field, value);
    }
    
    /**
     * @param tableName
     * @param field
     * @param values
     * @return
     * @throws java.sql.SQLException
     */
    public int delete(String tableName, String field, Object[] values) throws SQLException {
        return PersistentOperations.delete(this.connection, tableName, field, values);
    }
    
    /**
     * @param tableName
     * @param fields
     * @param values
     * @return
     * @throws java.sql.SQLException
     */
    public int delete(String tableName, String[] fields, Object[] values) throws SQLException {
        return PersistentOperations.delete(this.connection, tableName, fields, values);
    }
    
    /**
     * @param tableName
     * @return
     * @throws java.sql.SQLException
     */
    public int deleteAll(String tableName) throws SQLException {
        return PersistentOperations.deleteAll(this.connection, tableName);
    }
   
    
    /**
     * @param fields
     * @param tableA
     * @param tableB
     * @param joinFieldA
     * @param joinFieldB
     * @return
     * @throws java.sql.SQLException
     */
    public TableTracker join(String[] fields, String tableA, String tableB, String joinFieldA, String joinFieldB) throws SQLException {
        return PersistentOperations.load(this.connection, fields, tableA, tableB, joinFieldA, joinFieldB);
    }
}