package hyperbase;

import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;

public class Hyperbase {
    public static void main(String[] args) {
        browserWindow.setBounds(0, 0, 600, screenSize.height);
        browserWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        browserWindow.setVisible(true);
    }
    private static final Browser browserWindow = new Browser();
    private static final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
}