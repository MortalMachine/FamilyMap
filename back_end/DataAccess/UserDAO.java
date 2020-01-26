package DataAccess;

import java.sql.*;
import Model.AuthToken;
import Model.User;

/**
 * UserDAO class adds, retrieves, or deletes the User object associated with the current user in the database.
 * @author Jordan Jenkins
 */
public class UserDAO extends Database
{
	public UserDAO() {}
	/**
	 * Adds a User object for the current user in the database.
	 * @param user the User object to add to database
	 */
	public void addUser(User user) throws SQLException
	{
		PreparedStatement stmt = null;

		try
		{
			openConnection();
			stmt = getConnection().prepareStatement("INSERT INTO USER ( USER_NAME, PASSWORD, EMAIL_ADDRESS, FIRST_NAME, LAST_NAME, GENDER, PERSON_ID ) VALUES ( ?, ?, ?, ?, ?, ?, ? );");
			stmt.setString(1, user.getUserName());
			stmt.setString(2, user.getPassword());
			stmt.setString(3, user.getEmail());
			stmt.setString(4, user.getFirstName());
			stmt.setString(5, user.getLastName());
			stmt.setString(6, user.getGender());
			stmt.setString(7, user.getPersonID());
			/*String sql = "INSERT INTO USER ( USER_NAME, PASSWORD, EMAIL_ADDRESS, FIRST_NAME, LAST_NAME, GENDER, PERSON_ID ) VALUES ( '" +
				user.getUserName() + "', '" + user.getPassword() + "', '" + user.getEmail() + "', '" + user.getFirstName() + "', '" +
				user.getLastName() + "', '" + user.getGender() + "', '" + user.getPersonID() + "' );";*/
			stmt.executeUpdate();
			stmt.close();
			closeConnection(true);
		}
		catch (SQLException e)
		{
			String exceptionStr = e.getMessage() + " : INSERT INTO USER failed";
			if (stmt != null) stmt.close();
			closeConnection(false);
			throw new SQLException(exceptionStr);
		}
	}
	public User getUser(String userName) throws SQLException
	{
		//String userName = userToGet.getUserName();
		User user = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try
		{
			openConnection();
			stmt = getConnection().prepareStatement("SELECT * FROM USER WHERE USER_NAME=?");
			stmt.setString(1, userName);
			rs = stmt.executeQuery();

			if (rs.next())
			{
				String password = rs.getString("password");
				String email = rs.getString("email_address");
				String firstName = rs.getString("first_name");
				String lastName = rs.getString("last_name");
				String gender = rs.getString("gender");
				String personID = rs.getString("person_id");

				user = new User();

				user.setUserName(userName);
				user.setPassword(password);
				user.setEmail(email);
				user.setFirstName(firstName);
				user.setLastName(lastName);
				user.setGender(gender);
				user.setPersonID(personID);
			}

			rs.close();
			stmt.close();
			closeConnection(true);
		}
		catch (SQLException e)
		{
			String exceptionStr = e.getMessage() + " : SELECT * FROM USER failed";
			if (stmt != null) stmt.close();
			if (rs != null) rs.close();
			closeConnection(false);
			throw new SQLException(exceptionStr);
		}
		return user;
	}
	/**
	 * Deletes the current user's User object from database.
	 */
	public void clear() throws SQLException
	{
		Statement stmt = null;

		try
		{
			openConnection();

			stmt = getConnection().createStatement();
			stmt.executeUpdate("DELETE FROM USER");
			stmt.close();
			closeConnection(true);
		}
		catch (SQLException e)
		{
			String exceptionStr = e.getMessage() + " : DELETE FROM USER failed";
			if (stmt != null) stmt.close();
			closeConnection(false);
			throw new SQLException(exceptionStr);
		}
	}
	public boolean validateUserName(User user) throws SQLException {
		boolean validated = false;
		String userName = user.getUserName();
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try
		{
			openConnection();

			stmt = getConnection().prepareStatement("SELECT USER_NAME FROM USER WHERE USER_NAME=?;");
			stmt.setString(1, userName);
			rs = stmt.executeQuery();

			if (rs.next()) {
				String dbUsername = rs.getString("user_name");

				if (dbUsername.equals(userName)) {
					validated = true;
				}
			}

			rs.close();
			stmt.close();
			closeConnection(true);
		}
		catch (SQLException e)
		{
			String exceptionStr = e.getMessage() + " : VALIDATION failed";
			if (stmt != null) stmt.close();
			if (rs != null) rs.close();
			closeConnection(false);
			throw new SQLException(exceptionStr);
		}
		return validated;
	}
	public boolean validateUserNameAndPassword(User user) throws SQLException {
		boolean validated = false;
		String userName = user.getUserName();
		String password = user.getPassword();
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try
		{
			openConnection();

			stmt = getConnection().prepareStatement("SELECT USER_NAME, PASSWORD FROM USER WHERE USER_NAME=? AND PASSWORD=?;");
			stmt.setString(1, userName);
			stmt.setString(2, password);
			rs = stmt.executeQuery();

			if (rs.next()) {
				String dbUsername = rs.getString("user_name");
				String dbPassword = rs.getString("password");

				if (dbUsername.equals(userName) && dbPassword.equals(password)) {
					validated = true;
				}
			}

			rs.close();
			stmt.close();
			closeConnection(true);
		}
		catch (SQLException e)
		{
			String exceptionStr = e.getMessage() + " : VALIDATION failed";
			if (stmt != null) stmt.close();
			if (rs != null) rs.close();
			closeConnection(false);
			throw new SQLException(exceptionStr);
		}
		return validated;
	}
}
