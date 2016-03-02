package hyperbase;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Library {
    Library(Index index, Browser browser) {
        library = new HashMap<>();
        this.index = index;
        this.browser = browser;
    }
    
    private Map<Integer, Document> library;
    private File libraryFile;
    private final String libraryFileName = "/library.ser";
    private final Index index;
    Browser browser;
    
    public void set(File folderFile) {
        libraryFile = new File(folderFile.getAbsolutePath() + libraryFileName);
        if (libraryFile.exists()) {
            try {
                try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(folderFile.getAbsolutePath() + libraryFileName))) {
                    library = (Map<Integer, Document>) in.readObject();
                }
            } catch(IOException | ClassNotFoundException i) {
            }
        } else {
            saveLibrary();
        }
    }
    
    public void addDocument(String text) throws Exception {
        Document document = new Document(text);
        Integer ID = newID();
        library.put(ID, document);
        Iterator iterator = library.get(ID).getsubjects().entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry label = (Map.Entry)iterator.next();
            index.addIDBySubject((String) label.getKey(), ID);
            iterator.remove();
        }
        browser.scrollPane.add(new DocumentPanel(document, browser));
    }
    
    public void removeDocumentByID(Integer ID) {
        Iterator iterator = library.get(ID).getsubjects().entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry label = (Map.Entry)iterator.next();
            index.removeIDFromSubject((String) label.getKey(), ID);
            iterator.remove();
        }
        library.remove(ID);
        saveLibrary();
    }
    
    public Document getDocumentByID(Integer ID) {
        return library.get(ID);
    }
    
    public Map getLibrary() {
        return library;
    }
    
    public void saveLibrary() {
        try {
            FileOutputStream fileOut = new FileOutputStream(libraryFile.getAbsolutePath());
            try (ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
                out.writeObject(library);
            }
        } catch(IOException i) {
        }
    }
    
    private Integer newID() {
        Integer ID = 0;
        while (library.containsKey(ID)) {
            ID = ID + 1;
        }
        return ID;
    }
}