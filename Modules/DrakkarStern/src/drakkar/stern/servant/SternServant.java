/*
 * DrakkarKeel - An Enterprise Collaborative Search Platform
 *
 * The contents of this file are subject under the terms described in the
 * DRAKKARKEEL_LICENSE file included in this distribution; you may not use this
 * file except in compliance with the License. 
 *
 * 2013-2014 DrakkarKeel Platform.
 */
package drakkar.stern.servant;

import drakkar.oar.DocumentMetaData;
import drakkar.oar.Documents;
import drakkar.oar.QuerySource;
import drakkar.oar.ScorePQT;
import drakkar.oar.Seeker;
import drakkar.oar.SessionProperty;
import drakkar.oar.exception.AwarenessException;
import drakkar.oar.exception.QueryNotExistException;
import drakkar.oar.exception.RecommendationException;
import drakkar.oar.exception.SeekerException;
import drakkar.oar.exception.SessionException;
import drakkar.oar.exception.TrackException;
import drakkar.oar.slice.client.ClientSidePrx;
import drakkar.mast.RetrievalManager;
import drakkar.mast.SearchException;
import drakkar.mast.SearchableException;
import drakkar.stern.SternAppSetting;
import drakkar.stern.controller.DataBaseController;
import drakkar.stern.facade.event.FacadeListener;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class SternServant extends Servant {

    
    /**
     *
     * @param retrievalManager
     */
    public  SternServant(RetrievalManager retrievalManager) {
        super(retrievalManager);


    }

    public  SternServant(RetrievalManager retrievalManager, SternAppSetting setting) {
        super(retrievalManager, setting);
    }

    /**
     *
     * @param retrievalManager
     */
    public   SternServant(RetrievalManager retrievalManager, DataBaseController dbController) {
        super(retrievalManager, dbController);
    }

    /**
     *
     * @param retrievalManager
     * @param listener
     * @param dbController
     */
    public SternServant(RetrievalManager retrievalManager, FacadeListener listener, DataBaseController dbController) {
        super(retrievalManager, listener, dbController);
    }

    /**
     *
     * @param retrievalManager
     * @param listener
     * @param dbController
     */
    public SternServant(RetrievalManager retrievalManager, FacadeListener listener, DataBaseController dbController, SternAppSetting setting) {
        super(retrievalManager, listener, dbController, setting);
    }

  
     


    /**
     * {@inheritDoc}
     */
    public void setConnection(Connection cxn) {
        this.cxn = cxn;
    }

    /**
     * {@inheritDoc}
     */
    public void setSessionUUID(String sessionUUID) {
        this.sessionUUID = sessionUUID;
    }

    /**
     * {@inheritDoc}
     */
    public void setUUIDClass(UUID uuid) {
        this.uuid = uuid;
    }

    /**
     *
     * @return
     */
    public FacadeListener getListener() {
        return listener;
    }

    /**
     *
     * @param listener
     */
    public void setListener(FacadeListener listener) {
        this.listener = listener;
    }

    /**
     * {@inheritDoc}
     */
    public synchronized void sendMessage(String sessionName, Seeker emitter, String message) throws SessionException, SeekerException, IOException {
        this.messenger.sendMessage(sessionName, emitter, message);
    }

    /**
     * {@inheritDoc}
     */
    public synchronized void sendMessage(String sessionName, Seeker emitter, Seeker receptor, String message) throws SessionException, SeekerException, IOException {
        this.messenger.sendMessage(sessionName, emitter, receptor, message);
    }

    /**
     * {@inheritDoc}
     */
    public synchronized void sendMessage(String sessionName, Seeker emitter, List<Seeker> receptors, String message) throws SessionException, SeekerException, IOException {
        this.messenger.sendMessage(sessionName, emitter, receptors, message);
    }

    /**
     * {@inheritDoc}
     */
    public synchronized void recommendResults(String sessionName, Seeker emitter, String comments, QuerySource data) throws SessionException, SeekerException, RecommendationException, IOException {
        this.recommend.recommendResults(sessionName, emitter, comments, data);
    }

    /**
     * {@inheritDoc}
     */
    public synchronized void recommendResults(String sessionName, Seeker emitter, Seeker receptor, String comments, QuerySource data) throws SessionException, SeekerException, RecommendationException, IOException {
        this.recommend.recommendResults(sessionName, emitter, receptor, comments, data);
    }

    /**
     * {@inheritDoc}
     */
    public synchronized void recommendResults(String sessionName, Seeker emitter, String comments, Documents docs, QuerySource data) throws SessionException, SeekerException, RecommendationException, IOException {
        this.recommend.recommendResults(sessionName, emitter, comments, docs, data);
    }

    /**
     * {@inheritDoc}
     */
    public synchronized void recommendResults(String sessionName, Seeker emitter, Seeker receptor, Documents docs, String comments, QuerySource data) throws SessionException, SeekerException, RecommendationException, IOException {
        this.recommend.recommendResults(sessionName, emitter, receptor, docs, comments, data);
    }

    /**
     * {@inheritDoc}
     */
    public synchronized void recommendResults(String sessionName, Seeker emitter, List<Seeker> receptors, String comments, QuerySource data) throws SessionException, SeekerException, RecommendationException, IOException {
        this.recommend.recommendResults(sessionName, emitter, receptors, comments, data);
    }

    /**
     * {@inheritDoc}
     */
    public synchronized void recommendResults(String sessionName, Seeker emitter, List<Seeker> receptors, Documents docs, String comments, QuerySource data) throws SessionException, SeekerException, RecommendationException, IOException {
        this.recommend.recommendResults(sessionName, emitter, receptors, docs, comments, data);
    }

    /**
     * {@inheritDoc}
     */
    public synchronized void recommendResults(String sessionName, Seeker emitter, String sessionNameRtr, Seeker receptor, String comments, QuerySource data) throws SessionException, SeekerException, RecommendationException, IOException {
        this.recommend.recommendResults(sessionName, emitter, sessionNameRtr, receptor, comments, data);
    }

    /**
     * {@inheritDoc}
     */
    public synchronized void recommendResults(String sessionName, Seeker emitter, String sessionNameRtrs, List<Seeker> receptors, String comments, QuerySource data) throws SessionException, SeekerException, RecommendationException, IOException {
        this.recommend.recommendResults(sessionName, emitter, sessionNameRtrs, receptors, comments, data);
    }

    /**
     * {@inheritDoc}
     */
    public synchronized void recommendResults(String sessionName, Seeker emitter, String sessionNameRtr, Seeker receptor, Documents docs, String comments, QuerySource data) throws SessionException, SeekerException, RecommendationException, IOException {
        this.recommend.recommendResults(sessionName, emitter, sessionNameRtr, receptor, docs, comments, data);
    }

    /**
     * {@inheritDoc}
     */
    public synchronized void recommendResults(String sessionName, Seeker emitter, String sessionNameRtrs, List<Seeker> receptors, Documents docs, String comments, QuerySource data) throws SessionException, SeekerException, RecommendationException, IOException {
        this.recommend.recommendResults(sessionName, emitter, sessionNameRtrs, receptors, docs, comments, data);
    }

    ///////////////////END*******Métodos Utilitarios de las búsquedas*****END//////////////////
    /**
     * {@inheritDoc}
     */
    public synchronized void executeSearch(String sessionName, String query, int searcher, int principle, boolean caseSensitive, Seeker seeker, ClientSidePrx seekerPrx) throws SessionException, SeekerException, SearchableException, SearchException, IOException {
        this.searcher.executeSearch(sessionName, query, searcher, principle, caseSensitive, seeker, seekerPrx);
    }

    /**
     * {@inheritDoc}
     */
    public synchronized void executeSearch(String sessionName, String query, int field, int searcher, int principle, boolean caseSensitive, Seeker seeker, ClientSidePrx seekerPrx) throws SessionException, SeekerException, SearchableException, SearchException, IOException {
        this.searcher.executeSearch(sessionName, query, field, searcher, principle, caseSensitive, seeker, seekerPrx);
    }

    /**
     * {@inheritDoc}
     */
    public synchronized void executeSearch(String sessionName, String query, int[] fields, int searcher, int principle, boolean caseSensitive, Seeker seeker, ClientSidePrx seekerPrx) throws SessionException, SeekerException, SearchableException, SearchException, IOException {
        this.searcher.executeSearch(sessionName, query, fields, searcher, principle, caseSensitive, seeker, seekerPrx);
    }

    /**
     * {@inheritDoc}
     */
    public void executeSearch(String sessionName, String query, String docType, int searcher, int principle, boolean caseSensitive, Seeker seeker, ClientSidePrx seekerPrx) throws SessionException, SeekerException, SearchableException, SearchException, IOException {
        this.searcher.executeSearch(sessionName, query, docType, searcher, principle, caseSensitive, seeker, seekerPrx);
    }

    /**
     * {@inheritDoc}
     */
    public void executeSearch(String sessionName, String query, String[] docTypes, int searcher, int principle, boolean caseSensitive, Seeker seeker, ClientSidePrx seekerPrx) throws SessionException, SeekerException, SearchableException, SearchException, IOException {
        this.searcher.executeSearch(sessionName, query, docTypes, searcher, principle, caseSensitive, seeker, seekerPrx);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("fallthrough")
    public synchronized void executeSearch(String sessionName, String query, String docType, int field, int searcher, int principle, boolean caseSensitive, Seeker seeker, ClientSidePrx seekerPrx) throws SessionException, SeekerException, SearchableException, SearchException, IOException {
        this.searcher.executeSearch(sessionName, query, docType, field, searcher, principle, caseSensitive, seeker, seekerPrx);
    }

    /**
     * {@inheritDoc}
     */
    public void executeSearch(String sessionName, String query, String[] docTypes, int field, int searcher, int principle, boolean caseSensitive, Seeker seeker, ClientSidePrx seekerPrx) throws SessionException, SeekerException, SearchableException, SearchException, IOException {
        this.searcher.executeSearch(sessionName, query, docTypes, field, searcher, principle, caseSensitive, seeker, seekerPrx);
    }

    /**
     * {@inheritDoc}
     */
    public void executeSearch(String sessionName, String query, String docType, int[] fields, int searcher, int principle, boolean caseSensitive, Seeker seeker, ClientSidePrx seekerPrx) throws SessionException, SeekerException, SearchableException, SearchException, IOException {
        this.searcher.executeSearch(sessionName, query, docType, fields, searcher, principle, caseSensitive, seeker, seekerPrx);
    }

    /**
     * {@inheritDoc}
     */
    public void executeSearch(String sessionName, String query, String[] docTypes, int[] fields, int searcher, int principle, boolean caseSensitive, Seeker seeker, ClientSidePrx seekerPrx) throws SessionException, SeekerException, SearchableException, SearchException, IOException {
        this.searcher.executeSearch(sessionName, query, docTypes, fields, searcher, principle, caseSensitive, seeker, seekerPrx);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * {@inheritDoc}
     */
    public void executeSearch(String sessionName, int principle, String query, boolean caseSensitive, Seeker seeker, ClientSidePrx seekerPrx) throws SessionException, SeekerException, SearchException, IOException {
        this.searcher.executeSearch(sessionName, principle, query, caseSensitive, seeker, seekerPrx);
    }

    /**
     * {@inheritDoc}
     */
    public void executeSearch(String sessionName, int principle, String query, int field, boolean caseSensitive, Seeker seeker, ClientSidePrx seekerPrx) throws SessionException, SeekerException, SearchException, IOException {
        this.searcher.executeSearch(sessionName, principle, query, field, caseSensitive, seeker, seekerPrx);
    }

    /**
     * {@inheritDoc}
     */
    public void executeSearch(String sessionName, int principle, String query, int[] fields, boolean caseSensitive, Seeker seeker, ClientSidePrx seekerPrx) throws SessionException, SeekerException, SearchException, IOException {
        this.searcher.executeSearch(sessionName, principle, query, fields, caseSensitive, seeker, seekerPrx);
    }

    /**
     * {@inheritDoc}
     */
    public void executeSearch(String sessionName, int principle, String query, String docType, boolean caseSensitive, Seeker seeker, ClientSidePrx seekerPrx) throws SessionException, SeekerException, SearchException, IOException {
        this.searcher.executeSearch(sessionName, principle, query, docType, caseSensitive, seeker, seekerPrx);
    }

    /**
     * {@inheritDoc}
     */
    public void executeSearch(String sessionName, int principle, String query, String[] docTypes, boolean caseSensitive, Seeker seeker, ClientSidePrx seekerPrx) throws SessionException, SeekerException, SearchException, IOException {
        this.searcher.executeSearch(sessionName, principle, query, docTypes, caseSensitive, seeker, seekerPrx);
    }

    /**
     * {@inheritDoc}
     */
    public void executeSearch(String sessionName, int principle, String query, String docType, int field, boolean caseSensitive, Seeker seeker, ClientSidePrx seekerPrx) throws SessionException, SeekerException, SearchException, IOException {
        this.searcher.executeSearch(sessionName, principle, query, docType, field, caseSensitive, seeker, seekerPrx);
    }

    /**
     * {@inheritDoc}
     */
    public void executeSearch(String sessionName, int principle, String query, String docType, int[] fields, boolean caseSensitive, Seeker seeker, ClientSidePrx seekerPrx) throws SessionException, SeekerException, SearchException, IOException {
        this.searcher.executeSearch(sessionName, principle, query, docType, fields, caseSensitive, seeker, seekerPrx);
    }

    /**
     * {@inheritDoc}
     */
    public void executeSearch(String sessionName, int principle, String query, String[] docTypes, int field, boolean caseSensitive, Seeker seeker, ClientSidePrx seekerPrx) throws SessionException, SeekerException, SearchException, IOException {
        this.searcher.executeSearch(sessionName, principle, query, docTypes, field, caseSensitive, seeker, seekerPrx);
    }

    /**
     * {@inheritDoc}
     */
    public void executeSearch(String sessionName, int principle, String query, String[] docTypes, int[] fields, boolean caseSensitive, Seeker seeker, ClientSidePrx seekerPrx) throws SessionException, SeekerException, SearchException, IOException {
        this.searcher.executeSearch(sessionName, principle, query, docTypes, fields, caseSensitive, seeker, seekerPrx);
    }

    /**
     * {@inheritDoc}
     */
    public void executeSearch(String sessionName, int principle, int[] searchers, String query, boolean caseSensitive, Seeker seeker, ClientSidePrx seekerPrx) throws SessionException, SeekerException, SearchableException, SearchException, IOException {
        this.searcher.executeSearch(sessionName, principle, searchers, query, caseSensitive, seeker, seekerPrx);
    }

    /**
     * {@inheritDoc}
     */
    public void executeSearch(String sessionName, int principle, int[] searchers, String query, int field, boolean caseSensitive, Seeker seeker, ClientSidePrx seekerPrx) throws SessionException, SeekerException, SearchableException, SearchException, IOException {
        this.searcher.executeSearch(sessionName, principle, searchers, query, field, caseSensitive, seeker, seekerPrx);
    }

    /**
     * {@inheritDoc}
     */
    public void executeSearch(String sessionName, int principle, int[] searchers, String query, int[] fields, boolean caseSensitive, Seeker seeker, ClientSidePrx seekerPrx) throws SessionException, SeekerException, SearchableException, SearchException, IOException {
        this.searcher.executeSearch(sessionName, principle, searchers, query, fields, caseSensitive, seeker, seekerPrx);
    }

    /**
     * {@inheritDoc}
     */
    public void executeSearch(String sessionName, int principle, int[] searchers, String query, String docType, boolean caseSensitive, Seeker seeker, ClientSidePrx seekerPrx) throws SessionException, SeekerException, SearchableException, SearchException, IOException {
        this.searcher.executeSearch(sessionName, principle, searchers, query, docType, caseSensitive, seeker, seekerPrx);
    }

    /**
     * {@inheritDoc}
     */
    public void executeSearch(String sessionName, int principle, int[] searchers, String query, String[] docTypes, boolean caseSensitive, Seeker seeker, ClientSidePrx seekerPrx) throws SessionException, SeekerException, SearchableException, SearchException, IOException {
        this.searcher.executeSearch(sessionName, principle, searchers, query, docTypes, caseSensitive, seeker, seekerPrx);
    }

    /**
     * {@inheritDoc}
     */
    public void executeSearch(String sessionName, int principle, int[] searchers, String query, String docType, int field, boolean caseSensitive, Seeker seeker, ClientSidePrx seekerPrx) throws SessionException, SeekerException, SearchableException, SearchException, IOException {
        this.searcher.executeSearch(sessionName, principle, searchers, query, docType, field, caseSensitive, seeker, seekerPrx);
    }

    /**
     * {@inheritDoc}
     */
    public void executeSearch(String sessionName, int principle, int[] searchers, String query, String docType, int[] fields, boolean caseSensitive, Seeker seeker, ClientSidePrx seekerPrx) throws SessionException, SeekerException, SearchableException, SearchException, IOException {
        this.searcher.executeSearch(sessionName, principle, searchers, query, docType, fields, caseSensitive, seeker, seekerPrx);
    }

    /**
     * {@inheritDoc}
     */
    public void executeSearch(String sessionName, int principle, int[] searchers, String query, String[] docTypes, int field, boolean caseSensitive, Seeker seeker, ClientSidePrx seekerPrx) throws SessionException, SeekerException, SearchableException, SearchException, IOException {
        this.searcher.executeSearch(sessionName, principle, searchers, query, docTypes, field, caseSensitive, seeker, seekerPrx);
    }

    /**
     * {@inheritDoc}
     */
    public void executeSearch(String sessionName, int principle, int[] searchers, String query, String[] docTypes, int[] fields, boolean caseSensitive, Seeker seeker, ClientSidePrx seekerPrx) throws SessionException, SeekerException, SearchableException, SearchException, IOException {
        this.searcher.executeSearch(sessionName, principle, searchers, query, docTypes, fields, caseSensitive, seeker, seekerPrx);
    }
    
    //svn search
    public void executeSearch(String sessionName, String query, String svnRepository, String fileType, String sort, String lastmodified, boolean fileBody, Seeker seeker, ClientSidePrx seekerPrx) throws SessionException, SeekerException, SearchableException, SearchException, IllegalArgumentException, IOException {
        this.searcher.executeSearch(sessionName, query, svnRepository, fileType, sort, lastmodified, fileBody, seeker, seekerPrx);
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * {@inheritDoc}
     */
    public synchronized SessionProperty getSessionProperties(String sessionName) throws SessionException {
        return this.evaluator.getSessionProperties(sessionName);
    }

    /**
     * {@inheritDoc}
     */
    public synchronized long getSearchesCount() {
        return this.searcher.getSearchesCount();
    }

    /**
     * {@inheritDoc}
     */
    public synchronized long getSearchesCount(String sessionName) throws SessionException {
        return this.searcher.getSearchesCount(sessionName);
    }

    /**
     * {@inheritDoc}
     */
    public synchronized long getSearchesCount(String sessionName, Seeker seeker) throws SessionException, SeekerException {
        return this.searcher.getSearchesCount(sessionName, seeker);
    }

    /**
     * {@inheritDoc}
     */
    public synchronized long getMessagesCount() {
        return messenger.getMessagesCount();
    }

    /**
     * {@inheritDoc}
     */
    public synchronized long getMessagesCount(String sessionName) throws SessionException {
        return this.messenger.getMessagesCount(sessionName);
    }

    /**
     * {@inheritDoc}
     */
    public synchronized long getMessagesCount(String sessionName, Seeker seeker) throws SessionException, SeekerException {
        return this.messenger.getMessagesCount(sessionName, seeker);
    }

    /**
     * {@inheritDoc}
     */
    public synchronized long getRecommendationsCount() {
        return this.recommend.getRecommendationsCount();
    }

    /**
     * {@inheritDoc}
     */
    public synchronized long getRecommendationsCount(String sessionName) throws SessionException {
        return this.recommend.getRecommendationsCount(sessionName);
    }

    /**
     * {@inheritDoc}
     */
    public synchronized long getRecommendationsCount(String sessionName, Seeker seeker) throws SessionException, SeekerException {
        return this.recommend.getRecommendationsCount(sessionName, seeker);
    }

    /**
     * {@inheritDoc}
     */
    public synchronized List<DocumentMetaData> getRelevantDocuments(String sessionName, String query, int searcher) throws SessionException, QueryNotExistException, SearchableException {
        return this.evaluator.getRelevantDocuments(sessionName, query, searcher);
    }

    /**
     * {@inheritDoc}
     * @throws SeekerException
     */
    public synchronized List<DocumentMetaData> getRelevantDocuments(String sessionName, Seeker seeker, String query, int searcher) throws SessionException, QueryNotExistException, SeekerException, SearchableException {
        return this.evaluator.getRelevantDocuments(sessionName, seeker, query, searcher);
    }

    /**
     * {@inheritDoc}
     */
    public synchronized List<DocumentMetaData> getRetrievedDocuments(String sessionName, String query, int searcher) throws SessionException, QueryNotExistException, SearchableException {
        return this.evaluator.getRetrievedDocuments(sessionName, query, searcher);
    }

    /**
     * {@inheritDoc}
     * @throws SeekerException
     */
    public synchronized List<DocumentMetaData> getRetrievedDocuments(String sessionName, Seeker seeker, String query, int searcher) throws SessionException, QueryNotExistException, SeekerException, SearchableException {
        return this.evaluator.getRetrievedDocuments(sessionName, seeker, query, searcher);
    }

    /**
     * {@inheritDoc}
     * @param searcherArray
     */
    public List<DocumentMetaData> getRelevantDocuments(String sessionName, String query, int[] searcherArray) throws SessionException, QueryNotExistException, SearchableException {
        return this.evaluator.getRelevantDocuments(sessionName, query, searcherArray);
    }

    /**
     * {@inheritDoc}
     */
    public synchronized long getRetrievedDocumentsCount(String sessionName, String query) throws SessionException, QueryNotExistException {
        return this.evaluator.getRetrievedDocumentsCount(sessionName, query);
    }

    /**
     * {@inheritDoc}
     */
    public synchronized List<String> getQuerys(String sessionName) throws SessionException {
        return this.evaluator.getQuerys(sessionName);
    }

    /**
     * {@inheritDoc}
     */
    public synchronized long getDurationSessionTime(String sessionName) throws SessionException {
        return this.evaluator.getDurationSessionTime(sessionName);
    }

    /**
     * {@inheritDoc}
     */
    public List<DocumentMetaData> getViewedDocuments(String sessionName, String query, int searcher) throws SessionException, QueryNotExistException, SearchableException {
        return this.evaluator.getViewedDocuments(sessionName, query, searcher);
    }

    /**
     * {@inheritDoc}
     * 
     */
    public List<DocumentMetaData> getViewedDocuments(String sessionName, Seeker seeker, String query, int searcher) throws SessionException, QueryNotExistException, SearchableException, SeekerException {
        return this.evaluator.getViewedDocuments(sessionName, seeker, query, searcher);
    }

    /**
     * {@inheritDoc}
     * @param searcherArray
     */
    public List<DocumentMetaData> getViewedDocuments(String sessionName, String query, int[] searcherArray) throws SessionException, QueryNotExistException, SearchableException {
        return this.evaluator.getViewedDocuments(sessionName, query, searcherArray);
    }

    /**
     * {@inheritDoc}
     */
    public void trackRecommendation(String sessionName, Seeker emitter, ClientSidePrx emitterPrx) throws TrackException {
        this.tracker.trackRecommendation(sessionName, emitter, emitterPrx);
    }

    /**
     * {@inheritDoc}
     */
    public void trackRecommendation(String sessionName, Seeker seeker, Seeker emitter, ClientSidePrx emitterPrx) throws TrackException {
        this.tracker.trackRecommendation(sessionName, seeker, emitter, emitterPrx);
    }

    /**
     * {@inheritDoc}
     */
    public void trackRecommendation(String sessionName, Date date, Seeker emitter, ClientSidePrx emitterPrx) throws TrackException {
        this.tracker.trackRecommendation(sessionName, date, emitter, emitterPrx);
    }

    /**
     * {@inheritDoc}
     */
    public void trackRecommendation(String sessionName, String query, Seeker emitter, ClientSidePrx emitterPrx) throws TrackException {
        this.tracker.trackRecommendation(sessionName, query, emitter, emitterPrx);
    }

    /**
     * {@inheritDoc}
     */
    public void trackRecommendation(String sessionName, Seeker seeker, String query, Seeker emitter, ClientSidePrx emitterPrx) throws TrackException {
        this.tracker.trackRecommendation(sessionName, seeker, query, emitter, emitterPrx);
    }

    /**
     * {@inheritDoc}
     */
    public void trackRecommendation(String sessionName, String query, Date date, Seeker emitter, ClientSidePrx emitterPrx) throws TrackException {
        this.tracker.trackRecommendation(sessionName, query, date, emitter, emitterPrx);
    }

    /**
     * {@inheritDoc}
     */
    public void trackRecommendation(String sessionName, Seeker seeker, Date date, Seeker emitter, ClientSidePrx emitterPrx) throws TrackException {
        this.tracker.trackRecommendation(sessionName, seeker, date, emitter, emitterPrx);
    }

    /**
     * {@inheritDoc}
     */
    public void trackRecommendation(String sessionName, Seeker seeker, Date date, String query, Seeker emitter, ClientSidePrx emitterPrx) throws TrackException {
        this.tracker.trackRecommendation(sessionName, seeker, date, query, emitter, emitterPrx);
    }

    /**
     * {@inheritDoc}
     */
    public void trackSearch(String sessionName, int group, Seeker emitter, ClientSidePrx emitterPrx) throws TrackException {
        this.tracker.trackSearch(sessionName, group, emitter, emitterPrx);
    }

    /**
     * {@inheritDoc}
     */
    public void trackSearch(String sessionName, Seeker seeker, int group, Seeker emitter, ClientSidePrx emitterPrx) throws TrackException {
        this.tracker.trackSearch(sessionName, seeker, group, emitter, emitterPrx);
    }

    /**
     * {@inheritDoc}
     */
    public void trackSearch(String sessionName, Date date, int group, Seeker emitter, ClientSidePrx emitterPrx) throws TrackException {
        this.tracker.trackSearch(sessionName, date, group, emitter, emitterPrx);
    }

    /**
     * {@inheritDoc}
     */
    public void trackSearch(String sessionName, String query, int group, Seeker emitter, ClientSidePrx emitterPrx) throws TrackException {
        this.tracker.trackSearch(sessionName, query, group, emitter, emitterPrx);
    }

    /**
     * {@inheritDoc}
     */
    public void trackSearch(String sessionName, Seeker seeker, String query, int group, Seeker emitter, ClientSidePrx emitterPrx) throws TrackException {
        this.tracker.trackSearch(sessionName, seeker, query, group, emitter, emitterPrx);
    }

    /**
     * {@inheritDoc}
     */
    public void trackSearch(String sessionName, String query, Date date, int group, Seeker emitter, ClientSidePrx emitterPrx) throws TrackException {
        this.tracker.trackSearch(sessionName, query, date, group, emitter, emitterPrx);
    }

    /**
     * {@inheritDoc}
     */
    public void trackSearch(String sessionName, Seeker seeker, Date date, int group, Seeker emitter, ClientSidePrx emitterPrx) throws TrackException {
        this.tracker.trackSearch(sessionName, seeker, date, group, emitter, emitterPrx);
    }

    /**
     * {@inheritDoc}
     */
    public void trackSearch(String sessionName, Seeker seeker, Date date, String query, int group, Seeker emitter, ClientSidePrx emitterPrx) throws TrackException {
        this.tracker.trackSearch(sessionName, seeker, date, query, group, emitter, emitterPrx);
    }

    /**
     * {@inheritDoc}
     */
    public void trackSession(String sessionName, Date date, Seeker emitter, ClientSidePrx prx) throws TrackException {
        this.tracker.trackSession(sessionName, date, emitter, prx);
    }

     /**
     * {@inheritDoc}
     */
    public void sendQueryChangeAction(String sessionName, String query, Map<String, ScorePQT> statistics, Seeker emitter) throws SessionException, SeekerException, AwarenessException {
        this.awareness.sendQueryChangeAction(sessionName, query, statistics, emitter);
    }

    /**
     * {@inheritDoc}
     */
    public void sendQueryTypedAction(String sessionName, boolean typed, Seeker emitter) throws SessionException, SeekerException, AwarenessException {
        this.awareness.sendQueryTypedAction(sessionName, typed, emitter);
    }

    /**
     * {@inheritDoc}
     */
    public void sendTermAcceptanceAction(String sessionName, String term, int event, String user, Seeker emitter) throws SessionException, SeekerException, AwarenessException {
        this.awareness.sendTermAcceptanceAction(sessionName, term, event, user, emitter);
    }

    /**
     * {@inheritDoc}
     */
    public void sendPuttingQueryTermsTogetherAction(String sessionName, int event, Seeker emitter) throws SessionException, SeekerException, AwarenessException {
        this.awareness.sendPuttingQueryTermsTogetherAction(sessionName, event, emitter);
    }
  

    public void sendCollabTermsSuggestAction(String sessionName, int event, Seeker emitter, ClientSidePrx prx) throws SessionException, SeekerException, AwarenessException {
        this.impRecomend.sendCollabTermsSuggestAction(sessionName, event, emitter, prx);
    }



}

