/*
 * DrakkarKeel - An Enterprise Collaborative Search Platform
 *
 * The contents of this file are subject under the terms described in the
 * DRAKKARKEEL_LICENSE file included in this distribution; you may not use this
 * file except in compliance with the License. 
 *
 * 2013-2014 DrakkarKeel Platform.
 */
package drakkar.cover.swing;

import drakkar.oar.DocumentMetaData;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

/**
 * Esta clase constituye una especificación de la clase AbstractTableModel para el
 * Framework DrakkarKeel
 */
public class MetaDocTableModel extends AbstractTableModel {

    /**
     *
     */
    protected ArrayList<DocumentMetaData> values;
    /**
     *
     */
    protected boolean indexing;
    /**
     *
     */
    protected String column[] = {"Item", "URI", "Size(B)", "Score"};
    Class[] types = new Class[]{
        java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
    };

    /**
     * Constructor por defecto de la clase
     */
    public MetaDocTableModel() {
        this.values = new ArrayList<>();
    }

    /**
     * Constructor de la clase
     *
     * @param documents lista de documentos
     */
    public MetaDocTableModel(ArrayList<DocumentMetaData> documents) {
        this.values = documents;
    }

    /**
     * Adiciona un nuevo metadocumento
     *
     * @param doc metadocumento
     */
    public void addElement(DocumentMetaData doc) {
        boolean flag = !values.contains(doc);
        if (flag) {
            values.add(doc);
        }
    }

    /**
     * Elimina todos los elementos del modelo
     */
    public void clearModel() {
        int index = this.values.size()-1;
        this.values.clear();
        if (index >= 0) {
	    fireTableDataChanged();

	}
    }

    /**
     * Modifica los valores del modelo
     *
     * @param data datos del modelo
     */
    public void setValues(ArrayList<DocumentMetaData> data) {
        this.values = data;
        fireTableDataChanged();
    }

     /**
     * Modifica los valores del modelo
     *
     */
    public ArrayList<DocumentMetaData> getValues() {
       return this.values;
    }


     /**
     * {@inheritDoc}
     */
    public int getRowCount() {
        return this.values.size();
    }

     /**
     * {@inheritDoc}
     */
    public int getColumnCount() {
        return column.length;
    }

     /**
     * {@inheritDoc}
     */
    public Object getValueAt(int rowIndex, int columnIndex) {
        //throw new UnsupportedOperationException("Not supported yet.");

        DocumentMetaData doc = values.get(rowIndex);

        if (columnIndex == 0) {
            return doc.getName();
        }/* else if (columnIndex == 1) {
        return (indexing)?doc.getPath():doc.getScore();
        }*/ else if (columnIndex == 1) {
            return (indexing) ? doc.getSize() : doc.getPath();
        }
        return doc.getSize();
    }

     /**
     * {@inheritDoc}
     */
    @Override
    public String getColumnName(int column) {
        return this.column[column];

    }

     /**
     * {@inheritDoc}
     */
    @Override
    public Class getColumnClass(int columnIndex) {
        return types[columnIndex];
    }


}

