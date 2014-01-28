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
 * Esta excepción es lanzada cuando ocurre alguna violación de las propiedades de
 * la sesión
 *
 * 
 */
public class SessionPropertyException extends Exception {

    /**
     * Creates a new instance of <code>SessionPropertyException</code> without detail message.
     */
    public SessionPropertyException() {
    }


    /**
     * Constructs an instance of <code>SessionPropertyException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public SessionPropertyException(String msg) {
        super(msg);
    }
}
