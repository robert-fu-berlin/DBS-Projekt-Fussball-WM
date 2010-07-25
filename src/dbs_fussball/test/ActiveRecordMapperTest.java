package dbs_fussball.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import active_record.ActiveRecord;
import active_record.ActiveRecordMapper;

import com.google.common.collect.Sets;

import dbs_fussball.model.Cup;
import dbs_fussball.model.Event;
import dbs_fussball.model.Match;
import dbs_fussball.model.Person;
import dbs_fussball.model.Stadium;
import dbs_fussball.model.Team;

public class ActiveRecordMapperTest {

	private ActiveRecordMapper arm;

	private List<Class<? extends ActiveRecord>> classes;

	@Before
	public void setUp() throws Exception {
		classes = new ArrayList<Class<? extends ActiveRecord>>();

		classes.add(Stadium.class);
		classes.add(Person.class);
		classes.add(Team.class);
		classes.add(Event.class);
		classes.add(Match.class);
		classes.add(Cup.class);

		arm = new ActiveRecordMapper("dbs_fussball", "postgres", "vuvuzela", "test");
		for (Class<? extends ActiveRecord> activeRecord : classes)
			arm.createTable(activeRecord);
	}

	@After
	public void tearDown() throws Exception {
		Collections.reverse(classes);
		for (Class<? extends ActiveRecord> activeRecord : classes)
			arm.dropTable(activeRecord);
	}

	@Test
	public void testFindBy() throws Exception {
		Person podolski = new Person("Lukas", "Podolski");
		arm.save(podolski);
		Long podolksiId = new Long(podolski.getId());

		Person maybePodolksi = arm.findBy(Person.class, podolksiId);

		Assert.assertEquals(podolski, maybePodolksi);
	}

	@Test
	public void testFind() throws Exception {
		Person podolski = new Person("Lukas", "Podolski");
		arm.save(podolski);
		Person maybePodolksi = arm.find(Person.class).where("firstName").is("Lukas").and("lastName").is("Podolski").please();
		Assert.assertEquals(podolski, maybePodolksi);
	}

	@Test
	public void testFindAll() throws Exception {
		Person gomez = new Person("Mario", "Gomez");
		gomez.setStageName("Majo");
		Person ronaldoC = new Person("Christiano", "Ronaldo");
		Person ronaldoL = new Person("Ronaldo" ,"Luis Nazario");
		Person ronaldoG = new Person("Ronaldo" ,"Gueario");
		Person ronaldoM = new Person("Ronaldo" ,"Maczinski");
		Person ronaldoCe = new Person("Ronaldo" ,"Cerritos");
		arm.save(gomez, ronaldoC, ronaldoCe, ronaldoL, ronaldoG, ronaldoM);
		Set<Person> expectedSet = new HashSet<Person>();
		expectedSet.add(gomez);
		expectedSet.add(ronaldoCe);
		expectedSet.add(ronaldoL);
		expectedSet.add(ronaldoG);
		expectedSet.add(ronaldoM);
		Set<Person> resultSet = new HashSet<Person>(arm.findAll(Person.class).where("firstName").is("Ronaldo").or("firstName").is("Mario").please());
		Assert.assertEquals(expectedSet, resultSet);
	}

	@Test
	public void testOrderBy() throws Exception {
		Person az = new Person("a", "z");
		Person by = new Person("b" ,"y");
		Person cx = new Person("c" ,"x");
		Person dw = new Person("d" ,"w");
		Person ev = new Person("e" ,"v");
		Person cacau = new Person("Cacau");	//only stage name, neither first- nor last name
		arm.save(cacau);
		arm.save(dw);
		arm.save(by);
		arm.save(cx);
		arm.save(ev);
		arm.save(az);
		List<Person> alphabeticallyByFirst = new ArrayList<Person>();
		alphabeticallyByFirst.add(az);
		alphabeticallyByFirst.add(by);
		alphabeticallyByFirst.add(cx);
		alphabeticallyByFirst.add(dw);
		alphabeticallyByFirst.add(ev);
		alphabeticallyByFirst.add(cacau);	//expecting that no first name is sorted to the end of the list
		List<Person> maybeAlphabeticallyByFirst = new ArrayList<Person>();
		maybeAlphabeticallyByFirst = arm.findAll(Person.class).orderBy("firstName", true).please();
		Assert.assertEquals(alphabeticallyByFirst, maybeAlphabeticallyByFirst);
	}

	@Test
	public void testOrderByMultiple() throws Exception {
		Person az = new Person("a", "z");
		Person ay = new Person("a" ,"y");
		Person cx = new Person("c" ,"x");
		Person aw = new Person("a" ,"w");
		Person cv = new Person("c" ,"v");
		Person cacau = new Person("Cacau");	//only stage name, neither first- nor last name
		arm.save(cacau);
		arm.save(cx);
		arm.save(ay);
		arm.save(az);
		arm.save(aw);
		arm.save(cv);
		List<Person> alphabeticallyByFirstAndLast = new ArrayList<Person>();
		alphabeticallyByFirstAndLast.add(aw);
		alphabeticallyByFirstAndLast.add(ay);
		alphabeticallyByFirstAndLast.add(az);
		alphabeticallyByFirstAndLast.add(cv);
		alphabeticallyByFirstAndLast.add(cx);
		alphabeticallyByFirstAndLast.add(cacau);	//expecting that no first and last name is sorted to the end of the list
		List<Person> maybeAlphabeticallyByFirstAndLast = new ArrayList<Person>();
		maybeAlphabeticallyByFirstAndLast = arm.findAll(Person.class).orderBy("firstName", true).orderBy("lastName", true).please();
		Assert.assertEquals(alphabeticallyByFirstAndLast, maybeAlphabeticallyByFirstAndLast);
	}

	@Test
	public void testEnumSerialization() throws Exception {
		Event beginEvent = Event.begin();
		arm.save(beginEvent);
		Event maybeBeginEvent = arm.findBy(Event.class, beginEvent.getId());
		Assert.assertEquals(beginEvent.getType(), maybeBeginEvent.getType());
	}

	@Test
	public void testLazySet() throws Exception {
		Team team = new Team();
		Set<Person> supposedPlayers = new HashSet<Person>();

		Person poldi = new Person("Lukas", "Podolski");
		Person cacau = new Person("Cacau");
		Person gomez = new Person("Mario", "Gomez");
		Person wiese = new Person("Tim", "Wiese");

		team.addPlayer(poldi);
		team.addPlayer(cacau);
		team.addPlayer(gomez);
		team.addPlayer(wiese);

		arm.save(team);

		supposedPlayers.add(poldi);
		supposedPlayers.add(cacau);
		supposedPlayers.add(gomez);
		supposedPlayers.add(wiese);

		Team anderesTeam = arm.findBy(Team.class, team.getId());

		Set<Person> otherPlayers = Sets.newHashSet(anderesTeam.players());

		Assert.assertEquals(supposedPlayers, otherPlayers);
	}

	@Test
	public void testForeignKeyDeletion() throws Exception {
		Person poldi = new Person("Lukas", "Podolski");
		Team koeln = new Team();

		koeln.addPlayer(poldi);

		arm.save(poldi);
		arm.save(koeln);

		arm.delete(poldi);

		koeln = arm.findBy(Team.class, koeln.getId());

		Assert.assertFalse(koeln.containsPlayer(poldi));
	}

}
