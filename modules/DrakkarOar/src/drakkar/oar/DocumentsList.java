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
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Esta clase almacena los resultados de búsqueda por buscadores para una
 * determinada consulta
 * 
 * 
 * @deprecated As of DrakkarKeel version 1.1,
 * replaced by <code>ResultSetMetaData</code>.
 *
 * @see drakkar.oar.ResultSetMetaData
 *
 */
public class DocumentsList implements java.io.Serializable {

     private static final long serialVersionUID = 70000000000006L;
    private String query;
    private Map<Integer, ArrayList<MetaDocument>> results;

    /**
     * Constructor por defecto
     */
    public DocumentsList() {
        this.query = "";
        this.results = new HashMap<>();

    }

    /**
     * Constructor de la clase
     *
     * @param query  consulta de búsqueda
     */
    public DocumentsList(String query) {
        this.query = query;
        this.results = new HashMap<>();
    }

    /**
     * Constructor de la clase
     *
     * @param query   consulta de búsqueda
     * @param results resultados de la búsqueda
     */
    public DocumentsList(String query, Map<Integer, ArrayList<MetaDocument>> results) {
        this.query = query;
        this.results = results;
    }

    /**
     * Constructor de la clase
     *
     * @param query    consulta de búsqueda
     * @param searcher buscador empleado para la búsqueda
     */
    public DocumentsList(String query, int searcher) {
        this.query = query;
        this.results = new HashMap<>();
        this.results.put(searcher, new ArrayList<MetaDocument>());
    }

    /**
     * Constructor de la clase
     * 
     * @param query     consulta de búsqueda
     * @param searcher buscador empleado para la búsqueda
     * @param values    resultados de la búsqueda
     */
    public DocumentsList(String query, int searcher, ArrayList<MetaDocument> values) {
        this.query = query;
        this.results = new HashMap<>();
        this.results.put(searcher, values);
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
     * Devuelve los resultados de búsqueda por cada buscador
     *
     * @return resultados
     */
    public Map<Integer, ArrayList<MetaDocument>> getResults() {
        return this.results;
    }

    /**
     * Devuelve todos los resultados de búsqueda
     *
     * @return resultados
     */
    public ArrayList<MetaDocument> getResultsList() {
        Collection<ArrayList<MetaDocument>> temp = this.results.values();
        ArrayList<MetaDocument> finalList = new ArrayList<>(temp.size());

        for (ArrayList<MetaDocument> arrayList : temp) {
            finalList.addAll(arrayList);
        }

        return finalList;
    }

    /**
     * Devuelve los resultados de búsqueda obtenidos para una buscador
     *
     * @param searcher  buscador
     * @return lista de metadocument para ese searchable, o null si el searchable especificado
     *          no cuenta con resultados.
     */
    public ArrayList<MetaDocument> getResultsList(int searcher) {
        ArrayList<MetaDocument> temp = this.results.get(searcher);
        return temp;
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
     * Agrega una lista de docuementos obtenidos por un buscador
     *
     * @param searcher buscador
     * @param docs     documentos obtenidos
     */
    public void add(int searcher, ArrayList<MetaDocument> docs) {
        this.results.put(searcher, docs);
    }

    /**
     * Modifica el valor de la consulta de búsqueda
     *
     * @param query nueva consulta
     */
    public void setQuery(String query) {
        this.query = query;
    }

    /**
     * Devuelve el número total de documentos
     * 
     * @return total
     */
    public int size() {
        Collection<ArrayList<MetaDocument>> temp = this.results.values();
        int size = 0;
        for (ArrayList<MetaDocument> arrayList : temp) {
            size += arrayList.size();
        }

        return size;
    }
}
