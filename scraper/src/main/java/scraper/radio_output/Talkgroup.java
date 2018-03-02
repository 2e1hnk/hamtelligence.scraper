package scraper.radio_output;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

public class Talkgroup implements Comparable<Talkgroup> {
	private int slot;
	private int id;
	private String name;
	
	public Talkgroup() {
		
	}
	
	public Talkgroup(int slot, int id, String name) {
		this.setSlot(slot);
		this.setId(id);
		this.setName(name);
	}

	public int getSlot() {
		return slot;
	}

	public void setSlot(int slot) {
		this.slot = slot;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public DigitalContact getTalkgroupAsContact(int listID, int listLetID) {
		return new DigitalContact(listID, listLetID, "GRPCALL", this.getId(), this.getName(), "Enabled");
	}
	
	public DigitalReceiveGroup getTalkgroupAsReceiveList(int listID) {
		DigitalReceiveGroup drg = new DigitalReceiveGroup();
		drg.setHeader(new DigitalReceiveGroupHeader(this.getName(), "Enabled", listID));
		// TODO: first zero below needs to change to the ListLetID of the talkgroup in the digital contacts list
		drg.addEntry(new DigitalReceiveGroupEntry("Enabled", 0, listID, 0));
		return drg;
	}
	
	public String toString() {
		return String.format("slot:%s talkgroup:%s name:%s", this.getSlot(), this.getId(), this.getName());
	}

	@Override
	public int compareTo(Talkgroup o) {
		return this.toString().compareTo(o.toString());
	}
	
	public String getTalkgroupAsContactXml(int listID, int listLetID) throws JAXBException {
		
		DigitalContact talkgroup = this.getTalkgroupAsContact(listID, listLetID);
		
		JAXBContext context = JAXBContext.newInstance(DigitalContact.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        
        StringWriter sw = new StringWriter();
        marshaller.marshal(talkgroup, sw);

        String result = sw.toString();
        
        return result;
	}

	public String getTalkgroupAsReceiveGroupXml(int listID) throws JAXBException {
		
		DigitalReceiveGroup talkgroup = this.getTalkgroupAsReceiveList(listID);
		
		JAXBContext context = JAXBContext.newInstance(DigitalReceiveGroup.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        
        StringWriter sw = new StringWriter();
        marshaller.marshal(talkgroup, sw);

        String result = sw.toString();
        
        return result;
	}
}

