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

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Clase que provee la autenticación y autorización de un usuario a BD de Derby
 */
public class DerbyAuthentication {

    private Connection conex; //private property of connection object      
    
    /** empty parameters constructor */
    public DerbyAuthentication() {
    }

    /** 
     * Constructor with connection parameter
     * 
     * @param conex
     */
    public DerbyAuthentication(Connection conex) {
        this.conex = conex;
    }

    /**
     * Getter for connection property
     *
     * @return
     */
    public Connection getConex() {
        return conex;
    }

    /**
     * Setter for connection property
     *
     * @param conex
     */
    public void setConex(Connection conex) {
        this.conex = conex;
    }

    /**
     * Turn on derby authentication
     *
     * @throws SQLException
     */
    public void turnOnUserAuthentication() throws SQLException {
        try (CallableStatement cs = conex.prepareCall("CALL SYSCS_UTIL.SYSCS_SET_DATABASE_PROPERTY(?, ?)")) {
            cs.setString(1, "derby.connection.requireAuthentication");
            cs.setString(2, "true");
            cs.execute();
        }

    }

    /**
     * Turn off derby authentication
     * 
     * @throws SQLException
     */
    public void turnOffUserAuthentication() throws SQLException {
        try (CallableStatement cs = conex.prepareCall("CALL SYSCS_UTIL.SYSCS_SET_DATABASE_PROPERTY(?, ?)")) {
            cs.setString(1, "derby.connection.requireAuthentication");
            cs.setString(2, "false");
            cs.execute();
        }

    }

    /**
     * Get authentication status
     *
     * @return
     * @throws SQLException
     */
    public boolean getAuthenticationStatus() throws SQLException {
        ResultSet rs;
        boolean status;
        try (Statement s = conex.createStatement()) {
            rs = s.executeQuery("VALUES SYSCS_UTIL.SYSCS_GET_DATABASE_PROPERTY(" + "'derby.connection.requireAuthentication')");
            rs.next();
            status = rs.getBoolean(1);
        }

        rs.close();
        return status; //return current status of authentication property

    }

    /**
     * Sets BuiltIn provider of derby authentication
     * 
     * @throws SQLException
     */
    public void setBuiltInProvider() throws SQLException {
        try (Statement s = conex.createStatement()) {
            s.executeUpdate("CALL SYSCS_UTIL.SYSCS_SET_DATABASE_PROPERTY(" + "'derby.authentication.provider', 'BUILTIN')");
        }

    }

    /**
     * Adds an user and password to the builtIn repository
     *
     * @param user
     * @param password
     * @throws SQLException
     */
    public void addUser(String user, String password) throws SQLException {
        try (CallableStatement cs = conex.prepareCall("CALL SYSCS_UTIL.SYSCS_SET_DATABASE_PROPERTY(?, ?)")) {
            String username = "derby.user.".concat(user); //concat proper url of user sql sentence

            cs.setString(1, username);
            cs.setString(2, password);
            cs.execute();
        }

    }

    /**
     * Deletes an user from the builtIn repository
     * 
     * @param user
     * @throws SQLException
     */
    public void deleteUser(String user) throws SQLException {
        try (CallableStatement cs = conex.prepareCall("CALL SYSCS_UTIL.SYSCS_SET_DATABASE_PROPERTY(?, null)")) {
            String username = "derby.user.".concat(user); //concat proper url of user sql sentence

            cs.setString(1, username);
            cs.execute();
        }

    }

    /**
     * Get the password for a user.
     * 
     * @param user
     * @return                  password encrypted by Derby with SHA-1 authentication scheme.
     * @throws SQLException 
     */
    public String getUserPassword(String user) throws SQLException {

        String pass = null;        
        ResultSet rs;
        try (Statement s = conex.createStatement()) {
            String userName = "'derby.user.".concat(user) + "')";
            rs = s.executeQuery("VALUES SYSCS_UTIL.SYSCS_GET_DATABASE_PROPERTY(" + userName);
            rs.next();
            pass = rs.getString(1);
        }
        rs.close();

        return pass;

    }

    /**
     * Change user password
     *
     * @param user
     * @param password
     * 
     * @throws SQLException 
     */
    public void setNewPassword(String user, String password) throws SQLException {
        try (CallableStatement cs = conex.prepareCall("CALL SYSCS_UTIL.SYSCS_SET_DATABASE_PROPERTY(?, ?)")) {
            String username = "derby.user.".concat(user); //concat proper url of user sql sentence

            cs.setString(1, username);
            cs.setString(2, password);
            cs.execute();
        }

    }

