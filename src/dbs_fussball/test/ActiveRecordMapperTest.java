package dbs_fussball.test;

import static org.junit.Assert.fail;

import java.sql.SQLException;

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
	private Person podolski;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		ActiveRecordMapper arm = new ActiveRecordMapper("dbs_fussball", "postgres", "vuvuzela", "test");
		arm.createTable(Person.class);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		ActiveRecordMapper arm = new ActiveRecordMapper("dbs_fussball", "postgres", "vuvuzela", "test");
		arm.dropTable(Person.class);
	}

	@Before
	public void setUp() throws Exception {
		this.arm = new ActiveRecordMapper("dbs_fussball", "postgres", "vuvuzela", "test");
		podolski = new Person("Lukas", "Podolski");
		arm.save(podolski);
	}

	@After
	public void tearDown() throws Exception {
		arm.delete(podolski);
	}

	@Test
	public void testFindBy() throws Exception {
		Long podolksiId = new Long(podolski.getId());
			
		Person maybePodolksi = arm.findBy(Person.class, podolksiId);
			
		Assert.assertEquals(podolski, maybePodolksi);
	}

	@Test
	public void testFind() throws Exception {
		Person maybePodolksi = arm.find(Person.class).where("firstName").is("Lukas").where("lastName").is("Podolski").please();
			
		Assert.assertEquals(podolski, maybePodolksi);
	}

}
