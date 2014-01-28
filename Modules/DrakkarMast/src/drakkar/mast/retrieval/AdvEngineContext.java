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
import drakkar.oar.facade.event.FacadeDesktopListener;
import drakkar.mast.SearchException;
import java.util.ArrayList;

public abstract class AdvEngineContext extends EngineContext {

    /**
     *
     */
    public AdvEngineContext() {
    }

    /**
     * 
     * @param listener
     */
    public AdvEngineContext(FacadeDesktopListener listener) {
        super(listener);
    }

    /**
     * Ejecuta una búsqueda apartir de los parámetros de entrada
     *
     * @param query             consulta
     * @param field             campo del documento
     * @param caseSensitive     tener en cuenta mayúsculas y minísculas
     *
     * @return                  resultados de la búsqueda
     *
     * @throws SearchException  si ocurre alguna excepción durante el proceso de búsqueda
     */
    public abstract ArrayList<DocumentMetaData> search(String query, int field, boolean caseSensitive) throws SearchException;

    /**
     * Ejecuta una búsqueda apartir de los parámetros de entrada
     *
     * @param query             consulta
     * @param fields            campos del documento
     * @param caseSensitive     tener en cuenta mayúsculas y minísculas
     *
     * @return                  resultados de la búsqueda
     *
     * @throws SearchException  si ocurre alguna excepción durante el proceso de búsqueda
     */
    public abstract ArrayList<DocumentMetaData> search(String query, int[] fields, boolean caseSensitive) throws SearchException;

    /**
     * Ejecuta una búsqueda apartir de los parámetros de entrada
     *
     * @param query             consulta
     * @param docType           tipo de documento
     * @param field             campo del documento
     * @param caseSensitive     tener en cuenta mayúsculas y minísculas
     *
     * @return                  resultados de la búsqueda
     *
     * @throws SearchException  si ocurre alguna excepción durante el proceso de búsqueda
     */
    public abstract ArrayList<DocumentMetaData> search(String query, String docType, int field, boolean caseSensitive) throws SearchException;

    /**
     * Ejecuta una búsqueda apartir de los parámetros de entrada
     *
     * @param query             consulta
     * @param docTypes          tipos de documento
     * @param field             campo del documento
     * @param caseSensitive     tener en cuenta mayúsculas y minísculas
     *
     * @return                  resultados de la búsqueda
     *
     * @throws SearchException  si ocurre alguna excepción durante el proceso de búsqueda
     */
    public abstract ArrayList<DocumentMetaData> search(String query, String[] docTypes, int field, boolean caseSensitive) throws SearchException;

    /**
     * Ejecuta una búsqueda apartir de los parámetros de entrada
     *
     * @param query             consulta
     * @param docTypes          tipos de documento
     * @param fields            campos del documento
     * @param caseSensitive     tener en cuenta mayúsculas y minísculas
     *
     * @return                  resultados de la búsqueda
     *
     * @throws SearchException  si ocurre alguna excepción durante el proceso de búsqueda
     */
    public abstract ArrayList<DocumentMetaData> search(String query, String[] docTypes, int[] fields, boolean caseSensitive) throws SearchException;

    /**
     * Ejecuta una búsqueda apartir de los parámetros de entrada
     *
     * @param query             consulta
     * @param docType           tipos de documento
     * @param fields            campos del documento
     * @param caseSensitive     tener en cuenta mayúsculas y minísculas
     *
     * @return                  resultados de la búsqueda
     *
     * @throws SearchException  si ocurre alguna excepción durante el proceso de búsqueda
     */
    public abstract ArrayList<DocumentMetaData> search(String query, String docType, int[] fields, boolean caseSensitive) throws SearchException;

    /**
     * Devuelve el nombre de un campo dado su id (clase Assignable)
     *
     * @param field campo del documento
     *
     * @return nombre
     */
    public abstract String getDocumentField(int field);
}
