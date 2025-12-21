package Main.servicio.Implementaciones;

import Main.modelo.Constantes.Direccion;
import Main.modelo.Constantes.EstadoJuego;
import Main.modelo.Constantes.TipoCelda;
import Main.modelo.Dominio.*;
import Main.modelo.Transferencia.ResultadoJuego;
import Main.servicio.Interfaces.GeneradorLaberinto;
import Main.servicio.Interfaces.Persistencia;
import Main.servicio.Interfaces.ServicioJuego;

import java.time.LocalDateTime;
import java.time.Duration;
import java.util.List;

/**
 * Implementación concreta de la lógica de negocio para la gestión de partidas
 * de Maze Hunter.
 * <p>
 * Se encarga de la inicialización de juegos, el manejo de movimientos, las
 * interacciones
 * del jugador con las celdas y la gestión del estado de la partida
 * a través de la persistencia.
 * </p>
 * 
 * @author Mario Sanchez
 * @version 1.0
 * @since 11/11/2025
 */
public class ServicioJuegoImpl implements ServicioJuego {
    /**
     * Interfaz de persistencia utilizada para guardar y cargar juegos/estadísticas.
     */
    private Persistencia persistencia;
    /** Interfaz para la generación de laberintos. */
    private GeneradorLaberinto generadorLaberinto;

    /**
     * Constructor. Inicializa el servicio de juego con la dependencia de
     * persistencia.
     *
     * @param persistencia La implementación del almacén de datos.
     */
    public ServicioJuegoImpl(Persistencia persistencia) {
        this.persistencia = persistencia;
        this.generadorLaberinto = new GeneradorLaberintoImpl();
    }

    /**
     * Inicia una nueva partida, generando un laberinto con las dimensiones
     * especificadas.
     *
     * @param filas    El número de filas del laberinto.
     * @param columnas El número de columnas del laberinto.
     * @param usuario  El correo electrónico del usuario que inicia el juego.
     * @return El nuevo objeto {@code Juego} inicializado.
     * @throws IllegalArgumentException Si las dimensiones son menores a 5x5.
     * @throws IllegalStateException    Si la posición de entrada no se encuentra en
     *                                  el laberinto generado.
     */
    @Override
    public Juego iniciarNuevoJuego(int filas, int columnas, String usuario) {
        // Validar tamaño mínimo
        if (filas < 5 || columnas < 5) {
            throw new IllegalArgumentException("El laberinto debe ser de al menos 5x5");
        }

        Laberinto laberinto = generadorLaberinto.generar(filas, columnas);

        // Encontrar posición de entrada para el jugador
        int[] posicionInicial = encontrarPosicionEntrada(laberinto);
        if (posicionInicial == null) {
            throw new IllegalStateException("No se encontró posición de entrada en el laberinto");
        }

        Jugador jugador = new Jugador(100, 0, false); // 100% vida, 0 cristales, sin llave
        jugador.setPosX(posicionInicial[0]);
        jugador.setPosY(posicionInicial[1]);

        Juego juego = new Juego(laberinto, jugador, usuario, LocalDateTime.now());

        // Marcar celda inicial como visitada
        Celda celdaInicial = laberinto.getCelda(posicionInicial[0], posicionInicial[1]);
        celdaInicial.setVisitada(true);
        celdaInicial.setVisible(true);

        return juego;
    }

    /**
     * Carga el estado de un juego previamente guardado para un usuario.
     *
     * @param usuario El correo electrónico del usuario.
     * @return El objeto {@code Juego} cargado, o {@code null} si no existe un juego
     *         guardado.
     */
    @Override
    public Juego cargarJuegoGuardado(String usuario) {
        Juego juego = persistencia.cargarJuego(usuario);
        if (juego != null) {
            System.out.println("✅ Juego cargado exitosamente para: " + usuario);
        } else {
            System.out.println("❌ No se encontró juego guardado para: " + usuario);
        }
        return juego;
    }

