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
import drakkar.mast.SearchException;
import drakkar.mast.SearchableException;
import drakkar.mast.retrieval.Searchable;
import java.util.List;
import java.util.Map;

/**
 * Esta clase maneja los diferentes métodos de búsquedas colaborativas ó no, soportadas 
 * por DrakkarKeel para los principios de búsquedas: MultiSearch y MultiSearch and Split
 *
 * 
 */
public class MultiSearch implements ResultsSetImprovable {

    private DefaultMultiSearch dMultiSearch;
    private CollaborativeMultiSearch cMultiSearch;

     /**
     * Constructor de la clase
     *
     * @param searchers lista de buscadores
     */
    public MultiSearch(Map<Integer, Searchable> searcherHash, List<Searchable> searchersList) {
        this.dMultiSearch = new DefaultMultiSearch(searcherHash, searchersList);
        this.cMultiSearch = new CollaborativeMultiSearch(dMultiSearch);
    }

    /**
     * {@inheritDoc}
     */
    public ResultSetMetaData search(String query, boolean caseSensitive) throws SearchException {
        return this.dMultiSearch.search(query, caseSensitive);
    }

    /**
     * {@inheritDoc}
     */
    public ResultSetMetaData search(String query, int field, boolean caseSensitive) throws SearchException {
        return this.dMultiSearch.search(query, field, caseSensitive);
    }

    /**
     * {@inheritDoc}
     */
    public ResultSetMetaData search(String query, int[] fields, boolean caseSensitive) throws SearchException {
        return this.dMultiSearch.search(query, fields, caseSensitive);
    }

    /**
     * {@inheritDoc}
     */
    public ResultSetMetaData search(String query, String docType, boolean caseSensitive) throws SearchException {
        return this.dMultiSearch.search(query, docType, caseSensitive);
    }

    /**
     * {@inheritDoc}
     */
    public ResultSetMetaData search(String query, String[] docTypes, boolean caseSensitive) throws SearchException {
        return this.dMultiSearch.search(query, docTypes, caseSensitive);
    }

    /**
     * {@inheritDoc}
     */
    public ResultSetMetaData search(String query, String docType, int field, boolean caseSensitive) throws SearchException {
        return this.dMultiSearch.search(query, docType, field, caseSensitive);
    }

    /**
     * {@inheritDoc}
     */
    public ResultSetMetaData search(String query, String docType, int[] fields, boolean caseSensitive) throws SearchException {
        return this.dMultiSearch.search(query, docType, fields, caseSensitive);
    }

    /**
     * {@inheritDoc}
     */
    public ResultSetMetaData search(String query, String[] docTypes, int field, boolean caseSensitive) throws SearchException {
        return this.dMultiSearch.search(query, docTypes, field, caseSensitive);
    }

    /**
     * {@inheritDoc}
     */
    public ResultSetMetaData search(String query, String[] docTypes, int[] fields, boolean caseSensitive) throws SearchException {
        return this.dMultiSearch.search(query, docTypes, fields, caseSensitive);
    }

    //---   -   -   -   -   --  -   -   -   -   -   -   -   -   -   --  -   -   -   -   //
    /**
     * {@inheritDoc}
     */
    public ResultSetMetaData search(int[] searchers, String query, boolean caseSensitive) throws SearchableException, SearchException {
        return this.dMultiSearch.search(searchers, query, caseSensitive);
    }

    /**
     * {@inheritDoc}
     */
    public ResultSetMetaData search(int[] searchers, String query, int field, boolean caseSensitive) throws SearchableException, SearchException {
        return this.dMultiSearch.search(searchers, query, field, caseSensitive);
    }

    /**
     * {@inheritDoc}
     */
    public ResultSetMetaData search(int[] searchers, String query, int[] fields, boolean caseSensitive) throws SearchableException, SearchException {
        return this.dMultiSearch.search(searchers, query, fields, caseSensitive);
    }

    /**
     * {@inheritDoc}
     */
    public ResultSetMetaData search(int[] searchers, String query, String docType, boolean caseSensitive) throws SearchableException, SearchException {
        return this.dMultiSearch.search(searchers, query, docType, caseSensitive);
    }

    /**
     * {@inheritDoc}
     */
    public ResultSetMetaData search(int[] searchers, String query, String[] docTypes, boolean caseSensitive) throws SearchableException, SearchException {
        return this.dMultiSearch.search(searchers, query, docTypes, caseSensitive);
    }

    /**
     * {@inheritDoc}
     */
    public ResultSetMetaData search(int[] searchers, String query, String docType, int field, boolean caseSensitive) throws SearchableException, SearchException {
        return this.dMultiSearch.search(searchers, query, docType, field, caseSensitive);
    }

    /**
     * {@inheritDoc}
     */
    public ResultSetMetaData search(int[] searchers, String query, String docType, int[] fields, boolean caseSensitive) throws SearchableException, SearchException {
        return this.dMultiSearch.search(searchers, query, docType, fields, caseSensitive);
    }

    /**
     * {@inheritDoc}
     */
    public ResultSetMetaData search(int[] searchers, String query, String[] docTypes, int field, boolean caseSensitive) throws SearchableException, SearchException {
        return this.dMultiSearch.search(searchers, query, docTypes, field, caseSensitive);
    }

