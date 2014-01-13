/*
 * DrakkarKeel - An Enterprise Collaborative Search Platform
 *
 * The contents of this file are subject under the terms described in the
 * DRAKKARKEEL_LICENSE file included in this distribution; you may not use this
 * file except in compliance with the License. 
 *
 * 2013-2014 DrakkarKeel Platform.
 */
package drakkar.stern.servant.service;

import drakkar.oar.Seeker;
import drakkar.oar.exception.SeekerException;
import drakkar.oar.exception.SessionException;
import drakkar.oar.slice.client.ClientSidePrx;
import drakkar.mast.SearchException;
import drakkar.mast.SearchableException;
import java.io.IOException;

/**
 * The <code>ResponseUtilFactory</code> class is.....
 * Esta interfaz declara todos los métodos búsqueda soportados por el framework
 * DrakkarKeel
 */
public interface SearchableInServerSide {

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, con el buscador
     * seleccionado. Permite la aplicación de mecanismos de división de trabajo
     *
     * @param sessionName    nombre de la sesión
     * @param query          consulta de la búsqueda
     * @param searcher       buscador
     * @param principle      principio de búsqueda
     * @param caseSensitive  tener en cuenta mayúsculas y minísculas
     * @param seeker         usuario
     * @param seekerPrx      proxy del usuario
     * 
     * @throws SessionException  si la sesión especificada no existe
     * @throws SeekerException     si el usuario que invoca la búsqueda no existe
     * @throws IllegalArgumentException  si el principio seleccionado no es válido
     * @throws SearchableException       si el buscador especificado  no es soportado por el servidor
     * @throws IOException               si ocurre alguna excepción durante el proceso de
     *                                   serialización del objeto Response
     * @throws SearchException           si ocurre alguna excepción durante el proceso de búsqueda
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
     * <tt>-SINGLE_SEARCH_AND_SPLIT</tt><br>
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
    public void executeSearch(String sessionName, String query, int searcher, int principle, boolean caseSensitive, Seeker seeker, ClientSidePrx seekerPrx) throws SessionException, SeekerException, SearchableException, SearchException, IllegalArgumentException, IOException;

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, con el buscador
     * seleccionado. Permite la aplicación de mecanismos de división de trabajo
     *
     * @param sessionName    nombre de la sesión
     * @param query          consulta de la búsqueda
     * @param field          campo del documento
     * @param searcher       buscador
     * @param principle      principio de búsqueda
     * @param caseSensitive  tener en cuenta mayúsculas y minísculas
     * @param seeker         usuario
     * @param seekerPrx      proxy del usuario
     *
     * @throws SessionException  si la sesión especificada no existe
     * @throws SeekerException     si el usuario que invoca la búsqueda no existe
     * @throws IllegalArgumentException  si el principio seleccionado no es válido
     * @throws SearchableException       si el buscador especificado  no es soportado por el servidor
     * @throws IOException               si ocurre alguna excepción durante el proceso de
     *                                   serialización del objeto Response
     * @throws SearchException           si ocurre alguna excepción durante el proceso de búsqueda
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
     * <tt>-SINGLE_SEARCH_AND_SPLIT</tt><br>
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
    public void executeSearch(String sessionName, String query, int field, int searcher, int principle, boolean caseSensitive, Seeker seeker, ClientSidePrx seekerPrx) throws SessionException, SeekerException, SearchableException, SearchException, IllegalArgumentException, IOException;

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, con el buscador
     * seleccionado. Permite la aplicación de mecanismos de división de trabajo
     *
     * @param sessionName    nombre de la sesión
     * @param query          consulta de la búsqueda
     * @param fields         campos del docuemento
     * @param searcher       buscador
     * @param principle      principio de búsqueda
     * @param caseSensitive  tener en cuenta mayúsculas y minísculas
     * @param seeker         usuario
     * @param seekerPrx      proxy del usuario
     *
     * @throws SessionException  si la sesión especificada no existe
     * @throws SeekerException     si el usuario que invoca la búsqueda no existe
     * @throws IllegalArgumentException  si el principio seleccionado no es válido
     * @throws SearchableException       si el buscador especificado  no es soportado por el servidor
     * @throws IOException               si ocurre alguna excepción durante el proceso de
     *                                   serialización del objeto Response
     * @throws SearchException           si ocurre alguna excepción durante el proceso de búsqueda
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
     * <tt>-SINGLE_SEARCH_AND_SPLIT</tt><br>
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
    public void executeSearch(String sessionName, String query, int[] fields, int searcher, int principle, boolean caseSensitive, Seeker seeker, ClientSidePrx seekerPrx) throws SessionException, SeekerException, SearchableException, SearchException, IllegalArgumentException, IOException;

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, con el buscador
     * seleccionado. Permite la aplicación de mecanismos de división de trabajo
     *
     * @param sessionName    nombre de la sesión
     * @param query          consulta de la búsqueda
     * @param docType        tipo de documento
     * @param searcher       buscador
     * @param principle      principio de búsqueda
     * @param caseSensitive  tener en cuenta mayúsculas y minísculas
     * @param seeker         usuario
     * @param seekerPrx      proxy del usuario
     *
     * @throws SessionException  si la sesión especificada no existe
     * @throws SeekerException     si el usuario que invoca la búsqueda no existe
     * @throws IllegalArgumentException  si el principio seleccionado no es válido
     * @throws SearchableException       si el buscador especificado  no es soportado por el servidor
     * @throws IOException               si ocurre alguna excepción durante el proceso de
     *                                   serialización del objeto Response
     * @throws SearchException           si ocurre alguna excepción durante el proceso de búsqueda
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
     * <tt>-SINGLE_SEARCH_AND_SPLIT</tt><br>
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
    public void executeSearch(String sessionName, String query, String docType, int searcher, int principle, boolean caseSensitive, Seeker seeker, ClientSidePrx seekerPrx) throws SessionException, SeekerException, SearchableException, SearchException, IllegalArgumentException, IOException;

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, con el buscador
     * seleccionado. Permite la aplicación de mecanismos de división de trabajo
     *
     * @param sessionName    nombre de la sesión
     * @param query          consulta de la búsqueda
     * @param docTypes       tipos de documentos
     * @param searcher       buscador
     * @param principle      principio de búsqueda
     * @param caseSensitive  tener en cuenta mayúsculas y minísculas
     * @param seeker         usuario
     * @param seekerPrx      proxy del usuario
     *
     * @throws SessionException  si la sesión especificada no existe
     * @throws SeekerException     si el usuario que invoca la búsqueda no existe
     * @throws IllegalArgumentException  si el principio seleccionado no es válido
     * @throws SearchableException       si el buscador especificado  no es soportado por el servidor
     * @throws IOException               si ocurre alguna excepción durante el proceso de
     *                                   serialización del objeto Response
     * @throws SearchException           si ocurre alguna excepción durante el proceso de búsqueda
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
     * <tt>-SINGLE_SEARCH_AND_SPLIT</tt><br>
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
    public void executeSearch(String sessionName, String query, String[] docTypes, int searcher, int principle, boolean caseSensitive, Seeker seeker, ClientSidePrx seekerPrx) throws SessionException, SeekerException, SearchableException, SearchException, IllegalArgumentException, IOException;

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, con el buscador
     * seleccionado. Permite la aplicación de mecanismos de división de trabajo
     *
     * @param sessionName    nombre de la sesión
     * @param query          consulta de la búsqueda
     * @param docType        tipo de documento
     * @param field          campo del documento
     * @param searcher       buscador
     * @param principle      principio de búsqueda
     * @param caseSensitive  tener en cuenta mayúsculas y minísculas
     * @param seeker         usuario
     * @param seekerPrx      proxy del usuario
     *
     * @throws SessionException  si la sesión especificada no existe
     * @throws SeekerException     si el usuario que invoca la búsqueda no existe
     * @throws IllegalArgumentException  si el principio seleccionado no es válido
     * @throws SearchableException       si el buscador especificado  no es soportado por el servidor
     * @throws IOException               si ocurre alguna excepción durante el proceso de
     *                                   serialización del objeto Response
     * @throws SearchException           si ocurre alguna excepción durante el proceso de búsqueda
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
     * <tt>-SINGLE_SEARCH_AND_SPLIT</tt><br>
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
    public void executeSearch(String sessionName, String query, String docType, int field, int searcher, int principle, boolean caseSensitive, Seeker seeker, ClientSidePrx seekerPrx) throws SessionException, SeekerException, SearchableException, SearchException, IllegalArgumentException, IOException;

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, con el buscador
     * seleccionado. Permite la aplicación de mecanismos de división de trabajo
     *
     * @param sessionName    nombre de la sesión
     * @param query          consulta de la búsqueda
     * @param docType        tipo de documento
     * @param fields         campos del documento
     * @param searcher       buscador
     * @param principle      principio de búsqueda
     * @param caseSensitive  tener en cuenta mayúsculas y minísculas
     * @param seeker         usuario
     * @param seekerPrx      proxy del usuario
     *
     * @throws SessionException  si la sesión especificada no existe
     * @throws SeekerException     si el usuario que invoca la búsqueda no existe
     * @throws IllegalArgumentException  si el principio seleccionado no es válido
     * @throws SearchableException       si el buscador especificado  no es soportado por el servidor
     * @throws IOException               si ocurre alguna excepción durante el proceso de
     *                                   serialización del objeto Response
     * @throws SearchException           si ocurre alguna excepción durante el proceso de búsqueda
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
     * <tt>-SINGLE_SEARCH_AND_SPLIT</tt><br>
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
    public void executeSearch(String sessionName, String query, String docType, int[] fields, int searcher, int principle, boolean caseSensitive, Seeker seeker, ClientSidePrx seekerPrx) throws SessionException, SeekerException, SearchableException, SearchException, IllegalArgumentException, IOException;

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, con el buscador
     * seleccionado. Permite la aplicación de mecanismos de división de trabajo
     *
     * @param sessionName    nombre de la sesión
     * @param query          consulta de la búsqueda
     * @param docTypes       tipos de documento
     * @param field          campo del documento
     * @param searcher       buscador
     * @param principle      principio de búsqueda
     * @param caseSensitive  tener en cuenta mayúsculas y minísculas
     * @param seeker         usuario
     * @param seekerPrx      proxy del usuario
     *
     * @throws SessionException  si la sesión especificada no existe
     * @throws SeekerException     si el usuario que invoca la búsqueda no existe
     * @throws IllegalArgumentException  si el principio seleccionado no es válido
     * @throws SearchableException       si el buscador especificado  no es soportado por el servidor
     * @throws IOException               si ocurre alguna excepción durante el proceso de
     *                                   serialización del objeto Response
     * @throws SearchException           si ocurre alguna excepción durante el proceso de búsqueda
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
     * <tt>-SINGLE_SEARCH_AND_SPLIT</tt><br>
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
    public void executeSearch(String sessionName, String query, String[] docTypes, int field, int searcher, int principle, boolean caseSensitive, Seeker seeker, ClientSidePrx seekerPrx) throws SessionException, SeekerException, SearchableException, SearchException, IllegalArgumentException, IOException;

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, con el buscador
     * seleccionado. Permite la aplicación de mecanismos de división de trabajo
     *
     * @param sessionName    nombre de la sesión
     * @param query          consulta de la búsqueda
     * @param docTypes       tipos de documento
     * @param fields         campo del documento
     * @param searcher       buscador
     * @param principle      principio de búsqueda
     * @param caseSensitive  tener en cuenta mayúsculas y minísculas
     * @param seeker         usuario
     * @param seekerPrx      proxy del usuario
     *
     * @throws SessionException  si la sesión especificada no existe
     * @throws SeekerException     si el usuario que invoca la búsqueda no existe
     * @throws IllegalArgumentException  si el principio seleccionado no es válido
     * @throws SearchableException       si el buscador especificado  no es soportado por el servidor
     * @throws IOException               si ocurre alguna excepción durante el proceso de
     *                                   serialización del objeto Response
     * @throws SearchException           si ocurre alguna excepción durante el proceso de búsqueda
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
     * <tt>-SINGLE_SEARCH_AND_SPLIT</tt><br>
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
    public void executeSearch(String sessionName, String query, String[] docTypes, int[] fields, int searcher, int principle, boolean caseSensitive, Seeker seeker, ClientSidePrx seekerPrx) throws SessionException, SeekerException, SearchableException, SearchException, IllegalArgumentException, IOException;
    //////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, con todos los
     * buscadores activos en el servidor. Se permite la aplicación de mecanismos de
     * división de trabajo
     *
     * @param sessionName    nombre de la sesión
     * @param query          consulta de la búsqueda
     * @param principle      principio de búsqueda
     * @param caseSensitive  tener en cuenta mayúsculas y minísculas
     * @param seeker         usuario
     * @param seekerPrx      proxy del usuario
     *
     * @throws SessionException  si la sesión especificada no existe
     * @throws SeekerException     si el usuario que invoca la búsqueda no existe
     * @throws IllegalArgumentException  si el principio seleccionado no es válido
     * @throws IOException               si ocurre alguna excepción durante el proceso de
     *                                   serialización del objeto Response
     * @throws SearchException           si ocurre alguna excepción durante el proceso de búsqueda
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
     * <tt>-META_SEARCH_AND_SPLIT</tt><br>
     * <tt>-MULTI_SEARCH_AND_SWITCH</tt><br>
     * <br>
     * Estas constantes se encuentran definidas en la clase <code>SearchPrinciple</code>,
     * contenida en el paquete drakkar.oar.util
     * <br>
     * <br>
     *
     * @see SearchPrinciple
     */
    public void executeSearch(String sessionName, int principle, String query, boolean caseSensitive, Seeker seeker, ClientSidePrx seekerPrx) throws SessionException, SeekerException, SearchException, IllegalArgumentException, IOException;

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, con todos los
     * buscadores activos en el servidor. Se permite la aplicación de mecanismos de
     * división de trabajo
     *
     * @param sessionName    nombre de la sesión
     * @param query          consulta de la búsqueda
     * @param principle      principio de búsqueda
     * @param field          campo del documento
     * @param caseSensitive  tener en cuenta mayúsculas y minísculas
     * @param seeker         usuario
     * @param seekerPrx      proxy del usuario
     *
     * @throws SessionException  si la sesión especificada no existe
     * @throws SeekerException     si el usuario que invoca la búsqueda no existe
     * @throws IllegalArgumentException  si el principio seleccionado no es válido
     * @throws IOException               si ocurre alguna excepción durante el proceso de
     *                                   serialización del objeto Response
     * @throws SearchException           si ocurre alguna excepción durante el proceso de búsqueda
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
     * <tt>-META_SEARCH_AND_SPLIT</tt><br>
     * <tt>-MULTI_SEARCH_AND_SWITCH</tt><br>
     * <br>
     * Estas constantes se encuentran definidas en la clase <code>SearchPrinciple</code>,
     * contenida en el paquete drakkar.oar.util
     * <br>
     * <br>
     *
     * @see SearchPrinciple
     */
    public void executeSearch(String sessionName, int principle, String query, int field, boolean caseSensitive, Seeker seeker, ClientSidePrx seekerPrx) throws SessionException, SeekerException, SearchException, IllegalArgumentException, IOException;

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, con todos los
     * buscadores activos en el servidor. Se permite la aplicación de mecanismos de
     * división de trabajo
     *
     * @param sessionName    nombre de la sesión
     * @param query          consulta de la búsqueda
     * @param principle      principio de búsqueda
     * @param fields         campo del documento
     * @param caseSensitive  tener en cuenta mayúsculas y minísculas
     * @param seeker         usuario
     * @param seekerPrx      proxy del usuario
     *
     * @throws SessionException  si la sesión especificada no existe
     * @throws SeekerException     si el usuario que invoca la búsqueda no existe
     * @throws IllegalArgumentException  si el principio seleccionado no es válido
     * @throws IOException               si ocurre alguna excepción durante el proceso de
     *                                   serialización del objeto Response
     * @throws SearchException           si ocurre alguna excepción durante el proceso de búsqueda
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
     * <tt>-META_SEARCH_AND_SPLIT</tt><br>
     * <tt>-MULTI_SEARCH_AND_SWITCH</tt><br>
     * <br>
     * Estas constantes se encuentran definidas en la clase <code>SearchPrinciple</code>,
     * contenida en el paquete drakkar.oar.util
     * <br>
     * <br>
     *
     * @see SearchPrinciple
     */
    public void executeSearch(String sessionName, int principle, String query, int[] fields, boolean caseSensitive, Seeker seeker, ClientSidePrx seekerPrx) throws SessionException, SeekerException, SearchException, IllegalArgumentException, IOException;

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, con todos los
     * buscadores activos en el servidor. Se permite la aplicación de mecanismos de
     * división de trabajo
     *
     * @param sessionName    nombre de la sesión
     * @param query          consulta de la búsqueda
     * @param principle      principio de búsqueda
     * @param docType        tipo de documento
     * @param caseSensitive  tener en cuenta mayúsculas y minísculas
     * @param seeker         usuario
     * @param seekerPrx      proxy del usuario
     *
     * @throws SessionException  si la sesión especificada no existe
     * @throws SeekerException     si el usuario que invoca la búsqueda no existe
     * @throws IllegalArgumentException  si el principio seleccionado no es válido
     * @throws IOException               si ocurre alguna excepción durante el proceso de
     *                                   serialización del objeto Response
     * @throws SearchException           si ocurre alguna excepción durante el proceso de búsqueda
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
     * <tt>-META_SEARCH_AND_SPLIT</tt><br>
     * <tt>-MULTI_SEARCH_AND_SWITCH</tt><br>
     * <br>
     * Estas constantes se encuentran definidas en la clase <code>SearchPrinciple</code>,
     * contenida en el paquete drakkar.oar.util
     * <br>
     * <br>
     *
     * @see SearchPrinciple
     */
    public void executeSearch(String sessionName, int principle, String query, String docType, boolean caseSensitive, Seeker seeker, ClientSidePrx seekerPrx) throws SessionException, SeekerException, SearchException, IllegalArgumentException, IOException;

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, con todos los
     * buscadores activos en el servidor. Se permite la aplicación de mecanismos de
     * división de trabajo
     *
     * @param sessionName    nombre de la sesión
     * @param query          consulta de la búsqueda
     * @param principle      principio de búsqueda
     * @param docTypes       tipo de documento
     * @param caseSensitive  tener en cuenta mayúsculas y minísculas
     * @param seeker         usuario
     * @param seekerPrx      proxy del usuario
     *
     * @throws SessionException  si la sesión especificada no existe
     * @throws SeekerException     si el usuario que invoca la búsqueda no existe
     * @throws IllegalArgumentException  si el principio seleccionado no es válido
     * @throws IOException               si ocurre alguna excepción durante el proceso de
     *                                   serialización del objeto Response
     * @throws SearchException           si ocurre alguna excepción durante el proceso de búsqueda
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
     * <tt>-META_SEARCH_AND_SPLIT</tt><br>
     * <tt>-MULTI_SEARCH_AND_SWITCH</tt><br>
     * <br>
     * Estas constantes se encuentran definidas en la clase <code>SearchPrinciple</code>,
     * contenida en el paquete drakkar.oar.util
     * <br>
     *
     * @see SearchPrinciple
     */
    public void executeSearch(String sessionName, int principle, String query, String[] docTypes, boolean caseSensitive, Seeker seeker, ClientSidePrx seekerPrx) throws SessionException, SeekerException, SearchException, IllegalArgumentException, IOException;

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, con todos los
     * buscadores activos en el servidor. Se permite la aplicación de mecanismos de
     * división de trabajo
     *
     * @param sessionName    nombre de la sesión
     * @param query          consulta de la búsqueda
     * @param principle      principio de búsqueda
     * @param docType        tipo de documento
     * @param field          campo del documento
     * @param caseSensitive  tener en cuenta mayúsculas y minísculas
     * @param seeker         usuario
     * @param seekerPrx      proxy del usuario
     *
     * @throws SessionException  si la sesión especificada no existe
     * @throws SeekerException     si el usuario que invoca la búsqueda no existe
     * @throws IllegalArgumentException  si el principio seleccionado no es válido
     * @throws IOException               si ocurre alguna excepción durante el proceso de
     *                                   serialización del objeto Response
     * @throws SearchException           si ocurre alguna excepción durante el proceso de búsqueda
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
     * <tt>-META_SEARCH_AND_SPLIT</tt><br>
     * <tt>-MULTI_SEARCH_AND_SWITCH</tt><br>
     * <br>
     * Estas constantes se encuentran definidas en la clase <code>SearchPrinciple</code>,
     * contenida en el paquete drakkar.oar.util
     * <br>
     * <br>
     *
     * @see SearchPrinciple
     */
    public void executeSearch(String sessionName, int principle, String query, String docType, int field, boolean caseSensitive, Seeker seeker, ClientSidePrx seekerPrx) throws SessionException, SeekerException, SearchException, IllegalArgumentException, IOException;

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, con todos los
     * buscadores activos en el servidor. Se permite la aplicación de mecanismos de
     * división de trabajo
     *
     * @param sessionName    nombre de la sesión
     * @param query          consulta de la búsqueda
     * @param principle      principio de búsqueda
     * @param docType        tipo de documento
     * @param fields         campo del documento
     * @param caseSensitive  tener en cuenta mayúsculas y minísculas
     * @param seeker         usuario
     * @param seekerPrx      proxy del usuario
     *
     * @throws SessionException  si la sesión especificada no existe
     * @throws SeekerException     si el usuario que invoca la búsqueda no existe
     * @throws IllegalArgumentException  si el principio seleccionado no es válido
     * @throws IOException               si ocurre alguna excepción durante el proceso de
     *                                   serialización del objeto Response
     * @throws SearchException           si ocurre alguna excepción durante el proceso de búsqueda
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
     * <tt>-META_SEARCH_AND_SPLIT</tt><br>
     * <tt>-MULTI_SEARCH_AND_SWITCH</tt><br>
     * <br>
     * Estas constantes se encuentran definidas en la clase <code>SearchPrinciple</code>,
     * contenida en el paquete drakkar.oar.util
     * <br>
     * <br>
     *
     * @see SearchPrinciple
     */
    public void executeSearch(String sessionName, int principle, String query, String docType, int[] fields, boolean caseSensitive, Seeker seeker, ClientSidePrx seekerPrx) throws SessionException, SeekerException, SearchException, IllegalArgumentException, IOException;

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, con todos los
     * buscadores activos en el servidor. Se permite la aplicación de mecanismos de
     * división de trabajo
     *
     * @param sessionName    nombre de la sesión
     * @param query          consulta de la búsqueda
     * @param principle      principio de búsqueda
     * @param docTypes       tipos de documento
     * @param field          campo del documento
     * @param caseSensitive  tener en cuenta mayúsculas y minísculas
     * @param seeker         usuario
     * @param seekerPrx      proxy del usuario
     *
     * @throws SessionException  si la sesión especificada no existe
     * @throws SeekerException     si el usuario que invoca la búsqueda no existe
     * @throws IllegalArgumentException  si el principio seleccionado no es válido
     * @throws IOException               si ocurre alguna excepción durante el proceso de
     *                                   serialización del objeto Response
     * @throws SearchException           si ocurre alguna excepción durante el proceso de búsqueda
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
     * <tt>-META_SEARCH_AND_SPLIT</tt><br>
     * <tt>-MULTI_SEARCH_AND_SWITCH</tt><br>
     * <br>
     * Estas constantes se encuentran definidas en la clase <code>SearchPrinciple</code>,
     * contenida en el paquete drakkar.oar.util
     * <br>
     * 
     * @see SearchPrinciple
     */
    public void executeSearch(String sessionName, int principle, String query, String[] docTypes, int field, boolean caseSensitive, Seeker seeker, ClientSidePrx seekerPrx) throws SessionException, SeekerException, SearchException, IllegalArgumentException, IOException;

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, con todos los
     * buscadores activos en el servidor. Se permite la aplicación de mecanismos de
     * división de trabajo
     *
     * @param sessionName    nombre de la sesión
     * @param query          consulta de la búsqueda
     * @param principle      principio de búsqueda
     * @param docTypes       tipos de documento
     * @param fields         campos del documento
     * @param caseSensitive  tener en cuenta mayúsculas y minísculas
     * @param seeker         usuario
     * @param seekerPrx      proxy del usuario
     *
     * @throws SessionException  si la sesión especificada no existe
     * @throws SeekerException     si el usuario que invoca la búsqueda no existe
     * @throws IllegalArgumentException  si el principio seleccionado no es válido
     * @throws IOException               si ocurre alguna excepción durante el proceso de
     *                                   serialización del objeto Response
     * @throws SearchException           si ocurre alguna excepción durante el proceso de búsqueda
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
     * <tt>-META_SEARCH_AND_SPLIT</tt><br>
     * <tt>-MULTI_SEARCH_AND_SWITCH</tt><br>
     * <br>
     * Estas constantes se encuentran definidas en la clase <code>SearchPrinciple</code>,
     * contenida en el paquete drakkar.oar.util
     * <br>
     *
     * @see SearchPrinciple
     */
    public void executeSearch(String sessionName, int principle, String query, String[] docTypes, int[] fields, boolean caseSensitive, Seeker seeker, ClientSidePrx seekerPrx) throws SessionException, SeekerException, SearchException, IllegalArgumentException, IOException;
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, con los
     * buscadores seleccionados. Se permite la aplicación de mecanismos de
     * división de trabajo
     *
     * @param sessionName    nombre de la sesión
     * @param query          consulta de la búsqueda
     * @param searchers      buscadores
     * @param principle      principio de búsqueda
     * @param caseSensitive  tener en cuenta mayúsculas y minísculas
     * @param seeker         usuario
     * @param seekerPrx      proxy del usuario
     *
     * @throws SessionException  si la sesión especificada no existe
     * @throws SeekerException     si el usuario que invoca la búsqueda no existe
     * @throws IllegalArgumentException  si el principio seleccionado no es válido
     * @throws SearchableException       si el buscador especificado  no es soportado por el servidor
     * @throws IOException               si ocurre alguna excepción durante el proceso de
     *                                   serialización del objeto Response
     * @throws SearchException           si ocurre alguna excepción durante el proceso de búsqueda
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
     * <tt>-META_SEARCH_AND_SPLIT</tt><br>
     * <tt>-MULTI_SEARCH_AND_SWITCH</tt><br>
     * <br>
     * Estas constantes se encuentran definidas en la clase <code>SearchPrinciple</code>,
     * contenida en el paquete drakkar.oar.util
     * <br>
     *
     * @see SearchPrinciple
     */
    public void executeSearch(String sessionName, int principle, int[] searchers, String query, boolean caseSensitive, Seeker seeker, ClientSidePrx seekerPrx) throws SessionException, SeekerException, SearchableException, SearchException, IllegalArgumentException, IOException;

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, con los
     * buscadores seleccionados. Se permite la aplicación de mecanismos de
     * división de trabajo
     *
     * @param sessionName    nombre de la sesión
     * @param query          consulta de la búsqueda
     * @param searchers      buscadores
     * @param principle      principio de búsqueda
     * @param field          campo del documento
     * @param caseSensitive  tener en cuenta mayúsculas y minísculas
     * @param seeker         usuario
     * @param seekerPrx      proxy del usuario
     *
     * @throws SessionException  si la sesión especificada no existe
     * @throws SeekerException     si el usuario que invoca la búsqueda no existe
     * @throws IllegalArgumentException  si el principio seleccionado no es válido
     * @throws SearchableException       si el buscador especificado  no es soportado por el servidor
     * @throws IOException               si ocurre alguna excepción durante el proceso de
     *                                   serialización del objeto Response
     * @throws SearchException           si ocurre alguna excepción durante el proceso de búsqueda
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
     * <tt>-META_SEARCH_AND_SPLIT</tt><br>
     * <tt>-MULTI_SEARCH_AND_SWITCH</tt><br>
     * <br>
     * Estas constantes se encuentran definidas en la clase <code>SearchPrinciple</code>,
     * contenida en el paquete drakkar.oar.util
     * <br>
     *
     * @see SearchPrinciple
     */
    public void executeSearch(String sessionName, int principle, int[] searchers, String query, int field, boolean caseSensitive, Seeker seeker, ClientSidePrx seekerPrx) throws SessionException, SeekerException, SearchableException, SearchException, IllegalArgumentException, IOException;

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, con los
     * buscadores seleccionados. Se permite la aplicación de mecanismos de
     * división de trabajo
     *
     * @param sessionName    nombre de la sesión
     * @param query          consulta de la búsqueda
     * @param searchers      buscadores
     * @param principle      principio de búsqueda
     * @param fields         campos del documento
     * @param caseSensitive  tener en cuenta mayúsculas y minísculas
     * @param seeker         usuario
     * @param seekerPrx      proxy del usuario
     *
     * @throws SessionException  si la sesión especificada no existe
     * @throws SeekerException     si el usuario que invoca la búsqueda no existe
     * @throws IllegalArgumentException  si el principio seleccionado no es válido
     * @throws SearchableException       si el buscador especificado  no es soportado por el servidor
     * @throws IOException               si ocurre alguna excepción durante el proceso de
     *                                   serialización del objeto Response
     * @throws SearchException           si ocurre alguna excepción durante el proceso de búsqueda
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
     * <tt>-META_SEARCH_AND_SPLIT</tt><br>
     * <tt>-MULTI_SEARCH_AND_SWITCH</tt><br>
     * <br>
     * Estas constantes se encuentran definidas en la clase <code>SearchPrinciple</code>,
     * contenida en el paquete drakkar.oar.util
     * <br>
     *
     * @see SearchPrinciple
     */
    public void executeSearch(String sessionName, int principle, int[] searchers, String query, int[] fields, boolean caseSensitive, Seeker seeker, ClientSidePrx seekerPrx) throws SessionException, SeekerException, SearchableException, SearchException, IllegalArgumentException, IOException;

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, con los
     * buscadores seleccionados. Se permite la aplicación de mecanismos de
     * división de trabajo
     *
     * @param sessionName    nombre de la sesión
     * @param query          consulta de la búsqueda
     * @param searchers      buscadores
     * @param principle      principio de búsqueda
     * @param docType        tipo de documento
     * @param caseSensitive  tener en cuenta mayúsculas y minísculas
     * @param seeker         usuario
     * @param seekerPrx      proxy del usuario
     *
     * @throws SessionException  si la sesión especificada no existe
     * @throws SeekerException     si el usuario que invoca la búsqueda no existe
     * @throws IllegalArgumentException  si el principio seleccionado no es válido
     * @throws SearchableException       si el buscador especificado  no es soportado por el servidor
     * @throws IOException               si ocurre alguna excepción durante el proceso de
     *                                   serialización del objeto Response
     * @throws SearchException           si ocurre alguna excepción durante el proceso de búsqueda
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
     * <tt>-META_SEARCH_AND_SPLIT</tt><br>
     * <tt>-MULTI_SEARCH_AND_SWITCH</tt><br>
     * <br>
     * Estas constantes se encuentran definidas en la clase <code>SearchPrinciple</code>,
     * contenida en el paquete drakkar.oar.util
     * <br>
     *
     * @see SearchPrinciple
     */
    public void executeSearch(String sessionName, int principle, int[] searchers, String query, String docType, boolean caseSensitive, Seeker seeker, ClientSidePrx seekerPrx) throws SessionException, SeekerException, SearchableException, SearchException, IllegalArgumentException, IOException;

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, con los
     * buscadores seleccionados. Se permite la aplicación de mecanismos de
     * división de trabajo
     *
     * @param sessionName    nombre de la sesión
     * @param query          consulta de la búsqueda
     * @param searchers      buscadores
     * @param principle      principio de búsqueda
     * @param docTypes       tipos de documentos
     * @param caseSensitive  tener en cuenta mayúsculas y minísculas
     * @param seeker         usuario
     * @param seekerPrx      proxy del usuario
     *
     * @throws SessionException  si la sesión especificada no existe
     * @throws SeekerException     si el usuario que invoca la búsqueda no existe
     * @throws IllegalArgumentException  si el principio seleccionado no es válido
     * @throws SearchableException       si el buscador especificado  no es soportado por el servidor
     * @throws IOException               si ocurre alguna excepción durante el proceso de
     *                                   serialización del objeto Response
     * @throws SearchException           si ocurre alguna excepción durante el proceso de búsqueda
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
     * <tt>-META_SEARCH_AND_SPLIT</tt><br>
     * <tt>-MULTI_SEARCH_AND_SWITCH</tt><br>
     * <br>
     * Estas constantes se encuentran definidas en la clase <code>SearchPrinciple</code>,
     * contenida en el paquete drakkar.oar.util
     * <br>
     *
     * @see SearchPrinciple
     */
    public void executeSearch(String sessionName, int principle, int[] searchers, String query, String[] docTypes, boolean caseSensitive, Seeker seeker, ClientSidePrx seekerPrx) throws SessionException, SeekerException, SearchableException, SearchException, IllegalArgumentException, IOException;

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, con los
     * buscadores seleccionados. Se permite la aplicación de mecanismos de
     * división de trabajo
     *
     * @param sessionName    nombre de la sesión
     * @param query          consulta de la búsqueda
     * @param searchers      buscadores
     * @param principle      principio de búsqueda
     * @param docType        tipo de documento
     * @param field          campo de documento
     * @param caseSensitive  tener en cuenta mayúsculas y minísculas
     * @param seeker         usuario
     * @param seekerPrx      proxy del usuario
     *
     * @throws SessionException  si la sesión especificada no existe
     * @throws SeekerException     si el usuario que invoca la búsqueda no existe
     * @throws IllegalArgumentException  si el principio seleccionado no es válido
     * @throws SearchableException       si el buscador especificado  no es soportado por el servidor
     * @throws IOException               si ocurre alguna excepción durante el proceso de
     *                                   serialización del objeto Response
     * @throws SearchException           si ocurre alguna excepción durante el proceso de búsqueda
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
     * <tt>-META_SEARCH_AND_SPLIT</tt><br>
     * <tt>-MULTI_SEARCH_AND_SWITCH</tt><br>
     * <br>
     * Estas constantes se encuentran definidas en la clase <code>SearchPrinciple</code>,
     * contenida en el paquete drakkar.oar.util
     * <br>
     *
     * @see SearchPrinciple
     */
    public void executeSearch(String sessionName, int principle, int[] searchers, String query, String docType, int field, boolean caseSensitive, Seeker seeker, ClientSidePrx seekerPrx) throws SessionException, SeekerException, SearchableException, SearchException, IllegalArgumentException, IOException;

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, con los
     * buscadores seleccionados. Se permite la aplicación de mecanismos de
     * división de trabajo
     *
     * @param sessionName    nombre de la sesión
     * @param query          consulta de la búsqueda
     * @param searchers      buscadores
     * @param principle      principio de búsqueda
     * @param docType        tipo de documento
     * @param fields         campos del documento
     * @param caseSensitive  tener en cuenta mayúsculas y minísculas
     * @param seeker         usuario
     * @param seekerPrx      proxy del usuario
     *
     * @throws SessionException  si la sesión especificada no existe
     * @throws SeekerException     si el usuario que invoca la búsqueda no existe
     * @throws IllegalArgumentException  si el principio seleccionado no es válido
     * @throws SearchableException       si el buscador especificado  no es soportado por el servidor
     * @throws IOException               si ocurre alguna excepción durante el proceso de
     *                                   serialización del objeto Response
     * @throws SearchException           si ocurre alguna excepción durante el proceso de búsqueda
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
     * <tt>-META_SEARCH_AND_SPLIT</tt><br>
     * <tt>-MULTI_SEARCH_AND_SWITCH</tt><br>
     * <br>
     * Estas constantes se encuentran definidas en la clase <code>SearchPrinciple</code>,
     * contenida en el paquete drakkar.oar.util
     * <br>
     *
     * @see SearchPrinciple
     */
    public void executeSearch(String sessionName, int principle, int[] searchers, String query, String docType, int[] fields, boolean caseSensitive, Seeker seeker, ClientSidePrx seekerPrx) throws SessionException, SeekerException, SearchableException, SearchException, IllegalArgumentException, IOException;

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, con los
     * buscadores seleccionados. Se permite la aplicación de mecanismos de
     * división de trabajo
     *
     * @param sessionName    nombre de la sesión
     * @param query          consulta de la búsqueda
     * @param searchers      buscadores
     * @param principle      principio de búsqueda
     * @param docTypes       tipos de documento
     * @param field          campo del documento
     * @param caseSensitive  tener en cuenta mayúsculas y minísculas
     * @param seeker         usuario
     * @param seekerPrx      proxy del usuario
     *
     * @throws SessionException  si la sesión especificada no existe
     * @throws SeekerException     si el usuario que invoca la búsqueda no existe
     * @throws IllegalArgumentException  si el principio seleccionado no es válido
     * @throws SearchableException       si el buscador especificado  no es soportado por el servidor
     * @throws IOException               si ocurre alguna excepción durante el proceso de
     *                                   serialización del objeto Response
     * @throws SearchException           si ocurre alguna excepción durante el proceso de búsqueda
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
     * <tt>-META_SEARCH_AND_SPLIT</tt><br>
     * <tt>-MULTI_SEARCH_AND_SWITCH</tt><br>
     * <br>
     * Estas constantes se encuentran definidas en la clase <code>SearchPrinciple</code>,
     * contenida en el paquete drakkar.oar.util
     * <br>
     *
     * @see SearchPrinciple
     */
    public void executeSearch(String sessionName, int principle, int[] searchers, String query, String[] docTypes, int field, boolean caseSensitive, Seeker seeker, ClientSidePrx seekerPrx) throws SessionException, SeekerException, SearchableException, SearchException, IllegalArgumentException, IOException;

