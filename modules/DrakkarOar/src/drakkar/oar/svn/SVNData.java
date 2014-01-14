/*
 * DrakkarKeel - An Enterprise Collaborative Search Platform
 *
 * The contents of this file are subject under the terms described in the
 * DRAKKARKEEL_LICENSE file included in this distribution; you may not use this
 * file except in compliance with the License. 
 *
 * 2013-2014 DrakkarKeel Platform.
 */
package drakkar.oar.svn;

/**
 * Clase que contiene los datos necesarios para la indexación de documentos en repositorios SVN
 */
public class SVNData {

    String url;
    String names;
    String user;
    String password;
    String directPath;
    String indexPath;
    String mergeFactor;

    /**
     *
     */
    public SVNData() {
    }

    
    /**
     * Constructor de la clase
     *
     * @param url             url del repositorio
     * @param names           nombre (s) de los repositorio (s)
     * @param user            nombre del usuario para autenticarse al repositorio
     * @param password        contraseña del usuario para autenticarse al repositorio
     * @param directPath      camino directo al repositorio
     * @param indexPath       dirección del índice
     * @param mergeFactor     
     */
    public SVNData(String url, String names, String user, String password, String directPath, String indexPath, String mergeFactor) {
        this.url = url;
        this.names = names;
        this.user = user;
        this.password = password;
        this.directPath = directPath;
        this.indexPath = indexPath;
        this.mergeFactor = mergeFactor;
    }

    /**
     *
     * @return
     */
    public String getDirectPath() {
        return directPath;
    }

    /**
     *
     * @param directPath
     */
    public void setDirectPath(String directPath) {
        this.directPath = directPath;
    }

    /**
     *
     * @return
     */
    public String getIndexPath() {
        return indexPath;
    }

    /**
     *
     * @param indexPath
     */
    public void setIndexPath(String indexPath) {
        this.indexPath = indexPath;
    }

    /**
     *
     * @return
     */
    public String getNames() {
        return names;
    }

    /**
     *
     * @param names
     */
    public void setNames(String names) {
        this.names = names;
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
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     *
     * @return
     */
    public String getUrl() {
        return url;
    }

    /**
     *
     * @param url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     *
     * @return
     */
    public String getUser() {
        return user;
    }

    /**
     *
     * @param user
     */
    public void setUser(String user) {
        this.user = user;
    }

    /**
     * 
     * @return
     */
    public String getMergeFactor() {
        return mergeFactor;
    }

    /**
     *
     * @param mergeFactor
     */
    public void setMergeFactor(String mergeFactor) {
        this.mergeFactor = mergeFactor;
    }

    

}
