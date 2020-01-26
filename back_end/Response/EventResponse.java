package Response;

import Model.Event;

/**
* EventResponse class stores the message to be reported to the server whether the Event object was found or not, or if Event object is null, the success of returning ALL events for ALL family members, if successful or not.
* @author Jordan Jenkins
*/
public class EventResponse
{
	private Event event;
/*
	private String eventID;
	private String descendant;
	private String personID;
	private double latitude;
	private double longitude;
	private String country;
	private String city;
	private String eventType;
	private int year;
*/

	private Event[] events;
	private String errorMessage;

	public EventResponse() {}
	public void setEvent(Event event) { this.event = new Event(event); }
	public void setEventID(String eventID) { this.event.setEventID(eventID); }
	public void setDescendant(String descendant) { this.event.setDescendant(descendant); }
	public void setPersonID(String personID) { this.event.setPersonID(personID); }
	public void setLatitude(double latitude) { this.event.setLatitude(latitude); }
	public void setLongitude(double longitude) { this.event.setLongitude(longitude); }
	public void setCountry(String country) { this.event.setCountry(country); }
	public void setCity(String city) { this.event.setCity(city); }
	public void setEventType(String eventType) { this.event.setEventType(eventType); }
	public void setYear(int year) { this.event.setYear(year); }
	public void setEvents(Event[] events) { this.events = events; }
	public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }

	public String getEventID() { return this.event.getEventID(); }
	public String getDescendant() { return this.event.getDescendant(); }
	public String getPersonID() { return this.event.getPersonID(); }
	public double getLatitude() { return this.event.getLatitude(); }
	public double getLongitude() { return this.event.getLongitude(); }
	public String getCountry() { return this.event.getCountry(); }
	public String getCity() { return this.event.getCity(); }
	public String getEventType() { return this.event.getEventType(); }
	public int getYear() { return this.event.getYear(); }
	public Event[] getEvents() { return events; }
	public String getErrorMessage() { return errorMessage; }
}
