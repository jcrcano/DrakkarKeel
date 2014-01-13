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

import drakkar.oar.TermSuggest;

public class TermListItem {

    private TermSuggest string;
    private boolean isSelected;

    /**
     *
     * @param string
     * @param isSelected
     */
    public TermListItem(TermSuggest string, boolean isSelected) {
        this.string = string;
        this.isSelected = isSelected;
    }



    /**
     * Get the value of isSelected
     *
     * @return the value of isSelected
     */
    public boolean isIsSelected() {
        return isSelected;
    }

    /**
     * Set the value of isSelected
     *
     * @param isSelected new value of isSelected
     */
    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }


    /**
     * Get the value of string
     *
     * @return the value of string
     */
    public TermSuggest getTermSuggest() {
        return string;
    }

    /**
     * Set the value of string
     *
     * @param string new value of string
     */
    public void setTermSuggest(TermSuggest string) {
        this.string = string;
    }


}
