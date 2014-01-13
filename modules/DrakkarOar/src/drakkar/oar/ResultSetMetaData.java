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
import java.util.List;
import java.util.Map;

/**
 * Esta clase almacena los resultados de búsqueda por buscadores para una
 * determinada consulta
 * 
 *
 */
public class ResultSetMetaData implements java.io.Serializable {
    
    private static final long serialVersionUID = 70000000000012L;

    private String query;
    private Map<Integer, List<DocumentMetaData>> results;

    /**
     * Constructor por defecto
     */
    public ResultSetMetaData() {
        this.query = "";
        this.results = new HashMap<>();

    }

    /**
     * Constructor de la clase
     *
     * @param query  consulta de búsqueda
     */
    public ResultSetMetaData(String query) {
        this.query = query;
        this.results = new HashMap<>();
    }

    /**
     * Constructor de la clase
     *
     * @param query   consulta de búsqueda
     * @param results resultados de la búsqueda
     */
    public ResultSetMetaData(String query, Map<Integer, List<DocumentMetaData>> results) {
        this.query = query;
        this.results = results;
    }

    /**
     * Constructor de la clase
     *
     * @param query    consulta de búsqueda
     * @param searcher buscador empleado para la búsqueda
     */
    public ResultSetMetaData(String query, int searcher) {
        this.query = query;
        this.results = new HashMap<>();
        this.results.put(searcher, new ArrayList<DocumentMetaData>());
    }

    /**
     * Constructor de la clase
     * 
     * @param query     consulta de búsqueda
     * @param searcher buscador empleado para la búsqueda
     * @param values    resultados de la búsqueda
     */
    public ResultSetMetaData(String query, int searcher, List<DocumentMetaData> values) {
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
    public Map<Integer, List<DocumentMetaData>> getResultsMap() {
        return this.results;
    }

    /**
     * Devuelve todos los resultados de búsqueda
     *
     * @return resultados
     */
    public List<DocumentMetaData> getAllResultList() {
        Collection<List<DocumentMetaData>> temp = this.results.values();

        if (temp != null) {
            List<DocumentMetaData> finalList = new ArrayList<>(temp.size());

            for (List<DocumentMetaData> arrayList : temp) {
                finalList.addAll(arrayList);
            }

            return finalList;
        }

        return new ArrayList<>();

    }

    /**
     * Devuelve los resultados de búsqueda obtenidos para una buscador
     *
     * @param searcher  buscador
     * @return lista de metadocument para ese searchable, o null si el searchable especificado
     *          no cuenta con resultados.
     */
    public List<DocumentMetaData> getResultList(int searcher) {
        List<DocumentMetaData> temp = this.results.get(searcher);
        return temp;
    }

    /**
     * Devuelve los resultados de búsqueda obtenidos para una buscador
     *
     * @return lista de metadocument para ese searchable, o null si el searchable especificado
     *          no cuenta con resultados.
     */
    public List<List<DocumentMetaData>> getResultList() {

        Collection<List<DocumentMetaData>> temp = this.results.values();

        if (temp != null) {
            List<List<DocumentMetaData>> finalList = new ArrayList<>();
            for (List<DocumentMetaData> arrayList : temp) {
                finalList.add(arrayList);
            }

            return finalList;
        }

        return new ArrayList<>();


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
    public void add(int searcher, List<DocumentMetaData> docs) {
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
    public int getDocumentsCount() {
        Collection<List<DocumentMetaData>> temp = this.results.values();
        int size = 0;
        for (List<DocumentMetaData> arrayList : temp) {
            size += arrayList.size();
        }

        return size;
    }

     /**
     * Devuelve el número total de documentos
     *
     * @return total
     */
    public int getSearchersCount() {
        return this.results.size();
    }


    /**
     * Elimina un documento de todos los resultados
     * revisar...............
     * @param url
     */
    public void deleteFromAllList(String url) {

        Collection<List<DocumentMetaData>> e = this.results.values();
        for (List<DocumentMetaData> list : e) {
            for (int i = 0; i < list.size(); i++) {
                DocumentMetaData documentMetaData = list.get(i);
                if(documentMetaData.getPath().equals(url)){
                    list.remove(documentMetaData);
                }
            }
        }
        
    }


    /**
     *
     * @param results
     */
    public void setResultsMap(Map<Integer, List<DocumentMetaData>> results) {
        this.results = results;
    }


    
}
