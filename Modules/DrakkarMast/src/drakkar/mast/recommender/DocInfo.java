/*
 * DrakkarKeel - An Enterprise Collaborative Search Platform
 *
 * The contents of this file are subject under the terms described in the
 * DRAKKARKEEL_LICENSE file included in this distribution; you may not use this
 * file except in compliance with the License. 
 *
 * 2013-2014 DrakkarKeel Platform.
 */
package drakkar.mast.recommender;

public class DocInfo implements java.io.Serializable  {

    private String name;
    private String filePath;

    public DocInfo(String name, String filePath) {
        this.name = name;
        this.filePath = filePath;
    }
    

    /**
     * Get the value of filePath
     *
     * @return the value of filePath
     */
    public String getFilePath() {
        return filePath;
    }

    /**
     * Set the value of filePath
     *
     * @param filePath new value of filePath
     */
    public void setFilePath(String filePath) {
        this.filePath = filePath;
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

    @Override
    public String toString() {
        return "DocInfo{" + "name=" + name + " filePath=" + filePath + '}';
    }




}
