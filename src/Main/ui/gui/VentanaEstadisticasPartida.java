package Main.ui.gui;

import Main.modelo.Dominio.EstadisticasJuego;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

/**
 * Vista que muestra las estadísticas de la partida recién finalizada.
 * <p>
 * Esta clase rediseñada presenta un resumen visual impactante de la aventura,
 * utilizando un diseño de tablero (dashboard) en dos columnas con tipografía
 * de gran tamaño y elementos visuales temáticos.
 * </p>
 *
 * @author Mario Sanchez
 * @version 1.5
 * @since 07/01/26
 */
public class VentanaEstadisticasPartida extends BorderPane {

    private static final String BACKGROUND_IMAGE_PATH = "/imagenes/fondo4.jpg";
    private static final String BUTTON_IMAGE_PATH = "/imagenes/boton2.jpg";

    /**
     * Construye la vista de estadísticas de la partida.
     *
     * @param estadisticas El objeto que contiene las métricas de la partida
     *                     finalizada.
     * @param onVolver     Callback que se ejecuta al presionar el botón de retorno
     *                     al menú.
     */
    public VentanaEstadisticasPartida(EstadisticasJuego estadisticas, Runnable onVolver) {
        inicializarGUI(estadisticas, onVolver);
    }

    /**
     * Inicializa la interfaz gráfica de usuario, configurando el fondo, el título,
     * el tablero de estadísticas y el botón de navegación.
     *
     * @param estadisticas Datos de la partida para poblar la vista.
     * @param onVolver     Acción para el botón de volver.
     */
    private void inicializarGUI(EstadisticasJuego estadisticas, Runnable onVolver) {
        // Cargar Fondo
        try {
            Image fondoImg = new Image(getClass().getResourceAsStream(BACKGROUND_IMAGE_PATH));
            BackgroundImage bgImg = new BackgroundImage(
                    fondoImg,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER,
                    new BackgroundSize(100, 100, true, true, false, true));
            this.setBackground(new Background(bgImg));
        } catch (Exception e) {
            this.setStyle("-fx-background-color: #1a150a;");
        }
        this.setPadding(new Insets(40));

        // Título
        Label titulo = new Label("RESUMEN DE LA AVENTURA");
        titulo.setStyle(
                "-fx-font-family: 'Papyrus', 'Copperplate', serif; " +
                        "-fx-font-size: 48px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-text-fill: #FFD700; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.9), 15, 0, 0, 5);");
        BorderPane.setAlignment(titulo, Pos.CENTER);
        this.setTop(titulo);

        // Contenedor de Estadísticas (GridPane)
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(100); // Espacio entre columnas
        grid.setVgap(30); // Espacio entre filas
        grid.setPadding(new Insets(40));
        grid.setStyle(
                "-fx-background-color: rgba(0, 0, 0, 0.6); -fx-background-radius: 20; -fx-border-color: #DAA520; -fx-border-width: 3; -fx-border-radius: 20;");

        // --- Columna 1: Información General ---
        int row = 0;
        agregarFilaEstadistica(grid, "Estado:", estadisticas.isGanado() ? "VICTORIA" : "DERROTA",
                estadisticas.isGanado() ? "#2ecc71" : "#e74c3c", row++, 0);

        long segundos = estadisticas.getTiempoSegundos();
        String tiempoStr = String.format("%02d:%02d", segundos / 60, segundos % 60);
        agregarFilaEstadistica(grid, "Tiempo ⏱️:", tiempoStr, "#FFFFFF", row++, 0);

        agregarFilaEstadistica(grid, "Tamaño 📏:", estadisticas.getTamanioLaberinto(), "#FFFFFF", row++, 0);
        agregarFilaEstadistica(grid, "Niebla  :", estadisticas.isNieblaDeGuerra() ? "SÍ" : "NO", "#FFFFFF", row++, 0);
        agregarFilaEstadistica(grid, "Fecha 📅:", estadisticas.getFechaFormateada().split(" ")[0], "#FFFFFF", row++, 0);

        // --- Columna 2: Rendimiento ---
        row = 0;
        agregarFilaEstadistica(grid, "Cristales 💎:", String.valueOf(estadisticas.getCristalesRecolectados()),
                "#00FBFF", row++, 1);
        agregarFilaEstadistica(grid, "Bombas 💣:", String.valueOf(estadisticas.getBombasRecolectadas()), "#FFFFFF",
                row++, 1);
        agregarFilaEstadistica(grid, "Fósforos 🔥:", String.valueOf(estadisticas.getFosforosUsados()), "#FF4500", row++,
                1);
        agregarFilaEstadistica(grid, "Muros 💥:", String.valueOf(estadisticas.getMurosDestruidos()), "#FFFFFF", row++,
                1);
        agregarFilaEstadistica(grid, "Trampas 💀:", String.valueOf(estadisticas.getTrampasActivadas()), "#FF0000",
                row++, 1);

        this.setCenter(grid);

        // Botón Volver
        Button btnVolver = new Button("VOLVER AL MENÚ");
        estilizarBoton(btnVolver);
        btnVolver.setOnAction(e -> onVolver.run());

        HBox bottomBox = new HBox(btnVolver);
        bottomBox.setAlignment(Pos.CENTER);
        bottomBox.setPadding(new Insets(40, 0, 0, 0));
        this.setBottom(bottomBox);
    }

