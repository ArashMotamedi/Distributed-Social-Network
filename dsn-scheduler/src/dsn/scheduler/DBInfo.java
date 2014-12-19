package dsn.scheduler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.sql.*;

public class DBInfo extends CalendarHelper{

	public String user_id, sp_id, sp_login_id, access_token, secret_token, currentDateTime, status, messagetimestamp, sm_sp_id;
	public DateFormat dateFormat;
	public Date datestamp;
	public Time timestamp;
	public CalendarHelper calendarHelper;
	public static String dbUrl = "jdbc:mysql://localhost:3306/dsndb";
	public static String dbClass = "com.mysql.jdbc.Driver";
	public Statement stmt = null;
	public ResultSet rs, rs2, rs3, rs4 = null;
	public Integer us = 0;
	public String userAccounts[][];
	public String thisUserAccount[];

	public static String queryAllAccounts, queryAccountSize, queryUniqueUsers, queryAllAccountsThisUser, queryAllAccountsThisUserSize;
	public static String querySPs = "SELECT COUNT(DISTINCT sp_id) FROM service_providers";

	public void main(String args[]) throws SQLException {

	}
	
	private String dbSafe(String str)
	{
		return str.replace("'", "''");
	}

	public UserTokens getAccounts(int intervalInMin) throws SQLException {
		int a, c, i, j, r, s = 0;
		int k = 0;
		int flag, flag2;
		String[] tokens;
		String[] secrets;
		intervalInMin = intervalInMin/60000;
		//System.out.println("intervalInMin:" +intervalInMin);
		calendarHelper = new CalendarHelper();
		
		queryAllAccounts = "SELECT * FROM accounts WHERE (date_add(last_updated, interval " +intervalInMin+ " MINUTE) < now()) ORDER BY user_id";
		queryAccountSize = "SELECT COUNT(*) FROM accounts";
		queryUniqueUsers = "SELECT COUNT(DISTINCT user_id) FROM accounts WHERE (date_add(last_updated, interval " +intervalInMin+ " MINUTE))";
		
		c = this.getSPSize(querySPs);
		a = this.getAccountSize(queryAccountSize);
		r = this.getUniqueUserSize(queryUniqueUsers);
		
		userAccounts = new String[r + 1][c + 2];
		tokens = new String[c+2];
		secrets = new String[c+2];
		
		for (i = 0; i < r; i++) {
			for (j = 0; j < c + 2; j++) {
				userAccounts[i][j] = "0";
			}
		}

		try {
			Class.forName(dbClass);
			Connection con = DriverManager.getConnection(dbUrl, "root", "");
			stmt = con.createStatement();
			rs = stmt.executeQuery(queryAllAccounts);

			System.out.println("total# accounts: " + a);
			System.out.println("total# unique users: " + r);
			System.out.println("total# SPs: " + c);

			while (rs.next()) {
				s = 0;
				i = 0;
				k = 0;
				flag = 0; // sp_login_id exists
				flag2 = 0; // user_id exists
				user_id = rs.getString(2);
				sp_id = rs.getString(3);
				sp_login_id = rs.getString(4);
				access_token = rs.getString(5);
				secret_token = rs.getString(6);
				timestamp = rs.getTime(7);

				System.out.println("user_id:" +user_id+ "-spid:" +sp_id+
						"-sp_login_id:" +sp_login_id+ "access_token:" +access_token+
						"-secret_token:" +secret_token+ "-timestamp:" +timestamp);

				if(sp_id.equals("fb")) { s = 1; } 
				else if(sp_id.equals("fl")) { s = 2; }
				else if(sp_id.equals("tw")) { s = 3; }
				else if(sp_id.equals("li") ){ s = 4; }
				
				for (i = 0; i < r; i++) {
					if (userAccounts[i][0].equals(user_id)) { // user exists
						//System.out.println("USER EXIST");
						for (j = 1; j < c + 1; j++) {
							//if (userAccounts[i][j].equals(sp_login_id)) { // user sp account exists
							if (userAccounts[i][j].equals("1")) { // user sp account exists	
								//System.out.println("USER AND SP EXIST");
								flag = 1;
								break;
							}
						}
						if (flag == 0) { // user exists but no sp
							//System.out.println("USER EXISTS but not SP");
							//userAccounts[i][s] = sp_login_id;
							userAccounts[i][s] = "1";
							tokens[i] = access_token;
							secrets[i] = secret_token;
							
							flag = 1;
							flag2 = 1;
							
							break;
						}
					}
				}
				
				if (flag == 0) {
					//System.out.println("USER AND SP NOT EXIST");
					for (i = 0; i < r; i++) {
						if (userAccounts[i][0].equals("0")) {
							userAccounts[i][0] = user_id;
							//userAccounts[i][s] = sp_login_id;
							userAccounts[i][s] = "1";
							tokens[i] = access_token;
							secrets[i] = secret_token;
							
							flag = 1;
							flag2 = 1;
							
							break;
						}
					}
				}
				k++;
			}

			con.close();
		} catch (ClassNotFoundException e) { e.printStackTrace();
		} catch (SQLException e) { e.printStackTrace(); }

		
		  //for(i = 0; i < r; i++) { for(j = 1; j < c+1; j++) {
		  //System.out.println("row:" + i + "-column:" + j);
		  //System.out.println("sp_login exits: " + userAccounts[i][j]); } }

		UserTokens userTokens = new UserTokens();
		userTokens.secrets = secrets;
		userTokens.tokens = tokens;
		userTokens.users = userAccounts;
		
		return userTokens;
		//return userAccounts;
	}

