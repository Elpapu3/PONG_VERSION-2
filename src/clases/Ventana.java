package clases;

import javax.swing.*;
import java.awt.*;

public class Ventana extends JFrame {

    private static final long serialVersionUID = 1L;

    private final int ancho = 800;
    private final int alto = 500; 
    private Tablero lamina;
    private Hilo hilo;
    private JPanel panelBotones;
    private JButton btnVolverJugar;
    private JButton btnSalir;

    public Ventana(boolean mostrarContador) {
        setTitle("PONG");
        setSize(ancho, alto);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        lamina = new Tablero();
        add(lamina, BorderLayout.CENTER);

        addKeyListener(new Teclado());
        setFocusable(true);

        crearPanelBotones();

        setVisible(true);

        if (mostrarContador) {
            mostrarCuentaRegresiva();
            lamina.iniciarTimer();
            iniciarHilo();
        } else {
            lamina.iniciarTimer();
            iniciarHilo();
        }
    }

    private void crearPanelBotones() {
        panelBotones = new JPanel();
        panelBotones.setLayout(new FlowLayout());
        panelBotones.setBackground(Color.DARK_GRAY);

        btnVolverJugar = new JButton("Volver a jugar");
        btnSalir = new JButton("Salir");

        btnVolverJugar.setFont(new Font("Arial", Font.BOLD, 18));
        btnSalir.setFont(new Font("Arial", Font.BOLD, 18));

        btnVolverJugar.addActionListener(e -> {
           
            if (hilo != null && hilo.isAlive()) {
                hilo.detener();
            }
          
            limpiarTeclas();

            lamina.reiniciarJuego();
            panelBotones.setVisible(false);

          
            Ventana.this.requestFocusInWindow();

            iniciarHilo();
        });

        btnSalir.addActionListener(e -> {
            System.exit(0);
        });

        panelBotones.add(btnVolverJugar);
        panelBotones.add(btnSalir);

        panelBotones.setVisible(false);

        add(panelBotones, BorderLayout.SOUTH);
    }

    private void limpiarTeclas() {
        Teclado.w = false;
        Teclado.s = false;
        Teclado.up = false;
        Teclado.down = false;
    }

    private void iniciarHilo() {
      
        this.requestFocusInWindow();

        hilo = new Hilo(lamina);
        hilo.start();

        new Thread(() -> {
            while (true) {
                if (lamina.isJuegoTerminado()) {
                    hilo.detener();
                    SwingUtilities.invokeLater(() -> {
                        panelBotones.setVisible(true);
                    });
                    break;
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }).start();
    }

    private void mostrarCuentaRegresiva() {
        try {
            Graphics g = lamina.getGraphics();
            g.setFont(new Font("Arial", Font.BOLD, 80));
            g.setColor(Color.WHITE);

            for (int i = 3; i > 0; i--) {
                lamina.paintImmediately(0, 0, lamina.getWidth(), lamina.getHeight());
                g.drawString(String.valueOf(i), ancho / 2 - 20, alto / 2);
                Thread.sleep(1000);
            }

            lamina.paintImmediately(0, 0, lamina.getWidth(), lamina.getHeight());
            g.drawString("¡GO!", ancho / 2 - 60, alto / 2);
            Thread.sleep(800);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Ventana(true); 
        });
    }
}
