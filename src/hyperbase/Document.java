package hyperbase;

import java.util.Map;

public final class Document {
    Document(String text) throws Exception {
        parser = new Parser();
        updateBody(text);
    }
    
    private String body;
    public Map<String, Integer> subjects;
    private final Parser parser;
    
    public void updateBody(String text) throws Exception {
        body = text;
        subjects = parser.parse(body);
    }
    
    public String getBody() {
        return body;
    }
    
    public Map getsubjects() {
        return subjects;
    }
    
    public Integer getWeightOfsubject(String subject) {
        return this.subjects.get(subject);
    }
}