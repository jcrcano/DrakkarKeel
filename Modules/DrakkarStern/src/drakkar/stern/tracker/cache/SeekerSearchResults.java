/*
 * DrakkarKeel - An Enterprise Collaborative Search Platform
 *
 * The contents of this file are subject under the terms described in the
 * DRAKKARKEEL_LICENSE file included in this distribution; you may not use this
 * file except in compliance with the License. 
 *
 * 2013-2014 DrakkarKeel Platform.
 */
package drakkar.stern.tracker.cache;

import drakkar.oar.DocumentMetaData;
import drakkar.oar.MarkupData;
import drakkar.oar.SearchResultData;
import drakkar.oar.SearchTracker;
import drakkar.oar.Seeker;
import drakkar.oar.SeekerQuery;
import drakkar.oar.exception.QueryNotExistException;
import drakkar.oar.exception.SeekerException;
import drakkar.oar.util.SeekerAction;
import drakkar.stern.tracker.persistent.DBUtil;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Esta clase almacena los resultados de las búsquedas de cada usuario de la sesión
 */
public class SeekerSearchResults {

    /**
     * Esta tabla hash almacena los documentos obtenidos en las búsquedas de cada
     * usuario
     */
    public Map<Seeker, SearchData> record;
    private String sessionName;
    private ViewedDocuments viewed;
    private SelectedDocuments selected;
    private CommentDocuments comments;

    /**
     *
     * @param sessionName
     */
    public SeekerSearchResults(String sessionName) {
        this.sessionName = sessionName;
        this.viewed = new ViewedDocuments();
        this.selected = new SelectedDocuments();
        this.comments = new CommentDocuments();
        this.record = new HashMap<>();
    }

    /**
     * 
     * @param sessionName
     * @param viewed
     */
    public SeekerSearchResults(String sessionName, ViewedDocuments viewed, SelectedDocuments selected, CommentDocuments comments) {
        this.sessionName = sessionName;
        this.viewed = viewed;
        this.selected = selected;
        this.comments = comments;
        this.record = new HashMap<>();
    }

    /**
     *
     * @return
     */
    public List<SearchTracker> getSearchResults() {
        List<SearchTracker> list = new ArrayList<>();

        Collection<SearchData> col = this.record.values();

        for (SearchData item : col) {
            list.addAll(item.getSearchResults());
        }

        return list;
    }

    /**
     *
     * @param query
     *
     * @return
     *
     * @throws QueryNotExistException
     */
    public List<SearchTracker> getSearchResults(String query, int group) throws QueryNotExistException {
        List<SearchTracker> list = new ArrayList<>();

        Collection<SearchData> col = this.record.values();
        for (SearchData item : col) {
            list.addAll(item.getSearchResults(query, group));
        }

        return list;
    }

    /**
     *
     * @param seeker
     * 
     * @return
     *
     * @throws SeekerException
     */
    public List<SearchTracker> getSearchResults(Seeker seeker, int group) throws SeekerException {
        List<SearchTracker> list = new ArrayList<>();

        SearchData col = this.record.get(seeker);

        if (col == null) {
            throw new SeekerException("The seeker " + seeker.getUser() + " doesn´t for this session");
        }
        list = col.getSearchResults(seeker, group);

        return list;
    }

    /**
     *
     * @param query
     * @param seeker
     * @return
     *
     * @throws QueryNotExistException
     * @throws SeekerException 
     */
    public List<SearchTracker> getSearchResults(String query, Seeker seeker, int group) throws QueryNotExistException, SeekerException {
        List<SearchTracker> list = new ArrayList<>();

        SearchData col = this.record.get(seeker);
        if (col == null) {
            throw new SeekerException("The seeker " + seeker.getUser() + " doesn´t for this session");
        }

        list = col.getSearchResults(query, seeker, group);
        return list;
    }

