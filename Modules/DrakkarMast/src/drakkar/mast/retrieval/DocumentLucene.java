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
import org.apache.lucene.document.*;

public class DocumentLucene extends DocumentMetaData {

    private Document doc;

    /**
     * 
     */
    public DocumentLucene() {
        doc = new Document();
    }

    /**
     * Método para adicionar un campo al índice
     * 
     * @param name   nombre del campo
     * @param value  valor del campo
     * @return
     */
    public Document addField(String name, String value) {

        if (value != null) {
            Field f1 = new Field(name, value, Field.Store.YES, Field.Index.ANALYZED);
            getDoc().add(f1);
        } else {
            Field f1 = new Field(name, " ", Field.Store.YES, Field.Index.ANALYZED);
            getDoc().add(f1);
        }

        return getDoc();
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
