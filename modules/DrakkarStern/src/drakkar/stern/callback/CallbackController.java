/*
 * DrakkarKeel - An Enterprise Collaborative Search Platform
 *
 * The contents of this file are subject under the terms described in the
 * DRAKKARKEEL_LICENSE file included in this distribution; you may not use this
 * file except in compliance with the License. 
 *
 * 2013-2014 DrakkarKeel Platform.
 */
package drakkar.stern.callback;

import drakkar.oar.Seeker;
import drakkar.stern.servant.SternServant;

public class CallbackController {


    private static CallbackController instance;
    private static boolean flag = true;
    private SternServant serverServant;


    private CallbackController() {
        instance = null;
    }

    /**
     *s
     * @return
     */
    public static CallbackController getInstance() {
        if (flag) {
            instance = new CallbackController();
            flag = false;
        } else {
        }
        return instance;
    }

  
    public void setServerServant(SternServant serverServant) {
        this.serverServant = serverServant;
    }

    /**
     * Informa al servidor el error ocurrido mientras se trataba de localizar
     * al objeto de retrollamada de seeker.
     *
     * @param seeker receptor de la notificacion
     */
    public void notifyConnectionRefused(Seeker seeker){
        serverServant.updateCommunicationSession(seeker);
    }


    @Override
    protected void finalize() throws Throwable {
        flag = true;
        super.finalize();
    }



}
