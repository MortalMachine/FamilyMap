package ui;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.util.*;

import client.model.Event;
//import jordanrj.familymap.MapsActivity;
import client.model.Model;
import client.model.MapColor;
import client.model.Person;
import client.model.Settings;
import jordanrj.familymap.R;

public class MapFragment extends SupportMapFragment implements OnMapReadyCallback
{
    //private static final String PARENT_ACTIVITY_NAME = "event_activity";
    private String parentActName;
    private ImageView imageView;
    private TextView textView;
    private MenuItem searchMenuItem;
    private MenuItem filterMenuItem;
    private MenuItem settingsMenuItem;
    private Event selectedEvent;
    private Map<Marker, Event> markersToEvents;
    private List<Polyline> lines;
    Drawable genderIcon;

    private GoogleMap map;
    private SupportMapFragment mapFragment;

    private String personID;

/*
    public static MapFragment newInstance()
    {
        return new MapFragment();
    }
*/
    public MapFragment() { lines = new ArrayList<Polyline>(); }
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getActivity().invalidateOptionsMenu();
        //setHasOptionsMenu(true);

        //parentActName = (String) getArguments().getCharSequence(PARENT_ACTIVITY_NAME);
    }

/*
    public static MapFragment newInstance(String parentActName) {
        Bundle args = new Bundle();
        args.putCharSequence(PARENT_ACTIVITY_NAME, parentActName);
        MapFragment fragment = new MapFragment();
        fragment.setArguments(args);
        return fragment;
    }
*/
/*

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu, menu);
    }
*/

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState)
    {
        super.onSaveInstanceState(savedInstanceState);
        //Log.i(TAG, "onSaveInstanceState");
        //savedInstanceState.putInt(KEY_INDEX, currentIndex);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_map, container, false);

        mapFragment = (SupportMapFragment)getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        //Intent intent = new Intent(getActivity(), MapsActivity.class);
        //startActivity(intent);

        /*markersToEvents = new HashMap<>();
        selectedEvent = null;
        lines = new ArrayList<>();

        if (savedInstanceState == null)
        {
            if (haveEventIDArgument())
            {
                String eventID = getArguments().getString(ARG_EVENT_ID);
                selectedEvent = Model.instance().getEventByID(eventID);
            }
            else
            {
                if (savedInstanceState)
            }

            (SupportMapFragment)getChildFragmentManager().findFragmentById(R.id.map).getMapAsync(this);
            return view;
        }*/

        imageView = view.findViewById(R.id.genderFigure);
        textView = view.findViewById(R.id.eventDetails);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PersonActivity.class);
                intent.putExtra("PERSON_ID_OF_PERSON_CLICKED_ON", personID);
                startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (selectedEvent != null) {
            drawLinesOnMap(selectedEvent);
        }
        else {
            drawLinesOnMap(null);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        //drawLinesOnMap(null);
        markersToEvents = new HashMap<>();

        // Retrieve eventID from getArguments and move camera to its location
        if (getArguments() != null) {
            String eventID = getArguments().getString("EVENT_ID");
            selectedEvent = Model.instance().getEvent(eventID);
            LatLng eventCoord = new LatLng(selectedEvent.getLatitude(), selectedEvent.getLongitude());
            //map.addMarker(new MarkerOptions().position(eventCoord).title("Marker in Sydney"));
            map.moveCamera(CameraUpdateFactory.newLatLng(eventCoord));

            displayEventInfo(selectedEvent);
            drawLinesOnMap(selectedEvent);
        }

        Map<String, List<Event>> personEvents = Model.instance().getPersonEvents();
        Iterator<Map.Entry<String, List<Event>>> entries = personEvents.entrySet().iterator();
        while (entries.hasNext())
        {
            PolylineOptions polylineOptions = new PolylineOptions();
            Map.Entry<String, List<Event>> entry = entries.next();
            List<Event> events = entry.getValue();

            for (Event event : events)
            {
                LatLng eventLocation = new LatLng(event.getLatitude(), event.getLongitude());

                polylineOptions.add(eventLocation);

                StringBuilder title = new StringBuilder();
                title.append(Model.instance().getPerson(event.getPersonID()).getFirstName());
                title.append(" ");
                title.append(Model.instance().getPerson(event.getPersonID()).getLastName());

                StringBuilder snippet = new StringBuilder();
                snippet.append(event.getEventType() + ": ");
                snippet.append(event.getCity()  + ", " + event.getCountry());
                snippet.append("(" + event.getYear() + ")");

                Model.instance().setEventTypeColor(event.getEventType());
                MapColor mapColor = Model.instance().getEventTypeColor(event.getEventType());
                float color = mapColor.getColor();

                Marker marker = map.addMarker(new MarkerOptions()
                        .position(eventLocation)
                        .title(title.toString())
                        .snippet(snippet.toString())
                        .icon(BitmapDescriptorFactory.defaultMarker(color)));
                marker.setTag(event.getEventID());
                markersToEvents.put(marker, event);
            }

/*
            Polyline line = map.addPolyline((polylineOptions).width(5).color(Color.RED));
            lines.add(line);
*/
        }

        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                selectedEvent = Model.instance().getEvent(marker.getTag().toString());
                displayEventInfo(selectedEvent);
                drawLinesOnMap(selectedEvent);
                return true;
            }
        });
    }

    public void displayEventInfo(Event event) {
        //Event event = Model.instance().getEvent(marker.getTag().toString());
        personID = event.getPersonID();
        Person person = Model.instance().getPerson(personID);

        StringBuilder sb = new StringBuilder();
        sb.append(person.getFirstName() + " " + person.getLastName() + " \n");
        sb.append(event.getEventType() + ": ");
        sb.append(event.getCity()  + ", " + event.getCountry());
        sb.append("(" + event.getYear() + ")");

        textView.setText(sb.toString());

        //Model.instance().setPersonID(person.getPersonID());

        if (person.getGender().equalsIgnoreCase("m")
                || person.getGender().equalsIgnoreCase("male"))
        {
            genderIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_male)
                    .colorRes(R.color.male_icon).sizeDp(40);
        }
        else if (person.getGender().equalsIgnoreCase("f")
                || person.getGender().equalsIgnoreCase("female"))
        {
            genderIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_female)
                    .colorRes(R.color.female_icon).sizeDp(40);
        }
        imageView.setImageDrawable(genderIcon);
    }

    private void drawLinesOnMap(Event event) {
        if (event != null) {
            Settings settings = Model.instance().getSettings();
            //settings.setMap(map);
            settings.setSpousePolylineOptions(event);
            settings.setStoryPolylinesOptions(event);
            settings.setTreePolylineOptions(event);

            for (Polyline line : lines) {
                line.remove();
            }
            lines.clear();

            for (PolylineOptions options : settings.getSpousePolylineOptions()) {
                Polyline line = map.addPolyline(options);
                lines.add(line);
            }
            for (PolylineOptions options : settings.getTreePolylineOptions()) {
                Polyline line = map.addPolyline(options);
                lines.add(line);
            }
            for (PolylineOptions options : settings.getStoryPolylinesOptions()) {
                Polyline line = map.addPolyline(options);
                lines.add(line);
            }
        }
        else {
            for (Polyline line : lines) {
                line.remove();
            }
            lines.clear();
        }
    }
}
