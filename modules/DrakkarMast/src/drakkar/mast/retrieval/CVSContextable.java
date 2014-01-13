/*
 * DrakkarKeel - An Enterprise Collaborative Search Platform
 *
 * The contents of this file are subject under the terms described in the
 * DRAKKARKEEL_LICENSE file included in this distribution; you may not use this
 * file except in compliance with the License. 
 *
 * 2013-2014 DrakkarKeel Platform.
 */
package drakkar.mast.retrieval;

import drakkar.oar.DocumentMetaData;
import drakkar.oar.Response;
import drakkar.oar.facade.event.FacadeDesktopEvent;
import drakkar.oar.facade.event.FacadeDesktopListener;
import static drakkar.oar.util.KeyTransaction.*;
import drakkar.oar.util.NotifyAction;
import drakkar.mast.IndexException;
import drakkar.mast.SearchException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Clase abstracta para los CVS
 *
 */
public abstract class CVSContextable implements Contextable {

    protected FacadeDesktopListener listener;
    /**
     *
     */
    public String defaultIndexPath;

    /**
     *
     */
    public CVSContextable() {
    }

    /**
     *
     * @param listener
     */
    public CVSContextable(FacadeDesktopListener listener) {
        this.listener = listener;
    }

    /**
     * Crea un índice del repositorio SVN en la dirección por defecto
     *
     * @return cantidad de documentos indexados
     *
     * @throws IndexException si ocurre algún error en el proceso de indexación
     */
    public abstract long makeIndex() throws IndexException;

    /**
     * Carga el índice que se encuentra en la ubicación por defecto
     *
     * @return true si cargó el índice de la dirección especificada, false en caso contrario
     *
     * @throws IndexException si ocurre algún error en el proceso de carga del indice
     * @throws IOException
     */
    public abstract boolean loadIndex() throws IndexException, IOException;

    /**
     * Carga el índice que se encuentra en la ubicación especificada
     *
     * @param indexPath dirección del índice
     *
     * @return true si cargó el índice de la dirección especificada, false en caso contrario
     *
     * @throws IndexException si ocurre algún error en el proceso de carga del indice
     * @throws IOException 
     */
    public abstract boolean loadIndex(File indexPath) throws IndexException, IOException;

    /**
     * Ejecuta una búsqueda a partir de los parámetros de entrada
     *
     * @param query             consulta
     *
     * @param sort 
     * @param fileType
     * @param date
     * @param user
     * @param fileBody
     * @return                  resultados de la búsqueda
     *
     * @throws SearchException  si ocurre alguna excepción durante el proceso de búsqueda
     */
    public abstract ArrayList<DocumentMetaData> search(String query, String sort, String fileType, String date, String user, boolean fileBody) throws SearchException;

    /////////////////////////////////////////////
    /**
     * Este método notifica al servidor el progreso de las actividades invocadas
     * para actualizar el tablón de Log y Monitor.
     *
     * @param messageType   tipo de mensage:<tt>INFORMATION_MESSAGE,ERROR_MESSAGE</tt>
     * @param message       contenido del mensaje
     */
    public void notifyTaskProgress(int messageType, String message) {
        if (listener != null) {
            Response rs = new Response();
            rs.put(OPERATION, NotifyAction.NOTIFY_TEXT_MESSAGE);
            rs.put(MESSAGE_TYPE, messageType);
            rs.put(MESSAGE, message);
            FacadeDesktopEvent evt = new FacadeDesktopEvent(this, rs);
            listener.notify(evt);
        }


    }

    /**
     * Este método notifica al servidor el progreso de la indexación de documentos
     * @param count
     */
    public void notifyLoadedDocument(int count) {
        if (listener != null) {
            Response rs = new Response();
            rs.put(OPERATION, NotifyAction.NOTIFY_LOADED_DOCUMENT);
            rs.put(VALUE, count);
            FacadeDesktopEvent evt = new FacadeDesktopEvent(this, rs);
            listener.notify(evt);
        }
    }

    /**
     *
     * @param count
     */
    public void notifyIndexedDocument(int count) {
        if (listener != null) {
            Response rs = new Response();
            rs.put(OPERATION, NotifyAction.NOTIFY_INDEXED_DOCUMENT_COUNT);
            rs.put(VALUE, count);
            FacadeDesktopEvent evt = new FacadeDesktopEvent(this, rs);
            listener.notify(evt);
        }
    }
}
