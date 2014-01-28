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

import drakkar.oar.DocumentMetaData;
import drakkar.oar.ResultSetMetaData;
import drakkar.oar.util.KeySearchable;
import drakkar.mast.SearchException;
import drakkar.mast.SearchableException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Esta clase maneja todos los métodos de búsqueda colaborativa para un solo
 * buscador, que pueden ser invocados por los clientes
 */
public class CollaborativeSingleSearch extends SearchFactory {

    private DefaultSingleSearch dSingleSearch;

    /**
     * Constructor de la clase
     *
     * @param searchers     lista buscadores
     * @param dSingleSearch objeto DefaultSingleSearch
     */
    public CollaborativeSingleSearch(DefaultSingleSearch dSingleSearch) {
        super(dSingleSearch.getSearchersHash(), dSingleSearch.getSearchableList());
        this.dSingleSearch = dSingleSearch;
    }

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, aplicando
     * mecanismos de división del trabajo
     *
     * @param searcher      buscador
     * @param query         consulta de la búsqueda
     * @param caseSensitive tener en cuenta mayúsculas y minísculas
     * @param members       número de miembros de la sesión
     *
     * @return lista de documentos encontrados
     *
     * @throws SearchException  si ocurre alguna excepción durante el proceso de búsqueda
     * @throws SearchableException  si el buscador no se encuentra disponible
     * <br>
     * <br>
     * <b>Nota:</b>
     * <br>
     * Las constantes que representan los diferentes buscadores soportados, se encuentran
     * definidas en la clase <code>KeySearchable</code>, del paquete drakkar.oar.util
     * <br>
     * <br>
     *
     * @see KeySearchable
     *
     */
    public List<ResultSetMetaData> search(int searcher, String query, boolean caseSensitive, int members) throws SearchException, SearchableException {
        ResultSetMetaData temp = this.dSingleSearch.search(query, searcher, caseSensitive);
        List<ResultSetMetaData> results = this.divideSearchResults(query, temp, searcher, members);

        return results;
    }

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, aplicando
     * mecanismos de división del trabajo
     *
     * @param searcher      buscador
     * @param query         consulta de la búsqueda
     * @param field         campo del documento
     * @param caseSensitive tener en cuenta mayúsculas y minísculas
     * @param members       número de miembros de la sesión
     *
     * @return lista de documentos encontrados
     *
     * @throws SearchException  si ocurre alguna excepción durante el proceso de búsqueda
     * @throws SearchableException  si el buscador no se encuentra disponible
     * <br>
     * <br>
     * <b>Nota:</b>
     * <br>
     * Las constantes que representan los diferentes buscadores soportados, se encuentran
     * definidas en la clase <code>KeySearchable</code>, del paquete drakkar.oar.util
     * <br>
     * <br>
     *
     * @see KeySearchable
     *
     */
    public List<ResultSetMetaData> search(int searcher, String query, int field, boolean caseSensitive, int members) throws SearchException, SearchableException {
        ResultSetMetaData temp = this.dSingleSearch.search(query, field, searcher, caseSensitive);
        List<ResultSetMetaData> results = this.divideSearchResults(query, temp, searcher, members);

        return results;
    }

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, aplicando
     * mecanismos de división del trabajo
     *
     * @param searcher      buscador
     * @param query         consulta de la búsqueda
     * @param fields        campos del documento
     * @param caseSensitive tener en cuenta mayúsculas y minísculas
     * @param members       número de miembros de la sesión
     *
     * @return lista de documentos encontrados
     *
     * @throws SearchException  si ocurre alguna excepción durante el proceso de búsqueda
     * @throws SearchableException  si el buscador no se encuentra disponible
     *
     * <br>
     * <br>
     * <b>Nota:</b>
     * <br>
     * Las constantes que representan los diferentes buscadores soportados, se encuentran
     * definidas en la clase <code>KeySearchable</code>, del paquete drakkar.oar.util
     * <br>
     * <br>
     *
     * @see KeySearchable
     *
     */
    public List<ResultSetMetaData> search(int searcher, String query, int[] fields, boolean caseSensitive, int members) throws SearchException, SearchableException {
        ResultSetMetaData temp = this.dSingleSearch.search(query, fields, searcher, caseSensitive);
        List<ResultSetMetaData> results = this.divideSearchResults(query, temp, searcher, members);

        return results;
    }

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, aplicando
     * mecanismos de división del trabajo
     *
     * @param searcher      buscador
     * @param query         consulta de la búsqueda
     * @param docType       tipo de documento
     * @param caseSensitive tener en cuenta mayúsculas y minísculas
     * @param members       número de miembros de la sesión
     *
     * @return lista de documentos encontrados
     *
     * @throws SearchException  si ocurre alguna excepción durante el proceso de búsqueda
     * @throws SearchableException  si el buscador no se encuentra disponible
     * <br>
     * <br>
     * <b>Nota:</b>
     * <br>
     * Las constantes que representan los diferentes buscadores soportados, se encuentran
     * definidas en la clase <code>KeySearchable</code>, del paquete drakkar.oar.util
     * <br>
     * <br>
     *
     * @see KeySearchable
     *
     */
    public List<ResultSetMetaData> search(int searcher, String query, String docType, boolean caseSensitive, int members) throws SearchException, SearchableException {
        ResultSetMetaData temp = this.dSingleSearch.search(query, docType, searcher, caseSensitive);
        List<ResultSetMetaData> results = this.divideSearchResults(query, temp, searcher, members);

        return results;
    }

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, aplicando
     * mecanismos de división del trabajo
     *
     * @param searcher      buscador
     * @param query         consulta de la búsqueda
     * @param docTypes      tipo de documento
     * @param caseSensitive tener en cuenta mayúsculas y minísculas
     * @param members       número de miembros de la sesión
     *
     * @return lista de documentos encontrados
     *
     * @throws SearchException  si ocurre alguna excepción durante el proceso de búsqueda
     * @throws SearchableException  si el buscador no se encuentra disponible
     * <br>
     * <br>
     * <b>Nota:</b>
     * <br>
     * Las constantes que representan los diferentes buscadores soportados, se encuentran
     * definidas en la clase <code>KeySearchable</code>, del paquete drakkar.oar.util
     * <br>
     * <br>
     *
     * @see KeySearchable
     *
     */
    public List<ResultSetMetaData> search(int searcher, String query, String[] docTypes, boolean caseSensitive, int members) throws SearchException, SearchableException {
        ResultSetMetaData temp = this.dSingleSearch.search(query, docTypes, searcher, caseSensitive);
        List<ResultSetMetaData> results = this.divideSearchResults(query, temp, searcher, members);

        return results;
    }

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, aplicando
     * mecanismos de división del trabajo
     *
     * @param searcher      buscador
     * @param query         consulta de la búsqueda
     * @param docType       tipo de documento
     * @param field         campo del documento
     * @param caseSensitive tener en cuenta mayúsculas y minísculas
     * @param members       número de miembros de la sesión
     *
     * @return lista de documentos encontrados
     *
     * @throws SearchException  si ocurre alguna excepción durante el proceso de búsqueda
     * @throws SearchableException  si el buscador no se encuentra disponible
     * <br>
     * <br>
     * <b>Nota:</b>
     * <br>
     * Las constantes que representan los diferentes buscadores soportados, se encuentran
     * definidas en la clase <code>KeySearchable</code>, del paquete drakkar.oar.util
     * <br>
     * <br>
     *
     * @see KeySearchable
     *
     */
    public List<ResultSetMetaData> search(int searcher, String query, String docType, int field, boolean caseSensitive, int members) throws SearchException, SearchableException {
        ResultSetMetaData temp = this.dSingleSearch.search(query, docType, field, searcher, caseSensitive);
        List<ResultSetMetaData> results = this.divideSearchResults(query, temp, searcher, members);

        return results;
    }

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, aplicando
     * mecanismos de división del trabajo
     *
     * @param searcher      buscador
     * @param query         consulta de la búsqueda
     * @param docType       tipo de documento
     * @param fields        campos del documento
     * @param caseSensitive tener en cuenta mayúsculas y minísculas
     * @param members       número de miembros de la sesión
     *
     * @return lista de documentos encontrados
     *
     * @throws SearchException  si ocurre alguna excepción durante el proceso de búsqueda
     * @throws SearchableException  si el buscador no se encuentra disponible
     * <br>
     * <br>
     * <b>Nota:</b>
     * <br>
     * Las constantes que representan los diferentes buscadores soportados, se encuentran
     * definidas en la clase <code>KeySearchable</code>, del paquete drakkar.oar.util
     * <br>
     * <br>
     *
     * @see KeySearchable
     *
     */
    public List<ResultSetMetaData> search(int searcher, String query, String docType, int[] fields, boolean caseSensitive, int members) throws SearchException, SearchableException {
        ResultSetMetaData temp = this.dSingleSearch.search(query, docType, fields, searcher, caseSensitive);
        List<ResultSetMetaData> results = this.divideSearchResults(query, temp, searcher, members);

        return results;
    }

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, aplicando
     * mecanismos de división del trabajo
     *
     * @param searcher      buscador
     * @param query         consulta de la búsqueda
     * @param docTypes      tipo de documento
     * @param field         campo del documento
     * @param caseSensitive tener en cuenta mayúsculas y minísculas
     * @param members       número de miembros de la sesión
     *
     * @return lista de documentos encontrados
     *
     * @throws SearchException  si ocurre alguna excepción durante el proceso de búsqueda
     * @throws SearchableException  si el buscador no se encuentra disponible
     * <br>
     * <br>
     * <b>Nota:</b>
     * <br>
     * Las constantes que representan los diferentes buscadores soportados, se encuentran
     * definidas en la clase <code>KeySearchable</code>, del paquete drakkar.oar.util
     * <br>
     * <br>
     *
     * @see KeySearchable
     *
     */
    public List<ResultSetMetaData> search(int searcher, String query, String[] docTypes, int field, boolean caseSensitive, int members) throws SearchException, SearchableException {
        ResultSetMetaData temp = this.dSingleSearch.search(query, docTypes, field, searcher, caseSensitive);
        List<ResultSetMetaData> results = this.divideSearchResults(query, temp, searcher, members);

        return results;
    }

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, aplicando
     * mecanismos de división del trabajo
     *
     * @param searcher      buscador
     * @param query         consulta de la búsqueda
     * @param docTypes      tipo de documento
     * @param fields        campos del documento
     * @param caseSensitive tener en cuenta mayúsculas y minísculas
     * @param members       número de miembros de la sesión
     *
     * @return lista de documentos encontrados
     *
     * @throws SearchException  si ocurre alguna excepción durante el proceso de búsqueda
     * @throws SearchableException  si el buscador no se encuentra disponible
     * <br>
     * <br>
     * <b>Nota:</b>
     * <br>
     * Las constantes que representan los diferentes buscadores soportados, se encuentran
     * definidas en la clase <code>KeySearchable</code>, del paquete drakkar.oar.util
     * <br>
     * <br>
     *
     * @see KeySearchable
     *
     */
    public List<ResultSetMetaData> search(int searcher, String query, String[] docTypes, int[] fields, boolean caseSensitive, int members) throws SearchException, SearchableException {
        ResultSetMetaData temp = this.dSingleSearch.search(query, docTypes, fields, searcher, caseSensitive);
        List<ResultSetMetaData> results = this.divideSearchResults(query, temp, searcher, members);

        return results;
    }

