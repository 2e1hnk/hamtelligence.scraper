package scraper.aprs;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.ab0oo.aprs.parser.APRSPacket;
import net.ab0oo.aprs.parser.MessagePacket;
import net.ab0oo.aprs.parser.PositionPacket;
import scraper.Scraper;
import scraper.station.GeoLocation;
import scraper.station.Location;
import scraper.station.Station;
import scraper.station.StationAttribute;

public class MessageHandler {

	private Map<String, Map<String, AprsMessage>> history;

	public MessageHandler() {
		this.history = new HashMap<String, Map<String, AprsMessage>>();
	}

	public void handle(APRSPacket packet) {
		/*
		Map<String, AprsMessage> existingStream;
		if ( history.containsKey(this.getStreamId(packet))) {
			existingStream = history.get(this.getStreamId(packet));
		} else {
			existingStream = new HashMap<String, AprsMessage>();
		}
		*/
		/*
		if ( ((MessagePacket) packet.getAprsInformation()).isAck() ) {
			// Create a new AprsMessage object as an easy way to find the message to mark as ack'ed
			//AprsMessage tempMessage = new AprsMessage(packet);
			
			// Need to find out if we actually have the message - might have received the ack but not the original message!
			//if ( existingStream.get(tempMessage.getMessageId()) != null ) {
			//	existingStream.get(tempMessage.getMessageId()).setAck(true);
			//}
			
			//this.printStream(this.getStreamId(packet));
		} else {
			AprsMessage msg = new AprsMessage(packet);
			existingStream.put(msg.getMessageId(), msg);
			
			history.put(this.getStreamId(packet), existingStream);
			
			Station fromStation = Scraper.fetchStation(msg.getFromCall());
			
			
			//this.printStream(this.getStreamId(packet));
		}
		*/
		
		switch (packet.getType() ) {
		case T_POSITION:
			AprsPosition pos = new AprsPosition(packet);
			Station station = Scraper.fetchStation(Station.normaliseCallsign(pos.getCallsign()));
			Location location = new Location();
			location.latitude = pos.getLatitude();
			location.longitude = pos.getLongitude();
			station.addLocation(location);
			Scraper.saveStation(station);
			break;
		case T_STATUS:
			AprsStatus status = new AprsStatus(packet);
			Scraper.getLogger().info(status.toJson());
			break;
		default:
			break;
		}
		
	}

	private String getStreamId(APRSPacket packet) {
		MessagePacket msgPacket = (MessagePacket) packet.getAprsInformation();

		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			String call1 = packet.getSourceCall();
			String call2 = msgPacket.getTargetCallsign();

			String streamName;
			if (call1.compareTo(call2) > 0) {
				streamName = call1 + call2;
			} else {
				streamName = call2 + call1;
			}
			md5.update(streamName.getBytes());

			byte[] hash = md5.digest();

			StringBuffer hexString = new StringBuffer();

			for (int i = 0; i < hash.length; i++) {
				if ((0xff & hash[i]) < 0x10) {
					hexString.append("0" + Integer.toHexString((0xFF & hash[i])));
				} else {
					hexString.append(Integer.toHexString(0xFF & hash[i]));
				}
			}

			return hexString.toString();

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}

	private void printStream(String streamId) {
		System.out.println("--- Message Stream " + streamId + " ------------------------------------------------------------------------");

		for (String messageId : this.history.get(streamId).keySet()) {
			System.out.println(String.format("%-10s %-10s %-10s %-7s %s %s",
					this.history.get(streamId).get(messageId).getDateTime().toString(),
					this.history.get(streamId).get(messageId).getFromCall(),
					this.history.get(streamId).get(messageId).getToCall(),
					this.history.get(streamId).get(messageId).getMessageNumber(),
					this.history.get(streamId).get(messageId).getAck(),
					this.history.get(streamId).get(messageId).getMessage()));
		}

		System.out.println("----------------------------------------------------------------------------------------------------------------------------");
		System.out.println(String.format("Tracking %d conversations", this.history.keySet().size()));
		System.out.println("----------------------------------------------------------------------------------------------------------------------------\n");
	}
}
