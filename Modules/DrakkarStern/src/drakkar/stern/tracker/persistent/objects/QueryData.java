/*
 * DrakkarKeel - An Enterprise Collaborative Search Platform
 *
 * The contents of this file are subject under the terms described in the
 * DRAKKARKEEL_LICENSE file included in this distribution; you may not use this
 * file except in compliance with the License. 
 *
 * 2013-2014 DrakkarKeel Platform.
 */
package drakkar.stern.tracker.persistent.objects;

import java.util.ArrayList;

/**
 * Esta clase contiene los datos de las búsquedas realizadas
 */
public class QueryData {

    private int id;
    private String query;
    private String date;
    private String principle;
    private String user;
    private ArrayList<String> engines;

    
    public QueryData(int idQuery, String text, String queryDate, String selectedPrinciple, String seeker, ArrayList<String> searchEng) {
        id = idQuery;
        query = text;
        date = queryDate;
        principle = selectedPrinciple;
        user = seeker;
        engines = searchEng;
    }

    /**
     * @return the query
     */
    public String getQuery() {
        return query;
    }

    /**
     * @param query the query to set
     */
    public void setQuery(String query) {
        this.query = query;
    }

    /**
     * @return the date
     */
    public String getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * @return the principle
     */
    public String getPrinciple() {
        return principle;
    }

    /**
     * @param principle the principle to set
     */
    public void setPrinciple(String principle) {
        this.principle = principle;
    }

    /**
     * @return the user
     */
    public String getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(String user) {
        this.user = user;
    }

    /**
     * @return the engines
     */
    public ArrayList<String> getEngines() {
        return engines;
    }

    /**
     * @param engines the engines to set
     */
    public void setEngines(ArrayList<String> engines) {
        this.engines = engines;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }
}
