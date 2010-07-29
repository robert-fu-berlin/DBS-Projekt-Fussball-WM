package dbs_fussball;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import active_record.ActiveRecord;
import active_record.ActiveRecordMapper;

import com.google.common.base.Joiner;

import dbs_fussball.model.Cup;
import dbs_fussball.model.Event;
import dbs_fussball.model.FifaCountry;
import dbs_fussball.model.Match;
import dbs_fussball.model.Person;
import dbs_fussball.model.Stadium;
import dbs_fussball.model.Team;
import dbs_fussball.model.User;
import dbs_fussball.model.Usergroup;
import dbs_fussball.model.Cup.CupBuilder;

public class UseCases {

	private ActiveRecordMapper arm;

	private List<Class<? extends ActiveRecord>> classes;

	private static final String dbsName = "dbs_fussball";
	private static final String user = "postgres";
	private static final String password = "vuvuzela";
	private static final String prefix = "dbs";

	@BeforeClass
	public static void beforeClassSetUp() throws Exception {
		ActiveRecordMapper beforeClassArm = new ActiveRecordMapper(dbsName, user, password, prefix);

		List<Class<? extends ActiveRecord>>classes = new ArrayList<Class<? extends ActiveRecord>>();

		classes.add(Stadium.class);
		classes.add(Person.class);
		classes.add(Event.class);
		classes.add(Team.class);
		classes.add(Cup.class);
		classes.add(User.class);
		classes.add(Usergroup.class);
		classes.add(Match.class);

		//try to drop tables
		try {
			for (Class<? extends ActiveRecord> activeRecord : classes)
				beforeClassArm.dropTable(activeRecord);
		} catch (Exception e) {}
	}

	@Before
	public void setUp() throws Exception {
		classes = new ArrayList<Class<? extends ActiveRecord>>();

		classes.add(Stadium.class);
		classes.add(Person.class);
		classes.add(Event.class);
		classes.add(Team.class);
		classes.add(Cup.class);
		classes.add(User.class);
		classes.add(Usergroup.class);
		classes.add(Match.class);

		/**
		 * 
		 * Hier die Datenbank - Credentials eintragen
		 * Bsp.: arm = new ActiveRecordMapper("Datenbankname", "User", "Passwort", "Prefix fuer die Datenbank")
		 * 
		 */
		arm = new ActiveRecordMapper(dbsName, user, password, prefix);

		//try to drop tables
		try {
			for (Class<? extends ActiveRecord> activeRecord : classes)
				arm.dropTable(activeRecord);
		} catch (Exception e) {}

		//create tables
		for (Class<? extends ActiveRecord> activeRecord : classes)
			arm.createTable(activeRecord);
	}

	@After
	public void tearDown() throws Exception {
		for (Class<? extends ActiveRecord> activeRecord : classes)
			arm.dropTable(activeRecord);
	}

	@Test
	public void einsatzEintragen() throws Exception {
		Cup cup = createCup();
		arm.save(cup);
		Match match = (Match) cup.getMatches('A').toArray()[0];
		Team team = match.getTeamA();
		Iterator<Person> it = team.players().iterator();
		Person player = it.next();
		match.addPlayerToLineUpTeamA(player);
		arm.save(match);
		Match matchFromDB = arm.findBy(Match.class, match.getId());
		Assert.assertTrue(matchFromDB.lineUpTeamAContains(player));
	}

	@Test
	public void torEintragen() throws Exception {
		Cup cup = createCup();
		arm.save(cup);
		Match match = (Match) cup.getMatches('A').toArray()[0];
		Team team = match.getTeamA();
		Iterator<Person> it = team.players().iterator();
		Person player = it.next();
		match.addPlayerToLineUpTeamA(player);
		match.addEvent(new Event(Event.Type.GOAL, player, null, 89.9F));
		arm.save(match);
		Match matchFromDB = arm.findBy(Match.class, match.getId());
		Assert.assertEquals(1, matchFromDB.getGoalsA());
	}

