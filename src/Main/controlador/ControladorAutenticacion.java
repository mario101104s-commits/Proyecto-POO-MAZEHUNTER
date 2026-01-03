package Main.controlador;

import Main.modelo.Dominio.Usuario;
import Main.servicio.Interfaces.Cifrador;
import Main.servicio.Interfaces.ServicioUsuario;

/**
 * Controlador encargado de gestionar los procesos de seguridad y acceso de usuarios.
 * <p>
 * Actúa como intermediario entre la interfaz de usuario (Vista) y los servicios de negocio.
 * Maneja la lógica de inicio de sesión, registro de nuevos usuarios, recuperación
 * de credenciales y validaciones de formato de datos.
 * </p>
 *
 * @author Mario Sanchez
 * @version 1.1
 * @since 22/12/25
 */
public class ControladorAutenticacion {

    /**
     *  Servicio para la gestión de datos y persistencia de usuarios.
     */
    private ServicioUsuario servicioUsuario;

    /**
     *  Componente encargado de las operaciones de cifrado y descifrado.
     */
    private Cifrador cifrador;

    /**
     * Constructor que inicializa el controlador con sus dependencias necesarias.
     *
     * @param servicioUsuario Implementación del servicio de usuarios.
     * @param cifrador Implementación del componente de cifrado.
     */
    public ControladorAutenticacion(ServicioUsuario servicioUsuario, Cifrador cifrador) {
        this.servicioUsuario = servicioUsuario;
        this.cifrador = cifrador;
    }

    /**
     * Valida las credenciales de acceso de un usuario.
     * <p>
     * El proceso incluye la obtención del usuario, el descifrado de la contraseña almacenada
     * y la comparación con la contraseña proporcionada en texto plano.
     * </p>
     *
     * @param email Correo electrónico del usuario que intenta acceder.
     * @param contrasenia Contraseña en texto plano ingresada en la vista.
     * @return El correo electrónico si la autenticación es exitosa; {@code null} en caso de
     * usuario inexistente, error de descifrado o contraseña incorrecta.
     */
    public String iniciarSesion(String email, String contrasenia) {
        Usuario usuario = servicioUsuario.obtenerUsuario(email);

        if (usuario == null) {
            return null;
        }

        String contraseniaDescifrada = cifrador.descifrarContrasenia(usuario.getContraseniaCifrada());

        if (contraseniaDescifrada == null) {
            return null;
        }

        if (contraseniaDescifrada.equals(contrasenia)) {
            return email;
        }

        return null;
    }

    /**
     * Recupera un objeto de usuario basado en su identificador único (email).
     *
     * @param email El correo electrónico del usuario a buscar.
     * @return El objeto {@link Usuario} correspondiente o {@code null} si no se encuentra.
     */
    public Usuario obtenerUsuario(String email) {
        return servicioUsuario.obtenerUsuario(email);
    }

    /**
     * Registra un nuevo usuario en el sistema previo cifrado de sus datos sensibles.
     * <p>
     * Incluye validaciones de robustez para la contraseña:
     * - Mínimo 6 caracteres.
     * - Al menos una letra mayúscula.
     * - Al menos un carácter especial (@#$%^&+=!).
     * </p>
     *
     * @param email Correo electrónico para la nueva cuenta.
     * @param contrasenia Contraseña en texto plano que será validada y cifrada.
     * @return {@code true} si el registro fue exitoso; {@code false} si el usuario ya existe
     * o la contraseña no cumple los requisitos.
     */
    public boolean registrarUsuario(String email, String contrasenia) {
        // 1. Verificar si el usuario ya existe
        if (servicioUsuario.existeUsuario(email)) {
            return false;
        }

        String regex = "^(?=.*[A-Z])(?=.*[@#$%^&+=!]).{6,}$";

        if (contrasenia == null || !contrasenia.matches(regex)) {
            System.out.println("Seguridad: La contraseña no cumple con los requisitos mínimos.");
            return false;
        }

        // 3. Proceder con el cifrado y registro si pasa las validaciones
        String contraseniaCifrada = cifrador.cifrarContrasenia(contrasenia);
        return servicioUsuario.registrarUsuario(email, contraseniaCifrada);
    }
    /**
     * Actualiza la contraseña de un usuario existente en el sistema.
     *
     * @param email Correo del usuario a actualizar.
     * @param nuevaContrasenia Nueva contraseña en texto plano.
     * @return {@code true} si la actualización fue exitosa; {@code false} si hubo un error
     * en la persistencia o el usuario no existe.
     */
    public boolean recuperarContrasenia(String email, String nuevaContrasenia) {
        Usuario usuario = servicioUsuario.obtenerUsuario(email);

        if (usuario == null) {
            return false;
        }

        String contraseniaCifrada = cifrador.cifrarContrasenia(nuevaContrasenia);
        usuario.setContraseniaCifrada(contraseniaCifrada);

        try {
            servicioUsuario.actualizarUsuario(usuario);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Evalúa si una contraseña cumple con las políticas de seguridad mínimas.
     * <p>
     * Requisitos: Mínimo 8 caracteres, al menos una letra mayúscula y al menos un
     * carácter especial.
     * </p>
     *
     * @param password La cadena de texto de la contraseña a validar.
     * @return {@code true} si cumple los requisitos; {@code false} en caso contrario.
     */
    public boolean validarContrasenia(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }

        boolean tieneMayuscula = password.matches(".*[A-Z].*");
        boolean tieneEspecial = password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*");

        return tieneMayuscula && tieneEspecial;
    }

    /**
     * Verifica que el formato del correo electrónico sea válido mediante expresiones regulares.
     *
     * @param email La cadena de texto del correo a validar.
     * @return {@code true} si el formato es correcto; {@code false} si es nulo o inválido.
     */
    public boolean validarEmail(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    /**
     * Consulta la existencia de un usuario en el sistema.
     *
     * @param email Correo electrónico a verificar.
     * @return {@code true} si el email ya está registrado; {@code false} si está disponible.
     */
    public boolean existeUsuario(String email) {
        return servicioUsuario.existeUsuario(email);
    }
}