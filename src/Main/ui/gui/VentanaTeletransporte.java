package Main.ui.gui;

import Main.modelo.Dominio.Celda;
import Main.modelo.Dominio.Laberinto;
import Main.modelo.Constantes.TipoCelda;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.HashMap;
import java.util.Map;

/**
 * Ventana emergente que muestra el laberinto completo y permite al jugador
 * seleccionar una celda de suelo para teletransportarse.
 * <p>
 * Esta ventana se activa cuando el jugador presiona la tecla 'L' con la llave
 * negra
 * en su inventario. Muestra únicamente muros y suelos para no revelar la
 * ubicación
 * de objetos, y permite seleccionar con el cursor una celda válida.
 * </p>
 * 
 * @author Mario Sanchez
 * @version 1.0
 * @since 15/01/2026
 */
public class VentanaTeletransporte extends Stage {

    /** Tamaño de cada celda en píxeles para el renderizado del mapa. */
    private static final int TILE_SIZE = 16;

    /** Almacena la posición seleccionada por el usuario [fila, columna]. */
    private int[] posicionSeleccionada = null;

    /** Canvas donde se dibuja el mapa del laberinto. */
    private Canvas canvas;

    /** Contexto gráfico del canvas. */
    private GraphicsContext gc;

    /** Referencia al laberinto del juego. */
    private Laberinto laberinto;

    /** Posición actual del jugador [fila, columna]. */
    private int[] posicionJugador;

    /** Coordenadas para permitir arrastrar la ventana. */
    private double xOffset = 0;
    private double yOffset = 0;

    /**
     * Label para mostrar las coordenadas de la celda sobre la que está el cursor.
     */
    private Label lblCoordenadas;

    /** Mapa de imágenes para renderizar el laberinto. */
    private Map<String, Image> imagenes;

    /** Posición actual del cursor [fila, columna]. */
    private int[] posicionCursor = null;

