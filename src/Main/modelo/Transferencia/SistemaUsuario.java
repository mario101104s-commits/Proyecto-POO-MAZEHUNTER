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
 * Actúa como un punto de control que utiliza los servicios de dominio
 * ({@code ServicioUsuario})
 * y las utilidades de validación para interactuar con el usuario a través de la
 * consola.
 * * Esta clase también implementa los métodos de cifrado para manejar
 * directamente
 * la lógica de seguridad durante el registro e inicio de sesión.*
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
    ValidadorEmail validadorEmail = new ValidadorEmail();
    /**
     * Utilidad para validar, solicitar y gestionar la recuperación de la
     * contraseña.
     */
    ValidadorContrasenia validadorContrasenia = new ValidadorContrasenia();
    /**
     * Instancia de la persistencia para acceder directamente a la carga de usuarios
     * (usada en recuperación).
     */
    PersistenciaJASON persistencia = new PersistenciaJASON();

    // METODOS DE CIFRADOR
    /** La clave secreta fija utilizada para el cifrado AES. */
    private static final String CLAVE_CIFRADO = "MiClaveSecreta12";

    /**
     * Cifra una cadena de texto plano (contraseña) utilizando el algoritmo AES.
     * * Es la implementación directa del metodo de la interfaz {@code Cifrador}.
     *
     * @param contrasenia La contraseña original en texto legible.
     * @return La contraseña cifrada y codificada en Base64, o {@code null} si el
     *         cifrado falla.
     */
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
     * * Es la implementación directa del metodo de la interfaz {@code Cifrador}.
     *
     * @param contraseniaCifrada La contraseña almacenada y cifrada.
     * @return La contraseña original en texto plano, o {@code null} si el
     *         descifrado falla.
     */
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
     * Recupera la contraseña cifrada almacenada para un usuario específico
     * utilizando la persistencia.
     * * Es la implementación directa del metodo de la interfaz {@code Cifrador}.
     *
     * @param email El correo electrónico del usuario.
     * @return La contraseña cifrada del usuario si existe, o {@code null} si el
     *         usuario no es encontrado.
     */
    public String recuperarContraseniaCifrada(String email) {
        Usuario usuario = persistencia.cargarUsuario(email);
        return (usuario != null) ? usuario.getContraseniaCifrada() : null;
    }

    /**
     * Constructor. Inicializa el servicio de usuario, el escáner de consola
     * y la instancia del cifrador.
     */
    public SistemaUsuario() {
        this.servicioUsuario = new ServicioUsuarioImpl();
        this.scanner = new Scanner(System.in);

    }

    // METODO PRINCIPAL - Registro de usuario
    /**
     * Guía al usuario a través del proceso de registro, validando el email y la
     * contraseña,
     * cifrando la contraseña y guardando el usuario.
     *
     * @return {@code true} si el usuario fue registrado exitosamente, {@code false}
     *         en caso contrario.
     */
    public boolean registrarUsuario() {
        System.out.println("\n=== REGISTRO DE USUARIO ===");

        // 1. Validar email
        String email = validadorEmail.solicitarEmail();
        if (email == null)
            return false;

        // 2. Verificar si el email ya existe
        if (servicioUsuario.existeUsuario(email)) {
            System.out.println("❌ Este correo ya está registrado.");
            System.out.println("¿Desea recuperar su contraseña? (s/n)");
            String respuesta = scanner.nextLine();
            if (respuesta.equalsIgnoreCase("s")) {
                validadorContrasenia.recuperarContrasenia(email);
            }
            return false;
        }

        // 3. Validar contraseña
        String contrasenia = validadorContrasenia.solicitarContrasenia();
        if (contrasenia == null)
            return false;

        // 4. Cifrar contraseña
        String contraseniaCifrada = cifrarContrasenia(contrasenia);
        if (contraseniaCifrada == null) {
            System.out.println("❌ Error al cifrar la contraseña.");
            return false;
        }

        // 5. Guardar usuario usando ServicioUsuario
        boolean registrado = servicioUsuario.registrarUsuario(email, contraseniaCifrada);
        if (registrado) {
            System.out.println("✅ ¡Usuario registrado exitosamente!");
            System.out.println("📧 Email: " + email);
            System.out.println("📅 Fecha de registro: " + servicioUsuario.obtenerFechaRegistroFormateada(email));
            return true;
        } else {
            System.out.println("❌ Error al registrar el usuario.");
            return false;
        }
    }

    // METODO PARA AUTENTICAR
    /**
     * Solicita los Datos al usuario y verifica su existencia y validez de
     * contraseña.
     *
     * @return {@code true} si las credenciales son correctas y el usuario está
     *         autenticado, {@code false} en caso contrario.
     */
    public boolean autenticarUsuario() {
        System.out.println("\n=== INICIAR SESIÓN ===");

        System.out.print("Email: ");
        String email = scanner.nextLine().trim();

        System.out.print("Contraseña: ");
        String contrasenia = scanner.nextLine();

        // Cifrar la contraseña ingresada para comparar
        String contraseniaCifrada = cifrarContrasenia(contrasenia);
        if (contraseniaCifrada == null) {
            System.out.println("❌ Error al procesar la contraseña.");
            return false;
        }

        // Autenticar usando el servicio
        boolean autenticado = servicioUsuario.autenticarUsuario(email, contraseniaCifrada);
        if (autenticado) {
            System.out.println("✅ ¡Autenticación exitosa!");
            System.out.println("📅 Te registraste el: " + servicioUsuario.obtenerFechaRegistroFormateada(email));
            return true;
        } else {
            System.out.println("❌ Credenciales incorrectas.");
            return false;
        }
    }

    // METODO PARA MOSTRAR ESTADÍSTICAS
    /**
     * Imprime en la consola el resumen de las estadísticas de todos los usuarios.
     */
    public void mostrarEstadisticasUsuarios() {
        System.out.println("\n" + servicioUsuario.obtenerEstadisticasUsuarios());
    }

}