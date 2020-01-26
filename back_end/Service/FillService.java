package Service;

import java.time.*;
import java.io.*;
import java.util.*;
import java.sql.*;
import com.google.gson.*;

import DataAccess.*;
import Model.*;
import Request.FillRequest;
import Response.FillResponse;

/**
 * FillService class runs the task of filling the database with persons and events for the Family Tree of the specified username.
 * @author Jordan Jenkins
 */
public class FillService
{
	private String userName;
	private int generations;
	private int numPersonsFilled;
	private int numEventsFilled;
	private String[] boyNames;
	private String[] girlNames;
	private String[] lastNames;
	private Location[] locations;

	public FillService()
	{
		userName = new String();
		generations = 4;
		numPersonsFilled = 0;
		numEventsFilled = 0;
		boyNames = parseJsonToStringArray(new File("json/mnames.json"));
		girlNames = parseJsonToStringArray(new File("json/fnames.json"));
		lastNames = parseJsonToStringArray(new File("json/snames.json"));
		locations = parseJsonToLocationArray(new File("json/locations.json"));
	}
	private void setUserName(String userName) { this.userName = userName; }
	private void setGenerations(int generations) { this.generations = generations; }
	private void incrementNumPersonsFilled() { ++numPersonsFilled; }
	private void incrementNumEventsFilled() { ++numEventsFilled; }
	public FillResponse fill(FillRequest r)
	{
		FillResponse fillResponse = new FillResponse();

		setUserName(r.getUserName());
		setGenerations(r.getGenerations());

		try
		{
			if (!foundInDatabase(userName))
			{
				fillResponse.setMessage("Username not found in database. No info filled into database.");
				return fillResponse;
			}

			clearPersonsAndEvents();
			fillPersonsAndEvents();

			String message = "Successfully added " + numPersonsFilled + " persons and " + numEventsFilled + " events to the database.";
			fillResponse.setMessage(message);
		}
		catch (SQLException e)
		{
			fillResponse.setMessage(e.getMessage());
		}
		return fillResponse;
	}
	private void fillPersonsAndEvents() throws SQLException
	{
		String male = "m";
		String female = "f";

		Person father = createPerson(male, boyNames, lastNames);
		Person mother = createPerson(female, girlNames, lastNames);
		father.setSpouse(mother.getPersonID());
		mother.setSpouse(father.getPersonID());

		fillEventsForPersons(father.getPersonID(), mother.getPersonID(), generations);

		//User userToGet = new User();
		//userToGet.setUserName(userName);
		try
		{
			UserDAO userDAO = new UserDAO();
			User user = userDAO.getUser(userName);

			Person userPerson = new Person();
			userPerson.setPersonID(user.getPersonID());
			userPerson.setDescendant(user.getUserName());
			userPerson.setFirstName(user.getFirstName());
			userPerson.setLastName(user.getLastName());
			userPerson.setGender(user.getGender());
			userPerson.setFather(father.getPersonID());
			userPerson.setMother(mother.getPersonID());

			PersonDAO personDAO = new PersonDAO();

			personDAO.addPerson(userPerson);
			incrementNumPersonsFilled();

			personDAO.addPerson(father);
			incrementNumPersonsFilled();

			personDAO.addPerson(mother);
			incrementNumPersonsFilled();

			Event userBirthEvent = createEvent(new Event(), userPerson.getPersonID(), "birth", 2001);
			new EventDAO().addEvent(userBirthEvent);
		}
		catch (SQLException e)
		{
			throw e;
		}

		if (generations - 1 > 0)
		{
			int genCounter = generations - 1;
			recursiveFiller(father, genCounter);
			recursiveFiller(mother, genCounter);
		}
	}