    public SeekerQuery getSeekerQuery() {
        SeekerQuery result = new SeekerQuery();
        Set<Seeker> seekerCol = this.record.keySet();
        Set<String> queries;

        SeekerSearchResults.SearchData data;
        for (Seeker item : seekerCol) {
            data = record.get(item);
            if (data != null) {
                queries = data.values.keySet();
                result.add(item.getUser(), new ArrayList<>(queries));
            }
        }
        return result;
    }

    /**
     * Esta clase interna almacena los resultados obtenidos por cada uno de los
     * buscadores empleados en la búsqueda
     */
    public class SearchData {

        /**
         * Esta tabla hash almacena los documentos obtenidos por hora por cada buscador
         */
        public Map<String, SearchResults> values;

        /**
         * Constructor de la clase
         */
        public SearchData() {
            this.values = new HashMap<>();
        }

        public SearchData(String query, SearchResults searchResults) {
            this.values = new HashMap<>();
            this.values.put(query, searchResults);
        }

        /**
         *
         * @return
         */
        public List<SearchTracker> getSearchResults() {
            List<SearchTracker> list = new ArrayList<>();

            Collection<SearchResults> data = this.values.values();

            for (SearchResults item : data) {
                list.addAll(item.getSearchTrackers(SeekerAction.SEARCH_ALL_TRACK));

            }

            return list;
        }

        /**
         *
         * @param query
         * @return
         * @throws QueryNotExistException
         */
        public List<SearchTracker> getSearchResults(String query, int group) throws QueryNotExistException {
            SearchResults item = this.values.get(query);
            if (item == null) {
                throw new QueryNotExistException("The query " + query + " doesn´t exist for this session");
            }
            List<SearchTracker> list = item.getSearchTrackers(query, group);
            return list;
        }

        /**
         *
         * @param seeker
         * @return
         */
        public List<SearchTracker> getSearchResults(Seeker seeker, int group) {
            List<SearchTracker> list = new ArrayList<>();
            Collection<SearchResults> col = this.values.values();

            for (SearchResults item : col) {
                list.addAll(item.getSearchTrackers(seeker, group));
            }


            return list;
        }

        /**
         *
         * @param query
         * @param seeker
         * @return
         * @throws QueryNotExistException
         */
        public List<SearchTracker> getSearchResults(String query, Seeker seeker, int group) throws QueryNotExistException {

            SearchResults item = this.values.get(query);

            if (item == null) {
                throw new QueryNotExistException("The query " + query + " doesn´t exist for this session");
            }

            List<SearchTracker> list = item.getSearchTrackers(query, seeker, group);

            return list;
        }

        /**
         * Devuelve los metadocumentos encontrados para el buecador especificado
         *
         * @param searcher buscador
         *
         * @return lista de metadocumentos
         */
        public List<DocumentMetaData> getMetaDocuments(int searcher) {
            List<DocumentMetaData> metaDocuments = new ArrayList<>();
            Collection<SearchResults> engineResults = values.values();

            for (SearchResults item : engineResults) {
                metaDocuments.addAll(item.results.get(searcher).getDocuments());
            }

            return metaDocuments;

        }

        /**
         *
         * @return
         */
        public List<DocumentMetaData> getMetaDocuments() {
            List<DocumentMetaData> metaDocuments = new ArrayList<>();
            Collection<SearchResults> engineResults = values.values();

            for (SearchResults item : engineResults) {
                metaDocuments.addAll(item.getAllDocuments());
            }

            return metaDocuments;

        }
    }

    /**
     * Esta clase guarda los resultados por buscadores
     */
    public class SearchResults {

        /**
         * resultados por buscadores
         */
        public Map<Integer, IndexMetaDoc> results;

        /**
         * Constructor
         */
        public SearchResults() {
            results = new HashMap<>();
        }

        public SearchResults(Map<Integer, List<DocumentMetaData>> hash) {
            IndexMetaDoc indexMDoc;
            results = new HashMap<>();
            Set<Integer> searchers = hash.keySet();
            for (Integer item : searchers) {
                indexMDoc = new IndexMetaDoc(hash.get(item));
                this.results.put(item, indexMDoc);
            }
        }

