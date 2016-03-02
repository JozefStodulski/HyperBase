package hyperbase;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;
import java.util.HashMap;
import java.util.HashSet;

public class Index {
    Index() {
        index = new HashMap<>();
    }
    private Map<String, HashSet<Integer>> index;
    private File indexFile;
    private final String indexFileName = "/index.ser";
    
    public void set(File folderFile) {
        indexFile = new File(folderFile.getAbsolutePath() + indexFileName);
        if (indexFile.exists()) {
            try {
                try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(folderFile.getAbsolutePath() + indexFileName))) {
                    index = (Map<String, HashSet<Integer>>) in.readObject();
                }
            } catch(IOException | ClassNotFoundException i) {
            }
        } else {
            saveIndex();
        }
    }
    
    public void addIDBySubject(String subject, Integer ID) {
        if (! index.containsKey(subject.toLowerCase())) {
            index.put(subject.toLowerCase(), new HashSet<>());
        }
        index.get(subject.toLowerCase()).add(ID);
        saveIndex();
    }
    
    public HashSet getIDsOfSubject(String subject) {
        return index.get(subject.toLowerCase());
    }
    
    public void removeIDFromSubject(String subject, Integer ID) {
        index.get(subject.toLowerCase()).remove(ID);
        if (index.get(subject.toLowerCase()).isEmpty()) {
            index.remove(subject.toLowerCase());
        }
        saveIndex();
    }
    
    public Boolean containsSubject (String subject) {
        return index.containsKey(subject.toLowerCase());
    }
    
    private void saveIndex(){
        try {
            FileOutputStream fileOut = new FileOutputStream(indexFile.getAbsolutePath());
            try (ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
                out.writeObject(index);
            }
        } catch(IOException i) {
        }
    }
}