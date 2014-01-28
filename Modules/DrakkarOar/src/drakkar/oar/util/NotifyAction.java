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
 * Esta contiene las diferentes constantes que pueden ser empleadas para efectuar
 * las notificaciones de operaciones del cliente y el servidor
 */
public class NotifyAction {

    /**
     * Notificar mensaje
     *
     * @deprecated use NOTIFY_TEXT_MESSAGE
     */
    public static final int NOTIFY_MESSAGE = 10;
    /**
     * Notificar recomendación
     * @deprecated use NOTIFY_EXPLICIT_RECOMMENDATION
     */
    public static final int NOTIFY_RECOMMENDATION = 20;
    /**
     * Notificar resultados de búsqueda
     */
    public static final int NOTIFY_SEARCH_RESULTS = 30;
    /**
     * Notificar actualización de la sesión
     *
     * @deprecated
     */
    public static final int NOTIFY_UPDATE_COLLAB_SESSION = 40;
    /**
     * Notificar estado actual del servidor
     */
    public static final int NOTIFY_SERVER_STATE = 50;
    /**
     * Notificar la conclusión de operación
     *
     * @deprecated use NOTIFY_COMMIT_TRANSACTION
     */
    public static final int NOTIFY_CONCLUSION_OPERATION = 70;
    /**
     * Notificar desconexión del usuario
     * @deprecated
     */
    public static final int NOTIFY_SEEKER_LOGOUT = 110;
    /**
     * Notificar desconexión del usuario
     * * @deprecated
     */
    public static final int NOTIFY_SEEKER_LOGIN = 80;
    /**
     * 
     */
    public static final int NOTIFY_SEEKER_EVENT = 100;
    /**
     * Notificar buscadores disponibles
     */
    public static final int NOTIFY_AVAILABLE_SEARCHERS = 150;
    /*************************PARA EL SERVER************************************/
    /**
     * Notificar documentos indexados
     */
    public static final int NOTIFY_INDEXED_DOCUMENT = 180;
    /**
     * Notificar documentos adicionados al índice
     */
    public static final int NOTIFY_ADDED_DOCUMENT = 190;
    /**
     * Notificar cantidad de documentos de un índice
     */
    public static final int NOTIFY_LOADED_DOCUMENT = 200;
    /* Notificar documentos indexados para Terrier
     */
    /**
     *
     */
    public static final int NOTIFY_INDEXED_DOCUMENT_COUNT = 210;
    /**
     * 
     */
    public static final int NOTIFY_BEGIN_COLLAB_SESSION = 220;
    /**
     *
     */
    public static final int NOTIFY_COMMIT_TRANSACTION = 230;
    /**
     *
     */
    public static final int UPDATE_SEEKER_PROFILE = 240;
    /**
     * @deprecated use UPDATE_SEEKER_PROFILE
     */
    public static final int NOTIFY_UPDATE_SEEKER_DATA = 250;
    /**
     * Notificar mensaje
     *
     */
    public static final int NOTIFY_TEXT_MESSAGE = 260;
    /**
     * Notificar las sesiones colaborativas disponibles
     * @deprecated use NOTIFY_AVAILABLE_COLLAB_SESSION
     */
    public static final int NOTIFY_AVAILABLE_SESSION = 270;
    /**
     * Notificar mensaje
     *
     */
    public static final int NOTIFY_COLLAB_SESSION_ACCEPTANCE = 280;
    /**
     * Notificar mensaje
     *
     */
    public static final int NOTIFY_ACTION_TRACK = 300;
    /**
     *
     */
    public static final int NOTIFY_AVAILABLE_COLLAB_SESSION = 310;
    /**
     *
     */
    public static final int NOTIFY_COLLAB_SESSION_EVENT = 320;
    /**
     *
     */
    public static final int NOTIFY_CHAIRMAN_SETTING = 330;
//    /**
//     *
//     */
//    public static final int NOTIFY_COLLAB_SESSION_DELETED= 340;
//
//    /**
//     *
//     */
//    public static final int NOTIFY_COLLAB_SESSION_ENDING= 350;
    /**
     *
     */
    public static final int NOTIFY_COLLAB_SESSION_AUTHENTICATION = 360;
    /**
     *
     *
     * 
     */
    public static final int NOTIFY_REQUEST_CONNECTION = 370;
    /**
     * 
     */
    public static final int NOTIFY_EXPLICIT_RECOMMENDATION = 380;
    /**
     *
     */
    public static final int NOTIFY_EVENT = 390;
    /**
     *
     */
    public static final int NOTIFY_AVAILABLE_SVN_REPOSITORIES = 400;
    public static final int NOTIFY_AVAILABLE_SEARCH_PRINCIPLES = 410;
    /**
     *
     */
    public static final int NOTIFY_QUERY_TYPED = 420;
    /**
     *
     */
    public static final int NOTIFY_QUERY_TERM_ACCEPTANCE = 430;
    /**
     *
     */
    public static final int NOTIFY_PUTTING_QUERY_TERMS_TOGETHER = 440;
    /**
     * 
     */
    public static final int NOTIFY_QUERY_CHANGE = 450;

    /**
     *
     */
    public static final int NOTIFY_QUERY_TERMS_SUGGEST = 460;
     /**
     *
     */
    public static final int NOTIFY_CLOSE_CONNECTION = 470;
     /**
     *
     */
    public static final int NOTIFY_COLLAB_TERMS_SUGGEST = 480;
}
