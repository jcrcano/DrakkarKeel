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

import java.util.ArrayList;
import java.util.List;

public class TermDocSuggest implements java.io.Serializable {
    
     private static final long serialVersionUID = 70000000000021L;

    private List<TermSuggest> terms;
    private List<DocSuggest> docs;

    /**
     *
     */
    public TermDocSuggest() {
        this.terms = new ArrayList<>();
        this.docs = new ArrayList<>();
    }

    public TermDocSuggest(List<TermSuggest> terms, List<DocSuggest> docs) {
        this.terms = terms;
        this.docs = docs;
    }



    /**
     *
     * @param t
     */
    public void addTermSuggest(TermSuggest t){
        this.terms.add(t);

    }
    /**
     *
     * @param t
     * @return
     */
    public boolean  removeTermSuggest(TermSuggest t){
        return this.terms.remove(t);

    }

    /**
     *
     * @param t
     */
    public void addDocSuggest(DocSuggest t){
        this.docs.add(t);

    }
     /**
      *
      * @param t
      * @return
      */
     public boolean  removeDocSuggest(DocSuggest t){
        return this.docs.remove(t);

    }

     /**
      *
      * @return
      */
     public List<DocSuggest> getDocs() {
        return docs;
    }

    /**
     *
     * @param docs
     */
    public void setDocs(List<DocSuggest> docs) {
        this.docs = docs;
    }

    /**
     *
     * @return
     */
    public List<TermSuggest> getTerms() {
        return terms;
    }

    /**
     *
     * @param terms
     */
    public void setTerms(List<TermSuggest> terms) {
        this.terms = terms;
    }

    

}
