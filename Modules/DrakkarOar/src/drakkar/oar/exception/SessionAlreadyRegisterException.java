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
 * Esta sesión es lanzada cuando se intenta registrar una nueva sesión colaborativa
 * de búsqueda y existe otra con el mismo identificador ya registrada
 *
 * 
 */
public class SessionAlreadyRegisterException extends Exception {

    /**
     * Creates a new instance of <code>SessionAlreadyRegisterException</code> without detail message.
     */
    public SessionAlreadyRegisterException() {
    }


    /**
     * Constructs an instance of <code>SessionAlreadyRegisterException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public SessionAlreadyRegisterException(String msg) {
        super(msg);
    }
}
