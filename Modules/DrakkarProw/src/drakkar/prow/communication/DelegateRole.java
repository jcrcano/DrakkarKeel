/*
 * DrakkarKeel - An Enterprise Collaborative Search Platform
 *
 * The contents of this file are subject under the terms described in the
 * DRAKKARKEEL_LICENSE file included in this distribution; you may not use this
 * file except in compliance with the License. 
 *
 * 2013-2014 DrakkarKeel Platform.
 */
package drakkar.prow.communication;

import drakkar.prow.ApplicationManager;
import drakkar.oar.Seeker;
import drakkar.oar.slice.error.RequestException;
import drakkar.oar.slice.login.RolePrx;
import drakkar.oar.util.OutputMonitor;
import java.io.Serializable;

/**
 * Esta clase tiene el objetivo de registrar un usuario(Seeker) en una sesión de
 * un servidor, registrando además del objeto Seeker, el objeto ClientSideProxy de
 * este miembro para la posterior notificación de las operaciones llevadas  a cabo
 * en el servidor.
 */
public class DelegateRole implements Serializable{
    private static final long serialVersionUID = 80000000000015L;

    private RolePrx rolePrx;

    /**
     * Constructor de la clase.
     *
     * @param rolePrx objeto RolePrx
     */
    public DelegateRole(RolePrx rolePrx) {
        this.rolePrx = rolePrx;
    }

    /**
     * Este método es el encargado de registrar un nuevo miembro en una sesión
     * determinada.
     *
     * @param seeker    instancia de la clase Seeker, que representa al nuevo usuario.
     * @param client contiene el objeto proxy ClientSidePrx, que representa al nuevo usuario en
     *                  el tiempo de ejecución de Ice.
     * @param serverPrx contiene el objeto ServerSidePrxHolder, servidor a conectarse.
     */
    public void login(Seeker seeker, ApplicationManager client, RequestDispatcher serverPrx) {
        try {
          
            this.rolePrx.login(seeker, client.getClientSidePrx(), serverPrx.getServerSidePrxHolder());
            
        } catch (RequestException ex) {
            OutputMonitor.printStream("", ex);
        }
    }

    /**
     * Este método devuelve la instancia de RolePrx de la clase.
     * 
     * @return instancia de RolePrx
     */
    public RolePrx getRolePrx() {
        return rolePrx;
    }

    /**
     * Este método reemplaza el objeto RolePrx de la clase.
     *
     * @param rolePrx nueva instancia de RolePrx.
     */
    public void setRolePrx(RolePrx rolePrx) {
        this.rolePrx = rolePrx;
    }
}
