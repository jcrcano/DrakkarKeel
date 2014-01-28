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

import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractListModel;

public class QueryFieldListModel extends AbstractListModel {

    private List<TermListItem> values;

    /**
     * Constructor por defecto de la clase
     */
    public QueryFieldListModel() {
        this.values = new ArrayList<>();
    }

    /**
     * Constructor de la clase
     *
     * @param values lista de usuarios
     */
    public QueryFieldListModel(List<TermListItem> values) {
        this.values = values;

    }

    /**
     * Adiciona un nuevo objeto
     *
     * @param item objeto
     */
    public void add(TermListItem item) {
        int index = values.size();
        this.values.add(item);
        fireIntervalAdded(this, index, index);

    }

    /**
     * Adiciona un nuevo objeto
     *
     * @param index 
     * @param item objeto
     */
    public void add(int index, TermListItem item) {
        int size = values.size();
        this.values.add(index, item);
        fireIntervalAdded(this, index, size);

    }

    /**
     * {@inheritDoc}
     */
    public int getSize() {
        return this.values.size();
    }

    /**
     * Devuelve el usuario en la posición especificada
     *
     * @param index índice del usuario
     *
     * @return usuario
     */
    public TermListItem get(int index) {
        TermListItem obj = (this.values.get(index));
        return obj;
    }

    /**
     * Modifica el valor del objeto del usuario especificado
     *
     * @param index
     * @param item usuario
     */
    public void set(int index, TermListItem item) {

        if (index >= 0) {
            this.values.set(index, item);
            fireContentsChanged(this, index, index);
        }

    }

    /**
     * {@inheritDoc}
     */
    public Object getElementAt(int index) {
        Object obj = this.values.get(index);
        return obj;
    }

    /**
     * Devuelve la lista de usuarios del modelo
     *
     * @return usuarios
     */
    public List<TermListItem> getValues() {

        return this.values;
    }

    /**
     * Modifica los valores del modelo
     *
     * @param values nuevos valore
     */
    public void setValues(List<TermListItem> values) {
        int index = this.values.size();

        if (index >= 1) {
            fireIntervalRemoved(this, 0, (index - 1));
        }

        this.values.clear();

        int size = values.size();

        if (size > 0) {
            int index1 = size - 1;
            this.values.addAll(values);
            fireIntervalAdded(this, 0, index1);
            fireContentsChanged(this, 0, index1);
        }
    }

    /**
     * Inserta los nuevos términos sugeridos al principio de la lista de término
     *
     * @param values nuevos términos
     */
    public void insert(List<TermListItem> values) {
        this.values.addAll(0, values);
        int size = this.values.size();
        int index = (size > 0) ? size - 1 : 0;
        fireIntervalAdded(this, 0, index);
        fireContentsChanged(this, 0, index);
    }

    /**
     * Elimina el usuario especificado del modelo
     *
     * @param item usuario
     *
     * @return true si se pudo eliminar el usuario, false en caso contrario
     */
    public boolean remove(TermListItem item) {

        int index = this.values.indexOf(item);
        boolean flag = this.values.remove(item);
        if (index >= 0) {
            fireIntervalRemoved(this, index, index);
        }
        return flag;
    }

    /**
     * Elimina el usuario de la posición especificada
     *
     * @param index índice
     *
     * @return usuario eliminado
     */
    public TermListItem remove(int index) {
        TermListItem memb = this.values.remove(index);
        fireIntervalRemoved(this, index, index);
        return memb;
    }

    /**
     * Elimina todos los elementos del modelo
     */
    public void clear() {
        int index1 = values.size() - 1;
        this.values.clear();

        if (index1 >= 0) {
            fireIntervalRemoved(this, 0, index1);
            fireContentsChanged(this, 0, index1);
        }
    }

    public int find(String term) {
        String termToFind = term.toLowerCase() ;
        String termItem;
        for (int i = 0; i < values.size(); i++) {
            termItem = values.get(i).getTermSuggest().getTerm();
            if (termItem.equalsIgnoreCase(termToFind)) {
                return i;
            }
        }

        for (int i = 0; i < values.size(); i++) {
            termItem = values.get(i).getTermSuggest().getTerm().toLowerCase();
            if (termItem.contains(termToFind)) {
                return i;
            }
        }

        return -1;
    }

    /**
     *
     * @param item
     * @return
     */
    public boolean contains(TermListItem item) {
        return this.values.contains(item);
    }

    /**
     *
     * @param item
     * @return
     */
    public boolean isEmpty() {
        return this.values.isEmpty();
    }
}
