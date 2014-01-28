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

/**
 * A class that represents the application main window.
 */
public class DocTermInfo implements java.io.Serializable  {
    private int docNum;
    private int termFreq;

    public DocTermInfo(int docNum, int termFreq) {
        this.docNum = docNum;
        this.termFreq = termFreq;
       
    }

    /**
     * @return the docNum
     */
    public int getDocNum() {
        return docNum;
    }

    /**
     * @param docNum the docNum to set
     */
    public void setDocNum(int docNum) {
        this.docNum = docNum;
    }

    /**
     * @return the termFreq
     */
    public int getTermFreq() {
        return termFreq;
    }

    /**
     * @param termFreq the termFreq to set
     */
    public void setTermFreq(int termFreq) {
        this.termFreq = termFreq;
    }

    @Override
    public String toString() {
        return "DocTermInfo{" + "docNum=" + docNum + " termFreq=" + termFreq + '}';
    }


   
}
