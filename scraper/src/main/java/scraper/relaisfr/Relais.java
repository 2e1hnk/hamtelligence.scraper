package scraper.relaisfr;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import scraper.Collector;
import scraper.Scraper;
import scraper.station.Address;
import scraper.station.Location;
import scraper.station.Station;

public class Relais extends Collector {

	private String url = "https://www.radioamateur.org/relais/genxml.php?type=";
	private Map<String, String> depts = new HashMap<String, String>();

	public Relais() {
		depts.put("1", "Ain"); //	Bourg en Bresse	Auvergne Rh�ne-Alpes	Rhone Alpes
		depts.put("2", "Aisne"); //	Laon	Nord-Pas-de-Calais Picardie	Picardie
		depts.put("3", "Allier"); //	Vichy	Auvergne Rh�ne-Alpes	Auvergne
		depts.put("4", "Alpes de Haute"); // Provence	Digne	Provence-Alpes-C�te d'Azur	Provence-Alpes-C�te d'Azur
		depts.put("6", "Alpes Maritimes"); //	Nice	Provence-Alpes-C�te d'Azur	Provence-Alpes-C�te d'Azur
		depts.put("7", "Ard�che	Privas"); //	Auvergne Rh�ne-Alpes	Rhone Alpes
		depts.put("8", "Ardennes"); //	Charleville-M�zi�es	Grand Est	Champagne-Ardenne
		depts.put("9", "Ari�ge"); //	Foix	Languedoc-Roussillon Midi-Pyr�n�es	Midi-Pyr�n�es
		depts.put("10", "Aube"); //	Troyes	Grand Est	Champagne-Ardenne
		depts.put("11", "Aude"); //	Carcassonne	Languedoc-Roussillon Midi-Pyr�n�es	Languedoc-Roussillon
		depts.put("12", "Aveyron"); //	Rodez	Languedoc-Roussillon Midi-Pyr�n�es	Midi-Pyr�n�es
		depts.put("67", "Bas Rhin"); //	Strasbourg	Grand Est	Alsace
		depts.put("13", "Bouches du Rh�ne"); //	Marseille	Languedoc-Roussillon Midi-Pyr�n�es	Provence-Alpes-C�te d'Azur
		depts.put("14", "Calvados"); //	Caen	Normandie	Basse-Normandie
		depts.put("15", "Cantal	Aurillac"); //	Auvergne Rh�ne-Alpes	Auvergne
		depts.put("16", "Charente"); //	Angoul�me	Poitou-Charentes	Poitou-Charentes
		depts.put("17", "Charente Maritime"); //	La Rochelle	Poitou-Charentes	Poitou-Charentes
		depts.put("18", "Cher"); //	Bourges	Centre Val de Loire	Centre
		depts.put("19", "Corr�ze"); //	Brive	Nouvelle-Aquitaine	Limousin
		depts.put("2a", "Corse du Sud"); //	Ajaccio	Corse	Corse
		depts.put("21", "Cote d'Or"); //	Dijon	Bourgogne Franche-Comt�	Bourgogne
		depts.put("22", "Cotes d'Armor"); //	Saint Brieuc	Bretagne	Bretagne
		depts.put("23", "Creuse	Gu�ret"); //	Nouvelle-Aquitaine	Limousin
		depts.put("79", "Deux S�vres"); //	Niort	Poitou-Charentes	Poitou-Charentes
		depts.put("24", "Dordogne"); //	P�rigueux	Nouvelle-Aquitaine	Aquitaine
		depts.put("25", "Doubs"); //	Besan�on	Bourgogne Franche-Comt�	Franche-Comt�
		depts.put("26", "Dr�me"); //	Valence	Auvergne Rh�ne-Alpes	Rhone Alpes
		depts.put("91", "Essonne"); //	Evry	�le-de-France	�le-de-France
		depts.put("27", "Eure"); //	Evreux	Normandie	Upper-Normandy
		depts.put("28", "Eure et Loir"); //	Chartres	Centre Val de Loire	Centre
		depts.put("29", "Finist�re"); //	Brest	Bretagne	Bretagne
		depts.put("30", "Gard"); //	N�mes	Languedoc-Roussillon Midi-Pyr�n�es	Languedoc-Roussillon
		depts.put("32", "Gers"); //	Auch	Languedoc-Roussillon Midi-Pyr�n�es	Midi-Pyr�n�es
		depts.put("33", "Gironde"); //	Bordeaux	Nouvelle-Aquitaine	Aquitaine
		depts.put("68", "Haut Rhin"); //	Colmar	Grand Est	Alsace
		depts.put("2b", "Haute Corse"); //	Bastia	Corse	Corse
		depts.put("31", "Haute Garonne"); //	Toulouse	Languedoc-Roussillon Midi-Pyr�n�es	Midi-Pyr�n�es
		depts.put("43", "Haute Loire"); //	Le Puy	Auvergne Rh�ne-Alpes	Auvergne
		depts.put("52", "Haute Marne"); //	Chaumont	Grand Est	Champagne-Ardenne
		depts.put("74", "Haute Savoie"); //	Annecy	Auvergne Rh�ne-Alpes	Rhone Alpes
		depts.put("87", "Haute Vienne"); //	Limoges	Nouvelle-Aquitaine	Limousin
		depts.put("5", "Hautes Alpes"); //	Gap	Provence-Alpes-C�te d'Azur	Provence-Alpes-C�te d'Azur
		depts.put("65", "Hautes Pyr�n�es"); //	Tarbes	Languedoc-Roussillon Midi-Pyr�n�es	Midi-Pyr�n�es
		depts.put("92", "Hauts de Seine"); //	Nanterre	�le-de-France	�le-de-France
		depts.put("34", "H�rault"); //	Montpellier	Languedoc-Roussillon Midi-Pyr�n�es	Languedoc-Roussillon
		depts.put("35", "Ille et Vilaine"); //	Rennes	Bretagne	Bretagne
		depts.put("36", "Indre"); //	Ch�teauroux	Centre Val de Loire	Centre
		depts.put("37", "Indre et Loire"); //	Tours	Centre Val de Loire	Centre
		depts.put("38", "Is�re"); //	Grenoble	Auvergne Rh�ne-Alpes	Rhone Alpes
		depts.put("39", "Jura"); //	Lons le Saunier	Bourgogne Franche-Comt�	Franche-Comt�
		depts.put("40", "Landes"); //	Mont de Marsan	Nouvelle-Aquitaine	Aquitaine
		depts.put("42", "Loire"); //	Saint Etienne	Auvergne Rh�ne-Alpes	Rhone Alpes
		depts.put("44", "Loire Atlantique"); //	Nantes	Pays-de-la-Loire	Pays-de-la-Loire
		depts.put("41", "Loir et Cher"); //	Blois	Centre Val de Loire	Centre
		depts.put("45", "Loiret"); //	Orl�ans	Centre Val de Loire	Centre
		depts.put("46", "Lot"); //	Cahors	Languedoc-Roussillon Midi-Pyr�n�es	Midi-Pyr�n�es
		depts.put("47", "Lot et Garonne"); //	Agen	Nouvelle-Aquitaine	Aquitaine
		depts.put("48", "Loz�re	Mende"); //	Languedoc-Roussillon Midi-Pyr�n�es	Languedoc-Roussillon
		depts.put("49", "Maine et Loire"); //	Angers	Pays-de-la-Loire	Pays-de-la-Loire
		depts.put("50", "Manche	Saint-L�"); //	Normandie	Basse-Normandie
		depts.put("51", "Marne"); //	Reims	Grand Est	Champagne-Ardenne
		depts.put("53", "Mayenne"); //	Laval	Pays-de-la-Loire	Pays-de-la-Loire
		depts.put("54", "Meurthe et Moselle"); //	Nancy	Grand Est	Lorraine
		depts.put("55", "Meuse"); //	Bar le Duc	Grand Est	Lorraine
		depts.put("56", "Morbihan"); //	Vannes	Bretagne	Bretagne
		depts.put("57", "Moselle"); //	Metz	Grand Est	Lorraine
		depts.put("58", "Ni�vre"); //	Nevers	Bourgogne Franche-Comt�	Bourgogne
		depts.put("59", "Nord"); //	Lille	Nord-Pas-de-Calais Picardie	Nord-Pas-de-Calais
		depts.put("60", "Oise"); //	Beauvais	Nord-Pas-de-Calais Picardie	Picardie
		depts.put("61", "Orne"); //	Alen�on	Normandie	Basse-Normandie
		depts.put("75", "Paris"); //	Paris	�le-de-France	�le-de-France
		depts.put("62", "Pas de Calais"); //	Arras	Nord-Pas-de-Calais Picardie	Nord-Pas-de-Calais
		depts.put("63", "Puy de D�me"); //	Clermont-Ferrand	Auvergne Rh�ne-Alpes	Auvergne
		depts.put("64", "Pyr�n�es Atlantiques"); //	Pau	Nouvelle-Aquitaine	Aquitaine
		depts.put("66", "Pyr�n�es Orientales"); //	Perpignan	Languedoc-Roussillon Midi-Pyr�n�es	Languedoc-Roussillon
		depts.put("69", "Rh�ne"); //	Lyon	Auvergne Rh�ne-Alpes	Rhone Alpes
		depts.put("69M", "M�tropole de Lyon"); //	Lyon	Auvergne Rh�ne-Alpes	Rhone Alpes
		depts.put("71", "Sa�ne et Loire"); //	Macon	Bourgogne Franche-Comt�	Bourgogne
		depts.put("70", "Haute Sa�ne"); //	Vesoul	Bourgogne Franche-Comt�	Franche-Comt�
		depts.put("72", "Sarthe"); //	Le Mans	Pays-de-la-Loire	Pays-de-la-Loire
		depts.put("73", "Savoie"); //	Savoie : 73	Auvergne Rh�ne-Alpes	Rhone Alpes
		depts.put("77", "Seine et Marne"); //	Melun	�le-de-France	�le-de-France
		depts.put("76", "Seine Maritime"); //	Rouen	Normandie	Upper-Normandy
		depts.put("93", "Seine Saint Denis"); //	Bobigny	�le-de-France	�le-de-France
		depts.put("80", "Somme"); //	Amiens	Nord-Pas-de-Calais Picardie	Picardie
		depts.put("81", "Tarn"); //	Albi	Languedoc-Roussillon Midi-Pyr�n�es	Midi-Pyr�n�es
		depts.put("82", "Tarn et Garonne"); //	Montauban	Languedoc-Roussillon Midi-Pyr�n�es	Midi-Pyr�n�es
		depts.put("90", "Territoire de Belfort"); //	Belfort	Bourgogne Franche-Comt�	Franche-Comt�
		depts.put("95", "Val d'Oise	Pontoise"); //	�le-de-France	�le-de-France
		depts.put("94", "Val de Marne"); //	Cr�teil	�le-de-France	�le-de-France
		depts.put("83", "Var"); //	Toulon	Provence-Alpes-C�te d'Azur	Provence-Alpes-C�te d'Azur
		depts.put("84", "Vaucluse"); //	Avignon	Provence-Alpes-C�te d'Azur	Provence-Alpes-C�te d'Azur
		depts.put("85", "Vend�e"); //	La Roche sur Yon	Pays-de-la-Loire	Pays-de-la-Loire
		depts.put("86", "Vienne"); //	Poitiers	Nouvelle-Aquitaine	Poitou-Charentes
		depts.put("88", "Vosges"); //	Epinal	Grand Est	Lorraine
		depts.put("89", "Yonne"); //	Auxerre	Bourgogne Franche-Comt�	Bourgogne																																																																																																																																																																																															depts.put("78	Yvelines	Versailles	�le-de-France	�le-de-France
	}
	
