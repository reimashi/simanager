package weathercool.proyectosi;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static weathercool.proyectosi.TransactionUtils.doTransaction;

public class TimeTest extends SQLBasedTest {
	private static EntityManagerFactory emf;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		emf = Persistence.createEntityManagerFactory("si-database");
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		if (emf != null && emf.isOpen())
			emf.close();
	}

	@After
	public void renewConnectionAfterTest() throws ClassNotFoundException, SQLException {
        Statement statement = jdbcConnection.createStatement();
        statement.execute("DELETE FROM time");

		super.renewConnection();
	}
	
	

	@Test
	public void testCreateTime() throws SQLException {
		final Time tim = new Time();

		doTransaction(emf, ti -> {
			tim.setYear(2016);
			tim.setMonth(11);
			tim.setDay(24);
			tim.setHour(13);
			tim.setMinute(06);
			tim.setSecond(14);
			ti.persist(tim);
		});
		// check
		Statement statement = jdbcConnection.createStatement();
		ResultSet rs = statement.executeQuery("SELECT COUNT(*) AS Total FROM time WHERE id = " + tim.getId());
		rs.next();

		assertEquals(1, rs.getInt("total"));
	}

	@Test
	public void testFindById() throws SQLException {
		// prepare database for test
		Statement statement = jdbcConnection.createStatement();
		statement.executeUpdate("INSERT INTO time(year, month, day, hour, minute, second) values(2016, 11, 20, 17, 55, 38)",
				Statement.RETURN_GENERATED_KEYS);

		int id = getLastInsertedId(statement);

		// test code
		Time ti = emf.createEntityManager().find(Time.class, id);

		// assert code
		System.out.println("Time of id " + id + " is: " + ti);
		assertEquals(2016, ti.getYear());
		assertEquals(11, ti.getMonth());
		assertEquals(20, ti.getDay());
		assertEquals(17, ti.getHour());
		assertEquals(55, ti.getMinute());
		assertEquals(38, ti.getSecond());
		assertEquals(id, ti.getId());
	}

	@Test
	public void testUpdateTime() throws SQLException {
		// prepare database for test
		Statement statement = jdbcConnection.createStatement();
		statement.executeUpdate("INSERT INTO time(year, month, day, hour, minute, second) values(2016, 11, 20, 17, 55, 38)",
				Statement.RETURN_GENERATED_KEYS);

		int id = getLastInsertedId(statement);

		doTransaction(emf, ti -> {
			Time tim = ti.find(Time.class, id);		
			tim.setYear(2016);
			tim.setMonth(11);
			tim.setDay(20);
			tim.setHour(17);
			tim.setMinute(55);
			tim.setSecond(38);
		});

		// check
		statement = jdbcConnection.createStatement();
		ResultSet rs = statement.executeQuery("SELECT * FROM time WHERE id = " + id);
		rs.next();

		assertEquals(2016, rs.getInt("year"));
		assertEquals(11, rs.getInt("month"));
		assertEquals(20, rs.getInt("day"));
		assertEquals(17, rs.getInt("hour"));
		assertEquals(55, rs.getInt("minute"));
		assertEquals(38, rs.getInt("second"));
		assertEquals(id, rs.getInt("id"));
	}

	private Time aDetachedTime = null;

	@Test
	public void testUpdateTimeByMerge() throws SQLException {
		// prepare database for test
		Statement statement = jdbcConnection.createStatement();
		statement.executeUpdate("INSERT INTO time(year, month, day, hour, minute, second) values(2016, 11, 20, 17, 55, 38)",
				Statement.RETURN_GENERATED_KEYS);
		int id = getLastInsertedId(statement);

		doTransaction(emf, ti -> {
			aDetachedTime = ti.find(Time.class, id);
		});
		// e is detached, because the entitymanager lo is closed (see
		// doTransaction)
		aDetachedTime.setYear(2016);
		aDetachedTime.setMonth(11);
		aDetachedTime.setDay(20);
		aDetachedTime.setHour(17);
		aDetachedTime.setMinute(55);
		aDetachedTime.setSecond(38);

		doTransaction(emf, em -> {
			em.merge(aDetachedTime);
		});

		// check
		statement = jdbcConnection.createStatement();
		ResultSet rs = statement.executeQuery("SELECT * FROM time WHERE id = " + id);
		rs.next();

		assertEquals(2016, rs.getInt("year"));
		assertEquals(11, rs.getInt("month"));
		assertEquals(20, rs.getInt("day"));
		assertEquals(17, rs.getInt("hour"));
		assertEquals(55, rs.getInt("minute"));
		assertEquals(38, rs.getInt("second"));
		assertEquals(id, rs.getInt("id"));
	}

	@Test
	public void testDeleteTime() throws SQLException {
		// prepare database for test
		Statement statement = jdbcConnection.createStatement();
		statement.executeUpdate("INSERT INTO time(year, month, day, hour, minute, second) values(2016, 11, 20, 17, 55, 38)",
				Statement.RETURN_GENERATED_KEYS);
		int id = getLastInsertedId(statement);

		doTransaction(emf, ti -> {
			Time t = ti.find(Time.class, id);
			ti.remove(t);
		});

		// check
		statement = jdbcConnection.createStatement();
		ResultSet rs = statement.executeQuery("SELECT COUNT(*) as total FROM time WHERE id = " + id);
		rs.next();

		assertEquals(0, rs.getInt("total"));
	}

	@Test
	public void testListTime() throws SQLException {
		// prepare database for test
		Statement statement = jdbcConnection.createStatement();
		statement.executeUpdate("INSERT INTO time(year, month, day, hour, minute, second) values(2016, 11, 20, 17, 55, 38)",
				Statement.RETURN_GENERATED_KEYS);
		// prepare database for test
		statement.executeUpdate("INSERT INTO time(year, month, day, hour, minute, second) values(2016, 12, 14, 21, 27, 10)",
				Statement.RETURN_GENERATED_KEYS);

		List<Time> Times = emf.createEntityManager()
				.createQuery("SELECT t FROM Time t ORDER BY t.id", Time.class).getResultList();
		System.out.println(Times);
		// check
		assertEquals(2, Times.size());
		assertEquals(2016, Times.get(0).getYear());
		assertEquals(11, Times.get(0).getMonth());
		assertEquals(20, Times.get(0).getDay());
		assertEquals(17, Times.get(0).getHour());
		assertEquals(55, Times.get(0).getMinute());
		assertEquals(38, Times.get(0).getSecond());
		
		assertEquals(2016, Times.get(1).getYear());
		assertEquals(12, Times.get(1).getMonth());
		assertEquals(14, Times.get(1).getDay());
		assertEquals(21, Times.get(1).getHour());
		assertEquals(27, Times.get(1).getMinute());
		assertEquals(10, Times.get(1).getSecond());
	}
}