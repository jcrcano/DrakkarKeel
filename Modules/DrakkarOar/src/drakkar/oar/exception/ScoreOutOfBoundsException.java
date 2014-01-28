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
 * Esta excepción es lanzada cuando un usuario asigna una evaluación a un documento
 * fuera del rango de valores establecidos
 *
 * 
 */
public class ScoreOutOfBoundsException extends Exception {

    /**
     * Creates a new instance of <code>ScoreOutOfBoundsException</code> without detail message.
     */
    public ScoreOutOfBoundsException() {
    }

    /**
     * Constructs an instance of <code>ScoreOutOfBoundsException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public ScoreOutOfBoundsException(String msg) {
        super(msg);
    }
}