	public UserTokens getAccountsThisUser(String userId) throws SQLException {
		int a, c, i, r, u, s = 0;
		int k = 0;
		int flag;
		String[] tokens;
		String[] secrets;
		queryAllAccountsThisUserSize = "SELECT COUNT(*) FROM accounts WHERE user_id=" +userId;
		queryAllAccountsThisUser = "SELECT * FROM accounts WHERE user_id="+userId;
		
		c = this.getSPSize(querySPs);
		//r = this.getUniqueUserSize(queryUniqueUsers);
		//u = this.getAllAccountsThisUserSize(queryAllAccountsThisUserSize);
		
		thisUserAccount = new String[c+1];
		tokens = new String[c+1];
		secrets = new String[c+1];
		
		for (i = 0; i < c+1; i++) {
			thisUserAccount[i] = "0";
		}

		try {
			Class.forName(dbClass);
			Connection con = DriverManager.getConnection(dbUrl, "root", "");
			stmt = con.createStatement();
			rs4 = stmt.executeQuery(queryAllAccountsThisUser);

			while (rs4.next()) {
				k = 0;
				flag = 0; // sp_login_id exists
				user_id = rs4.getString(2);
				sp_id = rs4.getString(3);
				sp_login_id = rs4.getString(4);
				access_token = rs4.getString(5);
				secret_token = rs4.getString(6);
				datestamp = rs4.getDate(7);
				
				System.out.println("user_id:" +user_id+ "-spid:" +sp_id+
				"-sp_login_id:" +sp_login_id+ "-secret_token:" +secret_token+ "-datestamp:" +datestamp);
				
				if(sp_id.equals("fb")) { s = 1; } 
				else if(sp_id.equals("fl")) { s = 1; }
				else if(sp_id.equals("tw")) { s = 3; }
				else if(sp_id.equals("li") ){ s = 4; }
				
				if (thisUserAccount[0].equals(user_id)) { // user exists
					for (i = 1; i < c + 1; i++) {
						if (thisUserAccount[i].equals(sp_login_id)) { // sp exits
							flag = 1;
						}
					}
				}
				if (flag == 0) {
					thisUserAccount[0] = user_id;
					//thisUserAccount[s] = sp_login_id;
					thisUserAccount[s] = "1";
					tokens[s] = access_token;
					secrets[s] = secret_token;
					flag = 1;
				}
				k++;
			}

			con.close();
		} catch (ClassNotFoundException e) { e.printStackTrace(); 
		} catch (SQLException e) { e.printStackTrace(); }
		
		UserTokens userTokens = new UserTokens();
		userTokens.secrets = secrets;
		userTokens.tokens = tokens;
		userTokens.thisUserAccount = thisUserAccount;
		
		return userTokens;
		//return thisUserAccount;
	}
	
