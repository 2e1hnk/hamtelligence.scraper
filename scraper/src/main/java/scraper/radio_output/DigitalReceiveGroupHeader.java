package scraper.radio_output;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "RX_TG_LIST_DLH_TYPE")
@XmlType(propOrder = { "RXTG_LSTCTG", "RXTG_LEDSZ", "RXTG_LEQMAX", "RXTG_LLEQMAX", "RX_TG_LIST_DLH_TYPE_FTLLISHTYPE",
		"RX_TG_LIST_DLH_TYPE_FTLLISHID", "RXTG_RXTGLISTALIAS" })
public class DigitalReceiveGroupHeader {
	private String applicable = "Enabled";
	private int ListId; // ? List ID 0 = Digital Contacts, 1 = Analogue Contacts

	private ListEntry RXTG_LSTCTG;
	private ListEntry RXTG_LEDSZ;
	private ListEntry RXTG_LEQMAX;
	private ListEntry RXTG_LLEQMAX;
	private ListEntry RX_TG_LIST_DLH_TYPE_FTLLISHTYPE;
	private ListEntry RX_TG_LIST_DLH_TYPE_FTLLISHID;
	private ListEntry RXTG_RXTGLISTALIAS;

	public DigitalReceiveGroupHeader() {

	}
	
	public DigitalReceiveGroupHeader(String groupName, String applicable, int ListID) {
		this();
		this.setRXTG_RXTGLISTALIAS(groupName);
		this.setApplicable(applicable);
		this.setListId(ListID);
		
		// 'Standard Fields
		this.setRXTG_LSTCTG("1");
		this.setRXTG_LEDSZ("6");
		this.setRXTG_LEQMAX("16");
		this.setRXTG_LLEQMAX("1");
		this.setRX_TG_LIST_DLH_TYPE_FTLLISHTYPE("NULL");
		this.setRX_TG_LIST_DLH_TYPE_FTLLISHID("0");
	}
	
	@XmlAttribute
	public String getApplicable() {
		return applicable;
	}

	public void setApplicable(String applicable) {
		this.applicable = applicable;
	}

	@XmlAttribute
	public int getListId() {
		return ListId;
	}

	public void setListId(int listId) {
		ListId = listId;
	}

	public ListEntry getRXTG_LSTCTG() {
		return RXTG_LSTCTG;
	}

	public void setRXTG_LSTCTG(ListEntry rXTG_LSTCTG) {
		RXTG_LSTCTG = rXTG_LSTCTG;
	}

	public void setRXTG_LSTCTG(String value) {
		this.setRXTG_LSTCTG(new ListEntry(this.getApplicable(), null, this.getListId(), value));
	}

	public ListEntry getRXTG_LEDSZ() {
		return RXTG_LEDSZ;
	}

	public void setRXTG_LEDSZ(ListEntry rXTG_LEDSZ) {
		RXTG_LEDSZ = rXTG_LEDSZ;
	}

	public void setRXTG_LEDSZ(String value) {
		this.setRXTG_LEDSZ(new ListEntry(this.getApplicable(), null, this.getListId(), value));
	}

	public ListEntry getRXTG_LEQMAX() {
		return RXTG_LEQMAX;
	}

	public void setRXTG_LEQMAX(ListEntry rXTG_LEQMAX) {
		RXTG_LEQMAX = rXTG_LEQMAX;
	}

	public void setRXTG_LEQMAX(String value) {
		this.setRXTG_LEQMAX(new ListEntry(this.getApplicable(), null, this.getListId(), value));
	}

	public ListEntry getRXTG_LLEQMAX() {
		return RXTG_LLEQMAX;
	}

	public void setRXTG_LLEQMAX(ListEntry rXTG_LLEQMAX) {
		RXTG_LLEQMAX = rXTG_LLEQMAX;
	}

	public void setRXTG_LLEQMAX(String value) {
		this.setRXTG_LLEQMAX(new ListEntry(this.getApplicable(), null, this.getListId(), value));
	}

	public ListEntry getRX_TG_LIST_DLH_TYPE_FTLLISHTYPE() {
		return RX_TG_LIST_DLH_TYPE_FTLLISHTYPE;
	}

	public void setRX_TG_LIST_DLH_TYPE_FTLLISHTYPE(ListEntry rX_TG_LIST_DLH_TYPE_FTLLISHTYPE) {
		RX_TG_LIST_DLH_TYPE_FTLLISHTYPE = rX_TG_LIST_DLH_TYPE_FTLLISHTYPE;
	}

	public void setRX_TG_LIST_DLH_TYPE_FTLLISHTYPE(String value) {
		this.setRX_TG_LIST_DLH_TYPE_FTLLISHTYPE(new ListEntry(this.getApplicable(), null, this.getListId(), value));
	}

	public ListEntry getRX_TG_LIST_DLH_TYPE_FTLLISHID() {
		return RX_TG_LIST_DLH_TYPE_FTLLISHID;
	}

	public void setRX_TG_LIST_DLH_TYPE_FTLLISHID(ListEntry rX_TG_LIST_DLH_TYPE_FTLLISHID) {
		RX_TG_LIST_DLH_TYPE_FTLLISHID = rX_TG_LIST_DLH_TYPE_FTLLISHID;
	}

	public void setRX_TG_LIST_DLH_TYPE_FTLLISHID(String value) {
		this.setRX_TG_LIST_DLH_TYPE_FTLLISHID(new ListEntry(this.getApplicable(), null, this.getListId(), value));
	}

	public ListEntry getRXTG_RXTGLISTALIAS() {
		return RXTG_RXTGLISTALIAS;
	}

	public void setRXTG_RXTGLISTALIAS(ListEntry rXTG_RXTGLISTALIAS) {
		RXTG_RXTGLISTALIAS = rXTG_RXTGLISTALIAS;
	}

	public void setRXTG_RXTGLISTALIAS(String value) {
		this.setRXTG_RXTGLISTALIAS(new ListEntry(this.getApplicable(), null, this.getListId(), value));
	}

}