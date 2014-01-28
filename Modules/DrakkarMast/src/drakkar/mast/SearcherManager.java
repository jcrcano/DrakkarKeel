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

import drakkar.oar.ResultSetMetaData;
import drakkar.oar.svn.SVNData;
import static drakkar.oar.util.SearchPrinciple.*;
import drakkar.mast.retrieval.Searchable;
import drakkar.mast.retrieval.improves.MetaSearch;
import drakkar.mast.retrieval.improves.MultiSearch;
import drakkar.mast.retrieval.improves.SingleSearch;
import java.util.List;
import java.util.Map;

/**
 * Esta clase administra todos los métodos de búsqueda colaborativa o no soportados
 * por DrakkarKeel
 *
 *
 *
 */
public class SearcherManager implements DefaultGeneralSearch, CollaborativeGeneralSearch {

    private SingleSearch singleSearch;
    private MetaSearch metaSearch;
    private MultiSearch multiSearch;


    public SearcherManager(Map<Integer, Searchable> searcherHash, List<Searchable> searchersList) {
        this.singleSearch = new SingleSearch(searcherHash, searchersList);
        this.metaSearch = new MetaSearch(searcherHash, searchersList);
        this.multiSearch = new MultiSearch(searcherHash, searchersList);

    }


    /**
     * {@inheritDoc}
     */
    public ResultSetMetaData search(String query, int searcher, int principle, boolean caseSensitive) throws IllegalArgumentException, SearchableException, SearchException {

        ResultSetMetaData finalResults;
        switch (principle) {
            case SINGLE_SEARCH:

                finalResults = this.singleSearch.search(query, searcher, caseSensitive);
                return finalResults;


            case META_SEARCH:
                finalResults = this.metaSearch.search(query, caseSensitive);
                return finalResults;

            case MULTI_SEARCH:
                finalResults = this.multiSearch.search(query, caseSensitive);
                return finalResults;

            default:
                throw new IllegalArgumentException("Search principle is not supported.");
        }
    }

    /**
     * {@inheritDoc}
     */
    public ResultSetMetaData search(String query, int field, int searcher, int principle, boolean caseSensitive) throws IllegalArgumentException, SearchableException, SearchException {
        ResultSetMetaData finalResults;
        switch (principle) {
            case SINGLE_SEARCH:
                finalResults = this.singleSearch.search(query, field, searcher, caseSensitive);
                return finalResults;


            case META_SEARCH:
                finalResults = this.metaSearch.search(query, field, caseSensitive);
                return finalResults;

            case MULTI_SEARCH:
                finalResults = this.multiSearch.search(query, field, caseSensitive);
                return finalResults;

            default:
                throw new IllegalArgumentException("Search principle is not supported.");
        }
    }

    /**
     * {@inheritDoc}
     */
    public ResultSetMetaData search(String query, int[] fields, int searcher, int principle, boolean caseSensitive) throws IllegalArgumentException, SearchableException, SearchException {
        ResultSetMetaData finalResults;
        switch (principle) {
            case SINGLE_SEARCH:
                finalResults = this.singleSearch.search(query, fields, searcher, caseSensitive);
                return finalResults;

            case META_SEARCH:
                finalResults = this.metaSearch.search(query, fields, caseSensitive);
                return finalResults;

            case MULTI_SEARCH:
                finalResults = this.multiSearch.search(query, fields, caseSensitive);
                return finalResults;

            default:
                throw new IllegalArgumentException("Search principle is not supported.");
        }
    }

    /**
     * {@inheritDoc}
     */
    public ResultSetMetaData search(String query, String docType, int searcher, int principle, boolean caseSensitive) throws IllegalArgumentException, SearchableException, SearchException {

        ResultSetMetaData finalResults;
        switch (principle) {
            case SINGLE_SEARCH:
                finalResults = this.singleSearch.search(query, docType, searcher, caseSensitive);
                return finalResults;

            case META_SEARCH:
                finalResults = this.metaSearch.search(query, docType, caseSensitive);
                return finalResults;

            case MULTI_SEARCH:
                finalResults = this.multiSearch.search(query, docType, caseSensitive);
                return finalResults;

            default:
                throw new IllegalArgumentException("Search principle is not supported.");
        }

    }

