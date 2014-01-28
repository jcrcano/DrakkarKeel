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

import drakkar.oar.DocSuggest;
import drakkar.oar.ResultSetDocument;
import drakkar.oar.ResultSetTerm;
import drakkar.oar.TermSuggest;
import drakkar.oar.util.KeySearchable;
import drakkar.oar.util.OutputMonitor;
import drakkar.mast.recommender.TermDocSuggest;
import drakkar.mast.retrieval.CVSSearch;
import drakkar.mast.retrieval.EngineContext;
import drakkar.mast.retrieval.SearchEngine;
import drakkar.mast.retrieval.Searchable;
import drakkar.mast.retrieval.WebCrawler;
import drakkar.mast.retrieval.improves.RankingFusion;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * Esta clase constituye el administrador de los diferentes buscadores, es la encargada
 * de invocar las diferentes operaciones soportadas por cada uno de los buscadores
 *
 * 
 */
public class RetrievalManager {

    private File collectionPath, indexPath;
    private Map<Integer, Searchable> searcherMap;
    private List<Searchable> searchersList;
    private IndexerManager indexManager;
    private SearcherManager searcherManager;
    /**
     *
     */
    public static final int INDIVIDUAL_SEARCH = 0;
    /**
     * 
     */
    public static final int COLLABORATIVE_SEARCH = 1;

    /**
     * 
     */
    public RetrievalManager() {
        super();
        this.searcherMap = new HashMap<Integer, Searchable>();
        this.searchersList = new ArrayList<Searchable>();
        this.searcherManager = new SearcherManager(searcherMap, searchersList);
        this.indexManager = new IndexerManager(searcherMap, searchersList);


    }

    /**
     * Constructor de la clase
     *
     * @param searchers listado de buscadores
     */
    public RetrievalManager(Searchable[] searchers) {

        this.searcherMap = new HashMap<Integer, Searchable>();
        this.searchersList = new ArrayList<Searchable>();
        for (Searchable item : searchers) {
            if (item != null) {
                this.searchersList.add(item);
                this.searcherMap.put(item.getID(), item);

            }
        }

        this.searcherManager = new SearcherManager(searcherMap, searchersList);
        this.indexManager = new IndexerManager(searcherMap, searchersList);
    }

    /**
     * Constructor de la clase
     *
     * @param searchers listado de buscadores
     */
    public RetrievalManager(List<Searchable> searchers) {

        this.searcherMap = new HashMap<Integer, Searchable>();
        this.searchersList = new ArrayList<Searchable>();

        for (Searchable item : searchers) {
            if (item != null) {
                this.searchersList.add(item);
                this.searcherMap.put(item.getID(), item);

            }
        }

        this.searcherManager = new SearcherManager(searcherMap, searchersList);
        this.indexManager = new IndexerManager(searcherMap, searchersList);


    }

    /**
     * Devuelve una lista de sugerencias de términos de consulta, apartir
     * de la consulta especificada.
     *
     * @param query términos de la consulta de búsqueda
     *
     * @return  términos sugeridos
     */
    public List<TermSuggest> getTermsSuggest(String query) {
        List<List<TermSuggest>> list = new ArrayList<List<TermSuggest>>();
        boolean enable;
        Searchable item;
        SearchEngine searcher;
        EngineContext context;
        Set<Integer> searherSet = this.searcherMap.keySet();
        for (Integer searcherItem : searherSet) {
            item = this.searcherMap.get(searcherItem);
            enable = item.isEnabled();
            if (enable) {
                if (item instanceof SearchEngine) {
                    searcher = (SearchEngine) item;
                    context = (EngineContext) searcher.getContext();
                    if (context.isApplyLSI()) {
                        list.add(context.getLSIManager().getTermsSuggest(query));
                    }
                }
            }
        }

        List<TermSuggest> finalList = null;
        int size = list.size();
        if (size > 1) {
            finalList = RankingFusion.termsFusion(new ResultSetTerm(query, list), RankingFusion.DEFAULT_NORMALIZE, RankingFusion.DEFAULT_COMBINER);
        } else if (size == 1) {
            finalList = list.get(0);
        } else {
            finalList = new ArrayList<TermSuggest>();
        }


        return finalList;

    }

