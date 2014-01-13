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
import java.util.HashMap;
import java.util.Map;

/**
 * Esta clase almacena el valor total de acciones efectuadas por un usuario para
 * una determinada operación
 */
public class Count {

    /**
     * Esta tabla hash almacena el número acciones efectuadas por un usuario para
     * una determinada operación 
     */
    public Map<Seeker, Integer> record;

    /**
     * Constructor de la clase
     */
    public Count() {
        this.record = new HashMap<>();
    }
}
