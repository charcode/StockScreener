package oak.tests.db.postgres;

import static org.junit.Assert.fail;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Formatter;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Test;
import org.postgresql.copy.CopyManager;
import org.postgresql.core.BaseConnection;

public class PostgreJdbcTests {

	private String url = "jdbc:postgresql://localhost/testdb";
	private String user = "test_java";
	private String password = "easy_password";
	private String sql = "INSERT INTO authors(id,name) VALUES(?,?)";
	private static Logger lgr = Logger.getLogger(PostgreJdbcTests.class.getName());

	@Test
	public void testPreparedStatement() {
		PreparedStatement pst = null;
		Connection con = null;
		try {
			con = DriverManager.getConnection(url, user, password);
			pst = con.prepareStatement(sql);

			int id = 6;
			String author = "Trygve Gulbranssen";

			pst.setInt(1, id);
			pst.setString(2, author);

			pst.executeUpdate();

		} catch (SQLException e) {
			log(e, Level.SEVERE);
		} finally {
			try {
				if (pst != null) {
					pst.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException ex) {
				log(ex, Level.SEVERE);
			}
		}
	}

	@Test
	public void testNotPrepared() {
		Statement st = null;
		Connection con = null;
		try {
			con = DriverManager.getConnection(url, user, password);

			st = con.createStatement();

			for (int i = 1; i <= 1000; i++) {
				String query = "INSERT INTO Testing(Id) VALUES(" + 2 * i + ")";
				st.executeUpdate(query);
			}
		} catch (SQLException ex) {
			log(ex, Level.SEVERE);
		} finally {

			try {
				if (st != null) {
					st.close();
				}
				if (con != null) {
					con.close();
				}

			} catch (SQLException ex) {
				log(ex, Level.SEVERE);
			}
		}
	}

	@Test
	public void testPerformanceOfPrepared() {
		Connection con = null;
		PreparedStatement pst = null;

		try {
			con = DriverManager.getConnection(url, user, password);

			pst = con.prepareStatement("INSERT INTO Testing(Id) VALUES(?)");

			for (int i = 1; i <= 1000; i++) {
				pst.setInt(1, i * 2);
				pst.executeUpdate();
			}
		} catch (SQLException ex) {
			log(ex, Level.SEVERE);
		} finally {

			try {
				if (pst != null) {
					pst.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException ex) {
				log(ex, Level.SEVERE);
			}
		}
	}

	@Test
	public void testSelectPreparedStatement() {
		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;

		try {

			con = DriverManager.getConnection(url, user, password);
			pst = con.prepareStatement("SELECT * FROM authors");
			rs = pst.executeQuery();

			while (rs.next()) {
				System.out.print(rs.getInt(1));
				System.out.print(": ");
				System.out.println(rs.getString(2));
			}

		} catch (SQLException ex) {
			log(ex, Level.SEVERE);

		} finally {

			try {
				if (rs != null) {
					rs.close();
				}
				if (pst != null) {
					pst.close();
				}
				if (con != null) {
					con.close();
				}

			} catch (SQLException ex) {
				lgr.log(Level.WARNING, ex.getMessage(), ex);
			}
		}
	}

	@Test
	public void testSelectWithPropertyFile() {
		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;

		Properties props = new Properties();
		FileInputStream in = null;

		try {
			in = new FileInputStream("target/classes/properties/database.properties");
			props.load(in);

		} catch (IOException ex) {
			lgr.log(Level.SEVERE, ex.getMessage(), ex);
			return;
		} finally {

			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				lgr.log(Level.SEVERE, ex.getMessage(), ex);
			}
		}

		String url = props.getProperty("db.url");
		String user = props.getProperty("db.user");
		String passwd = props.getProperty("db.passwd");

		try {

			con = DriverManager.getConnection(url, user, passwd);
			pst = con.prepareStatement("SELECT * FROM Authors");
			rs = pst.executeQuery();

			while (rs.next()) {
				System.out.print(rs.getInt(1));
				System.out.print(": ");
				System.out.println(rs.getString(2));
			}

		} catch (Exception ex) {
			lgr.log(Level.SEVERE, ex.getMessage(), ex);

		} finally {

			try {
				if (rs != null) {
					rs.close();
				}
				if (pst != null) {
					pst.close();
				}
				if (con != null) {
					con.close();
				}

			} catch (SQLException ex) {
				lgr.log(Level.WARNING, ex.getMessage(), ex);
			}
		}
	}

	@Test
	public void testColumnHeadersMetaData() {
		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {

			con = DriverManager.getConnection(url, user, password);
			String query = "SELECT name, title From authors, " + "books WHERE authors.id=books.author_id";
			pst = con.prepareStatement(query);
			rs = pst.executeQuery();

			ResultSetMetaData meta = rs.getMetaData();

			String colname1 = meta.getColumnName(1);
			String colname2 = meta.getColumnName(2);

			Formatter fmt1 = new Formatter();
			fmt1.format("%-21s%s", colname1, colname2);
			System.out.println(fmt1);

			while (rs.next()) {
				Formatter fmt2 = new Formatter();
				fmt2.format("%-21s", rs.getString(1));
				System.out.print(fmt2);
				System.out.println(rs.getString(2));
			}

		} catch (SQLException ex) {
			log(ex, Level.SEVERE);

		} finally {

			try {
				if (rs != null) {
					rs.close();
				}
				if (pst != null) {
					pst.close();
				}
				if (con != null) {
					con.close();
				}

			} catch (SQLException ex) {
				lgr.log(Level.WARNING, ex.getMessage(), ex);
			}
		}
	}

	@Test
	public void testListTables() {
		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;

		try {

			con = DriverManager.getConnection(url, user, password);
			String query = "SELECT table_name FROM information_schema.tables " + "WHERE table_schema = 'public'";
			pst = con.prepareStatement(query);

			rs = pst.executeQuery();

			while (rs.next()) {
				System.out.println(rs.getString(1));
			}

		} catch (SQLException ex) {
			log(ex, Level.SEVERE);

		} finally {

			try {
				if (rs != null) {
					rs.close();
				}
				if (pst != null) {
					pst.close();
				}
				if (con != null) {
					con.close();
				}

			} catch (SQLException ex) {
				lgr.log(Level.WARNING, ex.getMessage(), ex);
			}
		}
	}

	@Test
	public void testBatchInsert() {
		Connection con = null;
		Statement st = null;

		try {

			con = DriverManager.getConnection(url, user, password);

			st = con.createStatement();

			con.setAutoCommit(false);

			st.addBatch("DROP TABLE IF EXISTS friends");
			st.addBatch("CREATE TABLE friends(id serial, name VARCHAR(10))");
			st.addBatch("INSERT INTO friends(name) VALUES ('Jane')");
			st.addBatch("INSERT INTO friends(name) VALUES ('Tom')");
			st.addBatch("INSERT INTO friends(name) VALUES ('Rebecca')");
			st.addBatch("INSERT INTO friends(name) VALUES ('Jim')");
			st.addBatch("INSERT INTO friends(name) VALUES ('Robert')");

			int counts[] = st.executeBatch();

			con.commit();

			System.out.println("Committed " + counts.length + " updates");

		} catch (SQLException ex) {

			System.out.println(ex.getNextException());

			if (con != null) {
				try {
					con.rollback();
				} catch (SQLException ex1) {
					lgr.log(Level.WARNING, ex1.getMessage(), ex1);
					fail(ex1.getMessage());
				}
			}
			log(ex, Level.SEVERE);
		} finally {

			try {

				if (st != null) {
					st.close();
				}
				if (con != null) {
					con.close();
				}

			} catch (SQLException ex) {
				log(ex, Level.WARNING);
			}
		}
	}

	@Test
	public void testExport() {

		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		FileWriter fw = null;
		try {

			con = DriverManager.getConnection(url, user, password);

			CopyManager cm = new CopyManager((BaseConnection) con);

			fw = new FileWriter("friends.txt");
			cm.copyOut("COPY friends TO STDOUT WITH DELIMITER AS '|'", fw);

		} catch (SQLException | IOException ex) {
			log(ex, Level.SEVERE);

		} finally {

			try {
				if (rs != null) {
					rs.close();
				}
				if (pst != null) {
					pst.close();
				}
				if (con != null) {
					con.close();
				}
				if (fw != null) {
					fw.close();
				}

			} catch (SQLException | IOException ex) {
				log(ex, Level.WARNING);
			}
		}
	}

	@Test
	public void testImport() {

		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		FileReader fr = null;

		try {

			con = DriverManager.getConnection(url, user, password);

			CopyManager cm = new CopyManager((BaseConnection) con);

			String name = "friends.txt";
			fr = new FileReader(name);
			lgr.info("reading from "+ name);
			cm.copyIn("COPY friends FROM STDIN WITH DELIMITER '|'", fr);

		} catch (SQLException | IOException ex) {
			log(ex,Level.SEVERE);
		} finally {

			try {
				if (rs != null) {
					rs.close();
				}
				if (pst != null) {
					pst.close();
				}
				if (con != null) {
					con.close();
				}
				if (fr != null) {
					fr.close();
				}

			} catch (SQLException | IOException ex) {
				log(ex, Level.WARNING);
			}
		}
	}

	private void log(Throwable ex, Level level) {
		lgr.log(level, ex.getMessage(), ex);
	}

}
