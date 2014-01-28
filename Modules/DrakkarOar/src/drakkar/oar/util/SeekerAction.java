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
 * Esta clase contiene todas las constantes que representan las operaciones que
 * pueden ser invocadas por el cliente, soportadas por DrakkarKeel en la aplicación
 * servidora
 */
public class SeekerAction {

    // TODO Revisar ultimas constantes 
    ////////////////////////////////////////////////////////////////////////////
    // Operaciones con sesiones
    /**
     * Crea una sesión colaborativa de búsqueda
     */
    public static final int CREATE_COLLAB_SESSION = 0;
    /**
     * Unirse a una sesión colaborativa de búsqueda
     */
    public static final int JOIN_COLLAB_SESSION = 1;
    /**
     * Finalizar una sesión colaborativa de búsqueda
     */
    public static final int FINALIZE_COLLABORATIVE_SESSION = 2;
    ////////////////////////////////////////////////////////////////////////////
    // mensajeria
    /**
     * Enviar un mensaje a un miembro de la sesión
     */
    public static final int SEND_SINGLE_MESSAGE = 3;
    /**
     * Enviar un mensaje a un grupo usuarios de la sesión
     */
    public static final int SEND_GROUP_MESSAGE = 4;
    /**
     * Enviar un mensaje a toda la sesión
     */
    public static final int SEND_SESSION_MESSAGE = 5;
    ////////////////////////////////////////////////////////////////////////////
    // recomendaciones
    /**
     * Recomendar todos los resultados de la búsqueda a toda la sesión de búsqueda
     */
    public static final int RECOMMEND_SESSION_RESULTS = 6;
    /**
     * Recomendar una selección de los resultados de la búsqueda a toda la
     * sesión de búsqueda
     */
    public static final int RECOMMEND_SESSION_SELECTION_RESULTS = 7;
    /**
     * Recomendar todos los resultados de la búsqueda a un miembro de la sesión
     * de búsqueda
     */
    public static final int RECOMMEND_SINGLE_RESULTS = 8;
    /**
     * Recomendar una selección de los  resultados de la búsqueda a un miembro
     * de la sesión de búsqueda
     */
    public static final int RECOMMEND_SINGLE_SELECTION_RESULTS = 9;
    /**
     * Recomendar todos los resultados de la búsqueda a un grupo de miembros de
     * la sesión de búsqueda
     */
    public static final int RECOMMEND_GROUP_RESULTS = 10;
    /**
     * Recomendar una selección de los  resultados de la búsqueda a un grupo de
     * miembros de la sesión de búsqueda
     */
    public static final int RECOMMEND_GROUP_SELECTION_RESULTS = 11;
    /**
     * Recomendar todos los resultados de la búsqueda a un miembro de otra sesión
     * de búsqueda
     */
    public static final int RECOMMEND_ANOTHER_SESSION_SINGLE_RESULTS = 12;
    /**
     * Recomendar una selección de los resultados de la búsqueda a un miembro de otra sesión
     * de búsqueda
     */
    public static final int RECOMMEND_ANOTHER_SESSION_SINGLE_SELECTION_RESULTS = 13;
    /**
     * Recomendar todos los resultados de la búsqueda a un grupo de miembros de
     * otra sesión de búsqueda
     */
    public static final int RECOMMEND_ANOTHER_SESSION_GROUP_RESULTS = 14;
    /**
     * Recomendar una selección de los resultados de la búsqueda a un grupo de
     * miembros de otra sesión de búsqueda
     */
    public static final int RECOMMEND_ANOTHER_SESSION_GROUP_SELECTION_RESULTS = 15;
    //////////////////////////////////////////////////////////////////////////////////////
    // busquedas
    /**
     * Búsqueda por una consulta, aplicando los principios:
     * <br>
     * <tt>- Single Search Engine</tt><br>
     * <tt>- Single Search Engine and Split</tt>
     * <br>
     */
    public static final int SEARCH_QRY__SS_SSSPLIT = 16;
    /**
     * Búsqueda por una consulta y un campo del documento, aplicando los principios:
     * <br>
     * <tt>- Single Search Engine</tt><br>
     * <tt>- Single Search Engine and Split</tt>
     * <br>
     */
    public static final int SEARCH_QRY_FLD__SS_SSSPLIT = 17;
    /**
     * Búsqueda por una consulta y un grupo de campos del documento, aplicando
     * los principios:
     * <br>
     * <tt>- Single Search Engine</tt><br>
     * <tt>- Single Search Engine and Split</tt>
     * <br>
     */
    public static final int SEARCH_QRY_FLDS__SS_SSSPLIT = 18;
    /**
     * Búsqueda por una consulta y un tipo de documento, aplicando
     * los principios:
     * <br>
     * <tt>- Single Search Engine</tt><br>
     * <tt>- Single Search Engine and Split</tt>
     * <br>
     */
    public static final int SEARCH_QRY_DOCTYPE__SS_SSSPLIT = 19;
    /**
     * Búsqueda por una consulta y varios tipos de documento, aplicando
     * los principios:
     * <br>
     * <tt>- Single Search Engine</tt><br>
     * <tt>- Single Search Engine and Split</tt>
     * <br>
     */
    public static final int SEARCH_QRY_DOCTYPES__SS_SSSPLIT = 20;
    /**
     * Búsqueda por una consulta, tipo de documento, y un campo del documento,
     * aplicando los principios:
     * <br>
     * <tt>- Single Search Engine</tt><br>
     * <tt>- Single Search Engine and Split</tt>
     * <br>
     */
    public static final int SEARCH_QRY_DOCTYPE_FLD__SS_SSSPLIT = 21;
    /**
     * Búsqueda por una consulta, varios tipos de documento, y un campo del documento,
     * aplicando los principios:
     * <br>
     * <tt>- Single Search Engine</tt><br>
     * <tt>- Single Search Engine and Split</tt>
     * <br>
     */
    public static final int SEARCH_QRY_DOCTYPES_FLD__SS_SSSPLIT = 22;
    /**
     * Búsqueda por una consulta, un tipo de documento, y varios campos del documento,
     * aplicando los principios:
     * <br>
     * <tt>- Single Search Engine</tt><br>
     * <tt>- Single Search Engine and Split</tt>
     * <br>
     */
    public static final int SEARCH_QRY_DOCTYPE_FLDS__SS_SSSPLIT = 23;
    /**
     * Búsqueda por una consulta, varios tipos de documento, y varios campos del documento,
     * aplicando los principios:
     * <br>
     * <tt>- Single Search Engine</tt><br>
     * <tt>- Single Search Engine and Split</tt>
     * <br>
     */
    public static final int SEARCH_QRY_DOCTYPES_FLDS__SS_SSSPLIT = 24;
    /**
     * Búsqueda por una consulta, aplicando los principios:
     * <br>
     * <tt>- Meta Search Engine</tt><br>
     * <tt>- Meta Search Engine and Split</tt><br>
     * <tt>- Multi Search Engine and Switch</tt>
     * <br>
     */
    public static final int SEARCH_QRY__MS_MSSPLIT_MSSWITCH = 25;
    /**
     * Búsqueda por una consulta y un campo del documento, aplicando los principios:
     * <br>
     * <tt>- Meta Search Engine</tt><br>
     * <tt>- Meta Search Engine and Split</tt><br>
     * <tt>- Multi Search Engine and Switch</tt>
     * <br>
     */
    public static final int SEARCH_QRY_FLD__MS_MSSPLIT_MSSWITCH = 26;
    /**
     * Búsqueda por una consulta y varios campos del documento, aplicando
     * los principios:
     * <br>
     * <tt>- Meta Search Engine</tt><br>
     * <tt>- Meta Search Engine and Split</tt><br>
     * <tt>- Multi Search Engine and Switch</tt>
     * <br>
     */
    public static final int SEARCH_QRY_FLDS__MS_MSSPLIT_MSSWITCH = 27;
    /**
     * Búsqueda por una consulta y un tipo de documento, aplicando
     * los principios:
     * <br>
     * <tt>- Meta Search Engine</tt><br>
     * <tt>- Meta Search Engine and Split</tt><br>
     * <tt>- Multi Search Engine and Switch</tt>
     * <br>
     */
    public static final int SEARCH_QRY_DOCTYPE__MS_MSSPLIT_MSSWITCH = 28;
    /**
     * Búsqueda por una consulta y varios tipos de documento, aplicando
     * los principios:
     * <br>
     * <tt>- Meta Search Engine</tt><br>
     * <tt>- Meta Search Engine and Split</tt><br>
     * <tt>- Multi Search Engine and Switch</tt>
     * <br>
     */
    public static final int SEARCH_QRY_DOCTYPES__MS_MSSPLIT_MSSWITCH = 29;
    /**
     * Búsqueda por una consulta, tipo de documento, y un campo del documento,
     * aplicando los principios:
     * <br>
     * <tt>- Meta Search Engine</tt><br>
     * <tt>- Meta Search Engine and Split</tt><br>
     * <tt>- Multi Search Engine and Switch</tt>
     * <br>
     */
    public static final int SEARCH_QRY_DOCTYPE_FLD__MS_MSSPLIT_MSSWITCH = 30;
    /**
     * Búsqueda por una consulta, un tipo de documento, y varios campos del documento,
     * aplicando los principios:
     * <br>
     * <tt>- Meta Search Engine</tt><br>
     * <tt>- Meta Search Engine and Split</tt><br>
     * <tt>- Multi Search Engine and Switch</tt>
     * <br>
     */
    public static final int SEARCH_QRY_DOCTYPE_FLDS__MS_MSSPLIT_MSSWITCH = 31;
    /**
     * Búsqueda por una consulta, varios tipos de documento, y un campo del documento,
     * aplicando los principios:
     * <br>
     * <tt>- Meta Search Engine</tt><br>
     * <tt>- Meta Search Engine and Split</tt><br>
     * <tt>- Multi Search Engine and Switch</tt>
     * <br>
     */
    public static final int SEARCH_QRY_DOCTYPES_FLD__MS_MSSPLIT_MSSWITCH = 32;
    /**
     * Búsqueda por una consulta, varios tipos de documento, y varios campos del documento,
     * aplicando los principios:
     * <br>
     * <tt>- Meta Search Engine</tt><br>
     * <tt>- Meta Search Engine and Split</tt><br>
     * <tt>- Multi Search Engine and Switch</tt>
     * <br>
     */
    public static final int SEARCH_QRY_DOCTYPES_FLDS__MS_MSSPLIT_MSSWITCH = 33;
    /**
     * Búsqueda por una consulta,varios buscadores, aplicando los principios:
     * <br>
     * <tt>- Meta Search Engine</tt><br>
     * <tt>- Meta Search Engine and Split</tt><br>
     * <tt>- Multi Search Engine and Switch</tt>
     * <br>
     */
    public static final int SEARCH_QRY__SEARCHERS__MS_MSSPLIT_MSSWITCH = 34;
    /**
     * Búsqueda por una consulta, varios buscadores y un campo del documento,
     * aplicando los principios:
     * <br>
     * <tt>- Meta Search Engine</tt><br>
     * <tt>- Meta Search Engine and Split</tt><br>
     * <tt>- Multi Search Engine and Switch</tt>
     * <br>
     */
    public static final int SEARCH_QRY_FLD__SEARCHERS__MS_MSSPLIT_MSSWITCH = 35;
    /**
     * Búsqueda por una consulta, varios buscadores y varios campos del documento,
     * aplicando los principios:
     * <br>
     * <tt>- Meta Search Engine</tt><br>
     * <tt>- Meta Search Engine and Split</tt><br>
     * <tt>- Multi Search Engine and Switch</tt>
     * <br>
     */
    public static final int SEARCH_QRY_FLDS__SEARCHERS__MS_MSSPLIT_MSSWITCH = 36;
    /**
     * Búsqueda por una consulta, varios buscadores y un tipo de documento, aplicando
     * los principios:
     * <br>
     * <tt>- Meta Search Engine</tt><br>
     * <tt>- Meta Search Engine and Split</tt><br>
     * <tt>- Multi Search Engine and Switch</tt>
     * <br>
     */
    public static final int SEARCH_QRY_DOCTYPE__SEARCHERS__MS_MSSPLIT_MSSWITCH = 37;
    /**
     * Búsqueda por una consulta, varios buscadores y varios tipos de documento, aplicando
     * los principios:
     * <br>
     * <tt>- Meta Search Engine</tt><br>
     * <tt>- Meta Search Engine and Split</tt><br>
     * <tt>- Multi Search Engine and Switch</tt>
     * <br>
     */
    public static final int SEARCH_QRY_DOCTYPES__SEARCHERS__MS_MSSPLIT_MSSWITCH = 38;
    /**
     * Búsqueda por una consulta, varios buscadores, tipo de documento, y un campo del documento,
     * aplicando los principios:
     * <br>
     * <tt>- Meta Search Engine</tt><br>
     * <tt>- Meta Search Engine and Split</tt><br>
     * <tt>- Multi Search Engine and Switch</tt>
     * <br>
     */
    public static final int SEARCH_QRY_DOCTYPE_FLD__SEARCHERS__MS_MSSPLIT_MSSWITCH = 39;
    /**
     * Búsqueda por una consulta, varios buscadores, un tipo de documento, y varios
     * campos del documento, aplicando los principios:
     * <br>
     * <tt>- Meta Search Engine</tt><br>
     * <tt>- Meta Search Engine and Split</tt><br>
     * <tt>- Multi Search Engine and Switch</tt>
     * <br>
     */
    public static final int SEARCH_QRY_DOCTYPE_FLDS__SEARCHERS__MS_MSSPLIT_MSSWITCH = 40;
    /**
     * Búsqueda por una consulta, varios buscadores, varios tipos de documento,
     * y un campo del documento, aplicando los principios:
     * <br>
     * <tt>- Meta Search Engine</tt><br>
     * <tt>- Meta Search Engine and Split</tt><br>
     * <tt>- Multi Search Engine and Switch</tt>
     * <br>
     */
    public static final int SEARCH_QRY_DOCTYPES_FLD__SEARCHERS__MS_MSSPLIT_MSSWITCH = 41;
    /**
     * Búsqueda por una consulta, varios buscadores, varios tipos de documento, 
     * y varios campos del documento, aplicando los principios:
     * <br>
     * <tt>- Meta Search Engine</tt><br>
     * <tt>- Meta Search Engine and Split</tt><br>
     * <tt>- Multi Search Engine and Switch</tt>
     * <br>
     */
    public static final int SEARCH_QRY_DOCTYPES_FLDS__SEARCHERS__MS_MSSPLIT_MSSWITCH = 42;
    ///////////////////////////////////////////////////////////////////////////////////////
    // notificaciones
    /**
     * Notificar la admisión de un seeker en una sesión colaborativa de búsqueda
     */
    public static final int NOTIFY_ADMISSION_COLLAB_SESSION = 43;
    /**
     * Notificar la no admisión de un seeker en una sesión colaborativa de búsqueda
     */
    public static final int NOTIFY_NO_ADMISSION_COLLAB_SESSION = 44;
    /**
     * Notificar la revisión de un documento efectuada por un seeker
     */
    public static final int NOTIFY_DOCUMENT_VIEWED = 45;
    /**
     * Notificar la evaluación de un documento efectuada por un seeker
     */
    public static final int NOTIFY_DOCUMENT_EVALUATED = 46;
    /**
     * Notificar el comentario de un documento efectuado por un seeker
     */
    public static final int NOTIFY_DOCUMENT_COMMENTED = 47;
    ////////////////////////////////////////////////////////////////////////////////////
    ////Actulizar estado de seeker
    /**
     * Actualizar el estado de un seeker
     */
    public static final int UPDATE_STATE_SEEKER = 48;
    ///////////////////////////////////////////////////////////////////////////
   
