package scraper.station;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import com.google.gson.Gson;

public class AllstarNode {
	public int node;
	public String channel;
	public String location;
	public Date lastSeen;
	public int uptimeWhenLastSeen;

	public AllstarNode() {

	}

	public AllstarNode(int node, String channel, String location, Date lastSeen, String uptimeWhenLastSeen) {
		this.setNode(node);
		this.setChannel(channel);
		this.setLocation(location);
		this.setLastSeen(lastSeen);
		this.setUptimeWhenLastSeen(uptimeWhenLastSeen);
	}

	public int getNode() {
		return node;
	}

	public void setNode(int node) {
		this.node = node;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Date getLastSeen() {
		return lastSeen;
	}

	public void setLastSeen(Date lastSeen) {
		this.lastSeen = lastSeen;
	}

	public int getUptimeWhenLastSeen() {
		return uptimeWhenLastSeen;
	}

	public void setUptimeWhenLastSeen(int uptimeWhenLastSeen) {
		this.uptimeWhenLastSeen = uptimeWhenLastSeen;
	}

	public void setUptimeWhenLastSeen(String uptimeWhenLastSeen) {
		LocalTime lt = LocalTime.parse(uptimeWhenLastSeen, DateTimeFormatter.ofPattern("h'H m'M s'S"));
		this.uptimeWhenLastSeen = lt.toSecondOfDay();
	}

	@Override
	public boolean equals(Object object) {
		boolean same = false;

		if (object instanceof AllstarNode) {
			same = this.node == ((AllstarNode) object).node;
		}

		return same;
	}

	@Override
	public int hashCode() {
		return node;
	}
	
	public String toJson() {
		Gson gson = new Gson();
		return gson.toJson(this);
	}
}
