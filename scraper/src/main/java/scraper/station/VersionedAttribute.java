package scraper.station;

import java.util.Date;

public interface VersionedAttribute {
	public Date created_at = null;
	public Date updated_at = null;
	public int version = 1;
}
