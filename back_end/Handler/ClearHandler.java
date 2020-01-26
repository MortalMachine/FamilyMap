package Handler;

import com.sun.net.httpserver.*;
import java.net.HttpURLConnection;
import com.google.gson.*;
import java.io.*;

import Request.ClearRequest;
import Response.ClearResponse;
import Service.ClearService;

/**
 * Created by jordanrj on 11/7/18.
 */

public class ClearHandler implements HttpHandler
{
    public ClearHandler() {}
    public void handle(HttpExchange exchange) throws IOException
    {
        boolean success = false;

        try
        {
            if (exchange.getRequestMethod().toLowerCase().equals("post"))
            {
                Reader reader = new InputStreamReader(exchange.getRequestBody());
                Gson gson = new Gson();
                ClearRequest clearRequest = gson.fromJson(reader, ClearRequest.class);

                ClearService clearService = new ClearService();
                ClearResponse clearResponse = clearService.clear(clearRequest);

                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);

                String respData = gson.toJson(clearResponse);
                OutputStreamWriter osWriter = new OutputStreamWriter(exchange.getResponseBody());
                osWriter.write(respData);
                osWriter.flush();

                exchange.getResponseBody().close();
                success = true;
            }

            if (!success)
            {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);

                exchange = handleHTTPError(exchange, "Bad http request: Database not cleared.");

                exchange.getResponseBody().close();
            }
        }
        catch (IOException e)
        {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR, 0);

            exchange = handleHTTPError(exchange, "Http server error. Database not cleared.");

            exchange.getResponseBody().close();
            e.printStackTrace();
        }
    }
    private HttpExchange handleHTTPError(HttpExchange exchange, String errorMessage) throws IOException
    {
        ClearResponse clearResponse = new ClearResponse();
        clearResponse.setMessage(errorMessage);
        Gson gson = new Gson();
        String respData = gson.toJson(clearResponse);
        OutputStreamWriter osWriter = new OutputStreamWriter(exchange.getResponseBody());
        osWriter.write(respData);
        osWriter.flush();

        return exchange;
    }
}
