package Main.ui.util;

import Main.modelo.Dominio.Usuario;
import Main.servicio.Implementaciones.CifradorImpl;
import Main.servicio.Implementaciones.PersistenciaJASON;
import Main.servicio.Implementaciones.ServicioUsuarioImpl;

import java.util.Scanner;

/**
 * Clase de utilidad encargada de la validación de contraseñas, la interacción
 * con el usuario para su solicitud, y la lógica de recuperación.
 * <p>
 * Esta clase se apoya en los servicios de {@code ServicioUsuarioImpl} y
 * {@code CifradorImpl} para manejar la seguridad y la persistencia de las
 * contraseñas.
 * </p>
 * 
 * @author Jose Berroteran
 * @version 1.0
 * @since 2025-11-15
 */
public class ValidadorContrasenia {
    /** Scanner para capturar la entrada de datos del usuario desde la consola. */
    private Scanner scanner;
    /**
     * Referencia al servicio de lógica de negocio para validar la existencia de
     * usuarios.
     */
    ServicioUsuarioImpl servicioUsuario = new ServicioUsuarioImpl();
    /** Referencia a la implementación de cifrado para descifrar contraseñas. */
    CifradorImpl cifrador = new CifradorImpl();
    /** Referencia a la persistencia para cargar y actualizar el objeto Usuario. */
    PersistenciaJASON persistencia = new PersistenciaJASON();

    /**
     * Válida si una cadena de texto cumple con los requisitos mínimos de seguridad.
     * <p>
     * Requisitos:
     * 1. Mínimo 6 caracteres de longitud.
     * 2. Contener al menos 1 letra mayúscula.
     * 3. Contener al menos 1 carácter especial.
     * </p>
     * 
     * @param contrasenia La cadena de texto a validar.
     * @return {@code true} si la contraseña es válida, {@code false} en caso
     *         contrario.
     */
    public boolean validarContrasenia(String contrasenia) {
        if (contrasenia == null || contrasenia.length() < 6)
            return false;
        boolean tieneMayuscula = contrasenia.matches(".*[A-Z].*");
        boolean tieneEspecial = contrasenia.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*");
        return tieneMayuscula && tieneEspecial;
    }

    /**
     * Solicita al usuario que ingrese y repita una contraseña, forzando la
     * validación
     * de requisitos y la coincidencia de ambas entradas.
     *
     * @return La contraseña válida en texto plano.
     */
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

    /**
     * Implementa la lógica de recuperación de contraseña para un usuario dado.
     * <p>
     * Verifica la existencia del email, recupera el hash cifrado, lo descifra y lo
     * muestra al usuario junto con información de la cuenta.
     * </p>
     * 
     * @param email El correo electrónico del usuario cuya contraseña se desea
     *              recuperar.
     */
    public void recuperarContrasenia(String email) {
        if (!servicioUsuario.existeUsuario(email)) {
            System.out.println("❌ Email no registrado en el sistema.");
            return;
        }

        // Obtener usuario y descifrar contraseña
        Usuario usuario = servicioUsuario.obtenerUsuario(email);

        if (usuario != null) {
            String contrasenia = cifrador.descifrarContrasenia(usuario.getContraseniaCifrada());

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
     * Actualiza la contraseña cifrada de un usuario en la capa de persistencia.
     * * Este metodo sobrescribe el usuario existente en la persistencia.
     *
     * @param email                   El correo electrónico del usuario a modificar.
     * @param nuevaContraseniaCifrada La nueva contraseña ya cifrada para guardar.
     * @return {@code true} si la contraseña fue actualizada exitosamente,
     *         {@code false} si el usuario no existe.
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
