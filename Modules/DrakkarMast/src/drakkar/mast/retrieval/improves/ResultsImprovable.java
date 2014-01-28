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

import drakkar.oar.ResultSetMetaData;
import drakkar.mast.SearchException;
import drakkar.mast.SearchableException;
import java.util.List;

/**
 * Esta interfaz declara los diferentes métodos de búsquedas individuales ó collaborativas
 * soportadas por el framework DrakkarKeel, para los principios de búsqueda SingleSearch y
 * SingleSearch and Split
 *
 * 
 *
 */
public interface ResultsImprovable {

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, con  el
     * buscador especificado
     *
     * @param query         consulta de la búsqueda
     * @param searcher      buscador
     * @param caseSensitive tener en cuenta mayúsculas y minúsculas
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
     * <br>
     *
     * @see KeySearchable
     *
     */
    public ResultSetMetaData search(String query, int searcher, boolean caseSensitive) throws SearchableException, SearchException;

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, con  el
     * buscador especificado
     *
     * @param query         consulta de la búsqueda
     * @param field         campo del documento
     * @param searcher      buscador
     * @param caseSensitive tener en cuenta mayúsculas y minúsculas
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
     * <br>
     *
     * @see KeySearchable
     *
     */
    public ResultSetMetaData search(String query, int field, int searcher, boolean caseSensitive) throws SearchableException, SearchException;

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, con  el
     * buscador especificado
     *
     * @param query         consulta de la búsqueda
     * @param fields        campos del documento
     * @param searcher      buscador
     * @param caseSensitive tener en cuenta mayúsculas y minúsculas
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
     * <br>
     *
     * @see KeySearchable
     *
     */
    public ResultSetMetaData search(String query, int[] fields, int searcher, boolean caseSensitive) throws SearchableException, SearchException;

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, con  el
     * buscador especificado
     *
     * @param query         consulta de la búsqueda
     * @param docType       tipo de documento
     * @param searcher      buscador
     * @param caseSensitive tener en cuenta mayúsculas y minúsculas
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
     * <br>
     *
     * @see KeySearchable
     *
     */
    public ResultSetMetaData search(String query, String docType, int searcher, boolean caseSensitive) throws SearchableException, SearchException;

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, con  el
     * buscador especificado
     *
     * @param query         consulta de la búsqueda
     * @param docTypes      tipos de documento
     * @param searcher      buscador
     * @param caseSensitive tener en cuenta mayúsculas y minúsculas
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
     * <br>
     *
     * @see KeySearchable
     *
     */
    public ResultSetMetaData search(String query, String[] docTypes, int searcher, boolean caseSensitive) throws SearchableException, SearchException;

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, con  el
     * buscador especificado
     *
     * @param query         consulta de la búsqueda
     * @param docType       tipo de documento
     * @param field         campo del documento
     * @param searcher      buscador
     * @param caseSensitive tener en cuenta mayúsculas y minúsculas
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
     * <br>
     *
     * @see KeySearchable
     *
     */
    public ResultSetMetaData search(String query, String docType, int field, int searcher, boolean caseSensitive) throws SearchableException, SearchException;

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, con  el
     * buscador especificado
     *
     * @param query         consulta de la búsqueda
     * @param docType       tipo de documento
     * @param fields        campos del documento
     * @param searcher      buscador
     * @param caseSensitive tener en cuenta mayúsculas y minúsculas
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
     * <br>
     *
     * @see KeySearchable
     *
     */
    public ResultSetMetaData search(String query, String docType, int[] fields, int searcher, boolean caseSensitive) throws SearchableException, SearchException;

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, con  el
     * buscador especificado
     *
     * @param query         consulta de la búsqueda
     * @param docTypes      tipo de documento
     * @param field         campos del documento
     * @param searcher      buscador
     * @param caseSensitive tener en cuenta mayúsculas y minúsculas
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
     * <br>
     *
     * @see KeySearchable
     *
     */
    public ResultSetMetaData search(String query, String[] docTypes, int field, int searcher, boolean caseSensitive) throws SearchableException, SearchException;

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, con  el
     * buscador especificado
     *
     * @param query         consulta de la búsqueda
     * @param docTypes      tipo de documento
     * @param fields        campos del documento
     * @param searcher      buscador
     * @param caseSensitive tener en cuenta mayúsculas y minúsculas
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
     * <br>
     *
     * @see KeySearchable
     *
     */
    public ResultSetMetaData search(String query, String[] docTypes, int[] fields, int searcher, boolean caseSensitive) throws SearchableException, SearchException;

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, con  el
     * buscador especificado, aplicando mecanismos de división del trabajo
     *
     * @param query         consulta de la búsqueda
     * @param searcher      buscador
     * @param caseSensitive tener en cuenta mayúsculas y minúsculas
     * @param members       número de miembros de la sesión
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
     * <br>
     *
     * @see KeySearchable
     *
     */
    public List<ResultSetMetaData> search(int searcher, String query, boolean caseSensitive, int members) throws SearchException, SearchableException;

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, con  el
     * buscador especificado, aplicando mecanismos de división del trabajo
     *
     * @param query         consulta de la búsqueda
     * @param searcher      buscador
     * @param field         campo del documento
     * @param caseSensitive tener en cuenta mayúsculas y minúsculas
     * @param members       número de miembros de la sesión
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
     * <br>
     *
     * @see KeySearchable
     *
     */
    public List<ResultSetMetaData> search(int searcher, String query, int field, boolean caseSensitive, int members) throws SearchException, SearchableException;

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, con  el
     * buscador especificado, aplicando mecanismos de división del trabajo
     *
     * @param query         consulta de la búsqueda
     * @param searcher      buscador
     * @param fields        campo del documento
     * @param caseSensitive tener en cuenta mayúsculas y minúsculas
     * @param members       número de miembros de la sesión
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
     * <br>
     *
     * @see KeySearchable
     *
     */
    public List<ResultSetMetaData> search(int searcher, String query, int[] fields, boolean caseSensitive, int members) throws SearchException, SearchableException;

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, con  el
     * buscador especificado, aplicando mecanismos de división del trabajo
     *
     * @param query         consulta de la búsqueda
     * @param searcher      buscador
     * @param docType       tipo de documento
     * @param caseSensitive tener en cuenta mayúsculas y minúsculas
     * @param members       número de miembros de la sesión
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
     * <br>
     *
     * @see KeySearchable
     *
     */
    public List<ResultSetMetaData> search(int searcher, String query, String docType, boolean caseSensitive, int members) throws SearchException, SearchableException;

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, con  el
     * buscador especificado, aplicando mecanismos de división del trabajo
     *
     * @param query         consulta de la búsqueda
     * @param searcher      buscador
     * @param docTypes      tipo de documento
     * @param caseSensitive tener en cuenta mayúsculas y minúsculas
     * @param members       número de miembros de la sesión
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
     * <br>
     *
     * @see KeySearchable
     *
     */
    public List<ResultSetMetaData> search(int searcher, String query, String[] docTypes, boolean caseSensitive, int members) throws SearchException, SearchableException;

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, con  el
     * buscador especificado, aplicando mecanismos de división del trabajo
     *
     * @param query         consulta de la búsqueda
     * @param searcher      buscador
     * @param docType       tipo de documento
     * @param field         campo del documento
     * @param caseSensitive tener en cuenta mayúsculas y minúsculas
     * @param members       número de miembros de la sesión
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
     * <br>
     *
     * @see KeySearchable
     *
     */
    public List<ResultSetMetaData> search(int searcher, String query, String docType, int field, boolean caseSensitive, int members) throws SearchException, SearchableException;

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, con  el
     * buscador especificado, aplicando mecanismos de división del trabajo
     *
     * @param query         consulta de la búsqueda
     * @param searcher      buscador
     * @param docType       tipo de documento
     * @param fields        campos del documento
     * @param caseSensitive tener en cuenta mayúsculas y minúsculas
     * @param members       número de miembros de la sesión
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
     * <br>
     *
     * @see KeySearchable
     *
     */
    public List<ResultSetMetaData> search(int searcher, String query, String docType, int[] fields, boolean caseSensitive, int members) throws SearchException, SearchableException;

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, con  el
     * buscador especificado, aplicando mecanismos de división del trabajo
     *
     * @param query         consulta de la búsqueda
     * @param searcher      buscador
     * @param docTypes      tipo de documento
     * @param field         campo del documento
     * @param caseSensitive tener en cuenta mayúsculas y minúsculas
     * @param members       número de miembros de la sesión
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
     * <br>
     *
     * @see KeySearchable
     *
     */
    public List<ResultSetMetaData> search(int searcher, String query, String[] docTypes, int field, boolean caseSensitive, int members) throws SearchException, SearchableException;

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, con  el
     * buscador especificado, aplicando mecanismos de división del trabajo
     *
     * @param query         consulta de la búsqueda
     * @param searcher      buscador
     * @param docTypes      tipo de documento
     * @param fields        campos del documento
     * @param caseSensitive tener en cuenta mayúsculas y minúsculas
     * @param members       número de miembros de la sesión
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
     * <br>
     *
     * @see KeySearchable
     *
     */
    public List<ResultSetMetaData> search(int searcher, String query, String[] docTypes, int[] fields, boolean caseSensitive, int members) throws SearchException, SearchableException;
}
