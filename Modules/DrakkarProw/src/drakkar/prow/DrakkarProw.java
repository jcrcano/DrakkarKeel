/*
 * DrakkarKeel - An Enterprise Collaborative Search Platform
 *
 * The contents of this file are subject under the terms described in the
 * DRAKKARKEEL_LICENSE file included in this distribution; you may not use this
 * file except in compliance with the License. 
 *
 * 2013-2014 DrakkarKeel Platform.
 */
package drakkar.prow;

import drakkar.prow.communication.ProwController;
import drakkar.prow.communication.NetworkController;
import drakkar.prow.facade.ListenerManager;
import drakkar.prow.facade.desktop.event.DefaultListenerManager;

import drakkar.oar.facade.event.FacadeDesktopListener;
import drakkar.oar.slice.error.RequestException;
import java.io.Serializable;

/**
 * Esta clase contiene los métodos que pueden emplear los clientes para invocar las
 * distintas operaciones soportadas por la aplicación servidora del Framework DrakkarKeel
 */
public class DrakkarProw extends ProwApplication implements Serializable{
    private static final long serialVersionUID = 80000000000002L;

    //@TODO Parse de la versión 1.1, pendiente de eliminar
    private ProwAppSetting clientAppSetting;
    private drakkar.prow.facade.desktop.event.ListenerManager listener;

    /**
     * Constructor de la clase
     */
    public DrakkarProw() {
        super();
        this.listenerManager = new DefaultListenerManager();
        this.netController = new ProwController((DefaultListenerManager) listenerManager);
    }

    /**
     * Contructor de la clase
     *
     * @param args argumentos para la incialización de la aplicación
     */
    public DrakkarProw(String[] args) {
        super(args);
        this.listenerManager = new DefaultListenerManager();
        this.netController = new ProwController((DefaultListenerManager) listenerManager);
        //@TODO Parse de la versión 1.1, pendiente de eliminar
        listener = new drakkar.prow.facade.desktop.event.ListenerManager();
    }

    /**
     * Contructor de la clase
     *
     * @param cnxServer     configuración del servidor
     */
    public DrakkarProw(Connection cnxServer) {
        super(cnxServer);
        this.listenerManager = new DefaultListenerManager();
        this.netController = new ProwController((DefaultListenerManager) listenerManager);
         //@TODO Parse de la versión 1.1, pendiente de eliminar
        listener = new drakkar.prow.facade.desktop.event.ListenerManager();
    }

    /**
     * Contructor de la clase
     *
     * @param cnxServer     configuración del servidor
     * @param client configuración del cliente
     */
    public DrakkarProw(Connection cnxServer, ProwSetting client) {
        super(cnxServer, client);
        this.listenerManager = new DefaultListenerManager();
        this.netController = new ProwController((DefaultListenerManager) listenerManager);
         //@TODO Parse de la versión 1.1, pendiente de eliminar
        listener = new drakkar.prow.facade.desktop.event.ListenerManager();
    }

    /**
     * Contructor de la clase
     *
     * @param cnxServer      configuración del servidor
     * @param clientSetting  configuración del cliente
     * @param propertiesPath ubicación del fichero de propiedades
     */
    public DrakkarProw(Connection cnxServer, ProwSetting clientSetting, String propertiesPath) {
        super(cnxServer, clientSetting, propertiesPath);
        this.listenerManager = new DefaultListenerManager();
        this.netController = new ProwController((DefaultListenerManager) listenerManager);
         //@TODO Parse de la versión 1.1, pendiente de eliminar
        listener = new drakkar.prow.facade.desktop.event.ListenerManager();
    }

    /**
     * Contructor de la clase
     *
     * @param cnxServer     configuración del servidor
     * @param clientSetting configuración del cliente
     * @param args          argumentos para la incialización de la aplicación
     */
    public DrakkarProw(Connection cnxServer, ProwSetting clientSetting, String[] args) {
        super(cnxServer, clientSetting, args);
        this.listenerManager = new DefaultListenerManager();
        this.netController = new ProwController((DefaultListenerManager) listenerManager);
         //@TODO Parse de la versión 1.1, pendiente de eliminar
        listener = new drakkar.prow.facade.desktop.event.ListenerManager();
    }

