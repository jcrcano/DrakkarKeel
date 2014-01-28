/*
 * DrakkarKeel - An Enterprise Collaborative Search Platform
 *
 * The contents of this file are subject under the terms described in the
 * DRAKKARKEEL_LICENSE file included in this distribution; you may not use this
 * file except in compliance with the License. 
 *
 * 2013-2014 DrakkarKeel Platform.
 */
package drakkar.stern.servant;

import Ice.Current;
import Ice.ObjectPrx;
import drakkar.oar.Communication;
import drakkar.oar.Seeker;
import drakkar.oar.exception.SeekerException;
import drakkar.oar.exception.SessionException;
import drakkar.oar.slice.client.ClientSidePrx;
import drakkar.oar.slice.login._RoleDisp;
import drakkar.oar.slice.server.ServerSide;
import drakkar.oar.slice.server.ServerSidePrx;
import drakkar.oar.slice.server.ServerSidePrxHelper;
import drakkar.oar.slice.server.ServerSidePrxHolder;
import drakkar.oar.util.OutputMonitor;
import drakkar.stern.controller.ServerSideController;
import java.io.IOException;

/**
 * Esta clase constitye el sirviente  del objeto ice Configuration, por lo cual
 * implementa los métodos definidos en esta interfaz, los cuales tienen objetivo
 * brindar a los desarrolladores operaciones que le permitan la administración
 * del servidor.
 */
public class RoleServant extends _RoleDisp {

    private Communication communication;
    private Servant servant;
    private ServerSidePrx serverSidePrx;
    private String session;

    /**
     *
     * @param comm
     * @param server
     * @param sessionName
     */
    public RoleServant(Communication comm, Servant server, String sessionName) {
        this.communication = comm;
        this.servant = server;
        this.session = sessionName;
    }

    /**
     * 
     * @param seeker
     * @param seekerPrx
     * @param serverHolder
     * @param current
     */
    public synchronized void login(Seeker seeker, ClientSidePrx seekerPrx, ServerSidePrxHolder serverHolder, Current current) {
        try {
            ServerSide serverSide = new ServerSideController(this.servant, seeker, seekerPrx, this.session);
            ObjectPrx proxy = this.communication.getAdapter().addWithUUID(serverSide);
           
            serverSidePrx = ServerSidePrxHelper.uncheckedCast(proxy);
            serverHolder.value = serverSidePrx;
        } catch (SessionException ex) {
            OutputMonitor.printStream("Session", ex);
        } catch (SeekerException ex) {
            OutputMonitor.printStream("Seeker", ex);
        } catch (IOException ex) {
            OutputMonitor.printStream("IO", ex);
        }
    }

    public synchronized void disconnect(Current __current) {
       
    }
}
