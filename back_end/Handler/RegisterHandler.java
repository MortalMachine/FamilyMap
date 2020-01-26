package Handler;

import com.google.gson.*;
import com.sun.net.httpserver.*;
import java.net.HttpURLConnection;
import java.io.*;

import Request.RegisterRequest;
import Response.RegisterResponse;
import Service.RegisterService;

/**
 * Created by jordanrj on 11/5/18.
 */

public class RegisterHandler implements HttpHandler
{
    public RegisterHandler() {}
    public void handle(HttpExchange exchange) throws IOException
    {
        boolean success = false;

        try
        {
            if (exchange.getRequestMethod().toLowerCase().equals("post"))
            {
                Reader reader = new InputStreamReader(exchange.getRequestBody());
                Gson gson = new Gson();
                RegisterRequest registerRequest = gson.fromJson(reader, RegisterRequest.class);

                RegisterService registerService = new RegisterService();
                RegisterResponse registerResponse = registerService.register(registerRequest);

                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);

                String respData = gson.toJson(registerResponse);
                OutputStreamWriter osWriter = new OutputStreamWriter(exchange.getResponseBody());
                osWriter.write(respData);
                osWriter.flush();

                exchange.getResponseBody().close();
                success = true;
            }

            if (!success)
            {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);

                exchange = handleHTTPError(exchange, "Bad Http request: User not registered in database.");

                exchange.getResponseBody().close();
            }
        }
        catch (IOException e)
        {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR, 0);

            exchange = handleHTTPError(exchange, "Http server error: User not registered in database.");

            exchange.getResponseBody().close();
            e.printStackTrace();
        }
    }
    private HttpExchange handleHTTPError(HttpExchange exchange, String errorMessage) throws IOException
    {
        RegisterResponse registerResponse = new RegisterResponse();
        registerResponse.setErrorMessage(errorMessage);
        Gson gson = new Gson();
        String respData = gson.toJson(registerResponse);
        OutputStreamWriter osWriter = new OutputStreamWriter(exchange.getResponseBody());
        osWriter.write(respData);
        osWriter.flush();

        return exchange;
    }

}
