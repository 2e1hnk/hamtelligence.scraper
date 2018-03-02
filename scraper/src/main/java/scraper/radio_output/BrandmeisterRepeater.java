package scraper.radio_output;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

public class BrandmeisterRepeater extends DmrRepeater {
	private List<Talkgroup> talkgroups = new ArrayList<Talkgroup>();
	
	public BrandmeisterRepeater() {
		this.talkgroups.add(new Talkgroup(1, 9990, "Echo Test"));
		this.talkgroups.add(new Talkgroup(1, 91, "Worldwide"));
		this.talkgroups.add(new Talkgroup(1, 92, "Europe"));
		this.talkgroups.add(new Talkgroup(1, 93, "USA"));
		this.talkgroups.add(new Talkgroup(1, 9, "Local"));
		this.talkgroups.add(new Talkgroup(2, 9, "Reflectors"));
		this.talkgroups.add(new Talkgroup(2, 9990, "Echo Test"));
	}
	
	public String getMototrboXml() throws JAXBException {
		StringBuilder sb = new StringBuilder();
		
		// Digital Contacts
		int i = 0;
		for ( Talkgroup tg : this.talkgroups ) {
			sb.append(tg.getTalkgroupAsContactXml(0, i));
			i++;
		}
		
		// Receive Groups
		i = 0;
		for ( Talkgroup tg : this.talkgroups ) {
			sb.append(tg.getTalkgroupAsReceiveGroupXml(i));
			i++;
		}
		
		return sb.toString();
	}

}