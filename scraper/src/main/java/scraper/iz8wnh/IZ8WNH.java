package scraper.iz8wnh;

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
import scraper.station.Location;
import scraper.station.Station;

public class IZ8WNH extends Collector {
	
	private String url = "https://www.iz8wnh.it/en/php/query-mappa.php?country=[%22Andorra%22,%22Austria%22,%22Belgium%22,%22Denmark%22,%22France%22,%22Germany%22,%22Hungary%22,%22Ireland%22,%22Italia%22,%22Liechtenstein%22,%22Luxembourg%22,%22Monaco%22,%22Montenegro%22,%22Netherlands%22,%22Philippines%22,%22Portugal%22,%22RepubblicaSanMarino%22,%22Slovenia%22,%22SouthKorea%22,%22Spain%22,%22Switzerland%22,%22TrinidadTobago%22,%22UnitedKingdom%22,%22UnitedStates%22,%22USA%22]&state=null&city=null&type=null&band=null&id=0.8603147233202986";

	@Override
	public void run() {
		try {
			this.doUpdate();
		} catch (JSONException | IOException e) {
			e.printStackTrace();
		}
	}

	public void doUpdate() throws JSONException, IOException {
		JSONArray json = readJsonFromUrl(this.url);
		
		for ( int i = 0; i < json.length(); i++ ) {
			JSONObject repeater = json.getJSONObject(i);
			Station station = new Station();
			try {
				station = Scraper.fetchStation(repeater.getString("Identificativo"));
			} catch (JSONException e) {
				continue;
			}
			
			Location location = new Location();
			location.latitude = Double.parseDouble(repeater.getString("Lat"));
			location.longitude = Double.parseDouble(repeater.getString("Long"));
			station.addLocation(location);
			
			Address address = new Address();
			address.city = repeater.getString("Localita");
			address.region = repeater.getString("Provincia");
			address.country = repeater.getString("Nazione");
			station.addAddress(address);
			
			station.setAttribute("channel", repeater.getString("Ripetitore"));
			station.setAttribute("band", repeater.getString("Banda"));
			station.setAttribute("output", repeater.getString("Frequenza"));
			
			Double input = Double.parseDouble(repeater.getString("Frequenza")) + Double.parseDouble(repeater.getString("Shift"));
			station.setAttribute("input", String.valueOf(input));
			
			station.setLocator(repeater.getString("Locator"));
			station.setAttribute("ctcss", repeater.getString("Tono"));
			try {
				station.setAttribute("keeper", repeater.getString("Gestore"));
			} catch (JSONException e) {
				// Nothing to do
			}
			try {
				station.setAttribute("info", repeater.getString("Rete"));
			} catch (JSONException e) {
				// Nothing to do
			}
			station.setAttribute("modes", repeater.getString("Tipologia"));
			
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