    /**
     * Solicitud de admisión en una sesión colaborativa de búsqueda
     */
    public static final int REQUEST_ADMISSION_SESSION = 50;
    /**
     * Obtener las propiedades de las sesiones activas en el servidor
     */
    public static final int GET_SESSIONS_PROPERTIES = 52;
    /**
     * Obtener la cantidad de sesiones activas en el servidor
     */
    public static final int GET_SESSIONS_COUNT = 53;
    /**
     * Obtener la cantidad de búsquedas realizadas en la actual sesión del servidor
     */
    public static final int GET_SEARCHES_COUNT = 54;
    /**
     * Obtener la cantidad de búsquedas realizadas por una sesión de búsqueda,
     * en la actual sesión del servidor
     */
    public static final int GET_SESSION_SEARCHES_COUNT = 55;
    /**
     * Obtener la cantidad de búsquedas realizadas por un seeker,
     * en la actual sesión del servidor
     */
    public static final int GET_MEMBER_SEARCHES_COUNT = 56;
    /**
     * Obtener la cantidad de mensajes en la actual sesión del servidor
     */
    public static final int GET_MESSAGES_COUNT = 57;
    /**
     * Obtener la cantidad de mensajes de una sesión, en la actual sesión del servidor
     */
    public static final int GET_SESSION_MESSAGES_COUNT = 58;
    /**
     * Obtener la cantidad de mensajes de un seeker, en la actual sesión del servidor
     */
    public static final int GET_MEMBER_MESSAGES_COUNT = 59;
    /**
     * Obtener la cantidad de recomendaciones efectuadas, en la actual sesión del servidor
     */
    public static final int GET_RECOMMENDATIONS_COUNT = 60;
    /**
     * Obtener la cantidad de recomendaciones efectuadas por una sesión, en la actual sesión del servidor
     */
    public static final int GET_SESSION_RECOMMENDATIONS_COUNT = 61;
    /**
     * Obtener la cantidad de recomendaciones efectuadas por un seeker, en la actual sesión del servidor
     */
    public static final int GET_MEMBER_RECOMMENDATIONS_COUNT = 62;
    /**
     * Obtener la cantidad de documentos relevantes para una consulta en una sesión,
     * en la actual sesión del servidor
     */
    public static final int GET_SESSION_RELEVANT_DOCUMENTS = 63;
    /**
     *
     */
    public static final int GET_MEMBER_RELEVANT_DOCUMENTS = 64;
    /**
     *
     */
    public static final int GET_SESSION_CHECKED_DOCUMENTS = 65;
    /**
     *
     */
    public static final int GET_MEMBER_CHECKED_DOCUMENTS = 66;
    /**
     *
     */
    public static final int GET_ONLINE_SEEKERS = 67;
    /**
     *
     */
    public static final int UPDATE_AVATAR_SEEKER = 69;
    /**
     *
     */
    public static final int GET_MESSAGE = 80;
    /**
     * 
     */
    public static final int GET_MESSAGE_LIST = 81;
    /**
     * 
     */
    public static final int GET_IDS_PERSISTENT_SESSIONS = 82;
    /**
     * 
     */
    public static final int GET_NAMES_PERSISTENT_SESSIONS = 83;
    /**
     * 
     */
    public static final int GET_IDS_NO_PERSISTENT_SESSIONS = 84;
    /**
     * 
     */
    public static final int GET_NAMES_NO_PERSISTENT_SESSIONS = 85;
    /**
     * 
     */
    public static final int GET_ID_NO_PERSISTENT_SESSION = 86;
    /**
     * 
     */
    public static final int GET_NAME_NO_PERSISTENT_SESSION = 87;
    /**
     * 
     */
    public static final int GET_ID_PERSISTENT_SESSION = 88;
    /**
     * 
     */
    public static final int GET_NAME_PERSISTENT_SESSION = 89;
    /**
     *
     */
    public static final int REMOVE_ID_NO_PERSISTENT_SESSION = 90;
    /**
     *
     */
    public static final int REMOVE_NAME_NO_PERSISTENT_SESSION = 91;
    /**
     *
     */
    public static final int GET_ID_MEMBER_PERSISTENT_SESSION = 92;
    /**
     *
     */
    public static final int GET_NAME_MEMBER_PERSISTENT_SESSION = 93;
    /**
     *
     */
    public static final int ADD_NO_PERSISTENT_SESSION = 94;
    /**
     *
     */
    public static final int ADD_PERSISTENT_SESSION = 95;
    /**
     *
     */
    public static final int REMOVE_NO_PERSISTENT_SESSION = 96;
    /**
     *
     */
    public static final int REMOVE_PERSISTENT_SESSION = 97;
    /**
     *
     */
    public static final int GET_UUID_CONTAINER = 99;
    /**
     *
     */
    public static final int REMOVE_NAME_SESSION = 100;
    /**
     *
     */
    public static final int REMOVE_ID_SESSION = 101;
    /**
     *
     */
    public static final int GET_MANAGER_NAME = 103;
    /**
     *
     */
    public static final int GET_MANAGER_PASSWORD = 104;
    /**
     *
     */
    public static final int SET_MANAGER_PASSWORD = 105;
    /**
     *
     */
    public static final int SET_MANAGER_NAME = 106;
    /**
     *
     */
    public static final int SET_CONTAINERS = 107;
    /**
     *
     */
    public static final int GET_CONTAINERS = 108;
    /**
     *
     */
    public static final int LOGIN_COLLAB_SESSION = 102;
    /**
     *
     */
    public static final int LOGOUT_COLLAB_SESSION = 142;
   
