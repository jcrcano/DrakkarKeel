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

import drakkar.oar.ResultSetMetaData;
import drakkar.oar.svn.SVNData;
import drakkar.mast.SearchException;
import drakkar.mast.SearchableException;
import drakkar.mast.retrieval.Searchable;
import java.util.List;
import java.util.Map;

/**
 * Esta clase maneja los diferentes métodos de búsquedas colaborativas ó no,soportadas
 * por DrakkarKeel para los principios de búsquedas: SingleSearch y SingleSearch and Split
 *
 * 
 *
 */
public class SingleSearch implements ResultsImprovable {

    private DefaultSingleSearch dSingleSearch;
    private CollaborativeSingleSearch cSingleSearch;


     /**
     *
      * @param searcherHash
      * @param searchersList
     */
    public SingleSearch(Map<Integer, Searchable> searcherHash,List<Searchable> searchersList) {
        this.dSingleSearch = new DefaultSingleSearch(searcherHash,searchersList);
        this.cSingleSearch = new CollaborativeSingleSearch(dSingleSearch);
    }

    /**
     * {@inheritDoc}
     */
    public ResultSetMetaData search(String query, int searcher, boolean caseSensitive) throws SearchableException, SearchException {
        return this.dSingleSearch.search(query, searcher, caseSensitive);
    }

    /**
     * {@inheritDoc}
     */
    public ResultSetMetaData search(String query, int field, int searcher, boolean caseSensitive) throws SearchableException, SearchException {
        return this.dSingleSearch.search(query, field, searcher, caseSensitive);
    }

    /**
     * {@inheritDoc}
     */
    public ResultSetMetaData search(String query, int[] fields, int searcher, boolean caseSensitive) throws SearchableException, SearchException {
        return this.dSingleSearch.search(query, fields, searcher, caseSensitive);
    }

    /**
     * {@inheritDoc}
     */
    public ResultSetMetaData search(String query, String docType, int searcher, boolean caseSensitive) throws SearchableException, SearchException {
        return this.dSingleSearch.search(query, docType, searcher, caseSensitive);
    }

    /**
     * {@inheritDoc}
     */
    public ResultSetMetaData search(String query, String[] docTypes, int searcher, boolean caseSensitive) throws SearchableException, SearchException {
        return this.dSingleSearch.search(query, docTypes, searcher, caseSensitive);
    }

    /**
     * {@inheritDoc}
     */
    public ResultSetMetaData search(String query, String docType, int field, int searcher, boolean caseSensitive) throws SearchableException, SearchException {
        return this.dSingleSearch.search(query, docType, field, searcher, caseSensitive);
    }

    /**
     * {@inheritDoc}
     */
    public ResultSetMetaData search(String query, String docType, int[] fields, int searcher, boolean caseSensitive) throws SearchableException, SearchException {
        return this.dSingleSearch.search(query, docType, fields, searcher, caseSensitive);
    }

    /**
     * {@inheritDoc}
     */
    public ResultSetMetaData search(String query, String[] docTypes, int field, int searcher, boolean caseSensitive) throws SearchableException, SearchException {
        return this.dSingleSearch.search(query, docTypes, field, searcher, caseSensitive);
    }

    /**
     * {@inheritDoc}
     */
    public ResultSetMetaData search(String query, String[] docTypes, int[] fields, int searcher, boolean caseSensitive) throws SearchableException, SearchException {
        return this.dSingleSearch.search(query, docTypes, fields, searcher, caseSensitive);
    }
/////////////////
    /**
     *
     * @param query
     * @param svnRepository
     * @param fileType
     * @param sort
     * @param lastmodified
     * @param user
     * @param fileBody
     * @return
     * @throws SearchableException
     * @throws SearchException
     */
    public ResultSetMetaData search(String query, SVNData svnRepository, String fileType, String sort, String lastmodified, String user,boolean fileBody) throws SearchableException, SearchException {
        return this.dSingleSearch.search(query,  svnRepository,  fileType,  sort,  lastmodified, user,fileBody);
    }




