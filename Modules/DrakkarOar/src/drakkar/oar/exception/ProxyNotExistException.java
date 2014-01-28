/*
 * DrakkarKeel - An Enterprise Collaborative Search Platform
 *
 * The contents of this file are subject under the terms described in the
 * DRAKKARKEEL_LICENSE file included in this distribution; you may not use this
 * file except in compliance with the License. 
 *
 * 2013-2014 DrakkarKeel Platform.
 */
package drakkar.oar.exception;

/**
 * Esta excepxión es lanzada cuando se intenta convertir un ObjectPrx a un proxy
 * en específico y este no está realmente asociado a esta interfaz.
 * Ej:
 *      ObjectPrx objPrx = .....// se obtiene el proxy
 *      DemoPrx proxy = DemoPrxHelper.checkedCast(objPrx);
 *
 * 
 */
public class ProxyNotExistException extends Exception {

    /**
     * Creates a new instance of <code>ProxyNotExistException</code> without detail message.
     */
    public ProxyNotExistException() {
    }


    /**
     * Constructs an instance of <code>ProxyNotExistException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public ProxyNotExistException(String msg) {
        super(msg);
    }
}
