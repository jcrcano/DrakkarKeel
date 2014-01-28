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

/**
 * Almacena la consulta, los motores y origen desde donde provienen estos datos
 *
 */
public class QuerySource implements Serializable{

    private static final long serialVersionUID = 70000000000009L;
    /**
     *  Consulta
     */
    private String query;
    /**
     * Fuente de donde está el documento
     * (puede ser historial, consulta local o
     * recomendaciones recibidas de la actual sesión)
     */
    private int source;
    /**
     * Se utilizan para especificar que se quieren recomendar
     * todos los resultados obtenidos para cada uno de los motores
     *
     */
    private int[] searchers;
    /**
     *  Indica si es de la cache o de la BD
     *
     *  true si es de cache (para un historial que muestre los resultados de búsqueda
     *  del día actual)
     *  false para la BD
     */
    boolean storeSource; 

    /**
     *
     * @param query     
     * @param source
     */
    public QuerySource(String query, int source) {
        this.query = query;      
        this.source = source;
    }

    /**
     *
     * @param query
     * @param source
     * @param storeSource
     */
    public QuerySource(String query, int source, boolean storeSource) {
        this.query = query;
        this.source = source;
        this.storeSource = storeSource;
    }



    /**
     *
     * @param query
     * @param source
     * @param searchers
     */
    public QuerySource(String query, int source, int[] searchers) {
        this.query = query;
       
        this.source = source;
        this.searchers = searchers;
    }

    /**
     *
     * @return
     */
    public String getQuery() {
        return query;
    }

    /**
     *
     * @param query
     */
    public void setQuery(String query) {
        this.query = query;
    }

    /**
     *
     * @return
     */
    public int[] getSearchers() {
        return searchers;
    }

    /**
     *
     * @param searchers
     */
    public void setSearchers(int[] searchers) {
        this.searchers = searchers;
    }

    /**
     *
     * @return
     */
    public int getSource() {
        return source;
    }

    /**
     *
     * @param source
     */
    public void setSource(int source) {
        this.source = source;
    }

    /**
     *
     * @return
     */
    public boolean isStoreSource() {
        return storeSource;
    }

    /**
     *
     * @param storeSource
     */
    public void setStoreSource(boolean storeSource) {
        this.storeSource = storeSource;
    }


    
}
