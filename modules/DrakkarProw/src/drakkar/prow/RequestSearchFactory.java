/*
 * DrakkarKeel - An Enterprise Collaborative Search Platform
 *
 * The contents of this file are subject under the terms described in the
 * DRAKKARKEEL_LICENSE file included in this distribution; you may not use this
 * file except in compliance with the License. 
 *
 * 2013-2014 DrakkarKeel Platform.
 */
package drakkar.prow;

import drakkar.oar.Request;
import static drakkar.oar.util.KeyTransaction.*;
import static drakkar.oar.util.SeekerAction.*;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


/**
 * Esta clase es permite construir objetos Request para efectuar los diferentes
 * métodos de búsquedas soportados por DrakkarKeel
 */
public class RequestSearchFactory implements Serializable{
    private static final long serialVersionUID = 80000000000009L;

    
    
    public static final String DEFAULT_SESSION = "DefaultSession";
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

    /**
     * Devuelve un objeto Request para efectuar una búsqueda determinada, a partir
     * de los parámetros de entrada aplicando los principios:
     * <br>
     * <br>
     * <tt>- Single Search Engine</tt><br>
     * <tt>- Single Search Engine and Split</tt>
     * <br>
     * <br>
     * @param sessionName   nombre de la sesión
     * @param query         consulta de la búsqueda
     * @param searcher      identificador del identificador del buscador a emplear
     * @param principle     principio de búsqueda a efectuar
     * @param caseSentitive tener en cuenta las minúsculas y mayúsculas
     *
     * @return              objeto request
     */
    public static Request create(String sessionName, String query, int searcher, int principle, boolean caseSentitive) {

            Map<Object, Object> hash = new HashMap<>(6);
            hash.put(OPERATION, SEARCH_QRY__SS_SSSPLIT);
            hash.put(SESSION_NAME, sessionName);
            hash.put(QUERY, query);
            hash.put(SEARCHER, searcher);
            hash.put(SEARCH_PRINCIPLE, principle);
            hash.put(CASE_SENTITIVE, caseSentitive);
      
            
            
            return new Request(hash);
        
    }

    /**
     * Devuelve un objeto Request para efectuar una búsqueda determinada, a partir
     * de los parámetros de entrada aplicando los principios:
     * <br>
     * <br>
     * <tt>- Single Search Engine</tt><br>
     * <tt>- Single Search Engine and Split</tt>
     * <br>
     * <br>
     * @param sessionName   nombre de la sesión
     * @param query         consulta de la búsqueda
     * @param field         campo del documento
     * @param searcher      identificador del buscador a emplear
     * @param principle     principio de búsqueda a efectuar
     * @param caseSentitive tener en cuenta las minúsculas y mayúsculas
     *
     * @return              objeto request
     */
    public static Request create(String sessionName, String query, int field, int searcher, int principle, boolean caseSentitive) {
        Map<Object, Object> hash = new HashMap<>(7);
        hash.put(OPERATION, SEARCH_QRY_FLD__SS_SSSPLIT);
        hash.put(SESSION_NAME, sessionName);
        hash.put(QUERY, query);
        hash.put(FIELD, field);
        hash.put(SEARCHER, searcher);
        hash.put(SEARCH_PRINCIPLE, principle);
        hash.put(CASE_SENTITIVE, caseSentitive);

        return new Request(hash);
    }

    /**
     * Devuelve un objeto Request para efectuar una búsqueda determinada, a partir
     * de los parámetros de entrada aplicando los principios:
     * <br>
     * <br>
     * <tt>- Single Search Engine</tt><br>
     * <tt>- Single Search Engine and Split</tt>
     * <br>
     * <br>
     *
     * @param sessionName   nombre de la sesión
     * @param query         consulta de la búsqueda
     * @param fields        campos del documento
     * @param searcher      identificador del buscador a emplear
     * @param principle     principio de búsqueda a efectuar
     * @param caseSentitive tener en cuenta las minúsculas y mayúsculas
     *
     * @return              objeto request
     */
    public static Request create(String sessionName, String query, int[] fields, int searcher, int principle, boolean caseSentitive) {
        Map<Object, Object> hash = new HashMap<>(7);
        hash.put(OPERATION, SEARCH_QRY_FLDS__SS_SSSPLIT);
        hash.put(SESSION_NAME, sessionName);
        hash.put(QUERY, query);
        hash.put(FIELDS, fields);
        hash.put(SEARCHER, searcher);
        hash.put(SEARCH_PRINCIPLE, principle);
        hash.put(CASE_SENTITIVE, caseSentitive);

        return new Request(hash);
    }

