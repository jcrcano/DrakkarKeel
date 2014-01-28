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
import java.util.Set;

/**
 * Esta clase maneja todos los métodos de búsqueda colaborativa con multiples
 * de buscadores que pueden ser invocados por los clientes
 *
 * 
 */
public class CollaborativeMultiSearch extends SearchFactory {

    private DefaultMultiSearch defMultisearch;

    /**
     * Constructor de la clase
     *
     * @param searchers       lista buscadores
     * @param defMultisearch  objeto DefaultMultiSearch
     */
    public CollaborativeMultiSearch(DefaultMultiSearch defMultisearch) {
        super(defMultisearch.getSearchersHash(), defMultisearch.getSearchableList());
        this.defMultisearch = defMultisearch;
    }

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, aplicando el
     * principio MultiSearch y mecanismos de división del trabajo, con todos los
     * buscadores activos en el servidor
     *
     * @param query         consulta de la búsqueda
     * @param caseSensitive tener en cuenta mayúsculas y minísculas
     * @param members       número de miembros de la sesión
     *
     * @return lista de documentos encontrados
     *    
     * @throws SearchException  si ocurre alguna excepción durante el proceso de búsqueda
     */
    public List<ResultSetMetaData> search(String query, boolean caseSensitive, int members) throws SearchException {
        ResultSetMetaData results = this.defMultisearch.search(query, caseSensitive);
        List<ResultSetMetaData> finalResults = this.multiSearch(results, members);
        return finalResults;
    }

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, aplicando el
     * principio MultiSearch y mecanismos de división del trabajo, con todos los
     * buscadores activos en el servidor
     *
     * @param query         consulta de la búsqueda
     * @param field         campo del documento
     * @param caseSensitive tener en cuenta mayúsculas y minísculas
     * @param members       número de miembros de la sesión
     *
     * @return lista de documentos encontrados
     *
     * @throws SearchException  si ocurre alguna excepción durante el proceso de búsqueda
     */
    public List<ResultSetMetaData> search(String query, int field, boolean caseSensitive, int members) throws SearchException {
        ResultSetMetaData results = this.defMultisearch.search(query, field, caseSensitive);
        List<ResultSetMetaData> finalResults = this.multiSearch(results, members);
        return finalResults;
    }

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, aplicando el
     * principio MultiSearch y mecanismos de división del trabajo, con todos los
     * buscadores activos en el servidor
     *
     * @param query         consulta de la búsqueda
     * @param fields        campos del documento
     * @param caseSensitive tener en cuenta mayúsculas y minísculas
     * @param members       número de miembros de la sesión
     *
     * @return lista de documentos encontrados
     *
     * @throws SearchException  si ocurre alguna excepción durante el proceso de búsqueda
     */
    public List<ResultSetMetaData> search(String query, int[] fields, boolean caseSensitive, int members) throws SearchException {
        ResultSetMetaData results = this.defMultisearch.search(query, fields, caseSensitive);
        List<ResultSetMetaData> finalResults = this.multiSearch(results, members);
        return finalResults;
    }

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, aplicando el
     * principio MultiSearch y mecanismos de división del trabajo, con todos los
     * buscadores activos en el servidor
     *
     * @param query         consulta de la búsqueda
     * @param docType       tipo del documento
     * @param caseSensitive tener en cuenta mayúsculas y minísculas
     * @param members       número de miembros de la sesión
     *
     * @return lista de documentos encontrados
     *
     * @throws SearchException  si ocurre alguna excepción durante el proceso de búsqueda
     */
    public List<ResultSetMetaData> search(String query, String docType, boolean caseSensitive, int members) throws SearchException {
        ResultSetMetaData results = this.defMultisearch.search(query, docType, caseSensitive);
        List<ResultSetMetaData> finalResults = this.multiSearch(results, members);
        return finalResults;
    }

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, aplicando el
     * principio MultiSearch y mecanismos de división del trabajo, con todos los
     * buscadores activos en el servidor
     *
     * @param query         consulta de la búsqueda
     * @param docTypes      tipo del documento
     * @param caseSensitive tener en cuenta mayúsculas y minísculas
     * @param members       número de miembros de la sesión
     *
     * @return lista de documentos encontrados
     *
     * @throws SearchException  si ocurre alguna excepción durante el proceso de búsqueda
     */
    public List<ResultSetMetaData> search(String query, String[] docTypes, boolean caseSensitive, int members) throws SearchException {
        ResultSetMetaData results = this.defMultisearch.search(query, docTypes, caseSensitive);
        List<ResultSetMetaData> finalResults = this.multiSearch(results, members);
        return finalResults;
    }

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, aplicando el
     * principio MultiSearch y mecanismos de división del trabajo, con todos los
     * buscadores activos en el servidor
     *
     * @param query         consulta de la búsqueda
     * @param docType       tipo del documento
     * @param field         campo del documento
     * @param caseSensitive tener en cuenta mayúsculas y minísculas
     * @param members       número de miembros de la sesión
     *
     * @return lista de documentos encontrados
     *
     * @throws SearchException  si ocurre alguna excepción durante el proceso de búsqueda
     */
    public List<ResultSetMetaData> search(String query, String docType, int field, boolean caseSensitive, int members) throws SearchException {
        ResultSetMetaData results = this.defMultisearch.search(query, docType, field, caseSensitive);
        List<ResultSetMetaData> finalResults = this.multiSearch(results, members);
        return finalResults;
    }

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, aplicando el
     * principio MultiSearch y mecanismos de división del trabajo, con todos los
     * buscadores activos en el servidor
     *
     * @param query         consulta de la búsqueda
     * @param docType       tipo del documento
     * @param fields        campos del documento
     * @param caseSensitive tener en cuenta mayúsculas y minísculas
     * @param members       número de miembros de la sesión
     *
     * @return lista de documentos encontrados
     *
     * @throws SearchException  si ocurre alguna excepción durante el proceso de búsqueda
     */
    public List<ResultSetMetaData> search(String query, String docType, int[] fields, boolean caseSensitive, int members) throws SearchException {
        ResultSetMetaData results = this.defMultisearch.search(query, docType, fields, caseSensitive);
        List<ResultSetMetaData> finalResults = this.multiSearch(results, members);
        return finalResults;
    }

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, aplicando el
     * principio MultiSearch y mecanismos de división del trabajo, con todos los
     * buscadores activos en el servidor
     *
     * @param query         consulta de la búsqueda
     * @param docTypes      tipo del documento
     * @param field         campo ó campos del documento
     * @param caseSensitive tener en cuenta mayúsculas y minísculas
     * @param members       número de miembros de la sesión
     *
     * @return lista de documentos encontrados
     *
     * @throws SearchException  si ocurre alguna excepción durante el proceso de búsqueda
     */
    public List<ResultSetMetaData> search(String query, String[] docTypes, int field, boolean caseSensitive, int members) throws SearchException {
        ResultSetMetaData results = this.defMultisearch.search(query, docTypes, field, caseSensitive);
        List<ResultSetMetaData> finalResults = this.multiSearch(results, members);
        return finalResults;
    }

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, aplicando el
     * principio MultiSearch y mecanismos de división del trabajo, con todos los
     * buscadores activos en el servidor
     *
     * @param query         consulta de la búsqueda
     * @param docTypes      tipo del documento
     * @param fields        campos del documento
     * @param caseSensitive tener en cuenta mayúsculas y minísculas
     * @param members       número de miembros de la sesión
     *
     * @return lista de documentos encontrados
     *
     * @throws SearchException  si ocurre alguna excepción durante el proceso de búsqueda
     */
    public List<ResultSetMetaData> search(String query, String[] docTypes, int[] fields, boolean caseSensitive, int members) throws SearchException {
        ResultSetMetaData results = this.defMultisearch.search(query, docTypes, fields, caseSensitive);
        List<ResultSetMetaData> finalResults = this.multiSearch(results, members);
        return finalResults;
    }

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, aplicando el
     * principio MultiSearch y mecanismos de división del trabajo, con los
     * buscadores selecionados
     *
     * @param searchers     buscadores
     * @param query         consulta de la búsqueda
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
    public List<ResultSetMetaData> search(int[] searchers, String query, boolean caseSensitive, int members) throws SearchException, SearchableException {
        ResultSetMetaData results = this.defMultisearch.search(searchers, query, caseSensitive);
        List<ResultSetMetaData> finalResults = this.multiSearch(results, members);
        return finalResults;
    }

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, aplicando el
     * principio MultiSearch y mecanismos de división del trabajo, con los
     * buscadores selecionados
     *
     * @param searchers     buscadores
     * @param query         consulta de la búsqueda
     * @param field         campo del documento
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
    public List<ResultSetMetaData> search(int[] searchers, String query, int field, boolean caseSensitive, int members) throws SearchException, SearchableException {
        ResultSetMetaData results = this.defMultisearch.search(searchers, query, field, caseSensitive);
        List<ResultSetMetaData> finalResults = this.multiSearch(results, members);
        return finalResults;
    }

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, aplicando el
     * principio MultiSearch y mecanismos de división del trabajo, con los
     * buscadores selecionados
     *
     * @param searchers     buscadores
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
    public List<ResultSetMetaData> search(int[] searchers, String query, int[] fields, boolean caseSensitive, int members) throws SearchException, SearchableException {
        ResultSetMetaData results = this.defMultisearch.search(searchers, query, fields, caseSensitive);
        List<ResultSetMetaData> finalResults = this.multiSearch(results, members);
        return finalResults;
    }

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, aplicando el
     * principio MultiSearch y mecanismos de división del trabajo, con los
     * buscadores selecionados
     *
     * @param searchers      buscadores
     * @param query          consulta de la búsqueda
     * @param docType        tipo del documento
     * @param caseSensitive  tener en cuenta mayúsculas y minísculas
     * @param members        número de miembros de la sesión
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
    public List<ResultSetMetaData> search(int[] searchers, String query, String docType, boolean caseSensitive, int members) throws SearchException, SearchableException {
        ResultSetMetaData results = this.defMultisearch.search(searchers, query, docType, caseSensitive);
        List<ResultSetMetaData> finalResults = this.multiSearch(results, members);
        return finalResults;
    }

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, aplicando el
     * principio MultiSearch y mecanismos de división del trabajo, con los
     * buscadores selecionados
     *
     * @param searchers     buscadores
     * @param query         consulta de la búsqueda
     * @param docTypes      tipo del documento
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
    public List<ResultSetMetaData> search(int[] searchers, String query, String[] docTypes, boolean caseSensitive, int members) throws SearchException, SearchableException {
        ResultSetMetaData results = this.defMultisearch.search(searchers, query, docTypes, caseSensitive);
        List<ResultSetMetaData> finalResults = this.multiSearch(results, members);
        return finalResults;
    }

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, aplicando el
     * principio MultiSearch y mecanismos de división del trabajo, con los
     * buscadores selecionados
     *
     * @param searchers     buscadores
     * @param query         consulta de la búsqueda
     * @param docType       tipo del documento
     * @param field         campo del documento
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
    public List<ResultSetMetaData> search(int[] searchers, String query, String docType, int field, boolean caseSensitive, int members) throws SearchException, SearchableException {
        ResultSetMetaData results = this.defMultisearch.search(searchers, query, docType, field, caseSensitive);
        List<ResultSetMetaData> finalResults = this.multiSearch(results, members);
        return finalResults;
    }

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, aplicando el
     * principio MultiSearch y mecanismos de división del trabajo, con los
     * buscadores selecionados
     *
     * @param searchers     buscadores
     * @param query         consulta de la búsqueda
     * @param docType       tipo del documento
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
    public List<ResultSetMetaData> search(int[] searchers, String query, String docType, int[] fields, boolean caseSensitive, int members) throws SearchException, SearchableException {
        ResultSetMetaData results = this.defMultisearch.search(searchers, query, docType, fields, caseSensitive);
        List<ResultSetMetaData> finalResults = this.multiSearch(results, members);
        return finalResults;
    }

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, aplicando el
     * principio MultiSearch y mecanismos de división del trabajo, con los
     * buscadores selecionados
     *
     * @param searchers     buscadores
     * @param query         consulta de la búsqueda
     * @param docTypes      tipo del documento
     * @param field         campo ó campos del documento
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
    public List<ResultSetMetaData> search(int[] searchers, String query, String[] docTypes, int field, boolean caseSensitive, int members) throws SearchException, SearchableException {
        ResultSetMetaData results = this.defMultisearch.search(searchers, query, docTypes, field, caseSensitive);
        List<ResultSetMetaData> finalResults = this.multiSearch(results, members);
        return finalResults;
    }

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, aplicando el
     * principio MultiSearch y mecanismos de división del trabajo, con los
     * buscadores selecionados
     *
     * @param searchers     buscadores
     * @param query         consulta de la búsqueda
     * @param docTypes      tipo del documento
     * @param fields        campo ó campos del documento
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
    public List<ResultSetMetaData> search(int[] searchers, String query, String[] docTypes, int[] fields, boolean caseSensitive, int members) throws SearchException, SearchableException {
        ResultSetMetaData results = this.defMultisearch.search(searchers, query, docTypes, fields, caseSensitive);
        List<ResultSetMetaData> finalResults = this.multiSearch(results, members);
        return finalResults;
    }

