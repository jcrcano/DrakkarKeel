/*
 * DrakkarKeel - An Enterprise Collaborative Search Platform
 *
 * The contents of this file are subject under the terms described in the
 * DRAKKARKEEL_LICENSE file included in this distribution; you may not use this
 * file except in compliance with the License. 
 *
 * 2013-2014 DrakkarKeel Platform.
 */
package drakkar.oar.util;

/**
 * Esta clase contiene las constantes que representan los tipos de invocaciones
 * soportadas por el servidor para sus operaciones
 */
public class Invocation {

    /**
     * Synchronous Method Invocation
     */
    public static final byte SYNCHRONOUS_METHOD_INVOCATION = 0;
    /**
     * Asynchronous Method Invocation
     */
    public static final byte ASYNCHRONOUS_METHOD_INVOCATION = 1;
    /**
     * Asynchronous Method Dispatch
     */
    public static final byte ASYNCHRONOUS_METHOD_DISPATCH = 2;
    /**
     * Asynchronous Method Invocation and Dispatch
     */
    public static final byte ASYNCHRONOUS_METHOD_INVOCATION_DISPATCH = 3;
    /**
     * Synchronous Method Invocation and Dispatch
     */
    public static final byte SYNCHRONOUS_METHOD_INVOCATION_DISPATCH = 4;
}
