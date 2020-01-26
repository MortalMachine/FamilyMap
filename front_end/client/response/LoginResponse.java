package client.response;

import client.model.AuthToken;
import client.model.User;

/**
 * LoginResponse class sends either a JSON object or a message to the server.
 * @author Jordan Jenkins
 */
public class LoginResponse
{
    private String authToken;
    private String userName;
    private String personID;
    private String errorMessage;

    public LoginResponse() {}
    /**
     * Constructs new LoginResponse object with the current user's User and AuthToken objects
     * @param a AuthToken object for current user
     * @param u User object for current user
     */
    public LoginResponse(AuthToken a, User u)
    {
        authToken = a.getAuthToken();
        userName = u.getUserName();
        personID = u.getPersonID();
    }
    public void setErrorMessage(String message)
    {
        errorMessage = message;
    }
    /**
     * Returns JSON if variables are not null, else an error message.
     */
    public String getMessage()
    {
        return errorMessage;
    }
    public String getAuthToken()
    {
        return authToken;
    }
    public String getUserName()
    {
        return userName;
    }
    public String getPersonID()
    {
        return personID;
    }
}
