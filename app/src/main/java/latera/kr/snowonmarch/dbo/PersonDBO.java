package latera.kr.snowonmarch.dbo;

import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class PersonDBO extends RealmObject{

	@PrimaryKey
	private int mId;
	@Index
	private String mName;
	@Required @Index
	private String mPhoneNumber;

	public PersonDBO() { }

	public PersonDBO(int id, String name, String phoneNumber) {
		this.mId = id;
		this.mName = name;
		this.mPhoneNumber = phoneNumber;
	}

	public int getId() { return this.mId; }
	public String getName() { return this.mName; }
	public String getPhoneNumber() { return this.mPhoneNumber; }

	public void setName(String name) { this.mName = name; }
	public void setPhoneNumber(String phoneNumber ) { this.mPhoneNumber = phoneNumber; }
}
