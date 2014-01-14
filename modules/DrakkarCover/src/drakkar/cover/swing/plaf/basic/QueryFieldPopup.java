/*
 * DrakkarKeel - An Enterprise Collaborative Search Platform
 *
 * The contents of this file are subject under the terms described in the
 * DRAKKARKEEL_LICENSE file included in this distribution; you may not use this
 * file except in compliance with the License. 
 *
 * 2013-2014 DrakkarKeel Platform.
 */
package drakkar.cover.swing.plaf.basic;

import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JList;


/**
 * The interface which defines the methods required for the implementation of the popup
 * portion of a query field.
 */
public interface QueryFieldPopup {
    /**
     * Shows the popup
     */
    public void show();

    /**
     * Hides the popup
     */
    public void hide();

    /**
     * Returns true if the popup is visible (currently being displayed).
     * 
     * @return <code>true</code> if the component is visible; <code>false</code> otherwise.
     */
    public boolean isVisible();

    /**
     * Returns the list that is being used to draw the items in the combo box.
     * This method is highly implementation specific and should not be used
     * for general list manipulation.
     * 
     * @return
     */
    public JList getList();

    /**
     * Returns a mouse listener that will be added to the combo box or null.
     * If this method returns null then it will not be added to the combo box.
     *
     * @return a <code>MouseListener</code> or null
     */
    public MouseListener getMouseListener();

    /**
     * Returns a mouse motion listener that will be added to the combo box or null.
     * If this method returns null then it will not be added to the combo box.
     *
     * @return a <code>MouseMotionListener</code> or null
     */
    public MouseMotionListener getMouseMotionListener();

    /**
     * Returns a key listener that will be added to the combo box or null.
     * If this method returns null then it will not be added to the combo box.
     * @return
     */
    public KeyListener getKeyListener();

    /**
     * Called to inform the ComboPopup that the UI is uninstalling.
     * If the ComboPopup added any listeners in the component, it should remove them here.
     */
    public void uninstallingUI();
}
