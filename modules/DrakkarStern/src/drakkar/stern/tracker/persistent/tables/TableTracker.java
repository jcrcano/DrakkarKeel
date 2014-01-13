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

import java.io.Serializable;
import java.sql.*;
import java.util.*;


/**
 * Clase que representa la tabla que almacenará los resultados de las consultas realizadas a la BD
 */
public class TableTracker implements Serializable {

    /** */
    protected ArrayList<RowTable> rows;
    /** */
    protected ArrayList<ColumnTable> columns;
    /** */
    protected String keyField;
    /** */
    protected String filter = "";
    /** */
    protected String orderBy = "";
    /** */
    protected ArrayList<Integer> interfaceRows;

    /**
     *
     * @throws java.sql.SQLException
     */
    public TableTracker() throws java.sql.SQLException {
        this(null);
    }

    /**
     * @param rs
     * @throws java.sql.SQLException
     */
    public TableTracker(ResultSet rs) throws java.sql.SQLException {
        rows = new ArrayList<>();
        columns = new ArrayList<>();


        if (rs != null) {

            ResultSetMetaData metadata = rs.getMetaData();
            int columnCount = metadata.getColumnCount();


            for (int i = 0; i < columnCount; i++) {
                String columnName = metadata.getColumnName(i + 1);
                String columnType = metadata.getColumnClassName(i + 1);
                columns.add(new ColumnTable(columnName, columnType));
            }

            this.setRows(rs);
        }
    }

