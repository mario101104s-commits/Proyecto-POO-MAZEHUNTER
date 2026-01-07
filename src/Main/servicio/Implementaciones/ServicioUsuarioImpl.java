package Main.servicio.Implementaciones;

import Main.modelo.Dominio.Usuario;
import Main.servicio.Interfaces.Persistencia;
import Main.servicio.Interfaces.ServicioUsuario;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Implementación concreta del servicio de gestión de usuarios
 * ({@code ServicioUsuario}).
 * <p>
 * Maneja la lógica de negocio para el registro, la autenticación de usuarios, y
 * la
 * obtención de información de la cuenta, interactuando con la capa de
 * persistencia.
 * </p>
 *
 * @author Jose Berroteran
 * @version 1.0
 * @since 11/11/2025
 */

public class ServicioUsuarioImpl implements ServicioUsuario {
    /** Referencia a la capa de persistencia para acceso a datos. */
    private Persistencia persistencia;
    private CifradorImpl cifrador = new CifradorImpl();
    /**
     * Formateador estándar para mostrar fechas y horas de registro de manera
     * legible.
     */
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    /**
     * Constructor por defecto. Inicializa la persistencia utilizando
     * {@code PersistenciaJASON}.
     * * Esto acopla la implementación directamente a un tipo de persistencia.
     */
    public ServicioUsuarioImpl() {
        this.persistencia = new PersistenciaJASON();
    }

    /**
     * Constructor utilizado para inyección de dependencias (para pruebas o
     * configuración modular).
     *
     * @param persistencia La implementación de la interfaz {@code Persistencia} a
     *                     utilizar.
     */
    public ServicioUsuarioImpl(Persistencia persistencia) {
        this.persistencia = persistencia;
    }

    /**
     * Intenta registrar un nuevo usuario en el sistema.
     * <p>
     * Si el email ya existe, el registro falla. En caso contrario, crea el objeto
     * {@code Usuario} con la fecha actual y lo guarda en la persistencia.
     * </p>
     *
     * @param email              El correo electrónico del nuevo usuario.
     * @param contraseniaCifrada La contraseña ya cifrada.
     * @return {@code true} si el usuario fue registrado exitosamente, {@code false}
     *         si ya existía o falló el guardado.
     */
    @Override
    public boolean registrarUsuario(String email, String contraseniaCifrada) {
        String emailCifrado = cifrador.cifrarEmail(email);
        if (persistencia.existeUsuario(emailCifrado)) {
            return false;
        }
        String fechaRegistro = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        Usuario nuevoUsuario = new Usuario(emailCifrado, contraseniaCifrada, fechaRegistro);

        return persistencia.guardarUsuario(nuevoUsuario);
    }

    /**
     * Verifica si las credenciales de inicio de sesión son correctas.
     * <p>
     * Carga el usuario por email y compara la contraseña cifrada proporcionada
     * con la almacenada.
     * </p>
     *
     * @param email              El correo electrónico del usuario.
     * @param contraseniaCifrada La contraseña ingresada por el usuario (ya
     *                           cifrada).
     * @return {@code true} si el usuario existe y las contraseñas coinciden,
     *         {@code false} en caso contrario.
     */
    @Override
    public boolean autenticarUsuario(String email, String contraseniaCifrada) {

        String emailProtegido = cifrador.cifrarEmail(email);

        Usuario usuario = persistencia.cargarUsuario(emailProtegido);

        return usuario != null && usuario.getContraseniaCifrada().equals(contraseniaCifrada);
    }

    /**
     * Obtiene la fecha y hora de registro de un usuario en formato
     * {@code ISO_LOCAL_DATE_TIME}.
     *
     * @param email El correo electrónico del usuario.
     * @return La fecha de registro como String, o {@code null} si el usuario no
     *         existe.
     */
    @Override
    public String obtenerFechaRegistro(String email) {
        Usuario usuario = persistencia.cargarUsuario(email);
        return (usuario != null) ? usuario.getFechaRegistro() : null;
    }

