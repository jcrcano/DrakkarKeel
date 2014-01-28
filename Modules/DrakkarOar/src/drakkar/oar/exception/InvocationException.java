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
 * Esta excepción ocurre cuando intenta realizar un tipo de invocación no soportada
 * por el servidor
 *
 * 
 */
public class InvocationException extends Exception {

    /**
     * Creates a new instance of <code>InvocationException</code> without detail message.
     */
    public InvocationException() {
    }

    /**
     * Constructs an instance of <code>InvocationException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public InvocationException(String msg) {
        super(msg);
    }
}
