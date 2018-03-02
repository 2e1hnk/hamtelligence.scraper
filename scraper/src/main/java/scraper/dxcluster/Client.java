package scraper.dxcluster;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.net.telnet.EchoOptionHandler;
import org.apache.commons.net.telnet.InvalidTelnetOptionException;
import org.apache.commons.net.telnet.SuppressGAOptionHandler;
import org.apache.commons.net.telnet.TelnetClient;
import org.apache.commons.net.telnet.TelnetOptionHandler;
import org.apache.commons.net.telnet.TerminalTypeOptionHandler;
import org.json.JSONArray;
import org.json.JSONObject;

import scraper.Scraper;
import scraper.station.Activity;
import scraper.station.GeoLocation;
import scraper.station.Location;
import scraper.station.Station;

public class Client implements Runnable {
	
	private String dxcluster_hostname;
	private int dxcluster_port;
	private int dxcluster_login_line;

	static TelnetClient telnetClient = new TelnetClient();
	private Thread clientThread;
	private boolean running = false;

	private static final Pattern dxRegex = Pattern.compile(
			"^DX\\sde\\s([a-zA-Z0-9\\/\\-\\#]+):\\s+([0-9\\.]+)\\s+([a-zA-Z0-9\\/]+)\\s+(.*)\\s([0-9]{4})Z\\s?([a-zA-Z0-9]{4})?");
	private static final Pattern wxRegex = Pattern.compile(
			"^WCY\\sde\\s([a-zA-Z0-9\\/\\-]+)\\s<([0-9]+)>\\s+:\\s+K=([0-9]+)\\s+expK=([0-9]+)\\s+A=([0-9]+)\\s+R=([0-9]+)\\s+SFI=([0-9]+)\\s+SA=([a-z]+)\\s+GMF=([a-z]+)\\s+Au=([a-z]+)$");
	private static final Pattern skimmerRegex = Pattern.compile("^DX\\sde\\s(?<spotter>[a-zA-Z0-9\\/]+)-?#?:\\s+(?<frequency>[0-9\\\\.]+)\\s+(?<dx>[a-zA-Z0-9\\/]+)\\s+(?<comments>(?<mode>[A-Z0-9]+)\\s(?<signalstrength>[0-9]+)\\sdB\\s(?<speed>[0-9]+)\\s(WPM|BPS))(.*)\\s(?<time>[0-9]{4})Z\\s?(?<gridsquare>[a-zA-Z0-9]{4})?");
	private static final Pattern sotaRegex = Pattern.compile("^DX\\sde\\s(?<spotter>[a-zA-Z0-9\\/\\-\\#]+)\\s+(?<frequency>[0-9\\.]+)\\s+(?<dx>[a-zA-Z0-9\\/]+)\\s+(?<summit>[A-Z0-9\\/\\-]+)\\s+(?<time>[0-9]{4})Z");
	
	BufferedReader r;
	BufferedWriter w;

	public Client(String host, int port, int loginLine) {
		this.dxcluster_hostname = host;
		this.dxcluster_port = port;
		this.dxcluster_login_line = loginLine;
	}

	public void connect() throws NumberFormatException, SocketException, IOException {
		// telnetClient.connect(config.getProperty("hostname"),
		// Integer.parseInt(config.getProperty("port")));
		// read();
		Scraper.getLogger().info("Connecting to DX Cluster");
		this.running = true;
		this.clientThread = new Thread(this);
		this.clientThread.start();
	}

	public void disconnect() {
		this.running = false;
	}

	private static void setOptionHandlers() throws IOException {
		ArrayList<TelnetOptionHandler> optionHandlers = new ArrayList<TelnetOptionHandler>();
		optionHandlers.add(new TerminalTypeOptionHandler("VT100", false, false, true, false));
		optionHandlers.add(new EchoOptionHandler(true, false, true, false));
		optionHandlers.add(new SuppressGAOptionHandler(true, true, true, true));
		for (TelnetOptionHandler handler : optionHandlers) {
			try {
				telnetClient.addOptionHandler(handler);
			} catch (InvalidTelnetOptionException e) {
				System.err.println("Error registering option handler " + handler.getClass().getSimpleName());
			}
		}
	}

	public static void write(byte[] data) throws IOException {
		telnetClient.getOutputStream().write(data);
		telnetClient.getOutputStream().flush();
	}

