package clases;

import java.awt.geom.Rectangle2D;

public class Raqueta {
    private int x;
    private int y;
    private final int ancho = 10;
    private final int alto = 60;

    public Raqueta(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Rectangle2D getRaqueta() {
        return new Rectangle2D.Double(x, y, ancho, alto);
    }

    public int getAncho() {
        return ancho;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getLeftEdge() {
        return x;
    }

    public int getRightEdge() {
        return x + ancho;
    }

    public void moverArriba(int limite) {
        if (y > 0) {
            y -= 2;
        }
    }

    public void moverAbajo(int limite) {
        if (y + alto < limite) {
            y += 2;
        }
    }
}