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
import com.jidesoft.swing.Selectable;

public class SeekerSelectable implements Selectable {

    protected Seeker seeker;
    protected boolean selected = false;
    protected boolean enabled = true;

    public SeekerSelectable(Seeker seeker) {

        this.seeker = seeker;
        this.enabled = seeker.getState()== Seeker.STATE_ONLINE ? true : false;
    }

    /**
     * Sets the actual element.
     *
     * @param seeker
     */
    public void setSeeker(Seeker seeker) {
        this.seeker = seeker;
    }

    /**
     * Gets the actual element.
     *
     * @return the actual element.
     */
    public Seeker getSeeker() {
        return seeker;
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
     * Enabled selection change. Enabled false doesn't mean selected is false. If it is selected before,
     * setEnable(false) won't make selected become false. In the other word, setEnabled won't change the the value of
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
     * Overrides to consider the hash code of the seeker only. From outside point of view, this class should behave just
     * like seeker itself. That's why we override hashCode.
     *
     * @return the hash code.
     */
    @Override
    public int hashCode() {
        return (seeker != null ? seeker.hashCode() : 0);
    }

    /**
     * Overrides to consider the toString() of seeker only. From outside point of view, this class should behave just
     * like seeker itself. That's why we override toString.
     *
     * @return toString() of seeker.
     */
    @Override
    public String toString() {
        return (seeker != null ? seeker.toString() : "");
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SeekerSelectable) {
            SeekerSelectable select = (SeekerSelectable)obj;
            if(select.getSeeker().equals(seeker)){
                return true;
            }else{
                return false;
            }
        } else {
            return false;
        }
    }
}