    /**
     * @param rs
     * @return
     */
    public boolean setRows(ResultSet rs) {
        try {

//            if(rs.wasNull()) rows = null;
            if (rs == null) {
                rows = null;
            } else {
                RowTable newRow;

                while (rs.next()) {
                    newRow = new RowTable();

                    for (int i = 0; i < columns.size(); i++) {
                        String columnName = columns.get(i).getName();
                        String columnType = columns.get(i).getType();
                       if(columnType.equalsIgnoreCase("byte[]")){

                        Class type = Class.forName("java.lang.Byte");
                       }else{
                        Class type = Class.forName(columnType);
                       }
                        Object value = rs.getObject(i + 1);

                        if (value != null) {
                            newRow.setValue(columnName, value);
                        }
                    }

                    if (!newRow.isEmpty()) {
                        rows.add(newRow);
                    }
                }


            }
            return true;
        } catch (java.sql.SQLException | java.lang.ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * @param rows
     * @return
     */
    public boolean setRows(ArrayList<RowTable> rows) {
        if (rows == null) {
            return false;
        }

        this.rows = rows;
        return true;
    }

    /**
     * @param index
     * @param row
     * @return
     */
    public boolean setRow(int index, RowTable row) {
        if (row == null || index < 0 || index >= rows.size()) {
            return false;
        }

        rows.set(index, row);
        return true;
    }

    /**
     * @param columnNames
     * @param values
     * @return
     */
    public boolean insertRow(String[] columnNames, Object[] values) {
        Object[] val = new Object[this.columns.size()];
        for (int i = 0; i < columnNames.length; i++) {
            if (this.getColumnNames().contains(columnNames[i])) {
                val[this.getColumnNames().indexOf(columnNames[i])] = values[i];
            }
        }

        for (int i = 0; i < val.length; i++) {
            if (val[i] == null) {
                val[i] = "";
            }
        }

        rows.add(new RowTable(this.getColumnNames().toArray(), val));
        return true;
    }

    /**
     * @param row
     * @return
     */
    public boolean insertRow(RowTable row) {
        rows.add(row);
        return true;
    }

    /**
     * @param index
     * @param row
     * @return
     */
    public boolean insertRow(int index, RowTable row) {
        if (row == null || index < 0 || index >= rows.size()) {
            return false;
        }

        rows.add(index, row);
        return true;
    }

    /**
     * @param column
     * @return
     */
    public boolean insertColumn(ColumnTable column) {
        columns.add(column);
        return true;
    }

    /**
     * @param columns
     * @return
     */
    public boolean setColumns(ArrayList<ColumnTable> columns) {
        if (columns == null) {
            return false;
        }

        this.columns = columns;
        return true;
    }

    /**
     * @param index
     * @param column
     * @return
     */
    public boolean setColumn(int index, ColumnTable column) {
        if (column == null || index < 0 || index >= columns.size()) {
            return false;
        }

        columns.set(index, column);
        return true;
    }

    /**
     * @param columnCaptions
     */
    public void setColumnCaptions(String[] columnCaptions) {
        try {
            for (int i = 0; i < columns.size(); i++) {
                columns.get(i).setCaption(columnCaptions[i]);
            }
        } catch (Exception e) {
        }
    }

    /**
     * @param rowIndex
     * @param columnIndex
     * @param value
     * @return
     */
    public boolean setValue(int rowIndex, int columnIndex, Object value) {
        if (value == null || rowIndex < 0 || rowIndex >= rows.size() || columnIndex < 0 || columnIndex >= columns.size()) {
            return false;
        }

        rows.get(rowIndex).setValue(columnIndex, value);
        return true;
    }

    /**
     * @param rowIndex
     * @param columnName
     * @param value
     * @return
     */
    public boolean setValue(int rowIndex, String columnName, Object value) {
        if (value == null || rowIndex < 0 || rowIndex >= rows.size() || !getColumnNames().contains(columnName)) {
            return false;
        }

        rows.get(rowIndex).setValue(columnName, value);
        return true;
    }

    /**
     * @param tableB
     * @throws java.sql.SQLException
     * @param joinFieldA
     * @param joinFieldB
     */
    public void leftJoin(TableTracker tableB, String joinFieldA, String joinFieldB) throws SQLException {
        TableTracker newTable = PersistentOperations.leftjoin(this, tableB, joinFieldA, joinFieldB);
        this.columns = newTable.getColumns();
        this.rows = newTable.getRows();
    }

    /**
     * @param index
     * @return
     */
    public boolean deleteRow(int index) {
        if (index < 0 || index >= rows.size()) {
            return false;
        }
        rows.remove(index);
        return true;
    }

    /**
     * @param filter
     */
    public void setFilter(String filter) {
        this.filter = filter;
        updateInterface();
    }

    /**
     * @param orderBy
     */
    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
        updateInterface();
    }

    /** */
    public void updateInterface() {
    }

    /**
     * @return
     */
    public ArrayList<RowTable> getInterfaceRows() {
        ArrayList<RowTable> result = new ArrayList<>();
        for (Integer index : interfaceRows) {
            result.add(rows.get(index));
        }

        return result;
    }

    /**
     * @return
     */
    public ArrayList<RowTable> getRows() {
        return rows;
    }

    /**
     * @param index
     * @return
     */
    public RowTable getRow(int index) {
        return (index < 0 || index >= rows.size()) ? null : rows.get(index);
    }

    /**
     * @return
     */
    public int getRowCount() {
        return (rows != null) ? rows.size() : 0;
    }

    /**
     * @return
     */
    public ArrayList<ColumnTable> getColumns() {
        return columns;
    }

    /**
     * @return
     */
    public ArrayList<String> getColumnNames() {
        ArrayList<String> result = new ArrayList();
        for (ColumnTable column : columns) {
            result.add(column.getName());
        }

        return result;
    }

    /**
     * @return
     */
    public ArrayList<String> getColumnCaptions() {
        ArrayList<String> result = new ArrayList();
        for (ColumnTable column : columns) {
            result.add(column.getCaption());
        }

        return result;
    }

    /**
     * @param columnNames
     * @return
     */
    public ArrayList<String> getColumnCaptions(String[] columnNames) {
        ArrayList<String> result = new ArrayList();
        for (int i = 0; i < columnNames.length; i++) {
            result.add(this.getColumn(columnNames[i]).getCaption());
        }

        return result;
    }

    /**
     * @return
     */
    public ArrayList<String> getColumnTypes() {
        ArrayList<String> result = new ArrayList();
        for (ColumnTable column : columns) {
            result.add(column.getType());
        }

        return result;
    }

    /**
     * @param index
     * @return
     */
    public ArrayList getColumnValues(int index) {
        if (index < 0 || index >= columns.size()) {
            return null;
        }

        ArrayList result = new ArrayList();
        for (RowTable row : rows) {
            result.add(row.getValue(index));
        }

        return result;
    }

    /**
     * @param columnName
     * @return
     */
    public ArrayList getColumnValues(String columnName) {
        if (!this.getColumnNames().contains(columnName)) {
            return null;
        }

        ArrayList result = new ArrayList();
        for (RowTable row : rows) {
            result.add(row.getValue(columnName));
        }

        return result;
    }

    /**
     * @param columnNames
     * @param separator
     * @return
     */
    public ArrayList getColumnValues(String[] columnNames, String separator) {
        try {
            ArrayList result = new ArrayList();
            int sizeColumnNames = columnNames.length;
            for (RowTable row : rows) {
                StringBuilder value = new StringBuilder(40);
                for (int i = 0; i < sizeColumnNames; i++) {
                    value.append(row.getValue(columnNames[i]).toString());
                    if (i < sizeColumnNames - 1) {
                        value.append(separator);
                    }
                }
                result.add(value.toString());
            }

            return result;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * @param columnName
     * @param filterColumn
     * @param filterValue
     * @return
     */
    public ArrayList getColumnValues(String columnName, String filterColumn, Object filterValue) {
        if (filterColumn == "" || filterValue == null) {
            return getColumnValues(columnName);
        }
        if (!this.getColumnNames().contains(columnName)) {
            return null;
        }
        if (!this.getColumnNames().contains(filterColumn)) {
            return null;
        }

        ArrayList result = new ArrayList();
        for (RowTable row : rows) {
            if (row.getValue(filterColumn).equals(filterValue)) {
                result.add(row.getValue(columnName));
            }
        }

        return result;
    }

    /**
     * @param columnNames
     * @param separator
     * @param filterColumn
     * @param filterValue
     * @return
     */
    public ArrayList getColumnValues(String[] columnNames, String separator, String filterColumn, Object filterValue) {
        if (filterColumn == "" || filterValue == null) {
            return getColumnValues(columnNames, separator);
        }

        try {
            ArrayList result = new ArrayList();
            int sizeColumnNames = columnNames.length;

            for (RowTable row : rows) {
                if (row.getValue(filterColumn).equals(filterValue)) {
                    StringBuilder value = new StringBuilder(40);
                    for (int i = 0; i < sizeColumnNames; i++) {
                        value.append(row.getValue(columnNames[i]).toString());
                        if (i < sizeColumnNames - 1) {
                            value.append(separator);
                        }
                    }
                    result.add(value.toString());
                }
            }

            return result;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * @param index
     * @return
     */
    public ColumnTable getColumn(int index) {
        return (index < 0 || index >= columns.size()) ? null : columns.get(index);
    }

    /**
     * @param name
     * @return
     */
    public ColumnTable getColumn(String name) {
        if (!this.getColumnNames().contains(name)) {
            return null;
        }
        for (ColumnTable column : columns) {
            if (column.getName().equals(name)) {
                return column;
            }
        }
        return null;
    }

    /**
     * @return
     */
    public int getColumnCount() {
        return (columns != null) ? columns.size() : 0;
    }

    /**
     * @param rowIndex
     * @param columnIndex
     * @return
     */
    public Object getValue(int rowIndex, int columnIndex) {
        if (rowIndex < 0 || rowIndex >= rows.size() || columnIndex < 0 || columnIndex >= columns.size()) {
            return null;
        }

        return rows.get(rowIndex).getValue(columnIndex);
    }

    /**
     * @param rowIndex
     * @param columnName
     * @return
     */
    public Object getValue(int rowIndex, String columnName) {
        if (rowIndex < 0 || rowIndex >= rows.size() || !getColumnNames().contains(columnName)) {
            return null;
        }

        return rows.get(rowIndex).getValue(columnName);
    }

    /**
     * @return
     */
    public Object[][] getValues() {
        int rowcount = this.getRowCount();
        int colcount = this.getColumnCount();
        Object[][] result = new Object[rowcount][colcount];

        for (int r = 0; r < rowcount; r++) {
            for (int c = 0; c < colcount; c++) {
                result[r][c] = rows.get(r).getValue(c);
            }
        }

        return result;
    }

    /**
     * @param columnNames
     * @return
     */
    public Object[][] getValues(String[] columnNames) {
        int rowcount = this.getRowCount();
        int colcount = columnNames.length;
        Object[][] result = new Object[rowcount][colcount];

        for (int r = 0; r < rowcount; r++) {
            for (int c = 0; c < colcount; c++) {
                result[r][c] = rows.get(r).getValue(columnNames[c]);
            }
        }

        return result;
    }

    /**
     * @return
     */
    public String getFilter() {
        return filter;
    }

    /**
     * @return
     */
    public String getOrderBy() {
        return orderBy;
    }

    /**
     * @param id
     * @return
     */
    public int indexOf(Object id) {
        return rows.indexOf(id);
    }

    /**
     * @return
     */
    public boolean isEmpty() {
        return (rows == null) ? true : rows.isEmpty();
    }

    /**
     * @param rowIndex
     * @return
     */
    public boolean isDisposable(int rowIndex) {
        if (rows == null) {
            return false;
        }
        if (rowIndex < 0 || rowIndex >= getRowCount()) {
            return false;
        }

        return getValue(rowIndex, 0).toString().equals("");
    }
}
