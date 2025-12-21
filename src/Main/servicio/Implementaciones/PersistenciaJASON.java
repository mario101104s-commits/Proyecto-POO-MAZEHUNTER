package Main.servicio.Implementaciones;

//Pasos para agregar Libreria GSON:
//File > Project Structure > Libraries
//Click + > From Maven
//Buscar: com.google.code.gson:gson:2.10.1
//Click OK para descargar y agregar

import Main.modelo.Constantes.EstadoJuego;
import Main.modelo.Constantes.TipoCelda;
import Main.modelo.Dominio.*;
import Main.servicio.Interfaces.Persistencia;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación concreta de la interfaz {@code Persistencia} utilizando
 * archivos JSON.
 * <p>
 * Utiliza la librería Google Gson para la serialización y deserialización de
 * objetos
 * de dominio (Usuario, Juego, EstadisticasJuego) y maneja la estructura de
 * directorios
 * para el almacenamiento de archivos.
 * </p>
 * 
 * @author Mario Sanchez y Niyerlin Muñoz
 * @version 1.0
 * @since 2025-11-15
 */

public class PersistenciaJASON implements Persistencia {
    /** El directorio base donde se almacenan todos los archivos de datos. */
    private static final String DIRECTORIO_BASE = "datos/";
    /** La ruta completa del archivo que almacena la lista de usuarios. */
    private static final String ARCHIVO_USUARIOS = DIRECTORIO_BASE + "usuarios.json";
    /**
     * El directorio donde se guardan los archivos de juegos guardados por usuario.
     */
    private static final String DIRECTORIO_JUEGOS = DIRECTORIO_BASE + "juegos/";
    /** El directorio donde se guardan los archivos de estadísticas por usuario. */
    private static final String DIRECTORIO_ESTADISTICAS = DIRECTORIO_BASE + "estadisticas/";
    /** Instancia de Gson configurada para manejar formatos y tipos específicos. */
    private Gson gson;

    /**
     * Constructor. Inicializa la instancia de Gson con formato bonito y el
     * adaptador
     * para manejar {@code LocalDateTime}. Llama al metodo para crear los
     * directorios necesarios.
     */
    public PersistenciaJASON() {
        // Configurar Gson para manejar LocalDateTime y para formato bonito
        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()) // ✅ REGISTRAR EL ADAPTER
                .create();

