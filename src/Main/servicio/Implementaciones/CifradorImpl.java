package Main.servicio.Implementaciones;

import Main.servicio.Interfaces.Cifrador;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

/**
 * Implementación del servicio de cifrado utilizando el algoritmo simétrico AES
 * (Advanced Encryption Standard).
 * <p>
 * Esta clase proporciona los mecanismos para proteger información sensible como
 * contraseñas y
 * correos electrónicos antes de su almacenamiento o comparación. Utiliza una
 * clave de 128 bits
 * y codificación Base64 para garantizar la compatibilidad del texto cifrado con
 * formatos de archivo estándar.
 * </p>
 * * @author Jose Berroteran
 * 
 * @version 1.1
 * @since 11/11/2025
 */
public class CifradorImpl implements Cifrador {

    /**
     * * Clave secreta fija de 16 bytes (128 bits) para el cifrado AES.
     * <p>
     * <b>Nota de seguridad:</b> En un entorno de producción, esta clave debería ser
     * gestionada mediante variables de entorno o un KeyStore seguro.
     * </p>
     */
    private static final String CLAVE_CIFRADO = "MiClaveSecreta12";

    /**
     * Constructor por defecto de la implementación del cifrador.
     */
    public CifradorImpl() {
    }

    /**
     * Cifra una contraseña en texto plano.
     * * @param contrasenia La cadena de texto a proteger.
     * 
     * @return El texto cifrado en formato Base64, o {@code null} si ocurre un error
     *         criptográfico.
     */
    @Override
    public String cifrarContrasenia(String contrasenia) {
        try {
            SecretKeySpec clave = new SecretKeySpec(CLAVE_CIFRADO.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, clave);
            byte[] contraseniaCifrada = cipher.doFinal(contrasenia.getBytes());
            return Base64.getUrlEncoder().encodeToString(contraseniaCifrada);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Descifra una contraseña previamente cifrada.
     * * @param contraseniaCifrada La contraseña en formato Base64 cifrado.
     * 
     * @return El texto original (plano) o {@code null} si la clave es incorrecta o
     *         el dato está corrupto.
     */
    @Override
    public String descifrarContrasenia(String contraseniaCifrada) {
        try {
            SecretKeySpec clave = new SecretKeySpec(CLAVE_CIFRADO.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, clave);
            byte[] contraseniaBytes = Base64.getUrlDecoder().decode(contraseniaCifrada);
            byte[] contraseniaDescifrada = cipher.doFinal(contraseniaBytes);
            return new String(contraseniaDescifrada);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Cifra una dirección de correo electrónico para proteger la privacidad del
     * usuario.
     * * @param email El email en texto claro.
     * 
     * @return El email cifrado y codificado en Base64.
     */
    @Override
    public String cifrarEmail(String email) {
        try {
            SecretKeySpec clave = new SecretKeySpec(CLAVE_CIFRADO.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, clave);
            byte[] emailCifrado = cipher.doFinal(email.getBytes());
            return Base64.getUrlEncoder().encodeToString(emailCifrado);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Descifra un correo electrónico cifrado para su uso en lógica de negocio o
     * visualización.
     * * @param emailCifrado El email en formato cifrado.
     * 
     * @return El email original en texto claro, o el parámetro original si no se
     *         puede procesar.
     */
    @Override
    public String descifrarEmail(String emailCifrado) {
        if (emailCifrado == null)
            return null;
        try {
            SecretKeySpec clave = new SecretKeySpec(CLAVE_CIFRADO.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, clave);
            byte[] bytesCifrados = Base64.getUrlDecoder().decode(emailCifrado);
            byte[] bytesDescifrados = cipher.doFinal(bytesCifrados);
            return new String(bytesDescifrados);
        } catch (Exception e) {
            return emailCifrado;
        }
    }
}