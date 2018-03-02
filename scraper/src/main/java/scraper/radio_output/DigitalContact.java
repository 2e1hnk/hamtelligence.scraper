package scraper.radio_output;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "DIGITAL_UCL_DLL_TYPE")
@XmlType(propOrder = { "DIGITAL_UCL_DLL_TYPE_NTLLISHTYPE", "DIGITAL_UCL_DLL_TYPE_NTLLISHID", "DU_LLEQ", "DU_CALLTYPE",
		"DU_UKPOTCFLG", "DU_CALLPRCDTNEN", "DU_ROUTETYPE", "DU_TXTMSGALTTNTP", "DU_RVRTPERS", "DU_RVRTPERSTYPE",
		"DU_RVRTPERSID", "DU_CALLLSTID", "DU_CALLALIAS", "DU_RINGTYPE", "DU_CONNECTTYPE", "DUL_RESERVED" })
public class DigitalContact {
	private String applicable = "Enabled";
	private int ListId; // ? List ID 0 = Digital Contacts, 1 = Analogue Contacts
	private int ListLetID; // List entry number (increments)
	private ListEntry DIGITAL_UCL_DLL_TYPE_NTLLISHTYPE; // ?
	private ListEntry DIGITAL_UCL_DLL_TYPE_NTLLISHID; // ?
	private ListEntry DU_LLEQ; // ?
	private ListEntry DU_CALLTYPE; // GRPCALL | PRIVCALL
	private ListEntry DU_UKPOTCFLG; // ?
	private ListEntry DU_CALLPRCDTNEN; // ?
	private ListEntry DU_ROUTETYPE; // ?
	private ListEntry DU_TXTMSGALTTNTP; // Text Message Alert type MOMENTARY | REPETITIVE
	private ListEntry DU_RVRTPERS; // ?
	private ListEntry DU_RVRTPERSTYPE; // ?
	private ListEntry DU_RVRTPERSID; // ?
	private ListEntry DU_CALLLSTID; // Call ID (Talkgroup number/terminal ID)
	private ListEntry DU_CALLALIAS; // Caller Alias
	private ListEntry DU_RINGTYPE; // Ringer type
	private ListEntry DU_CONNECTTYPE; // Connection Type (USB or BLUETOOTH?)
	private ListEntry DUL_RESERVED; // Unknown - presumably reserved?

	public DigitalContact() {

	}

	public DigitalContact(int listID, int listLetID, String callType, int callLstID, String callerAlias,
			String applicable) {
		this();
		this.setApplicable(applicable);
		this.setListId(listID);
		this.setListLetID(listLetID);
		this.setDU_CALLTYPE(new ListEntry(applicable, null, listID, listLetID, callType));
		this.setDU_CALLLSTID(String.valueOf(callLstID));
		this.setDU_CALLALIAS(callerAlias);

		// 'Standard' fields
		this.setDIGITAL_UCL_DLL_TYPE_NTLLISHTYPE("NULL");
		this.setDIGITAL_UCL_DLL_TYPE_NTLLISHID("0");
		this.setDU_LLEQ("1");
		this.setDU_UKPOTCFLG("0");
		this.setDU_CALLPRCDTNEN("1");
		this.setDU_ROUTETYPE("REGULAR");
		this.setDU_TXTMSGALTTNTP("MOMENTARY");
		this.setDU_RVRTPERS("0");
		this.setDU_RVRTPERSTYPE("SELECTED");
		this.setDU_RVRTPERSID("0");
		this.setDU_RINGTYPE("NOSTYLE");
		this.setDU_CONNECTTYPE("USB");
		this.setDUL_RESERVED("0");
	}

	@XmlAttribute(name = "Applicable")
	public String getApplicable() {
		return applicable;
	}

	public void setApplicable(String applicable) {
		this.applicable = applicable;
	}

	@XmlAttribute(name = "ListID")
	public int getListId() {
		return ListId;
	}

	public void setListId(int listId) {
		ListId = listId;
	}

	@XmlAttribute(name = "ListLetId")
	public int getListLetID() {
		return ListLetID;
	}

