package XandO;

import javax.swing.*;

public class App {
    public static void main(String[] args) {
        // Run GUI on Event Dispatch Thread (best practice for Swing)
        SwingUtilities.invokeLater(() -> {
            new WelcomeScreen(); // Launch the welcome screen
        });
    }
}
