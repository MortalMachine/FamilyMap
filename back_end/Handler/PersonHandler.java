package Handler;

import com.sun.net.httpserver.*;
import com.google.gson.*;
import java.net.*;
import java.io.*;
import java.util.*;

import Request.PersonRequest;
import Response.PersonResponse;
import Service.PersonService;
/**
 * Created by jordanrj on 11/9/18.
 */

public class PersonHandler implements HttpHandler
{
    public PersonHandler() {}
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
                    exchange = handleHTTPError(exchange, "No authorization token provided: Person not retrieved from database.");
                    exchange.getResponseBody().close();
                    return;
                }

                PersonRequest personRequest = new PersonRequest();
                personRequest.setAuthTokenStr(authTokenStr);

                String urlPath = exchange.getRequestURI().getPath();
                String[] splitPath = urlPath.split("/");

                if (splitPath.length == 3)
                {
                    personRequest.setPersonID(splitPath[2]);
                }

                PersonService personService = new PersonService();
                PersonResponse personResponse = personService.runPersonService(personRequest);

                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);

                Gson gson = new Gson();
                String respData = gson.toJson(personResponse);

                OutputStreamWriter osWriter = new OutputStreamWriter(exchange.getResponseBody());
                osWriter.write(respData);
                osWriter.flush();

                exchange.getResponseBody().close();
                success = true;
            }

            if (!success)
            {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                exchange = handleHTTPError(exchange, "Bad http request: Person not retrieved from database.");
                exchange.getResponseBody().close();
            }
        }
        catch (IOException e)
        {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR, 0);
            exchange = handleHTTPError(exchange, "Http server error. Person not retrieved from database.");
            exchange.getResponseBody().close();
            e.printStackTrace();
        }
    }
    private HttpExchange handleHTTPError(HttpExchange exchange, String errorMessage) throws IOException
    {
        PersonResponse personResponse = new PersonResponse();
        personResponse.setErrorMessage(errorMessage);
        Gson gson = new Gson();
        String respData = gson.toJson(personResponse);
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
