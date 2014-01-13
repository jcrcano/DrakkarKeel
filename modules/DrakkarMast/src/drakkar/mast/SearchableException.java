/*
 * DrakkarKeel - An Enterprise Collaborative Search Platform
 *
 * The contents of this file are subject under the terms described in the
 * DRAKKARKEEL_LICENSE file included in this distribution; you may not use this
 * file except in compliance with the License. 
 *
 * 2013-2014 DrakkarKeel Platform.
 */
package drakkar.mast;

/**
 * Esta excepción es lanzada cuando ocurre algún error con un buscador
 *
 * 
 */
public class SearchableException extends Exception {

    /**
     * Creates a new instance of <code>SearchableException</code> without detail message.
     */
    public SearchableException() {
    }

    /**
     * Constructs an instance of <code>SearchableException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public SearchableException(String msg) {
        super(msg);
    }
}
