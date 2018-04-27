package latera.kr.snowonmarch.dbo;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class GroupDBO extends RealmObject {
	@PrimaryKey
	private int id;
	private RealmList<PersonDBO> people;

	public GroupDBO() { }

	public GroupDBO(int id, List<PersonDBO> people) {
		this.id = id;
		this.people = new RealmList<>();
		this.people.addAll(people);
	}

	public int getId() { return this.id; }
	public RealmList<PersonDBO> getPeople() { return this.people; }

}
