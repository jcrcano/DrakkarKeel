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
 * Esta excepción es la lanazada cuando se invoca una operación no soportada
 *
 * 
 */
public class OperationNotExistException extends Exception {

    /**
     * Creates a new instance of <code>OperationNotExistException</code> without detail message.
     */
    public OperationNotExistException() {
    }


    /**
     * Constructs an instance of <code>OperationNotExistException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public OperationNotExistException(String msg) {
        super(msg);
    }
}
