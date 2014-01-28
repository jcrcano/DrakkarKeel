/*
 * DrakkarKeel - An Enterprise Collaborative Search Platform
 *
 * The contents of this file are subject under the terms described in the
 * DRAKKARKEEL_LICENSE file included in this distribution; you may not use this
 * file except in compliance with the License. 
 *
 * 2013-2014 DrakkarKeel Platform.
 */
package drakkar.mast.retrieval;

import drakkar.oar.DocumentMetaData;
import drakkar.mast.SearchException;
import java.util.ArrayList;

/**
 * Esta interfaz constituye encabeza la jerarquía de todos los buscadores de DrakkarKeel,
 * siendo implementada por los mismos.
 *
 *
 */
public interface Searchable {
    
    /**
     * Ejecuta una búsqueda apartir de los parámetros de entrada
     *
     * @param query             consulta
     * @param caseSensitive     tener en cuenta mayúsculas y minísculas
     *
     * @return                  resultados de la búsqueda
     *
     * @throws SearchException  si ocurre alguna excepción durante el proceso de búsqueda
     */
    public ArrayList<DocumentMetaData> search(String query, boolean caseSensitive) throws SearchException;

    /**
     * Ejecuta una búsqueda apartir de los parámetros de entrada
     *
     * @param query             consulta
     * @param docType           tipo de documento(extensión: .java,.doc,..)
     * @param caseSensitive     tener en cuenta mayúsculas y minísculas
     *
     * @return resultados de la búsqueda
     *
     * @throws SearchException  si ocurre alguna excepción durante el proceso de búsqueda
     */
    public ArrayList<DocumentMetaData> search(String query, String docType, boolean caseSensitive) throws SearchException;

    /**
     * Ejecuta una búsqueda apartir de los parámetros de entrada
     *
     * @param query             consulta
     * @param docTypes          tipos de documento(extensión: .java,.doc,..)
     * @param caseSensitive     tener en cuenta mayúsculas y minísculas
     *
     * @return resultados de la búsqueda
     *
     * @throws SearchException  si ocurre alguna excepción durante el proceso de búsqueda
     */
    public ArrayList<DocumentMetaData> search(String query, String[] docTypes, boolean caseSensitive) throws SearchException;

    /**
     * Devuelve el objeto buscador
     *
     * @return buscador
     */
    public Searchable getSearchable();

    /**
     * Modifica el estado del buscador
     *
     * @param flag true para activo, false en caso contrario
     */
    public void setEnabled(boolean flag);

    /**
     * Devuelve el estado del buscador
     *
     * @return flag true si está activo, false en caso contrario
     */
    public boolean isEnabled();

    /**
     * Devuelve el identificador del buscador
     *
     * @return identificador
     */
    public int getID();

     /**
     * Devuelve el nombre del buscador
     *
     * @return identificador
     */
    public String getName();

    public Contextable getContext();

    
}