	@Override
	public void run() {
		try {
			telnetClient.connect(this.dxcluster_hostname, this.dxcluster_port);

			InputStream inStream = telnetClient.getInputStream();
			r = new BufferedReader(new InputStreamReader(inStream));

			OutputStream outStream = telnetClient.getOutputStream();
			w = new BufferedWriter(new OutputStreamWriter(outStream));

			int i = 0;
			while (running) {

				// Line number are trial-and-error here...
				if (i == this.dxcluster_login_line) {
					Scraper.getLogger().info("Logging in to DX Cluster...");
					w.write("2E1HNK\n");
					w.flush();
				}

//				if (i == 26) {
//					Scraper.log(Scraper.LOG_INFO, "Setting DX Cluster QTH/QRA");
					/*
					w.write("set/QTH " + Scraper.getProperties().getProperty("dxcluster_qth") + "\n");
					w.flush();
					w.write("set/QRA " + Scraper.getProperties().getProperty("dxcluster_qra") + "\n");
					w.flush();
					*/
//					w.write("set dx filter\n");
//					w.flush();
//				}

				String line = r.readLine();

				switch (line) {
				case "login: ":
					w.write("2e1hnk\n");
					w.flush();
					break;
				default:
					Matcher dxMatch = dxRegex.matcher(line);
					Matcher skimmerMatch = skimmerRegex.matcher(line);
					Matcher wxMatch = wxRegex.matcher(line);
					Matcher sotaMatch = sotaRegex.matcher(line);
					
					if (skimmerMatch.find()) {
						Station station = Scraper.fetchStation(skimmerMatch.group("dx"));
						Activity activity = new Activity(skimmerMatch.group("spotter"), Double.valueOf(skimmerMatch.group("frequency")), skimmerMatch.group("comments"), Integer.valueOf(skimmerMatch.group("time")), skimmerMatch.group("gridsquare"), skimmerMatch.group("mode"), Integer.valueOf(skimmerMatch.group("signalstrength")), Integer.valueOf(skimmerMatch.group("speed")));
						station.addActivity(activity);
						Scraper.saveStation(station);
						Scraper.getLogger().info(station.getCallsign() + ": " + activity.toJson());
					} else if (dxMatch.find()) {
						Spot spot = new Spot(dxMatch.group(1), Double.valueOf(dxMatch.group(2)), dxMatch.group(3),
								dxMatch.group(4), Integer.parseInt(dxMatch.group(5)));

						if (dxMatch.group(6) != null) {
							spot.setGridsquare(dxMatch.group(6));
						}

						Scraper.getLogger().info(spot.toJson());
						
						Station station = Scraper.fetchStation(spot.getDx());
						Activity activity = new Activity(spot.getSpotter(), spot.getFrequency(), spot.getComments(), spot.getTime(), spot.getGridsquare());
						station.addActivity(activity);
						Scraper.saveStation(station);
					} else if (sotaMatch.find()) {
						Spot spot = new Spot(
								sotaMatch.group("spotter"),
								Double.valueOf(sotaMatch.group("frequency")),
								sotaMatch.group("dx"),
								sotaMatch.group("summit"),
								Integer.parseInt(sotaMatch.group("time")
						));

						Scraper.getLogger().info(spot.toJson());
						
						Station station = Scraper.fetchStation(spot.getDx());
						Activity activity = new Activity(spot.getSpotter(), spot.getFrequency(), "Spotted " + spot.getDx() + " on SOTA summit " + spot.getComments(), spot.getTime(), spot.getGridsquare());
						station.addActivity(activity);
						
						JSONArray summitData = Scraper.fetchJsonArray("http://parksnpeaks.org/api/SUMMIT/" + spot.getComments());
						if ( summitData != null ) {
							JSONObject summit = summitData.getJSONObject(0);
							Location location = new Location();
							location.latitude = summit.getDouble("Latitude");
							location.longitude = summit.getDouble("Longitude");
							location.data.put("sota_summit_code", spot.getComments());
							location.data.put("sota_association_name", summit.getString("AssociationName"));
							location.data.put("sota_region_name", summit.getString("RegionName"));
							location.data.put("sota_summit_name", summit.getString("SummitName"));
							location.data.put("sota_summit_altitude_metres", summit.getString("AltM"));
							location.data.put("sota_summit_altitude_feet", summit.getString("AltFt"));
							station.addLocation(location);
						}
						
						Scraper.saveStation(station);						
					} else if (wxMatch.find()) {
						// TODO: Create solar weather class
					} else {
						// Unrecognised line, log it for now
						Scraper.getLogger().error("UNRECOGNISED DX CLUSTER MESSAGE: " + line);
					}
					break;
				}

				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				i++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
