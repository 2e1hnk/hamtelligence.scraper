package scraper.station;

import java.util.Date;

import com.google.gson.Gson;

public class Activity implements VersionedAttribute {
	private String spotter;
	private Double frequency;
	private String comments;
	private int time;
	private String gridsquare;
	private String mode="";
	private int signalStrength=0;
	private int speed=0;
	private Date created_at;
	
	public Activity() {
		this.setCreated_at(new Date());
	}
	
	public Activity(String spotter, Double frequency, String comments, int time, String gridsquare) {
		this.setCreated_at(new Date());
		this.spotter = spotter;
		this.frequency = frequency;
		this.comments = comments;
		this.time = time;
		this.gridsquare = gridsquare;
	}

	public Activity(String spotter, Double frequency, String comments, int time, String gridsquare, String mode) {
		this(spotter, frequency, comments, time, gridsquare);
		this.setMode(mode);
	}

	public Activity(String spotter, Double frequency, String comments, int time, String gridsquare, String mode, int signalStrength, int speed) {
		this(spotter, frequency, comments, time, gridsquare, mode);
		this.setSignalStrength(signalStrength);
		this.setSpeed(speed);
	}

	public String getSpotter() {
		return spotter;
	}

	public void setSpotter(String spotter) {
		this.spotter = spotter;
	}

	public Double getFrequency() {
		return frequency;
	}

	public void setFrequency(Double frequency) {
		this.frequency = frequency;
	}
	
	public String getBand() {
		if ( this.frequency >= 1.8 && this.frequency <= 2 )
			return "160m";
		if ( this.frequency >= 3.5 && this.frequency <= 4 )
			return "80m";
		if ( this.frequency >= 5 && this.frequency <= 6 )
			return "60m";
		if ( this.frequency >= 7 && this.frequency <= 7.3 )
			return "40m";
		if ( this.frequency >= 10 && this.frequency <= 10.3 )
			return "30m";
		if ( this.frequency >= 14 && this.frequency <= 14.35 )
			return "20m";
		if ( this.frequency >= 18.068 && this.frequency <= 18.168 )
			return "17m";
		if ( this.frequency >= 21 && this.frequency <= 21.45 )
			return "15m";
		if ( this.frequency >= 24.89 && this.frequency <= 24.99 )
			return "12m";
		if ( this.frequency >= 28 && this.frequency <= 29.7 )
			return "10m";
		if ( this.frequency >= 50 && this.frequency <= 52 )
			return "6m";
		if ( this.frequency >= 70 && this.frequency <= 70.5 )
			return "4m";
		if ( this.frequency >= 144 && this.frequency <= 146 )
			return "2m";
		if ( this.frequency >= 430 && this.frequency <= 440 )
			return "70cm";
		
		return "unknown";
		
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments.trim();
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public String getGridsquare() {
		return gridsquare;
	}

	public void setGridsquare(String gridsquare) {
		this.gridsquare = gridsquare;
	}

	public Date getCreated_at() {
		return created_at;
	}

	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public int getSignalStrength() {
		return signalStrength;
	}

	public void setSignalStrength(int signalStrength) {
		this.signalStrength = signalStrength;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}
	
	public String toJson() {
		Gson gson = new Gson();
		return gson.toJson(this);
	}

}
