package scraper.allstar;

import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import scraper.Collector;
import scraper.Scraper;
import scraper.station.Station;

public class Allstar extends Collector {
	
	private String url = "http://stats.allstarlink.org/";
	private boolean running = true;
	private int delay = 300; // Scrape every 5 minutes

	@Override
	public void run() {
		while ( this.running ) {
			try {
				this.doUpdate();
				Thread.sleep(this.delay * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void stop() {
		this.running = false;
	}
	
	private void doUpdate() throws IOException {
		Document doc = Jsoup.connect(this.url).get();
		
		Elements nodeLines = doc.select("table tr");
		
		Scraper.getLogger().info("Found " + nodeLines.size() + " Allstar stations");
		
		int i = 0;
		
		for (Element nodeElement : nodeLines) {
			if ( i > 0 && nodeElement.children().size() >= 2 ) {
				Elements nodeDetails = nodeElement.children();
				Station station = Scraper.fetchStation(nodeDetails.get(2).text());
				
				station.setAttribute("allstar_node", nodeDetails.get(0).text());
				station.setAttribute("allstar_channel", nodeDetails.get(1).text());
				station.setAttribute("allstar_last_seen", new Date().toString());
				
				// Location is not normalised - ignore for now
				
				Scraper.saveStation(station);
			}
			i++;
		}
	}

}