        /**
         *
         * @return
         */
        public Map<Integer, List<DocumentMetaData>> getDocumentMap() {
            Map<Integer, List<DocumentMetaData>> docs = new HashMap<>();

            Set<Integer> searchers = results.keySet();
            for (Integer item : searchers) {
                docs.put(item, results.get(item).getDocuments());
            }
            return docs;
        }

        public void insertMetaDocs(Map<Integer, List<DocumentMetaData>> hash) {
            IndexMetaDoc indexMDoc;
            Set<Integer> searchers = hash.keySet();
            for (Integer item : searchers) {
                if (this.results.containsKey(item)) {
                    indexMDoc = this.results.get(item);
                    indexMDoc.insertMetaDoc(hash.get(item));
                }
            }
        }

        /**
         *
         * @return
         */
        public List<DocumentMetaData> getAllDocuments() {
            List<DocumentMetaData> docs = new ArrayList<>();

            Set<Integer> searchers = results.keySet();
            for (Integer item : searchers) {
                docs.addAll(results.get(item).getDocuments());
            }

            return docs;
        }

        //  -------------------------------------------------------------------------------------
        /**
         * Devuelve el historial del grupo especificado de los resultados búsqueda de la sesión
         *
         * @return
         */
        public List<SearchTracker> getSearchTrackers(int group) {
            List<SearchTracker> tracker = new ArrayList<>();
            List<DocumentMetaData> metaDocs = null;
            List<SelectDocumentsData> selectedDocs = null;
            List<SeekerCommentsData> commentsData = null;
            SearchTracker searchTracker = null;
            SearchResultData resultData = null;
            boolean review = false, select = false;



            String date = DBUtil.getCurrentDate().toString();
            Set<Integer> searchers = results.keySet();
            int docIndex;
            switch (group) {

                case SeekerAction.SEARCH_ALL_TRACK:
                    for (Integer item : searchers) {
                        metaDocs = results.get(item).getDocuments();

                        for (DocumentMetaData metaDocument : metaDocs) {
                            docIndex = metaDocument.getIndex();
                            review = viewed.isViewed(item, docIndex);
                            resultData = new SearchResultData(metaDocument, review, sessionName);
                            selectedDocs = selected.getSelectedDocuments(item, docIndex);
                            commentsData = comments.getSeekersComments(item, docIndex);

                            searchTracker = new SearchTracker(resultData, getMarkupData(metaDocument.getPath(), date, selectedDocs, commentsData));
                            tracker.add(searchTracker);
                        }
                    }

                    break;

                case SeekerAction.SEARCH_SELECTED_RELEVANT_TRACK:
                    for (Integer item : searchers) {
                        metaDocs = results.get(item).getDocuments();

                        for (DocumentMetaData metaDocument : metaDocs) {
                            docIndex = metaDocument.getIndex();
                            select = selected.isSelected(item, docIndex);
                            if (select) {
                                review = viewed.isViewed(item, docIndex);
                                resultData = new SearchResultData(metaDocument, review, sessionName);
                                selectedDocs = selected.getSelectedDocuments(item, docIndex);
                                commentsData = comments.getSeekersComments(item, docIndex);

                                searchTracker = new SearchTracker(resultData, getMarkupData(metaDocument.getPath(), date, selectedDocs, commentsData));
                                tracker.add(searchTracker);
                            }
                        }
                    }

                    break;

                case SeekerAction.SEARCH_REVIEWED_TRACK:
                    for (Integer item : searchers) {
                        metaDocs = results.get(item).getDocuments();

                        for (DocumentMetaData metaDocument : metaDocs) {
                            docIndex = metaDocument.getIndex();
                            review = viewed.isViewed(item, docIndex);
                            if (review) {
                                resultData = new SearchResultData(metaDocument, review, sessionName);
                                selectedDocs = selected.getSelectedDocuments(item, docIndex);
                                commentsData = comments.getSeekersComments(item, docIndex);

                                searchTracker = new SearchTracker(resultData, getMarkupData(metaDocument.getPath(), date, selectedDocs, commentsData));
                                tracker.add(searchTracker);
                            }
                        }
                    }

                    break;
            }



            return tracker;
        }

