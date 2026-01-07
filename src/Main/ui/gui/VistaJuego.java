package Main.ui.gui;

import Main.controlador.ControladorJuego;
import Main.modelo.Constantes.Direccion;
import Main.modelo.Constantes.EstadoJuego;
import Main.modelo.Dominio.Celda;
import Main.modelo.Dominio.Juego;
import Main.modelo.Dominio.Jugador;
import Main.modelo.Dominio.Laberinto;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;

import javafx.scene.paint.Color;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.function.Consumer;
import Main.modelo.Dominio.EstadisticasJuego;
import Main.modelo.Transferencia.ResultadoJuego;

import java.util.HashMap;
import java.util.Map;

/**
 * Representa la interfaz visual del entorno de juego activo para Maze Hunter.
 * <p>
 * Gestiona el renderizado 2D mediante un {@link Canvas}, el sistema de HUD
 * dinámico,
 * el procesamiento de eventos de teclado y el ciclo de vida visual de la
 * partida.
 * </p>
 *
 * @author Mario Sanchez
 * @version 1.2
 * @since 22/12/25
 */
public class VistaJuego extends BorderPane {

    private ControladorJuego controlador;
    private Canvas canvas;
    private GraphicsContext gc;
    private Map<String, Image> imagenes;
    private Consumer<EstadisticasJuego> onJuegoTerminado;
    private Runnable onSalir;
    private boolean niebla = true;

    private Label lblVida;
    private ProgressBar pbVida;
    private Label lblCristales;
    private Label lblBombas;
    private Label lblFosforos;
    private Label lblLlaves;
    private Label lblTiempo;
    private Timeline timer;

    private static final int TILE_SIZE = 32;

    /**
     * Construye la vista del juego, carga los recursos gráficos e inicializa los
     * componentes del HUD.
     *
     * @param controlador      El controlador de lógica del juego.
     * @param onJuegoTerminado Callback que recibe las estadísticas al finalizar la
     *                         partida.
     * @param onSalir          Callback para volver al menú principal (pausa o
     *                         salida).
     */
    public VistaJuego(ControladorJuego controlador, Consumer<EstadisticasJuego> onJuegoTerminado, Runnable onSalir) {
        this.controlador = controlador;
        this.onJuegoTerminado = onJuegoTerminado;
        this.onSalir = onSalir;
        this.niebla = controlador.getJuego().isNieblaDeGuerra();
        cargarImagenes();
        inicializarGUI();
        dibujar();

        this.setFocusTraversable(true);
        this.setOnKeyPressed(this::manejarTeclado);
        inicializarTimer();
    }

