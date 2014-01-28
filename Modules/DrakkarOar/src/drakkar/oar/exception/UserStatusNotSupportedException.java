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
 * Esta excepción es lanzada cuando se asigna un usuario un estado no soportado
 *
 * 
 */
public class UserStatusNotSupportedException extends Exception {

    /**
     * Creates a new instance of <code>UserStatusNotSupportedException</code> without detail message.
     */
    public UserStatusNotSupportedException() {
    }


    /**
     * Constructs an instance of <code>UserStatusNotSupportedException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public UserStatusNotSupportedException(String msg) {
        super(msg);
    }
}
