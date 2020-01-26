package DataAccess;

import java.sql.*;
import java.util.*;

import Model.AuthToken;
import Model.Person;
import Model.User;

/**
 * PersonDAO class runs the CRUD ops for Person objects in the database.
 * @author Jordan Jenkins
 */
public class PersonDAO extends Database
{
	public PersonDAO() {}
	/**
	 * Adds specific Person object to database.
	 */
	public void addPerson(Person person) throws SQLException
	{
		PreparedStatement stmt = null;
		try
		{
			openConnection();
			stmt = getConnection().prepareStatement("INSERT INTO PERSON (PERSON_ID, DESCENDANT, FIRST_NAME, LAST_NAME," +
					" GENDER, FATHER, MOTHER, SPOUSE) VALUES ( ?, ?, ?, ?, ?, ?, ?, ? );");
			stmt.setString(1, person.getPersonID());
			stmt.setString(2, person.getDescendant());
			stmt.setString(3, person.getFirstName());
			stmt.setString(4, person.getLastName());
			stmt.setString(5, person.getGender());
			stmt.setString(6, person.getFather());
			stmt.setString(7, person.getMother());
			stmt.setString(8, person.getSpouse());
			stmt.executeUpdate();

			stmt.close();
			closeConnection(true);
		}
		catch (SQLException e)
		{
			String exceptionStr = e.getMessage() + " : INSERT INTO PERSON failed";
			if (stmt != null) stmt.close();
			closeConnection(false);
			throw new SQLException(exceptionStr);
		}
	}
	/**
	 * Updates specific Person object in database with new Person object.
	 */
	public void updatePerson(Person person) throws SQLException
	{
		PreparedStatement stmt = null;

		try
		{
			openConnection();
			stmt = getConnection().prepareStatement("UPDATE PERSON SET DESCENDANT=?, FIRST_NAME=?, " +
					"LAST_NAME=?, GENDER=?, FATHER=?, MOTHER=?, SPOUSE=? WHERE PERSON_ID=?");
			stmt.setString(1, person.getDescendant());
			stmt.setString(2, person.getFirstName());
			stmt.setString(3, person.getLastName());
			stmt.setString(4, person.getGender());
			stmt.setString(5, person.getFather());
			stmt.setString(6, person.getMother());
			stmt.setString(7, person.getSpouse());
			stmt.setString(8, person.getPersonID());

			stmt.executeUpdate();

			stmt.close();
			closeConnection(true);
		}
		catch (SQLException e)
		{
			String exceptionStr = e.getMessage() + " : UPDATE PERSON failed";
			if (stmt != null) stmt.close();
			closeConnection(false);
			throw new SQLException(exceptionStr);
		}
	}
	/**
	 * Retrieves ALL Person objects from database in current user's family tree.
	 * @param token the authorization token for current user
	 */
	public Person[] getAllPeople(AuthToken token) throws SQLException
	{
		Set<Person> personSet = null;

		PreparedStatement stmt = null;
		ResultSet rs = null;

		try
		{
			openConnection();
			stmt = getConnection().prepareStatement("SELECT * FROM PERSON WHERE DESCENDANT=?");
			String userName = token.getUserName();
			stmt.setString(1, userName);
			rs = stmt.executeQuery();

			personSet = new HashSet<>();
			while (rs.next())
			{
				String personID = rs.getString("person_id");
				String descendant = rs.getString("descendant");
				String firstName = rs.getString("first_name");
				String lastName = rs.getString("last_name");
				String gender = rs.getString("gender");
				String father = rs.getString("father");
				String mother = rs.getString("mother");
				String spouse = rs.getString("spouse");

				Person person = new Person();
				person.setPersonID(personID);
				person.setDescendant(descendant);
				person.setFirstName(firstName);
				person.setLastName(lastName);
				person.setGender(gender);
				person.setFather(father);
				person.setMother(mother);
				person.setSpouse(spouse);

				personSet.add(person);
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
		Object[] objectArray = personSet.toArray();
		Person[] personArray = Arrays.copyOf(objectArray, objectArray.length, Person[].class);
		return personArray;
	}
	public Person getPerson(String userName, String personID) throws SQLException
	{
		Person person = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try
		{
			openConnection();
			stmt = getConnection().prepareStatement("SELECT * FROM PERSON WHERE PERSON_ID=? AND DESCENDANT=?");

			stmt.setString(1, personID);
			stmt.setString(2, userName);
			rs = stmt.executeQuery();
			while (rs.next())
			{
				String descendant = rs.getString("descendant");
				String firstName = rs.getString("first_name");
				String lastName = rs.getString("last_name");
				String gender = rs.getString("gender");
				String father = rs.getString("father");
				String mother = rs.getString("mother");
				String spouse = rs.getString("spouse");

				person = new Person();
				person.setPersonID(personID);
				person.setDescendant(descendant);
				person.setFirstName(firstName);
				person.setLastName(lastName);
				person.setGender(gender);
				person.setFather(father);
				person.setMother(mother);
				person.setSpouse(spouse);
			}
			rs.close();
			stmt.close();
			closeConnection(true);
		}
		catch (SQLException e)
		{
			String exceptionStr = e.getMessage() + " : SELECT PERSON failed";
			if (rs != null) rs.close();
			if (stmt != null) stmt.close();
			closeConnection(false);
			throw new SQLException(exceptionStr);
		}

		return person;
	}

	public void clear() throws SQLException
	{
		PreparedStatement stmt = null;

		try
		{
			openConnection();
			stmt = getConnection().prepareStatement("DELETE FROM PERSON");
			stmt.executeUpdate();
			stmt.close();
			closeConnection(true);
		}
		catch (SQLException e)
		{
			String exceptionStr = e.getMessage() + " : DELETE FROM PERSON failed";
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
			stmt = getConnection().prepareStatement("DELETE FROM PERSON WHERE DESCENDANT=?");
			stmt.setString(1, userName);
			stmt.executeUpdate();
			stmt.close();
			closeConnection(true);
		}
		catch (SQLException e)
		{
			String exceptionStr = e.getMessage() + " : DELETE USER'S PERSONS failed";
			if (stmt != null) stmt.close();
			closeConnection(false);
			throw new SQLException(exceptionStr);
		}
	}

}
