/*
 * DrakkarKeel - An Enterprise Collaborative Search Platform
 *
 * The contents of this file are subject under the terms described in the
 * DRAKKARKEEL_LICENSE file included in this distribution; you may not use this
 * file except in compliance with the License. 
 *
 * 2013-2014 DrakkarKeel Platform.
 */
package drakkar.prow.facade.desktop.event;

import drakkar.oar.facade.event.FacadeDesktopListener;

public interface SearchListener extends FacadeDesktopListener {

    /**
     * Notifica al oyente FacadeListener registrado en la aplicación del cliente
     * los resultados de la búsqueda invocada
     *
     * @param evt instancia de SearchEvent
     *
     * <br><br>
     * El objeto SearchEvent(evt), en dependencia de la acción a notificar tiene la
     * tiene los siguientes parámetros:
     * <br><pre>
     *  public void notifySearchResults(SearchEvent evt){
     *      Response rsp = evt.getResponse();
     *      int searchType = (Integer) rsp.get(SEARCH_TYPE);
     *      switch (reply) {
     *            case INDIVIDUAL_SEARCH:
     *              .......
     *             case COLLAB_SEARCH:
     *              .....
     *         }
     *  }
     * </pre>
     *
     * La siguiente tabla muestra la relación de campos(clave-valor) de los posibles
     * tipos de eventos que pueden originar esta notificación. Las constantes clave(key)
     * pertenecen a la clase <b>​drakkar.oar.​util.​KeyTransaction </b> y los valores(values) a
     * la clase  <b> drakkar.oar.​util.​SeekerAction </b>.
     *
     *
     * <br><br><table border = 1 summary="Shows property keys and associated values">
     * <tr><th>Key</th>
     *     <th>Associated Value</th><th>Description</th></tr>
     * <tr>
     *    <td rowspan= 2><code>SEARCH_TYPE</code></td>
     *     <td><code>INDIVIDUAL_SEARCH<code></td>
     *    <td>Notifica los resultados de una búsqueda individual.</td>
     * </tr>
     * <tr>
     *     <td><code>COLLAB_SEARCH<code></td>
     *    <td>Notifica los resultados de una búsqueda colaborativa.</td>
     * </tr>
     * 
     * </table>
     *
     * <br><b>Case:<b> INDIVIDUAL_SEARCH
     * <br><table border = 1 summary="Shows property keys and associated values">
     * <tr>
     *    <th>Key</th>
     *     <th>Associated Value</th>
     *    <th>Description</th></tr>
     * <tr>
     *    <td><code>IS_EMPTY</code></td>
     *     <td><code>Boolean<code></td>
     *    <td>True si se encontraron resultados, false en caso contrario.</td>
     * </tr>
     * <tr>
     *    <td><code>SEARCH_RESULTS<code></td>
     *   <td><code>ResultSetMetaData<code></td>
     *    <td>Resultados de la búsqueda.<b>Nota:</b> si IS_EMPTY = true, el valor de
     *   este atributo será nulo, por lo que se recomienda verificar antes(IS_EMPTY).
     *   </td>
     * </tr>
     *
     * </table>
     *
     * <br><b>Case:<b> COLLAB_SEARCH
     * <br><table border = 1 summary="Shows property keys and associated values">
     * <tr>
     *    <th>Key</th>
     *     <th>Associated Value</th>
     *    <th>Description</th></tr>
     *
     * <tr>
     *    <td><code>SEARCH_RESULTS<code></td>
     *   <td><code>ResultSetMetaData<code></td>
     *    <td>Resultados de la búsqueda.</td>
     * </tr>
     *
     *  <tr>
     *    <td><code>SEEKER_EMITTER<code></td>
     *   <td><code>Seeker<code></td>
     *    <td>Instancia del objeto Seeker, que representa al seeker que ejecutó la
     *    búsqueda colaborativa.</td>
     * </tr>
     *
     * </table>
     *
     */
    public void notifySearchResults(SearchEvent evt);

