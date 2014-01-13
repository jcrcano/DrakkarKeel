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
 * Esta clase contiene los datos referidos a las métricas de evaluación para una consulta en una sesión
 */
public class EvaluationData {

    
    String session;
    String query;
    float precision;
    float recall;

    /**
     * 
     */
    public EvaluationData() {
    }

    /**
     *
     * @param session
     * @param query
     * @param precision
     * @param recall
     */
    public EvaluationData(String session, String query, float precision, float recall) {

        this.session = session;
        this.query = query;
        this.precision = precision;
        this.recall = recall;


    }

   

    
    /**
     *
     * @return
     */
    public float getPrecision() {
        return precision;
    }

    /**
     * 
     * @param precision
     */
    public void setPrecision(float precision) {
        this.precision = precision;
    }

    /**
     *
     * @return
     */
    public String getQuery() {
        return query;
    }

    /**
     *
     * @param query
     */
    public void setQuery(String query) {
        this.query = query;
    }

    /**
     *
     * @return
     */
    public float getRecall() {
        return recall;
    }

    /**
     *
     * @param recall
     */
    public void setRecall(float recall) {
        this.recall = recall;
    }

    /**
     *
     * @return
     */
    public String getSession() {
        return session;
    }

    /**
     *
     * @param session
     */
    public void setSession(String session) {
        this.session = session;
    }



}
