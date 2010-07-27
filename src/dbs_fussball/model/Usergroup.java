package dbs_fussball.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import active_record.ActiveRecord;
import active_record.ValidationFailure;

public class Usergroup extends ActiveRecord{

	private final String name;
	private final Set<User> users;
	private final Set<Match> permissions;

	public Usergroup(String name) {
		this.name = name;
		users = new HashSet<User>();
		permissions = new HashSet<Match>();
	}

	public boolean addPermission(Match match) {
		return permissions.add(match);
	}

	public String getName() {
		return name;
	}

	public Set<User> getUsers() {
		return users;
	}

	public Set<Match> getPermissions() {
		return permissions;
	}

	public boolean addUser(User u) {
		return users.add(u);
	}

	@Override
	public List<ValidationFailure> validate() {
		List<ValidationFailure> failures = new ArrayList<ValidationFailure>();
		if (name == null)
			failures.add(new ValidationFailure("Usergroup must have a name"));
		return failures;
	}
}
