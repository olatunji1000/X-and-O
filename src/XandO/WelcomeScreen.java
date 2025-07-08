package XandO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class WelcomeScreen extends JFrame {

    public WelcomeScreen() {
        setTitle("🎮 X0X0");

        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                Color color1 = new Color(135, 206, 250);
                Color color2 = new Color(255, 255, 255);
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, getHeight(), color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel("X0X0", JLabel.CENTER);
        titleLabel.setFont(new Font("Verdana", Font.BOLD, 36));
        titleLabel.setForeground(new Color(25, 25, 112));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(40, 0, 20, 0));
        mainPanel.add(titleLabel);

        JButton startButton = new JButton("Start Game");
        startButton.setFont(new Font("Arial", Font.BOLD, 20));
        startButton.setFocusPainted(false);
        startButton.setBackground(new Color(60, 179, 113));
        startButton.setForeground(Color.WHITE);
        startButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        startButton.setContentAreaFilled(false);
        startButton.setOpaque(true);

        startButton.addActionListener(e -> {
            String[] options = {"Vs Player", "Vs Computer"};
            int mode = JOptionPane.showOptionDialog(
                    WelcomeScreen.this,
                    "Choose Game Mode",
                    "Select Mode",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]);

            dispose();

            if (mode == 0) {
                new PlayerSetupScreen(false, false); // Vs Player, no difficulty needed
            } else if (mode == 1) {
                String[] levels = {"Easy", "Timed"};
                int difficulty = JOptionPane.showOptionDialog(
                        WelcomeScreen.this,
                        "Choose Difficulty",
                        "Game Difficulty",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        levels,
                        levels[0]);

                boolean timedMode = (difficulty == 1);
                new PlayerSetupScreen(true, timedMode); // Vs Computer with difficulty
            }
        });

        mainPanel.add(startButton);
        add(mainPanel);
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
}

