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

import java.util.*;

/**
 * Clase que representa una fila de una tabla de la BD
 */
public class RowTable implements java.io.Serializable {
    protected ArrayList values;
    protected ArrayList<String> columnNames;
    
    /**
     *
     */
    public RowTable() {
        values = new ArrayList();
        columnNames = new ArrayList<>();
    }
    
    /**
     * Constructor de copia
     * @param row 
     */
    public RowTable(RowTable row) {
        this(row.getColumnNames().toArray(), row.getValues().toArray());
    }
    
    /**
     *
     * @param columnNames
     * @param values
     */
    public RowTable(Object[] columnNames, Object[] values) {
        this();
        for (int i = 0; i < columnNames.length; i++)
            setValue(columnNames[i].toString(),values[i]);
    }
    
    /**
     *
     * @param columnIndex
     * @param value
     * @return
     */
    public boolean setValue(int columnIndex, Object value){
        if (columnIndex < 0 || columnIndex >= values.size()) return false;
        
        if (value == null) value = new Object();
        values.set(columnIndex,value);
        
        return true;
    }
    
    /**
     * Cambiar el tipo de dato de una columna
     * @param columnName  nombre de la columna
     * @param value  nuevo valor
     * @return true si se puede modificar el valor o false en caso contrario 
     */
    public boolean setValue(String columnName, Object value){
        if (value == null) value = new Object();
        try{
            if(columnNames.contains(columnName))
                values.set(columnNames.indexOf(columnName),value);
            else {
                columnNames.add(columnName);
                values.add(value);
            }
        } catch(java.lang.IndexOutOfBoundsException e){
            return false;
        }
        return true;
    }
    
    /**
     *
     * @param columnNames
     * @param values
     * @return
     */
    public boolean setValues(ArrayList<String> columnNames, ArrayList values){
        try{
            if(columnNames.size() == values.size()){
                values.clear();
                columnNames.clear();
                for (int i=0; i<values.size(); i++)
                    this.setValue(columnNames.get(i),values.get(i));
            }
        } catch(java.lang.NullPointerException e){
            return false;
        }
        return true;
    }
    
    /**
     *
     * @return
     */
    public ArrayList getValues(){
        return values;
    }
    
    /**
     *
     * @param index
     * @return
     */
    public Object getValue(int index){
        return (index>-1 && index<values.size())?values.get(index):null;
    }
    
    /**
     *
     * @param columnName
     * @return
     */
    public Object getValue(String columnName){
        return (columnNames.contains(columnName))?this.getValue(columnNames.indexOf(columnName)):null;
    }
    
    /**
     *
     * @param index
     * @return
     */
    public boolean remove(int index){
        if (index>-1 && index<values.size() && index<columnNames.size()){
            values.remove(index);
            columnNames.remove(index);
            return true;
        }
        return false;
    }
    
    /**
     *
     * @param columnName
     * @return
     */
    public boolean remove(String columnName){
        if (columnNames.contains(columnName)){
            if (columnNames.indexOf(columnName)<values.size()) values.remove(columnNames.indexOf(columnName));
            columnNames.remove(columnName);
            return true;
        }
        return false;
    }
    
    /**
     *
     * @return
     */
    public int size(){
        return values.size();
    }
    
    /**
     *
     */
    public void clear(){
        values.clear();
        columnNames.clear();
    }
    
    /**
     *
     * @param index
     * @return
     */
    public String getColumnName(int index){
        return (index>-1 && index<columnNames.size())?columnNames.get(index):"";
    }
    
    /**
     *
     * @return
     */
    public ArrayList<String> getColumnNames(){
        return columnNames;
    }
    
    /**
     *
     * @param columnName
     * @return
     */
    public boolean contains(String columnName){
        return columnNames.contains(columnName);
    }
    
    /**
     *
     * @return true si el ArrayList values esta vacio o false en caso contrario
     */
    public boolean isEmpty(){
        return (values != null)?values.isEmpty():true;
    }
    
    @Override
    public String toString(){
        StringBuilder result = new StringBuilder(80);
        try{
            for (int i = 0; i<values.size(); i++)
                result.append("[".concat(columnNames.get(i))).append(":".concat(values.get(i).toString())).append("]");
        } catch(java.lang.IndexOutOfBoundsException e){
            return "[]";
        }
        
        String toString = result.toString();
        return (toString != "")?toString:"[]";
    }
    
    @Override
    public boolean equals(Object anObject){
        if (anObject == null || values == null) return false;
        if (values.isEmpty()) return false;
        
        String id = anObject.toString();
        return id == values.get(0).toString();
    }
}