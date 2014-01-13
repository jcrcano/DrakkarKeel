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
import javax.swing.AbstractListModel;

public class GenericCheckListModel extends AbstractListModel {

    private ArrayList<ItemSelectable> values;

    public GenericCheckListModel() {
        this.values = new ArrayList<>();
        
    }

    /**
     * Constructor de la clase
     *
     * @param values lista de usuarios
     */
    public GenericCheckListModel(ArrayList<ItemSelectable> values) {
        this.values = values;
    }

    /**
     * Adiciona un nuevo objeto
     *
     * @param item objeto
     */
    public void add(ItemSelectable item) {
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
    public void add(int index, ItemSelectable item) {
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
    public ItemSelectable get(int index) {
        ItemSelectable obj = this.values.get(index);
        return obj;
    }

    /**
     * Modifica el valor del objeto del usuario especificado
     *
     * @param item usuario
     * @return 
     */
    public boolean update(ItemSelectable item) {
        int index = this.values.indexOf(item);

        if (index >= 0) {
            this.values.set(index, item);
            fireContentsChanged(this, index, index);
            return true;
        }

        return false;

    }

    /**
     * Modifica el valor del objeto del usuario especificado
     *
     * @param index
     * @param item usuario
     */
    public void set(int index, ItemSelectable item) {

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
     * Devuelve la lista de elementos del modelo
     *
     * @return usuarios
     */
    public ArrayList<ItemSelectable> getValues() {
        return this.values;
    }

    /**
     * Modifica los valores del modelo
     *
     * @param values nuevos valore
     */
    public void setValues(ArrayList<ItemSelectable> values) {
        int index = this.values.size();

        if (index >= 1) {
            fireIntervalRemoved(this, 0, (index - 1));
        }

        
        this.values.clear();
        this.values.addAll(values);

         int size = values.size();

        if (size > 0) {
            int index1 = size - 1;
            fireIntervalAdded(this, 0, index1);
            fireContentsChanged(this, 0, index1);
        }

    }

    /**
     * Elimina el objeto especificado del modelo
     *
     * @param item objeto
     *
     * @return true si se pudo eliminar el objeto, false en caso contrario
     */
    public boolean remove(ItemSelectable item) {
        int index = this.values.indexOf(item);
        boolean flag = this.values.remove(item);
        if (index >= 0) {
            fireIntervalRemoved(this, index, index);
        }
        return flag;
    }

    /**
     * Elimina el objeto de la posición especificada
     *
     * @param index índice
     *
     * @return objeto eliminado
     */
    public ItemSelectable remove(int index) {
        ItemSelectable item = this.values.remove(index);
        fireIntervalRemoved(this, index, index);
        return item;
    }

    /**
     * Elimina todos los objetos del modelo
     */
    public void clear() {
        int index1 = values.size() - 1;
        this.values.clear();

        if (index1 >= 0) {
            fireIntervalRemoved(this, 0, index1);
            fireContentsChanged(this, 0, index1);
        }
    }

    /**
     *
     *
     * @param item 
     * @return
     */
    public boolean contains(ItemSelectable item) {
        return this.values.contains(item);
    }
}
