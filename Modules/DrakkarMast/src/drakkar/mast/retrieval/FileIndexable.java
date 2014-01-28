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

import drakkar.mast.IndexException;
import java.io.File;
import java.util.List;

/**
 * Esta interfaz declara los métodos para el trabajo con los índices de las
 * colecciones de datos, en los motores de búsqueda
 * 
 *
 * 
 */
public interface FileIndexable extends Indexable{

    public final int MAKE_INDEX = 0;
    public final int ADD_INDEX = 1;

    /**
     * Crea un índice de la colección de datos ubicada en la dirección por defecto
     *
     * @return cantidad de documentos indexados
     *
     * @throws IndexException si ocurre algún error en el proceso de indexación
     */
    public long makeIndex() throws IndexException;

    /**
     * Crea un índice de la colección de datos pasada por parámetros en la dirección
     *  por defecto
     *
     * @param collectionPath colección de datos
     * @return cantidad de documentos indexados
     *
     * @throws IndexException si ocurre algún error en el proceso de indexación
     */
    public long makeIndex(File collectionPath) throws IndexException;

    /**
     * Crea un índice de las colecciones de datos pasada por parámetros en la dirección
     *  por defecto
     *
     * @param collectionPath colecciones de datos
     * @return cantidad de documentos indexados
     *
     * @throws IndexException si ocurre algún error en el proceso de indexación
     */
    public long makeIndex(List<File> collectionPath) throws IndexException;

    /**
     * Crea un índice de la colección de datos pasada por parámetros en la dirección
     *  especificada
     *
     * @param collectionPath colección de datos
     * @param indexPath      dirección de índice
     *
     * @return cantidad de documentos indexados
     *
     * @throws IndexException si ocurre algún error en el proceso de indexación
     */
    public long makeIndex(File collectionPath, File indexPath) throws IndexException;

    /**
     * Crea un índice de las colecciones de datos pasada por parámetros en la dirección
     *  especificada
     *
     * @param collectionPath colecciones de datos
     * @param indexPath      dirección de índice
     *
     * @return cantidad de documentos indexados
     *
     * @throws IndexException si ocurre algún error en el proceso de indexación
     */
    public long makeIndex(List<File> collectionPath, File indexPath) throws IndexException;

    /**
     * Adiciona la colección de datos pasada por parámetros, al índice que se encuentra
     * en la dirección por defecto
     *
     * @param collectionPath colección de datos
     *
     * @return cantidad de documentos indexados
     *
     * @throws IndexException si ocurre algún error en el proceso de indexación
     */
    public long updateIndex(File collectionPath) throws IndexException;

    /**
     * Adiciona las colecciones de datos pasada por parámetros, al índice que se encuentra
     * en la dirección por defecto
     *
     * @param collectionPath colecciones de datos
     *
     * @return cantidad de documentos indexados
     *
     * @throws IndexException si ocurre algún error en el proceso de indexación
     */
    public long updateIndex(List<File> collectionPath) throws IndexException;

    /**
     * Adiciona la colección de datos pasada por parámetros, al índice que se encuentra
     * en la dirección especificada
     *
     * @param collectionPath colección de datos
     *
     * @param indexPath dirección del índice
     *
     * @return cantidad de documentos indexados
     *
     * @throws IndexException si ocurre algún error en el proceso de indexación
     */
    public long updateIndex(File collectionPath, File indexPath) throws IndexException;

    /**
     * Adiciona las colecciones de datos pasada por parámetros, al índice que se encuentra
     * en la dirección especificada
     *
     * @param collectionPath colecciones de datos
     *
     * @param indexPath dirección del índice
     *
     * @return cantidad de documentos indexados
     *
     * @throws IndexException si ocurre algún error en el proceso de indexación
     */
    public long updateIndex(List<File> collectionPath, File indexPath) throws IndexException;

    /**
     * Carga el índice que se encuentra en la ubicación especificada
     *
     * @return true si cargó el índice de la dirección especificada, false en caso contrario
     *
     * @throws IndexException si ocurre algún error en el proceso de carga del indice
     */
    public boolean loadIndex() throws IndexException;

    /**
     * Carga el índice que se encuentra en la ubicación especificada
     *
     * @param indexPath dirección del índice
     *
     * @return true si cargó el índice de la dirección especificada, false en caso contrario
     *
     * @throws IndexException si ocurre algún error en el proceso de carga del indice
     */
    public boolean loadIndex(File indexPath) throws IndexException;
}
