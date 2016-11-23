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

import weathercool.proyectosi.Employee;

public class EmployeeTest extends SQLBasedTest {
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
	public void testCreateEmployee() throws SQLException {
		final Employee emp = new Employee();
		
		doTransaction(emf, em -> {
				emp.setName("Daniel");
				em.persist(emp);
		});
		
		//check
		Statement statement = jdbcConnection.createStatement();
		ResultSet rs = statement.executeQuery(
				"SELECT COUNT(*) as total FROM Employee WHERE id = "+emp.getId());
		rs.next();
		
		assertEquals(1, rs.getInt("total"));
	}
	
	//R
	@Test
	public void testFindById() throws SQLException {
		//prepare database for test
		Statement statement = jdbcConnection.createStatement();
		statement.executeUpdate(
				"INSERT INTO Employee(name) values('Daniela')", 
				Statement.RETURN_GENERATED_KEYS);
		
		int id = getLastInsertedId(statement);
		
		//test code
		Employee e = emf.createEntityManager().find(Employee.class, id);
		
		//assert code
		System.out.println("employee of id "+id+" is: "+e);
		assertEquals("Daniela", e.getName());
		assertEquals(id, e.getId());
	}
	
	//U
	@Test
	public void testUpdateEmployee() throws SQLException {
		//prepare database for test
		Statement statement = jdbcConnection.createStatement();
		statement.executeUpdate(
					"INSERT INTO Employee(name) values('Daniel')", 
					Statement.RETURN_GENERATED_KEYS);
		
		int id = getLastInsertedId(statement);
		
		doTransaction(emf, em -> {
			Employee e = em.find(Employee.class, id);
			e.setName("Daniel Glez");
		});
		
		//check
		statement = jdbcConnection.createStatement();
		ResultSet rs = statement.executeQuery(
				"SELECT * FROM Employee WHERE id = "+id);
		rs.next();
		
		assertEquals("Daniel Glez", rs.getString("name"));
		assertEquals(id, rs.getInt("id"));
		
	}
	
	//U
	private Employee aDetachedEmployee = null;
	@Test
	public void testUpdateByMerge() throws SQLException {
		//prepare database for test
		Statement statement = jdbcConnection.createStatement();
		statement.executeUpdate(
					"INSERT INTO Employee(name) values('Daniel')", 
					Statement.RETURN_GENERATED_KEYS);
		int id = getLastInsertedId(statement);
		
		doTransaction(emf, em -> {
			aDetachedEmployee = em.find(Employee.class, id);
		});
		// e is detached, because the entitymanager em is closed (see doTransaction)
		
		aDetachedEmployee.setName("Daniel Glez");
		
		doTransaction(emf, em -> {
			em.merge(aDetachedEmployee);
		});
		
		//check
		statement = jdbcConnection.createStatement();
		ResultSet rs = statement.executeQuery(
				"SELECT * FROM Employee WHERE id = "+id);
		rs.next();
		
		assertEquals("Daniel Glez", rs.getString("name"));
		assertEquals(id, rs.getInt("id"));
	}
	
	//D
	@Test
	public void testDeleteEmployee() throws SQLException {
		//prepare database for test
		Statement statement = jdbcConnection.createStatement();
		statement.executeUpdate(
					"INSERT INTO Employee(name) values('Daniel')", 
					Statement.RETURN_GENERATED_KEYS);
		int id = getLastInsertedId(statement);
		
		doTransaction(emf, em -> {
			Employee e = em.find(Employee.class, id);
			em.remove(e);
		});
		
		//check
		statement = jdbcConnection.createStatement();
		ResultSet rs = statement.executeQuery(
				"SELECT COUNT(*) as total FROM Employee WHERE id = "+id);
		rs.next();
		
		assertEquals(0, rs.getInt("total"));
	}
	
	//L
	@Test
	public void testListEmployee() throws SQLException {
		//prepare database for test
		Statement statement = jdbcConnection.createStatement();
		statement.executeUpdate(
					"INSERT INTO Employee(name) values('Daniel')", 
					Statement.RETURN_GENERATED_KEYS);
		//prepare database for test
		statement.executeUpdate(
					"INSERT INTO Employee(name) values('Pepe')", 
					Statement.RETURN_GENERATED_KEYS);
		
		List<Employee> employees = emf.createEntityManager()
			.createQuery("SELECT e FROM Employee e ORDER BY e.name", Employee.class)
			.getResultList();
		
		//check
		assertEquals(2, employees.size());
		assertEquals("Daniel", employees.get(0).getName());
		assertEquals("Pepe", employees.get(1).getName());
	}
	
}