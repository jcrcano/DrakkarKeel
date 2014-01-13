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
 * motor de búsqueda de RI Lemur versión 4.10
 *
 * Nota: poner en la variable de entorno PATH, la direccion del fichero lemur_jni.dll
 * y los archivos correspondientes para Unix, y MacOSX
 */
public class IndriSearchEngine extends SearchEngine {

    IndriContext indriContext;
     
    public IndriSearchEngine() {
        indriContext = new IndriContext();
    }

    /**
     *
     * @param listener
     */
    public IndriSearchEngine(FacadeDesktopListener listener) {
        indriContext = new IndriContext(listener);
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
        return KeySearchable.INDRI_SEARCH_ENGINE;
    }

    public String getName() {
        return "Indri";
    }

    public Contextable getContext() {
        return indriContext;
    }


} 

  