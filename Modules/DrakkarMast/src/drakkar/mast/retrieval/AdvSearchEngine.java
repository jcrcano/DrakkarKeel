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
 * Esta clase representa un motor de búsqueda avanzado con sus respectivos métodos
 *
 * 
 *
 */
public abstract class AdvSearchEngine extends SearchEngine {

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
    public ArrayList<DocumentMetaData> search(String query, int field, boolean caseSensitive) throws SearchException {
        ArrayList<DocumentMetaData> results = ((AdvEngineContext) this.getContext()).search(query, field, caseSensitive);
        return results;
    }

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
    public ArrayList<DocumentMetaData> search(String query, int[] fields, boolean caseSensitive) throws SearchException {
        ArrayList<DocumentMetaData> results = ((AdvEngineContext) this.getContext()).search(query, fields, caseSensitive);
        return results;
    }

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
    public ArrayList<DocumentMetaData> search(String query, String docType, int field, boolean caseSensitive) throws SearchException {
        ArrayList<DocumentMetaData> results = ((AdvEngineContext) this.getContext()).search(query, docType, field, caseSensitive);

        return results;
    }

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
    public ArrayList<DocumentMetaData> search(String query, String[] docTypes, int field, boolean caseSensitive) throws SearchException {
        ArrayList<DocumentMetaData> results = ((AdvEngineContext) this.getContext()).search(query, docTypes, field, caseSensitive);

        return results;
    }

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
    public ArrayList<DocumentMetaData> search(String query, String docType, int[] fields, boolean caseSensitive) throws SearchException {
        ArrayList<DocumentMetaData> results = ((AdvEngineContext) this.getContext()).search(query, docType, fields, caseSensitive);

        return results;
    }

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
    public ArrayList<DocumentMetaData> search(String query, String[] docTypes, int[] fields, boolean caseSensitive) throws SearchException {
        ArrayList<DocumentMetaData> results = ((AdvEngineContext) this.getContext()).search(query, docTypes, fields, caseSensitive);

        return results;
    }
}
