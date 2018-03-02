package scraper.station;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.google.gson.Gson;

import scraper.Scraper;

public class Station {
	private String callsign;
	private List<Name> names;
	private Set<Long> dmr_ids;
	private List<Location> locations;
	private List<Address> addresses;
	private List<Activity> activity;
	private List<LogEntry> logbook;
	private Map<String, StationAttribute> attributes = new HashMap<String, StationAttribute>();
	private String locator;
	
	public Station() {
		this.dmr_ids = new HashSet<Long>();
		this.locations = new ArrayList<Location>();
		this.addresses = new ArrayList<Address>();
		this.activity = new ArrayList<Activity>();
		this.logbook = new ArrayList<LogEntry>();
		}
	
	public Station(String callsign) {
		this();
		this.setCallsign(callsign);
	}

	public String getCallsign() {
		return callsign;
	}
	
	public static String normaliseCallsign(String callsign) {
		if ( callsign.contains("-") ) {
			callsign = callsign.substring(0, callsign.indexOf("-"));
			Scraper.getLogger().debug("Reduced callsign to " + callsign);
		}
		
		if ( callsign.contains("/") ) {
			String[] parts = callsign.split("/");
			if ( parts.length >= 3 ) {
				callsign = parts[1];
				Scraper.getLogger().debug("Reduced callsign to " + callsign);
			} else {
				if ( parts[0].length() > parts[1].length() ) {
					callsign = parts[0];
					Scraper.getLogger().debug("Reduced callsign to " + callsign);
				} else {
					callsign = parts[1];
					Scraper.getLogger().debug("Reduced callsign to " + callsign);
				}
			}
		}
		
		return callsign.toUpperCase();
	}

	public void setCallsign(String callsign) {
		this.callsign = normaliseCallsign(callsign);
	}
	
	/*
	public String getNameAndCallsign() {
		String namePart = "";
		List<String> fullName = new ArrayList<String>();
		
		if ( !this.getNames().isEmpty() ) {
			Name name = null;
			
			for ( Name interimName : this.getNames() ) {
				name = interimName;
			}
			
			if ( !name.forename.isEmpty() ) {
				fullName.add(name.forename);
			}
			if ( !name.middlename.isEmpty() ) {
				fullName.add(name.middlename);
			}
			if ( !name.surname.isEmpty() ) {
				fullName.add(name.surname);
			}
			namePart = String.join(" ", fullName);
		} else {
		
			if ( !this.getAttribute("forename").getContents().isEmpty() ) {
				fullName.add(this.getAttribute("forename").getContents());
			}
			if ( !this.getAttribute("middlename").getContents().isEmpty() ) {
				fullName.add(this.getAttribute("middlename").getContents());
			}
			if ( !this.getAttribute("surname").getContents().isEmpty() ) {
				fullName.add(this.getAttribute("surname").getContents());
			}
			namePart = String.join(" ", fullName);
		//}
		
		if ( !namePart.isEmpty() ) {
			return namePart + ", " + this.getCallsign();
		} else {
			return this.getCallsign();
		}
	}
	*/
	public List<Name> getNames() {
		return names;
	}

	public void setNames(List<Name> names) {
		this.names = names;
	}

	public void addName(Name name) {
		this.names.add(name);
	}

	public Set<Long> getDmr_ids() {
		return this.dmr_ids;
	}

	public void setDmr_ids(Set<Long> radio_identifiers) {
		this.dmr_ids = radio_identifiers;
	}

	public void addDmr_id(Long radio_identifier) {
		this.dmr_ids.add(radio_identifier);
	}

	public List<Location> getLocations() {
		return locations;
	}

	public void setLocations(List<Location> locations) {
		this.locations = locations;
	}

	public void addLocation(Location location) {
		this.locations.add(location);
	}

	public List<Address> getAddresses() {
		return addresses;
	}

	public void setAddresses(List<Address> addresses) {
		this.addresses = addresses;
	}

	public void addAddress(Address address) {
		if ( !this.addresses.contains(address) ) {
			this.addresses.add(address);
		}
	}

	public String getLocator() {
		return locator;
	}

	public void setLocator(String locator) {
		this.locator = locator;
	}

	public List<Activity> getActivity() {
		return activity;
	}

	public void setActivity(List<Activity> activity) {
		this.activity = activity;
	}
	
	public void addActivity(Activity activity) {
		this.activity.add(activity);
	}

	public List<LogEntry> getLogbook() {
		return logbook;
	}

	public void setLogbook(List<LogEntry> logbook) {
		this.logbook = logbook;
	}
	
	public void addLogbookEntry(LogEntry logEntry) {
		this.logbook.add(logEntry);
	}

	public Map<String, StationAttribute> getAttribute() {
		return attributes;
	}

	public StationAttribute getAttribute(String attribute) {
		return attributes.get(attribute);
	}

