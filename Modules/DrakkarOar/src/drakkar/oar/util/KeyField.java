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
 * las búsquedas por un determinado campo del documento
 */
public class KeyField {


    /**
     *  Dirección del documento
     */
    public static final int FIELD_FILEPATH = 1;
    /**
     * Nombre del documento
     */
    public static final int FIELD_NAME = 2;
    /**
     * Paquete de la clase java
     */
    public static final int FIELD_CODE_PACKAGE = 3;
    /**
     * Nombre de la clase java
     */
    public static final int FIELD_CODE_CLASSES_NAMES = 4;
    /**
     * Variable de la clase java
     */
    public static final int FIELD_CODE_VARIABLES_NAMES = 5;
    /**
     * Método de la clase java
     */
    public static final int FIELD_CODE_METHODS_NAMES = 6;
    /**
     * Comentarios de la clase, incluye el comentario
     * de la clase, de los métodos y de las variables.
     */
    public static final int FIELD_CODE_ALL_COMMENTS = 7;
    /**
     * Todo el código de la clase
     */
    public static final int FIELD_CODE_ALL_SOURCE = 8;
    /**
     * Documentación de la clase java
     */
    public static final int FIELD_CODE_JAVADOCS = 9;
    //////////////////////////////////////////////////////////////////


    /**
     * Contenido de un documento txt
     */
    public static final int FIELD_DOC_TEXT = 10;


    public static final int FIELD_DOC_BOOK = 11;

    /////////////////////TIPOS DE COLECCIONES////////////////////
    /**
     * Se refiere a ficheros de código fuente en el lenguaje Java
     */
    public static final int SOURCE_JAVA_CODE = 12;
    /**
     * Se refiere a documentos, del tipo .pdf y .txt
     */
    public static final int SOURCE_DOCUMENTS = 13;


    /////////////////////////////////////////////////////////////////
    /**
     * Tiene en cuenta mayúsculas y minúsculas
     */
    public static final int CASE_SENSITIVE = 14;

    /////////////////////////////////////////////////////////////////
    /**
     * Author
     */
    public static final int AUTHOR_DOCUMENTS = 15;    

    /////////////////////////////////////////////////////////////////
    /**
     * Last Modified
     */
    public static final int LAST_MODIFIED_DOCUMENTS = 16;        
   
}
