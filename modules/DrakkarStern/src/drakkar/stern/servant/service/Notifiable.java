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
import drakkar.oar.exception.NotificationException;
import drakkar.oar.exception.ScoreOutOfBoundsException;
import drakkar.oar.exception.SessionException;
import java.io.IOException;

/**
 * The <code>ResponseUtilFactory</code> class is.....
 * Esta interfaz declara los diferentes metodos empleados para notificar al servidor
 * los documentos revisados, evaluados y comentados
 */
public interface Notifiable {

    /**
     * Notifica al servidor el documento revisado, obtenido por un buscador determinado
     *
     * @param sessionName    nombre de la sesión
     * @param seeker         usuario que realiza la revisión.
     * @param query          consulta de la búsqueda
     * @param searchable     buscador
     * @param docIndex       índice del documento revisado
     *
     * @param uri
     * @throws SessionException  si la sesión no se encuentra registrada
     *                                   en el servidor
     * @throws NotificationException     si se invoca una actualización de estado
     *                                   usuario, y este no aparece registrado en
     *                                   sesión
     */
    public void notifyDocumentViewed(String sessionName, Seeker seeker, String query, int searchable, int docIndex, String uri) throws SessionException, NotificationException;

    /**
     * Notifica al servidor la evaluación de un documento revisado, obtenido por
     * un buscador determinado
     *
     * @param sessionName   nombre de la sesión
     * @param seeker        usuario que realiza la evaluación
     * @param query         consulta de la búsqueda
     * @param searchable    buscador
     * @param docIndex      índice del documento revisado
     * @param uri 
     * @param score         calificación dada al documento por su relevancia
     * @param source        fuente del documento: caché, BD, recomendación
     *
     * @throws SessionException   si la sesión no se encuentra registrada
     *                                    en el servidor
     * @throws NotificationException      si se invoca una actualización de estado
     *                                    usuario, y este no aparece registrado en
     *                                    sesión
     * @throws ScoreOutOfBoundsException  si el usuario asigna una puntuación al
     *                                    documento fuera del rango de valores
     *                                    establecidos
     * @throws IOException   si ocurre alguna excepción durante el proceso de
     *                       serialización del objeto Response
     */
    public void notifyDocumentEvaluated(String sessionName, Seeker seeker, String query, int searchable, int docIndex,String uri, byte score, int source) throws SessionException, NotificationException, ScoreOutOfBoundsException, IOException;

    /**
     * Notifica al servidor un comentario sobre un documento revisado, obtenido por
     * un buscador determinado
     *
     * @param sessionName    nombre de la sesión
     * @param seeker         usuario que realiza el comentario.
     * @param query          consulta de la búsqueda
     * @param searchable     buscador
     * @param docIndex       índice del documento revisado
     * @param uri 
     * @param comment        comentario del documento
     *
     * @param source         fuente del documento: caché, BD, recomendación
     * @throws SessionException  si la sesión no se encuentra registrada
     *                                   en el servidor
     * @throws NotificationException     si se invoca una actualización de estado
     *                                   usuario, y este no aparece registrado en
     *                                   sesión
     * @throws IOException   si ocurre alguna excepción durante el proceso de
     *                       serialización del objeto Response
     */
    public void notifyDocumentCommented(String sessionName, Seeker seeker, String query, int searchable, int docIndex, String uri, String comment,int source) throws SessionException, NotificationException, IOException;

    
}
