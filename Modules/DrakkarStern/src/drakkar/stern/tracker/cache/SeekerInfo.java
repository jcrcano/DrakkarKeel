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
import java.util.HashMap;
import java.util.Map;

/**
 * Esta clase almacena la relación de usuarios con sus correspondientes objetos
 * proxies de la sesión
 */
public class SeekerInfo {

    /**
     * Esta tabla hash almacena los objetos Seeker y los correspondientes proxies
     * de todos los usuarios de la sesión de comunicación
     */
    public Map<Seeker,ClientSidePrx> record;

    /**
     * Constructor por defecto de la clae
     */
    public SeekerInfo() {
        this.record = new HashMap<>();
    }

}
