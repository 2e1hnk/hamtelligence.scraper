package scraper.radio_output;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="RX_TG_LIST_DLL_TYPE")
public class DigitalReceiveGroupEntry {
	private String applicable = "Enabled";
	private int ListId; // ? List ID 0 = Digital Contacts, 1 = Analogue Contacts
	private int ListLetID; // List entry number (increments)
	private ListEntry RX_TG_LIST_DLL_TYPE_NTLLISHTYPE;
	private ListEntry RX_TG_LIST_DLL_TYPE_NTLLISHID;
	private ListEntry RXTG_LLEQ;
	private ListEntry RXTG_DGUCLCOMP;
	private ListEntry RXTG_DGUCLCOMPTYPE;
	private ListEntry RXTG_DGUCLCOMPID;
	private ListEntry RXTGL_RESERVED;

	public DigitalReceiveGroupEntry () {
		
	}
	
	public DigitalReceiveGroupEntry(String applicable, int listID, int listLetID, int talkgroupId) {
		this.setApplicable(applicable);
		this.setListId(listID);
		this.setListLetID(listLetID);
		
		// 'Standard' fields
		this.setRX_TG_LIST_DLL_TYPE_NTLLISHTYPE("NULL");
		this.setRX_TG_LIST_DLL_TYPE_NTLLISHID("0");
		this.setRXTG_LLEQ("1");
		this.setRXTG_DGUCLCOMP(String.valueOf(talkgroupId));	// This is the ListLetID of the associated talkgroup
		this.setRXTG_DGUCLCOMPTYPE("DIGITAL_UCL_DLL_TYPE");
		this.setRXTG_DGUCLCOMPID(String.valueOf(talkgroupId));
		this.setRXTGL_RESERVED("0");
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

	@XmlAttribute
	public int getListLetID() {
		return ListLetID;
	}

	public void setListLetID(int listLetID) {
		ListLetID = listLetID;
	}

	public ListEntry getRX_TG_LIST_DLL_TYPE_NTLLISHTYPE() {
		return RX_TG_LIST_DLL_TYPE_NTLLISHTYPE;
	}

	public void setRX_TG_LIST_DLL_TYPE_NTLLISHTYPE(ListEntry rX_TG_LIST_DLL_TYPE_NTLLISHTYPE) {
		RX_TG_LIST_DLL_TYPE_NTLLISHTYPE = rX_TG_LIST_DLL_TYPE_NTLLISHTYPE;
	}

	public void setRX_TG_LIST_DLL_TYPE_NTLLISHTYPE(String value) {
		this.setRX_TG_LIST_DLL_TYPE_NTLLISHTYPE(new ListEntry(this.getApplicable(), null, this.getListId(), this.getListLetID(), value));
	}

	public ListEntry getRX_TG_LIST_DLL_TYPE_NTLLISHID() {
		return RX_TG_LIST_DLL_TYPE_NTLLISHID;
	}

	public void setRX_TG_LIST_DLL_TYPE_NTLLISHID(ListEntry rX_TG_LIST_DLL_TYPE_NTLLISHID) {
		RX_TG_LIST_DLL_TYPE_NTLLISHID = rX_TG_LIST_DLL_TYPE_NTLLISHID;
	}

	public void setRX_TG_LIST_DLL_TYPE_NTLLISHID(String value) {
		this.setRX_TG_LIST_DLL_TYPE_NTLLISHID(new ListEntry(this.getApplicable(), null, this.getListId(), this.getListLetID(), value));
	}

	public ListEntry getRXTG_LLEQ() {
		return RXTG_LLEQ;
	}

	public void setRXTG_LLEQ(ListEntry rXTG_LLEQ) {
		RXTG_LLEQ = rXTG_LLEQ;
	}

	public void setRXTG_LLEQ(String value) {
		this.setRXTG_LLEQ(new ListEntry(this.getApplicable(), null, this.getListId(), this.getListLetID(), value));
	}

	public ListEntry getRXTG_DGUCLCOMP() {
		return RXTG_DGUCLCOMP;
	}

	public void setRXTG_DGUCLCOMP(ListEntry rXTG_DGUCLCOMP) {
		RXTG_DGUCLCOMP = rXTG_DGUCLCOMP;
	}

	public void setRXTG_DGUCLCOMP(String value) {
		this.setRXTG_DGUCLCOMP(new ListEntry(this.getApplicable(), "DIGITAL_UCL_DLL_TYPE", this.getListId(), this.getListLetID(), value));
	}

	public ListEntry getRXTG_DGUCLCOMPTYPE() {
		return RXTG_DGUCLCOMPTYPE;
	}

	public void setRXTG_DGUCLCOMPTYPE(ListEntry rXTG_DGUCLCOMPTYPE) {
		RXTG_DGUCLCOMPTYPE = rXTG_DGUCLCOMPTYPE;
	}

	public void setRXTG_DGUCLCOMPTYPE(String value) {
		this.setRXTG_DGUCLCOMPTYPE(new ListEntry(this.getApplicable(), null, this.getListId(), this.getListLetID(), value));
	}

	public ListEntry getRXTG_DGUCLCOMPID() {
		return RXTG_DGUCLCOMPID;
	}

	public void setRXTG_DGUCLCOMPID(ListEntry rXTG_DGUCLCOMPID) {
		RXTG_DGUCLCOMPID = rXTG_DGUCLCOMPID;
	}

	public void setRXTG_DGUCLCOMPID(String value) {
		this.setRXTG_DGUCLCOMPID(new ListEntry(this.getApplicable(), null, this.getListId(), this.getListLetID(), value));
	}

	public ListEntry getRXTGL_RESERVED() {
		return RXTGL_RESERVED;
	}

	public void setRXTGL_RESERVED(ListEntry rXTGL_RESERVED) {
		RXTGL_RESERVED = rXTGL_RESERVED;
	}
	
	public void setRXTGL_RESERVED(String value) {
		this.setRXTGL_RESERVED(new ListEntry(this.getApplicable(), null, this.getListId(), this.getListLetID(), value));
	}
	
}