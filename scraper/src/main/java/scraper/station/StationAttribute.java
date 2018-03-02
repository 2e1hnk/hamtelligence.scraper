package scraper.station;

import java.util.Date;

import com.google.gson.Gson;

import scraper.Scraper;

public class StationAttribute implements VersionedAttribute {
	private Date created_at;
	private Date updated_at;
	private int version;
	private String contents;
	
	public StationAttribute() {
		
	}
	
	public StationAttribute(String contents) {
		this();
		try {
			this.setContents(contents);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setContents(String contents) throws Exception {
		
		Date now = new Date();
		
		if ( this.contents == null || this.contents.isEmpty() ) {
			// this is a creation
			this.created_at = now;
			this.updated_at = now;
			this.version = 1;
			this.contents = contents;
		} else if ( !this.contents.equals(contents) ) {
			// this is an update
			this.updated_at = now;
			this.version += 1;
			this.contents = contents;
		} else if (this.contents.equals(contents)) {
			// there is no change
		} else {
			// We should never get here...
			Scraper.getLogger().error("Error setting StationAttribute to \"" + contents + "\" (previously \"" + this.contents + "\")");
			throw new Exception();
		}
	}
	
	public String getContents() {
		return contents;
	}

	public Date getCreated_at() {
		return created_at;
	}

	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}

	public Date getUpdated_at() {
		return updated_at;
	}

	public void setUpdated_at(Date updated_at) {
		this.updated_at = updated_at;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public String toString() {
		return this.contents.toString();
	}
	
	public String toJson() {
		return (new Gson()).toJson(this);
	}
}
