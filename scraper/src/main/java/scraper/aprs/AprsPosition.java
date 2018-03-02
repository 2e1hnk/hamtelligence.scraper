package scraper.aprs;

import net.ab0oo.aprs.parser.APRSPacket;
import net.ab0oo.aprs.parser.PositionPacket;

public class AprsPosition {
	private String callsign = null;
	private double latitude;
	private double longitude;
	
	public AprsPosition(APRSPacket packet) {
		this.setLatitude(((PositionPacket) packet.getAprsInformation()).getPosition().getLatitude());
		this.setLongitude(((PositionPacket) packet.getAprsInformation()).getPosition().getLongitude());
		this.setCallsign(packet.getSourceCall());
	}

	public String getCallsign() {
		return callsign;
	}

	public void setCallsign(String callsign) {
		this.callsign = callsign;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
}