    /**
     * Inicializa el cronómetro de la interfaz para actualizar el tiempo
     * transcurrido cada segundo.
     */
    /**
     * Inicializa el cronómetro de la interfaz para actualizar el tiempo
     * transcurrido cada segundo.
     */
    private void inicializarTimer() {
        // Restaurar tiempo si es un juego cargado
        long tiempoGuardado = controlador.getJuego().getTiempoJugadoSegundos();
        if (tiempoGuardado > 0) {
            // Ajustar el inicio del juego para reflejar el tiempo ya jugado
            LocalDateTime nuevoInicio = LocalDateTime.now().minusSeconds(tiempoGuardado);
            controlador.getJuego().setInicio(nuevoInicio);
        }

        timer = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            actualizarHUD();
            // Actualizar tiempo en el objeto juego en tiempo real (opcional, pero seguro)
            long segundos = ChronoUnit.SECONDS.between(controlador.getJuego().getInicio(), LocalDateTime.now());
            controlador.getJuego().setTiempoJugadoSegundos(segundos);
        }));
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();
    }

    /**
     * Activa o desactiva la capa visual de niebla de guerra.
     *
     * @param niebla true para activar la niebla, false para ver el mapa completo.
     */
    public void setNiebla(boolean niebla) {
        this.niebla = niebla;
        dibujar();
    }

    /**
     * Carga las imágenes necesarias para el renderizado desde el directorio de
     * recursos.
     */
    private void cargarImagenes() {
        imagenes = new HashMap<>();
        String[] nombres = { "jugador", "muro", "muro_rojo", "suelo", "cristal", "bomba",
                "llave", "fosforo", "salida", "trampa", "energia", "vida", "niebla" };

        for (String nombre : nombres) {
            try {
                String path = "/imagenes/" + nombre + ".png";
                imagenes.put(nombre, new Image(getClass().getResourceAsStream(path)));
            } catch (Exception e) {
                System.err.println("Error cargando imagen: " + nombre);
            }
        }
    }

    /**
     * Configura la disposición de los elementos gráficos (HUD superior, inferior y
     * canvas central).
     */
    private void inicializarGUI() {
        // HUD Superior
        HBox hud = new HBox(20);
        hud.setStyle("-fx-background-color: linear-gradient(to bottom, #332b1a, #1a150a); " +
                "-fx-padding: 10; " +
                "-fx-alignment: center-left; " +
                "-fx-border-color: #DAA520; " +
                "-fx-border-width: 0 0 2 0;");

        lblVida = crearLabelHUD("Vida: 100%");
        pbVida = new ProgressBar(1.0);
        pbVida.setPrefWidth(100);
        pbVida.setStyle("-fx-accent: #e74c3c;");

        lblCristales = crearLabelHUD("💎 0");
        lblBombas = crearLabelHUD("💣 0");
        lblFosforos = crearLabelHUD("🔥 0");
        lblLlaves = crearLabelHUD("🔑 No");
        lblTiempo = crearLabelHUD("⏱️ 00:00");

        // Espaciador para empujar el botón a la derecha
        javafx.scene.layout.Region spacer = new javafx.scene.layout.Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button btnMenu = new Button("⚙️ Menú");
        btnMenu.setStyle("-fx-background-color: linear-gradient(to bottom, #555, #222); " +
                "-fx-text-fill: #DAA520; " +
                "-fx-font-weight: bold; " +
                "-fx-border-color: #DAA520; " +
                "-fx-cursor: hand;");
        btnMenu.setOnMouseEntered(e -> btnMenu.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #777, #444); -fx-text-fill: #FFD700; -fx-font-weight: bold; -fx-border-color: #FFD700; -fx-cursor: hand;"));
        btnMenu.setOnMouseExited(e -> btnMenu.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #555, #222); -fx-text-fill: #DAA520; -fx-font-weight: bold; -fx-border-color: #DAA520; -fx-cursor: hand;"));
        btnMenu.setFocusTraversable(false); // Para no robar foco del teclado
        btnMenu.setOnAction(e -> mostrarMenuPausa());

        hud.getChildren().addAll(lblVida, pbVida, lblCristales, lblBombas, lblFosforos, lblLlaves, lblTiempo, spacer,
                btnMenu);
        this.setTop(hud);

        // HUD Inferior (Instrucciones)
        HBox instructionsHud = new HBox(30);
        instructionsHud.setStyle("-fx-background-color: linear-gradient(to top, #332b1a, #1a150a); " +
                "-fx-padding: 8; " +
                "-fx-alignment: center; " +
                "-fx-border-color: #DAA520; " +
                "-fx-border-width: 2 0 0 0;");
        Label lblMov = crearLabelHUD("🎮 WASD: Mover");
        Label lblBomba = crearLabelHUD("💣 K: Activar Bomba");
        Label lblEsc = crearLabelHUD("⚙️ ESC: Pausa");
        instructionsHud.getChildren().addAll(lblMov, lblBomba, lblEsc);
        this.setBottom(instructionsHud);

        // Canvas Central con ScrollPane
        Laberinto lab = controlador.getJuego().getLaberinto();
        canvas = new Canvas(lab.getColumnas() * TILE_SIZE, lab.getFilas() * TILE_SIZE);
        gc = canvas.getGraphicsContext2D();

        // StackPane para centrar el canvas si es más pequeño que la ventana
        StackPane canvasHolder = new StackPane(canvas);
        canvasHolder.setStyle("-fx-background-color: #111;");

        ScrollPane scrollPane = new ScrollPane(canvasHolder);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setStyle("-fx-background: #111; -fx-border-color: #111;");

        // Asegurar que el canvas tenga foco al hacer click
        canvas.setOnMouseClicked(e -> this.requestFocus());

        this.setCenter(scrollPane);
    }

    /**
     * Despliega un diálogo de confirmación que permite guardar la partida o salir
     * al menú.
     */
    private void mostrarMenuPausa() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Menú de Pausa");
        alert.setHeaderText("Juego Pausado");
        alert.setContentText("¿Qué deseas hacer?");

        ButtonType btnGuardar = new ButtonType("Guardar Partida");
        ButtonType btnSalir = new ButtonType("Salir al Menú");
        ButtonType btnCancelar = new ButtonType("Volver al Juego",
                javafx.scene.control.ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(btnGuardar, btnSalir, btnCancelar);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent()) {
            if (result.get() == btnGuardar) {
                // Actualizar tiempo antes de guardar
                long segundos = ChronoUnit.SECONDS.between(controlador.getJuego().getInicio(), LocalDateTime.now());
                controlador.getJuego().setTiempoJugadoSegundos(segundos);

                if (controlador.guardarJuego(controlador.getJuego())) {
                    mostrarMensajeInfo("Guardado", "Partida guardada exitosamente.");
                } else {
                    mostrarMensajeInfo("Error", "No se pudo guardar la partida.");
                }
            } else if (result.get() == btnSalir) {
                // Actualizar tiempo antes de salir
                long segundos = ChronoUnit.SECONDS.between(controlador.getJuego().getInicio(), LocalDateTime.now());
                controlador.getJuego().setTiempoJugadoSegundos(segundos);

                // NO guardar estadísticas parciales al salir para evitar "PERDIDO"
                // controlador.guardarEstadisticasParciales(controlador.getJuego());
                onSalir.run();
            }
        }
    }

    /**
     * Muestra un mensaje informativo al usuario.
     *
     * @param titulo  El título del diálogo.
     * @param mensaje El contenido del mensaje.
     */
    private void mostrarMensajeInfo(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    /**
     * Crea un objeto {@link Label} estilizado para el HUD.
     *
     * @param text El texto inicial del label.
     * @return El label configurado.
     */
    private Label crearLabelHUD(String text) {
        Label l = new Label(text);
        l.setStyle("-fx-text-fill: #DAA520; -fx-font-weight: bold; -fx-font-size: 14px; -fx-font-family: 'Serif';");
        return l;
    }

    /**
     * Realiza el renderizado gráfico de todas las celdas del laberinto y el
     * jugador.
     * <p>
     * Se encarga de procesar la visibilidad (niebla) y las capas de dibujo (suelo y
     * objetos).
     * </p>
     */
    private void dibujar() {
        Juego juego = controlador.getJuego();
        Laberinto lab = juego.getLaberinto();
        Jugador jugador = juego.getJugador();

        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        for (int i = 0; i < lab.getFilas(); i++) {
            for (int j = 0; j < lab.getColumnas(); j++) {
                Celda celda = lab.getCelda(i, j);
                double x = j * TILE_SIZE;
                double y = i * TILE_SIZE;

                // Si hay niebla y no es visible, dibujar niebla
                if (niebla && !celda.isVisible()) {
                    gc.drawImage(imagenes.get("niebla"), x, y, TILE_SIZE, TILE_SIZE);
                    continue;
                }

                // Dibujar suelo base siempre
                gc.drawImage(imagenes.get("suelo"), x, y, TILE_SIZE, TILE_SIZE);

                // Dibujar contenido según tipo
                Image img = switch (celda.getTipo()) {
                    case MURO -> imagenes.get("muro");
                    case MURO_ROJO -> imagenes.get("muro_rojo");
                    case CRISTAL -> imagenes.get("cristal");
                    case BOMBA -> imagenes.get("bomba");
                    case LLAVE -> imagenes.get("llave");
                    case FOSFORO -> imagenes.get("fosforo");
                    case SALIDA -> imagenes.get("salida");
                    case TRAMPA -> imagenes.get("trampa"); // Podría ser invisible si no se ha pisado
                    case ENERGIA -> imagenes.get("energia");
                    case VIDA -> imagenes.get("vida");
                    default -> null; // Camino ya tiene suelo
                };

                if (img != null) {
                    gc.drawImage(img, x, y, TILE_SIZE, TILE_SIZE);
                }
            }
        }

        // Dibujar Jugador
        gc.drawImage(imagenes.get("jugador"),
                jugador.getPosY() * TILE_SIZE,
                jugador.getPosX() * TILE_SIZE,
                TILE_SIZE, TILE_SIZE);

        actualizarHUD();
    }

    /**
     * Sincroniza los elementos visuales del HUD con el estado actual del jugador y
     * el tiempo.
     */
    private void actualizarHUD() {
        Jugador j = controlador.getJuego().getJugador();
        lblVida.setText("Vida: " + j.getVida() + "%");
        pbVida.setProgress(j.getVida() / 100.0);
        lblCristales.setText("💎 " + j.getCristales());
        lblBombas.setText("💣 " + j.getBombas());
        lblFosforos.setText("🔥 " + j.getFosforos());
        lblLlaves.setText("🔑 " + (j.isTieneLlave() ? "Sí" : "No") + (j.isTieneLlave() ? " (Busca la SALIDA)" : ""));

        // Actualizar Tiempo
        LocalDateTime inicio = controlador.getJuego().getInicio();
        if (inicio != null) {
            long segundos = ChronoUnit.SECONDS.between(inicio, LocalDateTime.now());
            long min = segundos / 60;
            long seg = segundos % 60;
            lblTiempo.setText(String.format("⏱️ %02d:%02d", min, seg));
        }
    }

    /**
     * Gestiona las pulsaciones de teclas para el movimiento y uso de objetos.
     *
     * @param event El evento de teclado capturado.
     */
    private void manejarTeclado(KeyEvent event) {
        Juego juego = controlador.getJuego();
        if (juego.getEstado() != EstadoJuego.EN_CURSO)
            return;

        boolean movio = false;
        switch (event.getCode()) {
            case W, UP -> movio = controlador.moverJugador(juego, Direccion.ARRIBA);
            case S, DOWN -> movio = controlador.moverJugador(juego, Direccion.ABAJO);
            case A, LEFT -> movio = controlador.moverJugador(juego, Direccion.IZQUIERDA);
            case D, RIGHT -> movio = controlador.moverJugador(juego, Direccion.DERECHA);
            case K -> {
                if (controlador.activarExplosion(juego)) {
                    dibujar();
                }
            }
            case ESCAPE -> mostrarMenuPausa();
            default -> {
            }
        }

        if (movio) {
            dibujar();
            verificarFinJuego();
        }
    }

    /**
     * Verifica si el juego ha terminado (Victoria o Derrota) y realiza las acciones
     * pertinentes.
     */
    private void verificarFinJuego() {
        Juego juego = controlador.getJuego();
        if (juego.getEstado() == EstadoJuego.GANADO) {
            ResultadoJuego resultado = controlador.terminarJuego(juego);
            mostrarAlerta("¡VICTORIA!",
                    "Has escapado del templo con " + juego.getJugador().getCristales() + " cristales.");
            if (onJuegoTerminado != null) {
                onJuegoTerminado.accept(resultado.getEstadisticas());
            }
        } else if (juego.getEstado() == EstadoJuego.PERDIDO) {
            ResultadoJuego resultado = controlador.terminarJuego(juego);
            mostrarAlerta("DERROTA", "Has perdido en el laberinto.");
            if (onJuegoTerminado != null) {
                onJuegoTerminado.accept(resultado.getEstadisticas());
            }
        }
    }

    /**
     * Muestra un diálogo de alerta de tipo información.
     *
     * @param titulo  El título de la alerta.
     * @param mensaje El contenido del mensaje.
     */
    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}