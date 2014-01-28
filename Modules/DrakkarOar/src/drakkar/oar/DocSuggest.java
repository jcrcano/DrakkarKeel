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

public class DocSuggest implements java.lang.Cloneable, java.io.Serializable, Comparable<DocSuggest> {

    private static final long serialVersionUID = 70000000000004L;
    
    private String name;
    private String path;
    private double score;

    /**
     *
     * @param name
     * @param path
     * @param score
     */
    public DocSuggest(String name, String path, double score) {
        this.name = name;
        this.path = path;
        this.score = score;
    }

  /**
     * Get the value of name
     *
     * @return the value of name
     */
    public String getName() {
        return name;
    }

    /**
     * Set the value of name
     *
     * @param name new value of name
     */
    public void setName(String name) {
        this.name = name;
    }

     /**
     * Get the value of path
     *
     * @return the value of path
     */
    public String getPath() {
        return path;
    }

    /**
     * Set the value of path
     *
     * @param path new value of path
     */
    public void setPath(String path) {
        this.path = path;
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


     public int compareTo(DocSuggest o) {
        int lastCmp = Double.compare(o.score,score);
        return lastCmp;
    }

    @Override
    public String toString() {
        return "DocSuggest{" + "name=" + name + " path=" + path + " score=" + score + '}';
    }




}
