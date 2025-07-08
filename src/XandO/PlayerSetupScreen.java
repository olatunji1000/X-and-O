package XandO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PlayerSetupScreen extends JFrame {
    private JTextField player1Field;
    private JTextField player2Field;
    private boolean vsComputer;
    private boolean timedMode;

    // Updated constructor with timedMode
    public PlayerSetupScreen(boolean vsComputer, boolean timedMode) {
        this.vsComputer = vsComputer;
        this.timedMode = timedMode;
        setTitle("Player Setup");

        setLayout(new BorderLayout());

        // Title
        JLabel titleLabel = new JLabel("Enter Player Name(s)", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        add(titleLabel, BorderLayout.NORTH);

        // Center inputs
        JPanel inputPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        // Player 1 label and field (with size control)
        inputPanel.add(new JLabel("Player 1 Name:"));
        JPanel p1Wrapper = new JPanel(new FlowLayout(FlowLayout.LEFT));
        player1Field = new JTextField();
        player1Field.setFont(new Font("Arial", Font.PLAIN, 18));
        player1Field.setPreferredSize(new Dimension(250, 30));
        p1Wrapper.add(player1Field);
        inputPanel.add(p1Wrapper);

        // Player 2 only if not vsComputer
        if (!vsComputer) {
            inputPanel.add(new JLabel("Player 2 Name:"));
            JPanel p2Wrapper = new JPanel(new FlowLayout(FlowLayout.LEFT));
            player2Field = new JTextField();
            player2Field.setFont(new Font("Arial", Font.PLAIN, 18));
            player2Field.setPreferredSize(new Dimension(250, 30));
            p2Wrapper.add(player2Field);
            inputPanel.add(p2Wrapper);
        }

        add(inputPanel, BorderLayout.CENTER);

        // Continue button
        JButton continueButton = new JButton("Continue");
        continueButton.setFont(new Font("Arial", Font.PLAIN, 20));
        continueButton.setBackground(new Color(34, 139, 34));
        continueButton.setForeground(Color.WHITE);
        add(continueButton, BorderLayout.SOUTH);

        // Button action
        continueButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String player1 = player1Field.getText().trim();
                String player2 = (vsComputer ? "Computer" : player2Field.getText().trim());

                if (player1.isEmpty() || (!vsComputer && player2.isEmpty())) {
                    JOptionPane.showMessageDialog(PlayerSetupScreen.this,
                            "Please enter all required names.",
                            "Input Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                dispose();
                if (vsComputer) {
                    new GameScreenAI(player1, timedMode);  // Pass timed mode
                } else {
                    new GameScreen(player1, player2);
                }
            }
        });

        // Final window setup
        setSize(500, vsComputer ? 350 : 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
}
