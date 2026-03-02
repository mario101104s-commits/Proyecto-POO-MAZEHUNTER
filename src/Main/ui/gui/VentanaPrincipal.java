package Main.ui.gui;

import Main.controlador.ControladorAutenticacion;
import Main.controlador.ControladorJuego;
import Main.modelo.Dominio.Usuario;
import Main.servicio.Implementaciones.CifradorImpl;
import Main.servicio.Implementaciones.PersistenciaJASON;
import Main.servicio.Implementaciones.ServicioJuegoImpl;
import Main.servicio.Implementaciones.ServicioUsuarioImpl;
import Main.ui.gui.audio.GestorAudio;
import Main.ui.gui.audio.ControladorAudioUI;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Orquestador principal de la interfaz gráfica de usuario (GUI) para Maze
 * Hunter.
 * <p>
 * Esta clase centraliza la navegación de la aplicación, gestionando la
 * transición
 * entre el sistema de autenticación, el menú principal y el entorno de juego
 * activo.
 * Utiliza un contenedor {@link BorderPane} para realizar el intercambio
 * dinámico de vistas.
 * </p>
 *
 * @author Mario Sanchez
 * @version 1.3
 * @since 03/01/26
 */
public class VentanaPrincipal {

    /** Contenedor raíz de la aplicación que permite alternar entre pantallas. */
    private BorderPane root;

    /** Controlador para la gestión de usuarios, sesiones y seguridad. */
    private ControladorAutenticacion controladorAuth;

    /**
     * Controlador para la gestión de la lógica de negocio y estado del laberinto.
     */
    private ControladorJuego controladorJuego;

    /** Entidad que representa al Hunter que ha iniciado sesión actualmente. */
    private Usuario usuarioActual;

    /** Implementación del servicio de cifrado para el manejo de datos sensibles. */
    private CifradorImpl cifrador;

    /**
     * Inicializa la estructura visual básica y la cadena de dependencias del
     * sistema.
     * 
     * @param stage El escenario principal de JavaFX sobre el que se montará la
     *              vista.
     */
    public VentanaPrincipal(Stage stage) {
        this.root = new BorderPane();
        inicializarControladores();

        // Iniciar audio
        GestorAudio.getInstancia().reproducirMusica("principal.mp3");

        mostrarPantallaLogin();
    }

    /**
     * Configura la arquitectura del sistema instanciando la persistencia,
     * los servicios y finalmente los controladores de la aplicación.
     */
    private void inicializarControladores() {
        PersistenciaJASON persistencia = new PersistenciaJASON();
        ServicioUsuarioImpl servicioUsuario = new ServicioUsuarioImpl(persistencia);
        CifradorImpl cifrador = new CifradorImpl();
        this.controladorAuth = new ControladorAutenticacion(servicioUsuario, cifrador);
        this.cifrador = new CifradorImpl();
        ServicioJuegoImpl servicioJuego = new ServicioJuegoImpl(persistencia);
        this.controladorJuego = new ControladorJuego(servicioJuego);
    }

    /**
     * @return El nodo raíz {@link Parent} que contiene la interfaz actual para ser
     *         renderizada.
     */
    public Parent getView() {
        return root;
    }

    /**
     * Muestra la ventana de instrucciones desde el menú principal.
     */
    private void mostrarInstruccionesDesdeMenu() {
        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(30));

        // Control de audio
        ControladorAudioUI audioUI = new ControladorAudioUI();

        // Aplicar fondo de imagen fondo2.jpg
        try {
            Image fondoImg = new Image(getClass().getResourceAsStream("/imagenes/fondo2.jpg"));
            BackgroundImage bgImg = new BackgroundImage(
                    fondoImg,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER,
                    new BackgroundSize(100, 100, true, true, false, true));
            layout.setBackground(new Background(bgImg));
        } catch (Exception e) {
            layout.setStyle("-fx-background-color: linear-gradient(to bottom, #332b1a, #1a150a);");
            System.err.println("Error cargando fondo2.jpg en instrucciones: " + e.getMessage());
        }

