package sk.zimanyi;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Identifikacia sedeni na zaklade 30 minutoveho casoveho okna (STT).
 * 
 * @author tibor.zimanyi
 *
 */
public class IdentifikaciaSedeniOkno {

	public static void main(String[] args) throws IOException, ParseException {
		// ku pouzivatelovi je priradeny jeho posledny cas.
		final Map<Integer, Long> userIdToLastSessionTime = new HashMap<>();
		// ku pouzivatelovi je priradene jeho posledne session Id
		final Map<Integer, Integer> userIdtoSessionId = new HashMap<>();
		
		final BufferedWriter writer = new BufferedWriter(
                new FileWriter("ukflogIdentSedeniStt"));
        try {
            final File inputFile = new File(
            		"ukflogIdentPouz");
            final BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            try {
            	int sessionCounter = 0;
            	// 30 minut
            	long sessionCheckTimeInMillis = 30 * 60 * 1000;
                while (reader.ready()) {
                    final String line = reader.readLine().trim();
                    System.out.println(line);
                    final Integer userId = Integer.valueOf(line.substring(0, line.indexOf(" ")));
                    
                    // zistime si datum a cas.
                    final int dateTimeIndexTo = line.indexOf("]");
                    final int dateTimeIndexFrom = line.substring(0, dateTimeIndexTo).indexOf("[") + 1;
                    final String dateTimeField = line.substring(dateTimeIndexFrom, dateTimeIndexTo).toLowerCase();
                    
                    final Calendar dateTimeFromLineCal = Calendar.getInstance();
                    final DateFormat dateFormatter = new SimpleDateFormat("dd/MMM/yyyy:HH:mm:ss Z");
                    dateTimeFromLineCal.setTime(dateFormatter.parse(dateTimeField));
                    
                    Integer userIdlastSession = userIdtoSessionId.get(userId);
                    if (userIdlastSession == null) {
                    	// len sa poznacia posledne hodnoty.
                    	userIdlastSession = Integer.valueOf(++sessionCounter);
                    	userIdtoSessionId.put(userId, userIdlastSession);
                    	userIdToLastSessionTime.put(userId, dateTimeFromLineCal.getTimeInMillis());
                    } else {
                    	// porovnavanie casov.
                    	Long userIdLastSessionTime = userIdToLastSessionTime.get(userId);
                    	if (dateTimeFromLineCal.getTimeInMillis() - userIdLastSessionTime > sessionCheckTimeInMillis) {
                    		// len sa poznacia posledne hodnoty.
                        	userIdlastSession = Integer.valueOf(++sessionCounter);
                        	userIdtoSessionId.put(userId, userIdlastSession);
                        	userIdToLastSessionTime.put(userId, dateTimeFromLineCal.getTimeInMillis());
                    	}
                    }
                    
                    writer.write(userIdlastSession + " " + line + System.getProperty("line.separator"));
                }
            } finally {
                reader.close();
            }
        } finally {
            writer.close();
        } 
	}

}
