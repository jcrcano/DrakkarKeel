/*
 * DrakkarKeel - An Enterprise Collaborative Search Platform
 *
 * The contents of this file are subject under the terms described in the
 * DRAKKARKEEL_LICENSE file included in this distribution; you may not use this
 * file except in compliance with the License. 
 *
 * 2013-2014 DrakkarKeel Platform.
 */
package drakkar.mast.retrieval.improves;

import drakkar.oar.TermSuggest;
import es.uam.eps.nets.rankfusion.interfaces.IFResource;

public interface ITermResource extends IFResource{
    /**
     * 
     * @return
     */
    public TermSuggest getTermSuggest();
}
