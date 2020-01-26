package client.response;

/**
 * FillResponse class holds message to report to server whether tables belonging to user were successfully cleared and filled, or if the request failed.
 * @author Jordan Jenkins
 */
public class FillResponse
{
    private String message;

    public FillResponse() {}
    public void setMessage(String message)
    {
        this.message = message;
    }
    public String getMessage() { return message; }
}
