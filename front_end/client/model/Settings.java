package client.model;

import android.graphics.Color;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Settings {
    private int spouseLineColor;
    private int treeLineColor;
    private int storyLineColor;
    //private GoogleMap map;
    private String mapType;
    private boolean spouseSwitchChecked;
    private boolean treeSwitchChecked;
    private boolean storySwitchChecked;
    private List<PolylineOptions> spousePolylineOptions;
    private List<PolylineOptions> treePolylineOptions;
    private List<PolylineOptions> storyPolylinesOptions;

    public Settings() {
        spouseSwitchChecked = false;
        treeSwitchChecked = false;
        storySwitchChecked = false;
        spousePolylineOptions = new ArrayList<PolylineOptions>();
        treePolylineOptions = new ArrayList<PolylineOptions>();
        storyPolylinesOptions = new ArrayList<PolylineOptions>();
    }

    public void setSpouseLineColor(String color) {
        if (color.equals("Red")) {
            this.spouseLineColor = Color.RED;
        }
        else if (color.equals("Green")) {
            this.spouseLineColor = Color.GREEN;
        }
        else if (color.equals("Blue")) {
            this.spouseLineColor = Color.BLUE;
        }
    }
    public void setTreeLineColor(String color) {
        if (color.equals("Red")) {
            this.treeLineColor = Color.RED;
        }
        else if (color.equals("Green")) {
            this.treeLineColor = Color.GREEN;
        }
        else if (color.equals("Blue")) {
            this.treeLineColor = Color.BLUE;
        }
    }
    public void setStoryLineColor(String color) {
        if (color.equals("Red")) {
            this.storyLineColor = Color.RED;
        }
        else if (color.equals("Green")) {
            this.storyLineColor = Color.GREEN;
        }
        else if (color.equals("Blue")) {
            this.storyLineColor = Color.BLUE;
        }
    }
/*
    public void setMap(GoogleMap map) { this.map = map; }
*/
    public void setMapType(String mapType) { this.mapType = mapType; }
    public void setSpouseSwitchChecked(boolean isChecked) { this.spouseSwitchChecked = isChecked; }
    public void setTreeSwitchChecked(boolean isChecked) { this.treeSwitchChecked = isChecked; }
    public void setStorySwitchChecked(boolean isChecked) { this.storySwitchChecked = isChecked; }
    private Event getEarliestEvent(Person personToAttachTo) {
        List<Event> personEvents = Model.instance().getPersonEvents().get(personToAttachTo.getPersonID());
        Event earliestEvent = null;

        boolean hasBirthEvent = false;
        for (Event e : personEvents) {
            if (e.getEventType().equalsIgnoreCase("birth")) {
                hasBirthEvent = true;
                earliestEvent = e;
            }
        }

        if (!hasBirthEvent && !personEvents.isEmpty()) {
            int minYear = Calendar.getInstance().get(Calendar.YEAR);
            for (Event e : personEvents) {
                if (e.getYear() < minYear) {
                    minYear = e.getYear();
                    earliestEvent = e;
                }
            }
        }
        return earliestEvent;
    }
    private void drawLineBetweenTwoEvents(Event eventStart, Person personToAttachTo, List<PolylineOptions> lines, int lineColor, float width) {
        List<Event> personEvents = Model.instance().getPersonEvents().get(personToAttachTo.getPersonID());

        Event earliestEvent = getEarliestEvent(personToAttachTo);

        PolylineOptions polylineOptions = new PolylineOptions();
        LatLng eventLocation = new LatLng(eventStart.getLatitude(), eventStart.getLongitude());
        LatLng destLocation = new LatLng(earliestEvent.getLatitude(), earliestEvent.getLongitude());
        polylineOptions
                .add(eventLocation, destLocation)
                .width(width)
                .color(lineColor);

        lines.add(polylineOptions);

/*
        boolean hasBirthEvent = false;
        for (Event e : personEvents) {
            if (e.getEventType().equalsIgnoreCase("birth")) hasBirthEvent = true;
        }

        if (hasBirthEvent) {
            Event birthEvent = new Event();
            for (Event e : personEvents) {
                if (e.getEventType().equalsIgnoreCase("birth")) {
                    birthEvent = e;
                    break;
                }
            }

            PolylineOptions polylineOptions = new PolylineOptions();
            LatLng eventLocation = new LatLng(eventStart.getLatitude(), eventStart.getLongitude());
            LatLng destLocation = new LatLng(birthEvent.getLatitude(), birthEvent.getLongitude());
            polylineOptions
                    .add(eventLocation, destLocation)
                    .width(width)
                    .color(lineColor);

            lines.add(polylineOptions);
        }
        else if (!personEvents.isEmpty()) {
            Event earliestEvent = new Event();
            // Get current year from system
            int minYear = Calendar.getInstance().get(Calendar.YEAR);
            for (Event e : personEvents) {
                if (e.getYear() < minYear) {
                    minYear = e.getYear();
                    earliestEvent = e;
                }
            }

            PolylineOptions polylineOptions = new PolylineOptions();
            LatLng eventLocation = new LatLng(eventStart.getLatitude(), eventStart.getLongitude());
            LatLng destLocation = new LatLng(earliestEvent.getLatitude(), earliestEvent.getLongitude());
            polylineOptions
                    .add(eventLocation, destLocation)
                    .width(width)
                    .color(lineColor);

            lines.add(polylineOptions);
        }
*/
    }
    public void setSpousePolylineOptions(Event selectedEvent) {
        if (this.spouseSwitchChecked) {
            Person selectedPerson = Model.instance().getPerson(selectedEvent.getPersonID());
            this.spousePolylineOptions.clear();
            if (selectedPerson.getSpouse() != null) {
                Person spouse = Model.instance().getPerson(selectedPerson.getSpouse());
                float lineWidth = 10;
                drawLineBetweenTwoEvents(selectedEvent, spouse, this.spousePolylineOptions, this.spouseLineColor, lineWidth);
            }
        }
        else {
            this.spousePolylineOptions.clear();
        }
    }
    public void setTreePolylineOptions(Event selectedEvent) {
        if (this.treeSwitchChecked) {
            Person selectedPerson = Model.instance().getPerson(selectedEvent.getPersonID());
            this.treePolylineOptions.clear();

            float lineWidth = 12;
            if (selectedPerson.getFather() != null) {
                Person father = Model.instance().getPerson(selectedPerson.getFather());
                //drawLineBetweenTwoEvents(selectedEvent, father, this.treePolylineOptions, this.treeLineColor, lineWidth);

                recursiveTreeLineDraw(selectedEvent, father, lineWidth);

            }
            if (selectedPerson.getMother() != null) {
                Person mother = Model.instance().getPerson(selectedPerson.getMother());
                //drawLineBetweenTwoEvents(selectedEvent, mother, this.treePolylineOptions, this.treeLineColor, lineWidth);

                recursiveTreeLineDraw(selectedEvent, mother, lineWidth);
            }
        }
        else {
            this.treePolylineOptions.clear();
        }
    }
    private void recursiveTreeLineDraw(Event selectedEvent, Person parent, float lineWidth) {
        drawLineBetweenTwoEvents(selectedEvent, parent, this.treePolylineOptions, this.treeLineColor, lineWidth);

        Event parentsEvent = getEarliestEvent(parent);

        if (parent.getFather() != null) {
            Person father = Model.instance().getPerson(parent.getFather());
            //drawLineBetweenTwoEvents(selectedEvent, father, this.treePolylineOptions, this.treeLineColor, lineWidth);
            recursiveTreeLineDraw(parentsEvent, father, (lineWidth - 4));
        }
        if (parent.getMother() != null) {
            Person mother = Model.instance().getPerson(parent.getMother());
            //drawLineBetweenTwoEvents(selectedEvent, mother, this.treePolylineOptions, this.treeLineColor, lineWidth);
            recursiveTreeLineDraw(parentsEvent, mother, (lineWidth - 4));
        }
    }
    public void setStoryPolylinesOptions(Event selectedEvent) {
        if (this.storySwitchChecked) {

        }
        else {
            this.storyPolylinesOptions.clear();
        }
    }
    public int getSpouseLineColor(String color) {
        return spouseLineColor;
    }
    public int getTreeLineColor() {
        return treeLineColor;
    }
    public int getStoryLineColor() {
        return storyLineColor;
    }
    public String getMapType() {
        return mapType;
    }
    public boolean getSpouseSwitchChecked() { return this.spouseSwitchChecked; }
    public boolean getTreeSwitchChecked() { return this.treeSwitchChecked; }
    public boolean getStorySwitchChecked() { return this.storySwitchChecked; }
    public List<PolylineOptions> getSpousePolylineOptions() {
        return spousePolylineOptions;
    }
    public List<PolylineOptions> getTreePolylineOptions() {
        return treePolylineOptions;
    }
    public List<PolylineOptions> getStoryPolylinesOptions() {
        return storyPolylinesOptions;
    }
}
