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

import drakkar.oar.Response;
import drakkar.oar.facade.event.FacadeDesktopEvent;



/**
 * Esta clase contituye la superclase de todos los eventos que pueden arrojarse
 * en las aplicaciones servidora
 */
public class FacadeEvent extends FacadeDesktopEvent{

    /**
     * Constructor de la clase
     *
     * @param source   objeto
     * @param response instancia del objeto response,que contiene la información
     *                 del evento
     */
    public FacadeEvent(Object source, Response response) {
        super(source, response);
    }

    
       
}
