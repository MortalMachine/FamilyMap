package Handler;

import com.sun.net.httpserver.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.nio.file.*;

public class FileHandler implements HttpHandler
{
	public FileHandler() {}

	public void handle(HttpExchange exchange) throws IOException
	{
		OutputStream respBody;
		try
		{
			if (exchange.getRequestMethod().toLowerCase().equals("get"))
			{
				String urlPath = exchange.getRequestURI().getPath();

				if (urlPath.length() == 0 || urlPath.equals("/"))
				{
					urlPath = "/index.html";
				}
				else if (!urlPath.equals("/index.html") && !urlPath.equals("/css/main.css") && !urlPath.equals("/favicon.ico") && !urlPath.equals("favicon.jpg"))
				{
					urlPath = "/HTML/404.html";
				}

				String filePathStr = "web" + urlPath;
				Path filePath = FileSystems.getDefault().getPath(filePathStr);
				File file = new File(filePathStr);

				boolean success = false;

				if (file.exists() && file.canRead())
				{
					exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
					respBody = exchange.getResponseBody();
					Files.copy(filePath, exchange.getResponseBody());
					respBody.close();
					success = true;
				}

				if (!success)
				{
					exchange.sendResponseHeaders(HttpURLConnection.HTTP_NOT_FOUND, 0);
					respBody = exchange.getResponseBody();
					respBody.close();
				}
			}
		}
		catch (IOException e)
		{
			exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR, 0);
			respBody = exchange.getResponseBody();
			respBody.close();
			e.printStackTrace();
		}
	}
}
