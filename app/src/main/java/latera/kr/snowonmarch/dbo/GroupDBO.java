package latera.kr.snowonmarch.dbo;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

public class GroupDBO extends RealmObject {
	@PrimaryKey
	private int mId;
	private RealmList<PersonDBO> mPeople;

	public GroupDBO() { }

	public GroupDBO(int id, List<PersonDBO> people) {
		this.mId = id;
		mPeople = new RealmList<>();
		mPeople.addAll(people);
	}

	public int getId() { return this.mId; }
	public RealmList<PersonDBO> getPeople() { return this.mPeople; }

}
