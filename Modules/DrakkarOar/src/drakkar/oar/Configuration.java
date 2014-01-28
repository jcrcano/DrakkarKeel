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
 * Esta clase tiene como objetivo brindar al desarrollador la posibilidad de crear
 * ficheros de propiedades para las aplicaciones, de tal forma que sea más fácil
 * la configuración de los objetos Ice inicializados en las mismas.
 *
 *
 */
public class Configuration implements Serializable{
     private static final long serialVersionUID = 70000000000002L;

    private Ice.Properties properties = null;
    private String[] arguments = null;

    /**
     * Constructor por defecto de la clase
     *
     * @param arguments  parámetros para crear un fichero de Propiedades
     */
    public Configuration(String[] arguments) {
        this.arguments = arguments;
        try {
            this.properties = Ice.Util.createProperties(this.arguments);
        } catch (Ice.LocalException err) {
            err.printStackTrace();
        }
    }

    /**
     * Constructor de la clase
     *
     * @param properties objeto Properties
     */
    public Configuration(Ice.Properties properties) {
        if (properties != null) {
            this.properties = properties;
        }
    }

    /**
     * Este método reemplaza el objeto Properties de la clase
     *
     * @param properties nuevo objeto Properties
     */
    public void setProperties(Ice.Properties properties) {
        this.properties = properties;
    }

    /**
     * Este método devuelve el objeto Properties de la clase
     *
     * @return objeto Properties
     */
    public Ice.Properties getProperties() {
        return this.properties;
    }

    /**
     * Carga un objeto Properties a partir de su URI
     * 
     * @param uri dirección del objeto Properties
     */
    public void loadProperties(String uri) {
        try {
            this.properties.load(uri);
        } catch (Ice.LocalException err) {
            err.printStackTrace();
        }
    }

   
   
}
