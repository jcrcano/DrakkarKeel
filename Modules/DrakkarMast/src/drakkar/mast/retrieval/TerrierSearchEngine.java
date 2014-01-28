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
 * motor de búsqueda de RI Terrier versión 2.1
 *
 * 
 */
public class TerrierSearchEngine extends SearchEngine {
    
    TerrierContext terrierContext;

    /**
     *constructor
     */
    public TerrierSearchEngine() {
        this.terrierContext = new TerrierContext();

    }

    /**
     *
     * @param listener
     */
    public TerrierSearchEngine(FacadeDesktopListener listener) {
        this.terrierContext = new TerrierContext(listener);
    }

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
        return KeySearchable.TERRIER_SEARCH_ENGINE;
    }


    public String getName() {
        return "Terrier";
    }

    public Contextable getContext() {
        return terrierContext;
    }
}

