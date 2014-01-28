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
 * Esta excepción es lanzada cuando se produce algún error en el proceso de búsqueda
 *
 * 
 */
public class SearchException extends Exception {

    /**
     * Creates a new instance of <code>SearchException</code> without detail message.
     */
    public SearchException() {
    }


    /**
     * Constructs an instance of <code>SearchException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public SearchException(String msg) {
        super(msg);
    }
}
