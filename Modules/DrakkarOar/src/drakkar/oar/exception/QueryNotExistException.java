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
 * Esta excepción es lanzada cuando se solicita algún tipo de información de una
 * consulta determinada realizada en una sesión de búsqueda y está no existe
 *
 * 
 */
public class QueryNotExistException extends Exception {

    /**
     * Creates a new instance of <code>QueryNotExistException</code> without detail message.
     */
    public QueryNotExistException() {
    }


    /**
     * Constructs an instance of <code>QueryNotExistException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public QueryNotExistException(String msg) {
        super(msg);
    }
}
