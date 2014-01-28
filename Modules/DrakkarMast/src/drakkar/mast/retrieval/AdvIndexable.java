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
import drakkar.mast.SearchableNotSupportedException;
import java.io.File;
import java.util.List;

/**
 * Esta interfaz declara todos las posibles operaciones relacionadas con
 * el proceso de indexación de los diferentes buscadores soportados por DrakkarKeel
 *
 * 
 */
public interface AdvIndexable {

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
     * por defecto
     *
     * @param collectionPath colección de datos
     * @return cantidad de documentos indexados
     *
     * @throws IndexException si ocurre algún error en el proceso de indexación
     */
    public long makeIndex(File collectionPath) throws IndexException;

    /**
     * Crea un índice de las colecciones de datos pasada por parámetros en la dirección
     * por defecto
     *
     * @param collectionPath colecciones de datos
     * @return cantidad de documentos indexados
     *
     * @throws IndexException si ocurre algún error en el proceso de indexación
     */
    public long makeIndex(List<File> collectionPath) throws IndexException;

    /**
     * Crea un índice de la colección de datos pasada por parámetros en la dirección
     * especificada
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
     * especificada
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
     * Crea un índice de la colección de datos pasada por parámetros en la dirección
     * por defecto, para el buscador seleccionado
     *
     * @param searcher       buscador
     * @param collectionPath colección de datos
     *
     * @return cantidad de documentos indexados
     *
     * @throws SearchableNotSupportedException si el buscador especificado no es soportado
     * @throws IndexException si ocurre algún error en el proceso de indexación
     *
     * <br>
     * <br>
     * <b>Nota:</b>
     * <br>
     * Las constantes que representan los diferentes buscadores soportados, se encuentran
     * definidas en la clase <code>KeySearchable</code>, del paquete drakkar.oar.util
     * <br>
     * <br>
     *
     * @see KeySearchable
     *
     */
    public long makeIndex(int searcher, File collectionPath) throws SearchableNotSupportedException, IndexException;

    /**
     * Crea un índice de la colección de datos pasada por parámetros en la dirección
     * especificada para el buscador seleccionado
     *
     * @param searcher       buscador
     * @param collectionPath colección de datos
     * @param indexPath      dirección de índice
     *
     * @return cantidad de documentos indexados
     *
     * @throws SearchableNotSupportedException si el buscador especificado no es soportado
     * @throws IndexException si ocurre algún error en el proceso de indexación
     *
     * <br>
     * <br>
     * <b>Nota:</b>
     * <br>
     * Las constantes que representan los diferentes buscadores soportados, se encuentran
     * definidas en la clase <code>KeySearchable</code>, del paquete drakkar.oar.util
     * <br>
     * <br>
     *
     * @see KeySearchable
     *
     */
    public long makeIndex(int searcher, File collectionPath, File indexPath) throws SearchableNotSupportedException, IndexException;

    /**
     * Crea un índice de la colección de datos pasada por parámetros en la dirección
     * por defecto, para el buscador seleccionado
     *
     * @param searcher       buscador
     * @param collectionPath colección de datos
     *
     * @return cantidad de documentos indexados
     *
     * @throws SearchableNotSupportedException si el buscador especificado no es soportado
     * @throws IndexException si ocurre algún error en el proceso de indexación
     *
     * <br>
     * <br>
     * <b>Nota:</b>
     * <br>
     * Las constantes que representan los diferentes buscadores soportados, se encuentran
     * definidas en la clase <code>KeySearchable</code>, del paquete drakkar.oar.util
     * <br>
     * <br>
     *
     * @see KeySearchable
     *
     */
    public long makeIndex(int searcher, List<File> collectionPath) throws SearchableNotSupportedException, IndexException;

