/*
 * DrakkarKeel - An Enterprise Collaborative Search Platform
 *
 * The contents of this file are subject under the terms described in the
 * DRAKKARKEEL_LICENSE file included in this distribution; you may not use this
 * file except in compliance with the License. 
 *
 * 2013-2014 DrakkarKeel Platform.
 */
package drakkar.oar.util;

/**
 * Esta clase constituye la estructura para almacenar los distintas propiedades
 * configurables de cliente y el servidor para hacerlas persistentes mediante un
 * fichero XML
 */
public class Parameter {

    private Object key;
    private Object value;

    /**
     * Constructor de la clase
     *
     * @param key   llave del parámetro
     * @param value valor del parámetro
     */
    public Parameter(Object key, Object value) {
        this.key = key;
        this.value = value;
    }

    /**
     * Devuelve la llave del parámetro
     *
     * @return llave
     */
    public Object getKey() {
        return key;
    }

    /**
     * Modifica la llave del parámetro
     *
     * @param key nueva llave
     */
    public void setKey(Object key) {
        this.key = key;
    }

    /**
     * Devuelve el valor del parámetro
     *
     * @return valor
     */
    public Object getValue() {
        return value;
    }

    /**
     * Modifica el valor del parámetro
     *
     * @param value nuevo valor
     */
    public void setValue(Object value) {
        this.value = value;
    }





}