    /**
     * Intenta mover al jugador en la dirección especificada.
     *
     * Si la posición es transitable, actualiza las coordenadas del jugador, procesa
     * la celda destino (recolectar, sufrir daño) y revela celdas adyacentes.
     *
     * @param juego     El objeto {@code Juego} actual.
     * @param direccion La dirección del movimiento (Arriba, Abajo, Izquierda,
     *                  Derecha).
     * @return {@code true} si el movimiento fue exitoso, {@code false} si la celda
     *         no es válida o transitable.
     */
    @Override
    public boolean moverJugador(Juego juego, Direccion direccion) {
        if (juego.getEstado() != EstadoJuego.EN_CURSO) {
            return false;
        }

        Jugador jugador = juego.getJugador();
        Laberinto laberinto = juego.getLaberinto();

        int nuevaX = jugador.getPosX() + direccion.getDeltaFila();
        int nuevaY = jugador.getPosY() + direccion.getDeltaColumna();

        // Verificar si la nueva posición es válida y transitable
        if (!laberinto.esPosicionValida(nuevaX, nuevaY) ||
                !laberinto.esTransitable(nuevaX, nuevaY)) {
            return false;
        }

        // Mover jugador
        jugador.setPosX(nuevaX);
        jugador.setPosY(nuevaY);

        // Procesar la celda destino
        Celda celdaDestino = laberinto.getCelda(nuevaX, nuevaY);
        procesarCelda(juego, celdaDestino);

        // Marcar como visitada y visible
        celdaDestino.setVisitada(true);
        celdaDestino.setVisible(true);

        // Revelar celdas adyacentes (visión limitada)
        revelarCeldasAdyacentes(laberinto, nuevaX, nuevaY);

        // Verificar condiciones de fin de juego
        verificarEstadoJuego(juego);

        // Guardar juego (Verificado automatico despues de cada movimiento) :)

        guardarJuego(juego);

        return true;
    }

    /**
     * Ejecuta la lógica correspondiente al tipo de celda en la que cae el jugador.
     *
     * Esto incluye: recolectar cristales/llave, recuperar vida, o activar trampas.
     *
     * @param juego El objeto {@code Juego} actual.
     * @param celda La {@code Celda} destino que debe ser procesada.
     */
    private void procesarCelda(Juego juego, Celda celda) {
        Jugador jugador = juego.getJugador();

        // MANEJO ESPECÍFICO PARA TRAMPAS - SIN DEBUG
        if (celda.getTipo() == TipoCelda.TRAMPA) {
            // 1. Activar efecto en jugador
            int vidaAntes = jugador.getVida();
            jugador.activarTrampa();

            // 2. Incrementar contador SILENCIOSAMENTE
            int trampasAntes = juego.getTrampasActivadas();
            juego.setTrampasActivadas(trampasAntes + 1);

            // 3. Convertir trampa a camino
            celda.setTipo(TipoCelda.CAMINO);

            // ✅ SOLO MOSTRAR MENSAJE AL USUARIO, NO DEBUG
            System.out.println("💀 ¡Trampa activada! Vida restante: " + jugador.getVida() + "%");
            return;
        }

        // Procesar otros tipos de celdas (sin debug)
        switch (celda.getTipo()) {
            case CRISTAL:
                jugador.recolectarCristal();
                System.out.println("¡💎 Cristal recolectado! Total: " + jugador.getCristales());
                celda.setTipo(TipoCelda.CAMINO);
                break;

            case LLAVE:
                jugador.recogerLlave();
                System.out.println("🗝️ ¡Llave obtenida! Ahora puedes salir del laberinto");
                celda.setTipo(TipoCelda.CAMINO);
                break;

            case ENERGIA:
                jugador.setVida(Math.min(100, jugador.getVida() + 10)); // ✅ NO EXCEDER 100
                System.out.println("⚡ ¡Energía obtenida! Vida: " + jugador.getVida() + "%");
                celda.setTipo(TipoCelda.CAMINO);
                break;

            case VIDA:
                jugador.setVida(Math.min(100, jugador.getVida() + 25)); // ✅ NO EXCEDER 100
                System.out.println("➕ ¡Vida extra! Vida: " + jugador.getVida() + "%");
                celda.setTipo(TipoCelda.CAMINO);
                break;
        }
    }

