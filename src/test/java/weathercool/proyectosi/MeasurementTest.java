package weathercool.proyectosi;

import static org.junit.Assert.*;
import static weathercool.proyectosi.TransactionUtils.doTransaction;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static weathercool.proyectosi.TransactionUtils.doTransaction;

public class MeasurementTest extends SQLBasedTest {
	private static EntityManagerFactory emf;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		emf = Persistence.createEntityManagerFactory("si-database");
	}
	
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		if(emf!=null && emf.isOpen()) emf.close();
	}
	
	@After
	public void renewConnectionAfterTest() throws ClassNotFoundException, SQLException {
        Statement statement = jdbcConnection.createStatement();
        statement.execute("DELETE FROM alert");

		super.renewConnection();
	}
	
	@Test
	public void testCreateMeasurement() throws SQLException {

		Statement statement = jdbcConnection.createStatement();
		statement.executeUpdate(
				"INSERT INTO location(latitude, longitude) values(94.132, 45.189)", 
				Statement.RETURN_GENERATED_KEYS);
		int locId = getLastInsertedId(statement);
		
		statement = jdbcConnection.createStatement();
		statement.executeUpdate(
				"INSERT INTO alert(temperature_high, temperature_half, temperature_low, rain_high, rain_half, rain_low) values(15, 25, 27, 30, 1, 23)",
				Statement.RETURN_GENERATED_KEYS);

		int alertId = getLastInsertedId(statement);
		
		statement = jdbcConnection.createStatement();
		statement.executeUpdate(
				"INSERT INTO time(year, month, day, hour, minute, second) values(2016, 11, 20, 17, 55, 38)", 
				Statement.RETURN_GENERATED_KEYS);

		int timeId = getLastInsertedId(statement);
		
		statement.executeUpdate(
				"INSERT INTO Measurement(alert_id, location_id, time_id, elevation, temperature_surface, temperature_500mb,"
				+ "temperature_850mb, temperature_sea_level, cloud_cover_high, cloud_cover_half, cloud_cover_low,"
				+ "visibility, salinity, water_speed_eastward, water_speed_northward, wave_direction_mean,wave_period_absolute, wave_period_peak, wave_direction_peak,"
				+ "snow_level, snow_precipitation, rain_pecipitation, humidity, wind_direction, wind_lon, wind_lat, wind_gust) "
				+ "values("+alertId+","+locId+","+timeId+",0,20,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)", 
				Statement.RETURN_GENERATED_KEYS);
		// check
		Statement statement2 = jdbcConnection.createStatement();
		ResultSet rs = statement2.executeQuery("SELECT COUNT(*) AS Total FROM Measurement WHERE alert_id = "+ alertId);
		rs.next();

		assertEquals(1, rs.getInt("total"));
	}
	
	@Test
	public void testFindMeasures() throws SQLException {
		Statement statement = jdbcConnection.createStatement();
		statement.executeUpdate(
				"INSERT INTO location(latitude, longitude) values(94.132, 45.189)", 
				Statement.RETURN_GENERATED_KEYS);
		int locId = getLastInsertedId(statement);
		statement = jdbcConnection.createStatement();
		statement.executeUpdate(
				"INSERT INTO alert(temperature_high, temperature_half, temperature_low, rain_high, rain_half, rain_low) values(15, 25, 27, 30, 1, 23)",
				Statement.RETURN_GENERATED_KEYS);

		int alertId = getLastInsertedId(statement);
		
		statement = jdbcConnection.createStatement();
		statement.executeUpdate(
				"INSERT INTO time(year, month, day, hour, minute, second) values(2016, 11, 20, 17, 55, 38)", 
				Statement.RETURN_GENERATED_KEYS);

		int timeId = getLastInsertedId(statement);
		
		statement = jdbcConnection.createStatement();
		statement.executeUpdate(
				"INSERT INTO Measurement(alert_id, location_id, time_id, elevation, temperature_surface, temperature_500mb,"
				+ "temperature_850mb, temperature_sea_level, cloud_cover_high, cloud_cover_half, cloud_cover_low,"
				+ "visibility, salinity, water_speed_eastward, water_speed_northward, wave_direction_mean,wave_period_absolute, wave_period_peak, wave_direction_peak,"
				+ "snow_level, snow_precipitation, rain_pecipitation, humidity, wind_direction, wind_lon, wind_lat, wind_gust) "
				+ "values("+alertId+","+locId+","+timeId+",0,20,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)", 
				Statement.RETURN_GENERATED_KEYS);
		
	
		EntityManager em = emf.createEntityManager();
		LocationClass l = em.find(LocationClass.class, locId);
		
		List<Measurement> measures = em.createQuery("SELECT m "
				+ "FROM Measurement m WHERE location_id = :l", Measurement.class)
		.setParameter("l", l).getResultList();
		
		//check
		assertEquals(1, measures.size());
		assertEquals(alertId, measures.get(0).getAlert());
	}
	
	@Test
	public void testDeleteMeasure() throws SQLException {
		Statement statement = jdbcConnection.createStatement();
		statement.executeUpdate(
				"INSERT INTO location(latitude, longitude) values(94.132, 45.189)", 
				Statement.RETURN_GENERATED_KEYS);
		int locId = getLastInsertedId(statement);
		statement = jdbcConnection.createStatement();
		statement.executeUpdate(
				"INSERT INTO alert(temperature_high, temperature_half, temperature_low, rain_high, rain_half, rain_low) values(15, 25, 27, 30, 1, 23)",
				Statement.RETURN_GENERATED_KEYS);

		int alertId = getLastInsertedId(statement);
		
		statement = jdbcConnection.createStatement();
		statement.executeUpdate(
				"INSERT INTO time(year, month, day, hour, minute, second) values(2016, 11, 20, 17, 55, 38)", 
				Statement.RETURN_GENERATED_KEYS);

		int timeId = getLastInsertedId(statement);
		
		statement = jdbcConnection.createStatement();
		statement.executeUpdate(
				"INSERT INTO Measurement(alert_id, location_id, time_id, elevation, temperature_surface, temperature_500mb,"
				+ "temperature_850mb, temperature_sea_level, cloud_cover_high, cloud_cover_half, cloud_cover_low,"
				+ "visibility, salinity, water_speed_eastward, water_speed_northward, wave_direction_mean,wave_period_absolute, wave_period_peak, wave_direction_peak,"
				+ "snow_level, snow_precipitation, rain_pecipitation, humidity, wind_direction, wind_lon, wind_lat, wind_gust) "
				+ "values("+alertId+","+locId+","+timeId+",0,20,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)", 
				Statement.RETURN_GENERATED_KEYS);
		int id = getLastInsertedId(statement);

		doTransaction(emf, me -> {
			Measurement m = me.find(Measurement.class, id);
			me.remove(m);
		});

		// check
		statement = jdbcConnection.createStatement();
		ResultSet rs = statement.executeQuery("SELECT COUNT(*) as total FROM Measurement WHERE id = " + id);
		rs.next();

		assertEquals(0, rs.getInt("total"));
	}
	
	@Test
	public void testFindById() throws SQLException {
		// prepare database for test
		Statement statement = jdbcConnection.createStatement();
		statement.executeUpdate(
				"INSERT INTO location(latitude, longitude) values(94.132, 45.189)", 
				Statement.RETURN_GENERATED_KEYS);
		int locId = getLastInsertedId(statement);
		statement = jdbcConnection.createStatement();
		statement.executeUpdate(
				"INSERT INTO alert(temperature_high, temperature_half, temperature_low, rain_high, rain_half, rain_low) values(15, 25, 27, 30, 1, 23)",
				Statement.RETURN_GENERATED_KEYS);

		int alertId = getLastInsertedId(statement);
		
		statement = jdbcConnection.createStatement();
		statement.executeUpdate(
				"INSERT INTO time(year, month, day, hour, minute, second) values(2016, 11, 20, 17, 55, 38)", 
				Statement.RETURN_GENERATED_KEYS);

		int timeId = getLastInsertedId(statement);
		
		statement = jdbcConnection.createStatement();
		statement.executeUpdate(
				"INSERT INTO Measurement(alert_id, location_id, time_id, elevation, temperature_surface, temperature_500mb,"
				+ "temperature_850mb, temperature_sea_level, cloud_cover_high, cloud_cover_half, cloud_cover_low,"
				+ "visibility, salinity, water_speed_eastward, water_speed_northward, wave_direction_mean,wave_period_absolute, wave_period_peak, wave_direction_peak,"
				+ "snow_level, snow_precipitation, rain_pecipitation, humidity, wind_direction, wind_lon, wind_lat, wind_gust) "
				+ "values("+alertId+","+locId+","+timeId+",0,20,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)", 
				Statement.RETURN_GENERATED_KEYS);

		int id = getLastInsertedId(statement);

		// test code
		EntityManager em = emf.createEntityManager();
		Measurement me = em.find(Measurement.class, id);

		// assert code
		assertEquals(0, me.getElevation(), 1e-10);
		assertEquals(20, me.getTemperatureSurface(), 1e-10);
		assertEquals(0, me.getTemperature500mb(), 1e-10);
		assertEquals(0, me.getTemperature850mb(), 1e-10);
		assertEquals(0, me.getTemperatureSeaLevel(), 1e-10);
		assertEquals(0, me.getCloudCoverHigh(), 1e-10);
		assertEquals(0, me.getCloudCoverHalf(), 1e-10);
		assertEquals(0, me.getCloudCoverLow(), 1e-10);
		assertEquals(0, me.getVisibility(), 1e-10);
		assertEquals(0, me.getSalinity(), 1e-10);
		assertEquals(0, me.getWaterSpeedEastward(), 1e-10);
		assertEquals(0, me.getWaterSpeedNorthward(), 1e-10);
		assertEquals(0, me.getWaterDirectionMean(), 1e-10);
		assertEquals(0, me.getWaterDirectionPeak(), 1e-10);
		assertEquals(0, me.getWaterPeriodAbsolute(), 1e-10);
		assertEquals(0, me.getWaterPeriodPeak(), 1e-10);
		assertEquals(0, me.getSnowLevel(), 1e-10);
		assertEquals(0, me.getSnowPrecipitation(), 1e-10);
		assertEquals(0, me.getRainPrecipitation(), 1e-10);
		assertEquals(0, me.getHumidity(), 1e-10);
		assertEquals(0, me.getWindDirection(), 1e-10);
		assertEquals(0, me.getWindLon(), 1e-10);
		assertEquals(0, me.getWindLat(), 1e-10);
		assertEquals(0, me.getWindGust(), 1e-10);
		
	}
}