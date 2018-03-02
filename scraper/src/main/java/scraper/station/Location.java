package scraper.station;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Location implements VersionedAttribute {
	public Map<String, String> data = new HashMap<String, String>();
	public Double latitude;
	public Double longitude;
	private Date created_at;
	
	public Location() {
		this.created_at = new Date();
	}
	
	public Date getCreated_at() {
		return this.created_at;
	}

	public Map<String, String> getData() {
		return data;
	}

	public void setData(Map<String, String> data) {
		this.data = data;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}
}
