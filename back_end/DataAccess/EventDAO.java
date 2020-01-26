package DataAccess;

import java.sql.*;
import java.util.*;

import Model.AuthToken;
import Model.Event;
import Model.User;

/**
 * EventDAO class accesses the database to add, read, update, or delete an Event object.
 * @author Jordan Jenkins
 */
public class EventDAO extends Database
{
	public EventDAO() {}
	/**
	 * Adds addE to database.
	 * @param event The Event object to add
	 */
	public void addEvent(Event event) throws SQLException
	{
		PreparedStatement stmt = null;

		try
		{
			openConnection();
			stmt = getConnection().prepareStatement("INSERT INTO EVENT (EVENT_ID, DESCENDANT, PERSON_ID, LATITUDE," +
					" LONGITUDE, COUNTRY, CITY, EVENT_TYPE, YEAR) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ? );");
			stmt.setString(1, event.getEventID());
			stmt.setString(2, event.getDescendant());
			stmt.setString(3, event.getPersonID());
			stmt.setDouble(4, event.getLatitude());
			stmt.setDouble(5, event.getLongitude());
			stmt.setString(6, event.getCountry());
			stmt.setString(7, event.getCity());
			stmt.setString(8, event.getEventType());
			stmt.setInt(9, event.getYear());
			stmt.executeUpdate();

			stmt.close();
			closeConnection(true);
		}
		catch (SQLException e)
		{
			String exceptionStr = e.getMessage() + " : INSERT INTO EVENT failed";
			if (stmt != null) stmt.close();
			closeConnection(false);
			throw new SQLException(exceptionStr);
		}
	}
	public Event[] getAllEvents(AuthToken token) throws SQLException
	{
		Set<Event> eventSet = null;

		PreparedStatement stmt = null;
		ResultSet rs = null;
		try
		{
			openConnection();
			stmt = getConnection().prepareStatement("SELECT * FROM EVENT WHERE EVENT.DESCENDANT=?");

			String userName = token.getUserName();
			stmt.setString(1, userName);

			rs = stmt.executeQuery();

			eventSet = new HashSet<>();
			while (rs.next())
			{
				String eventID = rs.getString("event_id");
				String descendant = rs.getString("descendant");
				String personID = rs.getString("person_id");
				double latitude = rs.getDouble("latitude");
				double longitude = rs.getDouble("longitude");
				String country = rs.getString("country");
				String city = rs.getString("city");
				String eventType = rs.getString("event_type");
				int year = rs.getInt("year");

				Event event = new Event();
				event.setEventID(eventID);
				event.setDescendant(descendant);
				event.setPersonID(personID);
				event.setLatitude(latitude);
				event.setLongitude(longitude);
				event.setCountry(country);
				event.setCity(city);
				event.setEventType(eventType);
				event.setYear(year);

				eventSet.add(event);
			}
			rs.close();
			stmt.close();
			closeConnection(true);
		}
		catch (SQLException e)
		{
			String exceptionStr = e.getMessage() + " : SELECT ALL EVENTS failed";
			if (stmt != null) stmt.close();
			if (rs != null) rs.close();
			closeConnection(false);
			throw new SQLException(exceptionStr);
		}
		Object[] objectArray = eventSet.toArray();
		Event[] eventArray = Arrays.copyOf(objectArray, objectArray.length, Event[].class);
		return eventArray;
	}
	public Event getEvent(String userName, String eventID) throws SQLException
	{
		Event event = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try
		{
			openConnection();
			stmt = getConnection().prepareStatement("SELECT * FROM EVENT WHERE EVENT_ID=? AND DESCENDANT=?");

			stmt.setString(1, eventID);
			stmt.setString(2, userName);
			rs = stmt.executeQuery();
			while (rs.next())
			{
				String descendant = rs.getString("descendant");
				String personID = rs.getString("person_id");
				double latitude = rs.getDouble("latitude");
				double longitude = rs.getDouble("longitude");
				String country = rs.getString("country");
				String city = rs.getString("city");
				String eventType = rs.getString("event_type");
				int year = rs.getInt("year");

				event = new Event();
				event.setEventID(eventID);
				event.setDescendant(descendant);
				event.setPersonID(personID);
				event.setLatitude(latitude);
				event.setLongitude(longitude);
				event.setCountry(country);
				event.setCity(city);
				event.setEventType(eventType);
				event.setYear(year);
			}
			rs.close();
			stmt.close();
			closeConnection(true);
		}
		catch (SQLException e)
		{
			String exceptionStr = e.getMessage() + " : SELECT EVENT failed";
			if (stmt != null) stmt.close();
			if (rs != null) rs.close();
			closeConnection(false);
			throw new SQLException(exceptionStr);
		}
		return event;
	}
	public void clear() throws SQLException
	{
		PreparedStatement stmt = null;

		try
		{
			openConnection();
			stmt = getConnection().prepareStatement("DELETE FROM EVENT");
			stmt.executeUpdate();
			stmt.close();
			closeConnection(true);
		}
		catch (SQLException e)
		{
			String exceptionStr = e.getMessage() + " : DELETE FROM EVENT failed";
			if (stmt != null) stmt.close();
			closeConnection(false);
			throw new SQLException(exceptionStr);
		}
	}
	public void clear(User user) throws SQLException
	{
		String userName = user.getUserName();
		PreparedStatement stmt = null;

		try
		{
			openConnection();
			stmt = getConnection().prepareStatement("DELETE FROM EVENT WHERE DESCENDANT=?");
			stmt.setString(1, userName);
			stmt.executeUpdate();
			stmt.close();
			closeConnection(true);
		}
		catch (SQLException e)
		{
			String exceptionStr = e.getMessage() + " : DELETE USER'S EVENTS failed";
			if (stmt != null) stmt.close();
			closeConnection(false);
			throw new SQLException(exceptionStr);
		}
	}

}
