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
 * la construcción de los objetos Request/Response de las distintas operaciones
 */
public class KeyTransaction {

    /**
     * Nombre de la operación
     */
    public static final int OPERATION = 1;
    /**
     * Usuario
     */
    public static final int SEEKER = 2;
    /**
     * Usuario emisor
     */
    public static final int SEEKER_EMITTER = 3;
    /**
     * Usuario receptor
     */
    public static final int MEMBER_RECEPTOR = 4;
    /**
     * Estado del usuario
     */
    public static final int SEEKER_STATE = 5;
    /**
     * Nombre de la sesión
     */
    public static final int SESSION_NAME = 6;
    /**
     * Contenido del mensaje
     */
    public static final int MESSAGE = 7;
    /**
     * Tipo de mensaje
     */
    public static final int MESSAGE_TYPE = 8;
    /**
     * Tipo de actualización
     * @deprecated use DISTRIBUTED_EVENT
     *
     */
    public static final int UPDATE_TYPE = 9;
    /**
     * Resusltados de la búsqueda
     */
    public static final int SEARCH_RESULTS = 10;
    /**
     * Recomendación
     */
    public static final int RECOMMENDATION = 11;
    /**
     * Comnetarios
     */
    public static final int COMMENT = 12;
    /**
     * ¿Existen resultados?
     */
    public static final int IS_EMPTY = 13;
    /**
     * Causa
     */
    public static final int CAUSE = 14;
    /**
     * Usuarios de la sesión
     */
    public static final int SEEKERS_SESSION = 15;
    /**
     * Jefe de la sesión
     */
    public static final int SEEKER_CHAIRMAN = 16;
    /**
     * Objeto proxy
     */
    public static final int PROXY = 17;
    /**
     * Número mínimo de miembros
     */
    public static final int MEMBERS_MIN_NUMBER = 18;
    /**
     * Número maximo de miembros
     */
    public static final int MEMBERS_MAX_NUMBER = 19;
    /**
     * Criterio de Integridad de la sesión
     */
    public static final int INTEGRITY_CRITERIA = 20;
    /**
     * Política de membresía de sesión
     */
    public static final int MEMBERSHIP_POLICY = 21;
    /**
     * Usuarios receptores
     */
    public static final int SEEKERS_RECEPTORS = 22;
    /**
     * Búscador
     */
    public static final int SEARCHER = 23;
    /**
     * Consulta
     */
    public static final int QUERY = 24;
    /**
     * Tipo de documento
     */
    public static final int DOC_TYPE = 25;
    /**
     * Campo
     */
    public static final int FIELD = 26;
    /**
     * Campos
     */
    public static final int FIELDS = 27;
    /**
     * Principio de Búsqueda
     */
    public static final int SEARCH_PRINCIPLE = 28;
    /**
     * Indices de los documentos a recomnedar
     */
    public static final int RECOMMENDATIONS_INDEX = 29;
    /**
     * Nombre de la otra sesión
     */
    public static final int OTHER_SESSION_NAME = 30;
    /**
     * Usuario a notificar
     */
    public static final int SEEKER_NOTIFY = 31;
    /**
     * Indice del documento
     */
    public static final int DOC_INDEX = 32;
    /**
     * Relevancia del documento
     */
    public static final int RELEVANCE = 33;
    /**
     * Nombre del contenedor de la sesión
     */
    public static final int CONTAINER_NAME = 34;
    /**
     *
     */
    public static final int CONTAINER_UUID = 50;
    /**
     *
     */
    public static final int SESSION_ID = 51;
    /**
     *
     */
    public static final int SESSION_DESCRIPTION = 52;
    /**
     *
     */
    public static final int SESSIONS_IDS = 53;
    /**
     *
     */
    public static final int PERSISTENT_SESSIONS = 54;
    /**
     *
     */
    public static final int MANAGER_NAME = 55;
    /**
     *
     */
    public static final int MANAGER_PASSWORD = 56;
    /**
     *
     */
    public static final int CONTAINERS = 57;
    /**
     * Resultado
     */
    public static final int RESULT = 58;
    /**
     * Estado del servidor
     */
    public static final int SERVER_STATE = 61;
    /**
     * Buscadores
     */
    public static final int SEARCHERS = 62;
    /**
     * Tener en cuenta mayúsculas y minúsculas
     */
    public static final int CASE_SENTITIVE = 63;
    /**
     * Tipos de documentos
     */
    public static final int DOC_TYPES = 64;
    /**
     * Nombre del usuario
     */
    public static final int SEEKER_NAME = 65;
    /**
     * Apellidos del usuario
     */
    public static final int SEEKER_LAST_NAME = 66;
    /**
     * Contraseña del usuario
     */
    public static final int SEEKER_PASSWORD = 67;
    /**
     * Correo del usuario
     */
    public static final int SEEKER_EMAIL = 68;
    /**
     * Sobrenombre del usuario
     */
    public static final int SEEKER_NICKNAME = 69;
    /**
     * Foto del usuario
     */
    public static final int SEEKER_AVATAR = 70;
    /**
     * Respuesta
     */
    public static final int ANSWER = 71;
    /**
     * Antiguo contraseña
     */
    public static final int OLD_PASSWORD = 72;
    /**
     * nueva contraseña
     */
    public static final int NEW_PASSWORD = 73;
    /**
     * Valor
     */
    public static final int VALUE = 74;
    /**
     * Rol de el usuario
     */
    public static final int SEEKER_ROL = 75;
    /**
     * PARA LOS HISTORIALES
     */
    public static final int TRACK_FILTER = 80;
    /**
     *
     */
    public static final int DATE = 81;
    /**
     * 
     */
    public static final int DOCUMENT_GROUP = 82;
    /**
     *
     */
    public static final int URI = 83;
    /**
     *
     */
    public static final int EMITTER_NICKNAME = 84;
    /**
     * 
     */
    public static final int RECEPTOR_NICKNAME = 85;
    /**
     *
     */
    public static final int RECOMMEND_DATA = 86;
    /**
     *
     */
    public static final int SEARCH_DATA = 87;
    /**
     *
     */
    public static final int SEARCH_RESULT_SOURCE = 88;
    /**
     *
     */
    public static final int QUERY_SOURCE = 89;
    /**
     *
     */
    public static final int DOCUMENTS = 90;
    /**
     *
     */
    public static final int TIME = 91;
    /**
     *
     */
    public static final int REPLY = 92;
    /**
     *
     */
    public static final int SEARCH_PRINCIPLES = 93;
    /**
     *
     */
    public static final int IS_CHAIRMAN = 94;
    /**
     * 
     */
    public static final int CHAIRMAN_NAME = 95;
    /**
     *
     */
    public static final int OPEN_SESSIONS = 96;
    /**
     * 
     */
    public static final int SESSION = 97;
    /**
     *
     */
    public static final int SESSION_DATA = 98;
    /**
     * 
     */
    public static final int SEEKER_DESCRIPTION = 99;
    /**
     *
     */
    public static final int SEARCH_TYPE = 100;
    /**
     *
     */
    public static final int DISTRIBUTED_EVENT = 101;
     /**
     *
     */
    public static final int ACTION = 102;
     /**
     *
     */
    public static final int SEEKER_ID = 103;
     /**
     *
     */
    public static final int SVN_REPOSITORIES_NAMES = 104;
     /**
     *
     */
    public static final int SVN_REPOSITORY = 105;
     /**
     *
     */
    public static final int ONLY_FILE_BODY = 106;
     /**
     *
     */
    public static final int FILE_TYPE = 107;
     /**
     *
     */
    public static final int SORT_TYPE = 108;
     /**
     *
     */
    public static final int LAST_MODIFIED = 109;
	
	 /**
     *
     */
    public static final int SCORE_PQT = 110;
    /**
     *
     */
    public static final int QUERY_TYPED = 111;
    /**
     *
     */
    public static final int QUERY_TERM = 112;
    /**
     *
     */
    public static final int QUERY_TERMS_SUGGEST = 113;

    
}
