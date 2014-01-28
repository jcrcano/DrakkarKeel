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

import drakkar.oar.RecommendTracker;
import drakkar.oar.Seeker;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SeekerRecommends {

    /**
     *
     */
    public Map<Seeker, RecomendsData> values;//por usuario todas las recomendaciones recibidas

    /**
     *
     * @param record
     */
    public SeekerRecommends(Map<Seeker, RecomendsData> record) {
        this.values = record;
    }

    /**
     *
     */
    public SeekerRecommends() {
        values = new HashMap<>();
    }

    /**
     *
     * @return
     */
    public List<RecommendTracker> getRecommendations() {
        List<RecommendTracker> list = new ArrayList<>();

        Collection<RecomendsData> col = this.values.values();

        for (RecomendsData item : col) {
            list.addAll(item.getRecommendations());
        }

        return list;

    }

    /**
     *
     * @param query
     * @return
     */
    public List<RecommendTracker> getRecommendations(String query) {
        List<RecommendTracker> list = new ArrayList<>();

        Collection<RecomendsData> col = this.values.values();

        for (RecomendsData item : col) {
            list.addAll(item.getRecommendations());
        }


        return list;

    }

    /**
     * 
     * @param seeker
     * @return
     */
    public List<RecommendTracker> getRecommendations(Seeker seeker) {
        List<RecommendTracker> list = new ArrayList<>();

        RecomendsData col = this.values.get(seeker);
        list = col.getRecommendations();

        return list;

    }

    /**
     *
     * @param query
     * @param seeker
     * @return
     */
    public List<RecommendTracker> getRecommendations(String query, Seeker seeker) {
        List<RecommendTracker> list = new ArrayList<>();

        RecomendsData col = this.values.get(seeker);
        list = col.getRecommendations(query);

        return list;

    }

    /**
     *
     */
    public class RecomendsData {
        //query

        /**
         *
         */
        public Map<String, SearcherResults> data;

        /**
         *
         */
        public RecomendsData() {
            data = new HashMap<>();
        }

        /**
         *
         * @return
         */
        public List<RecommendTracker> getRecommendations() {
            List<RecommendTracker> list = new ArrayList<>();

            Collection<SearcherResults> col = this.data.values();

            for (SearcherResults item : col) {
                list.addAll(item.getRecommendations());
            }

            return list;

        }

        /**
         *
         * @param query
         * @return
         */
        public List<RecommendTracker> getRecommendations(String query) {
            List<RecommendTracker> list = new ArrayList<>();

            SearcherResults col = this.data.get(query);
            list = col.getRecommendations();

            return list;

        }
    }

    /**
     * Almacena por buscadores las recomendaciones
     */
    public class SearcherResults {
        //searcher - recomendación

        /**
         *
         */
        public Map<Integer, IndexRecommend> searcher;

        /**
         *
         */
        public SearcherResults() {
            searcher = new HashMap<>();

        }

        /**
         *
         * @param hash relación de docuementos recomendados por buscadores
         */
        public SearcherResults(Map<Integer, List<RecommendTracker>> hash) {
            this.searcher = new HashMap<>();
            IndexRecommend index;
            Set<Integer> enu = hash.keySet();
            for (Integer item : enu) {
                index = new IndexRecommend(hash.get(item));
                this.searcher.put(item, index);
            }
        }

        /**
         *
         * @param hash
         */
        public void insertRecommendation(Map<Integer, List<RecommendTracker>> hash) {
            IndexRecommend index;
            List<RecommendTracker> temp;
            Set<Integer> searchers = hash.keySet();
            for (Integer item : searchers) {
                temp = hash.get(item);
                if (this.searcher.containsKey(item)) {
                    index = this.searcher.get(item);
                    index.insertIndexRecommend(temp);
                } else {
                    index = new IndexRecommend(temp);
                    this.searcher.put(item, index);
                }
            }
        }

        /**
         *
         * @return
         */
        public List<RecommendTracker> getRecommendations() {
            List<RecommendTracker> list = new ArrayList<>();

            Collection<IndexRecommend> col = this.searcher.values();

            for (IndexRecommend item : col) {
                list.addAll(item.getRecommendations());
            }

            return list;

        }
    }

    /**
     * Almacena por id de doc las recomendaciones
     */
    public class IndexRecommend {
        //id del documento

        Map<Integer, RecommendTracker> index;

        /**
         *
         */
        public IndexRecommend() {
            index = new HashMap<>();
        }

        /**
         *
         * @param rec
         */
        public IndexRecommend(List<RecommendTracker> rec) {
            index = new HashMap<>();

            for (RecommendTracker recommendData : rec) {
                index.put(recommendData.getIdDoc(), recommendData);
            }
        }

        /**
         *
         * @param rec
         */
        public void insertIndexRecommend(List<RecommendTracker> rec) {
            RecommendTracker temp;
            int idDoc;
            for (RecommendTracker item : rec) {
                idDoc = item.getIdDoc();
                if (index.containsKey(idDoc)) {
                    temp = index.get(idDoc);
                    temp.getReceptors().addAll(item.getReceptors());

                } else {
                    index.put(idDoc, item);
                }

            }
        }

        /**
         *
         * @return
         */
        public List<RecommendTracker> getRecommendations() {
            List<RecommendTracker> list = new ArrayList<>();

            Collection<RecommendTracker> col = this.index.values();
            list = new ArrayList<>(col);

            return list;

        }
    }
}
