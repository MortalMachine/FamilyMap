package Handler;

import com.google.gson.*;
import com.sun.net.httpserver.*;
import java.net.HttpURLConnection;
import java.io.*;

import Request.LoginRequest;
import Response.LoginResponse;
import Service.LoginService;

/**
 * Created by jordanrj on 11/2/18.
 */

public class LoginHandler implements HttpHandler
{
    public LoginHandler() {}
    public void handle(HttpExchange exchange) throws IOException
    {
        boolean success = false;

        try
        {
            if (exchange.getRequestMethod().toLowerCase().equals("post"))
            {
                Reader reader = new InputStreamReader(exchange.getRequestBody());
                Gson gson = new Gson();
                LoginRequest loginRequest = gson.fromJson(reader, LoginRequest.class);

                LoginService loginService = new LoginService();
                LoginResponse loginResponse = loginService.login(loginRequest);

                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);

                String respData = gson.toJson(loginResponse);
                OutputStreamWriter osWriter = new OutputStreamWriter(exchange.getResponseBody());
                osWriter.write(respData);
                osWriter.flush();

                exchange.getResponseBody().close();
                success = true;
            }

            if (!success)
            {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);

                exchange = handleHTTPError(exchange, "Bad http request: User not logged in.");

                exchange.getResponseBody().close();
            }
        }
        catch (IOException e)
        {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR, 0);

            exchange = handleHTTPError(exchange, "Http server error: User not logged in.");

            exchange.getResponseBody().close();
            e.printStackTrace();
        }
    }
    private HttpExchange handleHTTPError(HttpExchange exchange, String errorMessage) throws IOException
    {
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setErrorMessage(errorMessage);
        Gson gson = new Gson();
        String respData = gson.toJson(loginResponse);
        OutputStreamWriter osWriter = new OutputStreamWriter(exchange.getResponseBody());
        osWriter.write(respData);
        osWriter.flush();

        return exchange;
    }
}
