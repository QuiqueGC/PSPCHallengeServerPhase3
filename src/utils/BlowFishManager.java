package utils;

import data_classes.User;
import org.mindrot.jbcrypt.BCrypt;

/**
 * Contiene los métodos referentes al tratado de contraseñas
 */
public class BlowFishManager {


    /**
     * Encripta el password introducido por el usuario
     *
     * @param passwd el string introducido por el usuario
     * @return el password encriptado
     */
    public static String encryptingPasswd(String passwd) {
        String encryptedPasswd;

        encryptedPasswd = BCrypt.hashpw(passwd, BCrypt.gensalt(10));

        return encryptedPasswd;
    }

    /**
     * Chequea si el usuario ha introducido la contraseña correcta
     *
     * @param passwd el string con el password introducido
     * @param user   el usuario que estamos chequeando para verificar contraseña
     * @return boolean true/false dependiendo de si es correcto o no el chequeo
     */
    public static boolean checkingPasswd(String passwd, User user) {
        boolean rightPasswd = false;

        if (BCrypt.checkpw(passwd, user.getPasswd())) {

            rightPasswd = true;
        }

        return rightPasswd;
    }
}
