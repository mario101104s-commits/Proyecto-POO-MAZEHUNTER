package Main.ui.gui;

import Main.controlador.ControladorAutenticacion;
import Main.controlador.ControladorJuego;
import Main.modelo.Dominio.Usuario;
import Main.servicio.Implementaciones.CifradorImpl;
import Main.servicio.Implementaciones.PersistenciaJASON;
import Main.servicio.Implementaciones.ServicioJuegoImpl;
import Main.servicio.Implementaciones.ServicioUsuarioImpl;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class VentanaPrincipal {

    private BorderPane root;
    private ControladorAutenticacion controladorAuth;
    private ControladorJuego controladorJuego;
    private Usuario usuarioActual;

    public VentanaPrincipal(Stage stage) {

        this.root = new BorderPane();
        inicializarControladores();
        mostrarPantallaLogin();
    }

    private void inicializarControladores() {
        PersistenciaJASON persistencia = new PersistenciaJASON();
        ServicioUsuarioImpl servicioUsuario = new ServicioUsuarioImpl(persistencia);
        CifradorImpl cifrador = new CifradorImpl();
        this.controladorAuth = new ControladorAutenticacion(servicioUsuario, cifrador);

        ServicioJuegoImpl servicioJuego = new ServicioJuegoImpl(persistencia);
        this.controladorJuego = new ControladorJuego(servicioJuego);
    }

    public Parent getView() {
        return root;
    }

    private void mostrarPantallaLogin() {
        VBox layout = new VBox(15);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #332b1a, #1a150a); -fx-padding: 20; -fx-border-color: #DAA520; -fx-border-width: 2;");

        Label title = new Label("🏰 MAZE HUNTER");
        title.setStyle("-fx-font-size: 30px; -fx-font-weight: bold; -fx-text-fill: gold;");

        TextField emailField = new TextField();
        emailField.setPromptText("Correo Electrónico");
        emailField.setMaxWidth(300);

        PasswordField passField = new PasswordField();
        passField.setPromptText("Contraseña");
        passField.setMaxWidth(300);

        Button btnLogin = new Button("Iniciar Sesión");
        estilizarBoton(btnLogin);

        Button btnRegister = new Button("Registrarse");
        estilizarBoton(btnRegister);

        Button btnRecuperar = new Button("Recuperar Contraseña");
        estilizarBoton(btnRecuperar);

        Button btnSalir = new Button("Salir");
        estilizarBoton(btnSalir);

        btnLogin.setOnAction(e -> {
            String email = emailField.getText();
            String pass = passField.getText();
            if (controladorAuth.iniciarSesion(email, pass) != null) {
                this.usuarioActual = controladorAuth.obtenerUsuario(email);
                mostrarMenuPrincipal();
            } else {
                mostrarAlerta("Error", "Credenciales incorrectas");
            }
        });

        btnRegister.setOnAction(e -> {
            String email = emailField.getText();
            String pass = passField.getText();
            if (email.isEmpty() || pass.isEmpty()) {
                mostrarAlerta("Error", "Ingrese datos para registrarse");
                return;
            }
            if (controladorAuth.registrarUsuario(email, pass)) {
                mostrarAlerta("Éxito", "Usuario registrado. Inicie sesión.");
            } else {
                mostrarAlerta("Error", "No se pudo registrar (¿ya existe?)");
            }
        });

        btnRecuperar.setOnAction(e -> mostrarPantallaRecuperacion());
        btnSalir.setOnAction(e -> javafx.application.Platform.exit());

        layout.getChildren().addAll(title, emailField, passField, btnLogin, btnRegister, btnRecuperar, btnSalir);
        root.setCenter(layout);
    }

    private void mostrarPantallaRecuperacion() {
        VBox layout = new VBox(15);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #332b1a, #1a150a); -fx-padding: 20; -fx-border-color: #DAA520; -fx-border-width: 2;");

        Label title = new Label("📜 RECUPERAR ACCESO");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: gold;");

        TextField emailField = new TextField();
        emailField.setPromptText("Correo Electrónico");
        emailField.setMaxWidth(300);

        PasswordField newPassField = new PasswordField();
        newPassField.setPromptText("Nueva Contraseña");
        newPassField.setMaxWidth(300);

        Button btnRestablecer = new Button("Restablecer Contraseña");
        estilizarBoton(btnRestablecer);

        Button btnVolver = new Button("Volver");
        estilizarBoton(btnVolver);

        btnRestablecer.setOnAction(e -> {
            String email = emailField.getText();
            String pass = newPassField.getText();
            if (controladorAuth.recuperarContrasenia(email, pass)) {
                mostrarAlerta("Éxito", "Contraseña restablecida exitosamente.");
                mostrarPantallaLogin();
            } else {
                mostrarAlerta("Error", "No se pudo restablecer la contraseña.");
            }
        });

        btnVolver.setOnAction(e -> mostrarPantallaLogin());

        layout.getChildren().addAll(title, emailField, newPassField, btnRestablecer, btnVolver);
        root.setCenter(layout);
    }

    private void mostrarMenuPrincipal() {
        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #332b1a, #1a150a); -fx-border-color: #DAA520; -fx-border-width: 2;");

        Label bienvenido = new Label("Bienvenido, " + (usuarioActual != null ? usuarioActual.getEmail() : "Hunter"));
        bienvenido.setStyle("-fx-font-size: 24px; -fx-text-fill: white;");

        Button btnJugar = new Button("🏹 Nueva Partida");
        Button btnCargar = new Button("📂 Cargar Partida");
        Button btnAnales = new Button("📜 Anales del Templo");
        Button btnLogout = new Button("🔑 Cerrar Sesión");
        Button btnSalir = new Button("🚪 Salir");

        estilizarBoton(btnJugar);
        estilizarBoton(btnCargar);
        estilizarBoton(btnAnales);
        estilizarBoton(btnLogout);
        estilizarBoton(btnSalir);

        btnJugar.setOnAction(e -> mostrarSelectorDificultad());
        btnCargar.setOnAction(e -> cargarJuego());
        btnAnales.setOnAction(e -> mostrarAnales());
        btnLogout.setOnAction(e -> cerrarSesion());
        btnSalir.setOnAction(e -> System.exit(0));

        layout.getChildren().addAll(bienvenido, btnJugar, btnCargar, btnAnales, btnLogout, btnSalir);
        root.setCenter(layout);
    }

    private void mostrarSelectorDificultad() {
        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #332b1a, #1a150a); -fx-border-color: #DAA520; -fx-border-width: 2;");

        Label lbl = new Label("Selecciona Dificultad");
        lbl.setStyle("-fx-text-fill: white; -fx-font-size: 20px;");

        ComboBox<String> combo = new ComboBox<>();
        combo.getItems().addAll("Fácil", "Media", "Difícil");
        combo.setValue("Media");

        CheckBox checkNiebla = new CheckBox("Activar Niebla de Guerra");
        checkNiebla.setStyle("-fx-text-fill: white;");
        checkNiebla.setSelected(true);

        Button btnIniciar = new Button("Comenzar");
        estilizarBoton(btnIniciar);

        combo.setStyle("-fx-background-color: #444; -fx-text-fill: #DAA520; -fx-border-color: #DAA520;");
        checkNiebla.setStyle("-fx-text-fill: #DAA520; -fx-font-weight: bold;");

        btnIniciar.setOnAction(e -> {
            String diff = combo.getValue();
            boolean niebla = checkNiebla.isSelected();
            iniciarJuego(diff, niebla);
        });

        Button btnVolver = new Button("Volver");
        estilizarBoton(btnVolver);
        btnVolver.setOnAction(e -> mostrarMenuPrincipal());

        layout.getChildren().addAll(lbl, combo, checkNiebla, btnIniciar, btnVolver);
        root.setCenter(layout);
    }

    private void iniciarJuego(String dificultad, boolean niebla) {
        try {
            int diff = switch (dificultad) {
                case "Fácil" -> 1;
                case "Difícil" -> 3;
                default -> 2;
            };

            String estrategia = switch (diff) {
                case 1 -> "FACIL";
                case 3 -> "DIFICIL";
                default -> "MEDIA";
            };
            controladorJuego.setEstrategiaGeneracion(estrategia);

            int filas = controladorJuego.generarFilasAleatorias(estrategia);
            int cols = controladorJuego.generarColumnasAleatorias(estrategia);

            controladorJuego.iniciarNuevoJuego(filas, cols, usuarioActual.getEmail());

            mostrarVistaJuego();

            // Configurar niebla en la vista si es necesario (la vista lo lee del juego o se
            // le pasa)
            // En este caso, VistaJuego leerá el estado, pero la niebla es visual.
            // Pasaremos el parámetro a VistaJuego.
            ((VistaJuego) root.getCenter()).setNiebla(niebla);

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error", "No se pudo iniciar el juego: " + e.getMessage());
        }
    }

    private void cargarJuego() {
        if (controladorJuego.cargarJuegoGuardado(usuarioActual.getEmail()) != null) {
            mostrarVistaJuego();
        } else {
            mostrarAlerta("Info", "No tienes partidas guardadas.");
        }
    }

    private void mostrarVistaJuego() {
        VistaJuego vistaJuego = new VistaJuego(controladorJuego, () -> mostrarMenuPrincipal());
        root.setCenter(vistaJuego);
        vistaJuego.requestFocus(); // Para capturar teclado
    }

    private void mostrarAnales() {
        java.util.List<Main.modelo.Dominio.EstadisticasJuego> stats = controladorJuego
                .obtenerEstadisticas(usuarioActual.getEmail());
        VistaAnales vistaAnales = new VistaAnales(stats, () -> mostrarMenuPrincipal());
        root.setCenter(vistaAnales);
    }

    private void cerrarSesion() {
        this.usuarioActual = null;
        mostrarPantallaLogin();
    }

    private void estilizarBoton(Button btn) {
        String baseStyle = "-fx-background-color: linear-gradient(to bottom, #555, #222); " +
                "-fx-text-fill: #DAA520; " +
                "-fx-font-size: 14px; " +
                "-fx-font-weight: bold; " +
                "-fx-min-width: 220px; " +
                "-fx-border-color: #DAA520; " +
                "-fx-border-width: 1; " +
                "-fx-border-radius: 5; " +
                "-fx-background-radius: 5; " +
                "-fx-cursor: hand;";

        String hoverStyle = "-fx-background-color: linear-gradient(to bottom, #777, #444); " +
                "-fx-text-fill: #FFD700; " +
                "-fx-font-size: 14px; " +
                "-fx-font-weight: bold; " +
                "-fx-min-width: 220px; " +
                "-fx-border-color: #FFD700; " +
                "-fx-border-width: 2; " +
                "-fx-border-radius: 5; " +
                "-fx-background-radius: 5; " +
                "-fx-cursor: hand;";

        btn.setStyle(baseStyle);
        btn.setOnMouseEntered(e -> btn.setStyle(hoverStyle));
        btn.setOnMouseExited(e -> btn.setStyle(baseStyle));
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
