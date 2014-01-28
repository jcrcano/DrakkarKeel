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
 * Esta clase contiene los datos de una sesión de búsqueda
 */
public class SessionData {

    
    private String topic;
    private String descrip;
    private String startDate;
    private String stopDate;
    private int membership;
    private String chairman = null;
    private boolean criteria;
    private int maxMember;
    private int minMember;
    private int current = 0;
    private boolean enable;

    /**
     * 
     * @param topic
     * @param descrip
     * @param criteria
     * @param maxMember
     * @param minMember
     * @param current
     * @param startDate
     * @param stopDate
     * @param membership
     * @param chairman
     * @param enable
     */
    public SessionData(String topic, String descrip, boolean criteria, int maxMember, int minMember, int current, String startDate, String stopDate, int membership, String chairman, boolean enable) {

        
        this.topic = topic;
        this.descrip = descrip;
        this.startDate = startDate;
        this.stopDate = stopDate;
        this.membership = membership;
        this.chairman = chairman;
        this.criteria = criteria;
        this.maxMember = maxMember;
        this.minMember = minMember;
        this.current = current;
        this.enable = enable;

    }

    /**
     * @return the topic
     */
    public String getTopic() {
        return topic;
    }

    /**
     * @param topic the topic to set
     */
    public void setTopic(String topic) {
        this.topic = topic;
    }

    /**
     * @return the descrip
     */
    public String getDescrip() {
        return descrip;
    }

    /**
     * @param descrip the descrip to set
     */
    public void setDescrip(String descrip) {
        this.descrip = descrip;
    }

    /**
     * @return the startDate
     */
    public String getStartDate() {
        return startDate;
    }

    /**
     * @param startDate the startDate to set
     */
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    /**
     * @return the stopDate
     */
    public String getStopDate() {
        return stopDate;
    }

    /**
     * @param stopDate the stopDate to set
     */
    public void setStopDate(String stopDate) {
        this.stopDate = stopDate;
    }

    /**
     * @return the membership
     */
    public int getMembership() {
        return membership;
    }

    /**
     * @param membership the membership to set
     */
    public void setMembership(int membership) {
        this.membership = membership;
    }

    /**
     * @return the chairman
     */
    public String getChairman() {
        return chairman;
    }

    /**
     * @param chairman the chairman to set
     */
    public void setChairman(String chairman) {
        this.chairman = chairman;
    }

    /**
     * @return the criteria
     */
    public boolean isCriteria() {
        return criteria;
    }

    /**
     * @param criteria the criteria to set
     */
    public void setCriteria(boolean criteria) {
        this.criteria = criteria;
    }

    /**
     * @return the maxMember
     */
    public int getMaxMember() {
        return maxMember;
    }

    /**
     * @param maxMember the maxMember to set
     */
    public void setMaxMember(int maxMember) {
        this.maxMember = maxMember;
    }

    /**
     * @return the minMember
     */
    public int getMinMember() {
        return minMember;
    }

    /**
     * @param minMember the minMember to set
     */
    public void setMinMember(int minMember) {
        this.minMember = minMember;
    }

    /**
     * @return the current
     */
    public int getCurrent() {
        return current;
    }

    /**
     * @param current the current to set
     */
    public void setCurrent(int current) {
        this.current = current;
    }

    /**
     * @return the enable
     */
    public boolean isEnable() {
        return enable;
    }

    /**
     * @param enable the enable to set
     */
    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    @Override
    public String toString() {
        return this.topic;
    }


}
