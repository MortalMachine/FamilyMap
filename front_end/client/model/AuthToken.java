package client.model;

import java.util.UUID;

/**
 * Represents an authorization token.
 * @author Jordan Jenkins
 */
public class AuthToken {
    /**
     * authToken is the unique identifier data called the authorization token, used in tandem with a user account for account security purposes.
     */
    private String authToken;
    private String userName;

    public AuthToken() {}
    /**
     * Initializes authToken with a randomly-generated
     * alphanumeric string returned from the RandomAlphanumeric class.
     */
    public void setAuthToken()
    {
        authToken = UUID.randomUUID().toString();
    }
    public void setAuthToken(String existingToken) { authToken = existingToken; }
    public void setUserName(String userName) { this.userName = userName; }
    public String getAuthToken()
    {
        return authToken;
    }
    public String getUserName() { return userName; }
}
