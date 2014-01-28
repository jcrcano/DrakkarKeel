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

import drakkar.oar.DocumentMetaData;
import drakkar.oar.facade.event.FacadeDesktopListener;
import drakkar.mast.IndexException;
import drakkar.mast.SearchException;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Esta clase representa un adaptador de la clase AdvSearchEngineContext
 *
 *
 */
public abstract class AdvEngineContextAdapter extends AdvEngineContext {

   /**
     * Constructor por defecto de la clase
     */
    public AdvEngineContextAdapter() {
    }
    
    /**
     * Constructor de la clase
     *
     * @param listener oyente de la aplicación del servidor
     */
    public AdvEngineContextAdapter(FacadeDesktopListener listener) {
        super(listener);
    }

    

    /**
     * {@inheritDoc}
     */
    @Override
    public ArrayList<DocumentMetaData> search(String query, int field, boolean caseSensitive) throws SearchException {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ArrayList<DocumentMetaData> search(String query, String docType, int field, boolean caseSensitive) throws SearchException {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ArrayList<DocumentMetaData> search(String query, String docType, int[] fields, boolean caseSensitive) throws SearchException {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDocumentField(int field) {
        return null;
    }

//    /**
//     * {@inheritDoc}
//     */
//    @Override
//    public ArrayList<DocumentMetaData> search(String query, boolean caseSensitive) throws SearchException {
//        return null;
//    }
//
//    /**
//     * {@inheritDoc}
//     */
//    @Override
//    public ArrayList<DocumentMetaData> search(String query, String docType, boolean caseSensitive) throws SearchException {
//        return null;
//    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean safeToBuildIndex(File indexPath, int operation) {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    public long makeIndex() throws IndexException {
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    public long makeIndex(File collectionPath) throws IndexException {
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    public long makeIndex(List<File> collectionPath) throws IndexException {
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    public long makeIndex(File collectionPath, File indexPath) throws IndexException {
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    public long makeIndex(List<File> collectionPath, File indexPath) throws IndexException {
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    public long updateIndex(File collectionPath) throws IndexException {
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    public long updateIndex(List<File> collectionPath) throws IndexException {
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    public long updateIndex(File collectionPath, File indexPath) throws IndexException {
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    public long updateIndex(List<File> collectionPath, File indexPath) throws IndexException {
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    public boolean loadIndex() throws IndexException {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    public boolean loadIndex(File indexPath) throws IndexException {
        return false;
    }
}
