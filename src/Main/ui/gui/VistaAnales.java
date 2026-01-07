package Main.ui.gui;

import Main.modelo.Dominio.EstadisticasJuego;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.image.Image;

import java.util.List;

/**
 * Representa la interfaz gráfica de los "Anales del Templo", donde se visualiza
 * el historial de partidas del usuario.
 * <p>
 * Esta clase extiende de {@link BorderPane} y utiliza un {@link TableView} para
 * presentar de forma tabular métricas como el tiempo, cristales recolectados,
 * recursos usados y el resultado final (Victoria/Derrota).
 * </p>
 * * @author Mario Sanchez
 * 
 * @version 1.2
 * @since 22/12/25
 */
public class VistaAnales extends BorderPane {

    /** Acción a ejecutar para regresar al menú principal. */
    private Runnable onBack;

    /**
     * Construye una nueva vista de estadísticas.
     * * @param estadisticas Lista de objetos {@link EstadisticasJuego} a mostrar.
     * 
     * @param onBack Callback para la navegación de retorno.
     */
    public VistaAnales(List<EstadisticasJuego> estadisticas, Runnable onBack) {
        this.onBack = onBack;
        inicializarGUI(estadisticas);
    }

    /**
     * Configura los componentes visuales, estilos CSS y vincula los datos a la
     * tabla.
     * <p>
     * Se define una columna por cada atributo relevante de la partida, incluyendo
     * fábricas de valores de propiedad (PropertyValueFactory) para mapear
     * automáticamente los datos del modelo a la vista.
     * </p>
     * * @param estadisticas Datos históricos para poblar la tabla.
     */
    private void inicializarGUI(List<EstadisticasJuego> estadisticas) {
        // Aplicar fondo de imagen
        try {
            Image fondoImg = new Image(getClass().getResourceAsStream("/imagenes/fondo4.jpg"));
            BackgroundImage bgImg = new BackgroundImage(
                    fondoImg,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER,
                    new BackgroundSize(100, 100, true, true, false, true));
            this.setBackground(new Background(bgImg));
        } catch (Exception e) {
            // Fallback a gradiente si no se encuentra la imagen
            this.setStyle("-fx-background-color: linear-gradient(to bottom, #2a1a0a, #1a0a00);");
            System.err.println("Error cargando fondo4.jpg: " + e.getMessage());
        }
        this.setPadding(new Insets(20));

        // Título de la sección con tipografía jungle
        Label title = new Label("ANALES DEL TEMPLO");
        title.setStyle(
                "-fx-font-family: 'Papyrus', 'Copperplate', serif; -fx-font-size: 36px; -fx-font-weight: bold; -fx-text-fill: white; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.9), 12, 0, 0, 4);");
        BorderPane.setAlignment(title, Pos.CENTER);
        this.setTop(title);

        // Configuración de la tabla de datos
        TableView<EstadisticasJuego> table = new TableView<>();
        table.setStyle("-fx-background-color: rgba(26, 21, 10, 0.8); " +
                "-fx-control-inner-background: rgba(26, 21, 10, 0.8); " +
                "-fx-text-fill: white; " +
                "-fx-border-color: #DAA520; " +
                "-fx-border-width: 2; " +
                "-fx-font-family: 'Papyrus', 'Copperplate', serif; " +
                "-fx-font-size: 13px;");

        // Columna: Fecha
        TableColumn<EstadisticasJuego, String> colFecha = new TableColumn<>("Fecha");
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fechaFormateada"));
        colFecha.setPrefWidth(150);

        // Columna: Resultado (Personalizada para mostrar GANADO/PERDIDO)
        TableColumn<EstadisticasJuego, String> colResultado = new TableColumn<>("Resultado");
        colResultado.setCellValueFactory(cellData -> {
            boolean ganado = cellData.getValue().isGanado();
            return new javafx.beans.property.SimpleStringProperty(ganado ? "GANADO" : "PERDIDO");
        });
        colResultado.setPrefWidth(100);

        // Columna: Tiempo Transcurrido
        TableColumn<EstadisticasJuego, String> colTiempo = new TableColumn<>("Tiempo ⏱️");
        colTiempo.setCellValueFactory(cellData -> {
            long segundos = cellData.getValue().getTiempoSegundos();
            long minutos = segundos / 60;
            long segs = segundos % 60;
            return new javafx.beans.property.SimpleStringProperty(String.format("%02d:%02d", minutos, segs));
        });
        colTiempo.setPrefWidth(100);

        // Columnas de recursos (Cristales, Bombas, Fósforos, Muros destruidos)
        TableColumn<EstadisticasJuego, Integer> colCristales = new TableColumn<>("Cristales 💎");
        colCristales.setCellValueFactory(new PropertyValueFactory<>("cristalesRecolectados"));
        colCristales.setPrefWidth(100);

        TableColumn<EstadisticasJuego, Integer> colBombas = new TableColumn<>("Bombas 💣");
        colBombas.setCellValueFactory(new PropertyValueFactory<>("bombasRecolectadas"));
        colBombas.setPrefWidth(100);

        TableColumn<EstadisticasJuego, Integer> colFosforos = new TableColumn<>("Fósforos 🔥");
        colFosforos.setCellValueFactory(new PropertyValueFactory<>("fosforosUsados"));
        colFosforos.setPrefWidth(100);

        TableColumn<EstadisticasJuego, Integer> colMuros = new TableColumn<>("Muros 💥");
        colMuros.setCellValueFactory(new PropertyValueFactory<>("murosDestruidos"));
        colMuros.setPrefWidth(100);

        // Columna: Dimensiones del laberinto
        TableColumn<EstadisticasJuego, String> colTamanio = new TableColumn<>("Tamaño");
        colTamanio.setCellValueFactory(new PropertyValueFactory<>("tamanioLaberinto"));

        // Columna: Niebla de Guerra
        TableColumn<EstadisticasJuego, String> colNiebla = new TableColumn<>("Niebla");
        colNiebla.setCellValueFactory(cellData -> {
            boolean niebla = cellData.getValue().isNieblaDeGuerra();
            return new javafx.beans.property.SimpleStringProperty(niebla ? "SI" : "NO");
        });
        colNiebla.setPrefWidth(80);

        // Agregación de columnas a la tabla
        @SuppressWarnings("unchecked")
        TableColumn<EstadisticasJuego, ?>[] columns = new TableColumn[] {
                colFecha, colResultado, colTiempo, colCristales, colBombas, colFosforos, colMuros, colTamanio, colNiebla
        };
        table.getColumns().addAll(columns);

        // Carga de datos observables
        ObservableList<EstadisticasJuego> data = FXCollections.observableArrayList(estadisticas);
        table.setItems(data);

        this.setCenter(table);

        // Botón de navegación para regresar al menú
        Button btnVolver = new Button("Volver");
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
                "-fx-padding: 10 20 10 20; " +
                "-fx-min-width: 200px;";

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
                "-fx-scale-y: 1.05; " +
                "-fx-min-width: 200px;";

        btnVolver.setStyle(baseStyle);
        btnVolver.setOnMouseEntered(e -> btnVolver.setStyle(hoverStyle));
        btnVolver.setOnMouseExited(e -> btnVolver.setStyle(baseStyle));

        btnVolver.setOnAction(e -> onBack.run());

        HBox bottom = new HBox(btnVolver);
        bottom.setAlignment(Pos.CENTER);
        bottom.setPadding(new Insets(20, 0, 0, 0));
        this.setBottom(bottom);
    }
}