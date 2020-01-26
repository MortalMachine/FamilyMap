package Response;

/**
 * LoadResponse class contains message to be given to server to report whether the info was successfully loaded to the database or not.
 * @author Jordan Jenkins
 */
public class LoadResponse
{
	private String message;

	public LoadResponse() {}
	/**
	 * Returns JSON object for success of loadRequest
	 */
	public void setMessage(String message)
	{
		this.message = message;
	}
	public String getMessage() { return message; }
}