	public void setListLetID(int letListID) {
		ListLetID = letListID;
	}

	public ListEntry getDIGITAL_UCL_DLL_TYPE_NTLLISHTYPE() {
		return DIGITAL_UCL_DLL_TYPE_NTLLISHTYPE;
	}

	public void setDIGITAL_UCL_DLL_TYPE_NTLLISHTYPE(ListEntry dIGITAL_UCL_DLL_TYPE_NTLLISHTYPE) {
		DIGITAL_UCL_DLL_TYPE_NTLLISHTYPE = dIGITAL_UCL_DLL_TYPE_NTLLISHTYPE;
	}

	public void setDIGITAL_UCL_DLL_TYPE_NTLLISHTYPE(String value) {
		this.DIGITAL_UCL_DLL_TYPE_NTLLISHTYPE = new ListEntry(this.getApplicable(), null, this.getListId(),
				this.getListLetID(), value);
	}

	public ListEntry getDIGITAL_UCL_DLL_TYPE_NTLLISHID() {
		return DIGITAL_UCL_DLL_TYPE_NTLLISHID;
	}

	public void setDIGITAL_UCL_DLL_TYPE_NTLLISHID(ListEntry dIGITAL_UCL_DLL_TYPE_NTLLISHID) {
		DIGITAL_UCL_DLL_TYPE_NTLLISHID = dIGITAL_UCL_DLL_TYPE_NTLLISHID;
	}

	public void setDIGITAL_UCL_DLL_TYPE_NTLLISHID(String value) {
		this.DIGITAL_UCL_DLL_TYPE_NTLLISHID = new ListEntry(this.getApplicable(), null, this.getListId(),
				this.getListLetID(), value);
	}

	public ListEntry getDU_LLEQ() {
		return DU_LLEQ;
	}

	public void setDU_LLEQ(ListEntry dU_LLEQ) {
		DU_LLEQ = dU_LLEQ;
	}

	public void setDU_LLEQ(String value) {
		this.DU_LLEQ = new ListEntry(this.getApplicable(), null, this.getListId(), this.getListLetID(), value);
	}

	public ListEntry getDU_CALLTYPE() {
		return DU_CALLTYPE;
	}

	public void setDU_CALLTYPE(ListEntry dU_CALLTYPE) {
		DU_CALLTYPE = dU_CALLTYPE;
	}

	public void setDU_CALLTYPE(String value) {
		this.DU_CALLTYPE = new ListEntry(this.getApplicable(), null, this.getListId(), this.getListLetID(), value);
	}

	public ListEntry getDU_UKPOTCFLG() {
		return DU_UKPOTCFLG;
	}

	public void setDU_UKPOTCFLG(ListEntry dU_UKPOTCFLG) {
		DU_UKPOTCFLG = dU_UKPOTCFLG;
	}

	public void setDU_UKPOTCFLG(String value) {
		this.DU_UKPOTCFLG = new ListEntry(this.getApplicable(), null, this.getListId(), this.getListLetID(), value);
	}

	public ListEntry getDU_CALLPRCDTNEN() {
		return DU_CALLPRCDTNEN;
	}

	public void setDU_CALLPRCDTNEN(ListEntry dU_CALLPRCDTNEN) {
		DU_CALLPRCDTNEN = dU_CALLPRCDTNEN;
	}

	public void setDU_CALLPRCDTNEN(String value) {
		this.DU_CALLPRCDTNEN = new ListEntry(this.getApplicable(), null, this.getListId(), this.getListLetID(), value);
	}

	public ListEntry getDU_ROUTETYPE() {
		return DU_ROUTETYPE;
	}

	public void setDU_ROUTETYPE(ListEntry dU_ROUTETYPE) {
		DU_ROUTETYPE = dU_ROUTETYPE;
	}

	public void setDU_ROUTETYPE(String value) {
		this.DU_ROUTETYPE = new ListEntry(this.getApplicable(), null, this.getListId(), this.getListLetID(), value);
	}

	public ListEntry getDU_TXTMSGALTTNTP() {
		return DU_TXTMSGALTTNTP;
	}