        // Título
        Label titulo = new Label("¿Cómo jugar?");
        titulo.setStyle(
                "-fx-font-family: 'Papyrus', 'Copperplate', serif; -fx-text-fill: #DAA520; -fx-font-size: 32px; -fx-font-weight: bold; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.8), 10, 0, 0, 3);");

        // Contenedor de dos columnas
        HBox columnasContainer = new HBox(20);
        columnasContainer.setAlignment(Pos.CENTER);

        // Columna izquierda
        VBox columnaIzquierda = new VBox(12);
        columnaIzquierda.setAlignment(Pos.CENTER_LEFT);
        columnaIzquierda.setStyle("-fx-background-color: rgba(0, 0, 0, 0.8); " +
                "-fx-background-radius: 15; " +
                "-fx-padding: 20; " +
                "-fx-border-color: #DAA520; " +
                "-fx-border-width: 2; " +
                "-fx-border-radius: 15;");
        columnaIzquierda.setPrefWidth(320);

        // Columna derecha
        VBox columnaDerecha = new VBox(12);
        columnaDerecha.setAlignment(Pos.CENTER_LEFT);
        columnaDerecha.setStyle("-fx-background-color: rgba(0, 0, 0, 0.8); " +
                "-fx-background-radius: 15; " +
                "-fx-padding: 20; " +
                "-fx-border-color: #DAA520; " +
                "-fx-border-width: 2; " +
                "-fx-border-radius: 15;");
        columnaDerecha.setPrefWidth(320);

        // CONTENIDO COLUMNA IZQUIERDA
        Label controlesTitulo = new Label("🎮 CONTROLES");
        controlesTitulo.setStyle(
                "-fx-font-family: 'Papyrus', 'Copperplate', serif; -fx-text-fill: #FFD700; -fx-font-size: 20px; -fx-font-weight: bold;");

        Label controlesTexto = new Label(
                "• W,A,S,D - Mover jugador\n" +
                        "• K - Activar bomba\n" +
                        "• L - Activar Teletransporte\n" +
                        "• ESC - Menú pausa");
        controlesTexto.setStyle(
                "-fx-font-family: 'Papyrus', 'Copperplate', serif; -fx-text-fill: white; -fx-font-size: 16px;");

        Label objetivoTitulo = new Label("🏆 OBJETIVO");
        objetivoTitulo.setStyle(
                "-fx-font-family: 'Papyrus', 'Copperplate', serif; -fx-text-fill: #FFD700; -fx-font-size: 20px; -fx-font-weight: bold;");

        Label objetivoTexto = new Label(
                "• Encuentra llave 🔑\n" +
                        "• Localiza salida 🚪\n" +
                        "• Escapa con vida");
        objetivoTexto.setStyle(
                "-fx-font-family: 'Papyrus', 'Copperplate', serif; -fx-text-fill: white; -fx-font-size: 16px;");

        Label TeleportTitulo = new Label("TELETRANSPORTE");
        TeleportTitulo.setStyle(
                "-fx-font-family: 'Papyrus', 'Copperplate', serif; -fx-text-fill: #FFD700; -fx-font-size: 20px; -fx-font-weight: bold;");

        Label TeleportTexto = new Label(
                "• Necesitas: Llave Negra 🗝️\n" +
                        "• Limitado a 1 uso por partida");
        TeleportTexto.setStyle(
                "-fx-font-family: 'Papyrus', 'Copperplate', serif; -fx-text-fill: white; -fx-font-size: 16px;");

        // CONTENIDO COLUMNA DERECHA
        Label combateTitulo = new Label("💥 EXPLOSIÓN");
        combateTitulo.setStyle(
                "-fx-font-family: 'Papyrus', 'Copperplate', serif; -fx-text-fill: #FFD700; -fx-font-size: 20px; -fx-font-weight: bold;");

