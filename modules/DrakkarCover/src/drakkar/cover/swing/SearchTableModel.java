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
 * Esta clase constituye una especificación de la clase AbstractTableModel
 * para mostrar las búsquedas.
 *
 */
public class SearchTableModel extends AbstractTableModel {

    /**
     *
     */
    protected boolean indexing;
    private String serverHost;
    /**
     *
     */
    ArrayList<DocumentMetaData> list = null;
    /**
     *
     */
    protected String column[] = {"Item", "URI"};
    Class[] types = new Class[]{
        java.lang.String.class, java.lang.String.class, java.lang.Float.class,
        java.lang.String.class, java.lang.String.class, java.lang.Integer.class,
        java.lang.Double.class, java.lang.Integer.class
    };

    /**
     * Constructor por defecto de la clase
     */
    public SearchTableModel() {
        this.list = new ArrayList<>();


    }

    /**
     * Constructor de la clase
     *
     * @param documents lista de documentos
     */
    public SearchTableModel(ArrayList<DocumentMetaData> docs) {
        super();
        this.list = docs;

        fireTableStructureChanged();
    }

    /**
     * Adiciona un nuevo metadocumento
     *
     * @param doc metadocumento
     */
    /* public void addElement(DocumentMetaData doc) {
    boolean flag = !data.contains(doc);
    if (flag) {
    data.add(doc);
    }
    }*/
    /**
     * Elimina todos los elementos del modelo
     */
    public void clearModel() {
        int index = this.list.size() - 1;
        this.list.clear();
        if (index >= 0) {
            fireTableDataChanged();

        }
    }

    /**
     * Modifica los valores del modelo
     *
     * @param data datos del modelo
     */
    public void setDataModel(ArrayList<DocumentMetaData> docs) {
        this.list = docs;
        fireTableDataChanged();
    }

    /**
     * {@inheritDoc}
     */
    public int getRowCount() {
        return this.list.size();
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
        DocumentMetaData doc = list.get(rowIndex);
        ResultURI rURI;

        switch (columnIndex) {
            case 0:
                return doc.getName();
            case 1:
                rURI = new ResultURI(doc.getPath(), serverHost);
                return rURI;
            case 2:
                return doc.getSize();
            case 3:
                return doc.getSynthesis();
            case 4:
                return doc.getType();
            case 5:
                return doc.getIndex();
            case 6:
                return doc.getScore();
            case 7:
                return doc.getSearcher();
           
            default:
                return doc;

        }

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

    /**
     * 
     * @return
     */
    public int size() {
        return list.size();
    }

    
}

