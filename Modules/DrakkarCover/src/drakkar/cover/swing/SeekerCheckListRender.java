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
import drakkar.oar.util.ImageUtil;
import java.awt.Color;
import java.awt.Component;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

public class SeekerCheckListRender extends JLabel implements ListCellRenderer {

    private ArrayList<SeekerSelectable> values;
    private int widthIcon;
    private int heightIcon;
    private ImageIcon defaultImage;

    /**
     * Constructor por defecto de la clase
     */
    public SeekerCheckListRender() {
        super();
        this.values = new ArrayList<>();
        this.defaultImage = new ImageIcon(SeekerCheckListRender.class.getResource("/drakkar/cover/resources/user16.png"));
        this.heightIcon = 16;
        this.widthIcon = 16;
    }

    /**
     * Constructor de la clase
     *
     * @param seekersList lista de usuarios
     */
    public SeekerCheckListRender(ArrayList<Seeker> seekersList) {
        super();
        this.values = new ArrayList<>();
        for (Seeker seeker : seekersList) {
            this.values.add(new SeekerSelectable(seeker));
        }
        this.defaultImage = new ImageIcon(SeekerCheckListRender.class.getResource("/drakkar/cover/resources/user16.png"));
        this.heightIcon = 16;
        this.widthIcon = 16;
    }

    /**
     * Adiciona un usuario
     *
     * @param seeker usuario
     */
    public void add(Seeker seeker) {
        this.values.add(new SeekerSelectable(seeker));
    }

    /**
     * Adiciona un usuario
     *
     * @param index 
     * @param seeker usuario
     */
    public void add(int index, Seeker seeker) {
        this.values.add(index, new SeekerSelectable(seeker));
    }

    /**
     * Devuelve la lista de usuarios
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
     * Modifica la lista de usurios
     *
     * @param seekersList nueva lista de usurios
     */
    public void setValues(ArrayList<Seeker> seekersList) {
        this.values.clear();

        for (Seeker seeker : seekersList) {
            this.values.add(new SeekerSelectable(seeker));
        }
    }

    /**
     * Modifica el valor del usuario
     * @param seeker
     * @return 
     */
    public boolean update(Seeker seeker) {
        int index = this.values.indexOf(new SeekerSelectable(seeker));
        if (index >= 0) {
            this.values.set(index, new SeekerSelectable(seeker));
            return true;
        }
        return false;
    }

    /**
     * Modifica el valor del usuario
     * 
     * @param index
     * @param seeker
     */
    public void set(int index, Seeker seeker) {
        this.values.set(index, new SeekerSelectable(seeker));
    }

    /**
     * Elimina un usuario de la lista de usuarios
     *
     * @param seeker usuario
     */
    public void remove(Seeker seeker) {
        this.values.remove(new SeekerSelectable(seeker));
    }

    /**
     *
     * @param seeker
     * @return
     */
    public boolean contains(Seeker seeker) {
        return this.values.contains(new SeekerSelectable(seeker));
    }

    /**
     * Elimina todos los usuarios
     */
    public void clear() {
        this.values.clear();
    }

    // This is the only method defined by ListCellRenderer.
    // We just reconfigure the JLabel each time we're called.
    private String getState(String value) {
        String name = null;
        Seeker seeker;
        for (int i = 0; i < values.size(); i++) {
            seeker = (values.get(i)).getSeeker();
            name = seeker.getUser();
            if (value.equals(name)) {
                int state = seeker.getState();
                switch (state) {
                    case Seeker.STATE_ONLINE:
                        return "Online";
                    case Seeker.STATE_BUSY:
                        return "Busy";
                    case Seeker.STATE_AWAY:
                        return "Away";
                    case Seeker.STATE_OFFLINE:
                        return "Offline";
                }
            }
        }

        return null;

    }

    private Color getColor(String seekerName) {
        String name = null;
        Seeker seeker;
        for (int i = 0; i < values.size(); i++) {
            seeker = (values.get(i)).getSeeker();
            name = seeker.getUser();
            if (seekerName.equals(name)) {
                Color color = findColor(seeker.getState());
                return color;
            }
        }

        return null;

    }

    private Color findColor(int state) {
        Color color = null;

        switch (state) {
            case Seeker.STATE_ONLINE:
                color = new Color(11, 199, 141);
                break;
            case Seeker.STATE_BUSY:
                color = new Color(205, 133, 8);
                break;
            case Seeker.STATE_AWAY:
                color = new Color(59, 216, 242);
                break;
            case Seeker.STATE_OFFLINE:
                color = new Color(192, 192, 192);
                break;

        }

        return color;


    }

    public Component getListCellRendererComponent(JList list,
            Object value, // value to display
            int index, // cell index
            boolean isSelected, // is the cell selected
            boolean cellHasFocus) { // the list and the cell have the focus

        Seeker seeker = ((SeekerSelectable) value).getSeeker();
        String s = seeker.getUser();
        setText(s);
        ImageIcon icon = null;
        try {
//            icon = new ImageIcon(ImageUtil.toBufferedImage(seeker.getAvatar()));
            BufferedImage resized1 = ImageUtil.getFasterScaledInstance(ImageUtil.makeBufferedImage(new ImageIcon(ImageUtil.toBufferedImage(seeker.getAvatar())).getImage()), 16, 16, java.awt.RenderingHints.VALUE_INTERPOLATION_BILINEAR, false);
            icon = new ImageIcon(resized1);
        } catch (Exception e) {
            e.printStackTrace();
        }


        if (icon != null && icon.getIconWidth() > this.widthIcon && icon.getIconHeight() > this.heightIcon) {
            BufferedImage resized = ImageUtil.getFasterScaledInstance(ImageUtil.makeBufferedImage(icon.getImage()), this.widthIcon, this.heightIcon, java.awt.RenderingHints.VALUE_INTERPOLATION_BILINEAR, false);
            icon = new ImageIcon(resized);
            seeker.setAvatar(ImageUtil.toByte(resized));
        } else if (icon == null) {
            System.out.println("---------------------------------icon null");
            icon = defaultImage;
        }


        // setIcon(this.resizeImage(icon, this.widthIcon, this.heightIcon));
        setIcon(icon);
        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(getColor(s));
        }
        setToolTipText(getState(s));
        setEnabled(((SeekerSelectable) value).isEnabled());
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
