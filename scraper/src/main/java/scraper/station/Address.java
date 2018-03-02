package scraper.station;

import com.google.gson.Gson;
import java.util.Objects;

public class Address implements Comparable<Address> {
	public String street;
	public String city;
	public String region;
	public String country;

	public String toJson() {
		Gson gson = new Gson();
		return gson.toJson(this);
	}

	@Override
	public int compareTo(Address o) {
		if (o.street.equals(street) && o.city.equals(city) && o.region.equals(region) && o.country.equals(country)) {
			return 0;
		} else {
			return -1;
		}
	}

	@Override
	public boolean equals(Object o) {

		if (o == this)
			return true;

		if (!(o instanceof Address)) {
			return false;
		}

		Address address = (Address) o;
		try {
			return address.street.equals(street) && address.city.equals(city) && address.region.equals(region)
					&& address.country.equals(country);
		} catch (NullPointerException e) {
			return false;
		}
	}

	// Idea from effective Java : Item 9
	@Override
	public int hashCode() {
		return Objects.hash(street, city, region, country);
	}
}