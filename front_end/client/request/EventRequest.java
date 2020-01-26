package client.request;

/**
 * EventRequest contains request for single Event specified by eventID, or ALL events for ALL family members of current user.
 * @author Jordan Jenkins
 */
public class EventRequest
{
    private String eventID;
    private String authTokenStr;

    public EventRequest()
    {
        eventID = new String();
        authTokenStr = new String();
    }
    public void setEventID(String eventID)
    {
        this.eventID = eventID;
    }
    public void setAuthTokenStr(String authTokenStr) { this.authTokenStr = authTokenStr; }
    public String getEventID()
    {
        return eventID;
    }
    public String getAuthTokenStr() { return authTokenStr; }
}
