/*
 * DrakkarKeel - An Enterprise Collaborative Search Platform
 *
 * The contents of this file are subject under the terms described in the
 * DRAKKARKEEL_LICENSE file included in this distribution; you may not use this
 * file except in compliance with the License. 
 *
 * 2013-2014 DrakkarKeel Platform.
 */
package drakkar.stern.evaluation;

import drakkar.oar.DocumentMetaData;
import drakkar.oar.exception.QueryNotExistException;
import drakkar.oar.exception.SessionException;
import drakkar.oar.util.OutputMonitor;
import drakkar.stern.controller.DataBaseController;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Clase que calcula las métricas de precision y recall para las sesiones de búsquedas
 * que están guardadas en la BD
 */
public class RefereeDB {

    DataBaseController dbController;
    private List<String> queries = new ArrayList<>();
    Map<String, List<String>> hash;

    /**
     *
     * @param dbController
     * @param pathRelevJudg 
     */
    public RefereeDB(DataBaseController dbController , String pathRelevJudg) {
        this.dbController = dbController;
        hash = readQueryFile(pathRelevJudg);
    }

    /**
     * @param sessionName    nombre de la sesión
     * @param query          consulta de búsqueda
     * @param docs           documentos relevantes
     * @return
     * @throws SessionException
     * @throws QueryNotExistException
     */
    public float evaluatePrecision(String sessionName, String query, List<String> docs) throws SessionException, QueryNotExistException {
        long concurrency = this.getConcurrency(query, docs, hash);
        long retrievedDocs = getRetrievedDocumentsCount(sessionName, query);
        float precision = 0;
        if (concurrency > 0 && retrievedDocs > 0) {
            precision = (float)concurrency/retrievedDocs;
        }
        return precision;
    }

    /**
     *
     * @param sessionName    nombre de la sesión
     * @param query          consulta de búsqueda
     * @param docs           documentos relevantes de la consulta    
     * @return
     * @throws SessionException
     * @throws QueryNotExistException
     */
    public float evaluateRecall(String sessionName, String query, List<String> docs) throws SessionException, QueryNotExistException {
        long concurrency = this.getConcurrency(query, docs, hash);
        int relevDocs;

        relevDocs = getRelevantDocumentsCount(query, hash);
       
        float recall = 0;
        if (concurrency > 0 && relevDocs > 0) {
            recall = (float)concurrency/relevDocs;
       
        }
       
        return recall;
    }

    /**
     * Devuelve el valor de concurrencia de una lista de documentos seleccionados
     * con los respectivos documentos proporcionados por el juicio de relevancia
     *
     * @param query  consulta de búsqueda
     *
     * @return valor de concurrencia
     */
    private long getConcurrency(String query, List<String> docs, Map<String, List<String>> hash) throws QueryNotExistException {
               boolean flag = hash.containsKey(query);

        if (flag) {           
            List<String> querysList = hash.get(query);
            int count = 0;
            for (String string : docs) {
                               flag = querysList.contains(string);

                if (flag) {
                    count++;
                }
            }
            return count;
        } else {
            return -1;
        }
    }

    /**
     * Lee el fichero que contiene el juicio de relevancia para el experimento
     *
     * @return juicio de relevancia para el experimento
     */
    private Map<String, List<String>> readQueryFile(String relvJudg) {
        String value = "";
        BufferedReader readFile = null;
        Map<String, List<String>> tempHash = new HashMap<>();

        try {
            FileInputStream fis = new FileInputStream(relvJudg);
            readFile = new BufferedReader(new InputStreamReader(fis));
            String[] querys = null;
            List<String> temp;

            for (;;) {
                value = readFile.readLine();

                if (value != null && !value.equals("")) {

                    querys = value.split("#");
                    temp = new ArrayList<>();
                    queries.add(querys[0]);

                    for (int i = 1; i < querys.length; i++) {
                        temp.add(querys[i]);
                    }
                    tempHash.put(querys[0], temp);

                } else {
                   
                    readFile.close();

                    return tempHash;

                }
            }

        } catch (FileNotFoundException err) {
          OutputMonitor.printStream("", err);
        } catch (IOException err) {
            OutputMonitor.printStream("", err);

        } finally {
            try {
                readFile.close();
            } catch (IOException ex) {
                OutputMonitor.printStream("", ex);
            }
        }

        return tempHash;
    }

    

    /**
     * Devuelve el total de documentos relevantes proporcionados por el
     * juicio de relevancia para la consulta específicada
     *
     * @param query  consulta de búsqueda
     *
     * @return total de documentos relevantes
     */
    private int getRelevantDocumentsCount(String query, Map<String, List<String>> hash) {

        boolean flag = hash.containsKey(query);

        if (flag) {
            List<String> querysList = hash.get(query);
            int count = querysList.size();

            return count;

        } else {
            return -1;
        }

    }

    /**
     * Devuelve el total de documentos recuperados de una
     * consulta y sesión determinada que está guardados en la BD
     */
    private long getRetrievedDocumentsCount(String sessionName, String query) {
        long count = 0;

        if (dbController.isOpen()) {
            try {
             
                List<DocumentMetaData> list = dbController.getSearchResults(sessionName, query);
                count = list.size();
              
            } catch (SQLException ex) {
                OutputMonitor.printStream("SQL", ex);
            }

        }

        return count;
    }


    private void exportEvaluation(String sessionName, List<String> evaluations) {
        File eval = new File("Evaluations.xls");
        PrintWriter write = null;

        try {
            write = new PrintWriter(new FileOutputStream(eval));
            write.println(sessionName);

            for (String string : evaluations) {
                write.println(string);
            }
            write.close();

        } catch (FileNotFoundException err) {
            write.close();
        }
    }

    /**
     * @return the queries
     */
    public List<String> getQueries() {
        return queries;
    }

    /**
     * @param queries the queries to set
     */
    public void setQueries(List<String> queries) {
        this.queries = queries;
    }
}
