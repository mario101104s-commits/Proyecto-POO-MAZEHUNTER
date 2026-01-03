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
 * @version 1.2
 * @since 22/12/25
 */
public class VistaAnales extends BorderPane {

    /** Acción a ejecutar para regresar al menú principal. */
    private Runnable onBack;

    /**
     * Construye una nueva vista de estadísticas.
     * * @param estadisticas Lista de objetos {@link EstadisticasJuego} a mostrar.
     * @param onBack       Callback para la navegación de retorno.
     */
    public VistaAnales(List<EstadisticasJuego> estadisticas, Runnable onBack) {
        this.onBack = onBack;
        inicializarGUI(estadisticas);
    }

    /**
     * Configura los componentes visuales, estilos CSS y vincula los datos a la tabla.
     * <p>
     * Se define una columna por cada atributo relevante de la partida, incluyendo
     * fábricas de valores de propiedad (PropertyValueFactory) para mapear
     * automáticamente los datos del modelo a la vista.
     * </p>
     * * @param estadisticas Datos históricos para poblar la tabla.
     */
    private void inicializarGUI(List<EstadisticasJuego> estadisticas) {
        // Estilo del contenedor principal con temática de templo
        this.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #332b1a, #1a150a); -fx-border-color: #DAA520; -fx-border-width: 3;");
        this.setPadding(new Insets(20));

        // Título de la sección
        Label title = new Label("📜 ANALES DEL TEMPLO");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: gold;");
        BorderPane.setAlignment(title, Pos.CENTER);
        this.setTop(title);

        // Configuración de la tabla de datos
        TableView<EstadisticasJuego> table = new TableView<>();
        table.setStyle("-fx-background-color: #1a150a; " +
                "-fx-control-inner-background: #1a150a; " +
                "-fx-text-fill: #DAA520; " +
                "-fx-border-color: #DAA520; " +
                "-fx-border-width: 1; " +
                "-fx-font-family: 'Serif';");

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

        // Columna: Tiempo
        TableColumn<EstadisticasJuego, Long> colTiempo = new TableColumn<>("Tiempo (s)");
        colTiempo.setCellValueFactory(new PropertyValueFactory<>("tiempoSegundos"));

        // Columnas de recursos (Cristales, Bombas, Fósforos, Muros destruidos)
        TableColumn<EstadisticasJuego, Integer> colCristales = new TableColumn<>("💎");
        colCristales.setCellValueFactory(new PropertyValueFactory<>("cristalesRecolectados"));

        TableColumn<EstadisticasJuego, Integer> colBombas = new TableColumn<>("💣");
        colBombas.setCellValueFactory(new PropertyValueFactory<>("bombasRecolectadas"));

        TableColumn<EstadisticasJuego, Integer> colFosforos = new TableColumn<>("🔥");
        colFosforos.setCellValueFactory(new PropertyValueFactory<>("fosforosUsados"));

        TableColumn<EstadisticasJuego, Integer> colMuros = new TableColumn<>("💥");
        colMuros.setCellValueFactory(new PropertyValueFactory<>("murosDestruidos"));

        // Columna: Dimensiones del laberinto
        TableColumn<EstadisticasJuego, String> colTamanio = new TableColumn<>("Tamaño");
        colTamanio.setCellValueFactory(new PropertyValueFactory<>("tamanioLaberinto"));

        // Agregación de columnas a la tabla
        @SuppressWarnings("unchecked")
        TableColumn<EstadisticasJuego, ?>[] columns = new TableColumn[] {
                colFecha, colResultado, colTiempo, colCristales, colBombas, colFosforos, colMuros, colTamanio
        };
        table.getColumns().addAll(columns);

        // Carga de datos observables
        ObservableList<EstadisticasJuego> data = FXCollections.observableArrayList(estadisticas);
        table.setItems(data);

        this.setCenter(table);

        // Botón de navegación para regresar al menú
        Button btnVolver = new Button("Volver");
        btnVolver.setStyle("-fx-background-color: linear-gradient(to bottom, #555, #222); " +
                "-fx-text-fill: #DAA520; " +
                "-fx-font-size: 14px; " +
                "-fx-font-weight: bold; " +
                "-fx-min-width: 150px; " +
                "-fx-border-color: #DAA520; " +
                "-fx-border-width: 1; " +
                "-fx-cursor: hand;");

        // Efectos dinámicos (Hover)
        btnVolver.setOnMouseEntered(e -> btnVolver.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #777, #444); -fx-text-fill: #FFD700; -fx-font-size: 14px; -fx-font-weight: bold; -fx-min-width: 150px; -fx-border-color: #FFD700; -fx-border-width: 2; -fx-cursor: hand;"));
        btnVolver.setOnMouseExited(e -> btnVolver.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #555, #222); -fx-text-fill: #DAA520; -fx-font-size: 14px; -fx-font-weight: bold; -fx-min-width: 150px; -fx-border-color: #DAA520; -fx-border-width: 1; -fx-cursor: hand;"));

        btnVolver.setOnAction(e -> onBack.run());

        HBox bottom = new HBox(btnVolver);
        bottom.setAlignment(Pos.CENTER);
        bottom.setPadding(new Insets(20, 0, 0, 0));
        this.setBottom(bottom);
    }
}