    /**
     * Crea un índice de la colección de datos pasada por parámetros en la dirección
     * especificada para el buscador seleccionado
     *
     * @param searcher       buscador
     * @param collectionPath colección de datos
     * @param indexPath      dirección de índice
     *
     * @return cantidad de documentos indexados
     *
     * @throws SearchableNotSupportedException si el buscador especificado no es soportado
     * @throws IndexException si ocurre algún error en el proceso de indexación
     *
     * <br>
     * <br>
     * <b>Nota:</b>
     * <br>
     * Las constantes que representan los diferentes buscadores soportados, se encuentran
     * definidas en la clase <code>KeySearchable</code>, del paquete drakkar.oar.util
     * <br>
     * <br>
     *
     * @see KeySearchable
     *
     */
    public long makeIndex(int searcher, List<File> collectionPath, File indexPath) throws SearchableNotSupportedException, IndexException;

    /**
     * Crea un índice de la colección de datos pasada por parámetros en la dirección
     * por defecto, para el buscador seleccionado
     *
     * @param searchers       buscadores
     * @param collectionPath colección de datos
     *
     * @return cantidad de documentos indexados
     *
     * @throws SearchableNotSupportedException si el buscador especificado no es soportado
     * @throws IndexException si ocurre algún error en el proceso de indexación
     *
     * <br>
     * <br>
     * <b>Nota:</b>
     * <br>
     * Las constantes que representan los diferentes buscadores soportados, se encuentran
     * definidas en la clase <code>KeySearchable</code>, del paquete drakkar.oar.util
     * <br>
     * <br>
     *
     * @see KeySearchable
     *
     */
    public long makeIndex(int[] searchers, File collectionPath) throws SearchableNotSupportedException, IndexException;

    /**
     * Crea un índice de la colección de datos pasada por parámetros en la dirección
     * por defecto, para el buscador seleccionado
     *
     * @param searchers       buscadores
     * @param collectionPath colección de datos
     *
     * @return cantidad de documentos indexados
     *
     * @throws SearchableNotSupportedException si el buscador especificado no es soportado
     * @throws IndexException si ocurre algún error en el proceso de indexación
     *
     * <br>
     * <br>
     * <b>Nota:</b>
     * <br>
     * Las constantes que representan los diferentes buscadores soportados, se encuentran
     * definidas en la clase <code>KeySearchable</code>, del paquete drakkar.oar.util
     * <br>
     * <br>
     *
     * @see KeySearchable
     *
     */
    public long makeIndex(int[] searchers, List<File> collectionPath) throws SearchableNotSupportedException, IndexException;

    /**
     * Crea un índice de la colección de datos pasada por parámetros en la dirección
     * especificada, para el buscador seleccionado
     *
     * @param searchers      buscadores
     * @param collectionPath colección de datos
     * @param indexPath      dirección del índice
     *
     * @return cantidad de documentos indexados
     *
     * @throws SearchableNotSupportedException si el buscador especificado no es soportado
     * @throws IndexException si ocurre algún error en el proceso de indexación
     *
     * <br>
     * <br>
     * <b>Nota:</b>
     * <br>
     * Las constantes que representan los diferentes buscadores soportados, se encuentran
     * definidas en la clase <code>KeySearchable</code>, del paquete drakkar.oar.util
     * <br>
     * <br>
     *
     * @see KeySearchable
     *
     */
    public long makeIndex(int[] searchers, File collectionPath, File indexPath) throws SearchableNotSupportedException, IndexException;

    /**
     * Crea un índice de la colección de datos pasada por parámetros en la dirección
     * especificada, para el buscador seleccionado
     *
     * @param searchers      buscadores
     * @param collectionPath colección de datos
     * @param indexPath      dirección del índice
     *
     * @return cantidad de documentos indexados
     *
     * @throws SearchableNotSupportedException si el buscador especificado no es soportado
     * @throws IndexException si ocurre algún error en el proceso de indexación
     *
     * <br>
     * <br>
     * <b>Nota:</b>
     * <br>
     * Las constantes que representan los diferentes buscadores soportados, se encuentran
     * definidas en la clase <code>KeySearchable</code>, del paquete drakkar.oar.util
     * <br>
     * <br>
     *
     * @see KeySearchable
     *
     */
    public long makeIndex(int[] searchers, List<File> collectionPath, File indexPath) throws SearchableNotSupportedException, IndexException;

