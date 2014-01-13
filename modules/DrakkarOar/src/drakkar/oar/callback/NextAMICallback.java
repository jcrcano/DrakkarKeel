/*
 * DrakkarKeel - An Enterprise Collaborative Search Platform
 *
 * The contents of this file are subject under the terms described in the
 * DRAKKARKEEL_LICENSE file included in this distribution; you may not use this
 * file except in compliance with the License. 
 *
 * 2013-2014 DrakkarKeel Platform.
 */
package drakkar.oar.callback;

import Ice.LocalException;
import drakkar.oar.slice.transfer.AMI_File_next;

/**
 * Esta clase representa un instancia del objeto de retrollamada, de las invocaciones
 * asíncronas para la lectura de un fichero
 *
 * 
 *
 */
public class NextAMICallback extends AMI_File_next {

    byte[] readBytes = null;
    
    public NextAMICallback() {
        
    }

    public synchronized void ice_response(byte[] ret) {
        readBytes = ret;
        this.notifyAll();
    }

    public void ice_exception(Exception ex) {
        ex.printStackTrace();
    }

    @Override
    public void ice_exception(LocalException ex) {
        ex.printStackTrace();
    }

    /**
     *
     * @return
     */
    int count = 0;
    public synchronized byte[] getData() {
        count++;
        while (readBytes == null) {
            try {
                wait();
            } catch (java.lang.InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        byte[] results = readBytes.clone();
        readBytes = null;

        return results;
    }
 
  
}
