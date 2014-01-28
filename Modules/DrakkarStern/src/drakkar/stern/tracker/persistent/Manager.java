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

import drakkar.stern.tracker.persistent.tables.DerbyConnection;
import drakkar.stern.tracker.persistent.tables.PersistentOperations;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *  Clase que se encarga de establecer los valores por defecto que debe tener
 *  algunas tablas de la BD, además de instanciar las dos clases que contienen
 *  los métodos a utilizar en la aplicación cliente y servidora.
 */
public class Manager {

    DerbyConnection connection;
    private FacadeDBProw client;
    private FacadeDBStern server;
    private DBUtil util;

    /**
     * Consntructor de la clase
     *
     * @param connection
     */
    public Manager(DerbyConnection connection) {
        this.connection = connection;
        util = new DBUtil(connection);
        client = new FacadeDBProw(connection, util);
        server = new FacadeDBStern(connection, util);
    }

    /**
     * Llena las tablas de Membership, SE, Rol, SP, State
     *
     * @throws SQLException        si ocurre alguna SQLException durante la ejecución  de la operación
     */
    public void setConstantValues() throws SQLException {

        if (util.isEmpty("DRAKKARKEEL.MEMBERSHIP")) {
            this.setMembership();
        }
        if (util.isEmpty("DRAKKARKEEL.ROL")) {
            this.setRoles();
        }
        if (util.isEmpty("DRAKKARKEEL.SEARCH_ENGINE")) {
            this.setSearchEngines();
        }
        if (util.isEmpty("DRAKKARKEEL.SEARCH_PRINCIPLE")) {
            this.setSearchPrinciple();
        }
        if (util.isEmpty("DRAKKARKEEL.SEEKER_STATE")) {
            this.setStates();
        }
        if (util.isEmpty("DRAKKARKEEL.SEARCH_SESSION")) {
            this.setDefaultSession();
        }

    }

    /**
     * Llena la tabla SEARCH_ENGINE
     * 
     * @throws SQLException        si ocurre alguna SQLException durante la ejecución  de la operación
     */
    private void setSearchEngines() throws SQLException {
        String[] fields = new String[1];
        fields[0] = "SE_NAME";

        Object[][] values = new String[6][1];
        values[0][0] = "lucene";
        values[1][0] = "minion";
        values[2][0] = "terrier";
        values[3][0] = "indri";
        values[4][0] = "metasearcher";
        values[5][0] = "svnsearcher";

        PersistentOperations.insert(connection, "DRAKKARKEEL.SEARCH_ENGINE", fields, values);

    }

    /**
     * Llena la tabla DRAKKARKEEL.MEMBERSHIP
     *
     * @throws SQLException        si ocurre alguna SQLException durante la ejecución  de la operación
     */
    private void setMembership() throws SQLException {
        String[] fields = new String[1];
        fields[0] = "MSHIP_TYPE";

        Object[][] values = new String[3][1];
        values[0][0] = "Dynamic and open";
        values[1][0] = "Dynamic and close";
        values[2][0] = "Static";


        PersistentOperations.insert(connection, "DRAKKARKEEL.MEMBERSHIP", fields, values);

    }

    /**
     * Llena la tabla DRAKKARKEEL.ROL
     *
     * @throws SQLException        si ocurre alguna SQLException durante la ejecución  de la operación
     */
    private void setRoles() throws SQLException {
        String[] fields = new String[1];
        fields[0] = "ROL_NAME";

        Object[][] values = new String[3][1];
        values[0][0] = "Member";
        values[1][0] = "Potential Member";
        values[2][0] = "Chairman";

        PersistentOperations.insert(connection, "DRAKKARKEEL.ROL", fields, values);


    }

    /**
     * Llena la tabla DRAKKARKEEL.SEEKER_STATE
     *
     * @throws SQLException        si ocurre alguna SQLException durante la ejecución  de la operación
     */
    private void setStates() throws SQLException {
        String[] fields = new String[1];
        fields[0] = "STATE_TYPE";

        Object[][] values = new String[4][1];
        values[0][0] = "Online";
        values[1][0] = "Offline";
        values[2][0] = "Away";
        values[3][0] = "Busy";

        PersistentOperations.insert(connection, "DRAKKARKEEL.SEEKER_STATE", fields, values);


    }

    /**
     * Llena la tabla DRAKKARKEEL.SEARCH_PRINCIPLE
     *
     * @throws SQLException        si ocurre alguna SQLException durante la ejecución  de la operación
     */
    private void setSearchPrinciple() throws SQLException {
        String[] fields = new String[1];
        fields[0] = "PRINCIPLE";

        Object[][] values = new String[6][1];
        values[0][0] = "Single Search";
        values[1][0] = "Meta Search";
        values[2][0] = "Multi Search";
        values[3][0] = "Single Search and Split";
        values[4][0] = "Meta Search and Split";
        values[5][0] = "Multi Search and Switch";

        PersistentOperations.insert(connection, "DRAKKARKEEL.SEARCH_PRINCIPLE", fields, values);

    }

