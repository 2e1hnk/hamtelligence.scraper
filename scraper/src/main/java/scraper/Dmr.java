package scraper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import com.google.gson.Gson;

import scraper.station.Station;
import scraper.station.Identifier;

public class Dmr extends Collector {
	
	private boolean running = true;
	
	Config config;
	
	private String url = "http://www.dmr-marc.net/cgi-bin/trbo-database/datadump.cgi?table=users&format=json";
	private String ukLastHeardUrl = "http://www.opendmr.net/uk_lastusers_nodate.php?limit=1000";
	
	private String mototrboTemplate = "DP4800-template.xml";
	private String mototrboOutput = "DP4800-%s.xml";
	
	public Dmr() {
		config = new Config("dmr.properties");
	}
	
	public void stop() {
		this.running = false;
	}

	public void run() {
		while(this.running) {
			try {
				if ( ( (Scraper.getTime().getTime() / 1000L) - config.getPropertyAsInt("last_run") ) > config.getPropertyAsInt("refresh_interval") ) {
					Scraper.getLogger().info("Updating DMR");
					this.doUpdate();
					Scraper.getLogger().info("DMR Updated");
					config.setProperty("last_run", (Scraper.getTime().getTime()/1000L));
					config.save();
				}
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void doUpdate() {
		this.updateRegistrations();
		this.updateLastHeard();
	}
	
	private void updateRegistrations() {
	    BufferedReader reader = null;
	    try {
	        URL url = new URL(this.url);
	        reader = new BufferedReader(new InputStreamReader(url.openStream()));
	        StringBuffer buffer = new StringBuffer();
	        int read;
	        char[] chars = new char[1024];
	        
	        while ((read = reader.read(chars)) != -1) {
	            buffer.append(chars, 0, read);
	        }

	        Gson gson = new Gson();        
	        Data json = gson.fromJson(buffer.toString(), Data.class);
	        
	        int motorolaCounter = 0;
	        
	        for (User user : json.users) {
	        	Station station = Scraper.fetchStation(user.callsign);
	        	station.setAttribute("country", user.country != null ? user.country : "");
	        	station.setAttribute("name", user.name != null ? user.name : "");
	        	station.setAttribute("surname", user.surname != null ? user.surname : "");
	        	if ( user.radio_id != null ) {
	        		station.addDmr_id(Long.parseLong(user.radio_id));
	        	}
	        	station.setAttribute("state", user.state != null ? user.state : "");
	        	station.setAttribute("city", user.city != null ? user.city : "");
	        	station.setAttribute("remarks", user.remarks != null ? user.remarks : "");
	        	
	        	// Save station details to MongoDB
	        	Scraper.saveStation(station);
	        	
	        	try {
					Thread.sleep(150);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
	        	
	        }
	        
	        
	        //Scraper.log(Scraper.LOG_DEBUG, json.toString());
	        
	    } catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
	        if (reader != null)
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
	    }
	    
	}
	
    private void updateLastHeard() {
    	String replaceString = "INSERT_CONTACTS";
    	String contactXml = "";
	    BufferedReader reader = null;
	    try {
	        URL url = new URL(this.ukLastHeardUrl);
	        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

	        int motorolaCounter = 0;
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
            	String[] lines = inputLine.split("<BR>");
            	for ( String line : lines ) {
            		String[] parts = line.split(",");
	            	Station station = new Station(parts[1]);
	                station.setAttribute("name", parts[2]);
	                station.addDmr_id(Long.parseLong(parts[0]));
	                contactXml += station.toMotorolaXml(motorolaCounter);
	                motorolaCounter++;
            	}
            }
            in.close();
            
            
            Path templatePath = Paths.get(mototrboTemplate);
            Path outputPath = Paths.get(String.format(mototrboOutput, "" + Scraper.getTime()));
            
            String content = new String(Files.readAllBytes(templatePath), StandardCharsets.UTF_8);
            content = content.replaceAll(replaceString, contactXml);
            
            Files.write(outputPath, content.getBytes(StandardCharsets.UTF_8));
            
            Scraper.getLogger().debug("Written to " + outputPath.toAbsolutePath());
	        	        
	    } catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
	        if (reader != null)
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
	    }
    	
    }

    static class Data {
		List<User> users;

		public String toString() {
			StringBuilder sb = new StringBuilder();
			for(User user : users) {
				sb.append(user.toString());
			}
			return sb.toString();
		}
	}
	
	static class User {
		String country = "";
		String callsign = "";
		String name = "";
		String radio_id = "";
		String surname = "";
		String state = "";
		String city = "";
		String remarks = "";
		
		public String toString() {
			return String.format("%s %s\n", this.callsign, this.name);
		}
	}

}
