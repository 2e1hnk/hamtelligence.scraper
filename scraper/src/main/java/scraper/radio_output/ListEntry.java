package scraper.radio_output;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;

public class ListEntry {
	private String Applicable = "Enabled"; // Enabled | Disabled | NA
	private String TypeID = null;
	private int ListID;
	private int ListLetID;
	private String value;

	public ListEntry() {

	}

	public ListEntry(String applicable, String typeId, int listId, int listLetId, String value) {
		this.setApplicable(applicable);
		this.setTypeID(typeId);
		this.setListID(listId);
		this.setListLetID(listLetId);
		this.setValue(value);
	}

	public ListEntry(String applicable, String typeId, int listId, String value) {
		this.setApplicable(applicable);
		this.setTypeID(typeId);
		this.setListID(listId);
		this.setValue(value);
	}

	@XmlValue
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@XmlAttribute(name="Applicable")
	public String getApplicable() {
		return Applicable;
	}

	public void setApplicable(String applicable) {
		Applicable = applicable;
	}

	@XmlAttribute(name="TypeID")
	public String getTypeID() {
		return TypeID;
	}

	public void setTypeID(String typeID) {
		TypeID = typeID;
	}

	@XmlAttribute(name="ListID")
	public int getListID() {
		return ListID;
	}

	public void setListID(int listID) {
		ListID = listID;
	}

	@XmlAttribute(name="ListLetID")
	public int getListLetID() {
		return ListLetID;
	}

	public void setListLetID(int listLetID) {
		ListLetID = listLetID;
	}
}
