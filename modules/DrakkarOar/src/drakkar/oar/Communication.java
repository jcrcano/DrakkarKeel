/*
 * DrakkarKeel - An Enterprise Collaborative Search Platform
 *
 * The contents of this file are subject under the terms described in the
 * DRAKKARKEEL_LICENSE file included in this distribution; you may not use this
 * file except in compliance with the License. 
 *
 * 2013-2014 DrakkarKeel Platform.
 */
package drakkar.oar;

import java.io.Serializable;

/**
 * Esta clase es la encargada de incializar el tiempo de ejecución de Ice, para
 * poder establecer la comunicación entre las aplicaciones clientes y la aplicación
 * servidora
 *
 * 
 */
public class Communication implements Serializable{
     private static final long serialVersionUID = 70000000000001L;
    private Ice.Communicator communicator = null;
    private Ice.ObjectAdapter adapter = null;
    private boolean destroy = true, initialize = false;

    /**
     * Constructor por defecto de la  clase
     */
    public Communication() {
    }

    /**
     * 
     */
    public void initialize() {
        try {
            this.communicator = Ice.Util.initialize();
            destroy = false;
            initialize = true;
        } catch (Ice.LocalException err) {
            err.printStackTrace();

        }
    }

    /**
     * 
     * @param args
     */
    public void initialize(String[] args) {
        try {
            this.communicator = Ice.Util.initialize(args);
            destroy = false;
            initialize = true;
        } catch (Ice.LocalException err) {
            err.printStackTrace();

        }
    }

    /**
     * Reinicia el el objeto Ice.Communicator
     */
    public void reset() {
        if (this.adapter != null) {
            this.adapter.deactivate();
            this.adapter.destroy();
            this.adapter = null;
        }

        this.communicator.destroy();
        this.communicator = Ice.Util.initialize();
        destroy = false;
        initialize = true;
    }

    /**
     * Destruye el objeto Ice.Communicator
     */
    public void destroy() {
        this.communicator.destroy();
        destroy = true;
        initialize = false;

    }

    /**
     * Determina si el objeto Ice.Communicator está destruido
     *
     * @return true si el objeto Ice.Communicator esta destruido, false en caso contrario
     */
    public boolean isDestroy() {
        return destroy;
    }

    /**
     * Determina si el objeto Ice.Communicator está inicializado
     *
     * @return true si el objeto Ice.Communicator esta destruido, false en caso contrario
     */
    public boolean isInitialize() {
        return initialize;
    }

    /**
     *Devuelve la instancia del objeto Communicator de la clase
     *
     * @return instancia Communicator
     */
    public Ice.Communicator getCommunicator() {
        return this.communicator;
    }

    /**
     * Devuelve la instancia del objeto ObjectAdapter de la clase
     *
     * @return instancia de ObjectAdapter
     */
    public Ice.ObjectAdapter getAdapter() {
        return this.adapter;
    }

    /**
     * Modifica la instancia del objeto Communicator de la clase
     *
     * @param comm  nuevo instancia de Communicator
     */
    public void setCommunicator(Ice.Communicator comm) {
        this.communicator = comm;
    }

    /**
     * Modifica la instancia del objeto ObjectAdapter de la clase
     *
     * @param adapter nuevo instancia de ObjectAdapter
     */
    public void setAdapter(Ice.ObjectAdapter adapter) {
        this.adapter = adapter;
    }
}
