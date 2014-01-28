/*
 * DrakkarKeel - An Enterprise Collaborative Search Platform
 *
 * The contents of this file are subject under the terms described in the
 * DRAKKARKEEL_LICENSE file included in this distribution; you may not use this
 * file except in compliance with the License. 
 *
 * 2013-2014 DrakkarKeel Platform.
 */
package drakkar.mast.exception;

/**
 * Esta excepción es lanzada cuando se invoca una operación para un buscador no
 * soportado
 *
 * 
 */
public class SearchableNotSupportedException extends Exception {

    /**
     * Creates a new instance of <code>SearchableNotSupportedException</code> without detail message.
     */
    public SearchableNotSupportedException() {
    }

    /**
     * Constructs an instance of <code>SearchableNotSupportedException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public SearchableNotSupportedException(String msg) {
        super(msg);
    }
}
