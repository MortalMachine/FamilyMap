package Handler;

import java.io.*;
import com.google.gson.*;
import com.sun.net.httpserver.*;
import java.net.HttpURLConnection;

import Request.FillRequest;
import Response.FillResponse;
import Service.FillService;
/**
 * Created by jordanrj on 11/8/18.
 */

public class FillHandler implements HttpHandler
{
    public FillHandler() {}
    public void handle(HttpExchange exchange) throws IOException
    {
        boolean success = false;

        try
        {
            if (exchange.getRequestMethod().toLowerCase().equals("post"))
            {
                String urlPath = exchange.getRequestURI().getPath();
                String[] splitPath = urlPath.split("/");

                FillRequest fillRequest = new FillRequest();
                if (splitPath.length == 3)
                {
                    String userName = splitPath[2];
                    fillRequest.setUserName(userName);
                    fillRequest.setGenerations(4 /* Default generation count */);
                }
                else if (splitPath.length == 4)
                {
                    String userName = splitPath[2];
                    int generations = Integer.parseInt(splitPath[3]);
                    fillRequest.setUserName(userName);
                    fillRequest.setGenerations(generations);
                }

                FillService fillService = new FillService();
                FillResponse fillResponse = fillService.fill(fillRequest);

                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);

                Gson gson = new Gson();
                String respData = gson.toJson(fillResponse);
                OutputStreamWriter osWriter = new OutputStreamWriter(exchange.getResponseBody());
                osWriter.write(respData);
                osWriter.flush();

                exchange.getResponseBody().close();
                success = true;
            }

            if (!success)
            {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);

                exchange = handleHTTPError(exchange, "Bad Http Request: Data not filled for user.");

                exchange.getResponseBody().close();
            }
        }
        catch (IOException e)
        {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR, 0);

            exchange = handleHTTPError(exchange, "Http Server Error: Data not filled for user.");

            exchange.getResponseBody().close();
            e.printStackTrace();
        }
    }
    private HttpExchange handleHTTPError(HttpExchange exchange, String errorMessage) throws IOException
    {
        FillResponse fillResponse = new FillResponse();
        fillResponse.setMessage(errorMessage);
        Gson gson = new Gson();
        String respData = gson.toJson(fillResponse);
        OutputStreamWriter osWriter = new OutputStreamWriter(exchange.getResponseBody());
        osWriter.write(respData);
        osWriter.flush();

        return exchange;
    }
}
