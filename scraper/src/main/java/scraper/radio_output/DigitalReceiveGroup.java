package scraper.radio_output;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="RX_TG_LIST_DLT_TYPE")
public class DigitalReceiveGroup {
	private DigitalReceiveGroupHeader header;
	private List<DigitalReceiveGroupEntry> entries = new ArrayList<DigitalReceiveGroupEntry>();
	
	public DigitalReceiveGroup() {
		
	}

	@XmlElement(name = "RX_TG_LIST_DLH_TYPE")
	public DigitalReceiveGroupHeader getHeader() {
		return header;
	}

	public void setHeader(DigitalReceiveGroupHeader header) {
		this.header = header;
	}

	@XmlElement(name = "RX_TG_LIST_DLL_TYPE")
	public List<DigitalReceiveGroupEntry> getEntries() {
		return entries;
	}

	public void setEntries(List<DigitalReceiveGroupEntry> entries) {
		this.entries = entries;
	}
	
	public void addEntry(DigitalReceiveGroupEntry entry) {
		this.entries.add(entry);
	}
	
	
}
