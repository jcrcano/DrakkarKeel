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
import static drakkar.oar.util.KeySearchable.*;
import drakkar.mast.SearchException;
import drakkar.mast.SearchableException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Esta clase maneja todos los métodos de búsqueda colaborativa con fusión de
 * resultados que pueden ser invocados por los clientes
 *
 *
 */
public class CollaborativeMetaSearch extends SearchFactory {

    private DefaultMetaSearch dMetasearch;

  

     /**
     * Constructor de la clase
     *
     * @param searchers   lista buscadores
     * @param dMetasearch objeto DefaultMetaSearch
     */
    public CollaborativeMetaSearch(DefaultMetaSearch dMetasearch) {
        super(dMetasearch.getSearchersHash(),dMetasearch.searchersList);
        this.dMetasearch = dMetasearch;
    }


    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, aplicando el
     * principio MetaSearch y mecanismos de división del trabajo, con todos los
     * buscadores activos en el servidor
     *
     * @param query         consulta de la búsqueda
     * @param caseSensitive tener en cuenta mayúsculas y minísculas
     * @param members       número de miembros de la sesión
     *
     * @return lista de documentos encontrados
     *
    
     *                                   soportado
     * @throws SearchException  si ocurre alguna excepción durante el proceso de búsqueda
     */
    public List<ResultSetMetaData> search(String query, boolean caseSensitive, int members) throws  SearchException {
        ResultSetMetaData list = this.dMetasearch.search(query, caseSensitive);
        List<ResultSetMetaData> finalResults = this.divideSearchResults(list, members);

        return finalResults;
    }

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, aplicando el
     * principio MetaSearch y mecanismos de división del trabajo, con todos los
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
    public List<ResultSetMetaData> search(String query, int field, boolean caseSensitive, int members) throws  SearchException {
        ResultSetMetaData list = this.dMetasearch.search(query, field, caseSensitive);
        List<ResultSetMetaData> finalResults = this.divideSearchResults(list, members);
        return finalResults;
    }

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, aplicando el
     * principio MetaSearch y mecanismos de división del trabajo, con todos los
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
    public List<ResultSetMetaData> search(String query, int[] fields, boolean caseSensitive, int members) throws  SearchException {
        ResultSetMetaData list = this.dMetasearch.search(query, fields, caseSensitive);
        List<ResultSetMetaData> finalResults = this.divideSearchResults(list, members);
        return finalResults;
    }

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, aplicando el
     * principio MetaSearch y mecanismos de división del trabajo, con todos los
     * buscadores activos en el servidor
     *
     * @param query         consulta de la búsqueda
     * @param docType       tipo de documento
     * @param caseSensitive tener en cuenta mayúsculas y minísculas
     * @param members       número de miembros de la sesión
     *
     * @return lista de documentos encontrados
     *
     * @throws SearchException  si ocurre alguna excepción durante el proceso de búsqueda
     */
    public List<ResultSetMetaData> search(String query, String docType, boolean caseSensitive, int members) throws  SearchException {
        ResultSetMetaData list = this.dMetasearch.search(query, docType, caseSensitive);
        List<ResultSetMetaData> finalResults = this.divideSearchResults(list, members);
        return finalResults;
    }

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, aplicando el
     * principio MetaSearch y mecanismos de división del trabajo, con todos los
     * buscadores activos en el servidor
     *
     * @param query         consulta de la búsqueda
     * @param docTypes      tipo de documento
     * @param caseSensitive tener en cuenta mayúsculas y minísculas
     * @param members       número de miembros de la sesión
     *
     * @return lista de documentos encontrados
     *
     * @throws SearchException  si ocurre alguna excepción durante el proceso de búsqueda
     */
    public List<ResultSetMetaData> search(String query, String[] docTypes, boolean caseSensitive, int members) throws  SearchException {
        ResultSetMetaData list = this.dMetasearch.search(query, docTypes, caseSensitive);
        List<ResultSetMetaData> finalResults = this.divideSearchResults(list, members);
        return finalResults;
    }

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, aplicando el
     * principio MetaSearch y mecanismos de división del trabajo, con todos los
     * buscadores activos en el servidor
     *
     * @param query         consulta de la búsqueda
     * @param docType       tipo de documento
     * @param field         tipo de documento
     * @param caseSensitive tener en cuenta mayúsculas y minísculas
     * @param members       número de miembros de la sesión
     *
     * @return lista de documentos encontrados
     *     
     * @throws SearchException  si ocurre alguna excepción durante el proceso de búsqueda
     */
    public List<ResultSetMetaData> search(String query, String docType, int field, boolean caseSensitive, int members) throws  SearchException {
        ResultSetMetaData list = this.dMetasearch.search(query, docType, field, caseSensitive);
        List<ResultSetMetaData> finalResults = this.divideSearchResults(list, members);
        return finalResults;
    }

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, aplicando el
     * principio MetaSearch y mecanismos de división del trabajo, con todos los
     * buscadores activos en el servidor
     *
     * @param query         consulta de la búsqueda
     * @param docType       tipo de documento
     * @param fields        campo ó campos del documento
     * @param caseSensitive tener en cuenta mayúsculas y minísculas
     * @param members       número de miembros de la sesión
     *
     * @return lista de documentos encontrados
     *
     * @throws SearchException  si ocurre alguna excepción durante el proceso de búsqueda
     */
    public List<ResultSetMetaData> search(String query, String docType, int[] fields, boolean caseSensitive, int members) throws  SearchException {
        ResultSetMetaData list = this.dMetasearch.search(query, docType, fields, caseSensitive);
        List<ResultSetMetaData> finalResults = this.divideSearchResults(list, members);
        return finalResults;
    }

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, aplicando el
     * principio MetaSearch y mecanismos de división del trabajo, con todos los
     * buscadores activos en el servidor
     *
     * @param query         consulta de la búsqueda
     * @param docTypes      tipo de documento
     * @param field         campo del documento
     * @param caseSensitive tener en cuenta mayúsculas y minísculas
     * @param members       número de miembros de la sesión
     *
     * @return lista de documentos encontrados
     *
     * @throws SearchException  si ocurre alguna excepción durante el proceso de búsqueda
     */
    public List<ResultSetMetaData> search(String query, String[] docTypes, int field, boolean caseSensitive, int members) throws  SearchException {
        ResultSetMetaData list = this.dMetasearch.search(query, docTypes, field, caseSensitive);
        List<ResultSetMetaData> finalResults = this.divideSearchResults(list, members);
        return finalResults;
    }

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, aplicando el
     * principio MetaSearch y mecanismos de división del trabajo, con todos los
     * buscadores activos en el servidor
     *
     * @param query         consulta de la búsqueda
     * @param docTypes      tipo de documento
     * @param fields        campos del documento
     * @param caseSensitive tener en cuenta mayúsculas y minísculas
     * @param members       número de miembros de la sesión
     *
     * @return lista de documentos encontrados
     *
     * @throws SearchException  si ocurre alguna excepción durante el proceso de búsqueda
     */
    public List<ResultSetMetaData> search(String query, String[] docTypes, int[] fields, boolean caseSensitive, int members) throws  SearchException {
        ResultSetMetaData list = this.dMetasearch.search(query, docTypes, fields, caseSensitive);
        List<ResultSetMetaData> finalResults = this.divideSearchResults(list, members);
        return finalResults;
    }

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, aplicando el
     * principio MetaSearch y mecanismos de división del trabajo, con los buscadores
     * seleccionados
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
        ResultSetMetaData list = this.dMetasearch.search(searchers, query, caseSensitive);
        List<ResultSetMetaData> finalResults = this.divideSearchResults(list, members);
        return finalResults;
    }

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, aplicando el
     * principio MetaSearch y mecanismos de división del trabajo, con los buscadores
     * seleccionados
     *
     * @param searchers     buscadores
     * @param query         consulta de la búsqueda
     * @param field         tipo de documento
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
    public List<ResultSetMetaData> search(int[] searchers, String query, int field, boolean caseSensitive, int members) throws  SearchException, SearchableException {
        ResultSetMetaData list = this.dMetasearch.search(searchers, query, field, caseSensitive);
        List<ResultSetMetaData> finalResults = this.divideSearchResults(list, members);
        return finalResults;
    }

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, aplicando el
     * principio MetaSearch y mecanismos de división del trabajo, con los buscadores
     * seleccionados
     *
     * @param searchers     buscadores
     * @param query         consulta de la búsqueda
     * @param fields        tipo de documento
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
    public List<ResultSetMetaData> search(int[] searchers, String query, int[] fields, boolean caseSensitive, int members) throws  SearchException, SearchableException {
        ResultSetMetaData list = this.dMetasearch.search(searchers, query, fields, caseSensitive);
        List<ResultSetMetaData> finalResults = this.divideSearchResults(list, members);
        return finalResults;
    }

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, aplicando el
     * principio MetaSearch y mecanismos de división del trabajo, con los buscadores
     * seleccionados
     *
     * @param searchers     buscadores
     * @param query         consulta de la búsqueda
     * @param docType       tipo de documento
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
    public List<ResultSetMetaData> search(int[] searchers, String query, String docType, boolean caseSensitive, int members) throws  SearchException, SearchableException {
        ResultSetMetaData list = this.dMetasearch.search(searchers, query, docType, caseSensitive);
        List<ResultSetMetaData> finalResults = this.divideSearchResults(list, members);
        return finalResults;
    }

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, aplicando el
     * principio MetaSearch y mecanismos de división del trabajo, con los buscadores
     * seleccionados
     *
     * @param searchers     buscadores
     * @param query         consulta de la búsqueda
     * @param docTypes      tipo de documento
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
    public List<ResultSetMetaData> search(int[] searchers, String query, String[] docTypes, boolean caseSensitive, int members) throws  SearchException, SearchableException {
        ResultSetMetaData list = this.dMetasearch.search(searchers, query, docTypes, caseSensitive);
        List<ResultSetMetaData> finalResults = this.divideSearchResults(list, members);
        return finalResults;
    }

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, aplicando el
     * principio MetaSearch y mecanismos de división del trabajo, con los buscadores
     * seleccionados
     *
     * @param searchers     buscadores
     * @param query         consulta de la búsqueda
     * @param docType       tipo de documento
     * @param field         tipo de documento
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
    public List<ResultSetMetaData> search(int[] searchers, String query, String docType, int field, boolean caseSensitive, int members) throws  SearchException, SearchableException {
        ResultSetMetaData list = this.dMetasearch.search(searchers, query, docType, field, caseSensitive);
        List<ResultSetMetaData> finalResults = this.divideSearchResults(list, members);
        return finalResults;
    }

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, aplicando el
     * principio MetaSearch y mecanismos de división del trabajo, con los buscadores
     * seleccionados
     *
     * @param searchers     buscadores
     * @param query         consulta de la búsqueda
     * @param docType       tipo de documento
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
    public List<ResultSetMetaData> search(int[] searchers, String query, String docType, int[] fields, boolean caseSensitive, int members) throws  SearchException, SearchableException {
        ResultSetMetaData list = this.dMetasearch.search(searchers, query, docType, fields, caseSensitive);
        List<ResultSetMetaData> finalResults = this.divideSearchResults(list, members);
        return finalResults;
    }

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, aplicando el
     * principio MetaSearch y mecanismos de división del trabajo, con los buscadores
     * seleccionados
     *
     * @param searchers     buscadores
     * @param query         consulta de la búsqueda
     * @param docTypes      tipo de documento
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
    public List<ResultSetMetaData> search(int[] searchers, String query, String[] docTypes, int field, boolean caseSensitive, int members) throws  SearchException, SearchableException {
        ResultSetMetaData list = this.dMetasearch.search(searchers, query, docTypes, field, caseSensitive);
        List<ResultSetMetaData> finalResults = this.divideSearchResults(list, members);
        return finalResults;
    }

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, aplicando el
     * principio MetaSearch y mecanismos de división del trabajo, con los buscadores
     * seleccionados
     *
     * @param searchers     buscadores
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
    public List<ResultSetMetaData> search(int[] searchers, String query, String[] docTypes, int[] fields, boolean caseSensitive, int members) throws  SearchException, SearchableException {
        ResultSetMetaData list = this.dMetasearch.search(searchers, query, docTypes, fields, caseSensitive);
        List<ResultSetMetaData> finalResults = this.divideSearchResults(list, members);
        return finalResults;
    }

    /**
     * Este método es empleado internamente por los métodos de búsqueda que aplican
     * mecanismos de división del trabajo, para distribuir los resultados obtenidos
     * de todos los buscadores entre los miembros de la sesión
     *
     * @param ResultSetMetaData resultados de la búsqueda
     * @param members       número de miembros de la sesión
     *
     * @return lista con los resultados a enviar a cada miembro de la sesión
     */
    private List<ResultSetMetaData> divideSearchResults(ResultSetMetaData resultslist, int members) {

        List<DocumentMetaData> values = resultslist.getAllResultList();
        String query = resultslist.getQuery();
      
        List<ResultSetMetaData> list = this.getResultsList(query, MULTIPLE_SEARCHERS, members);
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
                valueTemp = itemHash.get(MULTIPLE_SEARCHERS);
                doc = values.get(count);
                valueTemp.add(doc);
                count++;
            }
        }

        for (int j = 0; j < a; j++) {
            itemList = list.get(j);
            itemHash = itemList.getResultsMap();
            valueTemp = itemHash.get(MULTIPLE_SEARCHERS);
            doc = values.get(count);
            valueTemp.add(doc);
            count++;
        }


        return list;
    }

    /**
     * Este método es empleado internamente por el método
     * <code>divideSearchResults(ResultSetMetaData ResultSetMetaData, int members)</code>
     * para obtener una lista que almacenará la lista de MetaDocumentos por cada usuario
     * de la sesión
     *
     * @param members número de miembros de la sesión
     *
     * @return lista de MetaDocumentos.
     */
    private List<ResultSetMetaData> getResultsList(String query, int searchEngine, int members) {
        List<ResultSetMetaData> list = new ArrayList<ResultSetMetaData>(members);

        for (int i = 0; i < members; i++) {
            list.add(new ResultSetMetaData(query, searchEngine));
        }
        return list;
    }

    /**
     * Este método es empleado internamente por los métodos <code>executeSearch(...)</code>
     * que emplean el mecanismo  multi-search en sus dos variantes, con el
     * propósito de eliminar los documentos repetidos.
     *
     * @param list lista de Documentos.
     *
     * @return lista de Documentos sin repeticiones.
     */
    private List<ResultSetMetaData> removeRepeatedDocuments(List<ResultSetMetaData> list) {

        ResultSetMetaData documentsList, documentsTemp;
        List<DocumentMetaData> documents, temp;
        DocumentMetaData metaDocument;
        int next, index = 0;
        int size = list.size();

        for (int i = 0; i < size; i++) {
            documentsList = list.get(i);
            documents = documentsList.getAllResultList();
            next = 0;
            for (int j = 0; j < documents.size(); j++) {
                metaDocument = documents.get(j);
                next = i + 1;
                if (next < size) {
                    documentsTemp = list.get(next);
                    temp = documentsTemp.getAllResultList();
                    index = temp.indexOf(metaDocument);
                    if (index >= 0) {
                        temp.remove(index);
                    }
                }
            }
        }
        return list;
    }
}
