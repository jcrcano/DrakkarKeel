/*
 * DrakkarKeel - An Enterprise Collaborative Search Platform
 *
 * The contents of this file are subject under the terms described in the
 * DRAKKARKEEL_LICENSE file included in this distribution; you may not use this
 * file except in compliance with the License. 
 *
 * 2013-2014 DrakkarKeel Platform.
 */
package drakkar.mast;

import drakkar.oar.facade.event.FacadeDesktopListener;
import drakkar.oar.util.KeySearchable;
import static drakkar.oar.util.KeySearchable.*;
import drakkar.mast.retrieval.AdvIndexable;
import drakkar.mast.retrieval.CVSSearch;
import drakkar.mast.retrieval.SearchEngine;
import drakkar.mast.retrieval.Searchable;
import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Esta clase maneja todos las posibles operaciones relacionadas con
 * el proceso de indexación de los diferentes buscadores soportados por DrakkarKeel
 *
 * 
 */
public class IndexerManager implements AdvIndexable {

    private Map<Integer, Searchable> searchableHash;
    private List<Searchable> searcherList;

    /**
     * Constructor de la clase
     *
     * @param searchableHash listado de buscadores
     * @param searcherList
     */
    public IndexerManager(Map<Integer, Searchable> searchableHash, List<Searchable> searcherList) {
        this.searchableHash = searchableHash;
        this.searcherList = searcherList;
    }

    /**
     * {@inheritDoc}
     */
    public long makeIndex() throws IndexException {
        return this.batchMakeIndex(null);
    }

    /**
     * {@inheritDoc}
     */
    public long makeIndex(File collectionPath) throws IndexException {
        return this.batchMakeIndex(collectionPath);
    }

    private long batchMakeIndex(File collectionPath) throws IndexException {
        long count = 0;
        SearchEngine engine;
        boolean flag = false;
        if (collectionPath == null) {
            flag = true;
        }

        for (Searchable searchable : searcherList) {
            if (searchable instanceof SearchEngine && searchable.isEnabled()) {
                engine = (SearchEngine) searchable;
                if (flag) {
                    count += engine.makeIndex();
                } else {
                    count += engine.makeIndex(collectionPath);
                }
            }
        }

        return count;

    }

    /**
     * {@inheritDoc}
     */
    public long makeIndex(List<File> collectionPath) throws IndexException {
        long count = 0;
        SearchEngine engine;

        for (Searchable searchable : searcherList) {
            if (searchable instanceof SearchEngine && searchable.isEnabled()) {
                engine = (SearchEngine) searchable;
                count += engine.makeIndex(collectionPath);
            }
        }

        return count;
    }

    /**
     * {@inheritDoc}
     */
    public long makeIndex(File collectionPath, File indexPath) throws IndexException {

        long count = 0;
        SearchEngine engine;

        for (Searchable searchable : searcherList) {
            if (searchable instanceof SearchEngine && searchable.isEnabled()) {
                engine = (SearchEngine) searchable;
                count += engine.makeIndex(collectionPath, indexPath);
            }
        }

        return count;

    }

    /**
     * {@inheritDoc}
     */
    public long makeIndex(List<File> collectionPath, File indexPath) throws IndexException {
        long count = 0;
        SearchEngine engine;

        for (Searchable searchable : searcherList) {
            if (searchable instanceof SearchEngine && searchable.isEnabled()) {
                engine = (SearchEngine) searchable;
                count += engine.makeIndex(collectionPath, indexPath);
            }
        }


        return count;
    }

    /**
     * {@inheritDoc}
     */
    public long makeIndex(int searcher, File collectionPath) throws SearchableNotSupportedException, IndexException {
        long count = 0;
        Searchable searchable = this.searchableHash.get(searcher);

        if (searchable == null || !(searchable instanceof SearchEngine)) {
            throw new SearchableNotSupportedException("The searcher " + searcher + " is not supported for this operation");
        } else if (searchable.isEnabled()) {
            count = ((SearchEngine) searchable).makeIndex(collectionPath);
        }
        return count;
    }

