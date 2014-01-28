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

import Ice.Communicator;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Esta clase es la encargada de encapsular todos los parámetros de las invocaciones
 * de los clientes hacia el servidor
 *
 * 
 */
public class Request implements java.io.Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 87011125789L;
    private Map<Object, Object> parameters;

    /**
     * Cosntructor por defecto de la clase
     */
    public Request() {
        this.parameters = new HashMap<>();
    }

    /**
     * Cosntructor de la clase
     *
     * @param parameters conjunto de parametros de la invocacion
     */
    public Request(Map<Object, Object> parameters) {
        this.parameters = parameters;
    }

    /**
     * Agregar un nuevo parametro
     *
     * @param key   llave del parametro
     * @param value valor del parametro
     */
    public void put(Object key, Object value) {
        this.parameters.put(key, value);
    }

    /**
     * Devuelve el valor del parametro asociado a una llave determinada
     *
     * @param key llave del parametro
     *
     * @return valor del parametro
     */
    public Object get(Object key) {
        return this.parameters.get(key);
    }

    /**
     * Devuelve ltodos los parametros de la invocación
     *
     * @return parametros
     */
    public Map<Object, Object> getParameters() {
        return parameters;
    }

    /**
     * Modifica todos los parametros de la invocación
     *
     * @param parameters nuevos parametros
     */
    public void setParameters(Map<Object, Object> parameters) {
        this.parameters = parameters;
    }

    /**
     * Serializa el objeto Request
     *
     * @return objeto Request serializado
     *
     * @throws IOException   Signals that an I/O exception of some sort has occurred.
     *                       This class is the general class of exceptions produced
     *                       by failed or interrupted I/O operations.
     */
    public byte[] toArray() throws IOException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutputStream stream = new ObjectOutputStream(byteStream);
        stream.writeObject(this);
        return byteStream.toByteArray();

    }

    /**
     * Deserializa el objeto Request
     *
     * @param request       objeto Request serializado
     * @param communicator  objeto Communicator que realiza la invocación
     *
     * @return  objeto Response deserializado
     *
     * @throws IOException   Signals that an I/O exception of some sort has occurred.
     *                       This class is the general class of exceptions produced
     *                       by failed or interrupted I/O operations.
     * 
     * @throws ClassNotFoundException Thrown when an application tries to load in a
     *                                class through its string name, but no definition
     *                                for the class with the specified name could be found.
     */
    public static Request arrayToRequest(byte[] request, Communicator communicator) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bais = new ByteArrayInputStream(request);
        Ice.ObjectInputStream ois = new Ice.ObjectInputStream(communicator, bais);
        return (Request) ois.readObject();
    }
}
