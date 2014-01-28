/*
 * DrakkarKeel - An Enterprise Collaborative Search Platform
 *
 * The contents of this file are subject under the terms described in the
 * DRAKKARKEEL_LICENSE file included in this distribution; you may not use this
 * file except in compliance with the License. 
 *
 * 2013-2014 DrakkarKeel Platform.
 */
package drakkar.oar;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *  Clase para encriptar un texto plano con el algoritmo SHA-1
 *
 * 
 * @deprecated As of DrakkarKeel version 1.1,
 * replaced by <code>DrakkarSecurity</code>.
 *
 * @see drakkar.oar.security.DrakkarSecurity
 *
 */
public class AlgorithmSha {

    /**
     * Convierte el array de enteros en un array de hexadecimales.
     *
     * @param array    array de enteros
     * @return         array de haxagesimales
     */
    public static String hex(byte[] array) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < array.length; ++i) {
            sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
        }
        return sb.toString();
    }

    /** Devuelve el hash SHA-1 correspondiente al mensaje especificado.
     *
     * @param message    texto del mensaje
     * @return           hash MD5 correspondiente al mensaje
     */
    public static String SHA(String message) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            return hex(md.digest(message.getBytes("CP1252")));
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
        }
        return null;
    }
}
