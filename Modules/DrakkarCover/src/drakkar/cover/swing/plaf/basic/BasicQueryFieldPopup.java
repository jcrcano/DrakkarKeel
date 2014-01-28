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

import drakkar.cover.swing.JQueryField;
import drakkar.cover.swing.QueryFieldListModel;
import drakkar.cover.swing.TermListItem;
import drakkar.cover.swing.facade.SearchFacade;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import javax.accessibility.AccessibleContext;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.event.*;

/**
 * This is a basic implementation of the <code>QueryFieldPopup</code> interface.
 * 
 * This class represents the ui for the popup portion of the query field.
 */
public class BasicQueryFieldPopup extends JPopupMenu implements QueryFieldPopup {

    static final ListModel EmptyListModel = new EmptyListModelClass();
    private static Border LIST_BORDER = new LineBorder(Color.BLACK, 1);
    protected JQueryField queryField;
    protected SearchFacade searchFacade;
    protected QueryFieldListModel model;
    /**
     * This protected field is implementation specific. Do not access directly
     * or override. Use the accessor methods instead.
     *
     * @see #getList
     * @see #createList
     */
    protected JList list;
    /**
     * This protected field is implementation specific. Do not access directly
     * or override. Use the create method instead
     *
     * @see #createScroller
     */
    protected JScrollPane scroller;
    /**
     * As of Java 2 platform v1.4 this previously undocumented field is no
     * longer used.
     */
    protected boolean valueIsAdjusting = false;
    // Listeners that are required by the QueryFieldPopup interface
    /**
     * Implementation of all the listener classes.
     */
    private Handler handler;
    /**
     * This protected field is implementation specific. Do not access directly
     * or override. Use the accessor or create methods instead.
     *
     * @see #getMouseMotionListener
     * @see #createMouseMotionListener
     */
    protected MouseMotionListener mouseMotionListener;
    /**
     * This protected field is implementation specific. Do not access directly
     * or override. Use the accessor or create methods instead.
     *
     * @see #getMouseListener
     * @see #createMouseListener
     */
    protected MouseListener mouseListener;
    /**
     * This protected field is implementation specific. Do not access directly
     * or override. Use the accessor or create methods instead.
     *
     * @see #getKeyListener
     * @see #createKeyListener
     */
    protected KeyListener keyListener;
    /**
     * This protected field is implementation specific. Do not access directly
     * or override. Use the create method instead.
     *
     * @see #createListSelectionListener
     */
    protected ListSelectionListener listSelectionListener;
    // Listeners that are attached to the list
    /**
     * This protected field is implementation specific. Do not access directly
     * or override. Use the create method instead.
     *
     * @see #createListMouseListener
     */
    protected MouseListener listMouseListener;
    /**
     * This protected field is implementation specific. Do not access directly
     * or override. Use the create method instead
     *
     * @see #createListMouseMotionListener
     */
    protected MouseMotionListener listMouseMotionListener;
    // Added to the query field for bound properties
    /**
     * This protected field is implementation specific. Do not access directly
     * or override. Use the create method instead
     *
     * @see #createPropertyChangeListener
     */
    protected PropertyChangeListener propertyChangeListener;
    // Added to the query field model
    /**
     * This protected field is implementation specific. Do not access directly
     * or override. Use the create method instead
     *
     * @see #createListDataListener
     */
    protected ListDataListener listDataListener;
    /**
     * This protected field is implementation specific. Do not access directly
     * or override. Use the create method instead
     *
     * @see #createQueryFieldFocusListener
     */
    protected FocusListener focusListener;
    /**
     * This protected field is implementation specific. Do not access directly
     * or override. Use the create method instead
     *
     * @see #createQueryFieldHierarchyBoundsListener
     */
    protected HierarchyBoundsListener hierarchyBoundsListener;
//    /**
//     * This protected field is implementation specific. Do not access directly
//     * or override. Use the create method instead
//     *
//     * @see #createItemListener
//     */
//    protected ItemListener itemListener;
    /**
     * This protected field is implementation specific. Do not access directly
     * or override. 
     */
    protected Timer autoscrollTimer;
    protected boolean hasEntered = false;
    protected boolean isAutoScrolling = false;
    protected int scrollDirection = SCROLL_UP;
    protected static final int SCROLL_UP = 0;
    protected static final int SCROLL_DOWN = 1;
    protected JPopupMenu auxiliarPopup;

    private static class EmptyListModelClass implements ListModel,
            Serializable {

        public int getSize() {
            return 0;
        }

        public Object getElementAt(int index) {
            return null;
        }

        public void addListDataListener(ListDataListener l) {
        }

        public void removeListDataListener(ListDataListener l) {
        }
    };
    //========================================
    // begin QueryFieldPopup method implementations
    //

    /**
     * Implementation of QueryFieldPopup.show().
     */
    public void show() {
//        setListSelection(queryField.getSelectedIndex()); // no me hace falta, solo es para seleccionar el item de la lista que está en el textfield

        Point location = getPopupLocation();
        show(queryField, location.x, location.y);
    }

