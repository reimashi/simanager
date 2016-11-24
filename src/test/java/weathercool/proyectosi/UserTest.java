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

public class UserTest extends SQLBasedTest {
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
		super.renewConnection();
	}
	
	//C
	@Test
	public void testCreateUser() throws SQLException {
		final User emp = new User();
		
		doTransaction(emf, em -> {
				emp.setName("Daniel");
				em.persist(emp);
		});
		
		//check
		Statement statement = jdbcConnection.createStatement();
		ResultSet rs = statement.executeQuery(
				"SELECT COUNT(*) as total FROM User WHERE id = "+emp.getId());
		rs.next();
		
		assertEquals(1, rs.getInt("total"));
	}
	
	//R
	@Test
	public void testFindById() throws SQLException {
		//prepare database for test
		Statement statement = jdbcConnection.createStatement();
		statement.executeUpdate(
				"INSERT INTO User(name) values('Daniela')",
				Statement.RETURN_GENERATED_KEYS);
		
		int id = getLastInsertedId(statement);
		
		//test code
		User e = emf.createEntityManager().find(User.class, id);
		
		//assert code
		System.out.println("User of id "+id+" is: "+e);
		assertEquals("Daniela", e.getName());
		assertEquals(id, e.getId());
	}
	
	//U
	@Test
	public void testUpdateUser() throws SQLException {
		//prepare database for test
		Statement statement = jdbcConnection.createStatement();
		statement.executeUpdate(
					"INSERT INTO User(name) values('Daniel')",
					Statement.RETURN_GENERATED_KEYS);
		
		int id = getLastInsertedId(statement);
		
		doTransaction(emf, em -> {
			User e = em.find(User.class, id);
			e.setName("Daniel Glez");
		});
		
		//check
		statement = jdbcConnection.createStatement();
		ResultSet rs = statement.executeQuery(
				"SELECT * FROM User WHERE id = "+id);
		rs.next();
		
		assertEquals("Daniel Glez", rs.getString("name"));
		assertEquals(id, rs.getInt("id"));
		
	}
	
	//U
	private User aDetachedUser = null;
	@Test
	public void testUpdateByMerge() throws SQLException {
		//prepare database for test
		Statement statement = jdbcConnection.createStatement();
		statement.executeUpdate(
					"INSERT INTO User(name) values('Daniel')",
					Statement.RETURN_GENERATED_KEYS);
		int id = getLastInsertedId(statement);
		
		doTransaction(emf, em -> {
			aDetachedUser = em.find(User.class, id);
		});
		// e is detached, because the entitymanager em is closed (see doTransaction)
		
		aDetachedUser.setName("Daniel Glez");
		
		doTransaction(emf, em -> {
			em.merge(aDetachedUser);
		});
		
		//check
		statement = jdbcConnection.createStatement();
		ResultSet rs = statement.executeQuery(
				"SELECT * FROM User WHERE id = "+id);
		rs.next();
		
		assertEquals("Daniel Glez", rs.getString("name"));
		assertEquals(id, rs.getInt("id"));
	}
	
	//D
	@Test
	public void testDeleteUser() throws SQLException {
		//prepare database for test
		Statement statement = jdbcConnection.createStatement();
		statement.executeUpdate(
					"INSERT INTO User(name) values('Daniel')",
					Statement.RETURN_GENERATED_KEYS);
		int id = getLastInsertedId(statement);
		
		doTransaction(emf, em -> {
			User e = em.find(User.class, id);
			em.remove(e);
		});
		
		//check
		statement = jdbcConnection.createStatement();
		ResultSet rs = statement.executeQuery(
				"SELECT COUNT(*) as total FROM User WHERE id = "+id);
		rs.next();
		
		assertEquals(0, rs.getInt("total"));
	}
	
	//L
	@Test
	public void testListUser() throws SQLException {
		//prepare database for test
		Statement statement = jdbcConnection.createStatement();
		statement.executeUpdate(
					"INSERT INTO User(name) values('Daniel')",
					Statement.RETURN_GENERATED_KEYS);
		//prepare database for test
		statement.executeUpdate(
					"INSERT INTO User(name) values('Pepe')",
					Statement.RETURN_GENERATED_KEYS);
		
		List<User> users = emf.createEntityManager()
			.createQuery("SELECT e FROM User e ORDER BY e.name", User.class)
			.getResultList();
		
		//check
		assertEquals(2, users.size());
		assertEquals("Daniel", users.get(0).getName());
		assertEquals("Pepe", users.get(1).getName());
	}
	
}