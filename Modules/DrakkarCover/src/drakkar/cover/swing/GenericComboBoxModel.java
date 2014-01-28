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

import java.io.Serializable;
import java.util.ArrayList;
import javax.swing.AbstractListModel;
import javax.swing.MutableComboBoxModel;

/**
 * Esta clase constituye una especificación de la clase AbstractListModel para el
 * Framework DrakkarKeel
 * 
 */
public class GenericComboBoxModel extends AbstractListModel implements MutableComboBoxModel, Serializable {

    ArrayList values;
    Object selectedObject;

    /**
     * Constructs an empty DefaultComboBoxModel object
     */
    public GenericComboBoxModel() {
        values = new ArrayList();
    }

    /**
     * Constructs a DefaultComboBoxModel object initialized with
     * an array of objects
     *
     * @param items  an array of Object objects
     */
    public GenericComboBoxModel(final Object[] items) {
        values = new ArrayList();
        values.ensureCapacity(items.length);

        int i, c;
        for (i = 0, c = items.length; i < c; i++) {
            values.add(items[i]);
        }

        if (getSize() > 0) {
            selectedObject = getElementAt(0);
        }
    }

    /**
     * Constructs a DefaultComboBoxModel object initialized with
     * a vector
     *
     * @param v  a Vector object ...
     */
    public GenericComboBoxModel(ArrayList<?> v) {
        values = v;

        if (getSize() > 0) {
            selectedObject = getElementAt(0);
        }
    }

    /**
     * {@inheritDoc}
     *
     */
    public void setSelectedItem(Object anObject) {

        if ((selectedObject != null && !selectedObject.equals(anObject))
                || (selectedObject == null && anObject != null)) {
            selectedObject = anObject;
            fireContentsChanged(this, -1, -1);
        }

    }

    /**
     * {@inheritDoc}
     *
     */
    public Object getSelectedItem() {
        return selectedObject;
    }

    /**
     * {@inheritDoc}
     *
     */
    public int getSize() {
        return values.size();
    }

    /**
     * {@inheritDoc}
     *
     */
    public Object getElementAt(int index) {
        if (index >= 0 && index < values.size()) {
            return values.get(index);
        } else {
            return null;
        }
    }

    /**
     * {@inheritDoc}
     *
     */
    public int getIndexOf(Object anObject) {
        return values.indexOf(anObject);
    }

    /**
     * {@inheritDoc}
     * 
     */
    public void addElement(Object anObject) {
        values.add(anObject);
        fireIntervalAdded(this, values.size() - 1, values.size() - 1);
        if (values.size() == 1 && selectedObject == null && anObject != null) {
            setSelectedItem(anObject);
        }
    }

    /**
     * {@inheritDoc}
     * @param element
     */
    public void insertElementAt(Object element, int index) {
        values.add(index, element);
        fireIntervalAdded(this, index, index);
    }

    /**
     * {@inheritDoc}
     */
    public void removeElementAt(int index) {
        if (getElementAt(index) == selectedObject) {
            if (index == 0) {

                setSelectedItem(getSize() == 1 ? null : getElementAt(index + 1));
            } else {

                setSelectedItem(getElementAt(index - 1));
            }
        }

        values.remove(index);

        fireIntervalRemoved(this, index, index);
    }

    /**
     * {@inheritDoc}
     *
     * 
     */
    public void removeElement(Object anObject) {
        int index = values.indexOf(anObject);

        if (index != -1) {
            removeElementAt(index);
        }
    }

    /**
     * Empties the list.
     */
    public void removeAllElements() {
        if (values.size() > 0) {
            int firstIndex = 0;
            int lastIndex = values.size() - 1;
            values.clear();
            selectedObject = null;
            fireIntervalRemoved(this, firstIndex, lastIndex);
        } else {
            selectedObject = null;
        }
    }

    /**
     * Modifica la lista de elementos
     *
     * @param list nueva lista
     */
    public void setValues(ArrayList list) {
        int index = this.values.size() - 1;
       
        if (index >= 0) {
            fireIntervalRemoved(this, 0, index);
        }

        this.values.clear();
        this.values.addAll(list);

        int size = list.size();

        if (size > 0) {
            int index1 = size - 1;
            fireIntervalAdded(this, 0, index1);
            fireContentsChanged(this, 0, index1);
        }

    }

    /**
     * Devuelve la lista de elementos
     *
     * @return elementos
     */
    public ArrayList getValues() {
        return this.values;
    }
}
