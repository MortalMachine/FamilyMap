package Service;

import java.sql.*;

import DataAccess.*;
import Model.*;
import Request.ClearRequest;
import Request.LoadRequest;
import Response.LoadResponse;

/**
 * LoadService class runs the task of loading the posted user, person, and event data into the database.
 * @author Jordan Jenkins
 */
public class LoadService
{
	public LoadService() {}

	/**
	 * LoadResponse loads the posted user, person, and event data into the database.
	 * @param r LoadRequest object with info to load into database
	 * @return LoadResponse object that reports whether loading was successful or a failure.
	 */
	public LoadResponse load(LoadRequest r)
	{
		/* Loads info from r into database */
		LoadResponse loadResponse = new LoadResponse();

		User[] users = r.getUsers();
		Person[] persons = r.getPersons();
		Event[] events = r.getEvents();

		try
		{
			if (users.length != 0 && persons.length != 0 && events.length != 0)
			{
				ClearRequest clearRequest = new ClearRequest();
				ClearService clearService = new ClearService();
				clearService.clear(clearRequest);

				AuthTokenDAO tokenDAO = new AuthTokenDAO();
				UserDAO userDAO = new UserDAO();
				PersonDAO personDAO = new PersonDAO();
				EventDAO eventDAO = new EventDAO();

				for (User user : users)
				{
					userDAO.addUser(user);
				}
				for (Person person : persons)
				{
					personDAO.addPerson(person);
				}
				for (Event event : events)
				{
					eventDAO.addEvent(event);
				}

				String message = "Successfully added " + users.length + " users, "+ persons.length + " persons, and " + events.length + " events to the database.";
				loadResponse.setMessage(message);
			}
			else
			{
				loadResponse.setMessage("Missing values. No info loaded into database.");
			}
		}
		catch (SQLException e)
		{
			loadResponse.setMessage(e.getMessage());
		}
		return loadResponse;
	}
}
