package Handler;

import com.google.gson.*;
import com.sun.net.httpserver.*;
import java.net.HttpURLConnection;
import java.io.*;
import java.sql.SQLException;

import Request.LoadRequest;
import Response.LoadResponse;
import Service.LoadService;

/**
 * Created by jordanrj on 11/7/18.
 */

public class LoadHandler implements HttpHandler
{
    public LoadHandler() {}
    public void handle(HttpExchange exchange) throws IOException
    {
        boolean success = false;

        try
        {
            if (exchange.getRequestMethod().toLowerCase().equals("post"))
            {
                Reader reader = new InputStreamReader(exchange.getRequestBody());
                Gson gson = new Gson();
                LoadRequest loadRequest = gson.fromJson(reader, LoadRequest.class);

                LoadService loadService = new LoadService();
                LoadResponse loadResponse = loadService.load(loadRequest);

                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);

                String respData = gson.toJson(loadResponse);
                OutputStreamWriter osWriter = new OutputStreamWriter(exchange.getResponseBody());
                osWriter.write(respData);
                osWriter.flush();

                exchange.getResponseBody().close();
                success = true;
            }

            if (!success)
            {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                exchange = handleHTTPError(exchange, "Bad Http Request: Data not loaded into database.");
                exchange.getResponseBody().close();
            }
        }
        catch (IOException e)
        {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR, 0);
            exchange = handleHTTPError(exchange, "Http Server Error: Data not loaded into database.");
            exchange.getResponseBody().close();
            e.printStackTrace();
        }
    }
    private HttpExchange handleHTTPError(HttpExchange exchange, String errorMessage) throws IOException
    {
        LoadResponse loadResponse = new LoadResponse();
        loadResponse.setMessage(errorMessage);
        Gson gson = new Gson();
        String respData = gson.toJson(loadResponse);
        OutputStreamWriter osWriter = new OutputStreamWriter(exchange.getResponseBody());
        osWriter.write(respData);
        osWriter.flush();

        return exchange;
    }
}
