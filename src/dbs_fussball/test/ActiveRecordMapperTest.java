package dbs_fussball.test;

import static org.junit.Assert.fail;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

import junit.framework.Assert;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import active_record.ActiveRecordMapper;
import dbs_fussball.model.Person;

public class ActiveRecordMapperTest {

	private ActiveRecordMapper arm;
	

	@Before
	public void setUp() throws Exception {
		ActiveRecordMapper arm = new ActiveRecordMapper("dbs_fussball", "postgres", "vuvuzela", "test");
		arm.createTable(Person.class);
		this.arm = new ActiveRecordMapper("dbs_fussball", "postgres", "vuvuzela", "test");
	}

	@After
	public void tearDown() throws Exception {
		arm.dropTable(Person.class);
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
		Person maybePodolksi = arm.find(Person.class).where("firstName").is("Lukas").where("lastName").is("Podolski").please();
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
		arm.save(gomez);
		arm.save(ronaldoC);
		arm.save(ronaldoCe);
		arm.save(ronaldoL);
		arm.save(ronaldoG);
		arm.save(ronaldoM);
		Set<Person> trueRonaldos = new HashSet<Person>();
		trueRonaldos.add(ronaldoCe);
		trueRonaldos.add(ronaldoL);
		trueRonaldos.add(ronaldoG);
		trueRonaldos.add(ronaldoM);
		Set<Person> maybeRonaldos = new HashSet<Person>(arm.findAll(Person.class).where("firstName").is("Ronaldo").please());
		Assert.assertEquals(trueRonaldos, maybeRonaldos);
	}
	
	@Test
	public void testOrderBy() throws Exception {
		Person az = new Person("a", "z");
		Person by = new Person("b" ,"y");
		Person cx = new Person("c" ,"x");
		Person dw = new Person("d" ,"w");
		Person ev = new Person("e" ,"v");
		Person cacau = new Person("Cacau");
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
		alphabeticallyByFirst.add(cacau);
		List<Person> maybeAlphabeticallyByFirst = new ArrayList<Person>();
		maybeAlphabeticallyByFirst = arm.findAll(Person.class).orderBy("firstName", true).please();
		Assert.assertEquals(alphabeticallyByFirst, maybeAlphabeticallyByFirst);
	}
}
