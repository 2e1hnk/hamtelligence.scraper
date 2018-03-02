package scraper.phoenixuk;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;

import scraper.Collector;
import scraper.Scraper;
import scraper.station.Address;
import scraper.station.Location;
import scraper.station.LogEntry;
import scraper.station.Station;

public class PhoenixUK extends Collector {
	
	private String baseUrl = "http://ham-dmr.de/live_dmr/jj3.yaws?p=%d";
	private long lastEntry = 0L;
	private boolean running = true;
	
	private int delay = 2000;
	private int loopCounter = 0;
	
	public PhoenixUK() {
		
	}
	
	public void stop() {
		this.running = false;
	}

	@Override
	public void run() {
		while ( running) {
			// TODO Auto-generated method stub
			String urlstr;
			URL url;
			
			try {
				urlstr = String.format(baseUrl, lastEntry);
				Scraper.getLogger().debug("Fetching " + urlstr);
				url = new URL(urlstr);
		        BufferedReader in = new BufferedReader(
		        new InputStreamReader(url.openStream()));
		
		        String inputLine;
		        this.loopCounter = 0;
		        while ((inputLine = in.readLine()) != null) {
		            this.processLine(inputLine);
		        }
		        
		        if ( this.loopCounter == 0 ) {
		        	// Nothing to process so back off a bit
		        	this.slowDown();
		        } else {
		        	// There was some data so speed up
		        	this.speedUp(this.loopCounter);
		        }
		        
		        in.close();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			try {
				Scraper.getLogger().debug("Delay = " + this.delay);
				Thread.sleep(delay);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * line looks something like this:
	 * 
	 * YYYYMMDDHHMMSS               MYCALL--                         Slot---                           Name---------------       Location-----------
	 *                YYYYMMDDHHMMSS        TOCALL--         MY-ID--             Talkgroup------------                      DTMF                     Country------------
	 * 20180220150152:20180220150152IZ5OQO__IK5XMK__2222501__2225039_0000002___D14261_________________ Gianni_____________  4213 Siena______________ Italy______________
	 *                                                                                                
	 * 00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000001111111111111111111111111111111111111111111111111111111111111
	 * 00000000001111111111222222222233333333334444444444555555555566666666667777777777888888888899999999990000000000111111111122222222223333333333444444444455555555556
	 * 01234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890
	 * @param line
	 */
	private void processLine(String line) {
		Long entry = Long.parseLong(line.substring(0, 13));
		
		if ( entry > this.lastEntry ) {
			try {
				Date logdate = new SimpleDateFormat("yyyyMMddHHmmss").parse(line.substring(0, 13));
	
				String myCall = line.substring(29, 36).replaceAll("_", "");
				String toCall = line.substring(37, 44).replaceAll("_", "");
				long myId = Long.valueOf(line.substring(54, 61).replaceAll("_", ""));
				long toId = Long.valueOf(line.substring(45, 53).replaceAll("_", ""));
				int timeslot = Integer.valueOf(line.substring(62, 71).replaceAll("_", ""));
				long talkgroup = Long.valueOf(line.substring(74, 94).replaceAll("_", ""));
				String myName = line.substring(96, 114).replaceAll("_", "");
				int DTMF = Integer.parseInt("0" + line.substring(117, 120).replaceAll(".", ""));
				String myCity = line.substring(122, 140).replaceAll("_", "");
				String myCountry = line.substring(142, 160).replaceAll("_", "");
				
				this.logStation(myCall, myId, myName, myCity, myCountry, toCall);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			this.lastEntry = entry;
			this.loopCounter++;
		}
		
	}
	
	private void logStation(String callsign, Long radio_id, String name, String city, String country, String remoteCall) {
		Station station = new Station();
		try {
			station = Scraper.fetchStation(callsign);
		} catch (JSONException e) {
			return;
		}
		
		Address address = new Address();
		address.city = city;
		address.country = country;
		station.addAddress(address);
		
		station.addDmr_id(radio_id);
		
		station.setAttribute("name", name);
		
		station.setAttribute("modes", "DMR");
		
		station.addLogbookEntry(new LogEntry(remoteCall));
		
		Scraper.saveStation(station);

		// Also add to the remote call's logbook
		try {
			Station remoteStation = Scraper.fetchStation(remoteCall);
			remoteStation.addLogbookEntry(new LogEntry(callsign));
			Scraper.saveStation(remoteStation);
		} catch (JSONException e) {
			return;
		}

	}
	
	private void slowDown() {
		// Slow down by half a second, to a maximum delay of 10 seconds
		if ( this.delay < 10000 ) {
			this.delay += 500;
		}
	}
	
	private void speedUp(int value) {
		// The higher the value, the more we need to speed up, up to 1 second
		this.delay -= (500 * value);
		if ( this.delay < 1000 ) {
			this.delay = 1000;
		}
		
	}

}
