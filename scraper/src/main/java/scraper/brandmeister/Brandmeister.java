package scraper.brandmeister;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import scraper.Collector;
import scraper.Scraper;
import scraper.station.Address;
import scraper.station.GeoLocation;
import scraper.station.Location;
import scraper.station.Station;

public class Brandmeister extends Collector {

	private boolean running = true;
	private int delay = 3600 * 1000;			// 1 hour
	private String apiUrl = "https://api.brandmeister.network/v1.0/repeater/?action=LIST";
	
	@Override
	public void run() {
		while ( running ) {
			try {
				JSONArray json = readJsonFromUrl(this.apiUrl);
				
				for ( int i = 0; i < json.length(); i++ ) {
					JSONObject obj = json.getJSONObject(i);
					try {
						this.parse(obj);
					} catch ( JSONException e ) {
						Scraper.getLogger().error("Error parsing JSON", e);
					}
				}
				
				Thread.sleep( delay );
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void stop() {
		this.running = false;
	}
	
	private void parse(JSONObject obj) throws JSONException {
		if ( !obj.get("callsign").toString().equals("") ) {
			Station station = Scraper.fetchStation(obj.get("callsign").toString());
			
			station.setAttribute("brandmeister_repeater_id", obj.get("repeaterid").toString());
			station.setAttribute("brandmeister_repeater_hardware", obj.get("hardware").toString());
			station.setAttribute("output", obj.get("tx").toString());
			station.setAttribute("input", obj.get("rx").toString());
			station.setAttribute("dmr_colour_code", obj.get("colorcode").toString());
			station.setAttribute("brandmeister_repeater_master", obj.get("lastKnownMaster").toString());
			station.setAttribute("url", obj.get("website").toString());
			station.setAttribute("pep", obj.get("website").toString());
			station.setAttribute("gain", obj.get("website").toString());
			station.setAttribute("antenna_height_above_ground_metres", obj.get("agl").toString());
			station.setAttribute("url", obj.get("website").toString());
			
			if ( !obj.isNull("lat") && !obj.isNull("lng") ) {
				Location location = new Location();
				location.latitude = Double.parseDouble(obj.getString("lat").toString());
				location.longitude = Double.parseDouble(obj.getString("lng").toString());
				station.addLocation(location);
			}
			
			if ( !obj.isNull("city") ) {
				Address address = new Address();
				address.city = obj.getString("city").toString();
				station.addAddress(address);
			}
			
			Scraper.saveStation(station);
		}
	}

	private static String readAll(Reader rd) throws IOException {
		StringBuilder sb = new StringBuilder();
		int cp;
		while ((cp = rd.read()) != -1) {
			sb.append((char) cp);
		}
		return sb.toString();
	}

	public static JSONArray readJsonFromUrl(String url) throws IOException, JSONException {
		InputStream is = new URL(url).openStream();
		try {
			BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
			String jsonText = readAll(rd);
			JSONArray json = new JSONArray(jsonText);
			return json;
		} finally {
			is.close();
		}
	}

}