    /**
     * Contructor de la clase
     *
     * @param cnxServer     configuración del servidor
     * @param client configuración del cliente
     *
     * @deprecated As of DrakkarKeel version 1.1, replaced by
     * <code> DrakkarProw(Connection cnxServer, ProwSetting client)</code>.
     */
    public DrakkarProw(Connection cnxServer, ProwAppSetting client) {
        super(cnxServer);
        this.clientSetting = new ProwSetting(client.getSeeker(), client.getPortNumber());
        this.clientAppSetting = client;
        this.listenerManager = new DefaultListenerManager();
        this.netController = new ProwController((DefaultListenerManager) listenerManager);
         //@TODO Parse de la versión 1.1, pendiente de eliminar
        listener = new drakkar.prow.facade.desktop.event.ListenerManager();
    }

    /**
     * Contructor de la clase
     *
     * @param cnxServer     configuración del servidor
     * @param client configuración del cliente
     * @param args          argumentos para la incialización de la aplicación
     *
     * @deprecated As of DrakkarKeel version 1.1, replaced by
     * <code> DrakkarProw(Connection cnxServer, ProwSetting clientSetting, String[] args)</code>.
     */
    public DrakkarProw(Connection cnxServer, ProwAppSetting client, String[] args) {
        super(cnxServer);
        this.clientSetting = new ProwSetting(client.getSeeker(), client.getPortNumber());
        this.clientAppSetting = client;
        this.listenerManager = new DefaultListenerManager();
        this.netController = new ProwController((DefaultListenerManager) listenerManager);
        this.args = args;
        this.initValues(args);
         //@TODO Parse de la versión 1.1, pendiente de eliminar
        listener = new drakkar.prow.facade.desktop.event.ListenerManager();
    }

    @Override
    protected void activeCommunication() throws RequestException {
        //@TODO Parse de la versión 1.1, pendiente de eliminar
        if (listener != null) {
            DefaultListenerManager temp = (DefaultListenerManager)netController.getListenerManager();
            temp.addFacadeListeners(listener.getFacadeDesktopListener());
        }

        if (clientAppSetting != null) {
            this.clientSetting.setPortNumber(clientAppSetting.getPortNumber());
            this.clientSetting.setSeeker(clientAppSetting.getSeeker());
        }
     
        super.activeCommunication();
    }

    /**
     * Devuelve la instancia de ListenerManager
     *
     * @return obejto listenersManager
     * 
     */
    public drakkar.prow.facade.desktop.event.ListenerManager getClientListenerManager() {       
        DefaultListenerManager dfListener = (DefaultListenerManager) netController.getListenerManager();
        FacadeDesktopListener[] array = dfListener.getFacadeDesktopListener();
        listener.addFacadeListeners(array);
        return listener;
    }

    /**
     * Devuelve la instancia de ListenerManager
     *
     * @return obejto listenersManager
     *
     */
    public ListenerManager getListenerManager() {
        return listenerManager;
    }

    /**
     * Modifica la instancia de la clase ClientAppListenerManager
     *
     * @param listenerManager
     *
     */
    public void setClientListenerManager(drakkar.prow.facade.desktop.event.ListenerManager listenerManager) {
        this.listener = listenerManager;
        DefaultListenerManager temp = (DefaultListenerManager) ((ProwController) netController).getListenerManager();
        temp.addFacadeListeners(listener.getFacadeDesktopListener());
    }

    /**
     * Modifica la instancia de la clase ClientAppListenerManager
     *
     * @param listenerManager
     *
     */
    @Override
    public void setListenerManager(ListenerManager listenerManager) {
        super.setListenerManager(listenerManager);
        ((ProwController) netController).setListenerManager(listenerManager);
    }

    /**
     *
     * @return instancia de la clase ProwAppSetting
     *
     */
    public ProwAppSetting getClientAppSetting() {
        return clientAppSetting;
    }

    /**
     * Modifica la instancia de la clase ProwAppSetting
     *
     * @param clientAppSetting nuevo objeto
     *
     */
    public void setClientAppSetting(ProwAppSetting clientAppSetting) {
        this.clientAppSetting = clientAppSetting;
        this.clientSetting.setPortNumber(clientAppSetting.getPortNumber());
        this.clientSetting.setSeeker(clientAppSetting.getSeeker());
    }

    @Override
    public NetworkController getNetworkController() {
        return netController;
    }
}
