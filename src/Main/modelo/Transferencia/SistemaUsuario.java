package Main.modelo.Transferencia;

import Main.modelo.Dominio.Usuario;
import Main.servicio.Implementaciones.PersistenciaJASON;
import Main.servicio.Implementaciones.ServicioUsuarioImpl;
import Main.servicio.Interfaces.Cifrador;
import Main.servicio.Interfaces.ServicioUsuario;
import Main.ui.util.ValidadorContrasenia;
import Main.ui.util.ValidadorEmail;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.*;
import java.util.Base64;

/**
 * Define la capa de presentación que maneja la interacción con el usuario para
 * el registro, autenticación y gestión de cuentas.
 * <p>
 * Actúa como un punto de control que utiliza los servicios de dominio ({@link ServicioUsuario})
 * y las utilidades de validación para interactuar con el usuario a través de la consola.
 * Esta clase también implementa los métodos de cifrado para manejar directamente
 * la lógica de seguridad durante el registro e inicio de sesión.
 * </p>
 *
 * @author Jose Berroteran
 * @version 1.0
 * @since 11/11/2025
 */
public class SistemaUsuario implements Cifrador {
    /** Referencia al servicio de lógica de negocio para la gestión de usuarios. */
    private ServicioUsuario servicioUsuario;
    /** Scanner para capturar la entrada de datos del usuario desde la consola. */
    private Scanner scanner;
    /** Utilidad para validar y solicitar el correo electrónico al usuario. */
    private ValidadorEmail validadorEmail = new ValidadorEmail();
    /** Utilidad para validar, solicitar y gestionar la recuperación de la contraseña. */
    private ValidadorContrasenia validadorContrasenia = new ValidadorContrasenia();
    /** Instancia de la persistencia para acceder directamente a la carga de usuarios. */
    private PersistenciaJASON persistencia = new PersistenciaJASON();

    /** La clave secreta fija utilizada para el cifrado AES. Debe tener 16 caracteres para AES-128. */
    private static final String CLAVE_CIFRADO = "MiClaveSecreta12";

    // --- MÉTODOS DE LA INTERFAZ CIFRADOR ---

    /**
     * Cifra una cadena de texto plano (contraseña) utilizando el algoritmo AES.
     * <p>
     * Utiliza la clave definida en {@code CLAVE_CIFRADO} y devuelve un resultado
     * en formato Base64 para facilitar su almacenamiento en archivos de texto.
     * </p>
     *
     * @param contrasenia La contraseña original en texto legible.
     * @return La contraseña cifrada y codificada en Base64, o {@code null} si ocurre un error.
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
     * @return La contraseña original en texto plano, o {@code null} si el proceso falla.
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
     * Recupera la contraseña cifrada almacenada para un usuario específico.
     *
     * @param email El correo electrónico (ID) del usuario.
     * @return La contraseña cifrada almacenada o {@code null} si no existe.
     */

    public String recuperarContraseniaCifrada(String email) {
        Usuario usuario = persistencia.cargarUsuario(email);
        return (usuario != null) ? usuario.getContraseniaCifrada() : null;
    }

    /**
     * Constructor. Inicializa el servicio de usuario y el escáner de consola.
     */
    public SistemaUsuario() {
        this.servicioUsuario = new ServicioUsuarioImpl();
        this.scanner = new Scanner(System.in);
    }

    // --- MÉTODOS DE LÓGICA DE USUARIO ---

    /**
     * Guía al usuario a través del proceso de registro.
     * <p>
     * El proceso incluye: validación de formato de email, cifrado del mismo para privacidad,
     * validación de fortaleza de contraseña, cifrado AES y persistencia final.
     * </p>
     *
     * @return {@code true} si el registro fue exitoso, {@code false} si el correo ya existe o hubo un error.
     */
    public boolean registrarUsuario() {
        System.out.println("\n=== REGISTRO DE USUARIO ===");

        String emailNormal = validadorEmail.solicitarEmail();
        if (emailNormal == null) return false;

        String emailCifrado = cifrarEmail(emailNormal);

        if (servicioUsuario.existeUsuario(emailCifrado)) {
            System.out.println("❌ Este correo ya está registrado.");
            return false;
        }

        String contraseniaNormal = validadorContrasenia.solicitarContrasenia();
        String contraseniaCifrada = cifrarContrasenia(contraseniaNormal);

        boolean registrado = servicioUsuario.registrarUsuario(emailCifrado, contraseniaCifrada);

        if (registrado) {
            System.out.println("✅ ¡Usuario registrado exitosamente!");
            return true;
        }
        return false;
    }

    /**
     * Gestiona el inicio de sesión comparando las credenciales ingresadas con las almacenadas.
     * <p>
     * Se cifran los datos ingresados antes de enviarlos al servicio para asegurar
     * que la comparación se realice sobre los valores protegidos.
     * </p>
     *
     * @return {@code true} si el inicio de sesión es válido, {@code false} en caso contrario.
     */
    public boolean autenticarUsuario() {
        System.out.println("\n=== INICIAR SESIÓN ===");

        System.out.print("Email: ");
        String emailIngresado = scanner.nextLine().trim();
        String emailCifrado = cifrarEmail(emailIngresado);

        System.out.print("Contraseña: ");
        String contrasenia = scanner.nextLine();
        String contraseniaCifrada = cifrarContrasenia(contrasenia);

        boolean autenticado = servicioUsuario.autenticarUsuario(emailCifrado, contraseniaCifrada);

        if (autenticado) {
            System.out.println("✅ ¡Autenticación exitosa!");
            System.out.println("📅 Te registraste el: " + servicioUsuario.obtenerFechaRegistroFormateada(emailCifrado));
            return true;
        } else {
            System.out.println("❌ Credenciales incorrectas.");
            return false;
        }
    }

    /**
     * Aplica el algoritmo de cifrado AES al correo electrónico.
     * * @param email Email en texto plano.
     * @return Email cifrado en Base64.
     */
    public String cifrarEmail(String email) {
        try {
            SecretKeySpec clave = new SecretKeySpec(CLAVE_CIFRADO.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, clave);
            byte[] emailCifrado = cipher.doFinal(email.getBytes());
            return Base64.getEncoder().encodeToString(emailCifrado);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Deshace el cifrado AES de un correo electrónico.
     * * @param emailCifrado Email en formato Base64 cifrado.
     * @return Email original en texto plano.
     */
    public String descifrarEmail(String emailCifrado) {
        if (emailCifrado == null) return null;
        try {
            SecretKeySpec clave = new SecretKeySpec(CLAVE_CIFRADO.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, clave);
            byte[] bytesCifrados = Base64.getDecoder().decode(emailCifrado);
            byte[] bytesDescifrados = cipher.doFinal(bytesCifrados);
            return new String(bytesDescifrados);
        } catch (Exception e) {
            return emailCifrado;
        }
    }

    /**
     * Muestra en consola el listado estadístico de los usuarios del sistema.
     */
    public void mostrarEstadisticasUsuarios() {
        System.out.println("\n" + servicioUsuario.obtenerEstadisticasUsuarios());
    }
}