    /**
     * Devuelve un objeto Request para efectuar una búsqueda determinada, a partir
     * de los parámetros de entrada aplicando los principios:
     * <br>
     * <br>
     * <tt>- Single Search Engine</tt><br>
     * <tt>- Single Search Engine and Split</tt>
     * <br>
     * <br>
     *
     * @param sessionName   nombre de la sesión
     * @param query         consulta de la búsqueda
     * @param docType       tipo de documento(extensión)
     * @param searcher      identificador del buscador a emplear
     * @param principle     principio de búsqueda a efectuar
     * @param caseSentitive tener en cuenta las minúsculas y mayúsculas
     *
     * @return              objeto request
     */
    public static Request create(String sessionName, String query, String docType, int searcher, int principle, boolean caseSentitive) {

        Map<Object, Object> hash = new HashMap<>(7);
        hash.put(OPERATION, SEARCH_QRY_DOCTYPE__SS_SSSPLIT);
        hash.put(SESSION_NAME, sessionName);
        hash.put(QUERY, query);
        hash.put(DOC_TYPE, docType);
        hash.put(SEARCHER, searcher);
        hash.put(SEARCH_PRINCIPLE, principle);
        hash.put(CASE_SENTITIVE, caseSentitive);

        return new Request(hash);
    }

    /**
     * Este método realiza una búsqueda avanzada, utilizando la extensión de los documentos
     * como filtro para la obtención de los resultados.
     * <br>
     * <br>
     * <tt>- Single Search Engine</tt><br>
     * <tt>- Single Search Engine and Split</tt>
     * <br>
     * <br>
     *
     * @param sessionName   nombre de la sesión.
     * @param query         consulta de la búsqueda
     * @param docTypes      tipos de documentos(extensiones)    
     * @param searcher      identificador del buscador a emplear
     * @param principle     principio de búsqueda a efectuar
     * @param caseSentitive tener en cuenta las minúsculas y mayúsculas
     *
     * @return              objeto request
     */
    public static Request create(String sessionName, String query, String[] docTypes, int searcher, int principle, boolean caseSentitive) {
        Map<Object, Object> hash = new HashMap<>(6);
        hash.put(OPERATION, SEARCH_QRY_DOCTYPES__SS_SSSPLIT);
        hash.put(SESSION_NAME, sessionName);
        hash.put(QUERY, query);
        hash.put(DOC_TYPES, docTypes);
        hash.put(SEARCHER, searcher);
        hash.put(SEARCH_PRINCIPLE, principle);
        hash.put(CASE_SENTITIVE, caseSentitive);

        return new Request(hash);
    }

    /**
     * Devuelve un objeto Request para efectuar una búsqueda determinada, a partir de los parámetros
     * de entrada aplicando los principios:
     * <br>
     * <br>
     * <tt>- Single Search Engine</tt><br>
     * <tt>- Single Search Engine and Split</tt>
     * <br>
     * <br>
     *
     * @param sessionName   nombre de la sesión
     * @param query         consulta de la búsqueda
     * @param docType       tipo de documento(extensión)
     * @param field         campo del documento
     * @param searcher      identificador del buscador a emplear
     * @param principle     principio de búsqueda a efectuar
     * @param caseSentitive tener en cuenta las minúsculas y mayúsculas
     *
     * @return              objeto request
     */
    public static Request create(String sessionName, String query, String docType, int field, int searcher, int principle, boolean caseSentitive) {
        Map<Object, Object> hash = new HashMap<>(8);
        hash.put(OPERATION, SEARCH_QRY_DOCTYPE_FLD__SS_SSSPLIT);
        hash.put(SESSION_NAME, sessionName);
        hash.put(QUERY, query);
        hash.put(DOC_TYPE, docType);
        hash.put(FIELD, field);
        hash.put(SEARCHER, searcher);
        hash.put(SEARCH_PRINCIPLE, principle);
        hash.put(CASE_SENTITIVE, caseSentitive);

        return new Request(hash);
    }

    /**
     * Devuelve un objeto Request para efectuar una búsqueda determinada, a partir 
     * de los parámetros de entrada aplicando los principios:
     *
     * <br>
     * <br>
     * <tt>- Single Search Engine</tt><br>
     * <tt>- Single Search Engine and Split</tt>
     * <br>
     * <br>
     *
     * @param sessionName   nombre de la sesión
     * @param query         consulta de la búsqueda
     * @param docType       tipo de documento(extensión)
     * @param fields        campos del documento
     * @param searcher      identificador del buscador a emplear
     * @param principle     principio de búsqueda a efectuar
     * @param caseSentitive tener en cuenta las minúsculas y mayúsculas
     *
     * @return              objeto request
     */
    public static Request create(String sessionName, String query, String docType, int[] fields, int searcher, int principle, boolean caseSentitive) {
        Map<Object, Object> hash = new HashMap<>(8);
        hash.put(OPERATION, SEARCH_QRY_DOCTYPE_FLDS__SS_SSSPLIT);
        hash.put(SESSION_NAME, sessionName);
        hash.put(QUERY, query);
        hash.put(DOC_TYPE, docType);
        hash.put(FIELDS, fields);
        hash.put(SEARCHER, searcher);
        hash.put(SEARCH_PRINCIPLE, principle);
        hash.put(CASE_SENTITIVE, caseSentitive);

        return new Request(hash);
    }

