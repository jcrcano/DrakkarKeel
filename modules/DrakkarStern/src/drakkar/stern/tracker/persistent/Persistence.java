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

/**
 * Esta clase contiene las tablas de la BD
 */
public class Persistence {

  public static String[] createTables = {
        "CREATE TABLE DRAKKARKEEL.MEMBERSHIP(ID_MSHIP INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY,   MSHIP_TYPE VARCHAR(255) NOT NULL)",
        "CREATE TABLE DRAKKARKEEL.SEARCH_SESSION (SESSION_TOPIC VARCHAR(255)PRIMARY KEY, DESCRIPTION VARCHAR(650),   CHAIRMAN VARCHAR (255) NOT NULL,   INTEGRITY_CRITERIA SMALLINT,   MAX_MEMBER_NUMBER INTEGER,   MIN_MEMBER_NUMBER INTEGER,   CURRENT_MEMBER_NUMBER INTEGER,   ENABLE SMALLINT,   START_DATE DATE NOT NULL WITH DEFAULT CURRENT_DATE,   STOP_DATE DATE NOT NULL WITH DEFAULT CURRENT_DATE,   ID_MSHIP INTEGER,   FOREIGN KEY(ID_MSHIP) REFERENCES DRAKKARKEEL.MEMBERSHIP(ID_MSHIP))",
        "CREATE TABLE DRAKKARKEEL.ROL(ID_ROL INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY,ROL_NAME VARCHAR(255) NOT NULL)",
        "CREATE TABLE DRAKKARKEEL.SEEKER_STATE(ID_STATE INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY,STATE_TYPE VARCHAR(255) NOT NULL)",
        "CREATE TABLE DRAKKARKEEL.SEEKER(SEEKER_USER VARCHAR(255)PRIMARY KEY,SEEKER_NAME VARCHAR(255) NOT NULL, PASSWORD VARCHAR(255) NOT NULL,AVATAR VARCHAR(3072) FOR BIT DATA,DESCRIPTION VARCHAR(650),EMAIL VARCHAR(255) NOT NULL,ID_ROL INTEGER,FOREIGN KEY(ID_ROL) REFERENCES DRAKKARKEEL.ROL(ID_ROL), ID_STATE INTEGER,FOREIGN KEY(ID_STATE) REFERENCES DRAKKARKEEL.SEEKER_STATE(ID_STATE))",
        "CREATE TABLE DRAKKARKEEL.SEARCH_PRINCIPLE(ID_SP INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY,PRINCIPLE VARCHAR(255) NOT NULL)",
        "CREATE TABLE DRAKKARKEEL.QUERY(ID_QUERY INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY,TEXT VARCHAR(255) NOT NULL,DATE_QUERY DATE NOT NULL WITH DEFAULT CURRENT_DATE,ID_SP INTEGER,FOREIGN KEY(ID_SP) REFERENCES DRAKKARKEEL.SEARCH_PRINCIPLE(ID_SP))",
        "CREATE TABLE DRAKKARKEEL.SEARCH_ENGINE(ID_SE INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY,SE_NAME VARCHAR(255) NOT NULL)",
        "CREATE TABLE DRAKKARKEEL.WEB_SERVICE(ID_WS INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY,WS_NAME VARCHAR(255) NOT NULL)",
        "CREATE TABLE DRAKKARKEEL.WEB_SEARCH_ENGINE(ID_WSE INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY,WSE_NAME VARCHAR(255) NOT NULL)",
        "CREATE TABLE DRAKKARKEEL.SEARCH_SESSION_QUERY(PRIMARY KEY(SESSION_TOPIC, ID_QUERY),SESSION_TOPIC VARCHAR(255),FOREIGN KEY(SESSION_TOPIC) REFERENCES DRAKKARKEEL.SEARCH_SESSION(SESSION_TOPIC),ID_QUERY INTEGER,FOREIGN KEY(ID_QUERY) REFERENCES DRAKKARKEEL.QUERY(ID_QUERY))",
        "CREATE TABLE DRAKKARKEEL.QUERY_SEARCH_ENGINE(PRIMARY KEY(ID_SE, ID_QUERY),ID_SE INTEGER,FOREIGN KEY(ID_SE) REFERENCES DRAKKARKEEL.SEARCH_ENGINE(ID_SE),ID_QUERY INTEGER,FOREIGN KEY(ID_QUERY) REFERENCES DRAKKARKEEL.QUERY(ID_QUERY))",
        "CREATE TABLE DRAKKARKEEL.QUERY_WEB_SEARCH_ENGINE(PRIMARY KEY(ID_WSE, ID_QUERY),ID_WSE INTEGER,FOREIGN KEY(ID_WSE) REFERENCES DRAKKARKEEL.WEB_SEARCH_ENGINE(ID_WSE),ID_QUERY INTEGER,FOREIGN KEY(ID_QUERY) REFERENCES DRAKKARKEEL.QUERY(ID_QUERY))",
        "CREATE TABLE DRAKKARKEEL.QUERY_WEB_SERVICE(PRIMARY KEY(ID_WS, ID_QUERY),ID_WS INTEGER,FOREIGN KEY(ID_WS) REFERENCES DRAKKARKEEL.WEB_SERVICE(ID_WS),ID_QUERY INTEGER,FOREIGN KEY(ID_QUERY) REFERENCES DRAKKARKEEL.QUERY(ID_QUERY))",
        "CREATE TABLE DRAKKARKEEL.INDEX(ID_INDEX INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY,URI VARCHAR(255) NOT NULL,CREATION_DATE DATE NOT NULL WITH DEFAULT CURRENT_DATE,DOC_COUNT INTEGER,ID_SE INTEGER,FOREIGN KEY(ID_SE) REFERENCES DRAKKARKEEL.SEARCH_ENGINE(ID_SE),ID_WSE INTEGER,FOREIGN KEY(ID_WSE) REFERENCES DRAKKARKEEL.WEB_SEARCH_ENGINE(ID_WSE))",
        "CREATE TABLE DRAKKARKEEL.SEARCH_RESULT(ID_SR INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY,URI VARCHAR(650) NOT NULL,SCORE DOUBLE PRECISION,INDEX INTEGER,SR_NAME VARCHAR(255) NOT NULL,SR_SIZE INTEGER,SR_TYPE VARCHAR(255) NOT NULL,REVIEW SMALLINT,SUMMARY VARCHAR(650),SESSION_TOPIC  VARCHAR(255),FOREIGN KEY(SESSION_TOPIC) REFERENCES DRAKKARKEEL.SEARCH_SESSION(SESSION_TOPIC))",
        "CREATE TABLE DRAKKARKEEL.RECOMMENDATION(ID_REC INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY,SEEKER_RECEPTOR VARCHAR(255) NOT NULL,TEXT VARCHAR(255),REC_DATE DATE NOT NULL WITH DEFAULT CURRENT_DATE,SEEKER_USER VARCHAR(255) NOT NULL,FOREIGN KEY(SEEKER_USER) REFERENCES DRAKKARKEEL.SEEKER(SEEKER_USER),ID_SR INTEGER,FOREIGN KEY(ID_SR) REFERENCES DRAKKARKEEL.SEARCH_RESULT(ID_SR),SESSION_TOPIC  VARCHAR(255),FOREIGN KEY(SESSION_TOPIC) REFERENCES DRAKKARKEEL.SEARCH_SESSION(SESSION_TOPIC))",
        "CREATE TABLE DRAKKARKEEL.MARKUP(ID_MARKUP INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY,COMMENT VARCHAR(255),RELEVANCE INTEGER,MARKUP_DATE DATE NOT NULL WITH DEFAULT CURRENT_DATE,SEEKER_USER VARCHAR(255) NOT NULL,FOREIGN KEY(SEEKER_USER) REFERENCES DRAKKARKEEL.SEEKER(SEEKER_USER),ID_SR INTEGER,FOREIGN KEY(ID_SR) REFERENCES DRAKKARKEEL.SEARCH_RESULT(ID_SR),SESSION_TOPIC  VARCHAR(255),FOREIGN KEY(SESSION_TOPIC) REFERENCES DRAKKARKEEL.SEARCH_SESSION(SESSION_TOPIC))",
        "CREATE TABLE DRAKKARKEEL.MESSAGE(ID_MSG INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY,TEXT VARCHAR(255) NOT NULL,SEEKER_RECEPTOR VARCHAR(255) NOT NULL,MSG_DATE DATE NOT NULL WITH DEFAULT CURRENT_DATE,SEEKER_USER VARCHAR(255) NOT NULL,FOREIGN KEY(SEEKER_USER) REFERENCES DRAKKARKEEL.SEEKER(SEEKER_USER),SESSION_TOPIC  VARCHAR(255),FOREIGN KEY(SESSION_TOPIC) REFERENCES DRAKKARKEEL.SEARCH_SESSION(SESSION_TOPIC))",
        "CREATE TABLE DRAKKARKEEL.SEARCH_SESSION_SEEKER(PRIMARY KEY(SESSION_TOPIC, SEEKER_USER),DECLINE SMALLINT,SESSION_TOPIC  VARCHAR(255),FOREIGN KEY(SESSION_TOPIC) REFERENCES DRAKKARKEEL.SEARCH_SESSION(SESSION_TOPIC),SEEKER_USER VARCHAR(255) NOT NULL,FOREIGN KEY(SEEKER_USER) REFERENCES DRAKKARKEEL.SEEKER(SEEKER_USER))",
        "CREATE TABLE DRAKKARKELL.SEEKER_QUERY(PRIMARY KEY(ID_QUERY, SEEKER_USER),ID_QUERY INTEGER,FOREIGN KEY(ID_QUERY) REFERENCES DRAKKARKEEL.QUERY(ID_QUERY),SEEKER_USER VARCHAR(255) NOT NULL,FOREIGN KEY(SEEKER_USER) REFERENCES DRAKKARKEEL.SEEKER(SEEKER_USER))",
        "CREATE TABLE DRAKKARKEEL.QUERY_SEARCH_RESULT(PRIMARY KEY(ID_QUERY, ID_SR),ID_QUERY INTEGER,FOREIGN KEY(ID_QUERY) REFERENCES DRAKKARKEEL.QUERY(ID_QUERY),ID_SR INTEGER,FOREIGN KEY(ID_SR) REFERENCES DRAKKARKEEL.SEARCH_RESULT(ID_SR))",
        "CREATE TABLE DRAKKARKEEL.SEARCH_ENGINE_SEARCH_RESULT(PRIMARY KEY(ID_SE, ID_SR),ID_SE INTEGER,FOREIGN KEY(ID_SE) REFERENCES DRAKKARKEEL.SEARCH_ENGINE(ID_SE),ID_SR INTEGER,FOREIGN KEY(ID_SR) REFERENCES DRAKKARKEEL.SEARCH_RESULT(ID_SR))",
        "CREATE TABLE DRAKKARKEEL.WEB_SEARCH_ENGINE_SEARCH_RESULT(PRIMARY KEY(ID_WSE, ID_SR),ID_WSE INTEGER,FOREIGN KEY(ID_WSE) REFERENCES DRAKKARKEEL.WEB_SEARCH_ENGINE(ID_WSE),ID_SR INTEGER,FOREIGN KEY(ID_SR) REFERENCES DRAKKARKEEL.SEARCH_RESULT(ID_SR))",
        "CREATE TABLE DRAKKARKEEL.WEB_SERVICE_SEARCH_RESULT(PRIMARY KEY(ID_WS, ID_SR),ID_WS INTEGER,FOREIGN KEY(ID_WS) REFERENCES DRAKKARKEEL.WEB_SERVICE(ID_WS),ID_SR INTEGER,FOREIGN KEY(ID_SR) REFERENCES DRAKKARKEEL.SEARCH_RESULT(ID_SR))",        
        "CREATE TABLE DRAKKARKEEL.COLLECTION( ID_COLLECTION INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY,COLLECTION_PATH VARCHAR(255) NOT NULL,CONTEXT VARCHAR(255) NOT NULL,DOC_COUNT INTEGER, DIRECT_PATH SMALLINT)",
        "CREATE TABLE DRAKKARKEEL.SEARCH_SESSION_COLLECTION(PRIMARY KEY(SESSION_TOPIC, ID_COLLECTION),SESSION_TOPIC  VARCHAR(255),FOREIGN KEY(SESSION_TOPIC) REFERENCES DRAKKARKEEL.SEARCH_SESSION(SESSION_TOPIC),ID_COLLECTION INTEGER,FOREIGN KEY(ID_COLLECTION) REFERENCES DRAKKARKEEL.COLLECTION(ID_COLLECTION))",
        "CREATE TABLE DRAKKARKEEL.INDEX_COLLECTION(PRIMARY KEY(ID_INDEX, ID_COLLECTION),USER_CVS VARCHAR(255),PASSWORD_CVS VARCHAR(650),REPOSITORY_NAME VARCHAR(255),ID_INDEX INTEGER,FOREIGN KEY(ID_INDEX) REFERENCES DRAKKARKEEL.INDEX(ID_INDEX),ID_COLLECTION INTEGER,FOREIGN KEY(ID_COLLECTION) REFERENCES DRAKKARKEEL.COLLECTION(ID_COLLECTION))"
    };


