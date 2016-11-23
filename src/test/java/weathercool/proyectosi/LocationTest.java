package weathercool.proyectosi;

import static org.junit.Assert.assertEquals;
import static weathercool.proyectosi.TransactionUtils.doTransaction;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import weathercool.proyectosi.location;

public class LocationTest extends SQLBasedTest {
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
		super.renewConnection();
	}

	@Test
	public void testCreateLocation() throws SQLException {
		final location loc = new location();

		doTransaction(emf, lo -> {
			loc.setLatitude(9.45);
			loc.setLongitude(42.14);
			lo.persist(loc);
		});
		// check
		Statement statement = jdbcConnection.createStatement();
		ResultSet rs = statement.executeQuery("SELECT COUNT(*) AS Total FROM location WHERE id = " + loc.getId());
		rs.next();

		assertEquals(1, rs.getInt("total"));
	}

	@Test
	public void testFindById() throws SQLException {
		// prepare database for test
		Statement statement = jdbcConnection.createStatement();
		statement.executeUpdate("INSERT INTO location(latitude, longitude) values(94.132, 45.189)",
				Statement.RETURN_GENERATED_KEYS);

		int id = getLastInsertedId(statement);

		// test code
		location lo = emf.createEntityManager().find(location.class, id);

		// assert code
		System.out.println("location of id " + id + " is: " + lo);
		assertEquals(94.132, lo.getLatitude(), 1e-10);
		assertEquals(45.189, lo.getLongitude(), 1e-10);
		assertEquals(id, lo.getId());
	}

	@Test
	public void testUpdateLocation() throws SQLException {
		// prepare database for test
		Statement statement = jdbcConnection.createStatement();
		statement.executeUpdate("INSERT INTO location(latitude, longitude) values(-94.132, -45.189)",
				Statement.RETURN_GENERATED_KEYS);

		int id = getLastInsertedId(statement);

		doTransaction(emf, lo -> {
			location l = lo.find(location.class, id);
			l.setLatitude(-6.132);
			l.setLongitude(-5.198);
		});

		// check
		statement = jdbcConnection.createStatement();
		ResultSet rs = statement.executeQuery("SELECT * FROM location WHERE id = " + id);
		rs.next();

		assertEquals(-5.132, rs.getDouble("latitude"), 1e-10);
		assertEquals(-6.189, rs.getDouble("longitude"), 1e-10);
		assertEquals(id, rs.getInt("id"));
	}

	private location aDetachedLocation = null;

	@Test
	public void testUpdateLocationByMerge() throws SQLException {
		// prepare database for test
		Statement statement = jdbcConnection.createStatement();
		statement.executeUpdate("INSERT INTO location(latitude, longitude) values(-94.132, -45.189)",
				Statement.RETURN_GENERATED_KEYS);
		int id = getLastInsertedId(statement);

		doTransaction(emf, lo -> {
			aDetachedLocation = lo.find(location.class, id);
		});
		// e is detached, because the entitymanager lo is closed (see
		// doTransaction)
		aDetachedLocation.setLatitude(-0.132);
		aDetachedLocation.setLongitude(-1.198);

		doTransaction(emf, em -> {
			em.merge(aDetachedLocation);
		});

		// check
		statement = jdbcConnection.createStatement();
		ResultSet rs = statement.executeQuery("SELECT * FROM location WHERE id = " + id);
		rs.next();

		assertEquals(-0.132, rs.getDouble("latitude"), 1e-10);
		assertEquals(-1.198, rs.getDouble("longitude"), 1e-10);
		assertEquals(id, rs.getInt("id"));
	}

	@Test
	public void testDeleteLocation() throws SQLException {
		// prepare database for test
		Statement statement = jdbcConnection.createStatement();
		statement.executeUpdate("INSERT INTO location(latitude, longitude) values(94.132, 45.189)",
				Statement.RETURN_GENERATED_KEYS);
		int id = getLastInsertedId(statement);

		doTransaction(emf, lo -> {
			location l = lo.find(location.class, id);
			lo.remove(l);
		});

		// check
		statement = jdbcConnection.createStatement();
		ResultSet rs = statement.executeQuery("SELECT COUNT(*) as total FROM location WHERE id = " + id);
		rs.next();

		assertEquals(0, rs.getInt("total"));
	}

	@Test
	public void testListLocation() throws SQLException {
		// prepare database for test
		Statement statement = jdbcConnection.createStatement();
		statement.executeUpdate("INSERT INTO location(latitude, longitude) values(4.524, 7.785)",
				Statement.RETURN_GENERATED_KEYS);
		// prepare database for test
		statement.executeUpdate("INSERT INTO location(latitude, longitude) values(8.456, 1.288)",
				Statement.RETURN_GENERATED_KEYS);

		List<location> locations = emf.createEntityManager()
				.createQuery("SELECT l FROM location l ORDER BY l.id", location.class).getResultList();

		// check
		assertEquals(2, locations.size());
		assertEquals(4.524, locations.get(0).getLatitude(), 1e-10);
		assertEquals(7.785, locations.get(0).getLongitude(), 1e-10);
		assertEquals(8.456, locations.get(1).getLatitude(), 1e-10);
		assertEquals(1.288, locations.get(1).getLongitude(), 1e-10);
	}
}