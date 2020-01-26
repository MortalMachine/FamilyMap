package client.request;

/**
 * PersonRequest is used to request a specific Person by their personID variable, or ALL family members of the current user.
 * @author Jordan Jenkins
 */
public class PersonRequest
{
    private String personID;
    private String authTokenStr;

    public PersonRequest()
    {
        personID = new String();
        authTokenStr = new String();
    }
    public void setPersonID(String personID)
    {
        this.personID = personID;
    }
    public void setAuthTokenStr(String authTokenStr) { this.authTokenStr = authTokenStr; }
    public String getPersonID()
    {
        return personID;
    }
    public String getAuthTokenStr() { return authTokenStr; }
}
