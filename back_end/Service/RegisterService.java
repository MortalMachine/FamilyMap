package Service;

import java.sql.SQLException;

import DataAccess.AuthTokenDAO;
import Model.*;
import DataAccess.UserDAO;
import Request.FillRequest;
import Request.RegisterRequest;
import Response.RegisterResponse;

/**
 * RegisterService class runs the task of registering the user in the database.
 * @author Jordan Jenkins
 */
public class RegisterService
{
	public RegisterService() {};

	/**
	 * Creates new User object and registers the user in the database.
	 * @param r RegisterRequest object with username and password data
	 * @return RegisterResponse object that reports whether userName and password were successfully registered in the database or not.
	 */
	public RegisterResponse register(RegisterRequest r)
	{
		/* Uses username and password info inside r to register user into database */
		RegisterResponse response = new RegisterResponse();
		/* Create User object, add to database through UserDAO class */
		String userName = r.getUserName();
		String password = r.getPassword();
		String email = r.getEmail();
		String firstName = r.getFirstName();
		String lastName = r.getLastName();
		String gender = r.getGender();

		try
		{
			if (!userName.isEmpty() && !password.isEmpty() && !email.isEmpty() && !firstName.isEmpty() && !lastName.isEmpty() && !gender.isEmpty())
			{
				User user = new User();
				user.setUserName(userName);
				UserDAO userDAO = new UserDAO();
				AuthTokenDAO tokenDAO = new AuthTokenDAO();
				boolean validated = userDAO.validateUserName(user);
				if (!validated)
				{
					/* Create and add new user to database */
					user.setUserName(userName);
					user.setPassword(password);
					user.setEmail(email);
					user.setFirstName(firstName);
					user.setLastName(lastName);
					user.setGender(gender);
					user.setPersonID();

					AuthToken token = new AuthToken();
					token.setUserName(userName);
					token.setAuthToken();

					userDAO.addUser(user);
					tokenDAO.addAuthToken(token);
					response = new RegisterResponse(token, user);

					FillRequest fillRequest = new FillRequest();
					fillRequest.setUserName(userName);
					fillRequest.setGenerations(4 /* Default generation count */);
					FillService fillService = new FillService();
					fillService.fill(fillRequest);
				}
				else
				{
					response.setErrorMessage("Username is already in use.\n");
				}
			}
			else
			{
				response.setErrorMessage("At least one of the fields is blank.\nPlease try again.");
			}
		}
		catch (SQLException e)
		{
			response.setErrorMessage(e.getMessage());
		}
		return response;
	}
}