    /**
     * {@inheritDoc}
     */
    public ResultSetMetaData search(String query, String[] docTypes, int searcher, int principle, boolean caseSensitive) throws IllegalArgumentException, SearchableException, SearchException {

        ResultSetMetaData finalResults;
        switch (principle) {
            case SINGLE_SEARCH:
                finalResults = this.singleSearch.search(query, docTypes, searcher, caseSensitive);
                return finalResults;

            case META_SEARCH:
                finalResults = this.metaSearch.search(query, docTypes, caseSensitive);
                return finalResults;

            case MULTI_SEARCH:
                finalResults = this.multiSearch.search(query, docTypes, caseSensitive);
                return finalResults;

            default:
                throw new IllegalArgumentException("Search principle is not supported.");
        }
    }

    /**
     * {@inheritDoc}
     */
    public ResultSetMetaData search(String query, String docType, int field, int searcher, int principle, boolean caseSensitive) throws IllegalArgumentException, SearchableException, SearchException {

        ResultSetMetaData finalResults;
        switch (principle) {
            case SINGLE_SEARCH:
                finalResults = this.singleSearch.search(query, docType, field, searcher, caseSensitive);
                return finalResults;

            case META_SEARCH:
                finalResults = this.metaSearch.search(query, docType, field, caseSensitive);
                return finalResults;

            case MULTI_SEARCH:
                finalResults = this.multiSearch.search(query, docType, field, caseSensitive);
                return finalResults;

            default:
                throw new IllegalArgumentException("Search principle is not supported.");
        }

    }

    /**
     * {@inheritDoc}
     */
    public ResultSetMetaData search(String query, String[] docTypes, int field, int searcher, int principle, boolean caseSensitive) throws IllegalArgumentException, SearchableException, SearchException {
        ResultSetMetaData finalResults;
        switch (principle) {
            case SINGLE_SEARCH:
                finalResults = this.singleSearch.search(query, docTypes, field, searcher, caseSensitive);
                return finalResults;

            case META_SEARCH:
                finalResults = this.metaSearch.search(query, docTypes, field, caseSensitive);
                return finalResults;

            case MULTI_SEARCH:
                finalResults = this.multiSearch.search(query, docTypes, field, caseSensitive);
                return finalResults;

            default:
                throw new IllegalArgumentException("Search principle is not supported.");
        }
    }

    /**
     * {@inheritDoc}
     */
    public ResultSetMetaData search(String query, String docType, int[] fields, int searcher, int principle, boolean caseSensitive) throws IllegalArgumentException, SearchableException, SearchException {
        ResultSetMetaData finalResults;

        switch (principle) {
            case SINGLE_SEARCH:
                finalResults = this.singleSearch.search(query, docType, fields, searcher, caseSensitive);
                return finalResults;

            case META_SEARCH:
                finalResults = this.metaSearch.search(query, docType, fields, caseSensitive);
                return finalResults;

            case MULTI_SEARCH:
                finalResults = this.multiSearch.search(query, docType, fields, caseSensitive);
                return finalResults;

            default:
                throw new IllegalArgumentException("Search principle is not supported.");
        }

    }

