package Service;

import java.sql.*;

import DataAccess.AuthTokenDAO;
import Model.AuthToken;
import Model.Event;
import DataAccess.EventDAO;
import Request.EventRequest;
import Response.EventResponse;
/**
 * EventService class runs the task of finding an event in the database by its eventID.
 */
public class EventService
{
	public EventService() {}
	/**
	 * Finds event in database using eventID.
	 * @param r the EventRequest object, containing the data necessary to affect Event data in the database
	 * @return EventResponse object, which will report whether the event was found successfully, otherwise that the event was not found.
	 */
	public EventResponse runEventService(EventRequest r)
	{
		EventResponse response = new EventResponse();
		String authTokenStr = r.getAuthTokenStr();
		String eventID = r.getEventID();

		try
		{
			if (!eventID.isEmpty())
			{
				response = getEvent(authTokenStr, eventID);
			}
			else
			{
				response = getAllEvents(authTokenStr);
			}
		}
		catch (SQLException e)
		{
			response.setErrorMessage(e.getMessage());
		}
		return response;
	}
	private EventResponse getEvent(String authTokenStr, String eventID) throws SQLException
	{
		EventDAO eventDAO = new EventDAO();
		AuthTokenDAO tokenDAO = new AuthTokenDAO();
		Event event = null;
		EventResponse response = new EventResponse();

		AuthToken authToken = tokenDAO.getAuthToken(authTokenStr);
		if (authToken != null) {
			event = eventDAO.getEvent(authToken.getUserName(), eventID);
		}
		else {
			response.setErrorMessage("Authtoken not found, user not authorized.");
			return response;
		}

		if (event == null)
		{
			response.setErrorMessage(eventID + " not found in user database.");
			return response;
		}

		response.setEvent(event);

		return response;
	}
	private EventResponse getAllEvents(String authTokenStr) throws SQLException
	{
		EventResponse response = new EventResponse();

		AuthToken authToken = new AuthTokenDAO().getAuthToken(authTokenStr);

		if (authToken != null) {
			EventDAO eventDAO = new EventDAO();
			Event[] events = eventDAO.getAllEvents(authToken);
			response.setEvents(events);
		}
		else {
			response.setErrorMessage("Authtoken not found, user not authorized.");
		}

		return response;
	}
}
