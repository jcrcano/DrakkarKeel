/*
 * DrakkarKeel - An Enterprise Collaborative Search Platform
 *
 * The contents of this file are subject under the terms described in the
 * DRAKKARKEEL_LICENSE file included in this distribution; you may not use this
 * file except in compliance with the License. 
 *
 * 2013-2014 DrakkarKeel Platform.
 */
package drakkar.stern.tracker.persistent.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.apache.derby.iapi.util.StringUtil;

/**
 * Clase para obtener la contraseña cifrada de Derby
 *
 * //Nota: version Apache Derby 10.4.2.0
 */
public class Encryption {

    // Pattern that is prefixed to the BUILTIN encrypted password
    public static final String ID_PATTERN_SHA1_SCHEME = "3b60";

    /**
     * Obtiene dada una contraseña en texto plano su encriptación
     * con el mismo algoritmo utilizado por Apache Derby para almacenar sus
     * contraseñas para la autenticación en BUILTIN.
     *
     * @param password                     user password in plain Text
     * @return                             encrypted password
     * @throws NoSuchAlgorithmException
     */
    public String getPass(String password) throws NoSuchAlgorithmException {
        String var = null;
        byte[] bytePasswd = null;
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");

        bytePasswd = StringUtil.toHexByte(password, 0, password.length());        
        messageDigest.update(bytePasswd);

        byte[] encryptVal = messageDigest.digest();
        String hexString = StringUtil.toHexString(encryptVal, 0, encryptVal.length);

        var = ID_PATTERN_SHA1_SCHEME + hexString;

        return var;
    }
 
    /** NOTES:
    User passwords are encrypted using a message digest algorithm
     * if they're stored in the database; otherwise they are not encrypted
     * if they were defined at the system level.
    - * SHA-1 digest is single hash (one way) digest and is considered very
    - * secure (160 bits).
    + * </p>
    + *
    + * <p>
    + * The passwords can be encrypted using two different schemes:
    + * </p>
    + *
    + * <ul>
    + * <li>The SHA-1 authentication scheme, which was the only available scheme
    + * in Derby 10.5 and earlier. This scheme uses the SHA-1 message digest
    + * algorithm.</li>
    + * <li>The configurable hash authentication scheme, which allows the users to
    + * specify which message digest algorithm to use.</li>
    + * </ul>
    + *
    + * <p>
    + * In order to use the configurable hash authentication scheme, the users have
    + * to set the {@code derby.authentication.builtin.algorithm} property (on
    + * system level or database level) to the name of an algorithm that's available
    + * in one of the security providers registered on the system. If this property
    + * is not set, or if it's set to NULL or an empty string, the SHA-1
    + * authentication scheme is used.
    + * </p>
     *
    + * <p>
    + * Which scheme to use is decided when a password is about to be stored in the
    + * database. One database may therefore contain passwords stored using
    + * different schemes. In order to determine which scheme to use when comparing
    + * a user's credentials with those stored in the database, the stored password
    + * is prefixed with an identifier that tells which scheme is being used.
    + * Passwords stored using the SHA-1 authentication scheme are prefixed with
    + * {@link #ID_PATTERN_SHA1_SCHEME}. Passwords that are stored using the
    + * configurable hash authentication scheme are prefixed with
    + * {@link #ID_PATTERN_CONFIGURABLE_HASH_SCHEME} and suffixed with the name of
    + * the message digest algorithm.
     */
    //http://mail-archives.apache.org/mod_mbox/db-derby-commits/201003.mbox/%3C20100312160121.B23ED23889B8@eris.apache.org%3E
}
