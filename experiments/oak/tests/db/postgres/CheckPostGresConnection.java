package oak.tests.db.postgres;

import static org.junit.Assert.assertNotNull;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

public class CheckPostGresConnection {

	private static Logger log = LogManager.getLogger(CheckPostGresConnection.class);
	@Test
	public void testPostGresConnection() {
		Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        
        String url = "jdbc:postgresql://localhost/testdb";
        String user = "test_java";
        String password = "easy_password";
        
        String version = null;
        try {
            con = DriverManager.getConnection(url, user, password);
            st = con.createStatement();
            rs = st.executeQuery("SELECT VERSION()");

            if (rs.next()) {
                version = rs.getString(1);
				System.out.println(version);
            }

        } catch (SQLException ex) {
            log.error( ex.getMessage(), ex);

        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (st != null) {
                    st.close();
                }
                if (con != null) {
                    con.close();
                }

            } catch (SQLException ex) {
                log.error(ex.getMessage(), ex);
            }
        }
        assertNotNull(version);
    }
}