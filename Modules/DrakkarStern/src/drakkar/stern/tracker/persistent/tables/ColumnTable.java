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

/**
 * Clase que representa una columna de uan tabla de la BD.
 */
public class ColumnTable {

    protected String name;
    protected String caption;
    protected String type;

    /**
     * 
     * @param name 
     * @param datatype 
     */
    public ColumnTable(String name, String datatype) {
        this.name = name;
        this.caption = name;
        this.type = datatype;
    }

    /**
     * 
     * @param name 
     * @param caption 
     * @param datatype 
     */
    public ColumnTable(String name, String caption, String datatype) {
        this.name = name;
        this.caption = caption;
        this.type = datatype;
    }

    /**
     * 
     * @param column 
     */
    public ColumnTable(ColumnTable column) {
        this.name = column.getName();
        this.caption = column.getName();
        this.type = column.getType();
    }

    /**
     * 
     * @return 
     */
    public String getType() {
        return this.type;
    }

    /**
     * 
     * @return 
     */
    public String getCaption() {
        return this.caption;
    }

    /**
     * 
     * @return 
     */
    public String getName() {
        return this.name;
    }

    /**
     * 
     * @param type 
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * 
     * @param caption 
     */
    public void setCaption(String caption) {
        this.caption = caption;
    }

    /**
     * 
     * @param name 
     */
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name + "(" + type + ")";
    }
}