    /**
     * Marca las celdas adyacentes a la posición actual del jugador como visibles.
     * * Esto simula el campo de visión limitado del jugador.
     *
     * @param laberinto El laberinto.
     * @param x         La coordenada de la fila del jugador.
     * @param y         La coordenada de la columna del jugador.
     */
    private void revelarCeldasAdyacentes(Laberinto laberinto, int x, int y) {
        int[][] direcciones = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 },
                { -1, -1 }, { -1, 1 }, { 1, -1 }, { 1, 1 } };

        for (int[] dir : direcciones) {
            int adjX = x + dir[0];
            int adjY = y + dir[1];

            if (laberinto.esPosicionValida(adjX, adjY)) {
                Celda celdaAdyacente = laberinto.getCelda(adjX, adjY);
                celdaAdyacente.setVisible(true);
            }
        }
    }

    /**
     * Verifica si se ha alcanzado una condición de fin de juego (Ganado o Perdido).
     * <p>
     * El juego termina si el jugador muere (vida < 1) o si llega a la salida
     * teniendo la llave.
     * </p>
     * 
     * @param juego El objeto {@code Juego} actual.
     */
    private void verificarEstadoJuego(Juego juego) {
        Jugador jugador = juego.getJugador();
        Laberinto laberinto = juego.getLaberinto();
        Celda celdaActual = laberinto.getCelda(jugador.getPosX(), jugador.getPosY());

        // Verificar si perdió
        if (!jugador.estaVivo()) {
            juego.setEstado(EstadoJuego.PERDIDO);
            System.out.println("💀 ¡Has perdido! Se te acabó la vida.");
            return;
        }

        // Verificar si ganó
        if (celdaActual.getTipo() == TipoCelda.SALIDA && jugador.isTieneLlave()) {
            juego.setEstado(EstadoJuego.GANADO);
            System.out.println("🎉 ¡Felicidades! Has escapado del laberinto.");
        }
    }

    /**
     * Guarda el estado actual de la partida en el sistema de persistencia.
     *
     * @param juego El objeto {@code Juego} a guardar.
     * @return {@code true} si el guardado fue exitoso.
     */
    @Override
    public boolean guardarJuego(Juego juego) {
        boolean exito = persistencia.guardarJuego(juego);
        if (exito) {
            System.out.println("💾 Juego guardado automáticamente");
        } else {
            System.out.println("❌ Error guardando el juego");
        }
        return exito;
    }

    /**
     * Finaliza la partida, calcula las métricas de rendimiento y guarda las
     * estadísticas finales.
     *
     * @param juego El objeto {@code Juego} terminado.
     * @return Un objeto {@code ResultadoJuego} con todas las métricas finales.
     */

    @Override
    public ResultadoJuego terminarJuego(Juego juego) {
        juego.setFin(LocalDateTime.now());
        Duration duracion = Duration.between(juego.getInicio(), juego.getFin());

        ResultadoJuego resultado = new ResultadoJuego();
        resultado.setTiempoSegundos(duracion.getSeconds());
        resultado.setCristalesRecolectados(juego.getJugador().getCristales());
        resultado.setTrampasActivadas(juego.getTrampasActivadas());
        resultado.setVidaRestante(juego.getJugador().getVida());
        resultado.setTamanioLaberinto(
                juego.getLaberinto().getFilas() + "x" + juego.getLaberinto().getColumnas());
        resultado.setGanado(juego.getEstado() == EstadoJuego.GANADO);

        // Guardar estadísticas
        EstadisticasJuego estadisticas = new EstadisticasJuego(juego.getUsuario(), juego.getFin());
        estadisticas.setTiempoSegundos(duracion.getSeconds());
        estadisticas.setCristalesRecolectados(juego.getJugador().getCristales());
        estadisticas.setTrampasActivadas(juego.getTrampasActivadas());
        estadisticas.setVidaRestante(juego.getJugador().getVida());
        estadisticas.setTamanioLaberinto(resultado.getTamanioLaberinto());
        estadisticas.setGanado(resultado.isGanado());

        persistencia.guardarEstadisticas(estadisticas);
        guardarJuego(juego);

        return resultado;
    }

    /**
     * Verifica si el jugador está en la celda de salida y tiene la llave.
     *
     * @param juego El objeto {@code Juego} actual.
     * @return {@code true} si se cumplen ambas condiciones para salir del
     *         laberinto.
     */
    @Override
    public boolean puedeSalir(Juego juego) {
        Celda celdaActual = juego.getLaberinto().getCelda(
                juego.getJugador().getPosX(),
                juego.getJugador().getPosY());
        return juego.getJugador().isTieneLlave() &&
                celdaActual.getTipo() == TipoCelda.SALIDA;
    }

    /**
     * Busca y retorna las coordenadas de la celda que ha sido marcada como
     * {@code ENTRADA}
     * en el laberinto.
     *
     * @param laberinto El objeto {@code Laberinto} donde buscar.
     * @return Un array {@code int[]} con [fila, columna] de la entrada, o
     *         {@code null} si no se encuentra.
     */
    private int[] encontrarPosicionEntrada(Laberinto laberinto) {
        for (int i = 0; i < laberinto.getFilas(); i++) {
            for (int j = 0; j < laberinto.getColumnas(); j++) {
                Celda celda = laberinto.getCelda(i, j);
                if (celda.getTipo() == TipoCelda.ENTRADA) {
                    return new int[] { i, j };
                }
            }
        }
        return null;
    }

    /**
     * Procesa la finalización de la partida en curso (cuando el usuario decide
     * salir) y guarda
     * las métricas de rendimiento como estadísticas parciales.
     * <p>
     * Si el juego ya terminó (Ganado/Perdido), llama directamente a
     * {@link #terminarJuego(Juego)}.
     * </p>
     * 
     * @param juego El objeto {@code Juego} que se está terminando parcialmente.
     * @return Un objeto {@code ResultadoJuego} con las métricas parciales, marcando
     *         el juego como no ganado.
     */
    public ResultadoJuego guardarEstadisticasParciales(Juego juego) {
        if (juego.getEstado() != EstadoJuego.EN_CURSO) {
            return terminarJuego(juego);
        }

        LocalDateTime ahora = LocalDateTime.now();
        Duration duracion = Duration.between(juego.getInicio(), ahora);

        ResultadoJuego resultado = new ResultadoJuego();
        resultado.setTiempoSegundos(duracion.getSeconds());
        resultado.setCristalesRecolectados(juego.getJugador().getCristales());
        resultado.setTrampasActivadas(juego.getTrampasActivadas());
        resultado.setVidaRestante(juego.getJugador().getVida());
        resultado.setTamanioLaberinto(
                juego.getLaberinto().getFilas() + "x" + juego.getLaberinto().getColumnas());
        resultado.setGanado(false); // No ganó porque salió

        // Guardar estadísticas parciales
        EstadisticasJuego estadisticas = new EstadisticasJuego(juego.getUsuario(), ahora);
        estadisticas.setTiempoSegundos(duracion.getSeconds());
        estadisticas.setCristalesRecolectados(juego.getJugador().getCristales());
        estadisticas.setTrampasActivadas(juego.getTrampasActivadas());
        estadisticas.setVidaRestante(juego.getJugador().getVida());
        estadisticas.setTamanioLaberinto(resultado.getTamanioLaberinto());
        estadisticas.setGanado(false);

        persistencia.guardarEstadisticas(estadisticas);
        guardarJuego(juego);

        return resultado;
    }

    // Verifica si existe un juego guardado para un usuario
    @Override
    public boolean existeJuegoGuardado(String usuario) {
        return persistencia.existeJuegoGuardado(usuario);
    }

    // Obtiene todas las estadísticas de un usuario
    @Override
    public List<EstadisticasJuego> obtenerEstadisticas(String emailUsuario) {
        return persistencia.cargarTodasEstadisticas(emailUsuario);
    }
}
