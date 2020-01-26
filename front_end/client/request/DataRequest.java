package client.request;

public class DataRequest
{
    private String authTokenStr;
    private String serverHost;
    private String serverPort;

    public DataRequest()
    {
        authTokenStr = new String();
    }
    public void setAuthTokenStr(String authTokenStr) { this.authTokenStr = authTokenStr; }
    public void setServerHost(String serverHost) { this.serverHost = serverHost; }
    public void setServerPort(String serverPort) { this.serverPort = serverPort; }
    public String getAuthTokenStr() { return authTokenStr; }
    public String getServerHost() { return serverHost; }
    public String getServerPort() { return serverPort; }
}