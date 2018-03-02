package scraper.aprs;

import scraper.Collector;
import scraper.Scraper;

public class AprsListener extends Collector {

	static AprsConnection aprsConn;

	@Override
	public void run() {
		aprsConn = new AprsConnection(Scraper.getProperties().getProperty("aprs_host"),
				Integer.parseInt(Scraper.getProperties().getProperty("aprs_port")), "2E1HNK");
		aprsConn.connect();
	}

}