    /**
     * {@inheritDoc}
     */
    public long makeIndex(int searcher, File collectionPath, File indexPath) throws SearchableNotSupportedException, IndexException {
        long count = 0;
        Searchable searchable = this.searchableHash.get(searcher);
        File newFile = null;

        if (searchable == null || !(searchable instanceof SearchEngine)) {
            throw new SearchableNotSupportedException("The searcher " + searcher + " is not supported for this operation");
        } else if (searchable.isEnabled()) {

            switch (searcher) {

                case LUCENE_SEARCH_ENGINE:
                    newFile = new File(indexPath.getPath().concat("/lucene/index"));
                    break;
                case MINION_SEARCH_ENGINE:
                    newFile = new File(indexPath.getPath().concat("/minion/index"));
                    break;
                case TERRIER_SEARCH_ENGINE:
                    newFile = new File(indexPath.getPath().concat("/terrier/index"));
                    break;
                case INDRI_SEARCH_ENGINE:
                    newFile = new File(indexPath.getPath().concat("/indri/index"));
                    break;
            }
            count = ((SearchEngine) searchable).makeIndex(collectionPath, newFile);
        }
        return count;

    }

    /**
     * Crea un indice en el path por defecto, de la colección pasada por parámetro,
     * para el buscador seleccionado
     *
     * @param searcher       buscador
     * @param collectionPath lista de colecciones de datos
     *
     * @return cantidad de documentos indexados
     *
     * @throws SearchableNotSupportedException es lanzada si el buscador especificado no es soportado
     * @throws IndexException es lanzada si ocurre algún error en el proceso de indexación
     */
    public long makeIndex(int searcher, List<File> collectionPath) throws SearchableNotSupportedException, IndexException {
        long count = 0;
        Searchable searchable = this.searchableHash.get(searcher);

        if (searchable == null || !(searchable instanceof SearchEngine)) {
            throw new SearchableNotSupportedException("The searcher " + searcher + " is not supported for this operation");
        } else if (searchable.isEnabled()) {
            count = ((SearchEngine) searchable).makeIndex(collectionPath);
        }
        return count;
    }

    /**
     * {@inheritDoc}
     */
    public long makeIndex(int searcher, List<File> collectionPath, File indexPath) throws SearchableNotSupportedException, IndexException {
        long count = 0;
        Searchable searchable = this.searchableHash.get(searcher);
        File newFile = null;

        if (searchable == null || !(searchable instanceof SearchEngine)) {
            throw new SearchableNotSupportedException("The searcher " + searcher + " is not supported for this operation");
        } else if (searchable.isEnabled()) {
            switch (searcher) {

                case LUCENE_SEARCH_ENGINE:
                    newFile = new File(indexPath.getPath().concat("/lucene/index"));
                    break;
                case MINION_SEARCH_ENGINE:
                    newFile = new File(indexPath.getPath().concat("/minion/index"));
                    break;
                case TERRIER_SEARCH_ENGINE:
                    newFile = new File(indexPath.getPath().concat("/terrier/index"));
                    break;
                case INDRI_SEARCH_ENGINE:
                    newFile = new File(indexPath.getPath().concat("/indri/index"));
                    break;
            }
            count = ((SearchEngine) searchable).makeIndex(collectionPath, newFile);
        }
        return count;
    }

    /**
     * Crea un indice en el path por defecto, de la colección pasada por parámetro,
     * para los buscadores seleccionados
     *
     * @param searchers      listado de buscadores
     * @param collectionPath colección de datos
     *
     * @return cantidad de documentos indexados
     *
     * @throws SearchableNotSupportedException es lanzada si el buscador especificado no es soportado
     * @throws IndexException es lanzada si ocurre algún error en el proceso de indexación
     */
    public long makeIndex(int[] searchers, File collectionPath) throws SearchableNotSupportedException, IndexException {
        long count = 0;
        for (int item : searchers) {
            count += this.makeIndex(item, collectionPath);
        }

        return count;
    }

    /**
     * {@inheritDoc}
     */
    public long makeIndex(int[] searchers, List<File> collectionPath) throws SearchableNotSupportedException, IndexException {
        long count = 0;
        for (int item : searchers) {
            count += this.makeIndex(item, collectionPath);
        }

        return count;
    }

    /**
     * {@inheritDoc}
     */
    public long makeIndex(int[] searchers, File collectionPath, File indexPath) throws SearchableNotSupportedException, IndexException {
        long count = 0;
        for (int item : searchers) {
            count += this.makeIndex(item, collectionPath, indexPath);
        }

        return count;
    }

    /**
     * {@inheritDoc}
     */
    public long makeIndex(int[] searchers, List<File> collectionPath, File indexPath) throws SearchableNotSupportedException, IndexException {
        long count = 0;
        for (int item : searchers) {
            count += this.makeIndex(item, collectionPath, indexPath);

        }

        return count;
    }