	public String[][] getStatusesThisUser(String user_id) {
		String queryStatusesThisUser = "SELECT * FROM status_messages WHERE user_id=" +user_id;
		// need to update query to order by time, and return x top messages
		
		int c = this.getMessagesThisUserSize(user_id);
		int i = 0;
		
		String[][] statusesThisUser = new String[5][c];
		
		try {
			Class.forName(dbClass);
			Connection con = DriverManager.getConnection(dbUrl, "root", "");
			stmt = con.createStatement();
			rs = stmt.executeQuery(queryStatusesThisUser);
			
			while (rs.next()) {
				statusesThisUser[0][i] = (user_id = rs.getString(2));
				statusesThisUser[1][i] = (sp_id = rs.getString(3));
				statusesThisUser[2][i] = (status = rs.getString(4));
				statusesThisUser[3][i] = (messagetimestamp = rs.getString(5));
				statusesThisUser[4][i] = (sm_sp_id = rs.getString(6));
				
				i++;
			}
			con.close();
		} catch (ClassNotFoundException e) { e.printStackTrace(); 
		} catch (SQLException e) { e.printStackTrace(); }
			        
		return statusesThisUser;
	}
	
	public Integer getMessagesThisUserSize(String user_id) {
		Integer u = null;
		String queryCountStatusesThisUser = "SELECT COUNT(*) FROM status_messages WHERE user_id=" +user_id;
		
		try {
			Class.forName(dbClass);
			Connection con = DriverManager.getConnection(dbUrl, "root", "");
			stmt = con.createStatement();
			rs = stmt.executeQuery(queryCountStatusesThisUser);

			while (rs.next()) { 
				u = Integer.parseInt(rs.getString(1)); }

			con.close();
		} catch (ClassNotFoundException e) { e.printStackTrace();
		} catch (SQLException e) { e.printStackTrace(); }

		return u;
	}
	
	public void checkUserAccountExists(String user_id, String sp_id,
			String sp_login_id, String token, String secret) {
	
		int flag_s = 0;
		int flag_u = 0;
		String queryUser = "SELECT * FROM accounts WHERE user_id=" +user_id;
		String queryUserSP = "SELECT * FROM accounts WHERE user_id=" +user_id+
			" AND sp_id='" +sp_id+ "'";

		try {
			Class.forName(dbClass);
			Connection con = DriverManager.getConnection(dbUrl, "root", "");
			stmt = con.createStatement();
			rs = stmt.executeQuery(queryUser);

			while (rs.next()) { flag_u = 1; }
			System.out.println("flag_u:" + flag_u);
			
			if(flag_u == 1) { // user_id exits
				System.out.println("USER exits");
				rs = stmt.executeQuery(queryUserSP);

				while (rs.next()) { flag_s = 1; }
				System.out.println("flag_s:" + flag_s);
				
				if(flag_s == 1) { // user_id and sp_id exits
					System.out.println("USER and SP exits");
					this.updateUserToken(user_id, sp_id, sp_login_id, token, secret);
				} else { // user_id exits but not sp_id. insert sp_id
					System.out.println("USER exits but SP doesnt");
					this.insertNewUserAccount(user_id, sp_id, sp_login_id, token, secret);
				}
			} else {
				System.out.println("USER and SP does not exits");
				this.insertNewUserAccount(user_id, sp_id, sp_login_id, token, secret);
			}
			con.close();
		} catch (ClassNotFoundException e) { e.printStackTrace();
		} catch (SQLException e) { e.printStackTrace();
		}
		
		/**
		 * if(user_id) exists {
		 * 	if(sp_id) exists {
		 * 		update sp_id etc info into DB
		 * 	else { insert token for sp }
		 * else { insert user, insert sp
		 * 
		 */
		
	}
	
	/*******************
	 * 
	 * GET COUNTS
	 * @throws ParseException 
	 * 
	 ********************/
	
	public UserStatusUpdates getLatestMessages(String last_index_call) throws ParseException {
		String[] users;
		String[] messages = null;
		String this_user, this_message = null;
		int u = this.getUpdatedUniqueUserSize(last_index_call);
		users = new String[u+2];
		messages = new String[u+2];
		
		String queryGetLatestMessages = "SELECT user_id, GROUP_CONCAT(status SEPARATOR '; ') AS messages FROM status_messages WHERE " +
			"timestamp > '" +last_index_call+ "' GROUP BY user_id";
		
        try {
			Class.forName(dbClass);
			Connection con = DriverManager.getConnection(dbUrl, "root", "");
			stmt = con.createStatement();
			rs = stmt.executeQuery(queryGetLatestMessages);
			int c = 0;
			
			while (rs.next()) {
				this_user = rs.getString(1);
				this_message = rs.getString(2);
				System.out.println(this_user+ "-" +this_message);
				users[c] = this_user; 
				messages[c] = this_message;
				c++;
			}
			/*
			for(int x = 0; x < users.length; x++) {
				System.out.println("USERS:" +users[x]);
			}
			for(int x = 0; x < messages.length; x++) {
				System.out.println("MESSAGES:" +messages[x]);
			}
			*/
			con.close();
		} catch (ClassNotFoundException e) { e.printStackTrace(); 
		} catch (SQLException e) { e.printStackTrace(); }

		UserStatusUpdates userStatusUpdates = new UserStatusUpdates();
		userStatusUpdates.users = users;
		userStatusUpdates.messages = messages;
		
		return userStatusUpdates;
	}
	
