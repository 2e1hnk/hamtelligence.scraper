package scraper.radio_output;

import java.io.StringWriter;
import java.util.Map;
import java.util.TreeMap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import scraper.station.Station;

public class DmrRepeater extends Station {
	public static Map<String, Map<Integer, Map<Integer, String>>> talkgroup = new TreeMap<String, Map<Integer, Map<Integer, String>>>();

	public static final int MAX_NAME_LENGTH = 16;

	public DmrRepeater() {
		this.populateTalkgroups();

		for (String networkName : this.talkgroup.keySet()) {
			System.out.println(String.format("%s:", networkName));
			for (Integer slot : this.talkgroup.get(networkName).keySet()) {
				System.out.println(String.format("\tSlot %s:", slot));
				for (Integer tg : this.talkgroup.get(networkName).get(slot).keySet()) {
					System.out.println(
							String.format("\t\t%d\t%s", tg, this.talkgroup.get(networkName).get(slot).get(tg)));
				}
			}
		}
	}

	private void populateTalkgroups() {
		try {
			// Brandmeister Talkgroups
			this.addTalkgroup("BRANDMEISTER", 1, 9990, "Echo Test");
			this.addTalkgroup("BRANDMEISTER", 1, 91, "Worldwide");
			this.addTalkgroup("BRANDMEISTER", 1, 92, "Europe");
			this.addTalkgroup("BRANDMEISTER", 1, 93, "USA");
			this.addTalkgroup("BRANDMEISTER", 1, 9, "Local");
			this.addTalkgroup("BRANDMEISTER", 2, 9, "Reflectors");
			this.addTalkgroup("BRANDMEISTER", 2, 9990, "Echo Test");

			// Phoenix/DMR-MARC Talkgroups
			this.addTalkgroup("PHOENIX", 1, 1, "WW Call");
			this.addTalkgroup("PHOENIX", 1, 2, "Europe Call");
			this.addTalkgroup("PHOENIX", 1, 9, "Local (Sec)");
			this.addTalkgroup("PHOENIX", 1, 9, "Local (Sec)");
			this.addTalkgroup("PHOENIX", 1, 13, "English");
			this.addTalkgroup("PHOENIX", 1, 80, "UK UA 1");
			this.addTalkgroup("PHOENIX", 1, 81, "UK UA 2");
			this.addTalkgroup("PHOENIX", 1, 82, "UK UA 3");
			this.addTalkgroup("PHOENIX", 1, 83, "UK UA 4");
			this.addTalkgroup("PHOENIX", 1, 84, "UK UA 5");
			this.addTalkgroup("PHOENIX", 1, 113, "WW Eng UA 1");
			this.addTalkgroup("PHOENIX", 1, 119, "WW UA 1");
			this.addTalkgroup("PHOENIX", 1, 123, "WW Eng UA 2");
			this.addTalkgroup("PHOENIX", 1, 129, "WW UA 2");
			this.addTalkgroup("PHOENIX", 1, 235, "UK Call");
			this.addTalkgroup("PHOENIX", 2, 801, "SE Eng");
			this.addTalkgroup("PHOENIX", 2, 810, "SW Eng");
			this.addTalkgroup("PHOENIX", 2, 820, "NW Eng");
			this.addTalkgroup("PHOENIX", 2, 830, "Midlands");
			this.addTalkgroup("PHOENIX", 2, 840, "East Eng");
			this.addTalkgroup("PHOENIX", 2, 850, "Scotland");
			this.addTalkgroup("PHOENIX", 2, 860, "NE Eng");
			this.addTalkgroup("PHOENIX", 2, 870, "Wales");
			this.addTalkgroup("PHOENIX", 2, 880, "N Irl");
			this.addTalkgroup("PHOENIX", 2, 9990, "Echo");


			// Salop Talkgroups
			this.addTalkgroup("SALOP", 1, 9, "Local");
			this.addTalkgroup("SALOP", 2, 950, "Cluster");

			// SW Cluster Talkgroups
			this.addTalkgroup("SW", 1, 9, "Local");
			this.addTalkgroup("SW", 2, 950, "Cluster");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void addTalkgroup(String network, int slot, int number, String name) throws Exception {
		
		if (slot < 1 || slot > 2) {
			throw new Exception("Invalid Slot number");
		}
		
		String tgName = "";
		if ( name.length() > MAX_NAME_LENGTH ) {
			throw new Exception("Talkgroup name too long");
		} else if ( (name.length() + " TG".length() + String.valueOf(number).length()) <= MAX_NAME_LENGTH ) {
			tgName = String.format("%s TG%d", name, number);
		} else if ( (name.length() + " ".length() + String.valueOf(number).length()) <= MAX_NAME_LENGTH ) {
			tgName = String.format("%s %d", name, number);
		} else if ( (name.length()) <= MAX_NAME_LENGTH ) {
			tgName = name;
		} 
		
		Map<Integer, Map<Integer, String>> networkTGs;
		if ( this.talkgroup.containsKey(network) ) {
			networkTGs = this.talkgroup.get(network);
		} else {
			networkTGs = new TreeMap<Integer, Map<Integer, String>>();
		}
		
		Map<Integer, String> slotTGs;
		if ( networkTGs.containsKey(slot) ) {
			slotTGs = networkTGs.get(slot);
		} else {
			slotTGs = new TreeMap<Integer, String>();
		}
		slotTGs.put(number, tgName);
		networkTGs.put(slot, slotTGs);
		this.talkgroup.put(network, networkTGs);
	}

	public static Map<String, Map<Integer, Map<Integer, String>>> getTalkgroup() {
		return talkgroup;
	}

	public static void setTalkgroup(Map<String, Map<Integer, Map<Integer, String>>> talkgroup) {
		DmrRepeater.talkgroup = talkgroup;
	}
	
	public DigitalContact getTalkgroupAsContact(int number, String name) {
		DigitalContact talkgroup = new DigitalContact(0, 0, "GRPCALL", number, name, "Enabled");
		return talkgroup;
	}

	public String getTalkgroupAsContactXml(int number, String name) throws JAXBException {
		
		DigitalContact talkgroup = this.getTalkgroupAsContact(number, name);
		
		JAXBContext context = JAXBContext.newInstance(DigitalContact.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        
        StringWriter sw = new StringWriter();
        marshaller.marshal(talkgroup, sw);

        String result = sw.toString();
        
        return result;
	}

	
}
