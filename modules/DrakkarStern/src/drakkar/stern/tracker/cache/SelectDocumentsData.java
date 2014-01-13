/*
 * DrakkarKeel - An Enterprise Collaborative Search Platform
 *
 * The contents of this file are subject under the terms described in the
 * DRAKKARKEEL_LICENSE file included in this distribution; you may not use this
 * file except in compliance with the License. 
 *
 * 2013-2014 DrakkarKeel Platform.
 */

package drakkar.stern.tracker.cache;

public class SelectDocumentsData {

    private byte relevance;
    private String user;
    

    /**
     *
     * @param relevance
     * @param user
     */
    public SelectDocumentsData(byte relevance, String user) {
        this.relevance = relevance;
        this.user = user;
    }

    /**
     *
     * @return
     */
    public byte getRelevance() {
        return relevance;
    }

    /**
     *
     * @param relevance
     */
    public void setRelevance(byte relevance) {
        this.relevance = relevance;
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




}
