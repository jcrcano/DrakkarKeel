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

public class TermSuggest implements java.lang.Cloneable, java.io.Serializable, Comparable<TermSuggest>{
    private static final long serialVersionUID = 70000000000023L;
    
    private String term;
    private double score;

    /**
     *
     * @param term
     * @param score
     */
    public TermSuggest(String term, double score) {
        this.term = term;
        this.score = score;
    }



    /**
     * Get the value of term
     *
     * @return the value of term
     */
    public String getTerm() {
        return term;
    }

    /**
     * Set the value of term
     *
     * @param term new value of term
     */
    public void setTerm(String term) {
        this.term = term;
    }

    /**
     * Get the value of score
     *
     * @return the value of score
     */
    public double getScore() {
        return score;
    }

    /**
     * Set the value of score
     *
     * @param score new value of score
     */
    public void setScore(double score) {
        this.score = score;
    }

     public int compareTo(TermSuggest o) {
        int lastCmp = Double.compare(o.score,score);
        return lastCmp;
    }

    @Override
    public String toString() {
        return "TermSuggest{" + "term=" + term + "score=" + score + '}';
    }

     
}
