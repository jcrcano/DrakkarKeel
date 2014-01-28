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
 * Esta excepción es lanzada cuando ocurre algún error el proceso de recomendación
 *
 * 
 */
public class RecommendationException extends Exception {

    /**
     * Creates a new instance of <code>RecommendationException</code> without detail message.
     */
    public RecommendationException() {
    }


    /**
     * Constructs an instance of <code>RecommendationException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public RecommendationException(String msg) {
        super(msg);
    }
}
