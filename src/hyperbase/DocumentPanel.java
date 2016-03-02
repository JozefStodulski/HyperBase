package hyperbase;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class DocumentPanel extends JPanel{
    DocumentPanel(Document document, Browser browser) {
        this.editor = browser.editor;
        
        setLayout(new BorderLayout());
        
        JPanel documentPanel = new JPanel();
        documentPanel.setLayout(new GridBagLayout());
        add(documentPanel, BorderLayout.NORTH);
        
        GridBagConstraints right = new GridBagConstraints();
        right.anchor = GridBagConstraints.WEST;
        
        words = document.getBody().split("^\\s+");
        for (int i = 0; i < words.length; i++) {
            words[i] = words[i].replaceAll("[^\\w]", "");
        }
        for (String word : words) {
            JLabel label = new JLabel(word);
            if (browser.index.containsSubject(word)) {
                label.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        browser.showDocumentsOfsubject(word);
                    }
                });
                label.setForeground(Color.BLUE);
                label.setBackground(Color.LIGHT_GRAY);
            }
            add(label);
        }
        
        JButton deleteButton = new JButton("Delete");
        documentPanel.add(deleteButton, right);
        
        JButton editButton = new JButton("Edit");
        documentPanel.add(editButton, right);
        
        deleteButton.addActionListener((ActionEvent e) -> {
            setVisible(false);
            browser.library.removeDocumentByID(ID);
        });
        
        editButton.addActionListener((ActionEvent e) -> {
            editor.setNewMode(false);
            editor.editDocument(document);
            editor.setVisible(true);
        });
    }
    
    private Editor editor;
    private Integer ID;
    private final String[] words;
}