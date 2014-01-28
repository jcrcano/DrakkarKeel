/*
 * DrakkarKeel - An Enterprise Collaborative Search Platform
 *
 * The contents of this file are subject under the terms described in the
 * DRAKKARKEEL_LICENSE file included in this distribution; you may not use this
 * file except in compliance with the License. 
 *
 * 2013-2014 DrakkarKeel Platform.
 */
package drakkar.stern.tracker.persistent.tables;

/**
 * Clase encargada de capturar las excepciones de Derby
 */
public class DriverException extends Exception {
    public DriverException(){
        super("Error while attempting to register the JDBC driver.\nPossible causes:"+"\n" +
                "* Class not found in the JDBC driver\n"+
                "* The driver could not be instantiated\n"+
                "* Illegal or incorrect access to the Database");
    }
    
    /**
     *
     * @param message
     */
    public DriverException(String message){
        super(message);
    }
}