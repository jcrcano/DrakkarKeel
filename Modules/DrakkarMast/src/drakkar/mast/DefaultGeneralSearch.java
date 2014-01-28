/*
 * DrakkarKeel - An Enterprise Collaborative Search Platform
 *
 * The contents of this file are subject under the terms described in the
 * DRAKKARKEEL_LICENSE file included in this distribution; you may not use this
 * file except in compliance with the License. 
 *
 * 2013-2014 DrakkarKeel Platform.
 */
package drakkar.mast;


import drakkar.oar.ResultSetMetaData;

/**
 * Esta interfaz declara todos los métodos de búsquedas no colaborativas, que
 * pueden ser invocados por los clientes
 *
 * 
 */
public interface DefaultGeneralSearch {

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, con el buscador
     * seleccionado
     *
     * @param query          consulta de la búsqueda
     * @param searcher       buscador
     * @param principle      principio de búsqueda
     * @param caseSensitive  tener en cuenta mayúsculas y minísculas
     *
     * @return lista de documentos encontrados
     *
     * @throws IllegalArgumentException si el principio especificado no es soportado
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
     * Principios de búsquedas soportados para este método:
     * <br>
     * <tt>-SINGLE_SEARCH</tt><br>
     * <tt>-META_SEARCH</tt><br>
     * <tt>-MULTI_SEARCH</tt><br>
     * <br>
     * Estas constantes se encuentran definidas en la clase <code>SearchPrinciple</code>,
     * contenida en el paquete drakkar.oar.util
     * <br>
     * <br>
     * De escogerse el principio de búsqueda <code>SINGLE_SEARCH</code>, el valor
     * del identificador del buscador debe <code>All_SEARCHABLES</code>.
     *
     * @see KeySearchable
     * @see SearchPrinciple
     */
    public ResultSetMetaData search(String query, int searcher, int principle, boolean caseSensitive) throws IllegalArgumentException, SearchableException, SearchException;

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, con el buscador
     * seleccionado
     *
     * @param query          consulta de la búsqueda
     * @param field          campo del documento
     * @param searcher       buscador
     * @param principle      principio de búsqueda
     * @param caseSensitive  tener en cuenta mayúsculas y minísculas
     *
     * @return lista de documentos encontrados
     *
     * @throws IllegalArgumentException si el principio especificado no es soportado
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
     * Principios de búsquedas soportados para este método:
     * <br>
     * <tt>-SINGLE_SEARCH</tt><br>
     * <tt>-META_SEARCH</tt><br>
     * <tt>-MULTI_SEARCH</tt><br>
     * <br>
     * Estas constantes se encuentran definidas en la clase <code>SearchPrinciple</code>,
     * contenida en el paquete drakkar.oar.util
     * <br>
     * <br>
     * De escogerse el principio de búsqueda <code>SINGLE_SEARCH</code>, el valor
     * del identificador del buscador debe <code>All_SEARCHABLES</code>.
     *
     * @see KeySearchable
     * @see SearchPrinciple
     */
    public ResultSetMetaData search(String query, int field, int searcher, int principle, boolean caseSensitive) throws IllegalArgumentException, SearchableException, SearchException;

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, con el buscador
     * seleccionado
     *
     * @param query         consulta de la búsqueda
     * @param fields        campos del documento
     * @param searcher      buscador
     * @param principle     principio de búsqueda
     * @param caseSensitive  tener en cuenta mayúsculas y minísculas
     *
     * @return lista de documentos encontrados
     *
     * @throws IllegalArgumentException si el principio especificado no es soportado
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
     * Principios de búsquedas soportados para este método:
     * <br>
     * <tt>-SINGLE_SEARCH</tt><br>
     * <tt>-META_SEARCH</tt><br>
     * <tt>-MULTI_SEARCH</tt><br>
     * <br>
     * Estas constantes se encuentran definidas en la clase <code>SearchPrinciple</code>,
     * contenida en el paquete drakkar.oar.util
     * <br>
     * <br>
     * De escogerse el principio de búsqueda <code>SINGLE_SEARCH</code>, el valor
     * del identificador del buscador debe <code>All_SEARCHABLES</code>.
     *
     * @see KeySearchable
     * @see SearchPrinciple
     */
    public ResultSetMetaData search(String query, int[] fields, int searcher, int principle, boolean caseSensitive) throws IllegalArgumentException, SearchableException, SearchException;

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, con el buscador
     * seleccionado
     *
     * @param query         consulta de la búsqueda
     * @param docType       tipo de documento
     * @param searcher      buscador
     * @param principle     principio de búsqueda
     * @param caseSensitive tener en cuenta mayúsculas y minísculas
     *
     * @return lista de documentos encontrados
     *
     * @throws IllegalArgumentException si el principio especificado no es soportado
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
     * Principios de búsquedas soportados para este método:
     * <br>
     * <tt>-SINGLE_SEARCH</tt><br>
     * <tt>-META_SEARCH</tt><br>
     * <tt>-MULTI_SEARCH</tt><br>
     * <br>
     * Estas constantes se encuentran definidas en la clase <code>SearchPrinciple</code>,
     * contenida en el paquete drakkar.oar.util
     * <br>
     * <br>
     * De escogerse el principio de búsqueda <code>SINGLE_SEARCH</code>, el valor
     * del identificador del buscador debe <code>All_SEARCHABLES</code>.
     *
     * @see KeySearchable
     * @see SearchPrinciple
     */
    public ResultSetMetaData search(String query, String docType, int searcher, int principle, boolean caseSensitive) throws IllegalArgumentException, SearchableException, SearchException;

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, con el buscador
     * seleccionado
     *
     * @param query         consulta de la búsqueda
     * @param docTypes      tipo de documento
     * @param searcher      buscador
     * @param principle     principio de búsqueda
     * @param caseSensitive tener en cuenta mayúsculas y minísculas
     *
     * @return lista de documentos encontrados
     *
     * @throws IllegalArgumentException si el principio especificado no es soportado
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
     * Principios de búsquedas soportados para este método:
     * <br>
     * <tt>-SINGLE_SEARCH</tt><br>
     * <tt>-META_SEARCH</tt><br>
     * <tt>-MULTI_SEARCH</tt><br>
     * <br>
     * Estas constantes se encuentran definidas en la clase <code>SearchPrinciple</code>,
     * contenida en el paquete drakkar.oar.util
     * <br>
     * <br>
     * De escogerse el principio de búsqueda <code>SINGLE_SEARCH</code>, el valor
     * del identificador del buscador debe <code>All_SEARCHABLES</code>.
     *
     * @see KeySearchable
     * @see SearchPrinciple
     */
    public ResultSetMetaData search(String query, String[] docTypes, int searcher, int principle, boolean caseSensitive) throws IllegalArgumentException, SearchableException, SearchException;

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, con el buscador
     * seleccionado
     *
     * @param query         consulta de la búsqueda
     * @param docType       tipo de documento
     * @param field         campo del MetaDocumento
     * @param searcher      buscador
     * @param principle     principio de búsqueda
     * @param caseSensitive tener en cuenta mayúsculas y minísculas
     * 
     * @return lista de documentos encontrados
     *
     * @throws IllegalArgumentException si el principio especificado no es soportado
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
     * Principios de búsquedas soportados para este método:
     * <br>
     * <tt>-SINGLE_SEARCH</tt><br>
     * <tt>-META_SEARCH</tt><br>
     * <tt>-MULTI_SEARCH</tt><br>
     * <br>
     * Estas constantes se encuentran definidas en la clase <code>SearchPrinciple</code>,
     * contenida en el paquete drakkar.oar.util
     * <br>
     * <br>
     * De escogerse el principio de búsqueda <code>SINGLE_SEARCH</code>, el valor
     * del identificador del buscador debe <code>All_SEARCHABLES</code>.
     *
     * @see KeySearchable
     * @see SearchPrinciple
     */
    public ResultSetMetaData search(String query, String docType, int field, int searcher, int principle, boolean caseSensitive) throws IllegalArgumentException, SearchableException, SearchException;

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, con el buscador
     * seleccionado
     *
     * @param query         consulta de la búsqueda
     * @param docType       tipo de documento
     * @param fields        campos del documento
     * @param searcher      buscador
     * @param principle     principio de búsqueda 
     * @param caseSensitive tener en cuenta mayúsculas y minísculas
     *
     * @return lista de documentos encontrados
     *
     * @throws IllegalArgumentException si el principio especificado no es soportado
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
     * Principios de búsquedas soportados para este método:
     * <br>
     * <tt>-SINGLE_SEARCH</tt><br>
     * <tt>-META_SEARCH</tt><br>
     * <tt>-MULTI_SEARCH</tt><br>
     * <br>
     * Estas constantes se encuentran definidas en la clase <code>SearchPrinciple</code>,
     * contenida en el paquete drakkar.oar.util
     * <br>
     * <br>
     * De escogerse el principio de búsqueda <code>SINGLE_SEARCH</code>, el valor
     * del identificador del buscador debe <code>All_SEARCHABLES</code>.
     *
     * @see KeySearchable
     * @see SearchPrinciple
     */
    public ResultSetMetaData search(String query, String docType, int[] fields, int searcher, int principle, boolean caseSensitive) throws IllegalArgumentException, SearchableException, SearchException;

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, con el buscador
     * seleccionado
     *
     * @param query         consulta de la búsqueda
     * @param docTypes      tipos de documento
     * @param field         campo del documento
     * @param searcher      buscador
     * @param principle     principio de búsqueda
     *
     * @param caseSensitive  tener en cuenta mayúsculas y minísculas
     * @return lista de documentos encontrados
     *
     * @throws IllegalArgumentException si el principio especificado no es soportado
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
     * Principios de búsquedas soportados para este método:
     * <br>
     * <tt>-SINGLE_SEARCH</tt><br>
     * <tt>-META_SEARCH</tt><br>
     * <tt>-MULTI_SEARCH</tt><br>
     * <br>
     * Estas constantes se encuentran definidas en la clase <code>SearchPrinciple</code>,
     * contenida en el paquete drakkar.oar.util
     * <br>
     * <br>
     * De escogerse el principio de búsqueda <code>SINGLE_SEARCH</code>, el valor
     * del identificador del buscador debe <code>All_SEARCHABLES</code>.
     *
     * @see KeySearchable
     * @see SearchPrinciple
     */
    public ResultSetMetaData search(String query, String[] docTypes, int field, int searcher, int principle, boolean caseSensitive) throws IllegalArgumentException, SearchableException, SearchException;

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, con el buscador
     * seleccionado
     *
     * @param query         consulta de la búsqueda
     * @param docTypes      tipos de documento
     * @param fields        campos del documento
     * @param searcher      buscador
     * @param principle     principio de búsqueda
     * @param caseSensitive tener en cuenta mayúsculas y minísculas
     *
     * @return lista de documentos encontrados
     *
     * @throws IllegalArgumentException si el principio especificado no es soportado
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
     * Principios de búsquedas soportados para este método:
     * <br>
     * <tt>-SINGLE_SEARCH</tt><br>
     * <tt>-META_SEARCH</tt><br>
     * <tt>-MULTI_SEARCH</tt><br>
     * <br>
     * Estas constantes se encuentran definidas en la clase <code>SearchPrinciple</code>,
     * contenida en el paquete drakkar.oar.util
     * <br>
     * <br>
     * De escogerse el principio de búsqueda <code>SINGLE_SEARCH</code>, el valor
     * del identificador del buscador debe <code>All_SEARCHABLES</code>.
     *
     * @see KeySearchable
     * @see SearchPrinciple
     */
    public ResultSetMetaData search(String query, String[] docTypes, int[] fields, int searcher, int principle, boolean caseSensitive) throws IllegalArgumentException, SearchableException, SearchException;

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, con los buscadores
     * seleccionados
     *
     * @param searchers     buscadores
     * @param query         consulta de la búsqueda
     * @param principle     principio de búsqueda 
     * @param caseSensitive tener en cuenta mayúsculas y minísculas
     *
     * @return lista de documentos encontrados
     *
     * @throws IllegalArgumentException si el principio especificado no es soportado
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
     * Principios de búsquedas soportados para este método:
     * <br>
     * <tt>-META_SEARCH</tt><br>
     * <tt>-MULTI_SEARCH</tt><br>
     * <br>
     * Estas constantes se encuentran definidas en la clase <code>SearchPrinciple</code>,
     * contenida en el paquete drakkar.oar.util
     *
     * @see KeySearchable
     * @see SearchPrinciple
     *
     */
    public ResultSetMetaData search(int[] searchers, String query, int principle, boolean caseSensitive) throws IllegalArgumentException, SearchableException, SearchException;

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, con los buscadores
     * seleccionados
     *
     * @param searchers     buscadores
     * @param query         consulta de la búsqueda
     * @param field         campo del documento
     * @param principle     principio de búsqueda 
     * @param caseSensitive tener en cuenta mayúsculas y minísculas
     *
     * @return lista de documentos encontrados
     *
     * @throws IllegalArgumentException si el principio especificado no es soportado
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
     * Principios de búsquedas soportados para este método:
     * <br>
     * <tt>-META_SEARCH</tt><br>
     * <tt>-MULTI_SEARCH</tt><br>
     * <br>
     * Estas constantes se encuentran definidas en la clase <code>SearchPrinciple</code>,
     * contenida en el paquete drakkar.oar.util
     *
     * @see KeySearchable
     * @see SearchPrinciple
     */
    public ResultSetMetaData search(int[] searchers, String query, int field, int principle, boolean caseSensitive) throws IllegalArgumentException, SearchableException, SearchException;

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, con los buscadores
     * seleccionados
     *
     * @param searchers     buscadores
     * @param query         consulta de la búsqueda
     * @param fields        campos del documento
     * @param principle     principio de búsqueda 
     * @param caseSensitive tener en cuenta mayúsculas y minísculas
     *
     * @return lista de documentos encontrados
     *
     * @throws IllegalArgumentException si el principio especificado no es soportado
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
     * Principios de búsquedas soportados para este método:
     * <br>
     * <tt>-META_SEARCH</tt><br>
     * <tt>-MULTI_SEARCH</tt><br>
     * <br>
     * Estas constantes se encuentran definidas en la clase <code>SearchPrinciple</code>,
     * contenida en el paquete drakkar.oar.util
     *
     * @see KeySearchable
     * @see SearchPrinciple
     */
    public ResultSetMetaData search(int[] searchers, String query, int[] fields, int principle, boolean caseSensitive) throws IllegalArgumentException, SearchableException, SearchException;

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, con los buscadores
     * seleccionados
     *
     * @param searchers     buscadores
     * @param query         consulta de la búsqueda
     * @param docType       tipo de documento
     * @param principle     principio de búsqueda
     * @param caseSensitive tener en cuenta mayúsculas y minísculas
     *
     * @return lista de documentos encontrados
     *
     * @throws IllegalArgumentException si el principio especificado no es soportado
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
     * Principios de búsquedas soportados para este método:
     * <br>
     * <tt>-META_SEARCH</tt><br>
     * <tt>-MULTI_SEARCH</tt><br>
     * <br>
     * Estas constantes se encuentran definidas en la clase <code>SearchPrinciple</code>,
     * contenida en el paquete drakkar.oar.util
     *
     * @see KeySearchable
     * @see SearchPrinciple
     */
    public ResultSetMetaData search(int[] searchers, String query, String docType, int principle, boolean caseSensitive) throws IllegalArgumentException, SearchableException, SearchException;

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, con los buscadores
     * seleccionados
     *
     * @param searchers     buscadores
     * @param query         consulta de la búsqueda
     * @param docTypes      tipo de documento
     * @param principle     principio de búsqueda 
     * @param caseSensitive tener en cuenta mayúsculas y minísculas
     *
     * @return lista de documentos encontrados
     *
     * @throws IllegalArgumentException si el principio especificado no es soportado
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
     * Principios de búsquedas soportados para este método:
     * <br>
     * <tt>-META_SEARCH</tt><br>
     * <tt>-MULTI_SEARCH</tt><br>
     * <br>
     * Estas constantes se encuentran definidas en la clase <code>SearchPrinciple</code>,
     * contenida en el paquete drakkar.oar.util
     *
     * @see KeySearchable
     * @see SearchPrinciple
     */
    public ResultSetMetaData search(int[] searchers, String query, String[] docTypes, int principle, boolean caseSensitive) throws IllegalArgumentException, SearchableException, SearchException;

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, con los buscadores
     * seleccionados
     *
     * @param searchers     buscadores
     * @param query         consulta de la búsqueda
     * @param docType       tipo de documento
     * @param field         campo del MetaDocumento
     * @param principle     principio de búsqueda
     * @param caseSensitive tener en cuenta mayúsculas y minísculas
     *
     * @return lista de documentos encontrados
     *
     * @throws IllegalArgumentException si el principio especificado no es soportado
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
     * Principios de búsquedas soportados para este método:
     * <br>
     * <tt>-META_SEARCH</tt><br>
     * <tt>-MULTI_SEARCH</tt><br>
     * <br>
     * Estas constantes se encuentran definidas en la clase <code>SearchPrinciple</code>,
     * contenida en el paquete drakkar.oar.util
     *
     * @see KeySearchable
     * @see SearchPrinciple
     */
    public ResultSetMetaData search(int[] searchers, String query, String docType, int field, int principle, boolean caseSensitive) throws IllegalArgumentException, SearchableException, SearchException;

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, con los buscadores
     * seleccionados
     *
     * @param searchers     buscadores
     * @param query         consulta de la búsqueda
     * @param docTypes      tipo de documento
     * @param field         campo del MetaDocumento
     * @param principle     principio de búsqueda 
     * @param caseSensitive tener en cuenta mayúsculas y minísculas
     *
     * @return lista de documentos encontrados
     *
     * @throws IllegalArgumentException si el principio especificado no es soportado
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
     * Principios de búsquedas soportados para este método:
     * <br>
     * <tt>-META_SEARCH</tt><br>
     * <tt>-MULTI_SEARCH</tt><br>
     * <br>
     * Estas constantes se encuentran definidas en la clase <code>SearchPrinciple</code>,
     * contenida en el paquete drakkar.oar.util
     *
     * @see KeySearchable
     * @see SearchPrinciple
     */
    public ResultSetMetaData search(int[] searchers, String query, String[] docTypes, int field, int principle, boolean caseSensitive) throws IllegalArgumentException, SearchableException, SearchException;

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, con los buscadores
     * seleccionados
     *
     * @param searchers     buscadores
     * @param query         consulta de la búsqueda
     * @param docType       tipo de documento
     * @param fields        campos del documento
     * @param principle     principio de búsqueda
     * @param caseSensitive  tener en cuenta mayúsculas y minísculas
     * 
     * @return lista de documentos encontrados
     *
     * @throws IllegalArgumentException si el principio especificado no es soportado
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
     * Principios de búsquedas soportados para este método:
     * <br>
     * <tt>-META_SEARCH</tt><br>
     * <tt>-MULTI_SEARCH</tt><br>
     * <br>
     * Estas constantes se encuentran definidas en la clase <code>SearchPrinciple</code>,
     * contenida en el paquete drakkar.oar.util
     *
     * @see KeySearchable
     * @see SearchPrinciple
     */
    public ResultSetMetaData search(int[] searchers, String query, String docType, int[] fields, int principle, boolean caseSensitive) throws IllegalArgumentException, SearchableException, SearchException;

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, con los buscadores
     * seleccionados
     *
     * @param searchers     buscadores
     * @param query         consulta de la búsqueda
     * @param docTypes      tipo de documento
     * @param fields        campos del documento
     * @param principle     principio de búsqueda     
     * @param caseSensitive tener en cuenta mayúsculas y minísculas
     * 
     * @return lista de documentos encontrados
     *
     * @throws IllegalArgumentException si el principio especificado no es soportado
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
     * Principios de búsquedas soportados para este método:
     * <br>
     * <tt>-META_SEARCH</tt><br>
     * <tt>-MULTI_SEARCH</tt><br>
     * <br>
     * Estas constantes se encuentran definidas en la clase <code>SearchPrinciple</code>,
     * contenida en el paquete drakkar.oar.util
     *
     * @see KeySearchable
     * @see SearchPrinciple
     */
    public ResultSetMetaData search(int[] searchers, String query, String[] docTypes, int[] fields, int principle, boolean caseSensitive) throws IllegalArgumentException, IllegalArgumentException, SearchableException, SearchException;
}
