package Service;

import java.sql.*;

import DataAccess.AuthTokenDAO;
import DataAccess.PersonDAO;
import Model.AuthToken;
import Model.Person;
import Request.PersonRequest;
import Response.PersonResponse;

/**
 * PersonService class runs the task of finding a person object by its personID.
 * @author Jordan Jenkins
 */
public class PersonService
{
	public PersonService() {}

	/**
	 * Checks if personID is in the database.
	 * @param r PersonRequest object containing personID 
	 * @return PersonResponse object that reports whether the person was found in the database or not.
	 */
	public PersonResponse runPersonService(PersonRequest r)
	{
		PersonResponse response = new PersonResponse();
		String authTokenStr = r.getAuthTokenStr();
		String personID = r.getPersonID();

		try
		{
			if (!personID.isEmpty())
			{
				response = getPerson(authTokenStr, personID);
			}
			else
			{
				response = getAllPeople(authTokenStr);
			}
		}
		catch (SQLException e)
		{
			response.setErrorMessage(e.getMessage());
		}
		return response;
	}
	private PersonResponse getPerson(String authTokenStr, String personID) throws SQLException
	{
		PersonDAO personDAO = new PersonDAO();
		AuthTokenDAO tokenDAO = new AuthTokenDAO();
		Person person = null;
		PersonResponse response = new PersonResponse();

		AuthToken authToken = tokenDAO.getAuthToken(authTokenStr);
		if (authToken != null) {
			person = personDAO.getPerson(authToken.getUserName(), personID);
		}
		else {
			response.setErrorMessage("Authtoken not found, user not authorized.");
			return response;
		}

		if (person == null)
		{
			response.setErrorMessage(personID + " not found in user database.");
			return response;
		}

		response.setPersonID(person.getPersonID());
		response.setFirstName(person.getFirstName());
		response.setLastName(person.getLastName());
		response.setGender(person.getGender());
		response.setFather(person.getFather());
		response.setMother(person.getMother());
		response.setDescendant(person.getDescendant());
		response.setSpouse(person.getSpouse());

		return response;
	}
	private PersonResponse getAllPeople(String authTokenStr) throws SQLException
	{
		PersonResponse response = new PersonResponse();

		AuthToken authToken = new AuthTokenDAO().getAuthToken(authTokenStr);

		if (authToken != null) {
			PersonDAO personDAO = new PersonDAO();
			Person[] persons = personDAO.getAllPeople(authToken);

			response.setPersons(persons);
		}
		else {
			response.setErrorMessage("Authtoken not found, user not authorized.");
		}

		return response;
	}
}
