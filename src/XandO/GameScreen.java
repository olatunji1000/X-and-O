package XandO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GameScreen extends JFrame {
    private JButton[][] buttons = new JButton[3][3];
    private boolean isPlayer1Turn = true;
    private String player1Name;
    private String player2Name;
    private JLabel turnLabel;
    private JLabel timerLabel;
    private int moveCount = 0;

    private Timer turnTimer;
    private int timeLeft = 9;

    public GameScreen(String player1, String player2) {
        this.player1Name = player1;
        this.player2Name = player2;

        setTitle("Tic Tac Toe - " + player1 + " vs " + player2);
        setLayout(new BorderLayout());

        // Turn display
        JPanel topPanel = new JPanel(new GridLayout(2, 1));
        turnLabel = new JLabel(player1Name + "'s Turn (X)", JLabel.CENTER);
        turnLabel.setFont(new Font("Arial", Font.BOLD, 20));
        turnLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        timerLabel = new JLabel("Time left: 9s", JLabel.CENTER);
        timerLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        timerLabel.setForeground(Color.RED);
        timerLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        topPanel.add(turnLabel);
        topPanel.add(timerLabel);
        add(topPanel, BorderLayout.NORTH);

        // Game board
        JPanel gridPanel = new JPanel(new GridLayout(3, 3, 5, 5));
        gridPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        initializeBoard(gridPanel);

        add(gridPanel, BorderLayout.CENTER);

        setSize(400, 470);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        startTimer(); // Start the first turn timer
    }

    private void initializeBoard(JPanel panel) {
        Font font = new Font("Arial", Font.BOLD, 48);
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                JButton btn = new JButton("");
                btn.setFont(font);
                buttons[row][col] = btn;

                final int r = row;
                final int c = col;

                btn.addActionListener(e -> handleMove(r, c));
                panel.add(btn);
            }
        }
    }

    private void handleMove(int row, int col) {
        JButton btn = buttons[row][col];
        if (!btn.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Cell already taken!", "Invalid Move", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String currentSymbol = isPlayer1Turn ? "X" : "O";
        btn.setText(currentSymbol);
        moveCount++;

        if (turnTimer != null) turnTimer.stop(); // Stop timer after a valid move

        if (checkWin(currentSymbol)) {
            String winner = isPlayer1Turn ? player1Name : player2Name;
            showResultDialog(winner + " wins!");
            return;
        }

        if (moveCount == 9) {
            showResultDialog("It’s a draw!");
            return;
        }

        isPlayer1Turn = !isPlayer1Turn;
        updateTurnDisplay();
        startTimer(); // Restart timer for next player
    }

    private void updateTurnDisplay() {
        turnLabel.setText((isPlayer1Turn ? player1Name : player2Name) + "'s Turn (" + (isPlayer1Turn ? "X" : "O") + ")");
    }

    private void startTimer() {
        timeLeft = 9;
        timerLabel.setText("Time left: 9s");

        if (turnTimer != null) turnTimer.stop(); // Reset any previous timer

        turnTimer = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                timeLeft--;
                timerLabel.setText("Time left: " + timeLeft + "s");

                if (timeLeft == 0) {
                    turnTimer.stop();
                    JOptionPane.showMessageDialog(GameScreen.this,
                            "Time's up! Turn skipped.",
                            "Timeout",
                            JOptionPane.WARNING_MESSAGE);
                    isPlayer1Turn = !isPlayer1Turn;
                    updateTurnDisplay();
                    startTimer(); // Start new turn timer
                }
            }
        });
        turnTimer.start();
    }

    private boolean checkWin(String symbol) {
        for (int i = 0; i < 3; i++) {
            if (buttons[i][0].getText().equals(symbol) &&
                    buttons[i][1].getText().equals(symbol) &&
                    buttons[i][2].getText().equals(symbol)) return true;

            if (buttons[0][i].getText().equals(symbol) &&
                    buttons[1][i].getText().equals(symbol) &&
                    buttons[2][i].getText().equals(symbol)) return true;
        }

        return (buttons[0][0].getText().equals(symbol) &&
                buttons[1][1].getText().equals(symbol) &&
                buttons[2][2].getText().equals(symbol))
                || (buttons[0][2].getText().equals(symbol) &&
                buttons[1][1].getText().equals(symbol) &&
                buttons[2][0].getText().equals(symbol));
    }

    private void showResultDialog(String message) {
        if (turnTimer != null) turnTimer.stop();

        int choice = JOptionPane.showOptionDialog(this,
                message + "\nWhat would you like to do?",
                "Game Over",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                new Object[]{"Play Again", "New Players", "Exit"},
                "Play Again");

        if (choice == 0) {
            dispose();
            new GameScreen(player1Name, player2Name);
        } else if (choice == 1) {
            dispose();
            new PlayerSetupScreen(false, false); // Human vs Human, no timer mode flag needed
        } else {
            System.exit(0);
        }
    }
}