        Label combateTexto = new Label(
                "• Necesitas: 1 bomba 💣\n" +
                        "• Necesitas: 1 fósforo 🔥\n" +
                        "• Rompen muros rojos\n" +
                        "• Úsalas estratégicamente");
        combateTexto.setStyle(
                "-fx-font-family: 'Papyrus', 'Copperplate', serif; -fx-text-fill: white; -fx-font-size: 16px;");

        Label mecanicasTitulo = new Label("⚔️ MECÁNICAS");
        mecanicasTitulo.setStyle(
                "-fx-font-family: 'Papyrus', 'Copperplate', serif; -fx-text-fill: #FFD700; -fx-font-size: 20px; -fx-font-weight: bold;");

        Label mecanicasTexto = new Label(
                "• 💀 Trampas: -20 vida\n" +
                        "• ⚡ Energía: +10 vida\n" +
                        "• 💎 Cristales: +puntos\n" +
                        "• 💣 Activar bomba: -35 vida");
        mecanicasTexto.setStyle(
                "-fx-font-family: 'Papyrus', 'Copperplate', serif; -fx-text-fill: white; -fx-font-size: 16px;");

        // Agregar contenido a columnas
        columnaIzquierda.getChildren().addAll(
                controlesTitulo, controlesTexto,
                objetivoTitulo, objetivoTexto,
                TeleportTitulo, TeleportTexto);

        columnaDerecha.getChildren().addAll(
                combateTitulo, combateTexto,
                mecanicasTitulo, mecanicasTexto);

        // Ensamblar columnas
        columnasContainer.getChildren().addAll(columnaIzquierda, columnaDerecha);

        // Botón volver - copiado exactamente del selector de dificultad
        Button btnVolver = new Button("Volver");
        estilizarBotonConFondo(btnVolver, "salida");
        btnVolver.setPrefSize(250, 60);

        btnVolver.setOnAction(e -> mostrarMenuPrincipal());

        layout.getChildren().addAll(titulo, columnasContainer, btnVolver);

        AnchorPane wrapper = new AnchorPane();
        wrapper.getChildren().addAll(layout, audioUI);

        AnchorPane.setTopAnchor(layout, 0.0);
        AnchorPane.setBottomAnchor(layout, 0.0);
        AnchorPane.setLeftAnchor(layout, 0.0);
        AnchorPane.setRightAnchor(layout, 0.0);

        AnchorPane.setTopAnchor(audioUI, 20.0);
        AnchorPane.setRightAnchor(audioUI, 20.0);

