package scraper.wspr;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import scraper.Collector;
import scraper.Scraper;
import scraper.station.Activity;
import scraper.station.Station;

public class WSPR extends Collector {

	private boolean running = true;
	private String url = "http://wsprnet.org/olddb?mode=html&band=all&limit=50&findcall=&findreporter=&sort=date";
	private int loopCounter;
	private int delay = 2000;
	private Map<Date, List<String>> cache = new HashMap<Date, List<String>>();
	
	@Override
	public void run() {
		while ( running ) {
			try {
				
				Scraper.getLogger().debug("Delay = " + this.delay + ", cache size = " 
						+ cache.size());
				Thread.sleep(delay);
				
				Scraper.getLogger().debug("Fetching " + url);
				Document doc = Jsoup.connect(url).get();
				
				Elements entries = doc.select("table").eq(2).select("tr");
		
		        this.loopCounter = 0;
		        int rowCounter = 0;
		        for ( Element entry : entries ) {
		        	if ( rowCounter > 1 ) {
		        		this.processEntry(entry);
		        	}
		        	rowCounter++;
		        }
		        
		        if ( this.loopCounter == 0 ) {
		        	// Nothing to process so back off a bit
		        	this.slowDown();
		        } else {
		        	// There was some data so speed up
		        	this.speedUp(this.loopCounter);
		        }
		        
		    } catch (IOException e) {
		    	e.printStackTrace();
		    } catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void processEntry(Element entry) {
		
		Elements entryDetails = entry.children();
		
		Date spotdate;
		try {
			String dateStr = entryDetails.get(0).text().replace("\u00a0", "");
			spotdate = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(dateStr);
			int time = Integer.parseInt("" + spotdate.getHours() + spotdate.getMinutes());
			String callsign = entryDetails.get(1).text().replace("\u00a0", "");
			Double frequency = Double.parseDouble(entryDetails.get(2).text().replace("\u00a0", ""));
			int snr = Integer.parseInt(entryDetails.get(3).text().replace("\u00a0", ""));
			int drift = Integer.parseInt(entryDetails.get(4).text().replace("\u00a0", ""));
			String grid = entryDetails.get(5).text().replace("\u00a0", "");
			int powerDbm = Integer.parseInt(entryDetails.get(6).text().replace("\u00a0", ""));
			Double powerW = Double.parseDouble(entryDetails.get(7).text().replace("\u00a0", ""));
			String reportedByCallsign = entryDetails.get(8).text().replace("\u00a0", "");
			String reportedByGrid = entryDetails.get(9).text().replace("\u00a0", "");
			int distKm = Integer.parseInt(entryDetails.get(10).text().replace("\u00a0", ""));
			int distMi = Integer.parseInt(entryDetails.get(11).text().replace("\u00a0", ""));
			
			String cacheString = callsign + "-" + reportedByCallsign + "-" + frequency;
			
			if ( !this.inCache(spotdate, cacheString) ) {
				Station station = Scraper.fetchStation(callsign);
				
				Activity activity = new Activity(reportedByCallsign, frequency, "", time, reportedByGrid, "WSPR");
				activity.setSignalStrength(snr);
				
				station.addActivity(activity);
				
				Scraper.saveStation(station);
				
				this.insertIntoCache(spotdate, cacheString);
			}
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	private boolean inCache(Date date, String contents) {
		if ( !cache.containsKey(date) ) {
			return false;
		} else {
			return cache.get(date).contains(contents);
		}
	}
	
	private void insertIntoCache(Date date, String contents) {
		List<String> cacheLine;
		if ( !cache.containsKey(date) ) {
			cacheLine = new ArrayList<String>();
		} else {
			cacheLine = cache.get(date);
		}

		cacheLine.add(contents);
		cache.put(date, cacheLine);
	}
	
	private void tidyCache() {
		for ( Date cacheDate : cache.keySet() ) {
			// Delete anything more than an hour old
			if ( cacheDate.before(new Date(System.currentTimeMillis() - 3600 * 1000) ) ) {
				cache.remove(cacheDate);
			}
		}
	}
	
	private void slowDown() {
		
	}
	
	private void speedUp(int amount) {
		
	}

	@Override
	public void stop() {
		this.running = false;
	}
}
