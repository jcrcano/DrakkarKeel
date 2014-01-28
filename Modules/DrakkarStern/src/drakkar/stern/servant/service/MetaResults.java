/*
 * DrakkarKeel - An Enterprise Collaborative Search Platform
 *
 * The contents of this file are subject under the terms described in the
 * DRAKKARKEEL_LICENSE file included in this distribution; you may not use this
 * file except in compliance with the License. 
 *
 * 2013-2014 DrakkarKeel Platform.
 */
package drakkar.stern.servant.service;

import drakkar.oar.DocumentMetaData;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The <code>ResponseUtilFactory</code> class is.....
 * Esta clase contiene la relación de índice-metadocument
 */
public class MetaResults {

    Map<Integer, DocumentMetaData> metaResults;

    /**
     *
     */
    public MetaResults() {
        metaResults = new HashMap<>();
    }

    /**
     *
     * @param metaResults
     */
    public MetaResults(Map<Integer, DocumentMetaData> metaResults) {
        this.metaResults = metaResults;
    }

    /**
     * 
     * @param i
     * @return
     */
    public DocumentMetaData get(int i) {
        return metaResults.get(i);
    }

    /**
     *
     * @param item
     *
     * @return
     */
    public boolean add(DocumentMetaData item) {
        int id = item.getIndex();

        if (metaResults.containsKey(id)) {
            return false;
        } else {
            metaResults.put(id, item);
            return true;
        }

    }

    public void add(List<DocumentMetaData> items) {

        int id = 0;
        for (DocumentMetaData metaDocument : items) {
            id = metaDocument.getIndex();
            this.add(metaDocument);
        }
    }

    /**
     *
     * @return
     */
    public Map<Integer, DocumentMetaData> getMetaResults() {
        return metaResults;
    }

    /**
     *
     * @param metaResults
     */
    public void setMetaResults(Map<Integer, DocumentMetaData> metaResults) {
        this.metaResults = metaResults;
    }
}
