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
 * Esta excepción es lanzada cuando se invoca o se envía una operación, por o a
 * un usuario que no existe, ó cuando se invoca una operación sin los privelegios
 * determinados
 * 
 * 
 */
public class SeekerException extends Exception {

    /**
     * Creates a new instance of <code>UserNotExistException</code> without detail message.
     */
    public SeekerException() {
    }


    /**
     * Constructs an instance of <code>UserNotExistException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public SeekerException(String msg) {
        super(msg);
    }
}
