/*
 * DrakkarKeel - An Enterprise Collaborative Search Platform
 *
 * The contents of this file are subject under the terms described in the
 * DRAKKARKEEL_LICENSE file included in this distribution; you may not use this
 * file except in compliance with the License. 
 *
 * 2013-2014 DrakkarKeel Platform.
 */
package drakkar.stern.tracker.persistent;

import drakkar.oar.util.ImageUtil;
import drakkar.stern.tracker.persistent.objects.SeekerData;
import drakkar.stern.tracker.persistent.tables.DerbyConnection;
import drakkar.stern.tracker.persistent.tables.PersistentOperations;
import drakkar.stern.tracker.persistent.tables.TableTracker;
import java.awt.image.BufferedImage;
import java.sql.SQLException;

/**
 * Clase que contiene los métodos relacionados con la persistencia de los datos
 * de los usuarios
 */
public class SeekerDB {

    DerbyConnection connection;
    DBUtil util;

    /**
     * Constructor de la clase
     * 
     * @param connection
     * @param util
     */
    public SeekerDB(DerbyConnection connection, DBUtil util) {
        this.connection = connection;
        this.util = util;

    }

    /*********************************SEEKER********************/
    /**
     * Registrar un nuevo usuario
     *
     * @param user              nombre del usuario
     * @param name              nombre completo
     * @param password          contraseña
     * @param description       descripción
     * @param email             correo
     * @param avatar            avatar
     * @param rol               rol
     * @param state             estado
     *
     * @return                  true si encontró el seeker, de lo contrario false
     * 
     * @throws SQLException
     */
    public boolean registerSeeker(String user, String name, String password, String description, String email, byte[] avatar, String rol, String state) throws SQLException {

//        BufferedImage buffered = ImageUtil.makeBufferedImage(avatar.getImage());
//        byte[] image = ImageUtil.toByte(buffered);

        String[] fields = new String[8];
        fields[0] = "SEEKER_USER";
        fields[1] = "SEEKER_NAME";
        fields[2] = "PASSWORD";
        fields[3] = "AVATAR";
        fields[4] = "DESCRIPTION";
        fields[5] = "EMAIL";
        fields[6] = "ID_ROL";
        fields[7] = "ID_STATE";

        Object[] oneValue = new Object[8];
        oneValue[0] = user;
        oneValue[1] = name;
        oneValue[2] = password; //contraseña cifrada
//        oneValue[3] = image;
        oneValue[3] = avatar;
        oneValue[4] = description;
        oneValue[5] = email;
        oneValue[6] = getRol(rol);
        oneValue[7] = getState(state);

        if (!existSeeker(user)) {
            PersistentOperations.insert(connection, "DRAKKARKEEL.SEEKER", fields, oneValue);
            return true;
        }

        return false;

    }

    /**
     * Elimina un usuario
     *
     * @param user             nombre del usuario
     * 
     * @return                 true si lo eliminó, false si no lo encontró
     *
     * @throws SQLException    si ocurre alguna SQLException durante la ejecución
     *                         de la operación
     */
    public boolean deleteSeeker(String user) throws SQLException {

        if (existSeeker(user)) {
            PersistentOperations.delete(connection, "DRAKKARKEEL.SEEKER", "SEEKER_USER", user);
            return true;
        }
        return false;

    }

    /**
     * Actualiza el rol del usuario
     *
     * @param user               usuario a actualizar
     * @param rol                nombre del nuevo rol
     * 
     * @return                   true si lo actualizó, de lo contrario devuelve false
     * 
     * @throws SQLException      si ocurre alguna SQLException durante la ejecución
     *                           de la operación
     */
    public boolean updateSeekerRol(String user, String rol) throws SQLException {

        String[] fields = new String[1];
        fields[0] = "ID_ROL";

        Object[] oneValue = new Object[1];
        oneValue[0] = getRol(rol);

        if (existSeeker(user)) {
            PersistentOperations.update(connection, "DRAKKARKEEL.SEEKER", fields, oneValue, "SEEKER_USER", user);
            return true;
        }
        return false;

    }

