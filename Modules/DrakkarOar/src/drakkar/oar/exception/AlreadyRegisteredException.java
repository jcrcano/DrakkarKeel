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
 * Esta excepción es lanzada si el usuario invoca la evaluación de un documento
 * ya evaluado
 *
 * 
 */
public class AlreadyRegisteredException extends Exception {

    /**
     * Creates a new instance of <code>AlreadyRegisteredException</code> without detail message.
     */
    public AlreadyRegisteredException() {
    }


    /**
     * Constructs an instance of <code>AlreadyRegisteredException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public AlreadyRegisteredException(String msg) {
        super(msg);
    }
}