	@Test
	public void ergebnisAusgeben() throws Exception {
		Cup cup = createCup();
		arm.save(cup);
		Match match = (Match) cup.getMatches('A').toArray()[0];
		Team teamA = match.getTeamA();
		Team teamB = match.getTeamB();
		Iterator<Person> it = teamA.players().iterator();
		Person player = it.next();
		match.addPlayerToLineUpTeamA(player);
		match.addEvent(new Event(Event.Type.GOAL, player, null, 89.9F));
		arm.save(match);
		Match matchFromDB = arm.findBy(Match.class, match.getId());
		String matchResult = teamA.getCountry() + " - " + teamB.getCountry() + " " + matchFromDB.getGoalsA() + " : " + matchFromDB.getGoalsB();
		System.out.println(matchResult);
	}

	// 	Dieser Anwendungsfall wird in der View realisiert.
	//
	//	@Test
	//	public void gruppentabelleBerechnen() throws Exception {}

	// 	Auchd ieser Anwendungsfall wird in der View realisiert.
	//
	//	@Test
	//	public void siegerAusgeben() throws Exception {}

	@Test
	public void turnierAnlegen() throws Exception {
		Connection connection = null;
		String procedure = "CREATE OR REPLACE FUNCTION CreateCup (text) RETURNS bigint AS $$\n" +
		"DECLARE\n"+
		"countries text[];\n"+
		"counter1 int8;\n"+
		"counter2 int8;\n"+
		"currentCountry text;\n"+
		"cupID bigint;\n"+
		"teamIDs bigint[32];\n"+
		"groupMatchID bigint;\n"+
		"currentTime bigint;\n"+
		"temp float;\n"+
		"tempID bigint;\n"+
		"BEGIN\n"+
		"select extract (epoch from now()) into temp;\n"+
		"temp = temp * 1000;\n"+
		"select CAST(temp as bigint) into currentTime;\n"+
		"SELECT INTO countries regexp_split_to_array($1, E',');\n"+
		"counter1 := array_lower(countries, 1);\n"+
		"--add teams and save their ids\n"+
		"WHILE (counter1 <= array_upper(countries, 1)) LOOP\n"+
		"        currentCountry := countries[counter1];\n"+
		"        INSERT INTO " + prefix + "_team (country,created_at,updated_at) VALUES (currentCountry,currentTime,currentTime) RETURNING id INTO tempID;\n"+
		"        teamIDs[counter1] = tempID;\n"+
		"        counter1 := counter1 + 1;\n"+
		"END LOOP;\n"+
		"--create cup\n"+
		"INSERT INTO " + prefix + "_cup (updated_at, created_at) VALUES (currentTime,currentTime) RETURNING id INTO cupID;\n"+
		"--add matches for group a\n"+
		"counter1 := array_lower(teamIDs, 1);\n"+
		"WHILE (counter1 < array_lower(teamIDs, 1) + 4) LOOP\n"+
		"        counter2 := counter1 + 1;\n"+
		"        WHILE (counter2 < array_lower(countries, 1) + 4) LOOP\n"+
		"                INSERT INTO " + prefix + "_match (team_a,team_b,created_at,updated_at) VALUES (teamIDs[counter1],teamIDs[counter2],currentTime,currentTime) RETURNING id INTO groupMatchID;\n"+
		"                INSERT INTO " + prefix + "_cup_matches_group_a (cheesecake,cherrybomb) VALUES (cupID, groupMatchID);\n"+
		"                counter2 = counter2 + 1;\n"+
		"        END LOOP;\n"+
		"        counter1 = counter1 + 1;\n"+
		"END LOOP;\n"+
		"--add matches for group b\n"+
		"counter1 := array_lower(teamIDs, 1) + 4;\n"+
		"WHILE (counter1 < array_lower(teamIDs, 1) + 8) LOOP\n"+
		"        counter2 := counter1 + 1;\n"+
		"        WHILE (counter2 < array_lower(countries, 1) + 8) LOOP\n"+
		"                INSERT INTO " + prefix + "_match (team_a,team_b,created_at,updated_at) VALUES (teamIDs[counter1],teamIDs[counter2],currentTime,currentTime) RETURNING id INTO groupMatchID;\n"+
		"                INSERT INTO " + prefix + "_cup_matches_group_b (cheesecake,cherrybomb) VALUES (cupID, groupMatchID);\n"+
		"                counter2 = counter2 + 1;\n"+
		"        END LOOP;\n"+
		"        counter1 = counter1 + 1;\n"+
		"END LOOP;\n"+
		"--add matches for group c\n"+
		"counter1 := array_lower(teamIDs, 1) + 8;\n"+
		"WHILE (counter1 < array_lower(teamIDs, 1) + 12) LOOP\n"+
		"        counter2 := counter1 + 1;\n"+
		"        WHILE (counter2 < array_lower(countries, 1) + 12) LOOP\n"+
		"                INSERT INTO " + prefix + "_match (team_a,team_b,created_at,updated_at) VALUES (teamIDs[counter1],teamIDs[counter2],currentTime,currentTime) RETURNING id INTO groupMatchID;\n"+
		"                INSERT INTO " + prefix + "_cup_matches_group_c (cheesecake,cherrybomb) VALUES (cupID, groupMatchID);\n"+
		"                counter2 = counter2 + 1;\n"+
		"        END LOOP;\n"+
		"        counter1 = counter1 + 1;\n"+
		"END LOOP;\n"+
		"--add matches for group d\n"+
		"counter1 := array_lower(teamIDs, 1) + 12;\n"+
		"WHILE (counter1 < array_lower(teamIDs, 1) + 16) LOOP\n"+
		"        counter2 := counter1 + 1;\n"+
		"        WHILE (counter2 < array_lower(countries, 1) + 16) LOOP\n"+
		"                INSERT INTO " + prefix + "_match (team_a,team_b,created_at,updated_at) VALUES (teamIDs[counter1], teamIDs[counter2],currentTime,currentTime) RETURNING id INTO groupMatchID;\n"+
		"                INSERT INTO " + prefix + "_cup_matches_group_d (cheesecake,cherrybomb) VALUES (cupID, groupMatchID);\n"+
		"                counter2 = counter2 + 1;\n"+
		"        END LOOP;\n"+
		"        counter1 = counter1 + 1;\n"+
		"END LOOP;\n"+
		"--add matches for group e\n"+
		"counter1 := array_lower(teamIDs, 1) + 16;\n"+
		"WHILE (counter1 < array_lower(teamIDs, 1) + 20) LOOP\n"+
		"        counter2 := counter1 + 1;\n"+
		"                WHILE (counter2 < array_lower(countries, 1) + 20) LOOP\n"+
		"                INSERT INTO " + prefix + "_match (team_a,team_b,created_at,updated_at) VALUES (teamIDs[counter1],teamIDs[counter2],currentTime,currentTime) RETURNING id INTO groupMatchID;\n"+
		"                INSERT INTO " + prefix + "_cup_matches_group_e (cheesecake,cherrybomb) VALUES (cupID, groupMatchID);\n"+
		"                counter2 = counter2 + 1;\n"+
		"        END LOOP;\n"+
		"        counter1 = counter1 + 1;\n"+
		"END LOOP;\n"+
		"--add matches for group f\n"+
		"counter1 := array_lower(teamIDs, 1) + 20;\n"+
		"WHILE (counter1 < array_lower(teamIDs, 1) + 24) LOOP\n"+
		"        counter2 := counter1 + 1;\n"+
		"        WHILE (counter2 < array_lower(countries, 1) + 24) LOOP\n"+
		"                INSERT INTO " + prefix + "_match (team_a,team_b,created_at,updated_at) VALUES (teamIDs[counter1],teamIDs[counter2],currentTime,currentTime) RETURNING id INTO groupMatchID;\n"+
		"                INSERT INTO " + prefix + "_cup_matches_group_f (cheesecake,cherrybomb) VALUES (cupID, groupMatchID);\n"+
		"                counter2 = counter2 + 1;\n"+
		"        END LOOP;\n"+
		"        counter1 = counter1 + 1;\n"+
		"END LOOP;\n"+
		"--add matches for group g\n"+
		"counter1 := array_lower(teamIDs, 1) + 24;\n"+
		"WHILE (counter1 < array_lower(teamIDs, 1) + 28) LOOP\n"+
		"        counter2 := counter1 + 1;\n"+
		"        WHILE (counter2 < array_lower(countries, 1) + 28) LOOP\n"+
		"                INSERT INTO " + prefix + "_match (team_a,team_b,created_at,updated_at) VALUES (teamIDs[counter1],teamIDs[counter2],currentTime,currentTime) RETURNING id INTO groupMatchID;\n"+
		"                INSERT INTO " + prefix + "_cup_matches_group_g (cheesecake,cherrybomb) VALUES (cupID, groupMatchID);\n"+
		"                counter2 = counter2 + 1;\n"+
		"        END LOOP;\n"+
		"        counter1 = counter1 + 1;\n"+
		"END LOOP;\n"+
		"--add matches for group h\n"+
		"counter1 := array_lower(teamIDs, 1) + 28;\n"+
		"WHILE (counter1 < array_lower(teamIDs, 1) + 32) LOOP\n"+
		"        counter2 := counter1 + 1;\n"+
		"        WHILE (counter2 < array_lower(countries, 1) + 32) LOOP\n"+
		"                INSERT INTO " + prefix + "_match (team_a,team_b,created_at,updated_at) VALUES (teamIDs[counter1],teamIDs[counter2],currentTime,currentTime) RETURNING id INTO groupMatchID;\n"+
		"                INSERT INTO " + prefix + "_cup_matches_group_h (cheesecake,cherrybomb) VALUES (cupID, groupMatchID);\n"+
		"                counter2 = counter2 + 1;\n"+
		"        END LOOP;\n"+
		"        counter1 = counter1 + 1;\n"+
		"        END LOOP;\n"+
		"--add cup_teams\n"+
		"counter1 := array_lower(teamIDs, 1);\n"+
		"WHILE (counter1 <= array_upper(teamIDs, 1)) LOOP\n"+
		"        INSERT INTO " + prefix + "_cup_teams (cheesecake, cherrybomb) VALUES (cupID, teamIDs[counter1]);\n"+
		"        counter1 = counter1 +1;\n"+
		"END LOOP;\n"+
		"RETURN cupID;\n"+
		"END; $$ LANGUAGE 'plpgsql';";
		connection = DriverManager.getConnection("jdbc:postgresql:" + dbsName, user, password);
		Statement createProcedure = connection.createStatement();
		createProcedure.execute(procedure);
		CallableStatement createDB = connection.prepareCall("{ call createCup(?) }");

		//build list of countries taking part in this worldcup
		List<String> countries = new ArrayList<String>();
		for (FifaCountry c : FifaCountry.values()) {
			countries.add(c.name());
			if (countries.size() >= 32)
				break;
		}
		createDB.setString(1, Joiner.on(',').join(countries));
		createDB.execute();
		Assert.assertNotNull(arm.find(Cup.class).where("id").is(1).please());
	}

	private Cup createCup() {
		FifaCountry[] countries = FifaCountry.values();
		Cup.CupBuilder cupBuilder = new CupBuilder();
		int i = 0;
		for (char group = 'A'; group <= 'H' ; group++)
		{
			cupBuilder.addCountryToGroup(countries[i++], group);
			cupBuilder.addCountryToGroup(countries[i++], group);
			cupBuilder.addCountryToGroup(countries[i++], group);
			cupBuilder.addCountryToGroup(countries[i++], group);
		}
		return cupBuilder.build();
	}
}
