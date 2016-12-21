package weathercool.proyectosi;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLBasedTest {

	protected static Connection jdbcConnection;
	
	private static String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	private static final String DB_URL = "jdbc:mysql://localhost:3306/si-database";
	private static final String USER = "esei";
	private static final String PASS = "eseipass";
	
	static {
		try {
			jdbcConnection = createConnection();
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public static void renewConnection() throws ClassNotFoundException, SQLException {
		try {
			jdbcConnection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		jdbcConnection = createConnection();
		
	}
	private static Connection createConnection() throws ClassNotFoundException, SQLException {
		Class.forName(JDBC_DRIVER);
		return DriverManager.getConnection(DB_URL, USER, PASS);
	}
	public int getLastInsertedId(Statement statement) throws SQLException {
		ResultSet rs = statement.getGeneratedKeys();
		rs.next();
		return rs.getInt(1);
	}
}
