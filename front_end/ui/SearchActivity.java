package ui;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.util.Locale;

import client.model.Event;
import client.model.Model;
import client.model.Person;
import jordanrj.familymap.R;

public class SearchActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{
    private ListView list;
    private ListViewAdapter adapter;
    private SearchView editSearch;
    private ArrayList<Person> peopleList = new ArrayList<Person>();
    private ArrayList<Event> eventList = new ArrayList<Event>();
    private int peopleListSize;
    private int eventListSize;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        peopleList = getPeopleAsArrayList();
        eventList = getEventsAsArrayList();

        list = (ListView) findViewById(R.id.listview);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (position < peopleListSize) {
                    Intent intent = new Intent(SearchActivity.this, PersonActivity.class);
                    ListViewAdapter.PersonViewHolder personVH = (ListViewAdapter.PersonViewHolder)view.getTag();
                    String personID = personVH.getPersonID();
                    intent.putExtra("PERSON_ID_OF_PERSON_CLICKED_ON", personID);
                    startActivity(intent);
                }
                else {
                    Intent intent = new Intent(SearchActivity.this, EventActivity.class);
                    ListViewAdapter.EventViewHolder eventVH = (ListViewAdapter.EventViewHolder)view.getTag();
                    String eventID = eventVH.getEventID();
                    intent.putExtra("EVENT_ID_OF_EVENT_CLICKED_ON", eventID);
                    startActivity(intent);
                }
            }
        });

        adapter = new ListViewAdapter(this, peopleList, eventList);

        list.setAdapter(adapter);

        editSearch = (SearchView) findViewById(R.id.search);
        editSearch.setOnQueryTextListener(this);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {

        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String text = newText;
        adapter.filter(text);
        return false;
    }

    private ArrayList<Person> getPeopleAsArrayList() {
        ArrayList<Person> pArrayList = new ArrayList<>();
        Map<String, Person> persons = Model.instance().getPersons();

        Set<Map.Entry<String, Person>> entrySet = persons.entrySet();
        for (Map.Entry<String, Person> entry : entrySet) {
            pArrayList.add(entry.getValue());
        }
        return pArrayList;
    }

    private ArrayList<Event> getEventsAsArrayList() {
        ArrayList<Event> eArrayList = new ArrayList<>();
        Map<String, Event> events = Model.instance().getEvents();

        Set<Map.Entry<String, Event>> entrySet = events.entrySet();
        for (Map.Entry<String, Event> entry : entrySet) {
            eArrayList.add(entry.getValue());
        }
        return eArrayList;
    }

    private class ListViewAdapter extends BaseAdapter {
        private Drawable genderFigureIcon;
        private Context mContext;
        private LayoutInflater inflater;
        private List<Person> personList = null;
        private ArrayList<Person> pArrayList;
        private List<Event> eventList = null;
        private ArrayList<Event> eArrayList;
        private Drawable pinpointIcon;

        public ListViewAdapter(Context context, List<Person> personList, List<Event> eventList) {
            mContext = context;
            this.personList = new ArrayList<>();
            this.eventList = new ArrayList<>();
            inflater = LayoutInflater.from(mContext);
            this.pArrayList = new ArrayList<Person>();
            this.pArrayList.addAll(personList);
            this.eArrayList = new ArrayList<Event>();
            this.eArrayList.addAll(eventList);
        }

        public class PersonViewHolder {
            private TextView name;
            private ImageView genderIcon;
            private String personID;

            public TextView getName() { return name; }
            public void setName(TextView name) { this.name = name; }
            public ImageView getGenderIcon() { return genderIcon; }
            public void setGenderIcon(ImageView genderIcon) { this.genderIcon = genderIcon; }
            public String getPersonID() { return personID; }
            public void setPersonID(String personID) { this.personID = personID; }
        }

        public class EventViewHolder {
            private ImageView pinpointImageView;
            private TextView eventDetailsTextView;
            private TextView fullNameTextView;
            private String eventID;

            public ImageView getPinpointImageView() { return pinpointImageView; }
            public void setPinpointImageView(ImageView pinpointImageView) { this.pinpointImageView = pinpointImageView; }
            public TextView getEventDetailsTextView() { return eventDetailsTextView; }
            public void setEventDetailsTextView(TextView eventDetailsTextView) { this.eventDetailsTextView = eventDetailsTextView; }
            public TextView getFullNameTextView() { return fullNameTextView; }
            public void setFullNameTextView(TextView fullNameTextView) { this.fullNameTextView = fullNameTextView; }
            public String getEventID() { return eventID; }
            public void setEventID(String eventID) { this.eventID = eventID; }
        }

        @Override
        public int getCount() {
            return (personList.size() + eventList.size());
        }

        @Override
        public Object getItem(int position) {
            if (position < personList.size()) {
                return personList.get(position);
            }
            else {
                return eventList.get(position);
            }
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public View getView(final int position, View view, ViewGroup parent) {
            if (position < personList.size()) {
                final PersonViewHolder holder;

                holder = new PersonViewHolder();
                view = inflater.inflate(R.layout.list_item_family_child, null);
                holder.setName((TextView) view.findViewById(R.id.child_list_item_family_full_name));
                holder.setGenderIcon((ImageView) view.findViewById(R.id.genderFigure));
                view.setTag(holder);

                String fullName = personList.get(position).getFirstName() + " "
                        + personList.get(position).getLastName();
                holder.getName().setText(fullName);

                if (personList.get(position).getGender().toLowerCase().startsWith("m")) {
                    genderFigureIcon = new IconDrawable(SearchActivity.this, FontAwesomeIcons.fa_male)
                            .colorRes(R.color.male_icon)
                            .sizeDp(36);
                }
                else {
                    genderFigureIcon = new IconDrawable(SearchActivity.this, FontAwesomeIcons.fa_female)
                            .colorRes(R.color.female_icon)
                            .sizeDp(36);
                }
                holder.getGenderIcon().setImageDrawable(genderFigureIcon);

                holder.setPersonID(personList.get(position).getPersonID());
            }
            else if (position < this.getCount()){
                int modPos = position - personList.size();
                final EventViewHolder holder;

                holder = new EventViewHolder();
                view = inflater.inflate(R.layout.list_item_events_child, null);
                holder.setPinpointImageView((ImageView) view.findViewById(R.id.pinpoint));
                holder.setEventDetailsTextView((TextView) view.findViewById(R.id.child_list_item_events_event_details));
                holder.setFullNameTextView((TextView) view.findViewById(R.id.child_list_item_events_full_name));
                view.setTag(holder);

                holder.getPinpointImageView().setImageDrawable(pinpointIcon);

                Event event = eventList.get(modPos);
                String details = event.getEventType() + ": "
                        + event.getCity() + ", "
                        + event.getCountry()
                        + " (" + event.getYear() + ")";
                holder.getEventDetailsTextView().setText(details);

                String personID = eventList.get(modPos).getPersonID();
                Person person = Model.instance().getPerson(personID);
                String fullName = person.getFirstName() + " " + person.getLastName();
                holder.getFullNameTextView().setText(fullName);

                holder.setEventID(event.getEventID());
            }
            return view;
        }

        public void filter(String charText) {
            charText = charText.toLowerCase(Locale.getDefault());
            personList.clear();
            if (charText.length() == 0) {
                //personList.addAll(pArrayList);
            } else {
                for (Person p : pArrayList) {
                    if (p.getFirstName().toLowerCase(Locale.getDefault()).contains(charText)
                        || p.getLastName().toLowerCase(Locale.getDefault()).contains(charText)) {
                        personList.add(p);
                    }
                }
            }
            peopleListSize = personList.size();

            eventList.clear();
            if (charText.length() == 0) {
                //eventList.addAll(eArrayList);
            } else {
                for (Event e : eArrayList) {
                    Person person = Model.instance().getPerson(e.getPersonID());
                    if (e.getEventType().toLowerCase(Locale.getDefault()).contains(charText)
                            || e.getCity().toLowerCase(Locale.getDefault()).contains(charText)
                            || e.getCountry().toLowerCase(Locale.getDefault()).contains(charText)
                            || String.valueOf(e.getYear()).contains(charText)
                            || person.getFirstName().toLowerCase(Locale.getDefault()).contains(charText)
                            || person.getLastName().toLowerCase(Locale.getDefault()).contains(charText)) {
                        eventList.add(e);
                    }
                }
            }
            eventListSize = eventList.size();

            notifyDataSetChanged();
        }
    }
}
