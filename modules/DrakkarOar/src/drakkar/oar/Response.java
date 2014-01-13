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
 * Esta clase es la encargada de encapsular todos los parametros de las respuestas
 * enviadas por la aplicación servidora al la aplicación cliente
 * 
 *
 */
public class Response implements java.io.Serializable {

     private static final long serialVersionUID = 87050425778L;

    private Map<Object, Object> parameters;

    /**
     * Cosntructor por defecto de la clase
     */
    public Response() {
        this.parameters = new HashMap<>();
    }

    /**
     * Cosntructor de la clase
     *
     * @param parameters conjunto de parametros de la invocacion
     */
    public Response(Map<Object, Object> parameters) {
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
     * @param key llave del parametro
     *
     * @return valor del parametro
     */
    public Object get(Object key) {
        return this.parameters.get(key);
    }

    /**
     * Devuelve ltodos los parametros de la invocaion
     *
     * @return parametros
     */
    public Map<Object, Object> getParameters() {
        return parameters;
    }

    /**
     * Modifica todos los parametros de la invocacion
     *
     * @param parameters nuevos parametros
     */
    public void setParameters(Map<Object, Object> parameters) {
        this.parameters = parameters;
    }

    /**
     * Serializa el objeto Response
     *
     * @return  objeto Response serializado
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
     * Este método deserializa el objeto Request pasado como un arreglo de bytes.
     *
     * @param response      objeto Response serializado
     * @param communicator  objeto Communicator que realiza la invocación
     *
     * @return objeto Response
     *
     * @throws IOException   Signals that an I/O exception of some sort has occurred.
     *                       This class is the general class of exceptions produced
     *                       by failed or interrupted I/O operations.
     * @throws ClassNotFoundException   Thrown when an application tries to load in a
     *                                  class through its string name, but no definition
     *                                  for the class with the specified name could be found.
     */
    public static Response arrayToResponse(byte[] response, Communicator communicator) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bais = new ByteArrayInputStream(response);
        Ice.ObjectInputStream ois = new Ice.ObjectInputStream(communicator, bais);
        return (Response) ois.readObject();
    }
}