   public static String[] deleteTables = {
        "DROP TABLE DRAKKARKEEL.WEB_SERVICE_SEARCH_RESULT",
        "DROP TABLE DRAKKARKEEL.WEB_SEARCH_ENGINE_SEARCH_RESULT",
        "DROP TABLE DRAKKARKEEL.QUERY_WEB_SEARCH_ENGINE",
        "DROP TABLE DRAKKARKEEL.QUERY_WEB_SERVICE",
        "DROP TABLE DRAKKARKEEL.SEARCH_SESSION_COLLECTION",
        "DROP TABLE DRAKKARKEEL.INDEX_COLLECTION",
        "DROP TABLE DRAKKARKEEL.COLLECTION",
        "DROP TABLE DRAKKARKEEL.WEB_SERVICE",
        "DROP TABLE DRAKKARKEEL.SEARCH_SESSION_SEEKER",
        "DROP TABLE DRAKKARKELL.SEEKER_QUERY",
        "DROP TABLE DRAKKARKEEL.SEARCH_ENGINE_SEARCH_RESULT",
        "DROP TABLE DRAKKARKEEL.QUERY_SEARCH_RESULT",
        "DROP TABLE DRAKKARKEEL.MESSAGE",
        "DROP TABLE DRAKKARKEEL.MARKUP",
        "DROP TABLE DRAKKARKEEL.RECOMMENDATION",
        "DROP TABLE DRAKKARKEEL.SEARCH_RESULT",
        "DROP TABLE DRAKKARKEEL.INDEX",        
        "DROP TABLE DRAKKARKEEL.WEB_SEARCH_ENGINE",
        "DROP TABLE DRAKKARKEEL.QUERY_SEARCH_ENGINE",
        "DROP TABLE DRAKKARKEEL.SEARCH_SESSION_QUERY",
        "DROP TABLE DRAKKARKEEL.SEARCH_ENGINE",
        "DROP TABLE DRAKKARKEEL.QUERY",
        "DROP TABLE DRAKKARKEEL.SEARCH_PRINCIPLE",
        "DROP TABLE DRAKKARKEEL.SEEKER",
        "DROP TABLE DRAKKARKEEL.SEEKER_STATE",
        "DROP TABLE DRAKKARKEEL.ROL",
        "DROP TABLE DRAKKARKEEL.SEARCH_SESSION",
        "DROP TABLE DRAKKARKEEL.MEMBERSHIP"
         };
}