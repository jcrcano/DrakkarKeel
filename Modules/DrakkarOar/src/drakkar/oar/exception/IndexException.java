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
 * Esta excepción es lanzada cuando ocurre un error en el proceso de indexación
 *
 * 
 */
public class IndexException extends Exception {

    /**
     * Creates a new instance of <code>IndexException</code> without detail message.
     */
    public IndexException() {
    }


    /**
     * Constructs an instance of <code>IndexException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public IndexException(String msg) {
        super(msg);
    }
}
