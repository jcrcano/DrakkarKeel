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

import Ice.ObjectPrx;
import drakkar.oar.Communication;
import drakkar.oar.Delegate;
import drakkar.oar.exception.ProxyNotExistException;
import drakkar.oar.slice.container.SessionContainerPrx;
import drakkar.oar.slice.container.SessionContainerPrxHelper;
import drakkar.oar.slice.management.ServerManagerPrx;
import drakkar.oar.slice.management.ServerManagerPrxHelper;
import java.io.Serializable;

/**
 * Esta clase tiene el objetivo de crear objetos proxy(ServerManagerPrx,SessionContainerPrx)
 * para que actuen como embajadores de los respectivos objetos registrados en el tiempo
 * de ejecución de ICE, pudiendose así ejecutar las operaciones soportadas por estos.
  */
public class DelegateServerManager extends Delegate implements Serializable{
    private static final long serialVersionUID = 80000000000016L;

    private ServerManagerPrx serverManagerPrx;

    /**
     * Constructor de la Clase.
     *
     * @param comm       instancia de Communication.
     * @param name       nombre del servidor
     * @param ip         dirección host
     * @param porNumber  puerto por el cual el servidor recibe las peticiones.
     */
    public DelegateServerManager(Communication comm, String name, String ip, int porNumber) {
        super(comm, name, ip, porNumber);
    }

    /**
     * Este método obtiene un proxy del objeto remoto(ServerSidePrx) para que
     * actué este actué como embajador del correspondiente objeto registrado en
     * el tiempo de ejecución de Ice.
     * @throws ProxyNotExistException
     */
    public void create() throws ProxyNotExistException {

        // stringToProxy: obtiene un proxy del objeto remoto
        this.proxy = this.communication.getCommunicator().stringToProxy(this.name + ":tcp -h " + this.ip + " -p " + this.portNumber);
        // convierte el ObjectPrx en un ServerSidePrx, la cual envía un mensaje
        // al servidor para comprobar que realmente está asociado a la interfaz
        // ServerSidePrx
        if (this.proxy == null) {
            throw new ProxyNotExistException("Invalid Proxy. Object Proxy " + name + " not found.");
        }

        try {
            this.serverManagerPrx = ServerManagerPrxHelper.checkedCast(this.proxy);
        } catch (Exception e) {
            throw new ProxyNotExistException("Invalid Proxy. Proxy miss type.");
        }


    }

    /**
     *
     * @throws ProxyNotExistException
     */
    public void createMultiListener() throws ProxyNotExistException {

        // stringToProxy: obtiene un proxy del objeto remoto
        this.proxy = this.communication.getCommunicator().stringToProxy(this.name + ":tcp -p " + this.portNumber);
        // convierte el ObjectPrx en un ServerSidePrx, la cual envía un mensaje
        // al servidor para comprobar que realmente está asociado a la interfaz
        // ServerSidePrx
        try {
            this.serverManagerPrx = ServerManagerPrxHelper.checkedCast(this.proxy);
        } catch (Exception e) {
            throw new ProxyNotExistException("Invalid Proxy");
        }
    }

    /**
     * Este método devuelve la instancia el objeto DelegateSessionContainer, apartir del
     * nombre pasado por parámetro.
     *
     * @param name  nombre del contenedor de la sessión.
     *
     * @return instancia de DelegateSessionContainer
     * @throws ProxyNotExistException
     */
    public DelegateSessionContainer getDlgSessionContainer(String name) throws ProxyNotExistException {

        ObjectPrx objectPrx = this.communication.getCommunicator().stringToProxy(name + ":tcp -h " + this.ip + " -p " + this.portNumber);

        SessionContainerPrx containerPrx = SessionContainerPrxHelper.checkedCast(objectPrx);

        if (containerPrx == null) {
            throw new ProxyNotExistException("Invalid Proxy");
        } else {
            DelegateSessionContainer container = new DelegateSessionContainer(this.communication, name, this.ip, this.portNumber, containerPrx);
            return container;
        }
    }

    /**
     *
     * @param name
     * @return
     * @throws ProxyNotExistException
     */
    public DelegateSessionContainer getDlgSessionContainerMultiListener(String name) throws ProxyNotExistException {

        ObjectPrx objectPrx = this.communication.getCommunicator().stringToProxy(name + ":default -p " + this.portNumber);

        SessionContainerPrx containerPrx = SessionContainerPrxHelper.checkedCast(objectPrx);

        if (containerPrx == null) {
            throw new ProxyNotExistException("Invalid Proxy");
        } else {
            DelegateSessionContainer container = new DelegateSessionContainer(this.communication, name, this.ip, this.portNumber, containerPrx);
            return container;
        }
    }

    /**
     * Este método devuelve el objeto ServerSidePrx de la clase.
     *
     * @return instancia de ServerSidePrx.
     */
    public ServerManagerPrx getServerManagerPrx() {
        return serverManagerPrx;
    }

    /**
     * Este método reemplaza el objeto ServerSidePrx de la clase.
     * 
     * @param serverSidePrx  nuevo ServerSidePrx.
     */
    public void setServerManagerPrx(ServerManagerPrx serverSidePrx) {
        this.serverManagerPrx = serverSidePrx;
    }
}
