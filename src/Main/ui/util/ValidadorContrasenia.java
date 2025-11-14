package Main.ui.util;

import Main.modelo.Dominio.Usuario;
import Main.servicio.Implementaciones.CifradorImpl;
import Main.servicio.Implementaciones.PersistenciaJASON;
import Main.servicio.Implementaciones.ServicioUsuarioImpl;

import java.util.Scanner;

public class ValidadorContrasenia {
    private Scanner scanner;
    ServicioUsuarioImpl servicioUsuario = new ServicioUsuarioImpl();
    CifradorImpl cifrador = new CifradorImpl();
    PersistenciaJASON persistencia = new PersistenciaJASON();

    public boolean validarContrasenia(String contrasenia) {
        if (contrasenia == null || contrasenia.length() < 6) return false;
        boolean tieneMayuscula = contrasenia.matches(".*[A-Z].*");
        boolean tieneEspecial = contrasenia.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*");
        return tieneMayuscula && tieneEspecial;
    }

    public String solicitarContrasenia() {
        String contrasenia, repetirContrasenia;

        while (true) {
            System.out.print("Ingrese su contraseña: ");
            contrasenia = scanner.nextLine();

            if (!validarContrasenia(contrasenia)) {
                System.out.println("❌ La contraseña no cumple los requisitos.");
                System.out.println("💡 Debe tener: mínimo 6 caracteres, 1 mayúscula y 1 carácter especial");
                continue;
            }

            System.out.print("Repita su contraseña: ");
            repetirContrasenia = scanner.nextLine();

            if (!contrasenia.equals(repetirContrasenia)) {
                System.out.println("❌ Las contraseñas no coinciden.");
            } else {
                break;
            }
        }

        return contrasenia;
    }

    public void recuperarContrasenia(String email) {
        if (!servicioUsuario.existeUsuario(email)) {
            System.out.println("❌ Email no registrado en el sistema.");
            return;
        }

        // Recuperar contraseña cifrada
        String contraseniaCifrada = cifrador.recuperarContraseniaCifrada(email);

        if (contraseniaCifrada != null) {
            // Descifrar para mostrar al usuario
            String contrasenia = cifrador.descifrarContrasenia(contraseniaCifrada);

            if (contrasenia != null) {
                System.out.println("✅ Contraseña recuperada:");
                System.out.println("📧 Email: " + email);
                System.out.println("📅 Fecha de registro: " + servicioUsuario.obtenerFechaRegistroFormateada(email));
                System.out.println("🔑 Contraseña: " + contrasenia);
            } else {
                System.out.println("❌ Error al descifrar la contraseña.");
            }
        } else {
            System.out.println("❌ Error al recuperar la contraseña.");
        }
    }

    /**
     * Actualiza la contraseña de un usuario
     */
    public boolean actualizarContrasenia(String email, String nuevaContraseniaCifrada) {
        Usuario usuario = persistencia.cargarUsuario(email);

        if (usuario == null) {
            return false;
        }

        usuario.setContraseniaCifrada(nuevaContraseniaCifrada);
        return persistencia.guardarUsuario(usuario);
    }
}
