package XandO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GameScreenAI {

    JFrame frame = new JFrame("X0X0 - Vs Computer (Minimax)");
    JButton[] buttons = new JButton[9];
    boolean playerTurn = true;
    int moveCount = 0;

    JLabel timerLabel;
    Timer turnTimer;
    int timeLeft = 9;

    boolean timedMode; // ✅ new flag

    public GameScreenAI(String playerName, boolean timedMode) {
        this.timedMode = timedMode;

        JPanel panel = new JPanel(new GridLayout(3, 3));
        Font font = new Font("Arial", Font.BOLD, 36);

        // Timer label only if timed
        if (timedMode) {
            timerLabel = new JLabel("Time left: 9s", JLabel.CENTER);
            timerLabel.setFont(new Font("Arial", Font.PLAIN, 16));
            timerLabel.setForeground(Color.RED);
            frame.add(timerLabel, BorderLayout.NORTH);
        }

        // Setup buttons
        for (int i = 0; i < 9; i++) {
            buttons[i] = new JButton();
            buttons[i].setFont(font);
            buttons[i].setFocusPainted(false);
            panel.add(buttons[i]);

            final int index = i;
            buttons[i].addActionListener(e -> {
                if (buttons[index].getText().equals("") && playerTurn) {
                    buttons[index].setText("X");
                    buttons[index].setEnabled(false);
                    moveCount++;
                    playerTurn = false;

                    if (timedMode && turnTimer != null) turnTimer.stop();

                    if (!checkGameOver("X")) {
                        // Add delay before computer moves
                        Timer delayTimer = new Timer(600, ev -> makeComputerMove());
                        delayTimer.setRepeats(false);
                        delayTimer.start();
                    }
                }
            });
        }

        frame.add(panel, BorderLayout.CENTER);
        frame.setSize(300, timedMode ? 340 : 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        if (timedMode) startTimer();
    }

    private void startTimer() {
        timeLeft = 9;
        timerLabel.setText("Time left: 9s");

        if (turnTimer != null) turnTimer.stop();

        turnTimer = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                timeLeft--;
                timerLabel.setText("Time left: " + timeLeft + "s");

                if (timeLeft == 0) {
                    turnTimer.stop();
                    JOptionPane.showMessageDialog(frame,
                            "Time's up! Turn skipped.",
                            "Timeout",
                            JOptionPane.WARNING_MESSAGE);

                    playerTurn = false;

                    Timer delay = new Timer(600, ev -> makeComputerMove());
                    delay.setRepeats(false);
                    delay.start();
                }
            }
        });
        turnTimer.start();
    }

    private void makeComputerMove() {
        int bestScore = Integer.MIN_VALUE;
        int bestMove = -1;

        for (int i = 0; i < 9; i++) {
            if (buttons[i].getText().equals("")) {
                buttons[i].setText("O");
                int score = minimax(false);
                buttons[i].setText("");

                if (score > bestScore) {
                    bestScore = score;
                    bestMove = i;
                }
            }
        }

        if (bestMove != -1) {
            buttons[bestMove].setText("O");
            buttons[bestMove].setEnabled(false);
            moveCount++;
            playerTurn = true;

            if (!checkGameOver("O")) {
                if (timedMode) startTimer();
            }
        }
    }

    private int minimax(boolean isMaximizing) {
        if (checkWinner("O")) return 10;
        if (checkWinner("X")) return -10;
        if (isDraw()) return 0;

        int bestScore = isMaximizing ? Integer.MIN_VALUE : Integer.MAX_VALUE;

        for (int i = 0; i < 9; i++) {
            if (buttons[i].getText().equals("")) {
                buttons[i].setText(isMaximizing ? "O" : "X");
                int score = minimax(!isMaximizing);
                buttons[i].setText("");

                if (isMaximizing) {
                    bestScore = Math.max(score, bestScore);
                } else {
                    bestScore = Math.min(score, bestScore);
                }
            }
        }

        return bestScore;
    }

    private boolean isDraw() {
        for (JButton button : buttons) {
            if (button.getText().equals("")) return false;
        }
        return true;
    }

    private boolean checkWinner(String symbol) {
        int[][] combos = {
                {0,1,2}, {3,4,5}, {6,7,8},
                {0,3,6}, {1,4,7}, {2,5,8},
                {0,4,8}, {2,4,6}
        };

        for (int[] c : combos) {
            if (buttons[c[0]].getText().equals(symbol) &&
                    buttons[c[1]].getText().equals(symbol) &&
                    buttons[c[2]].getText().equals(symbol)) {
                return true;
            }
        }
        return false;
    }

    private boolean checkGameOver(String symbol) {
        if (checkWinner(symbol)) {
            if (timedMode && turnTimer != null) turnTimer.stop();
            showResultDialog((symbol.equals("X") ? "You win!" : "Computer wins!"));
            return true;
        }

        if (moveCount == 9) {
            if (timedMode && turnTimer != null) turnTimer.stop();
            showResultDialog("It’s a draw!");
            return true;
        }

        return false;
    }

    private void showResultDialog(String message) {
        int choice = JOptionPane.showOptionDialog(frame,
                message + "\nWhat would you like to do?",
                "Game Over",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                new Object[]{"Play Again", "New Players", "Main Menu"},
                "Play Again");

        if (choice == 0) {
            resetGame();
        } else if (choice == 1) {
            frame.dispose();
            new PlayerSetupScreen(true, timedMode); // carry over mode
        } else if (choice == 2) {
            frame.dispose();
            new WelcomeScreen();
        }
    }

    private void resetGame() {
        for (JButton button : buttons) {
            button.setText("");
            button.setEnabled(true);
        }
        playerTurn = true;
        moveCount = 0;
        if (timedMode) startTimer();
    }
}
