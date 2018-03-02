package scraper.brandmeister;

import java.net.URISyntaxException;

import org.fusesource.mqtt.client.MQTT;

public class BrandmeisterMQTT implements Runnable {

	private boolean running = true;
	
	@Override
	public void run() {
		
		MQTT mqtt = new MQTT();
		
		try {
			mqtt.setHost("localhost", 1883);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		
		while ( running ) {
			
		}
	}
	
	public void stop () {
		this.running = false;
	}

}
