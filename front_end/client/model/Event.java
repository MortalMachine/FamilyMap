package client.model;

import android.support.annotation.NonNull;

import java.util.UUID;

/**
 * Event class represents an event in the life of a person.
 * @author Jordan Jenkins
 */
public class Event implements Comparable<Event>/*, ParentObject*/
{
    private String eventID;
    private String descendant;
    private String personID;
    private double latitude;
    private double longitude;
    private String country;
    private String city;
    private String eventType;
    private int year;

    //private List<Object> childrenList;

    public Event() {}
    public Event(Event event)
    {
        this.setEventID(event.getEventID());
        this.setDescendant(event.getDescendant());
        this.setPersonID(event.getPersonID());
        this.setLatitude(event.getLatitude());
        this.setLongitude(event.getLongitude());
        this.setCountry(event.getCountry());
        this.setCity(event.getCity());
        this.setEventType(event.getEventType());
        this.setYear(event.getYear());

        //this.childrenList = new ArrayList<>();
    }
    public void setEventID()
    {
        eventID = UUID.randomUUID().toString();
    }
    public void setEventID(String newEventID) { eventID = newEventID; }
    public void setDescendant(String newName)
    {
        descendant = newName;
    }
    public void setPersonID(String newID)
    {
        personID = newID;
    }
    public void setLatitude(double newLatitude)
    {
        latitude = newLatitude;
    }
    public void setLongitude(double newLongitude)
    {
        longitude = newLongitude;
    }
    public void setCountry(String newCountry)
    {
        country = newCountry;
    }
    public void setCity(String newCity)
    {
        city = newCity;
    }
    public void setEventType(String newType)
    {
        eventType = newType;
    }
    public void setYear(int newYear)
    {
        year = newYear;
    }

    /*@Override
    public void setChildObjectList(List<Object> list) {
        childrenList = list;
    }

    @Override
    public List<Object> getChildObjectList() {
        return childrenList;
    }*/

    public String getEventID()
    {
        return eventID;
    }
    public String getDescendant()
    {
        return descendant;
    }
    public String getPersonID()
    {
        return personID;
    }
    public double getLatitude()
    {
        return latitude;
    }
    public double getLongitude()
    {
        return longitude;
    }
    public String getCountry()
    {
        return country;
    }
    public String getCity()
    {
        return city;
    }
    public String getEventType()
    {
        return eventType;
    }
    public int getYear()
    {
        return year;
    }

    @Override
    public int compareTo(@NonNull Event event)
    {
		/* Note: this class has a natural ordering that is inconsistent with equals. */
        String eventType = event.getEventType();
        int retVal = 0;
        try
        {
            retVal = this.eventType.compareTo(eventType);
        }
        catch (NullPointerException e)
        {
            e.printStackTrace();
        }
        catch (ClassCastException e)
        {
            e.printStackTrace();
        }
        return retVal;
    }
}
