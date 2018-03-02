package scraper.aprs;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.Gson;

import net.ab0oo.aprs.parser.APRSPacket;
import net.ab0oo.aprs.parser.PositionPacket;
import scraper.Scraper;

public class AprsStatus {
	private String callsign = null;
	private String status = null;
	private Set<Float> frequencies = new HashSet<Float>();
	private float offset;
	private float ctcss;
	private int dmr_cc;
	private String url;
	
	public static final String frequency_regex = "([0-9]{3,}[\\.,][0-9]{2,})";
	public static final String offset_regex = "([\\-\\+])[\\s]?([0-9]{1,2}[\\.,]?[0-9]{1,}?)[\\s|\\\b|MHz]";
	public static final String ctcss_regex = "([0-9]{1,3}[\\.,]?[0-9]?)Hz";
	public static final String dmr_cc_regex = "(CC|Colour Code|Color Code)[:\\s]?([0-9]{1,2})";
	public static final String url_regex = "[-a-zA-Z0-9@:%._\\+~#=]{2,256}\\.[a-z]{2,6}\\b([-a-zA-Z0-9@:%_\\+.~#?&\\/\\/=]*)";
	
	
	public AprsStatus(APRSPacket packet) {
		
		this.setCallsign(packet.getSourceCall());
		this.setStatus(new String(packet.getAprsInformation().getRawBytes()));
		
		Pattern pattern;
		Matcher matcher;
		
		// Frequencies
		pattern = Pattern.compile(frequency_regex);
		matcher = pattern.matcher(this.getStatus());
		while (matcher.find()) {
			// Need to handle commas in frequency (e.g. 145,500)
            this.addFrequency(Float.valueOf(matcher.group()));
        }
		
		// Offset
		pattern = Pattern.compile(offset_regex);
		matcher = pattern.matcher(this.getStatus());
		while (matcher.find()) {
            this.setOffset(Float.valueOf(matcher.group()));
        }

		// CTCSS
		pattern = Pattern.compile(ctcss_regex);
		matcher = pattern.matcher(this.getStatus());
		while (matcher.find()) {
            this.setCtcss(Float.valueOf(matcher.group()));
        }

		// DMR Colour Code
		pattern = Pattern.compile(dmr_cc_regex);
		matcher = pattern.matcher(this.getStatus());
		while (matcher.find()) {
            this.setDmr_cc(Integer.valueOf(matcher.group()));
        }

		// URL
		pattern = Pattern.compile(url_regex);
		matcher = pattern.matcher(this.getStatus());
		while (matcher.find()) {
            this.setUrl(matcher.group());
        }

	}

	public String getCallsign() {
		return callsign;
	}

	public void setCallsign(String callsign) {
		this.callsign = callsign;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Set<Float> getFrequencies() {
		return frequencies;
	}

	public void setFrequencies(Set<Float> frequencies) {
		this.frequencies = frequencies;
	}
	
	public void addFrequency(Float frequency) {
		this.frequencies.add(frequency);
	}

	public float getOffset() {
		return offset;
	}

	public void setOffset(float offset) {
		this.offset = offset;
	}

	public float getCtcss() {
		return ctcss;
	}

	public void setCtcss(float ctcss) {
		this.ctcss = ctcss;
	}

	public int getDmr_cc() {
		return dmr_cc;
	}

	public void setDmr_cc(int dmr_cc) {
		this.dmr_cc = dmr_cc;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public String toJson() {
		Gson gson = new Gson();
		return gson.toJson(this);
	}

}
