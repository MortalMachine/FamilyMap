package Model;

import java.util.UUID;

/**
 * User class represents a user of the Family Map client.
 * @author Jordan Jenkins
 */
public class User {
    private String userName;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private String gender;
    private String personID;

    public User() {}
	public void setUserName(String newUname)
	{
		userName = newUname;
	}
	public void setPassword(String newPwd)
	{
		password = newPwd;
	}
	public void setEmail(String newEmail)
	{
		email = newEmail;
	}
	public void setFirstName(String newFname)
	{
		firstName = newFname;		
	}
	public void setLastName(String newLname)
	{
		lastName = newLname;
	}
	public void setGender(String newGen)
	{
		gender = newGen;
	}
	public void setPersonID()
	{
		personID = UUID.randomUUID().toString();
	}
	public void setPersonID(String newID) { personID = newID; }
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
	public String getPersonID()
	{
		return personID;
	}
}