    /**
     * Implementation of QueryFieldPopup.hide().
     */
    public void hide() {
        MenuSelectionManager manager = MenuSelectionManager.defaultManager();
        MenuElement[] selection = manager.getSelectedPath();
        for (int i = 0; i < selection.length; i++) {
            if (selection[i] == this) {
                manager.clearSelectedPath();
                break;
            }
        }
        if (selection.length > 0) {
            queryField.repaint();
        }
    }

    /**
     * Implementation of QueryFieldPopup.getList().
     */
    public JList getList() {
        return list;
    }

    /**
     * Implementation of QueryFieldPopup.getMouseListener().
     * 
     * @return a <code>MouseListener</code> or null
     * @see QueryFieldPopup#getMouseListener
     */
    public MouseListener getMouseListener() {
        if (mouseListener == null) {
            mouseListener = createMouseListener();
        }
        return mouseListener;
    }

    /**
     * Implementation of QueryFieldPopup.getMouseMotionListener().
     *
     * @return a <code>MouseMotionListener</code> or null
     * @see QueryFieldPopup#getMouseMotionListener
     */
    public MouseMotionListener getMouseMotionListener() {
        if (mouseMotionListener == null) {
            mouseMotionListener = createMouseMotionListener();
        }
        return mouseMotionListener;
    }

    /**
     * Implementation of QueryFieldPopup.getKeyListener().
     *
     * @return a <code>KeyListener</code> or null
     * @see QueryFieldPopup#getKeyListener
     */
    public KeyListener getKeyListener() {
        if (keyListener == null) {
            keyListener = createKeyListener();
        }
        return keyListener;
    }

    /**
     * Called when the UI is uninstalling.  Since this popup isn't in the component
     * tree, it won't get it's uninstallUI() called.  It removes the listeners that
     * were added in addComboBoxListeners().
     */
    public void uninstallingUI() {
        if (propertyChangeListener != null) {
            queryField.removePropertyChangeListener(propertyChangeListener);
        }

        uninstallQueryFieldModelListeners(queryField.getModel());
        uninstallKeyboardActions();
        uninstallListListeners();
        uninstallQueryFieldListeners();
        // We do this, otherwise the listener the ui installs on
        // the model (the combobox model in this case) will keep a
        // reference to the list, causing the list (and us) to never get gced.
        list.setModel(EmptyListModel);
    }

    //
    // end QueryFieldPopup method implementations
    //======================================
    /** 
     * Removes the listeners from the query field model
     *
     * @param model The query field model to install listeners
     * @see #installComboBoxModelListeners
     */
    protected void uninstallQueryFieldModelListeners(QueryFieldListModel model) {
        if (model != null && listDataListener != null) {
            model.removeListDataListener(listDataListener);
        }
    }

    protected void uninstallKeyboardActions() {
        // XXX - shouldn't call this method
//        queryField.unregisterKeyboardAction( KeyStroke.getKeyStroke( KeyEvent.VK_ENTER, 0 ) );
    }

    //===================================================================
    // begin Initialization routines
    //
    public BasicQueryFieldPopup(JQueryField queryField) {
        super();
        setName("QueryFieldPopup.popup");
        this.queryField = queryField;
        this.model = queryField.getModel();

        setLightWeightPopupEnabled(this.queryField.isLightWeightPopupEnabled());

        // UI construction of the popup.
        this.list = createList();
        this.list.setName("QueryField.list");

//        this.listTerm = queryField.getModel().getValues();
        this.scroller = createScroller();
        this.scroller.setName("QueryField.scrollPane");
        this.auxiliarPopup = createAuxiliarPopup();
        configureList();
        configureScroller();
        configurePopup();
        configureAuxiliarPopup();
        installQueryFieldListeners();
        installKeyboardActions();
        list.setComponentPopupMenu(auxiliarPopup);

    }

    // Overriden PopupMenuListener notification methods to inform query field
    // PopupMenuListeners.
    @Override
    protected void firePopupMenuWillBecomeVisible() {
        super.firePopupMenuWillBecomeVisible();
        queryField.firePopupMenuWillBecomeVisible();
    }

    @Override
    protected void firePopupMenuWillBecomeInvisible() {
        super.firePopupMenuWillBecomeInvisible();
        queryField.firePopupMenuWillBecomeInvisible();
    }

    @Override
    protected void firePopupMenuCanceled() {
        super.firePopupMenuCanceled();
        queryField.firePopupMenuCanceled();
    }

    /**
     * Creates a listener 
     * that will watch for mouse-press and release events on the query field.
     * 
     * <strong>Warning:</strong>
     * When overriding this method, make sure to maintain the existing
     * behavior.
     *
     * @return a <code>MouseListener</code> which will be added to 
     * the query field or null
     */
    protected MouseListener createMouseListener() {
        return getHandler();
    }

    /**
     * Creates the mouse motion listener which will be added to the combo
     * box.
     *
     * <strong>Warning:</strong>
     * When overriding this method, make sure to maintain the existing
     * behavior.
     *
     * @return a <code>MouseMotionListener</code> which will be added to
     *         the query field or null
     */
    protected MouseMotionListener createMouseMotionListener() {
        return getHandler();
    }

    /**
     * Creates the key listener that will be added to the query field. If
     * this method returns null then it will not be added to the query field.
     *
     * @return a <code>KeyListener</code> or null
     */
    protected KeyListener createKeyListener() {
        return getHandler();
    }