    /**
     * Gets the current user identifier ID that is log on
     * 
     * @return
     * @throws SQLException
     */
    public String getCurrentUserID() throws SQLException {
        ResultSet rs;
        String userID;
        try (Statement s = conex.createStatement()) {
            rs = s.executeQuery("VALUES CURRENT_USER");
            rs.next();
            userID = rs.getString(1);
        }

        rs.close();
        return userID; //return current user name 
        //If there is no current user, it returns APP

    }

    /**
     * Enables sql authorization
     * 
     * @throws SQLException
     */
    public void enableSqlAuthorization() throws SQLException {
        try (CallableStatement cs = conex.prepareCall("CALL SYSCS_UTIL.SYSCS_SET_DATABASE_PROPERTY(?, ?)")) {
            cs.setString(1, "derby.database.sqlAuthorization");
            cs.setString(2, "true");
            cs.execute();
        }

    }

    /**
     * Disables sql authorization (need to be tested because once sqlAuthorization is enable can not be disable)
     *
     * @throws SQLException
     */
    public void disableSqlAuthorization() throws SQLException {
        try (CallableStatement cs = conex.prepareCall("CALL SYSCS_UTIL.SYSCS_SET_DATABASE_PROPERTY(?, ?)")) {
            cs.setString(1, "derby.database.sqlAuthorization");
            cs.setString(2, "false");
            cs.execute();
        }

    }

    /**
     * Gets the default connection mode of database (user authorization)
     * 
     * @return
     * @throws SQLException
     */
    public String getDefaultConnectionMode() throws SQLException {
        ResultSet rs;
        String defaultConnectionMode;
        try (Statement s = conex.createStatement()) {
            rs = s.executeQuery("VALUES SYSCS_UTIL.SYSCS_GET_DATABASE_PROPERTY(" + "'derby.database.defaultConnectionMode')");
            rs.next();
            defaultConnectionMode = rs.getString(1);
        }
        rs.close();
        return defaultConnectionMode;

    }

    /**
     * Defining read write users
     *
     * @param usernames
     * @throws SQLException
     */
    public void setFullAccessUsers(ArrayList<String> usernames) throws SQLException {
        try (CallableStatement cs = conex.prepareCall("CALL SYSCS_UTIL.SYSCS_SET_DATABASE_PROPERTY(?, ?)")) {
            cs.setString(1, "derby.database.fullAccessUsers");

            StringBuilder stb = new StringBuilder();  //concat the proper user list separate by commas

            for (int i = 0; i < usernames.size(); i++) {
                stb.append(usernames.get(i));
                if (usernames.size() - 1 != i) {
                    stb.append(",");
                }

            }

            cs.setString(2, stb.toString());
            cs.execute();
        }

    }

    /**
     * Defining read only users
     * 
     * @param usernames
     * @throws SQLException
     */
    public void setReadOnlyAccessUsers(ArrayList<String> usernames) throws SQLException {
        try (CallableStatement cs = conex.prepareCall("CALL SYSCS_UTIL.SYSCS_SET_DATABASE_PROPERTY(?, ?)")) {
            cs.setString(1, "derby.database.readOnlyAccessUsers");

            StringBuilder stb = new StringBuilder();  //concat the proper user list separate by commas

            for (int i = 0; i < usernames.size(); i++) {
                stb.append(usernames.get(i));
                if (usernames.size() - 1 != i) {
                    stb.append(",");
                }

            }

            cs.setString(2, stb.toString());
            cs.execute();
        }

    }

    /**
     * Setting default connection access mode to NoAccess
     * (only full access users and read only users can log on the database)
     * 
     * @throws SQLException
     */
    public void setNoAccessConnectionMode() throws SQLException {
        try (Statement s = conex.createStatement()) {
            s.executeUpdate("CALL SYSCS_UTIL.SYSCS_SET_DATABASE_PROPERTY(" + "'derby.database.defaultConnectionMode', 'noAccess')");
        }
    }

    /**
     * Gets the users names list with full access
     * 
     * @return
     * @throws SQLException
     */
    public ArrayList<String> getFullAccessUsers() throws SQLException {
        ArrayList<String> userNames = new ArrayList<>();
        ResultSet rs;
        String userNamesbyCommas;
        try (Statement s = conex.createStatement()) {
            rs = s.executeQuery("VALUES SYSCS_UTIL.SYSCS_GET_DATABASE_PROPERTY(" + "'derby.database.fullAccessUsers')");
            rs.next();
            userNamesbyCommas = rs.getString(1);
        }

        rs.close();
        if (userNamesbyCommas != null) { //if users exist
            //use of stringTokenizer class to split the user names and add to arrayList

            StringTokenizer st = new StringTokenizer(userNamesbyCommas, ",");
            while (st.hasMoreTokens()) {
                userNames.add(st.nextToken());
            }

            return userNames;

        }

        //otherwise return null
        return null;


    }

