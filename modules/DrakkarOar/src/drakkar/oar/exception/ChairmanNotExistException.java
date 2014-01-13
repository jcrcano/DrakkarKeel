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
 * Esta excepción es lanzada cuando un se invoca una operación por un jefe de sesión
 * que no existe
 *
 * 
 */
public class ChairmanNotExistException extends Exception {

    /**
     * Creates a new instance of <code>ChairmanNotExistException</code> without detail message.
     */
    public ChairmanNotExistException() {
    }


    /**
     * Constructs an instance of <code>ChairmanNotExistException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public ChairmanNotExistException(String msg) {
        super(msg);
    }
}