    /**
     * Adiciona la colección de datos pasada por parámetros, al índice que se encuentra
     * en la dirección por defecto, para el buscador especificado
     *
     * @param searcher       buscador
     * @param collectionPath colección de datos
     *
     * @return cantidad de documentos indexados
     *
     * @throws SearchableNotSupportedException si el buscador especificado no es soportado
     * @throws IndexException si ocurre algún error en el proceso de indexación
     *
     * <br>
     * <br>
     * <b>Nota:</b>
     * <br>
     * Las constantes que representan los diferentes buscadores soportados, se encuentran
     * definidas en la clase <code>KeySearchable</code>, del paquete drakkar.oar.util
     * <br>
     * <br>
     *
     * @see KeySearchable
     *
     */
    public long updateIndex(int searcher, File collectionPath) throws SearchableNotSupportedException, IndexException;

    /**
     * Adiciona la colección de datos pasada por parámetros, al índice que se encuentra
     * en la dirección por defecto, para el buscador especificado
     *
     * @param searcher       buscador
     * @param collectionPath colección de datos
     *
     * @return cantidad de documentos indexados
     *
     * @throws SearchableNotSupportedException si el buscador especificado no es soportado
     * @throws IndexException si ocurre algún error en el proceso de indexación
     *
     * <br>
     * <br>
     * <b>Nota:</b>
     * <br>
     * Las constantes que representan los diferentes buscadores soportados, se encuentran
     * definidas en la clase <code>KeySearchable</code>, del paquete drakkar.oar.util
     * <br>
     * <br>
     *
     * @see KeySearchable
     *
     */
    public long updateIndex(int searcher, List<File> collectionPath) throws SearchableNotSupportedException, IndexException;

    /**
     * Adiciona la colección de datos pasada por parámetros, al índice que se encuentra
     * en la dirección especificada, para el buscador seleccionado
     *
     * @param searcher       buscador
     * @param collectionPath colección de datos
     * @param indexPath      dirección del índice
     *
     * @return cantidad de documentos indexados
     *
     * @throws SearchableNotSupportedException si el buscador especificado no es soportado
     * @throws IndexException si ocurre algún error en el proceso de indexación
     *
     * <br>
     * <br>
     * <b>Nota:</b>
     * <br>
     * Las constantes que representan los diferentes buscadores soportados, se encuentran
     * definidas en la clase <code>KeySearchable</code>, del paquete drakkar.oar.util
     * <br>
     * <br>
     *
     * @see KeySearchable
     *
     */
    public long updateIndex(int searcher, File collectionPath, File indexPath) throws SearchableNotSupportedException, IndexException;

    /**
     * Adiciona la colección de datos pasada por parámetros, al índice que se encuentra
     * en la dirección especificada, para el buscador seleccionado
     *
     * @param searcher       buscador
     * @param collectionPath colección de datos
     * @param indexPath      dirección del índice
     *
     * @return cantidad de documentos indexados
     *
     * @throws SearchableNotSupportedException si el buscador especificado no es soportado
     * @throws IndexException si ocurre algún error en el proceso de indexación
     *
     * <br>
     * <br>
     * <b>Nota:</b>
     * <br>
     * Las constantes que representan los diferentes buscadores soportados, se encuentran
     * definidas en la clase <code>KeySearchable</code>, del paquete drakkar.oar.util
     * <br>
     * <br>
     *
     * @see KeySearchable
     *
     */
    public long updateIndex(int searcher, List<File> collectionPath, File indexPath) throws SearchableNotSupportedException, IndexException;