    /**
     * Creates the focus listener that will be added to the query field. If
     * this method returns null then it will not be added to the query field.
     *
     * @return a <code>KeyListener</code> or null
     */
    protected FocusListener createFocusListener() {
        return getHandler();
    }

    /**
     * Creates the hierarchy bounds listener that will be added to the query field. If
     * this method returns null then it will not be added to the query field.
     *
     * @return a <code>HierarchyBoundsListener</code> or null
     */
    protected HierarchyBoundsListener createHierarchyBoundsListener() {
        return getHandler();
    }

    /**
     * Creates a list selection listener that watches for selection changes in
     * the popup's list.  If this method returns null then it will not
     * be added to the popup list.
     *
     * @return an instance of a <code>ListSelectionListener</code> or null
     */
    protected ListSelectionListener createListSelectionListener() {
        return null;


//        return l;
    }

    /**
     * Creates a list data listener which will be added to the
     * <code>ComboBoxModel</code>. If this method returns null then
     * it will not be added to the query field model.
     *
     * @return an instance of a <code>ListDataListener</code> or null
     */
    protected ListDataListener createListDataListener() {
        return null;
    }

    /**
     * Creates a mouse listener that watches for mouse events in
     * the popup's list. If this method returns null then it will
     * not be added to the query field.
     *
     * @return an instance of a <code>MouseListener</code> or null
     */
    protected MouseListener createListMouseListener() {
        return getHandler();
    }

    /**
     * Creates a mouse motion listener that watches for mouse motion 
     * events in the popup's list. If this method returns null then it will
     * not be added to the query field.
     *
     * @return an instance of a <code>MouseMotionListener</code> or null
     */
    protected MouseMotionListener createListMouseMotionListener() {
        return getHandler();
    }

    /**
     * Creates a <code>PropertyChangeListener</code> which will be added to
     * the query field. If this method returns null then it will not
     * be added to the query field.
     * 
     * @return an instance of a <code>PropertyChangeListener</code> or null
     */
    protected PropertyChangeListener createPropertyChangeListener() {
        return getHandler();
    }

    private Handler getHandler() {
        if (handler == null) {
            handler = new Handler();
        }
        return handler;
    }

    /**
     * Creates the JList used in the popup to display 
     * the items in the query field model. This method is called when the UI class
     * is created.
     *
     * @return a <code>JList</code> used to display the query field items
     */
    protected JList createList() {
        return new JList(queryField.getModel()) {

            @Override
            public void processMouseEvent(MouseEvent e) {
                if (BasicGraphicsUtils.isMenuShortcutKeyDown(e)) {
                    // Fix for 4234053. Filter out the Control Key from the list.
                    // ie., don't allow CTRL key deselection.
                    Toolkit toolkit = Toolkit.getDefaultToolkit();
                    e = new MouseEvent((Component) e.getSource(), e.getID(), e.getWhen(),
                            e.getModifiers() ^ toolkit.getMenuShortcutKeyMask(),
                            e.getX(), e.getY(),
                            e.getXOnScreen(), e.getYOnScreen(),
                            e.getClickCount(),
                            e.isPopupTrigger(),
                            MouseEvent.NOBUTTON);
                }
                super.processMouseEvent(e);
            }
        };
    }

    /**
     * Configures the list which is used to hold the query field items in the
     * popup. This method is called when the UI class
     * is created.
     *
     * @see #createList
     */
    protected void configureList() {
        list.setFont(queryField.getFont());
        list.setForeground(queryField.getForeground());
        list.setBackground(queryField.getBackground());
        list.setBorder(null);
        list.setCellRenderer(queryField.getRenderer());
        list.setFocusable(false);
        list.setValueIsAdjusting(true);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        installListListeners();
    }

    /**
     * Adds the listeners to the list control.
     */
    protected void installListListeners() {
        if ((listMouseListener = createListMouseListener()) != null) {
            list.addMouseListener(listMouseListener);
        }
        if ((listMouseMotionListener = createListMouseMotionListener()) != null) {
            list.addMouseMotionListener(listMouseMotionListener);
        }
        if ((listSelectionListener = createListSelectionListener()) != null) {
            list.addListSelectionListener(listSelectionListener);
        }
    }

    protected void uninstallListListeners() {
        if (listMouseListener != null) {
            list.removeMouseListener(listMouseListener);
            listMouseListener = null;
        }
        if (listMouseMotionListener != null) {
            list.removeMouseMotionListener(listMouseMotionListener);
            listMouseMotionListener = null;
        }
        if (listSelectionListener != null) {
            list.removeListSelectionListener(listSelectionListener);
            listSelectionListener = null;
        }


    }

    protected void uninstallQueryFieldListeners() {
        if (keyListener != null) {
            queryField.removeKeyListener(keyListener);
            keyListener = null;
        }
        if (focusListener != null) {
            queryField.removeFocusListener(focusListener);
            focusListener = null;
        }
        if (hierarchyBoundsListener != null) {
            queryField.removeHierarchyBoundsListener(hierarchyBoundsListener);
            hierarchyBoundsListener = null;
        }
        if (listDataListener != null) {
            queryField.getModel().removeListDataListener(listDataListener);
            listDataListener = null;
        }

    }