        // Crear directorios si no existen
        crearDirectorios();
    }

    /**
     * Crea los directorios base de datos si no existen previamente.
     */
    private void crearDirectorios() {
        try {
            Files.createDirectories(Paths.get(DIRECTORIO_BASE));
            Files.createDirectories(Paths.get(DIRECTORIO_JUEGOS));
            Files.createDirectories(Paths.get(DIRECTORIO_ESTADISTICAS));
        } catch (IOException e) {
            System.err.println("Error creando directorios: " + e.getMessage());
        }
    }

    // ===== IMPLEMENTACIÓN DE USUARIOS =====
    /**
     * Guarda un nuevo objeto {@code Usuario} en el archivo JSON.
     * * Verifica que el usuario no exista antes de agregarlo y rescribe el archivo
     * completo.
     *
     * @param usuario El objeto Usuario a guardar.
     * @return {@code true} si el usuario fue guardado exitosamente, {@code false}
     *         si ya existe o falló la I/O.
     */
    @Override
    public boolean guardarUsuario(Usuario usuario) {
        try {
            List<Usuario> usuarios = cargarTodosUsuarios();

            // Verificar si el usuario ya existe
            for (Usuario u : usuarios) {
                if (u.getEmail().equalsIgnoreCase(usuario.getEmail())) {
                    return false; // Usuario ya existe
                }
            }

            // Agregar nuevo usuario
            usuarios.add(usuario);

            // Guardar lista actualizada
            try (FileWriter writer = new FileWriter(ARCHIVO_USUARIOS)) {
                gson.toJson(usuarios, writer);
            }

            return true;

        } catch (IOException e) {
            System.err.println("Error guardando usuario: " + e.getMessage());
            return false;
        }
    }

    /**
     * Carga un usuario específico por su correo electrónico.
     *
     * @param email El correo electrónico del usuario a cargar.
     * @return El objeto {@code Usuario} si se encuentra, o {@code null} en caso
     *         contrario.
     */
    @Override
    public Usuario cargarUsuario(String email) {
        List<Usuario> usuarios = cargarTodosUsuarios();

        for (Usuario usuario : usuarios) {
            if (usuario.getEmail().equalsIgnoreCase(email)) {
                return usuario;
            }
        }

        return null;
    }

    /**
     * Carga la lista completa de todos los usuarios registrados desde el archivo
     * {@code usuarios.json}.
     *
     * @return Una {@code List} de todos los objetos {@code Usuario}, o una lista
     *         vacía si el archivo no existe o falla la carga.
     */
    @Override
    public List<Usuario> cargarTodosUsuarios() {
        try {
            File archivo = new File(ARCHIVO_USUARIOS);
            if (!archivo.exists()) {
                return new ArrayList<>();
            }

            try (FileReader reader = new FileReader(archivo)) {
                Type listType = new TypeToken<ArrayList<Usuario>>() {
                }.getType();
                List<Usuario> usuarios = gson.fromJson(reader, listType);
                return usuarios != null ? usuarios : new ArrayList<>();
            }

        } catch (IOException e) {
            System.err.println("Error cargando usuarios: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Escribe la lista completa de usuarios al archivo {@code usuarios.json}.
     * * Este es un metodo auxiliar privado.
     *
     * @param usuarios La lista de objetos Usuario a guardar.
     * @throws IOException Si ocurre un error al escribir en el archivo.
     */
    private void guardarListaUsuarios(List<Usuario> usuarios) throws IOException {
        try (FileWriter writer = new FileWriter(ARCHIVO_USUARIOS)) {
            gson.toJson(usuarios, writer);
        }
    }

    /**
     * Actualiza la información de un usuario existente (normalmente para cambiar la
     * contraseña).
     * * Busca al usuario por email, lo reemplaza en la lista y guarda la lista
     * completa.
     *
     * @param usuarioActualizado El objeto {@code Usuario} con los datos
     *                           actualizados.
     * @throws Exception Si el usuario no se encuentra o si ocurre un error de I/O
     *                   al guardar.
     */
    @Override
    public void actualizarUsuario(Usuario usuarioActualizado) throws Exception {
        List<Usuario> usuarios = cargarTodosUsuarios();
        boolean encontrado = false;
        for (int i = 0; i < usuarios.size(); i++) {
            if (usuarios.get(i).getEmail().equalsIgnoreCase(usuarioActualizado.getEmail())) {
                usuarios.set(i, usuarioActualizado);
                encontrado = true;
                break;
            }
        }

        if (encontrado) {
            guardarListaUsuarios(usuarios);
        } else {
            throw new Exception("Error al actualizar la contraseña: Usuario no encontrado en la base de datos.");
        }
    }

    /**
     * Verifica la existencia de un usuario por su correo electrónico.
     *
     * @param email El correo electrónico a verificar.
     * @return {@code true} si el usuario existe, {@code false} en caso contrario.
     */
    @Override
    public boolean existeUsuario(String email) {
        return cargarUsuario(email) != null;
    }

    /**
     * Implementación del metodo de la interfaz {@code Persistencia} para cargar
     * usuarios.
     * Llama a {@link #cargarTodosUsuarios()}.
     *
     * @deprecated Este metodo es redundante; use {@link #cargarTodosUsuarios()} en
     *             su lugar.
     */
    @Override
    public void cargarUsuarios() {
        cargarTodosUsuarios();
    }

    /**
     * Implementación del metodo de la interfaz {@code Persistencia} para cargar
     * estadísticas.
     * 
     * @return {@code null}
     */
    @Override
    public List<EstadisticasJuego> cargarEstadisticas() {
        cargarTodasEstadisticas();
        return null;
    }

    /**
     * Guarda el estado actual de un juego serializándolo en un archivo JSON usando
     * un DTO.
     * * El archivo se nombra con el correo del usuario.
     *
     * @param juego El objeto {@code Juego} con el estado actual del laberinto y
     *              jugador.
     * @return {@code true} si el juego fue guardado exitosamente.
     */
    @Override
    public boolean guardarJuego(Juego juego) {
        try {
            String archivoJuego = DIRECTORIO_JUEGOS + juego.getUsuario() + ".json";

            // Convertir juego a DTO para serialización
            JuegoDTO juegoDTO = new JuegoDTO(juego);

            try (FileWriter writer = new FileWriter(archivoJuego)) {
                gson.toJson(juegoDTO, writer);
            }

            return true;

        } catch (IOException e) {
            System.err.println("Error guardando juego: " + e.getMessage());
            return false;
        }
    }

    /**
     * Carga el juego guardado de un usuario por su email.
     *
     * @param usuario El correo electrónico del usuario.
     * @return El objeto {@code Juego} guardado, o {@code null} si no hay partida
     *         para ese usuario.
     */
    @Override
    public Juego cargarJuego(String usuario) {
        try {
            String archivoJuego = DIRECTORIO_JUEGOS + usuario + ".json";
            File archivo = new File(archivoJuego);

            if (!archivo.exists()) {
                return null;
            }

            try (FileReader reader = new FileReader(archivo)) {
                JuegoDTO juegoDTO = gson.fromJson(reader, JuegoDTO.class);
                return juegoDTO != null ? juegoDTO.toJuego() : null;
            }

        } catch (IOException e) {
            System.err.println("Error cargando juego: " + e.getMessage());
            return null;
        }
    }

    /**
     * Verifica si existe una partida guardada para un usuario específico.
     *
     * @param usuario El correo electrónico del usuario.
     * @return {@code true} si existe un archivo de juego guardado.
     */
    @Override
    public boolean existeJuegoGuardado(String usuario) {
        String archivoJuego = DIRECTORIO_JUEGOS + usuario + ".json";
        return new File(archivoJuego).exists();
    }

    // ===== IMPLEMENTACIÓN DE ESTADÍSTICAS =====
    /**
     * Guarda las estadísticas de una partida.
     * <p>
     * Agrega las nuevas estadísticas a la lista histórica existente del usuario y
     * rescribe el archivo.
     * </p>
     * 
     * @param estadisticas El objeto {@code EstadisticasJuego} a guardar.
     * @return {@code true} si las estadísticas fueron guardadas exitosamente.
     */
    @Override
    public boolean guardarEstadisticas(EstadisticasJuego estadisticas) {
        try {
            String archivoEstadisticas = DIRECTORIO_ESTADISTICAS + estadisticas.getUsuario() + ".json";

            List<EstadisticasJuego> estadisticasUsuario = cargarEstadisticas(estadisticas.getUsuario());
            estadisticasUsuario.add(estadisticas);

            try (FileWriter writer = new FileWriter(archivoEstadisticas)) {
                gson.toJson(estadisticasUsuario, writer);
            }

            return true;

        } catch (IOException e) {
            System.err.println("Error guardando estadísticas: " + e.getMessage());
            return false;
        }
    }

    /**
     * Carga la lista de estadísticas históricas asociadas a un usuario específico.
     *
     * @param usuario El correo electrónico del usuario.
     * @return Una lista de {@code EstadisticasJuego} jugadas por ese usuario, o
     *         lista vacía si no hay archivo.
     */
    @Override
    public List<EstadisticasJuego> cargarEstadisticas(String usuario) {
        try {
            String archivoEstadisticas = DIRECTORIO_ESTADISTICAS + usuario + ".json";
            File archivo = new File(archivoEstadisticas);

            if (!archivo.exists()) {
                return new ArrayList<>();
            }

            try (FileReader reader = new FileReader(archivo)) {
                Type listType = new TypeToken<ArrayList<EstadisticasJuego>>() {
                }.getType();
                List<EstadisticasJuego> estadisticas = gson.fromJson(reader, listType);
                return estadisticas != null ? estadisticas : new ArrayList<>();
            }

        } catch (IOException e) {
            System.err.println("Error cargando estadísticas: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Carga todas las estadísticas de juego de todos los usuarios, recorriendo el
     * directorio de estadísticas.
     *
     * @return Una lista que contiene las {@code EstadisticasJuego} de todos los
     *         usuarios.
     */
    @Override
    public List<EstadisticasJuego> cargarTodasEstadisticas() {
        List<EstadisticasJuego> todasEstadisticas = new ArrayList<>();
        File directorio = new File(DIRECTORIO_ESTADISTICAS);

        if (directorio.exists() && directorio.isDirectory()) {
            File[] archivos = directorio.listFiles((dir, name) -> name.endsWith(".json"));

            if (archivos != null) {
                for (File archivo : archivos) {
                    String usuario = archivo.getName().replace(".json", "");
                    todasEstadisticas.addAll(cargarEstadisticas(usuario));
                }
            }
        }

        return todasEstadisticas;
    }

    /**
     * Carga todas las estadísticas de juego de un usuario dado.
     *
     * @param usuario El correo electrónico del usuario.
     * @return Una lista de todas las {@code EstadisticasJuego} jugadas por el
     *         usuario, o lista vacía si hay error.
     */
    @Override
    public List<EstadisticasJuego> cargarTodasEstadisticas(String usuario) {
        try {
            String archivoEstadisticas = DIRECTORIO_ESTADISTICAS + usuario + ".json";
            File archivo = new File(archivoEstadisticas);

            // ✅ SI el archivo no existe, retornar lista vacía, no null
            if (!archivo.exists()) {
                return new ArrayList<>();
            }

            try (FileReader reader = new FileReader(archivo)) {
                Type listType = new TypeToken<ArrayList<EstadisticasJuego>>() {
                }.getType();
                List<EstadisticasJuego> estadisticas = gson.fromJson(reader, listType);

                // ✅ GARANTIZAR que nunca retorne null
                return estadisticas != null ? estadisticas : new ArrayList<>();
            }

        } catch (IOException e) {
            System.err.println("Error cargando estadísticas para " + usuario + ": " + e.getMessage());
            // ✅ SI hay error, retornar lista vacía, no null
            return new ArrayList<>();
        } catch (Exception e) {
            System.err.println("Error inesperado cargando estadísticas: " + e.getMessage());
            // ✅ CUALQUIER error retorna lista vacía
            return new ArrayList<>();
        }
    }

    // ===== CLASE DTO PARA SERIALIZACIÓN DE JUEGO =====
    /**
     * Clase auxiliar para serializar el objeto {@code Juego}.
     * * Necesaria para simplificar la estructura del JSON y manejar la
     * serialización
     * de referencias complejas como el Laberinto y el Jugador.
     */
    private static class JuegoDTO {
        private LaberintoDTO laberinto;
        private JugadorDTO jugador;
        private String usuario;
        private String inicio;
        private String fin;
        private String estado;
        private int trampasActivadas;
        private boolean nieblaDeGuerra;
        // Estadísticas acumulativas
        private int bombasRecolectadasTotal;
        private int llavesExplosionRecolectadasTotal;
        private int llavesExplosionUsadas;
        private int murosRojosDestruidos;

        public JuegoDTO(Juego juego) {
            this.laberinto = new LaberintoDTO(juego.getLaberinto());
            this.jugador = new JugadorDTO(juego.getJugador());
            this.usuario = juego.getUsuario();
            this.inicio = juego.getInicio().toString();
            this.fin = juego.getFin() != null ? juego.getFin().toString() : null;
            this.estado = juego.getEstado().name();
            this.trampasActivadas = juego.getTrampasActivadas();
            this.nieblaDeGuerra = juego.isNieblaDeGuerra();

            this.bombasRecolectadasTotal = juego.getBombasRecolectadasTotal();
            this.llavesExplosionRecolectadasTotal = juego.getLlavesExplosionRecolectadasTotal();
            this.llavesExplosionUsadas = juego.getLlavesExplosionUsadas();
            this.murosRojosDestruidos = juego.getMurosRojosDestruidos();
        }

        /**
         * Convierte el DTO de vuelta a un objeto {@code Juego} de dominio completo.
         * 
         * @return El objeto {@code Juego} reconstruido.
         */
        public Juego toJuego() {
            Laberinto laberintoObj = this.laberinto.toLaberinto();
            Jugador jugadorObj = this.jugador.toJugador();
            LocalDateTime inicioObj = LocalDateTime.parse(this.inicio);
            LocalDateTime finObj = this.fin != null ? LocalDateTime.parse(this.fin) : null;
            EstadoJuego estadoObj = EstadoJuego.valueOf(this.estado);

            Juego juego = new Juego(laberintoObj, jugadorObj, usuario, inicioObj);
            juego.setFin(finObj);
            juego.setEstado(estadoObj);
            juego.setTrampasActivadas(this.trampasActivadas);
            juego.setNieblaDeGuerra(this.nieblaDeGuerra);

            // Restaurar estadísticas acumulativas (usando reflexión o setters si
            // existieran,
            // pero como no hay setters directos para totales, simulamos incrementos o
            // añadimos setters en Juego.
            // Para simplificar y no modificar Juego.java más, asumiremos que se pueden
            // setear o
            // modificaremos Juego.java para permitir setear estos valores al cargar.
            // REVISIÓN: Juego.java no tiene setters para estos totales.
            // VOY A AÑADIR SETTERS EN JUEGO.JAVA PRIMERO O USAR UN BUCLE AQUÍ ES FEO.
            // MEJOR AÑADIR SETTERS EN JUEGO.JAVA.

            // Asumiendo que añadiré los setters en Juego.java:
            juego.setBombasRecolectadasTotal(this.bombasRecolectadasTotal);
            juego.setLlavesExplosionRecolectadasTotal(this.llavesExplosionRecolectadasTotal);
            juego.setLlavesExplosionUsadas(this.llavesExplosionUsadas);
            juego.setMurosRojosDestruidos(this.murosRojosDestruidos);

            return juego;
        }
    }

    /**
     * Clase auxiliar (DTO) para serializar el objeto {@code Laberinto}.
     */
    private static class LaberintoDTO {
        private CeldaDTO[][] celdas;
        private int filas;
        private int columnas;

        public LaberintoDTO(Laberinto laberinto) {
            this.filas = laberinto.getFilas();
            this.columnas = laberinto.getColumnas();
            this.celdas = new CeldaDTO[filas][columnas];

            for (int i = 0; i < filas; i++) {
                for (int j = 0; j < columnas; j++) {
                    this.celdas[i][j] = new CeldaDTO(laberinto.getCelda(i, j));
                }
            }
        }

        public Laberinto toLaberinto() {
            Celda[][] celdasObj = new Celda[filas][columnas];

            for (int i = 0; i < filas; i++) {
                for (int j = 0; j < columnas; j++) {
                    celdasObj[i][j] = this.celdas[i][j].toCelda();
                }
            }

            return new Laberinto(celdasObj, filas, columnas);
        }
    }

    /**
     * Clase auxiliar (DTO) para serializar el objeto {@code Celda}.
     */
    private static class CeldaDTO {
        private String tipo;
        private int fila;
        private int columna;
        private boolean visitada;
        private boolean visible;

        public CeldaDTO(Celda celda) {
            this.tipo = celda.getTipo().name();
            this.fila = celda.getFila();
            this.columna = celda.getColumna();
            this.visitada = celda.isVisitada();
            this.visible = celda.isVisible();
        }

        public Celda toCelda() {
            TipoCelda tipoObj = TipoCelda.valueOf(this.tipo);
            Celda celda = new Celda(tipoObj, fila, columna);
            celda.setVisitada(visitada);
            celda.setVisible(visible);
            return celda;
        }
    }

    /**
     * Clase auxiliar (DTO) para serializar el objeto {@code Jugador}.
     */
    private static class JugadorDTO {
        private int vida;
        private int cristales;
        private boolean tieneLlave;
        private int posX;
        private int posY;
        private int bombas;
        private int llavesExplosion;

        public JugadorDTO(Jugador jugador) {
            this.vida = jugador.getVida();
            this.cristales = jugador.getCristales();
            this.tieneLlave = jugador.isTieneLlave();
            this.posX = jugador.getPosX();
            this.posY = jugador.getPosY();
            this.bombas = jugador.getBombas();
            this.llavesExplosion = jugador.getLlavesExplosion();
        }

        public Jugador toJugador() {
            Jugador jugador = new Jugador(vida, cristales, tieneLlave);
            jugador.setPosX(posX);
            jugador.setPosY(posY);
            jugador.setBombas(bombas);
            jugador.setLlavesExplosion(llavesExplosion);
            return jugador;
        }
    }

    /**
     * Adaptador para la librería Gson que permite serializar y deserializar
     * correctamente
     * los objetos {@code LocalDateTime} de Java 8, asegurando un formato ISO-8601.
     */
    class LocalDateTimeAdapter
            implements com.google.gson.JsonSerializer<LocalDateTime>, com.google.gson.JsonDeserializer<LocalDateTime> {
        private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

        @Override
        public com.google.gson.JsonElement serialize(LocalDateTime src, java.lang.reflect.Type typeOfSrc,
                com.google.gson.JsonSerializationContext context) {
            return new com.google.gson.JsonPrimitive(formatter.format(src));
        }

        @Override
        public LocalDateTime deserialize(com.google.gson.JsonElement json, java.lang.reflect.Type typeOfT,
                com.google.gson.JsonDeserializationContext context) throws com.google.gson.JsonParseException {
            return LocalDateTime.parse(json.getAsString(), formatter);
        }
    }
}