	public void setDU_TXTMSGALTTNTP(ListEntry dU_TXTMSGALTTNTP) {
		DU_TXTMSGALTTNTP = dU_TXTMSGALTTNTP;
	}

	public void setDU_TXTMSGALTTNTP(String value) {
		this.DU_TXTMSGALTTNTP = new ListEntry(this.getApplicable(), null, this.getListId(), this.getListLetID(), value);
	}

	public ListEntry getDU_RVRTPERS() {
		return DU_RVRTPERS;
	}

	public void setDU_RVRTPERS(ListEntry dU_RVRTPERS) {
		DU_RVRTPERS = dU_RVRTPERS;
	}

	public void setDU_RVRTPERS(String value) {
		this.DU_RVRTPERS = new ListEntry(this.getApplicable(), null, this.getListId(), this.getListLetID(), value);
	}

	public ListEntry getDU_RVRTPERSTYPE() {
		return DU_RVRTPERSTYPE;
	}

	public void setDU_RVRTPERSTYPE(ListEntry dU_RVRTPERSTYPE) {
		DU_RVRTPERSTYPE = dU_RVRTPERSTYPE;
	}

	public void setDU_RVRTPERSTYPE(String value) {
		this.DU_RVRTPERSTYPE = new ListEntry(this.getApplicable(), null, this.getListId(), this.getListLetID(), value);
	}

	public ListEntry getDU_RVRTPERSID() {
		return DU_RVRTPERSID;
	}

	public void setDU_RVRTPERSID(ListEntry dU_RVRTPERSID) {
		DU_RVRTPERSID = dU_RVRTPERSID;
	}

	public void setDU_RVRTPERSID(String value) {
		this.DU_RVRTPERSID = new ListEntry(this.getApplicable(), null, this.getListId(), this.getListLetID(), value);
	}

	public ListEntry getDU_CALLLSTID() {
		return DU_CALLLSTID;
	}

	public void setDU_CALLLSTID(ListEntry dU_CALLLSTID) {
		DU_CALLLSTID = dU_CALLLSTID;
	}

	public void setDU_CALLLSTID(String value) {
		this.DU_CALLLSTID = new ListEntry(this.getApplicable(), null, this.getListId(), this.getListLetID(), value);
	}

	public ListEntry getDU_CALLALIAS() {
		return DU_CALLALIAS;
	}

	public void setDU_CALLALIAS(ListEntry dU_CALLALIAS) {
		DU_CALLALIAS = dU_CALLALIAS;
	}

	public void setDU_CALLALIAS(String value) {
		this.DU_CALLALIAS = new ListEntry(this.getApplicable(), null, this.getListId(), this.getListLetID(), value);
	}

	public ListEntry getDU_RINGTYPE() {
		return DU_RINGTYPE;
	}

	public void setDU_RINGTYPE(ListEntry dU_RINGTYPE) {
		DU_RINGTYPE = dU_RINGTYPE;
	}

	public void setDU_RINGTYPE(String value) {
		this.DU_RINGTYPE = new ListEntry(this.getApplicable(), null, this.getListId(), this.getListLetID(), value);
	}

	public ListEntry getDU_CONNECTTYPE() {
		return DU_CONNECTTYPE;
	}

	public void setDU_CONNECTTYPE(ListEntry dU_CONNECTTYPE) {
		DU_CONNECTTYPE = dU_CONNECTTYPE;
	}

	public void setDU_CONNECTTYPE(String value) {
		this.DU_CONNECTTYPE = new ListEntry(this.getApplicable(), null, this.getListId(), this.getListLetID(), value);
	}

	public ListEntry getDUL_RESERVED() {
		return DUL_RESERVED;
	}

	public void setDUL_RESERVED(ListEntry dUL_RESERVED) {
		DUL_RESERVED = dUL_RESERVED;
	}

	public void setDUL_RESERVED(String value) {
		this.DUL_RESERVED = new ListEntry(this.getApplicable(), null, this.getListId(), this.getListLetID(), value);
	}

}
