/*
 * DrakkarKeel - An Enterprise Collaborative Search Platform
 *
 * The contents of this file are subject under the terms described in the
 * DRAKKARKEEL_LICENSE file included in this distribution; you may not use this
 * file except in compliance with the License. 
 *
 * 2013-2014 DrakkarKeel Platform.
 */
package drakkar.oar;

import drakkar.oar.util.ImageUtil;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;

/**
 * Esta clase representa todos los atributos que se manejan de los clientes durante
 * las sesión de comunicación del servidor
 *
 *
 */
public class Seeker implements java.lang.Cloneable, java.io.Serializable, Comparable<Seeker> {

    private static final long serialVersionUID = 70000000000017L;
    /**
     * Rol de miembro de una sesión colaborativa de busqueda
     */
    public static final int ROLE_MEMBER = 1;
    /**
     * Rol de miembro póntencial de una sesión colaborativa de busqueda
     */
    public static final int ROLE_POTENTIAL_MEMBER = 2;
    /**
     * Rol de jefe de una sesión colaborativa de busqueda
     */
    public static final int ROLE_CHAIRMAN = 3;
    /**
     * En linea. Estado del usuario
     */
    public static final int STATE_ONLINE = 1;
    /**
     * Desconectado. Estado del usuario
     */
    public static final int STATE_OFFLINE = 2;
    /**
     * Lejos. Estado del usuario
     */
    public static final int STATE_AWAY = 3;
    /**
     * Trabajando. Estado del usuario
     */
    public static final int STATE_BUSY = 4;
    private String userName;
    private int role;
    private int state;
    private byte[] avatar;

    /**
     * Constructor por defecto de la clase
     */
    public Seeker() {

        this.userName = System.getProperty("user.name");
        this.role = ROLE_POTENTIAL_MEMBER;
        this.state = STATE_ONLINE;
        this.avatar = getAvatarArray();
    }

    private byte[] getAvatarArray() {
        ImageIcon icon = new ImageIcon(Seeker.class.getResource("/drakkar/oar/resources/user16.jpg"));
        BufferedImage bf = ImageUtil.makeBufferedImage(icon.getImage());

        byte[] array = ImageUtil.toByte(bf);
        return array;
    }

    /**
     * * Constructor de la clase
     * 
     * @param userName     nombre
     * @param role         rol
     * @param state        estado
     * @param avatar       foto
     */
    public Seeker(String userName, int role, int state, byte[] avatar) {
        this.userName = userName;
        this.role = role;
        this.state = state;
        this.avatar = avatar;
    }

    /**
     * Devuelve la imagen del Seeker
     *
     * @return imagen
     */
    public byte[] getAvatar() {
        if (avatar == null) {
            this.avatar = getAvatarArray();
        }

        return avatar;

    }

    /**
     * Modifica la image del Seeker
     *
     * @param avatar nuevo avatar
     */
    public void setAvatar(byte[] avatar) {
        this.avatar = avatar;
    }

    /**
     * Decvuelve el nombre del Seeker
     *
     * @return nombre
     */
    public String getUser() {
        return userName;
    }

    /**
     * Modifica el nombre del Seeker
     *
     * @param userName
     */
    public void setUser(String userName) {
        this.userName = userName;
    }

    /**
     * Develve el rol del Seeker
     *
     * @return rol
     */
    public int getRole() {
        return role;
    }

    /**
     * Modifica el rol del Seeker
     *
     * @param role nuevo rol
     */
    public void setRole(int role) {
        this.role = role;
    }

    /**
     * Devuelve el estado del Seeker
     *
     * @return estado
     */
    public int getState() {
        return state;
    }

    /**
     * Mdifica el estado del Seeker
     *
     * @param state nuevo estado
     */
    public void setState(int state) {
        this.state = state;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Seeker other = (Seeker) obj;

        if ((this.userName == null) ? (other.userName != null) : !this.userName.equals(other.userName)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;

        hash = 97 * hash + (this.userName != null ? this.userName.hashCode() : 0);
        return hash;
    }

    public int compareTo(Seeker o) {
        int lastCmp = this.userName.compareTo(o.userName);
        return lastCmp;
    }

    @Override
    public String toString() {
        return this.userName;
    }
}
