package scraper.radio_output;

import scraper.station.Station;

public interface RadioOutput {
	public void addRepeater(int zone, Station station);
	public void addContact(int contact_type, Station station);
}
