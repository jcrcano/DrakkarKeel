/*
 * DrakkarKeel - An Enterprise Collaborative Search Platform
 *
 * The contents of this file are subject under the terms described in the
 * DRAKKARKEEL_LICENSE file included in this distribution; you may not use this
 * file except in compliance with the License. 
 *
 * 2013-2014 DrakkarKeel Platform.
 */
package drakkar.prow.facade;

/**
 * Esta interface es la encargada de registrar todos los oyentes de la aplicación
 * cliente, para su posterior notificación al ocurrir algún tipo de notificación
 * por parte de la aplicación servidora
 */
public interface ListenerManager {

    /**
     * 
     */
    public void notifyFailedConnection();


}
