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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Calendar;

/**
 * Esta clase contiene métodos de utilidades
 */
public class Utilities {

    /**
     * Devuelve la fecha del sistema
     *
     * @return fecha
     */
    public static String getDate() {
        Calendar calendar = Calendar.getInstance();
        String date = null;
        date = "[" + calendar.get(Calendar.YEAR) + "/"
                + calendar.get(Calendar.MONTH) + "/"
                + calendar.get(Calendar.DATE) + " ]";
        return date;
    }

    /**
     * Devuelve la fecha de sistema con delimitador personalizado
     *
     * @param delim delimitador
     *
     * @return fecha
     */
    public static String getDate(char delim) {
        Calendar calendar = Calendar.getInstance();
        String date = null;
        date = "[" + calendar.get(Calendar.YEAR) + delim
                + calendar.get(Calendar.MONTH) + delim
                + calendar.get(Calendar.DATE) + " ]";
        return date;
    }

    /**
     * Devuelve la fecha y hora del sistema.
     *
     * @return feha y hora 
     */
    public static String getDateTime() {
        Calendar calendar = Calendar.getInstance();
        String date = null;
        date = "[" + calendar.get(Calendar.YEAR) + "/"
                + calendar.get(Calendar.MONTH) + "/"
                + calendar.get(Calendar.DATE) + " " + calendar.get(Calendar.HOUR)
                + ":" + calendar.get(Calendar.MINUTE) + ":"
                + calendar.get(Calendar.SECOND) + ":"
                + calendar.get(Calendar.MILLISECOND) + "]";
        return date;
    }

    /**
     * Devuelve la fecha y hora del sistema.
     *
     * @return feha y hora
     */
    public static String getTime() {
        Calendar calendar = Calendar.getInstance();
        String date = null;
        date = "[" + calendar.get(Calendar.HOUR)
                + ":" + calendar.get(Calendar.MINUTE) + ":"
                + calendar.get(Calendar.SECOND) + " "
                + (calendar.get(Calendar.AM_PM) == 1 ? "PM" : "AM")
                + "]";
        return date;
    }

    /**
     * Devuelve la fecha y hora del sistema con delimitador determinado.
     *
     * @param delim delimitador
     *
     * @return fecha y hora 
     */
    public static String getDateTime(char delim) {
        Calendar calendar = Calendar.getInstance();
        String date = null;
        date = "[" + calendar.get(Calendar.YEAR) + delim
                + calendar.get(Calendar.MONTH) + delim
                + calendar.get(Calendar.DATE) + " " + calendar.get(Calendar.HOUR)
                + ":" + calendar.get(Calendar.MINUTE) + ":"
                + calendar.get(Calendar.SECOND) + ":"
                + calendar.get(Calendar.MILLISECOND) + "]";
        return date;
    }

    /**
     * Devuelve un número de puerto disponible en el sistema
     *
     * @return puerto
     */
    public static int getAvailablePort() {
        Socket socket;
        for (int port = 10000; port <= 77777; port++) {
            try {
                socket = new Socket("localhost", port);
                socket.close();
            } catch (UnknownHostException e) {
                OutputMonitor.printStream("Searching available port.", e);
                return -1;
            } catch (IOException e) {
                OutputMonitor.printLine("The port "+port+" is available.", OutputMonitor.INFORMATION_MESSAGE);
                return port;
            }
        }

        return -1;

    }

    /**
     * Chequea que el puerto especificado se encuentre disponible en sistema
     *
     * @param port número de puerto seleccionado
     *
     * @return true si el puerto está disponible, false en caso contrario
     */
    public static boolean isAvailablePort(int port) {

        try {
            Socket socket = new Socket("localhost", port);
            socket.close();
        } catch (UnknownHostException e) {
            OutputMonitor.printStream("Searching available port.", e);
            return false;
        } catch (IOException e) {
             OutputMonitor.printLine("The port "+port+" is available.", OutputMonitor.INFORMATION_MESSAGE);
            return true;
        }

        return false;

    }

    /**
     * Elimina los archivos de un directorio
     *
     * @param dir  directorio a procesar
     */
    public static void deleteFiles(java.io.File dir) {
        String[] files = dir.list();
        for (int i = 0; i < files.length; i++) {
            java.io.File f = new java.io.File(dir, files[i]);
            if (f.exists()) {
                if (f.isFile()) {
                    OutputMonitor.printLine("Deleting: " + f + ": " + f.delete(), OutputMonitor.INFORMATION_MESSAGE);
                } else {
                    deleteFiles(f);
                }
            }
        }
    }

    /**
     * Devuelve el contenido de un fichero txt
     *
     * @param f fichero
     *
     * @return contenido del fichero
     */
    public static String readFile(File f) {
        String result = " ";
        char c;

        FileInputStream in = null;
        try {
            in = new FileInputStream(f);
        } catch (FileNotFoundException ex) {
            OutputMonitor.printStream("", ex);
        }

        int buffer;

        try {
            while ((buffer = in.read()) != -1) {
                c = (char) buffer;
                result = result.concat(String.valueOf(c));
            }
            in.close();
        } catch (IOException e) {
            OutputMonitor.printStream("Reading file "+f.getName(), e);
        }

        return result;
    }

}