    /**
     * Creates the scroll pane which houses the scrollable list.
     * @return
     */
    protected JScrollPane createScroller() {
        JScrollPane sp = new JScrollPane(list,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        sp.setHorizontalScrollBar(null);
        return sp;
    }

    /**
     * Creates the auxiliar popup menu.
     * @return
     */
    protected JPopupMenu createAuxiliarPopup() {
        JPopupMenu jp = new JPopupMenu();
        JMenuItem mItemDelete = new JMenuItem("Delete", new ImageIcon(BasicQueryFieldPopup.class.getResource("/drakkar/cover/resources/termSuggestDelete.png")));
        mItemDelete.setActionCommand("delete");
        AuxiliarPopupActionHandler actionHandler = new AuxiliarPopupActionHandler();
        mItemDelete.addActionListener(actionHandler);
        JMenuItem mItemClear = new JMenuItem("Clear", new ImageIcon(BasicQueryFieldPopup.class.getResource("/drakkar/cover/resources/termSuggestClear.png")));
        mItemClear.setActionCommand("clear");
        mItemClear.addActionListener(actionHandler);
        jp.add(mItemDelete);
        jp.add(mItemClear);

        return jp;
    }

    /**
     * Configures the scrollable portion which holds the list within 
     * the query field popup. This method is called when the UI class
     * is created.
     */
    protected void configureScroller() {
        scroller.setFocusable(false);
        scroller.getVerticalScrollBar().setFocusable(false);
        scroller.setBorder(null);
        scroller.setSize(200, 200);
    }

    /**
     * Configures the popup portion of the query field. This method is called
     * when the UI class is created.
     */
    protected void configurePopup() {
//        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setLayout(new BorderLayout());
        setBorderPainted(true);
        setBorder(LIST_BORDER);
        setOpaque(false);
        add(scroller);
        setDoubleBuffered(true);
        setFocusable(false);
        setSize(200, 150);
    }

    /**
     * Configures the popup portion of the query field. This method is called
     * when the UI class is created.
     */
    protected void configureAuxiliarPopup() {
        auxiliarPopup.setBorderPainted(true);
        auxiliarPopup.setOpaque(false);
        auxiliarPopup.setDoubleBuffered(true);
        auxiliarPopup.setFocusable(false);
    }

    /**
     * This method adds the necessary listeners to the QueryField.
     */
    protected void installQueryFieldListeners() {
        if ((keyListener = createKeyListener()) != null) {
            queryField.addKeyListener(keyListener);
        }
        if ((focusListener = createFocusListener()) != null) {
            queryField.addFocusListener(focusListener);
        }
        if ((hierarchyBoundsListener = createHierarchyBoundsListener()) != null) {
            queryField.addHierarchyBoundsListener(hierarchyBoundsListener);
        }
        installListModelListeners(queryField.getModel());
    }

    /** 
     * Installs the listeners on the query field model. Any listeners installed
     * on the query field model should be removed in
     * <code>uninstallComboBoxModelListeners</code>.
     *
     * @param model The query field model to install listeners
     * @see #uninstallComboBoxModelListeners
     */
    protected void installListModelListeners(QueryFieldListModel model) {
        if (model != null && (listDataListener = createListDataListener()) != null) {
            model.addListDataListener(listDataListener);
        }
    }

    protected void installKeyboardActions() {
        /* XXX - shouldn't call this method. take it out for testing.
        ActionListener action = new ActionListener() {
        public void actionPerformed(ActionEvent e){
        }
        };

        queryField.registerKeyboardAction( action,
        KeyStroke.getKeyStroke( KeyEvent.VK_ENTER, 0 ),
        JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT ); */
    }

    //
    // end Initialization routines
    //=================================================================
    //===================================================================
    // begin Event Listenters
    //
    /**
     * A listener to be registered upon the query field
     * (<em>not</em> its popup menu)
     * to handle mouse events
     * that affect the state of the popup menu.
     * The main purpose of this listener is to make the popup menu
     * appear and disappear.
     * This listener also helps
     * with click-and-drag scenarios by setting the selection if the mouse was
     * released over the list during a drag.
     *
     * <p>
     * <strong>Warning:</strong>
     * We recommend that you <em>not</em> 
     * create subclasses of this class.
     * If you absolutely must create a subclass,
     * be sure to invoke the superclass
     * version of each method.
     *
     * @see BasicQueryFieldPopup#createMouseListener
     */
    protected class InvocationMouseHandler extends MouseAdapter {

        /**
         * Responds to mouse-pressed events on the query field.
         *
         * @param e the mouse-press event to be handled
         */
        @Override
        public void mousePressed(MouseEvent e) {
            getHandler().mousePressed(e);
        }

        /**
         * Responds to the user terminating
         * a click or drag that began on the query field.
         *
         * @param e the mouse-release event to be handled
         */
        @Override
        public void mouseReleased(MouseEvent e) {
            getHandler().mouseReleased(e);
        }
    }

    /**
     * This listener watches for dragging and updates the current selection in the
     * list if it is dragging over the list.
     */
    protected class InvocationMouseMotionHandler extends MouseMotionAdapter {

        public void mouseDragged(MouseEvent e) {
            getHandler().mouseDragged(e);
        }
    }

    /**
     * As of Java 2 platform v 1.4, this class is now obsolete and is only included for
     * backwards API compatibility. Do not instantiate or subclass.
     * <p>
     * All the functionality of this class has been included in 
     * BasicComboBoxUI ActionMap/InputMap methods.
     */
    public class InvocationKeyHandler extends KeyAdapter {

        public void keyReleased(KeyEvent e) {
        }
    }

    /**
     * As of Java 2 platform v 1.4, this class is now obsolete, doesn't do anything, and
     * is only included for backwards API compatibility. Do not call or 
     * override.
     */
    protected class ListSelectionHandler implements ListSelectionListener {

        public void valueChanged(ListSelectionEvent e) {
        }
    }

    /**
     * As of 1.4, this class is now obsolete, doesn't do anything, and
     * is only included for backwards API compatibility. Do not call or 
     * override. 
     * <p>
     * The functionality has been migrated into <code>ItemHandler</code>.
     *
     * @see #createItemListener
     */
    public class ListDataHandler implements ListDataListener {

        public void contentsChanged(ListDataEvent e) {
        }

        public void intervalAdded(ListDataEvent e) {
        }

        public void intervalRemoved(ListDataEvent e) {
        }
    }

    /**
     * This listener hides the popup when the mouse is released in the list.
     */
    protected class ListMouseHandler extends MouseAdapter {

        public void mousePressed(MouseEvent e) {
        }

        public void mouseReleased(MouseEvent anEvent) {
            getHandler().mouseReleased(anEvent);
        }
    }

    /**
     * This listener changes the selected item as you move the mouse over the list.
     * The selection change is not committed to the model, this is for user feedback only.
     */
    protected class ListMouseMotionHandler extends MouseMotionAdapter {

        @Override
        public void mouseMoved(MouseEvent anEvent) {
            getHandler().mouseMoved(anEvent);
        }
    }

    /**
     * This listener watches for bound properties that have changed in the
     * query field.
     * <p>
     * Subclasses which wish to listen to query field property changes should
     * call the superclass methods to ensure that the combo popup correctly 
     * handles property changes.
     * 
     * @see #createPropertyChangeListener
     */
    protected class PropertyChangeHandler implements PropertyChangeListener {

        public void propertyChange(PropertyChangeEvent e) {
            getHandler().propertyChange(e);
        }
    }

    private class AutoScrollActionHandler implements ActionListener {

        private int direction;

        AutoScrollActionHandler(int direction) {
            this.direction = direction;
        }

        public void actionPerformed(ActionEvent e) {
            if (direction == SCROLL_UP) {
                autoScrollUp();
            } else {
                autoScrollDown();
            }
        }
    }

    private class AuxiliarPopupActionHandler implements ActionListener {

        AuxiliarPopupActionHandler() {
        }

        public void actionPerformed(ActionEvent e) {
            JMenuItem source = (JMenuItem) (e.getSource());
            if (source.getActionCommand().equals("delete")) {
                model.remove(list.getSelectedIndex());
                if (model.getSize() == 0) {
                    setVisible(false);
                }
            } else {
                model.clear();
                setVisible(false);
            }
        }
    }

    private class Handler implements KeyListener, MouseListener,
            MouseMotionListener, PropertyChangeListener, FocusListener, HierarchyBoundsListener,
            Serializable {

        private int keyCount = 0;
        private int vkType = -1;
        //
        // MouseListener
        // NOTE: this is added to both the JList and JComboBox
        //

        public void ancestorMoved(HierarchyEvent e) {
            if (isVisible()) {
                setVisible(false);
            }
        }

        /**
         * Called when an ancestor of the source is resized.
         */
        public void ancestorResized(HierarchyEvent e) {
            if (isVisible()) {
                setVisible(false);
            }
        }

        public void focusGained(FocusEvent e) {
        }

        public void focusLost(FocusEvent e) {
            if (e.getSource() == queryField) {
                if (isVisible()) {
                    setVisible(false);
                    keyCount = 0;
                }
            }
        }

        public void keyTyped(KeyEvent e) {
            keyCount++;
        }

        public void keyPressed(KeyEvent e) {
            int keyCode = e.getKeyCode();
            if (keyCode == KeyEvent.VK_ESCAPE) {
                if (isVisible()) {
                    setVisible(false);
                    keyCount = 0;
                }
            } else if (keyCode == KeyEvent.VK_ENTER) {
                if (isVisible() && !list.isSelectionEmpty()) {
                    TermListItem item = (TermListItem) list.getSelectedValue();
                    StringBuilder terms = new StringBuilder(queryField.getText());
                    terms.append(" ");
                    terms.append(item.getTermSuggest().getTerm());
                    queryField.setText(terms.toString());
                    if (!item.isIsSelected()) {
                        item.setIsSelected(true);
                    }
                }
            }
            vkType = keyCode;
        }

        public void keyReleased(KeyEvent e) {
            int keyCode = e.getKeyCode();

            if ((keyCode == KeyEvent.VK_DOWN || keyCode == KeyEvent.VK_UP) && isVisible()) {
                int indexSelected = -1;
                switch (keyCode) {
                    case KeyEvent.VK_DOWN:
                        indexSelected = list.getSelectedIndex();
                        if (indexSelected < queryField.getItemCount() - 1) {
                            int newIndex = indexSelected + 1;
                            list.setSelectedIndex(newIndex);
                            list.ensureIndexIsVisible(newIndex);
                        }
                        break;
                    default:
                        indexSelected = list.getSelectedIndex();
                        int newIndex = indexSelected - 1;
                        if (indexSelected > 0) {
                            list.setSelectedIndex(newIndex);
                            list.ensureIndexIsVisible(newIndex);
                        }
                        break;
                }

            } else if (keyCount > 1 && vkType != KeyEvent.VK_ESCAPE && vkType != KeyEvent.VK_ENTER && vkType != KeyEvent.VK_SPACE && vkType != KeyEvent.VK_DELETE) {
                if (isVisible()) {
                    String query = queryField.getText();
                    if (!query.equals("")) {
                        String[] term = query.split(" |,");
                        findTerm(term[term.length - 1]);
                    }
                } else if (!model.isEmpty()) {
                    setPreferredSize(new Dimension(queryField.getSize().width, 100));
                    Point p = queryField.getLocationOnScreen();
                    setLocation(p.x, p.y + queryField.getSize().height);
                    setVisible(true);
                    queryField.requestFocusInWindow();
                }

            } else if (vkType == KeyEvent.VK_ENTER && searchFacade != null) {
                searchFacade.search(queryField.getText());
            } else if (vkType == KeyEvent.VK_DELETE && queryField.getSelectedText() == null) {
                int index = list.getSelectedIndex();
                model.remove(index);
                if (model.isEmpty()) {
                    setVisible(false);
                } else if (index == 0) {
                    list.setSelectedIndex(index);
                } else {
                    list.setSelectedIndex(--index);
                    list.ensureIndexIsVisible(index);
                }
            }

        }

        public void mouseClicked(MouseEvent evt) {
            if (evt.getSource() == list) {
                boolean isLeftBtn = SwingUtilities.isLeftMouseButton(evt);
                if (isLeftBtn & evt.getClickCount() == 2) {
                    TermListItem item = (TermListItem) list.getSelectedValue();
                    StringBuilder terms = new StringBuilder(queryField.getText());
                    terms.append(" ");
                    terms.append(item.getTermSuggest().getTerm());
                    queryField.setText(terms.toString());
                    if (!item.isIsSelected()) {
                        item.setIsSelected(true);
                    }
                }
            }

        }

        public void mousePressed(MouseEvent e) {
            if (e.getSource() == list) {
                return;
            }
            if (!SwingUtilities.isLeftMouseButton(e) || !queryField.isEnabled()) {
                return;
            }

            if (queryField.isEditable()) {
                Component comp = queryField;
                if ((!(comp instanceof JComponent)) || ((JComponent) comp).isRequestFocusEnabled()) {
                    comp.requestFocus();
                }
            } else if (queryField.isRequestFocusEnabled()) {
                queryField.requestFocus();
            }
            togglePopup();
        }

        public void mouseReleased(MouseEvent e) {
        }

        public void mouseEntered(MouseEvent e) {
        }

        public void mouseExited(MouseEvent e) {
        }

        //
        // MouseMotionListener:
        // NOTE: this is added to both the List and QueryField
        //
        public void mouseMoved(MouseEvent anEvent) {
            if (anEvent.getSource() == list) {
                Point location = anEvent.getPoint();
                Rectangle r = new Rectangle();
                list.computeVisibleRect(r);
                if (r.contains(location)) {
                    updateListBoxSelectionForEvent(anEvent, false);
                }
            }
        }

        public void mouseDragged(MouseEvent e) {
            if (e.getSource() == list) {
                return;
            }
            if (isVisible()) {
                MouseEvent newEvent = convertMouseEvent(e);
                Rectangle r = new Rectangle();
                list.computeVisibleRect(r);

                if (newEvent.getPoint().y >= r.y && newEvent.getPoint().y <= r.y + r.height - 1) {
                    hasEntered = true;
                    if (isAutoScrolling) {
                        stopAutoScrolling();
                    }
                    Point location = newEvent.getPoint();
                    if (r.contains(location)) {
                        updateListBoxSelectionForEvent(newEvent, false);
                    }
                } else {
                    if (hasEntered) {
                        int directionToScroll = newEvent.getPoint().y < r.y ? SCROLL_UP : SCROLL_DOWN;
                        if (isAutoScrolling && scrollDirection != directionToScroll) {
                            stopAutoScrolling();
                            startAutoScrolling(directionToScroll);
                        } else if (!isAutoScrolling) {
                            startAutoScrolling(directionToScroll);
                        }
                    } else {
                        if (e.getPoint().y < 0) {
                            hasEntered = true;
                            startAutoScrolling(SCROLL_UP);
                        }
                    }
                }
            }
        }

        //
        // PropertyChangeListener
        //
        public void propertyChange(PropertyChangeEvent e) {
            JQueryField queryFld = (JQueryField) e.getSource();
            String propertyName = e.getPropertyName();
            switch (propertyName) {
                case "model":
                    QueryFieldListModel oldModel = (QueryFieldListModel) e.getOldValue();
                    QueryFieldListModel newModel = (QueryFieldListModel) e.getNewValue();
                    uninstallQueryFieldModelListeners(oldModel);
                    installListModelListeners(newModel);
                    list.setModel(newModel);
                    if (isVisible()) {
                        hide();
                    }
                    break;
                case "renderer":
                    list.setCellRenderer(queryFld.getRenderer());
                    if (isVisible()) {
                        hide();
                    }
                    break;
                case "componentOrientation":
                    {
                        // Pass along the new component orientation
                        // to the list and the scroller
                        ComponentOrientation o = (ComponentOrientation) e.getNewValue();
                        JList list = getList();
                        if (list != null && list.getComponentOrientation() != o) {
                            list.setComponentOrientation(o);
                        }
                        if (scroller != null && scroller.getComponentOrientation() != o) {
                            scroller.setComponentOrientation(o);
                        }
                        if (o != getComponentOrientation()) {
                            setComponentOrientation(o);
                        }
                        break;
                    }
                case "lightWeightPopupEnabled":
                    setLightWeightPopupEnabled(queryFld.isLightWeightPopupEnabled());
                    break;
            }
        }
    }

    //
    // end Event Listeners
    //=================================================================
    /**
     * Overridden to unconditionally return false.
     */
    public boolean isFocusTraversable() {
        return false;
    }

    //===================================================================
    // begin Autoscroll methods
    //
    /**
     * This protected method is implementation specific and should be private.
     * do not call or override.
     */
    protected void startAutoScrolling(int direction) {
        // XXX - should be a private method within InvocationMouseMotionHandler
        // if possible.
        if (isAutoScrolling) {
            autoscrollTimer.stop();
        }

        isAutoScrolling = true;

        if (direction == SCROLL_UP) {
            scrollDirection = SCROLL_UP;
            Point convertedPoint = SwingUtilities.convertPoint(scroller, new Point(1, 1), list);
            int top = list.locationToIndex(convertedPoint);
            list.setSelectedIndex(top);

            autoscrollTimer = new Timer(100, new AutoScrollActionHandler(
                    SCROLL_UP));
        } else if (direction == SCROLL_DOWN) {
            scrollDirection = SCROLL_DOWN;
            Dimension size = scroller.getSize();
            Point convertedPoint = SwingUtilities.convertPoint(scroller,
                    new Point(1, (size.height - 1) - 2),
                    list);
            int bottom = list.locationToIndex(convertedPoint);
            list.setSelectedIndex(bottom);

            autoscrollTimer = new Timer(100, new AutoScrollActionHandler(
                    SCROLL_DOWN));
        }
        autoscrollTimer.start();
    }

    /**
     * This protected method is implementation specific and should be private.
     * do not call or override.
     */
    protected void stopAutoScrolling() {
        isAutoScrolling = false;

        if (autoscrollTimer != null) {
            autoscrollTimer.stop();
            autoscrollTimer = null;
        }
    }

    /**
     * This protected method is implementation specific and should be private.
     * do not call or override.
     */
    protected void autoScrollUp() {
        int index = list.getSelectedIndex();
        if (index > 0) {
            list.setSelectedIndex(index - 1);
            list.ensureIndexIsVisible(index - 1);
        }
    }

    /**
     * This protected method is implementation specific and should be private.
     * do not call or override.
     */
    protected void autoScrollDown() {
        int index = list.getSelectedIndex();
        int lastItem = list.getModel().getSize() - 1;
        if (index < lastItem) {
            list.setSelectedIndex(index + 1);
            list.ensureIndexIsVisible(index + 1);
        }
    }

    //
    // end Autoscroll methods
    //=================================================================
    //===================================================================
    // begin Utility methods
    //
    /**
     * Gets the AccessibleContext associated with this BasicQueryFieldPopup.
     * The AccessibleContext will have its parent set to the QueryField.
     *
     * @return an AccessibleContext for the BasicQueryFieldPopup
     * @since 1.5
     */
    public AccessibleContext getAccessibleContext() {
        AccessibleContext context = super.getAccessibleContext();
        context.setAccessibleParent(queryField);
        return context;
    }

    /**
     * This is is a utility method that helps event handlers figure out where to
     * send the focus when the popup is brought up.  The standard implementation
     * delegates the focus to the editor (if the query field is editable) or to
     * the JComboBox if it is not editable.
     */
    protected void delegateFocus(MouseEvent e) {
        if (queryField.isEditable()) {
//            Component comp = queryField.getEditor().getEditorComponent();
            Component comp = queryField;
            if ((!(comp instanceof JComponent)) || ((JComponent) comp).isRequestFocusEnabled()) {
                comp.requestFocus();
            }
        } else if (queryField.isRequestFocusEnabled()) {
            queryField.requestFocus();
        }
    }

    /**
     * Makes the popup visible if it is hidden and makes it hidden if it is 
     * visible.
     */
    protected void togglePopup() {
        if (isVisible()) {
            hide();
        } else {
            show();
        }
    }

    /**
     * Sets the list selection index to the selectedIndex. This 
     * method is used to synchronize the list selection with the 
     * query field selection.
     * 
     * @param selectedIndex the index to set the list
     */
    private void setListSelection(int selectedIndex) {
        if (selectedIndex == -1) {
            list.clearSelection();
        } else {
            list.setSelectedIndex(selectedIndex);
            list.ensureIndexIsVisible(selectedIndex);
        }
    }

    protected MouseEvent convertMouseEvent(MouseEvent e) {
        Point convertedPoint = SwingUtilities.convertPoint((Component) e.getSource(),
                e.getPoint(), list);
        MouseEvent newEvent = new MouseEvent((Component) e.getSource(),
                e.getID(),
                e.getWhen(),
                e.getModifiers(),
                convertedPoint.x,
                convertedPoint.y,
                e.getXOnScreen(),
                e.getYOnScreen(),
                e.getClickCount(),
                e.isPopupTrigger(),
                MouseEvent.NOBUTTON);
        return newEvent;
    }

    /**
     * Retrieves the height of the popup based on the current
     * ListCellRenderer and the maximum row count.
     */
    protected int getPopupHeightForRowCount(int maxRowCount) {
        // Set the cached value of the minimum row count
        int minRowCount = Math.min(maxRowCount, queryField.getItemCount());
        int height = 0;
        ListCellRenderer renderer = list.getCellRenderer();
        Object value = null;

        for (int i = 0; i < minRowCount; ++i) {
            value = list.getModel().getElementAt(i);
            Component c = renderer.getListCellRendererComponent(list, value, i, false, false);
            height += c.getPreferredSize().height;
        }

        if (height == 0) {
            height = queryField.getHeight();
        }

        Border border = scroller.getViewportBorder();
        if (border != null) {
            Insets insets = border.getBorderInsets(null);
            height += insets.top + insets.bottom;
        }

        border = scroller.getBorder();
        if (border != null) {
            Insets insets = border.getBorderInsets(null);
            height += insets.top + insets.bottom;
        }

        return height;
    }

    /**
     * Calculate the placement and size of the popup portion of the query field based
     * on the query field location and the enclosing screen bounds. If
     * no transformations are required, then the returned rectangle will
     * have the same values as the parameters.
     * 
     * @param px starting x location
     * @param py starting y location
     * @param pw starting width
     * @param ph starting height
     * @return a rectangle which represents the placement and size of the popup
     */
    protected Rectangle computePopupBounds(int px, int py, int pw, int ph) {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Rectangle screenBounds;

        // Calculate the desktop dimensions relative to the query field.
        GraphicsConfiguration gc = queryField.getGraphicsConfiguration();
        Point p = new Point();
        SwingUtilities.convertPointFromScreen(p, queryField);
        if (gc != null) {
            Insets screenInsets = toolkit.getScreenInsets(gc);
            screenBounds = gc.getBounds();
            screenBounds.width -= (screenInsets.left + screenInsets.right);
            screenBounds.height -= (screenInsets.top + screenInsets.bottom);
            screenBounds.x += (p.x + screenInsets.left);
            screenBounds.y += (p.y + screenInsets.top);
        } else {
            screenBounds = new Rectangle(p, toolkit.getScreenSize());
        }

        Rectangle rect = new Rectangle(px, py, pw, ph);
        if (py + ph > screenBounds.y + screenBounds.height
                && ph < screenBounds.height) {
            rect.y = -rect.height;
        }
        return rect;
    }

    /**
     * Calculates the upper left location of the Popup.
     */
    private Point getPopupLocation() {
        Dimension popupSize = queryField.getSize();
        Insets insets = getInsets();

        // reduce the width of the scrollpane by the insets so that the popup
        // is the same width as the query field.
        popupSize.setSize(popupSize.width - (insets.right + insets.left),
                getPopupHeightForRowCount(queryField.getMaximumRowCount()));
        Rectangle popupBounds = computePopupBounds(0, queryField.getBounds().height,
                popupSize.width, popupSize.height);
        Dimension scrollSize = popupBounds.getSize();
        Point popupLocation = popupBounds.getLocation();

        scroller.setMaximumSize(scrollSize);
        scroller.setPreferredSize(scrollSize);
        scroller.setMinimumSize(scrollSize);

        list.revalidate();

        return popupLocation;
    }

    /**
     * A utility method used by the event listeners.  Given a mouse event, it changes
     * the list selection to the list item below the mouse.
     */
    protected void updateListBoxSelectionForEvent(MouseEvent anEvent, boolean shouldScroll) {
        // XXX - only seems to be called from this class. shouldScroll flag is
        // never true
        Point location = anEvent.getPoint();
        if (list == null) {
            return;
        }
        int index = list.locationToIndex(location);
        if (index == -1) {
            if (location.y < 0) {
                index = 0;
            } else {
                index = queryField.getModel().getSize() - 1;
            }
        }
        if (list.getSelectedIndex() != index) {
            list.setSelectedIndex(index);
            if (shouldScroll) {
                list.ensureIndexIsVisible(index);
            }
        }
    }

    @Override
    public void setSize(int width, int height) {
        super.setSize(width, height);

    }

    protected void findTerm(String term) {
        int index = model.find(term);
        if (index >= 0) {
            list.setSelectedIndex(index);
            list.ensureIndexIsVisible(index);
        }
    }

    /**
     *
     * @return
     */
    public SearchFacade getComponentFacade() {
        return this.searchFacade;
    }

    /**
     *
     * @param facade
     */
    public void setComponentFacade(SearchFacade facade) {

        this.searchFacade = facade;
    }
    //
    // end Utility methods
    //=================================================================
}