    /**
     * Gets the users names list with read only access
     * 
     * @return
     * @throws SQLException
     */
    public ArrayList<String> getReadOnlyAccessUsers() throws SQLException {
        ArrayList<String> userNames = new ArrayList<>();
        ResultSet rs;
        String userNamesbyCommas;
        try (Statement s = conex.createStatement()) {
            rs = s.executeQuery("VALUES SYSCS_UTIL.SYSCS_GET_DATABASE_PROPERTY(" + "'derby.database.readOnlyAccessUsers')");
            rs.next();
            userNamesbyCommas = rs.getString(1);
        }

        rs.close();

        if (userNamesbyCommas != null) { //if users exist
            //use of stringTokenizer class to split the user names and add to arrayList

            StringTokenizer st = new StringTokenizer(userNamesbyCommas, ",");
            while (st.hasMoreTokens()) {
                userNames.add(st.nextToken());
            }

            return userNames;

        }

        //otherwise return null
        return null;

    }

    /**
     * Removes current authentication provider
     *
     * @throws SQLException
     */
    public void removeAuthenticationProvider() throws SQLException {
        try (Statement s = conex.createStatement()) {
            s.executeUpdate("CALL SYSCS_UTIL.SYSCS_SET_DATABASE_PROPERTY(" + "'derby.authentication.provider', null)");
        }

    }

    /**
     * Set the default connection mode to Full Access
     *
     * @throws SQLException
     */
    public void setFullAccessConnectionMode() throws SQLException {
        try (Statement s = conex.createStatement()) {
            s.executeUpdate("CALL SYSCS_UTIL.SYSCS_SET_DATABASE_PROPERTY(" + "'derby.database.defaultConnectionMode', 'fullAccess')");
        }

    }

    /** 
     * Set the default connection mode to readOnly Access
     * 
     * @throws SQLException
     */
    public void setReadOnlyAccessConnectionMode() throws SQLException {
        try (Statement s = conex.createStatement()) {
            s.executeUpdate("CALL SYSCS_UTIL.SYSCS_SET_DATABASE_PROPERTY(" + "'derby.database.defaultConnectionMode', 'readOnlyAccess')");
        }

    }

    /**
     * Delete all Full Access Users from builtIn repository
     * 
     * @throws SQLException
     */
    public void deleteAllFullAccessUsers() throws SQLException {
        try (Statement s = conex.createStatement()) {
            s.executeUpdate("CALL SYSCS_UTIL.SYSCS_SET_DATABASE_PROPERTY(" + "'derby.database.fullAccessUsers', null)");
        }

    }

    /**
     * Delete all Read Only Access Users from builtIn repository
     * 
     * @throws SQLException
     */
    public void deleteAllReadOnlyAccessUsers() throws SQLException {
        try (Statement s = conex.createStatement()) {
            s.executeUpdate("CALL SYSCS_UTIL.SYSCS_SET_DATABASE_PROPERTY(" + "'derby.database.readOnlyAccessUsers', null)");
        }

    }

    /**
     * Check out if user repository is empty (No users with privileges exist)
     * 
     * @return
     * @throws SQLException
     */
    public boolean isEmptyUserRepository() throws SQLException {
        if (getFullAccessUsers() == null && getReadOnlyAccessUsers() == null) {
            // no users with privileges exist 
            return true;
        } else { //otherwise at least one user exist

            return false;
        }

    }

    /**
     * Check out if in the user repository no Full Access Users exist
     * 
     * @return
     * @throws SQLException
     */
    public boolean isEmptyFullAccessUsersRepository() throws SQLException {
        if (getFullAccessUsers() == null) {
            //no Full Access Users exist at repository
            return true;
        } else { //otherwise at least one user with Full Access exist

            return false;
        }
    }

    /**
     * Check out if in the user repository no Read Only Access Users exist
     * 
     * @return
     * @throws SQLException
     */
    public boolean isEmptyReadOnlyAccessUsersRepository() throws SQLException {
        if (getReadOnlyAccessUsers() == null) {
            //no ReadOnly Access Users exist at repository
            return true;
        } else { //otherwise at least one user with ReadOnly Access exist

            return false;
        }
    }

}




