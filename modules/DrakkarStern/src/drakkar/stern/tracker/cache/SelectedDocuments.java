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
import drakkar.oar.util.OutputMonitor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Esta clase almacena las evaluaciones efectuadas por cada usuario de la sesión,
 * sobre los documentos obtenidos para cada consulta de búsqueda
 */
public class SelectedDocuments {

    /**
     * Esta tabla hash almacena las evaluaciones efectuadas por cada uno de los
     * usuarios
     */
    public Map<Seeker, Evaluation> record;

    /**
     * Constructor de la clase
     */
    public SelectedDocuments() {
        this.record = new HashMap<>();
    }

    public boolean isSelected(int searcher, int docIndex) {
        Collection<Evaluation> col = this.record.values();
        boolean flag = false;

        for (Evaluation item : col) {

            flag = item.isSelected(searcher, docIndex);

            if (flag) {
                return flag;
            }
        }

        return flag;
    }

    public boolean isSelected(int searcher, int docIndex, String query) {

        Collection<Evaluation> col = this.record.values();
        boolean flag = false;

        for (Evaluation item : col) {
            flag = item.isSelected(query, searcher, docIndex);

            if (flag) {
                return flag;
            }
        }

        return flag;
    }

    public boolean isSelected(int searcher, int docIndex, Seeker seeker) {
        Evaluation col = this.record.get(seeker);
        boolean flag = false;
        if (col != null) {
            flag = col.isSelected(searcher, docIndex);

        } else {
            OutputMonitor.printLine("No hay evaluaciones del seeker: " + seeker.getUser(), OutputMonitor.ERROR_MESSAGE);
        }


        return flag;
    }

    public boolean isSelected(int searcher, int docIndex, String query, Seeker seeker) {
        boolean flag = false;
        Evaluation col = this.record.get(seeker);
        if (col != null) {
            flag = col.isSelected(query, searcher, docIndex);
        } else {
            OutputMonitor.printLine("No hay evaluaciones del seeker: " + seeker.getUser(), OutputMonitor.ERROR_MESSAGE);
        }

        return flag;
    }

    /**
     *
     * @param searcher
     * @param docIndex
     * @return
     */
    public ArrayList<SelectDocumentsData> getSelectedDocuments(int searcher, int docIndex) {
        ArrayList<SelectDocumentsData> list = new ArrayList<>();
        SelectDocumentsData data;
        Set<Seeker> seekers = this.record.keySet();
        for (Seeker seeker : seekers) {
            data = record.get(seeker).getSelectedDocuments(searcher, docIndex);
            if (data != null) {
                data.setUser(seeker.getUser());
                list.add(data);
            }
        }

        return list;
    }

    /**
     *
     * @param query
     * @param searcher
     * @param docIndex
     * @return
     */
    public ArrayList<SelectDocumentsData> getSelectedDocuments(String query, int searcher, int docIndex) {
        ArrayList<SelectDocumentsData> list = new ArrayList<>();
        SelectDocumentsData data;
        Set<Seeker> seekers = this.record.keySet();
        for (Seeker seeker : seekers) {
            data = record.get(seeker).getSelectedDocuments(query, searcher, docIndex);
            if (data != null) {
                data.setUser(seeker.getUser());
                list.add(data);
            }
        }

        
        return list;
    }

    /**
     *
     * @param seeker
     * @param searcher
     * @param docIndex
     * @return
     */
    public SelectDocumentsData getSelectedDocuments(Seeker seeker, int searcher, int docIndex) {
        SelectDocumentsData data = null;

        data = record.get(seeker).getSelectedDocuments(searcher, docIndex);
        if (data != null) {
            data.setUser(seeker.getUser());
        }

        return data;
    }

    /**
     *
     * @param query
     * @param seeker
     * @param searcher
     * @param docIndex
     * @return
     */
    public SelectDocumentsData getSelectedDocuments(String query, Seeker seeker, int searcher, int docIndex) {
        SelectDocumentsData data = null;

        data = record.get(seeker).getSelectedDocuments(query, searcher, docIndex);
        if (data != null) {
            data.setUser(seeker.getUser());
        }

        return data;
    }

    /**
     * Esta clase interna almacena la relación de evaluaciones efecuadas a
     * documentos obtenidos para una consulta en una hora determinada
     */
    public class Evaluation {

