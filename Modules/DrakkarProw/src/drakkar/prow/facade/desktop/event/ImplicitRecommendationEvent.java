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


import drakkar.oar.Response;
import drakkar.oar.facade.event.FacadeDesktopEvent;

/**
 * Esta clase representa el evento mediante el cual se notificarán las recomendaciones
 * entre usuarios
 */
 
public class ImplicitRecommendationEvent extends FacadeDesktopEvent {

    /**
     * Constructor de la clase
     *
     * @param source   objeto
     * @param response instancia del objeto response,que contiene la información
     *                 del evento
     */
    public ImplicitRecommendationEvent(Object source, Response response) {
        super(source, response);
    }

   

}
