package clases;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

public class Pelota {
    private int x;
    private int y;
    private int dx = 2, dy = 2; 
    private final int anc = 15, alt = 15; 
    private Color color;

    public Pelota(int x, int y, Color color) {
        this.x = x;
        this.y = y;
        this.color = color;    }

    public Rectangle2D getPelota() {
        return new Rectangle2D.Double(x, y, anc, alt);
    }
    
    public Color getColor() {
        return color;
    }

    /**
     * Mueve la pelota y detecta si salió por las paredes laterales.
     * @return 0 si no salió, 1 si salió por izquierda, 2 si salió por derecha
     */
    public int mover(Rectangle limites, boolean colisionR1, boolean colisionR2, Raqueta r1, Raqueta r2) {
        x += dx;
        y += dy;

        int pushBack = 5; 

        if (colisionR1) {
            dx = Math.abs(dx);  
            x = r1.getRightEdge() + pushBack; 
        } else if (colisionR2) {
            dx = -Math.abs(dx); 
            x = r2.getLeftEdge() - anc - pushBack; 
        }

        // Rebote techo y piso
        if (y < 0) {
            dy = Math.abs(dy);
            y = 0;
        }
        if (y > limites.getMaxY() - alt) {
            dy = -Math.abs(dy);
            y = (int) (limites.getMaxY() - alt);
        }

        // Detectar si salió por izquierda o derecha
        if (x < 0) {
            return 1; // salió por izquierda
        }
        if (x > limites.getMaxX() - anc) {
            return 2; // salió por derecha
        }

        return 0; // no salió
    }

    // Getters y setters

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getAncho() {
        return anc;
    }

    public int getAlto() {
        return alt;
    }

    public int getDx() {
        return dx;
    }

    public void setDx(int dx) {
        this.dx = dx;
    }

    public int getDy() {
        return dy;
    }

    public void setDy(int dy) {
        this.dy = dy;
    }

    public void invertirDireccion() {
        dx = -dx;
    }
}

