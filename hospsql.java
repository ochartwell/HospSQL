package hospitalData;
import java.util.*;
//import java.util.Map.Entry;
import java.sql.*;

/* Hospital Database (with JDBC) (01/15/17)
 * 
 * Prompt: Rank hospitals within a database based on response times to health emergencies within recovery rooms
 * as well as consumer ratings.  As well, since database would cover one particular area (for now), can give
 * average so that can give a general rating and response time expectation for hospitals within a district.
 * 
 * Improvement plans:
 * (1) Option to add districts
 * (2) Adding ratings for existing hospitals --> calculate new average for rating/response time and update table row
 * (3) FORMAT THE COLUMNS MY GOSH THEY ARE SO UGLY RIGHT NOW
 * 
 * No current bugs to report.
 * 
 */

public class hospsql {
	
	
	// The name of the MySQL account to use (or empty for anonymous) 
	private final String userName = "root";

	// The password for the MySQL account (or empty for anonymous) 
	private final String password = "root";
	/*
	** The name of the computer running MySQL 
	private final String serverName = "localhost";

	** The port of the MySQL server (default is 3306) 
	private final int portNumber = 3306;
	
	private final String dbName = "hosptable"; */
	
	/** establishes an SQL connection */
	public Connection getConnection() throws SQLException {
		 Connection conn = null;
		 Properties connProp = new Properties();
		 connProp.put("user", this.userName);
		 connProp.put("password", this.password);
		 
		 conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/hosptable?autoReconnect=true&useSSL=false", connProp);
		 return conn;
	}
	
	public boolean executeUpdate(Connection conn, String command) throws SQLException {
		Statement stm = null;
		try {
			stm = conn.createStatement();
			stm.executeUpdate(command);
			return true;
		} finally {
			if (stm != null)
				stm.close();
		}
	}
	
	public void main2() throws SQLException {
		
		Connection conn = null;
		try {
			conn = this.getConnection();
			System.out.println("** Connected to HospDB **");
		} catch (SQLException e) {
			System.out.println("Error connecting");
			e.printStackTrace();
			return;
		}
		
		/*try {
			String drop = "DROP TABLE Madison;";
			this.executeUpdate(conn, drop);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		try {
			String create =
					"CREATE TABLE Madison (" 
					+ "Id INTEGER NOT NULL, "
					+ "Name CHARACTER(40) NOT NULL, "
					+ "Rating FLOAT, "
					+ "ResponseTime FLOAT, "
					+ "PRIMARY KEY ( Id ))";
			this.executeUpdate(conn, create);
			System.out.println("Table created");
		} catch (SQLException e) {
			System.out.println("Error creating");
			e.printStackTrace();
			return;
		}*/
		
		Scanner sc = new Scanner(System.in);
		Scanner inp = new Scanner(System.in);
		int in = 0;
		String addin;
		
		Statement stm = null;
		
		while(true) {

			System.out.println("Choose a number: ");
			System.out.println("(1) View database "
					+ "(2) Add database entry "
					+ "(3) Find average for district "
					+ "(4) Evaluation "
					+ "(5) Add new district"
					+ "(6) Exit");
			in = sc.nextInt();
			if(in > 6 || in < 1) {
				System.out.println("Error: Please choose from the menu.");
				continue;
			}
			switch (in) {

			case 1: 
				//TODO: print database
				try {
					stm = conn.createStatement();
					String sql = "SELECT * FROM madison;";
					ResultSet res = stm.executeQuery(sql);
					while (res.next()) {
						int ids = res.getInt("Id");
						String name = res.getString("Name");
						float rating = res.getFloat("Rating");
						float rT = res.getFloat("ResponseTime");
						System.out.println(ids + "\t" + name 
								+ "\t" + rating + "\t" + rT);
					}
				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					if (stm != null)
						stm.close();
				}
				break;
			case 2:
				//TODO: Choose what district to add entry to
				
				System.out.println("Please enter as follows: ID, name, rating, responseTime *COMMAS INCLUDED*");
				addin = inp.nextLine();
				String[] addinn = addin.split(",");
				//System.out.println(Arrays.toString(addinn));
				double chg = Double.parseDouble(addinn[2]);
				if (chg > 5.0 || chg < 0.0) {
					System.out.println("Error: invalid rating");
					System.out.println("Entry addition unsuccessful");
					break;
				}
				try {
					//stm = conn.createStatement();
					String sql = "INSERT INTO Madison (Id, Name, Rating, ResponseTime) "
							   + "VALUES ('" + addinn[0] + "','" + addinn[1] + "','" + addinn[2] + "','" + addinn[3] + "');";
					this.executeUpdate(conn, sql);
				} catch (SQLException e) {
					e.printStackTrace();
				} /*finally {
					if (stm != null)
						stm.close();
				}*/
				System.out.println("Entry added successfully!");
				break;
			case 3:
				//TODO: calculate averages for each table
				
				System.out.println("Average rating (choose 1) or response time (choose 2)?");
				int choice = inp.nextInt();
				if (choice > 2 || choice < 1) {
					System.out.println("Invalid choice");
					break;
				}
				try {
					String sql = null;
					int sum = 0;
					int count = 0;
					int avg = 0;
					if (choice == 1) {
						stm = conn.createStatement();
						sql = "SELECT Rating FROM Madison;";
						ResultSet rs = stm.executeQuery(sql);
						while (rs.next()) {
							float rating = rs.getFloat("Rating");
							sum += rating;
							count++;
						}
						avg = sum / count;
						System.out.println("Average rating for Madison is: " + avg);
					} else if (choice == 2) {
						stm = conn.createStatement();
						sql = "SELECT ResponseTime FROM Madison;";
						ResultSet rs = stm.executeQuery(sql);
						while (rs.next()) {
							float rating = rs.getFloat("ResponseTime");
							sum += rating;
							count++;
						}
						avg = sum / count;
						System.out.println("Average response time for Madison is: " + avg);
					}
				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					if (stm != null) 
						stm.close();
				}
				break;
			case 4:
				//TODO: find best hospital per district (best hospital of a table)
				//In terms of response time vs. rating?
				String name = null;
				try {
					stm = conn.createStatement();
					String sql = "SELECT MAX(Rating), Name from Madison "
							+ "GROUP BY Name;";
					ResultSet res = stm.executeQuery(sql);
					while (res.next()) {
						name = res.getString("Name");
					}
					System.out.println("Best hospital in the district is: " + name);
				} catch (SQLException e) {
					e.printStackTrace();
					System.out.println("Error getting max");
				} finally {
					if (stm != null)
						stm.close();
				}
				/*double bestScore = agg();*/
				break;
			case 5:
				// TODO: adding a district (new table) to the db
				System.out.print("Please enter the district you wish to add: ");
				String newDis = inp.next();
				try {
					String sql = "CREATE TABLE " + newDis + "("
							+ "Id INTEGER NOT NULL, "
							+ "Name CHARACTER(40) NOT NULL, "
							+ "Rating FLOAT, "
							+ "ResponseTime FLOAT, "
							+ "PRIMARY KEY ( Id ));";
					
							
				}
				break;
			case 6:
				System.out.println("Exiting database . . .");
				System.exit(1);
				//break;
			}
		}
	}

	public static void main(String[] args) throws SQLException {
		// don't put SQL code here since static method 
	
		hospsql db = new hospsql();
		db.main2();
		
	}

}
