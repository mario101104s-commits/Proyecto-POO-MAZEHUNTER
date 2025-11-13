package Main.servicio.Implementaciones;

import Main.modelo.Dominio.Usuario;
import Main.servicio.Interfaces.Persistencia;
import Main.servicio.Interfaces.ServicioUsuario;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ServicioUsuarioImpl implements ServicioUsuario {
    private Persistencia persistencia;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public ServicioUsuarioImpl() {
        this.persistencia = new PersistenciaJASON();
    }

    public ServicioUsuarioImpl(Persistencia persistencia) {
        this.persistencia = persistencia;
    }

    @Override
    public boolean registrarUsuario(String email, String contraseniaCifrada) {
        if (persistencia.existeUsuario(email)) {
            return false;
        }
        String fechaRegistro = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        Usuario nuevoUsuario = new Usuario(email, contraseniaCifrada, fechaRegistro);

        return persistencia.guardarUsuario(nuevoUsuario);
    }

    @Override
    public boolean autenticarUsuario(String email, String contraseniaCifrada) {
         Usuario usuario = persistencia.cargarUsuario(email);
         return usuario != null && usuario.getContraseniaCifrada().equals(contraseniaCifrada);
    }

    @Override
    public String obtenerFechaRegistro(String email) {
        Usuario usuario = persistencia.cargarUsuario(email);
        return (usuario != null) ? usuario.getFechaRegistro() : null;
    }

        /**
         * Obtiene la fecha de registro formateada legible
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
         * Verifica si un usuario existe
         */
    @Override
    public boolean existeUsuario(String email) {
        return persistencia.existeUsuario(email);
    }

    /**
    * Obtiene todos los usuarios registrados
    */

    @Override
    public List<Usuario> obtenerTodosUsuarios() {
        return persistencia.cargarTodosUsuarios();
    }

    /**
    * Obtiene un usuario específico por email
    \*/
    @Override
    public Usuario obtenerUsuario(String email) {
        return persistencia.cargarUsuario(email);
    }

    /**
     * Obtiene información completa del usuario formateada
     * */
    @Override
    public String obtenerInformacionUsuario(String email) {
        Usuario usuario = persistencia.cargarUsuario(email);
        if (usuario == null) {
            return "Usuario no encontrado";
        }

        return String.format(
                "📧 Email: %s\n📅 Registrado: %s",
                usuario.getEmail(),
                obtenerFechaRegistroFormateada(email)
        );
    }

    /**
     * Obtiene estadísticas de usuarios
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


}