package scraper.ukrepeater;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.gson.Gson;

import scraper.Collector;
import scraper.Scraper;
import scraper.station.Address;
import scraper.station.GeoLocation;
import scraper.station.Location;
import scraper.station.Name;
import scraper.station.Station;


public class RepeaterLists extends Collector {
	
	private static Map<String, Repeater> repeaters;

	public void doUpdate() throws IOException, ParseException {
		
		repeaters = new HashMap<String, Repeater>();
		
		Document doc = Jsoup.connect("https://www.ukrepeater.net/repeaterlist1.htm").get();
		
		Elements repeaterLines = doc.select("table.mix tr");
		
		Scraper.getLogger().info(doc.title() + " (" + repeaterLines.size() + " entries)");
		
		int i = 0;
		
		for (Element repeaterElement : repeaterLines) {
			if ( i > 0 ) {
				Elements repeaterDetails = repeaterElement.children();
				String[] rawModes = repeaterDetails.get(4).text().split("/");
				Set<String> modes = new HashSet<String>();
				
				for ( String rawMode : rawModes ) {
					modes.add(Repeater.getModeName(rawMode));
				}
				
				for ( String mode : modes ) {
					Repeater repeater = new Repeater();
					
					repeater.setCallsign(repeaterDetails.get(0).text());
					repeater.setUrl(repeaterDetails.get(0).selectFirst("a").attr("href"));
					repeater.setChannel(repeaterDetails.get(1).text());
					repeater.setOutput(Float.parseFloat(repeaterDetails.get(2).text()));
					repeater.setInput(Float.parseFloat(repeaterDetails.get(3).text()));
					repeater.setModes(repeaterDetails.get(4).text());
					repeater.setQra(repeaterDetails.get(5).text());
					repeater.setLocation(repeaterDetails.get(8).text());
					repeater.setGridRef(repeaterDetails.get(9).text());
					repeater.setRegion(repeaterDetails.get(10).text());
					repeater.setCtcss(repeaterDetails.get(11).text());
					
					Element url = repeaterDetails.get(12).selectFirst("a");
					if ( url != null ) {
						repeater.setGroupUrl(url.attr("href"));
					}
					
					repeater.setKeeper(repeaterDetails.get(13).text());
					repeater.setLatitude(repeaterDetails.get(14).text());
					repeater.setLongitude(repeaterDetails.get(15).text());
					repeater.setStatus(repeaterDetails.get(16).text());
					
					// Get Extended Info
					Document repeaterDoc = Jsoup.connect(repeater.getUrl()).get();
					String textfield = repeaterDoc.selectFirst("div#body table tbody tr td").text();
					
					String regex = "NoV holder: ([a-zA-Z\\ \\-].*?) \\[.*Band: (\\w*) .* Database Entry: ([0-9a-zA-Z\\ ].*?) Date of NoV Issue: ([0-9a-zA-Z\\ ].*?) Renewal Date: ([0-9a-zA-Z\\ ].*)";
					
			        Pattern pattern = Pattern.compile(regex);

			        Matcher matcher = pattern.matcher(textfield);
			        
			        while(matcher.find()) {
			        	repeater.setKeeperName(matcher.group(1));
			        	repeater.setBand(matcher.group(2));
			        	repeater.setDatabaseEntryDate(new SimpleDateFormat("dd MMM yyyy").parse(matcher.group(3)));
			        	repeater.setNovIssueDate(new SimpleDateFormat("dd MMM yyyy").parse(matcher.group(4)));
			        	repeater.setRenewalDate(new SimpleDateFormat("dd MMM yyyy").parse(matcher.group(5)));
				    }					
					
					// Get DMR-specific fields
					if ( mode != null ) {
						if ( mode.equals(Repeater.DMR) ) {
							System.out.println("Processing DMR");
							String dmr_regex = "cc:(\\d+) Connectivity: ([0-9A-Z\\ ].+?)\\s(?:ETCC|This|DMR)";
							
					        Pattern dmr_pattern = Pattern.compile(dmr_regex);
	
					        Matcher dmr_matcher = dmr_pattern.matcher(textfield);
					        
					        while(dmr_matcher.find()) {
					        	System.out.println("Matches - CC: " + dmr_matcher.group(1) + " Network: " + dmr_matcher.group(2));
					        	repeater.setDmrColourCode(Integer.parseInt(dmr_matcher.group(1)));
					        	repeater.setDmrNetwork(dmr_matcher.group(2));
					        }							
						} else {
							System.out.println("Mode is not DMR! (it's " + mode + ")");
						}
					} else {
						System.out.println("Mode Null!");
					}
					
					System.out.println(repeater.toJson());
					
					repeaters.put(repeater.getCallsign(), repeater);
					
					// Send to DB
					Station station = Scraper.fetchStation(repeater.getCallsign());
					Location location = new Location();
					location.latitude = (double) repeater.getLatitude();
					location.longitude = (double) repeater.getLongitude();
					station.addLocation(location);
					Address address = new Address();
					address.city = repeater.getLocation();
					address.region = repeater.getRegion();
					address.country = "United Kingdom";
					station.addAddress(address);
					station.setAttribute("url", repeater.getUrl());
					station.setAttribute("groupUrl", repeater.getGroupUrl());
					station.setAttribute("channel", repeater.getChannel());
					station.setAttribute("band", repeater.getBand());
					station.setAttribute("input", String.valueOf(repeater.getInput()));
					station.setAttribute("output", String.valueOf(repeater.getOutput()));
					station.setAttribute("grid_ref", repeater.getGridRef());
					station.setLocator(repeater.getQra());
					station.setAttribute("ctcss", repeater.getCtcss());
					station.setAttribute("keeper", repeater.getKeeper());
					station.setAttribute("keeper_name", repeater.getKeeperName());
					station.setAttribute("status", repeater.getStatus());
					station.setAttribute("modes", repeater.getModesAsString());
					station.setAttribute("rsgb_database_entry_date", repeater.getDatabaseEntryDate().toString());
					station.setAttribute("nov_issue_date", repeater.getNovIssueDate().toString());
					station.setAttribute("renewal_date", repeater.getRenewalDate().toString());
					
					if ( repeater.getDmrNetwork() != null ) {
						station.setAttribute("dmr_network", repeater.getDmrNetwork());
						station.setAttribute("dmr_colour_code", String.valueOf(repeater.getDmrColourCode()));
					}
					
					Scraper.saveStation(station);
					
					// Record keeper/name too
					Station keeper = Scraper.fetchStation(repeater.getKeeper());
					Name keeperName = new Name();
					String[] parts = repeater.getKeeperName().split(" ");
					keeperName.forename = parts[0];
					keeperName.surname = parts[((parts.length)-1)];
					Scraper.saveStation(keeper);
				}
			}
			i++;
		}
		
		// Print out KML
		
		PrintWriter writer = new PrintWriter("repeaters.kml", "UTF-8");
		writer.println("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
		writer.println("<kml xmlns=\"http://www.opengis.net/kml/2.2\">");
		writer.println("  <Document>");
		writer.println("    <name>repeaters.kml</name>");
		writer.println("    <visibility>0</visibility>");
		writer.println("    <open>1</open>");
		writer.println("    <Style id=\"OPERATING\">");
		writer.println("      <IconStyle>");
		writer.println("        <Icon>");
		writer.println("          <href>http://maps.google.com/mapfiles/kml/pushpin/grn-pushpin.png</href>"); 
		writer.println("        </Icon>");
		writer.println("      </IconStyle>"); 
		writer.println("    </Style>");
		writer.println("    <Style id=\"LICENSED\">");
		writer.println("      <IconStyle>");
		writer.println("        <Icon>");
		writer.println("          <href>http://maps.google.com/mapfiles/kml/pushpin/blue-pushpin.png</href>"); 
		writer.println("        </Icon>");
		writer.println("      </IconStyle>"); 
		writer.println("    </Style>");
		writer.println("    <Style id=\"APPROVED\">");
		writer.println("      <IconStyle>");
		writer.println("        <Icon>");
		writer.println("          <href>http://maps.google.com/mapfiles/kml/pushpin/ltblu-pushpin.png</href>"); 
		writer.println("        </Icon>");
		writer.println("      </IconStyle>"); 
		writer.println("    </Style>");
		writer.println("    <Style id=\"REDUCED\">");
		writer.println("      <IconStyle>");
		writer.println("        <Icon>");
		writer.println("          <href>http://maps.google.com/mapfiles/kml/pushpin/ylw-pushpin.png</href>"); 
		writer.println("        </Icon>");
		writer.println("      </IconStyle>"); 
		writer.println("    </Style>");
		writer.println("    <Style id=\"NOT.OP\">");
		writer.println("      <IconStyle>");
		writer.println("        <Icon>");
		writer.println("          <href>http://maps.google.com/mapfiles/kml/pushpin/red-pushpin.png</href>"); 
		writer.println("        </Icon>");
		writer.println("      </IconStyle>"); 
		writer.println("    </Style>");

		for ( String callsign : repeaters.keySet() ) {
			writer.println(repeaters.get(callsign).toKml());
		}

		writer.println("  </Document>");
		writer.println("</kml>");
		writer.close();
		
		
		// Write out MotoTRBO XML
	}

	@Override
	public void run() {
		try {
			this.doUpdate();
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
		
	}
}
