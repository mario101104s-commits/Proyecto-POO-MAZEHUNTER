package Main.controlador;

import Main.modelo.Dominio.Usuario;
import Main.servicio.Interfaces.Cifrador;
import Main.servicio.Interfaces.ServicioUsuario;

// Controlador que maneja toda la lógica de autenticación
public class ControladorAutenticacion {
    private ServicioUsuario servicioUsuario;
    private Cifrador cifrador;

    public ControladorAutenticacion(ServicioUsuario servicioUsuario, Cifrador cifrador) {
        this.servicioUsuario = servicioUsuario;
        this.cifrador = cifrador;
    }

    // Valida credenciales y retorna el email si es exitoso, null si falla
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

    // Obtiene un usuario por su email
    public Usuario obtenerUsuario(String email) {
        return servicioUsuario.obtenerUsuario(email);
    }

    // Registra un nuevo usuario
    public boolean registrarUsuario(String email, String contrasenia) {
        if (servicioUsuario.existeUsuario(email)) {
            return false;
        }

        String contraseniaCifrada = cifrador.cifrarContrasenia(contrasenia);
        return servicioUsuario.registrarUsuario(email, contraseniaCifrada);
    }

    // Recupera contraseña de un usuario existente
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

    // Valida que la contraseña cumpla con los requisitos de seguridad
    public boolean validarContrasenia(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }

        boolean tieneMayuscula = password.matches(".*[A-Z].*");
        boolean tieneEspecial = password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*");

        return tieneMayuscula && tieneEspecial;
    }

    // Valida formato de email
    public boolean validarEmail(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    // Verifica si un usuario existe
    public boolean existeUsuario(String email) {
        return servicioUsuario.existeUsuario(email);
    }
}
