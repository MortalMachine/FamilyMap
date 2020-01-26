package Response;

import Model.AuthToken;
import Model.User;

/**
 * RegisterResponse class returns its member variables through GSON on successful response, and an error message on failed response.
 * @author Jordan Jenkins
 */
public class RegisterResponse
{
	private String authToken;
	private String username;
	private String personID;
	private String errorMessage;

	public RegisterResponse() {}
	/**
	 * Constructs RegisterResponse object
	 * @param tokenObj has the authorization token string
	 * @param userObj has the user's username and user's person ID strings
	 */
	public RegisterResponse(AuthToken tokenObj, User userObj)
	{
		authToken = tokenObj.getAuthToken();
		username = userObj.getUserName();
		personID = userObj.getPersonID();
	}
	public void setErrorMessage(String message)
	{
		errorMessage = message;
	}
	public String getErrorMessage() {return errorMessage; }
}
