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

    private Stage stage;
    private BorderPane root;
    private ControladorAutenticacion controladorAuth;
    private ControladorJuego controladorJuego;
    private Usuario usuarioActual;

    public VentanaPrincipal(Stage stage) {
        this.stage = stage;
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
        layout.setStyle("-fx-background-color: #222; -fx-padding: 20;");

        Label title = new Label("🏰 MAZE HUNTER");
        title.setStyle("-fx-font-size: 30px; -fx-font-weight: bold; -fx-text-fill: gold;");

        TextField emailField = new TextField();
        emailField.setPromptText("Correo Electrónico");
        emailField.setMaxWidth(300);

        PasswordField passField = new PasswordField();
        passField.setPromptText("Contraseña");
        passField.setMaxWidth(300);

        Button btnLogin = new Button("Iniciar Sesión");
        btnLogin.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");

        Button btnRegister = new Button("Registrarse");
        btnRegister.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");

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

        layout.getChildren().addAll(title, emailField, passField, btnLogin, btnRegister);
        root.setCenter(layout);
    }

    private void mostrarMenuPrincipal() {
        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: #333;");

        Label bienvenido = new Label("Bienvenido, " + (usuarioActual != null ? usuarioActual.getEmail() : "Hunter"));
        bienvenido.setStyle("-fx-font-size: 24px; -fx-text-fill: white;");

        Button btnJugar = new Button("🏹 Nueva Partida");
        Button btnCargar = new Button("📂 Cargar Partida");
        Button btnSalir = new Button("🚪 Salir");

        estilizarBoton(btnJugar);
        estilizarBoton(btnCargar);
        estilizarBoton(btnSalir);

        btnJugar.setOnAction(e -> mostrarSelectorDificultad());
        btnCargar.setOnAction(e -> cargarJuego());
        btnSalir.setOnAction(e -> System.exit(0));

        layout.getChildren().addAll(bienvenido, btnJugar, btnCargar, btnSalir);
        root.setCenter(layout);
    }

    private void mostrarSelectorDificultad() {
        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: #222;");

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

    private void estilizarBoton(Button btn) {
        btn.setStyle("-fx-background-color: #555; -fx-text-fill: white; -fx-font-size: 14px; -fx-min-width: 200px;");
        btn.setOnMouseEntered(e -> btn.setStyle(
                "-fx-background-color: #777; -fx-text-fill: white; -fx-font-size: 14px; -fx-min-width: 200px;"));
        btn.setOnMouseExited(e -> btn.setStyle(
                "-fx-background-color: #555; -fx-text-fill: white; -fx-font-size: 14px; -fx-min-width: 200px;"));
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
