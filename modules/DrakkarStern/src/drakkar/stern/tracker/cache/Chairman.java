/*
 * DrakkarKeel - An Enterprise Collaborative Search Platform
 *
 * The contents of this file are subject under the terms described in the
 * DRAKKARKEEL_LICENSE file included in this distribution; you may not use this
 * file except in compliance with the License. 
 *
 * 2013-2014 DrakkarKeel Platform.
 */
package drakkar.stern.tracker.cache;

import drakkar.oar.Seeker;
import drakkar.oar.slice.client.ClientSidePrx;


/**
 * Esta clase almacena el objeto Seeker que se desempeña como Chairman de una sesión
 * colaborativa de búsqueda con su respectivo objeto proxy
 */
public class Chairman {
    private Seeker seeker;
    private ClientSidePrx seekerPrx;

    /**
     * Constructor de la clase
     *
     * @param seeker    jefe de la sesión
     * @param seekerPrx objeto proxy del jefe de la sesión
     */
    public Chairman(Seeker seeker, ClientSidePrx seekerPrx) {
        this.seeker = seeker;
        this.seekerPrx = seekerPrx;
    }

    /**
     * Devuelve el objeto Seeker del chairman
     *
     * @return objeto
     */
    public Seeker getSeeker() {
        return seeker;
    }

    /**
     * Modifica el valor del objeto Seeker del chairman
     *
     * @param seeker nuevo objeto
     */
    public void setSeeker(Seeker seeker) {
        this.seeker = seeker;
    }

    /**
     * Devuelve el objeto proxy del chairman
     *
     * @return proxy
     */
    public ClientSidePrx getClientSidePrx() {
        return seekerPrx;
    }

    /**
     * Modifica el objeto proxy del chairman
     *
     * @param seekerPrx nuevo proxy
     */
    public void setClientSidePrx(ClientSidePrx seekerPrx) {
        this.seekerPrx = seekerPrx;
    }


    /**
     * Devuelve le nombre del chairman
     *
     * @return nombre
     */
    public String getName() {
        return seeker.getUser();
    }

}
