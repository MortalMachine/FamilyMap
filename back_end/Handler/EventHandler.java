package Handler;

import com.google.gson.*;
import com.sun.net.httpserver.*;
import java.net.HttpURLConnection;
import java.io.*;

import Request.EventRequest;
import Response.EventResponse;
import Service.EventService;
/**
 * Created by jordanrj on 11/10/18.
 */

public class EventHandler implements HttpHandler
{
    public EventHandler() {}
    public void handle(HttpExchange exchange) throws IOException
    {
        boolean success = false;

        try
        {
            if (exchange.getRequestMethod().toLowerCase().equals("get"))
            {
                String authTokenStr = getAuthTokenFromHeaders(exchange.getRequestHeaders());

                if (authTokenStr.isEmpty())
                {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_UNAUTHORIZED, 0);
                    exchange = handleHTTPError(exchange, "No authorization token provided: Event not retrieved from database.");
                    exchange.getResponseBody().close();
                    return;
                }

                EventRequest eventRequest = new EventRequest();
                eventRequest.setAuthTokenStr(authTokenStr);

                String urlPath = exchange.getRequestURI().getPath();
                String[] splitPath = urlPath.split("/");

                if (splitPath.length == 3)
                {
                    eventRequest.setEventID(splitPath[2]);
                }

                EventService eventService = new EventService();
                EventResponse eventResponse = eventService.runEventService(eventRequest);

                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);

                Gson gson = new Gson();
                String respData = gson.toJson(eventResponse);

                OutputStreamWriter osWriter = new OutputStreamWriter(exchange.getResponseBody());
                osWriter.write(respData);
                osWriter.flush();

                exchange.getResponseBody().close();
                success = true;
            }

            if (!success)
            {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                exchange = handleHTTPError(exchange, "Bad http request: Event not retrieved from database.");
                exchange.getResponseBody().close();
            }
        }
        catch (IOException e)
        {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR, 0);
            exchange = handleHTTPError(exchange, "Http server error. Event not retrieved from database.");
            exchange.getResponseBody().close();
            e.printStackTrace();
        }
    }
    private HttpExchange handleHTTPError(HttpExchange exchange, String errorMessage) throws IOException
    {
        EventResponse eventResponse = new EventResponse();
        eventResponse.setErrorMessage(errorMessage);
        Gson gson = new Gson();
        String respData = gson.toJson(eventResponse);
        OutputStreamWriter osWriter = new OutputStreamWriter(exchange.getResponseBody());
        osWriter.write(respData);
        osWriter.flush();

        return exchange;
    }
    private String getAuthTokenFromHeaders(Headers headers)
    {
        String authTokenStr = new String();
        if (headers.containsKey("Authorization"))
        {
            Object obj = headers.getFirst("Authorization");
            authTokenStr = String.valueOf(obj);
        }
        return authTokenStr;
    }
}
