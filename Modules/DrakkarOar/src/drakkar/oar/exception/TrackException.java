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

public class TrackException extends Exception {

    /**
     * Creates a new instance of <code>TrackException</code> without detail message.
     */
    public TrackException() {
    }


    /**
     * Constructs an instance of <code>TrackException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public TrackException(String msg) {
        super(msg);
    }
}
