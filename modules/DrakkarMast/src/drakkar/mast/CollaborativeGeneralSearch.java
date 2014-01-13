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
import java.util.List;

/**
 * Esta interfaz declara todos los métodos de búsqueda colaborativa aplicando
 * mecanismos de división del trabajo que pueden ser invocados por los clientes
 *
 * 
 */
public interface CollaborativeGeneralSearch {

    /**
     * Invoca una búsqueda a partir de los parámetros de entradad, aplicando
     * mecanismos de división del trabajo
     *
     * @param searcher      buscador
     * @param principle     principio de búsqueda
     * @param query         consulta de la búsqueda
     * @param caseSensitive tener en cuenta mayúsculas y minísculas
     * @param members       número de miembros de la sesión
     *
     * @return lista de documentos encontrados
     *
     * @throws IllegalArgumentException si el principio especificado no es soportado
    
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
     * Principios de búsquedas soportados para este método:
     * <br>
     * <tt>-SINGLE_SEARCH_AND_SPLIT</tt><br>
     * <tt>-META_SEARCH_AND_SPLIT</tt><br>
     * <tt>-MULTI_SEARCH_AND_SWITCH</tt><br>
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
    public List<ResultSetMetaData> search(int searcher, int principle, String query, boolean caseSensitive, int members) throws IllegalArgumentException, SearchException, SearchableException;

    /**
     * Invoca una búsqueda a partir de los parámetros de entradad, aplicando
     * mecanismos de división del trabajo
     *
     * @param searcher      buscador
     * @param principle     principio de búsqueda
     * @param query         consulta de la búsqueda
     * @param field         campo del documento
     * @param caseSensitive tener en cuenta mayúsculas y minísculas
     * @param members       número de miembros de la sesión
     *
     * @return lista de documentos encontrados
     *
     * @throws IllegalArgumentException si el principio especificado no es soportado
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
    * Principios de búsquedas soportados para este método:
     * <br>
     * <tt>-SINGLE_SEARCH_AND_SPLIT</tt><br>
     * <tt>-META_SEARCH_AND_SPLIT</tt><br>
     * <tt>-MULTI_SEARCH_AND_SWITCH</tt><br>
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
    public List<ResultSetMetaData> search(int searcher, int principle, String query, int field, boolean caseSensitive, int members) throws IllegalArgumentException, SearchException, SearchableException;

    /**
     * Invoca una búsqueda a partir de los parámetros de entradad, aplicando
     * mecanismos de división del trabajo
     *
     * @param searcher      buscador
     * @param principle     principio de búsqueda
     * @param query         consulta de la búsqueda
     * @param fields        campos del documento
     * @param caseSensitive tener en cuenta mayúsculas y minísculas
     * @param members       número de miembros de la sesión
     *
     * @return lista de documentos encontrados
     *
     * @throws IllegalArgumentException si el principio especificado no es soportado
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
    public List<ResultSetMetaData> search(int searcher, int principle, String query, int[] fields, boolean caseSensitive, int members) throws IllegalArgumentException, SearchException, SearchableException;

    /**
    * Invoca una búsqueda a partir de los parámetros de entradad, aplicando
     * mecanismos de división del trabajo
     *
     * @param searcher      buscador
     * @param principle     principio de búsqueda
     * @param query         consulta de la búsqueda
     * @param docType       tipo de documento
     * @param caseSensitive tener en cuenta mayúsculas y minísculas
     * @param members       número de miembros de la sesión
     *
     * @return lista de documentos encontrados
     *
     * @throws IllegalArgumentException si el principio especificado no es soportado
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
    * Principios de búsquedas soportados para este método:
     * <br>
     * <tt>-SINGLE_SEARCH_AND_SPLIT</tt><br>
     * <tt>-META_SEARCH_AND_SPLIT</tt><br>
     * <tt>-MULTI_SEARCH_AND_SWITCH</tt><br>
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
    public List<ResultSetMetaData> search(int searcher, int principle, String query, String docType, boolean caseSensitive, int members) throws IllegalArgumentException, SearchException, SearchableException;

    /**
     * Invoca una búsqueda a partir de los parámetros de entradad, aplicando
     * mecanismos de división del trabajo
     *
     * @param searcher      buscador
     * @param principle     principio de búsqueda
     * @param query         consulta de la búsqueda
     * @param docTypes      tipo de documento
     * @param caseSensitive tener en cuenta mayúsculas y minísculas
     * @param members       número de miembros de la sesión
     *
     * @return lista de documentos encontrados
     *
     * @throws IllegalArgumentException si el principio especificado no es soportado
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
    * Principios de búsquedas soportados para este método:
     * <br>
     * <tt>-SINGLE_SEARCH_AND_SPLIT</tt><br>
     * <tt>-META_SEARCH_AND_SPLIT</tt><br>
     * <tt>-MULTI_SEARCH_AND_SWITCH</tt><br>
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
    public List<ResultSetMetaData> search(int searcher, int principle, String query, String[] docTypes, boolean caseSensitive, int members) throws IllegalArgumentException, SearchException, SearchableException;

    /**
     * Invoca una búsqueda a partir de los parámetros de entradad, aplicando
     * mecanismos de división del trabajo
     *
     * @param searcher      buscador
     * @param principle     principio de búsqueda
     * @param query         consulta de la búsqueda
     * @param docType       tipo de documento
     * @param field         campo del documento
     * @param caseSensitive tener en cuenta mayúsculas y minísculas
     * @param members       número de miembros de la sesión
     *
     * @return lista de documentos encontrados
     *
     * @throws IllegalArgumentException si el principio especificado no es soportado
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
    * Principios de búsquedas soportados para este método:
     * <br>
     * <tt>-SINGLE_SEARCH_AND_SPLIT</tt><br>
     * <tt>-META_SEARCH_AND_SPLIT</tt><br>
     * <tt>-MULTI_SEARCH_AND_SWITCH</tt><br>
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
    public List<ResultSetMetaData> search(int searcher, int principle, String query, String docType, int field, boolean caseSensitive, int members) throws IllegalArgumentException, SearchException, SearchableException;

    /**
     * Invoca una búsqueda a partir de los parámetros de entradad, aplicando
     * mecanismos de división del trabajo
     *
     * @param searcher      buscador
     * @param principle     principio de búsqueda
     * @param query         consulta de la búsqueda
     * @param docType       tipo de documento
     * @param fields        campos del documento
     * @param caseSensitive tener en cuenta mayúsculas y minísculas
     * @param members       número de miembros de la sesión
     *
     * @return lista de documentos encontrados
     *
     * @throws IllegalArgumentException si el principio especificado no es soportado
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
    * Principios de búsquedas soportados para este método:
     * <br>
     * <tt>-SINGLE_SEARCH_AND_SPLIT</tt><br>
     * <tt>-META_SEARCH_AND_SPLIT</tt><br>
     * <tt>-MULTI_SEARCH_AND_SWITCH</tt><br>
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
    public List<ResultSetMetaData> search(int searcher, int principle, String query, String docType, int[] fields, boolean caseSensitive, int members) throws IllegalArgumentException, SearchException, SearchableException;

    /**
     * Invoca una búsqueda a partir de los parámetros de entradad, aplicando
     * mecanismos de división del trabajo
     *
     * @param searcher      buscador
     * @param principle     principio de búsqueda
     * @param query         consulta de la búsqueda
     * @param docTypes      tipo de documento
     * @param field         campo del documento
     * @param caseSensitive tener en cuenta mayúsculas y minísculas
     * @param members       número de miembros de la sesión
     *
     * @return lista de documentos encontrados
     *
     * @throws IllegalArgumentException si el principio especificado no es soportado
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
    * Principios de búsquedas soportados para este método:
     * <br>
     * <tt>-SINGLE_SEARCH_AND_SPLIT</tt><br>
     * <tt>-META_SEARCH_AND_SPLIT</tt><br>
     * <tt>-MULTI_SEARCH_AND_SWITCH</tt><br>
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
    public List<ResultSetMetaData> search(int searcher, int principle, String query, String[] docTypes, int field, boolean caseSensitive, int members) throws IllegalArgumentException, SearchException, SearchableException;

    /**
    * Invoca una búsqueda a partir de los parámetros de entradad, aplicando
     * mecanismos de división del trabajo
     *
     * @param searcher      buscador
     * @param principle     principio de búsqueda
     * @param query         consulta de la búsqueda
     * @param docTypes      tipo de documento
     * @param fields        campos del documento
     * @param caseSensitive tener en cuenta mayúsculas y minísculas
     * @param members       número de miembros de la sesión
     *
     * @return lista de documentos encontrados
     *
     * @throws IllegalArgumentException si el principio especificado no es soportado
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
    * Principios de búsquedas soportados para este método:
     * <br>
     * <tt>-SINGLE_SEARCH_AND_SPLIT</tt><br>
     * <tt>-META_SEARCH_AND_SPLIT</tt><br>
     * <tt>-MULTI_SEARCH_AND_SWITCH</tt><br>
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
    public List<ResultSetMetaData> search(int searcher, int principle, String query, String[] docTypes, int[] fields, boolean caseSensitive, int members) throws IllegalArgumentException, SearchException, SearchableException;

    /**
     * Invoca una búsqueda a partir de los parámetros de entradad, aplicando
     * mecanismos de división del trabajo
     *
     * @param searchers     buscadores
     * @param principle     principio de búsqueda
     * @param query         consulta de la búsqueda
     * @param caseSensitive tener en cuenta mayúsculas y minísculas
     * @param members       número de miembros de la sesión
     *
     * @return lista de documentos encontrados
     *
     * @throws IllegalArgumentException si el principio especificado no es soportado
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
    * Principios de búsquedas soportados para este método:
     * <br>
     * <tt>-SINGLE_SEARCH_AND_SPLIT</tt><br>
     * <tt>-META_SEARCH_AND_SPLIT</tt><br>
     * <tt>-MULTI_SEARCH_AND_SWITCH</tt><br>
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
    public List<ResultSetMetaData> search(int[] searchers, int principle, String query, boolean caseSensitive, int members) throws IllegalArgumentException, SearchException, SearchableException;

    /**
     * Invoca una búsqueda a partir de los parámetros de entradad, aplicando
     * mecanismos de división del trabajo
     *
     * @param searchers     buscadores
     * @param principle     principio de búsqueda
     * @param query         consulta de la búsqueda
     * @param field         campo del documento
     * @param caseSensitive tener en cuenta mayúsculas y minísculas
     * @param members       número de miembros de la sesión
     *
     * @return lista de documentos encontrados
     *
     * @throws IllegalArgumentException si el principio especificado no es soportado
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
    * Principios de búsquedas soportados para este método:
     * <br>
     * <tt>-SINGLE_SEARCH_AND_SPLIT</tt><br>
     * <tt>-META_SEARCH_AND_SPLIT</tt><br>
     * <tt>-MULTI_SEARCH_AND_SWITCH</tt><br>
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
    public List<ResultSetMetaData> search(int[] searchers, int principle, String query, int field, boolean caseSensitive, int members) throws IllegalArgumentException, SearchException, SearchableException;

    /**
     * Invoca una búsqueda a partir de los parámetros de entradad, aplicando
     * mecanismos de división del trabajo
     *
     * @param searchers     buscadores
     * @param principle     principio de búsqueda
     * @param query         consulta de la búsqueda
     * @param fields        campos del documentos
     * @param caseSensitive tener en cuenta mayúsculas y minísculas
     * @param members       número de miembros de la sesión
     *
     * @return lista de documentos encontrados
     *
     * @throws IllegalArgumentException si el principio especificado no es soportado
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
    * Principios de búsquedas soportados para este método:
     * <br>
     * <tt>-SINGLE_SEARCH_AND_SPLIT</tt><br>
     * <tt>-META_SEARCH_AND_SPLIT</tt><br>
     * <tt>-MULTI_SEARCH_AND_SWITCH</tt><br>
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
    public List<ResultSetMetaData> search(int[] searchers, int principle, String query, int[] fields, boolean caseSensitive, int members) throws IllegalArgumentException, SearchException, SearchableException;

    /**
    * Invoca una búsqueda a partir de los parámetros de entradad, aplicando
     * mecanismos de división del trabajo
     *
     * @param searchers      buscadores
     * @param principle      principio de búsqueda
     * @param query          consulta de la búsqueda
     * @param docType        tipo de documento
     * @param caseSensitive  tener en cuenta mayúsculas y minísculas
     * @param members        número de miembros de la sesión
     *
     * @return lista de documentos encontrados
     *
     * @throws IllegalArgumentException si el principio especificado no es soportado
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
    * Principios de búsquedas soportados para este método:
     * <br>
     * <tt>-SINGLE_SEARCH_AND_SPLIT</tt><br>
     * <tt>-META_SEARCH_AND_SPLIT</tt><br>
     * <tt>-MULTI_SEARCH_AND_SWITCH</tt><br>
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
    public List<ResultSetMetaData> search(int[] searchers, int principle, String query, String docType, boolean caseSensitive, int members) throws IllegalArgumentException, SearchException, SearchableException;

    /**
     * Invoca una búsqueda a partir de los parámetros de entradad, aplicando
     * mecanismos de división del trabajo
     *
     * @param searchers     buscadores
     * @param principle     principio de búsqueda
     * @param query         consulta de la búsqueda
     * @param docTypes      tipo de documento
     * @param caseSensitive tener en cuenta mayúsculas y minísculas
     * @param members       número de miembros de la sesión
     *
     * @return lista de documentos encontrados
     *
     * @throws IllegalArgumentException si el principio especificado no es soportado
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
    * Principios de búsquedas soportados para este método:
     * <br>
     * <tt>-SINGLE_SEARCH_AND_SPLIT</tt><br>
     * <tt>-META_SEARCH_AND_SPLIT</tt><br>
     * <tt>-MULTI_SEARCH_AND_SWITCH</tt><br>
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
    public List<ResultSetMetaData> search(int[] searchers, int principle, String query, String[] docTypes, boolean caseSensitive, int members) throws IllegalArgumentException, SearchException, SearchableException;

    /**
    * Invoca una búsqueda a partir de los parámetros de entradad, aplicando
     * mecanismos de división del trabajo
     *
     * @param searchers     buscadores
     * @param principle     principio de búsqueda
     * @param query         consulta de la búsqueda
     * @param docType       tipo de documento
     * @param field         campo del documento
     * @param caseSensitive tener en cuenta mayúsculas y minísculas
     * @param members       número de miembros de la sesión
     *
     * @return lista de documentos encontrados
     *
     * @throws IllegalArgumentException si el principio especificado no es soportado
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
    * Principios de búsquedas soportados para este método:
     * <br>
     * <tt>-SINGLE_SEARCH_AND_SPLIT</tt><br>
     * <tt>-META_SEARCH_AND_SPLIT</tt><br>
     * <tt>-MULTI_SEARCH_AND_SWITCH</tt><br>
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
    public List<ResultSetMetaData> search(int[] searchers, int principle, String query, String docType, int field, boolean caseSensitive, int members) throws IllegalArgumentException, SearchException, SearchableException;

    /**
     * Invoca una búsqueda a partir de los parámetros de entradad, aplicando
     * mecanismos de división del trabajo
     *
     * @param searchers     buscadores
     * @param principle     principio de búsqueda
     * @param query         consulta de la búsqueda
     * @param docType       tipo de documento
     * @param fields        campos del documento
     * @param caseSensitive tener en cuenta mayúsculas y minísculas
     * @param members       número de miembros de la sesión
     *
     * @return lista de documentos encontrados
     *
     * @throws IllegalArgumentException si el principio especificado no es soportado
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
    * Principios de búsquedas soportados para este método:
     * <br>
     * <tt>-SINGLE_SEARCH_AND_SPLIT</tt><br>
     * <tt>-META_SEARCH_AND_SPLIT</tt><br>
     * <tt>-MULTI_SEARCH_AND_SWITCH</tt><br>
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
    public List<ResultSetMetaData> search(int[] searchers, int principle, String query, String docType, int[] fields, boolean caseSensitive, int members) throws IllegalArgumentException, SearchException, SearchableException;

    /**
     * Invoca una búsqueda a partir de los parámetros de entradad, aplicando
     * mecanismos de división del trabajo
     *
     * @param searchers     buscadores
     * @param principle     principio de búsqueda
     * @param query         consulta de la búsqueda
     * @param docTypes      tipo de documento
     * @param field         campo del documento
     * @param caseSensitive tener en cuenta mayúsculas y minísculas
     * @param members       número de miembros de la sesión
     *
     * @return lista de documentos encontrados
     *
     * @throws IllegalArgumentException si el principio especificado no es soportado
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
    * Principios de búsquedas soportados para este método:
     * <br>
     * <tt>-SINGLE_SEARCH_AND_SPLIT</tt><br>
     * <tt>-META_SEARCH_AND_SPLIT</tt><br>
     * <tt>-MULTI_SEARCH_AND_SWITCH</tt><br>
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
    public List<ResultSetMetaData> search(int[] searchers, int principle, String query, String[] docTypes, int field, boolean caseSensitive, int members) throws IllegalArgumentException, SearchException, SearchableException;

    /**
     * Invoca una búsqueda a partir de los parámetros de entradad, aplicando
     * mecanismos de división del trabajo
     *
     * @param searchers     buscadores
     * @param principle     principio de búsqueda
     * @param query         consulta de la búsqueda
     * @param docTypes      tipo de documento
     * @param fields        campos del documento
     * @param caseSensitive tener en cuenta mayúsculas y minísculas
     * @param members       número de miembros de la sesión
     *
     * @return lista de documentos encontrados
     *
     * @throws IllegalArgumentException si el principio especificado no es soportado
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
    * Principios de búsquedas soportados para este método:
     * <br>
     * <tt>-SINGLE_SEARCH_AND_SPLIT</tt><br>
     * <tt>-META_SEARCH_AND_SPLIT</tt><br>
     * <tt>-MULTI_SEARCH_AND_SWITCH</tt><br>
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
    public List<ResultSetMetaData> search(int[] searchers, int principle, String query, String[] docTypes, int[] fields, boolean caseSensitive, int members) throws IllegalArgumentException, SearchException, SearchableException;
}
