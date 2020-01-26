package Request;

import Model.User;
import Model.Person;
import Model.Event;

public class LoadRequest
{
	private User[] users;
	private Person[] persons;
	private Event[] events;

	public LoadRequest() {}
	public void setUsers(User[] u)
	{
		users = new User[u.length];
		users = u;
	}
	public void setPersons(Person[] p)
	{
		persons = new Person[p.length];
		persons = p;
	}
	public void setEvents(Event[] e)
	{
		events = new Event[e.length];
		events = e;
	}
	public User[] getUsers()
	{
		return users;
	}
	public Person[] getPersons()
	{
		return persons;
	}
	public Event[] getEvents()
	{
		return events;
	}
}