    /**
     * {@inheritDoc}
     */
    public ResultSetMetaData search(String query, String[] docTypes, int[] fields, int searcher, int principle, boolean caseSensitive) throws IllegalArgumentException, SearchableException, SearchException {
        ResultSetMetaData finalResults;
        switch (principle) {
            case SINGLE_SEARCH:
                finalResults = this.singleSearch.search(query, docTypes, fields, searcher, caseSensitive);
                return finalResults;

            case META_SEARCH:
                finalResults = this.metaSearch.search(query, docTypes, fields, caseSensitive);
                return finalResults;

            case MULTI_SEARCH:
                finalResults = this.multiSearch.search(query, docTypes, fields, caseSensitive);
                return finalResults;

            default:
                throw new IllegalArgumentException("Search principle is not supported.");
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * {@inheritDoc}
     */
    public ResultSetMetaData search(int[] searchers, String query, int principle, boolean caseSensitive) throws IllegalArgumentException, SearchableException, SearchException {
        switch (principle) {

            case META_SEARCH:
                ResultSetMetaData metaResults = this.metaSearch.search(searchers, query, caseSensitive);
                return metaResults;

            case MULTI_SEARCH:
                ResultSetMetaData multiResults = this.multiSearch.search(searchers, query, caseSensitive);
                return multiResults;

            default:
                throw new IllegalArgumentException("Search principle is not supported.");
        }
    }

    /**
     * {@inheritDoc}
     */
    public ResultSetMetaData search(int[] searchers, String query, int field, int principle, boolean caseSensitive) throws IllegalArgumentException, SearchableException, SearchException {

        switch (principle) {
            case META_SEARCH:
                ResultSetMetaData metaResults = this.metaSearch.search(searchers, query, field, caseSensitive);
                return metaResults;

            case MULTI_SEARCH:
                ResultSetMetaData multiResults = this.multiSearch.search(searchers, query, field, caseSensitive);
                return multiResults;

            default:
                throw new IllegalArgumentException("Search principle is not supported.");
        }
    }

    /**
     * {@inheritDoc}
     */
    public ResultSetMetaData search(int[] searchers, String query, int[] fields, int principle, boolean caseSensitive) throws IllegalArgumentException, SearchableException, SearchException {

        switch (principle) {
            case META_SEARCH:
                ResultSetMetaData metaResults = this.metaSearch.search(searchers, query, fields, caseSensitive);
                return metaResults;

            case MULTI_SEARCH:
                ResultSetMetaData multiResults = this.multiSearch.search(searchers, query, fields, caseSensitive);
                return multiResults;

            default:
                throw new IllegalArgumentException("Search principle is not supported.");
        }
    }

    /**
     * {@inheritDoc}
     */
    public ResultSetMetaData search(int[] searchers, String query, String docType, int principle, boolean caseSensitive) throws IllegalArgumentException, SearchableException, SearchException {

        switch (principle) {

            case META_SEARCH:
                ResultSetMetaData metaResults = this.metaSearch.search(searchers, query, docType, caseSensitive);
                return metaResults;

            case MULTI_SEARCH:
                ResultSetMetaData multiResults = this.multiSearch.search(searchers, query, docType, caseSensitive);
                return multiResults;

            default:
                throw new IllegalArgumentException("Search principle is not supported.");
        }

    }

    /**
     * {@inheritDoc}
     */
    public ResultSetMetaData search(int[] searchers, String query, String[] docTypes, int principle, boolean caseSensitive) throws IllegalArgumentException, SearchableException, SearchException {
        switch (principle) {

            case META_SEARCH:
                ResultSetMetaData metaResults = this.metaSearch.search(searchers, query, docTypes, caseSensitive);
                return metaResults;

            case MULTI_SEARCH:
                ResultSetMetaData multiResults = this.multiSearch.search(searchers, query, docTypes, caseSensitive);
                return multiResults;

            default:
                throw new IllegalArgumentException("Search principle is not supported.");
        }
    }

    /**
     * {@inheritDoc}
     */
    public ResultSetMetaData search(int[] searchers, String query, String docType, int field, int principle, boolean caseSensitive) throws IllegalArgumentException, SearchableException, SearchException {

        switch (principle) {

            case META_SEARCH:
                ResultSetMetaData metaResults = this.metaSearch.search(searchers, query, docType, field, caseSensitive);
                return metaResults;

            case MULTI_SEARCH:
                ResultSetMetaData multiResults = this.multiSearch.search(searchers, query, docType, field, caseSensitive);
                return multiResults;

            default:
                throw new IllegalArgumentException("Search principle is not supported.");
        }

    }

    /**
     * {@inheritDoc}
     */
    public ResultSetMetaData search(int[] searchers, String query, String[] docTypes, int field, int principle, boolean caseSensitive) throws IllegalArgumentException, SearchableException, SearchException {
        switch (principle) {

            case META_SEARCH:
                ResultSetMetaData metaResults = this.metaSearch.search(searchers, query, docTypes, field, caseSensitive);
                return metaResults;

            case MULTI_SEARCH:
                ResultSetMetaData multiResults = this.multiSearch.search(searchers, query, docTypes, field, caseSensitive);
                return multiResults;

            default:
                throw new IllegalArgumentException("Search principle is not supported.");
        }
    }

    /**
     * {@inheritDoc}
     */
    public ResultSetMetaData search(int[] searchers, String query, String docType, int[] fields, int principle, boolean caseSensitive) throws IllegalArgumentException, SearchableException, SearchException {

        switch (principle) {

            case META_SEARCH:
                ResultSetMetaData metaResults = this.metaSearch.search(searchers, query, docType, fields, caseSensitive);
                return metaResults;

            case MULTI_SEARCH:
                ResultSetMetaData multiResults = this.multiSearch.search(searchers, query, docType, fields, caseSensitive);
                return multiResults;

            default:
                throw new IllegalArgumentException("Search principle is not supported.");
        }

    }

    /**
     * {@inheritDoc}
     */
    public ResultSetMetaData search(int[] searchers, String query, String[] docTypes, int[] fields, int principle, boolean caseSensitive) throws IllegalArgumentException, SearchableException, SearchException {
        switch (principle) {

            case META_SEARCH:
                ResultSetMetaData metaResults = this.metaSearch.search(searchers, query, docTypes, fields, caseSensitive);
                return metaResults;

            case MULTI_SEARCH:
                ResultSetMetaData multiResults = this.multiSearch.search(searchers, query, docTypes, fields, caseSensitive);
                return multiResults;

            default:
                throw new IllegalArgumentException("Search principle is not supported.");
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////
    /**
     * {@inheritDoc}
     */
    public List<ResultSetMetaData> search(int searcher, int principle, String query, boolean caseSensitive, int members) throws IllegalArgumentException, IllegalArgumentException, SearchException, SearchableException {

        List<ResultSetMetaData> finalResults;

        switch (principle) {
            case SINGLE_SEARCH_AND_SPLIT:

                finalResults = this.singleSearch.search(searcher, query, caseSensitive, members);
                return finalResults;

            case META_SEARCH_AND_SPLIT:
                finalResults = this.metaSearch.search(query, caseSensitive, members);
              

                return finalResults;

            // eliminar repetidos
            case MULTI_SEARCH_AND_SWITCH:

                finalResults = this.multiSearch.search(query, caseSensitive, members);
                return finalResults;

            default:
                throw new IllegalArgumentException("Division of labor principle is not supported.");

        }
    }

    /**
     * {@inheritDoc}
     */
    public List<ResultSetMetaData> search(int searcher, int principle, String query, int field, boolean caseSensitive, int members) throws IllegalArgumentException, IllegalArgumentException, SearchException, SearchableException {
        List<ResultSetMetaData> finalResults;

        switch (principle) {
            case SINGLE_SEARCH_AND_SPLIT:
                finalResults = this.singleSearch.search(searcher, query, field, caseSensitive, members);
                return finalResults;

            case META_SEARCH_AND_SPLIT:

                finalResults = this.metaSearch.search(query, field, caseSensitive, members);
                return finalResults;

            // eliminar repetidos
            case MULTI_SEARCH_AND_SWITCH:

                finalResults = this.multiSearch.search(query, field, caseSensitive, members);
                return finalResults;

            default:
                throw new IllegalArgumentException("Division of labor principle is not supported.");

        }
    }

    /**
     * {@inheritDoc}
     */
    public List<ResultSetMetaData> search(int searcher, int principle, String query, int[] fields, boolean caseSensitive, int members) throws IllegalArgumentException, IllegalArgumentException, SearchException, SearchableException {
        List<ResultSetMetaData> finalResults;

        switch (principle) {
            case SINGLE_SEARCH_AND_SPLIT:
                finalResults = this.singleSearch.search(searcher, query, fields, caseSensitive, members);
                return finalResults;

            case META_SEARCH_AND_SPLIT:

                finalResults = this.metaSearch.search(query, fields, caseSensitive, members);
                return finalResults;

            // eliminar repetidos
            case MULTI_SEARCH_AND_SWITCH:

                finalResults = this.multiSearch.search(query, fields, caseSensitive, members);
                return finalResults;

            default:
                throw new IllegalArgumentException("Division of labor principle is not supported.");

        }
    }

    /**
     * {@inheritDoc}
     */
    public List<ResultSetMetaData> search(int searcher, int principle, String query, String docType, boolean caseSensitive, int members) throws IllegalArgumentException, IllegalArgumentException, SearchException, SearchableException {

        List<ResultSetMetaData> finalResults;

        switch (principle) {
            case SINGLE_SEARCH_AND_SPLIT:

                finalResults = this.singleSearch.search(searcher, query, docType, caseSensitive, members);
                return finalResults;

            case META_SEARCH_AND_SPLIT:

                finalResults = this.metaSearch.search(query, docType, caseSensitive, members);
                return finalResults;

            case MULTI_SEARCH_AND_SWITCH:

                finalResults = this.multiSearch.search(query, docType, caseSensitive, members);
                return finalResults;

            default:
                throw new IllegalArgumentException("Division of labor principle is not supported.");
        }
    }

    /**
     * {@inheritDoc}
     */
    public List<ResultSetMetaData> search(int searcher, int principle, String query, String[] docTypes, boolean caseSensitive, int members) throws IllegalArgumentException, IllegalArgumentException, SearchException, SearchableException {
        List<ResultSetMetaData> finalResults;

        switch (principle) {
            case SINGLE_SEARCH_AND_SPLIT:
                finalResults = this.singleSearch.search(searcher, query, docTypes, caseSensitive, members);
                return finalResults;

            case META_SEARCH_AND_SPLIT:

                finalResults = this.metaSearch.search(query, docTypes, caseSensitive, members);
                return finalResults;

            case MULTI_SEARCH_AND_SWITCH:

                finalResults = this.multiSearch.search(query, docTypes, caseSensitive, members);
                return finalResults;

            default:
                throw new IllegalArgumentException("Division of labor principle is not supported.");
        }
    }

    /**
     * {@inheritDoc}
     */
    public List<ResultSetMetaData> search(int searcher, int principle, String query, String docType, int field, boolean caseSensitive, int members) throws IllegalArgumentException, IllegalArgumentException, SearchException, SearchableException {

        List<ResultSetMetaData> finalResults;

        switch (principle) {
            case SINGLE_SEARCH_AND_SPLIT:
                finalResults = this.singleSearch.search(searcher, query, docType, field, caseSensitive, members);
                return finalResults;

            case META_SEARCH_AND_SPLIT:

                finalResults = this.metaSearch.search(query, docType, field, caseSensitive, members);
                return finalResults;

            case MULTI_SEARCH_AND_SWITCH:

                finalResults = this.multiSearch.search(query, docType, field, caseSensitive, members);
                return finalResults;

            default:
                throw new IllegalArgumentException("Division of labor principle is not supported.");
        }
    }

    /**
     * {@inheritDoc}
     */
    public List<ResultSetMetaData> search(int searcher, int principle, String query, String[] docTypes, int field, boolean caseSensitive, int members) throws IllegalArgumentException, IllegalArgumentException, SearchException, SearchableException {
        List<ResultSetMetaData> finalResults;

        switch (principle) {
            case SINGLE_SEARCH_AND_SPLIT:
                finalResults = this.singleSearch.search(searcher, query, docTypes, field, caseSensitive, members);
                return finalResults;

            case META_SEARCH_AND_SPLIT:

                finalResults = this.metaSearch.search(query, docTypes, field, caseSensitive, members);
                return finalResults;

            case MULTI_SEARCH_AND_SWITCH:

                finalResults = this.multiSearch.search(query, docTypes, field, caseSensitive, members);
                return finalResults;

            default:
                throw new IllegalArgumentException("Division of labor principle is not supported.");
        }
    }

    /**
     * {@inheritDoc}
     */
    public List<ResultSetMetaData> search(int searcher, int principle, String query, String docType, int[] fields, boolean caseSensitive, int members) throws IllegalArgumentException, IllegalArgumentException, SearchException, SearchableException {

        List<ResultSetMetaData> finalResults;

        switch (principle) {
            case SINGLE_SEARCH_AND_SPLIT:

                finalResults = this.singleSearch.search(searcher, query, docType, fields, caseSensitive, members);
                return finalResults;

            case META_SEARCH_AND_SPLIT:

                finalResults = this.metaSearch.search(query, docType, fields, caseSensitive, members);
                return finalResults;

            case MULTI_SEARCH_AND_SWITCH:

                finalResults = this.multiSearch.search(query, docType, fields, caseSensitive, members);
                return finalResults;

            default:
                throw new IllegalArgumentException("Division of labor principle is not supported.");
        }
    }

    /**
     * {@inheritDoc}
     */
    public List<ResultSetMetaData> search(int searcher, int principle, String query, String[] docTypes, int[] fields, boolean caseSensitive, int members) throws IllegalArgumentException, IllegalArgumentException, SearchException, SearchableException {
        List<ResultSetMetaData> finalResults;

        switch (principle) {
            case SINGLE_SEARCH_AND_SPLIT:

                finalResults = this.singleSearch.search(searcher, query, docTypes, fields, caseSensitive, members);
                return finalResults;

            case META_SEARCH_AND_SPLIT:

                finalResults = this.metaSearch.search(query, docTypes, fields, caseSensitive, members);
                return finalResults;

            case MULTI_SEARCH_AND_SWITCH:

                finalResults = this.multiSearch.search(query, docTypes, fields, caseSensitive, members);
                return finalResults;

            default:
                throw new IllegalArgumentException("Division of labor principle is not supported.");
        }
    }

    /**
     * {@inheritDoc}
     */
    public List<ResultSetMetaData> search(int[] searchers, int principle, String query, boolean caseSensitive, int members) throws IllegalArgumentException, IllegalArgumentException, SearchException, SearchableException {

        List<ResultSetMetaData> finalResults;

        switch (principle) {

            case META_SEARCH_AND_SPLIT:

                finalResults = this.metaSearch.search(searchers, query, caseSensitive, members);
                return finalResults;

            case MULTI_SEARCH_AND_SWITCH:

                finalResults = this.multiSearch.search(searchers, query, caseSensitive, members);
                return finalResults;

            default:
                throw new IllegalArgumentException("Division of labor principle is not supported.");
        }
    }

    /**
     * {@inheritDoc}
     */
    public List<ResultSetMetaData> search(int[] searchers, int principle, String query, int field, boolean caseSensitive, int members) throws IllegalArgumentException, IllegalArgumentException, SearchException, SearchableException {
        List<ResultSetMetaData> finalResults;

        switch (principle) {

            case META_SEARCH_AND_SPLIT:

                finalResults = this.metaSearch.search(searchers, query, field, caseSensitive, members);
                return finalResults;

            case MULTI_SEARCH_AND_SWITCH:

                finalResults = this.multiSearch.search(searchers, query, field, caseSensitive, members);
                return finalResults;

            default:
                throw new IllegalArgumentException("Division of labor principle is not supported.");
        }
    }

    /**
     * {@inheritDoc}
     */
    public List<ResultSetMetaData> search(int[] searchers, int principle, String query, int[] fields, boolean caseSensitive, int members) throws IllegalArgumentException, IllegalArgumentException, SearchException, SearchableException {
        List<ResultSetMetaData> finalResults;

        switch (principle) {

            case META_SEARCH_AND_SPLIT:

                finalResults = this.metaSearch.search(searchers, query, fields, caseSensitive, members);
                return finalResults;

            case MULTI_SEARCH_AND_SWITCH:

                finalResults = this.multiSearch.search(searchers, query, fields, caseSensitive, members);
                return finalResults;

            default:
                throw new IllegalArgumentException("Division of labor principle is not supported.");
        }
    }

    /**
     * {@inheritDoc}
     */
    public List<ResultSetMetaData> search(int[] searchers, int principle, String query, String docType, boolean caseSensitive, int members) throws IllegalArgumentException, IllegalArgumentException, SearchException, SearchableException {

        List<ResultSetMetaData> finalResults;

        switch (principle) {

            case META_SEARCH_AND_SPLIT:

                finalResults = this.metaSearch.search(searchers, query, docType, caseSensitive, members);
                return finalResults;

            case MULTI_SEARCH_AND_SWITCH:

                finalResults = this.multiSearch.search(searchers, query, docType, caseSensitive, members);
                return finalResults;

            default:
                throw new IllegalArgumentException("Division of labor principle is not supported.");
        }
    }

    /**
     * {@inheritDoc}
     */
    public List<ResultSetMetaData> search(int[] searchers, int principle, String query, String[] docTypes, boolean caseSensitive, int members) throws IllegalArgumentException, IllegalArgumentException, SearchException, SearchableException {
        List<ResultSetMetaData> finalResults;

        switch (principle) {

            case META_SEARCH_AND_SPLIT:

                finalResults = this.metaSearch.search(searchers, query, docTypes, caseSensitive, members);
                return finalResults;

            case MULTI_SEARCH_AND_SWITCH:

                finalResults = this.multiSearch.search(searchers, query, docTypes, caseSensitive, members);
                return finalResults;

            default:
                throw new IllegalArgumentException("Division of labor principle is not supported.");
        }
    }

    /**
     * {@inheritDoc}
     */
    public List<ResultSetMetaData> search(int[] searchers, int principle, String query, String docType, int field, boolean caseSensitive, int members) throws IllegalArgumentException, IllegalArgumentException, SearchException, SearchableException {

        List<ResultSetMetaData> finalResults;

        switch (principle) {

            case META_SEARCH_AND_SPLIT:

                finalResults = this.metaSearch.search(searchers, query, docType, field, caseSensitive, members);
                return finalResults;

            case MULTI_SEARCH_AND_SWITCH:

                finalResults = this.multiSearch.search(searchers, query, docType, field, caseSensitive, members);
                return finalResults;

            default:
                throw new IllegalArgumentException("Division of labor principle is not supported.");
        }
    }

    /**
     * {@inheritDoc}
     *
     */
    public List<ResultSetMetaData> search(int[] searchers, int principle, String query, String docType, int[] fields, boolean caseSensitive, int members) throws IllegalArgumentException, IllegalArgumentException, SearchException, SearchableException {

        List<ResultSetMetaData> finalResults;

        switch (principle) {

            case META_SEARCH_AND_SPLIT:

                finalResults = this.metaSearch.search(searchers, query, docType, fields, caseSensitive, members);
                return finalResults;

            case MULTI_SEARCH_AND_SWITCH:

                finalResults = this.multiSearch.search(searchers, query, docType, fields, caseSensitive, members);
                return finalResults;

            default:
                throw new IllegalArgumentException("Division of labor principle is not supported.");
        }
    }

    /**
     * {@inheritDoc}
     *
     */
    public List<ResultSetMetaData> search(int[] searchers, int principle, String query, String[] docTypes, int field, boolean caseSensitive, int members) throws IllegalArgumentException, IllegalArgumentException, SearchException, SearchableException {
        List<ResultSetMetaData> finalResults;

        switch (principle) {

            case META_SEARCH_AND_SPLIT:

                finalResults = this.metaSearch.search(searchers, query, docTypes, field, caseSensitive, members);
                return finalResults;

            case MULTI_SEARCH_AND_SWITCH:

                finalResults = this.multiSearch.search(searchers, query, docTypes, field, caseSensitive, members);
                return finalResults;

            default:
                throw new IllegalArgumentException("Division of labor principle is not supported.");
        }

    }

    /**
     * {@inheritDoc}
     *
     */
    public List<ResultSetMetaData> search(int[] searchers, int principle, String query, String[] docTypes, int[] fields, boolean caseSensitive, int members) throws IllegalArgumentException, IllegalArgumentException, SearchException, SearchableException {
        List<ResultSetMetaData> finalResults;

        switch (principle) {

            case META_SEARCH_AND_SPLIT:

                finalResults = this.metaSearch.search(searchers, query, docTypes, fields, caseSensitive, members);
                return finalResults;

            case MULTI_SEARCH_AND_SWITCH:

                finalResults = this.multiSearch.search(searchers, query, docTypes, fields, caseSensitive, members);
                return finalResults;

            default:
                throw new IllegalArgumentException("Division of labor principle is not supported.");
        }
    }
////////////////////////////////////////
    
    //SVN SEARCH
     public ResultSetMetaData search(String query,SVNData data, String fileType, String sort, String lastmodified, String user,boolean fileBody) throws IllegalArgumentException, SearchableException, SearchException {

        
           ResultSetMetaData finalResults = this.singleSearch.search(query,  data,  fileType,  sort,  lastmodified, user,fileBody);


           return finalResults;
  
    }

    public MetaSearch getMetaSearch() {
        return metaSearch;
    }

    public void setMetaSearch(MetaSearch metaSearch) {
        this.metaSearch = metaSearch;
    }

    public MultiSearch getMultiSearch() {
        return multiSearch;
    }

    public void setMultiSearch(MultiSearch multiSearch) {
        this.multiSearch = multiSearch;
    }

    public SingleSearch getSingleSearch() {
        return singleSearch;
    }

    public void setSingleSearch(SingleSearch singleSearch) {
        this.singleSearch = singleSearch;
    }




}
