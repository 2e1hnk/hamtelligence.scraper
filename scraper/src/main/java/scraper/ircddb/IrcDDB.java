package scraper.ircddb;

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

public class IrcDDB extends Collector {
	
	private String baseUrl = "http://live2.ircddb.net:8080/jj3.yaws?p=%d";
	private long lastEntry = 0L;
	private boolean running = true;
	
	private int delay = 2000;
	private int loopCounter = 0;
	
	public IrcDDB() {
		
	}
	
	@Override
	public void stop() {
		this.running = false;
	}

	@Override
	public void run() {
		while ( running) {
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
	 * Entry-               MYCALL--         RPT2----        FLAGS-      DEST----
	 *        YYYYMMDDHHMMSS        RPT1----         TOCALL--      ??????        Comment-------------
	 * 785319:20180220163820G4HFG__MMB6CY__C0MB6CY__GCQCQCQ__000000880H00DCS102_BGRAHAM,_OLDHAM,_UK__
	 *                                                                                                
	 * 0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000
	 * 0000000000111111111122222222223333333333444444444455555555556666666666777777777788888888889999
	 * 0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123
	 * @param line
	 */
	private void processLine(String line) {
		if ( line.length() < 93 ) {
			return;
		}
		
		Long entry = Long.parseLong(line.substring(0, 6));
		
		if ( entry > this.lastEntry ) {
			try {
				Date logdate = new SimpleDateFormat("yyyyMMddHHmmss").parse(line.substring(7, 20));
	
				// TODO: Work out how to fix callsigns like A1BCD__M
				String myCall = line.substring(21, 28).replaceAll("_", "");
				String toCall = line.substring(66, 73).replaceAll("_", ""); 
				
				Scraper.getLogger().debug(myCall + " " + toCall);
				
				if ( !myCall.equals("*******") ) {
					this.logStation(myCall, toCall);
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
			this.lastEntry = entry;
			this.loopCounter++;
		}
		
	}
	
	private void logStation(String callsign, String remoteCall) {
		Station station = new Station();
		try {
			station = Scraper.fetchStation(callsign);
		} catch (JSONException | NullPointerException e) {
			return;
		}
		
		station.setAttribute("modes", "DSTAR");
		
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
