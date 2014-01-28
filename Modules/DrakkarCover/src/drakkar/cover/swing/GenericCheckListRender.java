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

import drakkar.oar.util.ImageUtil;
import java.awt.Component;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

public class GenericCheckListRender extends JLabel implements ListCellRenderer {
    // TODO: encontrar met resize más eficiente, validar con imágenes h=0 y w=0(Error),
    // si la icon tiene las dimensiones establecidas no redimensionar

    private ArrayList<ItemSelectable> values;
    private int widthIcon;
    private int heightIcon;
    private ImageIcon defaultImage;

    /**
     * Constructor por defecto de la clase
     */
    public GenericCheckListRender() {
        super();
        this.values = new ArrayList<>();
        this.defaultImage = new ImageIcon();
        this.heightIcon = 16;
        this.widthIcon = 16;
        
    }

    /**
     * Constructor de la clase
     *
     * @param values lista de objetos
     */
    public GenericCheckListRender(ArrayList<ItemSelectable> values) {
        super();
        this.values = new ArrayList<>();
        for (ItemSelectable item : values) {
            this.values.add(item);
        }
        this.defaultImage = new ImageIcon();
        this.heightIcon = 16;
        this.widthIcon = 16;
    }

    /**
     * Adiciona un objeto
     *
     * @param item objeto
     */
    public void add(ItemSelectable item) {
        this.values.add(item);
    }

    /**
     * Adiciona un objeto
     *
     * @param index
     * @param item objeto
     */
    public void add(int index, ItemSelectable item) {
        this.values.add(index, item);
    }

    /**
     * Devuelve la lista de objetos
     *
     * @return objetos
     */
    public ArrayList<ItemSelectable> getValues() {


        return this.values;
    }

    /**
     * Modifica la lista de objetos
     *
     * @param values nueva lista de objetos
     */
    public void setValues(ArrayList<ItemSelectable> values) {
        this.values = values;

    }

    /**
     * Actualiza el valor del objeto
     *
     * @param item objeto
     *
     * @return true si se modifico el objeto, false en caso contrario
     */
    public boolean update(ItemSelectable item) {
        int index = this.values.indexOf(item);
        if (index >= 0) {
            this.values.set(index, item);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Actualiza el valor del objeto
     *
     * @param index
     * @param item objeto
     *
     *
     */
    public void set(int index, ItemSelectable item) {
        this.values.set(index, item);

    }

    /**
     * Elimina el objeto de la lista de objetos del render
     *
     * @param item usuario
     */
    public void remove(ItemSelectable item) {
        this.values.remove(item);
    }

    /**
     * 
     * @param item
     * @return
     */
    public boolean contains(ItemSelectable item){
        return this.values.contains(item);
    }

    /**
     * Elimina todos los objetos del modelo
     */
    public void clear() {
        this.values.clear();
    }

    /**
     *
     * @return
     */
    public int getHeightIcon() {
        return heightIcon;
    }

    /**
     * Modifica el valor de la altura de los iconos a mostrar
     *
     * @param heightIcon nueva altura
     */
    public void setHeightIcon(int heightIcon) {
        this.heightIcon = heightIcon;
    }

    /**
     * Devuelve el valor de la altura de los iconos a mostrar. Por defecto 16x16 pixel
     *
     * @return altura
     */
    public int getWidthIcon() {
        return widthIcon;
    }

    /**
     * Modifica el valor del ancho de los iconos a mostrar
     *
     * @param widthIcon
     */
    public void setWidthIcon(int widthIcon) {
        this.widthIcon = widthIcon;
    }

    /**
     * Devuelve la imagen del icono por defecto
     *
     * @return icono
     */
    public ImageIcon getDefaultImage() {
        return defaultImage;
    }

    /**
     * Modifica la imagen del icono por defecto 
     * 
     * @param defaultImage
     */
    public void setDefaultImage(ImageIcon defaultImage) {
        this.defaultImage = defaultImage;
    }

    public Component getListCellRendererComponent(JList list,
            Object value, // value to display
            int index, // cell index
            boolean isSelected, // is the cell selected
            boolean cellHasFocus) { // the list and the cell have the focus

        ItemSelectable item = ((ItemSelectable) value);
        String s = item.getLabel();

        setText(s);
        ImageIcon icon = null;
        try {
            icon = item.getIcon();

            if (icon != null && icon.getIconWidth() > this.widthIcon && icon.getIconHeight() > this.heightIcon) {
                BufferedImage resized = ImageUtil.getFasterScaledInstance(ImageUtil.makeBufferedImage(icon.getImage()), this.widthIcon, this.heightIcon, java.awt.RenderingHints.VALUE_INTERPOLATION_BILINEAR, false);
                icon = new ImageIcon(resized);
                item.setIcon(icon);
            } else if (icon == null) {
                icon = defaultImage;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        setIcon(icon);

        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(item.getColor());
        }
        setToolTipText(item.getToolTip());

        setEnabled(((ItemSelectable) value).isEnabled());
        setFont(list.getFont());

        setOpaque(true);

        return this;
    }

    

}
