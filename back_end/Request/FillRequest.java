package Request;

/**
 * Contains data needed for filling database.
 * @author Jordan Jenkins
 */
public class FillRequest
{
	private String userName;
	private int generations;

	public FillRequest() {}
	public void setUserName(String userName) { this.userName = userName; }
	public void setGenerations(int generations) { this.generations = generations; }
	public String getUserName()
	{
		return userName;
	}
	public int getGenerations()
	{
		return generations;
	}
}
