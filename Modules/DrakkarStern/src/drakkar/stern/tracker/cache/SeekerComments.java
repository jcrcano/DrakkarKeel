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
import java.util.Map;
import java.util.Set;

/**
 * Esta clase almacena la relación de los comentarios efectuados por los usuarios,
 * a los documentos encontrados en las búsquedas realizadas y por recomendaciones
 */
public class SeekerComments {

    /**
     * Esta tabla hash almacena todos los comentarios efectuados por cada uno de
     * los usuarios
     */
    public Map<Seeker, Comments> record;

    /**
     *
     */
    public SeekerComments() {
        this.record = new HashMap<>();
    }

    /**
     *
     * @param searcher
     * @param docIndex
     * @return
     */
    public ArrayList<SeekerCommentsData> getSeekersComments(int searcher, int docIndex) {

        ArrayList<SeekerCommentsData> list = new ArrayList<>();
        SeekerCommentsData data;

        Set<Seeker> seekers = this.record.keySet();
        for (Seeker seeker : seekers) {
            data = record.get(seeker).getSeekersComments(searcher, docIndex);
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
    public ArrayList<SeekerCommentsData> getSeekersComments(String query, int searcher, int docIndex) {
        ArrayList<SeekerCommentsData> list = new ArrayList<>();
        SeekerCommentsData data;
        Set<Seeker> seekers = this.record.keySet();
        for (Seeker seeker : seekers) {
            data = record.get(seeker).getSeekersComments(query, searcher, docIndex);
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
    public SeekerCommentsData getSeekersComments(Seeker seeker, int searcher, int docIndex) {
        SeekerCommentsData data = null;
        data = record.get(seeker).getSeekersComments(searcher, docIndex);
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
    public SeekerCommentsData getSeekersComments(String query, Seeker seeker, int searcher, int docIndex) {
        SeekerCommentsData data = null;

        data = record.get(seeker).getSeekersComments(query, searcher, docIndex);
        if (data != null) {
            data.setUser(seeker.getUser());
        }

        return data;
    }

    /**
     * Esta clase interna almacena la relación de comentarios efecuados a los 
     * documentos obtenidos para una consulta determinada
     */
    public class Comments {

        /**
         * Esta tabla hash almacena la relación de comentarios de los documentos
         * por cada consulta(<consulta,comentarios>)
         */
        public Map<String, CommentsData> record;

        /**
         * Contructor de la clase
         */
        public Comments() {
            this.record = new HashMap<>();
        }

        private SeekerCommentsData getSeekersComments(int searcher, int docIndex) {
            SeekerCommentsData data = null;
            Collection<CommentsData> col = this.record.values();

            for (CommentsData item : col) {
                data = item.getSeekersComments(searcher, docIndex);

                if (data != null) {
                    return data;
                }

            }

            return data;

        }

        private SeekerCommentsData getSeekersComments(String query, int searcher, int docIndex) {

            SeekerCommentsData data = this.record.get(query).getSeekersComments(searcher, docIndex);

            return data;
        }
    }

    /**
     * Esta clase interna almacena la relación de comentarios efectuados para
     * cada uno de los documentos obtenidos en una búsqueda, para un buscador
     * determinado
     */
    public class CommentsData {

        /**
         * Esta tabla hash almacena los documentos comentados por usuario
         * para cada buscador
         *
         */
        public Map<Integer, CommentDocs> values;

        /**
         * Constructor de la clase
         */
        public CommentsData() {
            this.values = new HashMap<>();
        }

        private SeekerCommentsData getSeekersComments(int searcher, int docIndex) {
            SeekerCommentsData data = null;
            String comment;

            try {
                CommentDocs commentDocs = this.values.get(searcher);

                if (commentDocs.values.containsKey(docIndex)) {
                    comment = commentDocs.values.get(docIndex);
                    data = new SeekerCommentsData(null, comment);
                }

            } catch (NullPointerException ex) {
             OutputMonitor.printStream("No existen resultados asociados al buscador", ex);
            }

            return data;

        }
    }

    /**
     * Esta clase interna almacena la relación de
     * comentarios por documentos
     */
    public class CommentDocs {

        /**
         * Esta tabla hash almacena la relación de comentarios
         * por documentos
         * (<indice del documento,lista de comentarios>)
         */
        public Map<Integer, String> values;

        /**
         * Constructor de la clase
         */
        public CommentDocs() {
            this.values = new HashMap<>();
        }

        /**
         * Constructor de la clase
         *
         * @param docIndex índice del documento
         * @param comment  comentario
         */
        public CommentDocs(int docIndex, String comment) {
            this.values = new HashMap<>();
            this.values.put(docIndex, comment);
        }
    }
}
