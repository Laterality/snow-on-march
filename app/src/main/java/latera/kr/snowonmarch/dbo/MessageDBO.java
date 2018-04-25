package latera.kr.snowonmarch.dbo;

import io.realm.RealmObject;
import io.realm.annotations.Index;

public class MessageDBO extends RealmObject {

	@Index
	private String mPhoneNumber;
	private String mBody;
	private long mDateSent;

	public MessageDBO() { }

	public MessageDBO(String phoneNumber, String body, long date) {
		this.mPhoneNumber = phoneNumber;
		this.mBody = body;
		this.mDateSent = date;
	}

	public String getPhoneNumber() { return this.mPhoneNumber; }
	public String getBody() { return this.mBody; }
	public long getDate() { return this.mDateSent; }
}
