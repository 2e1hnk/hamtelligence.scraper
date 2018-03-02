package scraper.ukrepeater;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.google.gson.Gson;

public class Repeater {
	private String callsign;
	private String url;
	private String channel;
	private String band;
	private float output;
	private float input;
	private Set<String> modes = new HashSet<String>();
	private String qra;
	private String location;
	private String gridRef;
	private String region;
	private String ctcss;
	private String groupUrl;
	private String keeper;
	private String keeperName;
	private float latitude;
	private float longitude;
	private String status;
	private Date databaseEntryDate;
	private Date novIssueDate;
	private Date renewalDate;

	private int dmrColourCode;
	private String dmrNetwork;

	public static final String LICENSED = "LICENSED";
	public static final String OPERATING = "OPERATING";
	public static final String NOT_OP = "NOT_OP";
	public static final String REDUCED = "REDUCED";

	public static final String AV = "AV";
	public static final String FUSION = "FUSION";
	public static final String DSTAR = "DSTAR";
	public static final String DMR = "DMR";

	public String getCallsign() {
		return callsign;
	}

	public void setCallsign(String callsign) {
		this.callsign = callsign;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getBand() {
		return band;
	}

	public void setBand(String band) {
		this.band = band;
	}

	public float getOutput() {
		return output;
	}

	public void setOutput(float output) {
		this.output = output;
	}

	public float getInput() {
		return input;
	}

	public void setInput(float input) {
		this.input = input;
	}

	public Set<String> getModes() {
		return modes;
	}

	public String getModesAsString() {
		return String.join(", ", this.modes);
	}

	public void setModes(Set<String> modes) {
		this.modes = modes;
	}

	public void setModes(String modes) {
		for (String mode : modes.split("/")) {
			this.addMode(mode);
		}
	}

	public static String getModeName(String mode) {
		switch (mode) {
		case "AV":
		case "ANL":
			return AV;
		case "D-STAR":
		case "DSTAR":
			return DSTAR;
		case "C4FM":
		case "FUSION":
			return FUSION;
		case "DMR":
			return DMR;
		case "MULTI":
			return null;
		default:
			System.out.println("!!! UNRECOGNISED MODE: " + mode);
			return null;
		}
	}

	public void addMode(String mode) {
		if (getModeName(mode) != null) {
			this.modes.add(getModeName(mode));
		}
	}

	public String getQra() {
		return qra;
	}

	public void setQra(String qra) {
		this.qra = qra;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getGridRef() {
		return gridRef;
	}

	public void setGridRef(String gridRef) {
		this.gridRef = gridRef;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getCtcss() {
		return ctcss;
	}

	public void setCtcss(String ctcss) {
		this.ctcss = ctcss;
	}

	public String getGroupUrl() {
		return groupUrl;
	}

	public void setGroupUrl(String url) {
		this.groupUrl = url;
	}

	public String getKeeper() {
		return keeper;
	}

	public void setKeeper(String keeper) {
		this.keeper = keeper;
	}

	public float getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		try {
			this.latitude = Float.parseFloat(latitude);
		} catch (Exception e) {

		}
	}

	public float getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		try {
			this.longitude = Float.parseFloat(longitude);
		} catch (Exception e) {

		}
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getDmrColourCode() {
		return dmrColourCode;
	}

	public void setDmrColourCode(int dmpColourCode) {
		this.dmrColourCode = dmpColourCode;
	}

	public String getDmrNetwork() {
		return dmrNetwork;
	}

	public void setDmrNetwork(String dmrNetwork) {
		this.dmrNetwork = dmrNetwork;
	}

	public String getKeeperName() {
		return keeperName;
	}

	public void setKeeperName(String keeperName) {
		this.keeperName = keeperName;
	}

	public Date getDatabaseEntryDate() {
		return databaseEntryDate;
	}

	public void setDatabaseEntryDate(Date databaseEntryDate) {
		this.databaseEntryDate = databaseEntryDate;
	}

	public Date getNovIssueDate() {
		return novIssueDate;
	}

	public void setNovIssueDate(Date novIssueDate) {
		this.novIssueDate = novIssueDate;
	}

	public Date getRenewalDate() {
		return renewalDate;
	}

	public void setRenewalDate(Date renewalDate) {
		this.renewalDate = renewalDate;
	}

	public String toJson() {
		Gson gson = new Gson();
		return gson.toJson(this);
	}
	
	public String toKml() {
		StringBuilder sb = new StringBuilder();
		sb.append("    <Placemark>\n");
		sb.append(String.format("      <name>%s</name>\n", this.getCallsign()));
		sb.append(String.format("      <styleUrl>#%s</styleUrl>", this.getStatus()));
		sb.append("      <description><![CDATA[");
		sb.append(String.format("      <h1>%s (%s)</h1>", this.getCallsign(), this.getStatus()));
		sb.append(String.format("      <b>Channel:</b> %s (%s)<br />", this.getChannel(), this.getBand()));
		sb.append(String.format("      <b>Mode(s):</b> %s<br />", this.getModesAsString()));
		sb.append(String.format("      <b>Repeater RX:</b> %s<br />", this.getInput()));
		sb.append(String.format("      <b>Repeater TX:</b> %s<br />", this.getOutput()));
		sb.append(String.format("      <b>Location:</b> %s, %s<br />", this.getLocation(), this.getRegion()));
		sb.append(String.format("      <b>Keeper:</b> %s, %s<br />", this.getKeeperName(), this.getKeeper()));
		sb.append("      ]]></description>\n");
		sb.append("      <Point>\n");
		sb.append(String.format("        <coordinates>%f,%f,0</coordinates>\n", this.getLongitude(), this.getLatitude()));
		sb.append("      </Point>\n");
		sb.append("    </Placemark>");
		return sb.toString();
	}
	
	public String toMotorolaXml() {
		
		return null;
	}
}
