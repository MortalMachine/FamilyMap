package client.model;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import java.util.*;

public class Model
{
    private static Model instance;
    private static User user;
    private static Person userPerson;
    private static AuthToken authToken;
    //private static String personID;
    private static Map<String, Person> persons;
    private static Map<String, Event> events;
    private static Map<String, List<Event>> personEvents;
    private static List<String> eventTypes;
    private static Map<String, MapColor> eventTypeColors;
    private static Set<String> paternalAncestors;
    private static Set<String> maternalAncestors;
    private static Map<String, List<Person>> personChildren;
    private static Map<String, List<Person>> personParents;

    private static Settings settings;
    private static Filter filter;

    private static boolean loggedIn = false;

    private Model() {}
    public static Model instance()
    {
        if (instance == null) {
            instance = new Model();
            settings = new Settings();
        }
        return instance;
    }

    public static void setLoggedInValue(boolean b) { loggedIn = b; }
    public static boolean isLoggedIn() { return loggedIn; }

    public void setUser(User u) { user = u; }
    public void setUserPerson(Person u) { userPerson = u; }
    public void setAuthToken(AuthToken a) { authToken = a; }
    //public void setPersonID(String personID) { this.personID = personID; }
    public void setPersons(Person[] array)
    {
        persons = new HashMap<>();
        for (Person person : array) persons.put(person.getPersonID(), person);
    }
    public void setEvents(Event[] array)
    {
        events = new HashMap<>();
        for (Event event : array) events.put(event.getEventID(), event);
    }
    public void setPersonEvents(Person[] persons, Event[] events/*Map<String, List<Event>> map*/) {
        //personEvents = map;
        personEvents = new HashMap<>();

        for (int i = 0; i < persons.length; i++)
        {
            List<Event> eventList = new ArrayList<>();
            String personID = new String(persons[i].getPersonID());

            for (int j = 0; j < events.length; j++)
            {
                if (personID.equals(events[j].getPersonID()))
                {
                    eventList.add(events[j]);
                }
            }

            Collections.sort(eventList, new Comparator<Event>() {
                public int compare(Event e1, Event e2) {
                    if (e1.getEventType().equalsIgnoreCase("birth")) {
                        return 1;
                    }
                    else if (e2.getEventType().equalsIgnoreCase("birth")) {
                        return -1;
                    }
                    else if (e1.getEventType().equalsIgnoreCase("death")) {
                        return -1;
                    }
                    else if (e2.getEventType().equalsIgnoreCase("death")) {
                        return 1;
                    }
                    else if (e1.getYear() == e2.getYear()) {
                        if (e1.getEventType().equals(e2.getEventType().toLowerCase())) {
                            return 1;
                        }
                        else if (e2.getEventType().equals(e1.getEventType().toLowerCase())) return -1;
                        else return 0;
                        //return 0;
                    }
                    else {
                        return e1.getYear() > e2.getYear() ? -1 : 1;
                    }
                }
            });
            Collections.reverse(eventList);

            personEvents.put(personID, eventList);
        }
    }
    public void setEventTypes(Event[] events) {
        eventTypes = new ArrayList<>();

        for (Event event : events) {
            String eventType = event.getEventType();
            String eventTypeLC = eventType.toLowerCase();

            boolean notFound = true;
            for (String type : eventTypes) {
                String typeLC = type.toLowerCase();
                if (typeLC.equals(eventTypeLC)) {
                    notFound = false;
                }
            }

            if (notFound) {
                eventTypes.add(eventType);
            }
        }
    }
    public void setEventTypeColor(String eventType)
    {
        if (eventTypeColors == null) eventTypeColors = new HashMap<>();
        if (eventTypeColors.get(eventType.toLowerCase()) == null)
        {
            if (eventType.equalsIgnoreCase("birth")) eventTypeColors.put(eventType.toLowerCase(), new MapColor(BitmapDescriptorFactory.HUE_AZURE/*Color.rgb(51, 153, 255)*/));
            else if (eventType.equalsIgnoreCase("baptism")) eventTypeColors.put(eventType.toLowerCase(), new MapColor(BitmapDescriptorFactory.HUE_BLUE/*Color.rgb(0,255,255)*/));
            else if (eventType.equalsIgnoreCase("marriage")) eventTypeColors.put(eventType.toLowerCase(), new MapColor(BitmapDescriptorFactory.HUE_ROSE/*Color.rgb(254,254,254)*/));
            else if (eventType.equalsIgnoreCase("death")) eventTypeColors.put(eventType.toLowerCase(), new MapColor(BitmapDescriptorFactory.HUE_YELLOW/*Color.rgb(0,0,0)*/));
            else
            {
                /*Random random = new Random();
                int r = random.nextInt(255);
                int g = random.nextInt(255);
                int b = random.nextInt(255);
                eventTypeColors.put(eventType, new MapColor(Color.rgb(r, g, b)));*/
                Random random = new Random();
                float rColor = (float)random.nextInt(360);
                eventTypeColors.put(eventType.toLowerCase(), new MapColor(rColor));
            }
        }
    }
    public void setPaternalAncestors(Person[] persons/*Set<String> set*/) {
        //paternalAncestors = set;
        String descendantID = persons[0].getDescendant();
        Person descendant = new Person();
        for (int i = 0; i < persons.length; i++)
        {
            if (descendantID.equals(persons[i].getPersonID()))
            {
                descendant = new Person(persons[i]);
                break;
            }
        }

        paternalAncestors = new HashSet<>();

        for (int i = 0; i < persons.length; i++)
        {
            if (descendant.getFather() != null && descendant.getFather().equals(persons[i].getPersonID()))
            {
                paternalAncestors.add(persons[i].getPersonID());
                recursiveFatherFinder(persons[i], persons, paternalAncestors);
                break;
            }
        }
    }
    public void setMaternalAncestors(Person[] persons/*Set<String> set*/) {
        //paternalAncestors = set;
        String descendantID = persons[0].getDescendant();
        Person descendant = new Person();
        for (int i = 0; i < persons.length; i++)
        {
            if (descendantID.equals(persons[i].getPersonID()))
            {
                descendant = new Person(persons[i]);
                break;
            }
        }

        maternalAncestors = new HashSet<>();

        for (int i = 0; i < persons.length; i++)
        {
            if (descendant.getMother() != null && descendant.getMother().equals(persons[i].getPersonID()))
            {
                maternalAncestors.add(persons[i].getPersonID());
                recursiveMotherFinder(persons[i], persons, maternalAncestors);
                break;
            }
        }
    }
    private void recursiveFatherFinder(Person child, Person[] persons, Set<String> paternalAncestors)
    {
        for (int i = 0; i < persons.length; i++)
        {
            if (child.getFather() != null && child.getFather().equals(persons[i].getPersonID()))
            {
                paternalAncestors.add(persons[i].getPersonID());
                recursiveFatherFinder(persons[i], persons, paternalAncestors);
                break;
            }
        }
    }
    private void recursiveMotherFinder(Person child, Person[] persons, Set<String> maternalAncestors)
    {
        for (int i = 0; i < persons.length; i++)
        {
            if (child.getMother() != null && child.getMother().equals(persons[i].getPersonID()))
            {
                maternalAncestors.add(persons[i].getPersonID());
                recursiveMotherFinder(persons[i], persons, maternalAncestors);
                break;
            }
        }
    }
    public void setPersonChildren(Person[] persons/*Map<String, List<Person>> map*/) {
        //personChildren = map;
        personChildren = new HashMap<>();

        for (int i = 0; i < persons.length; i++)
        {
            Person parent = new Person(persons[i]);
            List<Person> children = new ArrayList<>();
            for (int j = 0; j < persons.length; j++)
            {
                if (j == i) continue;
                if ( ( persons[j].getFather() != null && persons[j].getFather().equals(parent.getPersonID()) )
                        || ( persons[j].getMother() != null && persons[j].getMother().equals(parent.getPersonID()) ) )
                {
                    children.add(persons[j]);
                }
            }
            personChildren.put(parent.getPersonID(), children);
        }
    }
    public void setPersonParents(Person[] persons/*Map<String, List<Person>> map*/) {
        //personParents = map;
        personParents = new HashMap<>();

        for (int i = 0; i < persons.length; i++)
        {
            Person child = new Person(persons[i]);
            List<Person> parents = new ArrayList<>();
            if (child.getFather() != null) parents.add(getPerson(child.getFather()));
            if (child.getMother() != null) parents.add(getPerson(child.getMother()));
            personParents.put(child.getPersonID(), parents);
        }
    }
    //public void setSettings() { settings = new Settings(); }