	@Override
	public void run() {
		try {
			this.doUpdate();
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
	}

	public void doUpdate() throws ParserConfigurationException, MalformedURLException, SAXException, IOException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(new URL(url).openStream());

		Node markers = doc.getFirstChild();
		
		NodeList repeaters = markers.getChildNodes();
		
		for (int i = 1; i < repeaters.getLength(); i++) {

			Node repeater = repeaters.item(i);

			// Send to DB
			Station station = Scraper.fetchStation(repeater.getAttributes().getNamedItem("indicatif").getNodeValue());

			Location location = new Location();
			location.latitude = Double.valueOf(repeater.getAttributes().getNamedItem("lat").getNodeValue());
			location.longitude = Double.valueOf(repeater.getAttributes().getNamedItem("lng").getNodeValue());
			station.addLocation(location);

			Address address = new Address();
			address.street = repeater.getAttributes().getNamedItem("nom").getNodeValue();
			address.city = repeater.getAttributes().getNamedItem("ville").getNodeValue();
			address.region = depts.get(repeater.getAttributes().getNamedItem("dpt").getNodeValue()) + " (Dept " + repeater.getAttributes().getNamedItem("dpt").getNodeValue() + ")";
			address.country = "France";
			station.addAddress(address);

			station.setAttribute("channel", repeater.getAttributes().getNamedItem("canal").getNodeValue());
			station.setAttribute("band", repeater.getAttributes().getNamedItem("type").getNodeValue());
			station.setAttribute("input", repeater.getAttributes().getNamedItem("qrg_in").getNodeValue());
			station.setAttribute("output", repeater.getAttributes().getNamedItem("qrg_out").getNodeValue());

			if (repeater.getAttributes().getNamedItem("categorie").getNodeValue().equals("ana")
					&& repeater.getAttributes().getNamedItem("decl").getNodeValue().equals("CTCSS")) {
				station.setAttribute("modes", "FM");
				try {
					station.setAttribute("status", repeater.getAttributes().getNamedItem("info").getNodeValue());
				} catch ( NullPointerException e) {
					// Nothing to do
				}
			} else if ( repeater.getAttributes().getNamedItem("categorie").getNodeValue().equals("ana") ) {
				station.setAttribute("modes", "FM");
				station.setAttribute("ctcss", repeater.getAttributes().getNamedItem("decl").getNodeValue());
				try {
					station.setAttribute("status", repeater.getAttributes().getNamedItem("info").getNodeValue());
				} catch ( NullPointerException e) {
					// Nothing to do
				}
			} else if ( repeater.getAttributes().getNamedItem("categorie").getNodeValue().equals("num") ) {
				station.setAttribute("modes", repeater.getAttributes().getNamedItem("decl").getNodeValue());
				try {
					station.setAttribute("status", repeater.getAttributes().getNamedItem("info").getNodeValue());
				} catch ( NullPointerException e) {
					// Nothing to do
				}
			}

			station.setLocator(repeater.getAttributes().getNamedItem("locator").getNodeValue());
			station.setAttribute("keeper", repeater.getAttributes().getNamedItem("resp").getNodeValue());
			
			Scraper.saveStation(station);
		}

	}

}
