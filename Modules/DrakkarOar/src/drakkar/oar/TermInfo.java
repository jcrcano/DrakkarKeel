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

/**
 * A class that represents the application main window.
 */
public class TermInfo implements java.lang.Cloneable, java.io.Serializable, Comparable<TermInfo> {

    private static final long serialVersionUID = 70000000000022L;
    private String term;
    private int id, absTermFrec;

    /**
     *
     * @param id
     * @param term
     * @param absTermFrec
     */
    public TermInfo(int id ,String term, int absTermFrec) {
        this.id = id;
        this.term = term;
        this.absTermFrec = absTermFrec;
    }

    
    /**
     * @return the term
     */
    public String getTerm() {
        return term;
    }

    /**
     * @param term the term to set
     */
    public void setTerm(String term) {
        this.term = term;
    }

    /**
     * @return the absTermFrec
     */
    public int getAbsTermFrec() {
        return absTermFrec;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }



    @Override
    public String toString() {
        return "\nTermInfo{" + "term=" + term + "}\n";
    }


     public int compareTo(TermInfo o) {
         if (id < o.id) {
             return -1;
         }else if (id > o.id) {
             return 1;
         }
        
        return 0;
    }

}
