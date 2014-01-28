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

/**
 * Clase que representa una conexión embebida de Derby.
 */
public class DerbyEmbeddedConnection extends DerbyConnection {

    /**
     * @throws DriverException 
     */
    public DerbyEmbeddedConnection() throws DriverException {
        super("org.apache.derby.jdbc.EmbeddedDriver");
    }

    /**
     * @param autoCommit
     * @throws DriverException 
     * @throws java.sql.SQLException
     */
    public DerbyEmbeddedConnection(boolean autoCommit) throws DriverException, SQLException {
        super("org.apache.derby.jdbc.EmbeddedDriver", autoCommit);
    }

    /**
     * @param dbUrl
     * @param user
     * @param password
     * @return
     * @throws java.sql.SQLException
     */
    public boolean open(String dbUrl, String user, String password) throws SQLException {
        this.databaseurl = dbUrl;
        this.user = user;       
        this.password = DrakkarSecurity.decryptPassword(password);        

        if (!this.isOpen()) {
            this.connection = DriverManager.getConnection("jdbc:derby:" + this.databaseurl, this.user, this.password);
        }

        this.setAutoCommit(this.autoCommit);

        return true;
    }

    /**
     * 
     * @param dbUrl
     * @param user
     * @param password
     * @return
     * @throws SQLException
     */
    public boolean createDataBase(String dbUrl) throws SQLException {
        this.databaseurl = dbUrl;
        String statement = "jdbc:derby:" + this.databaseurl + "; create = true";
        this.connection = DriverManager.getConnection(statement);
        return true;
    }
}
