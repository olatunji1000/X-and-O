# X-and-O
package XandO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class PlayerSetupScreen extends JFrame {
    private JTextField player1Field;
    private JTextField player2Field;
    private JComboBox<String> avatar1Combo;
    private JComboBox<String> avatar2Combo;
    private JCheckBox timedModeToggle;
    private boolean vsComputer;
    private boolean timedMode;

    private static final int MAX_NAME_LENGTH = 12;

    public PlayerSetupScreen(boolean vsComputer, boolean timedMode) {
        this.vsComputer = vsComputer;
        this.timedMode = timedMode;
        setTitle("Player Setup");
        setLayout(new BorderLayout());

        // Title
        JLabel titleLabel = new JLabel("Enter Player Details", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        add(titleLabel, BorderLayout.NORTH);

        // Center input panel
        JPanel inputPanel = new JPanel(new GridLayout(10, 1, 10, 5));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        // Player 1 input
        inputPanel.add(new JLabel("Player 1 Name (max " + MAX_NAME_LENGTH + " chars):"));
        player1Field = new JTextField();
        JLabel p1CharCount = new JLabel("0/" + MAX_NAME_LENGTH);
        player1Field.setFont(new Font("Arial", Font.PLAIN, 16));
        player1Field.setPreferredSize(new Dimension(250, 30));
        player1Field.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                p1CharCount.setText(player1Field.getText().length() + "/" + MAX_NAME_LENGTH);
            }
        });
        inputPanel.add(player1Field);
        inputPanel.add(p1CharCount);

        // Avatar selection for Player 1
        avatar1Combo = new JComboBox<>(new String[]{"😀 Default", "😎 Cool", "👾 Alien", "🐱 Cat"});
        inputPanel.add(new JLabel("Choose Player 1 Avatar:"));
        inputPanel.add(avatar1Combo);

        if (!vsComputer) {
            // Player 2 input
            inputPanel.add(new JLabel("Player 2 Name (max " + MAX_NAME_LENGTH + " chars):"));
            player2Field = new JTextField();
            JLabel p2CharCount = new JLabel("0/" + MAX_NAME_LENGTH);
            player2Field.setFont(new Font("Arial", Font.PLAIN, 16));
            player2Field.setPreferredSize(new Dimension(250, 30));
            player2Field.addKeyListener(new KeyAdapter() {
                public void keyReleased(KeyEvent e) {
                    p2CharCount.setText(player2Field.getText().length() + "/" + MAX_NAME_LENGTH);
                }
            });
            inputPanel.add(player2Field);
            inputPanel.add(p2CharCount);

            // Avatar selection for Player 2
            avatar2Combo = new JComboBox<>(new String[]{"😀 Default", "😎 Cool", "👾 Alien", "🐱 Cat"});
            inputPanel.add(new JLabel("Choose Player 2 Avatar:"));
            inputPanel.add(avatar2Combo);
        }

        // Timed mode toggle
        timedModeToggle = new JCheckBox("Enable Timed Mode", timedMode);
        inputPanel.add(timedModeToggle);

        add(inputPanel, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton continueButton = new JButton("Continue");
        continueButton.setFont(new Font("Arial", Font.BOLD, 18));
        continueButton.setBackground(new Color(0, 120, 215));
        continueButton.setForeground(Color.WHITE);
        getRootPane().setDefaultButton(continueButton); // Press Enter to continue

        buttonPanel.add(continueButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Continue button action
        continueButton.addActionListener(e -> {
            String player1 = player1Field.getText().trim();
            String player2 = vsComputer ? "Computer" : player2Field.getText().trim();
            String avatar1 = (String) avatar1Combo.getSelectedItem();
            String avatar2 = vsComputer ? "🤖" : (String) avatar2Combo.getSelectedItem();
            boolean timed = timedModeToggle.isSelected();

            // Validation
            if (player1.isEmpty() || (!vsComputer && player2.isEmpty())) {
                JOptionPane.showMessageDialog(this,
                        "Please enter all required names.",
                        "Input Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (player1.length() > MAX_NAME_LENGTH || (!vsComputer && player2.length() > MAX_NAME_LENGTH)) {
                JOptionPane.showMessageDialog(this,
                        "Player names must be at most " + MAX_NAME_LENGTH + " characters.",
                        "Name Too Long",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            dispose();

            // Simulated use of avatars (optional in actual game)
            if (vsComputer) {
                new GameScreenAI(player1 + " " + avatar1, timed);  // Inject avatar emoji
            } else {
                new GameScreen(player1 + " " + avatar1, player2 + " " + avatar2);
            }
        });

        // Frame setup
        setSize(550, vsComputer ? 500 : 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }
}
