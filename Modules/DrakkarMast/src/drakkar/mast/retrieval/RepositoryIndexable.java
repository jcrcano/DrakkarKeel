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

/**
 * Esta interfaz declara los métodos para el trabajo con los índices de las
 * colecciones de datos, para la búsqueda con sistemas de control de versiones
 *
 * 
 */
public interface RepositoryIndexable extends Indexable {

    /**
     * Crea un índice de la colección de datos ubicada en la dirección por defecto
     *
     * @return cantidad de documentos indexados
     *
     * @throws IndexException si ocurre algún error en el proceso de indexación
     */
    public long makeIndex() throws IndexException;

    /**
     * Crea un índice en una dirección dada
     *
     * @param indexPath      dirección de índice
     * @return cantidad de documentos indexados
     *
     * @throws IndexException si ocurre algún error en el proceso de indexación
     */
    public long makeIndex(File indexPath) throws IndexException;

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
