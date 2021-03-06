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
import drakkar.cover.swing.facade.SearchFacade;
import drakkar.cover.swing.plaf.basic.BasicQueryFieldPopup;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

public class JQueryField extends JTextField {

    private int keyCount = 0;
    private QueryFieldListModel model;
    private QueryFieldListRender render;
    private boolean lightWeightPopupEnabled = JPopupMenu.getDefaultLightWeightPopupEnabled();
    private BasicQueryFieldPopup popup;
    /**
     * This protected field is implementation specific. Do not access directly
     * or override. Use the accessor methods instead.
     *
     * @see #getMaximumRowCount
     * @see #setMaximumRowCount
     */
    protected int maximumRowCount = 8;

    /** Creates new form BeanForm */
    public JQueryField() {
        model = new QueryFieldListModel();
        render = new QueryFieldListRender();
        initComponents();
        popup = new BasicQueryFieldPopup(this);

    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                formActionPerformed(evt);
            }
        });
        addHierarchyBoundsListener(new java.awt.event.HierarchyBoundsListener() {
            public void ancestorResized(java.awt.event.HierarchyEvent evt) {
            }
            public void ancestorMoved(java.awt.event.HierarchyEvent evt) {
                formAncestorMoved(evt);
            }
        });
        addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                formFocusLost(evt);
            }
        });
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                formKeyTyped(evt);
            }
        });
    }// </editor-fold>//GEN-END:initComponents

    private void formActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_formActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_formActionPerformed

    private void formKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyTyped

    }//GEN-LAST:event_formKeyTyped
    int vkScape = -1;
    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed

    }//GEN-LAST:event_formKeyPressed

    private void formFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_formFocusLost

    }//GEN-LAST:event_formFocusLost

    private void formAncestorMoved(java.awt.event.HierarchyEvent evt) {//GEN-FIRST:event_formAncestorMoved

    }//GEN-LAST:event_formAncestorMoved

    public void addSuggestTerms(List<TermSuggest> list) {
        List<TermListItem> values = new ArrayList<>(list.size());
        for (TermSuggest termSuggest : list) {
            values.add(new TermListItem(termSuggest, false));
        }

        model.insert(values);
        render.insert(values);

    }

    public void deleteSuggestTerms() {
        model.clear();
        render.clear();
    }

    /**
     * Notifies <code>PopupMenuListener</code>s that the popup portion of the
     * combo box will become visible.
     * <p>
     * This method is public but should not be called by anything other than
     * the UI delegate.
     * @see #addPopupMenuListener
     * @since 1.4
     */
    public void firePopupMenuWillBecomeVisible() {
        Object[] listeners = listenerList.getListenerList();
        PopupMenuEvent e = null;
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == PopupMenuListener.class) {
                if (e == null) {
                    e = new PopupMenuEvent(this);
                }
                ((PopupMenuListener) listeners[i + 1]).popupMenuWillBecomeVisible(e);
            }
        }
    }

    /**
     * Notifies <code>PopupMenuListener</code>s that the popup portion of the
     * combo box has become invisible.
     * <p>
     * This method is public but should not be called by anything other than
     * the UI delegate.
     * @see #addPopupMenuListener
     * @since 1.4
     */
    public void firePopupMenuWillBecomeInvisible() {
        Object[] listeners = listenerList.getListenerList();
        PopupMenuEvent e = null;
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == PopupMenuListener.class) {
                if (e == null) {
                    e = new PopupMenuEvent(this);
                }
                ((PopupMenuListener) listeners[i + 1]).popupMenuWillBecomeInvisible(e);
            }
        }
    }

    /**
     * Notifies <code>PopupMenuListener</code>s that the popup portion of the
     * combo box has been canceled.
     * <p>
     * This method is public but should not be called by anything other than
     * the UI delegate.
     * @see #addPopupMenuListener
     * @since 1.4
     */
    public void firePopupMenuCanceled() {
        Object[] listeners = listenerList.getListenerList();
        PopupMenuEvent e = null;
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == PopupMenuListener.class) {
                if (e == null) {
                    e = new PopupMenuEvent(this);
                }
                ((PopupMenuListener) listeners[i + 1]).popupMenuCanceled(e);
            }
        }
    }

    public QueryFieldListModel getModel() {
        return model;
    }

    public void setModel(QueryFieldListModel model) {
        this.model = model;
    }

    public QueryFieldListRender getRenderer() {
        return render;
    }

    public void setRenderer(QueryFieldListRender render) {
        this.render = render;
    }


    /* Accessing the model */
    /**
     * Returns the number of items in the list.
     *
     * @return an integer equal to the number of items in the list
     */
    public int getItemCount() {
        return model.getSize();
    }

    /**
     * Sets the maximum number of rows the <code>JQueryField</code> displays.
     * If the number of objects in the model is greater than count,
     * the combo box uses a scrollbar.
     *
     * @param count an integer specifying the maximum number of items to
     *              display in the list before using a scrollbar
     * @beaninfo
     *        bound: true
     *    preferred: true
     *  description: The maximum number of rows the popup should have
     */
    public void setMaximumRowCount(int count) {
        int oldCount = maximumRowCount;
        maximumRowCount = count;
        firePropertyChange("maximumRowCount", oldCount, maximumRowCount);
    }

    /**
     * Returns the maximum number of items the combo box can display
     * without a scrollbar
     *
     * @return an integer specifying the maximum number of items that are
     *         displayed in the list before using a scrollbar
     */
    public int getMaximumRowCount() {
        return maximumRowCount;
    }

    /**
     * Sets the <code>lightWeightPopupEnabled</code> property, which
     * provides a hint as to whether or not a lightweight
     * <code>Component</code> should be used to contain the
     * <code>JQueryField</code>, versus a heavyweight
     * <code>Component</code> such as a <code>Panel</code>
     * or a <code>Window</code>.  The decision of lightweight
     * versus heavyweight is ultimately up to the
     * <code>JQueryField</code>.  Lightweight windows are more
     * efficient than heavyweight windows, but lightweight
     * and heavyweight components do not mix well in a GUI.
     * If your application mixes lightweight and heavyweight
     * components, you should disable lightweight popups.
     * The default value for the <code>lightWeightPopupEnabled</code>
     * property is <code>true</code>, unless otherwise specified
     * by the look and feel.  Some look and feels always use
     * heavyweight popups, no matter what the value of this property.
     * <p>
     * See the article <a href="http://java.sun.com/products/jfc/tsc/articles/mixing/index.html">Mixing Heavy and Light Components</a>
     * on <a href="http://java.sun.com/products/jfc/tsc">
     * <em>The Swing Connection</em></a>
     * This method fires a property changed event.
     *
     * @param aFlag if <code>true</code>, lightweight popups are desired
     *
     * @beaninfo
     *        bound: true
     *       expert: true
     *  description: Set to <code>false</code> to require heavyweight popups.
     */
    public void setLightWeightPopupEnabled(boolean aFlag) {
        boolean oldFlag = lightWeightPopupEnabled;
        lightWeightPopupEnabled = aFlag;
        firePropertyChange("lightWeightPopupEnabled", oldFlag, lightWeightPopupEnabled);
    }

    /**
     * Gets the value of the <code>lightWeightPopupEnabled</code>
     * property.
     *
     * @return the value of the <code>lightWeightPopupEnabled</code>
     *    property
     * @see #setLightWeightPopupEnabled
     */
    public boolean isLightWeightPopupEnabled() {
        return lightWeightPopupEnabled;
    }

    /**
     * Adds a <code>PopupMenu</code> listener which will listen to notification
     * messages from the popup portion of the combo box.
     * <p>
     * For all standard look and feels shipped with Java, the popup list
     * portion of combo box is implemented as a <code>JPopupMenu</code>.
     * A custom look and feel may not implement it this way and will
     * therefore not receive the notification.
     *
     * @param l  the <code>PopupMenuListener</code> to add
     * @since 1.4
     */
    public void addPopupMenuListener(PopupMenuListener l) {
        listenerList.add(PopupMenuListener.class, l);
    }

    /**
     * Removes a <code>PopupMenuListener</code>.
     *
     * @param l  the <code>PopupMenuListener</code> to remove
     * @see #addPopupMenuListener
     * @since 1.4
     */
    public void removePopupMenuListener(PopupMenuListener l) {
        listenerList.remove(PopupMenuListener.class, l);
    }

    /**
     * Causes the combo box to display its popup window.
     * @see #setPopupVisible
     */
    public void showPopup() {
        setPopupVisible(true);
    }

    /**
     * Causes the combo box to close its popup window.
     * @see #setPopupVisible
     */
    public void hidePopup() {
        setPopupVisible(false);
    }

    /**
     * Sets the visibility of the popup.
     */
    public void setPopupVisible(boolean v) {
        popup.setVisible(v);
    }

    /**
     * Determines the visibility of the popup.
     *
     * @return true if the popup is visible, otherwise returns false
     */
    public boolean isPopupVisible() {
        return popup.isVisible();
    }

    /**
     *
     * @return
     */
    public SearchFacade getComponentFacade() {
        return this.popup.getComponentFacade();
    }

    /**
     *
     * @param facade
     */
    public void setComponentFacade(SearchFacade facade) {
        this.popup.setComponentFacade(facade);
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
