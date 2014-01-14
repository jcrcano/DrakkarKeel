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
 * Esta excepción es lanzada cuando un usuario trata de registrarse y ya se encuentra
 * registrado otro usuario con el mismo identificador
 *
 * 
 */
public class UserAlreadyRegisterException extends Exception {

    /**
     * Creates a new instance of <code>UserAlreadyRegisterException</code> without detail message.
     */
    public UserAlreadyRegisterException() {
    }


    /**
     * Constructs an instance of <code>UserAlreadyRegisterException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public UserAlreadyRegisterException(String msg) {
        super(msg);
    }
}
