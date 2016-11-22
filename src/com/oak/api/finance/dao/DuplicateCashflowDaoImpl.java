package com.oak.api.finance.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.oak.api.finance.model.dto.StatementPeriod;

public class DuplicateCashflowDaoImpl implements DuplicateCashflowsDao {

//	Class.forName("org.postgresql.Driver");
//	Connection connection = null;
//	connection = DriverManager.getConnection(
//	   "jdbc:postgresql://hostname:port/dbname","username", "password");
//	connection.close();
	private String getDups = "select * from (select ticker, statement_period, end_date, count (*) size "
			+ "from cashflow_statment group by ticker, end_date, statement_period) c"
			+ " where c.size > 1";
	
	public DuplicateCashflowDaoImpl() {

	}
	
	
//	@Query(value="select ticker, statement_period, end_date, size from (select ticker, statement_period, end_date, count (*) size from cashflow_statment group by ticker, end_date, statement_period) c where c.size > 1")
	@Override
	public List<Duplicate>findDuplicateCashflows(){
		ArrayList<Duplicate>dupes = new ArrayList<Duplicate>();
		try {
			
			Class.forName("org.postgresql.Driver");
			
			Connection connection = null;
			connection = DriverManager.getConnection(
					"jdbc:postgresql://127.0.0.1:5432/financedb", "teyta_usr",
					"w0nd3rfu!!");
			
			if (connection != null) {
			} else {
				System.out.println("Failed to make connection!");
			}
			
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(getDups);
			while (rs.next()) {
				Duplicate d = new Duplicate();
				String ticker = rs.getString("ticker");
				StatementPeriod statementPeriod = StatementPeriod.valueOf(rs.getString("statement_period"));
				Date endDate = rs.getDate("end_date");
				int size = rs.getInt("size");
				d.setEndDate(endDate);
				d.setSize(size);
				d.setTicker(ticker);
				d.setStatementPeriod(statementPeriod);
				dupes.add(d);
			}
			
		} catch (ClassNotFoundException e) {
			
			System.out.println("Where is your PostgreSQL JDBC Driver? "
					+ "Include in your library path!");
			e.printStackTrace();
			
		} catch (SQLException e) {
			
			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();
			
		}
		return dupes;
	}
	

}
