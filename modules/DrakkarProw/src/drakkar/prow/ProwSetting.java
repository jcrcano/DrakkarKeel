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

import drakkar.oar.Seeker;
import java.io.Serializable;

/**
 * Esta clase almacena la configuración de la aplicación cliente para inicializar la
 * comunicación con la aplicación servidora
 */
public class ProwSetting implements Serializable{
    private static final long serialVersionUID = 80000000000005L;
    private Seeker seeker;
    private int portNumber;

    /**
     * Constructor de la clase
     */
    public ProwSetting() {
       this.seeker = new Seeker();
       this.portNumber = -1;

    }

    /**
     * Constructor de la clase
     *
     * @param seeker objeto seeker que representa al usuario
     * <br>
     * <b>Nota:</b>
     * <br>
     * <tt>Al construir el objeto se obtine el número de un puerto disponible en
     * el sistema, para escuchar las notificaciones de la aplicación servidora.</tt>
     * <br>
     */
    public ProwSetting(Seeker seeker) {
        this.seeker = seeker;
        this.portNumber = -1;
    }

    /**
     * Constructor de la clase
     *
     * @param seeker     objeto seeker que representa al usuario
     * @param portNumber puerto de escucha de la aplicación cliente
     *
     * <br>
     * <br>
     * <b>Nota:</b>
     * <br>
     * <tt>Al construir el objeto se chequea el número del puerto sea >= 10000 y que
     * este se encuentre disponible en el sistema. En caso caso de no cumplir
     * con las anteriores premisas, el sistema se autogestiona un número de puerto. </tt>
     * <br>
     */
    public ProwSetting(Seeker seeker, int portNumber) {
        this.seeker = seeker;
        this.portNumber = portNumber;
    }

    /**
     * Devuelve el número del puerto de escucha el cliente
     *
     * @return puerto
     */
    public int getPortNumber() {
        return portNumber;
    }

    /**
     *
     * @param portNumber puerto de escucha de la aplicación cliente
     *
     * <br>
     * <br>
     * <b>Nota:</b>
     * <br>
     * <tt>El número del puerto sea >= 10000 y encuentrarse disponible en el sistema.
     * En caso caso de no cumplir con las anteriores premisas, el sistema se
     * autogestiona un puerto disponible.</tt>
     * <br>
     */
    public void setPortNumber(int portNumber) {
        this.portNumber = portNumber;
    }

    /**
     * Devuelve el objeto seeker que representa al usuario
     *
     * @return seeker
     */
    public Seeker getSeeker() {
        return seeker;
    }

    /**
     * Modifica el objeto seeker que representa al usuario
     *
     * @param seeker nuevo objeto seeker
     */
    public void setSeeker(Seeker seeker) {
        this.seeker = seeker;
    }
}