    /**
     * Crea y añade una fila de estadística al tablero, compuesta por una etiqueta
     * descriptiva y su valor correspondiente con un color específico.
     *
     * @param grid       El contenedor GridPane donde se añadirá la fila.
     * @param labelText  El texto descriptivo de la estadística.
     * @param valueText  El valor de la estadística.
     * @param valueColor El color hexadecimal para el texto del valor.
     * @param row        La fila del grid.
     * @param col        La columna del grid.
     */
    private void agregarFilaEstadistica(GridPane grid, String labelText, String valueText, String valueColor, int row,
            int col) {
        Label lbl = new Label(labelText);
        lbl.setStyle(
                "-fx-font-family: 'Papyrus', 'Copperplate', serif; -fx-font-size: 28px; -fx-text-fill: #DAA520; -fx-font-weight: bold;");

        Label val = new Label(valueText);
        val.setStyle("-fx-font-family: 'Papyrus', 'Copperplate', serif; -fx-font-size: 28px; -fx-text-fill: "
                + valueColor + "; -fx-font-weight: bold;");

        HBox box = new HBox(15, lbl, val);
        box.setAlignment(Pos.CENTER_LEFT);
        grid.add(box, col, row);
    }

    /**
     * Aplica el estilo visual temático al botón de retorno, incluyendo la imagen
     * de fondo, la tipografía y los efectos de iluminación al pasar el ratón.
     *
     * @param btn El botón a estilizar.
     */
    private void estilizarBoton(Button btn) {
        String baseStyle = "-fx-background-image: url('" + BUTTON_IMAGE_PATH + "'); " +
                "-fx-background-size: 100% 100%; " +
                "-fx-background-repeat: no-repeat; " +
                "-fx-background-position: center; " +
                "-fx-font-family: 'Papyrus', 'Copperplate', serif; " +
                "-fx-font-size: 28px; " +
                "-fx-font-weight: bold; " +
                "-fx-text-fill: white; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.8), 10, 0, 0, 3); " +
                "-fx-cursor: hand; " +
                "-fx-border-color: #DAA520; " +
                "-fx-border-width: 3; " +
                "-fx-border-radius: 10; " +
                "-fx-background-radius: 10; " +
                "-fx-padding: 15 40 15 40; " +
                "-fx-min-width: 300px;";

        String hoverStyle = baseStyle
                + "-fx-text-fill: #FFD700; -fx-scale-x: 1.1; -fx-scale-y: 1.1; -fx-border-color: #FFD700;";

        btn.setStyle(baseStyle);
        btn.setOnMouseEntered(e -> btn.setStyle(hoverStyle));
        btn.setOnMouseExited(e -> btn.setStyle(baseStyle));
    }
}
