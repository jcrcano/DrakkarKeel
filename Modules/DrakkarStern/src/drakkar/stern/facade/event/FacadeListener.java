/*
 * DrakkarKeel - An Enterprise Collaborative Search Platform
 *
 * The contents of this file are subject under the terms described in the
 * DRAKKARKEEL_LICENSE file included in this distribution; you may not use this
 * file except in compliance with the License. 
 *
 * 2013-2014 DrakkarKeel Platform.
 */
package drakkar.stern.facade.event;


import drakkar.oar.facade.event.FacadeDesktopListener;

/**
 * Esta interfaz define los diferentes métodos de notificación de las operaciones
 * soportadas por DrakkarKeel para  las aplicaciones seervidoras
 */
public interface FacadeListener extends FacadeDesktopListener {

    
    
    /**
     * Notifica a la aplicación del servidor la realización de una nueva búsqueda
     */
    public void notifyDoneSearch();

    /**
     * Notifica a la aplicación del servidor el número de documentos encontrados
     * para una búsqueda
     */
    public void notifyFoundDocument();

    /**
     * Notifica a la aplicación del servidor el envió de un nuevo memsaje
     */
    public void notifySentMessage();

    /**
     * Notifica a la aplicación del servidor la realización de una nueva recomendación
     */
    public void notifyDoneRecommendation();


}
