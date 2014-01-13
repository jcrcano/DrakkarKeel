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

import drakkar.oar.util.KeySearchable;

public class YahooWebSearchService extends WebSearchService{
    YahooContext context;


     /**
     * {@inheritDoc}
     */
    public Searchable getSearchable() {
        return this;
    }

     /**
     * {@inheritDoc}
     */
    public int getID() {
        return KeySearchable.YAHOO_WEB_SEARCH_SERVICE;
    }

    public String getName() {
       return "Yahoo";
    }

    public Contextable getContext() {
        return context;
    }

}
