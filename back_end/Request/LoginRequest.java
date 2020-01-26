package Request;

/**
 * LoginRequest class holds the userName and password that the current user is attempting to log in with, for the purpose of carrying this info to the LoginService.
 * @author Jordan Jenkins
 */
public class LoginRequest
{
	/**
	 * The userName the current user wants to log in with.
	 */
	private String userName;
	/**
	 * The password the current user wants to log in with.
	 */
	private String password;
	public LoginRequest()
	{
		userName = null;
		password = null;
	}
	public LoginRequest(String u, String p)
	{
		setUserName(u);
		setPassword(p);
	}
	public void setUserName(String userName) { this.userName = userName; }
	public void setPassword(String password) { this.password = password; }
	public String getUserName()
	{
		return userName;
	}
	public String getPassword()
	{
		return password;
	}
}
