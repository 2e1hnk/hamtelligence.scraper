package scraper.station;

import java.util.Date;

public class GeoLocation implements VersionedAttribute {
	public Float latitude;
	public Float longitude;
	public int altitude_metres;
	
	private Date created_at;
	
	public GeoLocation() {
		this.setCreated_at(new Date());
	}
	
	public GeoLocation(Float latitude, Float longitude) {
		this.setCreated_at(new Date());
		this.setLatitude(latitude);
		this.setLongitude(longitude);
	}

	public GeoLocation(double latitude, double longitude) {
		this.setCreated_at(new Date());
		this.setLatitude((float) latitude);
		this.setLongitude((float) longitude);
	}

	public Float getLatitude() {
		return latitude;
	}

	public void setLatitude(Float latitude) {
		this.latitude = latitude;
	}

	public Float getLongitude() {
		return longitude;
	}

	public void setLongitude(Float longitude) {
		this.longitude = longitude;
	}

	public int getAltitude_metres() {
		return altitude_metres;
	}

	public void setAltitude_metres(int altitude_metres) {
		this.altitude_metres = altitude_metres;
	}

	public Date getCreated_at() {
		return created_at;
	}

	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}
}