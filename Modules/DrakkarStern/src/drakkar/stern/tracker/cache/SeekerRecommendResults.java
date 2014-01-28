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
import drakkar.oar.Seeker;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Esta clase almacena los resultados de las recomendaciones efectuadas a cada
 * usuario de la sesión
 */
public class SeekerRecommendResults {

    /**
     * Esta tabla hash almacena los documentos obtenidos en las búsquedas de cada
     * usuario
     */
    public Map<Seeker, SeekerRecData> record;

    /**
     * Constructor de la clase
     */
    public SeekerRecommendResults() {
        this.record = new HashMap<>();


    }

    /**
     * Esta clase interna almacena los documentos obtenidos para una consulta
     * de búsqueda
     */
    /* Esta clase interna almacena los resultados obtenidos por cada uno de los
     * buscadores empleados en la búsqueda
     */
    public class SeekerRecData {

        /**
         * Esta tabla hash almacena los documentos obtenidos por hora por cada buscador
         */
        public Map<String, RecommendResults> values;

        /**
         * Constructor de la clase
         */
        public SeekerRecData() {
            this.values = new HashMap<>();
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
            Collection<RecommendResults> engineResults = values.values();

            for (RecommendResults item : engineResults) {
                metaDocuments.addAll(item.results.get(searcher).getDocuments());
            }

            return metaDocuments;

        }
    }

    /**
     * Esta clase guarda los resultados por buscadores
     */
    public class RecommendResults {

        /**
         *
         */
        public Map<Integer, IndexRecommendation> results;

        /**
         * Constructor
         */
        public RecommendResults() {
            results = new HashMap<>();
        }

        /**
         *
         * @return
         */
        public Map<Integer, List<DocumentMetaData>> getDocuments() {
            Map<Integer, List<DocumentMetaData>> docs = new HashMap<>();

            Set<Integer> searchers = results.keySet();
            for (Integer item : searchers) {
                docs.put(item, results.get(item).getDocuments());
            }
           
            return docs;
        }

        public void insertRecommendation(Map<Integer, List<DocumentMetaData>> hash) {
            
            IndexRecommendation index;
            List<DocumentMetaData> temp;
            Set<Integer> searchers = hash.keySet();
            for (Integer item : searchers) {
                temp = hash.get(item);

                if (this.results.containsKey(item)) {
                    index = this.results.get(item);
                    index.insertIndexRecommend(temp);
                } else {
                    index = new IndexRecommendation(temp);
                    this.results.put(item, index);
                }
            }
        }

    }

    /**
     *
     */
    public class IndexRecommendation {

        /**
         *
         */
        public Map<Integer, DocumentMetaData> index;

        /**
         *
         * @param docs
         */
        public IndexRecommendation(List<DocumentMetaData> docs) {
            this.index = new HashMap<>(docs.size());
            for (DocumentMetaData item : docs) {
                this.index.put(item.getIndex(), item);
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



        public void insertIndexRecommend(List<DocumentMetaData> rec) {
            int idDoc;
            for (DocumentMetaData item : rec) {
                idDoc = item.getIndex();
                if (!index.containsKey(idDoc)) {
                   index.put(idDoc, item);

                }

            }
        }
    }
}
