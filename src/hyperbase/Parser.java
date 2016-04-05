package hyperbase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Parser {
    Parser() {
        subjects = new HashMap<>();
    }
    
    public Map<String, Integer> parse(String text) throws Exception {
        words = text.toLowerCase().split("[\\s]");
        for (int i = 0; i < words.length; i++) {
            words[i] = words[i].replaceAll("[\\W]", "");
        }
        
        for (String word : words) {
            if (wikidataHasItemLabeled(word)) {                                 //check if item labeled word is in wikidata
                labelsOfvaluesOfproperties().stream().map((String label) -> {   //iterate item's properties' values' labels
                    if (! subjects.containsKey(label)) {
                        subjects.put(label, 1);
                    }
                    return label;
                }).forEach((label) -> {
                    subjects.put(label, subjects.get(word) + 1);
                });
                if (! subjects.containsKey(word)) {
                    subjects.put(word, 1);
                }
                subjects.put(word, subjects.get(word) + 1);
                
                //ADD LABELS OF ITEMS WITH PROPERTY WITH VALUE ITEM !!!
            }
        }
        return subjects;
    }
    
    private final Map<String, Integer> subjects;
    private String[] words;
    private JSONObject wikidataItem;
    
    private Boolean wikidataHasItemLabeled(String word) throws Exception {
        String searchItem = getByURL("https://www.wikidata.org/w/api.php?action=wbsearchentities&search=" + word + "&format=json&language=en&limit=1");
        JSONObject resultsObject = new JSONObject(searchItem);
        JSONArray search = resultsObject.getJSONArray("search");
        if (search.isNull(0)) {
            return false;
        }
        if (word.equals(search.getJSONObject(0).getString("label").toLowerCase())) {
            wikidataItem = resultsObject;
            return true;
        } else {
            return false;
        }
    }
    
    private ArrayList<String> labelsOfvaluesOfproperties() throws Exception {
        ArrayList<String> items = new ArrayList<>();
        
        //Search 
        JSONArray result = wikidataItem.getJSONArray("search");
        JSONObject resultItem = result.getJSONObject(0);
        String ID = resultItem.getString("id");
        String item = getByURL("https://www.wikidata.org/w/api.php?action=wbgetclaims&format=json&props=value&entity=" + ID);
        JSONObject itemObj = new JSONObject(item);
        JSONObject claims = itemObj.getJSONObject("claims");
        //GET numeric-IDs
        String labelsRequest = "https://www.wikidata.org/w/api.php?action=wbgetentities&props=labels&format=json&languages=en&ids=";
        Iterator<String> keys = claims.keys();
        while (keys.hasNext()) {
            JSONObject claim = claims.getJSONArray((String) keys.next()).getJSONObject(0);
            JSONObject mainsnak = claim.getJSONObject("mainsnak");
            if (! mainsnak.has("datavalue")) {
                continue;
            }
            JSONObject datavalue = mainsnak.getJSONObject("datavalue");
            if (! datavalue.has("value")) {
                continue;
            }
            JSONObject value;
            try {
                value = datavalue.getJSONObject("value");
            } catch (JSONException e) {
                continue;
            }
            if (! value.toString().contains("numeric-id")) {
                continue;
            }
            String nID = value.get("numeric-id").toString();
            
            labelsRequest = labelsRequest + "Q" + nID + "|";
        }
        labelsRequest = labelsRequest.substring(0, labelsRequest.length() - 1);
        String itemsLabels = getByURL(labelsRequest);
        JSONObject itemsLabelsObject = new JSONObject(itemsLabels);
        JSONObject entities = itemsLabelsObject.getJSONObject("entities");
        keys = entities.keys();
        while (keys.hasNext()) {
            items.add(entities.getJSONObject(keys.next()).getJSONObject("labels").getJSONObject("en").get("value").toString());
        }
        System.out.println("labels:");
        System.out.println(items);
        
        return items;
    }
    
    private static String getByURL(String targetURL) throws Exception {
        URL url;
        BufferedReader reader = null;
        StringBuilder stringBuilder;
        
        try {
            url = new URL(targetURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            
            connection.setRequestMethod("GET");
            connection.setReadTimeout(15000);
            connection.connect();
            
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            stringBuilder = new StringBuilder();
            
            String line;
            while((line = reader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
            return stringBuilder.toString();
        } catch (Exception e) {
            throw e;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ioe) {
                    
                }
            }
        }
    }
}

//CALL:
// https://www.wikidata.org/w/api.php?action=wbgetclaims&format=json&props=value&entity=Q2
//TO GET ALL CLAIMS FOR ENTITY (Q2):
//PARSE RESULT FOR:
// property > value > numeric-id
//TO GET ID NUMBERS -> 459173 544 3241540 185969 715269 27527
//CALL:
// https://www.wikidata.org/w/api.php?action=wbgetentities&props=labels&format=json&languages=en&ids=Q459173|Q544|Q3241540|Q185969|Q715269|Q27527
//TO GET ALL ITEM LABELS
//PARSE RESULT FOR:
// entities > entity > value




//String call = "https://www.wikidata.org/w/api.php?action=wbsearchentities&search=" + word + "&language=en&limit=1";

//FileReader reader = new FileReader(jsonFileURL);
//JSONObject jsonObject = (JSONObject) new JSONParser(),parse(reader)
//jsonObject.get(propertyString);
//https://www.wikidata.org/w/api.php?action=wbsearchentities&format=json&search=abc&language=en