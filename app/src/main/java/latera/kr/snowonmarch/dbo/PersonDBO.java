package latera.kr.snowonmarch.dbo;

import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.Sort;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;
import io.realm.internal.OsResults;

public class PersonDBO extends RealmObject{

	@PrimaryKey
	private int id;
	@Index
	private String name;
	@Required @Index
	private String address;
	private RealmList<MessageDBO> messagesSent;
	private long recent;

	public PersonDBO() { }

	public PersonDBO(int id, String name, String phoneNumber) {
		this.id = id;
		this.name = name;
		this.address = phoneNumber;
		this.messagesSent = new RealmList<>();
		this.recent = -1;
	}

	public int getId() { return this.id; }
	public String getName() { return this.name; }
	public String getAddress() { return this.address; }
	public RealmList<MessageDBO> getSentMessages() { return this.messagesSent; }
	public MessageDBO getRecentMessage() { if (this.messagesSent.isEmpty()) {return null;} return this.messagesSent.last(); }

	public void setName(String name) { this.name = name; }
	public void setAddress(String address) { this.address = address; }
	public void addMessage(MessageDBO message) {
		this.messagesSent.add(message);
		if (message.getDate() > recent) {
			recent = message.getDate();
		}
		sort();
	}
	public void addMessages(ListIterator<MessageDBO> messages) {
		MessageDBO msg;
		while(messages.hasNext()) {
			msg = messages.next();
			msg.setSender(this);
			this.messagesSent.add(msg);
			if (msg.getDate() > this.recent) { recent = msg.getDate(); }
		}
		sort();
	}

	private void sort() {
		RealmList<MessageDBO> newList = new RealmList<>();
		newList.addAll(this.messagesSent.sort("dateSent", Sort.ASCENDING));
		this.messagesSent = newList;
	}
}