    /**
     * Adiciona la colección de datos pasada por parámetros, al índice que se encuentra
     * en la dirección especificada, para los buscadores seleccionados
     *
     * @param searchers       buscadores
     * @param collectionPath colección de datos
     * @param indexPath      dirección del índice
     *
     * @return cantidad de documentos indexados
     *
     * @throws SearchableNotSupportedException si el buscador especificado no es soportado
     * @throws IndexException si ocurre algún error en el proceso de indexación
     *
     * <br>
     * <br>
     * <b>Nota:</b>
     * <br>
     * Las constantes que representan los diferentes buscadores soportados, se encuentran
     * definidas en la clase <code>KeySearchable</code>, del paquete drakkar.oar.util
     * <br>
     * <br>
     *
     * @see KeySearchable
     *
     */
    public long updateIndex(int[] searchers, File collectionPath, File indexPath) throws SearchableNotSupportedException, IndexException;

    /**
     * Adiciona la colección de datos pasada por parámetros, al índice que se encuentra
     * en la dirección especificada, para los buscadores seleccionados
     *
     * @param searchers       buscadores
     * @param collectionPath colección de datos
     * @param indexPath      dirección del índice
     *
     * @return cantidad de documentos indexados
     *
     * @throws SearchableNotSupportedException si el buscador especificado no es soportado
     * @throws IndexException si ocurre algún error en el proceso de indexación
     *
     * <br>
     * <br>
     * <b>Nota:</b>
     * <br>
     * Las constantes que representan los diferentes buscadores soportados, se encuentran
     * definidas en la clase <code>KeySearchable</code>, del paquete drakkar.oar.util
     * <br>
     * <br>
     *
     * @see KeySearchable
     *
     */
    public long updateIndex(int[] searchers, List<File> collectionPath, File indexPath) throws SearchableNotSupportedException, IndexException;

    /**
     * Adiciona la colección de datos pasada por parámetros, al índice que se encuentra
     * en la dirección por defecto, para los buscadores seleccionados
     *
     * @param searchers       buscadores
     * @param collectionPath colección de datos
     *
     * @return cantidad de documentos indexados
     *
     * @throws SearchableNotSupportedException si el buscador especificado no es soportado
     * @throws IndexException si ocurre algún error en el proceso de indexación
     *
     * <br>
     * <br>
     * <b>Nota:</b>
     * <br>
     * Las constantes que representan los diferentes buscadores soportados, se encuentran
     * definidas en la clase <code>KeySearchable</code>, del paquete drakkar.oar.util
     * <br>
     * <br>
     *
     * @see KeySearchable
     *
     */
    public long updateIndex(int[] searchers, File collectionPath) throws SearchableNotSupportedException, IndexException;

    /**
     * Adiciona la colección de datos pasada por parámetros, al índice que se encuentra
     * en la dirección por defecto, para los buscadores seleccionados
     *
     * @param searchers      buscadores
     * @param collectionPath colección de datos
     *
     * @return cantidad de documentos indexados
     *
     * @throws SearchableNotSupportedException si el buscador especificado no es soportado
     * @throws IndexException si ocurre algún error en el proceso de indexación
     *
     * <br>
     * <br>
     * <b>Nota:</b>
     * <br>
     * Las constantes que representan los diferentes buscadores soportados, se encuentran
     * definidas en la clase <code>KeySearchable</code>, del paquete drakkar.oar.util
     * <br>
     * <br>
     *
     * @see KeySearchable
     *
     */
    public long updateIndex(int[] searchers, List<File> collectionPath) throws SearchableNotSupportedException, IndexException;

    /**
     * Carga del ó los indices ubicados el la dirección por defecto, para todos los
     * buscadores activos en el servidor
     *
     * @return true si se cargó él  ó los indices satisfactoriamente, false en caso contrario
     *
     * @throws IndexException si ocurre algún error en el proceso de carga del índice
     */
    public boolean loadIndex() throws IndexException;

