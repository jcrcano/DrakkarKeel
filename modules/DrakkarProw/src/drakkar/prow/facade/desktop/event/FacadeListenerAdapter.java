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

public abstract class FacadeListenerAdapter implements ProwListener, TextMessageListener,
        SearchListener, CollaborativeEnvironmentListener, ExplicitRecommendationListener,
        SynchronousAwarenessListener, ImplicitRecommendationListener {

    /**
     * {@inheritDoc} 
     */
    public void notifySeekerEvent(SeekerEvent evt) {
    }

    /**
     * {@inheritDoc}
     */
    public void notifyRequestConnection(SeekerEvent evt) {
    }

    /**
     * {@inheritDoc}
     */
    public void notifyServerState(ServerEvent evt) {
    }

    /**
     * {@inheritDoc}
     */
    public void notifyCommitTransaction(TransactionEvent evt) {
    }

    /**
     * {@inheritDoc}
     */
    public void notify(FacadeDesktopEvent evt) {
    }

    /**
     * {@inheritDoc}
     */
    public void notifyTextMessage(TextMessageEvent evt) {
    }

    /**
     * {@inheritDoc}
     */
    public void notifySearchResults(SearchEvent evt) {
    }

    /**
     * {@inheritDoc}
     */
    public void notifyAvailableSearchers(SearchEvent evt) {
    }

    /**
     * {@inheritDoc}
     */
    public void notifyAvailableSearchPrinciples(SearchEvent evt) {
    }

    /**
     * {@inheritDoc}
     */
    public void notifyAvailableSVNRepositories(SearchEvent evt) {
    }

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
    public void notifyRecommendation(ExplicitRecommendationEvent evt) {
    }

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
     public void notifyCollabTermsRecommendation(ImplicitRecommendationEvent evt){
     }

    /**
     * {@inheritDoc}
     */
    public void notifyQueryTermsRecommendation(ImplicitRecommendationEvent evt) {
    }

    /**
     * {@inheritDoc}
     */
    public void notifyCloseConnection(SeekerEvent evt) {
    }
}
