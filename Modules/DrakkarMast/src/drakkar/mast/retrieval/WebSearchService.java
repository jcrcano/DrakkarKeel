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
import drakkar.mast.SearchException;
import java.util.ArrayList;

public abstract class WebSearchService implements Searchable {

    protected boolean enabled = true;

    /**
     * {@inheritDoc}
     */
    public ArrayList<DocumentMetaData> search(String query, boolean caseSensitive) throws SearchException {
        ArrayList<DocumentMetaData> results = ((WebServiceContextable)this.getContext()).search(query, caseSensitive);
        return results;
    }

    /**
     * {@inheritDoc}
     */
    public ArrayList<DocumentMetaData> search(String query, String docType, boolean caseSensitive) throws SearchException {
        ArrayList<DocumentMetaData> results = ((WebServiceContextable)this.getContext()).search(query, docType, caseSensitive);
        return results;
    }

     /**
     * {@inheritDoc}
     */
    public ArrayList<DocumentMetaData> search(String query, String[] docTypes, boolean caseSensitive) throws SearchException {
        ArrayList<DocumentMetaData> results = ((WebServiceContextable)this.getContext()).search(query, docTypes, caseSensitive);
        return results;
    }

   

    /**
     * {@inheritDoc}
     */
    public void setEnabled(boolean flag) {
        this.enabled = flag;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isEnabled() {
        return this.enabled;
    }
}