    /**
     * Actualiza el estado del usuario
     *
     * @param user              nombre del usuario
     * @param state             estado
     * 
     * @return                  true si lo actualizó, de lo contrario devuelve false
     *
     * @throws SQLException     si ocurre alguna SQLException durante la ejecución
     *                          de la operación
     */
    public boolean updateSeekerState(String user, String state) throws SQLException {

        String[] fields = new String[1];
        fields[0] = "ID_STATE";

        Object[] oneValue = new Object[1];
        oneValue[0] = getState(state);

        if (existSeeker(user)) {
            PersistentOperations.update(connection, "DRAKKARKEEL.SEEKER", fields, oneValue, "SEEKER_USER", user);
            return true;
        }

        return false;

    }

    /**
     * Actualiza la imagen del usuario
     *
     * @param user             nombre del usuario
     * @param newAvatar        imagen que representa a este usuario
     *
     * @return                  true si lo actualizó, de lo contrario devuelve false
     *
     * @throws SQLException     si ocurre alguna SQLException durante la ejecución
     *                          de la operación
     */
    public boolean updateSeekerAvatar(String user, Object newAvatar) throws SQLException {

        String[] fields = new String[1];
        fields[0] = "AVATAR";

        Object[] value = new Object[1];
        value[0] = newAvatar;

        if (existSeeker(user) && newAvatar != null) {
            PersistentOperations.update(connection, "DRAKKARKEEL.SEEKER", fields, value, "SEEKER_USER", user);
            return true;
        }
        return false;

    }

    /**
     * Obtiene el avatar de un usuario
     *
     * @param user              nombre del usuario
     * 
     * @return                  el avatar del usuario
     *
     * @throws SQLException     si ocurre alguna SQLException durante la ejecución
     *                          de la operación
     */
    public BufferedImage getAvatar(String user) throws SQLException {

        byte[] array;

        BufferedImage buffer = null;

        String[] fields = new String[1];
        fields[0] = "AVATAR";

        TableTracker table = PersistentOperations.load(connection, fields, "DRAKKARKEEL.SEEKER", "SEEKER_USER", user);

        Object[][] values = table.getValues();

        if (values.length != 0) {

            array = (byte[]) values[0][0];
            buffer = ImageUtil.toBufferedImage(array);

        }
        return buffer;
    }

    /**
     * Compara contraseñas de un usuario
     *
     * @param user               nombre del usuario
     * @param password           contraseña
     * 
     * @return                   true si la contraseña coincide, de lo contrario false
     *
     * @throws SQLException      si ocurre alguna SQLException durante la ejecución
     *                           de la operación
     */
    public boolean compareSeekerPassword(String user, String password) throws SQLException {

        String[] fields = new String[1];
        fields[0] = "PASSWORD";
        String result = null;

        TableTracker table = PersistentOperations.load(connection, fields, "DRAKKARKEEL.SEEKER", "SEEKER_USER", user);

        Object[][] values = table.getValues();

        if (values.length != 0) {
            result = values[0][0].toString();
            if (password.equals(result)) {
                return true;
            } else {
                return false;
            }

        }

        return false;
    }