    /**
     * Devuelve un objeto Request para efectuar una búsqueda determinada, a partir
     * de los parámetros de entrada aplicando los principios:
     * <br>
     * <br>
     * <tt>- Single Search Engine</tt><br>
     * <tt>- Single Search Engine and Split</tt>
     * <br>
     * <br>
     *
     * @param sessionName   nombre de la sesión
     * @param query         consulta de la búsqueda
     * @param docTypes      tipos de documentos(extensiones)
     * @param field         campo del documento
     * @param searcher      identificador del buscador a emplear
     * @param principle     principio de búsqueda a efectuar
     * @param caseSentitive tener en cuenta las minúsculas y mayúsculas
     *
     * @return              objeto request
     */
    public static Request create(String sessionName, String query, String[] docTypes, int field, int searcher, int principle, boolean caseSentitive) {
        Map<Object, Object> hash = new HashMap<>(8);
        hash.put(OPERATION, SEARCH_QRY_DOCTYPES_FLD__SS_SSSPLIT);
        hash.put(SESSION_NAME, sessionName);
        hash.put(QUERY, query);
        hash.put(DOC_TYPES, docTypes);
        hash.put(FIELD, field);
        hash.put(SEARCHER, searcher);
        hash.put(SEARCH_PRINCIPLE, principle);
        hash.put(CASE_SENTITIVE, caseSentitive);

        return new Request(hash);
    }

    /**
     * Devuelve un objeto Request para efectuar una búsqueda determinada, a partir
     * de los parámetros de entrada aplicando los principios:
     * <br>
     * <br>
     * <tt>- Single Search Engine</tt><br>
     * <tt>- Single Search Engine and Split</tt>
     * <br>
     * <br>
     *
     * @param sessionName   nombre de la sesión
     * @param query         consulta de la búsqueda
     * @param docTypes      tipos de documentos(extensiones)
     * @param fields        campos del documento
     * @param searcher    identificador del buscador a emplear
     * @param principle     principio de búsqueda a efectuar
     * @param caseSentitive tener en cuenta las minúsculas y mayúsculas
     *
     * @return              objeto request
     */
    public static Request create(String sessionName, String query, String[] docTypes, int[] fields, int searcher, int principle, boolean caseSentitive) {
        Map<Object, Object> hash = new HashMap<>(8);
        hash.put(OPERATION, SEARCH_QRY_DOCTYPES_FLDS__SS_SSSPLIT);
        hash.put(SESSION_NAME, sessionName);
        hash.put(QUERY, query);
        hash.put(DOC_TYPES, docTypes);
        hash.put(FIELDS, fields);
        hash.put(SEARCHER, searcher);
        hash.put(SEARCH_PRINCIPLE, principle);
        hash.put(CASE_SENTITIVE, caseSentitive);

        return new Request(hash);
    }

    /**
     * Devuelve un objeto Request para efectuar una búsqueda determinada, a partir 
     * de los parámetros de entrada aplicando los principios:
     * <br>
     * <br>
     * <tt>- Meta Search Engine</tt><br>
     * <tt>- Meta Search Engine and Split</tt><br>
     * <tt>- Meta Search Engine and Switch</tt>
     * <br>
     * <br>
     * @param sessionName   nombre de la sesión
     * @param query         consulta de la búsqueda
     * @param principle     principio de búsqueda a efectuar
     * @param caseSentitive tener en cuenta las minúsculas y mayúsculas
     *
     * @return              objeto request
     */
    public static Request create(String sessionName, int principle, String query, boolean caseSentitive) {
        Map<Object, Object> hash = new HashMap<>(5);
        hash.put(OPERATION, SEARCH_QRY__MS_MSSPLIT_MSSWITCH);
        hash.put(SESSION_NAME, sessionName);
        hash.put(QUERY, query);
        hash.put(SEARCH_PRINCIPLE, principle);
        hash.put(CASE_SENTITIVE, caseSentitive);

        return new Request(hash);
    }

    /**
     * Devuelve un objeto Request para efectuar una búsqueda determinada, a partir
     * de los parámetros de entrada aplicando los principios:
     * <br>
     * <br>
     * <tt>- Meta Search Engine</tt><br>
     * <tt>- Meta Search Engine and Split</tt><br>
     * <tt>- Meta Search Engine and Switch</tt>
     * <br>
     * <br>
     * @param sessionName   nombre de la sesión
     * @param query         consulta de la búsqueda
     * @param principle     principio de búsqueda a efectuar
     * @param caseSentitive tener en cuenta las minúsculas y mayúsculas
     *
     * @return              objeto request
     */
    public static Request create(String query, int principle) {
        Map<Object, Object> hash = new HashMap<>(5);
        hash.put(OPERATION, SEARCH_QRY__MS_MSSPLIT_MSSWITCH);
        hash.put(SESSION_NAME, RequestSearchFactory.DEFAULT_SESSION);
        hash.put(QUERY, query);
        hash.put(SEARCH_PRINCIPLE, principle);
        hash.put(CASE_SENTITIVE, false);

        return new Request(hash);
    }