    /**
     * Invoca una búsqueda a partir de los parámetros de entrada, con los
     * buscadores seleccionados. Se permite la aplicación de mecanismos de
     * división de trabajo
     *
     * @param sessionName    nombre de la sesión
     * @param query          consulta de la búsqueda
     * @param searchers      buscadores
     * @param principle      principio de búsqueda
     * @param docTypes       tipos de documento
     * @param fields         campos del documento
     * @param caseSensitive  tener en cuenta mayúsculas y minísculas
     * @param seeker         usuario
     * @param seekerPrx      proxy del usuario
     *
     * @throws SessionException  si la sesión especificada no existe
     * @throws SeekerException     si el usuario que invoca la búsqueda no existe
     * @throws IllegalArgumentException  si el principio seleccionado no es válido
     * @throws SearchableException       si el buscador especificado  no es soportado por el servidor
     * @throws IOException               si ocurre alguna excepción durante el proceso de
     *                                   serialización del objeto Response
     * @throws SearchException           si ocurre alguna excepción durante el proceso de búsqueda
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
     * <tt>-META_SEARCH_AND_SPLIT</tt><br>
     * <tt>-MULTI_SEARCH_AND_SWITCH</tt><br>
     * <br>
     * Estas constantes se encuentran definidas en la clase <code>SearchPrinciple</code>,
     * contenida en el paquete drakkar.oar.util
     * <br>
     *
     * @see SearchPrinciple
     */
    public void executeSearch(String sessionName, int principle, int[] searchers, String query, String[] docTypes, int[] fields, boolean caseSensitive, Seeker seeker, ClientSidePrx seekerPrx) throws SessionException, SeekerException, SearchableException, SearchException, IllegalArgumentException, IOException;

