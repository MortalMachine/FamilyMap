package client.model;

import java.util.UUID;

/**
 * Person class represents a person in a Family Tree.
 * @author Jordan Jenkins
 */
public class Person {
    private String personID;
    private String descendant;
    private String firstName;
    private String lastName;
    private String gender;
    private String father;
    private String mother;
    private String spouse;

    public Person()
    {
        father = null;
        mother = null;
        spouse = null;
    }
    public Person(Person person)
    {
        this.personID = new String(person.getPersonID());
        this.descendant = new String(person.getDescendant());
        this.firstName = new String(person.getFirstName());
        this.lastName = new String(person.getLastName());
        this.gender = new String(person.getGender());
        if (person.getFather() != null) this.father = new String(person.getFather());
        if (person.getMother() != null) this.mother = new String(person.getMother());
        if (person.getSpouse() != null) this.spouse = new String(person.getSpouse());
    }
    public void setPersonID() { personID = UUID.randomUUID().toString(); }
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
}
