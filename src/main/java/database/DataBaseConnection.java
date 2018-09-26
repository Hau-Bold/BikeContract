package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.sqlite.SQLiteConfig;

public class DataBaseConnection {

	private Connection dataBaseConnection;

	/**
	 * Constructor. to establish a connection
	 * 
	 * @return
	 */
	public Connection getConnection() {
		return dataBaseConnection;
	}

	public DataBaseConnection(String databaseConnectionPath) {
		/** loading driver */
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			System.err.println("error while loading jdbc-driver");
			e.printStackTrace();
		}

		initDatabaseConnection(databaseConnectionPath);
	}

	/**
	 * establishes a connection
	 * 
	 * @param databaseConnectionPath
	 */
	private void initDatabaseConnection(String databaseConnectionPath) {
		try {

			SQLiteConfig config = new SQLiteConfig();

			/** to enable usage of foreign keys */
			config.enforceForeignKeys(true);

			dataBaseConnection = DriverManager.getConnection("jdbc:sqlite:" + databaseConnectionPath,
					config.toProperties());
		} catch (SQLException e) {
			System.out.println("connection" + e.getMessage());
		}

		/** handling when program is terminated */
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				try {
					if (dataBaseConnection != null && !dataBaseConnection.isClosed()) {
						dataBaseConnection.close();
						if (dataBaseConnection.isClosed())
							System.out.println("Status(database): disconnected");
					}
				} catch (Exception ex) {
					System.out.println("Error! " + ex.getMessage());
				}
			}
		});
	}
}
