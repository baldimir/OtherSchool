package sk.zimanyi;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

/**
 * Ocistenie dat od vyhladavacich sluzieb. 
 * 
 * @author tibor.zimanyi
 *
 */
public class CistenieDat2 {

	public static void main(String[] args) throws IOException {
		
		final Map<String, String> ipToHostMap = new HashMap<>();
		
		final BufferedWriter writer = new BufferedWriter(
                new FileWriter("d:\\UserData\\baldimir\\Downloads\\ukflogCistenie2"));
        try {
            final File inputFile = new File(
            		"d:\\UserData\\baldimir\\Downloads\\ukflog");
            final BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            try {
                while (reader.ready()) {
                    final String line = reader.readLine().trim();
                    System.out.println(line);
                    final String textIp = line.substring(0, line.indexOf(" "));
                    String host = ipToHostMap.get(textIp); 
                    if (host == null) {
                        final InetAddress ipAddress =
                                Inet4Address.getByName(textIp);
                        host = ipAddress.getHostName().toLowerCase();
                        ipToHostMap.put(textIp, host);
                    }
                    
                    // Mame host, treba este skontrolovat user-agent polozku ci to nie je nejaky bot.
                    // Kedze oddelovac v subore su medzery, tak to musime takto krepo, lebo polia tym padom 
                    // nemaju jednoznacny oddelovac, na zaklade, ktoreho by sa dali oddelit.
                    // Cistenie robi okrem cistenia 2 snad aj cistenie 1 - neviem co presne sa malo cistit v 1ke, 
                    // tak som podla logov nieco vybral.
                    final int userAgentIndexTo = line.lastIndexOf("\"");
                    final int userAgentIndexFrom = line.substring(0, userAgentIndexTo).lastIndexOf("\"") + 1;
                    final String userAgentField = line.substring(userAgentIndexFrom, userAgentIndexTo).toLowerCase();
                    
					if (!host.contains("bot") && !host.contains("crawl") && !host.contains("scan")
							&& !userAgentField.contains("bot") && !userAgentField.contains("crawl") 
									&& !userAgentField.contains("scan") && !userAgentField.contains("baidu") 
									&& !userAgentField.equals("-") && !userAgentField.equals("zmeu")) {
                    	String newLine = line.replace(textIp, textIp + " " + host);
                    	writer.write(newLine + System.getProperty("line.separator"));
                    }
                }
            } finally {
                reader.close();
            }
        } finally {
            writer.close();
        } 
	}

}
