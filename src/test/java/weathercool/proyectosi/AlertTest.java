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

import static org.junit.Assert.assertEquals;
import static weathercool.proyectosi.TransactionUtils.doTransaction;

public class AlertTest extends SQLBasedTest {
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
	public void testCreateAlert() throws SQLException {
		final Alert al = new Alert();

		doTransaction(emf, a -> {
			al.setTemperatureHigh(15);
			al.setTemperatureHalf(25);
			al.setTemperatureLow(27);
			al.setRainHigh(30);
			al.setRainHalf(1);
			al.setRainLow(23);
			a.persist(al);
		});
		// check
		Statement statement = jdbcConnection.createStatement();
		ResultSet rs = statement.executeQuery("SELECT COUNT(*) AS Total FROM Alert WHERE id = " + al.getId());
		rs.next();

		assertEquals(1, rs.getInt("total"));
	}

	@Test
	public void testFindById() throws SQLException {
		// prepare database for test
		Statement statement = jdbcConnection.createStatement();
		statement.executeUpdate("INSERT INTO alert(temperature_high, temperature_half, temperature_low, rain_high, rain_half, rain_low) values(15, 25, 27, 30, 1, 23)",
				Statement.RETURN_GENERATED_KEYS);

		int id = getLastInsertedId(statement);

		// test code
		Alert al = emf.createEntityManager().find(Alert.class, id);

		// assert code
		
		assertEquals(15, al.getTemperatureHigh());
		assertEquals(25, al.getTemperatureHalf());
		assertEquals(27, al.getTemperatureLow());
		assertEquals(30, al.getRainHigh());
		assertEquals(1, al.getRainHalf());
		assertEquals(23, al.getRainLow());
		assertEquals(id, al.getId());
	}

	@Test
	public void testUpdateAlerts() throws SQLException {
		// prepare database for test
		Statement statement = jdbcConnection.createStatement();
		statement.executeUpdate("INSERT INTO alert(temperature_high, temperature_half, temperature_low, rain_high, rain_half, rain_low) values(15, 25, 27, 30, 1, 23)",
				Statement.RETURN_GENERATED_KEYS);

		int id = getLastInsertedId(statement);

		doTransaction(emf, a -> {
			Alert al = a.find(Alert.class, id);
			al.setTemperatureHigh(15);
			al.setTemperatureHalf(25);
			al.setTemperatureLow(27);
			al.setRainHigh(30);
			al.setRainHalf(1);
			al.setRainLow(23);
			a.persist(al);
		});

		// check
		statement = jdbcConnection.createStatement();
		ResultSet rs = statement.executeQuery("SELECT * FROM alerts WHERE id = " + id);
		rs.next();

		assertEquals(15, rs.getInt("temperature_high"));
		assertEquals(25, rs.getInt("temperature_half"));
		assertEquals(27, rs.getInt("temperature_low"));
		assertEquals(30, rs.getInt("rain_high"));
		assertEquals(1, rs.getInt("rain_half"));
		assertEquals(23, rs.getInt("rain_low"));;
		assertEquals(id, rs.getInt("id"));
	}

	private Alert aDetachedAlert = null;

	@Test
	public void testUpdateTimeByMerge() throws SQLException {
		// prepare database for test
		Statement statement = jdbcConnection.createStatement();
		statement.executeUpdate("INSERT INTO alert(temperature_high, temperature_half, temperature_low, rain_high, rain_half, rain_low) values(15, 25, 27, 30, 1, 23)",
				Statement.RETURN_GENERATED_KEYS);
		int id = getLastInsertedId(statement);

		doTransaction(emf, a -> {
			aDetachedAlert = a.find(Alert.class, id);
		});
		// e is detached, because the entitymanager lo is closed (see
		// doTransaction)
		
		aDetachedAlert.setTemperatureHigh(15);
		aDetachedAlert.setTemperatureHalf(25);
		aDetachedAlert.setTemperatureLow(27);
		aDetachedAlert.setRainHigh(30);
		aDetachedAlert.setRainHalf(1);
		aDetachedAlert.setRainLow(23);

		doTransaction(emf, em -> {
			em.merge(aDetachedAlert);
		});

		// check
		statement = jdbcConnection.createStatement();
		ResultSet rs = statement.executeQuery("SELECT * FROM alerts WHERE id = " + id);
		rs.next();

        assertEquals(15, rs.getInt("temperature_high"));
        assertEquals(25, rs.getInt("temperature_half"));
        assertEquals(27, rs.getInt("temperature_low"));
        assertEquals(30, rs.getInt("rain_high"));
        assertEquals(1, rs.getInt("rain_half"));
        assertEquals(23, rs.getInt("rain_low"));;
		assertEquals(id, rs.getInt("id"));
	}

	@Test
	public void testDeleteAlert() throws SQLException {
		// prepare database for test
		Statement statement = jdbcConnection.createStatement();
		statement.executeUpdate("INSERT INTO alert(temperature_high, temperature_half, temperature_low, rain_high, rain_half, rain_low) values(15, 25, 27, 30, 1, 23)",
				Statement.RETURN_GENERATED_KEYS);
		int id = getLastInsertedId(statement);

		doTransaction(emf, a -> {
			Alert al = a.find(Alert.class, id);
			a.remove(al);
		});

		// check
		statement = jdbcConnection.createStatement();
		ResultSet rs = statement.executeQuery("SELECT COUNT(*) as total FROM Alert WHERE id = " + id);
		rs.next();

		assertEquals(0, rs.getInt("total"));
	}

}