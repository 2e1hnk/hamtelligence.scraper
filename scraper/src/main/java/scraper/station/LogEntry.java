package scraper.station;

import java.util.Date;

public class LogEntry implements VersionedAttribute {
	String callsign;
	private Date created_at;
	
	public LogEntry() {
		this.setCreated_at(new Date());
	}
	
	public LogEntry(String callsign) {
		this.setCreated_at(new Date());
		this.setCallsign(callsign);
	}
	
	public String getCallsign() {
		return callsign;
	}

	public void setCallsign(String callsign) {
		this.callsign = callsign;
	}

	public Date getCreated_at() {
		return created_at;
	}

	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}


}
