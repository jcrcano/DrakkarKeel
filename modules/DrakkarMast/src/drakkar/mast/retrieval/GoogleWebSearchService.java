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

/**
 * Esta clase representas los métodos de búsquedas soportados por el servicio
 * web de búsqueda Google
 *
 * 
 */
public class GoogleWebSearchService extends WebSearchService {

    GoogleContext context;

    

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
        return KeySearchable.GOOGLE_WEB_SEARCH_SERVICE;
    }

     /**
     * {@inheritDoc}
     */
    public String getName() {
        return "Google";
    }

    public Contextable getContext() {
        return context;
    }

   


}
