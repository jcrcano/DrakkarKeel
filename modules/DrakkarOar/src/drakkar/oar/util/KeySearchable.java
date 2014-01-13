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
 * Esta clase contiene las diferentes constantes que pueden ser empleadas para
 * la  representacion de los diferentes buscadores soportados por DRakkarKeel
 */
public class KeySearchable {

    /**
     * Motor de búsqueda Lucene
     */
    public static final int LUCENE_SEARCH_ENGINE = 1;
    /**
     * Motor de búsqueda Minion
     */
    public static final int MINION_SEARCH_ENGINE = 2;
    /**
     * Motor de búsqueda Terier
     */
    public static final int TERRIER_SEARCH_ENGINE = 3;
    /**
     * Motor de búsqueda Indri
     */
    public static final int INDRI_SEARCH_ENGINE = 4;   
      /**
     * Todos los buscadores
     */
    public static final int MULTIPLE_SEARCHERS = 5;
    
    /**
     * Servicio de búsqueda Yahoo
     */
    public static final int YAHOO_WEB_SEARCH_SERVICE = 6;
    /**
     * Servicio de búsqueda Google
     */
    public static final int GOOGLE_WEB_SEARCH_SERVICE = 7;
  
    
     /**
     * Búsqueda en indices de repositorios SVN
     */
    public static final int SVN_SEARCHER = 8;
    
    
}