    /**
     *
     */
    public static final int INCREMENT = 145;
    /**
     *
     */
    public static final int DECREMENT = 146;
    /**
     *
     */
    public static final int UPDATE_COUNT_MEMBERS = 168;
    /**
     *
     */
    public static final int UPDATE_SESSIONS = 169;

    /**
     *
     */
    public static final int UPDATE_SEEKER_ROL = 170;

    ///////////////////////////////////////////////////////////////////////////


    /**
     *
     */
    public static final int GET_RECOMMENDATION_TRACK = 171;

    /**
     *
     */
    public static final int GET_SEARCH_TRACK = 172;
    
    //////////////////////FILTERS
    /**
     *
     */
    public static final int QUERY_TRACK = 173;

    /**
     *
     */
    public static final int SEEKER_TRACK = 174;

    /**
     *
     */
    public static final int DATE_TRACK = 175;

    /**
     *
     */
    public static final int SESSION_TRACK = 183;

    //////////////////////////////////
    /**
     *
     */
    public static final int QUERY_SEEKER_TRACK = 176;

    /**
     *
     */
    public static final int QUERY_DATE_TRACK = 177;

    /**
     *
     */
    public static final int QUERY_SEEKER_DATE_TRACK = 178;

    /**
     *
     */
    public static final int SEEKER_DATE_TRACK = 179;
    /////////////////////////////////

