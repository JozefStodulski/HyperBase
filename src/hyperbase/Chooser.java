package hyperbase;

import javax.swing.JFileChooser;

public class Chooser extends JFileChooser{
    Chooser() {
        setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    }
}