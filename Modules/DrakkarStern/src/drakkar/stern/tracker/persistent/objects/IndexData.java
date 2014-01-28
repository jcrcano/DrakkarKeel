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

/**
 * Esta clase contiene los datos de los índices creados
 */
public class IndexData {

    
    private String uri;
    private String date;
    private String searchEngine;
    private int count;

    public IndexData(String uri, String date, String searchEngine, int count) {
        this.uri = uri;
        this.date = date;
        this.searchEngine = searchEngine;
        this.count = count;

    }

    /**
     * @return the address
     */
    public String getAddress() {
        return uri;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.uri = address;
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
     * @return the searchEngine
     */
    public String getSearchEngine() {
        return searchEngine;
    }

    /**
     * @param searchEngine the searchEngine to set
     */
    public void setSearchEngine(String searchEngine) {
        this.searchEngine = searchEngine;
    }

    /**
     * @return the count
     */
    public int getCount() {
        return count;
    }

    /**
     * @param count the count to set
     */
    public void setCount(int count) {
        this.count = count;
    }
}
