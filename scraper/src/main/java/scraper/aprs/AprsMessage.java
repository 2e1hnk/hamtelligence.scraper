package scraper.aprs;

import java.security.MessageDigest;
import java.util.Date;

import net.ab0oo.aprs.parser.APRSPacket;
import net.ab0oo.aprs.parser.MessagePacket;

public class AprsMessage {
	private String messageId;
	private String fromCall = null;
	private String toCall = null;
	private Date dateTime = null;
	private String message;
	private boolean ack;
	private String messageNumber;

	public AprsMessage() {

	}
	
	public AprsMessage(APRSPacket packet) {
		this(packet.getSourceCall(), ((MessagePacket) packet.getAprsInformation()).getTargetCallsign(), new Date(), ((MessagePacket) packet.getAprsInformation()).getMessageBody(), ((MessagePacket) packet.getAprsInformation()).getMessageNumber());
		
	}

	public AprsMessage(String fromCall, String toCall, Date dateTime, String message, String messageNumber) {
		try {
			this.setDateTime(dateTime);
			this.setFromCall(fromCall);
			this.setToCall(toCall);
			this.setMessage(message);
			this.setMessageId(this.generateMessageId());
			this.setMessageNumber(messageNumber);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public String generateMessageId() throws Exception {
		if (this.getFromCall() == null || this.getToCall() == null || this.getDateTime() == null) {
			throw new Exception();
		} else {
			String messageName = this.getFromCall() + this.getToCall() + this.getMessageNumber();
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(messageName.getBytes());

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
		}
	}

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public String getFromCall() {
		return fromCall;
	}

	public void setFromCall(String fromCall) {
		this.fromCall = fromCall;
	}

	public String getToCall() {
		return toCall;
	}

	public void setToCall(String toCall) {
		this.toCall = toCall;
	}

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isAck() {
		return ack;
	}

	public String getAck() {
		if ( this.isAck() ) {
			return "#";
		} else {
			return "-";
		}
	}

	public void setAck(boolean ack) {
		this.ack = ack;
	}

	public String getMessageNumber() {
		return messageNumber;
	}

	public void setMessageNumber(String messageNumber) {
		this.messageNumber = messageNumber;
	}

}