    /**
     * Notifica los buscadores disponibles en el servidor
     * @param evt instancia de la evento SearchEvent
     *
     * 
     * El objeto SearchEvent contiene la relación de buscadores disponibles en el
     * servidor, para la búsquedas de los clientes(seekers).
     * <pre>
     *  public void notifyAvailableSearchers(SearchEvent evt){
     *      Response rsp = evt.getResponse();
     *      String [] searchers = (String[]) rsp.get(SEARCHERS);
     *      ...
     *  }
     * </pre>
     *
     * La siguiente tabla muestra la relación de campos(clave-valor) de esta notificación.
     * Las constantes clave(key) pertenecen a la clase <b>​drakkar.oar.​util.​KeyTransaction </b>.
     *
     *
     * <table border = 1 summary="Shows property keys and associated values">
     * <tr><th>Key</th>
     *     <th>Associated Value</th><th>Description</th></tr>
     * <tr>
     *    <td><code>SEARCHERS</code></td>
     *     <td><code>String[] <code></td>
     *    <td>Listado de los nombres de buscadores disponibles en el servidor.</td>
     * </tr>
     *
     * </table>
     */
    public void notifyAvailableSearchers(SearchEvent evt);

    /**
     * Notifica los servidores subversión disponibles en el servidor (Falta Arreglar esto)
     * 
     * @param evt instancia de la evento SearchEvent
     *
     *
     * El objeto SearchEvent contiene la relación de buscadores disponibles en el
     * servidor, para la búsquedas de los clientes(seekers).
     * <pre>
     *  public void notifyAvailableSearchers(SearchEvent evt){
     *      Response rsp = evt.getResponse();
     *      String [] searchers = (String[]) rsp.get(SEARCHERS);
     *      ...
     *  }
     * </pre>
     *
     * La siguiente tabla muestra la relación de campos(clave-valor) de esta notificación.
     * Las constantes clave(key) pertenecen a la clase <b>​drakkar.oar.​util.​KeyTransaction </b>.
     *
     *
     * <table border = 1 summary="Shows property keys and associated values">
     * <tr><th>Key</th>
     *     <th>Associated Value</th><th>Description</th></tr>
     * <tr>
     *    <td><code>SEARCHERS</code></td>
     *     <td><code>String[] <code></td>
     *    <td>Listado de los nombres de buscadores disponibles en el servidor.</td>
     * </tr>
     *
     * </table>
     */
    public void notifyAvailableSVNRepositories(SearchEvent evt);


    /**
     * Notifica los principios de búsqueda disponibles en el servidor
     * @param evt instancia de la evento SearchEvent
     *
     *
     * El objeto SearchEvent contiene la relación de buscadores disponibles en el
     * servidor, para la búsquedas de los clientes(seekers).
     * <pre>
     *  public void notifyAvailableSearchPrinciples(SearchEvent evt){
     *      Response rsp = evt.getResponse();
     *      ArrayList<String> principles = (ArrayList<String>) rsp.get(SEARCH_PRINCIPLES);
     *      ...
     *  }
     * </pre>
     *
     * La siguiente tabla muestra la relación de campos(clave-valor) de esta notificación.
     * Las constantes clave(key) pertenecen a la clase <b>​drakkar.oar.​util.​KeyTransaction </b>.
     *
     *
     * <table border = 1 summary="Shows property keys and associated values">
     * <tr><th>Key</th>
     *     <th>Associated Value</th><th>Description</th></tr>
     * <tr>
     *   <td ><code>SEARCH_PRINCIPLES</code></td>
     *    <td><code>ArrayList&lt;String&gt; <code></td>
     *    <td>Principios de búsquedas disponibles para el seeker.</td>
     * </tr>
     *
     * </table>
     */
    public void notifyAvailableSearchPrinciples(SearchEvent evt);
}
