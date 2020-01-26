package Service;

import java.sql.SQLException;

import DataAccess.AuthTokenDAO;
import DataAccess.EventDAO;
import DataAccess.PersonDAO;
import DataAccess.UserDAO;
import Request.ClearRequest;
import Response.ClearResponse;

/**
 * ClearService class runs the task of clearing the database.
 * @author Jordan Jenkins
 */
public class ClearService
{
	public ClearService() {}

	/**
	 * Clears the database.
	 * @param r the ClearRequest object 
	 * @return ClearResponse object which will say whether clearing database was successful or a failure.
	 */
	public ClearResponse clear(ClearRequest r)
	{
		ClearResponse clearResponse = new ClearResponse();

		try
		{
			AuthTokenDAO tokenDAO = new AuthTokenDAO();
			UserDAO userDAO = new UserDAO();
			PersonDAO personDAO = new PersonDAO();
			EventDAO eventDAO = new EventDAO();

			tokenDAO.clear();
			userDAO.clear();
			personDAO.clear();
			eventDAO.clear();

			clearResponse.setMessage("Clear succeeded.");
		}
		catch (SQLException e)
		{
			clearResponse.setMessage(e.getMessage());
		}
		return clearResponse;
	}
}