    /**
     * Devuelve un objeto Request para efectuar una búsqueda determinada, a partir
     * de los parámetros de entrada aplicando los principios:
     * <br>
     * <br>
     * <tt>- Meta Search Engine</tt><br>
     * <tt>- Meta Search Engine and Split</tt><br>
     * <tt>- Meta Search Engine and Switch</tt>
     * <br>
     * <br>
     * @param sessionName   nombre de la sesión
     * @param query         consulta de la búsqueda
     * @param principle     principio de búsqueda a efectuar
     * @param caseSentitive tener en cuenta las minúsculas y mayúsculas
     *
     * @return              objeto request
     */
    public static Request create(String sessionName, String query, int principle) {
        Map<Object, Object> hash = new HashMap<>(5);
        hash.put(OPERATION, SEARCH_QRY__MS_MSSPLIT_MSSWITCH);
        hash.put(SESSION_NAME, sessionName);
        hash.put(QUERY, query);
        hash.put(SEARCH_PRINCIPLE, principle);
        hash.put(CASE_SENTITIVE, false);

        return new Request(hash);
    }

    /**
     * Devuelve un objeto Request para efectuar una búsqueda determinada, a partir
     * de los parámetros de entrada aplicando los principios:
     * <br>
     * <br>
     * <tt>- Meta Search Engine</tt><br>
     * <tt>- Meta Search Engine and Split</tt><br>
     * <tt>- Meta Search Engine and Switch</tt>
     * <br>
     * <br>
     *
     * @param sessionName   nombre de la sesión
     * @param query         consulta de la búsqueda
     * @param principle     principio de búsqueda a efectuar
     * @param field         campo del documento
     * @param caseSentitive tener en cuenta las minúsculas y mayúsculas
     *
     * @return              objeto request
     */
    public static Request create(String sessionName, int principle, String query, int field, boolean caseSentitive) {
        Map<Object, Object> hash = new HashMap<>(6);
        hash.put(OPERATION, SEARCH_QRY_FLD__MS_MSSPLIT_MSSWITCH);
        hash.put(SESSION_NAME, sessionName);
        hash.put(QUERY, query);
        hash.put(FIELD, field);
        hash.put(SEARCH_PRINCIPLE, principle);
        hash.put(CASE_SENTITIVE, caseSentitive);

        return new Request(hash);
    }

    /**
     * Devuelve un objeto Request para efectuar una búsqueda determinada, a partir 
     * de los parámetros de entrada aplicando los principios:
     * <br>
     * <br>
     * <tt>- Meta Search Engine</tt><br>
     * <tt>- Meta Search Engine and Split</tt><br>
     * <tt>- Meta Search Engine and Switch</tt>
     * <br>
     * <br>
     *
     * @param sessionName   nombre de la sesión
     * @param query         consulta de la búsqueda
     * @param principle     principio de búsqueda a efectuar
     * @param fields        campos del documento
     * @param caseSentitive tener en cuenta las minúsculas y mayúsculas
     *
     * @return              objeto request
     */
    public static Request create(String sessionName, int principle, String query, int[] fields, boolean caseSentitive) {
        Map<Object, Object> hash = new HashMap<>(6);
        hash.put(OPERATION, SEARCH_QRY_FLDS__MS_MSSPLIT_MSSWITCH);
        hash.put(SESSION_NAME, sessionName);
        hash.put(QUERY, query);
        hash.put(FIELDS, fields);
        hash.put(SEARCH_PRINCIPLE, principle);
        hash.put(CASE_SENTITIVE, caseSentitive);

        return new Request(hash);
    }

    /**
     * Devuelve un objeto Request para efectuar una búsqueda determinada, a partir
     * de los parámetros de entrada aplicando los principios:
     * <br>
     * <br>
     * <tt>- Meta Search Engine</tt><br>
     * <tt>- Meta Search Engine and Split</tt><br>
     * <tt>- Meta Search Engine and Switch</tt>
     * <br>
     * <br>
     *
     * @param sessionName   nombre de la sesión
     * @param query         consulta de la búsqueda
     * @param principle     principio de búsqueda a efectuar
     * @param docType       tipo de documento(extensión)
     * @param caseSentitive tener en cuenta las minúsculas y mayúsculas
     *
     * @return              objeto request
     */
    public static Request create(String sessionName, int principle, String query, String docType, boolean caseSentitive) {
        Map<Object, Object> hash = new HashMap<>(6);
        hash.put(OPERATION, SEARCH_QRY_DOCTYPE__MS_MSSPLIT_MSSWITCH);
        hash.put(SESSION_NAME, sessionName);
        hash.put(QUERY, query);
        hash.put(DOC_TYPE, docType);
        hash.put(SEARCH_PRINCIPLE, principle);
        hash.put(CASE_SENTITIVE, caseSentitive);

        return new Request(hash);
    }

