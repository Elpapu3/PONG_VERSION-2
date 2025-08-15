package clases;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuInicio extends JFrame {

    public MenuInicio() {
        setTitle("Menú Principal - Pong");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        JPanel panel = new JPanel();
        panel.setBackground(new Color(30, 30, 30));
        panel.setLayout(new BorderLayout());

        JLabel titulo = new JLabel("PONG", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 48));
        titulo.setForeground(Color.WHITE);
        panel.add(titulo, BorderLayout.CENTER);

        JButton botonJugar = new JButton("Jugar");
        botonJugar.setFont(new Font("Arial", Font.BOLD, 24));
        botonJugar.setBackground(new Color(0, 150, 0));
        botonJugar.setForeground(Color.WHITE);
        botonJugar.setFocusPainted(false);

        botonJugar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Cerrar menú
                new Ventana(true); // Abrir el juego con contador
            }
        });

        panel.add(botonJugar, BorderLayout.SOUTH);
        add(panel);

        setVisible(true);
    }

    public static void main(String[] args) {
        new MenuInicio();
    }
}