    /**
     * Devuelve una lista de sugerencias de documentos, apartir de la consulta especificada.
     *
     * @param query términos de la consulta de búsqueda
     *
     * @return  documentos sugeridos
     */
    public List<DocSuggest> getDocsSuggest(String query) {

        List<List<DocSuggest>> list = new ArrayList<List<DocSuggest>>();
        boolean enable;
        Searchable item;
        SearchEngine searcher;
        EngineContext context;
        Set<Integer> searherSet = this.searcherMap.keySet();
        for (Integer searcherItem : searherSet) {
            item = this.searcherMap.get(searcherItem);
            enable = item.isEnabled();
            if (enable) {
                if (item instanceof SearchEngine) {
                    searcher = (SearchEngine) item;
                    context = (EngineContext) searcher.getContext();
                    if (context.isApplyLSI()) {
                        list.add(context.getLSIManager().getDocsSuggest(query));
                    }
                }
            }
        }

        List<DocSuggest> finalList = null;
        int size = list.size();
        if (size > 1) {
            finalList = RankingFusion.docsFusion(new ResultSetDocument(query, list), RankingFusion.DEFAULT_NORMALIZE, RankingFusion.DEFAULT_COMBINER);
        } else if (size == 1) {
            finalList = list.get(0);
        } else {
            finalList = new ArrayList<DocSuggest>();
        }


        return finalList;
    }

    /**
     * Devuelve una lista de sugerencias de términos de consulta y documentos, apartir
     * de la consulta especificada.
     *
     * @param query términos de la consulta de búsqueda
     *
     * @return términos y documentos sugeridos
     */
    public TermDocSuggest getTermsDocsSuggest(String query) {
        List<DocSuggest> docs = getDocsSuggest(query);
        List<TermSuggest> terms = getTermsSuggest(query);

        TermDocSuggest termsDocsSuggest = new TermDocSuggest(terms, docs);

        return termsDocsSuggest;
    }

    /**
     * Devuelve una lista de sugerencias de términos de consulta, apartir
     * de la consulta especificada.
     *
     * @param query términos de la consulta de búsqueda
     * @param searhersID
     * @return  términos sugeridos
     */
    public List<TermSuggest> getTermsSuggest(String query, int[] searhersID) {
        List<List<TermSuggest>> list = new ArrayList<List<TermSuggest>>();
        boolean enable;
        Searchable item;
        SearchEngine searcher;
        EngineContext context;

        for (int searher : searhersID) {
            if (searcherMap.containsKey(searher)) {
                item = this.searcherMap.get(searher);
                enable = item.isEnabled();
                if (enable) {
                    if (item instanceof SearchEngine) {
                        searcher = (SearchEngine) item;
                        context = (EngineContext) searcher.getContext();
                        if (context.isApplyLSI()) {
                            list.add(context.getLSIManager().getTermsSuggest(query));
                        }
                    }
                }

            }

        }

       List<TermSuggest> finalList = null;
        int size = list.size();
        if (size > 1) {
            finalList = RankingFusion.termsFusion(new ResultSetTerm(query, list), RankingFusion.DEFAULT_NORMALIZE, RankingFusion.DEFAULT_COMBINER);
        } else if (size == 1) {
            finalList = list.get(0);
        } else {
            finalList = new ArrayList<TermSuggest>();
        }



        return finalList;
    }

