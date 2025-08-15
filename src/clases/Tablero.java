package clases;

import javax.swing.JPanel;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Random;

public class Tablero extends JPanel {

    private static final long serialVersionUID = 1L;

    // Lista para múltiples pelotas
    private ArrayList<Pelota> pelotas = new ArrayList<>();
    private Raqueta r1 = new Raqueta(10, 200);
    private Raqueta r2 = new Raqueta(0, 200);

    private int punj1 = 0;
    private int puntj2 = 0;
    private final int punmax = 5;
    private boolean juegoTerminado = false;

    private long tiempoInicio; 
    private final long tiempoLimite = 60_000; 

    private int pelotasAgregadas = 0; // cant de pelotas que se van a agregar 
    private Random rnd = new Random();

    public Tablero() {
        setBackground(Color.BLACK);

    
        pelotas.add(new Pelota(0, 0, new Color(0, 255, 200)));
        resetearPelotas();
        // no se inicxializa el tiempo para no meter 2 pelotas de 1
    }

    public void iniciarTimer() {
        tiempoInicio = System.currentTimeMillis();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

  
        GradientPaint grad = new GradientPaint(0, 0, new Color(10, 10, 30), getWidth(), getHeight(), new Color(30, 0, 50));
        g2.setPaint(grad);
        g2.fillRect(0, 0, getWidth(), getHeight());

       
        g2.setColor(Color.GRAY);
        Stroke oldStroke = g2.getStroke();
        g2.setStroke(new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{15}, 0));
        g2.drawLine(getWidth() / 2, 0, getWidth() / 2, getHeight());
        g2.setStroke(oldStroke);

     
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // que las pelotas se vean redondas y de manera fluida 

     
        for (Pelota p : pelotas) {
            g2.setColor(p.getColor());
            g2.fillOval(p.getX(), p.getY(), p.getAncho(), p.getAlto()); // que las pelotas sean de diferentes cvolores y redondas
        }

    
        g2.setColor(new Color(255, 100, 100));
        g2.fill(r1.getRaqueta());

        g2.setColor(new Color(100, 150, 255));
        g2.fill(r2.getRaqueta());

      
        g2.setFont(new Font("Consolas", Font.BOLD, 40));
        g2.setColor(Color.WHITE);
        String textoP1 = String.valueOf(punj1);
        int anchoTextoP1 = g2.getFontMetrics().stringWidth(textoP1);
        g2.drawString(textoP1, getWidth() / 4 - anchoTextoP1 / 2, 60);

        String textoP2 = String.valueOf(puntj2);
        int anchoTextoP2 = g2.getFontMetrics().stringWidth(textoP2);
        g2.drawString(textoP2, getWidth() * 3 / 4 - anchoTextoP2 / 2, 60);

       
        long tiempoPasado = System.currentTimeMillis() - tiempoInicio;
        long tiempoRestante = Math.max(0, tiempoLimite - tiempoPasado);
        String textoTiempo = "Tiempo: " + (tiempoRestante / 1000) + "s";
        int anchoTiempo = g2.getFontMetrics().stringWidth(textoTiempo);
        g2.drawString(textoTiempo, getWidth() / 2 - anchoTiempo / 2, 60);

  
        if (juegoTerminado) {
            String ganador;
            if (punj1 > puntj2) {
                ganador = " GANA JUGADOR 1 ";
            } else if (puntj2 > punj1) {
                ganador = " GANA JUGADOR 2 ";
            } else {
                ganador = " EMPATE ";
            }
            g2.setFont(new Font("Consolas", Font.BOLD, 48));
            g2.setColor(Color.YELLOW);
            int anchoGanador = g2.getFontMetrics().stringWidth(ganador);

            //SOMBREADO
            g2.setColor(Color.BLACK);
            g2.drawString(ganador, getWidth() / 2 - anchoGanador / 2 + 3, getHeight() / 2 + 3);
            g2.setColor(Color.YELLOW);
            g2.drawString(ganador, getWidth() / 2 - anchoGanador / 2, getHeight() / 2);
        }
    }

    public void actualizar() {
        if (juegoTerminado) return;

        int margen = 10;
        r2.setX(getWidth() - margen - r2.getAncho());

        if (Teclado.w) {
            r1.moverArriba(getHeight());
        }
        if (Teclado.s) {
            r1.moverAbajo(getHeight());
        }
        if (Teclado.up) {
            r2.moverArriba(getHeight());
        }
        if (Teclado.down) {
            r2.moverAbajo(getHeight());
        }

        // Mover todas las pelotas y chequear colisiones
        for (Pelota p : pelotas) {
            boolean colisionR1 = p.getPelota().intersects(r1.getRaqueta());
            boolean colisionR2 = p.getPelota().intersects(r2.getRaqueta());
            int resultado = p.mover(getBounds(), colisionR1, colisionR2, r1, r2);

            if (resultado == 1) {
                puntj2++;
                resetearPelotas();
                break; // para no incrementar múltiples veces en el mismo ciclo
            } else if (resultado == 2) {
                punj1++;
                resetearPelotas();
                break;
            }
        }

        //CALCULAR TIEMPO 
        long tiempoPasado = System.currentTimeMillis() - tiempoInicio;
        int segundosPasados = (int) (tiempoPasado / 1000);

    
        int bloquesQuinceSeg = segundosPasados / 15;

  
        while (pelotasAgregadas < bloquesQuinceSeg) {
            agregarPelota();
            pelotasAgregadas++;
        }

   
        if (punj1 >= punmax || puntj2 >= punmax || tiempoPasado >= tiempoLimite) {
            juegoTerminado = true;
        }
    }

    private void agregarPelota() {
        Color colorAleatorio = new Color(rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        Pelota p = new Pelota(getWidth() / 2 - 7, getHeight() / 2 - 7, colorAleatorio);
        p.setDx(rnd.nextBoolean() ? 2 : -2);
        p.setDy(rnd.nextBoolean() ? 2 : -2);
        pelotas.add(p);
    }

    private void resetearPelotas() {
        for (Pelota p : pelotas) {
            p.setX(getWidth() / 2 - p.getAncho() / 2);
            p.setY(getHeight() / 2 - p.getAlto() / 2);
            p.setDx(rnd.nextBoolean() ? 2 : -2);
            p.setDy(rnd.nextBoolean() ? 2 : -2);
        }
    }


    public boolean isJuegoTerminado() {
        return juegoTerminado;
    }

    public void reiniciarJuego() {
        punj1 = 0;
        puntj2 = 0;
        juegoTerminado = false;
        tiempoInicio = System.currentTimeMillis();
        pelotas.clear();
        pelotas.add(new Pelota(0, 0, new Color(0, 255, 200)));
        pelotasAgregadas = 0;
        resetearPelotas();
        repaint();
    }

    public int getPuntajeJugador1() {
        return punj1;
    }

    public int getPuntajeJugador2() {
        return puntj2;
    }
}
