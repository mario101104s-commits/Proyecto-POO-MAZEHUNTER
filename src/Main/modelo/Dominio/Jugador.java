package Main.modelo.Dominio;

public class Jugador {
    private int vida;
    private int cristales;
    private boolean tieneLlave;
    private int posX;
    private int posY;

    public Jugador(int vida, int cristales, boolean tieneLlave) {
        this.vida = vida;
        this.cristales = cristales;
        this.tieneLlave = tieneLlave;
        this.posX = 0;
        this.posY = 0;
    }

    //Metodos

    public void mover(int deltaX, int deltaY) {
        this.posX += deltaX;
        this.posY += deltaY;
    }

    public void recolectarCristal() {
        this.cristales++;
    }

    public void activarTrampa() {
        this.vida -= 20; //
    }

    public void recogerLlave() {
        this.tieneLlave = true;
    }

    public boolean estaVivo() {
        return vida > 0;
    }

    //Getter y Setters:

    public int getVida() {
        return vida;
    }

    public void setVida(int vida) {
        this.vida = vida;
    }

    public int getCristales() {
        return cristales;
    }

    public void setCristales(int cristales) {
        this.cristales = cristales;
    }

    public boolean isTieneLlave() {
        return tieneLlave;
    }

    public void setTieneLlave(boolean tieneLlave) {
        this.tieneLlave = tieneLlave;
    }

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }
}
