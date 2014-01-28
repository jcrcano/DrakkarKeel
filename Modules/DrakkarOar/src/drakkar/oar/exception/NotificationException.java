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
 * Esta excepción es lanzada cuando un se invoca una notificación al servidor,
 * y la sesión ó el miembro no se encuentra registrado, para soportar dicha
 * invocación
 *
 * 
 */
public class NotificationException extends Exception {

    /**
     * Creates a new instance of <code>NotificationException</code> without detail message.
     */
    public NotificationException() {
    }


    /**
     * Constructs an instance of <code>NotificationException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public NotificationException(String msg) {
        super(msg);
    }
}
