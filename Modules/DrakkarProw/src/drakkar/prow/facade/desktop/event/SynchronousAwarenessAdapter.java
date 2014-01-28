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

import drakkar.oar.facade.event.FacadeDesktopEvent;

public abstract class SynchronousAwarenessAdapter implements SynchronousAwarenessListener {

    /**
     * {@inheritDoc}
     */
    public void notifyPuttingQueryTermsTogether(SynchronousAwarenessEvent evt) {
    }

    /**
     * {@inheritDoc}
     */
    public void notifyQueryChange(SynchronousAwarenessEvent evt) {
    }

    /**
     * {@inheritDoc}
     */
    public void notifyQueryTyped(SynchronousAwarenessEvent evt) {
    }

    /**
     * {@inheritDoc}
     */
    public void notifyQueryTermAcceptance(SynchronousAwarenessEvent evt) {
    }

    /**
     * {@inheritDoc}
     */
    public void notify(FacadeDesktopEvent evt) {
    }
}