    /**
     * Devuelve un objeto Request para efectuar una búsqueda determinada, a partir
     * de los parámetros de entrada aplicando los principios:
     * <br>
     * <br>
     * <tt>- Meta Search Engine</tt><br>
     * <tt>- Meta Search Engine and Split</tt><br>
     * <tt>- Meta Search Engine and Switch</tt>
     * <br>
     * <br>
     *
     * @param sessionName   nombre de la sesión
     * @param query         consulta de la búsqueda
     * @param principle     principio de búsqueda a efectuar
     * @param docTypes      tipos de documentos(extensiones)
     * @param caseSentitive tener en cuenta las minúsculas y mayúsculas
     *
     * @return              objeto request
     */
    public static Request create(String sessionName, int principle, String query, String[] docTypes, boolean caseSentitive) {
        Map<Object, Object> hash = new HashMap<>(6);
        hash.put(OPERATION, SEARCH_QRY_DOCTYPES__MS_MSSPLIT_MSSWITCH);
        hash.put(SESSION_NAME, sessionName);
        hash.put(QUERY, query);
        hash.put(DOC_TYPES, docTypes);
        hash.put(SEARCH_PRINCIPLE, principle);
        hash.put(CASE_SENTITIVE, caseSentitive);

        return new Request(hash);
    }

    /**
     * Devuelve un objeto Request para efectuar una búsqueda determinada, a partir
     * de los parámetros de entrada aplicando los principios:
     * <br>
     * <br>
     * <tt>- Meta Search Engine</tt><br>
     * <tt>- Meta Search Engine and Split</tt><br>
     * <tt>- Meta Search Engine and Switch</tt>
     * <br>
     * <br>
     *
     * @param sessionName   nombre de la sesión
     * @param query         consulta de la búsqueda
     * @param principle     principio de búsqueda a efectuar
     * @param docType       tipo de documento(extensión)
     * @param field         campo del documento
     * @param caseSentitive tener en cuenta las minúsculas y mayúsculas
     *
     * @return              objeto request
     */
    public static Request create(String sessionName, int principle, String query, String docType, int field, boolean caseSentitive) {
        Map<Object, Object> hash = new HashMap<>(7);
        hash.put(OPERATION, SEARCH_QRY_DOCTYPE_FLD__MS_MSSPLIT_MSSWITCH);
        hash.put(SESSION_NAME, sessionName);
        hash.put(QUERY, query);
        hash.put(DOC_TYPE, docType);
        hash.put(FIELD, field);
        hash.put(SEARCH_PRINCIPLE, principle);
        hash.put(CASE_SENTITIVE, caseSentitive);

        return new Request(hash);
    }

    /**
     * Devuelve un objeto Request para efectuar una búsqueda determinada, a partir
     * de los parámetros de entrada aplicando los principios:
     * <br>
     * <br>
     * <tt>- Meta Search Engine</tt><br>
     * <tt>- Meta Search Engine and Split</tt><br>
     * <tt>- Meta Search Engine and Switch</tt>
     * <br>
     * <br>
     *
     * @param sessionName   nombre de la sesión
     * @param query         consulta de la búsqueda
     * @param principle     principio de búsqueda a efectuar
     * @param docType       tipo de documento(extensión)
     * @param fields         campos del documento
     * @param caseSentitive tener en cuenta las minúsculas y mayúsculas
     *
     * @return              objeto request
     */
    public static Request create(String sessionName, int principle, String query, String docType, int[] fields, boolean caseSentitive) {
        Map<Object, Object> hash = new HashMap<>(7);
        hash.put(OPERATION, SEARCH_QRY_DOCTYPE_FLDS__MS_MSSPLIT_MSSWITCH);
        hash.put(SESSION_NAME, sessionName);
        hash.put(QUERY, query);
        hash.put(DOC_TYPE, docType);
        hash.put(FIELDS, fields);
        hash.put(SEARCH_PRINCIPLE, principle);
        hash.put(CASE_SENTITIVE, caseSentitive);

        return new Request(hash);
    }

    /**
     * Devuelve un objeto Request para efectuar una búsqueda determinada, a partir
     * de los parámetros de entrada aplicando los principios:
     * <br>
     * <br>
     * <tt>- Meta Search Engine</tt><br>
     * <tt>- Meta Search Engine and Split</tt><br>
     * <tt>- Meta Search Engine and Switch</tt>
     * <br>
     * <br>
     *
     * @param sessionName   nombre de la sesión
     * @param query         consulta de la búsqueda
     * @param principle     principio de búsqueda a efectuar
     * @param docTypes      tipos de documentos(extensiones)
     * @param field         campo del documento
     * @param caseSentitive tener en cuenta las minúsculas y mayúsculas
     *
     * @return              objeto request
     */
    public static Request create(String sessionName, int principle, String query, String[] docTypes, int field, boolean caseSentitive) {
        Map<Object, Object> hash = new HashMap<>(7);
        hash.put(OPERATION, SEARCH_QRY_DOCTYPES_FLD__MS_MSSPLIT_MSSWITCH);
        hash.put(SESSION_NAME, sessionName);
        hash.put(QUERY, query);
        hash.put(DOC_TYPES, docTypes);
        hash.put(FIELD, field);
        hash.put(SEARCH_PRINCIPLE, principle);
        hash.put(CASE_SENTITIVE, caseSentitive);

        return new Request(hash);
    }