    /**
     *
     */
    public static final int SEARCH_REVIEWED_TRACK = 180;

    /**
     * 
     */
    public static final int SEARCH_SELECTED_RELEVANT_TRACK = 181;

    /**
     *
     */
    public static final int SEARCH_ALL_TRACK = 182;

     /**
     *
     */
    public static final int CLOSE_COLLAB_SESSION = 183;

    /**
     *
     */
    public static final int DECLINE_SEEKER_COLLAB_SESSION = 184;

    /**
     *
     */
    public static final int LOCAL_SEARCH_RESULT = 185;


    /**
     *
     */
    public static final int RECOMMEND_SEARCH_RESULT = 186;


    /**
     *
     */
    public static final int TRACK_SEARCH_RESULT = 187;

    /**
     * 
     */
    public static final int ENTER_COLLAB_SESSION = 188;

   
    /**
     *
     */
    public static final int GET_SESSION_TRACK = 189;


    /**
     *
     */
    public static final int INDIVIDUAL_SEARCH = 190;

    /**
     *
     */
    public static final int COLLAB_SEARCH = 191;


    /**
     *
     */
    public static final int SELF_LOGIN_COLLAB_SESSION = 192;

     /**
      *
      */
     public static final int SEARCH_SVN_QRY_FILE_SORT_MODIFIED =193;

     /**
      * 
      */
     public static final int SVN_DATA_REQUEST = 194;
	 
	  /**
     *
     */
    public static final int SEND_ACTION_QUERY_CHANGE = 195;
    /**
     *
     */
    public static final int SEND_ACTION_QUERY_TYPED = 196;

     /**
     *
     */
    public static final int SEND_ACTION_TERM_ACCEPTANCE = 197;

    /**
     *
     */
    public static final int PUTTING_QUERY_TERMS_TOGETHER = 198;

     /**
     *
     */
    public static final int SELF_LOGOUT_COLLAB_SESSION = 200;

     /**
     *
     */
    public static final int COLLABORATIVE_TERMS_SUGGEST = 201;

	 
	 
}