        /**
         * Devuelve el historial de búsqueda de la sesón
         *
         * @param query
         * @return
         */
        public List<SearchTracker> getSearchTrackers(String query, int group) {
            List<SearchTracker> tracker = new ArrayList<>();
            List<DocumentMetaData> metaDocs = null;
            List<SelectDocumentsData> selectedDocs = null;
            List<SeekerCommentsData> commentsData = null;
            SearchTracker searchTracker = null;
            SearchResultData resultData = null;
            boolean review = false, select = false;



            String date = DBUtil.getCurrentDate().toString();
            int docIndex;
            Set<Integer> searchers = results.keySet();

            switch (group) {

                case SeekerAction.SEARCH_ALL_TRACK:
                    for (Integer searcher : searchers) {
                        metaDocs = results.get(searcher).getDocuments();

                        for (DocumentMetaData metaDocument : metaDocs) {
                            docIndex = metaDocument.getIndex();
                            review = viewed.isViewed(searcher, docIndex, query);
                            resultData = new SearchResultData(metaDocument, review, sessionName);
                            selectedDocs = selected.getSelectedDocuments(query, searcher, docIndex);
                            commentsData = comments.getSeekersComments(query, searcher, docIndex);
                            searchTracker = new SearchTracker(resultData, getMarkupData(metaDocument.getPath(), date, selectedDocs, commentsData));
                            tracker.add(searchTracker);
                        }
                    }

                    break;

                case SeekerAction.SEARCH_SELECTED_RELEVANT_TRACK:
                    for (Integer searcher : searchers) {
                        metaDocs = results.get(searcher).getDocuments();

                        for (DocumentMetaData metaDocument : metaDocs) {
                            docIndex = metaDocument.getIndex();
                            select = selected.isSelected(searcher, docIndex, query);
                            if (select) {
                                review = viewed.isViewed(searcher, docIndex, query);
                                resultData = new SearchResultData(metaDocument, review, sessionName);
                                selectedDocs = selected.getSelectedDocuments(query, searcher, docIndex);
                                commentsData = comments.getSeekersComments(query, searcher, docIndex);
                                searchTracker = new SearchTracker(resultData, getMarkupData(metaDocument.getPath(), date, selectedDocs, commentsData));
                                tracker.add(searchTracker);
                            }
                        }
                    }

                    break;

                case SeekerAction.SEARCH_REVIEWED_TRACK:
                    for (Integer searcher : searchers) {
                        metaDocs = results.get(searcher).getDocuments();

                        for (DocumentMetaData metaDocument : metaDocs) {
                            docIndex = metaDocument.getIndex();
                            review = viewed.isViewed(searcher, docIndex, query);
                            if (review) {
                                resultData = new SearchResultData(metaDocument, review, sessionName);
                                selectedDocs = selected.getSelectedDocuments(query, searcher, docIndex);
                                commentsData = comments.getSeekersComments(query, searcher, docIndex);
                                searchTracker = new SearchTracker(resultData, getMarkupData(metaDocument.getPath(), date, selectedDocs, commentsData));
                                tracker.add(searchTracker);
                            }
                        }
                    }

                    break;
            }
            return tracker;
        }

