package ui;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;

import net.ServerProxy;

import client.model.Event;
import client.model.Model;
import client.model.Person;
import client.model.User;
import client.request.DataRequest;
import client.response.DataResponse;

public class GetDataTask extends AsyncTask<DataRequest, Void, DataResponse>
{
    public interface Listener {
        FragmentActivity getActivity();
        Context getContext();
        User getmUser();
        void setmSignInButton(boolean b);
        void setmRegisterButtonButton(boolean b);
        boolean anyEmptyFields();
        void displayToast(String s);
    }

    private Listener listener;

    public GetDataTask(Listener l) { listener = l; }
    @Override
    protected DataResponse doInBackground(DataRequest... requests)
    {
        ServerProxy serverProxy = new ServerProxy();
        DataResponse response = new DataResponse();
        for (DataRequest request: requests)
        {
            response = serverProxy.getData(request);
        }
        return response;
    }

    @Override
    protected void onPostExecute(DataResponse response)
    {
        if (response.getErrorMessage() == null || response.getErrorMessage().isEmpty())
        {
            putDataInModel(response);

            Person userPerson = Model.instance().getPerson(listener.getmUser().getPersonID());
            String fname = userPerson.getFirstName();
            String lname = userPerson.getLastName();

            listener.displayToast(fname + " " + lname);

            ((MainActivity)listener.getActivity()).swapFragment(MainActivity.MAP_FRAGMENT);
        }
        else
        {
            listener.displayToast(response.getErrorMessage());
        }
    }

    private void putDataInModel(DataResponse response)
    {
        Person[] persons = response.getPersons();
        Event[] events = response.getEvents();

        Model.instance().setPersons(persons);
        Model.instance().setEvents(events);
        //setPersonEvents(persons, events);
        //setEventTypes(events);
        //setPaternalAncestors(persons);
        //setPersonChildren(persons);

        Model.instance().setPersonChildren(persons);
        Model.instance().setPersonParents(persons);
        Model.instance().setPersonEvents(persons, events);
        Model.instance().setEventTypes(events);
        Model.instance().setPaternalAncestors(persons);
        Model.instance().setMaternalAncestors(persons);
    }
}
