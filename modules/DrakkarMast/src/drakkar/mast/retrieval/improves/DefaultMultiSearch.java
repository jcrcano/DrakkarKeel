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
import drakkar.oar.util.OutputMonitor;
import drakkar.mast.SearchException;
import drakkar.mast.retrieval.AdvSearchEngine;
import drakkar.mast.retrieval.Searchable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Esta clase maneja todos los métodos de búsqueda no colaborativa, con multiples
 * de buscadores que pueden ser invocados por los clientes
 *
 * 
 */
public class DefaultMultiSearch extends SearchFactory {

    /**
     * Constructor de la clase
     *
     * @param searchers lista de buscadores
     */
    public DefaultMultiSearch(Map<Integer, Searchable> searcherHash, List<Searchable> searchersList) {
        super(searcherHash, searchersList);
    }


    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, empleando todos
     * los buscadores activos en el servidor
     *
     * @param query         consulta de la búsqueda
     * @param caseSensitive tener en cuenta mayúsculas y minísculas
     *
     * @return lista de documentos encontrados
     *
     * @throws SearchException  si ocurre alguna excepción durante el proceso de búsqueda
     */
    public ResultSetMetaData search(String query, boolean caseSensitive) throws SearchException {
        ResultSetMetaData results = new ResultSetMetaData(query);
        List<DocumentMetaData> temp = null;

        for (Searchable item : searchersList) {
            if (item.isEnabled()) {
                temp = item.search(query, caseSensitive);
                results.add(item.getID(), temp);
            }
        }
        return results;
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
        List<DocumentMetaData> temp = null;

        for (Searchable item : searchersList) {
            if (item.isEnabled() && item instanceof AdvSearchEngine) {
                temp = ((AdvSearchEngine) item).search(query, field, caseSensitive);
                results.add(item.getID(), temp);
            }
        }

        return results;
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
     */
    public ResultSetMetaData search(String query, int[] fields, boolean caseSensitive) throws SearchException {
        ResultSetMetaData results = new ResultSetMetaData(query);
        List<DocumentMetaData> temp = null;

        for (Searchable item : searchersList) {
            if (item.isEnabled() && item instanceof AdvSearchEngine) {
                temp = ((AdvSearchEngine) item).search(query, fields, caseSensitive);
                results.add(item.getID(), temp);
            }
        }

        return results;
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
     */
    public ResultSetMetaData search(String query, String docType, boolean caseSensitive) throws SearchException {
        ResultSetMetaData results = new ResultSetMetaData(query);
        List<DocumentMetaData> temp = null;

        for (Searchable item : searchersList) {
            if (item.isEnabled()) {
                temp = item.search(query, docType, caseSensitive);
                results.add(item.getID(), temp);
            }
        }

        return results;
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
     */
    public ResultSetMetaData search(String query, String[] docTypes, boolean caseSensitive) throws SearchException {
        ResultSetMetaData results = new ResultSetMetaData(query);
        List<DocumentMetaData> temp = null;

        for (Searchable item : searchersList) {
            if (item.isEnabled()) {
                temp = item.search(query, docTypes, caseSensitive);
                results.add(item.getID(), temp);
            }
        }

        return results;
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
     */
    public ResultSetMetaData search(String query, String docType, int field, boolean caseSensitive) throws SearchException {
        ResultSetMetaData results = new ResultSetMetaData(query);
        List<DocumentMetaData> temp = null;
        AdvSearchEngine engine;

        for (Searchable item : searchersList) {
            if (item.isEnabled() && item instanceof AdvSearchEngine) {
                engine = (AdvSearchEngine) item;
                temp = engine.search(query, docType, field, caseSensitive);
                results.add(item.getID(), temp);
            }
        }

        return results;
    }

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, empleando todos
     * los buscadores activos en el servidor
     *
     * @param query         consulta de la búsqueda
     * @param docTypes      tipo de documento
     * @param field         campo del documento
     * @param caseSensitive tener en cuenta mayúsculas y minísculas
     *
     * @return lista de documentos encontrados
     *
     * @throws SearchException  si ocurre alguna excepción durante el proceso de búsqueda
     */
    public ResultSetMetaData search(String query, String[] docTypes, int field, boolean caseSensitive) throws SearchException {
        ResultSetMetaData results = new ResultSetMetaData(query);
        List<DocumentMetaData> temp = null;
        AdvSearchEngine engine;

        for (Searchable item : searchersList) {
            if (item.isEnabled() && item instanceof AdvSearchEngine) {
                engine = (AdvSearchEngine) item;
                temp = engine.search(query, docTypes, field, caseSensitive);
                results.add(item.getID(), temp);
            }
        }

        return results;
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
     */
    public ResultSetMetaData search(String query, String docType, int[] fields, boolean caseSensitive) throws SearchException {
        ResultSetMetaData results = new ResultSetMetaData(query);
        List<DocumentMetaData> temp = null;
        AdvSearchEngine engine;

        for (Searchable item : searchersList) {
            if (item.isEnabled() && item instanceof AdvSearchEngine) {
                engine = (AdvSearchEngine) item;
                temp = engine.search(query, docType, fields, caseSensitive);
                results.add(item.getID(), temp);
            }
        }

        return results;
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
     */
    public ResultSetMetaData search(String query, String[] docTypes, int[] fields, boolean caseSensitive) throws SearchException {
        ResultSetMetaData results = new ResultSetMetaData(query);
        List<DocumentMetaData> temp = null;
        AdvSearchEngine engine;

        for (Searchable item : searchersList) {
            if (item.isEnabled() && item instanceof AdvSearchEngine) {
                engine = (AdvSearchEngine) item;
                temp = engine.search(query, docTypes, fields, caseSensitive);
                results.add(item.getID(), temp);
            }
        }

        return results;
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
    public ResultSetMetaData search(int[] searchers, String query, boolean caseSensitive) throws SearchException {
        ResultSetMetaData results = new ResultSetMetaData(query);
        List<DocumentMetaData> temp = null;

        Searchable searcherItem;
        for (Integer searcher : searchers) {
            searcherItem = this.searchersHash.get(searcher);
            if (searcherItem != null && searcherItem.isEnabled()) {
                temp = searcherItem.search(query, caseSensitive);
                results.add(searcherItem.getID(), temp);
            }
        }

        return results;
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
    public ResultSetMetaData search(int[] searchers, String query, int field, boolean caseSensitive) throws SearchException {
        ResultSetMetaData results = new ResultSetMetaData(query);
        List<DocumentMetaData> temp = null;

        Searchable searcherItem;
        for (Integer searcher : searchers) {
            searcherItem = this.searchersHash.get(searcher);
            if (searcherItem != null && searcherItem.isEnabled() && searcherItem instanceof AdvSearchEngine) {
                temp = ((AdvSearchEngine) searcherItem).search(query, field, caseSensitive);
                results.add(searcherItem.getID(), temp);
            }
        }

        return results;
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
    public ResultSetMetaData search(int[] searchers, String query, int[] fields, boolean caseSensitive) throws SearchException {
        ResultSetMetaData results = new ResultSetMetaData(query);
        List<DocumentMetaData> temp = null;

        Searchable searcherItem;
        for (Integer searcher : searchers) {
            searcherItem = this.searchersHash.get(searcher);
            if (searcherItem != null && searcherItem.isEnabled() && searcherItem instanceof AdvSearchEngine) {
                temp = ((AdvSearchEngine) searcherItem).search(query, fields, caseSensitive);
                results.add(searcherItem.getID(), temp);
            }
        }

        
        return results;
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
    public ResultSetMetaData search(int[] searchers, String query, String docType, boolean caseSensitive) throws SearchException {
        ResultSetMetaData results = new ResultSetMetaData(query);
        List<DocumentMetaData> temp = null;

        Searchable searcherItem;
        for (Integer item : searchers) {
            searcherItem = this.searchersHash.get(item);

            if (searcherItem != null && searcherItem.isEnabled()) {
                temp = searcherItem.search(query, docType, caseSensitive);
                results.add(searcherItem.getID(), temp);
            }
        }

       
        return results;
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
    public ResultSetMetaData search(int[] searchers, String query, String[] docTypes, boolean caseSensitive) throws SearchException {
        ResultSetMetaData results = new ResultSetMetaData(query);
        List<DocumentMetaData> temp = null;

        Searchable searcherItem;
        for (Integer searcher : searchers) {
            searcherItem = this.searchersHash.get(searcher);

            if (searcherItem != null && searcherItem.isEnabled()) {
                temp = searcherItem.search(query, docTypes, caseSensitive);
                results.add(searcherItem.getID(), temp);
            }
        }

       
        return results;
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
    public ResultSetMetaData search(int[] searchers, String query, String docType, int field, boolean caseSensitive) throws SearchException {
        ResultSetMetaData results = new ResultSetMetaData(query);
        List<DocumentMetaData> temp = null;
        Searchable searcherItem;
        AdvSearchEngine engine;

        for (Integer item : searchers) {
            searcherItem = this.searchersHash.get(item);

            if (searcherItem != null && searcherItem.isEnabled() && searcherItem instanceof AdvSearchEngine) {
                engine = (AdvSearchEngine) searcherItem;
                temp = engine.search(query, docType, field, caseSensitive);
                results.add(searcherItem.getID(), temp);
            }
        }

        return results;
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
    public ResultSetMetaData search(int[] searchers, String query, String docType, int[] fields, boolean caseSensitive) throws SearchException {
        ResultSetMetaData results = new ResultSetMetaData(query);
        List<DocumentMetaData> temp = null;
        Searchable searcherItem;
        AdvSearchEngine engine;

        for (Integer item : searchers) {
            searcherItem = this.searchersHash.get(item);

            if (searcherItem != null && searcherItem.isEnabled() && searcherItem instanceof AdvSearchEngine) {
                engine = (AdvSearchEngine) searcherItem;
                temp = engine.search(query, docType, fields, caseSensitive);
                results.add(searcherItem.getID(), temp);
            }
        }

        return results;
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
    public ResultSetMetaData search(int[] searchers, String query, String[] docTypes, int field, boolean caseSensitive) throws SearchException {
        ResultSetMetaData results = new ResultSetMetaData(query);
        List<DocumentMetaData> temp = null;
        Searchable searcherItem;
        AdvSearchEngine engine;

        for (Integer item : searchers) {
            searcherItem = this.searchersHash.get(item);

            if (searcherItem != null && searcherItem.isEnabled() && searcherItem instanceof AdvSearchEngine) {
                engine = (AdvSearchEngine) searcherItem;
                temp = engine.search(query, docTypes, field, caseSensitive);
                results.add(searcherItem.getID(), temp);
            }
        }

        return results;
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
    public ResultSetMetaData search(int[] searchers, String query, String[] docTypes, int[] fields, boolean caseSensitive) throws SearchException {
        ResultSetMetaData results = new ResultSetMetaData(query);
        List<DocumentMetaData> temp = null;
        Searchable searcherItem;
        AdvSearchEngine engine;

        for (Integer item : searchers) {
            searcherItem = this.searchersHash.get(item);

            if (searcherItem != null && searcherItem.isEnabled() && searcherItem instanceof AdvSearchEngine) {
                engine = (AdvSearchEngine) searcherItem;
                temp = engine.search(query, docTypes, fields, caseSensitive);
                results.add(searcherItem.getID(), temp);
            }
        }

        
        return results;
    }

    
    private ResultSetMetaData removeRepeatedDocuments(ResultSetMetaData documentsList) {

        List<DocumentMetaData> documents, tempDocuments = null;
        ResultSetMetaData temp = documentsList;
        DocumentMetaData metaDocument1, metaDocument2;
        
        Map<Integer, List<DocumentMetaData>> hash = temp.getResultsMap();
        Map<Integer, List<DocumentMetaData>> hashTemp = new HashMap<Integer, List<DocumentMetaData>>();

        Set<Integer> enumeration = hash.keySet();
        for (Integer searcher : enumeration) {
            documents = hash.get(searcher);
            deleteRepeated(documents);

            if (tempDocuments != null) {
                for (int i = 0; i < tempDocuments.size(); i++) {
                    for (int idx = 0; idx < documents.size(); idx++) {
                        metaDocument1 = tempDocuments.get(i);
                        metaDocument2 = documents.get(idx);

                        if(metaDocument1.getPath().equals(metaDocument2.getPath())){
                            documents.remove(metaDocument2);
                        }
                    }
                }
            }
            tempDocuments = documents;
            hashTemp.put(searcher, documents);
        }        

        temp.setResultsMap(hashTemp);
        temp.setQuery(documentsList.getQuery());
       
        return temp;
    }

    private void deleteRepeated(List<DocumentMetaData> list) {

        try {

            for (int i = 0; i < list.size() - 1; i++) {
                for (int k = 0; k < list.size(); k++) {
                    for (int j = i + 1; j < list.size(); j++) {
                        DocumentMetaData docData1 = list.get(j);
                        DocumentMetaData docData2 = list.get(i);
                        if (docData1.getPath().equals(docData2.getPath())) {
                            list.remove(j);
                        }

                    }
                }
            }

        } catch (NullPointerException ex) {
            OutputMonitor.printStream("", ex);
        }

    }
}