        /**
         * Devuelve el historial de búsqueda de la sesón
         *
         * @param query
         * @param seeker
         * @return
         */
        public List<SearchTracker> getSearchTrackers(String query, Seeker seeker, int group) {
            List<SearchTracker> tracker = new ArrayList<>();
            List<DocumentMetaData> metaDocs = null;
            SelectDocumentsData selectedDocs = null;
            SeekerCommentsData commentsData = null;
            SearchTracker searchTracker = null;
            SearchResultData resultData = null;
            boolean review = false, select = false;

            Set<Integer> searchers = results.keySet();
            String date = DBUtil.getCurrentDate().toString();
            int docIndex;
            switch (group) {
                case SeekerAction.SEARCH_ALL_TRACK:
                    for (Integer searcher : searchers) {
                        metaDocs = results.get(searcher).getDocuments();
                        for (DocumentMetaData metaDocument : metaDocs) {
                            docIndex = metaDocument.getIndex();
                            review = viewed.isViewed(searcher, docIndex, query, seeker);
                            resultData = new SearchResultData(metaDocument, review, sessionName);
                            selectedDocs = selected.getSelectedDocuments(query, seeker, searcher, docIndex);
                            commentsData = comments.getSeekersComments(query, seeker, searcher, docIndex);
                            searchTracker = new SearchTracker(resultData, getMarkupData(metaDocument.getPath(), date, selectedDocs, commentsData));
                            tracker.add(searchTracker);
                        }
                    }

                    break;

                case SeekerAction.SEARCH_SELECTED_RELEVANT_TRACK:
                    for (Integer searcher : searchers) {
                        metaDocs = results.get(searcher).getDocuments();

                        for (DocumentMetaData metaDocument : metaDocs) {
                            docIndex = metaDocument.getIndex();
                            select = selected.isSelected(searcher, docIndex, query, seeker);
                            if (select) {
                                review = viewed.isViewed(searcher, docIndex, query, seeker);
                                resultData = new SearchResultData(metaDocument, review, sessionName);

                                selectedDocs = selected.getSelectedDocuments(query, seeker, searcher, docIndex);
                                commentsData = comments.getSeekersComments(query, seeker, searcher, docIndex);

                                searchTracker = new SearchTracker(resultData, getMarkupData(metaDocument.getPath(), date, selectedDocs, commentsData));
                                tracker.add(searchTracker);
                            }
                        }
                    }

                    break;

                case SeekerAction.SEARCH_REVIEWED_TRACK:
                    for (Integer searcher : searchers) {
                        metaDocs = results.get(searcher).getDocuments();

                        for (DocumentMetaData metaDocument : metaDocs) {
                            docIndex = metaDocument.getIndex();
                            review = viewed.isViewed(searcher, docIndex, query, seeker);

                            if (review) {
                                resultData = new SearchResultData(metaDocument, review, sessionName);
                                selectedDocs = selected.getSelectedDocuments(query, seeker, searcher, docIndex);
                                commentsData = comments.getSeekersComments(query, seeker, searcher, docIndex);

                                searchTracker = new SearchTracker(resultData, getMarkupData(metaDocument.getPath(), date, selectedDocs, commentsData));
                                tracker.add(searchTracker);
                            }
                        }
                    }

                    break;
            }


            return tracker;
        }