	private void recursiveFiller(Person child, int genCounter) throws SQLException
	{
		String male = "m";
		String female = "f";

		Person father = createPerson(male, boyNames, lastNames);
		Person mother = createPerson(female, girlNames, lastNames);
		father.setSpouse(mother.getPersonID());
		mother.setSpouse(father.getPersonID());
		child.setFather(father.getPersonID());
		child.setMother(mother.getPersonID());

		fillEventsForPersons(father.getPersonID(), mother.getPersonID(), genCounter);

		try
		{
			PersonDAO personDAO = new PersonDAO();

			personDAO.addPerson(father);
			incrementNumPersonsFilled();

			personDAO.addPerson(mother);
			incrementNumPersonsFilled();

			personDAO.updatePerson(child);
		}
		catch (SQLException e)
		{
			throw e;
		}

		if (genCounter - 1 > 0)
		{
			--genCounter;
			recursiveFiller(father, genCounter);
			recursiveFiller(mother, genCounter);
		}
	}
	private void fillEventsForPersons(String father, String mother, int genCounter) throws SQLException
	{
		int currYear = Year.now().getValue();
		int avgLifeSpan = 75;
		int avgMarriageAge = 20;
		int multiplier = generations + 1 - genCounter;

		int birthYear = currYear - (avgLifeSpan * multiplier);
		int marriageYear = birthYear + avgMarriageAge;
		int deathYear = birthYear + avgLifeSpan;

		Event fatherBirth = createEvent(new Event(), father, "birth", birthYear);
		Event fatherMarriage = createEvent(new Event(), father, "marriage", marriageYear);
		Event fatherDeath = createEvent(new Event(), father, "death", deathYear);

		Event motherBirth = createEvent(new Event(), mother, "birth", birthYear);
		Event motherMarriage = new Event(fatherMarriage);
		motherMarriage.setEventID();
		motherMarriage.setPersonID(mother);
		Event motherDeath = createEvent(new Event(), mother, "death", deathYear);

		try
		{
			EventDAO eventDAO = new EventDAO();

			eventDAO.addEvent(fatherBirth);
			incrementNumEventsFilled();

			eventDAO.addEvent(fatherMarriage);
			incrementNumEventsFilled();

			eventDAO.addEvent(fatherDeath);
			incrementNumEventsFilled();

			eventDAO.addEvent(motherBirth);
			incrementNumEventsFilled();

			eventDAO.addEvent(motherMarriage);
			incrementNumEventsFilled();

			eventDAO.addEvent(motherDeath);
			incrementNumEventsFilled();
		}
		catch (SQLException e)
		{
			throw e;
		}
	}
	private Event createEvent(Event event, String personID, String eventType, int year)
	{
		Location location = locations[new Random().nextInt(locations.length)];

		event.setDescendant(userName);
		event.setEventID();
		event.setPersonID(personID);
		event.setLatitude(location.getLatitude());
		event.setLongitude(location.getLongitude());
		event.setCountry(location.getCountry());
		event.setCity(location.getCity());
		event.setEventType(eventType);
		event.setYear(year);

		return event;
	}
	private Person createPerson(String gender, String[] firstNames, String[] lastNames)
	{
		Person ancestor = new Person();

		String firstName = firstNames[new Random().nextInt(firstNames.length)];
		String lastName = lastNames[new Random().nextInt(lastNames.length)];

		ancestor.setFirstName(firstName);
		ancestor.setLastName(lastName);
		ancestor.setDescendant(userName);
		ancestor.setGender(gender);
		ancestor.setPersonID();

		return ancestor;
	}
	private String[] parseJsonToStringArray(File file)
	{
		Set<String> nameSet	= new HashSet<>();
		try (FileReader fileReader = new FileReader(file))
		{
			JsonParser jsonParser = new JsonParser();
			JsonObject rootObj = (JsonObject)jsonParser.parse(fileReader);
			JsonArray namesArray = (JsonArray)rootObj.get("data");

			for (int i = 0; i < namesArray.size(); ++i)
			{
				JsonPrimitive nameNode = (JsonPrimitive)namesArray.get(i);
				String name = nameNode.getAsString();
				nameSet.add(name);
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return new String[0];
		}
		Object[] tmpArray = nameSet.toArray();
		String[] nameStrings = Arrays.copyOf(tmpArray, tmpArray.length, String[].class);
		return nameStrings;
	}
	private Location[] parseJsonToLocationArray(File file)
	{
		Set<Location> locationSet	= new HashSet<>();
		try (FileReader fileReader = new FileReader(file))
		{
			JsonParser jsonParser = new JsonParser();
			JsonObject rootObj = (JsonObject)jsonParser.parse(fileReader);
			JsonArray namesArray = (JsonArray)rootObj.get("data");

			for (int i = 0; i < namesArray.size(); ++i)
			{
				JsonObject locationNode = (JsonObject)namesArray.get(i);
				JsonPrimitive countryNode = (JsonPrimitive)locationNode.get("country");
				JsonPrimitive cityNode = (JsonPrimitive)locationNode.get("city");
				JsonPrimitive latNode = (JsonPrimitive)locationNode.get("latitude");
				JsonPrimitive longNode = (JsonPrimitive)locationNode.get("longitude");

				Location location = new Location();
				location.setCountry(countryNode.getAsString());
				location.setCity(cityNode.getAsString());
				location.setLatitude(latNode.getAsDouble());
				location.setLongitude(longNode.getAsDouble());
				locationSet.add(location);
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return new Location[0];
		}
		Object[] tmpArray = locationSet.toArray();
		Location[] locations = Arrays.copyOf(tmpArray, tmpArray.length, Location[].class);
		return locations;
	}
	private boolean foundInDatabase(String userName) throws SQLException
	{
		boolean found = false;

		UserDAO userDAO = new UserDAO();
		try
		{
			//User userToGet = new User();
			//userToGet.setUserName(userName);
			User user = userDAO.getUser(userName);
			if (user != null)
			{
				found = true;
			}
		}
		catch (SQLException e)
		{
			throw e;
		}

		return found;
	}
	private void clearPersonsAndEvents() throws SQLException
	{
		try
		{
			PersonDAO personDAO = new PersonDAO();
			EventDAO eventDAO = new EventDAO();
			User user = new User();
			user.setUserName(userName);
			personDAO.clear(user);
			eventDAO.clear(user);
		}
		catch (SQLException e)
		{
			throw e;
		}
	}
	public class Location
	{
		private String country;
		private String city;
		private double latitude;
		private double longitude;

		public Location () {}
		public void setCountry(String country) { this.country = country; }
		public void setCity(String city) { this.city = city; }
		public void setLatitude(double latitude) { this.latitude = latitude; }
		public void setLongitude(double longitude) { this.longitude = longitude; }
		public String getCountry() { return country; }
		public String getCity() { return city; }
		public double getLatitude() { return latitude; }
		public double getLongitude() { return longitude; }
	}
}