    /**
     * To make an index from an SVN repository
     *
     * @param url
     * @param repListFile
     * @param indexPath
     * @param user
     * @param password
     * @param mergeFactor
     * @param directPath
     * @param facade
     * @return
     * @throws IndexException
     */
    public long makeSVNIndex(String url, String repListFile, String indexPath, String user, String password, String mergeFactor, String directPath, FacadeDesktopListener facade) throws IndexException, SearchableNotSupportedException, IndexException {

        Properties p = new Properties();
        p.setProperty("url", url);

        if (repListFile == null) {
            p.setProperty("directPath", directPath);
        } else {
            p.setProperty("repListFile", repListFile);
        }

        p.setProperty("indexPath", indexPath);

        if (user != null && password != null) {
            p.setProperty("user", user);
            p.setProperty("password", password);
        }

        p.setProperty("mergeFactor", mergeFactor);//max loadedDocs of files keept in memory by Lucene

        long count = 0;
        int searcher = KeySearchable.SVN_SEARCHER;
        Searchable searchable = this.searchableHash.get(searcher);

        if (searchable == null || !(searchable instanceof CVSSearch)) {
            throw new SearchableNotSupportedException("The searcher " + searcher + " is not supported for this operation");
        } else if (searchable.isEnabled()) {

            CVSSearch search = ((CVSSearch) searchable);
            search.setListener(facade);
            search.setProperties(p);
            count = search.makeIndex();
        }
        return count;


    }

    /**
     * {@inheritDoc}
     */
    public long updateIndex(int searcher, File collectionPath) throws SearchableNotSupportedException, IndexException {
        long count = 0;
        Searchable searchable = this.searchableHash.get(searcher);

        if (searchable == null || !(searchable instanceof SearchEngine)) {
            throw new SearchableNotSupportedException("The searcher " + searcher + " is not supported for this operation");
        } else if (searchable.isEnabled()) {
            count = ((SearchEngine) searchable).updateIndex(collectionPath);
        }
        return count;
    }

    /**
     * {@inheritDoc}
     */
    public long updateIndex(int searcher, List<File> collectionPath) throws SearchableNotSupportedException, IndexException {
        long count = 0;
        Searchable searchable = this.searchableHash.get(searcher);

        if (searchable == null || !(searchable instanceof SearchEngine)) {
            throw new SearchableNotSupportedException("The searcher " + searcher + " is not supported for this operation");
        } else if (searchable.isEnabled()) {
            count = ((SearchEngine) searchable).updateIndex(collectionPath);
        }
        return count;
    }

    /**
     * {@inheritDoc}
     */
    public long updateIndex(int searcher, File collectionPath, File indexPath) throws SearchableNotSupportedException, IndexException {
        long count = 0;
        Searchable searchable = this.searchableHash.get(searcher);
        File newFile = null;
        if (searchable == null || !(searchable instanceof SearchEngine)) {
            throw new SearchableNotSupportedException("The searcher " + searcher + " is not supported for this operation");
        } else if (searchable.isEnabled()) {
            switch (searcher) {

                case LUCENE_SEARCH_ENGINE:
                    newFile = new File(indexPath.getPath().concat("/lucene/index"));
                    break;
                case MINION_SEARCH_ENGINE:
                    newFile = new File(indexPath.getPath().concat("/minion/index"));
                    break;
                case TERRIER_SEARCH_ENGINE:
                    newFile = new File(indexPath.getPath().concat("/terrier/index"));
                    break;
                case INDRI_SEARCH_ENGINE:
                    newFile = new File(indexPath.getPath().concat("/indri/index"));
                    break;
            }
            count = ((SearchEngine) searchable).updateIndex(collectionPath, newFile);
        }
        return count;
    }

    /**
     * {@inheritDoc}
     */
    public long updateIndex(int searcher, List<File> collectionPath, File indexPath) throws SearchableNotSupportedException, IndexException {
        long count = 0;
        Searchable searchable = this.searchableHash.get(searcher);
        File newFile = null;

        if (searchable == null || !(searchable instanceof SearchEngine)) {
            throw new SearchableNotSupportedException("The searcher " + searcher + " is not supported for this operation");
        } else if (searchable.isEnabled()) {
            switch (searcher) {

                case LUCENE_SEARCH_ENGINE:
                    newFile = new File(indexPath.getPath().concat("/lucene/index"));
                    break;
                case MINION_SEARCH_ENGINE:
                    newFile = new File(indexPath.getPath().concat("/minion/index"));
                    break;
                case TERRIER_SEARCH_ENGINE:
                    newFile = new File(indexPath.getPath().concat("/terrier/index"));
                    break;
                case INDRI_SEARCH_ENGINE:
                    newFile = new File(indexPath.getPath().concat("/indri/index"));
                    break;
            }
            count = ((SearchEngine) searchable).updateIndex(collectionPath, newFile);
        }
        return count;
    }

