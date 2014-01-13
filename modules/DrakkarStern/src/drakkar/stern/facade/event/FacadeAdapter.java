/*
 * DrakkarKeel - An Enterprise Collaborative Search Platform
 *
 * The contents of this file are subject under the terms described in the
 * DRAKKARKEEL_LICENSE file included in this distribution; you may not use this
 * file except in compliance with the License. 
 *
 * 2013-2014 DrakkarKeel Platform.
 */
package drakkar.stern.facade.event;

import drakkar.oar.facade.event.FacadeDesktopEvent;


/**
 * Esta clase constituye un adaptador de la clase FacadeEvent
 */
public abstract class FacadeAdapter implements FacadeListener {
    
    /**
     * {@inheritDoc}
     */
    public void notify(FacadeDesktopEvent evt) {
    }

    /**
     * {@inheritDoc}
     */
    public void notifyDoneSearch() {
    }

    /**
     * {@inheritDoc}
     */
    public void notifyFoundDocument() {
    }

    /**
     * {@inheritDoc}
     */
    public void notifySentMessage() {
    }

    /**
     * {@inheritDoc}
     */
    public void notifyDoneRecommendation() {
    }
}