    private List<ResultSetMetaData> multiSearch(ResultSetMetaData results, int members) {
        int searcherCount = results.getSearchersCount();
        if (searcherCount == members) {
            Map<Integer, List<DocumentMetaData>> values = results.getResultsMap();
            List<ResultSetMetaData> finalResults = new ArrayList<ResultSetMetaData>();
            Set<Integer> keys = values.keySet();
            for (Integer key : keys) {
                ResultSetMetaData item = new ResultSetMetaData(results.getQuery());
                item.add(key, values.get(key));
                finalResults.add(item);
            }

            return finalResults;

        } else {

            List<ResultSetMetaData> finalResults = this.divideSearchResults(results, members, searcherCount);
            return finalResults;

        }


    }

    /**
     * Este método es empleado internamente por los métodos de búsqueda que aplican
     * mecanismos de división del trabajo, para distribuir los resultados obtenidos
     * por cada buscador entre los miembros de la sesión
     *
     * @param ResultSetMetaData resultadosde la búsqueda
     * @param members       número de miembros.
     *
     * @return lista con los resultados a enviar a cada miembro de la sesión
     */
    private List<ResultSetMetaData> divideSearchResults(ResultSetMetaData searchResults, int members, int searchersCount) {

        List<ResultSetMetaData> finalResults = new ArrayList<ResultSetMetaData>();
        Map<Integer, List<DocumentMetaData>> values = searchResults.getResultsMap();
        String query = searchResults.getQuery();

        if (members > searchersCount) {

            int x = members / searchersCount;
            int y = x * searchersCount;
            int z = members - y;

            List<ResultSetMetaData> temp;
            Set<Integer> keys = values.keySet();
            int w = 0;

            for (Integer searcher : keys) {
                if (z > 0) {
                    w = x + 1;
                    temp = this.divideSearchResults(query, searcher, values.get(searcher), w);
                    finalResults.addAll(temp);
                    z--;
                } else {

                    temp = this.divideSearchResults(query, searcher, values.get(searcher), x);
                    finalResults.addAll(temp);
                }
            }

        } else {

            int x = searchersCount / members;
            int y = x * members;
            int z = searchersCount - y;


            List<ResultSetMetaData> results = new ArrayList<ResultSetMetaData>();
            ResultSetMetaData temp;
            List<Integer> keyCollection = new ArrayList<Integer>();
            Set<Integer> keyTemp = values.keySet();
            for (Integer item : keyTemp) {
                  keyCollection.add(item);
            }

            List<List<DocumentMetaData>> valueCollection = new ArrayList<List<DocumentMetaData>>(values.values());

            int w = 0;
            int count;
            int j = 0;
            for (int i = 0; i < members; i++) {
                count = 0;
                if (z > 0) {
                    w = x + 1;
                    temp = new ResultSetMetaData(query);

                    while (count < w) {
                        temp.add(keyCollection.get(j), valueCollection.get(j));
                        j++;
                        count++;
                    }
                    results.add(temp);
                    z--;
                } else {

                    temp = new ResultSetMetaData(query);
                    while (count < x) {
                        temp.add(keyCollection.get(j), valueCollection.get(j));
                        j++;
                        count++;
                    }
                    results.add(temp);
                }
            }// end for

            return results;
        }

        return null;

    }

    private List<ResultSetMetaData> divideSearchResults(String query, int searcher, List<DocumentMetaData> values, int members) {

        List<ResultSetMetaData> list = this.getResultsList(query, searcher, members);
        DocumentMetaData doc = null;
        List<DocumentMetaData> valueTemp;
        ResultSetMetaData itemList;
        Map<Integer, List<DocumentMetaData>> itemHash;

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

    /**
     * 
     * @return
     */
    public DefaultMultiSearch getDefaultMultisearch() {
        return defMultisearch;
    }

    /**
     *
     * @param defMultisearch
     */
    public void setDefaultMultisearch(DefaultMultiSearch defMultisearch) {
        this.defMultisearch = defMultisearch;
    }
}