    /**
     * {@inheritDoc}
     */
    public ResultSetMetaData search(int[] searchers, String query, String[] docTypes, int[] fields, boolean caseSensitive) throws SearchableException, SearchException {
        return this.dMultiSearch.search(searchers, query, docTypes, fields, caseSensitive);
    }

    //--    -   -   -   -   -   -   --  -   -   -   -   -   -   -   -   -   -   -   //
    /**
     * {@inheritDoc}
     */
    public List<ResultSetMetaData> search(String query, boolean caseSensitive, int members) throws SearchException {
        return this.cMultiSearch.search(query, caseSensitive, members);
    }

    /**
     * {@inheritDoc}
     */
    public List<ResultSetMetaData> search(String query, int field, boolean caseSensitive, int members) throws SearchException {
        return this.cMultiSearch.search(query, field, caseSensitive, members);
    }

    /**
     * {@inheritDoc}
     */
    public List<ResultSetMetaData> search(String query, int[] fields, boolean caseSensitive, int members) throws SearchException {
        return this.cMultiSearch.search(query, fields, caseSensitive, members);
    }

    /**
     * {@inheritDoc}
     */
    public List<ResultSetMetaData> search(String query, String docType, boolean caseSensitive, int members) throws SearchException {
        return this.cMultiSearch.search(query, docType, caseSensitive, members);
    }

    /**
    /**
     * {@inheritDoc}
     */
    public List<ResultSetMetaData> search(String query, String[] docTypes, boolean caseSensitive, int members) throws SearchException {
        return this.cMultiSearch.search(query, docTypes, caseSensitive, members);
    }

    /**
     * {@inheritDoc}
     */
    public List<ResultSetMetaData> search(String query, String docType, int field, boolean caseSensitive, int members) throws SearchException {
        return this.cMultiSearch.search(query, docType, field, caseSensitive, members);
    }

    /**
     * {@inheritDoc}
     */
    public List<ResultSetMetaData> search(String query, String docType, int[] fields, boolean caseSensitive, int members) throws SearchException {
        return this.cMultiSearch.search(query, docType, fields, caseSensitive, members);
    }

    /**
    /**
     * {@inheritDoc}
     */
    public List<ResultSetMetaData> search(String query, String[] docTypes, int field, boolean caseSensitive, int members) throws SearchException {
        return this.cMultiSearch.search(query, docTypes, field, caseSensitive, members);
    }

    /**
    /**
     * {@inheritDoc}
     */
    public List<ResultSetMetaData> search(String query, String[] docTypes, int[] fields, boolean caseSensitive, int members) throws SearchException {
        return this.cMultiSearch.search(query, docTypes, fields, caseSensitive, members);
    }

    //- --  -   -   -   -   -   -   -   -   --      --  -       -   -   -   --  -   //
    /**
    /**
     * {@inheritDoc}
     */
    public List<ResultSetMetaData> search(int[] searchers, String query, boolean caseSensitive, int members) throws  SearchException, SearchableException {
        return this.cMultiSearch.search(searchers, query, caseSensitive, members);
    }

    /**
     * {@inheritDoc}
     */
    public List<ResultSetMetaData> search(int[] searchers, String query, int field, boolean caseSensitive, int members) throws  SearchException, SearchableException {
        return this.cMultiSearch.search(searchers, query, field, caseSensitive, members);
    }

    /**
     * {@inheritDoc}
     */
    public List<ResultSetMetaData> search(int[] searchers, String query, int[] fields, boolean caseSensitive, int members) throws SearchException, SearchableException {
        return this.cMultiSearch.search(searchers, query, fields, caseSensitive, members);
    }

    /**
     * {@inheritDoc}
     */
    public List<ResultSetMetaData> search(int[] searchers, String query, String docType, boolean caseSensitive, int members) throws  SearchException, SearchableException {
        return this.cMultiSearch.search(searchers, query, docType, caseSensitive, members);
    }

    /**
     * {@inheritDoc}
     */
    public List<ResultSetMetaData> search(int[] searchers, String query, String[] docTypes, boolean caseSensitive, int members) throws  SearchException, SearchableException {
        return this.cMultiSearch.search(searchers, query, docTypes, caseSensitive, members);
    }

    /**
     * {@inheritDoc}
     */
    public List<ResultSetMetaData> search(int[] searchers, String query, String docType, int field, boolean caseSensitive, int members) throws  SearchException, SearchableException {
        return this.cMultiSearch.search(searchers, query, docType, field, caseSensitive, members);
    }

    /**
     * {@inheritDoc}
     */
    public List<ResultSetMetaData> search(int[] searchers, String query, String docType, int[] fields, boolean caseSensitive, int members) throws  SearchException, SearchableException {
        return this.cMultiSearch.search(searchers, query, docType, fields, caseSensitive, members);
    }

    /**
     * {@inheritDoc}
     */
    public List<ResultSetMetaData> search(int[] searchers, String query, String[] docTypes, int field, boolean caseSensitive, int members) throws  SearchException, SearchableException {
        return this.cMultiSearch.search(searchers, query, docTypes, field, caseSensitive, members);
    }

    /**
     * {@inheritDoc}
     */
    public List<ResultSetMetaData> search(int[] searchers, String query, String[] docTypes, int[] fields, boolean caseSensitive, int members) throws  SearchException, SearchableException {
        return this.cMultiSearch.search(searchers, query, docTypes, fields, caseSensitive, members);
    }
}