    /**
     * Devuelve todos los datos de un seeker dado su user
     *
     * @param user               nombre del usuario
     * 
     * @return                   devuelve un objeto que contiene los datos de un usuario
     * 
     * @throws SQLException      si ocurre alguna SQLException durante la ejecución
     *                           de la operación
     */
    public SeekerData getSeekerData(String user) throws SQLException {
        SeekerData seek = null;

        String[] fields = new String[7];
        fields[0] = "SEEKER_NAME";
        fields[1] = "PASSWORD";
        fields[2] = "AVATAR";
        fields[3] = "DESCRIPTION";
        fields[4] = "EMAIL";
        fields[5] = "ID_ROL";
        fields[6] = "ID_STATE";

        String name, password, email, descrip = null;
        byte[] avatar;
        int rol, state;

        TableTracker table = PersistentOperations.load(connection, fields, "DRAKKARKEEL.SEEKER", "SEEKER_USER", user);
        Object[][] values = table.getValues();

        if (values.length != 0) {
            name = values[0][0].toString();
            password = values[0][1].toString();
            avatar = (byte[]) values[0][2];
            descrip = values[0][3].toString();
            email = values[0][4].toString();
            rol = Integer.valueOf(values[0][5].toString());
            state = Integer.valueOf(values[0][6].toString());

            seek = new SeekerData(user, name, password, descrip, email, state, rol, avatar);

        }

        return seek;
    }

    /**
     * Obtiene el rol dado su id
     *
     * @param id                    id del rol
     * 
     * @return                      devuelve el nombre del rol
     *
     * @throws SQLException         si ocurre alguna SQLException durante la ejecución
     *                              de la operación
     */
    public String getRol(int id) throws SQLException {

        String[] fields = new String[1];
        fields[0] = "ROL_NAME";
        String result = null;

        TableTracker table = PersistentOperations.load(connection, fields, "DRAKKARKEEL.ROL", "ID_ROL", id);

        Object[][] values = table.getValues();

        if (values.length != 0) {
            result = values[0][0].toString();

        }

        return result;
    }

    /**
     * Obtiene el tipo de estado dado su id
     *
     * @param id                   id del estado
     * 
     * @return                     devuelve el tipo de estado
     *
     * @throws SQLException        si ocurre alguna SQLException durante la ejecución
     *                             de la operación
     */
    public String getState(int id) throws SQLException {
        String[] fields = new String[1];
        fields[0] = "STATE_TYPE";
        String result = null;

        TableTracker table = PersistentOperations.load(connection, fields, "DRAKKARKEEL.SEEKER_STATE", "ID_STATE", id);

        Object[][] values = table.getValues();

        if (values.length != 0) {
            result = values[0][0].toString();

        }

        return result;
    }

    /**
     * Obtiene el id del rol
     *
     * @param name                nombre del rol
     *
     * @return                    devuelve el id del rol
     * 
     * @throws SQLException       si ocurre alguna SQLException durante la ejecución
     *                            de la operación
     */
    public int getRol(String name) throws SQLException {

        String[] fields = new String[1];
        fields[0] = "ID_ROL";
        String result = null;
        int id = 0;

        TableTracker table = PersistentOperations.load(connection, fields, "DRAKKARKEEL.ROL", "ROL_NAME", name);

        Object[][] values = table.getValues();

        if (values.length != 0) {
            result = values[0][0].toString();
            id = Integer.parseInt(result);
        }

        return id;
    }

    /**
     * Obtiene el id del estado
     *
     * @param type             nombre del estado
     * 
     * @return                 devuelve el id del estado
     *
     * @throws SQLException    si ocurre alguna SQLException durante la ejecución
     *                            de la operación
     */
    public int getState(String type) throws SQLException {
        String[] fields = new String[1];
        fields[0] = "ID_STATE";
        String result = null;
        int id = 0;

        TableTracker table = PersistentOperations.load(connection, fields, "DRAKKARKEEL.SEEKER_STATE", "STATE_TYPE", type);

        Object[][] values = table.getValues();

        if (values.length != 0) {
            result = values[0][0].toString();
            id = Integer.parseInt(result);
        }

        return id;
    }

    /**
     * Verifica si un usuario determinado existe (si ya está registrado)
     *
     * @param user              nombre del usuario
     * 
     * @return                  true si existe el usuario, false de lo contrario
     *
     * @throws SQLException     si ocurre alguna SQLException durante la ejecución
     *                          de la operación
     */
    public boolean existSeeker(String user) throws SQLException {
        if (util.alreadyExist(user, "DRAKKARKEEL.SEEKER", "SEEKER_USER", "SEEKER_NAME")) {
            return true;
        }
        return false;
    }