    /**
     * Devuelve un objeto Request para efectuar una búsqueda determinada, a partir
     * de los parámetros de entrada aplicando los principios:
     * <br>
     * <br>
     * <tt>- Meta Search Engine</tt><br>
     * <tt>- Meta Search Engine and Split</tt><br>
     * <tt>- Meta Search Engine and Switch</tt>
     * <br>
     * <br>
     *
     * @param sessionName   nombre de la sesión
     * @param query         consulta de la búsqueda
     * @param principle     principio de búsqueda a efectuar
     * @param docTypes      tipos de documentos(extensiones)
     * @param fields         campos del documento
     * @param caseSentitive tener en cuenta las minúsculas y mayúsculas
     *
     * @return              objeto request
     */
    public static Request create(String sessionName, int principle, String query, String[] docTypes, int[] fields, boolean caseSentitive) {
        Map<Object, Object> hash = new HashMap<>(7);
        hash.put(OPERATION, SEARCH_QRY_DOCTYPES_FLDS__MS_MSSPLIT_MSSWITCH);
        hash.put(SESSION_NAME, sessionName);
        hash.put(QUERY, query);
        hash.put(DOC_TYPES, docTypes);
        hash.put(FIELDS, fields);
        hash.put(SEARCH_PRINCIPLE, principle);
        hash.put(CASE_SENTITIVE, caseSentitive);

        return new Request(hash);
    }

    /**
     * Devuelve un objeto Request para efectuar una búsqueda determinada, a partir
     * de los parámetros de entrada aplicando los principios:
     * <br>
     * <br>
     * <tt>- Meta Search Engine</tt><br>
     * <tt>- Meta Search Engine and Split</tt><br>
     * <tt>- Meta Search Engine and Switch</tt>
     * <br>
     * <br>
     *
     * @param sessionName   nombre de la sesión
     * @param searchers   identificadores del buscadores
     * @param query         consulta de la búsqueda
     * @param principle     principio de búsqueda a efectuar
     * @param caseSentitive tener en cuenta las minúsculas y mayúsculas
     *
     * @return              objeto request
     */
    public static Request create(String sessionName, int[] searchers, int principle, String query, boolean caseSentitive) {
        Map<Object, Object> hash = new HashMap<>(6);
        hash.put(OPERATION, SEARCH_QRY__SEARCHERS__MS_MSSPLIT_MSSWITCH);
        hash.put(SESSION_NAME, sessionName);
        hash.put(QUERY, query);
        hash.put(SEARCHERS, searchers);
        hash.put(SEARCH_PRINCIPLE, principle);
        hash.put(CASE_SENTITIVE, caseSentitive);

        return new Request(hash);
    }

    /**
     * Devuelve un objeto Request para efectuar una búsqueda determinada, a partir
     * de los parámetros de entrada aplicando los principios:
     * <br>
     * <br>
     * <tt>- Meta Search Engine</tt><br>
     * <tt>- Meta Search Engine and Split</tt><br>
     * <tt>- Meta Search Engine and Switch</tt>
     * <br>
     * <br>
     *
     * @param sessionName   nombre de la sesión
     * @param searchers   identificadores del buscadores
     * @param query         consulta de la búsqueda
     * @param principle     principio de búsqueda a efectuar
     * @param field         campo del documento
     * @param caseSentitive tener en cuenta las minúsculas y mayúsculas
     *
     * @return              objeto request
     */
    public static Request create(String sessionName, int[] searchers, int principle, String query, int field, boolean caseSentitive) {
        Map<Object, Object> hash = new HashMap<>(7);
        hash.put(OPERATION, SEARCH_QRY_FLD__SEARCHERS__MS_MSSPLIT_MSSWITCH);
        hash.put(SESSION_NAME, sessionName);
        hash.put(QUERY, query);
        hash.put(FIELD, field);
        hash.put(SEARCHERS, searchers);
        hash.put(SEARCH_PRINCIPLE, principle);
        hash.put(CASE_SENTITIVE, caseSentitive);

        return new Request(hash);
    }

    /**
     * Devuelve un objeto Request para efectuar una búsqueda determinada, a partir
     * de los parámetros de entrada aplicando los principios:
     * <br>
     * <br>
     * <tt>- Meta Search Engine</tt><br>
     * <tt>- Meta Search Engine and Split</tt><br>
     * <tt>- Meta Search Engine and Switch</tt>
     * <br>
     * <br>
     *
     * @param sessionName   nombre de la sesión
     * @param searchers   identificadores del buscadores
     * @param query         consulta de la búsqueda
     * @param principle     principio de búsqueda a efectuar
     * @param fields        campos del documento
     * @param caseSentitive tener en cuenta las minúsculas y mayúsculas
     *
     * @return              objeto request
     */
    public static Request create(String sessionName, int[] searchers, int principle, String query, int[] fields, boolean caseSentitive) {
        Map<Object, Object> hash = new HashMap<>(7);
        hash.put(OPERATION, SEARCH_QRY_FLDS__SEARCHERS__MS_MSSPLIT_MSSWITCH);
        hash.put(SESSION_NAME, sessionName);
        hash.put(QUERY, query);
        hash.put(FIELDS, fields);
        hash.put(SEARCHERS, searchers);
        hash.put(SEARCH_PRINCIPLE, principle);
        hash.put(CASE_SENTITIVE, caseSentitive);

        return new Request(hash);
    }

