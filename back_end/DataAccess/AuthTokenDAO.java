package DataAccess;

import java.sql.*;

import Model.AuthToken;
import Model.User;

/**
 * AuthTokenDAO class runs SQL commands to add, read, or delete an AuthToken object from the database for a specific user account.
 * @author Jordan Jenkins
 */
public class AuthTokenDAO extends Database
{
	/**
	 * Adds the AuthToken object addT to the database
	 * @param auth the AuthToken object to add to the database
	 */
	public void addAuthToken(AuthToken auth) throws SQLException
	{
		PreparedStatement stmt = null;

		try
		{
			openConnection();
			stmt = getConnection().prepareStatement("INSERT INTO AUTH_TOKEN ( USER_NAME, AUTH_TOKEN ) VALUES ( ?, ? )");
			stmt.setString(1, auth.getUserName());
			stmt.setString(2, auth.getAuthToken());
			stmt.executeUpdate();
			stmt.close();
			closeConnection(true);
		}
		catch (SQLException e)
		{
			String exceptionStr = e.getMessage() + " : INSERT INTO AUTH_TOKEN failed";
			if (stmt != null) stmt.close();
			closeConnection(false);
			throw new SQLException(exceptionStr);
		}
	}
	/**
	 * Retrieves the AuthToken object associated with the current user from the database.
	 * @param token the authtoken
	 */
	public AuthToken getAuthToken(String token) throws SQLException
	{
		AuthToken authToken = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try
		{
			openConnection();
			stmt = getConnection().prepareStatement("SELECT * FROM AUTH_TOKEN WHERE AUTH_TOKEN=?");
			stmt.setString(1, token);
			rs = stmt.executeQuery();

			if (rs.next())
			{
				String userName = rs.getString("user_name");
				String tokenStr = rs.getString("auth_token");

				authToken = new AuthToken();
				authToken.setUserName(userName);
				authToken.setAuthToken(tokenStr);
			}

			rs.close();
			stmt.close();
			closeConnection(true);
		}
		catch (SQLException e)
		{
			String exceptionStr = e.getMessage() + " : SELECT * FROM AUTH_TOKEN failed";
			if (stmt != null) stmt.close();
			if (rs != null) rs.close();
			closeConnection(false);
			throw new SQLException(exceptionStr);
		}
		return authToken;
	}
	/**
	 * Deletes the AuthToken associated with the current user from the database.
	 */
	public void clear() throws SQLException
	{
		Statement stmt = null;

		try
		{
			openConnection();
			stmt = getConnection().createStatement();
			stmt.executeUpdate("DELETE FROM AUTH_TOKEN");
			stmt.close();
			closeConnection(true);
		}
		catch (SQLException e)
		{
			String exceptionStr = e.getMessage() + " : DELETE FROM AUTH_TOKEN failed";
			if (stmt != null) stmt.close();
			closeConnection(false);
			throw new SQLException(exceptionStr);
		}
	}
}
