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

import drakkar.oar.Seeker;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Esta clase almacena la relación de los documentos revisados por cada uno de
 * los miembros de la sesión, de los resultados obtenidos para cada consulta de
 * búsqueda y buscador utilizado
 */
public class ViewedDocuments {

    /**
     * Esta tabla hash almacena la relación de documentos revisados por cada uno de
     * los miembros de la sesión
     */
    public Map<Seeker, ViewedData> record;

    public ViewedDocuments() {
        this.record = new HashMap<>();
    }

    public boolean isViewed(int searcher, int docIndex) {
        Collection<ViewedData> col = this.record.values();
        boolean flag = false;

        for (ViewedData item : col) {

            flag = item.isViewed(searcher, docIndex);

            if (flag) {
                return flag;
            }
        }

        return flag;
    }

    public boolean isViewed(int searcher, int docIndex, String query) {

        Collection<ViewedData> col = this.record.values();
        boolean flag = false;

        for (ViewedData item : col) {

            flag = item.isViewed(query, searcher, docIndex);

            if (flag) {
                return flag;
            }
        }

        return flag;
    }

    public boolean isViewed(int searcher, int docIndex, Seeker seeker) {

        ViewedData col = this.record.get(seeker);
        boolean flag = col.isViewed(searcher, docIndex);

        return flag;
    }

    public boolean isViewed(int searcher, int docIndex, String query, Seeker seeker) {

        ViewedData col = this.record.get(seeker);
        boolean flag = col.isViewed(query, searcher, docIndex);

        return flag;
    }

    /**
     * Esta clase interna almacena la la relación de documentos revisados, obtenidos
     * por cada uno de los buscadores 
     */
    public class ViewedData {

        /**
         * Esta tabla hash almacena la la relación de documentos revisados, obtenidos
         * por cada uno de los buscadores utilizados en una hora dada
         */
        public Map<String, ViewedResults> values;

        /**
         * Constructor de la clase
         */
        public ViewedData() {
            values = new HashMap<>();
        }

        private boolean isViewed(int searcher, int docIndex) {
            Collection<ViewedResults> col = this.values.values();
            boolean flag = false;

            for (ViewedResults item : col) {
                flag = item.isViewed(searcher, docIndex);
                if (flag) {
                    return flag;
                }
            }

            return flag;
        }

        private boolean isViewed(String query, int searcher, int docIndex) {
            ViewedResults col = this.values.get(query);
            boolean flag = false;
//
//            col.sort();
            flag = col.isViewed(searcher, docIndex);

            return flag;
        }
    }

    /**
     * Esta clase guarda los resultados por buscadores
     */
    public class ViewedResults {

        /**
         *
         */
        public Map<Integer, List<Integer>> results;

        /**
         * Constructor
         */
        public ViewedResults() {
            results = new HashMap<>();
        }

        /**
         * Constructor de la clase
         *
         * @param searchable buscador
         * @param index      índice del documento
         */
        public ViewedResults(int searchable, int index) {
            results = new HashMap<>();
            List<Integer> temp = new ArrayList<>();
            temp.add(index);
            results.put(searchable, temp);
        }

        public boolean isViewed(int searcher, int docIndex) {
//            sort();

            if (this.results.containsKey(searcher)) {
                List<Integer> col = this.results.get(searcher);
                Collections.sort(col);
                int found = Collections.binarySearch(col, docIndex);

                boolean flag = (found < 0) ? false : true;
                return flag;
            } else {
                return false;
            }

        }
//        /**
//         * Ordena las listas de documentos
//         */
//        private void sort() {
//
//            Collection<List<Integer>> col = this.results.values();
//
//            for (List<Integer> item : col) {
//
//                Collections.sort(item);
//            }
//        }
    }
}