    /**
     * Devuelve un objeto Request para efectuar una búsqueda determinada, a partir
     * de los parámetros de entrada aplicando los principios:
     * <br>
     * <br>
     * <tt>- Meta Search Engine</tt><br>
     * <tt>- Meta Search Engine and Split</tt><br>
     * <tt>- Meta Search Engine and Switch</tt>
     * <br>
     * <br>
     *
     * @param sessionName   nombre de la sesión
     * @param searchers   identificadores del buscadores
     * @param query         consulta de la búsqueda
     * @param principle     principio de búsqueda a efectuar
     * @param docType       tipo de documento(extensión)
     * @param caseSentitive tener en cuenta las minúsculas y mayúsculas
     *
     * @return              objeto request
     */
    public static Request create(String sessionName, int[] searchers, int principle, String query, String docType, boolean caseSentitive) {
        Map<Object, Object> hash = new HashMap<>(7);
        hash.put(OPERATION, SEARCH_QRY_DOCTYPE__SEARCHERS__MS_MSSPLIT_MSSWITCH);
        hash.put(SESSION_NAME, sessionName);
        hash.put(QUERY, query);
        hash.put(DOC_TYPE, docType);
        hash.put(SEARCHERS, searchers);
        hash.put(SEARCH_PRINCIPLE, principle);
        hash.put(CASE_SENTITIVE, caseSentitive);

        return new Request(hash);
    }

    /**
     * Devuelve un objeto Request para efectuar una búsqueda determinada, a partir
     * de los parámetros de entrada aplicando los principios:
     * <br>
     * <br>
     * <tt>- Meta Search Engine</tt><br>
     * <tt>- Meta Search Engine and Split</tt><br>
     * <tt>- Meta Search Engine and Switch</tt>
     * <br>
     * <br>
     *
     * @param sessionName   nombre de la sesión
     * @param searchers     identificadores del buscadores
     * @param query         consulta de la búsqueda
     * @param principle     principio de búsqueda a efectuar
     * @param docTypes      tipos de documentos(extensiones)
     * @param caseSentitive tener en cuenta las minúsculas y mayúsculas
     *
     * @return              objeto request
     */
    public static Request create(String sessionName, int[] searchers, int principle, String query, String[] docTypes, boolean caseSentitive) {
        Map<Object, Object> hash = new HashMap<>(7);
        hash.put(OPERATION, SEARCH_QRY_DOCTYPES__SEARCHERS__MS_MSSPLIT_MSSWITCH);
        hash.put(SESSION_NAME, sessionName);
        hash.put(QUERY, query);
        hash.put(DOC_TYPES, docTypes);
        hash.put(SEARCHERS, searchers);
        hash.put(SEARCH_PRINCIPLE, principle);
        hash.put(CASE_SENTITIVE, caseSentitive);

        return new Request(hash);
    }

    /**
     * Devuelve un objeto Request para efectuar una búsqueda determinada, a partir
     * de los parámetros de entrada aplicando los principios:
     * <br>
     * <br>
     * <tt>- Meta Search Engine</tt><br>
     * <tt>- Meta Search Engine and Split</tt><br>
     * <tt>- Meta Search Engine and Switch</tt>
     * <br>
     * <br>
     *
     * @param sessionName   nombre de la sesión
     * @param searchers     identificadores del buscadores
     * @param query         consulta de la búsqueda
     * @param principle     principio de búsqueda a efectuar
     * @param docType       tipo de documento(extensión)
     * @param field         campo del documento
     * @param caseSentitive tener en cuenta las minúsculas y mayúsculas
     *
     * @return              objeto request
     */
    public static Request create(String sessionName, int[] searchers, int principle, String query, String docType, int field, boolean caseSentitive) {
        Map<Object, Object> hash = new HashMap<>(8);
        hash.put(OPERATION, SEARCH_QRY_DOCTYPE_FLD__SEARCHERS__MS_MSSPLIT_MSSWITCH);
        hash.put(SESSION_NAME, sessionName);
        hash.put(QUERY, query);
        hash.put(DOC_TYPE, docType);
        hash.put(FIELD, field);
        hash.put(SEARCHERS, searchers);
        hash.put(SEARCH_PRINCIPLE, principle);
        hash.put(CASE_SENTITIVE, caseSentitive);

        return new Request(hash);
    }

