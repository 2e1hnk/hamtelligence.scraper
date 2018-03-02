package scraper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.xml.bind.JAXBException;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

import scraper.allstar.Allstar;
import scraper.aprs.AprsListener;
import scraper.brandmeister.Brandmeister;
import scraper.dxcluster.DxClusterClient;
import scraper.ircddb.IrcDDB;
import scraper.iz8wnh.IZ8WNH;
import scraper.phoenixuk.PhoenixUK;
import scraper.radio_output.BrandmeisterRepeater;
import scraper.radio_output.DmrRepeater;
import scraper.relaisfr.Relais;
import scraper.station.Station;
import scraper.ukrepeater.RepeaterLists;
import scraper.wspr.WSPR;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.gt;
import static com.mongodb.client.model.Filters.not;
import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;
import static java.util.Arrays.asList;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class Scraper {

	static Properties properties;

	static List<Collector> collectors = new ArrayList<Collector>();
	static List<Thread> collectorThreads = new ArrayList<Thread>();

	public static final int LOG_DEBUG = 0;
	public static final int LOG_INFO = 1;
	public static final int LOG_WARN = 2;
	public static final int LOG_ERROR = 3;
	public static final int LOG_CRITICAL = 4;

	private static int minLogLevel = LOG_DEBUG;

	static MongoClient mongoClient;
	static MongoCollection<Station> collection;
	
	static final Logger logger = Logger.getLogger(Scraper.class);

	public static void main(String[] args) {
		
		//BasicConfigurator.configure();
		logger.info("Starting");
		// Load properties
		Scraper scraper = new Scraper();
		scraper.loadProperties("work.properties");

		// Set up proxy settings
		if (properties.getProperty("use_proxy").equals("true")) {
			logger.info("Setting Proxy");
			System.setProperty("http.proxyHost", properties.getProperty("proxy_host"));
			System.setProperty("http.proxyPort", properties.getProperty("proxy_port"));
			System.setProperty("https.proxyHost", properties.getProperty("proxy_host"));
			System.setProperty("https.proxyPort", properties.getProperty("proxy_port"));
		}

		mongoClient = new MongoClient();

		// create codec registry for POJOs
		/*
		 * CodecRegistry pojoCodecRegistry =
		 * fromRegistries(MongoClient.getDefaultCodecRegistry(),
		 * fromProviders(PojoCodecProvider.builder().automatic(true).build()));
		 */

		CodecProvider pojoCodecProvider = PojoCodecProvider.builder().register("scraper.station").build();
		CodecRegistry pojoCodecRegistry = fromRegistries(MongoClient.getDefaultCodecRegistry(),
				fromProviders(pojoCodecProvider));

		// get handle to "hamtelligence" database
		MongoDatabase database = mongoClient.getDatabase(properties.getProperty("mongo_database"))
				.withCodecRegistry(pojoCodecRegistry);

		// get a handle to the "stations" collection
		collection = database.getCollection(properties.getProperty("mongo_collection"), Station.class);

		collectors.add(new Dmr());
		collectors.add(new DxClusterClient("localhost", 23, 10));	// DX Cluster
		collectors.add(new DxClusterClient("localhost", 24, 7));	// SOTA Cluster
		collectors.add(new RepeaterLists());
		collectors.add(new Relais());
		collectors.add(new IZ8WNH());
		collectors.add(new PhoenixUK());
		collectors.add(new IrcDDB());
		collectors.add(new WSPR());
		collectors.add(new AprsListener());
		collectors.add(new Brandmeister());
		collectors.add(new Allstar());

		for (Collector collector : collectors) {
			collectorThreads.add(new Thread(collector));
		}

		for (Thread collectorThread : collectorThreads) {
			collectorThread.start();
			// Delay is to prevent all threads starting at the same time and hammering the network
			try {
				Thread.sleep(30 * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void loadProperties(String filename) {
		// Load properties
		try {
			ClassLoader classloader = getClass().getClassLoader();
			File file = new File(classloader.getResource(filename).getFile());
			FileInputStream fileInput = new FileInputStream(file);
			properties = new Properties();
			properties.load(fileInput);
			fileInput.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Date getTime() {
		return new Date();
	}

	public static Station fetchStation(String callsign) {
		Station station = collection.find(eq("callsign", callsign)).first();
		if (station != null) {
			return station;
		} else {
			station = new Station(callsign);
			collection.insertOne(station);
			return station;
		}
	}
	
	public static Station fetchRandomStation() {
		Station station = collection.aggregate(Arrays.asList(Aggregates.sample(1))).first();
		return station;
	}

	public static void saveStation(Station station) {
		// collection.insertOne(station);
		logger.info(station.toJson());
		collection.replaceOne(eq("callsign", station.getCallsign()), station);
	}

	public static void saveStations(List<Station> stations) {
		collection.insertMany(stations);
	}

	public static void stop() {
		for (Collector collector : collectors) {
			collector.stop();
		}
		mongoClient.close();
	}

	public static Properties getProperties() {
		return properties;
	}

	public static JSONObject fetchJsonObject(String url) throws MalformedURLException, IOException {
		InputStream is = new URL(url).openStream();
		try {
			BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
			String jsonText = readAll(rd);
			JSONObject json = new JSONObject(jsonText);
			return json;
		} finally {
			is.close();
		}
	}

	public static JSONArray fetchJsonArray(String url) throws MalformedURLException, IOException {
		InputStream is = new URL(url).openStream();
		try {
			BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
			String jsonText = readAll(rd);
			JSONArray json = new JSONArray(jsonText);
			return json;
		} catch (JSONException e) {
			logger.info("Error reading summit data");
			return null;
		} finally {
			is.close();
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
	
	public static Logger getLogger() {
		return logger;
	}
}
