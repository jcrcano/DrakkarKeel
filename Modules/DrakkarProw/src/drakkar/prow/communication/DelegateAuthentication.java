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

import drakkar.oar.Communication;
import drakkar.oar.Delegate;
import drakkar.oar.Request;
import drakkar.oar.Seeker;
import drakkar.oar.exception.ProxyNotExistException;
import drakkar.oar.slice.error.RequestException;
import drakkar.oar.slice.login.AuthenticationPrx;
import drakkar.oar.slice.login.AuthenticationPrxHelper;
import java.io.IOException;
import java.io.Serializable;


/**
 * Esta clase tiene el objetivo de crear un objeto proxy(AuthenticationPrx)
 * para que actúe como embajador del respectivo objeto registrado en el servidor,
 * pudiendo así ejecutar las operaciones soportadas por el mismo.
 */
public class DelegateAuthentication extends Delegate implements Serializable{
    private static final long serialVersionUID = 80000000000014L;

    private AuthenticationPrx authentPrx;

    /**
     * Constructor de la Clase
     *
     * @param comm      instancia de Communication
     * @param name      nombre del servidor
     * @param ip        dirección host
     * @param porNumber puerto por el cual el servidor recibe las peticiones
     */
    public DelegateAuthentication(Communication comm, String name, String ip, int porNumber) {
        super(comm, name, ip, porNumber);
    }
    

    /**
     * Constructor de la Clase.
     *
     * @param comm  instancia de Communication.
     * @param name  nombre del servidor
     * @param ip    dirección host
     * @param porNumber  puerto por el cual el servidor recibe las peticiones.
     * @param authentPrx
     */
    public DelegateAuthentication(Communication comm, String name, String ip, int porNumber, AuthenticationPrx authentPrx) {
        super(comm, name, ip, porNumber);
        this.authentPrx = authentPrx;
        this.proxy = authentPrx;
    }

    /**
     * Este método crea una instancia del objeto proxy remoto AuthenticationPrx.
     * @throws ProxyNotExistException 
     */
    public void create() throws ProxyNotExistException {

        this.proxy = this.communication.getCommunicator().stringToProxy(this.name
                + ":tcp -h " + this.ip + " -p " + this.portNumber);

        this.authentPrx = AuthenticationPrxHelper.checkedCast(this.proxy);

        if (this.authentPrx == null) {
            throw new ProxyNotExistException("Invalid Proxy");
        }
    }

    /**
     * Este método reemplaza el objeto AuthenticationPrx de la clase.
     *
     * @param authentPrx  nuevo objeto AuthenticationPrx.
     */
    public void setAuthenticationPrx(AuthenticationPrx authentPrx) {
        this.authentPrx = authentPrx;
    }

    /**
     * Este método devuelve el objeto AuthenticationPrx de la clase.
     * 
     * @return  instancia de AuthenticationPrx.
     *
     */
    public AuthenticationPrx getAuthenticationPrx() {
        return this.authentPrx;
    }

//  Antes
//  public Seeker loginByPassword(String name,String passw)
//    public Seeker loginByName(String name)
    /**
     * Este método es utilizado para registrar el administrador del servidor
     * tanto por nombre, como por nombre y contraseña.
     *
     * @param request  Parámetros de la operación.
     *
     * @return Seeker
     *
     * @throws RequestException
     */
    public Seeker login(Request request) throws RequestException, IOException {
        return this.authentPrx.login(request.toArray());

    }

    /**
     * Este método es utilizado para deslogear al miembro del administrador del servidor
     *
     * @param member  miembro a deslogear
     *
     * @return true si puede deslogear al miembro, false en caso contrario.
     * @throws RequestException
     */
    public boolean logout(Seeker member) throws drakkar.oar.slice.error.RequestException {

        return this.authentPrx.logout(member);

    }
}
