package latera.kr.snowonmarch.dbo;

import io.realm.RealmObject;
import io.realm.annotations.Index;

public class MessageDBO extends RealmObject {

	@Index
	private String address;
	private String body;
	private long dateSent;
	private PersonDBO sender;
	private boolean sent;

	public MessageDBO() { }

	public MessageDBO(String phoneNumber, String body, long date) {
		this.address = phoneNumber;
		this.body = body;
		this.dateSent = date;
		this.sender = null;
	}

	public MessageDBO(String phoneNumber, String body, long date, boolean sent) {
		this.address = phoneNumber;
		this.body = body;
		this.dateSent = date;
		this.sender = null;
		this.sent = sent;
	}

	public String getAddress() { return this.address; }
	public String getBody() { return this.body; }
	public long getDate() { return this.dateSent; }
	public PersonDBO getSender() { return sender; }
	public boolean isIsent() { return this.sent; }

	public void setSender(PersonDBO sender) { this.sender = sender; }
}
