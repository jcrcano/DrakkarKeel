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

/**
 * Esta clase contiene datos referentes a una evaluación realizada a un documento
 */
public class Markup {

    private String user;
    private int relevance;
    private String comment;

    /**
     *
     * @param user
     * @param relevance
     * @param comment
     */
    public Markup(String user, int relevance, String comment) {
        this.user = user;
        this.relevance = relevance;
        this.comment = comment;
    }
    



    /**
     * Get the value of user
     *
     * @return the value of user
     */
    public String getUser() {
        return user;
    }

    /**
     * Set the value of user
     *
     * @param user new value of user
     */
    public void setUser(String user) {
        this.user = user;
    }

    /**
     * Get the value of relevance
     *
     * @return the value of relevance
     */
    public int getRelevance() {
        return relevance;
    }

    /**
     * Set the value of relevance
     *
     * @param relevance new value of relevance
     */
    public void setRelevance(int relevance) {
        this.relevance = relevance;
    }

    /**
     * Get the value of comment
     *
     * @return the value of comment
     */
    public String getComment() {
        return comment;
    }

    /**
     * Set the value of comment
     *
     * @param comment new value of comment
     */
    public void setComment(String comment) {
        this.comment = comment;
    }
}