    /**
     * Obtiene la fecha de registro formateada de manera legible.
     *
     * @param email El correo electrónico del usuario.
     * @return La fecha de registro formateada (dd/MM/yyyy HH:mm), o "No disponible"
     *         si el usuario no existe.
     */
    @Override
    public String obtenerFechaRegistroFormateada(String email) {
        Usuario usuario = persistencia.cargarUsuario(email);
        if (usuario == null) {
            return "No disponible";
        }

        try {
            LocalDateTime fecha = LocalDateTime.parse(usuario.getFechaRegistro());
            return fecha.format(formatter);
        } catch (Exception e) {
            return usuario.getFechaRegistro(); // Retorna el string original si hay error
        }
    }

    /**
     * Verifica la existencia de un usuario por su correo electrónico.
     *
     * @param email El correo electrónico a verificar.
     * @return {@code true} si el usuario existe, {@code false} en caso contrario.
     */
    @Override
    public boolean existeUsuario(String email) {
        return persistencia.existeUsuario(cifrador.cifrarEmail(email));
    }

    /**
     * Obtiene la lista completa de todos los usuarios registrados en el sistema.
     *
     * @return Una {@code List} de todos los objetos {@code Usuario}.
     */
    @Override
    public List<Usuario> obtenerTodosUsuarios() {
        return persistencia.cargarTodosUsuarios();
    }

    /**
     * Obtiene un usuario específico utilizando su correo electrónico.
     *
     * @param email El correo electrónico del usuario.
     * @return El objeto {@code Usuario} si se encuentra, o {@code null} si no
     *         existe.
     */
    @Override
    public Usuario obtenerUsuario(String email) {
        return persistencia.cargarUsuario(cifrador.cifrarEmail(email));
    }

    /**
     * Obtiene información básica completa del usuario formateada en una cadena de
     * texto.
     *
     * @param email El correo electrónico del usuario.
     * @return Una cadena con el email y la fecha de registro formateada, o un
     *         mensaje si el usuario no es encontrado.
     */
    @Override
    public String obtenerInformacionUsuario(String email) {
        Usuario usuario = persistencia.cargarUsuario(email);
        if (usuario == null) {
            return "Usuario no encontrado";
        }

        return String.format(
                "📧 Email: %s\n📅 Registrado: %s",
                usuario.getEmail(),
                obtenerFechaRegistroFormateada(email));
    }

    /**
     * Genera un resumen estadístico de los usuarios, incluyendo el total y los
     * últimos 5 registrados.
     *
     * @return Una cadena con las estadísticas formateadas.
     */
    @Override
    public String obtenerEstadisticasUsuarios() {
        List<Usuario> usuarios = obtenerTodosUsuarios();

        if (usuarios.isEmpty()) {
            return "No hay usuarios registrados";
        }

        // Ordenar por fecha de registro (más reciente primero)
        usuarios.sort((u1, u2) -> u2.getFechaRegistro().compareTo(u1.getFechaRegistro()));

        StringBuilder stats = new StringBuilder();
        stats.append("=== ESTADÍSTICAS DE USUARIOS ===\n");
        stats.append(String.format("Total de usuarios: %d\n\n", usuarios.size()));

        stats.append("Últimos usuarios registrados:\n");
        int count = Math.min(usuarios.size(), 5);
        for (int i = 0; i < count; i++) {
            Usuario usuario = usuarios.get(i);
            stats.append(String.format("%d. %s - %s\n",
                    i + 1,
                    usuario.getEmail(),
                    obtenerFechaRegistroFormateada(usuario.getEmail())));
        }

        return stats.toString();
    }

    /**
     * Actualiza los datos de un usuario en el almacenamiento persistente.
     * * @param usuario Objeto con la información actualizada.
     * 
     * @throws Exception Si el usuario no existe o hay un error en la base de
     *                   datos/archivo.
     */
    @Override
    public void actualizarUsuario(Usuario usuario) throws Exception {
        persistencia.actualizarUsuario(usuario);
    }

}
