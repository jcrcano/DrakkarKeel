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

import drakkar.oar.facade.event.FacadeDesktopListener;
import drakkar.oar.util.KeySearchable;

/**
 * Esta clase representa la indexación y búsqueda realizada por el
 * motor de búsqueda de RI Apache Lucene versión 3.0.0
 *
 *
 */
public class LuceneSearchEngine extends AdvSearchEngine {

    
    LuceneContext luceneContext;

    /**
     *
     */
    public LuceneSearchEngine() {
        luceneContext = new LuceneContext();
    }

     /**
     *
     * @param listener
     */
    public LuceneSearchEngine(FacadeDesktopListener listener) {
        this.luceneContext = new LuceneContext(listener);
    }
    /**
     * {@inheritDoc}
     */
    

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
        return KeySearchable.LUCENE_SEARCH_ENGINE;
    }

    public String getName() {
        return "Apache Lucene";
    }

    public Contextable getContext() {
        return luceneContext;
    }
}

