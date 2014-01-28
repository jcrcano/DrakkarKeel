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

public interface SeekerListener extends FacadeDesktopListener{

     /**
     * Notifica la acción del usuario en una sesión de comunicación (entrada, salida
     * y actualización del su profile).
     *
     * @param evt instancia de SessionEvent
     */
    public void notifySeekerEvent(SeekerEvent evt);
   
    /**
     * Notifica la respuesta de petición de conexión de un usuario a la sesión de comunicación
     *
     * @param evt
     */
    public void notifyRequestConnection(SeekerEvent evt);

     /**
     * Notifica el estado actual del servidor
     *
     * @param evt  instancia de ServerEvent
     */
    public void notifyServerState(ServerEvent evt);

    /**
     * Notifica si la acción ejecutada pudo ser completada, en caso contrario la
     * o las causas de la misma
     *
     * @param evt instancia de TransactionEvent
     */
    public void notifyCommitTransaction(TransactionEvent evt);

}