	public Integer getAccountSize(String queryAccountSize) {
		Integer a = null;

		try {
			Class.forName(dbClass);
			Connection con = DriverManager.getConnection(dbUrl, "root", "");
			stmt = con.createStatement();
			rs4 = stmt.executeQuery(queryAccountSize);

			while (rs4.next()) {
				a = Integer.parseInt(rs4.getString(1));
				
			}

			con.close();
		} catch (ClassNotFoundException e) { e.printStackTrace();
		} catch (SQLException e) { e.printStackTrace();
		}

		return a;
	}

	public Integer getUniqueUserSize(String queryUniqueUsers) {
		Integer s = null;

		try {
			Class.forName(dbClass);
			Connection con = DriverManager.getConnection(dbUrl, "root", "");
			stmt = con.createStatement();
			rs2 = stmt.executeQuery(queryUniqueUsers);

			while (rs2.next()) {
				s = Integer.parseInt(rs2.getString(1));
			}

			con.close();
		} catch (ClassNotFoundException e) { e.printStackTrace();
		} catch (SQLException e) { e.printStackTrace(); }

		return s;
	}
	
	public Integer getUpdatedUniqueUserSize(String last_index_call) {
		Integer s = null;

		String queryUpdatedUniqueUserSize = "SELECT COUNT(*) user_id, GROUP_CONCAT(status) FROM status_messages WHERE " +
		"timestamp > '" +last_index_call+ "' GROUP BY user_id";
		
		try {
			Class.forName(dbClass);
			Connection con = DriverManager.getConnection(dbUrl, "root", "");
			stmt = con.createStatement();
			rs2 = stmt.executeQuery(queryUpdatedUniqueUserSize);

			while (rs2.next()) {
				s = Integer.parseInt(rs2.getString(1));
			}

			con.close();
		} catch (ClassNotFoundException e) { e.printStackTrace();
		} catch (SQLException e) { e.printStackTrace(); }

		return s;
	}
	
	public Integer getThisUserMessageCount(String user_id) {
		Integer s = null;

		try {
			Class.forName(dbClass);
			Connection con = DriverManager.getConnection(dbUrl, "root", "");
			stmt = con.createStatement();
			rs2 = stmt.executeQuery(queryUniqueUsers);

			while (rs2.next()) {
				s = Integer.parseInt(rs2.getString(1));
			}

			con.close();
		} catch (ClassNotFoundException e) { e.printStackTrace();
		} catch (SQLException e) { e.printStackTrace(); }

		return s;
	}

	public Integer getSPSize(String querySPs) {
		Integer p = null;

		try {
			Class.forName(dbClass);
			Connection con = DriverManager.getConnection(dbUrl, "root", "");
			stmt = con.createStatement();
			rs3 = stmt.executeQuery(querySPs);

			while (rs3.next()) {
				p = Integer.parseInt(rs3.getString(1)); }

			con.close();
		} catch (ClassNotFoundException e) { e.printStackTrace();
		} catch (SQLException e) { e.printStackTrace(); }

		return p;
	}
	
	/*********************
	 * 
	 * UPDATES
	 * 
	 ********************/
	
	public String updateDateTime(String userId, int intervalInMin) {
		dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        datestamp = new Date();

        //intervalInMin = intervalInMin - 1;
        String queryUpdateDateTime = "UPDATE accounts SET last_updated='" +formatDateTime(datestamp)+ "' " +
		"WHERE (date_add(last_updated, interval " +intervalInMin+ " MINUTE) > now()) AND user_id='" +userId+ "'";

        try {
			Class.forName(dbClass);
			Connection con = DriverManager.getConnection(dbUrl, "root", "");
			stmt = con.createStatement();
			us = stmt.executeUpdate(queryUpdateDateTime);

			con.close();
		} catch (ClassNotFoundException e) { e.printStackTrace(); 
		} catch (SQLException e) { e.printStackTrace(); }
			
        return dateFormat.format(datestamp);
	}
	
