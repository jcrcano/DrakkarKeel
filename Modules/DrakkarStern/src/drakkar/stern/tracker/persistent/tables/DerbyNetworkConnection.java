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

import java.sql.*;


/**
 * Clase que establece una conexión a la BD del modo NetworkConnection
 */
public class DerbyNetworkConnection extends DerbyConnection {
    
    
    /**
     *
     * @throws DriverException
     */
    public DerbyNetworkConnection() throws DriverException {
        super("org.apache.derby.jdbc.ClientDriver");
    }
    
    /**
     *
     * @param autoCommit
     * @throws DriverException
     * @throws java.sql.SQLException
     */
    public DerbyNetworkConnection(boolean autoCommit) throws DriverException, SQLException {
        super("org.apache.derby.jdbc.ClientDriver", autoCommit);
    }
    
    
    
    /**
     * Metodo a ulilizar para abrir una conexion
     *
     * @param dataBaseUrl
     * @param user
     * @param password
     * @return
     * @throws java.sql.SQLException
     */
    public boolean open(String dataBaseUrl,String user,String password) throws SQLException {
        
        this.databaseurl = dataBaseUrl;
        this.user = user;
        this.password = password;
        
        if (!this.isOpen()){
            this.connection = DriverManager.getConnection("jdbc:derby://"+this.databaseurl, this.user,this.password);
            
            return true;
            
        }
        
        
        return false;
    }
    
    /**
     * Metodo para abrir una nueva conexion cliente-servidor
     * @param host
     * @param port
     * @param dataBaseUrl
     * @param user
     * @param password
     * @return
     * @throws java.sql.SQLException
     */
    public boolean open(String host, int port,  String dataBaseUrl,String user,String password) throws SQLException {
        
        this.databaseurl = dataBaseUrl;
        this.user = user;
        this.password = password;
        this.port = port;
        this.host = host;
        
        if (!this.isOpen()){
            this.connection = DriverManager.getConnection("jdbc:derby://"+ this.host+":"+this.port+"/"+this.databaseurl, this.user,this.password);
            return true;
        }
        
        return false;
    }

    @Override
    public boolean createDataBase(String path) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

   
    
    
}
