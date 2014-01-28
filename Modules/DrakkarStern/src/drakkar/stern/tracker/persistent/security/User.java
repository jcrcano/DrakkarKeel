/*
 * DrakkarKeel - An Enterprise Collaborative Search Platform
 *
 * The contents of this file are subject under the terms described in the
 * DRAKKARKEEL_LICENSE file included in this distribution; you may not use this
 * file except in compliance with the License. 
 *
 * 2013-2014 DrakkarKeel Platform.
 */
package drakkar.stern.tracker.persistent.security;

/**
 *
 * Clase que representa un usuario para la autenticación en una BD de Derby
 */
public class User {

    /** Name of the user to authenticate */
    private String userName;
    /** Password of user */
    private String password;
    /** Access level */
    private String userPrivilege;

    /**
     * 
     */
    public User() {
    }

    /**
     *
     * @param userName
     * @param password
     * @param userPrivilege
     */
    public User(String userName, String password, String userPrivilege) {
        this.userName = userName;
        this.password = password;
        this.userPrivilege = userPrivilege;
    }

    /**
     *
     * @return
     */
    public String getPassword() {
        return password;
    }

    /**
     *
     * @return
     */
    public String getUserName() {
        return userName;
    }

    /**
     *
     * @return
     */
    public String getUserPrivilege() {
        return userPrivilege;
    }

    /**
     *
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     *
     * @param userName
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     *
     * @param userPrivilege
     */
    public void setUserPrivilege(String userPrivilege) {
        this.userPrivilege = userPrivilege;
    }
}
