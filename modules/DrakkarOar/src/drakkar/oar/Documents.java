/*
 * DrakkarKeel - An Enterprise Collaborative Search Platform
 *
 * The contents of this file are subject under the terms described in the
 * DRAKKARKEEL_LICENSE file included in this distribution; you may not use this
 * file except in compliance with the License. 
 *
 * 2013-2014 DrakkarKeel Platform.
 */
package drakkar.oar;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Esta clase almacena los índices de documentos a recomendar por buscador
 * 
 */
public class Documents implements Serializable{
    
    private static final long serialVersionUID = 70000000000005L;

    private Map<Integer,List<Integer>> docs;

    /**
     *
     * @param docs
     */
    public Documents(Map<Integer,List<Integer>> docs) {
        this.docs = docs;
    }

    /**
     *
     * @return
     */
    public Map<Integer, List<Integer>> getDocs() {
        return docs;
    }

    /**
     *
     * @param docs
     */
    public void setDocs(Map<Integer, List<Integer>> docs) {
        this.docs = docs;
    }


    /**
     * Devuelve lod índices de los documentos
     *
     * @return
     */
    public List<Integer> getIndexDocuments() {

        List<Integer> documents= new ArrayList<>();

        Collection <List<Integer>> list = this.docs.values();

        for (List<Integer> arrayList : list) {
            documents.addAll(arrayList);
        }
        return documents;

    }


}
