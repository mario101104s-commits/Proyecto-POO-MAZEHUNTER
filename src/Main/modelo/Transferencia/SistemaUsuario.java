package Main.modelo.Transferencia;

import Main.modelo.Dominio.Usuario;
import Main.servicio.Implementaciones.CifradorImpl;
import Main.servicio.Implementaciones.PersistenciaJASON;
import Main.servicio.Implementaciones.ServicioUsuarioImpl;
import Main.servicio.Interfaces.Cifrador;
import Main.servicio.Interfaces.ServicioUsuario;
import Main.ui.util.ValidadorContrasenia;
import Main.ui.util.ValidadorEmail;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.*;
import java.util.regex.Pattern;
import java.util.Base64;

public class SistemaUsuario implements Cifrador {
    private ServicioUsuario servicioUsuario;
    private Scanner scanner;
    private CifradorImpl cifrador;
    ValidadorEmail validadorEmail = new ValidadorEmail();
    ValidadorContrasenia validadorContrasenia = new ValidadorContrasenia();
    PersistenciaJASON persistencia = new PersistenciaJASON();

    //METODOS DE CIFRADOR
    private static final String CLAVE_CIFRADO = "MiClaveSecreta12";
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


    public String recuperarContraseniaCifrada(String email) {
        Usuario usuario = persistencia.cargarUsuario(email);
        return (usuario != null) ? usuario.getContraseniaCifrada() : null;
    }
    
    public SistemaUsuario() {
        this.servicioUsuario = new ServicioUsuarioImpl();
        this.scanner = new Scanner(System.in);
        this.cifrador = new CifradorImpl();
    }

    // MÉTODO PRINCIPAL - Registro de usuario
    public boolean registrarUsuario() {
        System.out.println("\n=== REGISTRO DE USUARIO ===");

        // 1. Validar email
        String email = validadorEmail.solicitarEmail();
        if (email == null) return false;

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
        if (contrasenia == null) return false;

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

    // MÉTODO PARA AUTENTICAR
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

    // MÉTODO PARA MOSTRAR ESTADÍSTICAS
    public void mostrarEstadisticasUsuarios() {
        System.out.println("\n" + servicioUsuario.obtenerEstadisticasUsuarios());
    }

}