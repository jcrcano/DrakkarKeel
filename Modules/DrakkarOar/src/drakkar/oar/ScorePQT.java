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

public class ScorePQT implements Serializable{
     private static final long serialVersionUID = 70000000000014L;
    private int agree;
    private int disagree;

    /**
     *
     */
    public ScorePQT() {
        this.agree = 0;
        this.disagree = 0;
    }

    /**
     *
     * @param agree
     * @param disagree
     */
    public ScorePQT(int agree, int disagree) {
        this.agree = agree;
        this.disagree = disagree;
    }

        /**
     * Get the value of disagree
     *
     * @return the value of disagree
     */
    public int getDisagree() {
        return disagree;
    }

    /**
     * Set the value of disagree
     *
     * @param disagree new value of disagree
     */
    public void setDisagree(int disagree) {
        this.disagree = disagree;
    }


    /**
     * Get the value of agree
     *
     * @return the value of agree
     */
    public int getAgree() {
        return agree;
    }

    /**
     * Set the value of agree
     *
     * @param agrre new value of agree
     */
    public void setAgree(int agrre) {
        this.agree = agrre;
    }




}
