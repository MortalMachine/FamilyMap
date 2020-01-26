package Response;

import Model.Person;

/**
 * PersonResponse class contains the message to report to the server whether the Person object was found or not, or if ALL family members of the user were returned or not.
 * @author Jordan Jenkins
 */
public class PersonResponse
{
	private String personID;
	private String descendant;
	private String firstName;
	private String lastName;
	private String gender;
	private String father;
	private String mother;
	private String spouse;

	private Person[] persons;
	private String errorMessage;

	public PersonResponse()
	{
		father = null;
		mother = null;
		spouse = null;
	}
	public void setPersonID(String newPersonID) { personID = newPersonID; }
	public void setDescendant(String newName)
	{
		descendant = newName;
	}
	public void setFirstName(String newName)
	{
		firstName = newName;
	}
	public void setLastName(String newName)
	{
		lastName = newName;
	}
	public void setGender(String newGender)
	{
		gender = newGender;
	}
	public void setFather(String newID)
	{
		father = newID;
	}
	public void setMother(String newID)
	{
		mother = newID;
	}
	public void setSpouse(String newID)
	{
		spouse = newID;
	}
	public void setPersons(Person[] persons) { this.persons = persons; }
	public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }

	public String getPersonID()	{ return personID; }
	public String getDescendant() {	return descendant; }
	public String getFirstName()
	{
		return firstName;
	}
	public String getLastName()
	{
		return lastName;
	}
	public String getGender()
	{
		return gender;
	}
	public String getFather()
	{
		return father;
	}
	public String getMother()
	{
		return mother;
	}
	public String getSpouse() { return spouse; }
	public Person[] getPersons() { return persons; }
	public String getErrorMessage() { return errorMessage; }
}
