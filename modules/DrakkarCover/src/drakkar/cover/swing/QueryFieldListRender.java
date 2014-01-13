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

import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

public class QueryFieldListRender extends JLabel implements ListCellRenderer {

    private List<TermListItem> values;
    private int widthIcon;
    private int heightIcon;
    private ImageIcon defaultImage;
    private ImageIcon defaultImageDisabled;
    private Color selected, nonSelected;
    private boolean isShowIcon;

    /**
     * Constructor por defecto de la clase
     */
    public QueryFieldListRender() {
        super();
        this.values = new ArrayList<>();
        isShowIcon = true;
        this.defaultImage = new ImageIcon(QueryFieldListRender.class.getResource("/drakkar/cover/resources/termSuggest.png"));
        this.defaultImageDisabled = new ImageIcon(QueryFieldListRender.class.getResource("/drakkar/cover/resources/termSuggest_searched.png"));
        this.heightIcon = 16;
        this.widthIcon = 16;
        this.selected = new Color(192, 192, 192);
        this.nonSelected = new Color(11, 199, 141);

    }

    /**
     * Constructor de la clase
     *
     * @param itemsList lista de usuarios
     */
    public QueryFieldListRender(List<TermListItem> itemsList) {
        super();
        this.values = itemsList;
        isShowIcon = true;
        this.defaultImage = new ImageIcon(QueryFieldListRender.class.getResource("/drakkar/cover/resources/termSuggest.png"));
        this.defaultImageDisabled = new ImageIcon(QueryFieldListRender.class.getResource("/drakkar/cover/resources/termSuggest_searched.png"));
        this.heightIcon = 16;
        this.widthIcon = 16;
        this.selected = new Color(192, 192, 192);
        this.nonSelected = new Color(11, 199, 141);
    }

    /**
     * Adiciona un usuario
     *
     * @param item usuario
     */
    public void add(TermListItem item) {
        this.values.add(item);
    }

    /**
     * Adiciona un usuario
     *
     * @param index 
     * @param item usuario
     */
    public void add(int index, TermListItem item) {
        this.values.add(index, item);
    }

    /**
     * Devuelve la lista de usuarios
     *
     * @return usuarios
     */
    public List<TermListItem> getValues() {
        return values;
    }

    /**
     * Modifica la lista de usurios
     *
     * @param itemsList nueva lista de usurios
     */
    public void setValues(List<TermListItem> itemsList) {
        this.values.clear();

        this.values.addAll(itemsList);
    }

    /**
     * Modifica la lista de usurios
     *
     * @param itemsList nueva lista de usurios
     */
    public void insert(List<TermListItem> itemsList) {
        this.values.addAll(0, itemsList);
    }

    /**
     * Modifica el valor del usuario
     * 
     * @param index
     * @param item
     */
    public void set(int index, TermListItem item) {
        this.values.set(index, item);
    }

    /**
     * Elimina un usuario de la lista de usuarios
     *
     * @param item usuario
     */
    public void remove(TermListItem item) {
        this.values.remove(item);
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
     * Elimina todos los usuarios
     */
    public void clear() {
        this.values.clear();
    }

//    // This is the only method defined by ListCellRenderer.
//    // We just reconfigure the JLabel each time we're called.
//    private String getState(String value) {
//        String name = null;
//        TermListItem item;
//        for (int i = 0; i < values.size(); i++) {
//            item = (values.get(i)).getTermListItem();
//            name = item.getUser();
//            if (value.equals(name)) {
//                int state = item.getState();
//                switch (state) {
//                    case TermListItem.STATE_ONLINE:
//                        return "Online";
//                    case TermListItem.STATE_BUSY:
//                        return "Busy";
//                    case TermListItem.STATE_AWAY:
//                        return "Away";
//                    case TermListItem.STATE_OFFLINE:
//                        return "Offline";
//                }
//            }
//        }
//
//        return null;
//
//    }
    private Color getColor(String itemName) {
        String name = null;
        TermListItem item;
        for (int i = 0; i < values.size(); i++) {
            item = values.get(i);
            name = item.getTermSuggest().getTerm();
            if (itemName.equals(name)) {
                Color color = (item.isIsSelected()) ? selected : nonSelected;
                return color;
            }
        }

        return null;

    }

    private ImageIcon getImageIcon(String itemName) {
        String name = null;
        TermListItem item;
        for (int i = 0; i < values.size(); i++) {
            item = values.get(i);
            name = item.getTermSuggest().getTerm();
            if (itemName.equals(name)) {
                ImageIcon icon = (item.isIsSelected()) ? defaultImageDisabled : defaultImage;
                return icon;
            }
        }

        return null;

    }

    public Component getListCellRendererComponent(JList list,
            Object value, // value to display
            int index, // cell index
            boolean isSelected, // is the cell selected
            boolean cellHasFocus) { // the list and the cell have the focus

        TermListItem item = (TermListItem) value;
        String s = item.getTermSuggest().getTerm();
        setText(s);


        if (isShowIcon) {
            ImageIcon icon = getImageIcon(s);
            setIcon(icon);
        }

        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(getColor(s));
        }
        setFont(list.getFont());
        setOpaque(true);

        return this;
    }

    public int getHeightIcon() {
        return heightIcon;
    }

    public void setHeightIcon(int heightIcon) {
        this.heightIcon = heightIcon;
    }

    public int getWidthIcon() {
        return widthIcon;
    }

    public void setWidthIcon(int widthIcon) {
        this.widthIcon = widthIcon;
    }

    /**
     *
     * @return
     */
    public ImageIcon getDefaultImage() {
        return defaultImage;
    }

    /**
     *
     * @param defaultImage
     */
    public void setDefaultImage(ImageIcon defaultImage) {
        this.defaultImage = defaultImage;
    }
}
