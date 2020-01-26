package ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.Model.ParentObject;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import client.model.Event;
import client.model.Model;
import client.model.Person;
import jordanrj.familymap.R;

public class PersonActivity extends AppCompatActivity
{
    private RecyclerView eventRecyclerView;
    private RecyclerView familyRecyclerView;
    private Drawable pinpointIcon;
    private Drawable genderFigureIcon;
    private String personID;
    private TextView firstNameView;
    private TextView lastNameView;
    private TextView genderView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        personID = getIntent().getStringExtra("PERSON_ID_OF_PERSON_CLICKED_ON");

        firstNameView = (TextView)findViewById(R.id.firstNameText);
        lastNameView = (TextView)findViewById(R.id.lastNameText);
        genderView = (TextView)findViewById(R.id.genderText);
        Person person = Model.instance().getPerson(personID);
        String firstName = person.getFirstName();
        String lastName = person.getLastName();
        String gender = person.getGender();
        if (gender.charAt(0) == 'm') gender = "Male";
        else gender = "Female";
        firstNameView.setText(firstName);
        lastNameView.setText(lastName);
        genderView.setText(gender);

        Iconify.with(new FontAwesomeModule());
        pinpointIcon = new IconDrawable(this, FontAwesomeIcons.fa_map_marker)
                .colorRes(R.color.colorPrimaryDark)
                .sizeDp(36);
/*
        genderFigureIcon = new IconDrawable(this, FontAwesomeIcons.fa_male)
                .colorRes(R.color.male_icon)
                .sizeDp(36);
*/

        EventExpandableAdapter eventExpandableAdapter = new EventExpandableAdapter(this, generateEvents());
        eventExpandableAdapter.setCustomParentAnimationViewId(R.id.parent_life_events_expand_arrow);
        eventExpandableAdapter.setParentClickableViewAnimationDefaultDuration();
        eventExpandableAdapter.setParentAndIconExpandOnClick(true);

        eventRecyclerView = (RecyclerView) findViewById(R.id.life_events_recycler_view);
        eventRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        eventRecyclerView.setAdapter(eventExpandableAdapter);

        FamilyExpandableAdapter familyExpandableAdapter = new FamilyExpandableAdapter(this, generateFamily());
        familyExpandableAdapter.setCustomParentAnimationViewId(R.id.parent_family_expand_arrow);
        familyExpandableAdapter.setParentClickableViewAnimationDefaultDuration();
        familyExpandableAdapter.setParentAndIconExpandOnClick(true);

        familyRecyclerView = (RecyclerView) findViewById(R.id.family_recycler_view);
        familyRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        familyRecyclerView.setAdapter(familyExpandableAdapter);

