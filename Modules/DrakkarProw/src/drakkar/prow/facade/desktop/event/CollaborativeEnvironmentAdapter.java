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

/**
 * Esta interfaz se encargará de notificarán los eventos del entorno colaborativo
 */
public abstract class CollaborativeEnvironmentAdapter implements CollaborativeEnvironmentListener {

    /**
     * {@inheritDoc}
     */
    public void notifyAvailableCollabSession(CollaborativeEnvironmentEvent evt) {
    }

    /**
     * {@inheritDoc}
     */
    public void notifyCollabSessionCreation(CollaborativeEnvironmentEvent evt) {
    }

    /**
     * {@inheritDoc}
     */
    public void notifyChairmanSetting(CollaborativeEnvironmentEvent evt) {
    }

    /**
     * {@inheritDoc}
     */
    public void notifyCollabSessionDeleted(CollaborativeEnvironmentEvent evt) {
    }

    /**
     * {@inheritDoc}
     */
    public void notifyCollabSessionEnding(CollaborativeEnvironmentEvent evt) {
    }

    /**
     * {@inheritDoc}
     */
    public void notifyCollabSessionAuthentication(CollaborativeEnvironmentEvent evt) {
    }

    /**
     * {@inheritDoc}
     */
    public void notifyCollabSessionAcceptance(CollaborativeEnvironmentEvent evt) {
    }

    /**
     * {@inheritDoc}
     */
    public void notifyActionTrack(CollaborativeEnvironmentEvent evt) {
    }

     /**
     * {@inheritDoc}
     */
    public void notify(FacadeDesktopEvent evt) {
    }
}
