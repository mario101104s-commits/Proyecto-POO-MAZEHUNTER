package Main;

import Main.controlador.ControladorAutenticacion;
import Main.controlador.ControladorJuego;
import Main.servicio.Implementaciones.CifradorImpl;
import Main.servicio.Implementaciones.PersistenciaJASON;
import Main.servicio.Implementaciones.ServicioJuegoImpl;
import Main.servicio.Implementaciones.ServicioUsuarioImpl;
import Main.servicio.Interfaces.Cifrador;
import Main.servicio.Interfaces.ServicioJuego;
import Main.servicio.Interfaces.ServicioUsuario;
import Main.ui.consola.AutenticacionConsola;
import Main.ui.consola.MenuPrincipal;
import Main.ui.util.ConsoleUtils;

// Punto de entrada principal de la aplicación - Patrón MVC
public class Main {
    public static void main(String[] args) {
        ConsoleUtils.limpiarConsola();
        ConsoleUtils.mostrarMensaje("=== 🏰 MAZE HUNTER - EL TEMPLO PERDIDO ===");
        ConsoleUtils.mostrarMensaje("Bienvenido, valiente Hunter. El templo ancestral te espera...");

        try {
            // Inicializar capa de persistencia
            PersistenciaJASON persistencia = new PersistenciaJASON();
            persistencia.cargarUsuarios();
            persistencia.cargarEstadisticas();

            // Inicializar servicios
            Cifrador cifrador = new CifradorImpl();
            ServicioUsuario servicioUsuario = new ServicioUsuarioImpl(persistencia);
            ServicioJuego servicioJuego = new ServicioJuegoImpl(persistencia);

            // Inicializar controladores
            ControladorAutenticacion controladorAuth = new ControladorAutenticacion(servicioUsuario, cifrador);
            ControladorJuego controladorJuego = new ControladorJuego(servicioJuego);

            // Inicializar vistas
            AutenticacionConsola vistaAuth = new AutenticacionConsola(controladorAuth);
            MenuPrincipal menuPrincipal = new MenuPrincipal(controladorJuego);

            // Flujo principal de la aplicación
            String usuarioAutenticado = null;
            boolean ejecutando = true;

            while (ejecutando) {
                if (usuarioAutenticado == null) {
                    // Mostrar menú de autenticación
                    vistaAuth.mostrarMenu();
                    String resultado = vistaAuth.manejarMenu();

                    if ("SALIR".equals(resultado)) {
                        ejecutando = false;
                    } else if (resultado != null) {
                        usuarioAutenticado = resultado;
                    }
                } else {
                    // Mostrar menú principal
                    boolean continuarSesion = menuPrincipal.mostrarMenu(usuarioAutenticado);
                    if (!continuarSesion) {
                        usuarioAutenticado = null; // Cerrar sesión
                    }
                }
            }

            ConsoleUtils.mostrarMensaje("🎮 ¡Que los cristales te guíen, Hunter! Hasta la próxima aventura.");

        } catch (Exception e) {
            ConsoleUtils.mostrarError("Error crítico en el sistema del templo: " + e.getMessage());
            e.printStackTrace();
        }
    }
}