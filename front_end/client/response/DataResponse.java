package client.response;

import client.model.Event;
import client.model.Person;
import client.model.User;

public class DataResponse
{
    private Person[] persons;
    private Event[] events;
    private String errorMessage;

    public DataResponse()
    {
        errorMessage = new String();
    }
    public void setPersons(Person[] persons) { this.persons = persons; }
    public void setEvents(Event[] events) { this.events = events; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }

    public Person[] getPersons() { return persons; }
    public Event[] getEvents() { return events; }
    public String getErrorMessage() { return errorMessage; }
}
