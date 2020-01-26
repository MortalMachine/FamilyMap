package Service;

import java.sql.SQLException;

import DataAccess.AuthTokenDAO;
import DataAccess.UserDAO;
import Model.AuthToken;
import Model.User;
import Request.LoginRequest;
import Response.LoginResponse;

/**
 * LoginService class runs the task of logging the user into their account.
 * @author Jordan Jenkins
 */
public class LoginService
{
	public LoginService() {}

	/**
	 * LoginResponse logs the user into their account.
	 * @param r LoginRequest object with username and password info
	 * @return LoginResponse object that reports whether the login was successful or a failure.
	 */
	public LoginResponse login(LoginRequest r)
	{
		LoginResponse response = new LoginResponse();
		/* Create User object, add to database through UserDAO class */
		String userName = r.getUserName();
		String password = r.getPassword();

		try
		{
			if (!userName.isEmpty() && !password.isEmpty())
			{
				User userToValidate = new User();
				userToValidate.setUserName(userName);
				userToValidate.setPassword(password);

				UserDAO userDAO = new UserDAO();
				boolean validated = userDAO.validateUserNameAndPassword(userToValidate);
				if (validated)
				{
					AuthToken token = new AuthToken();
					token.setUserName(userName);
					token.setAuthToken();

					AuthTokenDAO tokenDAO = new AuthTokenDAO();
					tokenDAO.addAuthToken(token);

					User user = userDAO.getUser(userName);
					response = new LoginResponse(token, user);
				}
				else
				{
					response.setErrorMessage("Username and/or password is/are incorrect.\n");
				}
			}
			else
			{
				response.setErrorMessage("Username and/or password fields are blank.\nPlease enter userName and password again.");
			}
		}
		catch (SQLException e)
		{
			response.setErrorMessage(e.getMessage());
		}
		return response;
	}
}