    public User getUser() { return user; }
    public Person getUserPerson() { return userPerson; }
    public AuthToken getAuthToken() { return authToken; }
    //public String getPersonID() { return personID; }
    public Map<String, Person> getPersons() { return persons; }
    public Map<String, Event> getEvents() { return events; }
    public Map<String, List<Event>> getPersonEvents() { return personEvents; }
    public List<String> getEventTypes() { return eventTypes; }
    public MapColor getEventTypeColor(String eventType) { return eventTypeColors.get(eventType.toLowerCase()); }
    public Set<String> getPaternalAncestors() { return paternalAncestors; }
    public Set<String> getMaternalAncestors() { return maternalAncestors; }
    public List<Person> getPersonChildren(String personID) {
        List<Person> children = new ArrayList<>();
        Iterator<Map.Entry<String, List<Person>>> entries = personChildren.entrySet().iterator();
        while (entries.hasNext())
        {
            Map.Entry<String, List<Person>> entry = entries.next();
            String personIDKey = entry.getKey();
            if (personIDKey.equals(personID))
            {
                children = entry.getValue();
            }
        }
        return children;
    }
    public List<Person> getPersonParents(String personID) {
        List<Person> parents = new ArrayList<>();
        Iterator<Map.Entry<String, List<Person>>> entries = personParents.entrySet().iterator();
        while (entries.hasNext())
        {
            Map.Entry<String, List<Person>> entry = entries.next();
            String personIDKey = entry.getKey();
            if (personIDKey.equals(personID))
            {
                parents = entry.getValue();
            }
        }
        return parents;
    }
    public Person getPerson(String personID) { return persons.get(personID); }
    public Event getEvent(String eventID) { return events.get(eventID); }
    public Settings getSettings() { return settings; }
}
