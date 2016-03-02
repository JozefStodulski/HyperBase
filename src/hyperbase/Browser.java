package hyperbase;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.File;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Iterator;

public final class Browser extends JFrame{
    public Browser(){
        super("Browser");
        setLayout(new BorderLayout());
        
        searchPanel = new JPanel();
        searchPanel.setLayout(new GridBagLayout());
        add(searchPanel, BorderLayout.SOUTH);
        
        filePanel = new JPanel();
        filePanel.setLayout(new GridBagLayout());
        add(filePanel, BorderLayout.NORTH);
        
        GridBagConstraints left = new GridBagConstraints();
        left.anchor = GridBagConstraints.EAST;
        GridBagConstraints right = new GridBagConstraints();
        right.anchor = GridBagConstraints.WEST;
        
        JButton openButton = new JButton("Set Directory");
        filePanel.add(openButton, left);
        
        JLabel pathLabel = new JLabel("No directory set");
        filePanel.add(pathLabel, left);
        
        JButton createButton = new JButton("Add Document");
        createButton.setEnabled(false);
        filePanel.add(createButton, right);
        
        scrollPane = new JScrollPane();
        add(scrollPane, BorderLayout.CENTER);
        
        JTextField searchBox = new JTextField(20);
        searchBox.setEnabled(false);
        searchPanel.add(searchBox, left);
        
        JButton searchButton = new JButton("Search");
        searchButton.setEnabled(false);
        searchPanel.add(searchButton, left);
        
//        JButton showMiscButton = new JButton("Show Miscellaneous Documents");
//        searchButton.setEnabled(false);
//        searchPanel.add(showMiscButton, right);
        
        index = new Index();
        library = new Library(index, this);
        chooser = new Chooser();
        editor = new Editor(library);
        editor.setSize(600,400);
        
        openButton.addActionListener((ActionEvent e) -> {
            chooser.showOpenDialog(null);
            if (chooser.getSelectedFile() != null) {
                folderFile = chooser.getSelectedFile();
                index.set(folderFile);
                library.set(folderFile);
                pathLabel.setText(folderFile.getName());
                createButton.setEnabled(true);
                searchBox.setEnabled(true);
                searchButton.setEnabled(true);
            }
        });
        
        createButton.addActionListener((ActionEvent e) -> {
            editor.setNewMode(true);
            editor.setVisible(true);
        });
        
        searchButton.addActionListener((ActionEvent e) -> {
            showDocumentsOfsubject(searchBox.getText());
        });
        
//        showMiscButton.addActionListener((ActionEvent e) -> {
//            Iterator iterator = library.getLibrary().entrySet().iterator();
//            while (iterator.hasNext()) {
//                Map.Entry entry = (Map.Entry)iterator.next();
//                Document document = entry.getValue();
//                entry.getValue()
//            }
//            for (Map.Entry<Integer, Document> entry : library.getLibrary().entrySet()) {
//                
//            }
//        });
    }
    
    public void showDocumentsOfsubject(String subject) {
        if (index.containsSubject(subject)) {
            Iterator iterator = index.getIDsOfSubject(subject).iterator();
            ArrayList<Document> documents = new ArrayList();
            while (iterator.hasNext()) {
                Integer ID = (Integer) iterator.next();
                documents.add(library.getDocumentByID(ID));
            }
            int n = documents.size();
            while (n != 0) {
                int m = 0;
                for (int i = 1; i < n; i++) {
                    if (documents.get(i-1).getWeightOfsubject(subject) > documents.get(i).getWeightOfsubject(subject)) {
                        Document higher = documents.get(i-1);
                        Document lower = documents.get(i);
                        documents.add(i, higher);
                        documents.add(i-1, lower);
                        m = i;
                    }
                }
                n = m;
            }
            for (int i = 0; i < 4; i++) {
                scrollPane.add(new DocumentPanel(documents.get(i), this));
            }
        }
    }
    
    private Chooser chooser;
    public Editor editor;
    public Library library;
    private File folderFile;
    public Index index;
    
    private final JPanel searchPanel;
    private final JPanel filePanel;
    public final JScrollPane scrollPane;
}