    /**
     * {@inheritDoc}
     */
    public long updateIndex(int[] searchers, File collectionPath, File indexPath) throws SearchableNotSupportedException, IndexException {
        long count = 0;
        for (int item : searchers) {

            this.updateIndex(item, collectionPath, indexPath);
        }
        return count;
    }

    /**
     * {@inheritDoc}
     */
    public long updateIndex(int[] searchers, File collectionPath) throws SearchableNotSupportedException, IndexException {
        long count = 0;
        for (int item : searchers) {

            this.updateIndex(item, collectionPath);
        }
        return count;
    }

    /**
     * {@inheritDoc}
     */
    public long updateIndex(int[] searchers, List<File> collectionPath) throws SearchableNotSupportedException, IndexException {
        long count = 0;
        for (int item : searchers) {

            this.updateIndex(item, collectionPath);
        }
        return count;
    }

    /**
     * {@inheritDoc}
     */
    public long updateIndex(int[] searchers, List<File> collectionPath, File indexPath) throws SearchableNotSupportedException, IndexException {
        long count = 0;
        for (int item : searchers) {

            this.updateIndex(item, collectionPath, indexPath);
        }
        return count;
    }

    /**
     * {@inheritDoc}
     */
    public boolean loadIndex() throws IndexException {

        byte count = 0;

        for (Searchable searchable : searcherList) {
            if (searchable instanceof SearchEngine && searchable.isEnabled()) {
                ((SearchEngine) searchable).loadIndex();
                count++;
            }
        }

        if (count > 0) {
            return true;
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    public boolean loadIndex(File indexPath) throws IndexException {

        byte count = 0;

        for (Searchable searchable : searcherList) {
            if (searchable instanceof SearchEngine && searchable.isEnabled()) {
                ((SearchEngine) searchable).loadIndex(indexPath);
                count++;
            }
        }

        if (count > 0) {
            return true;
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    public boolean loadIndex(int searcher, File indexPath) throws SearchableNotSupportedException, IndexException {
        byte count = 0;
        Searchable searchable = this.searchableHash.get(searcher);
        File newFile = null;

        if (searchable == null || !(searchable instanceof SearchEngine)) {
            throw new SearchableNotSupportedException("The searcher " + searcher + " is not supported for this operation");
        } else if (searchable.isEnabled()) {
            switch (searcher) {

                case LUCENE_SEARCH_ENGINE:
                    newFile = new File(indexPath.getPath().concat("/lucene/index"));
                    break;
                case MINION_SEARCH_ENGINE:
                    newFile = new File(indexPath.getPath().concat("/minion/index"));
                    break;
                case TERRIER_SEARCH_ENGINE:
                    newFile = new File(indexPath.getPath().concat("/terrier/index"));
                    break;
                case INDRI_SEARCH_ENGINE:
                    newFile = new File(indexPath.getPath().concat("/indri/index"));
                    break;
            }
            ((SearchEngine) searchable).loadIndex(newFile);
            count++;
        }

        if (count > 0) {
            return true;
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    public boolean loadIndex(int searcher) throws SearchableNotSupportedException, IndexException {
        byte count = 0;
        Searchable searchable = this.searchableHash.get(searcher);

        if (searchable == null || !(searchable instanceof SearchEngine)) {
            throw new SearchableNotSupportedException("The searcher " + searcher + " is not supported for this operation");
        } else if (searchable.isEnabled()) {

            ((SearchEngine) searchable).loadIndex();
            count++;
        }

        if (count > 0) {
            return true;
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    public boolean loadIndex(int[] searchers, File indexPath) throws SearchableNotSupportedException, IndexException {
        byte count = 0;
        boolean flag = false;

        for (int item : searchers) {

            flag = this.loadIndex(item, indexPath);

            if (flag) {
                count++;
            }
        }
        if (count > 0) {
            return true;
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    public boolean loadIndex(int[] searchers) throws IndexException, SearchableNotSupportedException {
        byte count = 0;
        boolean flag = false;

        for (int item : searchers) {

            flag = this.loadIndex(item);

            if (flag) {
                count++;
            }
        }
        if (count > 0) {
            return true;
        }
        return false;
    }
}
