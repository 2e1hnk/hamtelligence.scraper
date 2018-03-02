package scraper.aprs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;

import net.ab0oo.aprs.parser.APRSPacket;
import net.ab0oo.aprs.parser.Parser;
import net.ab0oo.aprs.parser.Utilities;
import scraper.Scraper;

public class AprsConnection implements Runnable {

	private String server;
	private int port;
	private String username;

	private boolean running = false;
	private Thread clientThread;
	
	private Parser aprsParser = new Parser();
	private MessageHandler messageHandler = new MessageHandler();


	public AprsConnection(String server, int port, String username) {
		this.setServer(server);
		this.setPort(port);
		this.setUsername(username);
	}

	public void connect() {
		this.running = true;
		this.clientThread = new Thread(this);
		this.clientThread.start();
	}

	public void disconnect() {
		this.running = false;
	}

	public void run() {
		while (running) {
			try {
				Socket clientSocket = new Socket(server, port);
				PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
				BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
	
				// Do login
				Thread.sleep(500);
				out.println(this.getLoginString());
				
				while (running) {
					String line = in.readLine();
	
					// Skip any comment lines
					if (!line.startsWith("#")) {
						if (line != null) {
							try {
								APRSPacket packet = aprsParser.parse(line);
								messageHandler.handle(packet);
							} catch (Exception e) {
								Scraper.getLogger().error("Oops, exception thrown!");
								e.printStackTrace();
							}
						}
						Thread.sleep(100);
					}
				}
	
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			try {
				Scraper.getLogger().error("APRS thread died, trying again in 10 seconds");
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private String getLoginString() {
		return String.format("user %s pass %s vers testsoftware 0.0 filter %s", this.getUsername(),
				this.getPasscode(this.getUsername()), 
				//this.getRangeFilter(51.92, -2.1, 50)
				//this.getTypeFilter(new String[] {"Status"})
				this.getTypeFilter(new String[] {"Position", "Status"})
		);
	}

	private String getPasscode(String callsign) {
		return String.valueOf(Utilities.doHash(callsign));
	}
	
	private String getRangeFilter(double lat, double lng, int range) {
		return String.format("r/%f/%f/%d", lat, lng, range);
	}
	
	/**
	 * Get traffic from callsigns that start with xx
	 */
	private String getPrefixFilter(String[] prefix) {
		StringBuilder filter = new StringBuilder();
		filter.append("p");
		List<String> prefixes = Arrays.asList(prefix);
		for ( String part : prefixes ) {
			filter.append("/" + part);
		}
		return filter.toString();
	}
	
	/**
	 * Get messaged based on the type of message
	 * @return
	 */
	private String getTypeFilter(String[] types) {
		StringBuilder filter = new StringBuilder();
		filter.append("t/");
		for ( String type : types ) {
			filter.append(type.substring(0, 1).toLowerCase());
		}
		return filter.toString();
	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
