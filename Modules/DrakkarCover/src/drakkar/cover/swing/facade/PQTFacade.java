/*
 * DrakkarKeel - An Enterprise Collaborative Search Platform
 *
 * The contents of this file are subject under the terms described in the
 * DRAKKARKEEL_LICENSE file included in this distribution; you may not use this
 * file except in compliance with the License. 
 *
 * 2013-2014 DrakkarKeel Platform.
 */
package drakkar.cover.swing.facade;

import drakkar.oar.ScorePQT;
import java.util.Map;

public interface PQTFacade extends SearchFacade {

    /**
     *
     * @param term
     * @throws Exception
     */
    public void agree(String term, String user) throws Exception;

    /**
     *
     * @param term
     * @throws Exception
     */
    public void disagree(String term, String user) throws Exception;

    /**
     *
     * @param term 
     * @param score
     */
    public void showStatistics(String term, ScorePQT score);

    /**
     *
     * @param flag
     */
    public void notifyQueryTyped(boolean flag);

    /**
     *
     * @param query
     * @param statistics
     */
    public void notifyQueryChange(String query, Map<String, ScorePQT> statistics);
}
