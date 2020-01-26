package Response;

/**
* ClearResponse class contains the response message to report to the server whether the database was cleared successfully or not.
* @author Jordan Jenkins
*/
public class ClearResponse
{
	private String message;

	public ClearResponse() {}

	public void setMessage(String errorMessage)
	{
		this.message = errorMessage;
	}

	public String getMessage() { return message; }
}