        /**
         * Devuelve el historial de búsqueda de la sesón
         *
         * @param seeker
         * @return
         */
        public List<SearchTracker> getSearchTrackers(Seeker seeker, int group) {
            List<SearchTracker> tracker = new ArrayList<>();
            List<DocumentMetaData> metaDocs = null;
            SelectDocumentsData selectedDocs = null;
            SeekerCommentsData commentsData = null;
            SearchTracker searchTracker = null;
            SearchResultData resultData = null;
            boolean review = false, select = false;

            Set<Integer> searchers = results.keySet();
            String date = DBUtil.getCurrentDate().toString();
            int docIndex;

            switch (group) {

                case SeekerAction.SEARCH_ALL_TRACK:
                    for (Integer searcher : searchers) {
                        metaDocs = results.get(searcher).getDocuments();

                        for (DocumentMetaData metaDocument : metaDocs) {
                            docIndex = metaDocument.getIndex();
                            review = viewed.isViewed(searcher, docIndex, seeker);
                            resultData = new SearchResultData(metaDocument, review, sessionName);
                            selectedDocs = selected.getSelectedDocuments(seeker, searcher, docIndex);
                            commentsData = comments.getSeekersComments(seeker, searcher, docIndex);
                            searchTracker = new SearchTracker(resultData, getMarkupData(metaDocument.getPath(), date, selectedDocs, commentsData));
                            tracker.add(searchTracker);
                        }
                    }
                    break;

                case SeekerAction.SEARCH_SELECTED_RELEVANT_TRACK:
                    for (Integer searcher : searchers) {
                        metaDocs = results.get(searcher).getDocuments();
                        for (DocumentMetaData metaDocument : metaDocs) {
                            docIndex = metaDocument.getIndex();
                            select = selected.isSelected(searcher, docIndex, seeker);
                            if (select) {
                                review = viewed.isViewed(searcher, docIndex, seeker);
                                resultData = new SearchResultData(metaDocument, review, sessionName);

                                selectedDocs = selected.getSelectedDocuments(seeker, searcher, docIndex);
                                commentsData = comments.getSeekersComments(seeker, searcher, docIndex);

                                searchTracker = new SearchTracker(resultData, getMarkupData(metaDocument.getPath(), date, selectedDocs, commentsData));
                                tracker.add(searchTracker);
                            }
                        }
                    }

                    break;

                case SeekerAction.SEARCH_REVIEWED_TRACK:

                    for (Integer searcher : searchers) {
                        metaDocs = results.get(searcher).getDocuments();

                        for (DocumentMetaData metaDocument : metaDocs) {
                            docIndex = metaDocument.getIndex();
                            review = viewed.isViewed(searcher, docIndex, seeker);
                            if (review) {
                                resultData = new SearchResultData(metaDocument, review, sessionName);

                                selectedDocs = selected.getSelectedDocuments(seeker, searcher, docIndex);
                                commentsData = comments.getSeekersComments(seeker, searcher, docIndex);

                                searchTracker = new SearchTracker(resultData, getMarkupData(metaDocument.getPath(), date, selectedDocs, commentsData));
                                tracker.add(searchTracker);
                            }


                        }
                    }

                    break;
            }



            return tracker;
        }
        //------------------------
    }

    private List<MarkupData> getMarkupData(String uri, String date, List<SelectDocumentsData> selectedDocs,
            List<SeekerCommentsData> commentsData) {
        Map<String, MarkupData> data = new HashMap<>();

        for (SelectDocumentsData item : selectedDocs) {
            data.put(item.getUser(), new MarkupData(null, item.getRelevance(), item.getUser(), uri, date));
        }

        String user;
        for (SeekerCommentsData item : commentsData) {
            user = item.getUser();
            if (data.containsKey(user)) {
                data.get(user).setComment(item.getComment());

            } else {
                data.put(user, new MarkupData(item.getComment(), (byte) 0, user, uri, date));
            }
        }

        return new ArrayList<>(data.values());
    }

    private List<MarkupData> getMarkupData(String uri, String date, SelectDocumentsData selectedDocs,
            SeekerCommentsData commentsData) {

        String comment = (commentsData == null) ? "" : commentsData.getComment();
        byte relevance;
        String user;
        if (selectedDocs == null) {
            relevance = 0;
            user = "";
        } else {
            relevance = selectedDocs.getRelevance();
            user = selectedDocs.getUser();
        }

        MarkupData data = new MarkupData(comment, relevance, user, uri, date);

        List<MarkupData> d = new ArrayList<>();
        d.add(data);
        return d;
    }

    /**
     *
     */
    public class IndexMetaDoc {

        /**
         *
         */
        public Map<Integer, DocumentMetaData> index;

        /**
         *
         * @param docs
         */
        public IndexMetaDoc(List<DocumentMetaData> docs) {
            this.index = new HashMap<>(docs.size());
            for (DocumentMetaData item : docs) {
                this.index.put(item.getIndex(), item);
            }
        }

        public void insertMetaDoc(List<DocumentMetaData> docs) {
            this.index = new HashMap<>(docs.size());
            int idDoc;
            for (DocumentMetaData item : docs) {
                idDoc = item.getIndex();
                if (!index.containsKey(idDoc)) {
                    this.index.put(idDoc, item);
                }

            }
        }

        /**
         *
         * @return
         */
        public List<DocumentMetaData> getDocuments() {
            List<DocumentMetaData> docs = new ArrayList<>(this.index.values());
            return docs;
        }
    }
}
