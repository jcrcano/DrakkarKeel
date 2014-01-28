/*
 * DrakkarKeel - An Enterprise Collaborative Search Platform
 *
 * The contents of this file are subject under the terms described in the
 * DRAKKARKEEL_LICENSE file included in this distribution; you may not use this
 * file except in compliance with the License. 
 *
 * 2013-2014 DrakkarKeel Platform.
 */
package drakkar.oar.facade.event;

import java.util.EventListener;



/**
 * Esta interfaz representa la raíz de todos oyentes de las notificaciones desktop,
 * que son invocadas por el servidor de la aplicación
 */
public interface FacadeDesktopListener extends EventListener{

    /**
     * Notifica al oyente FacadeListener registrado en la aplicación del cliente
     * cualquier tipo de evento que tenga como superclase a DesktopEvent
     *
     * Este método representa la opción que brinda el sistema de eventos de DrakkarKeel
     * para extender las posibilidades de notificación de los tipos de eventos
     * definidos por el usuario
     *
     * @param evt  evento
     */
    public void notify(FacadeDesktopEvent evt);


    /**
     *
     * @param evt
     *
     * @deprecated 
     */
   // public void notify(DesktopEvent evt);
}
