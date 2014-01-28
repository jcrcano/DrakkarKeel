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
 * Esta clase contiene las constantes de los diferentes principios de búsquedas
 * que son soportados por DrakkarKeel
 */
public class SearchPrinciple {

    /**
     * Búsqueda con un buscador determinado
     */
    public static final int SINGLE_SEARCH = 1;
    /**
     * Búsqueda con multiples buscadores, fucionando resultados
     */
    public static final int META_SEARCH = 2;
    /**
     * Búsqueda con multiples buscadores, sin fucionar resultados
     */
    public static final int MULTI_SEARCH = 3;
     /**
     *  Búsqueda con un buscador determinado, aplicando mecanismos de división
      * de trabajo
     */
    public static final int SINGLE_SEARCH_AND_SPLIT = 4;

    /**
     * Búsqueda con multiples buscadores, fucionando resultados y aplicando
     * mecanismos de división de trabajo
     */
    public static final int META_SEARCH_AND_SPLIT = 5;
    /**
     * Búsqueda con multiples buscadores, sin fucionar resultados y aplicando
     * mecanismos de división de trabajo
     */
    public static final int MULTI_SEARCH_AND_SWITCH = 6;
   
}

