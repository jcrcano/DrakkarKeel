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
import drakkar.mast.IndexException;
import drakkar.mast.SearchException;
import drakkar.mast.recommender.LSIManager;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * Esta clase constituye la super clase de todos los motores de búsqueda soportados
 * por DrakkarKeel.
 * 
 * 
 */
public abstract class SearchEngine implements Searchable, FileIndexable {

    /**
     *
     */
    protected boolean enabled = false;

    /**
     * {@inheritDoc}
     */
    public ArrayList<DocumentMetaData> search(String query, boolean caseSensitive) throws SearchException {
        ArrayList<DocumentMetaData> results = ((EngineContext)this.getContext()).search(query, caseSensitive);
        return results;
    }

    /**
     * {@inheritDoc}
     */
    public ArrayList<DocumentMetaData> search(String query, String docType, boolean caseSensitive) throws SearchException {
        ArrayList<DocumentMetaData> results = ((EngineContext)this.getContext()).search(query, docType, caseSensitive);
        return results;
    }

    /**
     * {@inheritDoc}
     */
    public ArrayList<DocumentMetaData> search(String query, String[] docTypes, boolean caseSensitive) throws SearchException {
        ArrayList<DocumentMetaData> results = ((EngineContext)this.getContext()).search(query, docTypes, caseSensitive);
        return results;
    }

    /**
     * 
     * @return
     */
    

    /**
     * {@inheritDoc}
     */
    public long makeIndex() throws IndexException {
        return ((EngineContext)this.getContext()).makeIndex();
    }

    /**
     * {@inheritDoc}
     */
    public long makeIndex(File collectionPath) throws IndexException {
        return ((EngineContext)this.getContext()).makeIndex(collectionPath);
    }

    /**
     * {@inheritDoc}
     */
    public long makeIndex(List<File> collectionPath) throws IndexException {
        return ((EngineContext)this.getContext()).makeIndex(collectionPath);
    }

    /**
     * {@inheritDoc}
     */
    public long makeIndex(File collectionPath, File indexPath) throws IndexException {
        return ((EngineContext)this.getContext()).makeIndex(collectionPath, indexPath);
    }

    /**
     * {@inheritDoc}
     */
    public long makeIndex(List<File> collectionPath, File indexPath) throws IndexException {
        return ((EngineContext)this.getContext()).makeIndex(collectionPath, indexPath);
    }

    /**
     * {@inheritDoc}
     */
    public long updateIndex(File collectionPath) throws IndexException {
        return ((EngineContext)this.getContext()).updateIndex(collectionPath);
    }

    /**
     * {@inheritDoc}
     */
    public long updateIndex(List<File> collectionPath) throws IndexException {
        return ((EngineContext)this.getContext()).updateIndex(collectionPath);
    }

    /**
     * {@inheritDoc}
     */
    public long updateIndex(File collectionPath, File indexPath) throws IndexException {
        return ((EngineContext)this.getContext()).updateIndex(collectionPath, indexPath);
    }

    /**
     * {@inheritDoc}
     */
    public long updateIndex(List<File> collectionPath, File indexPath) throws IndexException {
        return ((EngineContext)this.getContext()).updateIndex(collectionPath, indexPath);
    }

    /**
     * {@inheritDoc}
     */
    public boolean loadIndex() throws IndexException {
        return ((EngineContext)this.getContext()).loadIndex();
    }

    /**
     * {@inheritDoc}
     */
    public boolean loadIndex(File indexPath) throws IndexException {
        return ((EngineContext)this.getContext()).loadIndex(indexPath);
    }

    /**
     * {@inheritDoc}
     */
    public void setEnabled(boolean flag) {
        this.enabled = flag;
    }

   /**
     * {@inheritDoc}
     */
    public boolean isEnabled() {
        return this.enabled;
    }

    /**
     *
     * @param uri
     */
    public void setIndexPath(String uri) throws FileNotFoundException {
        ((EngineContext)this.getContext()).setIndexPath(new File(uri));
    }

    /**
     *
     * @return
     */
    public String getIndexPath() {
        return ((EngineContext)this.getContext()).getIndexPath().getAbsolutePath();
    }

    /**
     *
     * @param uri
     */
    public void setCollectionPath(String uri) {
        ((EngineContext)this.getContext()).setCollectionPath(new File(uri));
    }

    /**
     *
     * @return
     */
    public String getCollectionPath() {
        return ((EngineContext)this.getContext()).getCollectionPath().getAbsolutePath();
    }

     /**
     * Devuelve la instancia de la clase LSIManager asociada al buscador.
     *
     * @return objeto LSIManager
     */
    public LSIManager getLSIManager(){
        return ((EngineContext)this.getContext()).getLSIManager();
    }
}