        /*Bundle extras;
        if (savedInstanceState == null)
        {
            *//*fetching extra data passed with intents in a Bundle type variable*//*
            extras = getIntent().getExtra();

            if (extras == null)
            {
                personID = null;
            }
            else
            {
                *//* fetching the string passed with intent using ‘extras’*//*
                personID = extras.getString(“PERSON_ID_OF_PERSON_CLICKED_ON”);
            }
        }*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        new MenuInflater(this).inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                startTopActivity(this, false);
                return true;
            case R.id.search:
                PersonActivity.this.startActivity(new Intent(this, SearchActivity.class));
                return true;
            case R.id.filter:
                PersonActivity.this.startActivity(new Intent(this, FilterActivity.class));
                return true;
            case R.id.settings:
                PersonActivity.this.startActivity(new Intent(this, SettingsActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static void startTopActivity(Context context, boolean newInstance) {
        Intent intent = new Intent(context, MainActivity.class);
        if (newInstance) {
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }
        else {
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }
        context.startActivity(intent);
    }

    private ArrayList<ParentObject> generateEvents() {
        Person person = Model.instance().getPerson(personID);
        List<Event> personEvents = Model.instance().getPersonEvents().get(person.getPersonID());

        ParentObject parentObject = new ParentObject() {
            private List<Object> mChildrenList;
            @Override
            public List<Object> getChildObjectList() {
                return mChildrenList;
            }

            @Override
            public void setChildObjectList(List<Object> list) {
                mChildrenList = list;
            }
        };

        ArrayList<Object> childList = new ArrayList<>();
        for (Event event : personEvents)
        {
            childList.add(event);
        }
        parentObject.setChildObjectList(childList);
        ArrayList<ParentObject> parentObjects = new ArrayList<>();
        parentObjects.add(parentObject);
        return parentObjects;
    }

    private class EventExpandableAdapter extends ExpandableRecyclerAdapter<EventParentViewHolder, EventChildViewHolder>
    {
        public LayoutInflater mInflater;

        public EventExpandableAdapter(Context context, List<ParentObject> parentItemList) {
            super(context, parentItemList);
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public EventParentViewHolder onCreateParentViewHolder(ViewGroup viewGroup) {
            View view = mInflater.inflate(R.layout.list_item_events_parent, viewGroup, false);
            return new EventParentViewHolder(view);
        }

        @Override
        public void onBindParentViewHolder(EventParentViewHolder eventParentViewHolder, int i, Object parentObject) {
            //Event event = (Event) parentObject;
            eventParentViewHolder.eventTitleTextView.setText(R.string.list_item_events_parent);
        }

        @Override
        public EventChildViewHolder onCreateChildViewHolder(ViewGroup viewGroup) {
            View view = mInflater.inflate(R.layout.list_item_events_child, viewGroup, false);
            return new EventChildViewHolder(view);
        }

        @Override
        public void onBindChildViewHolder(EventChildViewHolder eventChildViewHolder, int i, Object childObject) {
            eventChildViewHolder.pinpointImageView.setImageDrawable(pinpointIcon);

            Event event = (Event) childObject;
            eventChildViewHolder.setEventId(event.getEventID());
            String text = event.getEventType() + ": "
                    + event.getCity() + ", "
                    + event.getCountry()
                    + " (" + event.getYear() + ")";
            eventChildViewHolder.eventDetailsTextView.setText(text);

            Person person = Model.instance().getPerson(event.getPersonID());
            text = person.getFirstName() + " " + person.getLastName();
            eventChildViewHolder.fullNameTextView.setText(text);
        }
    }
    private class EventParentViewHolder extends ParentViewHolder
    {
        public TextView eventTitleTextView;
        public ImageButton parentDropDownArrow;

        public EventParentViewHolder(View itemView)
        {
            super(itemView);

            eventTitleTextView = (TextView) itemView.findViewById(R.id.parent_list_item_event_title_text_view);
            parentDropDownArrow = (ImageButton) itemView.findViewById(R.id.parent_life_events_expand_arrow);
        }
    }
    private class EventChildViewHolder extends ChildViewHolder implements View.OnClickListener
    {
        public ImageView pinpointImageView;
        public TextView eventDetailsTextView;
        public TextView fullNameTextView;
        private String eventIdCVH;

        public EventChildViewHolder(View itemView)
        {
            super(itemView);

            pinpointImageView = (ImageView) itemView.findViewById(R.id.pinpoint);
            pinpointImageView.setImageDrawable(pinpointIcon);
            eventDetailsTextView = (TextView) itemView.findViewById(R.id.child_list_item_events_event_details);
            fullNameTextView = (TextView) itemView.findViewById(R.id.child_list_item_events_full_name);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(PersonActivity.this, EventActivity.class);
            intent.putExtra("EVENT_ID_OF_EVENT_CLICKED_ON", eventIdCVH);
            startActivity(intent);
        }

        public void setEventId(String e) {
            eventIdCVH = e;
        }
    }


    private ArrayList<ParentObject> generateFamily() {
        Person person = Model.instance().getPerson(personID);
        List<Person> personParents = Model.instance().getPersonParents(personID);
        List<Person> personChildren = Model.instance().getPersonChildren(personID);
        Person personSpouse = Model.instance().getPerson(person.getSpouse());
        ParentObject parentObject = new ParentObject() {
            private List<Object> mChildrenList;
            @Override
            public List<Object> getChildObjectList() {
                return mChildrenList;
            }

            @Override
            public void setChildObjectList(List<Object> list) {
                mChildrenList = list;
            }
        };

        ArrayList<Object> childList = new ArrayList<>();
        for (Person parent : personParents) childList.add(parent);
        for (Person child : personChildren) childList.add(child);
        if (personSpouse != null) childList.add(personSpouse);

        parentObject.setChildObjectList(childList);
        ArrayList<ParentObject> parentObjects = new ArrayList<>();
        parentObjects.add(parentObject);
        return parentObjects;
    }
    private class FamilyExpandableAdapter extends ExpandableRecyclerAdapter<FamilyParentViewHolder, FamilyChildViewHolder>
    {
        public LayoutInflater mInflater;

        public FamilyExpandableAdapter(Context context, List<ParentObject> parentItemList) {
            super(context, parentItemList);
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public FamilyParentViewHolder onCreateParentViewHolder(ViewGroup viewGroup) {
            View view = mInflater.inflate(R.layout.list_item_family_parent, viewGroup, false);
            return new FamilyParentViewHolder(view);
        }

        @Override
        public void onBindParentViewHolder(FamilyParentViewHolder familyParentViewHolder, int i, Object parentObject) {
            //Person person = (Person) parentObject;
            familyParentViewHolder.familyTitleTextView.setText(R.string.list_item_family_parent);
        }

        @Override
        public FamilyChildViewHolder onCreateChildViewHolder(ViewGroup viewGroup) {
            View view = mInflater.inflate(R.layout.list_item_family_child, viewGroup, false);
            return new FamilyChildViewHolder(view);
        }

        @Override
        public void onBindChildViewHolder(FamilyChildViewHolder familyChildViewHolder, int i, Object childObject) {
            Person person = (Person) childObject;

            if (person.getGender().toLowerCase().startsWith("m")) {
                genderFigureIcon = new IconDrawable(PersonActivity.this, FontAwesomeIcons.fa_male)
                        .colorRes(R.color.male_icon)
                        .sizeDp(36);
            }
            else {
                genderFigureIcon = new IconDrawable(PersonActivity.this, FontAwesomeIcons.fa_female)
                        .colorRes(R.color.female_icon)
                        .sizeDp(36);
            }

            familyChildViewHolder.genderFigureImageView.setImageDrawable(genderFigureIcon);

            familyChildViewHolder.setPersonId(person.getPersonID());

            String fullName = person.getFirstName() + " " + person.getLastName();
            familyChildViewHolder.fullNameTextView.setText(fullName);

            Person mainPerson = Model.instance().getPerson(personID);
            String relationshipType = null;
            if (person.getPersonID().equals(mainPerson.getFather())) {
                relationshipType = "Father";
            }
            else if (person.getPersonID().equals(mainPerson.getMother())) {
                relationshipType = "Mother";
            }
            else if (person.getPersonID().equals(mainPerson.getSpouse())) {
                relationshipType = "Spouse";
            }
            else {
                relationshipType = "Child";
            }

            familyChildViewHolder.relationshipTextView.setText(relationshipType);
        }
    }
    private class FamilyParentViewHolder extends ParentViewHolder
    {
        public TextView familyTitleTextView;
        public ImageButton parentDropDownArrow;

        public FamilyParentViewHolder(View itemView)
        {
            super(itemView);

            familyTitleTextView = (TextView) itemView.findViewById(R.id.parent_list_item_family_title_text_view);
            parentDropDownArrow = (ImageButton) itemView.findViewById(R.id.parent_family_expand_arrow);
        }
    }
    private class FamilyChildViewHolder extends ChildViewHolder implements View.OnClickListener
    {
        public ImageView genderFigureImageView;
        public TextView fullNameTextView;
        public TextView relationshipTextView;
        private String personIdCVH;

        public FamilyChildViewHolder(View itemView)
        {
            super(itemView);

            genderFigureImageView = (ImageView) itemView.findViewById(R.id.genderFigure);
            genderFigureImageView.setImageDrawable(genderFigureIcon);
            fullNameTextView = (TextView) itemView.findViewById(R.id.child_list_item_family_full_name);
            relationshipTextView = (TextView) itemView.findViewById(R.id.child_list_item_family_relationship);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(PersonActivity.this, PersonActivity.class);
            intent.putExtra("PERSON_ID_OF_PERSON_CLICKED_ON", personIdCVH);
            startActivity(intent);
        }

        public void setPersonId(String p) {
            personIdCVH = p;
        }
    }
}