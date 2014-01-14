/*
 * DrakkarKeel - An Enterprise Collaborative Search Platform
 *
 * The contents of this file are subject under the terms described in the
 * DRAKKARKEEL_LICENSE file included in this distribution; you may not use this
 * file except in compliance with the License. 
 *
 * 2013-2014 DrakkarKeel Platform.
 */
package drakkar.oar.security;

import drakkar.oar.util.OutputMonitor;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *  Clase que contiene métodos de encriptación
 */
public class DrakkarSecurity {

    /**
     * Convierte el array de enteros en un array de hexadecimales.
     *
     * @param array array de enteros
     *
     * @return array de haxagesimales
     */
    private static String hex(byte[] array) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < array.length; ++i) {
            sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
        }
        return sb.toString();
    }

    /** Devuelve el hash SHA-1 correspondiente al mensaje especificado.
     *
     * @param message         mensaje
     * @return                hash MD5 correspondiente al mensaje
     */
    public static String SHA(String message) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            return hex(md.digest(message.getBytes("CP1252")));
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
        }
        return null;
    }

    /**
     * Encripta una cadena de caracteres
     *
     * @param password          contraseña
     *
     * @return
     */
    public static String encryptPassword(String password) {
        StringBuilder result = new StringBuilder();
        double constant = getKey();
        String separator = "c";
        byte[] array = password.getBytes();

        for (int i = 0; i < array.length; i++) {
            byte b = array[i];
            result.append(Double.valueOf(b) + constant);
            result.append(separator);
        }

        return result.toString();
    }

    /**
     * Desencripta un texto encriptado por el método anterior
     *
     * @param encryptPassword        contraseña encriptada
     *
     * @return
     * 
     */
    public static String decryptPassword(String encryptPassword) {

        StringBuilder result = new StringBuilder();
        double constant = getKey();
        String obj;
        byte[] array = encryptPassword.getBytes();

        obj = new String(array);
        if (obj.contains("c") && obj.startsWith("677")) {
            String[] all = obj.split("c");
            for (int i = 0; i < all.length; i++) {
                String string = all[i];
                double value = Double.valueOf(string);
                double resultValue = value - constant;

                Character a = (char) resultValue;
                result.append(a.toString());
            }
        }else{
            OutputMonitor.printLine("Decrypting password", OutputMonitor.ERROR_MESSAGE);
        }

        return result.toString();

    }

    /**
     * Key to identify the true number
     *
     * CIR in ASCII code
     */
    private static int getKey() {
        StringBuilder result = new StringBuilder();
        String word = "CIR";

        byte[] array = word.getBytes();

        for (int i = 0; i < array.length; i++) {
            byte b = array[i];
            result.append(Integer.valueOf(b));

        }
        return Integer.valueOf(result.toString());
    }
//    public static void main(String[] args) {
//        String pass = DrakkarSecurity.encryptPassword("hello 876hg*");
//        DrakkarSecurity.decryptPassword(pass);
//
//    }
}
