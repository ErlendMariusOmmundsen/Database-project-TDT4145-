package databaseprosjekt;
import java.sql.*;
import java.util.Properties;

public abstract class DBConn {
	protected Connection conn;

	public DBConn () {
	}

	public void connect() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url = "";
			String username = "";
			String password = 
			conn = DriverManager.getConnection(url, username, password);
		} catch (Exception e)
		{
			throw new RuntimeException("Unable to connect", e);
		}
	}

}

