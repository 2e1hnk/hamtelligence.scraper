package scraper.radio_output;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import scraper.station.Station;

public class MotoTRBO implements RadioOutput {
	
	public static final int maxZones = 250;
	public static final int maxChannelsPerZone = 160;
	public static final int maxChannels = 1000;
	public static final int maxAnalogueContacts = 500;
	public static final int maxDigitalContacts = 500;
	
	public static final int ANALOGUE_CONTACT_LIST = 0;
	public static final int DIGITAL_CONTACT_LIST = 1;
	
	private Map<Integer, Station[]> zones = new HashMap<Integer, Station[]>();
	private Map<Integer, TreeSet<Contact>> contacts = new HashMap<Integer, TreeSet<Contact>>();
	
	public MotoTRBO() {
		for ( int i = 0; i < maxZones; i++ ) {
			this.zones.put(i, new Station[maxChannelsPerZone]);
		}
		
		// Add Talkgroups to the Digital Contacts List
		
		
		//this.contacts.put(ANALOGUE_CONTACT_LIST, new Station[maxAnalogueContacts]);
		//this.contacts.put(DIGITAL_CONTACT_LIST, new Station[maxDigitalContacts]);
	}

	@Override
	public void addRepeater(int zone, Station station) {
		//Station[] this.zones.put(zone, station);
	}

	@Override
	public void addContact(int contact_type, Station station) {
		
	}

}