        /**
         * Esta tabla hash almacena la lista  de evaluaciones efecuadas a los
         * documento obtenidos por cada buscador
         */
        public Map<String, SelectedData> evaluation;

        /**
         * Constructor de la clase
         */
        public Evaluation() {
            this.evaluation = new HashMap<>();
        }

        private SelectDocumentsData getSelectedDocuments(int searcher, int docIndex) {

            SelectDocumentsData data = null;
            Collection<SelectedData> col = this.evaluation.values();

            for (SelectedData item : col) {

                data = item.getSelectedDocuments(searcher, docIndex);
                if (item != null) {
                    return data;
                }

            }

            return data;

        }

        private SelectDocumentsData getSelectedDocuments(String query, int searcher, int docIndex) {
            SelectDocumentsData data = null;
            SelectedData col = this.evaluation.get(query);
            if (col != null) {
                data = col.getSelectedDocuments(searcher, docIndex);
            }
            return data;
        }

        private boolean isSelected(int searcher, int docIndex) {
            Collection<SelectedData> col = this.evaluation.values();
            boolean flag = false;

            for (SelectedData item : col) {
                flag = item.isSelected(searcher, docIndex);
                if (flag) {
                    return flag;
                }
            }

            return flag;
        }

        private boolean isSelected(String query, int searcher, int docIndex) {

            SelectedData col = this.evaluation.get(query);
            boolean flag = false;

            if (col != null) {
                flag = col.isSelected(searcher, docIndex);
            }


            return flag;
        }
    }

    /**
     *
     */
    public class SelectedData {

        // buscador
        /**
         *
         */
        public Map<Integer, RelevanceDocs> values;

        /**
         * Constructor de la clase
         */
        public SelectedData() {
            this.values = new HashMap<>();
        }

        private SelectDocumentsData getSelectedDocuments(int searcher, int docIndex) {
            SelectDocumentsData data = null;
            byte relevance;

            try {
                Object object = this.values.get(searcher).values.get(docIndex);
                relevance = (object != null) ? (Byte) object : 0;
                data = new SelectDocumentsData(relevance, "");

            } catch (NullPointerException ex) {
                OutputMonitor.printStream("No existen resultados asociados al buscador.", ex);
            }

            return data;

        }

        private List<SelectDocumentsData> getSelectedDocuments() {
            List<SelectDocumentsData> list = null;

            SelectDocumentsData data = null;
            byte relevance;

            try {

                Set<Integer> searchers = this.values.keySet();

                RelevanceDocs relDocs;
                for (Integer searcher : searchers) {
                    relDocs = this.values.get(searcher);
                    Set<Integer> indexDocs = relDocs.values.keySet();
                    for (Integer docIndex : indexDocs) {
                        relevance = relDocs.values.get(docIndex);
                        data = new SelectDocumentsData(relevance, null);
                    }
                }

            } catch (NullPointerException ex) {
                OutputMonitor.printStream("No existen resultados asociados al buscador.", ex);
            }

            return list;



        }

        public boolean isSelected(int searcher, int docIndex) {

            boolean flag = false;


            RelevanceDocs col = this.values.get(searcher);
            if (col != null) {
                flag = col.isSelected(docIndex);
            }

            return flag;
        }
    }

    /**
     *
     */
    public class RelevanceDocs {

        /**
         * Esta tabla hash almacena la relación del valor de relevancia por
         * documentos (<indice del documento, valor de relevancia>)
         */
        public Map<Integer, Byte> values;

        public RelevanceDocs() {
            this.values = new HashMap<>();
        }

        /**
         * Constructor de la clase
         *
         * @param values lista de evaluaciones
         */
        public RelevanceDocs(Map<Integer, Byte> values) {
            this.values = values;
        }

        /**
         * Constructor de la clase
         *
         * @param index índice del documento
         * @param relevance evaluación
         */
        public RelevanceDocs(int index, byte relevance) {
            values = new HashMap<>();
            values.put(index, relevance);
        }

        public boolean isSelected(int docIndex) {
            Set<Integer> col = this.values.keySet();
            boolean flag = col.contains(docIndex);
            return flag;
        }
    }
}
