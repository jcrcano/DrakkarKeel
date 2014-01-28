/*
 * DrakkarKeel - An Enterprise Collaborative Search Platform
 *
 * The contents of this file are subject under the terms described in the
 * DRAKKARKEEL_LICENSE file included in this distribution; you may not use this
 * file except in compliance with the License. 
 *
 * 2013-2014 DrakkarKeel Platform.
 */
package drakkar.stern.tracker.persistent.tables;

import drakkar.oar.security.DrakkarSecurity;
import java.sql.*;
import java.util.*;

/**
 * Clase que establece la conexión con el driver de Derby y resto de operaciones
 * de la BD
 */

public abstract class DerbyConnection {

    protected Connection connection;
    protected String host;
    protected int port;
    protected String user;
    protected String password;
    protected String databaseurl;
    protected boolean autoCommit = true;

    /**
     *
     * @param driverURI
     * @throws DriverException
     */
    public DerbyConnection(String driverURI) throws DriverException {
        try {
            // Driver registration...
            Class.forName(driverURI).newInstance();
        } catch (java.lang.ClassNotFoundException e) {
            throw new DriverException("Class not found in JDBC driver");
        } catch (java.lang.InstantiationException e) {
            throw new DriverException("The driver could not be instantiated");
        } catch (java.lang.IllegalAccessException e) {
            throw new DriverException("Illegal or incorrect access to the Database");
        }
    }

    /**
     *
     * @param driverURI
     * @param autoCommit
     * @throws DriverException
     * @throws java.sql.SQLException
     */
    public DerbyConnection(String driverURI, boolean autoCommit) throws DriverException, SQLException {
        this(driverURI);
        this.autoCommit = autoCommit;
    }

    public abstract boolean open(String databaseurl, String user, String password) throws SQLException;


   public abstract boolean createDataBase(String path)throws SQLException;


    /**
     * Cerrar la conexion
     * @return true si se cierra la conexion o false en caso contrario
     */
    public final boolean closeConnection() {
        try {
            if (this.isOpen()) {
                this.connection.close();
            }
            return true;

        } catch (java.sql.SQLException e) {
            return false;
        }
    }

    /**
     * Devuelve el objeto Connection, de la actual conexion
     * @return
     */
    public Connection getConnection() {
        return this.connection;
    }

    /**
     *
     * @param storedProc
     * @param params
     * @return
     * @throws java.sql.SQLException
     */
    public ResultSet execute(String storedProc, ArrayList params) throws SQLException {
        if (!this.isOpen()) {
            this.open(this.databaseurl, this.user, this.password);
        }

        int paramsCount = params.size();
        String possibleParams = "";
        for (int i = 0; i < paramsCount; i++) {
            possibleParams.concat("?");
            if (i + 1 < paramsCount) {
                possibleParams.concat(",");
            }
        }

        CallableStatement callStoreProc = this.connection.prepareCall("{ call " + storedProc + "(" + possibleParams + ") }");

        for (int i = 0; i < paramsCount; i++) {
            callStoreProc.setObject(i + 1, params.get(i));
        }

        ResultSet result = callStoreProc.executeQuery();
        return result;
    }

    /**
     *
     * @param storedProc
     * @return
     * @throws java.sql.SQLException
     */
    public ResultSet execute(String storedProc) throws SQLException {
        if (!this.isOpen()) {
            this.open(this.databaseurl, this.user, this.password);
        }

        CallableStatement callStoreProc = this.connection.prepareCall("{ call " + storedProc + "() }");

        return callStoreProc.executeQuery();
    }



    /**
     *
     * @param SQLquery
     * @return
     *
     */
    public ResultSet executeQuery(String SQLquery) {
        try {
            if (!this.isOpen()) {
                this.open(this.databaseurl, this.user, this.password);
            }


            Statement stm = this.connection.createStatement();
            return stm.executeQuery(SQLquery);
        } catch (java.sql.SQLException e) {
            return null;
        }
    }

     /**
     *
     * @param backupDirectory
     *
     * @throws SQLException
     */
    public void backUpDatabase(String backupDirectory) throws SQLException {
        try (CallableStatement cs = this.connection.prepareCall("CALL SYSCS_UTIL.SYSCS_BACKUP_DATABASE(?)")) {
            cs.setString(1, backupDirectory);
            cs.execute();
        }

    }

    /**
     *
     * @param backupDirectory
     * @param enterUser
     * @param enterPassword
     * @throws SQLException
     */
    public void restoringDatabase(String backupDirectory, String enterUser, String enterPassword) throws SQLException {
      
        if (this.databaseurl != null) {
            this.connection = DriverManager.getConnection("jdbc:derby:" + this.databaseurl + ";restoreFrom=" + backupDirectory, enterUser, DrakkarSecurity.decryptPassword(enterPassword));
          
        }

    }


    /**
     *
     * @return true si la conexion esta abierta o false en caso contrario
     */
    public final boolean isOpen() {
        try {
            return (this.connection == null) ? false : !this.connection.isClosed();
        } catch (java.sql.SQLException e) {
            return false;
        }
    }

    /**
     *
     * @throws java.sql.SQLException
     */
    public void commit() throws SQLException {
        if (this.isOpen()) {
            this.connection.commit();
        }
    }

    /**
     *
     * @throws java.sql.SQLException
     */
    public void rollback() throws SQLException {
        if (this.isOpen()) {
            this.connection.rollback();
        }
    }

    /**
     *
     * @param autoCommit
     * @throws java.sql.SQLException
     */
    public void setAutoCommit(boolean autoCommit) throws SQLException {
        if (this.isOpen()) {
            this.connection.setAutoCommit(autoCommit);
        }
        this.autoCommit = autoCommit;
    }

    /**
     *
     * @return
     */
    public boolean getAutoCommit() {
        return this.autoCommit;
    }

    /**
     *
     * @return
     */
    public String getHost() {
        return this.host;
    }

    /**
     *
     * @return
     */
    public int getPort() {
        return this.port;
    }

    /**
     *
     * @return
     */
    public String getUser() {
        return this.user;
    }

    /**
     *
     * @return
     */
    public String getPassword() {
        return this.password;
    }

    /**
     *
     * @return
     */
    public String getDatabaseurl() {
        return this.databaseurl;
    }

    /**
     *
     * @param connection
     */
    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    /**
     *
     * @param host
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     *
     * @param user
     */
    public void setUser(String user) {
        this.user = user;
    }

    /**
     *
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     *
     * @param databaseurl
     */
    public void setDatabaseurl(String databaseurl) {
        this.databaseurl = databaseurl;
    }
}