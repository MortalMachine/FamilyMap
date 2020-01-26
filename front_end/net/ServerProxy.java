package net;


import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import com.google.gson.*;

import client.model.Event;
import client.model.Person;
import client.model.User;
import client.request.*;
import client.response.*;

/**
 * Created by jordanrj on 11/16/18.
 */

public class ServerProxy
{
    public ServerProxy() {}

    private static void writeString(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        sw.write(str);
        sw.flush();
    }

    private void sync() {}

    private void clear() {}
    private void fill() {}
    private void load() {}
    public static LoginResponse login(String serverHost, String serverPort, LoginRequest request)
    {
        LoginResponse response = new LoginResponse();
        try
        {
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/user/login");

            HttpURLConnection http = (HttpURLConnection) url.openConnection();

            http.setRequestMethod("POST");

            // Indicate that this request will or will not contain an HTTP request body
            http.setDoOutput(true);

            //http.addRequestProperty("Accept", "application/json");

            http.setConnectTimeout(720000000);
            http.setReadTimeout(72000000);
            http.connect();

            Gson gson = new Gson();
            String reqData = gson.toJson(request);
            OutputStream reqBody = http.getOutputStream();
            writeString(reqData, reqBody);
            reqBody.close();

            if (http.getResponseCode() == HttpURLConnection.HTTP_OK)
            {
                System.out.println("Login successful");
                Reader reader = new InputStreamReader(http.getInputStream());
                response = gson.fromJson(reader, LoginResponse.class);
            }
            else
            {
                System.out.println("Login failed");
            }
            http.disconnect();
            return response;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            response.setErrorMessage(e.getMessage());
            return response;
        }
    }
    public RegisterResponse register(String serverHost, String serverPort, RegisterRequest request)
    {
        RegisterResponse response = new RegisterResponse();
        try
        {
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/user/register");

            HttpURLConnection http = (HttpURLConnection) url.openConnection();

            http.setRequestMethod("POST");

            // Indicate that this request will or will not contain an HTTP request body
            http.setDoOutput(true);

            //http.addRequestProperty("Accept", "application/json");
            http.setConnectTimeout(720000000);
            http.setReadTimeout(72000000);
            http.connect();

            Gson gson = new Gson();
            String reqData = gson.toJson(request);
            OutputStream reqBody = http.getOutputStream();
            writeString(reqData, reqBody);
            reqBody.close();

            if (http.getResponseCode() == HttpURLConnection.HTTP_OK)
            {
                System.out.println("Login successful");
                InputStream respBody = http.getInputStream();
                Reader reader = new InputStreamReader(respBody);
                response = gson.fromJson(reader, RegisterResponse.class);
/*
                String respData = readString(respBody);
*/
            }
            else
            {
                System.out.println("Login failed");
            }
            http.disconnect();
            return response;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            response.setErrorMessage(e.getMessage());
            return response;
        }
    }
    public DataResponse getData(DataRequest request)
    {
        DataResponse response = new DataResponse();
        try {
            Person[] persons = getPersons(request);
            Event[] events = getEvents(request);

            response.setPersons(persons);
            response.setEvents(events);
        }
        catch (Exception e) {
            response.setErrorMessage(e.getMessage());
        }
        return response;
    }
    public Person[] getPersons(DataRequest request) throws Exception
    {
        String serverHost = request.getServerHost();
        String serverPort = request.getServerPort();
        DataResponse response = new DataResponse();
        try
        {
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/person");

            HttpURLConnection http = (HttpURLConnection) url.openConnection();

            http.setRequestMethod("GET");

            // Indicate that this request will or will not contain an HTTP request body
            http.setDoOutput(false);

            http.addRequestProperty("Authorization", request.getAuthTokenStr());
            http.addRequestProperty("Accept", "application/json");

            http.setConnectTimeout(72000000);
            http.setReadTimeout(7200000);

            http.connect();

            if (http.getResponseCode() == HttpURLConnection.HTTP_OK)
            {
                InputStream respBody = http.getInputStream();
                Reader reader = new InputStreamReader(respBody);

                Gson gson = new Gson();
                response = gson.fromJson(reader, DataResponse.class);
            }

            http.disconnect();
            return response.getPersons();
        }
        catch (Exception e)
        {
            throw e;
        }
    }
    public Event[] getEvents(DataRequest request) throws Exception
    {
        String serverHost = request.getServerHost();
        String serverPort = request.getServerPort();

        DataResponse response = new DataResponse();
        try
        {
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/event");

            HttpURLConnection http = (HttpURLConnection) url.openConnection();

            http.setRequestMethod("GET");

            // Indicate that this request will or will not contain an HTTP request body
            http.setDoOutput(false);

            http.addRequestProperty("Authorization", request.getAuthTokenStr());
            http.addRequestProperty("Accept", "application/json");

            http.connect();

            if (http.getResponseCode() == HttpURLConnection.HTTP_OK)
            {
                InputStream respBody = http.getInputStream();
                Reader reader = new InputStreamReader(respBody);

                Gson gson = new Gson();
                response = gson.fromJson(reader, DataResponse.class);
            }

            http.disconnect();
            return response.getEvents();
        }
        catch (IOException e)
        {
            throw e;
        }
    }
}
