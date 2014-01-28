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
import drakkar.oar.svn.SVNData;
import drakkar.oar.util.KeySearchable;
import drakkar.mast.SearchException;
import drakkar.mast.SearchableException;
import drakkar.mast.retrieval.AdvSearchEngine;
import drakkar.mast.retrieval.CVSSearch;
import drakkar.mast.retrieval.SVNSearch;
import drakkar.mast.retrieval.Searchable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Esta clase maneja todos los métodos de búsqueda soportados para un solo
 * buscador, que pueden ser invocados por los clientes
 *
 * 
 *
 */
public class DefaultSingleSearch extends SearchFactory {


    /**
     * Constructor de la clase
     *
     * @param searchers lista de buscadores
     */
    public DefaultSingleSearch(Map<Integer, Searchable> searcherHash, List<Searchable> searchersList) {
        super(searcherHash, searchersList);
    }


    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, con el buscador
     * seleccionado
     *
     * @param query          consulta de la búsqueda
     * @param searcher       buscador
     * @param caseSensitive  tener en cuenta mayúsculas y minísculas
     *
     * @return lista de documentos encontrados
     *
     * @throws SearchableException  si el buscador no es soportado por el servidor
     * @throws SearchException  si ocurre alguna excepción durante el proceso de búsqueda
     * 
     * <br>
     * <br>
     * <b>Nota:</b>
     * <br>
     * Las constantes que representan los diferentes buscadores soportados, se encuentran
     * definidas en la clase <code>KeySearchable</code>, del paquete drakkar.oar.util
     * <br>
     *
     * @see KeySearchable
     */
    public ResultSetMetaData search(String query, int searcher, boolean caseSensitive) throws SearchableException, SearchException {
        ResultSetMetaData results = new ResultSetMetaData(query);
        ArrayList<DocumentMetaData> temp = null;

        Searchable item = this.searchersHash.get(searcher);
        if (item != null) {
            if (item.isEnabled()) {
                temp = item.search(query, caseSensitive);
                results.add(item.getID(), temp);
                return results;
            } else {
                throw new SearchableException("The Searchable is not available");
            }
        } else {
            throw new SearchableException("They were not available Searchable");
        }
    }

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, con el buscador
     * seleccionado
     *
     * @param query          consulta de la búsqueda
     * @param field          campo del documento
     * @param searcher       buscador
     * @param caseSensitive  tener en cuenta mayúsculas y minísculas
     *
     * @return lista de documentos encontrados
     *
     * @throws SearchableException  si el buscador no es soportado por el servidor
     * @throws SearchException  si ocurre alguna excepción durante el proceso de búsqueda
     *
     * <br>
     * <br>
     * <b>Nota:</b>
     * <br>
     * Las constantes que representan los diferentes buscadores soportados, se encuentran
     * definidas en la clase <code>KeySearchable</code>, del paquete drakkar.oar.util
     * <br>
     *
     * @see KeySearchable
     */
    public ResultSetMetaData search(String query, int field, int searcher, boolean caseSensitive) throws SearchableException, SearchException {
        ResultSetMetaData results = new ResultSetMetaData(query);
        ArrayList<DocumentMetaData> temp = null;

        Searchable item = this.searchersHash.get(searcher);
        if (item != null) {
            if (item.isEnabled() && item instanceof AdvSearchEngine) {
                temp = ((AdvSearchEngine) item).search(query, field, caseSensitive);
                results.add(item.getID(), temp);
                return results;
            } else {
                throw new SearchableException("The Searchable is not available");
            }

        } else {
            throw new SearchableException("They were not available Searchable");
        }
    }

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, con el buscador
     * seleccionado
     *
     * @param query          consulta de la búsqueda
     * @param fields         campos del documento
     * @param searcher       buscador
     * @param caseSensitive  tener en cuenta mayúsculas y minísculas
     *
     * @return lista de documentos encontrados
     *
     * @throws SearchableException  si el buscador no es soportado por el servidor
     * @throws SearchException  si ocurre alguna excepción durante el proceso de búsqueda
     *
     * <br>
     * <br>
     * <b>Nota:</b>
     * <br>
     * Las constantes que representan los diferentes buscadores soportados, se encuentran
     * definidas en la clase <code>KeySearchable</code>, del paquete drakkar.oar.util
     * <br>
     *
     * @see KeySearchable
     */
    public ResultSetMetaData search(String query, int[] fields, int searcher, boolean caseSensitive) throws SearchableException, SearchException {
        ResultSetMetaData results = new ResultSetMetaData(query);
        ArrayList<DocumentMetaData> temp = null;

        Searchable item = this.searchersHash.get(searcher);
        if (item != null) {
            if (item.isEnabled() && item instanceof AdvSearchEngine) {
                temp = ((AdvSearchEngine) item).search(query, fields, caseSensitive);
                results.add(item.getID(), temp);
                return results;
            } else {
                throw new SearchableException("The Searchable is not available");
            }

        } else {
            throw new SearchableException("They were not available Searchable");
        }
    }

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, con el buscador
     * seleccionado
     *
     * @param query          consulta de la búsqueda
     * @param docType        tipo de documento
     * @param searcher       buscador
     * @param caseSensitive  tener en cuenta mayúsculas y minísculas
     *
     * @return lista de documentos encontrados
     *
     * @throws SearchableException  si el buscador no es soportado por el servidor
     * @throws SearchException  si ocurre alguna excepción durante el proceso de búsqueda
     *
     * <br>
     * <br>
     * <b>Nota:</b>
     * <br>
     * Las constantes que representan los diferentes buscadores soportados, se encuentran
     * definidas en la clase <code>KeySearchable</code>, del paquete drakkar.oar.util
     * <br>
     *
     * @see KeySearchable
     */
    public ResultSetMetaData search(String query, String docType, int searcher, boolean caseSensitive) throws SearchableException, SearchException {

        ResultSetMetaData results = new ResultSetMetaData(query);
        ArrayList<DocumentMetaData> temp = null;

        Searchable item = this.searchersHash.get(searcher);
        if (item != null) {
            if (item.isEnabled()) {
                temp = item.search(query, docType, caseSensitive);
                results.add(item.getID(), temp);
                return results;
            } else {
                throw new SearchableException("The Searchable is not available");
            }

        } else {
            throw new SearchableException("They were not available Searchable");
        }

    }

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, con el buscador
     * seleccionado
     *
     * @param query          consulta de la búsqueda   
     * @param docTypes       tipo de documento
     * @param searcher       buscador
     * @param caseSensitive  tener en cuenta mayúsculas y minísculas
     *
     * @return lista de documentos encontrados
     *
     * @throws SearchableException  si el buscador no es soportado por el servidor
     * @throws SearchException  si ocurre alguna excepción durante el proceso de búsqueda
     *
     * <br>
     * <br>
     * <b>Nota:</b>
     * <br>
     * Las constantes que representan los diferentes buscadores soportados, se encuentran
     * definidas en la clase <code>KeySearchable</code>, del paquete drakkar.oar.util
     * <br>
     *
     * @see KeySearchable
     */
    public ResultSetMetaData search(String query, String[] docTypes, int searcher, boolean caseSensitive) throws SearchableException, SearchException {

        ResultSetMetaData results = new ResultSetMetaData(query);
        ArrayList<DocumentMetaData> temp = null;

        Searchable item = this.searchersHash.get(searcher);
        if (item != null) {
            if (item.isEnabled()) {
                temp = item.search(query, docTypes, caseSensitive);
                results.add(item.getID(), temp);
                return results;
            } else {
                throw new SearchableException("The Searchable is not available");
            }

        } else {
            throw new SearchableException("They were not available Searchable");
        }
    }

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, con el buscador
     * seleccionado
     *
     * @param query         consulta de la búsqueda
     * @param docType       tipo de documento
     * @param field         campo del documento
     * @param searcher      buscador
     * @param caseSensitive tener en cuenta mayúsculas y minísculas
     *
     * @return lista de documentos encontrados
     *
     * @throws SearchableException  si el buscador no es soportado por el servidor
     * @throws SearchException  si ocurre alguna excepción durante el proceso de búsqueda
     *
     * <br>
     * <br>
     * <b>Nota:</b>
     * <br>
     * Las constantes que representan los diferentes buscadores soportados, se encuentran
     * definidas en la clase <code>KeySearchable</code>, del paquete drakkar.oar.util
     * <br>
     *
     * @see KeySearchable
     */
    public ResultSetMetaData search(String query, String docType, int field, int searcher, boolean caseSensitive) throws SearchableException, SearchException {
        ResultSetMetaData results = new ResultSetMetaData(query);
        ArrayList<DocumentMetaData> temp = null;
        AdvSearchEngine engine;

        Searchable item = this.searchersHash.get(searcher);
        if (item != null) {
            if (item.isEnabled() && item instanceof AdvSearchEngine) {
                engine = (AdvSearchEngine) item;
                temp = engine.search(query, docType, field, caseSensitive);
                results.add(item.getID(), temp);
                return results;
            } else {
                throw new SearchableException("The Searchable is not available");
            }

        } else {
            throw new SearchableException("They were not available Searchable");
        }
    }

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, con el buscador
     * seleccionado
     *
     * @param query         consulta de la búsqueda
     * @param docType       tipo de documento
     * @param fields        campos del documento
     * @param searcher      buscador
     * @param caseSensitive tener en cuenta mayúsculas y minísculas
     *
     * @return lista de documentos encontrados
     *
     * @throws SearchableException  si el buscador no es soportado por el servidor
     * @throws SearchException  si ocurre alguna excepción durante el proceso de búsqueda
     *
     * <br>
     * <br>
     * <b>Nota:</b>
     * <br>
     * Las constantes que representan los diferentes buscadores soportados, se encuentran
     * definidas en la clase <code>KeySearchable</code>, del paquete drakkar.oar.util
     * <br>
     *
     * @see KeySearchable
     */
    public ResultSetMetaData search(String query, String docType, int[] fields, int searcher, boolean caseSensitive) throws SearchableException, SearchException {
        ResultSetMetaData results = new ResultSetMetaData(query);
        ArrayList<DocumentMetaData> temp = null;
        AdvSearchEngine engine;

        Searchable item = this.searchersHash.get(searcher);
        if (item != null) {
            if (item.isEnabled() && item instanceof AdvSearchEngine) {
                engine = (AdvSearchEngine) item;
                temp = engine.search(query, docType, fields, caseSensitive);
                results.add(item.getID(), temp);
                return results;
            } else {
                throw new SearchableException("The Searchable is not available");
            }

        } else {
            throw new SearchableException("They were not available Searchable");
        }
    }

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, con el buscador
     * seleccionado
     *
     * @param query          consulta de la búsqueda
     * @param docTypes       tipo de documento
     * @param field          campo del documento
     * @param searcher       buscador
     * @param caseSensitive  tener en cuenta mayúsculas y minísculas
     *
     * @return lista de documentos encontrados
     *
     * @throws SearchableException  si el buscador no es soportado por el servidor
     * @throws SearchException  si ocurre alguna excepción durante el proceso de búsqueda
     *
     * <br>
     * <br>
     * <b>Nota:</b>
     * <br>
     * Las constantes que representan los diferentes buscadores soportados, se encuentran
     * definidas en la clase <code>KeySearchable</code>, del paquete drakkar.oar.util
     * <br>
     *
     * @see KeySearchable
     */
    public ResultSetMetaData search(String query, String[] docTypes, int field, int searcher, boolean caseSensitive) throws SearchableException, SearchException {
        ResultSetMetaData results = new ResultSetMetaData(query);
        ArrayList<DocumentMetaData> temp = null;

        Searchable item = this.searchersHash.get(searcher);
        if (item != null) {
            if (item.isEnabled() && item instanceof AdvSearchEngine) {
                temp = ((AdvSearchEngine) item).search(query, docTypes, field, caseSensitive);
                results.add(item.getID(), temp);
                return results;
            } else {
                throw new SearchableException("The Searchable is not available");
            }

        } else {
            throw new SearchableException("They were not available Searchable");
        }
    }

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, con el buscador
     * seleccionado
     *
     * @param query          consulta de la búsqueda
     * @param docType        tipo de documento
     * @param fields         campos del documento
     * @param searcher       buscador
     * @param caseSensitive  tener en cuenta mayúsculas y minísculas
     *
     * @return lista de documentos encontrados
     *
     * @throws SearchableException  si el buscador no es soportado por el servidor
     * @throws SearchException  si ocurre alguna excepción durante el proceso de búsqueda
     *
     * <br>
     * <br>
     * <b>Nota:</b>
     * <br>
     * Las constantes que representan los diferentes buscadores soportados, se encuentran
     * definidas en la clase <code>KeySearchable</code>, del paquete drakkar.oar.util
     * <br>
     *
     * @see KeySearchable
     */
    public ResultSetMetaData search(String query, String[] docType, int[] fields, int searcher, boolean caseSensitive) throws SearchableException, SearchException {
        ResultSetMetaData results = new ResultSetMetaData(query);
        ArrayList<DocumentMetaData> temp = null;
        AdvSearchEngine engine;

        Searchable item = this.searchersHash.get(searcher);
        if (item != null) {
            if (item.isEnabled() && item instanceof AdvSearchEngine) {
                engine = (AdvSearchEngine) item;
                temp = engine.search(query, docType, fields, caseSensitive);
                results.add(item.getID(), temp);
                return results;
            } else {
                throw new SearchableException("The Searchable is not available");
            }

        } else {
            throw new SearchableException("They were not available Searchable");
        }
    }

    ///svn search
    public ResultSetMetaData search(String query, SVNData svnRepository, String fileType, String sort, String lastmodified, String user, boolean fileBody) throws SearchableException, SearchException {
        ResultSetMetaData results = new ResultSetMetaData(query);
        ArrayList<DocumentMetaData> temp = null;
        SVNSearch engine;

        Searchable item = this.searchersHash.get(KeySearchable.SVN_SEARCHER);
        if (item != null) {
            if (item.isEnabled() && item instanceof CVSSearch) {
                engine = (SVNSearch) item;
                temp = engine.search(query, svnRepository, fileType, sort, lastmodified, user, fileBody);
                results.add(item.getID(), temp);
                return results;
            } else {
                throw new SearchableException("The Searchable is not available");
            }

        } else {
            throw new SearchableException("They were not available Searchable");
        }
    }
}
