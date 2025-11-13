package Main.servicio.Implementaciones;

import Main.servicio.Interfaces.Cifrador;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class CifradorImpl implements Cifrador {

    public CifradorImpl() {
    }

    private static final String CLAVE_CIFRADO = "MiClaveSecreta12";
    public String cifrarContrasenia(String contrasenia) {
        try {
            SecretKeySpec clave = new SecretKeySpec(CLAVE_CIFRADO.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, clave);
            byte[] contraseniaCifrada = cipher.doFinal(contrasenia.getBytes());
            return Base64.getEncoder().encodeToString(contraseniaCifrada);
        } catch (Exception e) {
            return null;
        }
    }

    public String descifrarContrasenia(String contraseniaCifrada) {
        try {
            SecretKeySpec clave = new SecretKeySpec(CLAVE_CIFRADO.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, clave);
            byte[] contraseniaBytes = Base64.getDecoder().decode(contraseniaCifrada);
            byte[] contraseniaDescifrada = cipher.doFinal(contraseniaBytes);
            return new String(contraseniaDescifrada);
        } catch (Exception e) {
            return null;
        }
    }
}