    /**
     * {@inheritDoc}
     * @throws IllegalArgumentException
     */
    public List<ResultSetMetaData> search(int searcher, String query, boolean caseSensitive, int members) throws IllegalArgumentException, SearchException, SearchableException {
        return this.cSingleSearch.search(searcher, query, caseSensitive, members);
    }

    public List<ResultSetMetaData> search(int searcher, String query, int field, boolean caseSensitive, int members) throws IllegalArgumentException, SearchException, SearchableException {
        return this.cSingleSearch.search(searcher, query, field, caseSensitive, members);
    }

    /**
     * {@inheritDoc}
     * @throws IllegalArgumentException
     */
    public List<ResultSetMetaData> search(int searcher, String query, int[] fields, boolean caseSensitive, int members) throws IllegalArgumentException, SearchException, SearchableException {
        return this.cSingleSearch.search(searcher, query, fields, caseSensitive, members);
    }

    /**
     * {@inheritDoc}
     * @throws IllegalArgumentException
     */
    public List<ResultSetMetaData> search(int searcher, String query, String docType, boolean caseSensitive, int members) throws IllegalArgumentException, SearchException, SearchableException {
        return this.cSingleSearch.search(searcher, query, docType, caseSensitive, members);
    }

    /**
     * {@inheritDoc}
     * @throws IllegalArgumentException
     */
    public List<ResultSetMetaData> search(int searcher, String query, String[] docTypes, boolean caseSensitive, int members) throws IllegalArgumentException, SearchException, SearchableException {
        return this.cSingleSearch.search(searcher, query, docTypes, caseSensitive, members);
    }

    /**
     * {@inheritDoc}
     * @throws IllegalArgumentException
     */
    public List<ResultSetMetaData> search(int searcher, String query, String docType, int field, boolean caseSensitive, int members) throws IllegalArgumentException, SearchException, SearchableException {
        return this.cSingleSearch.search(searcher, query, docType, field, caseSensitive, members);
    }

    /**
     * {@inheritDoc}
     * @throws IllegalArgumentException
     */
    public List<ResultSetMetaData> search(int searcher, String query, String docType, int[] fields, boolean caseSensitive, int members) throws IllegalArgumentException, SearchException, SearchableException {
        return this.cSingleSearch.search(searcher, query, docType, fields, caseSensitive, members);
    }

    /**
     * {@inheritDoc}
     * @throws IllegalArgumentException
     */
    public List<ResultSetMetaData> search(int searcher, String query, String[] docTypes, int field, boolean caseSensitive, int members) throws IllegalArgumentException, SearchException, SearchableException {
        return this.cSingleSearch.search(searcher, query, docTypes, field, caseSensitive, members);
    }

    /**
     * {@inheritDoc}
     * @throws IllegalArgumentException
     */
    public List<ResultSetMetaData> search(int searcher, String query, String[] docTypes, int[] fields, boolean caseSensitive, int members) throws IllegalArgumentException, SearchException, SearchableException {
        return this.cSingleSearch.search(searcher, query, docTypes, fields, caseSensitive, members);
    }

    /**
     *
     * @return
     */
    public CollaborativeSingleSearch getCollaborativeSingleSearch() {
        return cSingleSearch;
    }

    /**
     *
     * @param cSingleSearch
     */
    public void setCollaborativeSingleSearch(CollaborativeSingleSearch cSingleSearch) {
        this.cSingleSearch = cSingleSearch;
    }

    /**
     *
     * @return
     */
    public DefaultSingleSearch getDefaultSingleSearch() {
        return dSingleSearch;
    }

    /**
     *
     * @param dSingleSearch
     */
    public void setDefaultSingleSearch(DefaultSingleSearch dSingleSearch) {
        this.dSingleSearch = dSingleSearch;
    }




}
