/*
 * DrakkarKeel - An Enterprise Collaborative Search Platform
 *
 * The contents of this file are subject under the terms described in the
 * DRAKKARKEEL_LICENSE file included in this distribution; you may not use this
 * file except in compliance with the License. 
 *
 * 2013-2014 DrakkarKeel Platform.
 */
package drakkar.stern.tracker.persistent.security;

import drakkar.oar.util.OutputMonitor;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase para establecer la seguridad en la BD
 */

public class SafeAccess {

    Connection connection;
    DerbyAuthentication authentic;
    ArrayList<String> admins, readers;
    private String dataBasePath;
   
    /**
     *  default constructor
     */
    public SafeAccess() {
    }

    /**
     *
     * @param con
     */
    public SafeAccess(Connection con) {
        connection = con;
        authentic = new DerbyAuthentication(connection);
        admins = new ArrayList<>();
        readers = new ArrayList<>();
    }

    /**
     * Actualiza los usuarios que pueden acceder a la BD.
     * 
     * @param users
     *
     * @throws SQLException
     */
    public void setUsers(List<User> users) throws SQLException {

        authentic.setBuiltInProvider(); //para usar el repositorio interno
        authentic.turnOnUserAuthentication();
        
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            authentic.addUser(user.getUserName(), user.getPassword());

            if (UserAccessLevel.valueOf(user.getUserPrivilege()) == UserAccessLevel.FULLACCESS) {
                admins.add(user.getUserName());
                authentic.setFullAccessUsers(admins);
            } else if (UserAccessLevel.valueOf(user.getUserPrivilege()) == UserAccessLevel.READONLYACCESS) {
                readers.add(user.getUserName());
                authentic.setReadOnlyAccessUsers(readers);
            }
        }

    }

    /**
     * Obtiene el usuario actual.
     *
     * @return
     * 
     * @throws SQLException
     */
    public String getCurrentUser() throws SQLException {
        return authentic.getCurrentUserID();
    }

    /**
     * Obtener la contraseña de un usuario
     *
     * @param userName
     * @return
     * @throws SQLException
     */
    public String getUserPassword(String userName) throws SQLException{
        return authentic.getUserPassword(userName);
    }

    /**
     * Cambia la contraseña del usuario.
     *
     * @param user
     * @param pass
     * @throws SQLException
     */
    public void setNewPassword(String user, String pass) throws SQLException{
        authentic.setNewPassword(user, pass);
    }

    /**
     * Lista de los administradores.
     * 
     * @return
     *
     * @throws SQLException 
     */
    public ArrayList<String> getManager() throws SQLException {

        ArrayList<String> names = null;
//        if (!authentic.isEmptyFullAccessUsersRepository()) {
            names = authentic.getFullAccessUsers();
      //  } else {
      //  }

        return names;
    }

    /**
     * Lista de los usuarios que no pueden modificar la BD pero si ver sus datos
     *
     * @return
     * 
     * @throws SQLException
     */
    public ArrayList<String> getReader() throws SQLException {
        ArrayList<String> names = null;

        if (!authentic.isEmptyReadOnlyAccessUsersRepository()) {
            names = authentic.getReadOnlyAccessUsers();
        } else {
            OutputMonitor.printLine("No se han configurado los lectores",OutputMonitor.ERROR_MESSAGE);
        }
        return names;
    }

    /**
     * @return the dataBasePath
     */
    public String getDataBasePath() {
        return dataBasePath;
    }

    /**
     * @param dataBasePath the dataBasePath to set
     */
    public void setDataBasePath(String dataBasePath) {
        this.dataBasePath = dataBasePath;
    }

    /**
     * Elimina los usuarios creados.
     * 
     * @throws SQLException
     */
    public void deleteCurrentUsers() throws SQLException {
        if (!authentic.isEmptyFullAccessUsersRepository()) {
            authentic.deleteAllFullAccessUsers();
        }
        if (!authentic.isEmptyReadOnlyAccessUsersRepository()) {
            authentic.deleteAllReadOnlyAccessUsers();
        }
//        authentic.deleteUser("manager");
    }

   
}
