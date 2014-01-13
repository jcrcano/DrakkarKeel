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

import java.io.Serializable;
import java.util.List;

public class ResponseConnection implements Serializable{
    
    private static final long serialVersionUID = 70000000000010L;

    private String sessionName;
    private List<Seeker> seekers;
    private String[] searchers;
    private List<Session> persistentSession;
    private List<String> searchPrinciples;
    private List<Session> openSessions;

    /**
     * 
     * @param sessionName
     * @param seekers
     * @param searchers
     * @param persistentSession
     * @param searchPrinciples
     * @param openSessions
     */
    public ResponseConnection(String sessionName, List<Seeker> seekers, String[] searchers, List<Session> persistentSession, List<String> searchPrinciples, List<Session> openSessions) {
        this.sessionName = sessionName;
        this.seekers = seekers;
        this.searchers = searchers;
        this.persistentSession = persistentSession;
        this.searchPrinciples = searchPrinciples;
        this.openSessions = openSessions;
    }
    
    

    /**
     * Get the value of seekers
     *
     * @return the value of seekers
     */
    public List<Seeker> getSeekers() {
        return seekers;
    }

    /**
     * Set the value of seekers
     *
     * @param seekers new value of seekers
     */
    public void setSeekers(List<Seeker> seekers) {
        this.seekers = seekers;
    }
 
    

    /**
     * Get the value of openSessions
     *
     * @return the value of openSessions
     */
    public List<Session> getOpenSessions() {
        return openSessions;
    }

    /**
     * Set the value of openSessions
     *
     * @param openSessions new value of openSessions
     */
    public void setOpenSessions(List<Session> openSessions) {
        this.openSessions = openSessions;
    }

    /**
     * Get the value of searchPrinciples
     *
     * @return the value of searchPrinciples
     */
    public List<String> getSearchPrinciples() {
        return searchPrinciples;
    }

    /**
     * Set the value of searchPrinciples
     *
     * @param searchPrinciples new value of searchPrinciples
     */
    public void setSearchPrinciples(List<String> searchPrinciples) {
        this.searchPrinciples = searchPrinciples;
    }


    /**
     * Get the value of persistentSession
     *
     * @return the value of persistentSession
     */
    public List<Session> getPersistentSession() {
        return persistentSession;
    }

    /**
     * Set the value of persistentSession
     *
     * @param persistentSession new value of persistentSession
     */
    public void setPersistentSession(List<Session> persistentSession) {
        this.persistentSession = persistentSession;
    }


    /**
     * Get the value of searchers
     *
     * @return the value of searchers
     */
    public String[] getSearchers() {
        return searchers;
    }

    /**
     * Set the value of searchers
     *
     * @param searchers new value of searchers
     */
    public void setSearchers(String[] searchers) {
        this.searchers = searchers;
    }

    /**
     * Get the value of sessionName
     *
     * @return the value of sessionName
     */
    public String getSessionName() {
        return sessionName;
    }

    /**
     * Set the value of sessionName
     *
     * @param sessionName new value of sessionName
     */
    public void setSessionName(String sessionName) {
        this.sessionName = sessionName;
    }
}
