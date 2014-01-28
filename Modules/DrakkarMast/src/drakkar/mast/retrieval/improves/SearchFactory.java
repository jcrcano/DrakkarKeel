/*
 * DrakkarKeel - An Enterprise Collaborative Search Platform
 *
 * The contents of this file are subject under the terms described in the
 * DRAKKARKEEL_LICENSE file included in this distribution; you may not use this
 * file except in compliance with the License. 
 *
 * 2013-2014 DrakkarKeel Platform.
 */
package drakkar.mast.retrieval.improves;

import drakkar.mast.retrieval.Searchable;
import java.util.List;
import java.util.Map;

/**
 * Esta clase representa la raíz de todos las clases que manejan los diferentes
 * métodos de búsqueda colaborativa ó no, que pueden ser invocados por los clientes
 *
 * 
 */
public abstract class SearchFactory {

    protected List<Searchable> searchersList;
    protected Map<Integer, Searchable> searchersHash;

    /**
     * Constructor de la clase
     *
     * @param searchers listado de buscadores
     */
    public SearchFactory(Map<Integer, Searchable> searcherHash, List<Searchable> searchersList) {
        this.searchersList = searchersList;
        this.searchersHash = searcherHash;

    }

    /**
     * Devuelve la lista de buscadores
     *
     * @return buscadores
     */
    public List<Searchable> getSearchableList() {
        return searchersList;
    }

    /**
     * Modifica la lista de buscadores
     *
     * @param searchersList lista de buscadores
     */
    public void setSearchableList(List<Searchable> searchersList) {
        this.searchersList = searchersList;
    }

    /**
     * Devuelve una tabla hash donde las llaves son id de los buscadores y los
     * valores las instancias de los  buscadores
     *
     * @return buscadores
     */
    public Map<Integer, Searchable> getSearchersHash() {
        return searchersHash;
    }

    /**
     * Modifica las instancias de los buscadores de la clase
     *
     * @param searchersHash listado de buscadores
     */
    public void setSearchersHash(Map<Integer, Searchable> searchersHash) {
        this.searchersHash = searchersHash;
    }
}