    /**
     * Constructor que crea la ventana de teletransporte.
     * 
     * @param owner          Ventana principal que será bloqueada mientras esta
     *                       ventana esté activa.
     * @param laberinto      El laberinto completo del juego.
     * @param filaJugador    Fila actual del jugador.
     * @param columnaJugador Columna actual del jugador.
     */
    public VentanaTeletransporte(Stage owner, Laberinto laberinto, int filaJugador, int columnaJugador) {
        this.laberinto = laberinto;
        this.posicionJugador = new int[] { filaJugador, columnaJugador };

        initOwner(owner);
        initModality(Modality.APPLICATION_MODAL);
        initStyle(StageStyle.UNDECORATED);

        BorderPane root = new BorderPane();
        root.setMinWidth(800);
        root.setMinHeight(600);
        root.setPrefWidth(800);
        root.setPrefHeight(600);

        // Permitir arrastrar la ventana
        root.setOnMousePressed(e -> {
            xOffset = e.getSceneX();
            yOffset = e.getSceneY();
        });
        root.setOnMouseDragged(e -> {
            setX(e.getScreenX() - xOffset);
            setY(e.getScreenY() - yOffset);
        });

        // Fondo con imagen de mazmorra
        try {
            Image bgImage = new Image(getClass().getResourceAsStream("/imagenes/mazmorra.png"));
            BackgroundImage backgroundImage = new BackgroundImage(
                    bgImage,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER,
                    new BackgroundSize(100, 100, true, true, false, true));
            root.setBackground(new Background(backgroundImage));
        } catch (Exception e) {
            root.setStyle("-fx-background-color: #1a150a;");
        }

        root.setStyle(root.getStyle() + "; -fx-border-color: #DAA520; -fx-border-width: 4; -fx-border-radius: 12;");

        // Título
        Label lblTitulo = new Label("TELETRANSPORTE MÁGICO");
        lblTitulo.setStyle("-fx-font-family: 'Papyrus', serif; -fx-font-size: 28px; -fx-font-weight: bold; " +
                "-fx-text-fill: #FFD700; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.9), 10, 0, 0, 3);");
        lblTitulo.setAlignment(Pos.CENTER);

        // Instrucciones
        Label lblInstrucciones = new Label(
                "Selecciona una celda de SUELO (gris claro) para teletransportarte.\n" +
                        "Las celdas de MURO (gris oscuro/rojo) no se pueden seleccionar.");
        lblInstrucciones.setWrapText(true);
        lblInstrucciones.setMaxWidth(750);
        lblInstrucciones.setAlignment(Pos.CENTER);
        lblInstrucciones.setStyle("-fx-font-family: 'Papyrus', serif; -fx-font-size: 14px; " +
                "-fx-text-fill: white; -fx-font-weight: bold;");

        // Label de coordenadas
        lblCoordenadas = new Label("Posición: ");
        lblCoordenadas.setStyle("-fx-font-family: 'Papyrus', serif; -fx-font-size: 12px; " +
                "-fx-text-fill: #DAA520; -fx-font-weight: bold;");

        VBox topBox = new VBox(10, lblTitulo, lblInstrucciones, lblCoordenadas);
        topBox.setAlignment(Pos.CENTER);
        topBox.setPadding(new Insets(20, 20, 10, 20));
        topBox.setStyle("-fx-background-color: rgba(26,21,10,0.85); -fx-background-radius: 8 8 0 0;");

        // Canvas para el mapa
        canvas = new Canvas(laberinto.getColumnas() * TILE_SIZE, laberinto.getFilas() * TILE_SIZE);
        gc = canvas.getGraphicsContext2D();

        // Cargar imágenes
        cargarImagenes();

        dibujarMapa();

        // Eventos del canvas
        canvas.setOnMouseMoved(e -> {
            int col = (int) (e.getX() / TILE_SIZE);
            int fila = (int) (e.getY() / TILE_SIZE);
            if (fila >= 0 && fila < laberinto.getFilas() && col >= 0 && col < laberinto.getColumnas()) {
                Celda celda = laberinto.getCelda(fila, col);
                String tipo = celda.isTransitable() ? "TRANSITABLE" : "BLOQUEADO";
                lblCoordenadas.setText(String.format("Posición: [%d, %d] - %s", fila, col, tipo));

                // Actualizar posición del cursor y redibujar
                posicionCursor = new int[] { fila, col };
                dibujarMapa();
            }
        });

        canvas.setOnMouseExited(e -> {
            // Limpiar posición del cursor cuando sale del canvas
            posicionCursor = null;
            dibujarMapa();
        });

        canvas.setOnMouseClicked(e -> {
            int col = (int) (e.getX() / TILE_SIZE);
            int fila = (int) (e.getY() / TILE_SIZE);

            if (fila >= 0 && fila < laberinto.getFilas() && col >= 0 && col < laberinto.getColumnas()) {
                Celda celda = laberinto.getCelda(fila, col);

                // Solo permitir seleccionar celdas transitables (suelo)
                if (celda.isTransitable() && celda.getTipo() == TipoCelda.CAMINO) {
                    posicionSeleccionada = new int[] { fila, col };
                    close();
                } else if (celda.isTransitable() &&
                        (celda.getTipo() == TipoCelda.ENTRADA ||
                                celda.getTipo() == TipoCelda.SALIDA ||
                                celda.getTipo() == TipoCelda.CRISTAL ||
                                celda.getTipo() == TipoCelda.BOMBA ||
                                celda.getTipo() == TipoCelda.LLAVE ||
                                celda.getTipo() == TipoCelda.FOSFORO ||
                                celda.getTipo() == TipoCelda.ENERGIA ||
                                celda.getTipo() == TipoCelda.VIDA ||
                                celda.getTipo() == TipoCelda.TRAMPA ||
                                celda.getTipo() == TipoCelda.LLAVE_NEGRA)) {
                    // Permitir también otras celdas transitables
                    posicionSeleccionada = new int[] { fila, col };
                    close();
                }
            }
        });

        // ScrollPane para el canvas
        StackPane canvasHolder = new StackPane(canvas);
        canvasHolder.setStyle("-fx-background-color: #111;");

        ScrollPane scrollPane = new ScrollPane(canvasHolder);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setStyle("-fx-background: #111; -fx-border-color: #111;");

        // Botón Cancelar
        Button btnCancelar = new Button("CANCELAR");
        estilizarBoton(btnCancelar);
        btnCancelar.setPrefWidth(250);
        btnCancelar.setOnAction(e -> {
            posicionSeleccionada = null;
            close();
        });

        HBox bottomBox = new HBox(btnCancelar);
        bottomBox.setAlignment(Pos.CENTER);
        bottomBox.setPadding(new Insets(15));
        bottomBox.setStyle("-fx-background-color: rgba(26,21,10,0.85); -fx-background-radius: 0 0 8 8;");

        root.setTop(topBox);
        root.setCenter(scrollPane);
        root.setBottom(bottomBox);

        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        this.setScene(scene);
    }

