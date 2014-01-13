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

import drakkar.oar.Seeker;
import java.util.ArrayList;
import javax.swing.AbstractListModel;

public class SeekerCheckListModel extends AbstractListModel {

    private ArrayList<SeekerSelectable> values;

    /**
     * Constructor por defecto de la clase
     */
    public SeekerCheckListModel() {
        this.values = new ArrayList<>();
    }

    /**
     * Constructor de la clase
     *
     * @param values lista de usuarios
     */
    public SeekerCheckListModel(ArrayList<Seeker> values) {
        this.values = new ArrayList<>();
        for (Seeker seeker : values) {
            this.values.add(new SeekerSelectable(seeker));
        }

    }

    /**
     * Adiciona un nuevo objeto
     *
     * @param seeker objeto
     */
    public void add(Seeker seeker) {
        int index = values.size();
        this.values.add(new SeekerSelectable(seeker));
        fireIntervalAdded(this, index, index);

    }

    /**
     * Adiciona un nuevo objeto
     *
     * @param index 
     * @param seeker objeto
     */
    public void add(int index, Seeker seeker) {
        int size = values.size();
        this.values.add(index, new SeekerSelectable(seeker));
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
    public Seeker get(int index) {
        Seeker obj = (this.values.get(index)).getSeeker();
        return obj;
    }

    /**
     * Modifica el valor del objeto del usuario especificado
     *
     * @param seeker usuario
     * @return
     */
    public boolean update(Seeker seeker) {
        SeekerSelectable temp = new SeekerSelectable(seeker);
        int index = this.values.indexOf(temp);
        if (index >= 0) {
            this.values.set(index, temp);
            fireContentsChanged(this, index, index);
            return true;
        }

        return false;

    }

    /**
     * Modifica el valor del objeto del usuario especificado
     *
     * @param index
     * @param seeker usuario
     */
    public void set(int index, Seeker seeker) {

        if (index >= 0) {
            this.values.set(index, new SeekerSelectable(seeker));
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
    public ArrayList<Seeker> getValues() {
        ArrayList<Seeker> list = null;

        for (SeekerSelectable item : values) {

            list.add(item.getSeeker());
        }

        return list;
    }

    /**
     * Modifica los valores del modelo
     *
     * @param values nuevos valore
     */
    public void setValues(ArrayList<Seeker> values) {
        int index = this.values.size();

        if (index >= 1) {
            fireIntervalRemoved(this, 0, (index - 1));
        }

        this.values.clear();

        int size = values.size();

        if (size > 0) {
            int index1 = size - 1;
            for (Seeker seeker : values) {
                this.values.add(new SeekerSelectable(seeker));
            }
            fireIntervalAdded(this, 0, index1);
            fireContentsChanged(this, 0, index1);
        }
    }

    /**
     * Elimina el usuario especificado del modelo
     *
     * @param seeker usuario
     *
     * @return true si se pudo eliminar el usuario, false en caso contrario
     */
    public boolean remove(Seeker seeker) {
        SeekerSelectable temp = new SeekerSelectable(seeker);
        int index = this.values.indexOf(temp);
        boolean flag = this.values.remove(temp);
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
    public Seeker remove(int index) {
        Seeker memb = (this.values.remove(index)).getSeeker();
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

    /**
     *
     * @param seeker
     * @return
     */
    public boolean contains(Seeker seeker) {
        return this.values.contains(new SeekerSelectable(seeker));
    }
}
