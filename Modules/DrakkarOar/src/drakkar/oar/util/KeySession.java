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
 * la  representacion de los diferentes propiedades y operaciones soportadas para las sesiones
 * colaborativas de búsqueda.
 */
public class KeySession {

    /**
     * Sesion iniciada
     */
    public static final int SESSION_START = 0;
    /**
     * sesion detenida
     */
    public static final int SESSION_STOP = 1;
////////////////////////////////OJO NO CAMBIAR, SE USA EN BD////////////////////////////////////
    /**
     * Sesion Dinamica y  Abierta. Politica de Membresia
     */
    public static final int SESSION_DYNAMIC_AND_OPEN = 1;
    /**
     * Sesion Dinamica y Cerrada. Politica de Membresia
     */
    public static final int SESSION_DYNAMIC_AND_CLOSE = 2;

    /**
     * Sesion Estatica. Politica de Membresia
     */
    public static final int SESSION_STATIC = 3;
    //////////////////////////////////////////////////////////
    /**
     * Sesion Fuerte. Criterio de Integridad
     */
    public static final int SESSION_HARD = 5;
    /**
     * Sesion Flexible. Criterio de Integridad
     */
    public static final int SESSION_SOFT = 6;
    /**
     * Sesion terminada
     */
    public static final int END_SESSION = 7;
    /**
     * Agregar un nuevo miembro
     */
    public static final int ADD_SEEKER = 8;
    /**
     * Eliminar un miembro
     */
    public static final int REMOVE_SEEKER = 9;
    /**
     * Eliminar un miembro por su objeto proxy
     */
    public static final int REMOVE_SEEKER_PROXY = 10;
    
    /**
     *
     */
    public static final int GET_ROLEPRX = 11;
    /**
     *
     */
    public static final int IS_AVAILABLE_USER = 12;
    /**
     *
     */
    public static final int LOGIN_SEEKER = 13;
    /**
     *
     */
    public static final int REGISTER_SEEKER = 14;
    /**
     *
     */
    public static final int RECOVER_PASSWORD = 15;
    /**
     *
     */
    public static final int CHANGE_PASSWORD = 16;
    
     
     /**
     * El miembro ya se encuentra registrado en la sesión
     */
    public static final int SEEKER_ALREADY_REGISTERED = 17;
    /**
     *
     */
    public static final int SEEKER_LOGIN = 18;
    /**
     *
     */
    public static final int SEEKER_LOGOUT = 19;



    /**
     *
     */
    public static final int SEEKER_LOGIN_COLLAB_SESSION = 20;

    /**
     *
     */
    public static final int SEEKER_LOGOUT_COLLAB_SESSION = 21;

  /**
     *
     */
    public static final int NEW_CHAIRMAN_COLLAB_SESSION = 22;

    /**
     * Notificar creación de sesión colaborativa de búsqueda
     */
    public static final int CREATED_COLLAB_SESSION = 23;

    /**
     * Notificar admision de sesión
     */
    public static final int REQUEST_CONFIRM_COLLAB_SESSION = 25;

    
    /**
     *
     */
    public static final int BEGIN_COLLAB_SESSION = 26;
    /**
     * Notificar culminación de sesión colaborativa de búsqueda
     */
    public static final int END_COLLAB_SESSION = 27;

    /**
     * 
     */
    public static final int CONFIRM_COLLAB_SESSION = 28;


    /**
     *
     */
    public static final int  DECLINE_COLLAB_SESSION = 29;

    /**
     *
     */
    public static final int  COLLAB_RECOMMENDATION_TRACK = 30;

    /**
     *
     */
    public static final int  COLLAB_SEARCH_TRACK = 31;

    /**
     * @deprecated  use DELETED_COLLAB_SESSION
     */
    public static final int  CLOSED_COLLAB_SESSION = 32;

    
    /**
     * @deprecated  use FINALIZED_COLLAB_SESSION
     */
    public static final int  FINALIZE_COLLAB_SESSION = 33;

  

    /**
     *
     */
    public static final int  CONNECTION_SUCCESSFUL = 35;

    /**
     *
     */
    public static final int  CONNECTION_FAILED = 36;

    /**
     *
     */
    public static final int SERVER_STOPPED = 37;
    /**
     *
     */
    public static final int SERVER_RESET = 38;

    /**
     *
     */
    public static final int  COLLAB_SESSION_TRACK = 39;

    /**
     *
     */
    public static final String  MULTIPLE_QUERIES = "Multiple queries";

    /**
     *
     */
    public static final int FINALIZED_COLLAB_SESSION = 40;

    /**
     *
     */
    public static final int  DELETED_COLLAB_SESSION = 32;

     /**
     *
     */
    public static final int  GET_SEEKER_ID = 41;

    /**
     * 
     */
    public static final int CHANGE_CHAIRMAN_COLLAB_SESSION = 42;

    /**
     *
     */
    public static final int USER_OR_PASSWORD_INCORRECT = 43;

//     public static final int SERVER_CLOSE = 44;

public static final int GET_CHAIRMAN_NAME = 44;

public static final int HARD_INTEGRITY_CRITERIA = 45;


}
