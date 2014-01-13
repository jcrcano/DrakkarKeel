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
import static drakkar.oar.util.KeySearchable.*;
import drakkar.mast.SearchException;
import drakkar.mast.SearchableException;
import drakkar.mast.retrieval.AdvSearchEngine;
import drakkar.mast.retrieval.Searchable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Esta clase maneja todos los métodos de búsqueda no colaborativa con fusión de
 * resultados, que pueden ser invocados por los clientes
 *
 * 
 */
public class DefaultMetaSearch extends SearchFactory {

    /**
     * Constructor de la clase
     *
     * @param searcher lista de buscadores
     */
    public DefaultMetaSearch(Map<Integer, Searchable> searcherHash, List<Searchable> searchersList) {
        super(searcherHash, searchersList);
    }


    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, empleando todos
     * los buscadores activos en el servidor
     *
     * @param query          consulta de la búsqueda
     * @param caseSensitive  tener en cuenta mayúsculas y minísculas
     *
     * @return lista de documentos encontrados
     *
     * @throws SearchException  si ocurre alguna excepción durante el proceso de búsqueda
     *
     */
    public ResultSetMetaData search(String query, boolean caseSensitive) throws SearchException {
       
        ResultSetMetaData results = new ResultSetMetaData(query);

        ArrayList<DocumentMetaData> temp = null;

        for (Searchable item : searchersList) {           
            if (item.isEnabled()) {                
                temp = item.search(query, caseSensitive);
                results.add(item.getID(), temp);
            }
        }

        ResultSetMetaData docs = metaSearch(results);
        return docs;
    }

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, empleando todos
     * los buscadores activos en el servidor
     *
     * @param query         consulta de la búsqueda
     * @param field         campo del documento
     * @param caseSensitive tener en cuenta mayúsculas y minísculas
     * 
     * @return lista de documentos encontrados
     *
     * @throws SearchException  si ocurre alguna excepción durante el proceso de búsqueda
     */
    public ResultSetMetaData search(String query, int field, boolean caseSensitive) throws SearchException {
        ResultSetMetaData results = new ResultSetMetaData(query);
        ArrayList<DocumentMetaData> temp = null;

        for (Searchable item : searchersList) {
            if (item.isEnabled() && item instanceof AdvSearchEngine) {
                temp = ((AdvSearchEngine) item).search(query, field, caseSensitive);
                results.add( item.getID(), temp);
            }
        }

        ResultSetMetaData docs = metaSearch(results);
        return docs;
    }

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, empleando todos
     * los buscadores activos en el servidor
     *
     * @param query         consulta de la búsqueda
     * @param fields        campos del documento
     * @param caseSensitive tener en cuenta mayúsculas y minísculas
     *
     * @return lista de documentos encontrados
     *
     * @throws SearchException  si ocurre alguna excepción durante el proceso de búsqueda
     *
     */
    public ResultSetMetaData search(String query, int[] fields, boolean caseSensitive) throws SearchException {
        ResultSetMetaData results = new ResultSetMetaData(query);
        ArrayList<DocumentMetaData> temp = null;

        for (Searchable item : searchersList) {
            if (item.isEnabled() && item instanceof AdvSearchEngine) {
                temp = ((AdvSearchEngine) item).search(query, fields, caseSensitive);
                results.add(item.getID(), temp);
            }
        }

        ResultSetMetaData docs = metaSearch(results);
        return docs;
    }

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, empleando todos
     * los buscadores activos en el servidor
     *
     * @param query         consulta de la búsqueda
     * @param docType       tipo de documento
     * @param caseSensitive tener en cuenta mayúsculas y minísculas
     * 
     * @return lista de documentos encontrados
     *
     * @throws SearchException  si ocurre alguna excepción durante el proceso de búsqueda
     *
     */
    public ResultSetMetaData search(String query, String docType, boolean caseSensitive) throws SearchException {
        ResultSetMetaData results = new ResultSetMetaData(query);
        ArrayList<DocumentMetaData> temp = null;

        for (Searchable item : searchersList) {
            if (item.isEnabled()) {
                temp = item.search(query, docType, caseSensitive);
                results.add( item.getID(), temp);
            }
        }

        ResultSetMetaData docs = metaSearch( results);
        return docs;
    }

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, empleando todos
     * los buscadores activos en el servidor
     *
     * @param query         consulta de la búsqueda
     * @param docTypes      tipo de documento
     * @param caseSensitive tener en cuenta mayúsculas y minísculas
     *
     * @return lista de documentos encontrados
     *
     * @throws SearchException  si ocurre alguna excepción durante el proceso de búsqueda
     *
     */
    public ResultSetMetaData search(String query, String[] docTypes, boolean caseSensitive) throws SearchException {
        ResultSetMetaData results = new ResultSetMetaData(query);
        ArrayList<DocumentMetaData> temp = null;

        for (Searchable item : searchersList) {
            if (item.isEnabled()) {
                temp = item.search(query, docTypes, caseSensitive);
                results.add( item.getID(), temp);
            }
        }

        ResultSetMetaData docs = metaSearch( results);
        return docs;
    }

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, empleando todos
     * los buscadores activos en el servidor
     *
     * @param query         consulta de la búsqueda
     * @param docType       tipo de documento
     * @param field         campo del documento
     * @param caseSensitive tener en cuenta mayúsculas y minísculas
     * 
     * @return lista de documentos encontrados
     *
     * @throws SearchException  si ocurre alguna excepción durante el proceso de búsqueda
     *
     */
    public ResultSetMetaData search(String query, String docType, int field, boolean caseSensitive) throws SearchException {
        ResultSetMetaData results = new ResultSetMetaData(query);
        ArrayList<DocumentMetaData> temp = null;
        AdvSearchEngine engine;

        for (Searchable item : searchersList) {
            if (item.isEnabled() && item instanceof AdvSearchEngine) {
                engine = (AdvSearchEngine) item;
                temp = engine.search(query, docType, field, caseSensitive);
                results.add( item.getID(), temp);
            }
        }

        ResultSetMetaData docs = metaSearch( results);
        return docs;
    }

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, empleando todos
     * los buscadores activos en el servidor
     *
     * @param query         consulta de la búsqueda
     * @param docType       tipo de documento
     * @param fields        campos del documento
     * @param caseSensitive tener en cuenta mayúsculas y minísculas
     * 
     * @return lista de documentos encontrados
     *
     * @throws SearchException  si ocurre alguna excepción durante el proceso de búsqueda
     *
     */
    public ResultSetMetaData search(String query, String docType, int[] fields, boolean caseSensitive) throws SearchException {
        ResultSetMetaData results = new ResultSetMetaData(query);
        ArrayList<DocumentMetaData> temp = null;

        for (Searchable item : searchersList) {
            if (item.isEnabled() && item instanceof AdvSearchEngine) {
                temp = ((AdvSearchEngine) item).search(query, docType, fields, caseSensitive);
                results.add( item.getID(), temp);
            }
        }

        ResultSetMetaData docs = metaSearch( results);
        return docs;
    }

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, empleando todos
     * los buscadores activos en el servidor
     *
     * @param query         consulta de la búsqueda
     * @param docTypes      tipo de documento
     * @param field         campos del documento
     * @param caseSensitive tener en cuenta mayúsculas y minísculas
     *
     * @return lista de documentos encontrados
     *
     * @throws SearchException  si ocurre alguna excepción durante el proceso de búsqueda
     *
     */
    public ResultSetMetaData search(String query, String[] docTypes, int field, boolean caseSensitive) throws SearchException {
        ResultSetMetaData results = new ResultSetMetaData(query);
        ArrayList<DocumentMetaData> temp = null;

        for (Searchable item : searchersList) {
            if (item.isEnabled() && item instanceof AdvSearchEngine) {
                temp = ((AdvSearchEngine) item).search(query, docTypes, field, caseSensitive);
                results.add( item.getID(), temp);
            }
        }

        ResultSetMetaData docs = metaSearch( results);
        return docs;
    }

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, empleando todos
     * los buscadores activos en el servidor
     *
     * @param query         consulta de la búsqueda
     * @param docTypes      tipo de documento
     * @param fields        campos del documento
     * @param caseSensitive tener en cuenta mayúsculas y minísculas
     *
     * @return lista de documentos encontrados
     *
     * @throws SearchException  si ocurre alguna excepción durante el proceso de búsqueda
     *
     */
    public ResultSetMetaData search(String query, String[] docTypes, int[] fields, boolean caseSensitive) throws SearchException {
        ResultSetMetaData results = new ResultSetMetaData(query);
        ArrayList<DocumentMetaData> temp = null;

        for (Searchable item : searchersList) {
            if (item.isEnabled() && item instanceof AdvSearchEngine) {
                temp = ((AdvSearchEngine) item).search(query, docTypes, fields, caseSensitive);
                results.add( item.getID(), temp);
            }
        }

        ResultSetMetaData docs = metaSearch( results);
        return docs;
    }

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, empleando los
     * los buscadores seleccionados
     *
     * @param searchers     buscadores
     * @param query         consulta de la búsqueda
     * @param caseSensitive tener en cuenta mayúsculas y minísculas
     *
     * @return lista de documentos encontrados
     *
     * @throws SearchableException  si el buscador especificado  no es soportado por el servidor
     * @throws SearchException  si ocurre alguna excepción durante el proceso de búsqueda
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
    public ResultSetMetaData search(int[] searchers, String query, boolean caseSensitive) throws SearchableException, SearchException {
        ResultSetMetaData results = new ResultSetMetaData(query);
        ArrayList<DocumentMetaData> temp = null;
        Searchable searcherItem;

        for (Integer item : searchers) {
            searcherItem = this.searchersHash.get(item);
            if (searcherItem != null) {
                if (searcherItem.isEnabled()) {
                    temp = searcherItem.search(query, caseSensitive);
                    results.add( searcherItem.getID(), temp);
                }
            }
        }
        if (results.isEmpty()) {
            throw new SearchableException("Searchable not supported");
        }
        ResultSetMetaData docs = metaSearch( results);
        return docs;
    }

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, empleando los
     * los buscadores seleccionados
     *
     * @param searchers     buscadores
     * @param query         consulta de la búsqueda
     * @param field         campo del documento
     * @param caseSensitive tener en cuenta mayúsculas y minísculas
     *
     * @return lista de documentos encontrados
     *
     * @throws SearchableException  si el buscador especificado  no es soportado por el servidor
     * @throws SearchException  si ocurre alguna excepción durante el proceso de búsqueda
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
    public ResultSetMetaData search(int[] searchers, String query, int field, boolean caseSensitive) throws SearchableException, SearchException {
        ResultSetMetaData results = new ResultSetMetaData(query);
        ArrayList<DocumentMetaData> temp = null;
        Searchable searcherItem;

        for (Integer item : searchers) {
            searcherItem = this.searchersHash.get(item);
            if (searcherItem != null) {
                if (searcherItem.isEnabled() && searcherItem instanceof AdvSearchEngine) {
                    temp = ((AdvSearchEngine) searcherItem).search(query, field, caseSensitive);
                    results.add(searcherItem.getID(), temp);
                }
            }
        }
        if (results.isEmpty()) {
            throw new SearchableException("Searchable not supported");
        }
        ResultSetMetaData docs = metaSearch( results);
        return docs;
    }

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, empleando los
     * los buscadores seleccionados
     *
     * @param searchers     buscadores
     * @param query         consulta de la búsqueda
     * @param fields        campos del documento
     * @param caseSensitive tener en cuenta mayúsculas y minísculas
     *
     * @return lista de documentos encontrados
     *
     * @throws SearchableException  si el buscador especificado  no es soportado por el servidor
     * @throws SearchException  si ocurre alguna excepción durante el proceso de búsqueda
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
    public ResultSetMetaData search(int[] searchers, String query, int[] fields, boolean caseSensitive) throws SearchableException, SearchException {
        ResultSetMetaData results = new ResultSetMetaData(query);
        ArrayList<DocumentMetaData> temp = null;
        Searchable searcherItem;

        for (Integer item : searchers) {
            searcherItem = this.searchersHash.get(item);
            if (searcherItem != null) {
                if (searcherItem.isEnabled() && searcherItem instanceof AdvSearchEngine) {
                    temp = ((AdvSearchEngine) searcherItem).search(query, fields, caseSensitive);
                    results.add(searcherItem.getID(), temp);
                }
            }
        }
        if (results.isEmpty()) {
            throw new SearchableException("Searchable not supported");
        }
        ResultSetMetaData docs = metaSearch( results);
        return docs;
    }

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, empleando los
     * los buscadores seleccionados
     *
     * @param searchers     buscadores
     * @param query         consulta de la búsqueda
     * @param docType       tipo de documento
     * @param caseSensitive tener en cuenta mayúsculas y minísculas
     *
     * @return lista de documentos encontrados
     *
     * @throws SearchableException  si el buscador especificado  no es soportado por el servidor
     * @throws SearchException  si ocurre alguna excepción durante el proceso de búsqueda
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
    public ResultSetMetaData search(int[] searchers, String query, String docType, boolean caseSensitive) throws SearchableException, SearchException {
        ResultSetMetaData results = new ResultSetMetaData(query);
        ArrayList<DocumentMetaData> temp = null;
        Searchable searcherItem;

        for (Integer item : searchers) {
            searcherItem = this.searchersHash.get(item);
            if (searcherItem != null) {
                if (searcherItem.isEnabled()) {
                    temp = searcherItem.search(query, docType, caseSensitive);
                    results.add(searcherItem.getID(), temp);
                }
            }
        }
        if (results.isEmpty()) {
            throw new SearchableException("Searchable not supported");
        }
        ResultSetMetaData docs = metaSearch( results);
        return docs;
    }

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, empleando los
     * los buscadores seleccionados
     *
     * @param searchers     buscadores
     * @param query         consulta de la búsqueda
     * @param docTypes      tipos de documento
     * @param caseSensitive tener en cuenta mayúsculas y minísculas
     *
     * @return lista de documentos encontrados
     *
     * @throws SearchableException  si el buscador especificado  no es soportado por el servidor
     * @throws SearchException  si ocurre alguna excepción durante el proceso de búsqueda
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
    public ResultSetMetaData search(int[] searchers, String query, String[] docTypes, boolean caseSensitive) throws SearchableException, SearchException {
        ResultSetMetaData results = new ResultSetMetaData(query);
        ArrayList<DocumentMetaData> temp = null;
        Searchable searcherItem;

        for (Integer item : searchers) {
            searcherItem = this.searchersHash.get(item);
            if (searcherItem != null) {
                if (searcherItem.isEnabled()) {
                    temp = searcherItem.search(query, docTypes, caseSensitive);
                    results.add(searcherItem.getID(), temp);
                }
            }
        }
        if (results.isEmpty()) {
            throw new SearchableException("Searchable not supported");
        }
        ResultSetMetaData docs = metaSearch( results);
        return docs;
    }

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, empleando los
     * los buscadores seleccionados
     *
     * @param searchers     buscadores
     * @param query         consulta de la búsqueda
     * @param docType       tipo de documento
     * @param field         campo del documento
     * @param caseSensitive tener en cuenta mayúsculas y minísculas
     *
     * @return lista de documentos encontrados
     *
     * @throws SearchableException  si el buscador especificado  no es soportado por el servidor
     * @throws SearchException  si ocurre alguna excepción durante el proceso de búsqueda
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
    public ResultSetMetaData search(int[] searchers, String query, String docType, int field, boolean caseSensitive) throws SearchableException, SearchException {
        ResultSetMetaData results = new ResultSetMetaData(query);
        ArrayList<DocumentMetaData> temp = null;
        Searchable searcherItem;

        for (Integer item : searchers) {
            searcherItem = this.searchersHash.get(item);
            if (searcherItem != null) {
                if (searcherItem.isEnabled() && searcherItem instanceof AdvSearchEngine) {
                    temp = ((AdvSearchEngine) searcherItem).search(query, docType, field, caseSensitive);
                    results.add(searcherItem.getID(), temp);
                }
            }
        }
        if (results.isEmpty()) {
            throw new SearchableException("Searchable not supported");
        }
        ResultSetMetaData docs = metaSearch( results);
        return docs;
    }

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, empleando los
     * los buscadores seleccionados
     *
     * @param searchers     buscadores
     * @param query         consulta de la búsqueda
     * @param docType       tipo de documento
     * @param fields        campos del documento
     * @param caseSensitive tener en cuenta mayúsculas y minísculas
     *
     * @return lista de documentos encontrados
     *
     * @throws SearchableException  si el buscador especificado  no es soportado por el servidor
     * @throws SearchException  si ocurre alguna excepción durante el proceso de búsqueda
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
    public ResultSetMetaData search(int[] searchers, String query, String docType, int[] fields, boolean caseSensitive) throws SearchableException, SearchException {
        ResultSetMetaData results = new ResultSetMetaData(query);
        ArrayList<DocumentMetaData> temp = null;
        Searchable searcherItem;

        for (Integer item : searchers) {
            searcherItem = this.searchersHash.get(item);
            if (searcherItem != null) {

                if (searcherItem.isEnabled() && searcherItem instanceof AdvSearchEngine) {
                    temp = ((AdvSearchEngine) searcherItem).search(query, docType, fields, caseSensitive);
                    results.add(searcherItem.getID(), temp);
                }
            }
        }
        if (results.isEmpty()) {
            throw new SearchableException("Searchable not supported");
        }
        ResultSetMetaData docs = metaSearch( results);
        return docs;
    }

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, empleando los
     * los buscadores seleccionados
     *
     * @param searchers     buscadores
     * @param query         consulta de la búsqueda
     * @param docTypes      tipos de documento
     * @param field         campo del documento
     * @param caseSensitive tener en cuenta mayúsculas y minísculas
     *
     * @return lista de documentos encontrados
     *
     * @throws SearchableException  si el buscador especificado  no es soportado por el servidor
     * @throws SearchException  si ocurre alguna excepción durante el proceso de búsqueda
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
    public ResultSetMetaData search(int[] searchers, String query, String[] docTypes, int field, boolean caseSensitive) throws SearchableException, SearchException {
        ResultSetMetaData results = new ResultSetMetaData(query);
        ArrayList<DocumentMetaData> temp = null;
        Searchable searcherItem;

        for (Integer item : searchers) {
            searcherItem = this.searchersHash.get(item);
            if (searcherItem != null) {
                if (searcherItem.isEnabled() && searcherItem instanceof AdvSearchEngine) {
                    temp = ((AdvSearchEngine) searcherItem).search(query, docTypes, field, caseSensitive);
                    results.add(searcherItem.getID(), temp);
                }
            }
        }
        if (results.isEmpty()) {
            throw new SearchableException("Searchable not supported");
        }
        ResultSetMetaData docs = metaSearch( results);
        return docs;
    }

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, empleando los
     * los buscadores seleccionados
     *
     * @param searchers     buscadores
     * @param query         consulta de la búsqueda
     * @param docTypes      tipos de documento
     * @param fields        campos del documento
     * @param caseSensitive tener en cuenta mayúsculas y minísculas
     *
     * @return lista de documentos encontrados
     *
     * @throws SearchableException  si el buscador especificado  no es soportado por el servidor
     * @throws SearchException  si ocurre alguna excepción durante el proceso de búsqueda
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
    public ResultSetMetaData search(int[] searchers, String query, String[] docTypes, int[] fields, boolean caseSensitive) throws SearchableException, SearchException {
        ResultSetMetaData results = new ResultSetMetaData();
        ArrayList<DocumentMetaData> temp = null;
        Searchable searcherItem;

        for (Integer item : searchers) {
            searcherItem = this.searchersHash.get(item);
            if (searcherItem != null) {
                if (searcherItem.isEnabled() && searcherItem instanceof AdvSearchEngine) {
                    temp = ((AdvSearchEngine) searcherItem).search(query, docTypes, fields, caseSensitive);
                    results.add(searcherItem.getID(), temp);
                }
            }
        }
        if (results.isEmpty()) {
            throw new SearchableException("Searchable not supported");
        }
        ResultSetMetaData docs = metaSearch( results);
        return docs;
    }

    private ResultSetMetaData metaSearch(ResultSetMetaData results) {
       List<DocumentMetaData> metaResults;

        metaResults = RankingFusion.fusion(results, RankingFusion.DEFAULT_NORMALIZE, RankingFusion.DEFAULT_COMBINER);
        ResultSetMetaData docs = new ResultSetMetaData(results.getQuery(), MULTIPLE_SEARCHERS, metaResults);
        return docs;
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
