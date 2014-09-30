package sk.zimanyi;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Identifikacia pouzivatelov na zaklade IP a pola user-agent
 * 
 * @author tibor.zimanyi
 *
 */
public class IdentifikaciaPouzivatelov {

	public static void main(String[] args) throws IOException {
		final Map<String, Integer> userToId = new HashMap<>();
		
		final BufferedWriter writer = new BufferedWriter(
                new FileWriter("ukflogIdentPouz"));
        try {
            final File inputFile = new File(
            		"ukflogCistenie2");
            final BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            try {
            	int counterId = 0;
                while (reader.ready()) {
                    final String line = reader.readLine().trim();
                    System.out.println(line);
                    final String textIp = line.substring(0, line.indexOf(" "));
                    
                    // Kedze oddelovac v subore su medzery, tak to musime takto krepo, lebo polia tym padom 
                    // nemaju jednoznacny oddelovac, na zaklade, ktoreho by sa dali oddelit.
                    final int userAgentIndexTo = line.lastIndexOf("\"");
                    final int userAgentIndexFrom = line.substring(0, userAgentIndexTo).lastIndexOf("\"") + 1;
                    final String userAgentField = line.substring(userAgentIndexFrom, userAgentIndexTo).toLowerCase();
                    
                    // ked sa da ip a user agent do jedneho retazca, tak je tento novy retazec pre jedneho pouzivatela
                    // jedinecny.
                    final String userKey = textIp + userAgentField;
                    Integer userId = userToId.get(userKey);
                    if (userId == null) {
                    	userId = ++counterId;
                    	userToId.put(userKey, userId);
                    }  
                    writer.write(userId + " " + line + System.getProperty("line.separator"));
                }
            } finally {
                reader.close();
            }
        } finally {
            writer.close();
        } 

	}

}
