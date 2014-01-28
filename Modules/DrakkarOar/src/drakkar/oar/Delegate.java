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

import Ice.ObjectPrx;
import java.io.Serializable;

/**
 * Esta clase tiene el objetivo de proporcionar una serie de atributos
 * para las instancias delegados de los Objetos Ice registrados en el
 * servidor
 * 
 * 
 * 
 * 
 */
public class Delegate implements Serializable{
     private static final long serialVersionUID = 70000000000003L;

    protected Communication communication;
    protected String name;
    protected String ip;
    protected int portNumber;
    protected Ice.ObjectPrx proxy;

    /**
     * Constructor por defecto
     */
    public Delegate() {
        this.communication = null;
        this.name = "";
        this.ip = "";
        this.portNumber = 11000;
    }

    /**
     * Constructor de la Clase
     *
     * @param communication  instancia de Communication
     * @param name           nombre del servidor
     * @param ip             dirección host
     * @param porNumber      puerto de escucha del adaptador del objeto proxy
     */
    public Delegate(Communication communication, String name, String ip, int porNumber) {
        this.communication = communication;
        this.name = name;
        this.ip = ip;
        this.portNumber = porNumber;
    }

    /**
     * Constructor de la Clase
     *
     * @param communication  instancia de Communication
     * @param name           nombre del servidor
     * @param ip             dirección host
     * @param porNumber      puerto de escucha del adaptador del objeto proxy
     * @param proxy          objeto proxy  
     */
    public Delegate(Communication communication, String name, String ip, int porNumber, ObjectPrx proxy) {
        this.communication = communication;
        this.name = name;
        this.ip = ip;
        this.portNumber = porNumber;
        this.proxy = proxy;
    }

    /**
     * Devuelve la instancia del objeto Communication de esta clase
     *
     * @return  instancia de Communication
     */
    public Communication getCommunication() {
        return this.communication;
    }

    /**
     * Modifica la instancia del objeto Communication de esta clase
     *
     * @param communication  nuevo objeto Communication
     */
    public void setCommunication(Communication communication) {
        this.communication = communication;
    }

    /**
     * Devuelve el número de IP del host  del objeto proxy
     *
     * @return número de IP del host
     */
    public String getIP() {
        return this.ip;
    }

    /**
     * Modifica el número de IP del host del objeto proxy
     *
     * @param ip  nuevo número de IP del host
     */
    public void setIP(String ip) {
        this.ip = ip;
    }

    /**
     * Devuelve el nombre del objeto proxy
     *
     * @return  nombre
     */
    public String getName() {
        return this.name;
    }

    /**
     * Modifica el nombre del objeto proxy
     *
     * @param name  nuevo nombre 
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Devuelve el objeto Proxy de la clase
     *
     * @return  instancia de ObjectPrx.
     */
    public ObjectPrx getProxy() {
        return this.proxy;
    }

    /**
     * Modifica el objeto proxy de la clase
     *
     * @param proxy  nuevo ObjectPrx
     */
    public void setProxy(ObjectPrx proxy) {
        this.proxy = proxy;
    }

    /**
     * Devuelve el puerto de escucha del adptador del objeto proxy
     *
     * @return puerto
     */
    public int getPortNumber() {
        return portNumber;
    }

    /**
     * Modifica el puerto de escucha del adptador del objeto proxy
     *
     * @param porNumber nuevo puerto
     */
    public void setPortNumber(int porNumber) {
        this.portNumber = porNumber;
    }
}