    /**
     * Carga las imágenes necesarias para renderizar el mapa.
     */
    private void cargarImagenes() {
        imagenes = new HashMap<>();
        String[] nombres = { "muro", "muro_rojo", "suelo", "portal", "jugador" };

        for (String nombre : nombres) {
            try {
                String file = switch (nombre) {
                    case "muro" -> "muro2.jpeg";
                    case "muro_rojo" -> "muro_rojo2.png";
                    case "suelo" -> "suelo2.png";
                    case "portal" -> "portal2.png";
                    case "jugador" -> "jugador2.png";
                    default -> nombre + ".png";
                };
                String path = "/imagenes/" + file;
                imagenes.put(nombre, new Image(getClass().getResourceAsStream(path)));
            } catch (Exception e) {
                System.err.println("Error cargando imagen: " + nombre);
            }
        }
    }

    /**
     * Dibuja el mapa del laberinto mostrando solo muros y suelos con texturas.
     */
    private void dibujarMapa() {
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        for (int i = 0; i < laberinto.getFilas(); i++) {
            for (int j = 0; j < laberinto.getColumnas(); j++) {
                Celda celda = laberinto.getCelda(i, j);
                double x = j * TILE_SIZE;
                double y = i * TILE_SIZE;

                // Dibujar según el tipo de celda con texturas
                Image imgCelda = null;
                if (celda.getTipo() == TipoCelda.MURO) {
                    imgCelda = imagenes.get("muro");
                } else if (celda.getTipo() == TipoCelda.MURO_ROJO) {
                    imgCelda = imagenes.get("muro_rojo");
                } else if (celda.isTransitable()) {
                    imgCelda = imagenes.get("suelo");
                }

                if (imgCelda != null) {
                    gc.drawImage(imgCelda, x, y, TILE_SIZE, TILE_SIZE);
                }

                // Si es la posición del cursor y es transitable, dibujar portal
                if (posicionCursor != null &&
                        posicionCursor[0] == i && posicionCursor[1] == j &&
                        celda.isTransitable()) {
                    Image imgPortal = imagenes.get("portal");
                    if (imgPortal != null) {
                        gc.drawImage(imgPortal, x, y, TILE_SIZE, TILE_SIZE);
                    }
                }
            }
        }

        // Marcar posición actual del jugador con su imagen
        double jugadorX = posicionJugador[1] * TILE_SIZE;
        double jugadorY = posicionJugador[0] * TILE_SIZE;
        Image jugadorImg = imagenes.get("jugador");
        if (jugadorImg != null) {
            gc.drawImage(jugadorImg, jugadorX, jugadorY, TILE_SIZE, TILE_SIZE);
        }
    }

    /**
     * Aplica estilo visual al botón.
     */
    private void estilizarBoton(Button btn) {
        String imagePath = getClass().getResource("/imagenes/boton2.jpg").toExternalForm();
        String baseStyle = "-fx-background-image: url('" + imagePath + "'); " +
                "-fx-background-size: 100% 100%; " +
                "-fx-text-fill: white; " +
                "-fx-font-family: 'Papyrus', serif; " +
                "-fx-font-size: 18px; " +
                "-fx-font-weight: bold; " +
                "-fx-cursor: hand; " +
                "-fx-border-color: #DAA520; " +
                "-fx-border-radius: 8; " +
                "-fx-padding: 12 30;";

        btn.setStyle(baseStyle);
        btn.setOnMouseEntered(e -> btn.setStyle(baseStyle + " -fx-text-fill: #FFD700; -fx-border-color: #FFD700;"));
        btn.setOnMouseExited(e -> btn.setStyle(baseStyle));
    }

    /**
     * Muestra la ventana y espera a que el usuario seleccione una posición o
     * cancele.
     * 
     * @return Array [fila, columna] si se seleccionó una posición válida, null si
     *         se canceló.
     */
    public int[] mostrarYObtenerPosicion() {
        showAndWait();
        return posicionSeleccionada;
    }
}
