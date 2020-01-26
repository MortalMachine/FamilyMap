package client.request;

/**
 * Contains registration data to pass along to RegisterService class.
 * @author Jordan Jenkins
 */
public class RegisterRequest
{
    private String userName;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private String gender;

    public RegisterRequest() {}
    /**
     * @param u Username entered by current user
     * @param p Password entered by current user
     */
    public RegisterRequest(String u, String p, String e, String f, String l, String g)
    {
        userName = new String(u);
        password = new String(p);
        email = new String(e);
        firstName = new String(f);
        lastName = new String (l);
        gender = g;
    }
    public void setUserName(String userName) { this.userName = userName; }
    public void setPassword(String password) { this.password = password; }
    public void setEmail(String email) { this.email = email; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setGender(String gender) { this.gender = gender; }
    public String getUserName()
    {
        return userName;
    }
    public String getPassword()
    {
        return password;
    }
    public String getEmail()
    {
        return email;
    }
    public String getFirstName()
    {
        return firstName;
    }
    public String getLastName()
    {
        return lastName;
    }
    public String getGender()
    {
        return gender;
    }
}
