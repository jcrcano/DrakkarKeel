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

import java.util.ArrayList;
import java.util.List;

/**
 * Esta clase almacena los resultados de búsqueda por buscadores para una
 * determinada consulta
 * 
 *
 */
public class ResultSetDocument implements java.io.Serializable {
    
    private static final long serialVersionUID = 70000000000011L;

    private String query;
    private List<List<DocSuggest>> results;

    /**
     * Constructor por defecto
     */
    public ResultSetDocument() {
        this.query = "";
        this.results = new ArrayList<>();

    }

    /**
     * Constructor de la clase
     *
     * @param query  consulta de búsqueda
     */
    public ResultSetDocument(String query) {
        this.query = query;
        this.results = new ArrayList<>();
    }

    /**
     * Constructor de la clase
     *
     * @param query   consulta de búsqueda
     * @param results resultados de la búsqueda
     */
    public ResultSetDocument(String query, List<List<DocSuggest>> results) {
        this.query = query;
        this.results = results;
    }

    
    /**
     * Devuelve la consulta de la búsqueda
     *
     * @return consulta
     */
    public String getQuery() {
        return query;
    }

    /**
     * Determina si existen resultados de búsqueda
     *
     * @return true si no existen resultados de búsqueda, false en caso contrario
     */
    public boolean isEmpty() {
        return this.results.isEmpty();
    }


    /**
     * Modifica el valor de la consulta de búsqueda
     *
     * @param query nueva consulta
     */
    public void setQuery(String query) {
        this.query = query;
    }

    public List<List<DocSuggest>> getResults() {
        return results;
    }

    public void setResults(List<List<DocSuggest>> results) {
        this.results = results;
    }

    

    
}