    /**
     * Actualiza la contraseña de un usuario
     *
     * @param user                  nombre del usuario
     * @param newPassword           nueva contraseña
     *
     * @throws SQLException         si ocurre alguna SQLException durante la ejecución
     *                              de la operación
     */
    public void updateSeekerPassword(String user, String newPassword) throws SQLException {

        String[] fields = new String[1];
        fields[0] = "PASSWORD";

        Object[] value = new Object[1];
        value[0] = newPassword;

        PersistentOperations.update(connection, "DRAKKARKEEL.SEEKER", fields, value, "SEEKER_USER", user);

    }

    /**
     * Devuelve el email de un usuario
     *
     * @param user                 nombre del usuario
     * 
     * @return                     devuelve el email del usuario
     *
     * @throws SQLException        si ocurre alguna SQLException durante la ejecución
     *                             de la operación
     */
    public String getEmail(String user) throws SQLException {

        String email = null;

        String[] fields = new String[1];
        fields[0] = "EMAIL";

        TableTracker table = PersistentOperations.load(connection, fields, "DRAKKARKEEL.SEEKER", "SEEKER_USER", user);

        Object[][] values = table.getValues();

        if (values.length != 0) {
            email = values[0][0].toString();
        }

        return email;

    }

    /**
     * Establece el estado de baja a un usuario en una sesión
     *
     * @param session            nombre de la sesión
     * @param seeker             nombre del usuario
     * 
     * @return                   devuelve true si le dio baja al usuario de dicha sesión, devuelve false
     *                           en caso que ese usuario no pertenezca a la sesión especificada
     * 
     * @throws SQLException      si ocurre alguna SQLException durante la ejecución
     *                           de la operación
     */
    public boolean declineSeekerSession(String session, String seeker) throws SQLException {

        String[] fields = new String[1];
        fields[0] = "DECLINE";

        Object[] value = new Object[1];
        value[0] = true;

        String[] whereFields = new String[2];
        whereFields[0] = "SESSION_TOPIC";
        whereFields[1] = "SEEKER_USER";

        Object[] whereValues = new Object[2];
        whereValues[0] = session;
        whereValues[1] = seeker;

        if (util.relationExist("DRAKKARKEEL.SEARCH_SESSION_SEEKER", "SESSION_TOPIC", session, "SEEKER_USER", seeker)) {
            PersistentOperations.update(connection, "DRAKKARKEEL.SEARCH_SESSION_SEEKER", fields, value, whereFields, whereValues);
            return true;
        }

        return false;

    }

    /**
     * Verifica si un usuario pertenece a una sesión
     *
     * @param session             nombre de la sesión
     * @param seeker              nombre del usuario
     * 
     * @return                    devuelve true si el seeker pertenece a la sesión, false si no
     *
     * @throws SQLException      si ocurre alguna SQLException durante la ejecución
     *                           de la operación
     */
    public boolean verifySeeker(String session, String seeker) throws SQLException {

        boolean decline = true;

        String[] fields = new String[1];
        fields[0] = "DECLINE";

        String[] whereFields = new String[2];
        whereFields[0] = "SESSION_TOPIC";
        whereFields[1] = "SEEKER_USER";

        Object[] whereValues = new Object[2];
        whereValues[0] = session;
        whereValues[1] = seeker;

        if (util.relationExist("DRAKKARKEEL.SEARCH_SESSION_SEEKER", "SESSION_TOPIC", session, "SEEKER_USER", seeker)) {
            TableTracker table = PersistentOperations.load(connection, fields, "DRAKKARKEEL.SEARCH_SESSION_SEEKER", whereFields, whereValues);

            Object[][] values = table.getValues();

            if (values.length != 0) {
                decline = Boolean.getBoolean(values[0][0].toString());
            }
        }


        return !decline;
    }
}
