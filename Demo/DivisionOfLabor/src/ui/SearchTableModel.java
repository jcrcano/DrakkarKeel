/*
 * @(#)SearchTableModel.java   2.0.0   26/11/2013
 *
 * CIRLab - Collaborative Information Retrieval Laboratory
 * Webpage: unascribed
 * Contact: cirlab-dev@googlegroups.com
 * University of Granada - Department of Computing Science and Artificial
 * Intelligence
 * http://www.ugr.es/
 * University of the Informatics Sciences - Holguin Software Development Center
 * http://www.uci.cu/
 *
 * The contents of this file are subject under the terms described in the
 * CIRLAB_LICENSE file included in this distribution; you may not use this
 * file except in compliance with the License.
 *
 * Copyright (C) 2008-2013 CIRLab. All Rights Reserved.
 *
 * Contributor(s):
 *   RODRIGUEZ-AVILA, HUMBERTO <hrodrigueza{a.}facinf.uho.edu.cu>
 *   TORRES-LOPEZ, CARMEN <carmen{a.}hlg.desoft.cu>
 *   LEYVA-AMADOR, YOVIER <yovi{a.}hol.inv.cu>
 *   RODRIGUEZ-CANO, JULIO CESAR <jcrcano{a.}uci.cu>
 */
package ui;

import drakkar.oar.DocumentMetaData;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.table.AbstractTableModel;

/**
 * Esta clase constituye una especificación de la clase AbstractTableModel para
 * mostrar las búsquedas.
 *
 */
public class SearchTableModel extends AbstractTableModel {

    /**
     *
     */
    protected boolean indexing;
    private String host;
    /**
     *
     */
    ArrayList<DocumentMetaData> list = null;
    /**
     *
     */
    protected String column[] = {"", ""};
    Class[] types = new Class[]{
        javax.swing.ImageIcon.class, java.lang.String.class
    };

    /**
     * Constructor por defecto de la clase
     */
    @SuppressWarnings("Convert2Diamond")
    public SearchTableModel() {
        this.list = new ArrayList<DocumentMetaData>();
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
    public void setDataModel(ArrayList<DocumentMetaData> docs, String host) {
        this.host = host;
        this.list = docs;
        fireTableDataChanged();
    }

    /**
     * {@inheritDoc}
     */
    @Override
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
    public Object getValueAt(final int rowIndex, int columnIndex) {
        if (columnIndex == 0) {
            return getIcon(rowIndex);
        }
        try {
            DocumentMetaData docMetaData = new DocumentMetaData(list.get(rowIndex)) {
                @Override
                public String toString() {
                    return "    " + (rowIndex + 1) + " -     " + list.get(rowIndex).getName();
                }
            };
            return docMetaData;
        } catch (Exception e) {
            return "";
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

    private ImageIcon getIcon(int rowIndex) {
        try {
            if (list.get(rowIndex).getPath().endsWith(".pdf")) {
                return new ImageIcon(SearchTableModel.class.getResource("/resources/pdf.png"));
            }
            if (list.get(rowIndex).getPath().endsWith(".doc")) {
                return new ImageIcon(SearchTableModel.class.getResource("/resources/doc.png"));
            }
            if (list.get(rowIndex).getPath().endsWith(".txt")) {
                return new ImageIcon(SearchTableModel.class.getResource("/resources/text.png"));
            }
            if (list.get(rowIndex).getPath().endsWith(".java")) {
                return new ImageIcon(SearchTableModel.class.getResource("/resources/java.png"));
            }
        } catch (Exception e) {
        }
        return new ImageIcon();

    }
}