    //busqueda svn
    public void executeSearch(String sessionName, String query, String svnRepository, String fileType, String sort, String lastmodified, boolean fileBody,Seeker seeker, ClientSidePrx seekerPrx)throws SessionException, SeekerException, SearchableException, SearchException, IllegalArgumentException, IOException;

    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Devuelve el total de búsquedas realizadas por los usuarios de todas las
     * sesiones
     *
     * @return  total de búsquedas
     */
    public long getSearchesCount();

    /**
     * Devuelve el total de búsquedas realizadas por los usuarios de la sesión
     * especificada
     *
     * @param sessionName  nombre de la sesión
     *
     * @return total de búsquedas
     *
     * @throws SessionException si la sesión no se encuentra registrada
     *                                  en el servidor
     */
    public long getSearchesCount(String sessionName) throws SessionException;

    /**
     * Devuelve el total de búsquedas realizadas por un usuario de la sesión
     * especificada
     *
     * @param sessionName  nombre de la sesión
     * @param seeker       usuario
     *
     * @return total de búsquedas
     *
     * @throws SessionException si la sesión no se encuentra registrada
     *                                  en el servidor
     * @throws SeekerException    si el usuario del que solicita el total
     *                                  búsqueda no está registrado en la sesión
     */
    public long getSearchesCount(String sessionName, Seeker seeker) throws SessionException, SeekerException;
}
