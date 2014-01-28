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
public abstract class ProwAdapter implements ProwListener {

    /**
     * {@inheritDoc}
     */
    public void notifySeekerEvent(SeekerEvent evt) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * {@inheritDoc}
     */
    public void notifyRequestConnection(SeekerEvent evt) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * {@inheritDoc}
     */
    public void notifyServerState(ServerEvent evt) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * {@inheritDoc}
     */
    public void notifyCommitTransaction(TransactionEvent evt) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * {@inheritDoc}
     */
    public void notify(FacadeDesktopEvent evt) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * {@inheritDoc}
     */
    public void notifyCloseConnection(SeekerEvent evt) {
    }

    /**
     * {@inheritDoc}
     * @deprecated
     */
    public void notify(DesktopEvent evt) {
//                throw new UnsupportedOperationException("Not supported yet.");
    }
}
