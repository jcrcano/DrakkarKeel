/*
 * DrakkarKeel - An Enterprise Collaborative Search Platform
 *
 * The contents of this file are subject under the terms described in the
 * DRAKKARKEEL_LICENSE file included in this distribution; you may not use this
 * file except in compliance with the License. 
 *
 * 2013-2014 DrakkarKeel Platform.
 */
package drakkar.prow.facade.desktop.event;

import drakkar.oar.facade.event.DesktopEvent;
import drakkar.oar.facade.event.FacadeDesktopEvent;

/**
 * A class that represents the application main window.
 */
public abstract class SearchAdapter implements SearchListener {

    /**
     * {@inheritDoc}
     */
    public void notifySearchResults(SearchEvent evt) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * {@inheritDoc}
     */
    public void notifyAvailableSearchers(SearchEvent evt) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * {@inheritDoc}
     */
    public void notifyAvailableSVNRepositories(SearchEvent evt) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * {@inheritDoc}
     */
     public void notifyAvailableSearchPrinciples(SearchEvent evt){
     }

    /**
     * {@inheritDoc}
     */
    public void notify(FacadeDesktopEvent evt) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * {@inheritDoc}
     * @deprecated
     *
     */
    public void notify(DesktopEvent evt) {
//                throw new UnsupportedOperationException("Not supported yet.");
    }
}
