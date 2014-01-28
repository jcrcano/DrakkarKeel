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

public class SeekerCommentsData {

    private String user;
    private String comment;

    /**
     *
     * @param user
     * @param comment
     */
    public SeekerCommentsData(String user, String comment) {
        this.user = user;
        this.comment = comment;
    }

    /**
     *
     * @return
     */
    public String getComment() {
        return comment;
    }

    /**
     *
     * @param comment
     */
    public void setComment(String comment) {
        this.comment = comment;
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
