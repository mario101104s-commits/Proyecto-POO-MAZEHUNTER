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

public class ServicioJuegoImpl implements ServicioJuego {
    private Persistencia persistencia;
    private GeneradorLaberinto generadorLaberinto;

    public ServicioJuegoImpl(Persistencia persistencia) {
        this.persistencia = persistencia;
        this.generadorLaberinto = new GeneradorLaberintoImpl();
    }

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

    private void procesarCelda(Juego juego, Celda celda) {
        Jugador jugador = juego.getJugador();

        switch (celda.getTipo()) {
            case CRISTAL:
                jugador.recolectarCristal();
                System.out.println("¡💎 Cristal recolectado! Total: " + jugador.getCristales());
                celda.setTipo(TipoCelda.CAMINO); // El cristal desaparece
                break;

            case TRAMPA:
                jugador.activarTrampa();
                juego.incrementarTrampasActivadas();
                System.out.println("💀 ¡Trampa activada! Vida restante: " + jugador.getVida() + "%");
                celda.setTipo(TipoCelda.CAMINO); // La trampa se desactiva
                break;

            case LLAVE:
                jugador.recogerLlave();
                System.out.println("🗝️ ¡Llave obtenida! Ahora puedes salir del laberinto");
                celda.setTipo(TipoCelda.CAMINO); // La llave desaparece
                break;

            case ENERGIA:
                jugador.setVida(jugador.getVida() + 10); // Energía da 10% de vida
                System.out.println("⚡ ¡Energía obtenida! Vida: " + jugador.getVida() + "%");
                celda.setTipo(TipoCelda.CAMINO); // La energía desaparece
                break;

            case VIDA:
                jugador.setVida(jugador.getVida() + 25); // Vida extra da 25%
                System.out.println("➕ ¡Vida extra! Vida: " + jugador.getVida() + "%");
                celda.setTipo(TipoCelda.CAMINO); // La vida extra desaparece
                break;
        }
    }

    private void revelarCeldasAdyacentes(Laberinto laberinto, int x, int y) {
        int[][] direcciones = {{-1, 0}, {1, 0}, {0, -1}, {0, 1},
                {-1, -1}, {-1, 1}, {1, -1}, {1, 1}};

        for (int[] dir : direcciones) {
            int adjX = x + dir[0];
            int adjY = y + dir[1];

            if (laberinto.esPosicionValida(adjX, adjY)) {
                Celda celdaAdyacente = laberinto.getCelda(adjX, adjY);
                celdaAdyacente.setVisible(true);
            }
        }
    }

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
                juego.getLaberinto().getFilas() + "x" + juego.getLaberinto().getColumnas()
        );
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

    @Override
    public boolean puedeSalir(Juego juego) {
        Celda celdaActual = juego.getLaberinto().getCelda(
                juego.getJugador().getPosX(),
                juego.getJugador().getPosY()
        );
        return juego.getJugador().isTieneLlave() &&
                celdaActual.getTipo() == TipoCelda.SALIDA;
    }

    private int[] encontrarPosicionEntrada(Laberinto laberinto) {
        for (int i = 0; i < laberinto.getFilas(); i++) {
            for (int j = 0; j < laberinto.getColumnas(); j++) {
                Celda celda = laberinto.getCelda(i, j);
                if (celda.getTipo() == TipoCelda.ENTRADA) {
                    return new int[]{i, j};
                }
            }
        }
        return null;
    }
}