    /**
     * Devuelve una lista de sugerencias de documentos, apartir de la consulta especificada.
     *
     * @param query términos de la consulta de búsqueda
     *
     * @param searhersID
     * @return  documentos sugeridos
     */
    public List<DocSuggest> getDocsSuggest(String query, int[] searhersID) {

        List<List<DocSuggest>> list = new ArrayList<List<DocSuggest>>();
        boolean enable;
        Searchable item;
        SearchEngine searcher;
        EngineContext context;

        for (int searher : searhersID) {
            if (searcherMap.containsKey(searher)) {
                item = this.searcherMap.get(searher);
                enable = item.isEnabled();
                if (enable) {
                    if (item instanceof SearchEngine) {
                        searcher = (SearchEngine) item;
                        context = (EngineContext) searcher.getContext();
                        if (context.isApplyLSI()) {
                            list.add(context.getLSIManager().getDocsSuggest(query));
                        }
                    }
                }

            }

        }

       List<DocSuggest> finalList = null;
        int size = list.size();
        if (size > 1) {
            finalList = RankingFusion.docsFusion(new ResultSetDocument(query, list), RankingFusion.DEFAULT_NORMALIZE, RankingFusion.DEFAULT_COMBINER);
        } else if (size == 1) {
            finalList = list.get(0);
        } else {
            finalList = new ArrayList<DocSuggest>();
        }


        return finalList;
    }

    /**
     * Devuelve una lista de sugerencias de términos de consulta y documentos, apartir
     * de la consulta especificada.
     *
     * @param query términos de la consulta de búsqueda
     *
     * @param searhersID
     * @return términos y documentos sugeridos
     */
    public TermDocSuggest getTermsDocsSuggest(String query, int[] searhersID) {

        List<DocSuggest> docs = getDocsSuggest(query, searhersID);
        List<TermSuggest> terms = getTermsSuggest(query, searhersID);

        TermDocSuggest termsDocsSuggest = new TermDocSuggest(terms, docs);

        return termsDocsSuggest;
    }

    /**
     * Devuelve un arreglo con la lista de los nombres de búscadores disponibles
     *
     * @return nombre de los buscadores
     */
    public String[] getAvailableSearchers() {
        StringBuilder searchers = new StringBuilder();
        String[] availableSearchers;
        boolean enable;
        Searchable item;
        Set<Integer> searcherSet = this.searcherMap.keySet();
        for (Integer searcherItem : searcherSet) {
            item = this.searcherMap.get(searcherItem);
            enable = item.isEnabled();
            if (enable) {
                if (item instanceof SearchEngine) {
                    searchers.append(((SearchEngine) item).getName()).append(",");
                }
            }
        }

        String searcherList = searchers.toString();
        if (!searcherList.isEmpty()) {
            availableSearchers = searcherList.split(",");
        } else {
            availableSearchers = new String[0];
        }

        return availableSearchers;
    }

