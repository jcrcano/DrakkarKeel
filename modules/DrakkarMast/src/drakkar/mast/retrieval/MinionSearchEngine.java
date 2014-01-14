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
 * motor de búsqueda Minion 1.0
 */
public class MinionSearchEngine extends AdvSearchEngine {

    MinionContext minionContext;

    /**
     *
     */
    public MinionSearchEngine() {
        minionContext = new MinionContext();
    }

    /**
     *
     * @param listener
     */
    public MinionSearchEngine(FacadeDesktopListener listener) {
        minionContext = new MinionContext(listener);
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
        return KeySearchable.MINION_SEARCH_ENGINE;
    }

    public String getName() {
        return "Minion";
    }

    public Contextable getContext() {
        return minionContext;
    }
}