    /**
     * Inserta la sesión por defecto (DefaultSession)
     *
     * @throws SQLException        si ocurre alguna SQLException durante la ejecución  de la operación
     */
    private void setDefaultSession() throws SQLException {

        String[] fields = new String[10];
        fields[0] = "SESSION_TOPIC";
        fields[1] = "DESCRIPTION";
        fields[2] = "CHAIRMAN";
        fields[3] = "INTEGRITY_CRITERIA";
        fields[4] = "MAX_MEMBER_NUMBER";
        fields[5] = "MIN_MEMBER_NUMBER";
        fields[6] = "CURRENT_MEMBER_NUMBER";
        fields[7] = "ID_MSHIP";
        fields[8] = "START_DATE";
        fields[9] = "ENABLE";

        Object[] oneValue = new Object[10];
        oneValue[0] = "DefaultSession";
        oneValue[1] = " ";
        oneValue[2] = " ";
        oneValue[3] = true;
        oneValue[4] = 0;
        oneValue[5] = 0;
        oneValue[6] = 0;
        oneValue[7] = 1;
        oneValue[8] = DBUtil.getCurrentDate();
        oneValue[9] = true;

        PersistentOperations.insert(connection, "DRAKKARKEEL.SEARCH_SESSION", fields, oneValue);

    }

    /**
     * Elimina los datos de una tabla
     *
     * @param tableName           nombre de la tabla
     *
     * @throws SQLException        si ocurre alguna SQLException durante la ejecución  de la operación
     */
    public void cleanTable(String tableName) throws SQLException {

        PersistentOperations.deleteAll(connection, tableName);

    }

    /**
     * Elimina los datos de todas las tablas
     * 
     * @throws SQLException        si ocurre alguna SQLException durante la ejecución  de la operación
     */
    public void cleanAllTables() throws SQLException {

        String[] list = new String[28];
        list[0] = "DRAKKARKEEL.SEARCH_SESSION_SEEKER";
        list[1] = "DRAKKARKELL.SEEKER_QUERY";
        list[2] = "DRAKKARKEEL.QUERY_SEARCH_RESULT";
        list[3] = "DRAKKARKEEL.SEARCH_ENGINE_SEARCH_RESULT";
        list[4] = "DRAKKARKEEL.MESSAGE";
        list[5] = "DRAKKARKEEL.MARKUP";
        list[6] = "DRAKKARKEEL.RECOMMENDATION";
        list[7] = "DRAKKARKEEL.SEARCH_RESULT";
        list[8] = "DRAKKARKEEL.INDEX";
        list[9] = "DRAKKARKEEL.QUERY_SEARCH_ENGINE";
        list[10] = "DRAKKARKEEL.QUERY_WEB_SEARCH_ENGINE";
        list[11] = "DRAKKARKEEL.QUERY_WEB_SERVICE";
        list[12] = "DRAKKARKEEL.SEARCH_SESSION_COLLECTION";
        list[13] = "DRAKKARKEEL.WEB_SEARCH_ENGINE_SEARCH_RESULT";
        list[14] = "DRAKKARKEEL.WEB_SERVICE_SEARCH_RESULT";
        list[15] = "DRAKKARKEEL.WEB_SERVICE";
        list[16] = "DRAKKARKEEL.WEB_SEARCH_ENGINE";
        list[17] = "DRAKKARKEEL.SEARCH_SESSION_QUERY";
        list[18] = "DRAKKARKEEL.COLLECTION";
        list[19] = "DRAKKARKEEL.SEARCH_ENGINE";
        list[20] = "DRAKKARKEEL.QUERY";
        list[21] = "DRAKKARKEEL.SEARCH_PRINCIPLE";
        list[22] = "DRAKKARKEEL.SEEKER";
        list[23] = "DRAKKARKEEL.SEEKER_STATE";
        list[24] = "DRAKKARKEEL.ROL";
        list[25] = "DRAKKARKEEL.SEARCH_SESSION";
        list[26] = "DRAKKARKEEL.MEMBERSHIP";
        list[27] = "DRAKKARKEEL.INDEX_COLLECTION";

        for (int i = 0; i < list.length; i++) {
            String tableName = list[i];
            PersistentOperations.deleteAll(connection, tableName);
        }


    }

    /**
     * @return the client
     */
    public FacadeDBProw getClient() {
        return client;
    }

    /**
     * @param client the client to set
     */
    public void setClient(FacadeDBProw client) {
        this.client = client;
    }

    /**
     * @return the server
     */
    public FacadeDBStern getServer() {
        return server;
    }

    /**
     * @param server the server to set
     */
    public void setServer(FacadeDBStern server) {
        this.server = server;
    }

    /**
     * Método para crear las tablas de la BD y eliminar las que están
     * 
     * @param sql             script de la BD (sql)
     * 
     * @throws SQLException        si ocurre alguna SQLException durante la ejecución  de la operación
     */
    public void createDB(String sql) throws SQLException {
        Statement s = null;
        s = connection.getConnection().createStatement();
        s.execute(sql);
    }
}
