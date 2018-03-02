package scraper.aprs;

import net.ab0oo.aprs.parser.APRSPacket;
import net.ab0oo.aprs.parser.APRSTypes;
import net.ab0oo.aprs.parser.MessagePacket;

public class AprsAction {
	public String fromCall;
	public String toCall;
	public String message;
	
	public AprsAction(APRSPacket packet) {
		fromCall = packet.getSourceCall();
		MessagePacket payload = (MessagePacket) packet.getAprsInformation();
		toCall = payload.getTargetCallsign();
		message = payload.getMessageBody();
		
		// We're going to ignore any acks
		if ( !payload.isAck() ) {
			System.out.println(this.toString());
		}
	}
	
	public String toString() {
		return String.format("%-10s %-10s %s", fromCall, toCall, message);
	}
}
