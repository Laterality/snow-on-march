package latera.kr.snowonmarch.dbo;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class GroupDBO extends RealmObject {
	@PrimaryKey
	private int id;
	private String name;
	private RealmList<PersonDBO> people;
	private boolean immortal;

	public GroupDBO() { }

	public GroupDBO(int id, String name) {
		this.id = id;
		this.name = name;
		this.people = new RealmList<>();
		this.people.addAll(people);
		immortal = id == 1;
	}

	public int getId() { return this.id; }
	public String getName() { return this.name; }
	public RealmList<PersonDBO> getPeople() { return this.people; }
	public boolean isImmortal() { return this.immortal; }
	public void addPerson(PersonDBO p) { this.people.add(p); }
}
