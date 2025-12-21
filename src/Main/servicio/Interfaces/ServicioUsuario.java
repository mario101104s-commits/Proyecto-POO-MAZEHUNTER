package Main.servicio.Interfaces;

import Main.modelo.Dominio.Usuario;

import java.util.List;

// Define el contrato de la capa de servicio para gestión de usuarios
public interface ServicioUsuario {

    public boolean registrarUsuario(String email, String contraseniaCifrada);

    public boolean autenticarUsuario(String email, String contraseniaCifrada);

    public String obtenerFechaRegistro(String email);

    public String obtenerFechaRegistroFormateada(String email);

    public boolean existeUsuario(String email);

    public List<Usuario> obtenerTodosUsuarios();

    public Usuario obtenerUsuario(String email);

    public String obtenerInformacionUsuario(String email);

    public String obtenerEstadisticasUsuarios();

    // Actualiza la información de un usuario existente
    public void actualizarUsuario(Usuario usuario) throws Exception;
}
