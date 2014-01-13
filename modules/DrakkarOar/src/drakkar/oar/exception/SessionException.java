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
 * Esta excepción es lanzada cuando se invoca una operación de una sesión no
 * existente
 *
 */
public class SessionException extends Exception {

    /**
     * Creates a new instance of <code>SessionException</code> without detail message.
     */
    public SessionException() {
    }


    /**
     * Constructs an instance of <code>SessionException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public SessionException(String msg) {
        super(msg);
    }
}