    /**
     * Devuelve el total de buscadores disponibles
     *
     * @return nombre de los buscadores
     */
    public int getCountAvailableSearchers() {
        int count = 0;
        Set<Integer> searcherEnum = this.searcherMap.keySet();
        Searchable item;
        boolean enable = false;

        for (Integer searcherItem : searcherEnum) {
            item = this.searcherMap.get(searcherItem);
            enable = item.isEnabled();
            if (enable) {
                if (item instanceof SearchEngine) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     *
     * @param type
     * @return
     */
    public List<String> getSearchPrinciples(int type) {
        List<String> principles = new ArrayList<String>();
        int searchersCount = getCountAvailableSearchers();

        if (searchersCount > 0) {
            principles.add("Single Search");
            switch (type) {
                case INDIVIDUAL_SEARCH:
                    if (searchersCount > 1) {
                        principles.add("Meta Search");
                        principles.add("Multi Search");
                    }

                    return principles;

                case COLLABORATIVE_SEARCH:

                    principles.add("Single Search and Split");
                    if (searchersCount > 1) {
                        principles.add(1, "Meta Search");
                        principles.add(2, "Multi Search");
                        principles.add("Meta Search and Split");
                        principles.add("Multi Search and Switch");
                    }

                    return principles;

            }

        }


        return principles;



    }

    /**
     * Devuelve los repositorios SVN disponibles para la búsqueda
     *
     * @return
     */
    public String[] getAvailableSVNRepositories() {
        StringBuilder repositories = new StringBuilder();
        String[] availableSearchers;
        boolean enable;
        Searchable item;
        Set<Integer> searherSet = this.searcherMap.keySet();
        for (Integer searcherItem : searherSet) {
            item = this.searcherMap.get(searcherItem);
            enable = item.isEnabled();
            if (enable) {
                if (item instanceof CVSSearch) {
                    CVSSearch cVSSearch = (CVSSearch) item;
                    repositories = cVSSearch.getRepositoriesEnabled();
                }
            }
        }

        String searcherList = repositories.toString();
        if (!searcherList.isEmpty()) {
            availableSearchers = searcherList.split(",");
        } else {
            availableSearchers = new String[0];
        }

        return availableSearchers;
    }

    /**
     * Determina la disponibilidad del buscador
     *
     * @param searcher buscador
     *
     * @return true si el buscador se encuantra activo, false en caso contrario
     *
     * <br>
     * <br>
     * <b>Nota:</b>
     * <br>
     * Las constantes que representan los diferentes buscadores soportados, se encuentran
     * definidas en la clase <code>KeySearchable</code>, del paquete drakkar.oar.util
     * <br>
     * <br>
     *
     * @see KeySearchable
     *
     */
    public boolean isSearchableEnabled(int searcher) {
        boolean enable = this.isEnabled(searcher);
        return enable;
    }

    /**
     * Establece la disponibilidad del buscador especificado
     *
     * @param searcher buscador
     * @param enable   disponibilidad, true disponible, false no disponible
     *
     * <br>
     * <br>
     * <b>Nota:</b>
     * <br>
     * Las constantes que representan los diferentes buscadores soportados, se encuentran
     * definidas en la clase <code>KeySearchable</code>, del paquete drakkar.oar.util
     * <br>
     * <br>
     *
     * @throws SearchableNotSupportedException 
     * @see KeySearchable
     *
     */
    public void setSearchableEnabled(int searcher, boolean enable) throws SearchableNotSupportedException {
        this.setEnabled(searcher, enable);
    }

    /**
     * Establece el índice correspondiente a un buscador
     *
     * @param searcher buscador
     * @param uriIndex dirección del índice
     *
     * <br>
     * <br>
     * <b>Nota:</b>
     * <br>
     * Las constantes que representan los diferentes buscadores soportados, se encuentran
     * definidas en la clase <code>KeySearchable</code>, del paquete drakkar.oar.util
     * <br>
     * <br>
     *
     * @throws FileNotFoundException
     * @see KeySearchable
     *
     */
    public void setSearchableIndex(int searcher, String uriIndex) throws FileNotFoundException {
        setIndex(searcher, uriIndex);
    }

    private void setIndex(int searcher, String uriIndex) throws FileNotFoundException {

        Searchable searchable = this.searcherMap.get(searcher);
        if (searchable instanceof SearchEngine) {
            SearchEngine item = (SearchEngine) searchable;
            item.setIndexPath(uriIndex);
        } else if (searchable instanceof WebCrawler) {
            WebCrawler item = (WebCrawler) searchable;
//            item.setIndexPath(uriIndex);
        } else {
            OutputMonitor.printStream("Method setIndex(...).", new SearchableNotSupportedException("The searchable is not supported"));
        }

    }

    /**
     * Actualiza los indices SVN
     *
     * @param searcher
     * @param uriIndex
     * @param enable     indica si el indice esta activo o no
     * @throws FileNotFoundException
     */
    public void setSearchableSVNIndex(int searcher, String uriIndex, boolean enable) throws FileNotFoundException {
        Searchable searchable = this.searcherMap.get(searcher);

        if (searchable instanceof CVSSearch) {
            CVSSearch item = (CVSSearch) searchable;
            item.setIndexPath(uriIndex, enable);

        }
    }

    /**
     * Establece el índice correspondiente a un buscador
     *
     * @param searcher buscador
     * @param uriIndex dirección del índice
     *
     * <br>
     * <br>
     * <b>Nota:</b>
     * <br>
     * Las constantes que representan los diferentes buscadores soportados, se encuentran
     * definidas en la clase <code>KeySearchable</code>, del paquete drakkar.oar.util
     * <br>
     * <br>
     *
     * @see KeySearchable
     *
     */
    public void setSearchableCollection(int searcher, String uriIndex) {
        setCollection(searcher, uriIndex);
    }

    private void setCollection(int searcher, String uriCollection) {

        Searchable searchable = this.searcherMap.get(searcher);

        if (searchable instanceof SearchEngine) {
            SearchEngine item = (SearchEngine) searchable;

            item.setCollectionPath(uriCollection);

            //TODO: ver los web search engines

        } else {
            try {
                throw new SearchableNotSupportedException("The searchable is not supported");
            } catch (SearchableNotSupportedException ex) {
                OutputMonitor.printStream("", ex);
            }

        }

        this.searchersList = new ArrayList<Searchable>(this.searcherMap.values());

    }

    private boolean isEnabled(int searcherItem) {
        boolean enable = false;

        if (this.searcherMap.containsKey(searcherItem)) {
            enable = this.searcherMap.get(searcherItem).isEnabled();

        }

        return enable;

    }

    private void setEnabled(int searcherItem, boolean enabled) throws SearchableNotSupportedException {

        if (this.searcherMap.containsKey(searcherItem)) {
            this.searcherMap.get(searcherItem).setEnabled(enabled);
        } else {
            throw new SearchableNotSupportedException();
        }

        this.searchersList = new ArrayList<Searchable>(this.searcherMap.values());
    }

    /**
     * Modifica el la lista de buscadores disponibles en el servidor
     *
     * @param list lista de buscadores
     */
    public void setSearchables(List<Searchable> list) {
        this.searcherMap.clear();
//        this.lsiManagerMap.clear();
        SearchEngine e;
        for (Searchable item : list) {
            this.searcherMap.put(item.getID(), item);
        }

        this.searchersList = list;
    }

    /**
     * Devuelve la lista de buscadores disponibles en el servidor
     *
     * @return lista de buscadores
     */
    public List<Searchable> getSearchables() {
        List<Searchable> temp = new ArrayList<Searchable>(this.searcherMap.values());
        return temp;
    }

    /**
     * Devuelve la directorio donde se encuentra la colección de datos del servidor
     *
     * @return directorio
     */
    public File getCollectionPath() {
        return collectionPath;
    }

    /**
     * Modifica el directorio donde se encuentra la colección de datos del servidor
     *
     * @param collectionPath nuevo directorio
     */
    public void setCollectionPath(File collectionPath) {
        this.collectionPath = collectionPath;
    }

    /**
     * Devuelve el directorio donde se encuentra él ó los índices del servidor
     *
     * @return directorio
     */
    public File getIndexPath() {
        return indexPath;
    }

    /**
     * Modifica el directorio donde se encuentra él ó los índices del servidor
     *
     * @param indexPath nuevo directorio
     */
    public void setIndexPath(File indexPath) {
        this.indexPath = indexPath;
    }

    /**
     *
     * @return
     */
    public IndexerManager getIndexManager() {
        return indexManager;
    }

    /**
     *
     * @param indexManager
     */
    public void setIndexManager(IndexerManager indexManager) {
        this.indexManager = indexManager;
    }

    /**
     *
     * @return
     */
    public SearcherManager getSearcherManager() {
        return searcherManager;
    }

    /**
     *
     * @param searcherManager
     */
    public void setSearcherManager(SearcherManager searcherManager) {
        this.searcherManager = searcherManager;
    }

    /**
     *
     * @param s
     */
    public void addSearcher(Searchable s) {
        this.searcherMap.put(s.getID(), s);
        this.searchersList.add(s);
    }
}