	public String getAttributeAsString(String attribute) {
		return attributes.get(attribute).toString();
	}

	public String getAttributeAsJsonString(String attribute) {
		return attributes.get(attribute).toJson();
	}

	public void setAttribute(Map<String, StationAttribute> attribute) {
		this.attributes = attribute;
	}
	
	public void setAttribute(String key, String value) {
		try {
			if ( value != null ) {
				if ( this.getAttribute().keySet().contains(key) ) {
					this.getAttribute().get(key).setContents(value);
				} else {
					StationAttribute sa = new StationAttribute(value);
					this.attributes.put(key, sa);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String toJson() {
		Gson gson = new Gson();
		return gson.toJson(this);
	}
	
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
	
	/*
	 * This creates an unencrypted XML block for a MOTOTRBO codeplug
	 * 
	 * @param num Unique sequential number in the codeplug
	 */
	public String toMotorolaXml(int num) {
		String alertType = "MOMENTARY";		// MOMENTARY || REPETATIVE
		
		StringBuilder sb = new StringBuilder();
		
		for ( Long id : this.getDmr_ids() ) {
			String name = this.getCallsign();
			
			if ( this.getAttribute("name") != null ) {
				name += " " + this.getAttribute("name");
			}
			
			sb.append("<DIGITAL_UCL_DLL_TYPE Applicable=\"Enabled\" ListID=\"0\" ListLetID=\"" + num + "\">\n");
			sb.append("\t<DIGITAL_UCL_DLL_TYPE_NTLLISHTYPE Applicable=\"Enabled\" ListID=\"0\" ListLetID=\"" + num + "\">NULL</DIGITAL_UCL_DLL_TYPE_NTLLISHTYPE>\n");
			sb.append("\t<DIGITAL_UCL_DLL_TYPE_NTLLISHID Applicable=\"Enabled\" ListID=\"0\" ListLetID=\"" + num + "\">0</DIGITAL_UCL_DLL_TYPE_NTLLISHID>\n");
			sb.append("\t<DU_LLEQ Applicable=\"Enabled\" ListID=\"0\" ListLetID=\"" + num + "\">1</DU_LLEQ>\n");
			sb.append("\t<DU_CALLTYPE Applicable=\"Enabled\" ListID=\"0\" ListLetID=\"" + num + "\">PRIVCALL</DU_CALLTYPE>\n");
			sb.append("\t<DU_UKPOTCFLG Applicable=\"Enabled\" ListID=\"0\" ListLetID=\"" + num + "\">0</DU_UKPOTCFLG>\n");
			sb.append("\t<DU_CALLPRCDTNEN Applicable=\"Enabled\" ListID=\"0\" ListLetID=\"" + num + "\">1</DU_CALLPRCDTNEN>\n");
			sb.append("\t<DU_ROUTETYPE Applicable=\"Enabled\" ListID=\"0\" ListLetID=\"" + num + "\">REGULAR</DU_ROUTETYPE>\n");
			sb.append("\t<DU_TXTMSGALTTNTP Applicable=\"Enabled\" ListID=\"0\" ListLetID=\"" + num + "\">" + alertType + "</DU_TXTMSGALTTNTP>\n");
			sb.append("\t<DU_RVRTPERS TypeID=\"SELECTED\" Applicable=\"Enabled\" ListID=\"0\" ListLetID=\"" + num + "\">0</DU_RVRTPERS>\n");
			sb.append("\t<DU_RVRTPERSTYPE Applicable=\"Enabled\" ListID=\"0\" ListLetID=\"" + num + "\">SELECTED</DU_RVRTPERSTYPE>\n");
			sb.append("\t<DU_RVRTPERSID Applicable=\"Enabled\" ListID=\"0\" ListLetID=\"" + num + "\">0</DU_RVRTPERSID>\n");
			sb.append("\t<DU_CALLLSTID Applicable=\"Enabled\" ListID=\"0\" ListLetID=\"" + num + "\">" + id + "</DU_CALLLSTID>\n");
			sb.append("\t<DU_CALLALIAS Applicable=\"Enabled\" ListID=\"0\" ListLetID=\"" + num + "\">" + name + "</DU_CALLALIAS>\n");
			sb.append("\t<DU_RINGTYPE Applicable=\"Enabled\" ListID=\"0\" ListLetID=\"" + num + "\">NOSTYLE</DU_RINGTYPE>\n");
			sb.append("\t<DU_CONNECTTYPE Applicable=\"Disabled\" ListID=\"0\" ListLetID=\"" + num + "\">USB</DU_CONNECTTYPE>\n");
			sb.append("\t<DUL_RESERVED Applicable=\"Enabled\" ListID=\"0\" ListLetID=\"" + num + "\">0</DUL_RESERVED>\n");
			sb.append("</DIGITAL_UCL_DLL_TYPE>\n");
		}
		
		return sb.toString();
	}

}
