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


public class PersistenciaJASON implements Persistencia {
    private static final String DIRECTORIO_BASE = "datos/";
    private static final String ARCHIVO_USUARIOS = DIRECTORIO_BASE + "usuarios.json";
    private static final String DIRECTORIO_JUEGOS = DIRECTORIO_BASE + "juegos/";
    private static final String DIRECTORIO_ESTADISTICAS = DIRECTORIO_BASE + "estadisticas/";

    private Gson gson;

    public PersistenciaJASON() {
        // Configurar Gson para manejar LocalDateTime y para formato bonito
        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();

        // Crear directorios si no existen
        crearDirectorios();
    }

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

    @Override
    public List<Usuario> cargarTodosUsuarios() {
        try {
            File archivo = new File(ARCHIVO_USUARIOS);
            if (!archivo.exists()) {
                return new ArrayList<>();
            }

            try (FileReader reader = new FileReader(archivo)) {
                Type listType = new TypeToken<ArrayList<Usuario>>(){}.getType();
                List<Usuario> usuarios = gson.fromJson(reader, listType);
                return usuarios != null ? usuarios : new ArrayList<>();
            }

        } catch (IOException e) {
            System.err.println("Error cargando usuarios: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    private void guardarListaUsuarios(List<Usuario> usuarios) throws IOException {
        try (FileWriter writer = new FileWriter(ARCHIVO_USUARIOS)) {
            gson.toJson(usuarios, writer);
        }
    }
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


    @Override
    public boolean existeUsuario(String email) {
        return cargarUsuario(email) != null;
    }

    @Override
    public void cargarUsuarios() {
        cargarTodosUsuarios();
    }

    @Override
    public void cargarEstadisticas() {
        cargarTodasEstadisticas();
    }

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

    @Override
    public boolean existeJuegoGuardado(String usuario) {
        String archivoJuego = DIRECTORIO_JUEGOS + usuario + ".json";
        return new File(archivoJuego).exists();
    }

    // ===== IMPLEMENTACIÓN DE ESTADÍSTICAS =====

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

    @Override
    public List<EstadisticasJuego> cargarEstadisticas(String usuario) {
        try {
            String archivoEstadisticas = DIRECTORIO_ESTADISTICAS + usuario + ".json";
            File archivo = new File(archivoEstadisticas);

            if (!archivo.exists()) {
                return new ArrayList<>();
            }

            try (FileReader reader = new FileReader(archivo)) {
                Type listType = new TypeToken<ArrayList<EstadisticasJuego>>(){}.getType();
                List<EstadisticasJuego> estadisticas = gson.fromJson(reader, listType);
                return estadisticas != null ? estadisticas : new ArrayList<>();
            }

        } catch (IOException e) {
            System.err.println("Error cargando estadísticas: " + e.getMessage());
            return new ArrayList<>();
        }
    }

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

    // ===== CLASE DTO PARA SERIALIZACIÓN DE JUEGO =====

    private static class JuegoDTO {
        private LaberintoDTO laberinto;
        private JugadorDTO jugador;
        private String usuario;
        private String inicio;
        private String fin;
        private String estado;
        private int trampasActivadas;

        public JuegoDTO(Juego juego) {
            this.laberinto = new LaberintoDTO(juego.getLaberinto());
            this.jugador = new JugadorDTO(juego.getJugador());
            this.usuario = juego.getUsuario();
            this.inicio = juego.getInicio().toString();
            this.fin = juego.getFin() != null ? juego.getFin().toString() : null;
            this.estado = juego.getEstado().name();
            this.trampasActivadas = juego.getTrampasActivadas(); // ✅ Asignamos correctamente
        }

        public Juego toJuego() {
            Laberinto laberintoObj = this.laberinto.toLaberinto();
            Jugador jugadorObj = this.jugador.toJugador();
            LocalDateTime inicioObj = LocalDateTime.parse(this.inicio);
            LocalDateTime finObj = this.fin != null ? LocalDateTime.parse(this.fin) : null;
            EstadoJuego estadoObj = EstadoJuego.valueOf(this.estado);

            Juego juego = new Juego(laberintoObj, jugadorObj, usuario, inicioObj);
            juego.setFin(finObj);
            juego.setEstado(estadoObj);
            juego.setTrampasActivadas(this.trampasActivadas); // ✅ Recuperamos correctamente

            return juego;
        }
    }

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

    private static class JugadorDTO {
        private int vida;
        private int cristales;
        private boolean tieneLlave;
        private int posX;
        private int posY;

        public JugadorDTO(Jugador jugador) {
            this.vida = jugador.getVida();
            this.cristales = jugador.getCristales();
            this.tieneLlave = jugador.isTieneLlave();
            this.posX = jugador.getPosX();
            this.posY = jugador.getPosY();
        }

        public Jugador toJugador() {
            Jugador jugador = new Jugador(vida, cristales, tieneLlave);
            jugador.setPosX(posX);
            jugador.setPosY(posY);
            return jugador;
        }
    }
}

// Clase adaptadora para LocalDateTime
class LocalDateTimeAdapter implements com.google.gson.JsonSerializer<LocalDateTime>, com.google.gson.JsonDeserializer<LocalDateTime> {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @Override
    public com.google.gson.JsonElement serialize(LocalDateTime src, java.lang.reflect.Type typeOfSrc, com.google.gson.JsonSerializationContext context) {
        return new com.google.gson.JsonPrimitive(formatter.format(src));
    }

    @Override
    public LocalDateTime deserialize(com.google.gson.JsonElement json, java.lang.reflect.Type typeOfT, com.google.gson.JsonDeserializationContext context) throws com.google.gson.JsonParseException {
        return LocalDateTime.parse(json.getAsString(), formatter);
    }
}