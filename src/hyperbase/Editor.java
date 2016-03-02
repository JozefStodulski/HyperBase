package hyperbase;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class Editor extends JFrame{
    Editor(Library library) {
        super("Document Editor");
        setLayout(new BorderLayout());
        
        JPanel savePanel = new JPanel();
        savePanel.setLayout(new GridBagLayout());
        add(savePanel, BorderLayout.NORTH);
        
        GridBagConstraints right = new GridBagConstraints();
        right.anchor = GridBagConstraints.WEST;
        
        textBox = new JTextArea();
        textBox.setLineWrap(true);
        add(textBox, BorderLayout.CENTER);
        
        saveButton = new JButton("Save");
        savePanel.add(saveButton, right);
        
        saveButton.addActionListener((ActionEvent e) -> {
            setVisible(false);
            if (! textBox.getText().equals("")) {
                if (newMode) {
                    try {
                        library.addDocument(textBox.getText());
                    } catch (Exception ex) {
                        Logger.getLogger(Editor.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    try {
                        document.updateBody(textBox.getText());
                    } catch (Exception ex) {
                        Logger.getLogger(Editor.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                library.saveLibrary();
                textBox.setText("");
            }
        });
    }
    
    public void setNewMode(Boolean newMode) {
        this.newMode = newMode;
    }
    
    public void editDocument(Document document) {
        this.document = document;
    }

    public JTextArea textBox;
    private final JButton saveButton;
    private Boolean newMode;
    private Document document;
}