    /**
     * Este método es empleado internamente por los métodos de búsqueda que aplican
     * mecanismos de división del trabajo, para distribuir los resultados obtenidos
     * de todos los motores de búsquedas entre los miembros de la sesión
     *
     * @param ResultSetMetaData resultados de la búsqueda
     * @param members       número de miembros de la sesión
     *
     * @return lista con los resultados a enviar a cada miembro de la sesión
     */
    private List<ResultSetMetaData> divideSearchResults(String query, ResultSetMetaData results, int searcher, int members) {

        List<DocumentMetaData> values = results.getAllResultList();

        List<ResultSetMetaData> list = this.getResultsList(query, searcher, members);
        DocumentMetaData doc = null;
        List<DocumentMetaData> valueTemp;
        ResultSetMetaData itemList;
        Map<Integer, List<DocumentMetaData>> itemHash = null;

        int size = values.size();
        int x = size / members;
        int r = members * x;
        int a = size - r;

        int count = 0;
        for (int i = 0; i < r; i += members) {
            count = i;

            for (int j = 0; j < members; j++) {
                itemList = list.get(j);
                itemHash = itemList.getResultsMap();
                valueTemp = itemHash.get(searcher);
                doc = values.get(count);
                valueTemp.add(doc);
                count++;
            }
        }

        for (int j = 0; j < a; j++) {
            itemList = list.get(j);
            itemHash = itemList.getResultsMap();
            valueTemp = itemHash.get(searcher);
            doc = values.get(count);
            valueTemp.add(doc);
            count++;
        }


        return list;
    }

    /**
     * Este método es empleado internamente por el método
     * <code>divideSearchResults(ResultSetMetaData ResultSetMetaData, int members)</code>
     * para obtener una lista que almacenará la lista de documentos por cada usuario
     * de la sesión
     *
     * @param members número de miembros de la sesión
     *
     * @return lista de documentos.
     */
    private List<ResultSetMetaData> getResultsList(String query, int searcher, int members) {
        List<ResultSetMetaData> list = new ArrayList<ResultSetMetaData>(members);

        for (int i = 0; i < members; i++) {
            list.add(new ResultSetMetaData(query, searcher));
        }
        return list;
    }
}