    /**
     * Carga el ó los indices ubicados el la dirección especificada, para todos los
     * buscadores activos en el servidor
     *
     * @param indexPath dirección de el ó los índices
     *
     * @return true si se cargó él  ó los indices satisfactoriamente, false en caso contrario
     *
     * @throws IndexException si ocurre algún error en el proceso de carga del índice
     */
    public boolean loadIndex(File indexPath) throws IndexException;

    /**
     * Carga el ó los indices ubicados el la dirección por defecto, para el
     * buscador seleccionado
     *
     * @param searcher buscador
     *
     * @return true si se cargó él  ó los indices satisfactoriamente, false en caso contrario
     *
     * @throws IndexException si ocurre algún error en el proceso de carga del índice
     * @throws SearchableNotSupportedException si el buscador especificado no es soportado
     *
     * <br>
     * <br>
     * <b>Nota:</b>
     * <br>
     * Las constantes que representan los diferentes buscadores soportados, se encuentran
     * definidas en la clase <code>KeySearchable</code>, del paquete drakkar.oar.util
     * <br>
     * <br>
     *
     * @see KeySearchable
     *
     */
    public boolean loadIndex(int searcher) throws IndexException, SearchableNotSupportedException;

    /**
     * Carga el ó los indices ubicados el la dirección por defecto, para los
     * buscadores seleccionados
     *
     * @param searchers buscadorer
     *
     * @return true si se cargó él  ó los indices satisfactoriamente, false en caso contrario
     *
     * @throws IndexException si ocurre algún error en el proceso de carga del índice
     * @throws SearchableNotSupportedException si el buscador especificado no es soportado
     *
     * <br>
     * <br>
     * <b>Nota:</b>
     * <br>
     * Las constantes que representan los diferentes buscadores soportados, se encuentran
     * definidas en la clase <code>KeySearchable</code>, del paquete drakkar.oar.util
     * <br>
     * <br>
     *
     * @see KeySearchable
     *
     */
    public boolean loadIndex(int[] searchers) throws IndexException, SearchableNotSupportedException;

    /**
     * Carga el ó los indices ubicados el la dirección por especificada, para el
     * buscador seleccionado
     *
     * @param searcher  buscador
     * @param indexPath dirección de el ó los índices
     *
     * @return true si se cargó él  ó los indices satisfactoriamente, false en caso contrario
     *
     * @throws IndexException si ocurre algún error en el proceso de carga del índice
     * @throws SearchableNotSupportedException si el buscador especificado no es soportado
     *
     * <br>
     * <br>
     * <b>Nota:</b>
     * <br>
     * Las constantes que representan los diferentes buscadores soportados, se encuentran
     * definidas en la clase <code>KeySearchable</code>, del paquete drakkar.oar.util
     * <br>
     * <br>
     *
     * @see KeySearchable
     *
     */
    public boolean loadIndex(int searcher, File indexPath) throws SearchableNotSupportedException, IndexException;

    /**
     * Carga el ó los indices ubicados el la dirección por especificada, para los
     * buscadores seleccionados
     *
     * @param searchers  buscadores
     * @param indexPath dirección de el ó los índices
     *
     * @return true si se cargó él  ó los indices satisfactoriamente, false en caso contrario
     *
     * @throws IndexException si ocurre algún error en el proceso de carga del índice
     * @throws SearchableNotSupportedException si el buscador especificado no es soportado
     *
     * <br>
     * <br>
     * <b>Nota:</b>
     * <br>
     * Las constantes que representan los diferentes buscadores soportados, se encuentran
     * definidas en la clase <code>KeySearchable</code>, del paquete drakkar.oar.util
     * <br>
     * <br>
     *
     * @see KeySearchable
     *
     */
    public boolean loadIndex(int[] searchers, File indexPath) throws SearchableNotSupportedException, IndexException;
}