	public String updateUserToken(String user_id, String sp_id, String sp_login_id, String token, String secret) {
		
        String queryUpdateDateTime = "UPDATE accounts SET access_token='" +token+ "', secret_token='"
        	+secret+ "', sp_login_id='" +sp_login_id+ "' WHERE user_id='" +user_id+ "' AND sp_id='" +sp_id+ "'";

        try {
			Class.forName(dbClass);
			Connection con = DriverManager.getConnection(dbUrl, "root", "");
			stmt = con.createStatement();
			us = stmt.executeUpdate(queryUpdateDateTime);

			con.close();
		} catch (ClassNotFoundException e) { e.printStackTrace(); 
		} catch (SQLException e) { e.printStackTrace(); }
			
        return dateFormat.format(datestamp);
	}

	
	/*********************
	 * 
	 * INSERT statements
	 * 
	 ********************/
	
	
	public String insertUserSP(String user_id, String sp_id, String sp_login_id,
			String token, String secret) {
		
        String queryInsertUserSP = "UPDATE accounts SET user_id='" +user_id+ "', sp_id='" +dbSafe(sp_id)+ 
        	"', sp_login_id='" +dbSafe(sp_login_id)+ "', access_token='" +dbSafe(token)+ "', secret_token='" 
        	+dbSafe(secret)+ "'";
        

        try {
			Class.forName(dbClass);
			Connection con = DriverManager.getConnection(dbUrl, "root", "");
			stmt = con.createStatement();
			us = stmt.executeUpdate(queryInsertUserSP);
			
			con.close();
		} catch (ClassNotFoundException e) { e.printStackTrace(); 
		} catch (SQLException e) { e.printStackTrace(); }
			
        return dateFormat.format(datestamp);
	}
	
	public void insertNewStatus(String userId, String spId, String status, String timestamp, String sm_sp_id) {
		Integer currentDateTime[] = this.getCurrentDateTime();
		
		String queryInsertNewStatus = "INSERT INTO status_messages SET user_id=" +userId+
			", sp_id='" + dbSafe(spId) + "', status='" + dbSafe(status) + "', sm_sp_id='" + dbSafe(sm_sp_id) + "', timestamp='" + dbSafe(timestamp)+ "'";
		
		try {
			Class.forName(dbClass);
			Connection con = DriverManager.getConnection(dbUrl, "root", "");
			stmt = con.createStatement();
			stmt.executeUpdate(queryInsertNewStatus);

			con.close();
		} catch (ClassNotFoundException e) { e.printStackTrace();
		} catch (SQLException e) { e.printStackTrace();
		}
	}
	
	public void insertNewUserAccount(String userId, String sp_id, String sp_login_id,
			String token, String secret) {
		Integer currentDateTime[] = this.getCurrentDateTime();
		// update to insert current timestamp
		
		String queryInsertNewUserAccount = "INSERT INTO accounts (user_id, sp_id, sp_login_id, " +
				"secret_token, access_token) VALUES("+userId+ ",'" +dbSafe(sp_id)+ "','" +dbSafe(sp_login_id)+
				"','" +dbSafe(secret)+ "','" +dbSafe(token)+ "')";
		
		try {
			Class.forName(dbClass);
			Connection con = DriverManager.getConnection(dbUrl, "root", "");
			stmt = con.createStatement();
			stmt.executeUpdate(queryInsertNewUserAccount);

			con.close();
		} catch (ClassNotFoundException e) { e.printStackTrace();
		} catch (SQLException e) { e.printStackTrace();
		}
	}
	
	public void insertAccessToken(String userId, String spId, String sp_login_id, String token, String secret) {
		Integer currentDateTime[] = this.getCurrentDateTime();
		
		String queryInsertAccessToken = "INSERT INTO accounts set user_id='" +userId+
			"', sp_id='" +spId+ "', sp_login_id='" +sp_login_id+ "', access_token='" +token+ "', secret_token='" +secret+
			"', last_updated='" +currentDateTime+ "'";
	
		try {
			Class.forName(dbClass);
			Connection con = DriverManager.getConnection(dbUrl, "root", "");
			stmt = con.createStatement();
			stmt.executeUpdate(queryInsertAccessToken);

			con.close();
		} catch (ClassNotFoundException e) { e.printStackTrace();
		} catch (SQLException e) { e.printStackTrace();
		}
	}
}