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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A class that represents the application main window.
 */
public class CollectionInfo implements java.io.Serializable {

    private Map<TermInfo, List<DocTermInfo>> termsMap;
    private int singularValue;
    private String searcherName;
    private List<DocInfo> docsInfo;
    private List<String> terms;

    /**
     *
     */
    public CollectionInfo() {
        this.termsMap = new HashMap<TermInfo, List<DocTermInfo>>();
        this.singularValue = 0;
        this.searcherName = "";
        this.docsInfo = new ArrayList<DocInfo>();
        this.terms = new ArrayList<String>();
    }

    /**
     *
     * @param termsMap
     * @param searcherName
     * @param terms
     * @param docsInfo
     * @param singularValue 
     */
    public CollectionInfo(Map<TermInfo, List<DocTermInfo>> termsMap, String searcherName, List<String> terms, List<DocInfo> docsInfo, int singularValue) {
        this.termsMap = termsMap;
        this.searcherName = searcherName;
        this.docsInfo = docsInfo;
        this.terms = terms;
        this.singularValue = singularValue;
    }

    /**
     * @return the termsMap
     */
    public Map<TermInfo, List<DocTermInfo>> getTermsMap() {
        return termsMap;
    }

    /**
     * @param termsMap the termsMap to set
     */
    public void setTermsMap(Map<TermInfo, List<DocTermInfo>> termsMap) {
        this.termsMap = termsMap;
    }

    /**
     * @return the termCount
     */
    public int getTermCount() {
        return terms.size();
    }

   

    /**
     * @return the docsCount
     */
    public int getDocsCount() {
        return docsInfo.size();
    }

   
    /**
     * @return the searcherName
     */
    public String getSearcherName() {
        return searcherName;
    }

    /**
     * @param searcherName the searcherName to set
     */
    public void setSearcherName(String searcherName) {
        this.searcherName = searcherName;
    }

    /**
     *
     * @return
     */
    public List<DocInfo> getDocsInfo() {
        return docsInfo;
    }

    /**
     * 
     * @param docsInfo
     */
    public void setDocsInfo(List<DocInfo> docsInfo) {
        this.docsInfo = docsInfo;
    }

    /**
     *
     * @return
     */
    public List<String> getTerms() {
        return this.terms;
    }

    /**
     *
     * @return
     */
    public List<DocInfo> getDocs() {
        return this.docsInfo;
    }

    

    /**
     *
     * @return
     */
    public int getSingularValue() {
        return this.singularValue;
    }

}
