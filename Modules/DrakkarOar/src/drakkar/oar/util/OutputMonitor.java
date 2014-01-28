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

import static drakkar.oar.util.SettingProperties.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clase para manejar los mensajes emitidos por el framework
 */
public class OutputMonitor {

    /**
     *
     */
    public static final int ERROR_MESSAGE = 0;
    /**
     *
     */
    public static final int INFORMATION_MESSAGE = 1;
    /**
     *
     */
    public static final int WARNING_MESSAGE = 2;
    /**
     *
     */
    public static final int QUESTION_MESSAGE = 3;
     /**
     *
     */
    public static final int TRACE_MESSAGE = 3;
    /**
     *
     */
    public static final int PLAIN_MESSAGE = -1;
    private static String logFilePath = "./logs/system.log";
    private static File logFile = new File(logFilePath);
    private static FileOutputStream streamLogFile = null;
    private static PrintWriter logFileWriter = null;

    /**
     *
     * @param message
     */
    public static void printLine(String message) {
        printLine(message, PLAIN_MESSAGE);
    }

    /**
     *
     * @param message
     * @param messageType
     */
    public static void printLine(String message, int messageType) {
        try {

            if (ACTIVE_MONITOR_PROPERTY) {

                String header = "";

                switch (messageType) {
                    case ERROR_MESSAGE:
                        header = "ERROR: ";
                        break;
                    case INFORMATION_MESSAGE:
                        header = "INFO: ";
                        break;
                    case WARNING_MESSAGE:
                        header = "WARNING: ";
                        break;
                    case QUESTION_MESSAGE:
                        header = "QUESTION: ";
                        break;
                     default:
                         header = "INFO: ";
                        break;
                }

                if (CONSOLE_OUTPUT_PROPERTY && !LOG_FILE_OUTPUT_PROPERTY) {
                    System.out.println(header + message);

                } else if (LOG_FILE_OUTPUT_PROPERTY && !CONSOLE_OUTPUT_PROPERTY) {
                    getLogFile().println(header + message);

                } else if (CONSOLE_OUTPUT_PROPERTY && LOG_FILE_OUTPUT_PROPERTY) {
                    System.out.println(header + message);
                    getLogFile().println(header + message);
                }
            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(OutputMonitor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     * @param message
     * @param e
     */
    public static void printStream(String message, Exception e) {
        try {

            if (ACTIVE_MONITOR_PROPERTY) {

                if (CONSOLE_OUTPUT_PROPERTY && !LOG_FILE_OUTPUT_PROPERTY) {
                    System.err.println("Error: " + message);
                 
                } else if (LOG_FILE_OUTPUT_PROPERTY && !CONSOLE_OUTPUT_PROPERTY) {
                    getLogFile().println("Error: " + message);
                    e.printStackTrace(getLogFile());

                } else if (CONSOLE_OUTPUT_PROPERTY && LOG_FILE_OUTPUT_PROPERTY) {
                    System.err.println("Error: " + message);
                    Logger.getLogger(OutputMonitor.class.getName()).log(Level.SEVERE, null, e);
                    getLogFile().println("Error: " + message);
                    e.printStackTrace(getLogFile());

                }
            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(OutputMonitor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    private static PrintWriter getLogFile() throws FileNotFoundException {

        if (logFileWriter == null) {
            logFileWriter = getPrintWriter();
        }

        return logFileWriter;
    }

    /**
     *
     * @param path
     */
    public static void setLogFilePath(String path) {
        OutputMonitor.logFilePath = path;
        logFile = new File(path);
        logFileWriter = null;

    }

    private static PrintWriter getPrintWriter() throws FileNotFoundException {

        if (logFile.exists()) {
            streamLogFile = new FileOutputStream(logFile, true);
        } else {

            File f = new File(logFile.getParent());
            if (!f.exists()) {
                f.mkdir();
            }
            streamLogFile = new FileOutputStream(logFile);
        }

        return new PrintWriter(streamLogFile, true);

    }

}
