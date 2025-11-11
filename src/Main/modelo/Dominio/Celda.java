package Main.modelo.Dominio;

import Main.modelo.Constantes.TipoCelda;

public class Celda {
    private TipoCelda tipo;
    private int fila;
    private int columna;
    private boolean visitada;
    private boolean visible;

    public Celda(TipoCelda tipo, int fila, int columna) {
        this.tipo = tipo;
        this.fila = fila;
        this.columna = columna;
        this.visitada = false;
        this.visible = false;
    }
    //Getter y Setters
    public TipoCelda getTipo() {
        return tipo;
    }

    public void setTipo(TipoCelda tipo) {
        this.tipo = tipo;
    }

    public int getFila() {
        return fila;
    }

    public void setFila(int fila) {
        this.fila = fila;
    }

    public int getColumna() {
        return columna;
    }

    public void setColumna(int columna) {
        this.columna = columna;
    }

    public boolean isVisitada() {
        return visitada;
    }

    public void setVisitada(boolean visitada) {
        this.visitada = visitada;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    //Metodos

    public boolean isTransitable() {
        return tipo.isTransitable();
    }

    public char getSimbolo() {
        return tipo.getSimbolo();
    }

}

