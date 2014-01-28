/*
 * DrakkarKeel - An Enterprise Collaborative Search Platform
 *
 * The contents of this file are subject under the terms described in the
 * DRAKKARKEEL_LICENSE file included in this distribution; you may not use this
 * file except in compliance with the License. 
 *
 * 2013-2014 DrakkarKeel Platform.
 */
package drakkar.mast.retrieval;

import drakkar.oar.DocumentMetaData;
import com.sun.labs.minion.Document;
import com.sun.labs.minion.SearchEngine;
import com.sun.labs.minion.SimpleIndexer;

public class DocumentMinion extends DocumentMetaData{

    Document doc;
    SimpleIndexer sgeneral;

    public DocumentMinion(SimpleIndexer sind, String key) {
        sgeneral = sind;
        sgeneral.startDocument(key);
        
    }

    public DocumentMinion(SearchEngine eng, String key) {
        doc = eng.createDocument(key);
       
    }

    public void addField(String name, String value) {        
        if (value != null) {            
            sgeneral.addField(name, value);
            //sgeneral.addTerm(value);
        } else {
            sgeneral.addField(name, " ");
        }
    }

    public void closeDocument() {
        sgeneral.endDocument();
    }

    /**
     * @return the doc
     */
    public Document getDoc() {
        return doc;
    }

    /**
     * @param doc the doc to set
     */
    public void setDoc(Document doc) {
        this.doc = doc;
    }


}
