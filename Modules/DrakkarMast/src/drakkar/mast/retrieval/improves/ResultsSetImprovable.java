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
 * soportadas por el framework DrakkarKeel, para los principios de búsqueda MetaSearch, MultiSearch,
 * MetaSearch and Split, MultiSearch and Switch
 *
 * 
 */
public interface ResultsSetImprovable {

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, con  todos los
     * buscadores activos en servidor
     *
     * @param query         consulta de la búsqueda
     * @param caseSensitive   tener en cuenta mayúsculas y minúsculas 
     *
     * @return lista de documentos encontrados
     *
     * @throws SearchException  si ocurre alguna excepción durante el proceso de búsqueda
     *
     */
    public ResultSetMetaData search(String query, boolean caseSensitive) throws SearchException;

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, con  todos los
     * buscadores activos en servidor
     *
     * @param query         consulta de la búsqueda
     * @param field
     * @param caseSensitive   tener en cuenta mayúsculas y minúsculas
     * @return lista de documentos encontrados
     *
     * @throws SearchException  si ocurre alguna excepción durante el proceso de búsqueda
     */
    public ResultSetMetaData search(String query, int field, boolean caseSensitive) throws SearchException;

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, con  todos los
     * buscadores activos en servidor
     *
     * @param query         consulta de la búsqueda
     * @param fields        campos del documento
     * @param caseSensitive tener en cuenta mayúsculas y minúsculas
     * 
     * @return lista de documentos encontrados
     *
     * @throws SearchException  si ocurre alguna excepción durante el proceso de búsqueda
     */
    public ResultSetMetaData search(String query, int[] fields, boolean caseSensitive) throws SearchException;

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, con  todos los
     * buscadores activos en servidor
     *
     * @param query         consulta de la búsqueda
     * @param docType       tipo del documento
     * @param caseSensitive   tener en cuenta mayúsculas y minúsculas
     * 
     * @return lista de documentos encontrados
     *
     * @throws SearchException  si ocurre alguna excepción durante el proceso de búsqueda
     */
    public ResultSetMetaData search(String query, String docType, boolean caseSensitive) throws SearchException;

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, con  todos los
     * buscadores activos en servidor
     *
     * @param query         consulta de la búsqueda
     * @param docTypes      tipo del documento
     * @param caseSensitive tener en cuenta mayúsculas y minúsculas
     * 
     * @return lista de documentos encontrados
     *
     * @throws SearchException  si ocurre alguna excepción durante el proceso de búsqueda
     */
    public ResultSetMetaData search(String query, String[] docTypes, boolean caseSensitive) throws SearchException;

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, con  todos los
     * buscadores activos en servidor
     *
     * @param query         consulta de la búsqueda
     * @param docType       tipo del documento
     * @param field         campo del documento
     * @param caseSensitive tener en cuenta mayúsculas y minúsculas
     *
     * @return lista de documentos encontrados
     *
     * @throws SearchException  si ocurre alguna excepción durante el proceso de búsqueda
     */
    public ResultSetMetaData search(String query, String docType, int field, boolean caseSensitive) throws SearchException;

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, con  todos los
     * buscadores activos en servidor
     *
     * @param query         consulta de la búsqueda
     * @param docType       tipo del documento
     * @param fields        campos del documento
     * @param caseSensitive tener en cuenta mayúsculas y minúsculas
     * 
     * @return lista de documentos encontrados
     *
     * @throws SearchException  si ocurre alguna excepción durante el proceso de búsqueda
     */
    public ResultSetMetaData search(String query, String docType, int[] fields, boolean caseSensitive) throws SearchException;

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, con  todos los
     * buscadores activos en servidor
     *
     * @param query         consulta de la búsqueda
     * @param docTypes      tipo del documento
     * @param field         campos del documento
     * @param caseSensitive tener en cuenta mayúsculas y minúsculas
     * 
     * @return lista de documentos encontrados
     *
     * @throws SearchException  si ocurre alguna excepción durante el proceso de búsqueda
     */
    public ResultSetMetaData search(String query, String[] docTypes, int field, boolean caseSensitive) throws SearchException;

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, con  todos los
     * buscadores activos en servidor
     *
     * @param query         consulta de la búsqueda
     * @param docTypes      tipo del documento
     * @param fields        campos del documento
     * @param caseSensitive tener en cuenta mayúsculas y minúsculas
     *
     * @return lista de documentos encontrados
     *
     * @throws SearchException  si ocurre alguna excepción durante el proceso de búsqueda
     */
    public ResultSetMetaData search(String query, String[] docTypes, int[] fields, boolean caseSensitive) throws SearchException;

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, con  los
     * buscadores seleccionados
     *
     * @param searchers     buscadores
     * @param query         consulta de la búqueda
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
     *
     * @see KeySearchable
     */
    public ResultSetMetaData search(int[] searchers, String query, boolean caseSensitive) throws SearchableException, SearchException;

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, con  los
     * buscadores seleccionados
     *
     * @param searchers     buscadores
     * @param query         consulta de la búqueda
     * @param field         campo del documento
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
     *
     * @see KeySearchable
     */
    public ResultSetMetaData search(int[] searchers, String query, int field, boolean caseSensitive) throws SearchableException, SearchException;

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, con  los
     * buscadores seleccionados
     *
     * @param searchers     buscadores
     * @param query         consulta de la búqueda
     * @param fields        campos del documento
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
     *
     * @see KeySearchable
     */
    public ResultSetMetaData search(int[] searchers, String query, int[] fields, boolean caseSensitive) throws SearchableException, SearchException;

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, con  los
     * buscadores seleccionados
     *
     * @param searchers     buscadores
     * @param query         consulta de la búsqueda
     * @param docType       tipo del documento
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
     *
     * @see KeySearchable
     */
    public ResultSetMetaData search(int[] searchers, String query, String docType, boolean caseSensitive) throws SearchableException, SearchException;

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, con  los
     * buscadores seleccionados
     *
     * @param searchers     buscadores
     * @param query         consulta de la búsqueda
     * @param docTypes      tipo del documento
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
     *
     * @see KeySearchable
     */
    public ResultSetMetaData search(int[] searchers, String query, String[] docTypes, boolean caseSensitive) throws SearchableException, SearchException;

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, con  los
     * buscadores seleccionados
     *
     * @param searchers     buscadores
     * @param query         consulta de la búsqueda
     * @param docType       tipo del documento
     * @param field         campo del documento
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
     *
     * @see KeySearchable
     */
    public ResultSetMetaData search(int[] searchers, String query, String docType, int field, boolean caseSensitive) throws SearchableException, SearchException;

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, con  los
     * buscadores seleccionados
     *
     * @param searchers     buscadores
     * @param query         consulta de la búsqueda
     * @param docType       tipo del documento
     * @param fields        campos del documento
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
     *
     * @see KeySearchable
     */
    public ResultSetMetaData search(int[] searchers, String query, String docType, int[] fields, boolean caseSensitive) throws SearchableException, SearchException;

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, con  los
     * buscadores seleccionados
     *
     * @param searchers     buscadores
     * @param query         consulta de la búsqueda
     * @param docTypes      tipo del documento
     * @param field         campos del documento
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
     *
     * @see KeySearchable
     */
    public ResultSetMetaData search(int[] searchers, String query, String[] docTypes, int field, boolean caseSensitive) throws SearchableException, SearchException;

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, con  los
     * buscadores seleccionados
     *
     * @param searchers     buscadores
     * @param query         consulta de la búsqueda
     * @param docTypes      tipo del documento
     * @param fields        campos del documento
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
     *
     * @see KeySearchable
     */
    public ResultSetMetaData search(int[] searchers, String query, String[] docTypes, int[] fields, boolean caseSensitive) throws SearchableException, SearchException;

