package dbs_fussball.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import active_record.ActiveRecord;
import active_record.Inverse;
import active_record.ValidationFailure;

public class User extends ActiveRecord {

	private final String email;
	private final String password;
	private final Boolean isAdmin;

	private final Set<Match> permissions;

	@Inverse("dbs_fussball.model.Usergroup.users")
	private final Set<Usergroup> usergroups;

	public User(String email, String password) {
		this.email = email;
		this.password = password;
		this.isAdmin = false;
		permissions = new HashSet<Match>();
		usergroups = new HashSet<Usergroup>();
	}

	public User(String email, String password, boolean isAdmin) {
		this.email = email;
		this.password = password;
		this.isAdmin = isAdmin;
		permissions = new HashSet<Match>();
		usergroups = new HashSet<Usergroup>();
	}

	public boolean addPermission(Match match) {
		return permissions.add(match);
	}

	public boolean removePermission(Match match) {
		return permissions.remove(match);
	}

	public boolean addToUserGroup(Usergroup group) {
		return usergroups.add(group);
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

	public Boolean getIsAdmin() {
		return isAdmin;
	}

	public Set<Match> getPermissions() {
		return permissions;
	}

	public Set<Usergroup> getUserGroups() {
		return usergroups;
	}

	public boolean removeFromUserGroup(Usergroup group) {
		return usergroups.remove(group);
	}

	@Override
	public List<ValidationFailure> validate() {
		List<ValidationFailure> failures = new ArrayList<ValidationFailure>();
		if (email == null)
			failures.add(new ValidationFailure("User must have an email"));
		if (!email.matches("[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?"))
			failures.add(new ValidationFailure("Email must have valid format"));
		if (password == null)
			failures.add(new ValidationFailure("User must have a password"));
		return failures;
	}
}
