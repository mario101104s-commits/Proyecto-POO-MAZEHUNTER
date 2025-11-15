package Main.servicio.Implementaciones;

import Main.modelo.Dominio.Usuario;
import Main.servicio.Interfaces.Cifrador;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
/**
 * Implementación concreta del servicio de cifrado utilizando el algoritmo AES.
 * <p>
 * Esta clase maneja el cifrado y descifrado de contraseñas, además de interactuar
 * con la capa de persistencia para cargar la contraseña cifrada de un usuario.
 * </p>
 * @author Jose Berroteran y Mario Sanchez
 * @version 1.0
 * @since 11/11/25
 */

public class CifradorImpl implements Cifrador {
    /**
     * Instancia de la clase de persistencia para acceder a los datos de usuario.
     */
    PersistenciaJASON persistencia = new PersistenciaJASON();
    /**
     * Constructor por defecto.
     */
    public CifradorImpl() {
    }
    /**
     * La clave secreta fija utilizada para el cifrado AES.
     * * NOTA: Esta clave debe ser de 16 bytes (128 bits) para AES-128.
     */
    private static final String CLAVE_CIFRADO = "MiClaveSecreta12";
    /**
     * Cifra una cadena de texto plano (contraseña) utilizando el algoritmo AES.
     *
     * @param contrasenia La contraseña original en texto legible.
     * @return La contraseña cifrada y codificada en Base64, o {@code null} si el cifrado falla.
     */
    @Override
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
    /**
     * Descifra una cadena de texto cifrado (contraseña) codificada en Base64.
     *
     * @param contraseniaCifrada La contraseña almacenada y cifrada.
     * @return La contraseña original en texto plano, o {@code null} si el descifrado falla.
     */
    @Override
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
    /**
     * Busca y obtiene la contraseña cifrada de un usuario por su correo electrónico.
     * <p>
     * Este metodo utiliza la capa de persistencia para cargar los datos del usuario.
     * </p>
     * @param email El correo electrónico del usuario a buscar.
     * @return La contraseña cifrada del usuario si se encuentra, o {@code null} si el usuario no existe.
     */
    @Override
    public String recuperarContraseniaCifrada(String email) {
        Usuario usuario = persistencia.cargarUsuario(email);
        return (usuario != null) ? usuario.getContraseniaCifrada() : null;
    }
}