    ///////////////////////////////////////////////////
    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, con todos los
     * buscadores activos en el servidor, aplicando mecanismos de división del trabajo
     *
     * @param query         consulta de la búsqueda
     * @param caseSensitive tener en cuenta mayúsculas y minúsculas
     * @param members       número de miembros de la sesión
     *
     * @return lista de documentos encontrados
     *
     * @throws SearchException  si ocurre alguna excepción durante el proceso de búsqueda
     */
    public List<ResultSetMetaData> search(String query, boolean caseSensitive, int members) throws SearchException;

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, con todos los
     * buscadores activos en el servidor, aplicando mecanismos de división del trabajo
     *
     * @param query         consulta de la búsqueda
     * @param field         campo del documento
     * @param caseSensitive tener en cuenta mayúsculas y minúsculas
     * @param members       número de miembros de la sesión
     *
     * @return lista de documentos encontrados
     *
     * @throws SearchException  si ocurre alguna excepción durante el proceso de búsqueda
     */
    public List<ResultSetMetaData> search(String query, int field, boolean caseSensitive, int members) throws SearchException;

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, con todos los
     * buscadores activos en el servidor, aplicando mecanismos de división del trabajo
     *
     * @param query         consulta de la búsqueda
     * @param fields        campos del documento
     * @param caseSensitive tener en cuenta mayúsculas y minúsculas
     * @param members       número de miembros de la sesión
     *
     * @return lista de documentos encontrados
     *
     * @throws SearchException  si ocurre alguna excepción durante el proceso de búsqueda
     */
    public List<ResultSetMetaData> search(String query, int[] fields, boolean caseSensitive, int members) throws SearchException;

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, con todos los
     * buscadores activos en el servidor, aplicando mecanismos de división del trabajo
     *
     * @param query         consulta de la búsqueda
     * @param docType       tipo del documento
     * @param caseSensitive tener en cuenta mayúsculas y minúsculas
     * @param members       número de miembros de la sesión
     *
     * @return lista de documentos encontrados
     *
     * @throws SearchException  si ocurre alguna excepción durante el proceso de búsqueda
     */
    public List<ResultSetMetaData> search(String query, String docType, boolean caseSensitive, int members) throws SearchException;

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, con todos los
     * buscadores activos en el servidor, aplicando mecanismos de división del trabajo
     *
     * @param query         consulta de la búsqueda
     * @param docTypes      tipo del documento
     * @param caseSensitive tener en cuenta mayúsculas y minúsculas
     * @param members       número de miembros de la sesión
     *
     * @return lista de documentos encontrados
     *
     * @throws SearchException  si ocurre alguna excepción durante el proceso de búsqueda
     */
    public List<ResultSetMetaData> search(String query, String[] docTypes, boolean caseSensitive, int members) throws SearchException;

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, con todos los
     * buscadores activos en el servidor, aplicando mecanismos de división del trabajo
     *
     * @param query         consulta de la búsqueda
     * @param docType       tipo del documento
     * @param field         campo del documento
     * @param caseSensitive tener en cuenta mayúsculas y minúsculas
     * @param members       número de miembros de la sesión
     *
     * @return lista de documentos encontrados
     *
     * @throws SearchException  si ocurre alguna excepción durante el proceso de búsqueda
     */
    public List<ResultSetMetaData> search(String query, String docType, int field, boolean caseSensitive, int members) throws SearchException;

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, con todos los
     * buscadores activos en el servidor, aplicando mecanismos de división del trabajo
     *
     * @param query         consulta de la búsqueda
     * @param docType       tipo del documento
     * @param fields        campos del documento
     * @param caseSensitive tener en cuenta mayúsculas y minúsculas
     * @param members       número de miembros de la sesión
     *
     * @return lista de documentos encontrados
     *
     * @throws SearchException  si ocurre alguna excepción durante el proceso de búsqueda
     */
    public List<ResultSetMetaData> search(String query, String docType, int[] fields, boolean caseSensitive, int members) throws SearchException;

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, con todos los
     * buscadores activos en el servidor, aplicando mecanismos de división del trabajo
     *
     * @param query         consulta de la búsqueda
     * @param docTypes      tipo del documento
     * @param field         campo del documento
     * @param caseSensitive tener en cuenta mayúsculas y minúsculas
     * @param members       número de miembros de la sesión
     *
     * @return lista de documentos encontrados
     *
     * @throws SearchException  si ocurre alguna excepción durante el proceso de búsqueda
     */
    public List<ResultSetMetaData> search(String query, String[] docTypes, int field, boolean caseSensitive, int members) throws SearchException;

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, con todos los
     * buscadores activos en el servidor, aplicando mecanismos de división del trabajo
     *
     * @param query         consulta de la búsqueda
     * @param docTypes      tipo del documento
     * @param fields        campos del documento
     * @param caseSensitive tener en cuenta mayúsculas y minúsculas
     * @param members       número de miembros de la sesión
     *
     * @return lista de documentos encontrados
     *
     * @throws SearchException  si ocurre alguna excepción durante el proceso de búsqueda
     */
    public List<ResultSetMetaData> search(String query, String[] docTypes, int[] fields, boolean caseSensitive, int members) throws SearchException;

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, con los buscadores
     * seleccionados, aplicando mecanismos de división del trabajo
     *
     * @param searchers     buscadores
     * @param query         consulta de la búsqueda
     * @param caseSensitive tener en cuenta mayúsculas y minúsculas
     * @param members       número de miembros de la sesión
     *
     * @return lista de documentos encontrados
     *
     * @throws IllegalArgumentException  si el principio de división del trabajo no es
     *                                   soportado
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
    public List<ResultSetMetaData> search(int[] searchers, String query, boolean caseSensitive, int members) throws SearchException, SearchableException;

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, con los buscadores
     * seleccionados, aplicando mecanismos de división del trabajo
     *
     * @param searchers     buscadores
     * @param query         consulta de la búsqueda
     * @param field         campo del documento
     * @param caseSensitive tener en cuenta mayúsculas y minúsculas
     * @param members       número de miembros de la sesión
     *
     * @return lista de documentos encontrados
     *
     * @throws SearchException  si ocurre alguna excepción durante el proceso de búsqueda
     * @throws SearchableException  si el buscador no es soportado por el servidor
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
    public List<ResultSetMetaData> search(int[] searchers, String query, int field, boolean caseSensitive, int members) throws SearchException, SearchableException;

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, con los buscadores
     * seleccionados, aplicando mecanismos de división del trabajo
     *
     * @param searchers     buscadores
     * @param query         consulta de la búsqueda
     * @param fields        campos del documento
     * @param caseSensitive tener en cuenta mayúsculas y minúsculas
     * @param members       número de miembros de la sesión
     *
     * @return lista de documentos encontrados
     *
     * @throws SearchException  si ocurre alguna excepción durante el proceso de búsqueda
     * @throws SearchableException  si el buscador no es soportado por el servidor
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
    public List<ResultSetMetaData> search(int[] searchers, String query, int[] fields, boolean caseSensitive, int members) throws SearchException, SearchableException;

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, con los buscadores
     * seleccionados, aplicando mecanismos de división del trabajo
     *
     * @param searchers      buscadores
     * @param query          consulta de la búsqueda
     * @param docType        tipo del documento
     * @param caseSensitive  tener en cuenta mayúsculas y minúsculas
     * @param members        número de miembros de la sesión
     *
     * @return lista de documentos encontrados
     *
     * @throws SearchException  si ocurre alguna excepción durante el proceso de búsqueda
     * @throws SearchableException  si el buscador no es soportado por el servidor
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
    public List<ResultSetMetaData> search(int[] searchers, String query, String docType, boolean caseSensitive, int members) throws SearchException, SearchableException;

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, con los buscadores
     * seleccionados, aplicando mecanismos de división del trabajo
     *
     * @param searchers      buscadores
     * @param query          consulta de la búsqueda
     * @param docTypes       tipo del documento
     * @param caseSensitive  tener en cuenta mayúsculas y minúsculas
     * @param members        número de miembros de la sesión
     *
     * @return lista de documentos encontrados
     *
     * @throws SearchException  si ocurre alguna excepción durante el proceso de búsqueda
     * @throws SearchableException  si el buscador no es soportado por el servidor
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
    public List<ResultSetMetaData> search(int[] searchers, String query, String[] docTypes, boolean caseSensitive, int members) throws SearchException, SearchableException;

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, con los buscadores
     * seleccionados, aplicando mecanismos de división del trabajo
     *
     * @param searchers     buscadores
     * @param query         consulta de la búsqueda
     * @param docType       tipo del documento
     * @param field         campo del documento
     * @param caseSensitive tener en cuenta mayúsculas y minúsculas
     * @param members       número de miembros de la sesión
     *
     * @return lista de documentos encontrados
     *
     * @throws SearchException  si ocurre alguna excepción durante el proceso de búsqueda
     * @throws SearchableException  si el buscador no es soportado por el servidor
     *
     * <br>
     * <br>
     * <b>Nota:</b>
     * <br>
     * Las constantes que representan los diferentes buscadores soportados, se encuentran
     * definidas en la clase <code>KeySearchable</code>, del paquete drakkar.oar.util
     * <br>
     *
     *
     * @see KeySearchable
     */
    public List<ResultSetMetaData> search(int[] searchers, String query, String docType, int field, boolean caseSensitive, int members) throws SearchException, SearchableException;

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, con los buscadores
     * seleccionados, aplicando mecanismos de división del trabajo
     *
     * @param searchers     buscadores
     * @param query         consulta de la búsqueda
     * @param docType       tipo del documento
     * @param fields        campo ó campos del documento
     * @param caseSensitive tener en cuenta mayúsculas y minúsculas
     * @param members       número de miembros de la sesión
     *
     * @return lista de documentos encontrados
     *
     * @throws SearchException  si ocurre alguna excepción durante el proceso de búsqueda
     * @throws SearchableException  si el buscador no es soportado por el servidor
     *
     * <br>
     * <br>
     * <b>Nota:</b>
     * <br>
     * Las constantes que representan los diferentes buscadores soportados, se encuentran
     * definidas en la clase <code>KeySearchable</code>, del paquete drakkar.oar.util
     * <br>
     *
     *
     * @see KeySearchable
     */
    public List<ResultSetMetaData> search(int[] searchers, String query, String docType, int[] fields, boolean caseSensitive, int members) throws SearchException, SearchableException;

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, con los buscadores
     * seleccionados, aplicando mecanismos de división del trabajo
     *
     * @param searchers     buscadores
     * @param query         consulta de la búsqueda
     * @param docTypes      tipo del documento
     * @param field         campo ó campos del documento
     * @param caseSensitive tener en cuenta mayúsculas y minúsculas
     * @param members       número de miembros de la sesión
     *
     * @return lista de documentos encontrados
     *
     * @throws SearchException  si ocurre alguna excepción durante el proceso de búsqueda
     * @throws SearchableException  si el buscador no es soportado por el servidor
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
    public List<ResultSetMetaData> search(int[] searchers, String query, String[] docTypes, int field, boolean caseSensitive, int members) throws SearchException, SearchableException;

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, con los buscadores
     * seleccionados, aplicando mecanismos de división del trabajo
     *
     * @param searchers     buscadores
     * @param query         consulta de la búsqueda
     * @param docTypes      tipo del documento
     * @param fields        campo ó campos del documento
     * @param caseSensitive tener en cuenta mayúsculas y minúsculas
     * @param members       número de miembros de la sesión
     *
     * @return lista de documentos encontrados
     *
     * @throws SearchException  si ocurre alguna excepción durante el proceso de búsqueda
     * @throws SearchableException  si el buscador no es soportado por el servidor
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
    public List<ResultSetMetaData> search(int[] searchers, String query, String[] docTypes, int[] fields, boolean caseSensitive, int members) throws SearchException, SearchableException;
}
