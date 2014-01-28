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

import com.jidesoft.swing.Selectable;
import java.awt.Color;
import javax.swing.ImageIcon;

public class ItemSelectable implements Selectable {

    private String label;
    private Object value;
    private String toolTip;
    private ImageIcon icon;
    private Color color;
    private boolean selected;
    private boolean enabled;

    /**
     *
     * @param label
     */
    public ItemSelectable(String label) {
        this.label = label;
        this.value = null;
        this.toolTip = null;
        this.icon = null;
        this.selected = false;
        this.enabled = true;
        this.color = Color.BLACK;

    }

    /**
     *
     * @param label
     * @param value
     */
    public ItemSelectable(String label, Object value) {
        this.label = label;
        this.value = value;
        this.toolTip = null;
        this.icon = null;
        this.selected = false;
        this.enabled = true;
        this.color = Color.BLACK;
    }

    /**
     *
     * @param label
     * @param enabled
     */
    public ItemSelectable(String label, boolean enabled) {
        this.label = label;
        this.value = null;
        this.enabled = enabled;
        this.toolTip = null;
        this.icon = null;
        this.selected = false;
        this.color = Color.BLACK;

    }

    /**
     *
     * @param label
     * @param value
     * @param enabled
     */
    public ItemSelectable(String label, Object value, boolean enabled) {
        this.label = label;
        this.value = value;
        this.enabled = enabled;
        this.toolTip = null;
        this.icon = null;
        this.selected = false;
        this.color = Color.BLACK;

    }

    /**
     *
     * @param label
     * @param value
     * @param icon
     * @param enabled
     */
    public ItemSelectable(String label, Object value, ImageIcon icon, boolean enabled) {
        this.label = label;
        this.value = value;
        this.toolTip = null;
        this.icon = icon;
        this.enabled = enabled;
        this.selected = false;
        this.color = Color.BLACK;
    }

    /**
     *
     * @param label
     * @param value
     * @param toolTip
     * @param icon
     * @param enabled
     */
    public ItemSelectable(String label, Object value, String toolTip, ImageIcon icon, boolean enabled) {
        this.label = label;
        this.value = value;
        this.toolTip = toolTip;
        this.icon = icon;
        this.enabled = enabled;
        this.selected = false;
        this.color = Color.BLACK;
    }

    /**
     * Sets it as selected.
     *
     * @param selected
     */
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    /**
     * Inverts the selection status.
     */
    public void invertSelected() {
        setSelected(!selected);
    }

    /**
     * Gets the selected status.
     *
     * @return true if it is selected. Otherwise, false.
     */
    public boolean isSelected() {
        return selected;
    }

    /**
     * Enabled selection change. Enabled false doesn't mean selected is false.
     * If it is selected before, setEnable(false) won't make selected become
     * false. In the other word, setEnabled won't change the the value of
     * isSelected().
     *
     * @param enabled
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * Checks if selection change is allowed.
     *
     * @return true if selection change is allowed.
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Devuelve el color de la fuente del label
     *
     * @return color
     */
    public Color getColor() {
        return color;
    }

    /**
     * Modifica el color de la fuente del label
     *
     * @param color nuevo color
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * Devuelve del icono del objeto
     *
     * @return imagen
     */
    public ImageIcon getIcon() {
        return icon;
    }

    /**
     * Modifica del icono del objeto
     *
     * @param icon nuevo icono
     */
    public void setIcon(ImageIcon icon) {
        this.icon = icon;
    }

    /**
     * Devuelve el valor del label
     *
     * @return
     */
    public String getLabel() {
        return label;
    }

    /**
     * Modifica el valor del label
     *
     * @param label nuevo texto
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * Devuelve el valor del tooltip
     *
     * @return
     */
    public String getToolTip() {
        return toolTip;
    }

    /**
     * Modifica el valor del tooltip
     *
     * @param toolTip
     */
    public void setToolTip(String toolTip) {
        this.toolTip = toolTip;
    }

    /**
     * Devuelve el valor del objeto
     *
     * @return objeto
     */
    public Object getValue() {
        return value;
    }

    /**
     * Modifica el valor del objeto
     *
     * @param value nuevo valor
     */
    public void setValue(Object value) {
        this.value = value;
    }

    /**
     * Overrides to consider the hash code of the item only. From outside point
     * of view, this class should behave just like item itself. That's why we
     * override hashCode
     *
     * @return the hash code
     */
    @Override
    public int hashCode() {

        int hash = 3;

        hash = 95 * hash + (this.label != null ? this.label.hashCode() : 0) + (this.value != null ? this.value.hashCode() : 0);
        return hash;

    }

    /**
     * Overrides to consider the toString() of item only. From outside point of
     * view, this class should behave just like item itself. That's why we
     * override toString
     *
     * @return toString() of item
     */
    @Override
    public String toString() {
        return (value != null ? value.toString() : label);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ItemSelectable) {
            ItemSelectable select = (ItemSelectable) obj;

            if (select.hashCode() == this.hashCode()) {

                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}