        root.setCenter(wrapper);
    }

    /**
     * Construye y despliega la pantalla de autenticación con tema de jungla.
     */
    private void mostrarPantallaLogin() {
        GestorAudio.getInstancia().reproducirMusica("principal.mp3");
        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(40));

        // Aplicar fondo de imagen
        try {
            Image fondoImg = new Image(getClass().getResourceAsStream("/imagenes/fondo3.jpg"));
            BackgroundImage bgImg = new BackgroundImage(
                    fondoImg,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER,
                    new BackgroundSize(100, 100, true, true, false, true));
            layout.setBackground(new Background(bgImg));
        } catch (Exception e) {
            // Fallback a gradiente si no se encuentra la imagen
            layout.setStyle("-fx-background-color: linear-gradient(to bottom, #1a3a1a, #0a1a0a);");
            System.err.println("Error cargando fondo3.jpg: " + e.getMessage());
        }

        Label title = new Label("MAZE HUNTER");
        title.setStyle(
                "-fx-font-family: 'Papyrus', 'Copperplate', serif; -fx-font-size: 48px; -fx-font-weight: bold; -fx-text-fill: white; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.8), 10, 0, 0, 3);");

        TextField emailField = new TextField();
        emailField.setPromptText("Correo Electrónico");
        emailField.setMaxWidth(400);
        emailField.setStyle(
                "-fx-font-family: 'Papyrus', 'Copperplate', serif; -fx-font-size: 14px; -fx-background-color: rgba(255,255,255,0.9); -fx-background-radius: 5;");

        PasswordField passField = new PasswordField();
        passField.setPromptText("Contraseña");
        passField.setMaxWidth(400);
        passField.setStyle(
                "-fx-font-family: 'Papyrus', 'Copperplate', serif; -fx-font-size: 14px; -fx-background-color: rgba(255,255,255,0.9); -fx-background-radius: 5;");

        // Grid para botones en 2 columnas
        GridPane botonesGrid = new GridPane();
        botonesGrid.setAlignment(Pos.CENTER);
        botonesGrid.setHgap(20);
        botonesGrid.setVgap(15);
        botonesGrid.setPadding(new Insets(20, 0, 0, 0));

        Button btnLogin = new Button("Iniciar Sesión");
        estilizarBotonConFondo(btnLogin, "entrada");
        btnLogin.setPrefSize(250, 60);

        Button btnRegister = new Button("Registrarse");
        estilizarBotonConFondo(btnRegister, "entrada");
        btnRegister.setPrefSize(250, 60);

        Button btnRecuperar = new Button("Recuperar Contraseña");
        estilizarBotonConFondo(btnRecuperar, "generico");
        btnRecuperar.setPrefSize(250, 60);

        Button btnSalir = new Button("Salir");
        estilizarBotonConFondo(btnSalir, "salida");
        btnSalir.setPrefSize(250, 60);

        // Colocar botones en grid 2x2
        botonesGrid.add(btnLogin, 0, 0);
        botonesGrid.add(btnRegister, 1, 0);
        botonesGrid.add(btnRecuperar, 0, 1);
        botonesGrid.add(btnSalir, 1, 1);

        btnLogin.setOnAction(e -> {
            String email = emailField.getText();
            String pass = passField.getText();
            Main.modelo.Transferencia.ResultadoAutenticacion resultado = controladorAuth.iniciarSesionConDetalle(email,
                    pass);

            if (resultado.isExitoso()) {
                this.usuarioActual = controladorAuth.obtenerUsuario(email);
                mostrarMenuPrincipal();
            } else {
                mostrarAlerta("Error al iniciar sesión", resultado.getMensajeError());
            }
        });

        btnRegister.setOnAction(e -> {
            String email = emailField.getText();
            String pass = passField.getText();

            String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
            String passRegex = "^(?=.*[A-Z])(?=.*[@#$%^&+=!]).{6,}$";

            if (email.isEmpty() || pass.isEmpty()) {
                mostrarAlerta("Error", "Ingrese datos para registrarse");
                return;
            }

            if (!email.matches(emailRegex)) {
                mostrarAlerta("Email No Válido", "El formato del correo debe ser ejemplo@dominio.com");
                return;
            }

            if (!pass.matches(passRegex)) {
                mostrarAlerta("Contraseña No Válida",
                        "La contraseña debe tener:\n- Mínimo 6 caracteres\n- Una letra MAYÚSCULA\n- Un carácter especial (@#$%^&+=!)");
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

        layout.getChildren().addAll(title, emailField, passField, botonesGrid);

        // Control de audio
        ControladorAudioUI audioUI = new ControladorAudioUI();

        AnchorPane wrapper = new AnchorPane();
        wrapper.getChildren().addAll(layout, audioUI);

        AnchorPane.setTopAnchor(layout, 0.0);
        AnchorPane.setBottomAnchor(layout, 0.0);
        AnchorPane.setLeftAnchor(layout, 0.0);
        AnchorPane.setRightAnchor(layout, 0.0);

        AnchorPane.setTopAnchor(audioUI, 20.0);
        AnchorPane.setRightAnchor(audioUI, 20.0);

        root.setCenter(wrapper);
    }

    /**
     * Construye y despliega la pantalla para el restablecimiento de credenciales.
     */
    /**
     * Construye y despliega la pantalla para el restablecimiento de credenciales.
     */
    private void mostrarPantallaRecuperacion() {
        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(40));

        // Aplicar fondo de imagen
        try {
            Image fondoImg = new Image(getClass().getResourceAsStream("/imagenes/fondo3.jpg"));
            BackgroundImage bgImg = new BackgroundImage(
                    fondoImg,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER,
                    new BackgroundSize(100, 100, true, true, false, true));
            layout.setBackground(new Background(bgImg));
        } catch (Exception e) {
            layout.setStyle("-fx-background-color: linear-gradient(to bottom, #1a3a1a, #0a1a0a);");
            System.err.println("Error cargando fondo3.jpg en recuperación: " + e.getMessage());
        }

        Label title = new Label("RECUPERAR ACCESO");
        title.setStyle(
                "-fx-font-family: 'Papyrus', 'Copperplate', serif; -fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: white; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.8), 10, 0, 0, 3);");

        TextField emailField = new TextField();
        emailField.setPromptText("Correo Electrónico");
        emailField.setMaxWidth(400);
        emailField.setStyle(
                "-fx-font-family: 'Papyrus', 'Copperplate', serif; -fx-font-size: 14px; -fx-background-color: rgba(255,255,255,0.9); -fx-background-radius: 5;");

        PasswordField newPassField = new PasswordField();
        newPassField.setPromptText("Nueva Contraseña");
        newPassField.setMaxWidth(400);
        newPassField.setStyle(
                "-fx-font-family: 'Papyrus', 'Copperplate', serif; -fx-font-size: 14px; -fx-background-color: rgba(255,255,255,0.9); -fx-background-radius: 5;");

        Button btnRestablecer = new Button("Restablecer Contraseña");
        estilizarBotonConFondo(btnRestablecer, "generico");
        btnRestablecer.setPrefSize(300, 60);

        Button btnVolver = new Button("Volver");
        estilizarBotonConFondo(btnVolver, "generico");
        btnVolver.setPrefSize(300, 60);

        btnRestablecer.setOnAction(e -> {
            String email = emailField.getText();
            String pass = newPassField.getText();

            String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
            String passRegex = "^(?=.*[A-Z])(?=.*[@#$%^&+=!]).{6,}$";

            if (!email.matches(emailRegex)) {
                mostrarAlerta("Email No Válido", "Ingrese un correo válido para recuperar su cuenta.");
                return;
            }

            if (!pass.matches(passRegex)) {
                mostrarAlerta("Contraseña No Válida",
                        "La nueva contraseña debe tener:\n- Mínimo 6 caracteres\n- Una letra MAYÚSCULA\n- Un carácter especial (@#$%^&+=!)");
                return;
            }

            if (controladorAuth.recuperarContrasenia(email, pass)) {
                mostrarAlerta("Éxito", "Contraseña restablecida exitosamente.");
                mostrarPantallaLogin();
            } else {
                mostrarAlerta("Error", "No se pudo restablecer la contraseña.");
            }
        });

        btnVolver.setOnAction(e -> mostrarPantallaLogin());

        layout.getChildren().addAll(title, emailField, newPassField, btnRestablecer, btnVolver);

        // Control de audio
        ControladorAudioUI audioUI = new ControladorAudioUI();

        AnchorPane wrapper = new AnchorPane();
        wrapper.getChildren().addAll(layout, audioUI);

        AnchorPane.setTopAnchor(layout, 0.0);
        AnchorPane.setBottomAnchor(layout, 0.0);
        AnchorPane.setLeftAnchor(layout, 0.0);
        AnchorPane.setRightAnchor(layout, 0.0);

        AnchorPane.setTopAnchor(audioUI, 20.0);
        AnchorPane.setRightAnchor(audioUI, 20.0);

        root.setCenter(wrapper);
    }

    /**
     * Despliega el HUB central del usuario autenticado con tema de jungla.
     */
    private void mostrarMenuPrincipal() {
        GestorAudio.getInstancia().reproducirMusica("principal.mp3");
        VBox layout = new VBox(25);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(40));

        // Aplicar fondo de imagen
        try {
            Image fondoImg = new Image(getClass().getResourceAsStream("/imagenes/fondo3.jpg"));
            BackgroundImage bgImg = new BackgroundImage(
                    fondoImg,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER,
                    new BackgroundSize(100, 100, true, true, false, true));
            layout.setBackground(new Background(bgImg));
        } catch (Exception e) {
            layout.setStyle("-fx-background-color: linear-gradient(to bottom, #1a3a1a, #0a1a0a);");
            System.err.println("Error cargando fondo3.jpg en menú: " + e.getMessage());
        }

        String nombreAMostrar = "Hunter";
        if (usuarioActual != null) {
            String emailCifrado = usuarioActual.getEmail();
            nombreAMostrar = cifrador.descifrarEmail(emailCifrado);
        }
        Label bienvenido = new Label("Bienvenido, " + nombreAMostrar);
        bienvenido.setStyle(
                "-fx-font-family: 'Papyrus', 'Copperplate', serif; -fx-font-size: 32px; -fx-text-fill: white; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.8), 10, 0, 0, 3);");

        // Grid para botones en 2 columnas
        GridPane botonesGrid = new GridPane();
        botonesGrid.setAlignment(Pos.CENTER);
        botonesGrid.setHgap(25);
        botonesGrid.setVgap(20);
        botonesGrid.setPadding(new Insets(30, 0, 0, 0));

        Button btnJugar = new Button("Nueva Partida");
        estilizarBotonConFondo(btnJugar, "entrada");
        btnJugar.setPrefSize(250, 70);

        Button btnCargar = new Button("Cargar Partida");
        estilizarBotonConFondo(btnCargar, "generico");
        btnCargar.setPrefSize(250, 70);

        Button btnAnales = new Button("Anales del Templo");
        estilizarBotonConFondo(btnAnales, "anales");
        btnAnales.setPrefSize(250, 70);

        Button btnInstrucciones = new Button("Instrucciones");
        estilizarBotonConFondo(btnInstrucciones, "generico");
        btnInstrucciones.setPrefSize(250, 70);

        Button btnLogout = new Button("Cerrar Sesión");
        estilizarBotonConFondo(btnLogout, "generico");
        btnLogout.setPrefSize(250, 70);

        Button btnSalir = new Button("Salir");
        estilizarBotonConFondo(btnSalir, "salida");
        btnSalir.setPrefSize(250, 70);

        // Colocar botones en grid (3 botones por columna)
        botonesGrid.add(btnJugar, 0, 0);
        botonesGrid.add(btnCargar, 1, 0);
        botonesGrid.add(btnAnales, 0, 1);
        botonesGrid.add(btnInstrucciones, 1, 1);
        botonesGrid.add(btnLogout, 0, 2);
        botonesGrid.add(btnSalir, 1, 2);

        btnJugar.setOnAction(e -> mostrarSelectorDificultad());
        btnCargar.setOnAction(e -> cargarJuego());
        btnAnales.setOnAction(e -> mostrarAnales());
        btnInstrucciones.setOnAction(e -> mostrarInstruccionesDesdeMenu());
        btnLogout.setOnAction(e -> cerrarSesion());
        btnSalir.setOnAction(e -> javafx.application.Platform.exit());

        layout.getChildren().addAll(bienvenido, botonesGrid);

        // Control de audio
        ControladorAudioUI audioUI = new ControladorAudioUI();

        AnchorPane wrapper = new AnchorPane();
        wrapper.getChildren().addAll(layout, audioUI);

        AnchorPane.setTopAnchor(layout, 0.0);
        AnchorPane.setBottomAnchor(layout, 0.0);
        AnchorPane.setLeftAnchor(layout, 0.0);
        AnchorPane.setRightAnchor(layout, 0.0);

        AnchorPane.setTopAnchor(audioUI, 20.0);
        AnchorPane.setRightAnchor(audioUI, 20.0);

        root.setCenter(wrapper);
    }

    /**
     * Despliega la interfaz de selección de parámetros para una nueva partida.
     */
    private void mostrarSelectorDificultad() {
        GestorAudio.getInstancia().reproducirMusica("principal.mp3");
        VBox layout = new VBox(25);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(40));

        // Control de audio
        ControladorAudioUI audioUI = new ControladorAudioUI();

        // Aplicar fondo de imagen fondo2.jpg
        try {
            Image fondoImg = new Image(getClass().getResourceAsStream("/imagenes/fondo2.jpg"));
            BackgroundImage bgImg = new BackgroundImage(
                    fondoImg,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER,
                    new BackgroundSize(100, 100, true, true, false, true));
            layout.setBackground(new Background(bgImg));
        } catch (Exception e) {
            layout.setStyle("-fx-background-color: linear-gradient(to bottom, #332b1a, #1a150a);");
            System.err.println("Error cargando fondo2.jpg en selector: " + e.getMessage());
        }

        Label lbl = new Label("Selecciona Dificultad");
        lbl.setStyle(
                "-fx-font-family: 'Papyrus', 'Copperplate', serif; -fx-text-fill: white; -fx-font-size: 32px; -fx-font-weight: bold; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.8), 10, 0, 0, 3);");

        ComboBox<String> combo = new ComboBox<>();
        combo.getItems().addAll("Fácil", "Media", "Difícil");
        combo.setValue("Media");
        combo.setStyle(
                "-fx-font-family: 'Papyrus', 'Copperplate', serif; -fx-background-color: rgba(255, 255, 255, 0.9); -fx-text-fill: #1a150a; -fx-border-color: #DAA520; -fx-font-size: 16px; -fx-pref-width: 300;");

        CheckBox checkNiebla = new CheckBox("Activar Niebla de Guerra");
        checkNiebla.setStyle(
                "-fx-font-family: 'Papyrus', 'Copperplate', serif; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16px;");
        checkNiebla.setSelected(true);

        // Grid para botones
        GridPane botonesGrid = new GridPane();
        botonesGrid.setAlignment(Pos.CENTER);
        botonesGrid.setHgap(20);
        botonesGrid.setVgap(15);
        botonesGrid.setPadding(new Insets(20, 0, 0, 0));

        Button btnIniciar = new Button("Comenzar");
        estilizarBotonConFondo(btnIniciar, "entrada");
        btnIniciar.setPrefSize(250, 60);

        Button btnVolver = new Button("Volver");
        estilizarBotonConFondo(btnVolver, "salida");
        btnVolver.setPrefSize(250, 60);

        botonesGrid.add(btnIniciar, 0, 0);
        botonesGrid.add(btnVolver, 1, 0);

        btnIniciar.setOnAction(e -> {
            String diff = combo.getValue();
            boolean niebla = checkNiebla.isSelected();
            iniciarJuego(diff, niebla);
        });

        btnVolver.setOnAction(e -> mostrarMenuPrincipal());

        layout.getChildren().addAll(lbl, combo, checkNiebla, botonesGrid);

        AnchorPane wrapper = new AnchorPane();
        wrapper.getChildren().addAll(layout, audioUI);

        AnchorPane.setTopAnchor(layout, 0.0);
        AnchorPane.setBottomAnchor(layout, 0.0);
        AnchorPane.setLeftAnchor(layout, 0.0);
        AnchorPane.setRightAnchor(layout, 0.0);

        AnchorPane.setTopAnchor(audioUI, 20.0);
        AnchorPane.setRightAnchor(audioUI, 20.0);

        root.setCenter(wrapper);
    }

    /**
     * Ejecuta la lógica de generación del laberinto.
     */
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

            controladorJuego.iniciarNuevoJuego(filas, cols, usuarioActual.getEmail(), niebla);

            mostrarVistaJuego();

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
            mostrarAlerta("NO HAY PARTIDA GUARDADA",
                    "No tienes ninguna partida guardada. Primero guarda una partida desde el menú de pausa.");
        }
    }

    private void mostrarVistaJuego() {
        GestorAudio.getInstancia().reproducirMusica("juego.mp3");
        VistaJuego vistaJuego = new VistaJuego(
                controladorJuego,
                (stats) -> mostrarEstadisticasPartida(stats),
                () -> mostrarMenuPrincipal());
        root.setCenter(vistaJuego);
        vistaJuego.requestFocus();
    }

    private void mostrarEstadisticasPartida(Main.modelo.Dominio.EstadisticasJuego stats) {
        String track = stats.isGanado() ? "victoria.mp3" : "derrota.mp3";
        GestorAudio.getInstancia().reproducirMusica(track);

        VentanaEstadisticasPartida ventanaStats = new VentanaEstadisticasPartida(stats, () -> mostrarMenuPrincipal());
        root.setCenter(ventanaStats);
    }

    private void mostrarAnales() {
        GestorAudio.getInstancia().reproducirMusica("estadisticas.mp3");
        java.util.List<Main.modelo.Dominio.EstadisticasJuego> stats = controladorJuego
                .obtenerEstadisticas(usuarioActual.getEmail());
        VistaAnales vistaAnales = new VistaAnales(stats, () -> mostrarMenuPrincipal());
        root.setCenter(vistaAnales);
    }

    private void cerrarSesion() {
        this.usuarioActual = null;
        mostrarPantallaLogin();
    }

    /**
     * Estiliza un botón con imagen de fondo boton2.jpg usando CSS.
     * 
     * @param btn  El botón a estilizar
     * @param tipo El tipo de botón (no usado, todos usan boton2.jpg)
     */
    private void estilizarBotonConFondo(Button btn, String tipo) {
        // Ruta de la imagen para CSS
        String imagePath = "/imagenes/boton2.jpg";

        // Estilo base con imagen de fondo y tipografía jungle
        String baseStyle = "-fx-background-image: url('" + imagePath + "'); " +
                "-fx-background-size: 100% 100%; " +
                "-fx-background-repeat: no-repeat; " +
                "-fx-background-position: center; " +
                "-fx-font-family: 'Papyrus', 'Copperplate', serif; " +
                "-fx-font-size: 22px; " +
                "-fx-font-weight: bold; " +
                "-fx-text-fill: white; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.8), 10, 0, 0, 3); " +
                "-fx-cursor: hand; " +
                "-fx-border-color: rgba(218, 165, 32, 0.7); " +
                "-fx-border-width: 2; " +
                "-fx-border-radius: 8; " +
                "-fx-background-radius: 8; " +
                "-fx-padding: 10 20 10 20;";

        // Estilo hover: mantiene la imagen pero aumenta el brillo/borde
        String hoverStyle = "-fx-background-image: url('" + imagePath + "'); " +
                "-fx-background-size: 100% 100%; " +
                "-fx-background-repeat: no-repeat; " +
                "-fx-background-position: center; " +
                "-fx-font-family: 'Papyrus', 'Copperplate', serif; " +
                "-fx-font-size: 24px; " +
                "-fx-font-weight: bold; " +
                "-fx-text-fill: #FFD700; " +
                "-fx-effect: dropshadow(gaussian, rgba(255,215,0,0.9), 15, 0, 0, 4); " +
                "-fx-cursor: hand; " +
                "-fx-border-color: #FFD700; " +
                "-fx-border-width: 3; " +
                "-fx-border-radius: 8; " +
                "-fx-background-radius: 8; " +
                "-fx-padding: 10 20 10 20; " +
                "-fx-scale-x: 1.05; " +
                "-fx-scale-y: 1.05;";

        // Aplicar estilo inicial
        btn.setStyle(baseStyle);

        // Eventos de ratón
        btn.setOnMouseEntered(e -> btn.setStyle(hoverStyle));
        btn.setOnMouseExited(e -> btn.setStyle(baseStyle));
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        javafx.stage.Stage stage = (javafx.stage.Stage) root.getScene().getWindow();
        new VentanaAlertaPersonalizada(stage, titulo, mensaje, VentanaAlertaPersonalizada.Tipo.INFO).showAndWait();
    }
}