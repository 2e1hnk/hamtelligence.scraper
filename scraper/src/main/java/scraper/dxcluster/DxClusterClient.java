package scraper.dxcluster;

import java.io.IOException;
import java.net.SocketException;

import scraper.Collector;

public class DxClusterClient extends Collector {

	private Client client;
	private String dxcluster_hostname;
	private int dxcluster_port;
	private int dxcluster_login_line;

	
	public DxClusterClient(String host, int port, int loginLine) {
		this.dxcluster_hostname = host;
		this.dxcluster_port = port;
		this.dxcluster_login_line = loginLine;
	}
		
	public void stop() {
		this.client.disconnect();
	}

	@Override
	public void run() {
		try {
			this.client = new Client(dxcluster_hostname, dxcluster_port, dxcluster_login_line);
			this.client.connect();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}