    /**
     * Devuelve un objeto Request para efectuar una búsqueda determinada, a partir
     * de los parámetros de entrada aplicando los principios:
     * <br>
     * <br>
     * <tt>- Meta Search Engine</tt><br>
     * <tt>- Meta Search Engine and Split</tt><br>
     * <tt>- Meta Search Engine and Switch</tt>
     * <br>
     * <br>
     *
     * @param sessionName   nombre de la sesión
     * @param searchers     identificadores del buscadores
     * @param query         consulta de la búsqueda
     * @param principle     principio de búsqueda a efectuar
     * @param docType       tipo de documento(extensión)
     * @param fields        campos del documento
     * @param caseSentitive tener en cuenta las minúsculas y mayúsculas
     *
     * @return              objeto request
     */
    public static Request create(String sessionName, int[] searchers, int principle, String query, String docType, int[] fields, boolean caseSentitive) {
        Map<Object, Object> hash = new HashMap<>(8);
        hash.put(OPERATION, SEARCH_QRY_DOCTYPE_FLDS__SEARCHERS__MS_MSSPLIT_MSSWITCH);
        hash.put(SESSION_NAME, sessionName);
        hash.put(QUERY, query);
        hash.put(DOC_TYPE, docType);
        hash.put(FIELDS, fields);
        hash.put(SEARCHERS, searchers);
        hash.put(SEARCH_PRINCIPLE, principle);
        hash.put(CASE_SENTITIVE, caseSentitive);

        return new Request(hash);
    }

    /**
     * Devuelve un objeto Request para efectuar una búsqueda determinada, a partir
     * de los parámetros de entrada aplicando los principios:
     * <br>
     * <br>
     * <tt>- Meta Search Engine</tt><br>
     * <tt>- Meta Search Engine and Split</tt><br>
     * <tt>- Meta Search Engine and Switch</tt>
     * <br>
     * <br>
     *
     * @param sessionName   nombre de la sesión
     * @param searchers   identificadores del buscadores
     * @param query         consulta de la búsqueda
     * @param principle     principio de búsqueda a efectuar
     * @param docTypes      tipos de documentos(extensiones)
     * @param field         campo del documento
     * @param caseSentitive tener en cuenta las minúsculas y mayúsculas
     *
     * @return              objeto request
     */
    public static Request create(String sessionName, int[] searchers, int principle, String query, String[] docTypes, int field, boolean caseSentitive) {
        Map<Object, Object> hash = new HashMap<>(7);
        hash.put(OPERATION, SEARCH_QRY_DOCTYPES_FLD__SEARCHERS__MS_MSSPLIT_MSSWITCH);
        hash.put(SESSION_NAME, sessionName);
        hash.put(QUERY, query);
        hash.put(DOC_TYPES, docTypes);
        hash.put(FIELD, field);
        hash.put(SEARCHERS, searchers);
        hash.put(SEARCH_PRINCIPLE, principle);
        hash.put(CASE_SENTITIVE, caseSentitive);

        return new Request(hash);
    }

    /**
     * Devuelve un objeto Request para efectuar una búsqueda determinada, a partir
     * de los parámetros de entrada aplicando los principios:
     * <br>
     * <br>
     * <tt>- Meta Search Engine</tt><br>
     * <tt>- Meta Search Engine and Split</tt><br>
     * <tt>- Meta Search Engine and Switch</tt>
     * <br>
     * <br>
     *
     * @param sessionName   nombre de la sesión
     * @param searchers     identificadores del buscadores
     * @param query         consulta de la búsqueda
     * @param principle     principio de búsqueda a efectuar
     * @param docTypes      tipos de documentos(extensiones)
     * @param fields        campos del documento
     * @param caseSentitive tener en cuenta las minúsculas y mayúsculas
     *
     * @return              objeto request
     */
    public static Request create(String sessionName, int[] searchers, int principle, String query, String[] docTypes, int[] fields, boolean caseSentitive) {
        Map<Object, Object> hash = new HashMap<>(8);

        hash.put(OPERATION, SEARCH_QRY_DOCTYPES_FLDS__SEARCHERS__MS_MSSPLIT_MSSWITCH);
        hash.put(SESSION_NAME, sessionName);
        hash.put(QUERY, query);
        hash.put(DOC_TYPES, docTypes);
        hash.put(FIELDS, fields);
        hash.put(SEARCHERS, searchers);
        hash.put(SEARCH_PRINCIPLE, principle);
        hash.put(CASE_SENTITIVE, caseSentitive);

        return new Request(hash);
    }

    public static Request create(String sessionName, String query, String selectedRepository, String fileType, String sortType, String lastModified, boolean fileBody) {

        Map<Object, Object> hash = new HashMap<>(8);
        hash.put(OPERATION, SEARCH_SVN_QRY_FILE_SORT_MODIFIED);
        hash.put(SESSION_NAME, sessionName);
        hash.put(QUERY, query);
        hash.put(SVN_REPOSITORY, selectedRepository);
        hash.put(FILE_TYPE, fileType);
        hash.put(SORT_TYPE, sortType);
        hash.put(LAST_MODIFIED, lastModified);
        hash.put(ONLY_FILE_BODY, fileBody);

        return new Request(hash);


    }
}
